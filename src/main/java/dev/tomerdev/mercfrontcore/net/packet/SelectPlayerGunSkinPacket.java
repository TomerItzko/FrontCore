package dev.tomerdev.mercfrontcore.net.packet;

import dev.tomerdev.mercfrontcore.AddonConstants;
import dev.tomerdev.mercfrontcore.data.PlayerGunSkinStore;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record SelectPlayerGunSkinPacket(Identifier gunId, String skin) implements CustomPayload {
    public static final Id<SelectPlayerGunSkinPacket> ID = new Id<>(AddonConstants.id("select_player_gun_skin"));
    private static final PacketCodec<ByteBuf, Identifier> IDENTIFIER_CODEC =
        PacketCodecs.STRING.xmap(Identifier::of, Identifier::toString);
    public static final PacketCodec<ByteBuf, SelectPlayerGunSkinPacket> PACKET_CODEC = PacketCodec.tuple(
        IDENTIFIER_CODEC,
        SelectPlayerGunSkinPacket::gunId,
        PacketCodecs.STRING,
        SelectPlayerGunSkinPacket::skin,
        SelectPlayerGunSkinPacket::new
    );

    @Override
    public @NotNull Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void handleServer(SelectPlayerGunSkinPacket packet, IPayloadContext context) {
        if (!(context.player() instanceof ServerPlayerEntity player)) {
            return;
        }
        PlayerGunSkinStore store = PlayerGunSkinStore.getInstance();
        PlayerGunSkinStore.OwnedGunSkins previous = store.getPlayerSkins(player.getUuid()).get(packet.gunId().toString());
        if (!store.selectPlayerSkin(player.getUuid(), packet.gunId().toString(), packet.skin())) {
            return;
        }
        if (!store.save(player.getServer())) {
            return;
        }
        PlayerGunSkinStore.OwnedGunSkins current = store.getPlayerSkins(player.getUuid()).get(packet.gunId().toString());
        store.reconcilePlayerGun(player, packet.gunId().toString(), previous, current);
        PacketDistributor.sendToPlayer(player, store.toPacket(player.getUuid()));
    }
}
