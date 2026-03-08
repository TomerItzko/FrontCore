/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render.game.element;

import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.game.element.ClientGameElement;
import com.boehmod.blockfront.game.AbstractCapturePoint;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGameClient;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.tag.IHasCapturePoints;
import com.boehmod.blockfront.util.BFRes;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class CapturePointGameElement<C extends AbstractCapturePoint<P>, G extends AbstractGame<G, P, ?>, P extends AbstractGamePlayerManager<G>>
extends ClientGameElement<G, P> {
    private static final ResourceLocation CP_ALLIES = BFRes.loc("textures/text/cp_allies.png");
    private static final ResourceLocation CP_AXIS = BFRes.loc("textures/text/cp_axis.png");
    private static final ResourceLocation CP_NEUTRAL = BFRes.loc("textures/text/cp_neutral.png");
    private static final ResourceLocation CP_FLICKER = BFRes.loc("textures/text/cp_flicker.png");
    private static final int field_458 = 11;
    private static final int field_459 = 2;
    @NotNull
    private final ObjectList<Entry> field_461 = new ObjectArrayList();
    private int field_460 = 0;
    private boolean shouldFlicker = false;

    public CapturePointGameElement() {
        super(1);
    }

    private void renderEntry(@NotNull GuiGraphics guiGraphics, @NotNull PoseStack poseStack, @NotNull Font font, int n, int n2, int index) {
        Entry entry = (Entry)this.field_461.get(index);
        ResourceLocation resourceLocation = entry.texture();
        Component component = entry.name();
        int n3 = n + 3 + index * 13;
        int n4 = n2 + 2;
        BFRendering.texture(poseStack, guiGraphics, resourceLocation, n3, n4, 11, 11);
        if (this.shouldFlicker && entry.flicker()) {
            BFRendering.texture(poseStack, guiGraphics, CP_FLICKER, n3, n4, 11, 11);
        }
        float f = (float)n3 + 0.3f + 5.5f;
        int n5 = n4 + 3;
        BFRendering.centeredComponent2d(poseStack, font, guiGraphics, component, f, (float)n5, 0.75f);
    }

    @Override
    public void update(@NotNull Minecraft minecraft, @NotNull G game, @NotNull P playerManager, @NotNull AbstractGameClient<G, P> gameClient, @NotNull LocalPlayer player) {
        this.field_461.clear();
        ++this.field_460;
        if (this.field_460 >= 10) {
            this.field_460 = 0;
            this.shouldFlicker = !this.shouldFlicker;
        }
        List list = ((IHasCapturePoints)game).getCapturePoints();
        GameTeam gameTeam = ((AbstractGamePlayerManager)playerManager).getTeamByName("Axis");
        GameTeam gameTeam2 = ((AbstractGamePlayerManager)playerManager).getTeamByName("Allies");
        assert (gameTeam != null);
        assert (gameTeam2 != null);
        for (AbstractCapturePoint abstractCapturePoint : list) {
            GameTeam gameTeam3 = abstractCapturePoint.getCbTeam();
            ResourceLocation resourceLocation = CP_NEUTRAL;
            if (gameTeam2.equals(gameTeam3)) {
                resourceLocation = CP_ALLIES;
            } else if (gameTeam.equals(gameTeam3)) {
                resourceLocation = CP_AXIS;
            }
            MutableComponent mutableComponent = Component.literal((String)abstractCapturePoint.name).withStyle(ChatFormatting.BOLD);
            Entry entry = new Entry((Component)mutableComponent, resourceLocation, abstractCapturePoint.isBeingCaptured);
            this.field_461.add((Object)entry);
        }
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, @NotNull PoseStack poseStack, @NotNull Font font, int n, int n2, float f) {
        super.render(graphics, poseStack, font, n, n2, f);
        int n3 = this.field_461.size();
        for (int i = 0; i < n3; ++i) {
            this.renderEntry(graphics, poseStack, font, n, n2, i);
        }
    }

    @Override
    public int method_490(@NotNull Font font) {
        return this.field_461.size() * 13 + 4;
    }

    private record Entry(@NotNull Component name, @NotNull ResourceLocation texture, boolean flicker) {
        public boolean flickerDoNotCall() {
            return this.flicker;
        }
    }
}

