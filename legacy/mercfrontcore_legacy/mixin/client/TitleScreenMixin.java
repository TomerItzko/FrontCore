package red.vuis.mercfrontcore.mixin.client;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.client.screen.title.LobbyTitleScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {
	private TitleScreenMixin(Text title) {
		super(title);
	}
	
	@Redirect(
		method = "initWidgetsNormal",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/text/Text;translatable(Ljava/lang/String;)Lnet/minecraft/text/MutableText;",
			ordinal = 2
		)
	)
	private MutableText replaceRealmsButtonText(String key) {
		return BlockFront.DISPLAY_NAME_COMPONENT.copyContentOnly();
	}
	
	@Inject(
		method = "lambda$createNormalMenuOptions$9",
		at = @At("HEAD"),
		cancellable = true
	)
	private void replaceRealmsScreen(ButtonWidget button, CallbackInfo ci) {
		assert client != null;
		client.setScreen(new LobbyTitleScreen());
		ci.cancel();
	}
}
