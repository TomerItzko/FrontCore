/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.entity.vehicle;

import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.common.net.packet.BFVehicleControlPacket;
import com.boehmod.blockfront.unnamed.BF_624;
import com.boehmod.blockfront.util.PacketUtils;
import com.boehmod.blockfront.util.math.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec2;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractVehicleControl<V extends AbstractVehicleEntity> {
    public int field_2603 = 0;
    public float field_2593 = 0.0f;
    public float field_2594 = 0.0f;
    public boolean field_2587;
    public boolean field_2588;
    public boolean field_2589;
    public boolean field_2590;
    public boolean field_2591;
    public boolean field_2592;
    private float field_2595;
    private float field_2596 = 0.0f;
    private float field_2597;
    private float field_2598 = 0.0f;
    private float field_2599;
    private float field_2600 = 0.0f;
    private float field_2601;
    private float field_2602 = 0.0f;

    public float method_2299(float f) {
        return MathUtils.lerpf1(this.field_2599, this.field_2600, f);
    }

    public float method_2300(float f) {
        return MathUtils.lerpf1(this.field_2601, this.field_2602, f);
    }

    public float method_2301(float f) {
        return MathUtils.lerpf1(this.field_2595, this.field_2596, f);
    }

    public float method_2302(float f) {
        return MathUtils.lerpf1(this.field_2597, this.field_2598, f);
    }

    public void method_2296(@NotNull V v) {
        if (v.level().isClientSide()) {
            Vec2 vec2 = this.method_2295(v);
            this.field_2600 = this.field_2599;
            this.field_2602 = this.field_2601;
            this.field_2599 -= vec2.x;
            this.field_2601 += vec2.y;
            Vec2 vec22 = this.method_2290(v);
            this.field_2596 = this.field_2595;
            this.field_2598 = this.field_2597;
            this.field_2595 = vec22.x;
            this.field_2597 = vec22.y;
        }
        this.method_2297(v);
    }

    public abstract void method_2297(@NotNull V var1);

    @OnlyIn(value=Dist.CLIENT)
    public void method_2294(@NotNull LocalPlayer localPlayer, @NotNull V v, boolean bl) {
        if (bl) {
            this.method_2298(localPlayer);
            this.method_2291(v, localPlayer);
        }
        this.method_2292(v, bl);
    }

    @NotNull
    public abstract Vec2 method_2290(@NotNull V var1);

    @NotNull
    public abstract Vec2 method_2295(@NotNull V var1);

    @OnlyIn(value=Dist.CLIENT)
    public void method_2298(@NotNull LocalPlayer localPlayer) {
        Input input = localPlayer.input;
        this.field_2589 = input.left;
        this.field_2590 = input.right;
        this.field_2587 = input.up;
        this.field_2588 = input.down;
        this.field_2591 = Minecraft.getInstance().options.keySprint.isDown();
        this.field_2592 = input.jumping;
        if (this.field_2603++ >= 5) {
            this.field_2603 = 0;
            PacketUtils.sendToServer(new BFVehicleControlPacket(this.field_2587, this.field_2588, this.field_2589, this.field_2590, this.field_2591, this.field_2592, this.field_2593));
        }
    }

    @OnlyIn(value=Dist.CLIENT)
    public abstract void method_2292(@NotNull V var1, boolean var2);

    @OnlyIn(value=Dist.CLIENT)
    public abstract void method_2291(@NotNull V var1, @NotNull LocalPlayer var2);

    public abstract float method_2293(@NotNull BF_624<?> var1);
}

