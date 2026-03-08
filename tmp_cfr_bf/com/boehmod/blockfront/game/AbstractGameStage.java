/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game;

import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.net.packet.BFGamePacket;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.game.TeamJoinType;
import com.boehmod.blockfront.util.PacketUtils;
import java.util.Set;
import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractGameStage<G extends AbstractGame<G, P, ?>, P extends AbstractGamePlayerManager<G>> {
    private static final int field_3777 = 20;
    private int ticks = 0;

    public void update(@NotNull G game, @NotNull P playerManager, @NotNull BFAbstractManager<?, ?, ?> manager, @NotNull PlayerDataHandler<?> dataHandler, @NotNull ServerLevel level, @NotNull Set<UUID> players) {
        if (this.ticks++ < 20) {
            return;
        }
        this.ticks = 0;
        this.onSecondPassed(game, playerManager, manager, dataHandler, level, players);
        PacketUtils.sendToGamePlayers(new BFGamePacket((AbstractGame<?, ?, ?>)game, false), game);
    }

    public abstract void stageStarted(@NotNull G var1, @NotNull P var2, @NotNull BFAbstractManager<?, ?, ?> var3, @NotNull PlayerDataHandler<?> var4, @NotNull ServerLevel var5, @NotNull Set<UUID> var6);

    public abstract void onSecondPassed(@NotNull G var1, @NotNull P var2, @NotNull BFAbstractManager<?, ?, ?> var3, @NotNull PlayerDataHandler<?> var4, @NotNull ServerLevel var5, @NotNull Set<UUID> var6);

    public abstract boolean isStageOver(@NotNull G var1, @NotNull P var2, @NotNull BFAbstractManager<?, ?, ?> var3, @NotNull ServerLevel var4, @NotNull Set<UUID> var5);

    public abstract void stageEnded(@NotNull G var1, @NotNull P var2, @NotNull BFAbstractManager<?, ?, ?> var3, @NotNull PlayerDataHandler<?> var4, @NotNull ServerLevel var5, @NotNull Set<UUID> var6);

    public abstract void method_3911(@NotNull G var1, @NotNull ServerLevel var2, @NotNull Set<UUID> var3);

    @NotNull
    public abstract AbstractGameStage<G, P> createNextStage(@NotNull G var1);

    @NotNull
    public abstract GameStatus getStatus();

    public abstract void onPlayerJoin(@NotNull PlayerDataHandler<?> var1, @NotNull BFAbstractManager<?, ?, ?> var2, @NotNull TeamJoinType var3, @NotNull ServerLevel var4, @NotNull ServerPlayer var5, @NotNull G var6, @NotNull UUID var7);
}

