package red.vuis.mercfrontcore.mixin.client;

import java.util.Objects;

import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGameClient;
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.util.StringUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import red.vuis.mercfrontcore.client.data.config.AddonClientConfig;
import red.vuis.mercfrontcore.util.AddonUtils;

@Mixin(AbstractGameClient.class)
public abstract class AbstractGameClientMixin {
	@Shadow
	@Final
	@NotNull
	protected AbstractGame<?, ?, ?> game;
	
	@Inject(
		method = "renderGameElements",
		at = @At("HEAD"),
		cancellable = true
	)
	private void disableGameElementRendering(DrawContext context, TextRenderer textRenderer, MatrixStack matrices, int width, float renderTime, CallbackInfo ci) {
		if (AddonClientConfig.getMatchHudStyle().getDisabledGameElementTypes().contains(AddonUtils.getGameType(game))) {
			ci.cancel();
		}
	}
	
	@Redirect(
		method = "method_2693",
		at = @At(
			value = "INVOKE",
			target = "Lcom/boehmod/blockfront/util/StringUtils;makeFancy(Ljava/lang/String;)Ljava/lang/String;",
			ordinal = 0
		)
	)
	private String disableWaitingFancyText(String str) {
		return AddonClientConfig.getMatchHudStyle().isOldWaitingText() ? str : StringUtils.makeFancy(str);
	}
	
	@Redirect(
		method = "method_2693",
		at = @At(
			value = "INVOKE",
			target = "Lcom/boehmod/blockfront/client/render/BFRendering;centeredComponent2dWithShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/text/Text;II)V",
			ordinal = 0
		)
	)
	@SuppressWarnings("deprecation")
	private void renderWaitingTextBackground(MatrixStack matrices, TextRenderer textRenderer, DrawContext context, Text text, int x, int y) {
		if (!AddonClientConfig.getMatchHudStyle().isOldWaitingText()) {
			BFRendering.centeredComponent2dWithShadow(matrices, textRenderer, context, text, x, y);
			return;
		}
		
		if (game.getStatus() == GameStatus.POST_GAME) {
			text = Text.translatable("bf.message.match.title.gameover");
		}
		x -= 1;
		
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		assert player != null;
		
		int midY = context.getScaledWindowHeight() / 2;
		int baseY = midY / 2 + 2;
		float bgWidth = textRenderer.getWidth(text) + 6;
		float bgX = x - bgWidth / 2f;
		float bgY = baseY - 7.5f;
		
		BFRendering.rectangleWithDarkShadow(matrices, context, bgX, bgY, bgWidth, 15f, BFRendering.translucentBlack());
		BFRendering.centeredComponent2d(matrices, textRenderer, context, text, x, baseY - 4f, 1f);
		
		int lineColor = 0xFFFFFF;
		GameTeam team = game.getPlayerManager().getPlayerTeam(MinecraftClient.getInstance().player.getUuid());
		if (team != null) {
			lineColor = Objects.requireNonNull(team.getStyleText().getColor()).getRgb();
		}
		BFRendering.rectangle(matrices, context, bgX, bgY + 14f, bgWidth, 1f, 0xFF000000 | lineColor);
	}
	
	@ModifyConstant(
		method = "addKillFeedEntry",
		constant = @Constant(
			intValue = 5
		)
	)
	private int customKillFeedEntryMax(int constant) {
		return AddonClientConfig.getKillFeedLines();
	}
}
