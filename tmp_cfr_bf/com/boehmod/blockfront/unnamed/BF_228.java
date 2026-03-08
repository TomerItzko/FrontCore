/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.common.entity.VendorEntity;
import com.boehmod.blockfront.util.math.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public final class BF_228
extends PlayerModel<VendorEntity> {
    public BF_228(@NotNull ModelPart modelPart, boolean bl) {
        super(modelPart, bl);
    }

    public void setupAnim(@NotNull VendorEntity vendorEntity, float f, float f2, float f3, float f4, float f5) {
        super.setupAnim((LivingEntity)vendorEntity, f, f2, f3, f4, f5);
        float f6 = MathUtils.getTickDelta(Minecraft.getInstance());
        float f7 = vendorEntity.method_2488(f6);
        this.head.yRot = this.hat.yRot += f7;
        this.head.zRot = this.hat.zRot = f7 / 4.0f;
    }

    public /* synthetic */ void setupAnim(@NotNull LivingEntity livingEntity, float f, float f2, float f3, float f4, float f5) {
        this.setupAnim((VendorEntity)livingEntity, f, f2, f3, f4, f5);
    }

    public /* synthetic */ void setupAnim(@NotNull Entity entity, float f, float f2, float f3, float f4, float f5) {
        this.setupAnim((VendorEntity)entity, f, f2, f3, f4, f5);
    }
}

