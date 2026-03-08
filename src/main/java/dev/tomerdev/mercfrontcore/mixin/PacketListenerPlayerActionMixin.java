package dev.tomerdev.mercfrontcore.mixin;

import com.boehmod.blockfront.util.NettyUtils;
import dev.tomerdev.mercfrontcore.util.AfkCompat;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.boehmod.blockfront.server.net.PacketListenerPlayerAction")
public abstract class PacketListenerPlayerActionMixin {
    @Inject(
        method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Ljava/lang/Object;)V",
        at = @At("HEAD"),
        cancellable = true,
        require = 0,
        remap = false
    )
    private void mercfrontcore$handleActionPacketBridge(
        ChannelHandlerContext ctx,
        Object packet,
        CallbackInfo ci
    ) {
        ServerPlayerEntity player = NettyUtils.getServerPlayerFromConnection(ctx.channel());
        if (player == null) {
            ctx.fireChannelRead(packet);
            ci.cancel();
            return;
        }
        AfkCompat.markPlayerMoved(player, "action_packet");
    }
}
