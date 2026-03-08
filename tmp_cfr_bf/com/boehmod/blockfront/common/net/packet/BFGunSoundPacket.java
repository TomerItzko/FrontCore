/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.client.net.BFClientPacketHandlers;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.SerializationUtils;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record BFGunSoundPacket(@NotNull ItemStack itemStack, @NotNull Vec3 position, int entityID, boolean lastRound, boolean force, boolean decoy) implements CustomPacketPayload
{
    @NotNull
    public static final CustomPacketPayload.Type<BFGunSoundPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_gun_sound"));
    @NotNull
    public static final StreamCodec<RegistryFriendlyByteBuf, BFGunSoundPacket> CODEC = CustomPacketPayload.codec(BFGunSoundPacket::method_4365, BFGunSoundPacket::new);

    public BFGunSoundPacket(@NotNull ItemStack itemStack, @NotNull Vec3 vec3, int n, boolean bl, boolean bl2, boolean bl3) {
        this.itemStack = itemStack.copy();
        this.position = vec3;
        this.entityID = n;
        this.lastRound = bl;
        this.force = bl2;
        this.decoy = bl3;
    }

    public BFGunSoundPacket(@NotNull ItemStack itemStack, @NotNull Vec3 vec3, int n, boolean bl, boolean bl2) {
        this(itemStack, vec3, n, bl, bl2, false);
    }

    public BFGunSoundPacket(@NotNull ItemStack itemStack, @NotNull Vec3 vec3, int n, boolean bl) {
        this(itemStack, vec3, n, bl, false, false);
    }

    public BFGunSoundPacket(@NotNull RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        this(SerializationUtils.readItemStack(registryFriendlyByteBuf), registryFriendlyByteBuf.readVec3(), registryFriendlyByteBuf.readInt(), registryFriendlyByteBuf.readBoolean(), registryFriendlyByteBuf.readBoolean(), registryFriendlyByteBuf.readBoolean());
    }

    public void method_4365(@NotNull RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        SerializationUtils.writeItemStack(registryFriendlyByteBuf, this.itemStack);
        registryFriendlyByteBuf.writeVec3(this.position);
        registryFriendlyByteBuf.writeInt(this.entityID);
        registryFriendlyByteBuf.writeBoolean(this.lastRound);
        registryFriendlyByteBuf.writeBoolean(this.force);
        registryFriendlyByteBuf.writeBoolean(this.decoy);
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4360(BFGunSoundPacket bFGunSoundPacket, @NotNull IPayloadContext iPayloadContext) {
        BFClientPacketHandlers.gunSound(bFGunSoundPacket, iPayloadContext);
    }
}

