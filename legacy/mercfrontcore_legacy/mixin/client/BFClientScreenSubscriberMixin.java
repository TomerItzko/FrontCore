package red.vuis.mercfrontcore.mixin.client;

import com.boehmod.blockfront.client.event.BFClientScreenSubscriber;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.screen.TitleScreen;
import net.neoforged.neoforge.client.event.ScreenEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import red.vuis.mercfrontcore.client.screen.ForcedTitleScreen;

@Mixin(BFClientScreenSubscriber.class)
public abstract class BFClientScreenSubscriberMixin {
	@Definition(id = "getScreen", method = "Lnet/neoforged/neoforge/client/event/ScreenEvent$Opening;getScreen()Lnet/minecraft/client/gui/screen/Screen;")
	@Definition(id = "TitleScreen", type = TitleScreen.class)
	@Expression("?.getScreen() instanceof TitleScreen")
	@ModifyExpressionValue(
		method = "onOpenScreen",
		at = @At("MIXINEXTRAS:EXPRESSION")
	)
	private static boolean overrideIfForced(boolean original, @Local(argsOnly = true) ScreenEvent.Opening event) {
		return original && !(event.getScreen() instanceof ForcedTitleScreen);
	}
}
