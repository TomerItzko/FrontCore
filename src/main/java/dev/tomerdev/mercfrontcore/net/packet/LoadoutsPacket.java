package dev.tomerdev.mercfrontcore.net.packet;

import com.boehmod.blockfront.common.match.Loadout;
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
        PacketDistributor.sendToAllPlayers(packet);
        LoadoutEditorStore.getInstance().saveCurrent(player.getServer());
        player.sendMessage(Text.translatable("mercfrontcore.message.packet.loadouts.success"));
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
}
