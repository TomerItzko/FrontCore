/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.match.kill;

import com.boehmod.blockfront.common.match.kill.KillFeedEntry;
import com.boehmod.blockfront.game.AbstractGame;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractKillSection {
    public static final int field_3311 = 11;

    @OnlyIn(value=Dist.CLIENT)
    public abstract void update(@NotNull Minecraft var1, @NotNull AbstractGame<?, ?, ?> var2, @NotNull KillFeedEntry var3);

    @OnlyIn(value=Dist.CLIENT)
    public abstract void render(@NotNull PoseStack var1, @NotNull GuiGraphics var2, @NotNull Font var3, @NotNull AbstractGame<?, ?, ?> var4, @NotNull KillFeedEntry var5, float var6);

    @OnlyIn(value=Dist.CLIENT)
    public abstract int getWidth(@NotNull Font var1);

    @OnlyIn(value=Dist.CLIENT)
    public abstract int method_3223();

    @OnlyIn(value=Dist.CLIENT)
    public abstract void read(@NotNull RegistryFriendlyByteBuf var1);

    public abstract void write(@NotNull RegistryFriendlyByteBuf var1);
}

