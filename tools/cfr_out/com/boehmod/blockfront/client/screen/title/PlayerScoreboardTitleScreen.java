/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.Object2IntMap
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.screen.title;

import com.boehmod.blockfront.client.screen.title.ScoreboardTitleScreen;
import com.boehmod.blockfront.client.settings.BFClientSettings;
import com.boehmod.blockfront.unnamed.BF_216;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public final class PlayerScoreboardTitleScreen
extends ScoreboardTitleScreen {
    @Override
    @NotNull
    public Object2IntMap<UUID> method_1036() {
        return BFClientSettings.PLAYER_SCORES;
    }

    @Override
    @NotNull
    public BF_216.Type getType() {
        return BF_216.Type.PLAYER;
    }
}

