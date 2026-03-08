/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.menu.MenuRenderer;
import com.boehmod.blockfront.client.screen.BFMenuScreen;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Locale;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class BF_174
extends MenuRenderer {
    @Override
    public void renderBackground(@NotNull BFClientManager manager, @NotNull ClientPlayerDataHandler dataHandler, @NotNull Minecraft minecraft, @NotNull BFMenuScreen screen, @NotNull PoseStack poseStack, @NotNull Font font, @NotNull GuiGraphics graphics, int width, int height, int mouseX, int mouseY, float renderTime, float delta) {
        PlayerCloudData playerCloudData = dataHandler.getCloudData(minecraft);
        int n = width - 90;
        int n2 = 6;
        List list = playerCloudData.getActiveBoosters();
        if (!list.isEmpty()) {
            boolean bl = BFRendering.isPointInRectangle(n - 25, 1.0, 50.0, 15.0, mouseX, mouseY);
            ChatFormatting chatFormatting = bl ? ChatFormatting.WHITE : ChatFormatting.GREEN;
            BFRendering.centeredComponent2d(poseStack, font, graphics, (Component)Component.translatable((String)"bf.menu.text.boosters.active", (Object[])new Object[]{list.size()}).withStyle(chatFormatting).withStyle(ChatFormatting.BOLD), n, 6, 0.5f);
            if (bl) {
                String string;
                graphics.pose().pushPose();
                graphics.pose().translate(0.0f, 0.0f, 250.0f);
                int n3 = 11 * list.size();
                int n4 = 104;
                int n5 = 0;
                ObjectArrayList objectArrayList = new ObjectArrayList();
                for (Object object : list) {
                    string = String.valueOf(ChatFormatting.GREEN) + "Booster " + object.getType().getTitle().toUpperCase(Locale.ROOT) + " - " + String.valueOf(ChatFormatting.WHITE) + object.getTimeRemainingInMinutes() + "m" + String.valueOf(ChatFormatting.GREEN) + "/" + String.valueOf(ChatFormatting.WHITE) + object.getMaxMinutes() + "m";
                    objectArrayList.add(string);
                    int n6 = font.width(ChatFormatting.stripFormatting((String)string));
                    if (n6 <= n4) continue;
                    n4 = n6;
                }
                BFRendering.rectangleWithDarkShadow(graphics, mouseX - n4, mouseY, n4, n3, 0, 0.5f);
                for (Object object : objectArrayList) {
                    string = Component.literal((String)object);
                    BFRendering.drawString(font, graphics, (Component)string, mouseX - n4 + 2, mouseY + n5 + 2);
                    n5 += 11;
                }
                graphics.pose().popPose();
            }
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
    }
}

