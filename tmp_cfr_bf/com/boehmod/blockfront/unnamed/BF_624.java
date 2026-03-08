/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.common.entity.vehicle.AbstractVehicleControl;
import com.boehmod.blockfront.unnamed.BF_623;
import com.boehmod.blockfront.unnamed.BF_629;
import com.boehmod.blockfront.unnamed.BF_633;
import com.boehmod.blockfront.util.BFRes;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

public class BF_624<V extends AbstractVehicleEntity> {
    public AbstractVehicleControl<V> field_2675 = null;
    public Supplier<Item> field_2676 = null;
    @NotNull
    public List<BF_623> field_2678 = new ObjectArrayList();
    @NotNull
    public List<BF_633<V>> field_2679 = new ObjectArrayList();
    @NotNull
    public List<BF_629<V>> field_2680 = new ObjectArrayList();
    public float field_2684 = 1.0f;
    public float field_2685 = 6.0f;
    public int field_2700 = 0;
    public int field_2701 = 80;
    public float field_2686 = 0.025f;
    public float field_2687 = 0.015f;
    public float field_2688 = 1.0f;
    public float field_2689 = 0.8f;
    public float field_2690 = 0.0f;
    public float field_2691 = 0.1f;
    public float field_2692 = 0.1f;
    public float field_2693 = 0.1f;
    public float field_2694 = 0.1f;
    public float field_2695 = 0.75f;
    public float field_2696 = 0.7f;
    public float field_2697 = 1.5f;
    public float field_2698 = 1.5f;
    public float field_2699 = 1.3f;
    public String field_2677 = "tank";
    private ResourceLocation field_2683;
    public boolean field_2681 = false;
    public boolean field_2682 = false;

    public BF_624() {
        this.method_2411();
    }

    public void method_2411() {
        this.field_2683 = BFRes.loc("textures/gui/game/vehicle/" + this.field_2677 + ".png");
    }

    public BF_624<V> method_2402(String string) {
        this.field_2677 = string;
        return this;
    }

    public BF_624<V> method_2406(boolean bl) {
        this.field_2682 = bl;
        return this;
    }

    public BF_624<V> method_2405(@NotNull Supplier<Item> supplier) {
        this.field_2676 = supplier;
        return this;
    }

    public BF_624<V> method_2398(@NotNull AbstractVehicleControl<V> abstractVehicleControl) {
        this.field_2675 = abstractVehicleControl;
        return this;
    }

    public BF_624<V> method_2394(float f) {
        this.field_2684 = f;
        return this;
    }

    public BF_624<V> method_2400(@NotNull BF_629<V> bF_629) {
        this.field_2680.add(bF_629);
        return this;
    }

    public BF_624<V> method_2397(int n) {
        this.field_2700 = n;
        return this;
    }

    public BF_624<V> method_2408(float f) {
        this.field_2685 = f;
        return this;
    }

    public BF_624<V> method_2410(int n) {
        this.field_2701 = n;
        return this;
    }

    public BF_624<V> method_2395(float f, float f2) {
        this.field_2686 = f;
        this.field_2687 = f2;
        return this;
    }

    public BF_624<V> method_2413(float f) {
        this.field_2689 = f;
        return this;
    }

    public BF_624<V> method_2409(float f, float f2) {
        this.field_2691 = f;
        this.field_2692 = f2;
        return this;
    }

    public BF_624<V> method_2414(float f, float f2) {
        this.field_2693 = f;
        this.field_2694 = f2;
        return this;
    }

    public BF_624<V> method_2415(float f) {
        this.field_2696 = f;
        return this;
    }

    public BF_624<V> method_2417(float f) {
        this.field_2695 = f;
        return this;
    }

    public BF_624<V> method_2416(float f, float f2) {
        this.field_2697 = f;
        this.field_2698 = f2;
        return this;
    }

    public BF_624<V> method_2399(BF_623 bF_623) {
        this.field_2678.add(bF_623);
        return this;
    }

    public BF_624<V> method_2401(@NotNull BF_633<V> bF_633) {
        this.field_2679.add(bF_633);
        return this;
    }

    public BF_624<V> method_2419(float f) {
        this.field_2688 = f;
        return this;
    }

    public BF_624<V> method_2420(float f) {
        this.field_2690 = f;
        return this;
    }

    public BF_624<V> method_2412() {
        this.field_2681 = true;
        return this;
    }

    @Nullable
    public BF_623 method_2418() {
        for (BF_623 bF_623 : this.field_2678) {
            if (!bF_623.field_2660) continue;
            return bF_623;
        }
        return null;
    }

    public int method_2407() {
        int n = 0;
        for (BF_623 bF_623 : this.field_2678) {
            if (bF_623.field_2660) {
                return n;
            }
            ++n;
        }
        return -1;
    }

    public BF_623 method_2396(int n) {
        if (n >= this.field_2678.size()) {
            return new BF_623();
        }
        return this.field_2678.get(n);
    }

    public BF_633<V> method_2403(String string) {
        return this.field_2679.stream().filter(bF_633 -> bF_633.field_2736.equals(string)).findFirst().orElse(null);
    }

    public ResourceLocation method_2421() {
        return this.field_2683;
    }
}

