package dev.tomerdev.mercfrontcore.mixin;

import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.stat.BFStat;
import com.boehmod.blockfront.game.WinningTeamData;
import dev.tomerdev.mercfrontcore.data.WinnerSkinDropManager;
import java.util.Set;
import java.util.UUID;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.boehmod.blockfront.game.AbstractGame")
public abstract class AbstractGameWinnerSkinDropMixin {
    @Inject(method = "onPlayerMatchFinished", at = @At("TAIL"))
    private void mercfrontcore$maybeAwardWinnerSkinDrop(
        BFAbstractManager<?, ?, ?> manager,
        boolean awardAchievement,
        UUID uuid,
        Text subtitle,
        boolean victory,
        CallbackInfo ci
    ) {
        WinnerSkinDropManager.maybeAwardWinnerSkin(this, manager, uuid, victory);
    }

    @Inject(method = "finishMatch", at = @At("TAIL"))
    private void mercfrontcore$awardWinnerSkinDropsFromMatchFinish(
        net.minecraft.server.world.ServerWorld level,
        BFAbstractManager<?, ?, ?> manager,
        BFStat topStat,
        boolean awardAchievements,
        WinningTeamData winningTeam,
        Set<UUID> players,
        CallbackInfo ci
    ) {
        if (players == null || players.isEmpty()) {
            return;
        }

        for (UUID uuid : players) {
            boolean victory = false;
            if (winningTeam != null) {
                if (winningTeam.team != null && winningTeam.team.getPlayers().contains(uuid)) {
                    victory = true;
                } else if (winningTeam.topPlayers != null && winningTeam.topPlayers.contains(uuid)) {
                    victory = true;
                }
            }
            WinnerSkinDropManager.maybeAwardWinnerSkin(this, manager, uuid, victory);
        }
    }
}
