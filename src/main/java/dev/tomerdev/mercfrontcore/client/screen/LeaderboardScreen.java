package dev.tomerdev.mercfrontcore.client.screen;

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
        drawText(Text.literal("Player"), panelX + 48, panelY + 66, false, false);
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
        String selfName = MinecraftClient.getInstance().player == null ? "" : MinecraftClient.getInstance().player.getNameForScoreboard();
        for (int i = start; i < end; i++) {
            LeaderboardResponsePacket.Entry entry = entries.get(i);
            boolean self = entry.username().equalsIgnoreCase(selfName);
            int color = self ? 0xFFE7D27A : 0xFFFFFFFF;
            drawText(Text.literal(Integer.toString(i + 1)), panelX + 20, y, false, false);
            context.drawText(textRenderer, entry.username(), panelX + 48, y, color, false);
            String xpText = Integer.toString(entry.xp());
            context.drawText(textRenderer, xpText, panelX + 278 - textRenderer.getWidth(xpText), y, color, false);
            y += 12;
        }
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
