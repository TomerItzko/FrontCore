package dev.tomerdev.mercfrontcore.server.event;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.entity.VendorEntity;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.impl.inf.InfectedGame;
import com.boehmod.blockfront.server.BFServerManager;
import com.boehmod.blockfront.server.player.BFServerPlayerData;
import com.boehmod.blockfront.server.player.ServerPlayerDataHandler;
import net.neoforged.bus.api.SubscribeEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import dev.tomerdev.mercfrontcore.MercFrontCore;
import dev.tomerdev.mercfrontcore.data.AddonCommonData;
import dev.tomerdev.mercfrontcore.data.GunModifier;
import dev.tomerdev.mercfrontcore.data.GunModifierFiles;
import dev.tomerdev.mercfrontcore.data.LoadoutEditorStore;
import dev.tomerdev.mercfrontcore.data.LoadoutStore;
import dev.tomerdev.mercfrontcore.data.PlayerGunSkinStore;
import dev.tomerdev.mercfrontcore.net.packet.GunExtraOptionsPacket;
import dev.tomerdev.mercfrontcore.net.packet.GunModifiersPacket;
import dev.tomerdev.mercfrontcore.net.packet.LoadoutsPacket;
import dev.tomerdev.mercfrontcore.net.packet.PlayerGunSkinStatePacket;
import dev.tomerdev.mercfrontcore.net.packet.SetProfileOverridesPacket;
import dev.tomerdev.mercfrontcore.server.ProxyCompatibility;
import dev.tomerdev.mercfrontcore.server.command.MercFrontCoreCommand;
import dev.tomerdev.mercfrontcore.server.net.BfPacketRouter;
import dev.tomerdev.mercfrontcore.setup.GunExtraOptionsIndex;
import dev.tomerdev.mercfrontcore.setup.GunModifierIndex;
import dev.tomerdev.mercfrontcore.setup.LoadoutIndex;
import dev.tomerdev.mercfrontcore.util.MatchCompat;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class MercFrontCoreServerEvents {
    private static final Set<UUID> PENDING_LOADOUT_SYNC = ConcurrentHashMap.newKeySet();
    private static final Set<UUID> PENDING_BF_ROUTER_ATTACH = ConcurrentHashMap.newKeySet();
    private static final Map<UUID, Integer> PENDING_VENDOR_TRACK_SYNC = new ConcurrentHashMap<>();
    private static int permanentSkinTick = 0;

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        int loaded = AddonCommonData.getInstance().loadProfileOverrides(event.getServer());
        MercFrontCore.LOGGER.info("Loaded {} profile overrides.", loaded);
        int loadoutsLoaded = LoadoutStore.getInstance().load(event.getServer());
        MercFrontCore.LOGGER.info("Loaded {} loadouts.", loadoutsLoaded);
        int playerSkinMappings = PlayerGunSkinStore.getInstance().load(event.getServer());
        MercFrontCore.LOGGER.info("Loaded {} permanent player gun skin mappings.", playerSkinMappings);

        GunModifierIndex.init();
        GunModifier.ACTIVE.clear();
        GunModifier.ACTIVE.putAll(GunModifierIndex.DEFAULT);
        try {
            GunModifierFiles.loadModifierMap(
                event.getServer().getPath("mercfrontcore").resolve(GunModifierFiles.GUN_MODIFIERS_PATH),
                GunModifier.ACTIVE
            );
        } catch (Exception e) {
            MercFrontCore.LOGGER.error("Failed to load gun modifiers.", e);
        }
        GunExtraOptionsIndex.rebuild();
    }

    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) {
        int editorLoadoutsLoaded = LoadoutEditorStore.getInstance().loadAndApply(event.getServer());
        MercFrontCore.LOGGER.info("Loaded {} BF loadout-editor entries.", editorLoadoutsLoaded);
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {
        boolean saved = AddonCommonData.getInstance().saveProfileOverrides(event.getServer());
        if (!saved) {
            MercFrontCore.LOGGER.warn("Failed to save profile overrides during shutdown.");
        }
        boolean loadoutsSaved = LoadoutStore.getInstance().save(event.getServer());
        if (!loadoutsSaved) {
            MercFrontCore.LOGGER.warn("Failed to save loadouts during shutdown.");
        }
        boolean playerSkinsSaved = PlayerGunSkinStore.getInstance().save(event.getServer());
        if (!playerSkinsSaved) {
            MercFrontCore.LOGGER.warn("Failed to save permanent player gun skin mappings during shutdown.");
        }
        boolean editorLoadoutsSaved = LoadoutEditorStore.getInstance().saveCachedOrCurrent(event.getServer());
        if (!editorLoadoutsSaved) {
            MercFrontCore.LOGGER.warn("Failed to save BF loadout-editor entries during shutdown.");
        }
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        MercFrontCoreCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayerEntity player)) {
            return;
        }

        if (ProxyCompatibility.shouldBlockForwardedPlayers()) {
            MercFrontCore.LOGGER.warn(
                "enforceDirectConnection=true is set, but hard-blocking proxy traffic is not implemented in remake baseline."
            );
        }

        AddonCommonData.getInstance().refreshLiveProfile(player);
        PlayerGunSkinStore.getInstance().applyToPlayer(player);
        PacketDistributor.sendToPlayer(player, new SetProfileOverridesPacket(
            Map.copyOf(AddonCommonData.getInstance().getProfileOverrides())
        ));
        PacketDistributor.sendToPlayer(player, new GunModifiersPacket(Map.copyOf(GunModifier.ACTIVE), true));
        PacketDistributor.sendToPlayer(player, PlayerGunSkinStore.getInstance().toPacket(player.getUuid()));
        GunExtraOptionsIndex.rebuild();
        var gunOptions = GunExtraOptionsIndex.snapshot();
        PacketDistributor.sendToPlayer(player, new GunExtraOptionsPacket(gunOptions));
        MercFrontCore.LOGGER.info(
            "Sent gun option sync to {} (guns={})",
            player.getNameForScoreboard(),
            gunOptions.size()
        );

        // Defer initial sync to server tick; login event can fire during configuration transition.
        PENDING_LOADOUT_SYNC.add(player.getUuid());
        PENDING_BF_ROUTER_ATTACH.add(player.getUuid());
        PENDING_VENDOR_TRACK_SYNC.put(player.getUuid(), 100);
    }

    @SubscribeEvent
    public void onServerTickPost(ServerTickEvent.Post event) {
        forceRefreshAfkTrackers(event.getServer().getPlayerManager().getPlayerList());
        pulsePermanentGunSkins(event.getServer().getPlayerManager().getPlayerList());

        if (!PENDING_LOADOUT_SYNC.isEmpty()) {
            var iterator = PENDING_LOADOUT_SYNC.iterator();
            while (iterator.hasNext()) {
                UUID uuid = iterator.next();
                ServerPlayerEntity player = event.getServer().getPlayerManager().getPlayer(uuid);
                if (player == null || player.isDisconnected()) {
                    iterator.remove();
                    continue;
                }

                PacketDistributor.sendToPlayer(player, new LoadoutsPacket(LoadoutIndex.currentFlat()));
                iterator.remove();
            }
        }

        if (!PENDING_BF_ROUTER_ATTACH.isEmpty()) {
            var iterator = PENDING_BF_ROUTER_ATTACH.iterator();
            while (iterator.hasNext()) {
                UUID uuid = iterator.next();
                ServerPlayerEntity player = event.getServer().getPlayerManager().getPlayer(uuid);
                if (player == null || player.isDisconnected()) {
                    iterator.remove();
                    continue;
                }
                if (BfPacketRouter.attach(player)) {
                    iterator.remove();
                }
            }
        }
        pulseVendorTrackSync(event.getServer());

    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayerEntity player) {
            AddonCommonData.getInstance().forgetLiveProfileSnapshot(player.getUuid());
            PENDING_LOADOUT_SYNC.remove(player.getUuid());
            PENDING_BF_ROUTER_ATTACH.remove(player.getUuid());
            PENDING_VENDOR_TRACK_SYNC.remove(player.getUuid());
        }
    }

    @SubscribeEvent
    public void onItemToss(ItemTossEvent event) {
        if (!(event.getPlayer() instanceof ServerPlayerEntity player)) {
            return;
        }
        if (!player.isAlive() || !MatchCompat.isInGameSession(player)) {
            return;
        }

        ItemStack tossed = event.getEntity().getStack().copy();
        if (!tossed.isEmpty()) {
            if (!player.getInventory().insertStack(tossed)) {
                int selected = player.getInventory().selectedSlot;
                player.getInventory().setStack(selected, tossed);
            }
        }

        event.setCanceled(true);
        player.getInventory().markDirty();
        player.currentScreenHandler.sendContentUpdates();
        player.playerScreenHandler.sendContentUpdates();
    }

    private static void forceRefreshAfkTrackers(Iterable<ServerPlayerEntity> players) {
        try {
            BlockFront blockFront = BlockFront.getInstance();
            if (blockFront == null) {
                return;
            }
            BFAbstractManager<?, ?, ?> abstractManager = blockFront.getManager();
            if (!(abstractManager instanceof BFServerManager manager)) {
                return;
            }
            if (!(manager.getPlayerDataHandler() instanceof ServerPlayerDataHandler dataHandler)) {
                return;
            }

            for (ServerPlayerEntity player : players) {
                UUID uuid = player.getUuid();
                AbstractGame<?, ?, ?> game = manager.getGameWithPlayer(uuid);
                if (game == null || game.isPaused()) {
                    continue;
                }
                var playerData = dataHandler.getPlayerData(player);
                if (playerData instanceof BFServerPlayerData serverPlayerData) {
                    serverPlayerData.getAfkTracker().playerMoved();
                }
            }
        } catch (Throwable t) {
            MercFrontCore.LOGGER.debug("AFK tick keepalive hook failed: {}", t.toString());
        }
    }

    private static void pulseVendorTrackSync(net.minecraft.server.MinecraftServer server) {
        if (PENDING_VENDOR_TRACK_SYNC.isEmpty()) {
            return;
        }

        var iterator = PENDING_VENDOR_TRACK_SYNC.entrySet().iterator();
        while (iterator.hasNext()) {
            var entry = iterator.next();
            UUID uuid = entry.getKey();
            int remaining = entry.getValue();
            if (remaining <= 0) {
                iterator.remove();
                continue;
            }

            ServerPlayerEntity player = server.getPlayerManager().getPlayer(uuid);
            if (player == null || player.isDisconnected()) {
                iterator.remove();
                continue;
            }

            entry.setValue(remaining - 1);
            if ((remaining % 10) != 0) {
                continue;
            }

            try {
                BFServerManager manager = getServerManager();
                if (manager == null) {
                    continue;
                }
                AbstractGame<?, ?, ?> game = manager.getGameWithPlayer(player.getUuid());
                if (!(game instanceof InfectedGame infectedGame)) {
                    continue;
                }
                VendorEntity vendor = infectedGame.vendorEntity;
                if (vendor == null || vendor.isRemoved()) {
                    continue;
                }
                if (!(vendor.getWorld() instanceof ServerWorld vendorWorld) || player.getServerWorld() != vendorWorld) {
                    continue;
                }

                forceStartTrackingVendor(vendorWorld, vendor, player);
            } catch (Throwable t) {
                MercFrontCore.LOGGER.debug("Vendor track sync pulse failed: {}", t.toString());
            }
        }
    }

    private static void pulsePermanentGunSkins(Iterable<ServerPlayerEntity> players) {
        permanentSkinTick++;
        PlayerGunSkinStore store = PlayerGunSkinStore.getInstance();
        for (ServerPlayerEntity player : players) {
            if (player.isDisconnected()) {
                continue;
            }
            if (MatchCompat.isInGameSession(player) || (permanentSkinTick % 20) == 0) {
                store.applyToPlayer(player);
            }
        }
    }

    public static void queueVendorTrackSyncForGame(ServerWorld world, InfectedGame infectedGame, int ticks) {
        if (ticks <= 0) {
            return;
        }

        try {
            BFServerManager manager = getServerManager();
            if (manager == null || world.getServer() == null) {
                return;
            }

            for (ServerPlayerEntity player : world.getServer().getPlayerManager().getPlayerList()) {
                if (player.isDisconnected()) {
                    continue;
                }
                AbstractGame<?, ?, ?> game = manager.getGameWithPlayer(player.getUuid());
                if (game == infectedGame) {
                    PENDING_VENDOR_TRACK_SYNC.put(player.getUuid(), Math.max(PENDING_VENDOR_TRACK_SYNC.getOrDefault(player.getUuid(), 0), ticks));
                }
            }
        } catch (Throwable t) {
            MercFrontCore.LOGGER.debug("Failed to queue vendor tracking sync for relocate: {}", t.toString());
        }
    }

    @SuppressWarnings("unchecked")
    private static void forceStartTrackingVendor(ServerWorld world, VendorEntity vendor, ServerPlayerEntity player) {
        try {
            EntityTrackerEntry entry = new EntityTrackerEntry(
                world,
                vendor,
                vendor.getType().getTrackTickInterval(),
                vendor.getType().alwaysUpdateVelocity(),
                packet -> player.networkHandler.send(packet)
            );
            entry.startTracking(player);
        } catch (Throwable t) {
            MercFrontCore.LOGGER.debug("Failed to force vendor tracking sync: {}", t.toString());
        }
    }

    private static BFServerManager getServerManager() {
        BlockFront blockFront = BlockFront.getInstance();
        if (blockFront == null) {
            return null;
        }
        BFAbstractManager<?, ?, ?> manager = blockFront.getManager();
        if (manager instanceof BFServerManager serverManager) {
            return serverManager;
        }
        return null;
    }

}
