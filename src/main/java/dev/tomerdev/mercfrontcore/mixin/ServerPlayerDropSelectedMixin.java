package dev.tomerdev.mercfrontcore.mixin;

import dev.tomerdev.mercfrontcore.util.MatchCompat;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerDropSelectedMixin {
    @Inject(method = "dropSelectedItem(Z)Z", at = @At("HEAD"), cancellable = true, require = 0)
    private void mercfrontcore$blockDropSelectedInSession(boolean entireStack, CallbackInfoReturnable<Boolean> cir) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        if (!player.isAlive() || !MatchCompat.isInGameSession(player)) {
            return;
        }
        player.getInventory().markDirty();
        player.currentScreenHandler.sendContentUpdates();
        player.playerScreenHandler.sendContentUpdates();
        cir.setReturnValue(false);
    }
}
