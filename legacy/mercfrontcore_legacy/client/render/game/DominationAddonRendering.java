package red.vuis.mercfrontcore.client.render.game;

import java.util.List;
import java.util.UUID;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractCapturePoint;
import com.boehmod.blockfront.game.GameStageTimer;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.impl.dom.DominationGame;
import com.boehmod.blockfront.game.impl.dom.DominationPlayerManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

import red.vuis.mercfrontcore.client.render.AddonRendering;
import red.vuis.mercfrontcore.client.util.AddonClientUtils;

public final class DominationAddonRendering {
	private DominationAddonRendering() {
	}
	
	@SuppressWarnings("deprecation")
	public static void oldMatchHud(
		@NotNull MinecraftClient client,
		@NotNull BFClientManager manager,
		@NotNull ClientPlayerDataHandler dataHandler,
		@NotNull DominationGame game,
		@NotNull GameStageTimer timer,
		@NotNull DrawContext context,
		@NotNull TextRenderer textRenderer,
		@NotNull MatrixStack matrices,
		int midX,
		float renderTime
	) {
		DominationPlayerManager playerManager = game.getPlayerManager();
		
		AddonRendering.oldTimer(matrices, textRenderer, context, midX, 7, timer);
		
		GameTeam axisTeam = playerManager.getTeamByName("Axis");
		GameTeam alliesTeam = playerManager.getTeamByName("Allies");
		if (axisTeam == null || alliesTeam == null) {
			return;
		}
		
		List<UUID> axisPlayers = axisTeam.getPlayers().stream().sorted().toList();
		List<UUID> alliesPlayers = alliesTeam.getPlayers().stream().sorted().toList();
		AddonRendering.oldPlayerHeadList(client, manager, context, dataHandler, midX, axisPlayers, alliesPlayers);
		
		int axisScore = axisTeam.getObjectInt(BFStats.SCORE);
		int alliesScore = alliesTeam.getObjectInt(BFStats.SCORE);
		Text axisScoreText = Text.literal(Integer.toString(axisScore)).withColor(AddonRendering.OLD_AXIS_COLOR);
		Text alliesScoreText = Text.literal(Integer.toString(alliesScore)).withColor(AddonRendering.OLD_ALLIES_COLOR);
		
		float axisRectStartX = midX - 99f;
		float axisScoreStartX = midX - 19f;
		
		float alliesRectStartX = midX + 21f;
		float alliesScoreStartX = midX + 0.5f;
		
		int axisScoreRectWidth = (int) (78f * (axisScore / 500f));
		int alliesScoreRectWidth = (int) (78f * (alliesScore / 500f));
		
		// Axis score
		BFRendering.rectangle(matrices, context, axisScoreStartX, 15f, 18.5f, 10f, BFRendering.translucentBlack());
		BFRendering.centeredComponent2d(matrices, textRenderer, context, axisScoreText, axisScoreStartX + 9.75f, 16.5f);
		
		// Axis rect
		BFRendering.rectangle(matrices, context, axisRectStartX - 1f, 15f, 80f, 10f, BFRendering.translucentBlack());
		BFRendering.rectangle(matrices, context, axisRectStartX, 16f, 78f, 8f, 0, 0.35f);
		BFRendering.rectangle(matrices, context, axisRectStartX, 16f, 78f, 8f, AddonRendering.OLD_AXIS_COLOR, 0.35f);
		BFRendering.rectangle(matrices, context, axisRectStartX, 16f, axisScoreRectWidth, 8f, AddonRendering.OLD_AXIS_COLOR, 1f);
		
		// Allies score
		BFRendering.rectangle(matrices, context, alliesScoreStartX, 15f, 18.5f, 10f, BFRendering.translucentBlack());
		BFRendering.centeredComponent2d(matrices, textRenderer, context, alliesScoreText, alliesScoreStartX + 9.75f, 16.5f);
		
		// Allies rect
		BFRendering.rectangle(matrices, context, alliesRectStartX - 1f, 15f, 80f, 10f, BFRendering.translucentBlack());
		BFRendering.rectangle(matrices, context, alliesRectStartX, 16f, 78f, 8f, 0, 0.35f);
		BFRendering.rectangle(matrices, context, alliesRectStartX, 16f, 78f, 8f, AddonRendering.OLD_ALLIES_COLOR, 0.35f);
		BFRendering.rectangle(matrices, context, alliesRectStartX + (78f - alliesScoreRectWidth), 16f, alliesScoreRectWidth, 8f, AddonRendering.OLD_ALLIES_COLOR, 1f);
		
		List<? extends AbstractCapturePoint<?>> capturePoints = game.getCapturePoints();
		
		int axisArrows = 0;
		int alliesArrows = 0;
		
		for (AbstractCapturePoint<?> capturePoint : capturePoints) {
			if (capturePoint.cbTeam == axisTeam) {
				axisArrows++;
			} else if (capturePoint.cbTeam == alliesTeam) {
				alliesArrows++;
			}
		}
		
		float arrowAlpha = Math.min(0.5f + MathHelper.sin(renderTime / 15f) / 2f, 0.5f);
		
		for (int i = 0; i < axisArrows; i++) {
			BFRendering.texture(matrices, context, AddonRendering.CPOINT_ARROW_RIGHT_BLACK, midX - 98f + 7f * i, 17f, 6f, 6f, arrowAlpha);
		}
		for (int i = 0; i < alliesArrows; i++) {
			BFRendering.texture(matrices, context, AddonRendering.CPOINT_ARROW_LEFT_BLACK, midX + 92f - 7f * i, 17f, 6f, 6f, arrowAlpha);
		}
		
		AddonRendering.oldCapturePointIcons(matrices, context, game, capturePoints, midX, 34, BFRendering.getRenderTime(), true);
		AddonRendering.oldCapturePointNames(matrices, context, textRenderer, capturePoints, midX, 44);
	}
	
