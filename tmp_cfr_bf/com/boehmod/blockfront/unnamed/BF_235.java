/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.menu.MenuRenderer;
import com.boehmod.blockfront.client.screen.BFMenuScreen;
import com.boehmod.blockfront.client.screen.armory.ArmoryInspectScreen;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.CubeMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class BF_235
extends MenuRenderer {
    private static final ResourceLocation field_1405 = BFRes.loc("textures/gui/background/panorama/armory/panorama");
    private static final ResourceLocation field_1406 = BFRes.loc("textures/gui/background/smoke.png");
    private static final ResourceLocation field_1407 = BFRes.loc("textures/gui/background/smoke2.png");
    private static final CubeMap field_1404 = new CubeMap(field_1405);
    private static float field_1408 = 0.0f;
    private static float field_1409;
    private static float field_1410;
    private static float field_1411;

    @Override
    public void renderBackground(@NotNull BFClientManager manager, @NotNull ClientPlayerDataHandler dataHandler, @NotNull Minecraft minecraft, @NotNull BFMenuScreen screen, @NotNull PoseStack poseStack, @NotNull Font font, @NotNull GuiGraphics graphics, int width, int height, int mouseX, int mouseY, float renderTime, float delta) {
        if (screen instanceof ArmoryInspectScreen) {
            poseStack.pushPose();
            RenderSystem.disableDepthTest();
            field_1404.render(minecraft, -ArmoryInspectScreen.field_949, -ArmoryInspectScreen.field_950, 1.0f);
            RenderSystem.disableDepthTest();
            poseStack.popPose();
            BFRendering.backgroundBlur(minecraft, delta);
        }
        if (minecraft.level == null) {
            float f = 1.6f;
            float f2 = 0.1f;
            float f3 = MathUtils.lerpf1(field_1408, field_1409, delta);
            float f4 = Mth.sin((float)(renderTime / 100.0f));
            float f5 = Mth.sin((float)(renderTime / 120.0f));
            float f6 = MathUtils.lerpf1(field_1410, field_1411, delta);
            poseStack.pushPose();
            poseStack.scale(1.6f, 1.6f, 1.6f);
            float f7 = 20.0f * f4;
            BFRendering.texture(poseStack, graphics, field_1406, f3, f7, (float)width, (float)height, 0.1f);
            BFRendering.texture(poseStack, graphics, field_1406, f3 + (float)width, f7, (float)width, (float)height, 0.0f, 0.1f);
            f7 = 20.0f * f5;
            BFRendering.texture(poseStack, graphics, field_1407, f6, f7, (float)width, (float)height, 0.0f, 0.1f);
            BFRendering.texture(poseStack, graphics, field_1407, f6 - (float)width, f7, (float)width, (float)height, 0.0f, 0.1f);
            poseStack.popPose();
            float f8 = manager.getMatchMaking().method_1912(delta);
            float f9 = 0.15f * f8;
            BFRendering.rectangle(graphics, 0, 0, width, height, ColorReferences.COLOR_TEAM_ALLIES_SOLID, f9);
            BFRendering.texture(poseStack, graphics, ArmoryInspectScreen.SHADOWEFFECT_STRONG, 0, 0, width, height, 0.5f);
        }
    }

    @Override
    public void renderBelow(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, PlayerCloudData cloudData, @NotNull BFMenuScreen screen, @NotNull GuiGraphics graphics, @NotNull PoseStack poseStack, @NotNull Font font, int width, int height, int mouseX, int mouseY, float renderTime, float delta) {
    }

    @Override
    public void renderAbove(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull BFMenuScreen screen, @NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull Font font, int width, int height, int mouseX, int mouseY, float renderTime, float delta) {
    }

    @Override
    public void tick(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull BFMenuScreen screen, float renderTime) {
        if (field_1408 > (float)(-screen.width)) {
            field_1409 = field_1408;
            field_1408 -= 1.0f;
        } else {
            field_1409 = 0.0f;
            field_1408 = 0.0f;
        }
        if (field_1410 < (float)screen.width) {
            field_1411 = field_1410;
            field_1410 += 0.4f;
        } else {
            field_1411 = 0.0f;
            field_1410 = 0.0f;
        }
    }

    static {
        field_1410 = 0.0f;
    }
}

