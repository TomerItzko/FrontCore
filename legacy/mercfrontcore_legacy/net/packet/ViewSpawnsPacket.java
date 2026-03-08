package red.vuis.mercfrontcore.net.packet;

import java.util.List;

import com.boehmod.blockfront.util.math.FDSPose;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.text.Text;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import red.vuis.mercfrontcore.AddonConstants;
import red.vuis.mercfrontcore.client.data.AddonClientData;
import red.vuis.mercfrontcore.data.AddonPacketCodecs;

public record ViewSpawnsPacket(String gameName, List<FDSPose> spawns) implements CustomPayload {
	public static final Id<ViewSpawnsPacket> ID = new Id<>(AddonConstants.id("view_spawns"));
	public static final PacketCodec<ByteBuf, ViewSpawnsPacket> PACKET_CODEC = PacketCodec.tuple(
		PacketCodecs.STRING, ViewSpawnsPacket::gameName,
		AddonPacketCodecs.FDS_POSE.collect(PacketCodecs.toList()), ViewSpawnsPacket::spawns,
		ViewSpawnsPacket::new
	);
	
	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
	
	public static void handleClient(ViewSpawnsPacket packet, IPayloadContext context) {
		AddonClientData.getInstance().spawnView = packet.spawns();
		MinecraftClient.getInstance().getMessageHandler().onGameMessage(Text.translatable("mercfrontcore.message.packet.viewSpawns.success", packet.gameName()), false);
	}
}
