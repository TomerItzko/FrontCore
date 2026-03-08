/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.HolderLookup$Provider
 *  net.minecraft.data.PackOutput
 *  net.minecraft.tags.BlockTags
 *  net.minecraft.world.level.block.Block
 *  net.neoforged.neoforge.common.data.BlockTagsProvider
 *  net.neoforged.neoforge.common.data.ExistingFileHelper
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.registry.gen.provider;

import com.boehmod.blockfront.registry.BFBlocks;
import com.boehmod.blockfront.registry.gen.BarrierGenerator;
import com.boehmod.blockfront.registry.gen.MultiformGenerator;
import com.boehmod.blockfront.registry.gen.WoodTypeGenerator;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

public class BFBlockTagsProvider
extends BlockTagsProvider {
    public BFBlockTagsProvider(@NotNull PackOutput packOutput, CompletableFuture<HolderLookup.Provider> completableFuture, @NotNull ExistingFileHelper existingFileHelper) {
        super(packOutput, completableFuture, "bf", existingFileHelper);
    }

    protected void addTags(@NotNull HolderLookup.Provider provider) {
        for (Object object : MultiformGenerator.INSTANCES) {
            this.tag(BlockTags.WALLS).add((Object)((Block)((MultiformGenerator)object).wall.get()));
        }
        for (Object object : BarrierGenerator.INSTANCES) {
            this.tag(BlockTags.WALLS).add((Object)((Block)((BarrierGenerator)object).wall.get()));
        }
        for (Object object : WoodTypeGenerator.INSTANCES) {
            this.tag(BlockTags.FENCES).add((Object)((Block)((WoodTypeGenerator)object).fence.get()));
            this.tag(BlockTags.FENCE_GATES).add((Object)((Block)((WoodTypeGenerator)object).fenceGate.get()));
        }
        this.tag(BlockTags.CLIMBABLE).add((Object)((Block)BFBlocks.METAL_LADDER.get()));
    }
}

