package dev.tomerdev.mercfrontcore.net.packet;

import com.boehmod.blockfront.assets.AssetStore;
import com.boehmod.blockfront.assets.impl.MapAsset;
import com.boehmod.blockfront.map.MapEnvironment;
import com.boehmod.blockfront.map.effect.AbstractMapEffect;
import com.boehmod.blockfront.map.effect.PositionedMapEffect;
import io.netty.buffer.ByteBuf;
import java.util.Map;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.Vec3d;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import dev.tomerdev.mercfrontcore.AddonConstants;
import dev.tomerdev.mercfrontcore.data.AddonPacketCodecs;

public record MapEffectPositionPacket(String map, String environment, int index, Vec3d position) implements CustomPayload {
    public static final Id<MapEffectPositionPacket> ID = new Id<>(AddonConstants.id("map_effect_position"));
    public static final PacketCodec<ByteBuf, MapEffectPositionPacket> PACKET_CODEC = PacketCodec.tuple(
        PacketCodecs.STRING, MapEffectPositionPacket::map,
        PacketCodecs.STRING, MapEffectPositionPacket::environment,
        PacketCodecs.VAR_INT, MapEffectPositionPacket::index,
        AddonPacketCodecs.VEC3D, MapEffectPositionPacket::position,
        MapEffectPositionPacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    @SuppressWarnings("unchecked")
    public static void handleServer(MapEffectPositionPacket packet, IPayloadContext context) {
        if (!context.player().hasPermissionLevel(3)) {
            return;
        }
        context.enqueueWork(() -> {
            try {
                MapAsset mapAsset = AssetStore.getInstance().getRegistry(MapAsset.class).getByName(packet.map);
                if (mapAsset == null) {
                    return;
                }
                Object envObj = mapAsset.getClass().getMethod("getEnvironments").invoke(mapAsset);
                if (!(envObj instanceof Map<?, ?> envs)) {
                    return;
                }
                Object selected = envs.get(packet.environment);
                if (!(selected instanceof MapEnvironment env)) {
                    return;
                }
                if (packet.index < 0 || packet.index >= env.getMapEffects().size()) {
                    return;
                }
                AbstractMapEffect mapEffect = env.getMapEffects().get(packet.index);
                if (mapEffect instanceof PositionedMapEffect positioned) {
                    positioned.position = packet.position;
                }
            } catch (Throwable ignored) {
            }
        });
    }
}
