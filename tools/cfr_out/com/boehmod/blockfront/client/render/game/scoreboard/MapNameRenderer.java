/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 */
package com.boehmod.blockfront.client.render.game.scoreboard;

import com.boehmod.blockfront.assets.impl.MapAsset;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.game.scoreboard.ScoreboardHeaderRenderer;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Locale;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public class MapNameRenderer
extends ScoreboardHeaderRenderer {
    public static final ResourceLocation EMBLEM_TEXTURE = BFRes.loc("textures/gui/game/scoreboard/emblem.png");

    @Override
    public void render(PoseStack poseStack, Font font, GuiGraphics graphics, AbstractGame<?, ?, ?> game, int n, int n2, int n3, int n4, int n5, int n6, float f) {
        float f2 = this.method_3902(f);
        if (f2 <= 0.02f) {
            f2 = 0.02f;
        }
        int n7 = 10;
        int n8 = MathUtils.withAlphaf(n6, f2);
        String string = game.getType();
        MapAsset mapAsset = game.getMap();
        int n9 = 32;
        BFRendering.tintedTexture(poseStack, graphics, EMBLEM_TEXTURE, n + 10, n2 + 10, 32, 32, 0.0f, f2, n8);
        MutableComponent mutableComponent = Component.literal((String)(string.toUpperCase(Locale.ROOT) + " | " + mapAsset.getName())).withColor(n8);
        BFRendering.component2d(poseStack, font, graphics, (Component)mutableComponent, 58.0f, n2 + 10 + 3, n8, 2.0f);
        MutableComponent mutableComponent2 = Component.literal((String)mapAsset.getAuthor());
        BFRendering.drawString(font, graphics, (Component)mutableComponent2, 58, n2 + 10 + 21, n8);
    }
}

