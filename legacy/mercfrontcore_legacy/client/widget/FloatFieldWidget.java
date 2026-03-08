package red.vuis.mercfrontcore.client.widget;

import java.util.Optional;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import red.vuis.mercfrontcore.util.math.FloatBounds;

public class FloatFieldWidget extends TextFieldWidget {
	public final FloatBounds bounds;
	
	public FloatFieldWidget(TextRenderer textRenderer, int x, int y, int width, int height, Text message, FloatBounds bounds) {
		super(textRenderer, x, y, width, height, message);
		this.bounds = bounds;
		setTextPredicate(str -> {
			if (str == null) {
				return false;
			}
			if (str.isEmpty()) {
				return true;
			}
			try {
				Float.parseFloat(str);
			} catch (NumberFormatException e) {
				return false;
			}
			return true;
		});
		setChangedListener(str -> {
			if (str.isEmpty()) {
				return;
			}
			
			float value = Float.parseFloat(str);
			if (!bounds.within(value)) {
				setText(Float.toString(bounds.clamp(value)));
			}
		});
	}
	
	public FloatFieldWidget(TextRenderer font, int x, int y, int width, int height, Text message) {
		this(font, x, y, width, height, message, FloatBounds.all());
	}
	
	public FloatFieldWidget(TextRenderer font, WidgetDim dim, Text message, FloatBounds bounds) {
		this(font, dim.x(), dim.y(), dim.width(), dim.height(), message, bounds);
	}
	
	public FloatFieldWidget(TextRenderer font, WidgetDim dim, Text message) {
		this(font, dim, message, FloatBounds.all());
	}
	
	public Optional<Float> getOptionalFloat() {
		try {
			return Optional.of(getFloat());
		} catch (NumberFormatException e) {
			return Optional.empty();
		}
	}
	
	public float getFloat() {
		return Float.parseFloat(getText());
	}
	
	public void setFloat(float value) {
		setText(Float.toString(bounds.clamp(value)));
	}
}
