/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.geo.model;

import com.boehmod.blockfront.cloud.BFFeatureFlags;
import com.boehmod.blockfront.unnamed.BF_232;
import com.boehmod.blockfront.util.BFRes;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimationState;

public class EventDecorationModel<T extends BlockEntity>
extends BF_232<T> {
    private static final int NUM_DECORATIONS = 8;

    public static int randomFromBlockPos(@NotNull BlockPos pos) {
        long l = (long)pos.getX() * 73856093L ^ (long)pos.getY() * 19349663L ^ (long)pos.getZ() * 83492791L;
        return new Random(l).nextInt(8);
    }

    @Override
    public ResourceLocation getAnimationResource(@NotNull T t) {
        int n = EventDecorationModel.randomFromBlockPos(t.getBlockPos());
        return BFRes.loc("animations/block/decoration_" + BFFeatureFlags.currentEvent + "_" + n + ".animation.json");
    }

    @Override
    public ResourceLocation getModelResource(@NotNull T t) {
        int n = EventDecorationModel.randomFromBlockPos(t.getBlockPos());
        return BFRes.loc("geo/block/decoration_" + BFFeatureFlags.currentEvent + "_" + n + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(@NotNull T t) {
        int n = EventDecorationModel.randomFromBlockPos(t.getBlockPos());
        return BFRes.loc("textures/block/entity/decoration_" + BFFeatureFlags.currentEvent + "_" + n + ".png");
    }

    @Override
    public ResourceLocation method_1031(T t) {
        int n = EventDecorationModel.randomFromBlockPos(t.getBlockPos());
        return BFRes.loc("block/entity/decoration_" + BFFeatureFlags.currentEvent + "_" + n);
    }

    @Override
    public void applyMolangQueries(AnimationState<T> animationState, double d) {
    }

    @Override
    public /* synthetic */ ResourceLocation getAnimationResource(@NotNull GeoAnimatable geoAnimatable) {
        return this.getAnimationResource((BlockEntity)geoAnimatable);
    }

    @Override
    public /* synthetic */ ResourceLocation getTextureResource(@NotNull GeoAnimatable geoAnimatable) {
        return this.getTextureResource((BlockEntity)geoAnimatable);
    }

    @Override
    public /* synthetic */ ResourceLocation getModelResource(@NotNull GeoAnimatable geoAnimatable) {
        return this.getModelResource((BlockEntity)geoAnimatable);
    }
}

