package dev.tomerdev.mercfrontcore.net.packet;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Uuids;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import dev.tomerdev.mercfrontcore.AddonConstants;
import dev.tomerdev.mercfrontcore.data.AddonCommonData;
import dev.tomerdev.mercfrontcore.data.ProfileOverrideData;

public record SetProfileOverridesPacket(Map<UUID, ProfileOverrideData> overrideData) implements CustomPayload {
    public static final Id<SetProfileOverridesPacket> ID = new Id<>(AddonConstants.id("set_profile_overrides"));
    public static final PacketCodec<ByteBuf, SetProfileOverridesPacket> PACKET_CODEC = PacketCodec.tuple(
        PacketCodecs.map(Object2ObjectOpenHashMap::new, Uuids.PACKET_CODEC, ProfileOverrideData.PACKET_CODEC),
        SetProfileOverridesPacket::overrideData,
        SetProfileOverridesPacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void handleClient(SetProfileOverridesPacket packet, IPayloadContext context) {
        AddonCommonData data = AddonCommonData.getInstance();
        data.getProfileOverrides().clear();
        data.getProfileOverrides().putAll(packet.overrideData());
    }
}
