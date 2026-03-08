/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.entity.vehicle;

import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.common.entity.BasicVehicleEntity;
import com.boehmod.blockfront.common.entity.vehicle.AbstractVehicleControl;
import com.boehmod.blockfront.unnamed.BF_624;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class BasicVehicleControl
extends AbstractVehicleControl<BasicVehicleEntity> {
    public float field_2703 = 0.0f;

    @Override
    public void method_2297(@NotNull BasicVehicleEntity basicVehicleEntity) {
        if (!basicVehicleEntity.level().isClientSide) {
            basicVehicleEntity.setDeltaMovement(Vec3.ZERO);
        }
    }

    @Override
    @NotNull
    public Vec2 method_2290(@NotNull BasicVehicleEntity basicVehicleEntity) {
        return Vec2.ZERO;
    }

    @Override
    @NotNull
    public Vec2 method_2295(@NotNull BasicVehicleEntity basicVehicleEntity) {
        return Vec2.ZERO;
    }

    @Override
    public void method_2292(@NotNull BasicVehicleEntity basicVehicleEntity, boolean bl) {
        BF_624<AbstractVehicleEntity> bF_624 = basicVehicleEntity.method_2343();
        float f = bF_624.field_2691 / 1.5f;
        float f2 = 1.0f - Math.min(basicVehicleEntity.method_2313() / f, 1.0f);
        if (!basicVehicleEntity.isNoGravity()) {
            basicVehicleEntity.setDeltaMovement(basicVehicleEntity.getDeltaMovement().add(0.0, -0.03, 0.0).multiply(0.5, (double)f2, 0.5));
            if (basicVehicleEntity.getDeltaMovement().y > 0.0) {
                basicVehicleEntity.setDeltaMovement(basicVehicleEntity.getDeltaMovement().multiply(1.0, 0.0, 1.0));
            }
        }
        basicVehicleEntity.move(MoverType.SELF, basicVehicleEntity.getDeltaMovement());
    }

    @Override
    public void method_2291(@NotNull BasicVehicleEntity basicVehicleEntity, @NotNull LocalPlayer localPlayer) {
        BF_624<AbstractVehicleEntity> bF_624 = basicVehicleEntity.method_2343();
        float f = this.field_2593 / bF_624.field_2691;
        if (this.field_2587) {
            this.field_2593 += bF_624.field_2686;
        } else if (this.field_2588) {
            this.field_2593 -= bF_624.field_2687;
        }
        if (this.field_2593 > bF_624.field_2691) {
            this.field_2593 = bF_624.field_2691;
        }
        bF_624.field_2691 = 1.0f;
        if (this.field_2593 < 0.0f) {
            this.field_2593 = 0.0f;
        }
        float f2 = 0.05f * f;
        float f3 = basicVehicleEntity.getXRot();
        float f4 = basicVehicleEntity.getYRot();
        float f5 = -localPlayer.getXRot();
        float f6 = localPlayer.getYHeadRot();
        f3 = Mth.lerp((float)f2, (float)f3, (float)f5);
        f4 = Mth.lerp((float)(f2 / 4.0f), (float)f4, (float)f6);
        basicVehicleEntity.setXRot(f3);
        basicVehicleEntity.setYRot(f4);
        this.field_2594 = this.field_2703 * this.field_2593;
        float f7 = Mth.sin((float)(-basicVehicleEntity.getYRot() * ((float)Math.PI / 180))) * this.field_2593;
        float f8 = Mth.cos((float)(-(basicVehicleEntity.getXRot() - 90.0f) * ((float)Math.PI / 180))) * this.field_2593;
        float f9 = Mth.cos((float)(basicVehicleEntity.getYRot() * ((float)Math.PI / 180))) * this.field_2593;
        basicVehicleEntity.move(MoverType.SELF, new Vec3((double)f7, (double)f8, (double)f9));
    }

    @Override
    public float method_2293(@NotNull BF_624<?> bF_624) {
        return this.field_2594;
    }

    @Override
    public /* synthetic */ void method_2297(@NotNull AbstractVehicleEntity abstractVehicleEntity) {
        this.method_2297((BasicVehicleEntity)abstractVehicleEntity);
    }
}

