/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.client.net.BFClientPacketHandlers;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.SerializationUtils;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record BFEntitySoundPacket(@NotNull Holder<SoundEvent> soundEvent, SoundSource soundSource, int livingEntityID, float pitch, float volume) implements CustomPacketPayload
{
    @NotNull
    public static final CustomPacketPayload.Type<BFEntitySoundPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_entity_sound"));
    @NotNull
    public static final StreamCodec<RegistryFriendlyByteBuf, BFEntitySoundPacket> CODEC = CustomPacketPayload.codec(BFEntitySoundPacket::method_4071, BFEntitySoundPacket::new);

    public BFEntitySoundPacket(@NotNull Holder<SoundEvent> holder, @NotNull SoundSource soundSource, int n, float f) {
        this(holder, soundSource, n, f, 2.0f);
    }

    public BFEntitySoundPacket(@NotNull RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        this(SerializationUtils.readSoundEvent(registryFriendlyByteBuf), (SoundSource)registryFriendlyByteBuf.readEnum(SoundSource.class), registryFriendlyByteBuf.readInt(), registryFriendlyByteBuf.readFloat(), registryFriendlyByteBuf.readFloat());
    }

    public void method_4071(@NotNull RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        SerializationUtils.writeSoundEvent(registryFriendlyByteBuf, this.soundEvent);
        registryFriendlyByteBuf.writeEnum((Enum)this.soundSource);
        registryFriendlyByteBuf.writeInt(this.livingEntityID);
        registryFriendlyByteBuf.writeFloat(this.pitch);
        registryFriendlyByteBuf.writeFloat(this.volume);
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4069(BFEntitySoundPacket bFEntitySoundPacket, @NotNull IPayloadContext iPayloadContext) {
        BFClientPacketHandlers.entitySound(bFEntitySoundPacket, iPayloadContext);
    }
}

