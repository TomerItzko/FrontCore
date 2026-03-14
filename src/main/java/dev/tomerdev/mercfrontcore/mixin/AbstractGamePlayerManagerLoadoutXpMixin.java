package dev.tomerdev.mercfrontcore.mixin;

import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.match.Loadout;
import com.boehmod.blockfront.common.match.MatchClass;
import com.boehmod.blockfront.common.net.packet.BFCapturePointSpawnErrorPacket;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.util.PacketUtils;
import dev.tomerdev.mercfrontcore.util.ClassRankCompat;
import dev.tomerdev.mercfrontcore.util.LoadoutXpCompat;
import java.util.UUID;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "com.boehmod.blockfront.game.AbstractGamePlayerManager")
public abstract class AbstractGamePlayerManagerLoadoutXpMixin<G extends AbstractGame<G, ?, ?>> {
    @Shadow @Final protected G game;
    @Shadow @Final private PlayerDataHandler<?> playerData;

    @Inject(
        method = "changePlayerClass",
        at = @At("HEAD"),
        cancellable = true,
        require = 0,
        remap = false
    )
    private void mercfrontcore$blockLockedLoadoutSelection(
        @NotNull BFAbstractManager<?, ?, ?> manager,
        @NotNull ServerWorld level,
        @NotNull ServerPlayerEntity player,
        @NotNull UUID uuid,
        @NotNull MatchClass matchClass,
        int classLevel,
        CallbackInfoReturnable<Boolean> cir
    ) {
        if (player.isCreative()) {
            return;
        }
        GameTeam team = ((AbstractGamePlayerManagerAccessor) this).mercfrontcore$getPlayerTeam(uuid);
        if (team == null) {
            return;
        }
        PlayerCloudData profile = this.playerData.getCloudProfile((PlayerEntity) player);
        if (!ClassRankCompat.canUseClass(profile, matchClass)) {
            MutableText message = net.minecraft.text.Text.translatable(
                "bf.message.gamemode.class.error.rank",
                net.minecraft.text.Text.literal(ClassRankCompat.getRequiredRank(matchClass).getTitle()).formatted(net.minecraft.util.Formatting.GRAY)
            ).formatted(net.minecraft.util.Formatting.RED);
            PacketUtils.sendToPlayer(new BFCapturePointSpawnErrorPacket(message), player);
            cir.setReturnValue(false);
            return;
        }
        Loadout loadout = team.getDivisionData(this.game).getLoadout(matchClass, classLevel);
        int minimumXp = LoadoutXpCompat.resolveMinimumXp(this.game, team, matchClass, classLevel, loadout);
        if (loadout == null || minimumXp <= 0) {
            return;
        }
        int effectiveXp = LoadoutXpCompat.getEffectiveXp(profile, matchClass);
        if (effectiveXp >= minimumXp) {
            return;
        }
        MutableText message = LoadoutXpCompat.createMinimumXpMessage(minimumXp, effectiveXp);
        PacketUtils.sendToPlayer(new BFCapturePointSpawnErrorPacket(message), player);
        cir.setReturnValue(false);
    }

    @Inject(
        method = "checkClassChangeIsValid",
        at = @At("RETURN"),
        cancellable = true,
        require = 0,
        remap = false
    )
    private void mercfrontcore$enforceLoadoutMinimumXp(
        @NotNull BFAbstractManager<?, ?, ?> manager,
        @NotNull UUID uuid,
        @NotNull ServerPlayerEntity player,
        @NotNull GameTeam team,
        @NotNull MatchClass matchClass,
        @NotNull Loadout loadout,
        CallbackInfoReturnable<MutableText> cir
    ) {
        if (cir.getReturnValue() != null || player.isCreative()) {
            return;
        }
        int minimumXp = LoadoutXpCompat.resolveMinimumXp(this.game, team, matchClass, 0, loadout);
        if (minimumXp <= 0) {
            return;
        }
        PlayerCloudData profile = this.playerData.getCloudProfile((PlayerEntity) player);
        if (!ClassRankCompat.canUseClass(profile, matchClass)) {
            cir.setReturnValue(
                net.minecraft.text.Text.translatable(
                    "bf.message.gamemode.class.error.rank",
                    net.minecraft.text.Text.literal(ClassRankCompat.getRequiredRank(matchClass).getTitle()).formatted(net.minecraft.util.Formatting.GRAY)
                ).formatted(net.minecraft.util.Formatting.RED)
            );
            return;
        }
        int effectiveXp = LoadoutXpCompat.getEffectiveXp(profile, matchClass);
        if (effectiveXp >= minimumXp) {
            return;
        }
        cir.setReturnValue(LoadoutXpCompat.createMinimumXpMessage(minimumXp, effectiveXp));
    }
}
