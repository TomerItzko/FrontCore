package dev.tomerdev.mercfrontcore.net.packet;

import dev.tomerdev.mercfrontcore.AddonConstants;
import dev.tomerdev.mercfrontcore.client.data.AddonClientData;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record PlayerGunSkinStatePacket(Map<Identifier, GunSkinState> states) implements CustomPayload {
    public static final Id<PlayerGunSkinStatePacket> ID = new Id<>(AddonConstants.id("player_gun_skin_state"));
    private static final PacketCodec<ByteBuf, Identifier> IDENTIFIER_CODEC =
        PacketCodecs.STRING.xmap(Identifier::of, Identifier::toString);
    private static final PacketCodec<ByteBuf, List<String>> STRING_LIST_CODEC =
        PacketCodecs.STRING.collect(PacketCodecs.toList());
    private static final PacketCodec<ByteBuf, GunSkinState> GUN_SKIN_STATE_CODEC = PacketCodec.tuple(
        PacketCodecs.STRING,
        GunSkinState::selectedSkin,
        STRING_LIST_CODEC,
        GunSkinState::ownedSkins,
        GunSkinState::new
    );
    public static final PacketCodec<ByteBuf, PlayerGunSkinStatePacket> PACKET_CODEC = PacketCodec.tuple(
        PacketCodecs.map(Object2ObjectOpenHashMap::new, IDENTIFIER_CODEC, GUN_SKIN_STATE_CODEC),
        PlayerGunSkinStatePacket::states,
        PlayerGunSkinStatePacket::new
    );

    @Override
    public @NotNull Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void handleClient(PlayerGunSkinStatePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> AddonClientData.getInstance().setOwnedGunSkins(packet.states()));
    }

    public record GunSkinState(String selectedSkin, List<String> ownedSkins) {
    }
}
