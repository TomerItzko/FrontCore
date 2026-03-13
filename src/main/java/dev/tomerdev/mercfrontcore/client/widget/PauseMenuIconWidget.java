package dev.tomerdev.mercfrontcore.client.widget;

import com.boehmod.blockfront.registry.BFItems;
import dev.tomerdev.mercfrontcore.client.screen.LeaderboardScreen;
import dev.tomerdev.mercfrontcore.client.screen.OwnedGunSkinsScreen;
import dev.tomerdev.mercfrontcore.data.LeaderboardPeriod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public final class PauseMenuIconWidget extends ClickableWidget {
    private static final Identifier VANILLA_BUTTON = Identifier.ofVanilla("widget/button");
    private static final Identifier VANILLA_BUTTON_HIGHLIGHTED = Identifier.ofVanilla("widget/button_highlighted");

    private final GameMenuScreen parent;
    private final ItemStack iconStack;
    private final Action action;

    public PauseMenuIconWidget(int x, int y, int size, GameMenuScreen parent, Action action) {
        super(x, y, size, size, Text.empty());
        this.parent = parent;
        this.action = action;
        this.iconStack = action.createIcon();
        setTooltip(Tooltip.of(action.label()));
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) {
            return;
        }
        switch (action) {
            case LEADERBOARD -> client.setScreen(new LeaderboardScreen(parent, LeaderboardPeriod.ALL_TIME));
            case GUN_SKINS -> client.setScreen(new OwnedGunSkinsScreen(parent));
        }
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawGuiTexture(
            isHovered() ? VANILLA_BUTTON_HIGHLIGHTED : VANILLA_BUTTON,
            getX(),
            getY(),
            width,
            height
        );
        int iconX = getX() + (width - 16) / 2;
        int iconY = getY() + (height - 16) / 2;
        context.drawItem(iconStack, iconX, iconY);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        appendDefaultNarrations(builder);
    }

    public enum Action {
        LEADERBOARD(Text.literal("Leaderboard")) {
            @Override
            ItemStack createIcon() {
                return new ItemStack(BFItems.EVENT_TROPHY_GOLD.get());
            }
        },
        GUN_SKINS(Text.literal("Gun Skins")) {
            @Override
            ItemStack createIcon() {
                return new ItemStack(BFItems.GUN_M1928A1_THOMPSON.get());
            }
        };

        private final Text label;

        Action(Text label) {
            this.label = label;
        }

        Text label() {
            return label;
        }

        abstract ItemStack createIcon();
    }
}
