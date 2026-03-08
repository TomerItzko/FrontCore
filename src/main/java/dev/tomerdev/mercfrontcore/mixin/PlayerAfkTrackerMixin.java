package dev.tomerdev.mercfrontcore.mixin;

import com.boehmod.blockfront.server.player.PlayerAfkTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerAfkTracker.class)
public abstract class PlayerAfkTrackerMixin {
    @Inject(method = "update", at = @At("HEAD"), cancellable = true)
    private void mercfrontcore$disableAfkUpdate(CallbackInfo ci) {
        ci.cancel();
    }
}
