/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 *  org.jetbrains.annotations.NotNull
 *  org.joml.Vector3f
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.client.net.BFClientPacketHandlers;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.math.ShakeNodeData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public record BFPositionedShakeNodePacket(@NotNull ShakeNodeData data, @NotNull Vector3f position, float radius) implements CustomPacketPayload
{
    @NotNull
    public static final CustomPacketPayload.Type<BFPositionedShakeNodePacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_positioned_shake_node"));
    @NotNull
    public static final StreamCodec<FriendlyByteBuf, BFPositionedShakeNodePacket> CODEC = CustomPacketPayload.codec(BFPositionedShakeNodePacket::method_4402, BFPositionedShakeNodePacket::new);

    public BFPositionedShakeNodePacket(@NotNull FriendlyByteBuf friendlyByteBuf) {
        this(ShakeNodeData.readBuf(friendlyByteBuf), friendlyByteBuf.readVector3f(), friendlyByteBuf.readFloat());
    }

    public void method_4402(FriendlyByteBuf friendlyByteBuf) {
        this.data.writeBuf(friendlyByteBuf);
        friendlyByteBuf.writeVector3f(this.position);
        friendlyByteBuf.writeFloat(this.radius);
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4399(BFPositionedShakeNodePacket bFPositionedShakeNodePacket, @NotNull IPayloadContext iPayloadContext) {
        BFPositionedShakeNodePacket.method_4400(bFPositionedShakeNodePacket, iPayloadContext);
    }

    @OnlyIn(value=Dist.CLIENT)
    public static void method_4400(BFPositionedShakeNodePacket bFPositionedShakeNodePacket, @NotNull IPayloadContext iPayloadContext) {
        BFClientPacketHandlers.positionedShakeNode(bFPositionedShakeNodePacket, iPayloadContext);
    }
}

