/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.client.net.BFClientPacketHandlers;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.math.FDSPose;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record BFFrozenPacket(@Nullable FDSPose blockLocation) implements CustomPacketPayload
{
    @NotNull
    public static final CustomPacketPayload.Type<BFFrozenPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_frozen"));
    @NotNull
    public static final StreamCodec<RegistryFriendlyByteBuf, BFFrozenPacket> CODEC = CustomPacketPayload.codec(BFFrozenPacket::method_4378, BFFrozenPacket::new);

    public BFFrozenPacket(@NotNull FriendlyByteBuf friendlyByteBuf) {
        this(friendlyByteBuf.readBoolean() ? FDSPose.readBuf(friendlyByteBuf) : null);
    }

    public void method_4378(@NotNull FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeBoolean(this.blockLocation != null);
        if (this.blockLocation != null) {
            this.blockLocation.writeBuf(friendlyByteBuf);
        }
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4377(BFFrozenPacket bFFrozenPacket, @NotNull IPayloadContext iPayloadContext) {
        BFClientPacketHandlers.frozen(bFFrozenPacket, iPayloadContext);
    }
}

