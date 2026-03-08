/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.event;

import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.cloud.packet.common.mm.PacketMMSearchHostCanceled;
import com.boehmod.bflib.cloud.packet.common.party.PacketPartyLeave;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.cloud.client.ClientConnectionManager;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.util.BFLog;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid="bf", value={Dist.CLIENT})
public final class BFLoggingOutSubscriber {
    @SubscribeEvent
    public static void onLoggingOut(@NotNull ClientPlayerNetworkEvent.LoggingOut event) {
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        Minecraft minecraft = Minecraft.getInstance();
        ClientPlayerDataHandler clientPlayerDataHandler = (ClientPlayerDataHandler)bFClientManager.getPlayerDataHandler();
        PlayerCloudData playerCloudData = clientPlayerDataHandler.getCloudData(minecraft);
        Optional optional = playerCloudData.getParty();
        optional.ifPresent(matchParty -> {
            UUID uUID = Minecraft.getInstance().getUser().getProfileId();
            if (matchParty.isHost(uUID)) {
                BFLog.log("Sending leave/cancel packet...", new Object[0]);
                ((ClientConnectionManager)bFClientManager.getConnectionManager()).sendPacket((IPacket)(matchParty.isSearching() ? new PacketMMSearchHostCanceled() : new PacketPartyLeave()));
            }
        });
    }
}

