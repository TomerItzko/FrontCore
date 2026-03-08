/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.PathfinderMob
 *  net.minecraft.world.level.Level
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.entity;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.GameHolder;
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.util.BFLog;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class IntegratedGameEntity
extends PathfinderMob
implements GameHolder {
    @Nullable
    protected AbstractGame<?, ?, ?> game;

    protected IntegratedGameEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    @Nullable
    public AbstractGame<?, ?, ?> getGame() {
        return this.game;
    }

    @Override
    public void setGame(@Nullable AbstractGame<?, ?, ?> game) {
        this.game = game;
    }

    public void method_1937(@NotNull ServerLevel serverLevel) {
        if (!serverLevel.getServer().isDedicatedServer()) {
            return;
        }
        if (this.game == null || this.game.getStatus() == GameStatus.POST_GAME || this.game.getStatus() == GameStatus.IDLE) {
            BFLog.logError("Failed to load match for mini-game mob '" + this.getId() + "'. Discarding.", new Object[0]);
            this.discard();
        }
    }

    public void baseTick() {
        super.baseTick();
        Level level = this.level();
        if (level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level;
            this.method_1937(serverLevel);
        }
    }

    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putUUID("gameId", this.game != null ? this.game.getUUID() : UUID.randomUUID());
    }

    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is null!";
        if (compoundTag.contains("gameId")) {
            this.setGame(bFAbstractManager.retrieveGame(compoundTag.getUUID("gameId")));
        }
    }

    public void checkDespawn() {
    }
}

