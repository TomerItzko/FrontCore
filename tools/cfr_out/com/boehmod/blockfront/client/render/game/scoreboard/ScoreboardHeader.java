/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.render.game.scoreboard;

import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.game.scoreboard.MapNameRenderer;
import com.boehmod.blockfront.client.render.game.scoreboard.MatchStreakRenderer;
import com.boehmod.blockfront.client.render.game.scoreboard.ScoreboardHeaderRenderer;
import com.boehmod.blockfront.game.AbstractGame;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;

public class ScoreboardHeader {
    @NotNull
    public final List<ScoreboardHeaderRenderer> renderers = new ObjectArrayList();
    private int currentIndex = 0;
    private ScoreboardHeaderRenderer currentRenderer;

    public ScoreboardHeader() {
        this.renderers.add(new MapNameRenderer());
        this.renderers.add(new MatchStreakRenderer());
        this.currentRenderer = this.renderers.get(this.currentIndex);
    }

    public void onUpdate() {
        if (this.currentRenderer != null && this.currentRenderer.shouldSwitch()) {
            ++this.currentIndex;
            if (this.currentIndex >= this.renderers.size()) {
                this.currentIndex = 0;
            }
            this.currentRenderer.reset();
            this.currentRenderer = this.renderers.get(this.currentIndex);
        }
    }

    public void render(@NotNull PoseStack poseStack, @NotNull Font font, @NotNull GuiGraphics graphics, @NotNull AbstractGame<?, ?, ?> game, int n, int n2, int n3, int n4, int n5, int n6, float f) {
        float f2;
        if (this.currentRenderer != null && (f2 = this.currentRenderer.method_3902(f)) > 0.0f) {
            BFRendering.enableScissor(graphics, n, n2, n3, n4);
            this.currentRenderer.render(poseStack, font, graphics, game, n, n2, n3, n4, n5, n6, f);
            graphics.disableScissor();
        }
    }
}

