/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.common.ColorReferences
 *  com.mojang.blaze3d.platform.InputConstants
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.PoseStack
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  it.unimi.dsi.fastutil.objects.ObjectList
 *  javax.annotation.OverridingMethodsMustInvokeSuper
 *  net.minecraft.Util
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.AbstractWidget
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.resources.language.I18n
 *  net.minecraft.client.resources.sounds.SimpleSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.packs.resources.ResourceManager
 *  net.minecraft.sounds.SoundEvent
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.screen;

import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.gui.widget.BFButton;
import com.boehmod.blockfront.client.gui.widget.BFWidget;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.menu.MenuParticle;
import com.boehmod.blockfront.client.render.menu.MenuRenderer;
import com.boehmod.blockfront.client.render.menu.ParticleMenuRenderer;
import com.boehmod.blockfront.client.screen.BFScreen;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.unnamed.BF_173;
import com.boehmod.blockfront.unnamed.BF_174;
import com.boehmod.blockfront.unnamed.BF_175;
import com.boehmod.blockfront.unnamed.BF_176;
import com.boehmod.blockfront.unnamed.BF_233;
import com.boehmod.blockfront.unnamed.BF_235;
import com.boehmod.blockfront.util.BFLog;
import com.boehmod.blockfront.util.BFRes;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.NotNull;

