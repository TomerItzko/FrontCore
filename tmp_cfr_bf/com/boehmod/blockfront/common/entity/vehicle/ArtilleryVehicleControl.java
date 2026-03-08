/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.entity.vehicle;

import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.common.entity.ArtilleryVehicleEntity;
import com.boehmod.blockfront.common.entity.vehicle.AbstractVehicleControl;
import com.boehmod.blockfront.unnamed.BF_624;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class ArtilleryVehicleControl
extends AbstractVehicleControl<ArtilleryVehicleEntity> {
    private static final float field_2704 = 0.05f;
    public static final float field_2705 = 2.8f;

    @Override
    public void method_2297(@NotNull ArtilleryVehicleEntity artilleryVehicleEntity) {
    }

    @Override
    @NotNull
    public Vec2 method_2290(@NotNull ArtilleryVehicleEntity artilleryVehicleEntity) {
        return Vec2.ZERO;
    }

    @Override
    @NotNull
    public Vec2 method_2295(@NotNull ArtilleryVehicleEntity artilleryVehicleEntity) {
        float f = artilleryVehicleEntity.method_2313() * 2.8f;
        float f2 = artilleryVehicleEntity.method_2313() * 2.8f;
        float f3 = 0.0f;
        if (artilleryVehicleEntity.method_2335()) {
            f3 = 0.05f;
        } else if (artilleryVehicleEntity.method_2337()) {
            f3 = -0.05f;
        }
        return new Vec2(f2 -= f3, f += f3);
    }

    @Override
    public void method_2292(@NotNull ArtilleryVehicleEntity artilleryVehicleEntity, boolean bl) {
        if (!artilleryVehicleEntity.isNoGravity()) {
            artilleryVehicleEntity.setDeltaMovement(artilleryVehicleEntity.getDeltaMovement().add(0.0, -0.03, 0.0).multiply(0.5, 1.0, 0.5));
            if (artilleryVehicleEntity.getDeltaMovement().y > 0.0) {
                artilleryVehicleEntity.setDeltaMovement(artilleryVehicleEntity.getDeltaMovement().multiply(1.0, 0.0, 1.0));
            }
        }
        artilleryVehicleEntity.move(MoverType.SELF, artilleryVehicleEntity.getDeltaMovement());
    }

    @Override
    public void method_2291(@NotNull ArtilleryVehicleEntity artilleryVehicleEntity, @NotNull LocalPlayer localPlayer) {
        BF_624<AbstractVehicleEntity> bF_624 = artilleryVehicleEntity.method_2343();
        this.field_2594 = this.field_2589 ? (this.field_2594 -= bF_624.field_2693) : (this.field_2590 ? (this.field_2594 += bF_624.field_2694) : (this.field_2594 *= bF_624.field_2696));
        this.field_2594 = Mth.clamp((float)this.field_2594, (float)-1.0f, (float)1.0f);
        this.field_2593 = this.field_2587 ? (this.field_2593 += bF_624.field_2686) : (this.field_2588 ? (this.field_2593 -= bF_624.field_2687) : (this.field_2593 *= bF_624.field_2689));
        artilleryVehicleEntity.setYRot(artilleryVehicleEntity.getYRot() + Mth.clamp((float)this.method_2293(bF_624), (float)(-bF_624.field_2698), (float)bF_624.field_2697));
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
        if (this.field_2593 > f) {
            this.field_2593 = f;
        } else if (this.field_2593 < f2) {
            this.field_2593 = f2;
        }
        float f3 = Mth.sin((float)(-artilleryVehicleEntity.getYRot() * ((float)Math.PI / 180))) * this.field_2593;
        float f4 = Mth.cos((float)(artilleryVehicleEntity.getYRot() * ((float)Math.PI / 180))) * this.field_2593;
        artilleryVehicleEntity.move(MoverType.SELF, new Vec3((double)f3, 0.0, (double)f4));
    }

    @Override
    public float method_2293(@NotNull BF_624<?> bF_624) {
        return this.field_2594;
    }

    @Override
    public /* synthetic */ void method_2297(@NotNull AbstractVehicleEntity abstractVehicleEntity) {
        this.method_2297((ArtilleryVehicleEntity)abstractVehicleEntity);
    }
}

