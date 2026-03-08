/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.render;

import com.boehmod.blockfront.unnamed.BF_1150;
import com.boehmod.blockfront.unnamed.BF_266;
import com.boehmod.blockfront.unnamed.BF_269;
import com.boehmod.blockfront.util.BFLog;
import com.google.common.collect.EvictingQueue;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Map;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Matrix4fc;
import org.joml.Vector3dc;
import org.lwjgl.opengl.GL15C;
import org.lwjgl.opengl.GL20C;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL33C;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.NativeResource;

public class InstancedParticleHolder
implements NativeResource {
    @NotNull
    private static final VertexFormat VERTEX_FORMAT = VertexFormat.builder().add("Position", VertexFormatElement.POSITION).add("UV0", VertexFormatElement.UV0).build();
    private static final int field_1594 = 36;
    public static final int field_1595 = 16384;
    @NotNull
    private final Object2ObjectMap<BF_266, Collection<BF_1150>> field_1593 = new Object2ObjectOpenHashMap();
    private int field_1596;
    private VertexBuffer field_1590;
    private boolean field_1589 = false;
    @NotNull
    private final Matrix4f field_6495 = new Matrix4f();
    private double field_6497;
    private double field_6492;
    private double field_6493;
    private boolean field_6494 = true;
    @NotNull
    private final Matrix4f field_6496 = new Matrix4f();

    public void method_1113() {
        int n;
        this.field_1589 = true;
        this.field_1590 = new VertexBuffer(VertexBuffer.Usage.STATIC);
        Tesselator tesselator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferBuilder = tesselator.begin(VertexFormat.Mode.QUADS, VERTEX_FORMAT);
        bufferBuilder.addVertex(0.0f, 0.0f, 0.0f).setUv(0.0f, 1.0f);
        bufferBuilder.addVertex(0.0f, 0.0f, 1.0f).setUv(1.0f, 1.0f);
        bufferBuilder.addVertex(0.0f, 1.0f, 1.0f).setUv(1.0f, 0.0f);
        bufferBuilder.addVertex(0.0f, 1.0f, 0.0f).setUv(0.0f, 0.0f);
        this.field_1590.bind();
        this.field_1590.upload(bufferBuilder.buildOrThrow());
        this.field_1596 = GL15C.glGenBuffers();
        int n2 = 2;
        int n3 = 6;
        for (n = 2; n <= 6; ++n) {
            GL20C.glEnableVertexAttribArray((int)n);
        }
        GL15C.glBindBuffer((int)34962, (int)this.field_1596);
        GL20C.glVertexAttribPointer((int)2, (int)3, (int)5126, (boolean)false, (int)36, (long)0L);
        GL20C.glVertexAttribPointer((int)3, (int)1, (int)5126, (boolean)true, (int)36, (long)12L);
        GL20C.glVertexAttribPointer((int)4, (int)4, (int)5121, (boolean)true, (int)36, (long)16L);
        GL20C.glVertexAttribPointer((int)5, (int)3, (int)5126, (boolean)false, (int)36, (long)20L);
        GL20C.glVertexAttribPointer((int)6, (int)2, (int)5122, (boolean)false, (int)36, (long)32L);
        for (n = 2; n <= 6; ++n) {
            GL33C.glVertexAttribDivisor((int)n, (int)1);
        }
        GL15C.glBindBuffer((int)34962, (int)0);
        VertexBuffer.unbind();
    }

    public void tick() {
        for (Collection collection : this.field_1593.values()) {
            collection.removeIf(BF_269::method_1126);
        }
    }

    public void method_1112(@NotNull Minecraft minecraft, @NotNull GameRenderer gameRenderer, @NotNull Camera camera, float f) {
        if (!this.field_1589) {
            this.method_1113();
        }
        if (this.field_1593.isEmpty()) {
            return;
        }
        Vec3 vec3 = camera.getPosition();
        this.field_6497 = vec3.x();
        this.field_6492 = vec3.y();
        this.field_6493 = vec3.z();
        this.method_1114();
        for (Map.Entry entry : this.field_1593.entrySet()) {
            Collection collection = (Collection)entry.getValue();
            if (collection.isEmpty()) continue;
            this.method_1111(minecraft, gameRenderer, (BF_266)entry.getKey(), collection, f);
        }
    }

    private void method_1111(@NotNull Minecraft minecraft, @NotNull GameRenderer gameRenderer, @NotNull BF_266 bF_266, @NotNull Collection<BF_1150> collection, float f) {
        int n = collection.size();
        int n2 = n * 36;
        if (n == 0) {
            return;
        }
        GL15C.glBindBuffer((int)34962, (int)this.field_1596);
        GL15C.glBufferData((int)34962, (long)n2, (int)35040);
        ByteBuffer byteBuffer = GL15C.glMapBuffer((int)34962, (int)35001);
        if (byteBuffer != null) {
            for (BF_1150 bF_1150 : collection) {
                this.method_1109(bF_1150, byteBuffer, f);
            }
            GL15C.glUnmapBuffer((int)34962);
        } else {
            byteBuffer = MemoryUtil.memAlloc((int)n2);
            for (BF_1150 bF_1150 : collection) {
                this.method_1109(bF_1150, byteBuffer, f);
            }
            byteBuffer.rewind();
            GL15C.glBufferSubData((int)34962, (long)0L, (ByteBuffer)byteBuffer);
            MemoryUtil.memFree((Buffer)byteBuffer);
        }
        GL15C.glBindBuffer((int)34962, (int)0);
        bF_266.method_1105(minecraft);
        ShaderInstance shaderInstance = RenderSystem.getShader();
        assert (shaderInstance != null) : "Instanced particle rendering shader is null";
        gameRenderer.lightTexture().turnOnLightLayer();
        shaderInstance.setDefaultUniforms(VertexFormat.Mode.QUADS, (Matrix4f)RenderSystem.getModelViewStack(), RenderSystem.getProjectionMatrix(), minecraft.getWindow());
        shaderInstance.apply();
        this.field_1590.bind();
        GL31.glDrawElementsInstanced((int)this.field_1590.mode.asGLMode, (int)this.field_1590.indexCount, (int)this.field_1590.getIndexType().asGLType, (long)0L, (int)n);
        VertexBuffer.unbind();
        shaderInstance.clear();
        bF_266.method_1106(minecraft);
    }

    private void method_1114() {
        Matrix4fStack matrix4fStack = RenderSystem.getModelViewStack();
        if (!matrix4fStack.equals((Object)this.field_6496)) {
            this.field_6494 = true;
            this.field_6496.set((Matrix4fc)matrix4fStack);
        }
        if (this.field_6494) {
            matrix4fStack.invert(this.field_6495);
            Tesselator tesselator = RenderSystem.renderThreadTesselator();
            BufferBuilder bufferBuilder = tesselator.begin(VertexFormat.Mode.QUADS, VERTEX_FORMAT);
            bufferBuilder.addVertex(this.field_6495, -1.0f, -1.0f, 0.0f).setUv(0.0f, 1.0f);
            bufferBuilder.addVertex(this.field_6495, 1.0f, -1.0f, 0.0f).setUv(1.0f, 1.0f);
            bufferBuilder.addVertex(this.field_6495, 1.0f, 1.0f, 0.0f).setUv(1.0f, 0.0f);
            bufferBuilder.addVertex(this.field_6495, -1.0f, 1.0f, 0.0f).setUv(0.0f, 0.0f);
            this.field_1590.bind();
            this.field_1590.upload(bufferBuilder.buildOrThrow());
            this.field_6494 = false;
        } else {
            this.field_1590.bind();
        }
    }

    private void method_1109(@NotNull BF_1150 bF_1150, @NotNull ByteBuffer byteBuffer, float f) {
        Vector3dc vector3dc = bF_1150.method_1123(f);
        byteBuffer.putFloat((float)(vector3dc.x() - this.field_6497));
        byteBuffer.putFloat((float)(vector3dc.y() - this.field_6492));
        byteBuffer.putFloat((float)(vector3dc.z() - this.field_6493));
        byteBuffer.putFloat(bF_1150.method_1125(f));
        byteBuffer.putInt(bF_1150.method_1121(f));
        TextureAtlasSprite textureAtlasSprite = bF_1150.method_1122(f);
        byteBuffer.putFloat(textureAtlasSprite.getU0());
        byteBuffer.putFloat(textureAtlasSprite.getV0());
        byteBuffer.putFloat(textureAtlasSprite.getV1() - textureAtlasSprite.getV0());
        byteBuffer.putInt(bF_1150.method_1124(f));
    }

    public void method_1108(@NotNull BF_1150 bF_1150) {
        ((Collection)this.field_1593.computeIfAbsent((Object)bF_1150.method_1120(), object -> EvictingQueue.create((int)16384))).add(bF_1150);
    }

    public void clearAll() {
        this.field_1593.clear();
        BFLog.log("Cleared all instanced particles.", new Object[0]);
    }

    public int method_1107() {
        return this.field_1593.values().stream().mapToInt(Collection::size).sum();
    }

    public void method_1116() {
        this.field_1590.close();
        GL15C.glDeleteBuffers((int)this.field_1596);
    }
}

