/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.client.render.model.BakedVehicleMesh;
import com.boehmod.blockfront.unnamed.BF_288;
import com.mojang.blaze3d.vertex.VertexBuffer;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Iterator;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BF_363 {
    private static final int field_1770 = 20;
    @NotNull
    private final Map<BF_288, BakedVehicleMesh> field_1768 = new Object2ObjectOpenHashMap();
    private long field_1769;

    public void tick() {
        long l = this.field_1769++;
        Iterator<BakedVehicleMesh> iterator = this.field_1768.values().iterator();
        while (iterator.hasNext()) {
            BakedVehicleMesh bakedVehicleMesh = iterator.next();
            if (l - bakedVehicleMesh.getLastUpdated() <= 20L) continue;
            bakedVehicleMesh.close();
            iterator.remove();
        }
    }

    @Nullable
    public BakedVehicleMesh method_1299(@NotNull BF_288 bF_288) {
        BakedVehicleMesh bakedVehicleMesh = this.field_1768.get(bF_288);
        if (bakedVehicleMesh != null) {
            bakedVehicleMesh.setLastUpdated(this.field_1769);
        }
        return bakedVehicleMesh;
    }

    public void method_1300(@NotNull BF_288 bF_288, @NotNull VertexBuffer vertexBuffer) {
        this.field_1768.put(bF_288, new BakedVehicleMesh(vertexBuffer, this.field_1769));
    }

    public void method_1301() {
        this.field_1768.values().forEach(BakedVehicleMesh::close);
        this.field_1768.clear();
    }
}

