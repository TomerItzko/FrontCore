package dev.tomerdev.mercfrontcore.net.packet;

import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.Set;
import java.util.UUID;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Uuids;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import dev.tomerdev.mercfrontcore.AddonConstants;
import dev.tomerdev.mercfrontcore.data.AddonCommonData;
import dev.tomerdev.mercfrontcore.data.ProfileOverrideData;

public record SetProfileOverridesPropertyPacket(
    Set<UUID> uuids,
    int propertyId,
    String stringValue,
    int intValue
) implements CustomPayload {
    public static final int PROPERTY_DISPLAY_NAME = 0;
    public static final int PROPERTY_LEVEL = 1;
    public static final int PROPERTY_PRESTIGE = 2;

    public static final Id<SetProfileOverridesPropertyPacket> ID = new Id<>(AddonConstants.id("set_profile_overrides_property"));
    public static final PacketCodec<ByteBuf, SetProfileOverridesPropertyPacket> PACKET_CODEC = PacketCodec.tuple(
        Uuids.PACKET_CODEC.collect(PacketCodecs.toCollection(ObjectOpenHashSet::new)),
        SetProfileOverridesPropertyPacket::uuids,
        PacketCodecs.VAR_INT,
        SetProfileOverridesPropertyPacket::propertyId,
        PacketCodecs.STRING,
        SetProfileOverridesPropertyPacket::stringValue,
        PacketCodecs.VAR_INT,
        SetProfileOverridesPropertyPacket::intValue,
        SetProfileOverridesPropertyPacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void handleClient(SetProfileOverridesPropertyPacket packet, IPayloadContext context) {
        AddonCommonData data = AddonCommonData.getInstance();
        var profileOverrides = data.getProfileOverrides();
        for (UUID uuid : packet.uuids()) {
            ProfileOverrideData current = profileOverrides.getOrDefault(uuid, new ProfileOverrideData("unknown", 0, 0));
            ProfileOverrideData updated = switch (packet.propertyId()) {
                case PROPERTY_DISPLAY_NAME -> new ProfileOverrideData(packet.stringValue(), current.level(), current.prestige());
                case PROPERTY_LEVEL -> new ProfileOverrideData(current.displayName(), packet.intValue(), current.prestige());
                case PROPERTY_PRESTIGE -> new ProfileOverrideData(current.displayName(), current.level(), packet.intValue());
                default -> current;
            };
            profileOverrides.put(uuid, updated);
            data.reapplyProfileOverride(uuid);
        }
    }
}
