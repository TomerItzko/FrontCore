package dev.tomerdev.mercfrontcore.mixin.client;

import com.boehmod.blockfront.client.BFClient;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.net.ConnectionMode;
import com.boehmod.blockfront.client.screen.intro.BFIntroScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BFIntroScreen.class)
public abstract class BFIntroRouteMixin {
    @Inject(method = "method_655", at = @At("HEAD"), cancellable = true, require = 0)
    private static void mercfrontcore$forceRoute(CallbackInfoReturnable<Screen> cir) {
        if (mercfrontcore$isOfflineConnectionMode()) {
            cir.setReturnValue(new TitleScreen());
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
