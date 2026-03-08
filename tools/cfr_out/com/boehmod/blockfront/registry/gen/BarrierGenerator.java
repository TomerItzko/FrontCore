/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  it.unimi.dsi.fastutil.objects.ObjectList
 *  net.minecraft.world.level.block.Block
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.registry.gen;

import com.boehmod.blockfront.common.block.BarrierBlock;
import com.boehmod.blockfront.common.block.BarrierSlabBlock;
import com.boehmod.blockfront.common.block.BarrierStairBlock;
import com.boehmod.blockfront.common.block.BarrierWallBlock;
import com.boehmod.blockfront.common.block.base.IBarrierPredicate;
import com.boehmod.blockfront.registry.BFBlocks;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public class BarrierGenerator {
    @NotNull
    public static final ObjectList<BarrierGenerator> INSTANCES = new ObjectArrayList();
    @NotNull
    public final DeferredHolder<Block, Block> block;
    @NotNull
    public final DeferredHolder<Block, Block> slab;
    @NotNull
    public final DeferredHolder<Block, Block> stair;
    @NotNull
    public final DeferredHolder<Block, Block> wall;

    public BarrierGenerator(@NotNull String id, boolean bl, boolean bl2, @NotNull IBarrierPredicate predicate) {
        this.block = BFBlocks.DR.register(id, () -> new BarrierBlock(bl, bl2, predicate));
        this.slab = BFBlocks.DR.register(id + "_slab", () -> new BarrierSlabBlock(bl, bl2, predicate));
        this.stair = BFBlocks.DR.register(id + "_stair", () -> new BarrierStairBlock(((Block)this.block.get()).defaultBlockState(), bl, bl2, predicate));
        this.wall = BFBlocks.DR.register(id + "_wall", () -> new BarrierWallBlock(bl, bl2, predicate));
        INSTANCES.add((Object)this);
    }
}

