package dev.tomerdev.mercfrontcore.client.widget;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public final class Widgets {
	private Widgets() {
	}
	
	public static ButtonWidget button(Text label, int x, int y, int width, int height, ButtonWidget.PressAction action) {
		return ButtonWidget.builder(label, action).dimensions(x, y, width, height).build();
	}
	
	public static ButtonWidget button(Text label, WidgetDim dim, ButtonWidget.PressAction action) {
		return button(label, dim.x(), dim.y(), dim.width(), dim.height(), action);
	}
	
	public static CheckboxWidget checkbox(TextRenderer textRenderer, int x, int y, int size, boolean selected) {
		return CheckboxWidget.builder(Text.empty(), textRenderer).pos(x, y).maxWidth(size).checked(selected).build();
	}
	
	public static CheckboxWidget checkbox(TextRenderer textRenderer, int x, int y, int size, boolean selected, CheckboxWidget.Callback callback) {
		return CheckboxWidget.builder(Text.empty(), textRenderer).pos(x, y).maxWidth(size).checked(selected).callback(callback).build();
	}
	
	public static CheckboxWidget checkbox(TextRenderer textRenderer, WidgetDim dim, boolean selected) {
		return checkbox(textRenderer, dim.x(), dim.y(), dim.width(), selected);
	}
	
	public static CheckboxWidget checkbox(TextRenderer textRenderer, WidgetDim dim, boolean selected, CheckboxWidget.Callback callback) {
		return checkbox(textRenderer, dim.x(), dim.y(), dim.width(), selected, callback);
	}
	
	public static TextFieldWidget textField(TextRenderer textRenderer, WidgetDim dim) {
		return new TextFieldWidget(textRenderer, dim.x(), dim.y(), dim.width(), dim.height(), Text.empty());
	}
}