	public static void dayOfDefeatMatchHud(
		@NotNull DominationGame game,
		@NotNull GameStageTimer timer,
		@NotNull DrawContext context,
		@NotNull TextRenderer textRenderer,
		@NotNull MatrixStack matrices
	) {
		DominationPlayerManager playerManager = game.getPlayerManager();
		
		int baseX = 2;
		int baseY = 2;
		int endY = baseY + 16;
		
		GameTeam axisTeam = playerManager.getTeamByName("Axis");
		GameTeam alliesTeam = playerManager.getTeamByName("Allies");
		if (axisTeam == null || alliesTeam == null) {
			return;
		}
		
		List<? extends AbstractCapturePoint<?>> capturePoints = game.getCapturePoints();
		int numCapturePoints = capturePoints.size();
		
		for (int i = 0; i < numCapturePoints; i++) {
			AbstractCapturePoint<?> capturePoint = capturePoints.get(i);
			
			int cpStartX = baseX + i * 17;
			int cpEndX = baseX + (i + 1) * 17 - 1;
			
			boolean hasCaptureProgress = capturePoint.captureTimer > 0;
			
			if (hasCaptureProgress) {
				float captureProgress = capturePoint.captureTimer / (float) AbstractCapturePoint.field_3233;
				int scissorX = cpStartX + (int) (captureProgress * 16f);
				context.enableScissor(cpStartX, baseY, scissorX, endY);
				
				Identifier cpTexture = AddonClientUtils.getTeamIcon(capturePoint.cpTeam);
				BFRendering.texture(matrices, context, cpTexture, cpStartX, baseY, 16, 16);
				
				context.disableScissor();
				
				context.enableScissor(scissorX, baseY, cpEndX, endY);
			}
			
			Identifier cbTexture = AddonClientUtils.getTeamIcon(capturePoint.cbTeam);
			BFRendering.texture(matrices, context, cbTexture, cpStartX, baseY, 16, 16);
			
			if (hasCaptureProgress) {
				context.disableScissor();
			}
		}
		
		int axisScore = axisTeam.getObjectInt(BFStats.SCORE);
		int alliesScore = alliesTeam.getObjectInt(BFStats.SCORE);
		Text axisScoreText = Text.literal(Integer.toString(axisScore)).withColor(AddonRendering.OLD_AXIS_COLOR);
		Text alliesScoreText = Text.literal(Integer.toString(alliesScore)).withColor(AddonRendering.OLD_ALLIES_COLOR);
		
		int rectY = baseY + 18;
		int textX = baseX + 2;
		int textY = rectY + 2;
		
		BFRendering.rectangle(context, baseX, rectY, 100, 31, BFRendering.translucentBlack());
		
		BFRendering.drawString(textRenderer, context, Text.translatable("mercfrontcore.match.hud.day_of_defeat.score.allies", alliesScoreText), textX, textY);
		textY += 10;
		BFRendering.drawString(textRenderer, context, Text.translatable("mercfrontcore.match.hud.day_of_defeat.score.axis", axisScoreText), textX, textY);
		textY += 10;
		BFRendering.drawString(textRenderer, context, Text.translatable("mercfrontcore.match.hud.day_of_defeat.time", BFRendering.formatTime(timer.getSecondsRemaining())), textX, textY);
	}
	
	public static void dayOfInfamyMatchHud(
		@NotNull DominationGame game,
		@NotNull GameStageTimer timer,
		@NotNull DrawContext context,
		@NotNull TextRenderer textRenderer,
		@NotNull MatrixStack matrices,
		int height,
		int midX
	) {
		DominationPlayerManager playerManager = game.getPlayerManager();
		
		int baseY = height - 9;
		int topY = baseY - 6;
		int textY = baseY - 3;
		
		AddonRendering.oldTimer(matrices, textRenderer, context, midX, baseY, timer);
		
		GameTeam axisTeam = playerManager.getTeamByName("Axis");
		GameTeam alliesTeam = playerManager.getTeamByName("Allies");
		if (axisTeam == null || alliesTeam == null) {
			return;
		}
		
		int axisScore = axisTeam.getObjectInt(BFStats.SCORE);
		int alliesScore = alliesTeam.getObjectInt(BFStats.SCORE);
		Text axisScoreText = Text.literal(Integer.toString(axisScore)).withColor(AddonRendering.OLD_AXIS_COLOR);
		Text alliesScoreText = Text.literal(Integer.toString(alliesScore)).withColor(AddonRendering.OLD_ALLIES_COLOR);
		
		BFRendering.rectangle(context, midX - 58, topY, 38, 13, BFRendering.translucentBlack());
		BFRendering.centeredComponent2d(matrices, textRenderer, context, axisScoreText, midX - 39, textY, 1f);
		BFRendering.rectangle(context, midX + 20, topY, 38, 13, BFRendering.translucentBlack());
		BFRendering.centeredComponent2d(matrices, textRenderer, context, alliesScoreText, midX + 39, textY, 1f);
		
		List<? extends AbstractCapturePoint<?>> capturePoints = game.getCapturePoints();
		
		AddonRendering.oldCapturePointIcons(matrices, context, game, capturePoints, midX, height - 24, BFRendering.getRenderTime(), true);
		AddonRendering.oldCapturePointNames(matrices, context, textRenderer, capturePoints, midX, height - 40);
	}
}
