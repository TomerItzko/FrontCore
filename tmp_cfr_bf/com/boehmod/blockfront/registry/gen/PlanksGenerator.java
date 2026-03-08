/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.registry.gen;

import com.boehmod.blockfront.common.block.LongPlanksBlock;
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

public class PlanksGenerator {
    public static final ObjectList<PlanksGenerator> INSTANCES = new ObjectArrayList();
    public final String baseId;
    public final DeferredHolder<Block, Block> leaning0;
    public final DeferredHolder<Block, Block> leaning1;
    public final DeferredHolder<Block, Block> leaning2;
    public final DeferredHolder<Block, Block> leaning3;
    public final DeferredHolder<Block, Block> leaning4;
    public final DeferredHolder<Block, Block> pile0;
    public final DeferredHolder<Block, Block> pile1;
    public final DeferredHolder<Block, Block> pile2;
    public final DeferredHolder<Block, Block> pile3;
    public final DeferredHolder<Block, Block> single1;
    public final DeferredHolder<Block, Block> single2;
    public final DeferredHolder<Block, Block> single3;

    public PlanksGenerator(@Nonnull String baseId, @Nonnull BlockBehaviour.Properties blockProperties) {
        this.baseId = baseId;
        this.leaning0 = BFBlocks.DR_GEN.register(baseId + "_leaning_0", () -> new LongPlanksBlock(blockProperties));
        this.leaning1 = BFBlocks.DR_GEN.register(baseId + "_leaning_1", () -> new LongPlanksBlock(blockProperties));
        this.leaning2 = BFBlocks.DR_GEN.register(baseId + "_leaning_2", () -> new LongPlanksBlock(blockProperties));
        this.leaning3 = BFBlocks.DR_GEN.register(baseId + "_leaning_3", () -> new LongPlanksBlock(blockProperties));
        this.leaning4 = BFBlocks.DR_GEN.register(baseId + "_leaning_4", () -> new LongPlanksBlock(blockProperties));
        this.pile0 = BFBlocks.DR_GEN.register(baseId + "_pile_0", () -> new LongPlanksBlock(blockProperties));
        this.pile1 = BFBlocks.DR_GEN.register(baseId + "_pile_1", () -> new LongPlanksBlock(blockProperties));
        this.pile2 = BFBlocks.DR_GEN.register(baseId + "_pile_2", () -> new LongPlanksBlock(blockProperties));
        this.pile3 = BFBlocks.DR_GEN.register(baseId + "_pile_3", () -> new LongPlanksBlock(blockProperties));
        this.single1 = BFBlocks.DR_GEN.register(baseId + "_single_0", () -> new LongPlanksBlock(blockProperties));
        this.single2 = BFBlocks.DR_GEN.register(baseId + "_single_1", () -> new LongPlanksBlock(blockProperties));
        this.single3 = BFBlocks.DR_GEN.register(baseId + "_single_2", () -> new LongPlanksBlock(blockProperties));
        INSTANCES.add((Object)this);
    }

    public void provideBlock(@Nonnull BFBlockStateProvider provider) {
        this.datagenBlock(provider, this.leaning0, "_leaning_0");
        this.datagenBlock(provider, this.leaning1, "_leaning_1");
        this.datagenBlock(provider, this.leaning2, "_leaning_2");
        this.datagenBlock(provider, this.leaning3, "_leaning_3");
        this.datagenBlock(provider, this.leaning4, "_leaning_4");
        this.datagenBlock(provider, this.pile0, "_pile_0");
        this.datagenBlock(provider, this.pile1, "_pile_1");
        this.datagenBlock(provider, this.pile2, "_pile_2");
        this.datagenBlock(provider, this.pile3, "_pile_3");
        this.datagenBlock(provider, this.single1, "_single_0");
        this.datagenBlock(provider, this.single2, "_single_1");
        this.datagenBlock(provider, this.single3, "_single_2");
    }

