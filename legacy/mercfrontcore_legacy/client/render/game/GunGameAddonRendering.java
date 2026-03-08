package red.vuis.mercfrontcore.client.render.game;

import java.util.List;
import java.util.UUID;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.game.GameStageTimer;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.impl.gg.GunGame;
import com.boehmod.blockfront.game.impl.gg.GunGamePlayerManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.NotNull;

import red.vuis.mercfrontcore.client.render.AddonRendering;

public final class GunGameAddonRendering {
	private GunGameAddonRendering() {
	}
	
	public static void oldMatchHud(
		@NotNull MinecraftClient client,
		@NotNull BFClientManager manager,
		@NotNull ClientPlayerDataHandler dataHandler,
		@NotNull GunGame game,
		@NotNull GameStageTimer timer,
		@NotNull DrawContext context,
		@NotNull TextRenderer textRenderer,
		@NotNull MatrixStack matrices,
		int midX
	) {
		GunGamePlayerManager playerManager = game.getPlayerManager();
		
		AddonRendering.oldTimer(matrices, textRenderer, context, midX, 7, timer);
		
		GameTeam axisTeam = playerManager.getTeamByName("Axis");
		GameTeam alliesTeam = playerManager.getTeamByName("Allies");
		if (axisTeam == null || alliesTeam == null) {
			return;
		}
		
		List<UUID> axisPlayers = axisTeam.getPlayers().stream().sorted().toList();
		List<UUID> alliesPlayers = alliesTeam.getPlayers().stream().sorted().toList();
		AddonRendering.oldPlayerHeadList(client, manager, context, dataHandler, midX, axisPlayers, alliesPlayers);
	}
}
