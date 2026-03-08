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
import org.joml.Vector3f;

public record BFSpawnCorpsePacket(int playerId, @NotNull Context context) implements CustomPacketPayload
{
    @NotNull
    public static final CustomPacketPayload.Type<BFSpawnCorpsePacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_spawn_corpse"));
    @NotNull
    public static final StreamCodec<FriendlyByteBuf, BFSpawnCorpsePacket> CODEC = CustomPacketPayload.codec(BFSpawnCorpsePacket::method_4448, BFSpawnCorpsePacket::new);

    public BFSpawnCorpsePacket(@NotNull FriendlyByteBuf friendlyByteBuf) {
        this(friendlyByteBuf.readInt(), new Context(friendlyByteBuf));
    }

    public void method_4448(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeInt(this.playerId);
        this.context.method_4452(friendlyByteBuf);
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4446(BFSpawnCorpsePacket bFSpawnCorpsePacket, @NotNull IPayloadContext iPayloadContext) {
        BFClientPacketHandlers.spawnCorpse(bFSpawnCorpsePacket, iPayloadContext);
    }

    public record Context(boolean burned, boolean dismembered, boolean explosion, Vector3f velocity) {
        public Context(@NotNull FriendlyByteBuf friendlyByteBuf) {
            this(friendlyByteBuf.readBoolean(), friendlyByteBuf.readBoolean(), friendlyByteBuf.readBoolean(), friendlyByteBuf.readVector3f());
        }

        public void method_4452(@NotNull FriendlyByteBuf friendlyByteBuf) {
            friendlyByteBuf.writeBoolean(this.burned);
            friendlyByteBuf.writeBoolean(this.dismembered);
            friendlyByteBuf.writeBoolean(this.explosion);
            friendlyByteBuf.writeVector3f(this.velocity);
        }
    }
}

