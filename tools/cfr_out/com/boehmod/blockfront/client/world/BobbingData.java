/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  it.unimi.dsi.fastutil.objects.ObjectList
 *  net.minecraft.util.Mth
 *  org.jetbrains.annotations.NotNull
 *  org.joml.Vector3f
 */
package com.boehmod.blockfront.client.world;

import com.boehmod.blockfront.client.world.BobbingPoint;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.util.List;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class BobbingData {
    @NotNull
    private final ObjectList<BobbingPoint> field_1624 = new ObjectArrayList();
    @NotNull
    private final ObjectList<BobbingPoint> field_1625 = new ObjectArrayList();
    @NotNull
    private final ObjectList<BobbingPoint> field_1626 = new ObjectArrayList();

    public BobbingData method_1163(float f, float f2, float f3) {
        this.field_1624.add((Object)new BobbingPoint(f, f2, f3));
        return this;
    }

    public BobbingData method_1166(float f, float f2, float f3) {
        this.field_1625.add((Object)new BobbingPoint(f, f2, f3));
        return this;
    }

    public BobbingData method_1167(float f, float f2, float f3) {
        this.field_1626.add((Object)new BobbingPoint(f, f2, f3));
        return this;
    }

    private float method_1165(@NotNull List<BobbingPoint> list, float f, float f2) {
        return list.stream().map(bobbingPoint -> Float.valueOf(Mth.sin((float)(f / bobbingPoint.field_1630 + bobbingPoint.field_1632)) * bobbingPoint.field_1631)).reduce(Float.valueOf(0.0f), Float::sum).floatValue() / f2;
    }

    public Vector3f getCameraVec(float renderTime, float f) {
        return new Vector3f(this.method_1165((List<BobbingPoint>)this.field_1624, renderTime, f), this.method_1165((List<BobbingPoint>)this.field_1625, renderTime, f), this.method_1165((List<BobbingPoint>)this.field_1626, renderTime, f));
    }
}

