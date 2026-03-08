/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.player.BFAbstractPlayerData;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.util.BFRes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record BFAimingPacket(boolean aiming) implements CustomPacketPayload
{
    @NotNull
    public static final CustomPacketPayload.Type<BFAimingPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_aiming"));
    @NotNull
    public static final StreamCodec<FriendlyByteBuf, BFAimingPacket> CODEC = CustomPacketPayload.codec(BFAimingPacket::method_4031, BFAimingPacket::new);

    public BFAimingPacket(@NotNull FriendlyByteBuf friendlyByteBuf) {
        this(friendlyByteBuf.readBoolean());
    }

    public void method_4031(@NotNull FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeBoolean(this.aiming);
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4029(BFAimingPacket bFAimingPacket, @NotNull IPayloadContext iPayloadContext) {
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is null!";
        Object obj = bFAbstractManager.getPlayerDataHandler();
        Player player = iPayloadContext.player();
        Object d = ((PlayerDataHandler)obj).getPlayerData(player);
        ((BFAbstractPlayerData)d).method_833(bFAimingPacket.aiming);
    }
}

