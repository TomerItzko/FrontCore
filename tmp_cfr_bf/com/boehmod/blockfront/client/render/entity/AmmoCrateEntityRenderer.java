/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render.entity;

import com.boehmod.blockfront.client.render.entity.MedicalBagEntityRenderer;
import com.boehmod.blockfront.common.entity.AmmoCrateEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public class AmmoCrateEntityRenderer
extends MedicalBagEntityRenderer<AmmoCrateEntity> {
    public AmmoCrateEntityRenderer(@NotNull EntityRendererProvider.Context context, @NotNull ItemRenderer itemRenderer, @NotNull DeferredHolder<Item, ? extends Item> deferredHolder) {
        super(context, itemRenderer, deferredHolder);
    }

    @Override
    public float method_1181() {
        return 1.0f;
    }
}

