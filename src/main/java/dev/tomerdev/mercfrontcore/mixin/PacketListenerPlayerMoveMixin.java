package dev.tomerdev.mercfrontcore.mixin;

import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.boehmod.blockfront.server.net.PacketListenerPlayerMove")
public abstract class PacketListenerPlayerMoveMixin {
    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    private void mercfrontcore$allowVanillaFallbackWhenPlayerLookupFails(
        ChannelHandlerContext ctx,
        PlayerMoveC2SPacket packet,
        CallbackInfo ci
    ) {
        ctx.fireChannelRead(packet);
        ci.cancel();
    }
}
