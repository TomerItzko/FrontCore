/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.common.ColorReferences
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.DeltaTracker
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.gui.layer;

import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.gui.layer.BFAbstractGuiLayer;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.BFMenuScreen;
import com.boehmod.blockfront.common.item.IRenderConsumeInfo;
import com.boehmod.blockfront.common.item.RadioItem;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameEventType;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.util.EllipsisUtils;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class EquipmentGuiLayer
extends BFAbstractGuiLayer {
    private static void method_513(@NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, @NotNull Font font, @NotNull IRenderConsumeInfo iRenderConsumeInfo, int n, int n2, float f) {
        if (iRenderConsumeInfo.getTicksUntilAction() <= 0) {
            return;
        }
        int n3 = (iRenderConsumeInfo.getTotalTicksUntilAction() - iRenderConsumeInfo.getTicksUntilAction()) / 20;
        MutableComponent mutableComponent = Component.translatable((String)iRenderConsumeInfo.getTranslation()).append(EllipsisUtils.cyclingEllipsis(f));
        MutableComponent mutableComponent2 = Component.literal((String)(" " + BFRendering.formatTime(n3)));
        BFRendering.rectangleWithDarkShadow(guiGraphics, n - 70, n2 - 29, 140, 30, BFRendering.translucentBlack());
        BFRendering.texture(poseStack, guiGraphics, iRenderConsumeInfo.getIcon(), n - 70 - 16, n2 - 16 - 14, 32, 32);
        BFRendering.centeredString(font, guiGraphics, (Component)mutableComponent, n + 4, n2 - 25);
        int n4 = n - 43;
        int n5 = n2 - 12;
        int n6 = 10;
        int n7 = 100;
        float f2 = (float)(100 * iRenderConsumeInfo.getTicksUntilAction()) / (float)iRenderConsumeInfo.getTotalTicksUntilAction();
        BFRendering.rectangle(guiGraphics, n4, n5, 100, 10, ColorReferences.COLOR_BLACK_SOLID, (float)BFRendering.translucentBlack());
        BFRendering.rectangle(poseStack, guiGraphics, n4, n5, f2, 10.0f, iRenderConsumeInfo.getColor(), 0.5f);
        int n8 = n4 + 50;
        int n9 = n5 + 2;
        BFRendering.centeredString(font, guiGraphics, (Component)mutableComponent2.withColor(0xFFFFFF), n8, n9);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, @NotNull DeltaTracker delta, @NotNull BFClientManager manager) {
        Item item;
        Object object;
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        LocalPlayer localPlayer = minecraft.player;
        float f = BFRendering.getRenderTime();
        if (localPlayer == null || minecraft.options.hideGui) {
            return;
        }
        AbstractGame<?, ?, ?> abstractGame = manager.getGame();
        if (abstractGame == null) {
            return;
        }
        float f2 = delta.getGameTimeDeltaPartialTick(true);
        PoseStack poseStack = graphics.pose();
        Object obj = abstractGame.getPlayerManager();
        ItemStack itemStack = localPlayer.getItemInHand(InteractionHand.MAIN_HAND);
        int n = graphics.guiWidth();
        int n2 = graphics.guiHeight();
        int n3 = n / 2;
        int n4 = n2 / 2;
        if (minecraft.screen == null) {
            object = RadioItem.field_3961;
            float f3 = n4 - 45;
            float f4 = MathUtils.lerpf1(RadioItem.field_3966, RadioItem.field_3967, f2);
            BFRendering.centeredTextureScaled(poseStack, graphics, BFMenuScreen.BACKSHADOW, n3, f3, 64.0f, 64.0f, 1.0f + 0.25f * f4, f4 / 1.5f);
            if (!itemStack.isEmpty() && itemStack.getItem() instanceof RadioItem) {
                float f5 = 10.0f * (1.0f - f4);
                GameTeam gameTeam = ((AbstractGamePlayerManager)obj).getPlayerTeam(localPlayer.getUUID());
                if (gameTeam != null) {
                    int n5 = gameTeam.getObjectInt(BFStats.RADIO_DELAY, 0);
                    String string = BFRendering.formatTime(n5 / 20);
                    boolean bl = !((GameEventType)((Object)object)).hasTimeLimit() || n5 <= 0;
                    MutableComponent mutableComponent = Component.translatable((String)((GameEventType)((Object)object)).getLocale());
                    if (!bl) {
                        mutableComponent.append(" (" + string + ")");
                    }
                    ChatFormatting chatFormatting = bl ? ChatFormatting.GREEN : ChatFormatting.RED;
                    BFRendering.centeredComponent2dWithShadow(poseStack, font, graphics, (Component)mutableComponent.withStyle(chatFormatting), (float)n3, f3 + 20.0f - f5);
                }
            }
            if (f4 > 0.0f) {
                BFRendering.centeredTextureScaled(poseStack, graphics, ((GameEventType)((Object)object)).getIcon(), n3, f3 - 5.0f, 30.0f, 30.0f, 1.0f + 0.2f * f4, f4);
            }
        }
        if ((item = itemStack.getItem()) instanceof IRenderConsumeInfo) {
            object = (IRenderConsumeInfo)item;
            EquipmentGuiLayer.method_513(poseStack, graphics, font, (IRenderConsumeInfo)object, n3, n4, f);
        }
    }
}

