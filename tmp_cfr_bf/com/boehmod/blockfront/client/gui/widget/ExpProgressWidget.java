/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.gui.widget;

import com.boehmod.bflib.cloud.common.player.PlayerRank;
import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.gui.widget.BFWidget;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.StringUtils;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class ExpProgressWidget
extends BFWidget {
    @NotNull
    private static final ResourceLocation field_6629 = BFRes.loc("textures/misc/muzzleflash/whiteflash.png");
    private static final MutableComponent field_6628 = Component.translatable((String)"bf.message.game.match.summary.progress.exp.current");
    private static final MutableComponent field_6642 = Component.translatable((String)"bf.message.game.match.summary.progress.exp.earned");
    private static final MutableComponent field_6647 = Component.translatable((String)"bf.message.game.match.summary.progress.exp.promoted");
    private static final int field_6635 = 40;
    private static final int field_6637 = 140;
    private static final int field_6638 = 60;
    private static final int field_6639 = 40;
    private int field_6640 = 0;
    private boolean field_6625 = false;
    private int field_6641 = 60;
    private boolean field_6626 = false;
    private final int field_6643;
    private final int field_6644;
    private int field_6645;
    private float field_6630;
    private float field_6631;
    private PlayerRank field_6627;
    private int field_6646 = 0;
    private float field_6632 = 0.0f;
    private float field_6633 = 0.0f;
    private float field_6634 = 0.0f;
    private float field_6636 = 0.0f;

    public ExpProgressWidget(int n, int n2, int n3, int n4, @NotNull Screen screen, int n5, int n6) {
        super(n, n2, n3, n4, screen);
        this.field_6643 = n5;
        this.field_6644 = n6;
        this.field_6645 = n5;
        this.field_6627 = PlayerRank.getRankFromEXP((int)n5);
        int n7 = PlayerRank.getTotalRequiredEXPForRank((PlayerRank)this.field_6627);
        int n8 = PlayerRank.getTotalRequiredEXPForRank((PlayerRank)PlayerRank.getNextRank((int)this.field_6627.getID()));
        if (n8 > n7) {
            this.field_6630 = (float)(n5 - n7) / (float)(n8 - n7);
            this.field_6630 = Math.max(0.0f, Math.min(1.0f, this.field_6630));
        } else {
            this.field_6630 = 1.0f;
        }
        this.field_6631 = this.field_6630;
    }

    private void method_5626(@NotNull Minecraft minecraft) {
        this.field_6646 = 40;
        this.field_6632 = 1.0f;
        SoundManager soundManager = minecraft.getSoundManager();
        SimpleSoundInstance simpleSoundInstance = SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.GUI_GAME_SUMMARY_PROMOTED.get()), (float)1.0f, (float)1.0f);
        soundManager.play((SoundInstance)simpleSoundInstance);
    }

    public boolean method_5627() {
        return this.field_6625 && this.field_6646 <= 0 && this.field_6640 <= 0;
    }

    @Override
    public void method_537(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, float f) {
        PlayerRank playerRank;
        super.method_537(minecraft, bFClientManager, clientPlayerDataHandler, f);
        if (this.method_5627()) {
            return;
        }
        this.field_6633 = this.field_6632;
        this.field_6636 = this.field_6634;
        this.field_6631 = this.field_6630;
        this.field_6632 = MathUtils.moveTowards(this.field_6632, 0.0f, 0.05f);
        this.field_6634 = MathUtils.moveTowards(this.field_6634, 0.0f, 0.1f);
        if (!this.field_6626) {
            if (this.field_6641 > 0) {
                --this.field_6641;
                return;
            }
            this.field_6626 = true;
            SoundManager soundManager = minecraft.getSoundManager();
            SimpleSoundInstance simpleSoundInstance = SimpleSoundInstance.forUI((SoundEvent)SoundEvents.EXPERIENCE_ORB_PICKUP, (float)0.8f, (float)1.0f);
            soundManager.play((SoundInstance)simpleSoundInstance);
        }
        --this.field_6646;
        if (this.field_6646 == 0) {
            this.field_6630 = 0.0f;
            this.field_6631 = 0.0f;
        } else if (this.field_6646 > 0) {
            return;
        }
        if (this.field_6625) {
            if (this.field_6640 > 0) {
                --this.field_6640;
            }
            return;
        }
        int n = this.field_6644 - this.field_6643;
        int n2 = Math.max(1, n / 140);
        this.field_6645 = MathUtils.method_5745(this.field_6645, this.field_6644, n2);
        PlayerRank playerRank2 = PlayerRank.getRankFromEXP((int)this.field_6645);
        if (playerRank2.getID() > this.field_6627.getID()) {
            this.field_6627 = playerRank2;
            this.method_5626(minecraft);
            this.field_6630 = 0.0f;
            this.field_6631 = 0.0f;
        } else {
            playerRank = PlayerRank.getNextRank((int)this.field_6627.getID());
            int n3 = PlayerRank.getTotalRequiredEXPForRank((PlayerRank)this.field_6627);
            int n4 = PlayerRank.getTotalRequiredEXPForRank((PlayerRank)playerRank);
            if (n4 > n3) {
                this.field_6630 = (float)(this.field_6645 - n3) / (float)(n4 - n3);
                this.field_6630 = Math.max(0.0f, Math.min(1.0f, this.field_6630));
            } else {
                this.field_6630 = 1.0f;
            }
        }
        if (this.field_6645 >= this.field_6644) {
            this.field_6625 = true;
            this.field_6640 = 40;
            playerRank = minecraft.getSoundManager();
            SimpleSoundInstance simpleSoundInstance = SimpleSoundInstance.forUI((SoundEvent)SoundEvents.EXPERIENCE_ORB_PICKUP, (float)1.0f, (float)1.0f);
            playerRank.play((SoundInstance)simpleSoundInstance);
            this.field_6634 = 1.0f;
        } else {
            playerRank = minecraft.getSoundManager();
            float f2 = 1.0f + this.field_6630 * 0.5f;
            SimpleSoundInstance simpleSoundInstance = SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)SoundEvents.NOTE_BLOCK_HAT.value()), (float)f2, (float)0.25f);
            playerRank.play((SoundInstance)simpleSoundInstance);
        }
    }

    @Override
    public void render(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, ClientPlayerDataHandler clientPlayerDataHandler, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, Font font, @NotNull PlayerCloudData playerCloudData, int n, int n2, float f, float f2) {
        super.render(minecraft, bFClientManager, clientPlayerDataHandler, poseStack, guiGraphics, font, playerCloudData, n, n2, f, f2);
        int n3 = BFRendering.translucentBlack();
        BFRendering.rectangle(guiGraphics, this.field_564 + 1, this.field_565, this.field_566 - 2, 1, n3);
        BFRendering.rectangle(guiGraphics, this.field_564, this.field_565 + 1, this.field_566, this.height - 2, n3);
        BFRendering.rectangle(guiGraphics, this.field_564 + 1, this.field_565 + this.height - 1, this.field_566 - 2, 1, n3);
        this.method_5624(guiGraphics, f2);
        if (this.field_6646 > 0) {
            this.method_5623(poseStack, guiGraphics, font, f2);
        } else {
            this.method_5625(poseStack, guiGraphics, font, f2);
        }
    }

    private void method_5624(@NotNull GuiGraphics guiGraphics, float f) {
        float f2 = MathUtils.lerpf1(this.field_6634, this.field_6636, f);
        if (f2 <= 0.0f) {
            return;
        }
        int n = ColorReferences.COLOR_STOPWATCH_SOLID;
        float f3 = f2 * 0.5f;
        BFRendering.rectangle(guiGraphics, this.field_564 + 1, this.field_565, this.field_566 - 2, 1, n, f3);
        BFRendering.rectangle(guiGraphics, this.field_564, this.field_565 + 1, this.field_566, this.height - 2, n, f3);
        BFRendering.rectangle(guiGraphics, this.field_564 + 1, this.field_565 + this.height - 1, this.field_566 - 2, 1, n, f3);
    }

    private void method_5623(@NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, @NotNull Font font, float f) {
        float f2 = MathUtils.lerpf1(this.field_6632, this.field_6633, f);
        int n = ColorReferences.COLOR_THEME_PRESTIGE_SOLID;
        float f3 = f2 * 0.5f;
        BFRendering.rectangle(guiGraphics, this.field_564 + 1, this.field_565, this.field_566 - 2, 1, n, f3);
        BFRendering.rectangle(guiGraphics, this.field_564, this.field_565 + 1, this.field_566, this.height - 2, n, f3);
        BFRendering.rectangle(guiGraphics, this.field_564 + 1, this.field_565 + this.height - 1, this.field_566 - 2, 1, n, f3);
        MutableComponent mutableComponent = Component.literal((String)StringUtils.makeFancy(field_6647.getString()));
        int n2 = this.field_564 + this.field_566 / 2;
        int n3 = this.field_565 + this.height / 2 - 16;
        BFRendering.centeredComponent2dWithShadow(poseStack, font, guiGraphics, (Component)mutableComponent, n2, n3, 2.0f);
        MutableComponent mutableComponent2 = Component.literal((String)StringUtils.makeFancy(this.field_6627.getTitle())).withColor(n);
        int n4 = this.field_564 + this.field_566 / 2;
        int n5 = n3 + 18;
        BFRendering.centeredComponent2dWithShadow(poseStack, font, guiGraphics, (Component)mutableComponent2, n4, n5, 1.5f);
    }

    private void method_5625(@NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, Font font, float f) {
        Object object;
        Object object2;
        int n;
        MutableComponent mutableComponent;
        String string;
        int n2 = BFRendering.translucentBlack();
        int n3 = 40;
        int n4 = this.field_566 - 80;
        int n5 = 10;
        int n6 = this.field_564 + this.field_566 / 2 - n4 / 2;
        int n7 = this.field_565 + this.height - 10 - 8;
        BFRendering.rectangle(poseStack, guiGraphics, (float)(n6 + 1), (float)n7, (float)(n4 - 2), 1.0f, n2);
        BFRendering.rectangle(poseStack, guiGraphics, (float)n6, (float)(n7 + 1), (float)n4, 8.0f, n2);
        BFRendering.rectangle(poseStack, guiGraphics, (float)(n6 + 1), (float)(n7 + 10 - 1), (float)(n4 - 2), 1.0f, n2);
        float f2 = MathUtils.lerpf1(this.field_6630, this.field_6631, f);
        float f3 = (float)n4 * f2;
        int n8 = 2;
        int n9 = n6 + 2;
        int n10 = n7 + 2;
        float f4 = Mth.clamp((float)(f3 - 4.0f), (float)0.0f, (float)(n4 - 4));
        int n11 = 6;
        BFRendering.rectangle(poseStack, guiGraphics, n9, n10, n4 - 4, 6.0f, ColorReferences.COLOR_STOPWATCH_SOLID, 0.15f);
        BFRendering.rectangle(poseStack, guiGraphics, (float)n9, (float)n10, f4, 6.0f, ColorReferences.COLOR_STOPWATCH_SOLID);
        float f5 = Math.min(f4, 10.0f);
        BFRendering.rectangleGradientWithVertices(poseStack, guiGraphics, (float)n9 + f4 - f5, n10, f5, 6.0f, ColorReferences.COLOR_STOPWATCH_SOLID, ColorReferences.COLOR_WHITE_SOLID);
        float f6 = 6.0f;
        float f7 = (float)n9 + f4 + 3.0f;
        float f8 = (float)(n10 + 6) + 3.0f;
        if (this.field_6645 > 0) {
            String string2 = StringUtils.formatLong(this.field_6645);
            MutableComponent mutableComponent2 = Component.translatable((String)"bf.message.game.match.summary.progress.exp", (Object[])new Object[]{string2});
            BFRendering.component2dWithShadow(poseStack, font, guiGraphics, (Component)mutableComponent2, f7, f8, 16765813);
            string = StringUtils.makeFancy(field_6628.getString());
            mutableComponent = Component.literal((String)string);
            BFRendering.component2dShadow(poseStack, font, guiGraphics, (Component)mutableComponent, f7, f8 + 8.0f);
        }
        if ((n = this.field_6645 - this.field_6643) > 0) {
            float f9 = f8 + 18.0f;
            string = StringUtils.formatLong(n);
            mutableComponent = Component.translatable((String)"bf.message.game.match.summary.progress.exp", (Object[])new Object[]{string});
            BFRendering.component2dWithShadow(poseStack, font, guiGraphics, (Component)mutableComponent, f7, f9, 9633658);
            object2 = StringUtils.makeFancy(field_6642.getString());
            object = Component.literal((String)object2);
            BFRendering.component2dShadow(poseStack, font, guiGraphics, (Component)object, f7, f9 + 8.0f);
        }
        n = 4;
        int n12 = this.height - 8;
        int n13 = this.field_564 + 4;
        int n14 = this.field_565 + 4;
        object2 = BFRes.loc("textures/misc/ranks/" + this.field_6627.getTexture() + ".png");
        BFRendering.texture(poseStack, guiGraphics, object2, n13, n14, n12, n12);
        object = StringUtils.makeFancy(this.field_6627.getTitle());
        MutableComponent mutableComponent3 = Component.literal((String)object);
        int n15 = this.field_564 + this.field_566 / 2;
        int n16 = n7 - 15;
        BFRendering.centeredComponent2dWithShadow(poseStack, font, guiGraphics, (Component)mutableComponent3, n15, n16, 1.5f);
    }

    @Override
    protected void method_534(@NotNull Minecraft minecraft, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, int n, int n2, float f, float f2) {
        super.method_534(minecraft, clientPlayerDataHandler, poseStack, guiGraphics, n, n2, f, f2);
        float f3 = MathUtils.lerpf1(this.field_6632, this.field_6633, f2);
        int n3 = 512;
        int n4 = this.field_564 + this.field_566 / 2;
        int n5 = this.field_565 + this.height / 2;
        BFRendering.centeredTintedTexture(poseStack, guiGraphics, field_6629, n4, n5, 512, 512, 0.0f, f3, ColorReferences.COLOR_THEME_PRESTIGE_SOLID);
    }
}

