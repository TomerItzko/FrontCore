package red.vuis.mercfrontcore.client.render.game;

import java.util.List;
import java.util.UUID;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.GameStageTimer;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.impl.tdm.TeamDeathmatchGame;
import com.boehmod.blockfront.game.impl.tdm.TeamDeathmatchPlayerManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import red.vuis.mercfrontcore.client.render.AddonRendering;

public final class TeamDeathmatchAddonRendering {
	private TeamDeathmatchAddonRendering() {
	}
	
	public static void oldMatchHud(
		@NotNull MinecraftClient client,
		@NotNull BFClientManager manager,
		@NotNull ClientPlayerDataHandler dataHandler,
		@NotNull TeamDeathmatchGame game,
		@NotNull GameStageTimer timer,
		@NotNull DrawContext context,
		@NotNull TextRenderer textRenderer,
		@NotNull MatrixStack matrices,
		int midX
	) {
		TeamDeathmatchPlayerManager playerManager = game.getPlayerManager();
		
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
		
		BFRendering.rectangle(context, midX - 19, 15, 18, 11, BFRendering.translucentBlack());
		BFRendering.centeredString(textRenderer, context, axisScoreText, midX - 9, 17);
		BFRendering.rectangle(context, midX + 1, 15, 18, 11, BFRendering.translucentBlack());
		BFRendering.centeredString(textRenderer, context, alliesScoreText, midX + 10, 17);
	}
}
