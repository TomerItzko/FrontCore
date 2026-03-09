package dev.tomerdev.mercfrontcore.mixin.client;

import dev.tomerdev.mercfrontcore.util.ClientMatchCompat;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerDropSelectedMixin {
    @Inject(method = "dropSelectedItem(Z)Z", at = @At("HEAD"), cancellable = true, require = 0)
    private void mercfrontcore$blockDropSelectedInSession(boolean entireStack, CallbackInfoReturnable<Boolean> cir) {
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
        if (!ClientMatchCompat.isInGameSession(player)) {
            return;
        }
        cir.setReturnValue(false);
    }
}
