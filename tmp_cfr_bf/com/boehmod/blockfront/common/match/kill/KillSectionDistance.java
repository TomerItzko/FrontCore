/*
 * Decompiled with CFR.
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

public class KillSectionDistance
extends AbstractKillSection {
    @NotNull
    private Component field_3317;

    public KillSectionDistance(@NotNull Component component) {
        this.field_3317 = component;
    }

    public KillSectionDistance() {
        this((Component)Component.literal((String)"Unknown"));
    }

    @Override
    public void update(@NotNull Minecraft minecraft, @NotNull AbstractGame<?, ?, ?> game, @NotNull KillFeedEntry entry) {
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull Font font, @NotNull AbstractGame<?, ?, ?> game, @NotNull KillFeedEntry entry, float delta) {
        BFRendering.centeredComponent2dWithShadow(poseStack, font, graphics, this.field_3317, (float)this.getWidth(font) / 2.0f - 1.5f, 2.0f);
    }

    @Override
    public int getWidth(@NotNull Font font) {
        return font.width((FormattedText)this.field_3317) + 2;
    }

    @Override
    public int method_3223() {
        return -16776961;
    }

    @Override
    public void read(@NotNull RegistryFriendlyByteBuf buf) {
        this.field_3317 = (Component)ComponentSerialization.STREAM_CODEC.decode((Object)buf);
    }

    @Override
    public void write(@NotNull RegistryFriendlyByteBuf buf) {
        ComponentSerialization.STREAM_CODEC.encode((Object)buf, (Object)this.field_3317);
    }
}

