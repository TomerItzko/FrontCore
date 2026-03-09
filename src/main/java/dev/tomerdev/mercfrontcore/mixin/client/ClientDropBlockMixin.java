package dev.tomerdev.mercfrontcore.mixin.client;

import dev.tomerdev.mercfrontcore.util.ClientMatchCompat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecraftClient.class)
public abstract class ClientDropBlockMixin {
    @Redirect(
        method = "handleInputEvents",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/network/ClientPlayerEntity;dropSelectedItem(Z)Z"
        ),
        require = 0
    )
    private boolean mercfrontcore$blockDropKeyInMatch(ClientPlayerEntity player, boolean entireStack) {
        if (ClientMatchCompat.isInGameSession(player)) {
            return false;
        }
        return player.dropSelectedItem(entireStack);
    }
}
