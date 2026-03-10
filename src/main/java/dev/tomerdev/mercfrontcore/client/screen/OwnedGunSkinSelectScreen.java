package dev.tomerdev.mercfrontcore.client.screen;

import com.boehmod.blockfront.registry.BFDataComponents;
import dev.tomerdev.mercfrontcore.client.widget.ItemPreview;
import dev.tomerdev.mercfrontcore.client.data.AddonClientData;
import dev.tomerdev.mercfrontcore.net.packet.PlayerGunSkinStatePacket;
import dev.tomerdev.mercfrontcore.net.packet.SelectPlayerGunSkinPacket;
import dev.tomerdev.mercfrontcore.setup.GunSkinIndex;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.neoforged.neoforge.network.PacketDistributor;

public final class OwnedGunSkinSelectScreen extends AddonScreen {
    private final Screen parent;
    private final Identifier gunId;
    private final PlayerGunSkinStatePacket.GunSkinState state;
    private final List<SkinEntry> visibleSkinEntries = new ArrayList<>();
    private int page;
    private ItemPreview preview;

    public OwnedGunSkinSelectScreen(Screen parent, Identifier gunId, PlayerGunSkinStatePacket.GunSkinState state) {
        super(Text.literal("Select Gun Skin"));
        this.parent = parent;
        this.gunId = gunId;
        this.state = state;
    }

    @Override
    protected void init() {
        super.init();
        visibleSkinEntries.clear();
        List<String> skins = new ArrayList<>(state.ownedSkins());
        skins.sort(String.CASE_INSENSITIVE_ORDER);
        preview = new ItemPreview(width / 2 + 70, 52, 96)
            .setItemStack(createPreviewStack(state.selectedSkin()));
        addDrawable(preview);

        int pageSize = Math.max(1, (height - 100) / 24);
        int pageCount = Math.max(1, (int) Math.ceil(skins.size() / (double) pageSize));
        page = Math.max(0, Math.min(page, pageCount - 1));

        int start = page * pageSize;
        int end = Math.min(skins.size(), start + pageSize);
        int y = 50;
        for (int i = start; i < end; i++) {
            String skin = skins.get(i);
            boolean selected = skin.equalsIgnoreCase(state.selectedSkin());
            ButtonWidget button = addDrawableChild(ButtonWidget.builder(
                Text.literal((selected ? "> " : "") + skin),
                pressed -> {
                    PacketDistributor.sendToServer(new SelectPlayerGunSkinPacket(gunId, skin));
                    var updated = new PlayerGunSkinStatePacket.GunSkinState(skin, state.ownedSkins());
                    AddonClientData.getInstance().getOwnedGunSkins().put(gunId, updated);
                    MinecraftClient.getInstance().setScreen(new OwnedGunSkinSelectScreen(parent, gunId, updated));
                }
            ).dimensions(width / 2 - 150, y, 180, 20).build());
            visibleSkinEntries.add(new SkinEntry(skin, button));
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
        updatePreview(mouseX, mouseY);
        super.render(context, mouseX, mouseY, delta);
        Item item = Registries.ITEM.get(gunId);
        drawText(item == null ? Text.literal(gunId.toString()) : item.getName(), width / 2, 16, true);
        drawText(Text.literal(gunId.toString()), width / 2, 30, true);
        drawText(Text.literal("Preview"), width / 2 + 118, 34, true);
        drawText(Text.literal("Hover a skin to preview"), width / 2 + 118, 152, true);
    }

    private void updatePreview(int mouseX, int mouseY) {
        if (preview == null) {
            return;
        }

        String previewSkin = state.selectedSkin();
        for (SkinEntry entry : visibleSkinEntries) {
            if (entry.button.isMouseOver(mouseX, mouseY)) {
                previewSkin = entry.skin;
                break;
            }
        }
        preview.setItemStack(createPreviewStack(previewSkin));
    }

    private ItemStack createPreviewStack(String skin) {
        Item item = Registries.ITEM.get(gunId);
        if (item == null) {
            return null;
        }

        ItemStack stack = new ItemStack(item);
        GunSkinIndex.ensureInitialized();
        GunSkinIndex.getSkinId(item, skin).ifPresent(id -> stack.set(BFDataComponents.SKIN_ID, id));
        return stack;
    }

    private record SkinEntry(String skin, ButtonWidget button) {
    }
}
