package dev.tomerdev.mercfrontcore.mixin;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import dev.tomerdev.mercfrontcore.server.net.BfNettyCompat;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
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

        ServerPlayerEntity player = BfNettyCompat.resolvePlayer(ctx.channel());
        if (player == null) {
            ctx.fireChannelRead(packet);
            ci.cancel();
            return;
        }

        Object action = mercfrontcore$invokeNoArg(packet, "getAction");
        String actionName = action == null ? "" : action.toString();
        if (mercfrontcore$isDestroyAction(actionName)) {
            ctx.fireChannelRead(packet);
            ci.cancel();
            return;
        }

        BFAbstractManager<?, ?, ?> manager = BlockFront.getInstance() == null ? null : BlockFront.getInstance().getManager();
        AbstractGame<?, ?, ?> game = manager == null ? null : manager.getGameWithPlayer(player.getUuid());
        if (game != null) {
            if (mercfrontcore$isDropAction(actionName)) {
                ItemStack selected = player.getInventory().getStack(player.getInventory().selectedSlot);
                if (!((AbstractGamePlayerManager) game.getPlayerManager()).canPlayerDropItem(player, selected)) {
                    player.getInventory().setStack(player.getInventory().selectedSlot, selected);
                    player.currentScreenHandler.sendContentUpdates();
                    player.playerScreenHandler.sendContentUpdates();
                    ci.cancel();
                    return;
                }
            }

            if ("SWAP_ITEM_WITH_OFFHAND".equals(actionName) && !game.canPlayersSwapItems()) {
                player.currentScreenHandler.sendContentUpdates();
                player.playerScreenHandler.sendContentUpdates();
                ci.cancel();
                return;
            }
        }

        ctx.fireChannelRead(packet);
        ci.cancel();
    }

    private static boolean mercfrontcore$isDestroyAction(String actionName) {
        return "START_DESTROY_BLOCK".equals(actionName)
            || "ABORT_DESTROY_BLOCK".equals(actionName)
            || "STOP_DESTROY_BLOCK".equals(actionName);
    }

    private static boolean mercfrontcore$isDropAction(String actionName) {
        return "DROP_ITEM".equals(actionName) || "DROP_ALL_ITEMS".equals(actionName);
    }

    private static Object mercfrontcore$invokeNoArg(Object target, String methodName) {
        try {
            return target.getClass().getMethod(methodName).invoke(target);
        } catch (Throwable ignored) {
            return null;
        }
    }
}
