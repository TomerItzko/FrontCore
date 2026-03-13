package dev.tomerdev.mercfrontcore.mixin;

import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.match.Loadout;
import com.boehmod.blockfront.common.match.MatchClass;
import com.boehmod.blockfront.common.net.packet.BFCapturePointSpawnErrorPacket;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.util.PacketUtils;
import dev.tomerdev.mercfrontcore.MercFrontCore;
import dev.tomerdev.mercfrontcore.util.LoadoutXpCompat;
import java.util.UUID;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.boehmod.blockfront.game.AbstractGame")
public abstract class AbstractGameClassChangeGuardMixin {
    @Inject(
        method = "handleClassChangeRequest",
        at = @At("HEAD"),
        cancellable = true,
        require = 0,
        remap = false
    )
    private void mercfrontcore$guardClassChange(
        @NotNull BFAbstractManager<?, ?, ?> manager,
        @NotNull ServerWorld level,
        @NotNull ServerPlayerEntity player,
        @NotNull UUID uuid,
        @NotNull MatchClass matchClass,
        int classLevel,
        CallbackInfo ci
    ) {
        if (player.isCreative()) {
            return;
        }
        if (!((Object) this instanceof AbstractGame<?, ?, ?> game)) {
            return;
        }
        GameTeam team = game.getPlayerManager().getPlayerTeam(uuid);
        if (team == null) {
            return;
        }
        Loadout loadout = team.getDivisionData(game).getLoadout(matchClass, classLevel);
        int minimumXp = LoadoutXpCompat.resolveMinimumXp(game, team, matchClass, classLevel, loadout);
        PlayerCloudData profile = manager.getPlayerDataHandler().getCloudProfile((PlayerEntity) player);
        int classXp = LoadoutXpCompat.getEffectiveXp(profile, matchClass);
        MercFrontCore.LOGGER.info(
            "Direct XP check: player={}, class={}, level={}, minimumXp={}, classXp={}, globalXp={}, loadoutPresent={}",
            player.getNameForScoreboard(),
            matchClass,
            classLevel,
            minimumXp,
            classXp,
            profile.getExp(),
            loadout != null
        );
        if (minimumXp <= 0 || classXp >= minimumXp) {
            return;
        }

        MutableText message = LoadoutXpCompat.createMinimumXpMessage(minimumXp, classXp);
        PacketUtils.sendToPlayer(new BFCapturePointSpawnErrorPacket(message), player);
        ci.cancel();
    }
}
