/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.cloud.BFFeatureFlags;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.gun.bullet.LivingBulletCollision;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.server.BFServerManager;
import com.boehmod.blockfront.server.ac.bullet.ServerCollisionTracker;
import com.boehmod.blockfront.server.ac.bullet.ShotValidationManager;
import com.boehmod.blockfront.server.player.ServerPlayerDataHandler;
import com.boehmod.blockfront.unnamed.BF_948;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.SerializationUtils;
import java.util.List;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record BFBulletCollisionLivingPacket(@NotNull List<LivingBulletCollision> entityBulletHits, Vec3 position, Vec2 direction, float itemSpread, float shootBackPitch, long shootTick, long randomSeed) implements CustomPacketPayload
{
    @NotNull
    public static final CustomPacketPayload.Type<BFBulletCollisionLivingPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_bullet_collision_living"));
    @NotNull
    public static final StreamCodec<FriendlyByteBuf, BFBulletCollisionLivingPacket> CODEC = CustomPacketPayload.codec(BFBulletCollisionLivingPacket::method_4299, BFBulletCollisionLivingPacket::new);

    public BFBulletCollisionLivingPacket(@NotNull FriendlyByteBuf buf) {
        this((List<LivingBulletCollision>)SerializationUtils.readLivingBulletCollisions(buf), buf.readVec3(), new Vec2(buf.readFloat(), buf.readFloat()), buf.readFloat(), buf.readFloat(), buf.readLong(), buf.readLong());
    }

    public void method_4299(@NotNull FriendlyByteBuf friendlyByteBuf) {
        SerializationUtils.writeLivingBulletCollisions(friendlyByteBuf, this.entityBulletHits);
        friendlyByteBuf.writeVec3(this.position);
        friendlyByteBuf.writeFloat(this.direction.x);
        friendlyByteBuf.writeFloat(this.direction.y);
        friendlyByteBuf.writeFloat(this.itemSpread);
        friendlyByteBuf.writeFloat(this.shootBackPitch);
        friendlyByteBuf.writeLong(this.shootTick);
        friendlyByteBuf.writeLong(this.randomSeed);
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4298(BFBulletCollisionLivingPacket bFBulletCollisionLivingPacket, @NotNull IPayloadContext iPayloadContext) {
        Object object = iPayloadContext.player();
        if (!(object instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer serverPlayer = (ServerPlayer)object;
        object = BlockFront.getInstance().getManager();
        assert (object != null) : "Mod manager is null!";
        AbstractGame<?, ?, ?> abstractGame = ((BFAbstractManager)object).getGameWithPlayer((Player)serverPlayer);
        boolean bl = BFFeatureFlags.serverShotValidation;
        if (object instanceof BFServerManager) {
            BFServerManager bFServerManager = (BFServerManager)object;
            if (bl) {
                boolean bl2;
                long l = bFServerManager.getCollisionTracker().method_5842();
                boolean bl3 = bl2 = bFBulletCollisionLivingPacket.shootTick > l;
                if (!BFBulletCollisionLivingPacket.method_5806(bFBulletCollisionLivingPacket, serverPlayer, bFServerManager, (ServerPlayerDataHandler)bFServerManager.getPlayerDataHandler())) {
                    return;
                }
                if (bl2) {
                    return;
                }
            }
        }
        BF_948.method_3944(object, ((BFAbstractManager)object).getPlayerDataHandler(), serverPlayer.level(), (LivingEntity)serverPlayer, abstractGame, bFBulletCollisionLivingPacket.entityBulletHits);
    }

    private static boolean method_5806(@NotNull BFBulletCollisionLivingPacket bFBulletCollisionLivingPacket, @NotNull ServerPlayer serverPlayer, @NotNull BFServerManager bFServerManager, @NotNull ServerPlayerDataHandler serverPlayerDataHandler) {
        ServerCollisionTracker serverCollisionTracker = bFServerManager.getCollisionTracker();
        ShotValidationManager shotValidationManager = new ShotValidationManager(serverCollisionTracker);
        return shotValidationManager.checkClientAll(bFServerManager, serverPlayerDataHandler, serverPlayer.serverLevel(), serverPlayer, bFBulletCollisionLivingPacket.entityBulletHits, bFBulletCollisionLivingPacket.position, bFBulletCollisionLivingPacket.direction, bFBulletCollisionLivingPacket.itemSpread, bFBulletCollisionLivingPacket.shootBackPitch, bFBulletCollisionLivingPacket.shootTick, bFBulletCollisionLivingPacket.randomSeed);
    }
}

