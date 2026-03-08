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
import com.boehmod.blockfront.util.FormatUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.util.Calendar;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

public class BF_175
extends MenuRenderer {
    private static final int field_989 = Calendar.getInstance().get(1);
    private static final Component field_992 = Component.literal((String)String.valueOf(field_989)).withColor(11120486);
    private static final Component field_993 = Component.translatable((String)"bf.menu.text.copyright.bf.left", (Object[])new Object[]{field_992, "BlockFront"}).withColor(0xFFFFFF);
    private static final Component field_990 = Component.translatable((String)"bf.menu.text.copyright.mc.left", (Object[])new Object[]{field_992}).withColor(0xFFFFFF);
    private static final Component field_991 = Component.translatable((String)"bf.menu.text.copyright.right");
    private static final String field_988 = String.format("%s is a 3rd party modification and is not owned by or affiliated with %s, %s or %s", "BlockFront", String.valueOf(ChatFormatting.BOLD) + "Minecraft" + String.valueOf(ChatFormatting.RESET), String.valueOf(ChatFormatting.BOLD) + "Microsoft" + String.valueOf(ChatFormatting.RESET), String.valueOf(ChatFormatting.BOLD) + "Mojang AB" + String.valueOf(ChatFormatting.RESET));

    @Override
    public void renderBackground(@NotNull BFClientManager manager, @NotNull ClientPlayerDataHandler dataHandler, @NotNull Minecraft minecraft, @NotNull BFMenuScreen screen, @NotNull PoseStack poseStack, @NotNull Font font, @NotNull GuiGraphics graphics, int width, int height, int mouseX, int mouseY, float renderTime, float delta) {
        int n = width / 2;
        BFRendering.component2dWithShadow(poseStack, font, graphics, field_993, 3.0f, 3.0f, 0x44FFFFFF, 0.5f);
        BFRendering.component2dWithShadow(poseStack, font, graphics, field_990, 3.0f, 9.0f, 0x44FFFFFF, 0.5f);
        BFRendering.component2dWithShadow(poseStack, font, graphics, field_991, width - 44, 3.0f, 0x44FFFFFF, 0.5f);
        ObjectList<String> objectList = FormatUtils.parseMarkup(font, field_988, 260);
        int n2 = 0;
        for (String string : objectList) {
            MutableComponent mutableComponent = Component.literal((String)string);
            BFRendering.centeredComponent2d(poseStack, font, graphics, (Component)mutableComponent, n, height - 30 + 5 * n2, -11184811, 0.5f);
            ++n2;
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

