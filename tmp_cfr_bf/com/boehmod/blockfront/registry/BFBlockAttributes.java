/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.registry;

import com.boehmod.blockfront.registry.BFBlockSoundAttributes;
import com.boehmod.blockfront.registry.BFBlockTraversableAttributes;
import com.boehmod.blockfront.registry.BFBlocks;
import com.boehmod.blockfront.registry.custom.BlockAttribute;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

public class BFBlockAttributes {
    @NotNull
    public static final DeferredRegister<BlockAttribute> DR = DeferredRegister.create(BlockAttribute.REGISTRY, (String)"bf");
    @NotNull
    public static final BlockAttribute field_4370 = new BlockAttribute().method_4244(BFBlockSoundAttributes.STONE);
    @NotNull
    public static final DeferredHolder<BlockAttribute, ? extends BlockAttribute> HARD_METAL = DR.register("hard_metal", () -> new BlockAttribute().method_4243(BFBlockTraversableAttributes.SOLID).method_4244(BFBlockSoundAttributes.HARD_METAL).method_4242(SoundType.METAL));
    @NotNull
    public static final DeferredHolder<BlockAttribute, ? extends BlockAttribute> SOFT_METAL = DR.register("soft_metal", () -> new BlockAttribute().method_4243(BFBlockTraversableAttributes.SOFT_METAL).method_4244(BFBlockSoundAttributes.METAL_TRANSLUCENT).method_4241(Blocks.IRON_BARS));
    @NotNull
    public static final DeferredHolder<BlockAttribute, ? extends BlockAttribute> SOFT_STONE = DR.register("soft_stone", () -> new BlockAttribute().method_4243(BFBlockTraversableAttributes.SOFT_STONE).method_4244(BFBlockSoundAttributes.STONE).method_4241(Blocks.STONE_BUTTON));
    @NotNull
    public static final DeferredHolder<BlockAttribute, ? extends BlockAttribute> SOFT_DIRT = DR.register("soft_dirt", () -> new BlockAttribute().method_4243(BFBlockTraversableAttributes.SOLID).method_4244(BFBlockSoundAttributes.DIRT).method_4242(SoundType.GRAVEL).method_4242(SoundType.GRASS));
    @NotNull
    public static final DeferredHolder<BlockAttribute, ? extends BlockAttribute> SAND = DR.register("sand", () -> new BlockAttribute().method_4243(BFBlockTraversableAttributes.SOLID).method_4244(BFBlockSoundAttributes.SAND).method_4242(SoundType.SAND).method_4242(SoundType.SOUL_SAND).method_4242(SoundType.SUSPICIOUS_SAND));
    @NotNull
    public static final DeferredHolder<BlockAttribute, ? extends BlockAttribute> PLANTS = DR.register("plants", () -> new BlockAttribute().method_4243(BFBlockTraversableAttributes.AIR).method_4241(Blocks.TALL_GRASS).method_4241(Blocks.DEAD_BRAIN_CORAL).method_4241(Blocks.DEAD_BUBBLE_CORAL).method_4241(Blocks.DEAD_FIRE_CORAL).method_4241(Blocks.DEAD_HORN_CORAL).method_4241(Blocks.DEAD_TUBE_CORAL).method_4241(Blocks.DEAD_TUBE_CORAL_FAN).method_4241(Blocks.DEAD_BRAIN_CORAL_FAN).method_4241(Blocks.DEAD_BUBBLE_CORAL_FAN).method_4241(Blocks.DEAD_FIRE_CORAL_FAN).method_4241(Blocks.DEAD_HORN_CORAL_FAN));
    @NotNull
    public static final DeferredHolder<BlockAttribute, ? extends BlockAttribute> DENSE_PLANTS = DR.register("dense_plants", () -> new BlockAttribute().method_4243(BFBlockTraversableAttributes.DENSE_PLANT).method_4241(Blocks.COBWEB).method_4241(Blocks.OAK_LEAVES).method_4241(Blocks.SPRUCE_LEAVES).method_4241(Blocks.BIRCH_LEAVES).method_4241(Blocks.JUNGLE_LEAVES).method_4241(Blocks.ACACIA_LEAVES).method_4241(Blocks.CHERRY_LEAVES).method_4241(Blocks.DARK_OAK_LEAVES).method_4241(Blocks.MANGROVE_LEAVES).method_4241(Blocks.AZALEA_LEAVES).method_4241(Blocks.HAY_BLOCK).method_4241(Blocks.MANGROVE_ROOTS));
    @NotNull
    public static final DeferredHolder<BlockAttribute, ? extends BlockAttribute> BARRIER = DR.register("barrier", () -> new BlockAttribute().method_4243(BFBlockTraversableAttributes.AIR).method_4241(Blocks.BARRIER));
    @NotNull
    public static final DeferredHolder<BlockAttribute, ? extends BlockAttribute> CAKE = DR.register("cake", () -> new BlockAttribute().method_4243(BFBlockTraversableAttributes.AIR).method_4244(BFBlockSoundAttributes.DIRT).method_4241(Blocks.CAKE));
    @NotNull
    public static final DeferredHolder<BlockAttribute, ? extends BlockAttribute> SNOW = DR.register("snow", () -> new BlockAttribute().method_4243(BFBlockTraversableAttributes.SNOW).method_4244(BFBlockSoundAttributes.SNOW).method_4241(Blocks.SNOW).method_4241(Blocks.SNOW_BLOCK));
    @NotNull
    public static final DeferredHolder<BlockAttribute, ? extends BlockAttribute> ICE = DR.register("ice", () -> new BlockAttribute().method_4243(BFBlockTraversableAttributes.AIR).method_4244(BFBlockSoundAttributes.GLASS).method_4241(Blocks.ICE));
    @NotNull
    public static final DeferredHolder<BlockAttribute, ? extends BlockAttribute> HARD_ICE = DR.register("hard_ice", () -> new BlockAttribute().method_4243(BFBlockTraversableAttributes.SOLID).method_4244(BFBlockSoundAttributes.GLASS).method_4241(Blocks.BLUE_ICE).method_4241(Blocks.FROSTED_ICE).method_4241(Blocks.PACKED_ICE));
    @NotNull
    public static final DeferredHolder<BlockAttribute, ? extends BlockAttribute> GLASS = DR.register("glass", () -> new BlockAttribute().method_4243(BFBlockTraversableAttributes.AIR).method_4244(BFBlockSoundAttributes.GLASS).method_4242(SoundType.GLASS).method_4241(Blocks.GLASS).method_4241(Blocks.GLASS_PANE).method_4241(Blocks.WHITE_STAINED_GLASS).method_4241(Blocks.ORANGE_STAINED_GLASS).method_4241(Blocks.MAGENTA_STAINED_GLASS).method_4241(Blocks.LIGHT_BLUE_STAINED_GLASS).method_4241(Blocks.YELLOW_STAINED_GLASS).method_4241(Blocks.LIME_STAINED_GLASS).method_4241(Blocks.PINK_STAINED_GLASS).method_4241(Blocks.GRAY_STAINED_GLASS).method_4241(Blocks.LIGHT_GRAY_STAINED_GLASS).method_4241(Blocks.CYAN_STAINED_GLASS).method_4241(Blocks.PURPLE_STAINED_GLASS).method_4241(Blocks.BLUE_STAINED_GLASS).method_4241(Blocks.BROWN_STAINED_GLASS).method_4241(Blocks.GREEN_STAINED_GLASS).method_4241(Blocks.RED_STAINED_GLASS).method_4241(Blocks.BLACK_STAINED_GLASS));
    @NotNull
    public static final DeferredHolder<BlockAttribute, ? extends BlockAttribute> BARBED_WIRE = DR.register("barbed_wire", () -> new BlockAttribute().method_4243(BFBlockTraversableAttributes.AIR).method_4241((Block)BFBlocks.BARBED_WIRE_END.get()).method_4241((Block)BFBlocks.BARBED_WIRE_MID.get()).method_4241((Block)BFBlocks.BARBED_WIRE_MID_CROSS.get()));
    @NotNull
    public static final DeferredHolder<BlockAttribute, ? extends BlockAttribute> MARBLE_RAILING = DR.register("marble_railing", () -> new BlockAttribute().method_4243(BFBlockTraversableAttributes.AIR).method_4241((Block)BFBlocks.MARBLE_RAILING.get()));
    @NotNull
    public static final DeferredHolder<BlockAttribute, ? extends BlockAttribute> WOOD = DR.register("wood", () -> new BlockAttribute().method_4243(BFBlockTraversableAttributes.SOFT_WOOD).method_4244(BFBlockSoundAttributes.WOOD).method_4242(SoundType.STEM).method_4242(SoundType.WOOD).method_4242(SoundType.BAMBOO_WOOD).method_4242(SoundType.CHERRY_WOOD).method_4242(SoundType.NETHER_WOOD));
    @NotNull
    public static final DeferredHolder<BlockAttribute, ? extends BlockAttribute> WOOL = DR.register("wool", () -> new BlockAttribute().method_4243(BFBlockTraversableAttributes.WOOL).method_4244(BFBlockSoundAttributes.WOOL).method_4242(SoundType.WOOL));
    @NotNull
    public static final DeferredHolder<BlockAttribute, ? extends BlockAttribute> FLESH = DR.register("flesh", () -> new BlockAttribute().method_4244(BFBlockSoundAttributes.FLESH).method_4242(SoundType.NETHERRACK));

    @Nullable
    private static BlockAttribute method_4616(@NotNull Block block) {
        return BlockAttribute.field_4088.get(block);
    }

    @Nullable
    private static BlockAttribute method_4617(@NotNull SoundType soundType) {
        return BlockAttribute.field_4089.get(soundType);
    }

    @NotNull
    public static BlockAttribute method_4614(@NotNull Level level, @NotNull BlockState blockState, @NotNull BlockPos blockPos) {
        BlockAttribute blockAttribute = BFBlockAttributes.method_4616(blockState.getBlock());
        if (blockAttribute != null) {
            return blockAttribute;
        }
        SoundType soundType = blockState.getSoundType((LevelReader)level, blockPos, null);
        BlockAttribute blockAttribute2 = BFBlockAttributes.method_4617(soundType);
        if (blockAttribute2 != null) {
            return blockAttribute2;
        }
        return field_4370;
    }
}

