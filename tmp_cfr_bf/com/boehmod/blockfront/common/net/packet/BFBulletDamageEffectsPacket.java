/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.client.net.BFClientPacketHandlers;
import com.boehmod.blockfront.util.BFRes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record BFBulletDamageEffectsPacket() implements CustomPacketPayload
{
    @NotNull
    public static final CustomPacketPayload.Type<BFBulletDamageEffectsPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_bullet_damage_effects"));
    @NotNull
    public static final StreamCodec<FriendlyByteBuf, BFBulletDamageEffectsPacket> CODEC = CustomPacketPayload.codec(BFBulletDamageEffectsPacket::method_4304, BFBulletDamageEffectsPacket::new);

    public BFBulletDamageEffectsPacket(@NotNull FriendlyByteBuf friendlyByteBuf) {
        this();
    }

    public void method_4304(@NotNull FriendlyByteBuf friendlyByteBuf) {
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4303(BFBulletDamageEffectsPacket bFBulletDamageEffectsPacket, @NotNull IPayloadContext iPayloadContext) {
        BFClientPacketHandlers.bulletDamageEffects(bFBulletDamageEffectsPacket, iPayloadContext);
    }
}

