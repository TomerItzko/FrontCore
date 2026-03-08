/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.geo.model;

import com.boehmod.blockfront.common.item.GenericGeoItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class GenericGeoModel
extends GeoModel<GenericGeoItem> {
    public ResourceLocation getModelResource(GenericGeoItem genericGeoItem) {
        return genericGeoItem.getModel();
    }

    public ResourceLocation getTextureResource(GenericGeoItem genericGeoItem) {
        return genericGeoItem.getTexture();
    }

    public ResourceLocation getAnimationResource(GenericGeoItem genericGeoItem) {
        return genericGeoItem.getAnimation();
    }

    public void applyMolangQueries(AnimationState<GenericGeoItem> animationState, double d) {
    }

    public /* synthetic */ ResourceLocation getAnimationResource(GeoAnimatable item) {
        return this.getAnimationResource((GenericGeoItem)item);
    }

    public /* synthetic */ ResourceLocation getTextureResource(GeoAnimatable item) {
        return this.getTextureResource((GenericGeoItem)item);
    }

    public /* synthetic */ ResourceLocation getModelResource(GeoAnimatable item) {
        return this.getModelResource((GenericGeoItem)item);
    }
}

