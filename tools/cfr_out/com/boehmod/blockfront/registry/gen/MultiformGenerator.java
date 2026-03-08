/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  it.unimi.dsi.fastutil.objects.ObjectList
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.SlabBlock
 *  net.minecraft.world.level.block.StairBlock
 *  net.minecraft.world.level.block.WallBlock
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.neoforged.neoforge.client.model.generators.BlockModelBuilder
 *  net.neoforged.neoforge.client.model.generators.ConfiguredModel
 *  net.neoforged.neoforge.client.model.generators.ModelFile
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.boehmod.blockfront.registry.gen;

import com.boehmod.blockfront.registry.BFBlocks;
import com.boehmod.blockfront.registry.gen.provider.BFBlockStateProvider;
import com.boehmod.blockfront.registry.gen.provider.BFItemModelProvider;
import com.boehmod.blockfront.registry.gen.provider.BFLanguageProvider;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MultiformGenerator {
    @NotNull
    public static final ObjectList<MultiformGenerator> INSTANCES = new ObjectArrayList();
    @NotNull
    private static final List<String> EXCLUDE_LIST = Arrays.asList("deepslate_cobbled_tiles", "feldspar_bricks", "limestone_polished", "limestone_polished_brick", "stone_brick", "stone_cobbled", "stone_cracked_brick", "stone_stone");
    public final boolean doDatagen;
    public final boolean mirrorVariance;
    @NotNull
    public final String baseId;
    @NotNull
    public final DeferredHolder<Block, ? extends Block> block;
    @NotNull
    public final DeferredHolder<Block, ? extends SlabBlock> slab;
    @NotNull
    public final DeferredHolder<Block, ? extends StairBlock> stair;
    @NotNull
    public final DeferredHolder<Block, ? extends WallBlock> wall;

    @Nullable
    public static MultiformGenerator create(@NotNull String id, @NotNull BlockBehaviour.Properties blockProperties, boolean mirrorVariance) {
        if (EXCLUDE_LIST.contains(id)) {
            return null;
        }
        return new MultiformGenerator(id, blockProperties, true, mirrorVariance);
    }

    public MultiformGenerator(@NotNull String id, @NotNull BlockBehaviour.Properties properties) {
        this(id, properties, true, false);
    }

    public MultiformGenerator(@NotNull String baseId, @NotNull BlockBehaviour.Properties blockProperties, boolean doDatagen, boolean mirrorVariance) {
        this.baseId = baseId;
        this.doDatagen = doDatagen;
        this.mirrorVariance = mirrorVariance;
        this.block = BFBlocks.DR_GEN.register(baseId, () -> new Block(blockProperties));
        this.slab = BFBlocks.DR_GEN.register(baseId + "_slab", () -> new SlabBlock(blockProperties));
        this.stair = BFBlocks.DR_GEN.register(baseId + "_stair", () -> new StairBlock(((Block)this.block.get()).defaultBlockState(), blockProperties));
        this.wall = BFBlocks.DR_GEN.register(baseId + "_wall", () -> new WallBlock(blockProperties));
        INSTANCES.add((Object)this);
    }

    public void provideBlock(@NotNull BFBlockStateProvider provider) {
        if (this.doDatagen && this.mirrorVariance) {
            provider.getVariantBuilder((Block)this.block.get()).partialState().addModels(new ConfiguredModel[]{this.getConfiguredModel(provider, this.block.getId().getPath(), false, 0), this.getConfiguredModel(provider, this.block.getId().getPath() + "_mirrored", true, 0), this.getConfiguredModel(provider, this.block.getId().getPath(), false, 180), this.getConfiguredModel(provider, this.block.getId().getPath() + "_mirrored", true, 180)});
        } else if (this.doDatagen) {
            provider.simpleBlock((Block)this.block.get());
        }
        provider.slabBlock((SlabBlock)this.slab.get(), provider.blockTexture((Block)this.block.get()), provider.blockTexture((Block)this.block.get()));
        provider.stairsBlock((StairBlock)this.stair.get(), provider.blockTexture((Block)this.block.get()));
        provider.wallBlock((WallBlock)this.wall.get(), provider.blockTexture((Block)this.block.get()));
    }

    public void provideItem(@NotNull BFItemModelProvider provider) {
        provider.withExistingParent(this.block.getId().getPath(), this.block.getId().withPrefix("block/"));
        provider.withExistingParent(this.slab.getId().getPath(), this.slab.getId().withPrefix("block/"));
        provider.withExistingParent(this.stair.getId().getPath(), this.stair.getId().withPrefix("block/"));
        provider.wallInventory(this.wall.getId().getPath(), this.block.getId().withPrefix("block/"));
    }

    public void provideLanguage(BFLanguageProvider provider) {
        String string = this.createBaseDisplayName();
        provider.add((Block)this.block.get(), string);
        provider.add((Block)this.slab.get(), string + " Slab");
        provider.add((Block)this.stair.get(), string + " Stairs");
        provider.add((Block)this.wall.get(), string + " Wall");
    }

    @NotNull
    public String createBaseDisplayName() {
        return Arrays.stream(this.baseId.split("_")).map(word -> word.substring(0, 1).toUpperCase() + word.substring(1)).collect(Collectors.joining(" "));
    }

    @NotNull
    public ConfiguredModel getConfiguredModel(@NotNull BFBlockStateProvider provider, @NotNull String name, boolean mirrored, int rotationY) {
        return new ConfiguredModel((ModelFile)((BlockModelBuilder)provider.models().withExistingParent(name, mirrored ? "minecraft:block/cube_mirrored_all" : "minecraft:block/cube_all")).texture("all", provider.modLoc("block/" + this.baseId)), 0, rotationY, false);
    }
}

