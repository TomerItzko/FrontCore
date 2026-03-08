/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.common.ColorReferences
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.FormattedText
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.render.game.element;

import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.game.element.ClientGameElement;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGameClient;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.util.BFRes;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class TeamProgressGameElement<G extends AbstractGame<G, P, ?>, P extends AbstractGamePlayerManager<G>>
extends ClientGameElement<G, P> {
    private final int field_474;
    private int field_475;
    @NotNull
    private final String field_477;
    @NotNull
    private final ResourceLocation field_6354;
    @NotNull
    private Component field_476 = Component.empty();
    private final boolean field_473;

    public TeamProgressGameElement(int n, int n2, @NotNull String string, boolean bl) {
        super(n2);
        this.field_474 = n;
        this.field_477 = string;
        this.field_473 = bl;
        this.field_6354 = BFRes.loc("textures/gui/game/topelement/scores/progress_" + this.field_477.toLowerCase(Locale.ROOT) + ".png");
    }

    @Override
    public void update(@NotNull Minecraft minecraft, @NotNull G game, @NotNull P playerManager, @NotNull AbstractGameClient<G, P> gameClient, @NotNull LocalPlayer player) {
        this.field_475 = 0;
        GameTeam gameTeam = ((AbstractGamePlayerManager)playerManager).getTeamByName(this.field_477);
        if (gameTeam != null) {
            boolean bl = this.field_477.equals("Axis");
            int n = bl ? 14996948 : 15067868;
            this.field_475 = gameTeam.getObjectInt(BFStats.SCORE);
            this.field_476 = Component.literal((String)Integer.toString(this.field_475)).withColor(n);
        }
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, @NotNull PoseStack poseStack, @NotNull Font font, int n, int n2, float f) {
        int n3;
        super.render(graphics, poseStack, font, n, n2, f);
        int n4 = this.method_490(font);
        int n5 = 2;
        int n6 = n + 2;
        int n7 = n2 + 2;
        int n8 = n4 - 4;
        int n9 = 9;
        float f2 = Mth.clamp((float)((float)this.field_475 / (float)this.field_474), (float)0.0f, (float)1.0f);
        int n10 = Math.round(f2 * (float)(n8 - 2));
        BFRendering.rectangle(graphics, n6 + 1, n7, n8 - 2, 1, ColorReferences.COLOR_BLACK_SOLID);
        BFRendering.rectangle(graphics, n6, n7 + 1, n8, 9, ColorReferences.COLOR_BLACK_SOLID);
        BFRendering.rectangle(graphics, n6 + 1, n7 + 9 + 1, n8 - 2, 1, ColorReferences.COLOR_BLACK_SOLID);
        BFRendering.texture(poseStack, graphics, this.field_6354, n6 + 1, n7 + 1, n8 - 2, 9, 0.5f);
        if (n10 > 0) {
            n3 = this.field_473 ? n6 + n8 - n10 - 1 : n6 + 1;
            BFRendering.enableScissor(graphics, n3, n7 + 1, n10, 9);
            BFRendering.texture(poseStack, graphics, this.field_6354, n6 + 1, n7 + 1, n8 - 2, 9);
            graphics.disableScissor();
        }
        n3 = n2 + 4;
        int n11 = 2;
        int n12 = this.field_473 ? n6 + 2 : n6 + n8 - font.width((FormattedText)this.field_476) - 2;
        BFRendering.drawStringWithShadow(font, graphics, this.field_476, n12, n3, ColorReferences.COLOR_WHITE_SOLID);
    }
}

