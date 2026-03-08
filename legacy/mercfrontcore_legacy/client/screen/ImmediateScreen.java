package red.vuis.mercfrontcore.client.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.NotNull;

public abstract class ImmediateScreen extends Screen {
	private DrawContext context;
	
	public ImmediateScreen(Text title) {
		super(title);
	}
	
	@Override
	@MustBeInvokedByOverriders
	public void render(@NotNull DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		this.context = context;
	}
	
	protected void drawText(Text text, int x, int y, float scale, boolean centeredX, boolean centeredY) {
		MatrixStack matrices = context.getMatrices();
		matrices.push();
		int dx = centeredX ? x - textRenderer.getWidth(text) / 2 : x;
		int dy = centeredY ? y - textRenderer.fontHeight / 2 : y;
		matrices.translate(dx, dy, 0);
		if (scale != 1f) {
			matrices.scale(scale, scale, 1);
		}
		context.drawText(textRenderer, text, 0, 0, 0xFFFFFFFF, true);
		matrices.pop();
	}
	
	protected void drawText(Text text, int x, int y, boolean centeredX, boolean centeredY) {
		drawText(text, x, y, 1f, centeredX, centeredY);
	}
	
	protected void drawText(Text text, int x, int y, boolean centered) {
		drawText(text, x, y, centered, centered);
	}
}
