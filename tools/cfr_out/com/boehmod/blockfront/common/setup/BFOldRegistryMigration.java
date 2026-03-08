/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 */
package com.boehmod.blockfront.common.setup;

import com.boehmod.blockfront.registry.BFBlocks;
import net.minecraft.resources.ResourceLocation;

public class BFOldRegistryMigration {
    public static void init() {
        BFOldRegistryMigration.addAllForms("stone_fancy_0", "pumice_stone");
        BFOldRegistryMigration.addAllForms("stone_fancy_1", "slate_rough");
        BFOldRegistryMigration.addAllForms("stone_fancy_2", "deep_rock_rough");
        BFOldRegistryMigration.addAllForms("stone_fancy_3", "ozark_stone");
        BFOldRegistryMigration.addAllForms("stone_fancy_4", "fossil_rough");
        BFOldRegistryMigration.addAllForms("stone_fancy_5", "scoria_stone");
        BFOldRegistryMigration.addAllForms("stone_fancy_6", "chert_stone");
        BFOldRegistryMigration.addAllForms("stone_fancy_7", "limestone_stone");
        BFOldRegistryMigration.addAllForms("stone_fancy_8", "coquina_stone");
        BFOldRegistryMigration.addAllForms("stone_fancy_9", "soapstone_stone");
        BFOldRegistryMigration.addAllForms("stone_fancy_10", "quartzite_rough");
        BFOldRegistryMigration.addAllForms("stone_fancy_11", "moonrock_windswept");
        BFOldRegistryMigration.addAllForms("stone_fancy_12", "shale_smooth");
        BFOldRegistryMigration.addAllForms("stone_fancy_13", "redsandstone_smooth");
        BFOldRegistryMigration.addAllForms("stone_fancy_14", "redsandstone_rough");
        BFOldRegistryMigration.addAllForms("stone_powder_0", "redsandstone_powder");
        BFOldRegistryMigration.addAllForms("bricks_fancy_0", "chert_bricks");
        BFOldRegistryMigration.addAllForms("bricks_fancy_1", "augite_bricks");
        BFOldRegistryMigration.addAllForms("bricks_fancy_2", "hematite_bricks");
        BFOldRegistryMigration.addAllForms("bricks_fancy_3", "assorted_bricks");
        BFOldRegistryMigration.addAllForms("stone_bricks_fancy_0", "pumice_brick");
        BFOldRegistryMigration.addAllForms("stone_bricks_fancy_1", "mud_brick");
        BFOldRegistryMigration.addAllForms("stone_bricks_fancy_2", "chert_big_bricks");
        BFOldRegistryMigration.addAllForms("stone_bricks_fancy_3", "scoria_brick");
        BFOldRegistryMigration.addAllForms("stone_bricks_fancy_4", "hematite_big_bricks");
        BFOldRegistryMigration.addAllForms("stone_bricks_fancy_5", "half_n_half_bricks");
        BFOldRegistryMigration.addAllForms("stone_bricks_fancy_6", "pumice_brick");
        BFOldRegistryMigration.addAllForms("stone_bricks_fancy_7", "slate_brick");
        BFOldRegistryMigration.addAllForms("stone_bricks_fancy_8", "limestone_smooth_brick");
        BFOldRegistryMigration.addAllForms("stone_bricks_fancy_9", "pumice_smooth_brick");
        BFOldRegistryMigration.addAllForms("stone_bricks_fancy_10", "limestone_tiles");
        BFOldRegistryMigration.addAllForms("stone_bricks_fancy_11", "pumice_tiles");
        BFOldRegistryMigration.addAllForms("stone_bricks_fancy_12", "deepslate_tiles");
        BFOldRegistryMigration.addAllForms("stone_bricks_fancy_13", "sandstone_tiles");
        BFOldRegistryMigration.addAllForms("stone_bricks_fancy_14", "tuff_tiles");
        BFOldRegistryMigration.add("oil_barrel_black", "oil_barrel_gray");
    }

    public static void addAllForms(String oldId, String newId) {
        BFOldRegistryMigration.add(oldId, newId);
        BFOldRegistryMigration.add(oldId + "_slab", newId + "_slab");
        BFOldRegistryMigration.add(oldId + "_stair", newId + "_stair");
        BFOldRegistryMigration.add(oldId + "_wall", newId + "_wall");
    }

    public static void add(String oldId, String newId) {
        BFBlocks.DR.addAlias(ResourceLocation.fromNamespaceAndPath((String)"bf", (String)oldId), ResourceLocation.fromNamespaceAndPath((String)"bf", (String)newId));
    }
}

