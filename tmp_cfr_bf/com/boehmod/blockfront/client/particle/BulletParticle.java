/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import javax.annotation.Nonnull;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;

public class BulletParticle
extends TextureSheetParticle {
    public float field_1669;
    public float field_1668;
    @Nonnull
    public final Vector3f field_1670 = new Vector3f(0.0f, 0.0f, 0.0f);
    private static final Quaternionf field_6502 = new Quaternionf();
    private static final Quaternionf field_6503 = new Quaternionf();
    @Nonnull
    private final Vector3f[] field_6501 = new Vector3f[]{new Vector3f(-1.0f, -1.0f, 0.0f), new Vector3f(-1.0f, 1.0f, 0.0f), new Vector3f(1.0f, 1.0f, 0.0f), new Vector3f(1.0f, -1.0f, 0.0f)};

    protected BulletParticle(@Nonnull ClientLevel clientLevel, double d, double d2, double d3, @Nonnull Direction direction, @Nonnull SpriteSet spriteSet) {
        super(clientLevel, d, d2, d3, 0.0, 0.0, 0.0);
        this.pickSprite(spriteSet);
        this.quadSize = 1.0f;
        this.lifetime = (Minecraft.useFancyGraphics() ? 15 : 5) * 20;
        this.zd = 0.0;
        this.yd = 0.0;
        this.xd = 0.0;
        this.hasPhysics = false;
        this.scale(0.0625f);
        float f = 0.0f;
        float f2 = 0.0f;
        float f3 = 0.005f;
        switch (direction) {
            case WEST: {
                this.field_1670.set(-f3, 0.0f, 0.0f);
                f2 = 90.0f;
                break;
            }
            case EAST: {
                this.field_1670.set(f3, 0.0f, 0.0f);
                f2 = -90.0f;
                break;
            }
            case NORTH: {
                this.field_1670.set(0.0f, 0.0f, -f3);
                break;
            }
            case SOUTH: {
                this.field_1670.set(0.0f, 0.0f, f3);
                f2 = 180.0f;
                break;
            }
            case UP: {
                f = 90.0f;
                this.field_1670.set(0.0f, f3, 0.0f);
                break;
            }
            case DOWN: {
                f = -90.0f;
                this.field_1670.set(0.0f, -f3, 0.0f);
            }
        }
        float f4 = 0.0625f;
        if (direction == Direction.UP || direction == Direction.DOWN) {
            this.x -= this.x % (double)f4;
            this.z -= this.z % (double)f4;
        } else {
            this.x -= this.x % (double)f4;
            this.y -= this.y % (double)f4;
            this.z -= this.z % (double)f4;
        }
        this.field_1670.add(this.random.nextFloat() / 1000.0f, this.random.nextFloat() / 1000.0f, this.random.nextFloat() / 1000.0f);
        this.field_1668 = f;
        this.field_1669 = f2;
    }

    public void render(@NotNull VertexConsumer vertexConsumer, @NotNull Camera camera, float f) {
        if (this.age <= 0) {
            return;
        }
        Vec3 vec3 = camera.getPosition();
        float f2 = (float)(Mth.lerp((double)f, (double)this.xo, (double)this.x) - vec3.x + (double)this.field_1670.x);
        float f3 = (float)(Mth.lerp((double)f, (double)this.yo, (double)this.y) - vec3.y + (double)this.field_1670.y);
        float f4 = (float)(Mth.lerp((double)f, (double)this.zo, (double)this.z) - vec3.z + (double)this.field_1670.z);
        this.field_6501[0].set(-1.0f, -1.0f, 0.0f);
        this.field_6501[1].set(-1.0f, 1.0f, 0.0f);
        this.field_6501[2].set(1.0f, 1.0f, 0.0f);
        this.field_6501[3].set(1.0f, -1.0f, 0.0f);
        float f5 = this.getQuadSize(f);
        field_6502.identity().rotateX((float)Math.toRadians(this.field_1668));
        field_6503.identity().rotateY((float)Math.toRadians(this.field_1669));
        for (int i = 0; i < 4; ++i) {
            Vector3f vector3f = this.field_6501[i];
            vector3f.rotate((Quaternionfc)field_6502).rotate((Quaternionfc)field_6503);
            vector3f.mul(f5);
            vector3f.add(f2, f3, f4);
        }
        float f6 = this.getU0();
        float f7 = this.getU1();
        float f8 = this.getV0();
        float f9 = this.getV1();
        int n = this.getLightColor(f);
        Vector3f vector3f = this.field_6501[0];
        Vector3f vector3f2 = this.field_6501[1];
        Vector3f vector3f3 = this.field_6501[2];
        Vector3f vector3f4 = this.field_6501[3];
        vertexConsumer.addVertex(vector3f.x, vector3f.y, vector3f.z).setUv(f7, f9).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(n);
        vertexConsumer.addVertex(vector3f2.x, vector3f2.y, vector3f2.z).setUv(f7, f8).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(n);
        vertexConsumer.addVertex(vector3f3.x, vector3f3.y, vector3f3.z).setUv(f6, f8).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(n);
        vertexConsumer.addVertex(vector3f4.x, vector3f4.y, vector3f4.z).setUv(f6, f9).setColor(this.rCol, this.gCol, this.bCol, this.alpha).setLight(n);
    }

    public void tick() {
        super.tick();
        this.alpha = Math.max(0.0f, 1.0f - (float)this.age / ((float)this.lifetime - 4.0f));
        if (this.age >= this.lifetime) {
            this.remove();
        }
    }

    @NotNull
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider
    implements ParticleProvider<SimpleParticleType> {
        @Nonnull
        private final SpriteSet field_1672;

        public Provider(@Nonnull SpriteSet spriteSet) {
            this.field_1672 = spriteSet;
        }

        public Particle createParticle(@NotNull SimpleParticleType simpleParticleType, @NotNull ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6) {
            return new BulletParticle(clientLevel, d, d2, d3, Direction.values()[(int)d4], this.field_1672);
        }

        public /* synthetic */ Particle createParticle(@NotNull ParticleOptions particleOptions, @NotNull ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6) {
            return this.createParticle((SimpleParticleType)particleOptions, clientLevel, d, d2, d3, d4, d5, d6);
        }
    }
}

