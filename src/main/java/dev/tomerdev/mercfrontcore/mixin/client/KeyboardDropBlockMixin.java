package dev.tomerdev.mercfrontcore.mixin.client;

import dev.tomerdev.mercfrontcore.util.ClientMatchCompat;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public abstract class KeyboardDropBlockMixin {
    @Inject(method = "onKey", at = @At("HEAD"), cancellable = true, require = 0)
    private void mercfrontcore$blockDropKeyInSession(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.options == null || client.player == null) {
            return;
        }
        if (window != client.getWindow().getHandle()) {
            return;
        }
        if (!ClientMatchCompat.isInGameSession(client.player)) {
            return;
        }
        KeyBinding dropKey = client.options.dropKey;
        if (action != 0 && dropKey.matchesKey(key, scancode)) {
            ci.cancel();
        }
    }
}
