/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.registry.gen;

import com.boehmod.blockfront.common.block.CrateGunBlock;
import com.boehmod.blockfront.common.block.CrateOpenBlock;
import com.boehmod.blockfront.common.block.CrateSlabBlock;
import com.boehmod.blockfront.common.block.entity.CrateGunBlockEntity;
import com.boehmod.blockfront.registry.BFBlockEntityTypes;
import com.boehmod.blockfront.registry.BFBlocks;
import com.boehmod.blockfront.registry.gen.provider.BFBlockStateProvider;
import com.boehmod.blockfront.registry.gen.provider.BFItemModelProvider;
import com.boehmod.blockfront.registry.gen.provider.BFLanguageProvider;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.lang.invoke.StringConcatFactory;
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public class CrateGenerator {
    public static final ObjectList<CrateGenerator> INSTANCES = new ObjectArrayList();
    public final String baseId;
    public final DeferredHolder<Block, Block> base;
    public final DeferredHolder<Block, Block> squint;
    public final DeferredHolder<Block, Block> small;
    public final DeferredHolder<Block, Block> openEmpty;
    public final DeferredHolder<Block, Block> openBullets;
    public final DeferredHolder<Block, Block> smallOpen;
    public final DeferredHolder<Block, Block> bigOpen;

    public CrateGenerator(final @Nonnull String baseId, @Nonnull BlockBehaviour.Properties blockProperties) {
        this.baseId = baseId;
        this.base = BFBlocks.DR_GEN.register(baseId + "1", () -> new CrateSlabBlock(blockProperties));
        this.squint = BFBlocks.DR_GEN.register(baseId + "2", () -> new CrateSlabBlock(blockProperties));
        this.small = BFBlocks.DR_GEN.register("small_" + baseId, () -> new CrateSlabBlock(blockProperties));
        this.openEmpty = BFBlocks.DR_GEN.register(baseId + "_open1", () -> new CrateOpenBlock(blockProperties));
        this.openBullets = BFBlocks.DR_GEN.register(baseId + "_open2", () -> new CrateOpenBlock(blockProperties));
        this.smallOpen = BFBlocks.DR_GEN.register("small_" + baseId + "_open", () -> new CrateOpenBlock(blockProperties));
        this.bigOpen = BFBlocks.DR_GEN.register(baseId + "_big_open", () -> new CrateGunBlock(this, baseId, blockProperties){

            @Override
            @Nullable
            public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level2, @NotNull BlockState blockState2, @NotNull BlockEntityType<T> blockEntityType) {
                return BaseEntityBlock.createTickerHelper(blockEntityType, (BlockEntityType)((BlockEntityType)BFBlockEntityTypes.CRATES.get(baseId).get()), (level, blockPos, blockState, crateGunBlockEntity) -> CrateGunBlockEntity.tick(level, crateGunBlockEntity));
            }
        });
        INSTANCES.add((Object)this);
    }

    public void provideBlock(@Nonnull BFBlockStateProvider provider) {
        this.datagenSlabBase(provider, this.base, "1");
        this.datagenSlabBase(provider, this.squint, "2");
        this.datagenSmall(provider, this.small);
        this.datagenOpen(provider, this.openEmpty, "1");
        this.datagenOpen(provider, this.openBullets, "2");
        this.datagenSmallOpen(provider, this.smallOpen);
        this.datagenOpen(provider, this.bigOpen, "_big");
    }

    public void provideItem(@Nonnull BFItemModelProvider provider) {
        provider.withExistingParent(this.base.getId().getPath(), this.base.getId().withPrefix("block/"));
        provider.withExistingParent(this.squint.getId().getPath(), this.squint.getId().withPrefix("block/"));
        provider.withExistingParent(this.small.getId().getPath(), this.small.getId().withPrefix("block/"));
        provider.withExistingParent(this.openEmpty.getId().getPath(), this.openEmpty.getId().withPrefix("block/"));
        provider.withExistingParent(this.openBullets.getId().getPath(), this.openBullets.getId().withPrefix("block/"));
        provider.withExistingParent(this.smallOpen.getId().getPath(), this.smallOpen.getId().withPrefix("block/"));
        provider.withExistingParent(this.bigOpen.getId().getPath(), this.bigOpen.getId().withPrefix("block/"));
    }

    public void provideLanguage(BFLanguageProvider provider) {
        String string = this.createBaseDisplayName();
        provider.add((Block)this.base.get(), string);
        provider.add((Block)this.squint.get(), string + " (Squint)");
        provider.add((Block)this.openEmpty.get(), "Open " + string + " (Empty)");
        provider.add((Block)this.openBullets.get(), "Open " + string + " (Bullets)");
        provider.add((Block)this.bigOpen.get(), "Open " + string + " (Gun)");
        if (string.equals("Crate Black")) {
            provider.add((Block)this.small.get(), "Small Crate Oak");
            provider.add((Block)this.smallOpen.get(), "Small Crate Oak (Open)");
        } else {
            provider.add((Block)this.small.get(), "Small " + string);
            provider.add((Block)this.smallOpen.get(), "Small " + string + " (Open)");
        }
    }

    public String createBaseDisplayName() {
        return Arrays.stream(this.baseId.split("_")).map(word -> word.substring(0, 1).toUpperCase() + word.substring(1)).collect(Collectors.joining(" "));
    }

    private void datagenSlabBase(BFBlockStateProvider provider, DeferredHolder<Block, Block> block, String suffix) {
        provider.getVariantBuilder((Block)block.get()).partialState().with((Property)SlabBlock.TYPE, (Comparable)SlabType.BOTTOM).addModels(new ConfiguredModel[]{this.getConfiguredModel(provider, block.getId().getPath(), "block/crate_base" + suffix)}).partialState().with((Property)SlabBlock.TYPE, (Comparable)SlabType.TOP).addModels(new ConfiguredModel[]{this.getConfiguredModel(provider, block.getId().getPath(), "block/crate_base" + suffix)}).partialState().with((Property)SlabBlock.TYPE, (Comparable)SlabType.DOUBLE).addModels(new ConfiguredModel[]{this.getConfiguredModel(provider, block.getId().getPath() + "_double", "block/crate_double_base" + suffix), this.getConfiguredModel(provider, block.getId().getPath() + "_double_var", "block/crate_double_base" + suffix + "_var")});
    }

    private void datagenSmall(BFBlockStateProvider provider, DeferredHolder<Block, Block> block) {
        provider.getVariantBuilder((Block)block.get()).partialState().with((Property)SlabBlock.TYPE, (Comparable)SlabType.BOTTOM).addModels(new ConfiguredModel[]{this.getConfiguredModel(provider, block.getId().getPath(), "block/crate_base_small", "_small")}).partialState().with((Property)SlabBlock.TYPE, (Comparable)SlabType.TOP).addModels(new ConfiguredModel[]{this.getConfiguredModel(provider, block.getId().getPath(), "block/crate_base_small", "_small")}).partialState().with((Property)SlabBlock.TYPE, (Comparable)SlabType.DOUBLE).addModels(new ConfiguredModel[]{this.getConfiguredModel(provider, block.getId().getPath() + "_double", "block/crate_double_base_small", "_small")});
    }

    private void datagenOpen(BFBlockStateProvider provider, DeferredHolder<Block, Block> block, String suffix) {
        provider.horizontalBlock((Block)block.get(), this.getConfiguredModel((BFBlockStateProvider)provider, (String)block.getId().getPath(), (String)((Object)StringConcatFactory.makeConcatWithConstants("makeConcatWithConstants", new Object[]{"block/crate_open_base\u0001"}, (String)suffix))).model);
    }

    private void datagenSmallOpen(BFBlockStateProvider provider, DeferredHolder<Block, Block> block) {
        provider.horizontalBlock((Block)block.get(), this.getConfiguredModel((BFBlockStateProvider)provider, (String)block.getId().getPath(), (String)"block/crate_open_base_small", (String)"_small").model);
    }

    private ConfiguredModel getConfiguredModel(BFBlockStateProvider provider, String name, String parent, String suffix) {
        return new ConfiguredModel((ModelFile)((BlockModelBuilder)((BlockModelBuilder)provider.models().withExistingParent(name, provider.modLoc(parent))).texture("0", provider.modLoc("block/" + this.baseId + suffix))).texture("particle", provider.modLoc("block/" + this.baseId + suffix)));
    }

    private ConfiguredModel getConfiguredModel(BFBlockStateProvider provider, String name, String parent) {
        return this.getConfiguredModel(provider, name, parent, "");
    }
}

