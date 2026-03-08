package dev.tomerdev.mercfrontcore.client.widget;

import com.boehmod.blockfront.common.item.GunItem;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import dev.tomerdev.mercfrontcore.client.screen.WeaponExtraScreen;
import dev.tomerdev.mercfrontcore.data.WeaponExtraSettings;

import static dev.tomerdev.mercfrontcore.client.widget.WidgetDim.dim;

public class WeaponEditContainer extends ItemEditContainer {
    private boolean active = true;

    protected final ButtonWidget extraButton;

    public WeaponExtraSettings extra = new WeaponExtraSettings();

    public WeaponEditContainer(Screen screen, TextRenderer font, int x, int y, int width, int height) {
        super(font, x + 10, y, width - 10, height);
        extraButton = Widgets.button(
            Text.literal("*"),
            dim(x, y, 10, height),
            button -> {
                if (getItem() instanceof GunItem gunItem) {
                    MinecraftClient.getInstance().setScreen(
                        new WeaponExtraScreen(screen, gunItem, extra, this)
                    );
                }
            }
        );
    }

    public WeaponEditContainer(Screen screen, TextRenderer font, WidgetDim dim) {
        this(screen, font, dim.x(), dim.y(), dim.width(), dim.height());
    }

    @Override
    public Iterable<Object> getWidgets() {
        List<Object> widgets = new ArrayList<>();
        for (Object widget : super.getWidgets()) {
            widgets.add(widget);
        }
        widgets.add(extraButton);
        return widgets;
    }

    @Override
    protected void onItemIdChanged(String strId) {
        ItemStack itemStack = getPreviewItemStack(strId);
        preview.setItemStack(itemStack);
        extraButton.active = itemStack != null && itemStack.getItem() instanceof GunItem && active;
    }

    @Override
    protected @Nullable ItemStack getPreviewItemStack(String strId) {
        return extra.setComponents(super.getPreviewItemStack(strId));
    }

    @Override
    public ItemStack getValue() {
        return extra.setComponents(super.getValue());
    }

    @Override
    public void setValue(ItemStack itemStack) {
        super.setValue(itemStack);
        retrieveItemStackComponents(itemStack);
        refresh();
    }

    @Override
    public WeaponEditContainer setActive(boolean active) {
        super.setActive(active);
        if (!active && extraButton.active) {
            extraButton.active = false;
        }
        this.active = active;
        return this;
    }

    @Override
    public WeaponEditContainer setVisible(boolean visible) {
        super.setVisible(visible);
        extraButton.visible = visible;
        return this;
    }

    @Override
    public WeaponEditContainer clear() {
        super.clear();
        extra = new WeaponExtraSettings();
        return this;
    }

    public void refresh() {
        preview.setItemStack(extra.setComponents(super.getPreviewItemStack(itemIdField.getText())));
    }

    protected void retrieveItemStackComponents(@Nullable ItemStack itemStack) {
        if (itemStack != null && itemStack.getItem() instanceof GunItem) {
            extra.getComponents(itemStack);
        } else {
            extra = new WeaponExtraSettings();
        }
    }
}
