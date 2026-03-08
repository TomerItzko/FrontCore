/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.ShaderInstance
 *  net.minecraft.client.renderer.texture.TextureAtlas
 *  net.minecraft.resources.ResourceLocation
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.client.event.BFShaderSubscriber;
import com.boehmod.blockfront.client.render.BFShaders;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public interface BF_266 {
    public static final BF_266 field_6490 = new BF_266(){

        @Override
        public void method_1105(@NotNull Minecraft minecraft) {
            RenderSystem.enableDepthTest();
            RenderSystem.depthMask((boolean)true);
            RenderSystem.setShaderTexture((int)0, (ResourceLocation)TextureAtlas.LOCATION_PARTICLES);
            RenderSystem.disableBlend();
            RenderSystem.setShader(BFShaderSubscriber::getInstancedParticle);
            RenderSystem.disableCull();
        }

        @Override
        public void method_1106(@NotNull Minecraft minecraft) {
            RenderSystem.enableCull();
        }
    };
    public static final BF_266 field_1587 = new BF_266(){

        @Override
        public void method_1105(@NotNull Minecraft minecraft) {
            RenderSystem.enableDepthTest();
            RenderSystem.depthMask((boolean)false);
            RenderSystem.setShaderTexture((int)0, (ResourceLocation)TextureAtlas.LOCATION_PARTICLES);
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate((int)770, (int)771, (int)1, (int)771);
            RenderSystem.setShader(BFShaderSubscriber::getInstancedParticle);
            RenderSystem.disableCull();
        }

        @Override
        public void method_1106(@NotNull Minecraft minecraft) {
            RenderSystem.depthMask((boolean)true);
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableBlend();
            RenderSystem.enableCull();
        }
    };
    public static final BF_266 field_6491 = new BF_266(){

        @Override
        public void method_1105(@NotNull Minecraft minecraft) {
            RenderSystem.enableDepthTest();
            RenderSystem.depthMask((boolean)false);
            RenderSystem.setShaderTexture((int)0, (ResourceLocation)TextureAtlas.LOCATION_PARTICLES);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc((int)770, (int)1);
            RenderSystem.setShader(BFShaderSubscriber::getInstancedParticle);
            RenderSystem.disableCull();
        }

        @Override
        public void method_1106(@NotNull Minecraft minecraft) {
            RenderSystem.depthMask((boolean)true);
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableBlend();
            RenderSystem.enableCull();
        }
    };
    public static final BF_266 field_1588 = new BF_266(){

        @Override
        public void method_1105(@NotNull Minecraft minecraft) {
            RenderSystem.depthMask((boolean)false);
            RenderSystem.enableDepthTest();
            RenderSystem.setShader(BFShaders::getInstancedSoftParticle);
            RenderSystem.setShaderTexture((int)0, (ResourceLocation)TextureAtlas.LOCATION_PARTICLES);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            ShaderInstance shaderInstance = RenderSystem.getShader();
            shaderInstance.setSampler("DiffuseDepthSampler", (Object)minecraft.getMainRenderTarget().getDepthTextureId());
            RenderSystem.disableCull();
        }

        @Override
        public void method_1106(@NotNull Minecraft minecraft) {
            RenderSystem.depthMask((boolean)true);
            RenderSystem.disableBlend();
            RenderSystem.enableCull();
        }
    };

    public void method_1105(@NotNull Minecraft var1);

    public void method_1106(@NotNull Minecraft var1);
}

