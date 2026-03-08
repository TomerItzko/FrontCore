/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.level.Level
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.boehmod.blockfront.common.match.radio;

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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ReinforcementsRadioCommand
extends GameEvent {
    private static final int field_3339 = 2;
    public static final Component field_3337 = Component.translatable((String)"bf.message.gamemode.radio.command.reinforcements");
    private int field_3338 = 0;
    private int field_3340 = 15;

    public ReinforcementsRadioCommand(@NotNull ServerPlayer serverPlayer, int n) {
        super(serverPlayer, n, 40);
    }

    void method_3446(@NotNull ServerLevel serverLevel, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull Set<UUID> set) {
    }

    void method_3447(@NotNull ServerLevel serverLevel, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull Set<UUID> set) {
        if (this.field_3338-- <= 0) {
            this.field_3338 = 2;
            if (this.field_3340-- > 0) {
                this.method_3241(serverLevel, abstractGame);
            }
        }
    }

    void method_3445(@NotNull Level level, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull Set<UUID> set) {
    }

    @Override
    @Nullable
    public Component getMessage() {
        return field_3337;
    }

    private void method_3241(@NotNull ServerLevel serverLevel, @NotNull AbstractGame<?, ?, ?> abstractGame) {
        UUID uUID;
        if (this.field_3429 == null) {
            return;
        }
        Object obj = abstractGame.getPlayerManager();
        GameTeam gameTeam = ((AbstractGamePlayerManager)obj).getPlayerTeam(uUID = this.field_3429.getUUID());
        if (gameTeam != null && abstractGame instanceof DominationGame) {
            BFUtils.method_2908(abstractGame, serverLevel, gameTeam, false);
        }
    }
}

