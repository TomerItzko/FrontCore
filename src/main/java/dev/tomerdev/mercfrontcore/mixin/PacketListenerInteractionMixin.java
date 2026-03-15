package dev.tomerdev.mercfrontcore.mixin;

import dev.tomerdev.mercfrontcore.util.AfkCompat;
import dev.tomerdev.mercfrontcore.server.net.BfNettyCompat;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.boehmod.blockfront.server.net.PacketListenerInteraction")
public abstract class PacketListenerInteractionMixin {
    @Inject(
        method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Ljava/lang/Object;)V",
        at = @At("HEAD"),
        cancellable = true,
        require = 0,
        remap = false
    )
    private void mercfrontcore$handleInteractionPacketBridge(
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

        if (!player.isAlive()) {
            ci.cancel();
            return;
        }

        Entity target = mercfrontcore$getTarget(packet, player);
        if (target == null || !target.isAlive() || target.isRemoved()) {
            ci.cancel();
            return;
        }

        if (target != null && target.getId() == player.getId()) {
            ci.cancel();
            return;
        }

        AfkCompat.markPlayerMoved(player, "interaction_packet");
    }

    private static Entity mercfrontcore$getTarget(Object packet, ServerPlayerEntity player) {
        if (packet == null || player == null) {
            return null;
        }
        try {
            Object target = packet.getClass()
                .getMethod("getTarget", player.getServerWorld().getClass())
                .invoke(packet, player.getServerWorld());
            return target instanceof Entity entity ? entity : null;
        } catch (NoSuchMethodException ignored) {
            try {
                Object target = packet.getClass()
                    .getMethod("getEntity", player.getServerWorld().getClass())
                    .invoke(packet, player.getServerWorld());
                return target instanceof Entity entity ? entity : null;
            } catch (Throwable ignoredToo) {
                return null;
            }
        } catch (Throwable ignored) {
            return null;
        }
    }
}
