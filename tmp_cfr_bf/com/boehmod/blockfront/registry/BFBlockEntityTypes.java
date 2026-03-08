/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.registry;

import com.boehmod.blockfront.common.block.entity.BFBlockEntity;
import com.boehmod.blockfront.common.block.entity.CrateGunBlockEntity;
import com.boehmod.blockfront.common.block.entity.SwingingLogBlockEntity;
import com.boehmod.blockfront.registry.BFBlocks;
import com.boehmod.blockfront.registry.gen.CrateGenerator;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

public class BFBlockEntityTypes {
    @NotNull
    public static final DeferredRegister<BlockEntityType<?>> DR = DeferredRegister.create((ResourceKey)Registries.BLOCK_ENTITY_TYPE, (String)"bf");
    public static final Map<String, DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends CrateGunBlockEntity>>> CRATES = new HashMap();
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BFBlockEntity>> DECORATION_BLOCK;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BFBlockEntity>> GERMAN_RADAR;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends SwingingLogBlockEntity>> SWINGING_LOG;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BFBlockEntity>> FLAK88_SHIELD;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BFBlockEntity>> FLAK88_SHIELD_DESTROYED;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BFBlockEntity>> FLAK88_NOSHIELD;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BFBlockEntity>> FLAK88_NOSHIELD_DESTROYED;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BFBlockEntity>> TYPE96_SHIELD;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BFBlockEntity>> TYPE96_SHIELD_DESTROYED;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BFBlockEntity>> TYPE96_NOSHIELD;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BFBlockEntity>> TYPE96_NOSHIELD_DESTROYED;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BFBlockEntity>> BICYCLE;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BFBlockEntity>> P51_MUSTANG;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BFBlockEntity>> PZL11C;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BFBlockEntity>> JUNKERS_JU87;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BFBlockEntity>> A6M_ZERO;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BFBlockEntity>> A6M_ZERO_PARKED;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BFBlockEntity>> REGGIANE_RE2005;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BFBlockEntity>> SHERMAN_TANK;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BFBlockEntity>> RENAULT_FT_TANK;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BFBlockEntity>> SHERMAN_TANK_DESTROYED;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BFBlockEntity>> SHERMAN_TANK_SNORKEL;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BFBlockEntity>> SHERMAN_TANK_SNORKEL_DESTROYED;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BFBlockEntity>> TIGER_AUSF_H_TANK;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BFBlockEntity>> T34_TANK;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BFBlockEntity>> T34_TANK_DESTROYED;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BFBlockEntity>> PANZERIV_TANK;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BFBlockEntity>> PANZERIV_TANK_DESTROYED;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BFBlockEntity>> CHIHA_TANK;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BFBlockEntity>> CHIHA_TANK_DESTROYED;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BFBlockEntity>> KUROGANE;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BFBlockEntity>> KUBEL;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BFBlockEntity>> RENAULT_AHN;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BFBlockEntity>> CITROEN_11CV_TRACTION_AVANT;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BFBlockEntity>> CITROEN_11CV_TRACTION_AVANT_DESTROYED;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BFBlockEntity>> LANCIA_1ZM;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BFBlockEntity>> LANCIA_1ZM_DESTROYED;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BFBlockEntity>> WILLYS_JEEP;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BFBlockEntity>> WILLYS_JEEP_DESTROYED;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BFBlockEntity>> _150MM_TBTSK_C36_GUN;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BFBlockEntity>> LCVP;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BFBlockEntity>> LCVP_OPEN;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BFBlockEntity>> _7TP_PELICAN;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BFBlockEntity>> _7TP_PELICAN_DESTROYED;
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends BFBlockEntity>> WALL_FLAG_TALL;

