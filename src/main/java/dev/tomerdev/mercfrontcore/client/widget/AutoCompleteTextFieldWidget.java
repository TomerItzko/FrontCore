package dev.tomerdev.mercfrontcore.client.widget;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class AutoCompleteTextFieldWidget extends TextFieldWidget {
    private static final int MAX_RENDERED = 6;
    private static final List<AutoCompleteTextFieldWidget> INSTANCES = new CopyOnWriteArrayList<>();

    private final TextRenderer font;
    private final Screen ownerScreen;
    private final Supplier<List<String>> suggestionsSupplier;
    private Consumer<String> userChangedListener = s -> {
    };
    private List<String> filtered = List.of();
    private int selectedIndex = -1;

    public AutoCompleteTextFieldWidget(TextRenderer textRenderer, int x, int y, int width, int height, Supplier<List<String>> suggestionsSupplier) {
        super(textRenderer, x, y, width, height, Text.empty());
        this.font = textRenderer;
        this.ownerScreen = MinecraftClient.getInstance().currentScreen;
        this.suggestionsSupplier = suggestionsSupplier;
        super.setChangedListener(this::onTextChangedInternal);
        INSTANCES.add(this);
    }

    @Override
    public void setChangedListener(Consumer<String> changedListener) {
        this.userChangedListener = Objects.requireNonNullElseGet(changedListener, () -> s -> {
        });
    }

    private void onTextChangedInternal(String value) {
        refreshSuggestions(value);
        userChangedListener.accept(value);
    }

    private void refreshSuggestions(String value) {
        String query = value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
        List<String> all = suggestionsSupplier.get();
        if (all == null || all.isEmpty()) {
            filtered = List.of();
            selectedIndex = -1;
            return;
        }

        List<String> starts = new ArrayList<>();
        List<String> contains = new ArrayList<>();
        for (String suggestion : all) {
            if (suggestion == null || suggestion.isBlank()) {
                continue;
            }
            String lower = suggestion.toLowerCase(Locale.ROOT);
            if (query.isEmpty() || lower.startsWith(query)) {
                starts.add(suggestion);
            } else if (lower.contains(query)) {
                contains.add(suggestion);
            }
        }

        starts.addAll(contains);
        filtered = starts;
        selectedIndex = filtered.isEmpty() ? -1 : 0;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.active && this.visible && this.isFocused() && !filtered.isEmpty()) {
            if (keyCode == GLFW.GLFW_KEY_TAB || keyCode == GLFW.GLFW_KEY_DOWN) {
                cycle(1);
                return true;
            }
            if (keyCode == GLFW.GLFW_KEY_UP) {
                cycle(-1);
                return true;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void setFocused(boolean focused) {
        super.setFocused(focused);
        if (focused) {
            refreshSuggestions(this.getText());
        }
    }

    private void cycle(int direction) {
        if (filtered.isEmpty()) {
            return;
        }
        if (selectedIndex < 0) {
            selectedIndex = 0;
        } else {
            selectedIndex = Math.floorMod(selectedIndex + direction, filtered.size());
        }
        String next = filtered.get(selectedIndex);
        this.setText(next);
        this.setCursorToEnd(false);
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderWidget(context, mouseX, mouseY, delta);
    }

    private void renderOverlay(DrawContext context) {
        if (!this.active || !this.visible || !this.isFocused() || filtered.isEmpty()) {
            return;
        }

        int x1 = getX();
        int y1 = getY() + this.getHeight() + 1;
        int x2 = getX() + this.getWidth();
        int count = Math.min(MAX_RENDERED, filtered.size());
        int rowHeight = this.font.fontHeight + 4;
        int boxHeight = count * rowHeight + 2;

        context.getMatrices().push();
        context.getMatrices().translate(0, 0, 500);
        context.fill(x1, y1, x2, y1 + boxHeight, 0xF0101010);
        context.drawBorder(x1, y1, x2 - x1, boxHeight, 0xFF5A5A5A);
        for (int i = 0; i < count; i++) {
            int rowY = y1 + 1 + i * rowHeight;
            if (i == selectedIndex) {
                context.fill(x1 + 1, rowY, x2 - 1, rowY + rowHeight, 0xFF2F2F2F);
            }
            context.drawText(this.font, filtered.get(i), x1 + 3, rowY + 2, 0xFFFFFF, true);
        }
        context.getMatrices().pop();
    }

    public static void renderAllOverlays(DrawContext context) {
        Screen current = MinecraftClient.getInstance().currentScreen;
        for (AutoCompleteTextFieldWidget widget : INSTANCES) {
            if (widget == null) {
                continue;
            }
            if (widget.ownerScreen != current) {
                continue;
            }
            widget.renderOverlay(context);
        }
    }
}
