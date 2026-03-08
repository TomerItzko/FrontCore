/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.client.render.block.BFBlockRenderer;
import com.boehmod.blockfront.unnamed.BF_232;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;

public abstract class BF_283<T extends BlockEntity>
extends BFBlockRenderer<T> {
    private static final int field_1654 = 256;

    public BF_283(@NotNull BlockEntityRendererProvider.Context context) {
        this(new BF_232());
    }

    public BF_283(@NotNull GeoModel<T> geoModel) {
        super(geoModel);
    }

    public int getViewDistance() {
        return 256;
    }

    public boolean shouldRenderOffScreen(@NotNull T t) {
        return false;
    }

    @Override
    public boolean firePreRenderEvent(PoseStack poseStack, BakedGeoModel bakedGeoModel, @NotNull MultiBufferSource multiBufferSource, float f, int n) {
        return true;
    }
}

