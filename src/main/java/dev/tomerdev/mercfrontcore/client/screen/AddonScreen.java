package dev.tomerdev.mercfrontcore.client.screen;

import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.text.Text;

import dev.tomerdev.mercfrontcore.client.widget.CompoundWidget;

public abstract class AddonScreen extends ImmediateScreen {
	public AddonScreen(Text title) {
		super(title);
	}
	
	protected <T extends CompoundWidget> T addCompoundWidget(T compoundWidget) {
		for (Object widget : compoundWidget.getWidgets()) {
			if (widget instanceof Element && widget instanceof Selectable) {
				addSelectableChild((Element & Selectable) widget);
			}
			if (widget instanceof Drawable) {
				addDrawable((Drawable) widget);
			}
		}
		return compoundWidget;
	}
}
