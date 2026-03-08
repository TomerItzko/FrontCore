/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.RegistryFriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.client.net.BFClientPacketHandlers;
import com.boehmod.blockfront.common.match.kill.KillMessage;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.SerializationUtils;
import java.util.UUID;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record BFGameKilledMessagePacket(@NotNull UUID playerUUID, @NotNull ItemStack itemUsed, float userHealth) implements CustomPacketPayload
{
    @NotNull
    public static final CustomPacketPayload.Type<BFGameKilledMessagePacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_game_killed_message"));
    @NotNull
    public static final StreamCodec<RegistryFriendlyByteBuf, BFGameKilledMessagePacket> CODEC = CustomPacketPayload.codec(BFGameKilledMessagePacket::method_4331, BFGameKilledMessagePacket::new);

    public BFGameKilledMessagePacket(@NotNull KillMessage killMessage) {
        this(killMessage.getUUID(), killMessage.getItemUsing(), killMessage.getHealth());
    }

    public BFGameKilledMessagePacket(@NotNull RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        this(registryFriendlyByteBuf.readUUID(), SerializationUtils.readItemStack(registryFriendlyByteBuf), registryFriendlyByteBuf.readFloat());
    }

    public void method_4331(@NotNull RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        registryFriendlyByteBuf.writeUUID(this.playerUUID);
        SerializationUtils.writeItemStack(registryFriendlyByteBuf, this.itemUsed);
        registryFriendlyByteBuf.writeFloat(this.userHealth);
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4330(BFGameKilledMessagePacket bFGameKilledMessagePacket, @NotNull IPayloadContext iPayloadContext) {
        BFClientPacketHandlers.gameKilledMessage(bFGameKilledMessagePacket, iPayloadContext);
    }
}

