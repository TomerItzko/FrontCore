package dev.tomerdev.mercfrontcore.mixin.client;

import com.boehmod.blockfront.client.BFClient;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.net.ConnectionMode;
import com.boehmod.blockfront.client.event.BFClientScreenSubscriber;
import net.minecraft.client.gui.screen.TitleScreen;
import net.neoforged.neoforge.client.event.ScreenEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import dev.tomerdev.mercfrontcore.client.screen.ForcedTitleScreen;

@Mixin(BFClientScreenSubscriber.class)
public abstract class BFClientScreenSubscriberMixin {
    @Inject(
        method = "onOpenScreen",
        at = @At("HEAD"),
        cancellable = true,
        require = 0
    )
    private static void mercfrontcore$skipWhenForcedTitle(ScreenEvent.Opening event, CallbackInfo ci) {
        if (event.getScreen() instanceof ForcedTitleScreen
            || (mercfrontcore$isOfflineConnectionMode() && event.getScreen() instanceof TitleScreen)) {
            ci.cancel();
        }
    }

    private static boolean mercfrontcore$isOfflineConnectionMode() {
        try {
            BFClientManager manager = BFClient.getManager();
            return manager != null
                && manager.getConnectionManager() != null
                && manager.getConnectionManager().getConnectionMode() == ConnectionMode.OFFLINE;
        } catch (Throwable ignored) {
        }
        return false;
    }
}
