package red.vuis.mercfrontcore.mixin.client;

import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.common.match.kill.KillSectionPlayer;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import red.vuis.mercfrontcore.client.data.config.AddonClientConfig;

@Mixin(KillSectionPlayer.class)
public abstract class KillSectionPlayerMixin {
	@SuppressWarnings("deprecation")
	@Redirect(
		method = "method_3220",
		at = @At(
			value = "INVOKE",
			target = "Lcom/boehmod/blockfront/client/render/BFRendering;centeredComponent2dWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/text/Text;II)V"
		)
	)
	private void replaceDrawCall(MatrixStack matrices, TextRenderer textRenderer, DrawContext context, Text text, int x, int y) {
		if (AddonClientConfig.getMatchHudStyle().isOldKillFeed()) {
			BFRendering.centeredComponent2d(matrices, textRenderer, context, text, x, y);
		} else {
			BFRendering.centeredComponent2dWithShadow(matrices, textRenderer, context, text, x, y);
		}
	}
}
