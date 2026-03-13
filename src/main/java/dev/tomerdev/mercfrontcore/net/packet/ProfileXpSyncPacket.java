package dev.tomerdev.mercfrontcore.net.packet;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.common.match.MatchClass;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Uuids;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import dev.tomerdev.mercfrontcore.AddonConstants;

public record ProfileXpSyncPacket(UUID playerUuid, int exp, int prestige, List<Integer> classExp) implements CustomPayload {
    public static final Id<ProfileXpSyncPacket> ID = new Id<>(AddonConstants.id("profile_xp_sync"));
    private static final PacketCodec<ByteBuf, List<Integer>> CLASS_EXP_CODEC =
        PacketCodecs.collection(ObjectArrayList::new, PacketCodecs.VAR_INT);
    public static final PacketCodec<ByteBuf, ProfileXpSyncPacket> PACKET_CODEC = PacketCodec.tuple(
        Uuids.PACKET_CODEC,
        ProfileXpSyncPacket::playerUuid,
        PacketCodecs.VAR_INT,
        ProfileXpSyncPacket::exp,
        PacketCodecs.VAR_INT,
        ProfileXpSyncPacket::prestige,
        CLASS_EXP_CODEC,
        ProfileXpSyncPacket::classExp,
        ProfileXpSyncPacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void handleClient(ProfileXpSyncPacket packet, IPayloadContext context) {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        BFClientManager manager = BFClientManager.getInstance();
        if (minecraft == null || manager == null) {
            return;
        }
        if (!(manager.getPlayerDataHandler() instanceof ClientPlayerDataHandler dataHandler)) {
            return;
        }

        PlayerCloudData selfProfile = dataHandler.getCloudData(minecraft);
        if (selfProfile.getUUID().equals(packet.playerUuid())) {
            selfProfile.setExp(packet.exp());
            selfProfile.setPrestigeLevel(packet.prestige());
            applyClassExp(selfProfile, packet.classExp());
        }

        PlayerCloudData cachedProfile = dataHandler.getCloudProfile(packet.playerUuid());
        cachedProfile.setExp(packet.exp());
        cachedProfile.setPrestigeLevel(packet.prestige());
        applyClassExp(cachedProfile, packet.classExp());
    }

    private static void applyClassExp(PlayerCloudData profile, List<Integer> classExp) {
        int length = Math.min(MatchClass.values().length, classExp.size());
        for (int i = 0; i < length; i++) {
            profile.setClassExp(i, Math.max(0, classExp.get(i)));
        }
        for (int i = length; i < MatchClass.values().length; i++) {
            profile.setClassExp(i, 0);
        }
    }
}
