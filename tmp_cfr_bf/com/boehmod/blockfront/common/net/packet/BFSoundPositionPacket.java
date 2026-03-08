/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.client.net.BFClientPacketHandlers;
import com.boehmod.blockfront.util.BFRes;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record BFSoundPositionPacket(@NotNull Holder<SoundEvent> sound, @NotNull SoundSource soundSource, float volume, float pitch, @NotNull Vec3 position) implements CustomPacketPayload
{
    @NotNull
    public static final CustomPacketPayload.Type<BFSoundPositionPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_sound_position"));
    @NotNull
    public static final StreamCodec<RegistryFriendlyByteBuf, BFSoundPositionPacket> CODEC = CustomPacketPayload.codec(BFSoundPositionPacket::method_4443, BFSoundPositionPacket::new);

    public BFSoundPositionPacket(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        this((Holder<SoundEvent>)((Holder)SoundEvent.STREAM_CODEC.decode((Object)registryFriendlyByteBuf)), (SoundSource)registryFriendlyByteBuf.readEnum(SoundSource.class), registryFriendlyByteBuf.readFloat(), registryFriendlyByteBuf.readFloat(), registryFriendlyByteBuf.readVec3());
    }

    public BFSoundPositionPacket(Holder<SoundEvent> holder, SoundSource soundSource, float f, Vec3 vec3) {
        this(holder, soundSource, f, 1.0f, vec3);
    }

    public void method_4443(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        SoundEvent.STREAM_CODEC.encode((Object)registryFriendlyByteBuf, this.sound);
        registryFriendlyByteBuf.writeEnum((Enum)this.soundSource);
        registryFriendlyByteBuf.writeFloat(this.volume);
        registryFriendlyByteBuf.writeFloat(this.pitch);
        registryFriendlyByteBuf.writeVec3(this.position);
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4441(BFSoundPositionPacket bFSoundPositionPacket, @NotNull IPayloadContext iPayloadContext) {
        BFClientPacketHandlers.soundPosition(bFSoundPositionPacket, iPayloadContext);
    }
}

