package dev.tomerdev.mercfrontcore.mixin;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import java.util.NoSuchElementException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "com.boehmod.blockfront.server.BFServerManager")
public abstract class BFServerManagerMixin {
    @Redirect(
        method = "handlePlayerJoin",
        at = @At(
            value = "INVOKE",
            target = "Lio/netty/channel/ChannelPipeline;addBefore(Ljava/lang/String;Ljava/lang/String;Lio/netty/channel/ChannelHandler;)Lio/netty/channel/ChannelPipeline;"
        )
    )
    private ChannelPipeline mercfrontcore$addBeforeIdempotent(
        ChannelPipeline pipeline,
        String baseName,
        String name,
        ChannelHandler handler
    ) {
        try {
            if (pipeline.get(name) != null) {
                pipeline.remove(name);
            }
        } catch (NoSuchElementException ignored) {
        }
        return pipeline.addBefore(baseName, name, handler);
    }
}
