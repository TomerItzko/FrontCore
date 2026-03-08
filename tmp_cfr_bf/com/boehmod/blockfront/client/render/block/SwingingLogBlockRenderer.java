/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render.block;

import com.boehmod.blockfront.client.render.block.BFBlockRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

public class SwingingLogBlockRenderer<T extends BlockEntity>
extends BFBlockRenderer<T> {
    public SwingingLogBlockRenderer(@NotNull BlockEntityRendererProvider.Context context) {
    }

    @NotNull
    public AABB getRenderBoundingBox(@NotNull T t) {
        return super.getRenderBoundingBox(t).inflate(2.25);
    }

    @Override
    public boolean method_1284(@NotNull T t) {
        return false;
    }

    @Override
    public boolean method_5953() {
        return true;
    }
}

