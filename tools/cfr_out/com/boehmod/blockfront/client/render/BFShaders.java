/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.BufferBuilder
 *  com.mojang.blaze3d.vertex.DefaultVertexFormat
 *  com.mojang.blaze3d.vertex.Tesselator
 *  com.mojang.blaze3d.vertex.VertexFormat$Mode
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.particle.ParticleRenderType
 *  net.minecraft.client.renderer.ShaderInstance
 *  net.minecraft.client.renderer.texture.TextureAtlas
 *  net.minecraft.client.renderer.texture.TextureManager
 *  net.minecraft.resources.ResourceLocation
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.boehmod.blockfront.client.render;

import com.boehmod.blockfront.util.BFRes;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BFShaders
implements ParticleRenderType {
    @NotNull
    public static final BFShaders INSTANCE = new BFShaders();
    @NotNull
    public static final ResourceLocation SOFT_PARTICLE_NAME = BFRes.loc("soft_particle");
    @NotNull
    public static final ResourceLocation INSTANCED_SOFT_PARTICLE_NAME = BFRes.loc("instanced_soft_particle");
    private static ShaderInstance softParticle;
    private static ShaderInstance instancedSoftParticle;

    private BFShaders() {
    }

    @ApiStatus.Internal
    public static void setSoftParticle(@NotNull ShaderInstance shader) {
        softParticle = Objects.requireNonNull(shader);
    }

    @ApiStatus.Internal
    public static void setInstancedSoftParticle(@NotNull ShaderInstance shader) {
        instancedSoftParticle = Objects.requireNonNull(shader);
    }

    public static ShaderInstance getSoftParticle() {
        return softParticle;
    }

    public static ShaderInstance getInstancedSoftParticle() {
        return instancedSoftParticle;
    }

    @Nullable
    public BufferBuilder begin(Tesselator tesselator, @NotNull TextureManager textureManager) {
        Minecraft minecraft = Minecraft.getInstance();
        RenderSystem.setShader(BFShaders::getSoftParticle);
        RenderSystem.depthMask((boolean)false);
        RenderSystem.setShaderTexture((int)0, (ResourceLocation)TextureAtlas.LOCATION_PARTICLES);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        softParticle.setSampler("DiffuseDepthSampler", (Object)minecraft.getMainRenderTarget().getDepthTextureId());
        return tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
    }
}

