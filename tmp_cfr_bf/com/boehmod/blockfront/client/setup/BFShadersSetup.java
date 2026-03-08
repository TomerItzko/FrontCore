/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.setup;

import com.boehmod.blockfront.client.render.BFShaders;
import com.boehmod.blockfront.util.BFRes;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import java.io.IOException;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import org.jetbrains.annotations.NotNull;

public class BFShadersSetup {
    public static ShaderInstance shader;
    public static final ResourceLocation VEHICLE_BLOCK_NAME;

    public static void register(@NotNull RegisterShadersEvent event) throws IOException {
        event.registerShader(new ShaderInstance(event.getResourceProvider(), BFShaders.SOFT_PARTICLE_NAME, DefaultVertexFormat.PARTICLE), BFShaders::setSoftParticle);
        event.registerShader(new ShaderInstance(event.getResourceProvider(), BFShaders.INSTANCED_SOFT_PARTICLE_NAME, DefaultVertexFormat.PARTICLE), BFShaders::setInstancedSoftParticle);
        event.registerShader(new ShaderInstance(event.getResourceProvider(), VEHICLE_BLOCK_NAME, DefaultVertexFormat.NEW_ENTITY), shaderInstance -> {
            shader = shaderInstance;
        });
    }

    static {
        VEHICLE_BLOCK_NAME = BFRes.loc("rendertype_vehicle_block");
    }
}

