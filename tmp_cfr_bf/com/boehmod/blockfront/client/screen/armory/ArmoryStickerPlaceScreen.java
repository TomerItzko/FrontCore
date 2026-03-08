/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.screen.armory;

import com.boehmod.bflib.cloud.common.CloudRegistry;
import com.boehmod.bflib.cloud.common.item.CloudItem;
import com.boehmod.bflib.cloud.common.item.CloudItemStack;
import com.boehmod.bflib.cloud.common.item.CloudItemType;
import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.cloud.packet.common.inventory.PacketInventoryItemStickerAdd;
import com.boehmod.bflib.cloud.packet.common.inventory.PacketInventoryItemStickerRemove;
import com.boehmod.blockfront.client.gui.widget.BFButton;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.item.CloudItemRenderer;
import com.boehmod.blockfront.client.render.item.CloudItemRenderers;
import com.boehmod.blockfront.client.screen.BFMenuScreen;
import com.boehmod.blockfront.client.screen.armory.ArmorySelectScreen;
import com.boehmod.blockfront.client.screen.armory.IReceivesArmorySelection;
import com.boehmod.blockfront.client.screen.prompt.confirm.BFConfirmPromptScreen;
import com.boehmod.blockfront.client.screen.title.ArmoryTitleScreen;
import com.boehmod.blockfront.cloud.PlayerCloudInventory;
import com.boehmod.blockfront.cloud.client.ClientConnectionManager;
import com.boehmod.blockfront.common.gun.GunSoundConfig;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.item.ItemSkinIndex;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.util.BFStyles;
import com.boehmod.blockfront.util.InventorySoundUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public class ArmoryStickerPlaceScreen
extends BFMenuScreen
implements IReceivesArmorySelection {
    private static final Component field_960 = Component.translatable((String)"bf.message.prompt.item.sticker.place.title");
    private static final int field_957 = 3;
    public static final MutableComponent field_6318 = Component.translatable((String)"bf.screen.armory.inspect");
    public static float field_955 = 0.0f;
    public static float field_956 = 0.0f;
    private final CloudItemStack field_959;
    public int field_958 = 0;
    private CloudItemStack field_961 = null;
    private BFButton field_962 = null;
    private BFButton field_963 = null;
    private boolean field_953 = false;

    public ArmoryStickerPlaceScreen(@NotNull CloudItemStack cloudItemStack) {
        super((Component)field_6318);
        GunItem gunItem;
        DeferredHolder<SoundEvent, SoundEvent> deferredHolder;
        Object object;
        this.field_959 = cloudItemStack;
        SoundManager soundManager = this.minecraft.getSoundManager();
        CloudRegistry cloudRegistry = this.manager.getCloudRegistry();
        CloudItem cloudItem = cloudItemStack.getCloudItem(cloudRegistry);
        assert (cloudItem != null);
        ItemStack itemStack = ItemSkinIndex.method_1722(cloudItem, cloudItemStack);
        if (!itemStack.isEmpty() && (object = itemStack.getItem()) instanceof GunItem && (deferredHolder = ((GunSoundConfig)(object = (gunItem = (GunItem)object).getSoundConfig(itemStack))).getReload()) != null) {
            soundManager.play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)deferredHolder.get()), (float)1.0f, (float)1.0f));
        }
        InventorySoundUtils.playInspectFull(this.minecraft, cloudRegistry, cloudItemStack);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.field_953) {
            this.minecraft.setScreen((Screen)new ArmoryTitleScreen());
            return;
        }
        CloudRegistry cloudRegistry = this.manager.getCloudRegistry();
        PlayerCloudData playerCloudData = this.playerDataHandler.getCloudData(this.minecraft);
        PlayerCloudInventory playerCloudInventory = (PlayerCloudInventory)playerCloudData.getInventory();
        ObjectList objectList = playerCloudInventory.getItems(cloudRegistry, CloudItemType.STICKER);
        boolean bl = !objectList.isEmpty();
        boolean bl2 = this.field_959.hasSticker(this.field_958);
        this.field_962.active = bl;
        this.field_963.active = bl2;
        this.field_962.tip((Component)Component.translatable((String)(bl ? "bf.screen.armory.stickers.set.tooltip" : "bf.screen.armory.stickers.set.tooltip.inactive")));
        this.field_963.tip((Component)Component.translatable((String)(bl2 ? "bf.screen.armory.stickers.remove.tooltip" : "bf.screen.armory.stickers.remove.tooltip.inactive")));
    }

    @Override
    public void method_758() {
        super.method_758();
        int n = 20;
        int n2 = 50;
        boolean bl = true;
        this.addRenderableWidget((GuiEventListener)new BFButton(1, 50, 20, 20, (Component)Component.literal((String)"<"), button -> {
            --this.field_958;
            if (this.field_958 < 0) {
                this.field_958 = 2;
            }
        }));
        this.field_962 = new BFButton(21, 50, 25, 20, (Component)Component.literal((String)"SET"), button -> this.method_718());
        this.addRenderableWidget((GuiEventListener)this.field_962);
        this.field_963 = new BFButton(46, 50, 45, 20, (Component)Component.literal((String)"REMOVE"), button -> this.method_719());
        this.addRenderableWidget((GuiEventListener)this.field_963);
        this.addRenderableWidget((GuiEventListener)new BFButton(91, 50, 20, 20, (Component)Component.literal((String)">"), button -> {
            ++this.field_958;
            if (this.field_958 >= 3) {
                this.field_958 = 0;
            }
        }));
        Button.OnPress onPress = button -> this.minecraft.setScreen((Screen)new ArmoryTitleScreen());
        this.addRenderableWidget((GuiEventListener)new BFButton(5, 18, 20, 20, (Component)Component.empty(), onPress).texture(RETURN_ICON).size(20, 20).displayType(BFButton.DisplayType.NONE).tip((Component)Component.translatable((String)"bf.menu.button.back")));
    }

    private void method_718() {
        CloudRegistry cloudRegistry = this.manager.getCloudRegistry();
        PlayerCloudData playerCloudData = this.playerDataHandler.getCloudData(this.minecraft);
        ObjectList objectList = ((PlayerCloudInventory)playerCloudData.getInventory()).getItems(cloudRegistry, CloudItemType.STICKER);
        if (!objectList.isEmpty()) {
            this.minecraft.setScreen((Screen)new ArmorySelectScreen(this, (List<CloudItemStack>)objectList, 1));
        }
    }

    private void method_719() {
        BFConfirmPromptScreen bFConfirmPromptScreen = new BFConfirmPromptScreen((Screen)this, (Component)Component.translatable((String)"bf.message.prompt.item.sticker.remove.title"), bl -> {
            if (bl) {
                SoundManager soundManager = this.minecraft.getSoundManager();
                soundManager.play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, (float)1.0f));
                PacketInventoryItemStickerRemove packetInventoryItemStickerRemove = new PacketInventoryItemStickerRemove(this.field_959.getUUID(), this.field_958);
                ((ClientConnectionManager)this.manager.getConnectionManager()).sendPacket((IPacket)packetInventoryItemStickerRemove);
            }
            this.field_953 = true;
        });
        MutableComponent mutableComponent = Component.translatable((String)"bf.message.prompt.item.sticker.remove");
        bFConfirmPromptScreen.method_1084((Component)mutableComponent);
        this.minecraft.setScreen((Screen)bFConfirmPromptScreen);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
        super.render(guiGraphics, n, n2, f);
        CloudRegistry cloudRegistry = this.manager.getCloudRegistry();
        PoseStack poseStack = guiGraphics.pose();
        int n3 = this.width / 2;
        int n4 = this.height / 2;
        CloudItem cloudItem = this.field_959.getCloudItem(cloudRegistry);
        if (cloudItem == null) {
            return;
        }
        BFRendering.centeredTexture(poseStack, guiGraphics, BFMenuScreen.BACKSHADOW, n3, n4, 200, 200);
        poseStack.pushPose();
        float f2 = -0.5f + (float)n2 / (float)this.height;
        float f3 = -0.5f + (float)n / (float)this.width;
        poseStack.translate(0.0f, 0.0f, 150.0f);
        field_955 = 25.0f * f2;
        field_956 = 65.0f * f3;
        CloudItemRenderer<?> cloudItemRenderer = CloudItemRenderers.getRenderer(cloudItem);
        cloudItemRenderer.method_1747(cloudItem, this.field_959, this.minecraft, poseStack, guiGraphics, this.width, this.height, n, n2, f);
        poseStack.popPose();
        Object object = "";
        Optional optional = this.field_959.getNameTag();
        if (optional.isPresent()) {
            object = String.valueOf(ChatFormatting.ITALIC) + " (" + (String)optional.get() + ")";
        }
        String string = this.field_959.getDisplayName(cloudRegistry) + (String)object;
        int n5 = cloudItem.getRarity().getColor();
        MutableComponent mutableComponent = Component.literal((String)string).withColor(n5).withStyle(BFStyles.BOLD);
        BFRendering.centeredComponent2d(poseStack, this.font, guiGraphics, (Component)mutableComponent, n3, this.height - 55, 1.5f);
    }

    public CloudItemStack method_721() {
        return this.field_959;
    }

    @Override
    public void acceptSelected(@NotNull List<CloudItemStack> items) {
        if (items.isEmpty()) {
            return;
        }
        this.field_961 = items.getFirst();
        CloudRegistry cloudRegistry = this.manager.getCloudRegistry();
        CloudItem cloudItem = this.field_961.getCloudItem(cloudRegistry);
        if (cloudItem == null) {
            return;
        }
        String string = cloudItem.getSuffix();
        BFConfirmPromptScreen bFConfirmPromptScreen = new BFConfirmPromptScreen((Screen)this, field_960, bl -> {
            if (bl && this.field_961 != null) {
                SoundManager soundManager = this.minecraft.getSoundManager();
                soundManager.play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, (float)1.0f));
                PacketInventoryItemStickerAdd packetInventoryItemStickerAdd = new PacketInventoryItemStickerAdd(this.field_959.getUUID(), this.field_961.getUUID(), this.field_958);
                ((ClientConnectionManager)this.manager.getConnectionManager()).sendPacket((IPacket)packetInventoryItemStickerAdd);
            }
            this.field_961 = null;
            this.field_953 = true;
        });
        MutableComponent mutableComponent = Component.literal((String)string).withColor(cloudItem.getRarity().getColor());
        MutableComponent mutableComponent2 = Component.translatable((String)"bf.message.prompt.item.sticker.place", (Object[])new Object[]{mutableComponent});
        bFConfirmPromptScreen.method_1084((Component)mutableComponent2);
        this.minecraft.setScreen((Screen)bFConfirmPromptScreen);
    }
}

