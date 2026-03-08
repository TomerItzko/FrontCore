/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.block.state.properties.Property
 *  org.jetbrains.annotations.NotNull
 *  software.bernie.geckolib.GeckoLibConstants
 *  software.bernie.geckolib.animatable.GeoAnimatable
 *  software.bernie.geckolib.animation.AnimationState
 *  software.bernie.geckolib.cache.GeckoLibCache
 *  software.bernie.geckolib.cache.object.BakedGeoModel
 *  software.bernie.geckolib.model.GeoModel
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.common.block.base.BFBlockProperties;
import com.boehmod.blockfront.mixin.GeoModelAccessor;
import com.boehmod.blockfront.util.BFRes;
import javax.annotation.Nullable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.GeckoLibConstants;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.GeckoLibCache;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;

public class BF_232<T extends BlockEntity>
extends GeoModel<T> {
    @Nullable
    public ResourceLocation field_1396;
    @Nullable
    public ResourceLocation field_1397;
    private final Object field_1395 = new Object();

    public ResourceLocation getAnimationResource(T t) {
        if (this.field_1396 != null) {
            return this.field_1396;
        }
        String string = BuiltInRegistries.BLOCK.getKey((Object)t.getBlockState().getBlock()).getPath();
        this.field_1396 = BFRes.loc("animations/block/" + string + ".animation.json");
        return this.field_1396;
    }

    public ResourceLocation getModelResource(T t) {
        if (this.field_1397 != null) {
            return this.field_1397;
        }
        String string = BuiltInRegistries.BLOCK.getKey((Object)t.getBlockState().getBlock()).getPath();
        this.field_1397 = BFRes.loc("geo/block/" + string + ".geo.json");
        return this.field_1397;
    }

    public ResourceLocation getTextureResource(T t) {
        return BFRes.loc("textures/block/entity/" + this.method_5597(t) + ".png");
    }

    public ResourceLocation method_1031(T t) {
        return BFRes.loc("block/entity/" + this.method_5597(t));
    }

    private String method_5597(T t) {
        BlockState blockState = t.getBlockState();
        Block block = blockState.getBlock();
        Object object = BuiltInRegistries.BLOCK.getKey((Object)block).getPath();
        if (blockState.hasProperty((Property)BFBlockProperties.TEXTURE_INDEX)) {
            object = (String)object + String.valueOf(blockState.getValue((Property)BFBlockProperties.TEXTURE_INDEX));
        }
        return object;
    }

    public void applyMolangQueries(AnimationState<T> animationState, double d) {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public BakedGeoModel getBakedModel(@NotNull ResourceLocation resourceLocation) {
        BakedGeoModel bakedGeoModel = (BakedGeoModel)GeckoLibCache.getBakedModels().get(resourceLocation);
        if (bakedGeoModel == null) {
            if (!resourceLocation.getPath().contains("geo/")) {
                throw GeckoLibConstants.exception((ResourceLocation)resourceLocation, (String)"Invalid model resource path provided - GeckoLib models must be placed in assets/<modid>/geo/");
            }
            throw GeckoLibConstants.exception((ResourceLocation)resourceLocation, (String)"Unable to find model");
        }
        GeoModelAccessor geoModelAccessor = (GeoModelAccessor)((Object)this);
        Object object = this.field_1395;
        synchronized (object) {
            if (bakedGeoModel != geoModelAccessor.getCurrentModel()) {
                this.getAnimationProcessor().setActiveModel(bakedGeoModel);
                geoModelAccessor.setCurrentModel(bakedGeoModel);
            }
        }
        return geoModelAccessor.getCurrentModel();
    }

    public /* synthetic */ ResourceLocation getAnimationResource(GeoAnimatable geoAnimatable) {
        return this.getAnimationResource((BlockEntity)geoAnimatable);
    }

    public /* synthetic */ ResourceLocation getTextureResource(GeoAnimatable geoAnimatable) {
        return this.getTextureResource((BlockEntity)geoAnimatable);
    }

    public /* synthetic */ ResourceLocation getModelResource(GeoAnimatable geoAnimatable) {
        return this.getModelResource((BlockEntity)geoAnimatable);
    }
}

