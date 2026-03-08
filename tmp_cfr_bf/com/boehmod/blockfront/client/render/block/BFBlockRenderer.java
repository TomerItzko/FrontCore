/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render.block;

import com.boehmod.blockfront.common.block.entity.BFBlockEntity;
import com.boehmod.blockfront.common.block.entity.BakedEntityBlock;
import com.boehmod.blockfront.unnamed.BF_232;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public abstract class BFBlockRenderer<T extends BlockEntity>
extends GeoBlockRenderer<T> {
    public BFBlockRenderer(@NotNull GeoModel<T> geoModel) {
        super(geoModel);
    }

    public BFBlockRenderer() {
        this(new BF_232());
    }

    public abstract boolean method_1284(@NotNull T var1);

    public abstract boolean method_5953();

    public void render(T t, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int n, int n2) {
        if (!(t.getBlockState().getBlock() instanceof BakedEntityBlock) || !this.method_1284(t)) {
            super.render(t, f, poseStack, multiBufferSource, n, n2);
        }
    }

    public void updateAnimatedTextureFrame(T t) {
        BFBlockEntity bFBlockEntity;
        if (t instanceof BFBlockEntity && !(bFBlockEntity = (BFBlockEntity)((Object)t)).method_1882()) {
            return;
        }
        super.updateAnimatedTextureFrame(t);
    }

    public boolean firePreRenderEvent(PoseStack poseStack, BakedGeoModel bakedGeoModel, @NotNull MultiBufferSource multiBufferSource, float f, int n) {
        return true;
    }
}

