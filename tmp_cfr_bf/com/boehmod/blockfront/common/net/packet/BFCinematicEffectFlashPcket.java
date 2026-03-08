/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.client.net.BFClientPacketHandlers;
import com.boehmod.blockfront.util.BFRes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record BFCinematicEffectFlashPcket(int color, float speed, float speedFlipped, float targetAlpha) implements CustomPacketPayload
{
    @NotNull
    public static final CustomPacketPayload.Type<BFCinematicEffectFlashPcket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_cinematic_effect_flash"));
    @NotNull
    public static final StreamCodec<FriendlyByteBuf, BFCinematicEffectFlashPcket> CODEC = CustomPacketPayload.codec(BFCinematicEffectFlashPcket::method_5733, BFCinematicEffectFlashPcket::new);

    public BFCinematicEffectFlashPcket(@NotNull FriendlyByteBuf friendlyByteBuf) {
        this(friendlyByteBuf.readInt(), friendlyByteBuf.readFloat(), friendlyByteBuf.readFloat(), friendlyByteBuf.readFloat());
    }

    public void method_5733(@NotNull FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeInt(this.color);
        friendlyByteBuf.writeFloat(this.speed);
        friendlyByteBuf.writeFloat(this.speedFlipped);
        friendlyByteBuf.writeFloat(this.targetAlpha);
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_5731(BFCinematicEffectFlashPcket bFCinematicEffectFlashPcket, @NotNull IPayloadContext iPayloadContext) {
        BFClientPacketHandlers.cinematicEffectFlash(bFCinematicEffectFlashPcket, iPayloadContext);
    }
}

