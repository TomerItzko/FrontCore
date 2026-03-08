package red.vuis.mercfrontcore.client.widget;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class EnumButtonWidget<E extends Enum<E>> extends ButtonWidget {
	protected final E[] enumValues;
	private int enumIndex = 0;

	public EnumButtonWidget(E[] values, WidgetDim dim, PressAction onPress) {
		super(dim.x(), dim.y(), dim.width(), dim.height(), Text.literal(values[0].toString()), onPress, DEFAULT_NARRATION_SUPPLIER);
		this.enumValues = values;
	}
	
	public EnumButtonWidget(E[] values, WidgetDim dim) {
		this(values, dim, button -> {});
	}
	
	@Override
	public void onPress() {
		enumIndex = (enumIndex + 1) % enumValues.length;
		updateMessage();
		super.onPress();
	}
	
	private void updateMessage() {
		setMessage(Text.literal(enumValues[enumIndex].toString()));
	}
	
	public E getEnum() {
		return enumValues[enumIndex];
	}
	
	public void setEnum(E value) {
		enumIndex = value.ordinal();
		updateMessage();
	}
}
