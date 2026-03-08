/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.gun.bullet.BlockBulletCollision;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.unnamed.BF_948;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.EnvironmentUtils;
import com.boehmod.blockfront.util.SerializationUtils;
import java.util.List;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record BFBulletCollisionBlockPacket(@NotNull List<BlockBulletCollision> blockBulletHits) implements CustomPacketPayload
{
    @NotNull
    public static final CustomPacketPayload.Type<BFBulletCollisionBlockPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_bullet_collision_block"));
    @NotNull
    public static final StreamCodec<FriendlyByteBuf, BFBulletCollisionBlockPacket> CODEC = CustomPacketPayload.codec(BFBulletCollisionBlockPacket::method_4292, BFBulletCollisionBlockPacket::new);

    public BFBulletCollisionBlockPacket(@NotNull FriendlyByteBuf friendlyByteBuf) {
        this((List<BlockBulletCollision>)SerializationUtils.readBlockBulletCollisions(friendlyByteBuf));
    }

    public void method_4292(@NotNull FriendlyByteBuf friendlyByteBuf) {
        SerializationUtils.writeBlockBulletCollisions(friendlyByteBuf, this.blockBulletHits);
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4291(BFBulletCollisionBlockPacket bFBulletCollisionBlockPacket, @NotNull IPayloadContext iPayloadContext) {
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is null!";
        MinecraftServer minecraftServer = EnvironmentUtils.getServer();
        assert (minecraftServer != null) : "Minecraft server is null!";
        ServerLevel serverLevel = minecraftServer.getLevel(Level.OVERWORLD);
        assert (serverLevel != null) : "Server level is null!";
        Player player = iPayloadContext.player();
        AbstractGame<?, ?, ?> abstractGame = bFAbstractManager.getGameWithPlayer(player.getUUID());
        BF_948.method_3948(serverLevel, (LivingEntity)player, abstractGame, bFBulletCollisionBlockPacket.blockBulletHits);
    }
}

