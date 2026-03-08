/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.BlockItem
 *  net.minecraft.world.item.CreativeModeTab
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  net.neoforged.neoforge.registries.DeferredRegister
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.registry;

import com.boehmod.blockfront.common.item.GenericGeoItem;
import com.boehmod.blockfront.registry.BFBlocks;
import com.boehmod.blockfront.registry.BFItems;
import com.boehmod.blockfront.registry.gen.CrateGenerator;
import com.boehmod.blockfront.registry.gen.MultiformGenerator;
import com.boehmod.blockfront.registry.gen.PlanksGenerator;
import com.boehmod.blockfront.registry.gen.ShuttersGenerator;
import com.boehmod.blockfront.registry.gen.WoodTypeGenerator;
import java.util.Objects;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

public class BFCreativeTabs {
    @NotNull
    public static final DeferredRegister<CreativeModeTab> DR = DeferredRegister.create((ResourceKey)Registries.CREATIVE_MODE_TAB, (String)"bf");
    @NotNull
    private static final Component SHUTTERS_NAME = Component.translatable((String)"bf.tab.shutters", (Object[])new Object[]{"BlockFront"});
    @NotNull
    private static final Component PLANKS_NAME = Component.translatable((String)"bf.tab.planks", (Object[])new Object[]{"BlockFront"});
    @NotNull
    private static final Component CRATES_NAME = Component.translatable((String)"bf.tab.crates", (Object[])new Object[]{"BlockFront"});
    @NotNull
    private static final Component BUILDING_BLOCKS_NAME = Component.translatable((String)"bf.tab.building_blocks", (Object[])new Object[]{"BlockFront"});
    @NotNull
    private static final Component BLOCKS_NAME = Component.translatable((String)"bf.tab.blocks", (Object[])new Object[]{"BlockFront"});
    @NotNull
    private static final Component ITEMS_NAME = Component.translatable((String)"bf.tab.items", (Object[])new Object[]{"BlockFront"});
    @NotNull
    private static final Component WOODSET_NAME = Component.translatable((String)"bf.tab.woodset", (Object[])new Object[]{"BlockFront"});
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ITEM_TAB = DR.register("item_tab", () -> CreativeModeTab.builder().title(ITEMS_NAME).icon(() -> new ItemStack((ItemLike)BFItems.GUN_M1928A1_THOMPSON.get())).displayItems((itemDisplayParameters, output) -> BFItems.DR.getEntries().forEach(deferredHolder -> {
        Item item = (Item)deferredHolder.get();
        if (item instanceof BlockItem) {
            return;
        }
        if (item instanceof GenericGeoItem) {
            return;
        }
        output.accept((ItemLike)item);
    })).build());
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BLOCK_TAB = DR.register("block_tab", () -> CreativeModeTab.builder().title(BLOCKS_NAME).withTabsBefore(new ResourceLocation[]{ITEM_TAB.getId()}).icon(() -> new ItemStack((ItemLike)BFBlocks.OIL_BARREL_RED.get())).displayItems((itemDisplayParameters, output) -> BFBlocks.DR.getEntries().forEach(deferredHolder -> output.accept((ItemLike)deferredHolder.get()))).build());
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BUILDING_BLOCK_TAB = DR.register("building_block_tab", () -> CreativeModeTab.builder().title(BUILDING_BLOCKS_NAME).icon(() -> new ItemStack((ItemLike)BFBlocks.OZARK_SET.bricks.block.get())).withTabsBefore(new ResourceLocation[]{BLOCK_TAB.getId()}).displayItems((itemDisplayParameters, output) -> MultiformGenerator.INSTANCES.forEach(multiformGenerator -> output.accept((ItemLike)multiformGenerator.block.get()))).build());
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BUILDING_SLAB_TAB = DR.register("building_slab_tab", () -> CreativeModeTab.builder().title(BUILDING_BLOCKS_NAME).icon(() -> new ItemStack((ItemLike)BFBlocks.OZARK_SET.bricks.slab.get())).withTabsBefore(new ResourceLocation[]{BUILDING_BLOCK_TAB.getId()}).displayItems((itemDisplayParameters, output) -> MultiformGenerator.INSTANCES.forEach(multiformGenerator -> output.accept((ItemLike)multiformGenerator.slab.get()))).build());
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BUILDING_STAIR_TAB = DR.register("building_stair_tab", () -> CreativeModeTab.builder().title(BUILDING_BLOCKS_NAME).icon(() -> new ItemStack((ItemLike)BFBlocks.OZARK_SET.bricks.stair.get())).withTabsBefore(new ResourceLocation[]{BUILDING_SLAB_TAB.getId()}).displayItems((itemDisplayParameters, output) -> MultiformGenerator.INSTANCES.forEach(multiformGenerator -> output.accept((ItemLike)multiformGenerator.stair.get()))).build());
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BUILDING_WALL_TAB = DR.register("building_wall_tab", () -> CreativeModeTab.builder().title(BUILDING_BLOCKS_NAME).icon(() -> new ItemStack((ItemLike)BFBlocks.OZARK_SET.bricks.wall.get())).withTabsBefore(new ResourceLocation[]{BUILDING_STAIR_TAB.getId()}).displayItems((itemDisplayParameters, output) -> MultiformGenerator.INSTANCES.forEach(multiformGenerator -> output.accept((ItemLike)multiformGenerator.wall.get()))).build());
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CRATE_TAB = DR.register("crate_tab", () -> CreativeModeTab.builder().title(CRATES_NAME).icon(() -> new ItemStack((ItemLike)BFBlocks.CRATE.openEmpty.get())).withTabsBefore(new ResourceLocation[]{BUILDING_WALL_TAB.getId()}).displayItems((itemDisplayParameters, output) -> CrateGenerator.INSTANCES.forEach(crateGenerator -> {
        output.accept((ItemLike)crateGenerator.base.get());
        output.accept((ItemLike)crateGenerator.squint.get());
        output.accept((ItemLike)crateGenerator.small.get());
        output.accept((ItemLike)crateGenerator.openEmpty.get());
        output.accept((ItemLike)crateGenerator.openBullets.get());
        output.accept((ItemLike)crateGenerator.smallOpen.get());
        output.accept((ItemLike)crateGenerator.bigOpen.get());
    })).build());
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> PLANK_TAB = DR.register("plank_tab", () -> CreativeModeTab.builder().title(PLANKS_NAME).icon(() -> new ItemStack((ItemLike)BFBlocks.PLANK.pile0.get())).withTabsBefore(new ResourceLocation[]{CRATE_TAB.getId()}).displayItems((itemDisplayParameters, output) -> PlanksGenerator.INSTANCES.forEach(planksGenerator -> {
        output.accept((ItemLike)planksGenerator.leaning0.get());
        output.accept((ItemLike)planksGenerator.leaning1.get());
        output.accept((ItemLike)planksGenerator.leaning2.get());
        output.accept((ItemLike)planksGenerator.leaning3.get());
        output.accept((ItemLike)planksGenerator.leaning4.get());
        output.accept((ItemLike)planksGenerator.pile0.get());
        output.accept((ItemLike)planksGenerator.pile1.get());
        output.accept((ItemLike)planksGenerator.pile2.get());
        output.accept((ItemLike)planksGenerator.pile3.get());
        output.accept((ItemLike)planksGenerator.single1.get());
        output.accept((ItemLike)planksGenerator.single2.get());
        output.accept((ItemLike)planksGenerator.single3.get());
    })).build());
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> SHUTTER_TAB = DR.register("shutter_tab", () -> CreativeModeTab.builder().title(SHUTTERS_NAME).icon(() -> new ItemStack((ItemLike)BFBlocks.WINDOW_SHUTTERS.full.get())).withTabsBefore(new ResourceLocation[]{PLANK_TAB.getId()}).displayItems((itemDisplayParameters, output) -> ShuttersGenerator.INSTANCES.forEach(shuttersGenerator -> {
        output.accept((ItemLike)shuttersGenerator.full.get());
        output.accept((ItemLike)shuttersGenerator.left.get());
        output.accept((ItemLike)shuttersGenerator.right.get());
    })).build());
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> WOODSET_TAB = DR.register("woodset_tab", () -> CreativeModeTab.builder().title(WOODSET_NAME).icon(() -> new ItemStack((ItemLike)Objects.requireNonNull(BFBlocks.WALNUT_WOOD.log).get())).withTabsBefore(new ResourceLocation[]{SHUTTER_TAB.getId()}).displayItems((itemDisplayParameters, output) -> WoodTypeGenerator.INSTANCES.forEach(woodTypeGenerator -> {
        output.accept((ItemLike)woodTypeGenerator.planks.get());
        output.accept((ItemLike)woodTypeGenerator.slab.get());
        output.accept((ItemLike)woodTypeGenerator.stair.get());
        output.accept((ItemLike)woodTypeGenerator.fence.get());
        output.accept((ItemLike)woodTypeGenerator.fenceGate.get());
        output.accept((ItemLike)woodTypeGenerator.button.get());
        output.accept((ItemLike)woodTypeGenerator.pressurePlate.get());
        if (woodTypeGenerator.doDatagen) {
            output.accept((ItemLike)Objects.requireNonNull(woodTypeGenerator.log).get());
            output.accept((ItemLike)Objects.requireNonNull(woodTypeGenerator.strippedLog).get());
            output.accept((ItemLike)Objects.requireNonNull(woodTypeGenerator.leaves).get());
        }
    })).build());
}

