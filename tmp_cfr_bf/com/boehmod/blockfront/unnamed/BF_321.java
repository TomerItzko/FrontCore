/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;

public abstract class BF_321
extends TextureSheetParticle {
    protected BF_321(ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6, SpriteSet spriteSet) {
        super(clientLevel, d, d2, d3, d4, d5, d6);
        this.pickSprite(spriteSet);
        this.bCol = 1.0f;
        this.gCol = 1.0f;
        this.rCol = 1.0f;
        this.quadSize = 0.01f;
        this.lifetime = 400;
        this.gravity = 1.0f;
        this.setSize(0.8f, 0.01f);
    }

    public void tick() {
        super.tick();
        if (this.yd > 0.0) {
            this.yd = 0.0;
        }
        this.xd = 0.0;
        this.zd = 0.0;
        this.alpha = MathUtils.moveTowards(this.alpha, 0.0f, 0.0025f);
        if (this.alpha <= 0.0f) {
            this.remove();
            return;
        }
        if (!this.removed) {
            this.quadSize = Mth.lerp((float)0.6f, (float)this.quadSize, (float)0.5f);
        }
    }

    public void render(@NotNull VertexConsumer vertexConsumer, @NotNull Camera camera, float f) {
        Vec3 vec3 = camera.getPosition();
        float f2 = (float)(Mth.lerp((double)f, (double)this.xo, (double)this.x) - vec3.x);
        float f3 = (float)(Mth.lerp((double)f, (double)this.yo, (double)this.y) - vec3.y) + 5.0E-5f * (float)this.age;
        float f4 = (float)(Mth.lerp((double)f, (double)this.zo, (double)this.z) - vec3.z);
        Quaternionf quaternionf = Axis.XP.rotationDegrees(90.0f);
        Vector3f vector3f = new Vector3f(-1.0f, -1.0f, 0.0f);
        Vector3f vector3f2 = new Vector3f(-1.0f, 1.0f, 0.0f);
        Vector3f vector3f3 = new Vector3f(1.0f, 1.0f, 0.0f);
        Vector3f vector3f4 = new Vector3f(1.0f, -1.0f, 0.0f);
        float f5 = this.getQuadSize(f);
        vector3f.rotate((Quaternionfc)quaternionf).mul(f5).add(f2, f3, f4);
        vector3f2.rotate((Quaternionfc)quaternionf).mul(f5).add(f2, f3, f4);
        vector3f3.rotate((Quaternionfc)quaternionf).mul(f5).add(f2, f3, f4);
        vector3f4.rotate((Quaternionfc)quaternionf).mul(f5).add(f2, f3, f4);
        float f6 = this.getU0();
        float f7 = this.getU1();
        float f8 = this.getV0();
        float f9 = this.getV1();
        int n = this.getLightColor(f);
        vertexConsumer.addVertex(vector3f.x, vector3f.y, vector3f.z).setUv(f7, f9).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(n);
        vertexConsumer.addVertex(vector3f2.x, vector3f2.y, vector3f2.z).setUv(f7, f8).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(n);
        vertexConsumer.addVertex(vector3f3.x, vector3f3.y, vector3f3.z).setUv(f6, f8).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(n);
        vertexConsumer.addVertex(vector3f4.x, vector3f4.y, vector3f4.z).setUv(f6, f9).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(n);
    }

    @NotNull
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
}

