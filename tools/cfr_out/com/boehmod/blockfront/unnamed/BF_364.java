/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.Lighting
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.util.FastColor$ARGB32
 *  org.jetbrains.annotations.NotNull
 *  org.joml.Matrix4f
 *  org.joml.Vector2dc
 *  org.joml.Vector3f
 *  org.joml.Vector3fc
 */
package com.boehmod.blockfront.unnamed;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.FastColor;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector2dc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class BF_364
implements VertexConsumer {
    @NotNull
    private final ClientLevel field_1773;
    @NotNull
    private final VertexConsumer field_1771;
    @NotNull
    private final Vector2dc field_1772;
    @NotNull
    private final Vector2dc field_1774;

    public BF_364(@NotNull ClientLevel clientLevel, @NotNull VertexConsumer vertexConsumer, @NotNull Vector2dc vector2dc, @NotNull Vector2dc vector2dc2) {
        this.field_1773 = clientLevel;
        this.field_1771 = vertexConsumer;
        this.field_1772 = vector2dc;
        this.field_1774 = vector2dc2;
    }

    public void addVertex(float f, float f2, float f3, int n, float f4, float f5, int n2, int n3, float f6, float f7, float f8) {
        Vector3f vector3f = new Vector3f(f6, f7, f8);
        Vector3f vector3f2 = Lighting.DIFFUSE_LIGHT_0;
        Vector3f vector3f3 = Lighting.DIFFUSE_LIGHT_1;
        if (this.field_1773.effects().constantAmbientLight()) {
            vector3f2 = Lighting.NETHER_DIFFUSE_LIGHT_0;
            vector3f3 = Lighting.NETHER_DIFFUSE_LIGHT_1;
        }
        double d = Math.max(0.0, (double)vector3f2.dot((Vector3fc)vector3f));
        double d2 = Math.max(0.0, (double)vector3f3.dot((Vector3fc)vector3f));
        double d3 = Math.min(1.0, (d + d2) * 0.6 + 0.4);
        int n4 = (int)((double)FastColor.ARGB32.red((int)n) * d3);
        int n5 = (int)((double)FastColor.ARGB32.green((int)n) * d3);
        int n6 = (int)((double)FastColor.ARGB32.blue((int)n) * d3);
        int n7 = FastColor.ARGB32.color((int)n4, (int)n5, (int)n6);
        this.addVertex(f, f2, f3);
        this.setColor(n7);
        this.setUv(f4, f5);
        this.setOverlay(n2);
        this.setLight(n3);
        this.setNormal(f6, f7, f8);
    }

    @NotNull
    public VertexConsumer addVertex(@NotNull Matrix4f matrix4f, float f, float f2, float f3) {
        return super.addVertex(matrix4f, f, f2, f3);
    }

    @NotNull
    public VertexConsumer addVertex(float f, float f2, float f3) {
        this.field_1771.addVertex(f, f2, f3);
        return this;
    }

    @NotNull
    public VertexConsumer setColor(int n, int n2, int n3, int n4) {
        this.field_1771.setColor(n, n2, n3, n4);
        return this;
    }

    @NotNull
    public VertexConsumer setUv(float f, float f2) {
        double d = this.field_1772.x();
        double d2 = this.field_1772.y();
        double d3 = this.field_1774.x() - d;
        double d4 = this.field_1774.y() - d2;
        double d5 = (double)f * d3 + d;
        double d6 = (double)f2 * d4 + d2;
        this.field_1771.setUv((float)d5, (float)d6);
        return this;
    }

    @NotNull
    public VertexConsumer setUv1(int n, int n2) {
        this.field_1771.setUv1(n, n2);
        return this;
    }

    @NotNull
    public VertexConsumer setUv2(int n, int n2) {
        this.field_1771.setUv2(n, n2);
        return this;
    }

    @NotNull
    public VertexConsumer setNormal(float f, float f2, float f3) {
        this.field_1771.setNormal(f, f2, f3);
        return this;
    }
}

