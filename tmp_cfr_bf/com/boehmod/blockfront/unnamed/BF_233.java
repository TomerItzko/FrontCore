/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.bflib.cloud.connection.ConnectionStatus;
import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.ac.BFClientAntiCheat;
import com.boehmod.blockfront.client.net.ConnectionMode;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.menu.MenuRenderer;
import com.boehmod.blockfront.client.screen.BFMenuScreen;
import com.boehmod.blockfront.cloud.client.ClientConnectionManager;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.util.BFStyles;
import com.boehmod.blockfront.util.EllipsisUtils;
import com.boehmod.blockfront.util.StringUtils;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class BF_233
extends MenuRenderer {
    private static final Component field_1400 = Component.translatable((String)"bf.menu.text.network.anticheat").withStyle(ChatFormatting.RED).withStyle(ChatFormatting.BOLD);
    private static final Component field_1401 = Component.translatable((String)"bf.message.online").withStyle(ChatFormatting.RED);
    private static final Component field_1402 = Component.translatable((String)"bf.message.offline").withColor(ColorReferences.COLOR_THEME_YELLOW_SOLID);
    private static final Component field_1403 = Component.translatable((String)"bf.menu.text.network.anticheat").withStyle(ChatFormatting.RED).withStyle(BFStyles.BOLD);
    private int field_1398;
    private int field_1399 = 0;

    @Override
    public void renderBackground(@NotNull BFClientManager manager, @NotNull ClientPlayerDataHandler dataHandler, @NotNull Minecraft minecraft, @NotNull BFMenuScreen screen, @NotNull PoseStack poseStack, @NotNull Font font, @NotNull GuiGraphics graphics, int width, int height, int mouseX, int mouseY, float renderTime, float delta) {
        int n = width / 2;
        MutableComponent mutableComponent = this.method_1032(manager);
        BFRendering.centeredComponent2d(poseStack, font, graphics, (Component)mutableComponent, n, height - 7, ColorReferences.COLOR_WHITE_SOLID, 0.5f);
    }

    @Override
    public void renderBelow(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, PlayerCloudData cloudData, @NotNull BFMenuScreen screen, @NotNull GuiGraphics graphics, @NotNull PoseStack poseStack, @NotNull Font font, int width, int height, int mouseX, int mouseY, float renderTime, float delta) {
    }

    @Override
    public void renderAbove(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull BFMenuScreen screen, @NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull Font font, int width, int height, int mouseX, int mouseY, float renderTime, float delta) {
        BFClientAntiCheat bFClientAntiCheat = (BFClientAntiCheat)manager.getAntiCheat();
        ClientConnectionManager clientConnectionManager = (ClientConnectionManager)manager.getConnectionManager();
        if (clientConnectionManager.getStatus().isConnected() || clientConnectionManager.getConnectionMode() != ConnectionMode.ONLINE) {
            return;
        }
        int n = 16;
        int n2 = clientConnectionManager.getConnection().getReconnectTickCount();
        float f = MathUtils.lerpf1(this.field_1398, this.field_1399, delta);
        float f2 = f / 600.0f;
        float f3 = 1.0f + Mth.sin((float)(renderTime / 20.0f));
        MutableComponent mutableComponent = Component.literal((String)String.valueOf(n2 / 20));
        MutableComponent mutableComponent2 = Component.translatable((String)"bf.menu.text.network.reconnect", (Object[])new Object[]{mutableComponent}).append(EllipsisUtils.cyclingEllipsis(renderTime)).withStyle(ChatFormatting.YELLOW).withStyle(BFStyles.BOLD);
        BFRendering.component2d(poseStack, font, graphics, (Component)mutableComponent2, 16, height - 11, 0.5f);
        int n3 = height - 1;
        BFRendering.rectangle(graphics, 0, n3, width, 1, -12303292, 1.0f);
        BFRendering.rectangle(graphics, 0, n3, width, 1, ColorReferences.COLOR_WHITE_SOLID, Math.max(f3 / 4.0f, 0.0f));
        BFRendering.rectangle(poseStack, graphics, 0.0f, n3, (float)width * (1.0f - f2), 1.0f, ColorReferences.COLOR_THEME_YELLOW_SOLID, 1.0f);
        BFRendering.runningTexture(graphics, 0, height - 17, 16, ColorReferences.COLOR_THEME_YELLOW_SOLID, renderTime);
    }

    @Override
    public void tick(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull BFMenuScreen screen, float renderTime) {
        ClientConnectionManager clientConnectionManager = (ClientConnectionManager)manager.getConnectionManager();
        this.field_1399 = this.field_1398;
        this.field_1398 = clientConnectionManager.getConnection().getReconnectTickCount();
    }

    private MutableComponent method_1032(@NotNull BFClientManager bFClientManager) {
        ClientConnectionManager clientConnectionManager = (ClientConnectionManager)bFClientManager.getConnectionManager();
        boolean bl = clientConnectionManager.getStatus() == ConnectionStatus.CONNECTED_VERIFIED;
        int n = 8159560;
        int n2 = ChatFormatting.GRAY.getColor();
        MutableComponent mutableComponent = Component.translatable((String)"bf.menu.text.network.verified");
        MutableComponent mutableComponent2 = Component.translatable((String)"bf.menu.text.network.unverified");
        MutableComponent mutableComponent3 = field_1401.copy().append(", ").append((Component)(bl ? mutableComponent : mutableComponent2)).withColor(bl ? 8159560 : n2);
        MutableComponent mutableComponent4 = Component.literal((String)StringUtils.formatLong(clientConnectionManager.usersOnline)).withColor(8159560);
        MutableComponent mutableComponent5 = Component.literal((String)"???").withStyle(ChatFormatting.GRAY);
        MutableComponent mutableComponent6 = clientConnectionManager.getStatus().isConnected() ? mutableComponent3 : field_1402;
        return Component.translatable((String)"bf.menu.text.network", (Object[])new Object[]{mutableComponent6, bl ? mutableComponent4 : mutableComponent5});
    }
}

