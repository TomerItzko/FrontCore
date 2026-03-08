/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render.entity;

import com.boehmod.blockfront.util.BFRes;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class InvisibleEntityRenderer<T extends Entity>
extends EntityRenderer<T> {
    public InvisibleEntityRenderer(@NotNull EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.0f;
        this.shadowStrength = 0.0f;
    }

    @NotNull
    public ResourceLocation getTextureLocation(@NotNull T t) {
        return BFRes.loc("textures/entity/nothing");
    }

    public boolean shouldShowName(@NotNull T t) {
        return false;
    }
}

