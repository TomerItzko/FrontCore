/*
 * Decompiled with CFR.
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
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class MatchEntity
extends Entity
implements GameHolder {
    @Nullable
    protected AbstractGame<?, ?, ?> game;

    public MatchEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            this.method_1936();
        }
    }

    public void method_1936() {
        Level level = this.level();
        if (level.getServer() == null || !level.getServer().isDedicatedServer()) {
            return;
        }
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is null!";
        if (this.game == null || bFAbstractManager.retrieveGame(this.game.getUUID()) == null || this.game.getStatus() == GameStatus.POST_GAME) {
            BFLog.log("Failed to load match for mini-game mob '" + this.getId() + "'. Discarding.", new Object[0]);
            this.discard();
        }
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

    public void defineSynchedData(@NotNull SynchedEntityData.Builder builder) {
    }

    protected void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        compoundTag.putUUID("gameId", this.game != null ? this.game.getUUID() : UUID.randomUUID());
    }

    protected void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is null!";
        if (compoundTag.contains("gameId")) {
            this.setGame(bFAbstractManager.retrieveGame(compoundTag.getUUID("gameId")));
        }
    }
}