public class BFMenuScreen
extends BFScreen {
    public static final ResourceLocation RETURN_ICON = BFRes.loc("textures/gui/menu/icons/return.png");
    public static final ResourceLocation LOGO = BFRes.loc("textures/gui/logo/blockfront.png");
    public static final ResourceLocation BACKSHADOW = BFRes.loc("textures/gui/backshadow.png");
    public static final ResourceLocation FADE = BFRes.loc("textures/gui/effects/fade.png");
    public static final ResourceLocation FADE_YELLOW = BFRes.loc("textures/gui/effects/fade_yellow.png");
    public static final ResourceLocation STARBURST = BFRes.loc("textures/gui/effects/starburst.png");
    public static final ResourceLocation SHADOWEFFECT_STRONG = BFRes.loc("textures/gui/shadoweffect_strong.png");
    public static final ResourceLocation SHADOWEFFECT = BFRes.loc("textures/gui/shadoweffect.png");
    public static final ResourceLocation BG_US = BFRes.loc("textures/misc/ranks/bg_us.png");
    public static final ResourceLocation CREDITS_LINE = BFRes.loc("textures/gui/credits/line.png");
    public static final ResourceLocation TROPHY = BFRes.loc("textures/gui/trophy.png");
    public static final ResourceLocation FLARE_WHITE = BFRes.loc("textures/gui/menu/flare_white.png");
    public static final ResourceLocation BACKSHADOW_WHITE = BFRes.loc("textures/gui/backshadow_white.png");
    public static final ResourceLocation CORNERSHADOW = BFRes.loc("textures/gui/cornershadow.png");
    @NotNull
    public static final ObjectList<MenuRenderer> RENDERERS = new ObjectArrayList();
    @NotNull
    protected static final ObjectList<MenuParticle> particles = new ObjectArrayList();
    @NotNull
    private static final ResourceLocation SPLASHES = BFRes.loc("texts/splashes.txt");
    private static final String field_1067 = I18n.get((String)"bf.cloud.popup.title.error", (Object[])new Object[0]);
    @NotNull
    public static String splashText = BFMenuScreen.getSplashText();
    protected final List<BFWidget> field_1047 = new ObjectArrayList();
    @NotNull
    public Component field_1048;

    public BFMenuScreen(@NotNull Component component, @NotNull Component component2) {
        super(component);
        this.field_1048 = component2;
    }

    public BFMenuScreen(@NotNull Component component) {
        this(component, (Component)Component.empty());
    }

    public static void openUrl(@NotNull Minecraft minecraft, @NotNull String url) {
        SimpleSoundInstance simpleSoundInstance = SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.GUI_OPENURL.get()), (float)1.0f);
        minecraft.getSoundManager().play((SoundInstance)simpleSoundInstance);
        Util.getPlatform().openUri(url);
    }

    public static boolean method_766(@NotNull Minecraft minecraft) {
        return InputConstants.isKeyDown((long)minecraft.getWindow().getWindow(), (int)257);
    }

    public static boolean method_769(@NotNull Minecraft minecraft) {
        return InputConstants.isKeyDown((long)minecraft.getWindow().getWindow(), (int)256);
    }

    public static boolean method_771(@NotNull Minecraft minecraft) {
        return InputConstants.isKeyDown((long)minecraft.getWindow().getWindow(), (int)259);
    }

    @NotNull
    public static String getSplashText() {
        try {
            ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
            BufferedReader bufferedReader = resourceManager.openAsReader(SPLASHES);
            ObjectArrayList objectArrayList = new ObjectArrayList(bufferedReader.lines().map(String::trim).filter(string -> !string.startsWith("#")).toList());
            return (String)objectArrayList.get(ThreadLocalRandom.current().nextInt(objectArrayList.size()));
        }
        catch (IOException iOException) {
            BFLog.logThrowable("Error while updating splash text.", iOException, new Object[0]);
            return field_1067;
        }
    }

    public boolean isPauseScreen() {
        return false;
    }

    public void addParticle(@NotNull Minecraft minecraft, @NotNull MenuParticle particle) {
        particles.add((Object)particle);
        particle.init(minecraft);
    }

    public void tick() {
        super.tick();
        float f = BFRendering.getRenderTime();
        for (BFWidget bFWidget : this.field_1047) {
            bFWidget.method_537(this.minecraft, this.manager, this.playerDataHandler, f);
        }
        for (MenuRenderer menuRenderer : RENDERERS) {
            menuRenderer.tick(this.minecraft, this.manager, this, f);
        }
    }

    public void render(@NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
        PoseStack poseStack = guiGraphics.pose();
        float f2 = BFRendering.getRenderTime();
        ClientPlayerDataHandler clientPlayerDataHandler = (ClientPlayerDataHandler)this.manager.getPlayerDataHandler();
        ClientLevel clientLevel = this.minecraft.level;
        PlayerCloudData playerCloudData = clientPlayerDataHandler.getCloudData(this.minecraft);
        RenderSystem.disableDepthTest();
        poseStack.pushPose();
        poseStack.pushPose();
        poseStack.translate(0.0f, 0.0f, -100.0f);
        this.renderBackground(guiGraphics, n, n2, f);
        poseStack.popPose();
        super.render(guiGraphics, n, n2, f);
        if (clientLevel == null) {
            for (MenuRenderer menuRenderer : RENDERERS) {
                menuRenderer.renderBelow(this.minecraft, this.manager, playerCloudData, this, guiGraphics, poseStack, this.font, this.width, this.height, n, n2, f2, f);
            }
        }
        this.method_768(this.minecraft, clientPlayerDataHandler, poseStack, guiGraphics, playerCloudData, n, n2, f2, f);
        if (clientLevel == null) {
            for (MenuRenderer menuRenderer : RENDERERS) {
                menuRenderer.renderAbove(this.minecraft, this.manager, this, poseStack, guiGraphics, this.font, this.width, this.height, n, n2, f2, f);
            }
        }
        for (MenuRenderer menuRenderer : this.renderables) {
            if (!(menuRenderer instanceof BFButton)) continue;
            BFButton bFButton = (BFButton)((Object)menuRenderer);
            bFButton.method_379(this.minecraft, poseStack, this.font, guiGraphics, n, n2, f2);
        }
        poseStack.popPose();
    }

    public void method_768(@NotNull Minecraft minecraft, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, @NotNull PlayerCloudData playerCloudData, int n, int n2, float f, float f2) {
        for (BFWidget bFWidget : this.field_1047) {
            bFWidget.render(minecraft, this.manager, clientPlayerDataHandler, poseStack, guiGraphics, this.font, playerCloudData, n, n2, f, f2);
        }
        for (BFWidget bFWidget : this.field_1047) {
            bFWidget.method_541(minecraft, this.manager, clientPlayerDataHandler, this.font, guiGraphics, n, n2, f2);
        }
    }

    public void renderBackground(@NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
        ClientLevel clientLevel = this.minecraft.level;
        PoseStack poseStack = guiGraphics.pose();
        float f2 = BFRendering.getRenderTime();
        if (clientLevel != null && !this.minecraft.hasSingleplayerServer()) {
            BFRendering.backgroundBlur(this.minecraft, f);
            BFRendering.rectangle(guiGraphics, 0, 0, this.width, this.height, BFRendering.translucentBlack());
            return;
        }
        if (clientLevel == null) {
            BFRendering.rectangle(guiGraphics, 0, 0, this.width, this.height, ColorReferences.COLOR_BLACK_SOLID);
        }
        poseStack.pushPose();
        for (MenuRenderer menuRenderer : RENDERERS) {
            menuRenderer.renderBackground(this.manager, this.playerDataHandler, this.minecraft, this, poseStack, this.font, guiGraphics, this.width, this.height, n, n2, f2, f);
        }
        poseStack.popPose();
    }

    @NotNull
    public List<MenuParticle> getMenuParticles() {
        return particles;
    }

    public void init() {
        super.init();
        BFMenuScreen.getSplashText();
        this.method_758();
        this.method_774();
        for (BFWidget bFWidget : this.field_1047) {
            bFWidget.method_535(this.minecraft, this.manager, this.playerDataHandler);
        }
    }

    public boolean mouseClicked(double d, double d2, int n) {
        try {
            return this.field_1047.stream().anyMatch(bFWidget -> bFWidget.onPress(this.minecraft, this.manager, this.playerDataHandler, d, d2, n)) || super.mouseClicked(d, d2, n);
        }
        catch (ConcurrentModificationException concurrentModificationException) {
            return false;
        }
    }

    public boolean mouseReleased(double d, double d2, int n) {
        boolean bl = false;
        for (BFWidget bFWidget : this.field_1047) {
            if (!bFWidget.mouseReleased(d, d2, n)) continue;
            bl = true;
        }
        if (bl) {
            return true;
        }
        return super.mouseReleased(d, d2, n);
    }

    public boolean mouseScrolled(double d, double d2, double d3, double d4) {
        for (BFWidget bFWidget : this.field_1047) {
            if (!bFWidget.mouseScrolled(d, d2, d3, d4)) continue;
            return true;
        }
        return super.mouseScrolled(d, d2, d3, d4);
    }

    public boolean keyPressed(int n, int n2, int n3) {
        for (BFWidget bFWidget : this.field_1047) {
            if (!bFWidget.keyPressed(n, n2, n3)) continue;
            return true;
        }
        return super.keyPressed(n, n2, n3);
    }

    public void method_758() {
    }

    @OverridingMethodsMustInvokeSuper
    public void method_774() {
        this.method_759();
    }

    public void method_759() {
        this.field_1047.clear();
    }

    @OverridingMethodsMustInvokeSuper
    public void method_764(BFWidget bFWidget) {
        this.field_1047.add(bFWidget);
    }

    public boolean method_761(@NotNull BFButton bFButton) {
        return this.renderables.stream().anyMatch(renderable -> renderable == bFButton) || this.field_1047.stream().anyMatch(bFWidget -> bFWidget.method_549((AbstractWidget)bFButton));
    }

    static {
        RENDERERS.add((Object)new BF_176());
        RENDERERS.add((Object)new BF_235());
        RENDERERS.add((Object)new BF_173());
        RENDERERS.add((Object)new ParticleMenuRenderer());
        RENDERERS.add((Object)new BF_175());
        RENDERERS.add((Object)new BF_174());
        RENDERERS.add((Object)new BF_233());
    }
}

