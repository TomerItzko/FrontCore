/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 *  software.bernie.geckolib.cache.object.BakedGeoModel
 *  software.bernie.geckolib.model.GeoModel
 */
package com.boehmod.blockfront.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;

@Mixin(value={GeoModel.class})
public interface GeoModelAccessor {
    @Accessor(value="currentModel")
    public void setCurrentModel(BakedGeoModel var1);

    @Accessor(value="currentModel")
    @Nullable
    public BakedGeoModel getCurrentModel();
}

