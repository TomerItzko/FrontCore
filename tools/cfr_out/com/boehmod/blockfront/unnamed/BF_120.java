/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.CloudRegistry
 *  com.boehmod.bflib.cloud.common.item.CloudItem
 *  com.boehmod.bflib.cloud.common.item.CloudItemRarity
 *  com.boehmod.bflib.cloud.common.item.CloudItemStack
 *  com.boehmod.bflib.cloud.common.item.types.CloudItemArmour
 *  com.boehmod.bflib.cloud.common.item.types.CloudItemCallingCard
 *  com.boehmod.bflib.cloud.common.item.types.CloudItemSticker
 *  com.boehmod.bflib.common.ColorReferences
 *  com.mojang.blaze3d.vertex.PoseStack
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  it.unimi.dsi.fastutil.objects.ObjectList
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.MultiBufferSource$BufferSource
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.chat.Style
 *  net.minecraft.resources.ResourceLocation
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.bflib.cloud.common.CloudRegistry;
import com.boehmod.bflib.cloud.common.item.CloudItem;
import com.boehmod.bflib.cloud.common.item.CloudItemRarity;
import com.boehmod.bflib.cloud.common.item.CloudItemStack;
import com.boehmod.bflib.cloud.common.item.types.CloudItemArmour;
import com.boehmod.bflib.cloud.common.item.types.CloudItemCallingCard;
import com.boehmod.bflib.cloud.common.item.types.CloudItemSticker;
import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.item.CloudItemRenderer;
import com.boehmod.blockfront.client.render.item.CloudItemRenderers;
import com.boehmod.blockfront.client.render.menu.MenuParticle;
import com.boehmod.blockfront.client.render.menu.particle.ChristmasMenuParticle;
import com.boehmod.blockfront.client.render.menu.particle.HalloweenMenuParticle;
import com.boehmod.blockfront.client.render.menu.particle.ShineMenuParticle;
import com.boehmod.blockfront.client.screen.BFMenuScreen;
import com.boehmod.blockfront.client.settings.BFClientSettings;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.BFStyles;
import com.boehmod.blockfront.util.StringUtils;
import com.boehmod.blockfront.util.armory.ArmoryTooltipUtils;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.awt.Rectangle;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public final class BF_120 {
    private static final ResourceLocation field_546 = BFRes.loc("textures/gui/menu/icons/check_long_white.png");
    private static final ResourceLocation field_547 = BFRes.loc("textures/gui/menu/icons/magnifying_glass.png");
    public static final int field_7028 = 15;
    @NotNull
    public CloudItemStack field_548;
    public int field_554;
    public int field_555;
    public int field_556;
    public int field_557;
    public int field_558;
    public boolean field_550 = false;
    private boolean field_551;
    private boolean field_552;
    @NotNull
    private final Screen field_549;

    public BF_120(@NotNull Screen screen, @NotNull CloudItemStack cloudItemStack, int n, int n2, int n3, int n4, boolean bl, boolean bl2) {
        this.field_549 = screen;
        this.field_548 = cloudItemStack;
        this.field_554 = this.field_555 = n;
        this.field_556 = n2;
        this.field_557 = n3;
        this.field_558 = n4;
        this.field_551 = bl;
        this.field_552 = bl2;
    }

    @NotNull
    public BF_120 method_527(boolean bl) {
        this.field_550 = bl;
        return this;
    }

    public void updateParticles(@NotNull Minecraft minecraft, @NotNull CloudRegistry cloudRegistry) {
        Screen screen;
        CloudItem cloudItem = this.field_548.getCloudItem(cloudRegistry);
        if (cloudItem == null) {
            return;
        }
        CloudItemRarity cloudItemRarity = cloudItem.getRarity();
        MenuParticle menuParticle = null;
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        int n = this.field_557 / 2;
        int n2 = this.field_558 / 2;
        int n3 = -n + threadLocalRandom.nextInt(n * 2);
        int n4 = -n2 + threadLocalRandom.nextInt(n2 * 2);
        int n5 = this.field_554 + this.field_557 / 2;
        int n6 = this.field_556 + this.field_558 / 2;
        String string = cloudItem.getCollection();
        if (cloudItemRarity == CloudItemRarity.DIAMOND || cloudItemRarity == CloudItemRarity.OBSIDIAN) {
            menuParticle = new ShineMenuParticle(n5 + n3, n6 + n4);
        } else if (string != null && string.contains("Halloween")) {
            menuParticle = new HalloweenMenuParticle(n5 + n3, n6 + n4);
        } else if (string != null && string.contains("Christmas")) {
            menuParticle = new ChristmasMenuParticle(n5 + n3, n6 + n4);
        }
        if (menuParticle != null && (screen = minecraft.screen) instanceof BFMenuScreen) {
            BFMenuScreen bFMenuScreen = (BFMenuScreen)screen;
            if (Math.random() <= 0.04) {
                bFMenuScreen.addParticle(minecraft, menuParticle);
            }
        }
    }

    public void method_524(@NotNull Minecraft minecraft, @NotNull GuiGraphics guiGraphics, Font font, int n, int n2, float f) {
        this.method_525(minecraft, guiGraphics, font, false, n, n2, f);
    }

    public void method_525(@NotNull Minecraft minecraft, @NotNull GuiGraphics guiGraphics, Font font, boolean bl, int n, int n2, float f) {
        Object object;
        Optional optional;
        Object object2;
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        CloudRegistry cloudRegistry = bFClientManager.getCloudRegistry();
        int n3 = this.field_557 / 2;
        float f2 = MathUtils.lerpf1(this.field_554, this.field_555, f);
        boolean bl2 = this.field_549 == minecraft.screen && this.method_522(n, n2) && !bl;
        CloudItem cloudItem = this.field_548.getCloudItem(cloudRegistry);
        if (cloudItem == null) {
            return;
        }
        CloudItemRenderer<?> cloudItemRenderer = CloudItemRenderers.getRenderer(cloudItem);
        float f3 = (float)(this.field_548.getUUID().hashCode() + this.field_548.getDisplayName(cloudRegistry).hashCode()) / 100000.0f;
        int n4 = cloudItem.getRarity().getColor();
        PoseStack poseStack = guiGraphics.pose();
        MultiBufferSource.BufferSource bufferSource = guiGraphics.bufferSource();
        this.method_523(poseStack, guiGraphics, n4, (int)f2);
        cloudItemRenderer.method_1749(cloudItem, this.field_548, minecraft, guiGraphics, poseStack, (MultiBufferSource)bufferSource, (int)f2, this.field_556, this.field_557, this.field_558, n, n2, f2, f3, f);
        if (cloudItem instanceof CloudItemArmour) {
            CloudItemArmour cloudItemArmour = (CloudItemArmour)cloudItem;
            ResourceLocation resourceLocation = BFRes.loc("textures/misc/flag_" + cloudItemArmour.getNation().getTag() + ".png");
            BFRendering.texture(poseStack, guiGraphics, resourceLocation, (int)f2 + 2, this.field_556 + 2, 8, 8);
        }
        int n5 = 0;
        for (int i = 0; i < 16; ++i) {
            CloudItem cloudItem2;
            int n6 = this.field_548.getSticker(i);
            if (n6 == -1 || !((cloudItem2 = cloudRegistry.getItem(n6)) instanceof CloudItemSticker)) continue;
            object2 = (CloudItemSticker)cloudItem2;
            cloudItem2 = BFRes.loc("textures/stickers/" + object2.getSuffixForDisplay() + ".png");
            BFRendering.texture(poseStack, guiGraphics, (ResourceLocation)cloudItem2, (int)f2 + 2 + 10 * n5, this.field_556 + 2, 8, 8);
            ++n5;
        }
        Style style = Style.EMPTY.withColor(ChatFormatting.WHITE);
        String string = cloudItem.getName();
        object2 = cloudItem.getRarity() == CloudItemRarity.DEFAULT ? "Default" : cloudItem.getSuffix();
        int n7 = font.width(string);
        int n8 = font.width((String)object2);
        float f4 = n7 <= this.field_557 ? 0.85f : 0.55f;
        float f5 = n8 <= this.field_557 ? 0.7f : 0.5f;
        float f6 = n7 <= this.field_557 ? -1.0f : 0.5f;
        float f7 = n8 <= this.field_557 ? 0.0f : 1.0f;
        string = StringUtils.abbreviate(string, 12);
        MutableComponent mutableComponent = Component.literal((String)string).withStyle(style).withStyle(BFStyles.BOLD);
        if (bl2) {
            mutableComponent.withColor(ColorReferences.COLOR_THEME_YELLOW_SOLID);
        }
        BFRendering.centeredComponent2d(poseStack, font, guiGraphics, (Component)mutableComponent, f2 + (float)n3, (float)(this.field_556 + this.field_558 - 15) + f6, f4);
        if (((String)object2).isEmpty()) {
            object2 = "";
        }
        if ((optional = this.field_548.getNameTag()).isPresent()) {
            object2 = String.valueOf(ChatFormatting.ITALIC) + (String)optional.get();
        }
        if (cloudItem instanceof CloudItemCallingCard | cloudItem instanceof CloudItemArmour) {
            object2 = cloudItem.getSuffix();
        }
        int n9 = ((String)object2).length();
        int n10 = 14;
        if (n9 > 14) {
            object = ((String)object2).substring(0, 14);
            object2 = (String)object + "..";
        }
        object = Component.literal((String)object2);
        if (bl2) {
            object.withColor(ColorReferences.COLOR_THEME_YELLOW_SOLID);
        }
        BFRendering.centeredComponent2dWithShadow(poseStack, font, guiGraphics, (Component)object, (float)((int)f2 + n3), (float)(this.field_556 + this.field_558 - 7) + f7, f5);
        float f8 = f2 + (float)this.field_557 - 10.0f + 0.5f;
        int n11 = this.field_556 + 1;
        int n12 = 8;
        boolean bl3 = true;
        float f9 = 0.0f;
        poseStack.pushPose();
        poseStack.translate(0.0f, 0.0f, 15.0f);
        if (this.field_551) {
            BFRendering.tintedTexture(poseStack, guiGraphics, field_546, f8, (float)n11, 8.0f, 8.0f, 0.0f, 1.0f, n4);
            f9 -= 9.0f;
        }
        if (this.field_552) {
            float f10 = f8 + f9;
            BFRendering.tintedTexture(poseStack, guiGraphics, field_547, f10, (float)n11, 8.0f, 8.0f, 0.0f, 1.0f, n4);
        }
        poseStack.popPose();
        if (bl2) {
            if (this.field_550) {
                this.method_526(guiGraphics, (int)f2);
            }
            int n13 = 120;
            CloudItemStack cloudItemStack = this.field_548;
            ObjectArrayList objectArrayList = new ObjectArrayList();
            ArmoryTooltipUtils.append(bFClientManager.getCloudRegistry(), minecraft, (ClientPlayerDataHandler)bFClientManager.getPlayerDataHandler(), cloudItemStack, (ObjectList<Component>)objectArrayList, font, n13);
            if (BFClientSettings.DEBUG_TOGGLE_ARMORY_UUID.isEnabled()) {
                objectArrayList.add((Object)Component.empty());
                MutableComponent mutableComponent2 = Component.literal((String)cloudItemStack.getUUID().toString()).withStyle(ChatFormatting.GRAY);
                objectArrayList.add((Object)Component.literal((String)"UUID: ").withColor(0xFFFFFF).append((Component)mutableComponent2));
            }
            guiGraphics.renderTooltip(font, (List)objectArrayList, Optional.empty(), n, n2);
        }
    }

    private void method_523(@NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, int n, int n2) {
        BFRendering.rectangle(guiGraphics, n2, this.field_556 + 1, this.field_557, this.field_558 - 2, n, 0.4f);
        BFRendering.rectangle(guiGraphics, n2 + 1, this.field_556 + 1, this.field_557 - 2, this.field_558 - 1, n, 0.4f);
        BFRendering.rectangle(guiGraphics, n2, this.field_556, this.field_557, 1, n, 0.3f);
        BFRendering.rectangleGradient(guiGraphics, n2, this.field_556 + 1, this.field_557, this.field_558 - 16, 0x22000000, n);
        BFRendering.rectangleGradient(guiGraphics, n2, this.field_556 + this.field_558 - 19, this.field_557, 3, ColorReferences.COLOR_BLACK_TRANSPARENT, 0x33000000);
        BFRendering.rectangle(guiGraphics, n2, this.field_556 + this.field_558 - 15, this.field_557, 14, n);
        BFRendering.rectangle(guiGraphics, n2, this.field_556 + this.field_558 - 1, this.field_557, 1, n);
        BFRendering.rectangle(guiGraphics, n2, this.field_556 + this.field_558 - 8, this.field_557, 7, 0, 0.2f);
        BFRendering.rectangleGradient(guiGraphics, n2, this.field_556 + this.field_558 - 8, this.field_557, 3, 0x55000000, ColorReferences.COLOR_BLACK_TRANSPARENT);
        BFRendering.rectangle(guiGraphics, n2 + 1, this.field_556 + this.field_558 - 1, this.field_557 - 2, 1, 0, 0.2f);
        BFRendering.texture(poseStack, guiGraphics, BFMenuScreen.CORNERSHADOW, n2 + 1, this.field_556 + 1, this.field_557 - 2, this.field_558 - 2);
    }

    public void method_526(@NotNull GuiGraphics guiGraphics, int n) {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        int n2 = ColorReferences.COLOR_THEME_YELLOW_SOLID;
        BFRendering.rectangle(guiGraphics, n - 1, this.field_556, 1, this.field_558, n2);
        BFRendering.rectangle(guiGraphics, n + this.field_557, this.field_556, 1, this.field_558, n2);
        BFRendering.rectangle(guiGraphics, n - 1, this.field_556 - 1, this.field_557 + 2, 1, n2);
        BFRendering.rectangle(guiGraphics, n - 1, this.field_556 + this.field_558, this.field_557 + 2, 1, n2);
        poseStack.popPose();
    }

    public boolean method_522(int n, int n2) {
        Rectangle rectangle = new Rectangle(this.field_554, this.field_556, this.field_557, this.field_558);
        return rectangle.contains(n, n2);
    }

    public void method_528(boolean bl) {
        this.field_551 = bl;
    }

    public void method_529(boolean bl) {
        this.field_552 = bl;
    }

    @NotNull
    public CloudItemStack method_521() {
        return this.field_548;
    }
}

