package dev.tomerdev.mercfrontcore.mixin;

import io.netty.channel.ChannelHandlerContext;
import java.util.Locale;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.boehmod.blockfront.server.net.PacketListenerPlayerAction")
public abstract class PacketListenerPlayerActionMixin {
    @Inject(
        method = "channelRead0",
        at = @At("HEAD"),
        cancellable = true,
        require = 0,
        remap = false
    )
    private void mercfrontcore$bridgeActionPacket(ChannelHandlerContext ctx, Object packet, CallbackInfo ci) {
        if (packet == null) {
            return;
        }
        Object action = mercfrontcore$invokeNoArg(packet, "getAction");
        String actionName = action == null ? "" : action.toString().toUpperCase(Locale.ROOT);
        boolean isDestroyAction = actionName.contains("START_DESTROY_BLOCK")
            || actionName.contains("ABORT_DESTROY_BLOCK")
            || actionName.contains("STOP_DESTROY_BLOCK");
        if (isDestroyAction) {
            ctx.fireChannelRead(packet);
            ci.cancel();
        }
    }

    private static Object mercfrontcore$invokeNoArg(Object target, String methodName) {
        try {
            return target.getClass().getMethod(methodName).invoke(target);
        } catch (Throwable ignored) {
            return null;
        }
    }
}
