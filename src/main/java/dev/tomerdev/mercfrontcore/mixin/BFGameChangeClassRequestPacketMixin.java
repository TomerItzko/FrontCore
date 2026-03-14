package dev.tomerdev.mercfrontcore.mixin;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.match.Loadout;
import com.boehmod.blockfront.common.match.MatchClass;
import com.boehmod.blockfront.common.net.packet.BFCapturePointSpawnErrorPacket;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.server.BFServerManager;
import com.boehmod.blockfront.server.player.ServerPlayerDataHandler;
import com.boehmod.blockfront.util.PacketUtils;
import dev.tomerdev.mercfrontcore.MercFrontCore;
import dev.tomerdev.mercfrontcore.util.ClassRankCompat;
import dev.tomerdev.mercfrontcore.util.LoadoutXpCompat;
import java.util.UUID;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.boehmod.blockfront.common.net.packet.BFGameChangeClassRequestPacket")
public abstract class BFGameChangeClassRequestPacketMixin {
    @Inject(
        method = "method_4339",
        at = @At("HEAD"),
        cancellable = true,
        require = 0,
        remap = false
    )
    private static void mercfrontcore$blockLockedLoadoutPacket(
        Object packet,
        IPayloadContext context,
        CallbackInfo ci
    ) {
        if (!(context.player() instanceof ServerPlayerEntity player) || player.isCreative()) {
            return;
        }
        BFAbstractManager<?, ?, ?> rawManager = BlockFront.getInstance() != null ? BlockFront.getInstance().getManager() : null;
        if (!(rawManager instanceof BFServerManager manager)) {
            return;
        }
        if (!(manager.getPlayerDataHandler() instanceof ServerPlayerDataHandler dataHandler)) {
            return;
        }
        AbstractGame<?, ?, ?> game = manager.getGameWithPlayer(player.getUuid());
        if (game == null) {
            return;
        }

        MatchClass matchClass = mercfrontcore$getMatchClass(packet);
        int classLevel = mercfrontcore$getClassIndex(packet);
        if (matchClass == null || classLevel < 0) {
            return;
        }

        GameTeam team = game.getPlayerManager().getPlayerTeam(player.getUuid());
        if (team == null) {
            return;
        }
        PlayerCloudData profile = dataHandler.getCloudProfile((PlayerEntity) player);
        if (!ClassRankCompat.canUseClass(profile, matchClass)) {
            MutableText message = net.minecraft.text.Text.translatable(
                "bf.message.gamemode.class.error.rank",
                net.minecraft.text.Text.literal(ClassRankCompat.getRequiredRank(matchClass).getTitle()).formatted(net.minecraft.util.Formatting.GRAY)
            ).formatted(net.minecraft.util.Formatting.RED);
            PacketUtils.sendToPlayer(new BFCapturePointSpawnErrorPacket(message), player);
            ci.cancel();
            return;
        }
        Loadout loadout = team.getDivisionData(game).getLoadout(matchClass, classLevel);
        int minimumXp = LoadoutXpCompat.resolveMinimumXp(game, team, matchClass, classLevel, loadout);
        MercFrontCore.LOGGER.info(
            "Loadout XP check: player={}, class={}, level={}, minimumXp={}, classXp={}, globalXp={}, loadoutPresent={}",
            player.getNameForScoreboard(),
            matchClass,
            classLevel,
            minimumXp,
            profile.getExpForClass(matchClass.ordinal()),
            profile.getExp(),
            loadout != null
        );
        if (minimumXp <= 0) {
            return;
        }

        int effectiveXp = LoadoutXpCompat.getEffectiveXp(profile, matchClass);
        if (effectiveXp >= minimumXp) {
            return;
        }

        MutableText message = LoadoutXpCompat.createMinimumXpMessage(minimumXp, effectiveXp);
        PacketUtils.sendToPlayer(new BFCapturePointSpawnErrorPacket(message), player);
        ci.cancel();
    }

    private static MatchClass mercfrontcore$getMatchClass(Object packet) {
        try {
            Object value = packet.getClass().getMethod("classType").invoke(packet);
            return value instanceof MatchClass matchClass ? matchClass : null;
        } catch (Throwable ignored) {
            return null;
        }
    }

    private static int mercfrontcore$getClassIndex(Object packet) {
        try {
            Object value = packet.getClass().getMethod("classIndex").invoke(packet);
            return value instanceof Integer integer ? integer : -1;
        } catch (Throwable ignored) {
            return -1;
        }
    }
}
