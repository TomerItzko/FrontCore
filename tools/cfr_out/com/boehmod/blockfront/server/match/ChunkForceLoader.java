/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
 *  net.minecraft.core.BlockPos
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.level.ChunkPos
 *  net.minecraft.world.level.chunk.LevelChunk
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.server.match;

import com.boehmod.blockfront.common.net.packet.BFSequencePositionUpdatedPacket;
import com.boehmod.blockfront.util.PacketUtils;
import com.boehmod.blockfront.util.math.FDSPose;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.NotNull;

public class ChunkForceLoader {
    private static final int RADIUS = 8;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void loadAroundPosition(@NotNull ServerLevel level, @NotNull ServerPlayer player, @NotNull FDSPose pose) {
        ObjectOpenHashSet objectOpenHashSet;
        block11: {
            BlockPos blockPos = pose.asBlockPos();
            ChunkPos chunkPos = new ChunkPos(blockPos);
            boolean bl = false;
            objectOpenHashSet = new ObjectOpenHashSet();
            try {
                for (int i = -8; i <= 8; ++i) {
                    for (int j = -8; j <= 8; ++j) {
                        int n = chunkPos.x + i;
                        int n2 = chunkPos.z + j;
                        ChunkPos chunkPos2 = new ChunkPos(n, n2);
                        boolean bl2 = false;
                        if (!level.hasChunk(n, n2)) {
                            level.setChunkForced(n, n2, true);
                            objectOpenHashSet.add((Object)chunkPos2);
                            bl2 = true;
                        }
                        try {
                            LevelChunk levelChunk = level.getChunk(n, n2);
                            if (levelChunk.isEmpty()) continue;
                            ClientboundLevelChunkWithLightPacket clientboundLevelChunkWithLightPacket = new ClientboundLevelChunkWithLightPacket(levelChunk, level.getLightEngine(), null, null);
                            player.connection.send((Packet)clientboundLevelChunkWithLightPacket);
                            bl = true;
                            continue;
                        }
                        finally {
                            if (bl2) {
                                level.setChunkForced(n, n2, false);
                                objectOpenHashSet.remove((Object)chunkPos2);
                            }
                        }
                    }
                }
                if (!bl) break block11;
                PacketUtils.sendToPlayer(new BFSequencePositionUpdatedPacket(), player);
            }
            catch (Throwable throwable) {
                for (ChunkPos chunkPos3 : objectOpenHashSet) {
                    level.setChunkForced(chunkPos3.x, chunkPos3.z, false);
                }
                throw throwable;
            }
        }
        for (ChunkPos chunkPos : objectOpenHashSet) {
            level.setChunkForced(chunkPos.x, chunkPos.z, false);
        }
    }
}

