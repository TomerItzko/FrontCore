package dev.tomerdev.mercfrontcore.net.packet;

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
import dev.tomerdev.mercfrontcore.AddonConstants;
import dev.tomerdev.mercfrontcore.client.data.AddonClientData;
import dev.tomerdev.mercfrontcore.MercFrontCore;
import dev.tomerdev.mercfrontcore.setup.GunSkinIndex;

public record GunExtraOptionsPacket(Map<Identifier, GunOptions> options) implements CustomPayload {
    public static final Id<GunExtraOptionsPacket> ID = new Id<>(AddonConstants.id("gun_extra_options"));
    private static final PacketCodec<ByteBuf, Identifier> IDENTIFIER_CODEC =
        PacketCodecs.STRING.xmap(Identifier::of, Identifier::toString);
    private static final PacketCodec<ByteBuf, List<String>> STRING_LIST_CODEC =
        PacketCodecs.STRING.collect(PacketCodecs.toList());
    private static final PacketCodec<ByteBuf, GunOptions> GUN_OPTIONS_CODEC = PacketCodec.tuple(
        STRING_LIST_CODEC, GunOptions::magTypes,
        STRING_LIST_CODEC, GunOptions::barrelTypes,
        STRING_LIST_CODEC, GunOptions::skins,
        GunOptions::new
    );
    public static final PacketCodec<ByteBuf, GunExtraOptionsPacket> PACKET_CODEC = PacketCodec.tuple(
        PacketCodecs.map(Object2ObjectOpenHashMap::new, IDENTIFIER_CODEC, GUN_OPTIONS_CODEC),
        GunExtraOptionsPacket::options,
        GunExtraOptionsPacket::new
    );

    @Override
    public @NotNull Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void handleClient(GunExtraOptionsPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            AddonClientData.getInstance().setGunExtraOptions(packet.options());
            GunSkinIndex.ensureInitialized();
            int guns = packet.options().size();
            int withMagVariants = 0;
            int withBarrelVariants = 0;
            int withSkins = 0;
            for (GunOptions options : packet.options().values()) {
                if (options.magTypes().size() > 1) {
                    withMagVariants++;
                }
                if (options.barrelTypes().size() > 1) {
                    withBarrelVariants++;
                }
                if (!options.skins().isEmpty()) {
                    withSkins++;
                }
            }
            MercFrontCore.LOGGER.info(
                "Received gun option sync: guns={}, magVariants={}, barrelVariants={}, skins={}",
                guns,
                withMagVariants,
                withBarrelVariants,
                withSkins
            );
        });
    }

    public record GunOptions(List<String> magTypes, List<String> barrelTypes, List<String> skins) {
    }
}
