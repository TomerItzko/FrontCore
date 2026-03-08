/*
 * Decompiled with CFR.
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

