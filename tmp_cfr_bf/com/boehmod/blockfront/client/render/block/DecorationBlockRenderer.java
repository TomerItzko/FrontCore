/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render.block;

import com.boehmod.blockfront.client.geo.model.EventDecorationModel;
import com.boehmod.blockfront.client.render.block.DefaultGeoBlockRenderer;
import com.boehmod.blockfront.cloud.BFFeatureFlags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class DecorationBlockRenderer<T extends BlockEntity>
extends DefaultGeoBlockRenderer<T> {
    public DecorationBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(new EventDecorationModel());
    }

    @Override
    public boolean method_5953() {
        Minecraft minecraft = Minecraft.getInstance();
        return minecraft.player == null || minecraft.player.isCreative();
    }

    @Override
    public boolean method_1284(@NotNull T t) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null && minecraft.player.isCreative()) {
            return false;
        }
        if (BFFeatureFlags.currentEvent == null) {
            return false;
        }
        return super.method_1284(t);
    }

    public boolean shouldRender(@NotNull T block, @NotNull Vec3 vec3) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null && minecraft.player.isCreative()) {
            return true;
        }
        if (BFFeatureFlags.currentEvent == null) {
            return false;
        }
        return super.shouldRender(block, vec3);
    }
}

