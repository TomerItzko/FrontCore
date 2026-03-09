package dev.tomerdev.mercfrontcore.net.packet;

import com.boehmod.blockfront.common.match.Loadout;
import com.boehmod.blockfront.common.match.MatchClass;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.util.BFUtils;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import dev.tomerdev.mercfrontcore.AddonConstants;
import dev.tomerdev.mercfrontcore.client.data.AddonClientData;
import dev.tomerdev.mercfrontcore.data.AddonPacketCodecs;
import dev.tomerdev.mercfrontcore.data.LoadoutEditorStore;
import dev.tomerdev.mercfrontcore.setup.LoadoutIndex;

public record LoadoutsPacket(Map<LoadoutIndex.Identifier, List<Loadout>> loadouts) implements CustomPayload {
    public static final CustomPayload.Id<LoadoutsPacket> ID = new CustomPayload.Id<>(AddonConstants.id("loadouts"));
    public static final PacketCodec<RegistryByteBuf, LoadoutsPacket> PACKET_CODEC = PacketCodec.tuple(
        PacketCodecs.map(
            Object2ObjectOpenHashMap::new,
            AddonPacketCodecs.BF_COUNTRY,
            PacketCodecs.map(
                Object2ObjectOpenHashMap::new,
                PacketCodecs.STRING,
                PacketCodecs.map(
                    Object2ObjectOpenHashMap::new,
                    AddonPacketCodecs.MATCH_CLASS,
                    PacketCodecs.collection(ObjectArrayList::new, AddonPacketCodecs.LOADOUT)
                )
            )
        ),
        packet -> LoadoutIndex.flatToNested(packet.loadouts),
        nested -> new LoadoutsPacket(LoadoutIndex.nestedToFlat(nested))
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void handleClient(LoadoutsPacket packet, IPayloadContext context) {
        LoadoutIndex.apply(packet.loadouts);
        AddonClientData.getInstance().reloadLoadouts();
    }

    public static void handleServer(LoadoutsPacket packet, IPayloadContext context) {
        if (!(context.player() instanceof ServerPlayerEntity player)) {
            return;
        }
        if (!player.hasPermissionLevel(4)) {
            return;
        }
        if (anyGamesActive()) {
            player.sendMessage(Text.translatable(
                "mercfrontcore.message.packet.loadouts.game",
                Text.literal("/frontcore loadout sync").formatted(Formatting.GOLD)
            ).formatted(Formatting.RED));
            return;
        }

        LoadoutIndex.apply(packet.loadouts);
        refreshActivePlayersLoadouts(player);
        PacketDistributor.sendToAllPlayers(packet);
        boolean saved = LoadoutEditorStore.getInstance().save(player.getServer(), packet.loadouts);
        if (saved) {
            player.sendMessage(Text.translatable("mercfrontcore.message.packet.loadouts.success"));
            return;
        }

        player.sendMessage(Text.literal("Failed to persist loadouts to disk. Check server logs.").formatted(Formatting.RED));
    }

    private static boolean anyGamesActive() {
        try {
            Class<?> blockFrontClass = Class.forName("com.boehmod.blockfront.BlockFront");
            Object blockFront = blockFrontClass.getMethod("getInstance").invoke(null);
            if (blockFront == null) {
                return false;
            }

            Object manager = blockFrontClass.getMethod("getManager").invoke(blockFront);
            if (manager == null) {
                return false;
            }

            Object gamesObj = manager.getClass().getMethod("getGames").invoke(manager);
            if (!(gamesObj instanceof Map<?, ?> games)) {
                return false;
            }

            for (Object gameAsset : games.values()) {
                if (gameAsset == null) {
                    continue;
                }
                Object game = gameAsset.getClass().getMethod("getGame").invoke(gameAsset);
                if (game == null) {
                    continue;
                }

                Object status = game.getClass().getMethod("getStatus").invoke(game);
                if (status == null) {
                    continue;
                }
                if (!Objects.equals(String.valueOf(status), "IDLE")) {
                    return true;
                }
            }
        } catch (Throwable ignored) {
            return false;
        }
        return false;
    }

    private static void refreshActivePlayersLoadouts(ServerPlayerEntity source) {
        try {
            Class<?> blockFrontClass = Class.forName("com.boehmod.blockfront.BlockFront");
            Object blockFront = blockFrontClass.getMethod("getInstance").invoke(null);
            if (blockFront == null || source.getServer() == null) {
                return;
            }
            Object manager = blockFrontClass.getMethod("getManager").invoke(blockFront);
            if (manager == null) {
                return;
            }

            for (ServerPlayerEntity online : source.getServer().getPlayerManager().getPlayerList()) {
                if (online.isDisconnected() || online.isSpectator()) {
                    continue;
                }
                Object gameObj = manager.getClass().getMethod("getGameWithPlayer", java.util.UUID.class).invoke(manager, online.getUuid());
                if (!(gameObj instanceof AbstractGame<?, ?, ?> game)) {
                    continue;
                }
                Object playerManagerObj = game.getPlayerManager();
                if (playerManagerObj == null) {
                    continue;
                }

                Object teamObj = playerManagerObj.getClass().getMethod("getPlayerTeam", java.util.UUID.class).invoke(playerManagerObj, online.getUuid());
                if (!(teamObj instanceof GameTeam team)) {
                    continue;
                }

                var stats = game.getPlayerStatData(online.getUuid());
                int classOrdinal = stats.getInteger(BFStats.CLASS.getKey(), -1);
                int classLevel = stats.getInteger(BFStats.CLASS_INDEX.getKey(), 0);
                if (classOrdinal < 0 || classOrdinal >= MatchClass.values().length) {
                    continue;
                }

                MatchClass matchClass = MatchClass.values()[classOrdinal];
                Loadout loadout = team.getDivisionData(game).getLoadout(matchClass, classLevel);
                if (loadout == null) {
                    continue;
                }

                if (online.getWorld() instanceof ServerWorld serverWorld) {
                    BFUtils.giveLoadout(serverWorld, online, loadout);
                }
            }
        } catch (Throwable ignored) {
        }
    }
}
