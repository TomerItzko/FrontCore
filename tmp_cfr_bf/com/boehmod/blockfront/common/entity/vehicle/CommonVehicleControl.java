/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.entity.vehicle;

import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.common.entity.vehicle.AbstractVehicleControl;
import com.boehmod.blockfront.unnamed.BF_624;
import com.boehmod.blockfront.util.math.MathUtils;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class CommonVehicleControl
extends AbstractVehicleControl<AbstractVehicleEntity> {
    public static final float field_2702 = 3.0f;

    @Override
    public void method_2297(@NotNull AbstractVehicleEntity abstractVehicleEntity) {
    }

    @Override
    @NotNull
    public Vec2 method_2290(@NotNull AbstractVehicleEntity abstractVehicleEntity) {
        return new Vec2(this.field_2594, this.field_2594);
    }

    @Override
    @NotNull
    public Vec2 method_2295(@NotNull AbstractVehicleEntity abstractVehicleEntity) {
        return new Vec2(abstractVehicleEntity.method_2313(), abstractVehicleEntity.method_2313()).scale(3.0f);
    }

    @Override
    public void method_2292(@NotNull AbstractVehicleEntity abstractVehicleEntity, boolean bl) {
        if (!abstractVehicleEntity.isNoGravity()) {
            abstractVehicleEntity.setDeltaMovement(abstractVehicleEntity.getDeltaMovement().add(0.0, -0.03, 0.0).multiply(0.5, 1.0, 0.5));
            if (abstractVehicleEntity.getDeltaMovement().y > 0.0) {
                abstractVehicleEntity.setDeltaMovement(abstractVehicleEntity.getDeltaMovement().multiply(1.0, 0.0, 1.0));
            }
        }
        abstractVehicleEntity.move(MoverType.SELF, abstractVehicleEntity.getDeltaMovement());
    }

    @Override
    public void method_2291(@NotNull AbstractVehicleEntity abstractVehicleEntity, @NotNull LocalPlayer localPlayer) {
        BF_624<AbstractVehicleEntity> bF_624 = abstractVehicleEntity.method_2343();
        this.field_2594 = this.field_2589 ? (this.field_2594 -= bF_624.field_2693) : (this.field_2590 ? (this.field_2594 += bF_624.field_2694) : (this.field_2594 *= bF_624.field_2696));
        this.field_2594 = Mth.clamp((float)this.field_2594, (float)(-bF_624.field_2697), (float)bF_624.field_2698);
        abstractVehicleEntity.setYRot(abstractVehicleEntity.getYRot() + this.method_2293(bF_624));
        float f = bF_624.field_2691;
        float f2 = -bF_624.field_2692;
        if (this.field_2589 || this.field_2590) {
            f *= bF_624.field_2695;
            f2 *= bF_624.field_2695;
        }
        if (this.field_2591) {
            f *= bF_624.field_2688;
            f2 *= bF_624.field_2688;
        }
        this.field_2593 = this.field_2587 ? MathUtils.moveTowards(this.field_2593, f, bF_624.field_2686) : (this.field_2588 ? MathUtils.moveTowards(this.field_2593, f2, bF_624.field_2687) : (this.field_2593 *= bF_624.field_2689));
        if (this.field_2593 > f) {
            this.field_2593 = f;
        } else if (this.field_2593 < f2) {
            this.field_2593 = f2;
        }
        float f3 = Mth.sin((float)(-abstractVehicleEntity.getYRot() * ((float)Math.PI / 180))) * this.field_2593;
        float f4 = Mth.cos((float)(abstractVehicleEntity.getYRot() * ((float)Math.PI / 180))) * this.field_2593;
        abstractVehicleEntity.move(MoverType.SELF, new Vec3((double)f3, 0.0, (double)f4));
    }

    @Override
    public float method_2293(@NotNull BF_624<?> bF_624) {
        float f = this.field_2593 / (this.field_2588 ? bF_624.field_2692 : bF_624.field_2691);
        return this.field_2594 * f;
    }
}

