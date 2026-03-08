package red.vuis.mercfrontcore.mixin.client;

import com.boehmod.blockfront.client.net.ConnectionMode;
import com.boehmod.blockfront.client.screen.intro.BFIntroScreen;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import red.vuis.mercfrontcore.client.screen.ForcedTitleScreen;

@Mixin(BFIntroScreen.class)
public class BFIntroScreenMixin {
	@Definition(id = "getConnectionMode", method = "Lcom/boehmod/blockfront/cloud/client/ClientConnectionManager;getConnectionMode()Lcom/boehmod/blockfront/client/net/ConnectionMode;")
	@Expression("? = ?.getConnectionMode()")
	@Inject(
		method = "method_655",
		at = @At(
			value = "MIXINEXTRAS:EXPRESSION",
			shift = At.Shift.AFTER
		),
		cancellable = true
	)
	private static void makeForcedTitleScreenIfOffline(CallbackInfoReturnable<Screen> cir, @Local ConnectionMode mode) {
		if (mode == ConnectionMode.OFFLINE) {
			cir.setReturnValue(new ForcedTitleScreen());
		}
	}
}
