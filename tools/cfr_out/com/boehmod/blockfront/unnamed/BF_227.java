/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.model.PlayerModel
 *  net.minecraft.client.model.geom.ModelPart
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.common.entity.InfectedEntity;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class BF_227
extends PlayerModel<InfectedEntity> {
    public BF_227(@NotNull ModelPart modelPart, boolean bl) {
        super(modelPart, bl);
    }

    public void setupAnim(@NotNull InfectedEntity infectedEntity, float f, float f2, float f3, float f4, float f5) {
        super.setupAnim((LivingEntity)infectedEntity, f, f2, f3, f4, f5);
        float f6 = BFRendering.getRenderTime() + (float)infectedEntity.getId();
        infectedEntity.method_2067(this, f6);
    }

    public /* synthetic */ void setupAnim(@NotNull LivingEntity livingEntity, float f, float f2, float f3, float f4, float f5) {
        this.setupAnim((InfectedEntity)livingEntity, f, f2, f3, f4, f5);
    }

    public /* synthetic */ void setupAnim(@NotNull Entity entity, float f, float f2, float f3, float f4, float f5) {
        this.setupAnim((InfectedEntity)entity, f, f2, f3, f4, f5);
    }
}

