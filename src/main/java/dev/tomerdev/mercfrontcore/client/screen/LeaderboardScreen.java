package dev.tomerdev.mercfrontcore.client.screen;

import com.boehmod.blockfront.util.BFRes;
import dev.tomerdev.mercfrontcore.client.data.AddonClientData;
import dev.tomerdev.mercfrontcore.data.LeaderboardPeriod;
import dev.tomerdev.mercfrontcore.net.packet.LeaderboardRequestPacket;
import dev.tomerdev.mercfrontcore.net.packet.LeaderboardResponsePacket;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.neoforged.neoforge.network.PacketDistributor;

public final class LeaderboardScreen extends AddonScreen {
    private static final Text TITLE = Text.literal("XP Leaderboard");
    private static final int FIRST_PLACE_COLOR = 0xFFFFD54A;
    private static final int SECOND_PLACE_COLOR = 0xFFC9D1D9;
    private static final int THIRD_PLACE_COLOR = 0xFFCD8A5B;
    private static final int SELF_COLOR = 0xFFE7D27A;
    private final Screen parent;
    private LeaderboardPeriod period;
    private int page;
    private ButtonWidget previousButton;
    private ButtonWidget nextButton;

    public LeaderboardScreen(Screen parent, LeaderboardPeriod period) {
        super(TITLE);
        this.parent = parent;
        this.period = period;
    }

    @Override
    protected void init() {
        super.init();
        int panelX = width / 2 - 150;
        int panelY = 28;

        addDrawableChild(ButtonWidget.builder(Text.literal("Weekly"), button -> switchPeriod(LeaderboardPeriod.WEEKLY))
            .dimensions(panelX + 10, panelY + 18, 88, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Monthly"), button -> switchPeriod(LeaderboardPeriod.MONTHLY))
            .dimensions(panelX + 106, panelY + 18, 88, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("All Time"), button -> switchPeriod(LeaderboardPeriod.ALL_TIME))
            .dimensions(panelX + 202, panelY + 18, 88, 20).build());

        previousButton = addDrawableChild(ButtonWidget.builder(Text.literal("<"), button -> {
            page = Math.max(0, page - 1);
            updatePagingButtons();
        }).dimensions(panelX + 10, panelY + 194, 20, 20).build());
        nextButton = addDrawableChild(ButtonWidget.builder(Text.literal(">"), button -> {
            page = Math.min(getPageCount() - 1, page + 1);
            updatePagingButtons();
        }).dimensions(panelX + 270, panelY + 194, 20, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.translatable("gui.back"), button -> MinecraftClient.getInstance().setScreen(parent))
            .dimensions(panelX + 110, panelY + 194, 80, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Refresh"), button -> request(period))
            .dimensions(panelX + 194, panelY + 194, 68, 20).build());

        request(period);
        updatePagingButtons();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        int panelX = width / 2 - 150;
        int panelY = 28;
        int panelWidth = 300;
        int panelHeight = 220;

        context.fill(panelX, panelY, panelX + panelWidth, panelY + panelHeight, 0xDD12161E);
        context.fill(panelX + 1, panelY + 1, panelX + panelWidth - 1, panelY + 1, 0xFF657188);
        context.fill(panelX + 1, panelY + panelHeight - 2, panelX + panelWidth - 1, panelY + panelHeight - 1, 0xFF0A0D12);
        context.fill(panelX + 1, panelY + 1, panelX + 2, panelY + panelHeight - 1, 0xFF657188);
        context.fill(panelX + panelWidth - 2, panelY + 1, panelX + panelWidth - 1, panelY + panelHeight - 1, 0xFF0A0D12);
        context.fill(panelX + 10, panelY + 48, panelX + panelWidth - 10, panelY + 180, 0x661A2230);

        super.render(context, mouseX, mouseY, delta);

        List<LeaderboardResponsePacket.Entry> entries = AddonClientData.getInstance().getLeaderboard(period);
        page = Math.max(0, Math.min(page, Math.max(0, getPageCount() - 1)));
        updatePagingButtons();

        drawText(TITLE, width / 2, panelY + 8, true);
        drawText(period.displayText(), width / 2, panelY + 52, true);
        drawText(Text.literal("#"), panelX + 20, panelY + 66, false, false);
        drawText(Text.literal("Player"), panelX + 64, panelY + 66, false, false);
        drawText(Text.literal("XP"), panelX + 250, panelY + 66, false, false);

        if (AddonClientData.getInstance().isLeaderboardLoading(period) && entries.isEmpty()) {
            drawText(Text.literal("Loading leaderboard..."), width / 2, panelY + 122, true);
            return;
        }
        if (entries.isEmpty()) {
            drawText(Text.literal("No XP data is available for this period yet."), width / 2, panelY + 122, true);
            return;
        }

        int pageSize = getPageSize();
        int start = page * pageSize;
        int end = Math.min(entries.size(), start + pageSize);
        int y = panelY + 82;
        var self = MinecraftClient.getInstance().player;
        for (int i = start; i < end; i++) {
            LeaderboardResponsePacket.Entry entry = entries.get(i);
            boolean isSelf = self != null && entry.uuid().equals(self.getUuid());
            int color = getEntryColor(i, isSelf);
            context.drawText(textRenderer, Integer.toString(i + 1), panelX + 20, y, color, false);
            drawRankIcon(context, entry.rankTexture(), panelX + 48, y - 1);
            context.drawText(textRenderer, entry.username(), panelX + 64, y, color, false);
            String xpText = Integer.toString(entry.xp());
            context.drawText(textRenderer, xpText, panelX + 278 - textRenderer.getWidth(xpText), y, color, false);
            y += 12;
        }
    }

    private int getEntryColor(int entryIndex, boolean isSelf) {
        return switch (entryIndex) {
            case 0 -> FIRST_PLACE_COLOR;
            case 1 -> SECOND_PLACE_COLOR;
            case 2 -> THIRD_PLACE_COLOR;
            default -> isSelf ? SELF_COLOR : 0xFFFFFFFF;
        };
    }

    private void drawRankIcon(DrawContext context, String rankTexture, int x, int y) {
        if (rankTexture == null || rankTexture.isBlank()) {
            return;
        }
        context.drawTexture(BFRes.loc("textures/misc/ranks/" + rankTexture + ".png"), x, y, 0.0f, 0.0f, 10, 10, 10, 10);
    }

    private void switchPeriod(LeaderboardPeriod newPeriod) {
        if (period == newPeriod) {
            request(newPeriod);
            return;
        }
        period = newPeriod;
        page = 0;
        request(newPeriod);
        updatePagingButtons();
    }

    private void request(LeaderboardPeriod period) {
        AddonClientData.getInstance().markLeaderboardLoading(period);
        PacketDistributor.sendToServer(new LeaderboardRequestPacket(period.id()));
    }

    private int getPageCount() {
        int size = AddonClientData.getInstance().getLeaderboard(period).size();
        return Math.max(1, (int) Math.ceil(size / (double) getPageSize()));
    }

    private int getPageSize() {
        return 8;
    }

    private void updatePagingButtons() {
        if (previousButton == null || nextButton == null) {
            return;
        }
        int pageCount = getPageCount();
        previousButton.active = page > 0;
        nextButton.active = page + 1 < pageCount;
    }
}
