/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.DefaultVertexFormat
 *  net.minecraft.client.renderer.ShaderInstance
 *  net.minecraft.resources.ResourceLocation
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.client.event.RegisterShadersEvent
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.boehmod.blockfront.client.event;

import com.boehmod.blockfront.util.BFRes;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import java.io.IOException;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@EventBusSubscriber(modid="bf", value={Dist.CLIENT})
public class BFShaderSubscriber {
    public static final String INSTANCED_PARTICLE_ID = "instanced_particle";
    public static final ResourceLocation INSTANCED_PARTICLE_RES = BFRes.loc("instanced_particle");
    @Nullable
    private static ShaderInstance instancedParticle;

    @Nullable
    public static ShaderInstance getInstancedParticle() {
        return instancedParticle;
    }

    public static void setInstancedParticle(@NotNull ShaderInstance shader) {
        instancedParticle = shader;
    }

    @SubscribeEvent
    public static void method_1119(@NotNull RegisterShadersEvent registerShadersEvent) {
        try {
            registerShadersEvent.registerShader(new ShaderInstance(registerShadersEvent.getResourceProvider(), INSTANCED_PARTICLE_RES, DefaultVertexFormat.NEW_ENTITY), BFShaderSubscriber::setInstancedParticle);
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
    }
}

