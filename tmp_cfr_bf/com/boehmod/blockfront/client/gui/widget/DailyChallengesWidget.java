/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.gui.widget;

import com.boehmod.bflib.cloud.common.player.challenge.Challenge;
import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.gui.widget.BFPopoutWidget;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.player.PlayerChallengeManager;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.BFMenuScreen;
import com.boehmod.blockfront.cloud.client.ClientConnectionManager;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.BFStyles;
import com.boehmod.blockfront.util.ChallengeUtils;
import com.boehmod.blockfront.util.FormatUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class DailyChallengesWidget
extends BFPopoutWidget {
    private static final ResourceLocation field_648 = BFRes.loc("textures/gui/menu/icons/challenges.png");
    private static final ResourceLocation field_649 = BFRes.loc("textures/gui/challenges/shadow.png");
    private static final ResourceLocation field_650 = BFRes.loc("textures/gui/challenges/player.png");
    private static final ResourceLocation field_651 = BFRes.loc("textures/gui/challenges/check.png");
    private static final ResourceLocation field_652 = BFRes.loc("textures/gui/challenges/cross.png");
    private static final Component field_646 = Component.translatable((String)"bf.menu.dailychallenge.title").withStyle(BFStyles.BOLD).withColor(0);
    private static final Component field_647 = Component.translatable((String)"bf.menu.dailychallenge.description");

    public DailyChallengesWidget(int n, int n2, int n3, int n4, @NotNull Screen screen) {
        super(n, n2, n3, n4, screen);
    }

    @Override
    public boolean method_603(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler) {
        boolean bl = ((ClientConnectionManager)bFClientManager.getConnectionManager()).getConnection().getStatus().isConnected();
        PlayerCloudData playerCloudData = clientPlayerDataHandler.getCloudData(minecraft);
        return bl && !playerCloudData.getChallenges().isEmpty();
    }

    @Override
    public void render(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, ClientPlayerDataHandler clientPlayerDataHandler, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, Font font, @NotNull PlayerCloudData playerCloudData, int n, int n2, float f, float f2) {
        ObjectList<String> objectList;
        super.render(minecraft, bFClientManager, clientPlayerDataHandler, poseStack, guiGraphics, font, playerCloudData, n, n2, f, f2);
        float f3 = this.method_604(f2);
        ObjectList objectList2 = playerCloudData.getChallenges();
        boolean bl = ((ClientConnectionManager)bFClientManager.getConnectionManager()).getConnection().getStatus().isConnected();
        boolean bl2 = bl && !objectList2.isEmpty();
        int n3 = 0;
        float f4 = 0.0f;
        int n4 = this.field_566 / 2;
        int n5 = this.height / 2;
        poseStack.pushPose();
        poseStack.translate(0.0f, 0.0f, 400.0f);
        BFRendering.rectangle(guiGraphics, 0, 0, this.getScreen().width, this.getScreen().height, 0, f3 * 0.85f);
        for (Challenge challenge : objectList2) {
            objectList = ChallengeUtils.getComponent(challenge);
            MutableComponent mutableComponent = Component.literal((String)objectList.getString()).withStyle(ChatFormatting.BOLD);
            int n6 = font.width((FormattedText)mutableComponent);
            if ((float)n6 > f4) {
                f4 = n6;
            }
            if (challenge.isCompleted()) continue;
            ++n3;
        }
        f4 *= 0.5f;
        float f5 = (f4 += 13.0f) * f3;
        float f6 = (float)this.field_564 - f5;
        objectList = FormatUtils.parseMarkup(font, field_647.getString(), (int)(f4 * 1.5f));
        int n7 = objectList2.size();
        float f7 = 7.0f * (float)objectList.size() + 10.0f * f3;
        float f8 = 7.0f * (float)n7 + 5.0f + f7;
        if (n7 > 0) {
            f8 += 15.0f;
        }
        int n8 = 15;
        if (bl2) {
            BFRendering.texture(poseStack, guiGraphics, BFMenuScreen.FADE_YELLOW, f6 - 15.0f, (float)(this.field_565 - 15), f5 + 30.0f, f8 + 30.0f, f3);
            BFRendering.centeredTexture(poseStack, guiGraphics, BFMenuScreen.FADE_YELLOW, this.field_564 + n4, this.field_565 + n5, this.field_566 + 15, this.height + 15, 0.0f, 1.0f - f3);
            BFRendering.rectangle(guiGraphics, this.field_564, this.field_565, this.field_566, this.height, ColorReferences.COLOR_THEME_YELLOW_SOLID);
        } else {
            BFRendering.rectangle(guiGraphics, this.field_564, this.field_565, this.field_566, this.height, 0x77888888);
        }
        BFRendering.texture(poseStack, guiGraphics, field_648, this.field_564, this.field_565, this.field_566, this.height);
        if (bl2) {
            this.method_605(bFClientManager, font, guiGraphics, poseStack, playerCloudData, (List<String>)objectList, (int)f4, f8, f6, f5, f3, f7, n3, f, f2);
        }
        poseStack.popPose();
    }

    private void method_605(@NotNull BFClientManager bFClientManager, @NotNull Font font, @NotNull GuiGraphics guiGraphics, PoseStack poseStack, PlayerCloudData playerCloudData, List<String> list, int n, float f, float f2, float f3, float f4, float f5, int n2, float f6, float f7) {
        float f8;
        String string;
        float f9;
        float f10;
        RenderSystem.enableDepthTest();
        BFRendering.enableScissor(guiGraphics, this.field_564 - n, this.field_565, n, (int)f);
        BFRendering.rectangle(poseStack, guiGraphics, f2, (float)this.field_565, f3, f, ColorReferences.COLOR_THEME_YELLOW_SOLID);
        BFRendering.texture(poseStack, guiGraphics, field_649, f2, (float)this.field_565, f3, f);
        float f11 = 1.0f + 0.2f * f4;
        float f12 = f2 + 20.0f + 70.0f * f4 * f11;
        float f13 = (float)this.field_565 + 70.0f;
        poseStack.pushPose();
        BFRendering.centeredTextureScaled(poseStack, guiGraphics, field_650, f12, f13, 120.0f, 175.0f, f11, 0.4f * f4);
        float f14 = f2 + 10.0f;
        BFRendering.component2d(poseStack, font, guiGraphics, field_646, f2 + 10.0f, (float)this.field_565 + 5.0f, 0.75f);
        if (list.size() > 1) {
            PlayerChallengeManager playerChallengeManager = bFClientManager.getChallengeManager();
            float f15 = playerChallengeManager.method_928(f7);
            int n3 = Math.min(5, Math.max(1, playerChallengeManager.method_927()));
            f10 = f3 * f15;
            f9 = 2.0f;
            BFRendering.rectangle(poseStack, guiGraphics, f2, (float)this.field_565 + f - 2.0f, f3, 2.0f, 0x33000000);
            BFRendering.rectangle(poseStack, guiGraphics, f2, (float)this.field_565 + f - 2.0f, f10 * f15, 2.0f, ColorReferences.COLOR_BLACK_SOLID);
            MutableComponent mutableComponent = Component.literal((String)String.valueOf(n3));
            string = Component.translatable((String)"bf.menu.dailychallenge.request", (Object[])new Object[]{mutableComponent}).withColor(0);
            f8 = 50.0f * (1.0f - f4);
            BFRendering.centeredComponent2d(poseStack, font, guiGraphics, (Component)string, f2 + f3 / 2.0f + f8, (float)this.field_565 + f - 2.0f - 11.0f);
        }
        int n4 = 0;
        for (String string2 : list) {
            f10 = (float)this.field_565 + 14.0f + 5.0f * (float)n4;
            MutableComponent mutableComponent = Component.literal((String)string2).withColor(0);
            BFRendering.component2d(poseStack, font, guiGraphics, (Component)mutableComponent, f14, f10, 0.5f);
            ++n4;
        }
        int n5 = 0;
        for (Challenge challenge : playerCloudData.getChallenges()) {
            Component component = ChallengeUtils.getComponent(challenge);
            float f16 = (float)(this.field_565 + 4 + 7 * n5) + f5;
            string = component.getString();
            MutableComponent mutableComponent = Component.literal((String)string).withStyle(ChatFormatting.BOLD).withColor(0);
            BFRendering.texture(poseStack, guiGraphics, challenge.isCompleted() ? field_651 : field_652, f14 - 9.0f, f16 - 2.5f, 8.0f, 8.0f);
            BFRendering.component2d(poseStack, font, guiGraphics, (Component)mutableComponent, f14, f16, 0.5f);
            ++n5;
        }
        guiGraphics.disableScissor();
        if (n2 > 0) {
            int n6 = 8;
            int n7 = 4;
            f9 = f2 - 4.0f;
            float f17 = this.field_565 - 4;
            float f18 = Mth.sin((float)(f6 / 10.0f));
            f8 = 8.0f * (2.75f * f18);
            BFRendering.centeredTexture(poseStack, guiGraphics, BFMenuScreen.FADE_YELLOW, f9 + 4.0f, f17 + 4.0f, f8, f8, 0.0f, f18);
            BFRendering.rectangle(poseStack, guiGraphics, f9, f17, 8.0f, 8.0f, ColorReferences.COLOR_THEME_YELLOW_SOLID);
            BFRendering.rectangle(poseStack, guiGraphics, f9, f17, 8.0f, 8.0f, ColorReferences.COLOR_WHITE_SOLID, f18);
            BFRendering.centeredComponent2d(poseStack, font, guiGraphics, (Component)Component.literal((String)String.valueOf(n2)).withStyle(ChatFormatting.BOLD).withColor(0), f2, (float)(this.field_565 - 2), 0.5f);
        }
        poseStack.popPose();
        RenderSystem.disableDepthTest();
    }
}

