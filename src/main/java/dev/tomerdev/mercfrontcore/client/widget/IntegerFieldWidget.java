package dev.tomerdev.mercfrontcore.client.widget;

import java.util.Optional;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import dev.tomerdev.mercfrontcore.util.math.IntBounds;

public class IntegerFieldWidget extends TextFieldWidget {
	public final IntBounds bounds;
	
	public IntegerFieldWidget(TextRenderer font, int x, int y, int width, int height, Text message, IntBounds bounds) {
		super(font, x, y, width, height, message);
		this.bounds = bounds;
		setTextPredicate(str -> {
			if (str == null) {
				return false;
			}
			if (str.isEmpty()) {
				return true;
			}
			try {
				Integer.parseInt(str);
			} catch (NumberFormatException e) {
				return false;
			}
			return true;
		});
		setChangedListener(str -> {
			if (str.isEmpty()) {
				return;
			}
			
			int value = Integer.parseInt(str);
			if (!bounds.within(value)) {
				setText(Integer.toString(bounds.clamp(value)));
			}
		});
	}
	
	public IntegerFieldWidget(TextRenderer font, int x, int y, int width, int height, Text message) {
		this(font, x, y, width, height, message, IntBounds.all());
	}
	
	public IntegerFieldWidget(TextRenderer font, WidgetDim dim, Text message, IntBounds bounds) {
		this(font, dim.x(), dim.y(), dim.width(), dim.height(), message, bounds);
	}
	
	public IntegerFieldWidget(TextRenderer font, WidgetDim dim, Text message) {
		this(font, dim, message, IntBounds.all());
	}
	
	public Optional<Integer> getOptionalInt() {
		try {
			return Optional.of(getInt());
		} catch (NumberFormatException e) {
			return Optional.empty();
		}
	}
	
	public int getInt() {
		return Integer.parseInt(getText());
	}
	
	public void setInt(int value) {
		setText(Integer.toString(bounds.clamp(value)));
	}
}
