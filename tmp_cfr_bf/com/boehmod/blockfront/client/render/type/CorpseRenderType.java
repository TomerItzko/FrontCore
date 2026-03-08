/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render.type;

import com.boehmod.blockfront.util.BFRes;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.io.IOException;
import java.util.function.Function;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid="bf", value={Dist.CLIENT})
public class CorpseRenderType
extends RenderType {
    public static final String NAME = "corpse";
    public static final ResourceLocation ID = BFRes.loc("rendertype_corpse");
    private static ShaderInstance instance;
    private static final RenderStateShard.ShaderStateShard STATE;
    public static Function<ResourceLocation, RenderType> RENDER_TYPE_CONSTRUCTOR;

    public CorpseRenderType(String string, VertexFormat vertexFormat, VertexFormat.Mode mode, int n, boolean bl, boolean bl2, Runnable runnable, Runnable runnable2) {
        super(string, vertexFormat, mode, n, bl, bl2, runnable, runnable2);
    }

    private static RenderType createRenderType(@NotNull ResourceLocation id) {
        RenderType.CompositeState compositeState = RenderType.CompositeState.builder().setShaderState(STATE).setTextureState((RenderStateShard.EmptyTextureStateShard)new RenderStateShard.TextureStateShard(id, false, false)).setTransparencyState(NO_TRANSPARENCY).setLightmapState(LIGHTMAP).setOverlayState(OVERLAY).createCompositeState(true);
        return CorpseRenderType.create((String)NAME, (VertexFormat)DefaultVertexFormat.NEW_ENTITY, (VertexFormat.Mode)VertexFormat.Mode.QUADS, (int)256, (boolean)true, (boolean)false, (RenderType.CompositeState)compositeState);
    }

    public static void setInstance(@NotNull ShaderInstance instance) {
        CorpseRenderType.instance = instance;
    }

    @SubscribeEvent
    public static void onRegisterShaders(@NotNull RegisterShadersEvent event) {
        try {
            event.registerShader(new ShaderInstance(event.getResourceProvider(), ID, DefaultVertexFormat.NEW_ENTITY), CorpseRenderType::setInstance);
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
    }

    static {
        STATE = new RenderStateShard.ShaderStateShard(() -> instance);
        RENDER_TYPE_CONSTRUCTOR = Util.memoize(CorpseRenderType::createRenderType);
    }
}

