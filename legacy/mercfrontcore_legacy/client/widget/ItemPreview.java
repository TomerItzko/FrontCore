package red.vuis.mercfrontcore.client.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemPreview implements Drawable {
	private final int x, y, size;
	
	private @Nullable ItemStack itemStack;
	public boolean visible = true;
	
	public ItemPreview(int x, int y, int size) {
		this.x = x;
		this.y = y;
		this.size = size;
	}
	
	public ItemPreview(WidgetDim dim) {
		this(dim.x(), dim.y(), dim.width());
	}
	
	public ItemPreview setItemStack(@Nullable ItemStack itemStack) {
		this.itemStack = itemStack;
		return this;
	}
	
	@Override
	public void render(@NotNull DrawContext context, int mouseX, int mouseY, float delta) {
		if (!visible || itemStack == null) {
			return;
		}
		
		RenderSystem.disableDepthTest();
		MatrixStack matrices = context.getMatrices();
		matrices.push();
		
		matrices.translate(x, y, 0);
		float scale = size / 16f;
		matrices.scale(scale, scale, scale);
		
		context.drawItemWithoutEntity(itemStack, 0, 0);
		
		matrices.pop();
		RenderSystem.enableDepthTest();
	}
}
