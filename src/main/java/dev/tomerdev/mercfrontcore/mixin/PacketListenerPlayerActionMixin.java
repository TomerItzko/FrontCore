package dev.tomerdev.mercfrontcore.mixin;

import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.boehmod.blockfront.server.net.PacketListenerPlayerAction")
public abstract class PacketListenerPlayerActionMixin {
    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    private void mercfrontcore$allowVanillaFallbackWhenPlayerLookupFails(
        ChannelHandlerContext ctx,
        PlayerActionC2SPacket packet,
        CallbackInfo ci
    ) {
        ctx.fireChannelRead(packet);
        ci.cancel();
    }
}
