package red.vuis.mercfrontcore.mixin.client;

import java.util.Locale;

import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.game.AbstractCapturePoint;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.CapturePointGameClient;
import com.boehmod.blockfront.game.GameTeam;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import red.vuis.mercfrontcore.client.data.config.AddonClientConfig;

@Mixin(CapturePointGameClient.class)
public abstract class CapturePointGameClientMixin {
	@Shadow
//	@Nullable
	protected AbstractCapturePoint<?> field_2906;
	
	@ModifyConstant(
		method = "method_2748",
		constant = @Constant(intValue = 8159560)
	)
	private int changeAlliesColor(int constant) {
		return AddonClientConfig.getMatchHudStyle().isOldCapturingText() ? 7633224 : constant;
	}
	
	@SuppressWarnings("deprecation")
	@Inject(
		method = "method_2748",
		at = @At(
			value = "INVOKE",
			target = "Lcom/boehmod/blockfront/util/math/MathUtils;lerpf1(FFF)F",
			ordinal = 0,
			shift = At.Shift.BY,
			by = 2
		),
		cancellable = true
	)
	private void oldCapturePointText(
		@NotNull AbstractGamePlayerManager<?> playerManager,
		@NotNull ClientPlayerEntity player,
		@NotNull MatrixStack matrices,
		@NotNull DrawContext context,
		@NotNull TextRenderer textRenderer,
		int height,
		int midX,
		float renderTime,
		CallbackInfo ci,
		@Local(ordinal = 0) GameTeam playerTeam,
		@Local(ordinal = 4) int barColor,
		@Local(ordinal = 1) float captureProgress
	) {
		if (!AddonClientConfig.getMatchHudStyle().isOldCapturingText()) {
			return;
		}
		ci.cancel();
		
		GameTeam cbTeam = field_2906.cbTeam;
		if (playerTeam == cbTeam) {
			return;
		}
		
		int topY = height - 90;
		BFRendering.rectangleWithDarkShadow(matrices, context, midX - 60, topY, 120f, 12f, BFRendering.translucentBlack());
		BFRendering.rectangle(matrices, context, midX - 60 + 1, topY + 1, captureProgress * 120f / 12f - 2f, 10f, barColor, 0.8f);
		
		Style cbStyle = cbTeam != null ? cbTeam.getStyleText() : Style.EMPTY.withColor(Formatting.GRAY);
		MutableText pointName = Text.literal(field_2906.name.toUpperCase(Locale.ROOT)).setStyle(cbStyle);
		MutableText capturingText = Text.translatable("bf.message.gamemode.capturepoint.capturing", pointName)
			.formatted(Formatting.WHITE, Formatting.BOLD);
		BFRendering.centeredComponent2d(matrices, textRenderer, context, capturingText, midX, topY - 15);
	}
}
