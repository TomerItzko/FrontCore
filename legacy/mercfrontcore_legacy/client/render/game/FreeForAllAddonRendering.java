package red.vuis.mercfrontcore.client.render.game;

import com.boehmod.blockfront.game.GameStageTimer;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.NotNull;

import red.vuis.mercfrontcore.client.render.AddonRendering;

public final class FreeForAllAddonRendering {
	private FreeForAllAddonRendering() {
	}
	
	public static void oldMatchHud(
		@NotNull GameStageTimer timer,
		@NotNull DrawContext context,
		@NotNull TextRenderer textRenderer,
		@NotNull MatrixStack matrices,
		int midX
	) {
		AddonRendering.oldTimer(matrices, textRenderer, context, midX, 7, timer);
	}
}
