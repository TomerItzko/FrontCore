/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public abstract class BF_633<V extends AbstractVehicleEntity> {
    public final String field_2736;
    public int field_2739 = -1;
    public IntList field_2737 = new IntArrayList();
    public Vector3f field_2738 = new Vector3f(0.0f, 0.0f, 0.0f);
    public V field_2735;

    public BF_633(@NotNull String string, @NotNull V v) {
        this.field_2736 = string;
        this.field_2735 = v;
    }

    public BF_633<V> method_2464(int n) {
        this.field_2739 = n;
        return this;
    }

    public BF_633<V> method_2466(int n) {
        this.field_2737.add(n);
        return this;
    }

    public float method_2463() {
        return this.method_2471(1.0f);
    }

    public float method_2471(float f) {
        return 0.0f;
    }

    public abstract void method_2467(@NotNull V var1);

    @Nullable
    public Entity method_2465() {
        List list = this.field_2735.getPassengers();
        if (!list.isEmpty() && this.field_2739 > -1 && this.field_2739 < list.size()) {
            return (Entity)list.get(this.field_2739);
        }
        return null;
    }

    @OnlyIn(value=Dist.CLIENT)
    public abstract void method_2468(@NotNull V var1);

    @OnlyIn(value=Dist.CLIENT)
    public abstract void method_2470(@NotNull Minecraft var1);

    @NotNull
    public abstract ResourceLocation method_2469();
}

