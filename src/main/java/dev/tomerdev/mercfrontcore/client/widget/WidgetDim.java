package dev.tomerdev.mercfrontcore.client.widget;

import net.minecraft.client.gui.widget.ClickableWidget;

public class WidgetDim {
	private final int x, y, width, height;
	
	private WidgetDim(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public static WidgetDim dim(int x, int y, int width, int height) {
		return new WidgetDim(x, y, width, height);
	}
	
	public static WidgetDim centeredDim(int x, int y, int width, int height) {
		return new WidgetDim(x - width / 2, y - height / 2, width, height);
	}
	
	public static WidgetDim sqrDim(int x, int y, int size) {
		return dim(x, y, size, size);
	}
	
	public static WidgetDim sqrCenteredDim(int x, int y, int size) {
		return centeredDim(x, y, size, size);
	}
	
	public int x() {
		return x;
	}
	
	public int y() {
		return y;
	}
	
	public int width() {
		return width;
	}
	
	public int height() {
		return height;
	}
	
	public void apply(ClickableWidget widget) {
		widget.setPosition(x, y);
		widget.setWidth(width);
		widget.setHeight(height);
	}
}
