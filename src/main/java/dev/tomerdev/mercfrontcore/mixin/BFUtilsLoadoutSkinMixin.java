package dev.tomerdev.mercfrontcore.mixin;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.match.Loadout;
import com.boehmod.blockfront.common.match.MatchClass;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.server.BFServerManager;
import com.boehmod.blockfront.server.player.ServerPlayerDataHandler;
import com.boehmod.blockfront.util.BFUtils;
import dev.tomerdev.mercfrontcore.MercFrontCore;
import dev.tomerdev.mercfrontcore.data.PlayerGunSkinStore;
import dev.tomerdev.mercfrontcore.util.LoadoutXpCompat;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.boehmod.blockfront.util.BFUtils")
public abstract class BFUtilsLoadoutSkinMixin {
    @Inject(
        method = "giveLoadout",
        at = @At("HEAD"),
        require = 0,
        remap = false
    )
    private static void mercfrontcore$logLoadoutGrant(
        ServerWorld world,
        ServerPlayerEntity player,
        Loadout loadout,
        boolean keepUnset,
        CallbackInfo ci
    ) {
        if (loadout == null) {
            return;
        }
        int minimumXp = loadout.getMinimumXp();
        if (minimumXp <= 0) {
            return;
        }
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        String caller = stack.length > 3 ? stack[3].toString() : "unknown";
        MercFrontCore.LOGGER.info(
            "Granting loadout to {} with minimumXp={} keepUnset={} caller={}",
            player.getNameForScoreboard(),
            minimumXp,
            keepUnset,
            caller
        );
    }

    @Inject(
        method = "method_2950",
        at = @At("HEAD"),
        cancellable = true,
        require = 0,
        remap = false
    )
    private static void mercfrontcore$blockLockedRespawnLoadout(
        ServerWorld world,
        ServerPlayerEntity player,
        AbstractGame<?, ?, ?> game,
        GameTeam team,
        CallbackInfo ci
    ) {
        BFAbstractManager<?, ?, ?> rawManager = BlockFront.getInstance() != null ? BlockFront.getInstance().getManager() : null;
        if (!(rawManager instanceof BFServerManager manager)) {
            return;
        }
        if (!(manager.getPlayerDataHandler() instanceof ServerPlayerDataHandler dataHandler)) {
            return;
        }
        int classOrdinal = game.getPlayerStatData(player.getUuid()).getInteger(BFStats.CLASS.getKey(), -1);
        int classLevel = game.getPlayerStatData(player.getUuid()).getInteger(BFStats.CLASS_INDEX.getKey(), -1);
        if (classOrdinal < 0 || classLevel < 0 || classOrdinal >= MatchClass.values().length) {
            return;
        }
        MatchClass matchClass = MatchClass.values()[classOrdinal];
        Loadout loadout = BFUtils.method_2898(game, team, matchClass, classLevel);
        int minimumXp = LoadoutXpCompat.resolveMinimumXp(game, team, matchClass, classLevel, loadout);
        if (minimumXp <= 0) {
            return;
        }
        PlayerCloudData profile = dataHandler.getCloudProfile(player);
        int effectiveXp = LoadoutXpCompat.getEffectiveXp(profile, matchClass);
        if (effectiveXp >= minimumXp) {
            return;
        }

        game.getPlayerStatData(player.getUuid()).setInteger(BFStats.CLASS.getKey(), -1);
        game.getPlayerStatData(player.getUuid()).setInteger(BFStats.CLASS_INDEX.getKey(), -1);
        game.getPlayerStatData(player.getUuid()).setInteger(BFStats.CLASS_ALIVE.getKey(), -1);
        BFUtils.sendNoticeMessage(player, Text.literal("Selected loadout is locked by minimum XP. Choose another loadout."));
        ci.cancel();
    }

    @Inject(
        method = "giveLoadout",
        at = @At("TAIL"),
        require = 0,
        remap = false
    )
    private static void mercfrontcore$reapplySelectedGunSkins(
        ServerWorld world,
        ServerPlayerEntity player,
        Loadout loadout,
        boolean keepUnset,
        CallbackInfo ci
    ) {
        PlayerGunSkinStore.getInstance().applyToPlayer(player);
    }
}
