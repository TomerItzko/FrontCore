package dev.tomerdev.mercfrontcore.mixin.client;

import com.boehmod.blockfront.client.screen.intro.ModeIntroScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModeIntroScreen.class)
public abstract class ModeIntroScreenMixin {
    @Inject(method = "init", at = @At("HEAD"), require = 0)
    private void mercfrontcore$forceTitleOnInit(CallbackInfo ci) {
        // Intentionally no-op to avoid startup screen recursion and mode selection disruption.
    }
}
