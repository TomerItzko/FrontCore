/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.client.net.BFClientPacketHandlers;
import com.boehmod.blockfront.game.GameSequence;
import com.boehmod.blockfront.game.GameSequenceSettings;
import com.boehmod.blockfront.util.BFRes;
import java.io.IOException;
import java.util.Optional;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record BFSequencePacket(@Nullable GameSequence sequence, @NotNull Optional<GameSequenceSettings> settings) implements CustomPacketPayload
{
    @NotNull
    public static final CustomPacketPayload.Type<BFSequencePacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_sequence"));
    @NotNull
    public static final StreamCodec<RegistryFriendlyByteBuf, BFSequencePacket> CODEC = CustomPacketPayload.codec(BFSequencePacket::method_4423, BFSequencePacket::new);

    public BFSequencePacket(@NotNull RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        this(GameSequence.method_2182(registryFriendlyByteBuf), BFSequencePacket.method_4418(registryFriendlyByteBuf));
    }

    public void method_4423(@NotNull RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        if (this.sequence == null) {
            return;
        }
        try {
            this.sequence.method_2184(registryFriendlyByteBuf);
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
        try {
            BFSequencePacket.method_4419(registryFriendlyByteBuf, this.settings.orElse(null));
        }
        catch (IOException iOException) {
            throw new RuntimeException(iOException);
        }
    }

    public static Optional<GameSequenceSettings> method_4418(@NotNull RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        if (registryFriendlyByteBuf.readBoolean()) {
            return Optional.of(GameSequenceSettings.readNewWithRegistry(registryFriendlyByteBuf));
        }
        return Optional.empty();
    }

    public static void method_4419(@NotNull RegistryFriendlyByteBuf registryFriendlyByteBuf, @Nullable GameSequenceSettings gameSequenceSettings) throws IOException {
        boolean bl = gameSequenceSettings != null;
        registryFriendlyByteBuf.writeBoolean(bl);
        if (bl) {
            gameSequenceSettings.writeWithRegistry(registryFriendlyByteBuf);
        }
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4420(BFSequencePacket bFSequencePacket, @NotNull IPayloadContext iPayloadContext) {
        BFClientPacketHandlers.sequence(bFSequencePacket, iPayloadContext);
    }
}

