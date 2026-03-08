/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.particle.ParticleRenderType
 *  net.minecraft.client.particle.SpriteSet
 *  net.minecraft.client.particle.TextureSheetParticle
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  org.jetbrains.annotations.NotNull
 *  org.joml.Quaternionf
 */
package com.boehmod.blockfront.client.particle;

import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

public abstract class SurrenderPaperParticle
extends TextureSheetParticle {
    private static final float field_7059 = 360.0f;
    private static final float field_7060 = -0.07f;
    private static final float field_7061 = 0.025f;
    private static final float field_7062 = 0.01f;
    private static final float field_7063 = 0.01f;
    private static final float field_7064 = 0.02f;
    private static final float field_7065 = 0.03f;
    private boolean field_1582 = false;
    private float field_1579;
    private float field_1583;
    private float field_1580;
    private float field_1584;
    private float field_1581;
    private float field_1585;

    protected SurrenderPaperParticle(ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6, float f, SpriteSet spriteSet) {
        super(clientLevel, d, d2, d3, d4, d5, d6);
        this.pickSprite(spriteSet);
        this.alpha = 0.0f;
        this.quadSize = f;
        this.lifetime = 800;
        this.bCol = 0.9f;
        this.gCol = 0.9f;
        this.rCol = 0.9f;
        this.hasPhysics = true;
        this.field_1579 = this.field_1583 = this.random.nextFloat() * 360.0f;
        this.field_1580 = this.field_1584 = this.random.nextFloat() * 360.0f;
        this.field_1581 = this.field_1585 = this.random.nextFloat() * 360.0f;
    }

    protected void renderRotatedQuad(@NotNull VertexConsumer vertexConsumer, @NotNull Quaternionf quaternionf, float f, float f2, float f3, float f4) {
        quaternionf = new Quaternionf().rotateLocalX(MathUtils.lerpf1(this.field_1579, this.field_1583, f4)).rotateLocalY(MathUtils.lerpf1(this.field_1580, this.field_1584, f4)).rotateLocalZ(MathUtils.lerpf1(this.field_1581, this.field_1585, f4));
        super.renderRotatedQuad(vertexConsumer, quaternionf, f, f2, f3, f4);
    }

    public void tick() {
        this.yd = -0.07f;
        this.field_1583 = this.field_1579;
        this.field_1584 = this.field_1580;
        this.field_1585 = this.field_1581;
        if (this.onGround) {
            this.xd = 0.0;
            this.yd = 0.0;
            this.zd = 0.0;
        } else {
            this.field_1579 += 0.01f;
            this.field_1580 += 0.02f;
            this.field_1581 += 0.03f;
        }
        super.tick();
        if (this.onGround) {
            this.method_5971();
        } else {
            this.alpha = MathUtils.moveTowards(this.alpha, 1.0f, 0.01f);
        }
    }

    private void method_5971() {
        float f;
        if (!this.field_1582) {
            this.field_1582 = true;
            if (this.random.nextBoolean()) {
                float f2 = 0.8f + this.random.nextFloat() * 0.4f;
                this.level.playLocalSound(this.x, this.y, this.z, (SoundEvent)BFSounds.PARTICLE_SURRENDERPAPER_FALL.get(), SoundSource.AMBIENT, 0.15f, f2, false);
            }
        }
        this.alpha -= 0.025f;
        if (f <= 0.0f) {
            this.remove();
        }
    }

    @NotNull
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
}

