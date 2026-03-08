/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.common.ColorReferences
 *  com.mojang.blaze3d.platform.NativeImage
 *  com.mojang.blaze3d.vertex.PoseStack
 *  it.unimi.dsi.fastutil.longs.Long2ObjectMap
 *  it.unimi.dsi.fastutil.longs.Long2ObjectMap$Entry
 *  it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
 *  it.unimi.dsi.fastutil.longs.LongArrayList
 *  it.unimi.dsi.fastutil.longs.LongList
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  it.unimi.dsi.fastutil.objects.ObjectList
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.renderer.texture.AbstractTexture
 *  net.minecraft.client.renderer.texture.DynamicTexture
 *  net.minecraft.client.renderer.texture.TextureManager
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.SectionPos
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.ChunkPos
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.SlabBlock
 *  net.minecraft.world.level.block.SnowLayerBlock
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.material.Fluid
 *  net.minecraft.world.level.material.Fluids
 *  net.minecraft.world.level.material.MapColor$Brightness
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.NotNull
 *  org.joml.Vector3f
 */
package com.boehmod.blockfront.client.render.minimap;

import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.minimap.MinimapWaypoint;
import com.boehmod.blockfront.util.BFLog;
import com.boehmod.blockfront.util.BFRes;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class MinimapRendering {
    public static final int field_199 = 8;
    @NotNull
    private static final ResourceLocation field_210 = BFRes.loc("textures/gui/shadoweffect.png");
    @NotNull
    private static final ResourceLocation WAYPOINT_PLAYER = BFRes.loc("textures/gui/compass/waypoint_pp_player.png");
    private static final int field_200 = 128;
    private static final int field_201 = 16;
    private static final int field_202 = 512;
    private static final int field_203 = 1;
    @NotNull
    private static final LongList QUEUED_CHUNKS = new LongArrayList();
    @NotNull
    private static final Deque<Long> field_198 = new ArrayDeque<Long>();
    private static final int field_204 = 5;
    private static final int field_205 = 40;
    @NotNull
    private static final Long2ObjectMap<ResourceLocation> TERRAIN_TEXTURES = new Long2ObjectOpenHashMap();
    @NotNull
    private static final ObjectList<BlockPos> field_209 = new ObjectArrayList();
    public static final float field_208 = 0.7f;
    private static int rasterizeTimer = 0;
    private static int queueTimer = 0;

    public static void render(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull Font font, @NotNull LocalPlayer player, @NotNull Collection<MinimapWaypoint> waypoints, int x, int y, float zoom, int width, int height) {
        int n = x + width / 2;
        int n2 = y + height / 2;
        Vector3f vector3f = player.position().toVector3f();
        float f = player.getYRot() + 180.0f;
        int n3 = x + width;
        int n4 = y + height;
        poseStack.pushPose();
        BFRendering.rectangle(graphics, x, y, width, height, BFRendering.translucentBlack());
        BFRendering.rectangle(graphics, x + 1, y + 1, width - 2, height - 2, ColorReferences.COLOR_BLACK_SOLID);
        BFRendering.rectangle(graphics, x + 1, y + 1, width - 2, height - 2, 0x11FFFFFF);
        BFRendering.enableScissor(graphics, x + 1, y + 1, width - 2, height - 2);
        MinimapRendering.renderTerrain(poseStack, graphics, n, n2, vector3f.x, vector3f.z, zoom);
        graphics.disableScissor();
        MinimapRendering.renderWaypoints(poseStack, graphics, font, waypoints, x, y, width, height, n, n2, vector3f.x, vector3f.z, zoom, n3, n4);
        BFRendering.centeredTexture(poseStack, graphics, WAYPOINT_PLAYER, n, n2, 6, 6, f);
        poseStack.popPose();
    }

    private static void renderTerrain(PoseStack poseStack, @NotNull GuiGraphics graphics, float x, float y, float playerX, float playerZ, float zoom) {
        float f = zoom * 16.0f;
        float f2 = 0.65f;
        for (Long2ObjectMap.Entry entry : TERRAIN_TEXTURES.long2ObjectEntrySet()) {
            long l = entry.getLongKey();
            ResourceLocation resourceLocation = (ResourceLocation)entry.getValue();
            float f3 = x + ((float)(ChunkPos.getX((long)l) * 16) - playerX) * zoom;
            float f4 = y + ((float)(ChunkPos.getZ((long)l) * 16) - playerZ) * zoom;
            if (f3 < 0.0f || f4 < 0.0f || f3 > x * 2.0f || f4 > y * 2.0f) continue;
            BFRendering.texture(poseStack, graphics, resourceLocation, f3, f4, f, f, 0.65f);
        }
    }

    private static void renderWaypoints(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull Font font, @NotNull Collection<MinimapWaypoint> waypoints, int x, int y, int width, int height, float f, float f2, float f3, float f4, float f5, float f6, float f7) {
        for (MinimapWaypoint minimapWaypoint : waypoints) {
            Vec3 vec3 = minimapWaypoint.method_360();
            float f8 = (float)((double)f + (vec3.x - (double)f3) * (double)f5);
            float f9 = (float)((double)f2 + (vec3.z - (double)f4) * (double)f5);
            BFRendering.enableScissor(graphics, x, y, width, height);
            minimapWaypoint.renderComponent(poseStack, graphics, font, f8, f9);
            graphics.disableScissor();
            if (f8 < (float)x) {
                f8 = x;
            }
            if (f8 > f6 - 5.0f) {
                f8 = f6 - 5.0f;
            }
            if (f9 < (float)y) {
                f9 = y;
            }
            if (f9 > f7 - 5.0f) {
                f9 = f7 - 5.0f;
            }
            minimapWaypoint.renderIcon(poseStack, graphics, f8, f9, 1.0f);
        }
    }

    public static void update(@NotNull Minecraft minecraft, @NotNull LocalPlayer player, @NotNull ClientLevel level) {
        if (minecraft.options.hideGui) {
            MinimapRendering.clearTerrainTextures(minecraft);
        }
        if (rasterizeTimer-- <= 0) {
            rasterizeTimer = 5;
            MinimapRendering.rasterizeQueuedChunk(minecraft, level);
        }
        if (queueTimer-- <= 0) {
            queueTimer = 40;
            MinimapRendering.queueNearbyChunks(player);
        }
    }

    private static void queueNearbyChunks(@NotNull LocalPlayer player) {
        double d = player.getX();
        double d2 = player.getZ();
        for (int i = -8; i < 8; ++i) {
            for (int j = -8; j < 8; ++j) {
                int n = (int)(d + (double)(i * 16));
                int n2 = (int)(d2 + (double)(j * 16));
                long l = ChunkPos.asLong((int)SectionPos.blockToSectionCoord((int)n), (int)SectionPos.blockToSectionCoord((int)n2));
                if (TERRAIN_TEXTURES.containsKey(l)) continue;
                QUEUED_CHUNKS.add(l);
            }
        }
    }

    private static void rasterizeQueuedChunk(@NotNull Minecraft minecraft, @NotNull ClientLevel level) {
        if (!QUEUED_CHUNKS.isEmpty()) {
            try {
                MinimapRendering.method_180(minecraft, level, QUEUED_CHUNKS.getLong(0));
                QUEUED_CHUNKS.removeLong(0);
            }
            catch (IOException iOException) {
                BFLog.logThrowable("Failed to render queued chunk.", iOException, new Object[0]);
            }
        }
    }

    private static void method_180(@NotNull Minecraft minecraft, @NotNull ClientLevel clientLevel, long l) throws IOException {
        int n = ChunkPos.getX((long)l);
        int n2 = ChunkPos.getZ((long)l);
        int n3 = n * 16;
        int n4 = n2 * 16;
        NativeImage nativeImage = new NativeImage(16, 16, false);
        int n5 = clientLevel.getMaxBuildHeight();
        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                int n6 = MinimapRendering.getColumnColor(clientLevel, n3 + i, n4 + j, n5);
                nativeImage.setPixelRGBA(i, j, n6);
            }
        }
        MinimapRendering.addTerrainTexture(minecraft, l, nativeImage);
    }

    private static int getColumnColor(@NotNull ClientLevel level, int x, int z, int maxY) {
        int n;
        field_209.clear();
        int n2 = -1;
        for (n = maxY - 1; n > 0 && field_209.size() < 3; --n) {
            BlockPos blockPos = new BlockPos(x, n, z);
            BlockState blockState = level.getBlockState(blockPos);
            if (!MinimapRendering.method_185(level, blockPos, blockState)) continue;
            field_209.add((Object)blockPos);
            if (n2 != -1) continue;
            n2 = n;
        }
        if (field_209.isEmpty()) {
            return 0;
        }
        n = MinimapRendering.method_181(level);
        if (MinimapRendering.method_184(level, x, z, n2)) {
            int n3 = n >> 16 & 0xFF;
            int n4 = n >> 8 & 0xFF;
            int n5 = n & 0xFF;
            n = 0xFF000000 | (int)((float)n3 * 0.7f) << 16 | (int)((float)n4 * 0.7f) << 8 | (int)((float)n5 * 0.7f);
        }
        return n;
    }

    private static boolean method_184(@NotNull ClientLevel clientLevel, int n, int n2, int n3) {
        int[] nArray = new int[]{1, -1, 0, 0};
        int[] nArray2 = new int[]{0, 0, 1, -1};
        for (int i = 0; i < 4; ++i) {
            int n4 = n + nArray[i];
            int n5 = n2 + nArray2[i];
            int n6 = MinimapRendering.method_182(clientLevel, n4, n5);
            if (Math.abs(n6 - n3) <= 1) continue;
            return true;
        }
        return false;
    }

    private static int method_182(@NotNull ClientLevel clientLevel, int n, int n2) {
        for (int i = clientLevel.getMaxBuildHeight() - 1; i > 0; --i) {
            BlockPos blockPos = new BlockPos(n, i, n2);
            BlockState blockState = clientLevel.getBlockState(blockPos);
            if (!MinimapRendering.method_185(clientLevel, blockPos, blockState)) continue;
            return i;
        }
        return 0;
    }

    private static boolean method_185(@NotNull ClientLevel clientLevel, @NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        Block block = blockState.getBlock();
        return !clientLevel.canSeeSky(blockPos) && (blockState.isSolid() || block instanceof SlabBlock || block instanceof SnowLayerBlock || !blockState.getFluidState().isEmpty());
    }

    private static int method_181(@NotNull ClientLevel clientLevel) {
        int n;
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        boolean bl = false;
        for (BlockPos blockPos : field_209) {
            BlockState blockState = clientLevel.getBlockState(blockPos);
            n = blockState.getMapColor((BlockGetter)clientLevel, blockPos).calculateRGBColor(MapColor.Brightness.HIGH);
            if (blockState.getFluidState().is((Fluid)Fluids.WATER)) {
                bl = true;
            }
            n2 += n >> 16 & 0xFF;
            n3 += n >> 8 & 0xFF;
            n4 += n & 0xFF;
        }
        int n5 = field_209.size();
        if (n5 > 0) {
            int n6 = n2 / n5;
            int n7 = n3 / n5;
            n = n4 / n5;
            int n8 = bl ? 128 : 255;
            return n8 << 24 | n6 << 16 | n7 << 8 | n;
        }
        return 0;
    }

    private static void addTerrainTexture(@NotNull Minecraft minecraft, long chunkPos, @NotNull NativeImage image) {
        ResourceLocation resourceLocation = BFRes.loc("chunk" + chunkPos);
        TextureManager textureManager = minecraft.getTextureManager();
        DynamicTexture dynamicTexture = new DynamicTexture(image);
        textureManager.register(resourceLocation, (AbstractTexture)dynamicTexture);
        TERRAIN_TEXTURES.put(chunkPos, (Object)resourceLocation);
        MinimapRendering.refreshTerrainTextures(minecraft, chunkPos);
    }

    private static void refreshTerrainTextures(@NotNull Minecraft minecraft, long chunkPos) {
        TextureManager textureManager = minecraft.getTextureManager();
        field_198.remove(chunkPos);
        field_198.addFirst(chunkPos);
        while (field_198.size() > 512) {
            long l = field_198.removeLast();
            textureManager.release((ResourceLocation)TERRAIN_TEXTURES.get(l));
            TERRAIN_TEXTURES.remove(l);
        }
    }

    public static void clearTerrainTextures(@NotNull Minecraft minecraft) {
        TextureManager textureManager = minecraft.getTextureManager();
        QUEUED_CHUNKS.clear();
        for (ResourceLocation resourceLocation : TERRAIN_TEXTURES.values()) {
            textureManager.release(resourceLocation);
        }
        TERRAIN_TEXTURES.clear();
    }
}

