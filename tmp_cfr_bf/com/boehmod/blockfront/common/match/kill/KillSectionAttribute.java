/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.match.kill;

import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.common.match.kill.AbstractKillSection;
import com.boehmod.blockfront.common.match.kill.KillFeedEntry;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.util.BFRes;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class KillSectionAttribute
extends AbstractKillSection {
    @NotNull
    private ResourceLocation field_3312;
    @NotNull
    private String field_3313;

    public KillSectionAttribute(@NotNull String string) {
        this.field_3313 = string;
        this.field_3312 = BFRes.loc("textures/gui/killfeed/" + string + ".png");
    }

    public KillSectionAttribute() {
        this("headshot");
    }

    @Override
    public void update(@NotNull Minecraft minecraft, @NotNull AbstractGame<?, ?, ?> game, @NotNull KillFeedEntry entry) {
    }

    @Override
    public void render(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull Font font, @NotNull AbstractGame<?, ?, ?> game, @NotNull KillFeedEntry entry, float delta) {
        BFRendering.texture(poseStack, graphics, this.field_3312, 1, 0, 11, 11);
    }

    @Override
    public int getWidth(@NotNull Font font) {
        return 13;
    }

    @Override
    public int method_3223() {
        return -65536;
    }

    @Override
    public void read(@NotNull RegistryFriendlyByteBuf buf) {
        this.field_3313 = buf.readUtf();
        this.field_3312 = BFRes.loc("textures/gui/killfeed/" + this.field_3313 + ".png");
    }

    @Override
    public void write(@NotNull RegistryFriendlyByteBuf buf) {
        buf.writeUtf(this.field_3313);
    }
}

