package red.vuis.mercfrontcore.mixin.client;

import com.boehmod.blockfront.client.gui.widget.BFButton;
import com.boehmod.blockfront.client.screen.SidebarScreen;
import com.boehmod.blockfront.client.screen.title.sidebar.TitleSidebarScreen;
import com.boehmod.blockfront.util.BFStyles;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.neoforged.neoforge.client.gui.ModListScreen;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleSidebarScreen.class)
public abstract class TitleSidebarScreenMixin extends SidebarScreen {
	public TitleSidebarScreenMixin(@Nullable Screen screen, @NotNull Text component) {
		super(screen, component);
	}
	
	@Inject(
		method = "method_758",
		at = @At("TAIL")
	)
	private void addModsButton(CallbackInfo ci, @Local(ordinal = 1) int offsetX, @Local(ordinal = 3) int offsetY, @Local(ordinal = 2) int width) {
		addDrawableChild(
			new BFButton(
				offsetX + 4, offsetY + 40, width, 18,
				Text.translatable("fml.menu.mods").fillStyle(BFStyles.BOLD),
				button -> client.setScreen(new ModListScreen((TitleSidebarScreen) (Object) this))
			)
				.tip(Text.translatable("mercfrontcore.menu.button.mods.tip"))
				.alignment(BFButton.Alignment.LEFT)
		);
	}
}
