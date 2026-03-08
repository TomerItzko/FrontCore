/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.gui.widget;

import com.boehmod.bflib.cloud.common.CloudRegistry;
import com.boehmod.bflib.cloud.common.item.CloudInventoryFilter;
import com.boehmod.bflib.cloud.common.item.CloudItem;
import com.boehmod.bflib.cloud.common.item.CloudItemStack;
import com.boehmod.bflib.cloud.common.item.CloudItemType;
import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.gui.widget.BFButton;
import com.boehmod.blockfront.client.gui.widget.BFWidget;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.item.CloudItemRenderer;
import com.boehmod.blockfront.client.render.item.CloudItemRenderers;
import com.boehmod.blockfront.client.screen.BFMenuScreen;
import com.boehmod.blockfront.client.screen.title.ArmoryTitleScreen;
import com.boehmod.blockfront.cloud.PlayerCloudInventory;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.unnamed.BF_120;
import com.boehmod.blockfront.util.BFStyles;
import com.boehmod.blockfront.util.armory.ArmoryDropdownUtils;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import org.jetbrains.annotations.NotNull;

public final class NewItemWidget
extends BFWidget {
    private static final int field_579 = 15;
    private static final List<BF_120> field_587 = new ObjectArrayList();
    public static final Component field_588 = Component.translatable((String)"bf.menu.inventory.sync");
    public static final Component field_589 = Component.translatable((String)"bf.menu.inventory.text.none").withStyle(BFStyles.BOLD);
    public static final Component field_571 = Component.translatable((String)"bf.menu.inventory.newitem").withStyle(ChatFormatting.BOLD);
    public static final Component field_572 = Component.translatable((String)"bf.dropdown.text.next");
    public static final Component field_573 = Component.translatable((String)"bf.dropdown.text.finish");
    public static final int field_580 = 70;
    public static final int field_581 = 16;
    public static final int field_582 = 32;
    private static final int field_7029 = 80;
    public static int field_583 = 0;
    static int field_584 = 0;
    private static int field_585 = 1;
    private static int field_586 = 0;
    private static BFButton field_574;
    private static BFButton field_578;
    @Nullable
    private static CloudItemStack field_577;
    private static UUID field_575;
    private static boolean field_576;

    public NewItemWidget(int n, int n2, int n3, int n4, Screen screen) {
        super(n, n2, n3, n4, screen);
    }

    private static void method_574() {
        if (field_585 > 1 && field_583 < field_585 - 1) {
            ++field_583;
        }
    }

    private static void method_575() {
        if (field_583 > 0) {
            --field_583;
        }
    }

    @Override
    public void method_535(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, ClientPlayerDataHandler clientPlayerDataHandler) {
        int n;
        int n2;
        Object object;
        List<UUID> list;
        super.method_535(minecraft, bFClientManager, clientPlayerDataHandler);
        CloudRegistry cloudRegistry = bFClientManager.getCloudRegistry();
        PlayerCloudData playerCloudData = clientPlayerDataHandler.getCloudData(minecraft);
        PlayerCloudInventory playerCloudInventory = (PlayerCloudInventory)playerCloudData.getInventory();
        Collection collection = playerCloudInventory.getItems();
        List list2 = CloudInventoryFilter.filterByRarity((CloudRegistry)cloudRegistry, (Collection)collection);
        if (ArmoryTitleScreen.field_939 != CloudItemType.ALL) {
            list2 = CloudInventoryFilter.filterByType((CloudRegistry)cloudRegistry, (Collection)list2, (CloudItemType)ArmoryTitleScreen.field_939);
        }
        if (!(list = playerCloudInventory.method_1681()).isEmpty()) {
            if (!field_576) {
                field_576 = true;
                minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)SoundEvents.PLAYER_LEVELUP, (float)1.0f));
            }
        } else {
            field_576 = false;
        }
        field_587.clear();
        this.method_563();
        field_585 = this.method_564(list2.size());
        int n3 = this.field_564 + 12;
        int n4 = this.field_565 + 4;
        int n5 = 40;
        int n6 = 40;
        int n7 = 42;
        int n8 = 43;
        int n9 = 5;
        int n10 = 3;
        int n11 = field_583 * 15;
        int n12 = list2.size();
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 5; ++j) {
                if (n11 < n12) {
                    object = (CloudItemStack)list2.get(n11);
                    n2 = playerCloudInventory.isItemEquipped(cloudRegistry, (CloudItemStack)object);
                    n = playerCloudInventory.isItemShowcased(object.getUUID());
                    BF_120 bF_120 = new BF_120(this.getScreen(), (CloudItemStack)object, n3 + j * 42, n4 + i * 43, 40, 40, n2 != 0, n != 0).method_527(true);
                    field_587.add(bF_120);
                }
                ++n11;
            }
        }
        NewItemWidget newItemWidget = this;
        BFButton bFButton = new BFButton(this.field_564 + 2, this.field_565 + this.height / 2 - 3, 10, 10, (Component)Component.literal((String)"<"), button -> {
            if (field_577 == null) {
                NewItemWidget.method_575();
                newItemWidget.method_535(minecraft, bFClientManager, clientPlayerDataHandler);
            }
        });
        bFButton.displayType(BFButton.DisplayType.NONE);
        bFButton.method_366();
        this.addMcWidget((AbstractWidget)bFButton);
        object = new BFButton(this.field_564 + this.field_566 - 11, this.field_565 + this.height / 2 - 3, 10, 10, (Component)Component.literal((String)">"), button -> {
            if (field_577 == null) {
                NewItemWidget.method_574();
                newItemWidget.method_535(minecraft, bFClientManager, clientPlayerDataHandler);
            }
        });
        ((BFButton)((Object)object)).displayType(BFButton.DisplayType.NONE);
        ((BFButton)((Object)object)).method_366();
        this.addMcWidget((AbstractWidget)object);
        n2 = this.field_565 + this.height - 40;
        n = this.field_564 + 32;
        field_578 = new BFButton(n, n2, 70, 16, field_573, button -> {
            field_577 = null;
            minecraft.setScreen((Screen)new ArmoryTitleScreen());
            ((PlayerCloudInventory)playerCloudData.getInventory()).method_1678();
            field_576 = false;
        });
        field_578.displayType(BFButton.DisplayType.SHADOW);
        field_578.method_368(0.0f, 1.0f);
        field_578.method_366();
        this.addMcWidget((AbstractWidget)field_578);
        int n13 = this.field_564 + this.field_566 - 70 - 32;
        field_574 = new BFButton(n13, n2, 70, 16, field_572, button -> {
            field_577 = null;
            minecraft.setScreen((Screen)new ArmoryTitleScreen());
            minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)SoundEvents.EXPERIENCE_ORB_PICKUP, (float)((float)((double)0.9f + (double)0.2f * Math.random()))));
        });
        field_574.displayType(BFButton.DisplayType.SHADOW);
        field_574.method_368(0.0f, 1.0f);
        field_574.method_366();
        this.addMcWidget((AbstractWidget)field_574);
        this.method_573(false);
    }

    private void method_573(boolean bl) {
        NewItemWidget.field_574.visible = bl;
        NewItemWidget.field_574.active = bl;
        NewItemWidget.field_578.visible = bl;
        NewItemWidget.field_578.active = bl;
    }

    @Override
    public void method_537(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, float f) {
        super.method_537(minecraft, bFClientManager, clientPlayerDataHandler, f);
        CloudRegistry cloudRegistry = bFClientManager.getCloudRegistry();
        PlayerCloudData playerCloudData = clientPlayerDataHandler.getCloudData(minecraft);
        PlayerCloudInventory playerCloudInventory = (PlayerCloudInventory)playerCloudData.getInventory();
        if (field_584 > 0) {
            --field_584;
            return;
        }
        if (field_586++ > 5) {
            this.method_535(minecraft, bFClientManager, clientPlayerDataHandler);
            field_586 = 0;
        }
        field_587.forEach(bF_120 -> {
            bF_120.method_528(playerCloudInventory.isItemEquipped(cloudRegistry, bF_120.method_521()));
            boolean bl = playerCloudInventory.isItemShowcased(bF_120.method_521().getUUID());
            bF_120.method_529(bl);
        });
        if (field_577 == null) {
            playerCloudInventory.method_1671().ifPresent(uUID -> {
                field_577 = playerCloudInventory.getStackFromUUID((UUID)uUID).orElse(null);
            });
        }
        ObjectArrayList objectArrayList = playerCloudInventory.method_1681().stream().filter(arg_0 -> ((PlayerCloudInventory)playerCloudInventory).hasItemStack(arg_0)).collect(Collectors.toCollection(ObjectArrayList::new));
        playerCloudInventory.method_1680((List<UUID>)objectArrayList);
        this.method_573(field_577 != null);
        field_587.forEach(bF_120 -> bF_120.updateParticles(minecraft, cloudRegistry));
    }

    @Override
    public void render(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, ClientPlayerDataHandler clientPlayerDataHandler, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, Font font, @NotNull PlayerCloudData playerCloudData, int n, int n2, float f, float f2) {
        int n3;
        super.render(minecraft, bFClientManager, clientPlayerDataHandler, poseStack, guiGraphics, font, playerCloudData, n, n2, f, f2);
        MultiBufferSource.BufferSource bufferSource = guiGraphics.bufferSource();
        CloudRegistry cloudRegistry = bFClientManager.getCloudRegistry();
        poseStack.pushPose();
        int n4 = this.field_566 / 2;
        int n5 = this.height / 2;
        if (field_584 > 0) {
            BFRendering.centeredString(font, guiGraphics, field_588, this.field_564 + n4, this.field_565 + 50);
            return;
        }
        int n6 = field_587.size();
        BF_120 bF_120 = null;
        for (n3 = 0; n3 < n6; ++n3) {
            BF_120 bF_1202 = field_587.get(n3);
            bF_1202.method_525(minecraft, guiGraphics, font, field_577 != null, n, n2, f2);
            if (!bF_1202.method_522(n, n2) || bF_120 == bF_1202 || field_577 != null) continue;
            bF_120 = bF_1202;
        }
        if (field_587.isEmpty()) {
            n3 = 42;
            BFRendering.runningTexture(guiGraphics, this.field_564 + n4 - n3 / 2, this.field_565 + n5 - n3 + 8, n3, ColorReferences.COLOR_WHITE_SOLID, f);
            BFRendering.centeredString(font, guiGraphics, field_589, this.field_564 + n4, this.field_565 + this.height / 2 + 16);
        }
        if (minecraft.screen != this.getScreen()) {
            return;
        }
        if (bF_120 != null) {
            if (!bF_120.field_548.getUUID().equals(field_575)) {
                field_575 = bF_120.field_548.getUUID();
                minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.GUI_BUTTON_HOVER.get()), (float)1.0f));
                minecraft.getNarrator().sayNow(bF_120.field_548.getDisplayName(cloudRegistry, true));
            }
            poseStack.pushPose();
            poseStack.translate(0.0f, 0.0f, 400.0f);
            poseStack.popPose();
        }
        this.method_570(minecraft, guiGraphics, font, poseStack, (MultiBufferSource)bufferSource, cloudRegistry, playerCloudData, n, n2, f2);
        poseStack.popPose();
    }

    private void method_570(@NotNull Minecraft minecraft, @NotNull GuiGraphics guiGraphics, Font font, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, @NotNull CloudRegistry cloudRegistry, @NotNull PlayerCloudData playerCloudData, int n, int n2, float f) {
        if (field_577 == null) {
            return;
        }
        int n3 = this.field_566 / 2;
        int n4 = this.height / 2;
        CloudItem cloudItem = field_577.getCloudItem(cloudRegistry);
        if (cloudItem == null) {
            return;
        }
        int n5 = cloudItem.getRarity().getColor();
        poseStack.pushPose();
        poseStack.translate(0.0f, 0.0f, 80.0f);
        BFRendering.rectangle(guiGraphics, this.field_564, this.field_565, this.field_566, this.height, BFRendering.translucentBlack(), 0.9f);
        poseStack.pushPose();
        BFRendering.centeredTintedTexture(poseStack, guiGraphics, BFMenuScreen.BACKSHADOW_WHITE, this.field_564 + n3, this.field_565 + n4, this.field_566, this.height, 0.0f, 1.0f, n5);
        CloudItemRenderer<?> cloudItemRenderer = CloudItemRenderers.getRenderer(cloudItem);
        cloudItemRenderer.method_1748(cloudItem, field_577, minecraft, guiGraphics, poseStack, multiBufferSource, this.field_564, this.field_565, this.field_566, this.height, n, n2, f);
        int n6 = this.field_565 + 4;
        BFRendering.centeredComponent2dWithShadow(poseStack, font, guiGraphics, field_571, this.field_564 + this.field_566 / 2, n6, 1.5f);
        Object object = field_577.getDisplayName(cloudRegistry);
        float f2 = 2.0f;
        int n7 = 60;
        int n8 = this.field_566 - 60;
        float f3 = MathUtils.getScaledBoxTextWidth((String)object, minecraft.font, 2.0f, n8, 2.0f);
        int n9 = this.field_565 + 20;
        MutableComponent mutableComponent = Component.literal((String)object).withStyle(ChatFormatting.BOLD).withColor(n5);
        BFRendering.centeredComponent2dWithShadow(poseStack, font, guiGraphics, (Component)mutableComponent, this.field_564 + this.field_566 / 2, n9, f3);
        object = ((PlayerCloudInventory)playerCloudData.getInventory()).method_1681();
        if (!object.isEmpty()) {
            MutableComponent mutableComponent2 = Component.translatable((String)"bf.menu.inventory.newitem.more", (Object[])new Object[]{String.valueOf(object.size())});
            MutableComponent mutableComponent3 = mutableComponent2.withStyle(ChatFormatting.GRAY);
            BFRendering.centeredComponent2dWithShadow(poseStack, font, guiGraphics, (Component)mutableComponent3, this.field_564 + this.field_566 / 2, this.field_565 + this.height - 18);
        }
        field_574.render(guiGraphics, n, n2, f);
        field_578.render(guiGraphics, n, n2, f);
        poseStack.popPose();
        poseStack.popPose();
    }

    @Override
    public void method_534(@NotNull Minecraft minecraft, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, int n, int n2, float f, float f2) {
        BFRendering.rectangleWithDarkShadow(guiGraphics, this.field_564, this.field_565, this.field_566, this.height, BFRendering.translucentBlack());
    }

    private int method_564(int n) {
        int n2 = n;
        int n3 = 0;
        while (n2 > 0) {
            n2 -= 15;
            ++n3;
        }
        return n3;
    }

    @Override
    public boolean onPress(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, double d, double d2, int n) {
        for (BF_120 bF_120 : field_587) {
            Rectangle rectangle = new Rectangle(bF_120.field_554, bF_120.field_556, bF_120.field_557, bF_120.field_558);
            if (!rectangle.contains(d, d2) || n != 1) continue;
            Screen screen = ArmoryDropdownUtils.getScreenFromItem(this.getScreen(), bFClientManager, bF_120.field_548, (int)(d + 2.0), (int)(d2 + 2.0));
            minecraft.setScreen(screen);
            return true;
        }
        return super.onPress(minecraft, bFClientManager, clientPlayerDataHandler, d, d2, n);
    }

    static {
        field_577 = null;
        field_575 = null;
        field_576 = false;
    }
}