    public void provideItem(@Nonnull BFItemModelProvider provider) {
        provider.withExistingParent(this.leaning0.getId().getPath(), this.leaning0.getId().withPrefix("block/"));
        provider.withExistingParent(this.leaning1.getId().getPath(), this.leaning1.getId().withPrefix("block/"));
        provider.withExistingParent(this.leaning2.getId().getPath(), this.leaning2.getId().withPrefix("block/"));
        provider.withExistingParent(this.leaning3.getId().getPath(), this.leaning3.getId().withPrefix("block/"));
        provider.withExistingParent(this.leaning4.getId().getPath(), this.leaning4.getId().withPrefix("block/"));
        provider.withExistingParent(this.pile0.getId().getPath(), this.pile0.getId().withPrefix("block/"));
        provider.withExistingParent(this.pile1.getId().getPath(), this.pile1.getId().withPrefix("block/"));
        provider.withExistingParent(this.pile2.getId().getPath(), this.pile2.getId().withPrefix("block/"));
        provider.withExistingParent(this.pile3.getId().getPath(), this.pile3.getId().withPrefix("block/"));
        provider.withExistingParent(this.single1.getId().getPath(), this.single1.getId().withPrefix("block/"));
        provider.withExistingParent(this.single2.getId().getPath(), this.single2.getId().withPrefix("block/"));
        provider.withExistingParent(this.single3.getId().getPath(), this.single3.getId().withPrefix("block/"));
    }

    public void provideLanguage(BFLanguageProvider provider) {
        String string = this.createBaseDisplayName();
        provider.add((Block)this.leaning0.get(), string + " Leaning 0");
        provider.add((Block)this.leaning1.get(), string + " Leaning 1");
        provider.add((Block)this.leaning2.get(), string + " Leaning 2");
        provider.add((Block)this.leaning3.get(), string + " Leaning 3");
        provider.add((Block)this.leaning4.get(), string + " Leaning 4");
        provider.add((Block)this.pile0.get(), string + " Pile 0");
        provider.add((Block)this.pile1.get(), string + " Pile 1");
        provider.add((Block)this.pile2.get(), string + " Pile 2");
        provider.add((Block)this.pile3.get(), string + " Pile 3");
        provider.add((Block)this.single1.get(), string + " Single 0");
        provider.add((Block)this.single2.get(), string + " Single 1");
        provider.add((Block)this.single3.get(), string + " Single 2");
    }

    public String createBaseDisplayName() {
        return Arrays.stream(this.baseId.split("_")).map(word -> word.substring(0, 1).toUpperCase() + word.substring(1)).collect(Collectors.joining(" "));
    }

    private void datagenBlock(BFBlockStateProvider provider, DeferredHolder<Block, Block> block, String suffix) {
        provider.getVariantBuilder((Block)block.get()).partialState().with((Property)HorizontalDirectionalBlock.FACING, (Comparable)Direction.NORTH).addModels(new ConfiguredModel[]{this.getConfiguredModel(provider, block.getId().getPath(), "block/plank_base" + suffix, 180)}).partialState().with((Property)HorizontalDirectionalBlock.FACING, (Comparable)Direction.EAST).addModels(new ConfiguredModel[]{this.getConfiguredModel(provider, block.getId().getPath(), "block/plank_base" + suffix, 270)}).partialState().with((Property)HorizontalDirectionalBlock.FACING, (Comparable)Direction.SOUTH).addModels(new ConfiguredModel[]{this.getConfiguredModel(provider, block.getId().getPath(), "block/plank_base" + suffix, 0)}).partialState().with((Property)HorizontalDirectionalBlock.FACING, (Comparable)Direction.WEST).addModels(new ConfiguredModel[]{this.getConfiguredModel(provider, block.getId().getPath(), "block/plank_base" + suffix, 90)});
    }

    private ConfiguredModel getConfiguredModel(BFBlockStateProvider provider, String name, String parent, int rotationY) {
        return new ConfiguredModel((ModelFile)((BlockModelBuilder)((BlockModelBuilder)provider.models().withExistingParent(name, provider.modLoc(parent))).texture("0", provider.modLoc("block/" + this.baseId))).texture("particle", provider.modLoc("block/" + this.baseId)), 0, rotationY, false);
    }
}

