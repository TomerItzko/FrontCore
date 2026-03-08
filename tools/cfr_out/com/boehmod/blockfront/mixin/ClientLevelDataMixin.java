/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.multiplayer.ClientLevel$ClientLevelData
 *  net.minecraft.world.level.LevelHeightAccessor
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Overwrite
 */
package com.boehmod.blockfront.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.LevelHeightAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value={ClientLevel.ClientLevelData.class})
public class ClientLevelDataMixin {
    @Overwrite
    public double getHorizonHeight(LevelHeightAccessor levelHeightAccessor) {
        return levelHeightAccessor.getMinBuildHeight();
    }
}

