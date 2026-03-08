/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.bflib.cloud.common.player.Punishment;
import com.boehmod.bflib.cloud.common.player.PunishmentType;
import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.bflib.common.util.ModLibraryMathHelper;
import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.gui.widget.BFWidget;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.util.StringUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

public final class BF_130
extends BFWidget {
    private static final Component field_639 = Component.translatable((String)"bf.menu.text.punished.title").withStyle(ChatFormatting.RED);
    @NotNull
    private final List<MutableComponent> field_641 = new ObjectArrayList();

    public BF_130(int n, int n2, int n3, int n4, Screen screen) {
        super(n, n2, n3, n4, screen);
    }

    @Override
    public void method_535(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, ClientPlayerDataHandler clientPlayerDataHandler) {
        super.method_535(minecraft, bFClientManager, clientPlayerDataHandler);
        this.method_602(minecraft, clientPlayerDataHandler);
    }

    @Override
    public void render(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, ClientPlayerDataHandler clientPlayerDataHandler, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, Font font, @NotNull PlayerCloudData playerCloudData, int n, int n2, float f, float f2) {
        super.render(minecraft, bFClientManager, clientPlayerDataHandler, poseStack, guiGraphics, font, playerCloudData, n, n2, f, f2);
        int n3 = this.field_566 / 2;
        BFRendering.rectangleWithDarkShadow(guiGraphics, this.field_564, this.field_565, this.field_566, this.height + 2, BFRendering.translucentBlack());
        if (playerCloudData.hasActivePunishment(PunishmentType.BAN_MM) || playerCloudData.hasActivePunishment(PunishmentType.BAN_CLOUD)) {
            this.method_601(poseStack, guiGraphics, font, playerCloudData, n3);
        } else {
            this.method_600(poseStack, guiGraphics, font, n3);
        }
    }

    private void method_600(@NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, @NotNull Font font, int n) {
        float f = 14.2f;
        int n2 = this.field_565 + 1;
        int n3 = 0;
        for (MutableComponent mutableComponent : this.field_641) {
            float f2 = (float)n2 + (float)n3 * 15.2f;
            int n4 = n3 % 2 == 0 ? 0x15FFFFFF : 0x8FFFFFF;
            BFRendering.rectangle(poseStack, guiGraphics, (float)(this.field_564 + 1), f2, (float)(this.field_566 - 2), 14.2f, n4);
            BFRendering.centeredComponent2d(poseStack, font, guiGraphics, (Component)mutableComponent, this.field_564 + n, f2 + 3.0f);
            ++n3;
        }
    }

    private void method_601(@NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, @NotNull Font font, @NotNull PlayerCloudData playerCloudData, int n) {
        BFRendering.enableScissor(guiGraphics, this.field_564, this.field_565, this.field_566, this.height);
        BFRendering.centeredString(font, guiGraphics, field_639, this.field_564 + n, this.field_565 + 5);
        BFRendering.rectangle(guiGraphics, this.field_564 + 15, this.field_565 + 17, this.field_566 - 30, 1, ColorReferences.COLOR_WHITE_SOLID);
        guiGraphics.disableScissor();
        int n2 = this.field_564 + 5;
        int n3 = this.field_565 + 22;
        int n4 = 0;
        for (Punishment punishment : playerCloudData.getActivePunishments()) {
            int n5 = n3 + 18 * n4;
            BFRendering.component2d(poseStack, font, guiGraphics, (Component)Component.translatable((String)punishment.getType().getTranslatableName()).withStyle(ChatFormatting.RED), n2, n5, 0.5f);
            BFRendering.component2d(poseStack, font, guiGraphics, (Component)Component.literal((String)punishment.getReason()), n2, n5 + 5, 0.5f);
            MutableComponent mutableComponent = Component.literal((String)punishment.getTimeRemainingString()).withStyle(ChatFormatting.GRAY);
            MutableComponent mutableComponent2 = Component.translatable((String)"bf.message.profile.base.time.left", (Object[])new Object[]{mutableComponent});
            BFRendering.component2d(poseStack, font, guiGraphics, (Component)mutableComponent2, n2, n5 + 10, 0.5f);
            ++n4;
        }
    }

    public void method_602(@NotNull Minecraft minecraft, @NotNull ClientPlayerDataHandler clientPlayerDataHandler) {
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        PlayerCloudData playerCloudData = clientPlayerDataHandler.getCloudData(minecraft);
        this.field_641.clear();
        int n = playerCloudData.getDeaths();
        int n2 = playerCloudData.getKills();
        double d = (float)n2 / (n <= 0 ? 1.0f : (float)n);
        double d2 = ModLibraryMathHelper.round((double)d, (int)1);
        MutableComponent mutableComponent = BlockFront.VERSION_COMPONENT.copy().withColor(0xFFFFFF);
        MutableComponent mutableComponent2 = BlockFront.DISPLAY_NAME_COMPONENT.copy().withStyle(ChatFormatting.GRAY);
        MutableComponent mutableComponent3 = mutableComponent2.append(" ").append((Component)mutableComponent);
        this.field_641.add(mutableComponent3);
        String string = ModLibraryMathHelper.round((double)(playerCloudData.getMinutesPlayed() / 60.0), (int)1) + "h";
        MutableComponent mutableComponent4 = Component.literal((String)string).withStyle(ChatFormatting.DARK_GRAY);
        MutableComponent mutableComponent5 = Component.translatable((String)"bf.message.profile.details.time", (Object[])new Object[]{mutableComponent4}).withStyle(ChatFormatting.GRAY);
        this.field_641.add(mutableComponent5);
        MutableComponent mutableComponent6 = Component.literal((String)StringUtils.formatLong(playerCloudData.getTotalGames())).withStyle(ChatFormatting.DARK_GRAY);
        MutableComponent mutableComponent7 = Component.translatable((String)"bf.message.profile.details.games", (Object[])new Object[]{mutableComponent6}).withStyle(ChatFormatting.GRAY);
        this.field_641.add(mutableComponent7);
        MutableComponent mutableComponent8 = Component.literal((String)String.valueOf(d2)).withStyle(ChatFormatting.DARK_GRAY);
        MutableComponent mutableComponent9 = Component.translatable((String)"bf.message.profile.details.kd", (Object[])new Object[]{mutableComponent8}).withStyle(ChatFormatting.GRAY);
        this.field_641.add(mutableComponent9);
        MutableComponent mutableComponent10 = Component.literal((String)StringUtils.formatLong(n2)).withStyle(ChatFormatting.DARK_GRAY);
        MutableComponent mutableComponent11 = Component.translatable((String)"bf.message.profile.details.kills", (Object[])new Object[]{mutableComponent10}).withStyle(ChatFormatting.GRAY);
        this.field_641.add(mutableComponent11);
        MutableComponent mutableComponent12 = Component.literal((String)StringUtils.formatLong(n)).withStyle(ChatFormatting.DARK_GRAY);
        MutableComponent mutableComponent13 = Component.translatable((String)"bf.message.profile.details.deaths", (Object[])new Object[]{mutableComponent12}).withStyle(ChatFormatting.GRAY);
        this.field_641.add(mutableComponent13);
    }
}

