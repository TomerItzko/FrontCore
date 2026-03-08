/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  it.unimi.dsi.fastutil.objects.ObjectList
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.screens.Screen
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.screen.match.summary.MatchSummaryScreen;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.game.AbstractGameClient;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.NotNull;

public class GameSummaryManager {
    @NotNull
    private final ObjectList<MatchSummaryScreen> screens = new ObjectArrayList();
    @NotNull
    private final AbstractGameClient<?, ?> renderer;
    private boolean hasScreens = false;
    private final int playerExp;

    public GameSummaryManager(@NotNull BFClientManager manager, @NotNull AbstractGameClient<?, ?> renderer) {
        this.renderer = renderer;
        Minecraft minecraft = Minecraft.getInstance();
        PlayerCloudData playerCloudData = ((ClientPlayerDataHandler)manager.getPlayerDataHandler()).getCloudData(minecraft);
        this.playerExp = playerCloudData.getExp();
    }

    public int getPlayerExp() {
        return this.playerExp;
    }

    public void addScreens(boolean onlyVote) {
        this.screens.addAll(this.renderer.getSummaryScreens(onlyVote));
    }

    public void updateScreens(boolean onlyVote) {
        this.addScreens(onlyVote);
        this.hasScreens = true;
    }

    public void advanceScreen(@NotNull Minecraft minecraft) {
        MatchSummaryScreen matchSummaryScreen;
        Screen screen;
        if (this.screens.isEmpty()) {
            return;
        }
        if (minecraft.screen == null || (screen = minecraft.screen) instanceof MatchSummaryScreen && (matchSummaryScreen = (MatchSummaryScreen)screen).isFinished()) {
            screen = (MatchSummaryScreen)((Object)this.screens.removeFirst());
            minecraft.setScreen((Screen)screen.setIsLastSummary(this.screens.isEmpty()));
        }
    }

    public boolean hasScreens() {
        return this.hasScreens;
    }
}

