package red.vuis.mercfrontcore.mixin.client;

import com.boehmod.blockfront.client.gui.layer.BFAbstractGuiLayer;
import com.boehmod.blockfront.client.gui.layer.MatchGuiLayer;
import com.boehmod.blockfront.client.render.BFRendering;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.DrawContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

import red.vuis.mercfrontcore.client.data.config.AddonClientConfig;

@Mixin(MatchGuiLayer.class)
public abstract class MatchGuiLayerMixin extends BFAbstractGuiLayer {
	@ModifyArg(
		method = "method_499",
		at = @At(
			value = "INVOKE",
			target = "Lcom/boehmod/blockfront/client/render/minimap/MinimapRendering;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/client/network/ClientPlayerEntity;Ljava/util/Collection;IIFII)V"
		),
		index = 5
	)
	private int minimapXPosition(int x, @Local(argsOnly = true) DrawContext context) {
		return switch (AddonClientConfig.getMatchHudStyle()) {
			case DAY_OF_DEFEAT -> context.getScaledWindowWidth() - 106;
			default -> x;
		};
	}
	
	@ModifyConstant(
		method = "renderKillFeed",
		constant = @Constant(
			floatValue = 6.0F
		)
	)
	private float killFeedXTranslation(float constant) {
		return AddonClientConfig.getMatchHudStyle().isRightKillFeed() ? -6f : constant;
	}
	
	@ModifyConstant(
		method = "renderKillFeed",
		constant = @Constant(
			intValue = 16
		)
	)
	private int killFeedSpacing(int constant) {
		return AddonClientConfig.getMatchHudStyle().isOldKillFeed() ? 12 : constant;
	}
	
	@Redirect(
		method = "render",
		at = @At(
			value = "INVOKE",
			target = "Lcom/boehmod/blockfront/client/render/BFRendering;rectangle(Lnet/minecraft/client/gui/DrawContext;IIIIIF)V"
		)
	)
	private void toggleDeathFadeRects(DrawContext graphics, int x, int y, int width, int height, int color, float alpha) {
		if (AddonClientConfig.getEnableDeathFade()) {
			BFRendering.rectangle(graphics, x, y, width, height, color, alpha);
		}
	}
}
