/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render.block;

import com.boehmod.blockfront.client.render.block.BFBlockRenderer;
import com.boehmod.blockfront.unnamed.BF_232;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;

public class DefaultGeoBlockRenderer<T extends BlockEntity>
extends BFBlockRenderer<T> {
    private static final int field_1653 = 256;

    public DefaultGeoBlockRenderer(@NotNull BlockEntityRendererProvider.Context context) {
        this(new BF_232());
    }

    public DefaultGeoBlockRenderer(@NotNull GeoModel<T> geoModel) {
        super(geoModel);
    }

    @Override
    public boolean method_1284(@NotNull T t) {
        return true;
    }

    @Override
    public boolean method_5953() {
        return true;
    }

    public int getViewDistance() {
        return 256;
    }

    @NotNull
    public AABB getRenderBoundingBox(@NotNull T t) {
        return super.getRenderBoundingBox(t).inflate(5.0);
    }

    public boolean shouldRenderOffScreen(@NotNull T t) {
        return false;
    }

    @Override
    public boolean firePreRenderEvent(PoseStack poseStack, BakedGeoModel bakedGeoModel, @NotNull MultiBufferSource multiBufferSource, float f, int n) {
        return true;
    }
}

