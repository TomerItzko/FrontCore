/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.Registry
 *  net.minecraft.world.entity.player.Player
 *  net.neoforged.neoforge.attachment.AttachmentType
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  net.neoforged.neoforge.registries.DeferredRegister
 *  net.neoforged.neoforge.registries.NeoForgeRegistries
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.registry;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.server.BFServerManager;
import com.boehmod.blockfront.server.player.BFServerPlayerData;
import com.boehmod.blockfront.server.player.ServerPlayerDataHandler;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class BFAttachments {
    @NotNull
    public static final DeferredRegister<AttachmentType<?>> DR = DeferredRegister.create((Registry)NeoForgeRegistries.ATTACHMENT_TYPES, (String)"bf");
    @NotNull
    public static final String SERVER_PLAYER_DATA_ID = "server_player_data";
    @NotNull
    public static final String CLIENT_PLAYER_DATA_ID = "client_player_data";
    @NotNull
    public static final String CLOUD_PLAYER_DATA_ID = "cloud_player_data";
    @NotNull
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<BFServerPlayerData>> SERVER_PLAYER_DATA = DR.register("server_player_data", () -> AttachmentType.builder(iAttachmentHolder -> {
        if (iAttachmentHolder instanceof Player) {
            Player player = (Player)iAttachmentHolder;
            BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
            if (bFAbstractManager instanceof BFServerManager) {
                BFServerManager bFServerManager = (BFServerManager)bFAbstractManager;
                return (BFServerPlayerData)((ServerPlayerDataHandler)bFServerManager.getPlayerDataHandler()).getPlayerData(player.getUUID());
            }
            throw new IllegalStateException("Server mod manager is not an instance of ServerModManager!");
        }
        throw new IllegalArgumentException("Server player data must be created with a player instance!");
    }).build());
    @NotNull
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<BFClientPlayerData>> CLIENT_PLAYER_DATA = DR.register("client_player_data", () -> AttachmentType.builder(iAttachmentHolder -> {
        if (iAttachmentHolder instanceof Player) {
            Player player = (Player)iAttachmentHolder;
            BFClientManager bFClientManager = BFClientManager.getInstance();
            assert (bFClientManager != null) : "Client mod manager is null!";
            return (BFClientPlayerData)((ClientPlayerDataHandler)bFClientManager.getPlayerDataHandler()).getPlayerData(player.getUUID());
        }
        throw new IllegalArgumentException("Client player data must be created with a player instance!");
    }).build());
    @NotNull
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<PlayerCloudData>> CLOUD_PLAYER_DATA = DR.register("cloud_player_data", () -> AttachmentType.builder(iAttachmentHolder -> {
        if (iAttachmentHolder instanceof Player) {
            Player player = (Player)iAttachmentHolder;
            BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
            assert (bFAbstractManager != null) : "BlockFront manager is null!";
            return ((PlayerDataHandler)bFAbstractManager.getPlayerDataHandler()).getCloudProfile(player.getUUID());
        }
        throw new IllegalArgumentException("Player cloud data must be created with a player instance!");
    }).build());
}

