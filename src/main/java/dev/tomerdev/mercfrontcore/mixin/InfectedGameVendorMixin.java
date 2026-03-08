package dev.tomerdev.mercfrontcore.mixin;

import com.boehmod.blockfront.common.entity.VendorEntity;
import com.boehmod.blockfront.game.impl.inf.InfectedGame;
import com.boehmod.blockfront.util.math.FDSPose;
import com.llamalad7.mixinextras.sugar.Local;
import java.util.Set;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(InfectedGame.class)
public abstract class InfectedGameVendorMixin {
    @Redirect(
        method = "relocateVendor",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/world/ServerWorld;spawnEntity(Lnet/minecraft/entity/Entity;)Z",
            ordinal = 0
        ),
        require = 0
    )
    private boolean mercfrontcore$spawnVendorAtExactPose(ServerWorld world, Entity entity, @Local(argsOnly = true) FDSPose pose) {
        entity.refreshPositionAndAngles(pose.position, pose.rotation.x, pose.rotation.y);
        return world.spawnEntity(entity);
    }

    @Redirect(
        method = "relocateVendor",
        at = @At(
            value = "INVOKE",
            target = "Lcom/boehmod/blockfront/common/entity/VendorEntity;teleport(Lnet/minecraft/server/world/ServerWorld;DDDLjava/util/Set;FF)Z",
            ordinal = 0
        ),
        require = 0
    )
    private boolean mercfrontcore$disableVendorTeleport(
        VendorEntity instance,
        ServerWorld world,
        double destX,
        double destY,
        double destZ,
        Set<PositionFlag> flags,
        float yaw,
        float pitch
    ) {
        return false;
    }
}
