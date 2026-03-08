/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.client.net.BFClientPacketHandlers;
import com.boehmod.blockfront.common.gun.bullet.BulletTracer;
import com.boehmod.blockfront.util.BFRes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record BFBulletTracerToClientPacket(int entityId, @NotNull BulletTracer bulletTracer) implements CustomPacketPayload
{
    @NotNull
    public static final CustomPacketPayload.Type<BFBulletTracerToClientPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_bullet_tracer_to_client"));
    @NotNull
    public static final StreamCodec<RegistryFriendlyByteBuf, BFBulletTracerToClientPacket> CODEC = CustomPacketPayload.codec(BFBulletTracerToClientPacket::method_4311, BFBulletTracerToClientPacket::new);

    public BFBulletTracerToClientPacket(@NotNull RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        this(registryFriendlyByteBuf.readInt(), BulletTracer.method_1172(registryFriendlyByteBuf));
    }

    public void method_4311(@NotNull RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        registryFriendlyByteBuf.writeInt(this.entityId);
        this.bulletTracer.method_1173(registryFriendlyByteBuf);
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4309(BFBulletTracerToClientPacket bFBulletTracerToClientPacket, @NotNull IPayloadContext iPayloadContext) {
        BFClientPacketHandlers.bulletTracerToClient(bFBulletTracerToClientPacket, iPayloadContext);
    }
}

