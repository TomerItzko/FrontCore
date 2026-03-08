/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.registry.gen;

import com.boehmod.blockfront.registry.BFBlocks;
import com.boehmod.blockfront.registry.gen.provider.BFBlockStateProvider;
import com.boehmod.blockfront.registry.gen.provider.BFItemModelProvider;
import com.boehmod.blockfront.registry.gen.provider.BFLanguageProvider;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public class WoodTypeGenerator {
    @NotNull
    public static final ObjectList<WoodTypeGenerator> INSTANCES = new ObjectArrayList();
    public final boolean doDatagen;
    @NotNull
    public final String baseId;
    @NotNull
    public final DeferredHolder<Block, ? extends Block> planks;
    @NotNull
    public final DeferredHolder<Block, ? extends SlabBlock> slab;
    @NotNull
    public final DeferredHolder<Block, ? extends StairBlock> stair;
    @NotNull
    public final DeferredHolder<Block, ? extends FenceBlock> fence;
    @NotNull
    public final DeferredHolder<Block, ? extends FenceGateBlock> fenceGate;
    @NotNull
    public final DeferredHolder<Block, ? extends ButtonBlock> button;
    @NotNull
    public final DeferredHolder<Block, ? extends PressurePlateBlock> pressurePlate;
    @Nullable
    public DeferredHolder<Block, ? extends RotatedPillarBlock> log;
    @Nullable
    public DeferredHolder<Block, ? extends RotatedPillarBlock> strippedLog;
    @Nullable
    public DeferredHolder<Block, ? extends LeavesBlock> leaves;

    public WoodTypeGenerator(@NotNull String baseId, boolean doDatagen, BlockBehaviour.Properties blockProperties, @Nullable BlockBehaviour.Properties leavesProperties) {
        this.doDatagen = doDatagen;
        this.baseId = baseId;
        this.planks = BFBlocks.DR_GEN.register(baseId + "_planks", () -> new Block(blockProperties));
        this.slab = BFBlocks.DR_GEN.register(baseId + "_slab", () -> new SlabBlock(blockProperties));
        this.stair = BFBlocks.DR_GEN.register(baseId + "_stair", () -> new StairBlock(((Block)this.planks.get()).defaultBlockState(), blockProperties));
        this.fence = BFBlocks.DR_GEN.register(baseId + "_fence", () -> new FenceBlock(blockProperties));
        this.fenceGate = BFBlocks.DR_GEN.register(baseId + "_fence_gate", () -> new FenceGateBlock(WoodType.OAK, blockProperties));
        if (doDatagen) {
            this.log = BFBlocks.DR_GEN.register(baseId + "_log", () -> new RotatedPillarBlock(blockProperties));
            this.strippedLog = BFBlocks.DR_GEN.register("stripped_" + baseId + "_log", () -> new RotatedPillarBlock(blockProperties));
            this.leaves = BFBlocks.DR_GEN.register(baseId + "_leaves", () -> new LeavesBlock(Objects.requireNonNull(leavesProperties).noOcclusion()));
        }
        this.button = BFBlocks.DR_GEN.register(baseId + "_button", () -> new ButtonBlock(BlockSetType.OAK, 30, blockProperties.noCollission()));
        this.pressurePlate = BFBlocks.DR_GEN.register(baseId + "_pressure_plate", () -> new PressurePlateBlock(BlockSetType.OAK, blockProperties.noCollission()));
        INSTANCES.add((Object)this);
    }

    public void provideBlock(@NotNull BFBlockStateProvider provider) {
        provider.simpleBlock((Block)this.planks.get());
        provider.slabBlock((SlabBlock)this.slab.get(), provider.blockTexture((Block)this.planks.get()), provider.blockTexture((Block)this.planks.get()));
        provider.stairsBlock((StairBlock)this.stair.get(), provider.blockTexture((Block)this.planks.get()));
        provider.fenceBlock((FenceBlock)this.fence.get(), provider.blockTexture((Block)this.planks.get()));
        provider.fenceGateBlock((FenceGateBlock)this.fenceGate.get(), provider.blockTexture((Block)this.planks.get()));
        provider.buttonBlock((ButtonBlock)this.button.get(), provider.blockTexture((Block)this.planks.get()));
        provider.pressurePlateBlock((PressurePlateBlock)this.pressurePlate.get(), provider.blockTexture((Block)this.planks.get()));
        if (this.doDatagen) {
            provider.logBlock((RotatedPillarBlock)Objects.requireNonNull(this.log).get());
            provider.logBlock((RotatedPillarBlock)Objects.requireNonNull(this.strippedLog).get());
            provider.simpleBlock((Block)Objects.requireNonNull(this.leaves).get(), new ConfiguredModel[]{new ConfiguredModel((ModelFile)((BlockModelBuilder)provider.models().withExistingParent(this.leaves.getId().getPath(), "minecraft:block/leaves")).texture("all", ResourceLocation.fromNamespaceAndPath((String)"bf", (String)("block/" + this.baseId + "_leaves"))))});
        }
    }

    public void provideItem(@NotNull BFItemModelProvider provider) {
        provider.withExistingParent(this.planks.getId().getPath(), this.planks.getId().withPrefix("block/"));
        provider.withExistingParent(this.slab.getId().getPath(), this.slab.getId().withPrefix("block/"));
        provider.withExistingParent(this.stair.getId().getPath(), this.stair.getId().withPrefix("block/"));
        provider.fenceInventory(this.fence.getId().getPath(), this.planks.getId().withPrefix("block/"));
        provider.withExistingParent(this.fenceGate.getId().getPath(), this.fenceGate.getId().withPrefix("block/"));
        provider.buttonInventory(this.button.getId().getPath(), this.planks.getId().withPrefix("block/"));
        provider.withExistingParent(this.pressurePlate.getId().getPath(), this.pressurePlate.getId().withPrefix("block/"));
        if (this.doDatagen) {
            provider.withExistingParent(Objects.requireNonNull(this.log).getId().getPath(), this.log.getId().withPrefix("block/"));
            provider.withExistingParent(Objects.requireNonNull(this.strippedLog).getId().getPath(), this.strippedLog.getId().withPrefix("block/"));
            provider.withExistingParent(Objects.requireNonNull(this.leaves).getId().getPath(), this.leaves.getId().withPrefix("block/"));
        }
    }

    public void provideLanguage(BFLanguageProvider provider) {
        String string = this.createBaseDisplayName();
        provider.add((Block)this.planks.get(), string + " Planks");
        provider.add((Block)this.slab.get(), string + " Slab");
        provider.add((Block)this.stair.get(), string + " Stairs");
        provider.add((Block)this.fence.get(), string + " Fence");
        provider.add((Block)this.fenceGate.get(), string + " Fence Gate");
        provider.add((Block)this.button.get(), string + " Button");
        provider.add((Block)this.pressurePlate.get(), string + " Pressure Plate");
        if (this.doDatagen) {
            provider.add((Block)Objects.requireNonNull(this.log).get(), string + " Log");
            provider.add((Block)Objects.requireNonNull(this.strippedLog).get(), "Stripped " + string + " Log");
            provider.add((Block)Objects.requireNonNull(this.leaves).get(), string + " Leaves");
        }
    }

    @NotNull
    public String createBaseDisplayName() {
        return Arrays.stream(this.baseId.split("_")).map(word -> word.substring(0, 1).toUpperCase() + word.substring(1)).collect(Collectors.joining(" "));
    }
}

