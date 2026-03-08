/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.boehmod.blockfront.game;

import com.boehmod.blockfront.game.GameTeam;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;

public class WinningTeamData {
    @Nullable
    public GameTeam team;
    @Nullable
    public Set<UUID> topPlayers;

    public WinningTeamData(@Nullable GameTeam team, @Nullable Set<UUID> topPlayers) {
        this.team = team;
        this.topPlayers = topPlayers;
    }
}

