package dev.tomerdev.mercfrontcore.mixin;

import dev.tomerdev.mercfrontcore.server.net.BfNettyCompat;
import dev.tomerdev.mercfrontcore.util.AfkCompat;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.boehmod.blockfront.server.net.PacketListenerPlayerMove")
public abstract class PacketListenerPlayerMoveMixin {
    @Inject(
        method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Ljava/lang/Object;)V",
        at = @At("HEAD"),
        cancellable = true,
        require = 0,
        remap = false
    )
    private void mercfrontcore$handleMovePacketBridge(
        ChannelHandlerContext ctx,
        Object packet,
        CallbackInfo ci
    ) {
        ServerPlayerEntity player = BfNettyCompat.resolvePlayer(ctx.channel());
        if (player == null) {
            ctx.fireChannelRead(packet);
            ci.cancel();
            return;
        }
        AfkCompat.markPlayerMoved(player, "move_packet");
    }
}
