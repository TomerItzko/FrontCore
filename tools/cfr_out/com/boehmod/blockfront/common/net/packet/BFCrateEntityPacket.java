/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
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
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.SerializationUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record BFCrateEntityPacket(@NotNull BlockPos blockPos, @NotNull ItemStack itemStack) implements CustomPacketPayload
{
    @NotNull
    public static final CustomPacketPayload.Type<BFCrateEntityPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_crate_entity"));
    @NotNull
    public static final StreamCodec<RegistryFriendlyByteBuf, BFCrateEntityPacket> CODEC = CustomPacketPayload.codec(BFCrateEntityPacket::method_4049, BFCrateEntityPacket::new);

    public BFCrateEntityPacket(@NotNull RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        this(registryFriendlyByteBuf.readBlockPos(), SerializationUtils.readItemStack(registryFriendlyByteBuf));
    }

    public void method_4049(@NotNull RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        registryFriendlyByteBuf.writeBlockPos(this.blockPos);
        SerializationUtils.writeItemStack(registryFriendlyByteBuf, this.itemStack);
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4048(BFCrateEntityPacket bFCrateEntityPacket, @NotNull IPayloadContext iPayloadContext) {
        BFClientPacketHandlers.crateEntity(bFCrateEntityPacket, iPayloadContext);
    }
}

