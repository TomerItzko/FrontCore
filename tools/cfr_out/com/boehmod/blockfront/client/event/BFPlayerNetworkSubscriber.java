/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.channel.ChannelHandler
 *  io.netty.channel.ChannelPipeline
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent$LoggingIn
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.event;

import com.boehmod.blockfront.client.net.PacketListenerCustomPayloadPacket;
import com.boehmod.blockfront.client.net.PacketListenerSetChunkCacheCenterPacket;
import com.boehmod.blockfront.client.net.PacketListenerSetTimePacket;
import com.boehmod.blockfront.client.net.PacketListenerSystemChatPacket;
import com.boehmod.blockfront.util.BFLog;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid="bf", value={Dist.CLIENT})
public class BFPlayerNetworkSubscriber {
    @SubscribeEvent
    public static void onLoggingIn(@NotNull ClientPlayerNetworkEvent.LoggingIn event) {
        BFLog.log("Adding custom packet listeners for new connection...", new Object[0]);
        ChannelPipeline channelPipeline = event.getConnection().channel().pipeline();
        channelPipeline.addBefore("packet_handler", "mod_packet_handler_time", (ChannelHandler)new PacketListenerSetTimePacket());
        channelPipeline.addBefore("packet_handler", "mod_packet_handler_custom_payload", (ChannelHandler)new PacketListenerCustomPayloadPacket());
        channelPipeline.addBefore("packet_handler", "mod_packet_handler_system_chat", (ChannelHandler)new PacketListenerSystemChatPacket());
        channelPipeline.addBefore("packet_handler", "mod_packet_handler_set_chunk_cache_center", (ChannelHandler)new PacketListenerSetChunkCacheCenterPacket());
        BFLog.log("Finished adding custom packet listener for new connection!", new Object[0]);
    }
}

