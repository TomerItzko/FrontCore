/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.core.BlockPos
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.block.state.BlockState
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.block.entity;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.block.entity.AbstractCrateGunBlockEntity;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.tag.IHasCrateGuns;
import com.boehmod.blockfront.registry.BFBlockEntityTypes;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class CrateGunBlockEntity
extends AbstractCrateGunBlockEntity {
    private static final int field_2460 = 200;
    private static final int field_2461 = 200;
    private static final float field_2458 = 512.0f;
    private int refreshTimer = 0;
    private int updateStateTimer = 0;
    private int nearestGameTimer = 0;
    @Nullable
    private AbstractGame<?, ?, ?> nearestGame = null;

    public CrateGunBlockEntity(String id, @NotNull BlockPos pos, @NotNull BlockState state) {
        super((BlockEntityType)BFBlockEntityTypes.CRATES.get(id).get(), pos, state);
    }

    public static void tick(@NotNull Level level, @NotNull CrateGunBlockEntity crate) {
        AbstractGame<?, ?, ?> abstractGame;
        if (level.isClientSide()) {
            return;
        }
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Client mod manager is null";
        if (crate.nearestGameTimer-- <= 0) {
            crate.nearestGameTimer = 200;
            crate.nearestGame = bFAbstractManager.getNearestActiveGame(crate.getBlockPos(), 500.0);
        }
        if (!((abstractGame = crate.nearestGame) instanceof IHasCrateGuns)) {
            return;
        }
        IHasCrateGuns iHasCrateGuns = (IHasCrateGuns)((Object)abstractGame);
        if (crate.refreshTimer-- <= 0) {
            crate.refreshTimer = iHasCrateGuns.getCrateRefreshInterval(crate);
            iHasCrateGuns.putCrateItem(crate);
            crate.updateState();
        }
        if (crate.updateStateTimer-- <= 0) {
            crate.updateStateTimer = 200;
            level.sendBlockUpdated(crate.getBlockPos(), crate.getBlockState(), crate.getBlockState(), 2);
            crate.sync(crate.nearestGame);
        }
    }

    @Override
    public void putItem(@NotNull ItemStack itemStack) {
        super.putItem(itemStack);
        this.updateState();
    }

    public void updateState() {
        this.updateStateTimer = 0;
    }

    @Nullable
    public AbstractGame<?, ?, ?> getNearestActiveGame() {
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Client mod manager is null";
        return bFAbstractManager.getNearestActiveGame(this.getBlockPos(), 512.0);
    }
}

