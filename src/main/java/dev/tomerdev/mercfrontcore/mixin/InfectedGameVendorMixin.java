package dev.tomerdev.mercfrontcore.mixin;

import com.boehmod.blockfront.common.entity.VendorEntity;
import com.boehmod.blockfront.game.impl.inf.InfectedGame;
import com.boehmod.blockfront.util.math.FDSPose;
import com.llamalad7.mixinextras.sugar.Local;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(InfectedGame.class)
public abstract class InfectedGameVendorMixin {
    private static final Map<UUID, Long> FORCED_VENDOR_CHUNKS = new ConcurrentHashMap<>();

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
        mercfrontcore$forceVendorChunk(world, entity, pose);
        if (entity instanceof MobEntity mob) {
            mob.setPersistent();
        }
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
        float pitch,
        @Local(argsOnly = true) FDSPose pose
    ) {
        mercfrontcore$forceVendorChunk(world, instance, pose);
        if (instance instanceof MobEntity mob) {
            mob.setPersistent();
        }
        instance.refreshPositionAndAngles(pose.position, pose.rotation.x, pose.rotation.y);
        return true;
    }

    private static void mercfrontcore$forceVendorChunk(ServerWorld world, Entity entity, FDSPose pose) {
        UUID vendorId = entity.getUuid();
        Long previous = FORCED_VENDOR_CHUNKS.remove(vendorId);
        if (previous != null) {
            world.setChunkForced((int) (previous >> 32), (int) (long) previous, false);
        }

        BlockPos pos = BlockPos.ofFloored(pose.position);
        int chunkX = pos.getX() >> 4;
        int chunkZ = pos.getZ() >> 4;
        world.setChunkForced(chunkX, chunkZ, true);
        FORCED_VENDOR_CHUNKS.put(vendorId, mercfrontcore$chunkKey(chunkX, chunkZ));
    }

    private static long mercfrontcore$chunkKey(int chunkX, int chunkZ) {
        return ((long) chunkX << 32) | (chunkZ & 0xFFFFFFFFL);
    }
}
