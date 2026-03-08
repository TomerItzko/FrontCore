/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.common.ColorReferences
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.LivingEntity
 */
package com.boehmod.blockfront.client.render.game.scoreboard;

import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.player.FakePlayer;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.game.scoreboard.ScoreboardHeaderRenderer;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameMatchStreakTracker;
import com.boehmod.blockfront.util.StringUtils;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

public class MatchStreakRenderer
extends ScoreboardHeaderRenderer {
    public static final Component field_3764 = Component.translatable((String)"bf.message.match.streak.title");

    @Override
    public void render(PoseStack poseStack, Font font, GuiGraphics graphics, AbstractGame<?, ?, ?> game, int n, int n2, int n3, int n4, int n5, int n6, float f) {
        float f2 = this.method_3902(f);
        if (f2 <= 0.02f) {
            f2 = 0.02f;
        }
        int n7 = 10;
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        int n8 = MathUtils.withAlphaf(n6, f2);
        int n9 = MathUtils.withAlphaf(ColorReferences.COLOR_THEME_YELLOW_SOLID, f2);
        int n10 = 32;
        Minecraft minecraft = Minecraft.getInstance();
        ClientPlayerDataHandler clientPlayerDataHandler = (ClientPlayerDataHandler)bFClientManager.getPlayerDataHandler();
        Object obj = game.getPlayerManager();
        GameMatchStreakTracker gameMatchStreakTracker = ((AbstractGamePlayerManager)obj).getMatchStreakTracker();
        UUID uUID = gameMatchStreakTracker.getStreakLeader();
        MutableComponent mutableComponent = Component.literal((String)StringUtils.formatLong(gameMatchStreakTracker.getStreak())).withColor(n9);
        MutableComponent mutableComponent2 = Component.translatable((String)"bf.message.match.streak.match", (Object[])new Object[]{mutableComponent}).withColor(n8);
        MutableComponent mutableComponent3 = Component.literal((String)"???");
        if (uUID != null) {
            PlayerCloudData playerCloudData = clientPlayerDataHandler.getCloudProfile(uUID);
            float f3 = Mth.sin((float)(BFRendering.getRenderTime() / 40.0f));
            FakePlayer fakePlayer = BFRendering.ENVIRONMENT.getPlayerCached(minecraft, playerCloudData.getMcProfile());
            float f4 = (float)(n + 10) + 16.0f;
            float f5 = (float)(n2 + 10 + 115) + 57.0f * (1.0f - f2);
            float f6 = 30.0f * f3 * f2;
            BFRendering.entity(clientPlayerDataHandler, poseStack, graphics, minecraft, (LivingEntity)fakePlayer, f4, f5, 64.0f, 0.0f, f6, f2, n8);
            int n11 = gameMatchStreakTracker.getPlayerStreak(uUID);
            String string = n11 <= 1 ? "bf.message.match.streak.player.count" : "bf.message.match.streak.player.count.plural";
            MutableComponent mutableComponent4 = Component.literal((String)playerCloudData.getUsername()).withColor(n9);
            MutableComponent mutableComponent5 = Component.literal((String)StringUtils.formatLong(n11)).withColor(n9);
            MutableComponent mutableComponent6 = Component.translatable((String)string, (Object[])new Object[]{mutableComponent5});
            mutableComponent3 = Component.translatable((String)"bf.message.match.streak.player", (Object[])new Object[]{mutableComponent4, mutableComponent6}).withColor(n8);
        }
        BFRendering.component2d(poseStack, font, graphics, field_3764, 58.0f, n2 + 10 - 2, n8, 2.0f);
        BFRendering.drawString(font, graphics, (Component)mutableComponent2, 58, n2 + 10 + 16, n8);
        BFRendering.drawString(font, graphics, (Component)mutableComponent3, 58, n2 + 10 + 26, n8);
    }
}

