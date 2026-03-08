/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.level.Level
 *  org.apache.commons.lang3.tuple.Pair
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.boehmod.blockfront.game.event;

import com.boehmod.blockfront.common.match.DivisionData;
import com.boehmod.blockfront.common.match.Loadout;
import com.boehmod.blockfront.common.match.MatchClass;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.event.GameEvent;
import com.boehmod.blockfront.game.impl.dom.DominationGame;
import com.boehmod.blockfront.util.BFUtils;
import java.util.Set;
import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ReinforcementsMgRadioCommand
extends GameEvent {
    private static final int field_3342 = 4;
    public static final Component field_3341 = Component.translatable((String)"bf.message.gamemode.radio.command.reinforcements.mg");

    public ReinforcementsMgRadioCommand(@NotNull ServerPlayer serverPlayer, int n) {
        super(serverPlayer, n, 80);
    }

    @Override
    void method_3446(@NotNull ServerLevel serverLevel, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull Set<UUID> set) {
        for (int i = 0; i < 3; ++i) {
            this.method_3242(serverLevel, abstractGame, MatchClass.CLASS_RIFLEMAN, 0, 16.0f);
        }
        this.method_3242(serverLevel, abstractGame, MatchClass.CLASS_GUNNER, 0, 48.0f);
    }

    @Override
    void method_3447(@NotNull ServerLevel serverLevel, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull Set<UUID> set) {
    }

    @Override
    void method_3445(@NotNull Level level, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull Set<UUID> set) {
    }

    @Override
    @Nullable
    public Component getMessage() {
        return field_3341;
    }

    private void method_3242(@NotNull ServerLevel serverLevel, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull MatchClass matchClass, int n, float f) {
        DivisionData divisionData;
        Loadout loadout;
        UUID uUID;
        if (this.field_3429 == null) {
            return;
        }
        Object obj = abstractGame.getPlayerManager();
        GameTeam gameTeam = ((AbstractGamePlayerManager)obj).getPlayerTeam(uUID = this.field_3429.getUUID());
        if (gameTeam != null && abstractGame instanceof DominationGame && (loadout = (divisionData = gameTeam.getDivisionData(abstractGame)).getLoadout(matchClass, n)) != null) {
            BFUtils.spawnBot(abstractGame, serverLevel, gameTeam, (Pair<Loadout, MatchClass>)Pair.of((Object)loadout, (Object)((Object)matchClass)), false, f);
        }
    }
}

