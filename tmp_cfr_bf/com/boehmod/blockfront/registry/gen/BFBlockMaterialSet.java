/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.registry.gen;

import com.boehmod.blockfront.registry.gen.MultiformGenerator;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.NotNull;

public class BFBlockMaterialSet {
    public final MultiformGenerator bigBricks;
    public final MultiformGenerator brick;
    public final MultiformGenerator bricks;
    public final MultiformGenerator cobbled;
    public final MultiformGenerator cobbledTiles;
    public final MultiformGenerator crackedBrick;
    public final MultiformGenerator polished;
    public final MultiformGenerator polishedBrick;
    public final MultiformGenerator polishedSmoothBrick;
    public final MultiformGenerator powder;
    public final MultiformGenerator rough;
    public final MultiformGenerator smooth;
    public final MultiformGenerator smoothBrick;
    public final MultiformGenerator smoothCrackedBrick;
    public final MultiformGenerator smoothWindswept;
    public final MultiformGenerator stone;
    public final MultiformGenerator tiles;
    public final MultiformGenerator windswept;

    public BFBlockMaterialSet(@NotNull String baseId, @NotNull BlockBehaviour.Properties blockProperties) {
        this.bigBricks = MultiformGenerator.create(baseId + "_big_bricks", blockProperties, false);
        this.brick = MultiformGenerator.create(baseId + "_brick", blockProperties, false);
        this.bricks = MultiformGenerator.create(baseId + "_bricks", blockProperties, false);
        this.cobbled = MultiformGenerator.create(baseId + "_cobbled", blockProperties, false);
        this.cobbledTiles = MultiformGenerator.create(baseId + "_cobbled_tiles", blockProperties, false);
        this.crackedBrick = MultiformGenerator.create(baseId + "_cracked_brick", blockProperties, false);
        this.polished = MultiformGenerator.create(baseId + "_polished", blockProperties, false);
        this.polishedBrick = MultiformGenerator.create(baseId + "_polished_brick", blockProperties, false);
        this.polishedSmoothBrick = MultiformGenerator.create(baseId + "_polished_smooth_brick", blockProperties, false);
        this.powder = MultiformGenerator.create(baseId + "_powder", blockProperties, true);
        this.rough = MultiformGenerator.create(baseId + "_rough", blockProperties, true);
        this.smooth = MultiformGenerator.create(baseId + "_smooth", blockProperties, false);
        this.smoothBrick = MultiformGenerator.create(baseId + "_smooth_brick", blockProperties, false);
        this.smoothCrackedBrick = MultiformGenerator.create(baseId + "_smooth_cracked_brick", blockProperties, false);
        this.smoothWindswept = MultiformGenerator.create(baseId + "_smooth_windswept", blockProperties, true);
        this.stone = MultiformGenerator.create(baseId + "_stone", blockProperties, true);
        this.tiles = MultiformGenerator.create(baseId + "_tiles", blockProperties, false);
        this.windswept = MultiformGenerator.create(baseId + "_windswept", blockProperties, true);
    }
}

