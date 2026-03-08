/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.screens.Screen
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.unnamed.BF_208;
import com.boehmod.blockfront.unnamed.BF_218;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.NotNull;

public final class BF_209<S extends BF_218>
extends BF_208<S> {
    public BF_209(int n, int n2, int n3, int n4, @NotNull Screen screen) {
        super(n, n2, n3, n4, screen);
    }

    @Override
    public void method_535(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, ClientPlayerDataHandler clientPlayerDataHandler) {
        super.method_535(minecraft, bFClientManager, clientPlayerDataHandler);
        this.field_1313 = this.field_1310 = this.field_564 + this.field_566 - 10;
        this.method_947();
    }

    @Override
    int method_944() {
        return this.field_565 + 4;
    }

    @Override
    public void method_947() {
        int n = 0;
        int n2 = this.field_1302.size();
        for (int i = 0; i < n2; ++i) {
            BF_218 bF_218 = (BF_218)this.field_1302.get(i);
            bF_218.method_980(this, i, this.field_564, this.field_565 + n + bF_218.method_974());
            n += bF_218.height() + bF_218.method_975() + bF_218.method_974();
        }
        this.field_1305 = n;
        this.field_1317 = this.field_1305 > this.height ? this.field_565 + this.height - 4 - 40 : this.field_1316;
        if (this.field_1312 > this.field_1317) {
            this.field_1311 = this.field_1312 = this.field_1317;
        }
    }

    private void method_964(int n) {
        this.field_1312 = this.method_957(n);
    }

    @Override
    protected int method_945() {
        return this.field_1306 == -1 ? this.field_1312 : this.field_1306;
    }

    @Override
    public boolean onPress(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, double d, double d2, int n) {
        if (BFRendering.isPointInRectangle(this.field_1310, this.field_1312, 6.0, 40.0, d, d2)) {
            this.field_1306 = -1;
            this.field_1315 = (int)d2 - this.field_1312;
            this.field_1304 = true;
            return true;
        }
        for (BF_218 bF_218 : this.field_1302) {
            if (!bF_218.method_992()) continue;
            if (bF_218.method_990()) {
                this.method_958(bF_218);
            }
            bF_218.method_982(minecraft, bFClientManager, clientPlayerDataHandler, (int)d, (int)d2, n);
            return true;
        }
        return false;
    }

    @Override
    public void method_537(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, float f) {
        super.method_537(minecraft, bFClientManager, clientPlayerDataHandler, f);
        if (this.field_1304) {
            int n = (int)(minecraft.mouseHandler.ypos() * (double)minecraft.getWindow().getGuiScaledHeight() / (double)minecraft.getWindow().getScreenHeight());
            this.method_964(n - this.field_1315);
        } else if (this.field_1306 != -1) {
            double d = (double)(this.field_1306 - this.field_1312) / 6.0;
            this.method_964((int)((double)this.field_1312 + (d <= 0.0 ? Math.floor(d) : Math.ceil(d))));
            if (this.field_1312 == this.field_1306) {
                this.field_1306 = -1;
            }
        }
    }

    @Override
    public void render(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, Font font, @NotNull PlayerCloudData playerCloudData, int n, int n2, float f, float f2) {
        super.render(minecraft, bFClientManager, clientPlayerDataHandler, poseStack, guiGraphics, font, playerCloudData, n, n2, f, f2);
        BFRendering.enableScissor(guiGraphics, this.field_564, this.field_565, this.field_566 - 15, this.height);
        float f3 = this.method_963(f2);
        poseStack.pushPose();
        poseStack.translate(0.0f, -f3, 0.0f);
        this.method_953(poseStack, minecraft, bFClientManager, clientPlayerDataHandler, guiGraphics, font, n, n2, f3, f, f2);
        poseStack.popPose();
        guiGraphics.disableScissor();
        this.method_959(poseStack, minecraft, bFClientManager, clientPlayerDataHandler, guiGraphics, font, n, n2, f3, f, f2);
    }

    @Override
    public void method_956(GuiGraphics guiGraphics, float f, int n, int n2) {
        PoseStack poseStack = guiGraphics.pose();
        if (this.field_1305 > this.height) {
            float f2 = MathUtils.lerpf1(this.field_1310, this.field_1313, f);
            float f3 = MathUtils.lerpf1(this.field_1312, this.field_1311, f);
            boolean bl = this.field_1304 || BFRendering.isPointInRectangle(this.field_1310, this.field_1312, 6.0, 40.0, n, n2);
            float f4 = bl ? 0.5f : 0.1f;
            poseStack.pushPose();
            BFRendering.rectangle(poseStack, guiGraphics, f2 - 2.0f, this.field_565 + 2, 10.0f, this.height - 4, 0, 0.5f);
            BFRendering.rectangle(poseStack, guiGraphics, f2, f3, 6.0f, 40.0f, 0xFFFFFF, f4);
            poseStack.popPose();
        }
    }

    @Override
    public boolean method_952(@NotNull BF_218 bF_218, int n, int n2) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.screen != this.getScreen()) {
            return false;
        }
        if (!BFRendering.isPointInRectangle(this.field_564, this.field_565, this.field_566, this.height, n, n2)) {
            return false;
        }
        return BFRendering.isPointInRectangle(bF_218.field_1357, bF_218.field_1358, bF_218.method_989(), bF_218.height(), n, (float)n2 + this.method_963(1.0f));
    }

    @Override
    public float method_963(float f) {
        if (this.field_1305 <= this.height) {
            return 0.0f;
        }
        int n = this.field_1305 - this.height;
        float f2 = MathUtils.lerpf1(this.field_1312, this.field_1311, f);
        if (this.field_1317 == this.field_1316) {
            return 0.0f;
        }
        return (int)((f2 - (float)this.field_1316) / (float)(this.field_1317 - this.field_1316) * (float)n);
    }
}

