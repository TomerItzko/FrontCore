/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.player.PlayerRank
 *  com.boehmod.bflib.common.ColorReferences
 *  com.mojang.blaze3d.vertex.PoseStack
 *  javax.annotation.Nullable
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.Renderable
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.resources.sounds.SimpleSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.client.sounds.SoundManager
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.util.Mth
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.screen.title.overlay;

import com.boehmod.bflib.cloud.common.player.PlayerRank;
import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.gui.widget.BFButton;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.BFMenuScreen;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.BFStyles;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class RankupScreen
extends BFMenuScreen {
    public static final int field_1080 = -3223881;
    public static final int field_1081 = -601527;
    @NotNull
    private static final Component field_1073 = Component.translatable((String)"bf.rankup.title").withStyle(BFStyles.BOLD);
    @NotNull
    private static final Component field_1074 = Component.translatable((String)"bf.menu.button.close");
    public static final int field_1082 = 100;
    public static final int field_1083 = 15;
    public static boolean newRank = false;
    @NotNull
    private final Screen field_1086;
    @Nullable
    private BFButton field_1085;
    private int field_1084 = 40;
    private float field_1075;
    private float field_1076 = 0.0f;
    private float field_1077 = 0.0f;
    private float field_1078 = 0.0f;
    private float field_1079 = 0.0f;

    public RankupScreen(@NotNull Screen screen) {
        super(field_1073, field_1073);
        this.field_1086 = screen;
        SoundManager soundManager = this.minecraft.getSoundManager();
        soundManager.play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.GUI_BOOTCAMP_COMPLETE.get()), (float)1.0f, (float)1.0f));
    }

    public void method_775() {
        this.method_776();
        this.minecraft.setScreen(this.field_1086);
    }

    private void method_776() {
        SoundManager soundManager = this.minecraft.getSoundManager();
        soundManager.play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.GUI_SIDEMENU_CLOSE.get()), (float)1.25f, (float)1.0f));
    }

    public void removed() {
        super.removed();
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.field_1084-- <= 0 && this.field_1085 != null) {
            this.field_1085.visible = true;
        }
        this.field_1076 = this.field_1075;
        this.field_1075 = Mth.lerp((float)0.1f, (float)this.field_1075, (float)1.0f);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
        this.field_1086.render(guiGraphics, n, n2, f);
        PoseStack poseStack = guiGraphics.pose();
        float f2 = BFRendering.getRenderTime();
        poseStack.pushPose();
        poseStack.translate(0.0f, 0.0f, 400.0f);
        float f3 = 1.0f + Mth.sin((float)(f2 / 40.0f)) / 5.0f;
        this.field_1077 = Mth.lerp((float)0.1f, (float)this.field_1077, (float)f3);
        this.field_1078 = Mth.lerp((float)0.08f, (float)this.field_1078, (float)f3);
        this.field_1079 = Mth.lerp((float)0.06f, (float)this.field_1079, (float)f3);
        float f4 = MathUtils.lerpf1(this.field_1075, this.field_1076, f);
        PlayerCloudData playerCloudData = this.playerDataHandler.getCloudData(this.minecraft);
        PlayerRank playerRank = playerCloudData.getRank();
        int n3 = this.height / 2 - 20;
        int n4 = this.width / 2;
        float f5 = (float)this.height / 2.0f * f4;
        BFRendering.rectangle(guiGraphics, 0, 0, this.width, this.height, 0, 0.75f * f4);
        BFRendering.texture(poseStack, guiGraphics, SHADOWEFFECT_STRONG, 0, 0, this.width, this.height, f4);
        float f6 = 80.0f * this.field_1079;
        float f7 = f5 + 10.0f;
        BFRendering.centeredTintedTexture(poseStack, guiGraphics, FADE, (float)n4, f7, 320.0f, 320.0f, f2 / 3.0f, this.field_1077 * this.field_1075, -3223881);
        BFRendering.centeredTintedTexture(poseStack, guiGraphics, STARBURST, (float)n4, f7, 250.0f, 250.0f, -(f2 / 4.0f), this.field_1077 / 2.0f * this.field_1075, -3223881);
        BFRendering.centeredTexture(poseStack, guiGraphics, BACKSHADOW, (float)n4, f7, 150.0f, 150.0f, f2 / 3.0f, this.field_1077 * this.field_1075);
        BFRendering.centeredTexture(poseStack, guiGraphics, BACKSHADOW, (float)n4, f7, 150.0f, 150.0f, f2 / 3.0f, this.field_1077 * this.field_1075);
        Object object = BFRes.loc("textures/misc/ranks/" + playerRank.getTexture() + ".png");
        float f8 = Mth.sin((float)(f2 / 35.0f));
        float f9 = Mth.sin((float)((f2 + 30.0f) / 35.0f));
        float f10 = 1.0f + Mth.sin((float)(f2 / 55.0f));
        float f11 = 5.0f * f8;
        float f12 = 5.0f * f9;
        BFRendering.centeredTexture(poseStack, guiGraphics, BG_US, (float)n4, f7, f6 += 10.0f * f10, f6, f11, this.field_1079 * this.field_1075);
        BFRendering.centeredTexture(poseStack, guiGraphics, (ResourceLocation)object, (float)n4, f7, f6, f6, f12, this.field_1079 * this.field_1075);
        if (this.field_1084 <= 0) {
            object = Component.literal((String)playerRank.getTitle()).withColor(-601527);
            MutableComponent mutableComponent = Component.translatable((String)"bf.rankup.message", (Object[])new Object[]{object}).withColor(-3223881).withStyle(ChatFormatting.BOLD);
            BFRendering.centeredComponent2d(poseStack, this.font, guiGraphics, field_1073, n4, n3 - 75, 2.5f);
            BFRendering.centeredComponent2d(poseStack, this.font, guiGraphics, (Component)mutableComponent, n4, n3 - 50);
        }
        for (Renderable renderable : this.renderables) {
            if (!(renderable instanceof BFButton)) continue;
            BFButton bFButton = (BFButton)renderable;
            bFButton.render(guiGraphics, n, n2, f);
        }
        poseStack.popPose();
    }

    @Override
    public void method_758() {
        int n = this.width / 2;
        this.field_1085 = (BFButton)this.addRenderableWidget((GuiEventListener)new BFButton(n - 50, this.height - 30, 100, 15, field_1074, button -> this.method_775()).displayType(BFButton.DisplayType.SHADOW).method_395(ColorReferences.COLOR_TEAM_ALLIES_SOLID).method_397(ColorReferences.COLOR_TEAM_ALLIES_HOVERED_SOLID).textStyle(BFButton.TextStyle.SHADOW).method_368(0.0f, 1.5f));
        this.field_1085.visible = false;
    }
}

