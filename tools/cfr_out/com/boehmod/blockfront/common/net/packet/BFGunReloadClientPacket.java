/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.client.net.BFClientPacketHandlers;
import com.boehmod.blockfront.util.BFRes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public final class BFGunReloadClientPacket
implements CustomPacketPayload {
    public boolean field_6781;
    @NotNull
    public static final CustomPacketPayload.Type<BFGunReloadClientPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_gun_reload_client"));
    @NotNull
    public static final StreamCodec<FriendlyByteBuf, BFGunReloadClientPacket> CODEC = CustomPacketPayload.codec(BFGunReloadClientPacket::method_4359, BFGunReloadClientPacket::new);

    public BFGunReloadClientPacket() {
    }

    public BFGunReloadClientPacket(boolean bl) {
        this.field_6781 = bl;
    }

    public BFGunReloadClientPacket(@NotNull FriendlyByteBuf friendlyByteBuf) {
        this.field_6781 = friendlyByteBuf.readBoolean();
    }

    public void method_4359(@NotNull FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeBoolean(this.field_6781);
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4358(BFGunReloadClientPacket bFGunReloadClientPacket, @NotNull IPayloadContext iPayloadContext) {
        BFClientPacketHandlers.gunReloadClient(bFGunReloadClientPacket, iPayloadContext);
    }
}

