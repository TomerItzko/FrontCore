package dev.tomerdev.mercfrontcore.client.screen;

import dev.tomerdev.mercfrontcore.client.data.AddonClientData;
import dev.tomerdev.mercfrontcore.net.packet.PlayerGunSkinStatePacket;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public final class OwnedGunSkinsScreen extends AddonScreen {
    private static final Text TITLE = Text.literal("Owned Gun Skins");
    private final Screen parent;
    private int page;

    public OwnedGunSkinsScreen(Screen parent) {
        super(TITLE);
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        List<Map.Entry<Identifier, PlayerGunSkinStatePacket.GunSkinState>> entries =
            new ArrayList<>(AddonClientData.getInstance().getOwnedGunSkins().entrySet());
        entries.sort(Comparator.comparing(entry -> entry.getKey().toString(), String.CASE_INSENSITIVE_ORDER));

        int pageSize = Math.max(1, (height - 90) / 24);
        int pageCount = Math.max(1, (int) Math.ceil(entries.size() / (double) pageSize));
        page = Math.max(0, Math.min(page, pageCount - 1));

        int start = page * pageSize;
        int end = Math.min(entries.size(), start + pageSize);
        int y = 40;
        for (int i = start; i < end; i++) {
            var entry = entries.get(i);
            Identifier id = entry.getKey();
            PlayerGunSkinStatePacket.GunSkinState state = entry.getValue();
            Item item = Registries.ITEM.get(id);
            String itemName = item == null ? id.toString() : item.getName().getString();
            String selected = state.selectedSkin().isBlank() ? "none" : state.selectedSkin();
            addDrawableChild(ButtonWidget.builder(
                Text.literal(itemName + " [" + selected + "]"),
                button -> MinecraftClient.getInstance().setScreen(new OwnedGunSkinSelectScreen(this, id, state))
            ).dimensions(width / 2 - 130, y, 260, 20).build());
            y += 24;
        }

        addDrawableChild(ButtonWidget.builder(Text.literal("<"), button -> {
            page = Math.max(0, page - 1);
            clearAndInit();
        }).dimensions(width / 2 - 130, height - 28, 20, 20).build()).active = page > 0;

        addDrawableChild(ButtonWidget.builder(Text.literal(">"), button -> {
            page = Math.min(pageCount - 1, page + 1);
            clearAndInit();
        }).dimensions(width / 2 + 110, height - 28, 20, 20).build()).active = page + 1 < pageCount;

        addDrawableChild(ButtonWidget.builder(Text.translatable("gui.back"), button -> MinecraftClient.getInstance().setScreen(parent))
            .dimensions(width / 2 - 40, height - 28, 80, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        drawText(TITLE, width / 2, 16, true);
        if (AddonClientData.getInstance().getOwnedGunSkins().isEmpty()) {
            drawText(Text.literal("You do not own any granted gun skins yet."), width / 2, height / 2, true);
        }
    }
}
