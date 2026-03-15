package dev.tomerdev.mercfrontcore.net.packet;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.game.AbstractGame;
import dev.tomerdev.mercfrontcore.AddonConstants;
import dev.tomerdev.mercfrontcore.config.MercFrontCoreConfig;
import dev.tomerdev.mercfrontcore.config.MercFrontCoreConfigManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record LeaveMatchTeleportPacket() implements CustomPayload {
    public static final Id<LeaveMatchTeleportPacket> ID = new Id<>(AddonConstants.id("leave_match_teleport"));
    public static final PacketCodec<ByteBuf, LeaveMatchTeleportPacket> PACKET_CODEC =
        PacketCodec.unit(new LeaveMatchTeleportPacket());

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void handleServer(LeaveMatchTeleportPacket packet, IPayloadContext context) {
        if (!(context.player() instanceof ServerPlayerEntity player)) {
            return;
        }

        BFAbstractManager<?, ?, ?> manager = resolveManager();
        if (manager != null) {
            AbstractGame<?, ?, ?> game = manager.getGameWithPlayer(player.getUuid());
            if (game != null) {
                game.removePlayer(manager, player.getServerWorld(), player);
            }
        }

        MercFrontCoreConfig.LeaveMatchSettings settings = MercFrontCoreConfigManager.get().leaveMatch;
        player.teleport(player.getServerWorld(), settings.x, settings.y, settings.z, settings.yaw, settings.pitch);
        player.sendMessage(Text.literal("Returned to lobby."), false);
    }

    private static BFAbstractManager<?, ?, ?> resolveManager() {
        try {
            BlockFront blockFront = BlockFront.getInstance();
            return blockFront == null ? null : blockFront.getManager();
        } catch (Throwable ignored) {
            return null;
        }
    }
}
