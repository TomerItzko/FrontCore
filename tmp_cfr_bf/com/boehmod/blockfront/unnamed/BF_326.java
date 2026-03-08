/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.unnamed.BF_266;
import com.boehmod.blockfront.unnamed.BF_269;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.level.BlockAndTintGetter;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector3dc;

public abstract class BF_326
extends TextureSheetParticle
implements BF_269 {
    private static final int field_1687 = 5;
    @NotNull
    private final BlockPos.MutableBlockPos field_1684 = new BlockPos.MutableBlockPos();
    @NotNull
    private final Vector3d field_1686 = new Vector3d();
    protected float field_1685;
    private int field_1688 = 0;
    private int field_1689 = 0xFFFFFF;

    public BF_326(@NotNull ClientLevel clientLevel, double d, double d2, double d3, double d4, double d5, double d6, @NotNull SpriteSet spriteSet) {
        super(clientLevel, d, d2, d3, d4, d5, d6);
        this.pickSprite(spriteSet);
    }

    public void method_1219() {
        this.method_1220();
        super.tick();
    }

    private void method_1220() {
        this.field_1685 = this.quadSize;
        if (this.age % 5 == 0) {
            this.method_1222();
        }
        this.method_1221();
    }

    protected int getLightColor(float f) {
        return this.field_1688;
    }

    public void tick() {
        this.method_1220();
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        ++this.age;
        this.method_1221();
    }

    @Override
    public boolean method_1126() {
        this.tick();
        return this.removed;
    }

    @Override
    @NotNull
    public Vector3dc method_1123(float f) {
        return this.field_1686.set(Mth.lerp((double)f, (double)this.xo, (double)this.x), Mth.lerp((double)f, (double)this.yo, (double)this.y), Mth.lerp((double)f, (double)this.zo, (double)this.z));
    }

    @Override
    public float method_1125(float f) {
        return this.getQuadSize(f);
    }

    @Override
    public int method_1121(float f) {
        return this.field_1689;
    }

    @Override
    @NotNull
    public TextureAtlasSprite method_1122(float f) {
        return this.sprite;
    }

    public float getQuadSize(float f) {
        return Mth.lerp((float)f, (float)this.field_1685, (float)this.quadSize);
    }

    @Override
    public int method_1124(float f) {
        return this.getLightColor(f);
    }

    protected void method_1221() {
        this.field_1689 = FastColor.ARGB32.colorFromFloat((float)this.alpha, (float)this.rCol, (float)this.gCol, (float)this.bCol);
    }

    private void method_1222() {
        BlockPos blockPos = BlockPos.containing((double)this.x, (double)this.y, (double)this.z);
        if (!this.field_1684.equals((Object)blockPos)) {
            this.field_1688 = this.level.hasChunkAt(blockPos) ? LevelRenderer.getLightColor((BlockAndTintGetter)this.level, (BlockPos)blockPos) : 0;
            this.field_1684.set((Vec3i)blockPos);
        }
    }

    @NotNull
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    @NotNull
    public abstract BF_266 method_1120();
}

