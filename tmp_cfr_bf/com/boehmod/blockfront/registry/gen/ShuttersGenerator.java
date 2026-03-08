/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.registry.gen;

import com.boehmod.blockfront.common.block.ShuttersBlock;
import com.boehmod.blockfront.registry.BFBlocks;
import com.boehmod.blockfront.registry.gen.provider.BFBlockStateProvider;
import com.boehmod.blockfront.registry.gen.provider.BFItemModelProvider;
import com.boehmod.blockfront.registry.gen.provider.BFLanguageProvider;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ShuttersGenerator {
    public static final ObjectList<ShuttersGenerator> INSTANCES = new ObjectArrayList();
    public final String baseId;
    public final DeferredHolder<Block, Block> full;
    public final DeferredHolder<Block, Block> left;
    public final DeferredHolder<Block, Block> right;

    public ShuttersGenerator(@Nonnull String baseId, @Nonnull BlockBehaviour.Properties blockProperties) {
        this.baseId = baseId;
        this.full = BFBlocks.DR_GEN.register(baseId, () -> new ShuttersBlock(blockProperties));
        this.left = BFBlocks.DR_GEN.register(baseId + "_left", () -> new ShuttersBlock(blockProperties));
        this.right = BFBlocks.DR_GEN.register(baseId + "_right", () -> new ShuttersBlock(blockProperties));
        INSTANCES.add((Object)this);
    }

    public void provideBlock(@Nonnull BFBlockStateProvider provider) {
        this.datagenBlock(provider, this.full, "");
        this.datagenBlock(provider, this.left, "_left");
        this.datagenBlock(provider, this.right, "_right");
    }

    public void provideItem(@Nonnull BFItemModelProvider provider) {
        provider.withExistingParent(this.full.getId().getPath(), this.full.getId().withPrefix("block/"));
        provider.withExistingParent(this.left.getId().getPath(), this.left.getId().withPrefix("block/"));
        provider.withExistingParent(this.right.getId().getPath(), this.right.getId().withPrefix("block/"));
    }

    public void provideLanguage(BFLanguageProvider provider) {
        String string = this.createBaseDisplayName();
        provider.add((Block)this.full.get(), string);
        provider.add((Block)this.left.get(), string + " (Left)");
        provider.add((Block)this.right.get(), string + " (Right)");
    }

    public String createBaseDisplayName() {
        return Arrays.stream(this.baseId.split("_")).map(word -> word.substring(0, 1).toUpperCase() + word.substring(1)).collect(Collectors.joining(" "));
    }

    private void datagenBlock(BFBlockStateProvider provider, DeferredHolder<Block, Block> block, String suffix) {
        provider.getVariantBuilder((Block)block.get()).partialState().with((Property)HorizontalDirectionalBlock.FACING, (Comparable)Direction.NORTH).addModels(new ConfiguredModel[]{this.getConfiguredModel(provider, block.getId().getPath(), "block/window_shutters_base" + suffix, 0)}).partialState().with((Property)HorizontalDirectionalBlock.FACING, (Comparable)Direction.EAST).addModels(new ConfiguredModel[]{this.getConfiguredModel(provider, block.getId().getPath(), "block/window_shutters_base" + suffix, 90)}).partialState().with((Property)HorizontalDirectionalBlock.FACING, (Comparable)Direction.SOUTH).addModels(new ConfiguredModel[]{this.getConfiguredModel(provider, block.getId().getPath(), "block/window_shutters_base" + suffix, 180)}).partialState().with((Property)HorizontalDirectionalBlock.FACING, (Comparable)Direction.WEST).addModels(new ConfiguredModel[]{this.getConfiguredModel(provider, block.getId().getPath(), "block/window_shutters_base" + suffix, 270)});
    }

    private ConfiguredModel getConfiguredModel(BFBlockStateProvider provider, String name, String parent, int rotationY) {
        return new ConfiguredModel((ModelFile)((BlockModelBuilder)((BlockModelBuilder)provider.models().withExistingParent(name, provider.modLoc(parent))).texture("0", provider.modLoc("block/" + this.baseId))).texture("particle", provider.modLoc("block/" + this.baseId)), 0, rotationY, false);
    }
}

