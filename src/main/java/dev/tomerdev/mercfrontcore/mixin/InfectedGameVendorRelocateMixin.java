package dev.tomerdev.mercfrontcore.mixin;

import com.boehmod.blockfront.game.impl.inf.InfectedGame;
import com.boehmod.blockfront.util.math.FDSPose;
import dev.tomerdev.mercfrontcore.server.event.MercFrontCoreServerEvents;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InfectedGame.class)
public abstract class InfectedGameVendorRelocateMixin {
    @Inject(method = "relocateVendor", at = @At("TAIL"))
    private void mercfrontcore$queueVendorResyncOnRelocate(ServerWorld world, FDSPose pose, CallbackInfo ci) {
        MercFrontCoreServerEvents.queueVendorTrackSyncForGame(world, (InfectedGame) (Object) this, 100);
    }
}
