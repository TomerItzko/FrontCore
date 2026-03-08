/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  software.bernie.geckolib.animatable.GeoAnimatable
 *  software.bernie.geckolib.animation.AnimationState
 *  software.bernie.geckolib.model.GeoModel
 */
package com.boehmod.blockfront.client.geo.model;

import com.boehmod.blockfront.common.item.BFWeaponItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class BFWeaponItemGeoModel<T extends BFWeaponItem<T>>
extends GeoModel<T> {
    public ResourceLocation getModelResource(T t) {
        return ((BFWeaponItem)((Object)t)).method_3781();
    }

    public ResourceLocation getTextureResource(T t) {
        return ((BFWeaponItem)((Object)t)).method_3779();
    }

    public ResourceLocation getAnimationResource(T t) {
        return ((BFWeaponItem)((Object)t)).method_3780();
    }

    public void applyMolangQueries(AnimationState<T> animationState, double d) {
    }

    public /* synthetic */ ResourceLocation getAnimationResource(GeoAnimatable geoAnimatable) {
        return this.getAnimationResource((BFWeaponItem)geoAnimatable);
    }

    public /* synthetic */ ResourceLocation getTextureResource(GeoAnimatable geoAnimatable) {
        return this.getTextureResource((BFWeaponItem)geoAnimatable);
    }

    public /* synthetic */ ResourceLocation getModelResource(GeoAnimatable geoAnimatable) {
        return this.getModelResource((BFWeaponItem)geoAnimatable);
    }
}

