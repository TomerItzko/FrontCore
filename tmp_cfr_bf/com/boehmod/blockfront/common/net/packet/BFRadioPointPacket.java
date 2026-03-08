/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.client.net.BFClientPacketHandlers;
import com.boehmod.blockfront.game.GameEventType;
import com.boehmod.blockfront.util.BFRes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record BFRadioPointPacket(@NotNull String icon, int time, @NotNull Vec3 position, @NotNull GameEventType gameEventType) implements CustomPacketPayload
{
    @NotNull
    public static final CustomPacketPayload.Type<BFRadioPointPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_radio_point"));
    @NotNull
    public static final StreamCodec<FriendlyByteBuf, BFRadioPointPacket> CODEC = CustomPacketPayload.codec(BFRadioPointPacket::method_4414, BFRadioPointPacket::new);

    public BFRadioPointPacket(@NotNull FriendlyByteBuf friendlyByteBuf) {
        this(friendlyByteBuf.readUtf(Short.MAX_VALUE), friendlyByteBuf.readInt(), friendlyByteBuf.readVec3(), (GameEventType)friendlyByteBuf.readEnum(GameEventType.class));
    }

    public void method_4414(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeUtf(this.icon);
        friendlyByteBuf.writeInt(this.time);
        friendlyByteBuf.writeVec3(this.position);
        friendlyByteBuf.writeEnum((Enum)this.gameEventType);
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4412(BFRadioPointPacket bFRadioPointPacket, @NotNull IPayloadContext iPayloadContext) {
        BFClientPacketHandlers.radioPoint(bFRadioPointPacket, iPayloadContext);
    }
}