    static {
        for (CrateGenerator crateGenerator : CrateGenerator.INSTANCES) {
            CRATES.put(crateGenerator.baseId, DR.register(crateGenerator.bigOpen.getId().getPath(), () -> BlockEntityType.Builder.of((blockPos, blockState) -> new CrateGunBlockEntity(crateGenerator.baseId, blockPos, blockState), (Block[])new Block[]{(Block)crateGenerator.bigOpen.get()}).build(null)));
        }
        DECORATION_BLOCK = DR.register(BFBlocks.DECORATION_BLOCK.getId().getPath(), () -> BlockEntityType.Builder.of((blockPos, blockState) -> new BFBlockEntity((BlockEntityType)DECORATION_BLOCK.get(), blockPos, blockState), (Block[])new Block[]{(Block)BFBlocks.DECORATION_BLOCK.get()}).build(null));
        GERMAN_RADAR = DR.register(BFBlocks.GERMAN_RADAR.getId().getPath(), () -> BlockEntityType.Builder.of((blockPos, blockState) -> new BFBlockEntity((BlockEntityType)GERMAN_RADAR.get(), blockPos, blockState), (Block[])new Block[]{(Block)BFBlocks.GERMAN_RADAR.get()}).build(null));
        SWINGING_LOG = DR.register(BFBlocks.SWINGING_LOG.getId().getPath(), () -> BlockEntityType.Builder.of((blockPos, blockState) -> new SwingingLogBlockEntity((BlockEntityType)SWINGING_LOG.get(), blockPos, blockState), (Block[])new Block[]{(Block)BFBlocks.SWINGING_LOG.get()}).build(null));
        FLAK88_SHIELD = DR.register(BFBlocks.FLAK88_SHIELD.getId().getPath(), () -> BlockEntityType.Builder.of((blockPos, blockState) -> new BFBlockEntity((BlockEntityType)FLAK88_SHIELD.get(), blockPos, blockState), (Block[])new Block[]{(Block)BFBlocks.FLAK88_SHIELD.get()}).build(null));
        FLAK88_SHIELD_DESTROYED = DR.register(BFBlocks.FLAK88_SHIELD_DESTROYED.getId().getPath(), () -> BlockEntityType.Builder.of((blockPos, blockState) -> new BFBlockEntity((BlockEntityType)FLAK88_SHIELD_DESTROYED.get(), blockPos, blockState), (Block[])new Block[]{(Block)BFBlocks.FLAK88_SHIELD_DESTROYED.get()}).build(null));
        FLAK88_NOSHIELD = DR.register(BFBlocks.FLAK88_NOSHIELD.getId().getPath(), () -> BlockEntityType.Builder.of((blockPos, blockState) -> new BFBlockEntity((BlockEntityType)FLAK88_NOSHIELD.get(), blockPos, blockState), (Block[])new Block[]{(Block)BFBlocks.FLAK88_NOSHIELD.get()}).build(null));
        FLAK88_NOSHIELD_DESTROYED = DR.register(BFBlocks.FLAK88_NOSHIELD_DESTROYED.getId().getPath(), () -> BlockEntityType.Builder.of((blockPos, blockState) -> new BFBlockEntity((BlockEntityType)FLAK88_NOSHIELD_DESTROYED.get(), blockPos, blockState), (Block[])new Block[]{(Block)BFBlocks.FLAK88_NOSHIELD_DESTROYED.get()}).build(null));
        TYPE96_SHIELD = DR.register(BFBlocks.TYPE96_SHIELD.getId().getPath(), () -> BlockEntityType.Builder.of((blockPos, blockState) -> new BFBlockEntity((BlockEntityType)TYPE96_SHIELD.get(), blockPos, blockState), (Block[])new Block[]{(Block)BFBlocks.TYPE96_SHIELD.get()}).build(null));
        TYPE96_SHIELD_DESTROYED = DR.register(BFBlocks.TYPE96_SHIELD_DESTROYED.getId().getPath(), () -> BlockEntityType.Builder.of((blockPos, blockState) -> new BFBlockEntity((BlockEntityType)TYPE96_SHIELD_DESTROYED.get(), blockPos, blockState), (Block[])new Block[]{(Block)BFBlocks.TYPE96_SHIELD_DESTROYED.get()}).build(null));
        TYPE96_NOSHIELD = DR.register(BFBlocks.TYPE96_NOSHIELD.getId().getPath(), () -> BlockEntityType.Builder.of((blockPos, blockState) -> new BFBlockEntity((BlockEntityType)TYPE96_NOSHIELD.get(), blockPos, blockState), (Block[])new Block[]{(Block)BFBlocks.TYPE96_NOSHIELD.get()}).build(null));
        TYPE96_NOSHIELD_DESTROYED = DR.register(BFBlocks.TYPE96_NOSHIELD_DESTROYED.getId().getPath(), () -> BlockEntityType.Builder.of((blockPos, blockState) -> new BFBlockEntity((BlockEntityType)TYPE96_NOSHIELD_DESTROYED.get(), blockPos, blockState), (Block[])new Block[]{(Block)BFBlocks.TYPE96_NOSHIELD_DESTROYED.get()}).build(null));
        BICYCLE = DR.register(BFBlocks.BICYCLE.getId().getPath(), () -> BlockEntityType.Builder.of((blockPos, blockState) -> new BFBlockEntity((BlockEntityType)BICYCLE.get(), blockPos, blockState), (Block[])new Block[]{(Block)BFBlocks.BICYCLE.get()}).build(null));
        P51_MUSTANG = DR.register(BFBlocks.P51_MUSTANG.getId().getPath(), () -> BlockEntityType.Builder.of((blockPos, blockState) -> new BFBlockEntity((BlockEntityType)P51_MUSTANG.get(), blockPos, blockState), (Block[])new Block[]{(Block)BFBlocks.P51_MUSTANG.get()}).build(null));
        PZL11C = DR.register(BFBlocks.PZL11C.getId().getPath(), () -> BlockEntityType.Builder.of((blockPos, blockState) -> new BFBlockEntity((BlockEntityType)PZL11C.get(), blockPos, blockState), (Block[])new Block[]{(Block)BFBlocks.PZL11C.get()}).build(null));
        JUNKERS_JU87 = DR.register(BFBlocks.JUNKERS_JU87.getId().getPath(), () -> BlockEntityType.Builder.of((blockPos, blockState) -> new BFBlockEntity((BlockEntityType)JUNKERS_JU87.get(), blockPos, blockState), (Block[])new Block[]{(Block)BFBlocks.JUNKERS_JU87.get()}).build(null));
        A6M_ZERO = DR.register(BFBlocks.A6M_ZERO.getId().getPath(), () -> BlockEntityType.Builder.of((blockPos, blockState) -> new BFBlockEntity((BlockEntityType)A6M_ZERO.get(), blockPos, blockState), (Block[])new Block[]{(Block)BFBlocks.A6M_ZERO.get()}).build(null));
        A6M_ZERO_PARKED = DR.register(BFBlocks.A6M_ZERO_PARKED.getId().getPath(), () -> BlockEntityType.Builder.of((blockPos, blockState) -> new BFBlockEntity((BlockEntityType)A6M_ZERO_PARKED.get(), blockPos, blockState), (Block[])new Block[]{(Block)BFBlocks.A6M_ZERO_PARKED.get()}).build(null));
        REGGIANE_RE2005 = DR.register(BFBlocks.REGGIANE_RE2005.getId().getPath(), () -> BlockEntityType.Builder.of((blockPos, blockState) -> new BFBlockEntity((BlockEntityType)REGGIANE_RE2005.get(), blockPos, blockState), (Block[])new Block[]{(Block)BFBlocks.REGGIANE_RE2005.get()}).build(null));
        SHERMAN_TANK = DR.register(BFBlocks.SHERMAN_TANK.getId().getPath(), () -> BlockEntityType.Builder.of((blockPos, blockState) -> new BFBlockEntity((BlockEntityType)SHERMAN_TANK.get(), blockPos, blockState), (Block[])new Block[]{(Block)BFBlocks.SHERMAN_TANK.get()}).build(null));
        RENAULT_FT_TANK = DR.register(BFBlocks.RENAULT_FT_TANK.getId().getPath(), () -> BlockEntityType.Builder.of((blockPos, blockState) -> new BFBlockEntity((BlockEntityType)RENAULT_FT_TANK.get(), blockPos, blockState), (Block[])new Block[]{(Block)BFBlocks.RENAULT_FT_TANK.get()}).build(null));
        SHERMAN_TANK_DESTROYED = DR.register(BFBlocks.SHERMAN_TANK_DESTROYED.getId().getPath(), () -> BlockEntityType.Builder.of((blockPos, blockState) -> new BFBlockEntity((BlockEntityType)SHERMAN_TANK_DESTROYED.get(), blockPos, blockState), (Block[])new Block[]{(Block)BFBlocks.SHERMAN_TANK_DESTROYED.get()}).build(null));
        SHERMAN_TANK_SNORKEL = DR.register(BFBlocks.SHERMAN_TANK_SNORKEL.getId().getPath(), () -> BlockEntityType.Builder.of((blockPos, blockState) -> new BFBlockEntity((BlockEntityType)SHERMAN_TANK_SNORKEL.get(), blockPos, blockState), (Block[])new Block[]{(Block)BFBlocks.SHERMAN_TANK_SNORKEL.get()}).build(null));
        SHERMAN_TANK_SNORKEL_DESTROYED = DR.register(BFBlocks.SHERMAN_TANK_SNORKEL_DESTROYED.getId().getPath(), () -> BlockEntityType.Builder.of((blockPos, blockState) -> new BFBlockEntity((BlockEntityType)SHERMAN_TANK_SNORKEL_DESTROYED.get(), blockPos, blockState), (Block[])new Block[]{(Block)BFBlocks.SHERMAN_TANK_SNORKEL_DESTROYED.get()}).build(null));
        TIGER_AUSF_H_TANK = DR.register(BFBlocks.TIGER_AUSF_H_TANK.getId().getPath(), () -> BlockEntityType.Builder.of((blockPos, blockState) -> new BFBlockEntity((BlockEntityType)TIGER_AUSF_H_TANK.get(), blockPos, blockState), (Block[])new Block[]{(Block)BFBlocks.TIGER_AUSF_H_TANK.get()}).build(null));
        T34_TANK = DR.register(BFBlocks.T34_TANK.getId().getPath(), () -> BlockEntityType.Builder.of((blockPos, blockState) -> new BFBlockEntity((BlockEntityType)T34_TANK.get(), blockPos, blockState), (Block[])new Block[]{(Block)BFBlocks.T34_TANK.get()}).build(null));
        T34_TANK_DESTROYED = DR.register(BFBlocks.T34_TANK_DESTROYED.getId().getPath(), () -> BlockEntityType.Builder.of((blockPos, blockState) -> new BFBlockEntity((BlockEntityType)T34_TANK_DESTROYED.get(), blockPos, blockState), (Block[])new Block[]{(Block)BFBlocks.T34_TANK_DESTROYED.get()}).build(null));
        PANZERIV_TANK = DR.register(BFBlocks.PANZERIV_TANK.getId().getPath(), () -> BlockEntityType.Builder.of((blockPos, blockState) -> new BFBlockEntity((BlockEntityType)PANZERIV_TANK.get(), blockPos, blockState), (Block[])new Block[]{(Block)BFBlocks.PANZERIV_TANK.get()}).build(null));
        PANZERIV_TANK_DESTROYED = DR.register(BFBlocks.PANZERIV_TANK_DESTROYED.getId().getPath(), () -> BlockEntityType.Builder.of((blockPos, blockState) -> new BFBlockEntity((BlockEntityType)PANZERIV_TANK_DESTROYED.get(), blockPos, blockState), (Block[])new Block[]{(Block)BFBlocks.PANZERIV_TANK_DESTROYED.get()}).build(null));
        CHIHA_TANK = DR.register(BFBlocks.CHIHA_TANK.getId().getPath(), () -> BlockEntityType.Builder.of((blockPos, blockState) -> new BFBlockEntity((BlockEntityType)CHIHA_TANK.get(), blockPos, blockState), (Block[])new Block[]{(Block)BFBlocks.CHIHA_TANK.get()}).build(null));
        CHIHA_TANK_DESTROYED = DR.register(BFBlocks.CHIHA_TANK_DESTROYED.getId().getPath(), () -> BlockEntityType.Builder.of((blockPos, blockState) -> new BFBlockEntity((BlockEntityType)CHIHA_TANK_DESTROYED.get(), blockPos, blockState), (Block[])new Block[]{(Block)BFBlocks.CHIHA_TANK_DESTROYED.get()}).build(null));
        KUROGANE = DR.register(BFBlocks.KUROGANE.getId().getPath(), () -> BlockEntityType.Builder.of((blockPos, blockState) -> new BFBlockEntity((BlockEntityType)KUROGANE.get(), blockPos, blockState), (Block[])new Block[]{(Block)BFBlocks.KUROGANE.get()}).build(null));
        KUBEL = DR.register(BFBlocks.KUBEL.getId().getPath(), () -> BlockEntityType.Builder.of((blockPos, blockState) -> new BFBlockEntity((BlockEntityType)KUBEL.get(), blockPos, blockState), (Block[])new Block[]{(Block)BFBlocks.KUBEL.get()}).build(null));
        RENAULT_AHN = DR.register(BFBlocks.RENAULT_AHN.getId().getPath(), () -> BlockEntityType.Builder.of((blockPos, blockState) -> new BFBlockEntity((BlockEntityType)RENAULT_AHN.get(), blockPos, blockState), (Block[])new Block[]{(Block)BFBlocks.RENAULT_AHN.get()}).build(null));
        CITROEN_11CV_TRACTION_AVANT = DR.register(BFBlocks.CITROEN_11CV_TRACTION_AVANT.getId().getPath(), () -> BlockEntityType.Builder.of((blockPos, blockState) -> new BFBlockEntity((BlockEntityType)CITROEN_11CV_TRACTION_AVANT.get(), blockPos, blockState), (Block[])new Block[]{(Block)BFBlocks.CITROEN_11CV_TRACTION_AVANT.get()}).build(null));
        CITROEN_11CV_TRACTION_AVANT_DESTROYED = DR.register(BFBlocks.CITROEN_11CV_TRACTION_AVANT_DESTROYED.getId().getPath(), () -> BlockEntityType.Builder.of((blockPos, blockState) -> new BFBlockEntity((BlockEntityType)CITROEN_11CV_TRACTION_AVANT_DESTROYED.get(), blockPos, blockState), (Block[])new Block[]{(Block)BFBlocks.CITROEN_11CV_TRACTION_AVANT_DESTROYED.get()}).build(null));
        LANCIA_1ZM = DR.register(BFBlocks.LANCIA_1ZM.getId().getPath(), () -> BlockEntityType.Builder.of((blockPos, blockState) -> new BFBlockEntity((BlockEntityType)LANCIA_1ZM.get(), blockPos, blockState), (Block[])new Block[]{(Block)BFBlocks.LANCIA_1ZM.get()}).build(null));
        LANCIA_1ZM_DESTROYED = DR.register(BFBlocks.LANCIA_1ZM_DESTROYED.getId().getPath(), () -> BlockEntityType.Builder.of((blockPos, blockState) -> new BFBlockEntity((BlockEntityType)LANCIA_1ZM_DESTROYED.get(), blockPos, blockState), (Block[])new Block[]{(Block)BFBlocks.LANCIA_1ZM_DESTROYED.get()}).build(null));
        WILLYS_JEEP = DR.register(BFBlocks.WILLYS_JEEP.getId().getPath(), () -> BlockEntityType.Builder.of((blockPos, blockState) -> new BFBlockEntity((BlockEntityType)WILLYS_JEEP.get(), blockPos, blockState), (Block[])new Block[]{(Block)BFBlocks.WILLYS_JEEP.get()}).build(null));
        WILLYS_JEEP_DESTROYED = DR.register(BFBlocks.WILLYS_JEEP_DESTROYED.getId().getPath(), () -> BlockEntityType.Builder.of((blockPos, blockState) -> new BFBlockEntity((BlockEntityType)WILLYS_JEEP_DESTROYED.get(), blockPos, blockState), (Block[])new Block[]{(Block)BFBlocks.WILLYS_JEEP_DESTROYED.get()}).build(null));
        _150MM_TBTSK_C36_GUN = DR.register(BFBlocks._150MM_TBTSK_C36_GUN.getId().getPath(), () -> BlockEntityType.Builder.of((blockPos, blockState) -> new BFBlockEntity((BlockEntityType)_150MM_TBTSK_C36_GUN.get(), blockPos, blockState), (Block[])new Block[]{(Block)BFBlocks._150MM_TBTSK_C36_GUN.get()}).build(null));
        LCVP = DR.register(BFBlocks.LCVP.getId().getPath(), () -> BlockEntityType.Builder.of((blockPos, blockState) -> new BFBlockEntity((BlockEntityType)LCVP.get(), blockPos, blockState), (Block[])new Block[]{(Block)BFBlocks.LCVP.get()}).build(null));
        LCVP_OPEN = DR.register(BFBlocks.LCVP_OPEN.getId().getPath(), () -> BlockEntityType.Builder.of((blockPos, blockState) -> new BFBlockEntity((BlockEntityType)LCVP_OPEN.get(), blockPos, blockState), (Block[])new Block[]{(Block)BFBlocks.LCVP_OPEN.get()}).build(null));
        _7TP_PELICAN = DR.register(BFBlocks._7TP_PELICAN.getId().getPath(), () -> BlockEntityType.Builder.of((blockPos, blockState) -> new BFBlockEntity((BlockEntityType)_7TP_PELICAN.get(), blockPos, blockState), (Block[])new Block[]{(Block)BFBlocks._7TP_PELICAN.get()}).build(null));
        _7TP_PELICAN_DESTROYED = DR.register(BFBlocks._7TP_PELICAN_DESTROYED.getId().getPath(), () -> BlockEntityType.Builder.of((blockPos, blockState) -> new BFBlockEntity((BlockEntityType)_7TP_PELICAN_DESTROYED.get(), blockPos, blockState), (Block[])new Block[]{(Block)BFBlocks._7TP_PELICAN_DESTROYED.get()}).build(null));
        WALL_FLAG_TALL = DR.register(BFBlocks.WALL_FLAG_TALL.getId().getPath(), () -> BlockEntityType.Builder.of((blockPos, blockState) -> new BFBlockEntity((BlockEntityType)WALL_FLAG_TALL.get(), blockPos, blockState), (Block[])new Block[]{(Block)BFBlocks.WALL_FLAG_TALL.get()}).build(null));
    }
}

