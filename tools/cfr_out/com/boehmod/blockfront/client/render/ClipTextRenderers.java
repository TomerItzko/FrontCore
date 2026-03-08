/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.common.ColorReferences
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.chat.Style
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.render;

import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.gui.layer.MatchGuiLayer;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.common.gun.GunFireConfig;
import com.boehmod.blockfront.common.gun.GunMagType;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.util.StringUtils;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class ClipTextRenderers {
    public static final Renderer FLAMETHROWER = new Renderer(){

        @Override
        @OnlyIn(value=Dist.CLIENT)
        public void method_4116(@NotNull Minecraft minecraft, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, Font font, @NotNull GunItem gunItem, @NotNull ItemStack itemStack, @NotNull Style style, int n, float f, float f2, int n2, int n3, int n4, int n5, float f3) {
            GunMagType gunMagType = gunItem.getMagTypeOrDefault(itemStack);
            int n6 = gunMagType.capacity();
            float f4 = MathUtils.lerpf1(MatchGuiLayer.field_516, MatchGuiLayer.field_517, f3);
            int n7 = n2 - 2;
            float f5 = (float)MatchGuiLayer.field_516 / (float)n6 * (float)n7;
            MutableComponent mutableComponent = Component.literal((String)StringUtils.formatLong((int)f4)).withStyle(ChatFormatting.BOLD).withStyle(style);
            MutableComponent mutableComponent2 = Component.literal((String)"/").withColor(0xFFFFFF);
            MutableComponent mutableComponent3 = Component.literal((String)StringUtils.formatLong(MatchGuiLayer.field_518)).withColor(0xFFFFFF).withStyle(ChatFormatting.BOLD);
            MutableComponent mutableComponent4 = Component.literal((String)"ML").withColor(0xFFFFFF);
            MutableComponent mutableComponent5 = mutableComponent.append((Component)mutableComponent2).append((Component)mutableComponent3).append((Component)mutableComponent4);
            BFRendering.rectangle(poseStack, guiGraphics, f + 1.0f, f2, f5, 1.0f, ColorReferences.COLOR_WHITE_SOLID);
            BFRendering.component2dWithShadow(poseStack, font, guiGraphics, (Component)mutableComponent5, n4 - 102, f2 + 5.0f, n, 0.75f);
        }
    };
    private static final Component SLASH_COMPONENT = Component.literal((String)"/").withColor(0xFFFFFF);
    public static final Renderer DEFAULT = new Renderer(){

        @Override
        @OnlyIn(value=Dist.CLIENT)
        public void method_4116(@NotNull Minecraft minecraft, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, Font font, @NotNull GunItem gunItem, @NotNull ItemStack itemStack, @NotNull Style style, int n, float f, float f2, int n2, int n3, int n4, int n5, float f3) {
            BFClientManager bFClientManager = BFClientManager.getInstance();
            assert (bFClientManager != null) : "Client mod manager is null!";
            GunFireConfig gunFireConfig = gunItem.getDefaultFireConfig();
            float f4 = gunFireConfig.getFireRate();
            AbstractGame<?, ?, ?> abstractGame = bFClientManager.getGame();
            if (abstractGame != null) {
                f4 *= abstractGame.getFireRateMultiplier();
            }
            double d = Math.max(gunItem.method_4232(f3), 0.0f);
            float f5 = (float)(d / (double)f4);
            float f6 = Math.min(((float)n2 - 2.0f) * f5, (float)n2 - 2.0f);
            BFRendering.rectangle(poseStack, guiGraphics, f + 1.0f, f2, f6, 1.0f, ColorReferences.COLOR_WHITE_SOLID);
            MutableComponent mutableComponent = Component.literal((String)String.valueOf(MatchGuiLayer.field_516)).withStyle(ChatFormatting.BOLD).withStyle(style);
            MutableComponent mutableComponent2 = Component.literal((String)String.valueOf(MatchGuiLayer.field_518)).withColor(0xFFFFFF).withStyle(ChatFormatting.BOLD);
            MutableComponent mutableComponent3 = mutableComponent.append(SLASH_COMPONENT).append((Component)mutableComponent2);
            BFRendering.component2dWithShadow(poseStack, font, guiGraphics, (Component)mutableComponent3, n4 - 102, f2 + 4.0f, n);
            BFRendering.centeredComponent2dWithShadow(poseStack, font, guiGraphics, gunFireConfig.getMode().getDisplayName(), (float)(n4 - 40), f2 + 4.0f, n);
        }
    };

    public static abstract class Renderer {
        @OnlyIn(value=Dist.CLIENT)
        public abstract void method_4116(@NotNull Minecraft var1, @NotNull PoseStack var2, @NotNull GuiGraphics var3, Font var4, @NotNull GunItem var5, @NotNull ItemStack var6, @NotNull Style var7, int var8, float var9, float var10, int var11, int var12, int var13, int var14, float var15);
    }
}

