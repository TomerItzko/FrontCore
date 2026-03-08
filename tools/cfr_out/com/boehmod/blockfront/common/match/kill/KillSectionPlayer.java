/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.ComponentSerialization
 *  net.minecraft.network.chat.FormattedText
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.match.kill;

import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.common.match.kill.AbstractKillSection;
import com.boehmod.blockfront.common.match.kill.KillFeedEntry;
import com.boehmod.blockfront.game.AbstractGame;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.FormattedText;
import org.jetbrains.annotations.NotNull;

public class KillSectionPlayer
extends AbstractKillSection {
    @NotNull
    private Component field_3039;

    public KillSectionPlayer(@NotNull Component component) {
        this.field_3039 = component;
    }

    public KillSectionPlayer() {
        this((Component)Component.literal((String)"Unknown"));
    }

    @Override
    public void update(@NotNull Minecraft minecraft, @NotNull AbstractGame<?, ?, ?> game, @NotNull KillFeedEntry entry) {
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull Font font, @NotNull AbstractGame<?, ?, ?> game, @NotNull KillFeedEntry entry, float delta) {
        BFRendering.centeredComponent2dWithShadow(poseStack, font, graphics, this.field_3039, this.getWidth(font) / 2, 2);
    }

    @Override
    public int getWidth(@NotNull Font font) {
        return font.width((FormattedText)this.field_3039) + 2;
    }

    @Override
    public int method_3223() {
        return -16711681;
    }

    @Override
    public void read(@NotNull RegistryFriendlyByteBuf buf) {
        this.field_3039 = (Component)ComponentSerialization.STREAM_CODEC.decode((Object)buf);
        if (this.field_3039.getString().equals(Minecraft.getInstance().getUser().getName())) {
            this.field_3039 = this.field_3039.copy().withColor(0xFFFFFF);
        }
    }

    @Override
    public void write(@NotNull RegistryFriendlyByteBuf buf) {
        ComponentSerialization.STREAM_CODEC.encode((Object)buf, (Object)this.field_3039);
    }
}

