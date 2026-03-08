/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.entity.EntityRendererProvider$Context
 *  net.minecraft.client.renderer.entity.ItemRenderer
 *  net.minecraft.world.item.Item
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.render.entity;

import com.boehmod.blockfront.client.render.entity.MedicalBagEntityRenderer;
import com.boehmod.blockfront.common.entity.LandmineEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public class LandmineEntityRenderer
extends MedicalBagEntityRenderer<LandmineEntity> {
    public LandmineEntityRenderer(@NotNull EntityRendererProvider.Context context, @NotNull ItemRenderer itemRenderer, @NotNull DeferredHolder<Item, ? extends Item> deferredHolder) {
        super(context, itemRenderer, deferredHolder);
    }

    @Override
    public float method_1181() {
        return 1.0f;
    }
}

