/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.unnamed.BF_624;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.floats.FloatFloatImmutablePair;
import it.unimi.dsi.fastutil.floats.FloatFloatPair;
import java.util.stream.IntStream;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class BF_622 {
    public int field_2657 = 0;
    private float field_2649;
    private float field_2650;
    private float field_2651;
    private float field_2652;
    private float field_2653 = 0.0f;
    private float field_2654 = 0.0f;
    private float field_2655 = 0.0f;
    private float field_2656 = 0.0f;

    public void method_2375(@NotNull AbstractVehicleEntity abstractVehicleEntity) {
        this.field_2651 = this.field_2649;
        this.field_2652 = this.field_2650;
        this.field_2649 = this.field_2655;
        this.field_2650 = this.field_2656;
        if (this.field_2657++ >= 4) {
            this.field_2657 = 0;
            this.method_2377(abstractVehicleEntity);
        }
        this.method_2376(abstractVehicleEntity);
    }

    public void method_2376(@NotNull AbstractVehicleEntity abstractVehicleEntity) {
        BF_624<AbstractVehicleEntity> bF_624 = abstractVehicleEntity.method_2343();
        float f = abstractVehicleEntity.method_2313();
        float f2 = f < 0.0f ? bF_624.field_2692 : bF_624.field_2691;
        float f3 = f * f2;
        float f4 = Math.min(0.1f, 0.01f + 2.5f * Math.abs(f3));
        this.field_2655 = Mth.lerp((float)f4, (float)this.field_2655, (float)this.field_2653);
        this.field_2656 = Mth.lerp((float)f4, (float)this.field_2656, (float)this.field_2654);
    }

    public void method_2377(@NotNull AbstractVehicleEntity abstractVehicleEntity) {
        Level level = abstractVehicleEntity.level();
        float f = abstractVehicleEntity.getYRot() * ((float)Math.PI / 180) - 1.5707964f;
        Vec3 vec3 = abstractVehicleEntity.position().add(0.0, 2.0, 0.0);
        float f2 = 0.25f;
        int n = 6;
        this.field_2653 = (float)Mth.clamp((double)this.method_2370(level, vec3, f, 0.25f, 6, true), (double)-4.0, (double)4.0);
        this.field_2654 = (float)(-Mth.clamp((double)this.method_2370(level, vec3, f, 0.25f, 6, false), (double)-4.0, (double)4.0));
    }

    private double method_2370(Level level, Vec3 vec3, float f, float f2, int n2, boolean bl) {
        return IntStream.range(1, n2).mapToDouble(n -> {
            Vec3 vec32 = bl ? new Vec3((double)(f2 * (float)n), 0.0, 0.0) : new Vec3(0.0, 0.0, (double)(f2 * (float)n));
            return this.method_2371(level, vec32, vec3, f) - this.method_2371(level, vec32.scale(-1.0), vec3, f);
        }).sum();
    }

    private double method_2371(@NotNull Level level, @NotNull Vec3 vec3, @NotNull Vec3 vec32, float f) {
        vec3 = vec32.add(vec3.yRot(-f));
        BlockHitResult blockHitResult = level.clip(new ClipContext(vec3, vec3.add(0.0, -4.0, 0.0), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, CollisionContext.empty()));
        return vec3.y + blockHitResult.getLocation().y;
    }

    @OnlyIn(value=Dist.CLIENT)
    public void method_2374(@NotNull PoseStack poseStack, float f) {
        Vec2 vec2 = new Vec2(MathUtils.lerpf1(this.field_2649, this.field_2651, f), MathUtils.lerpf1(this.field_2650, this.field_2652, f));
        poseStack.mulPose(Axis.XP.rotationDegrees(-2.5f * (vec2.x * 3.0f)));
        poseStack.mulPose(Axis.ZP.rotationDegrees(2.5f * (vec2.y * 3.0f)));
        float f2 = Math.abs(vec2.x);
        float f3 = Math.abs(vec2.y);
        poseStack.translate(0.0f, -0.1f * Math.max(f2, f3), 0.0f);
    }

    public float method_2367() {
        return this.field_2655;
    }

    public float method_2368() {
        return this.field_2656;
    }

    public FloatFloatPair method_2369() {
        return FloatFloatImmutablePair.of((float)Math.abs(this.field_2649 - this.field_2651), (float)Math.abs(this.field_2650 - this.field_2652));
    }

    public FloatFloatPair method_2373(float f) {
        return FloatFloatImmutablePair.of((float)MathUtils.lerpf1(this.field_2649, this.field_2651, f), (float)MathUtils.lerpf1(this.field_2650, this.field_2652, f));
    }
}

