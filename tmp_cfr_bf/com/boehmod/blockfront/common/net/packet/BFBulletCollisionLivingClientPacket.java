/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.client.net.BFClientPacketHandlers;
import com.boehmod.blockfront.common.gun.bullet.LivingBulletCollision;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.SerializationUtils;
import java.util.List;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record BFBulletCollisionLivingClientPacket(@NotNull List<LivingBulletCollision> entityBulletHit) implements CustomPacketPayload
{
    @NotNull
    public static final CustomPacketPayload.Type<BFBulletCollisionLivingClientPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_bullet_collision_living_client"));
    @NotNull
    public static final StreamCodec<FriendlyByteBuf, BFBulletCollisionLivingClientPacket> CODEC = CustomPacketPayload.codec(BFBulletCollisionLivingClientPacket::method_4302, BFBulletCollisionLivingClientPacket::new);

    public BFBulletCollisionLivingClientPacket(@NotNull FriendlyByteBuf friendlyByteBuf) {
        this((List<LivingBulletCollision>)SerializationUtils.readLivingBulletCollisions(friendlyByteBuf));
    }

    public void method_4302(@NotNull FriendlyByteBuf friendlyByteBuf) {
        SerializationUtils.writeLivingBulletCollisions(friendlyByteBuf, this.entityBulletHit);
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4301(BFBulletCollisionLivingClientPacket bFBulletCollisionLivingClientPacket, @NotNull IPayloadContext iPayloadContext) {
        BFClientPacketHandlers.bulletCollisionLivingClient(bFBulletCollisionLivingClientPacket, iPayloadContext);
    }
}

