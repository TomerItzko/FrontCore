package red.vuis.mercfrontcore.client.widget;

import java.util.List;
import java.util.Optional;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import red.vuis.mercfrontcore.util.math.IntBounds;

import static red.vuis.mercfrontcore.util.math.IntBounds.ofMin;

public class ItemEditContainer implements CompoundWidget {
	private static final Text C_ITEM_ID_HINT = Text.translatable("mercfrontcore.widget.itemStack.itemId.hint");
	private static final IntBounds COUNT_BOUNDS = ofMin(1);
	
	protected final TextFieldWidget itemIdField;
	protected final IntegerFieldWidget countField;
	protected final ItemPreview preview;
	
	public ItemEditContainer(TextRenderer font, int x, int y, int width, int height) {
		itemIdField = new TextFieldWidget(font, x, y, width - 40, height, Text.empty());
		countField = new IntegerFieldWidget(font, x + width - 40, y, 20, height, Text.empty(), COUNT_BOUNDS);
		preview = new ItemPreview(x + width - 20, y, 20);
		
		itemIdField.setChangedListener(this::onItemIdChanged);
		itemIdField.setMaxLength(128);
		itemIdField.setPlaceholder(C_ITEM_ID_HINT);
		
		countField.setMaxLength(2);
		countField.setPlaceholder(Text.literal("#"));
	}
	
	public ItemEditContainer(TextRenderer font, WidgetDim dim) {
		this(font, dim.x(), dim.y(), dim.width(), dim.height());
	}
	
	@Override
	public Iterable<Object> getWidgets() {
		return List.of(itemIdField, countField, preview);
	}
	
	protected void onItemIdChanged(String strId) {
		preview.setItemStack(getPreviewItemStack(strId));
	}
	
	public @Nullable Item getItem() {
		return Optional.ofNullable(Identifier.tryParse(itemIdField.getText()))
			.map(Registries.ITEM::get)
			.orElse(null);
	}
	
	protected @Nullable ItemStack getPreviewItemStack(String strId) {
		Identifier rl = Identifier.tryParse(strId);
		if (rl == null) {
			preview.setItemStack(null);
			return null;
		}
		Optional<RegistryEntry.Reference<Item>> itemHolder = Registries.ITEM.getEntry(rl);
		if (itemHolder.isEmpty()) {
			preview.setItemStack(null);
			return null;
		}
		return new ItemStack(itemHolder.get().value());
	}
	
	public ItemStack getValue() {
		Item item = Registries.ITEM.get(Identifier.tryParse(itemIdField.getText()));
		return item == Items.AIR ? ItemStack.EMPTY : new ItemStack(item, countField.getOptionalInt().orElse(1));
	}
	
	public void setValue(ItemStack itemStack) {
		if (itemStack == null) {
			clear();
			return;
		}
		Item item = itemStack.getItem();
		if (item == Items.AIR) {
			clear();
			return;
		}
		itemIdField.setText(Registries.ITEM.getId(item).toString());
		itemIdField.setCursorToStart(false);
		countField.setInt(itemStack.getCount());
		countField.setCursorToStart(false);
	}
	
	public ItemEditContainer setActive(boolean active) {
		itemIdField.active = active;
		countField.active = active;
		return this;
	}
	
	public ItemEditContainer setVisible(boolean visible) {
		itemIdField.visible = visible;
		countField.visible = visible;
		preview.visible = visible;
		return this;
	}
	
	public ItemEditContainer clear() {
		itemIdField.setText("");
		countField.setText("");
		return this;
	}
}
