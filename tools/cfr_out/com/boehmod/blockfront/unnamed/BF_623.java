/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  net.minecraft.resources.ResourceLocation
 *  org.jetbrains.annotations.NotNull
 *  org.joml.Vector3f
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.unnamed.BF_633;
import com.boehmod.blockfront.util.BFRes;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class BF_623 {
    public static final ResourceLocation field_2665 = BFRes.loc("textures/gui/vehicle/icon_gunner.png");
    public static final ResourceLocation field_2666 = BFRes.loc("textures/gui/vehicle/icon_driver.png");
    public static final ResourceLocation field_2667 = BFRes.loc("textures/gui/vehicle/icon_commander.png");
    public static final ResourceLocation field_2668 = BFRes.loc("textures/gui/vehicle/icon_passenger.png");
    public boolean field_2660 = false;
    public float field_2670 = 0.0f;
    @NotNull
    private ResourceLocation icon = field_2666;
    @NotNull
    public Vector3f field_2669 = new Vector3f(0.0f, 0.0f, 0.0f);
    @NotNull
    public Vector3f field_2674 = new Vector3f(0.0f, 0.0f, 0.0f);
    @NotNull
    private final List<BF_633<?>> field_2659 = new ObjectArrayList();
    public float field_2671 = 75.0f;
    public float field_2672 = 105.0f;
    public boolean field_2661 = false;
    public boolean field_2662 = false;
    public boolean field_2663 = false;
    public boolean field_2664 = false;
    public String field_2658 = "";
    public float field_2673 = 0.0f;

    public BF_623 method_2385(@NotNull ResourceLocation resourceLocation) {
        this.icon = resourceLocation;
        return this;
    }

    public BF_623 method_2383(@NotNull BF_633<?> bF_633) {
        this.field_2659.add(bF_633);
        return this;
    }

    public BF_623 method_2380() {
        this.field_2664 = true;
        return this;
    }

    public BF_623 method_2387() {
        this.field_2663 = true;
        return this;
    }

    public BF_623 method_2391() {
        this.field_2660 = true;
        return this;
    }

    public BF_623 method_2392() {
        this.field_2661 = true;
        return this;
    }

    public BF_623 method_2393() {
        this.field_2662 = true;
        return this;
    }

    public BF_623 method_2382(float f, float f2) {
        this.field_2671 = f;
        this.field_2672 = f2;
        return this;
    }

    public BF_623 method_2381(float f) {
        this.field_2670 = f;
        return this;
    }

    public BF_623 method_2386(Vector3f vector3f) {
        this.field_2669 = vector3f;
        return this;
    }

    public BF_623 method_2390(Vector3f vector3f) {
        this.field_2674 = vector3f;
        return this;
    }

    public BF_623 method_2389(float f) {
        this.field_2673 = f;
        return this;
    }

    public String method_2378() {
        return this.field_2658;
    }

    public BF_623 method_2384(String string) {
        this.field_2658 = string;
        return this;
    }

    @NotNull
    public List<BF_633<?>> method_2388() {
        return this.field_2659;
    }

    public float method_2379() {
        return this.field_2673;
    }

    @NotNull
    public ResourceLocation getIcon() {
        return this.icon;
    }
}

