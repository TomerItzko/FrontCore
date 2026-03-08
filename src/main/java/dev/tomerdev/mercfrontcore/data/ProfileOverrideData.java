package dev.tomerdev.mercfrontcore.data;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public record ProfileOverrideData(String displayName, int level, int prestige) {
    public static final PacketCodec<ByteBuf, ProfileOverrideData> PACKET_CODEC = PacketCodec.tuple(
        PacketCodecs.STRING, ProfileOverrideData::displayName,
        PacketCodecs.VAR_INT, ProfileOverrideData::level,
        PacketCodecs.VAR_INT, ProfileOverrideData::prestige,
        ProfileOverrideData::new
    );
}
