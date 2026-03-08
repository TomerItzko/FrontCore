/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.particle;

import com.boehmod.blockfront.unnamed.BF_1146;
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
import org.joml.Vector3dc;
import org.lwjgl.opengl.GL15C;
import org.lwjgl.opengl.GL20C;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL33C;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.NativeResource;

public abstract class InstancedParticleManager<T extends BF_269>
implements NativeResource {
    @NotNull
    protected static final VertexFormat field_6478 = VertexFormat.builder().add("Position", VertexFormatElement.POSITION).add("UV0", VertexFormatElement.UV0).build();
    protected static final int field_6482 = 36;
    public static final int field_6483 = 16384;
    @NotNull
    protected final Object2ObjectMap<BF_266, Collection<T>> instances = new Object2ObjectOpenHashMap();
    protected int field_6484;
    protected VertexBuffer field_6477;
    protected boolean field_6475 = false;
    @NotNull
    protected final Matrix4f field_6479 = new Matrix4f();
    protected double field_6485;
    protected double field_6473;
    protected double field_6474;
    @NotNull
    protected final BF_1146 field_6476;

    protected InstancedParticleManager(@NotNull BF_1146 bF_1146) {
        this.field_6476 = bF_1146;
    }

    public void method_5575() {
        int n;
        this.field_6475 = true;
        this.field_6477 = new VertexBuffer(VertexBuffer.Usage.STATIC);
        Tesselator tesselator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferBuilder = tesselator.begin(VertexFormat.Mode.QUADS, field_6478);
        bufferBuilder.addVertex(0.0f, 0.0f, 0.0f).setUv(0.0f, 1.0f);
        bufferBuilder.addVertex(0.0f, 0.0f, 1.0f).setUv(1.0f, 1.0f);
        bufferBuilder.addVertex(0.0f, 1.0f, 1.0f).setUv(1.0f, 0.0f);
        bufferBuilder.addVertex(0.0f, 1.0f, 0.0f).setUv(0.0f, 0.0f);
        this.field_6477.bind();
        this.field_6477.upload(bufferBuilder.buildOrThrow());
        this.field_6484 = GL15C.glGenBuffers();
        int n2 = 2;
        int n3 = 6;
        for (n = 2; n <= 6; ++n) {
            GL20C.glEnableVertexAttribArray((int)n);
        }
        GL15C.glBindBuffer((int)34962, (int)this.field_6484);
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
        for (Collection collection : this.instances.values()) {
            collection.removeIf(BF_269::method_1126);
        }
    }

    public void method_5574(@NotNull Minecraft minecraft, @NotNull GameRenderer gameRenderer, @NotNull Camera camera, float f) {
        if (!this.field_6475) {
            this.method_5575();
        }
        if (this.instances.isEmpty()) {
            return;
        }
        Vec3 vec3 = camera.getPosition();
        this.field_6485 = vec3.x();
        this.field_6473 = vec3.y();
        this.field_6474 = vec3.z();
        this.method_5572(camera);
        for (Map.Entry entry : this.instances.entrySet()) {
            Collection collection = (Collection)entry.getValue();
            if (collection.isEmpty()) continue;
            this.method_5573(minecraft, gameRenderer, (BF_266)entry.getKey(), collection, f);
        }
    }

    protected void method_5573(@NotNull Minecraft minecraft, @NotNull GameRenderer gameRenderer, @NotNull BF_266 bF_266, @NotNull Collection<T> collection, float f) {
        int n = collection.size();
        int n2 = n * 36;
        if (n == 0) {
            return;
        }
        GL15C.glBindBuffer((int)34962, (int)this.field_6484);
        GL15C.glBufferData((int)34962, (long)n2, (int)35040);
        ByteBuffer byteBuffer = GL15C.glMapBuffer((int)34962, (int)35001);
        if (byteBuffer != null) {
            for (BF_269 bF_269 : collection) {
                this.method_5570(bF_269, byteBuffer, f);
            }
            GL15C.glUnmapBuffer((int)34962);
        } else {
            byteBuffer = MemoryUtil.memAlloc((int)n2);
            for (BF_269 bF_269 : collection) {
                this.method_5570(bF_269, byteBuffer, f);
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
        this.field_6477.bind();
        GL31.glDrawElementsInstanced((int)this.field_6477.mode.asGLMode, (int)this.field_6477.indexCount, (int)this.field_6477.getIndexType().asGLType, (long)0L, (int)n);
        VertexBuffer.unbind();
        shaderInstance.clear();
        bF_266.method_1106(minecraft);
    }

    protected void method_5572(@NotNull Camera camera) {
        boolean bl = this.field_6476.method_5578(camera, this.field_6479);
        if (bl) {
            Tesselator tesselator = RenderSystem.renderThreadTesselator();
            BufferBuilder bufferBuilder = tesselator.begin(VertexFormat.Mode.QUADS, field_6478);
            bufferBuilder.addVertex(this.field_6479, -1.0f, -1.0f, 0.0f).setUv(0.0f, 1.0f);
            bufferBuilder.addVertex(this.field_6479, 1.0f, -1.0f, 0.0f).setUv(1.0f, 1.0f);
            bufferBuilder.addVertex(this.field_6479, 1.0f, 1.0f, 0.0f).setUv(1.0f, 0.0f);
            bufferBuilder.addVertex(this.field_6479, -1.0f, 1.0f, 0.0f).setUv(0.0f, 0.0f);
            this.field_6477.bind();
            this.field_6477.upload(bufferBuilder.buildOrThrow());
        } else {
            this.field_6477.bind();
        }
    }

    protected void method_5570(@NotNull T t, @NotNull ByteBuffer byteBuffer, float f) {
        Vector3dc vector3dc = t.method_1123(f);
        byteBuffer.putFloat((float)(vector3dc.x() - this.field_6485));
        byteBuffer.putFloat((float)(vector3dc.y() - this.field_6473));
        byteBuffer.putFloat((float)(vector3dc.z() - this.field_6474));
        byteBuffer.putFloat(t.method_1125(f));
        byteBuffer.putInt(t.method_1121(f));
        TextureAtlasSprite textureAtlasSprite = t.method_1122(f);
        byteBuffer.putFloat(textureAtlasSprite.getU0());
        byteBuffer.putFloat(textureAtlasSprite.getV0());
        byteBuffer.putFloat(textureAtlasSprite.getV1() - textureAtlasSprite.getV0());
        byteBuffer.putInt(t.method_1124(f));
    }

    public void method_5569(@NotNull T t) {
        ((Collection)this.instances.computeIfAbsent((Object)t.method_1120(), object -> EvictingQueue.create((int)16384))).add(t);
    }

    public void clearAll() {
        this.instances.clear();
        BFLog.log("Cleared all instanced particles.", new Object[0]);
    }

    public int count() {
        return this.instances.values().stream().mapToInt(Collection::size).sum();
    }

    public void method_5577() {
        this.field_6477.close();
        GL15C.glDeleteBuffers((int)this.field_6484);
    }
}

