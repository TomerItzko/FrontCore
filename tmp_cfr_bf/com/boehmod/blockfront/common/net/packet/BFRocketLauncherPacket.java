/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.gun.GunTriggerSpawnType;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.util.BFRes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public final class BFRocketLauncherPacket
implements CustomPacketPayload {
    @NotNull
    public static final CustomPacketPayload.Type<BFRocketLauncherPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_rocket_launcher"));
    @NotNull
    public static final StreamCodec<FriendlyByteBuf, BFRocketLauncherPacket> CODEC = CustomPacketPayload.codec(BFRocketLauncherPacket::method_4417, BFRocketLauncherPacket::new);

    public BFRocketLauncherPacket() {
    }

    public BFRocketLauncherPacket(@NotNull FriendlyByteBuf friendlyByteBuf) {
    }

    public void method_4417(@NotNull FriendlyByteBuf friendlyByteBuf) {
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4416(BFRocketLauncherPacket bFRocketLauncherPacket, @NotNull IPayloadContext iPayloadContext) {
        Object object;
        Player player = iPayloadContext.player();
        ItemStack itemStack = player.getMainHandItem();
        if (!itemStack.isEmpty() && (object = itemStack.getItem()) instanceof GunItem) {
            GunItem gunItem = (GunItem)object;
            object = BlockFront.getInstance().getManager();
            assert (object != null) : "Mod manager is null";
            Object h = ((BFAbstractManager)object).getPlayerDataHandler();
            if (gunItem.getDefaultFireConfig().method_4023() == GunTriggerSpawnType.ENTITY) {
                gunItem.method_4128((PlayerDataHandler<?>)h, player.level(), player);
            }
        }
    }
}

