/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render.type;

import com.boehmod.blockfront.util.BFRes;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.io.IOException;
import java.util.function.Function;
import javax.annotation.Nullable;
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
public class WeaponOverlayRenderType
extends RenderType {
    @NotNull
    public static final String field_6663 = "weapon_overlay";
    @NotNull
    public static final ResourceLocation field_6666 = BFRes.loc("rendertype_weapon_overlay");
    private static ShaderInstance field_6669;
    @NotNull
    private static final RenderStateShard.ShaderStateShard field_6664;
    @NotNull
    public static Function<ResourceLocation, RenderType> field_6665;

    public WeaponOverlayRenderType(String string, VertexFormat vertexFormat, VertexFormat.Mode mode, int n, boolean bl, boolean bl2, Runnable runnable, Runnable runnable2) {
        super(string, vertexFormat, mode, n, bl, bl2, runnable, runnable2);
    }

    private static RenderType method_5632(@NotNull ResourceLocation resourceLocation) {
        RenderType.CompositeState compositeState = RenderType.CompositeState.builder().setShaderState(field_6664).setTextureState((RenderStateShard.EmptyTextureStateShard)new RenderStateShard.TextureStateShard(resourceLocation, false, false)).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setCullState(NO_CULL).setLightmapState(LIGHTMAP).setOverlayState(OVERLAY).createCompositeState(true);
        return WeaponOverlayRenderType.create((String)field_6663, (VertexFormat)DefaultVertexFormat.NEW_ENTITY, (VertexFormat.Mode)VertexFormat.Mode.QUADS, (int)256, (boolean)true, (boolean)false, (RenderType.CompositeState)compositeState);
    }

    public static void method_5635(@NotNull ShaderInstance shaderInstance) {
        field_6669 = shaderInstance;
    }

    @Nullable
    public static ShaderInstance method_5940() {
        return field_6669;
    }

    @SubscribeEvent
    public static void register(@NotNull RegisterShadersEvent event) {
        try {
            event.registerShader(new ShaderInstance(event.getResourceProvider(), field_6666, DefaultVertexFormat.NEW_ENTITY), WeaponOverlayRenderType::method_5635);
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
    }

    static {
        field_6664 = new RenderStateShard.ShaderStateShard(() -> field_6669);
        field_6665 = Util.memoize(WeaponOverlayRenderType::method_5632);
    }
}

