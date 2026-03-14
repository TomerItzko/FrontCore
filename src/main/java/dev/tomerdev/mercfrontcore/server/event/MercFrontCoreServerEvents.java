package dev.tomerdev.mercfrontcore.server.event;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.entity.VendorEntity;
import com.boehmod.blockfront.common.match.Loadout;
import com.boehmod.blockfront.common.match.MatchClass;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.WinningTeamData;
import com.boehmod.blockfront.game.impl.inf.InfectedGame;
import com.boehmod.blockfront.server.BFServerManager;
import com.boehmod.blockfront.server.player.BFServerPlayerData;
import com.boehmod.blockfront.server.player.ServerPlayerDataHandler;
import com.boehmod.blockfront.util.BFUtils;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import java.lang.reflect.Field;
import java.util.Locale;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.EventPriority;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.server.command.ServerCommandSource;
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
import dev.tomerdev.mercfrontcore.data.MatchXpManager;
import dev.tomerdev.mercfrontcore.data.PlayerXpStore;
import dev.tomerdev.mercfrontcore.data.PlayerGunSkinStore;
import dev.tomerdev.mercfrontcore.data.WinnerSkinDropManager;
import dev.tomerdev.mercfrontcore.net.packet.GunExtraOptionsPacket;
import dev.tomerdev.mercfrontcore.net.packet.GunModifiersPacket;
import dev.tomerdev.mercfrontcore.net.packet.LoadoutsPacket;
import dev.tomerdev.mercfrontcore.net.packet.PlayerGunSkinStatePacket;
import dev.tomerdev.mercfrontcore.net.packet.ProfileXpSyncPacket;
import dev.tomerdev.mercfrontcore.net.packet.SetClassRanksPacket;
import dev.tomerdev.mercfrontcore.net.packet.SetProfileOverridesPacket;
import dev.tomerdev.mercfrontcore.server.ProxyCompatibility;
import dev.tomerdev.mercfrontcore.server.command.AssetCommandHelper;
import dev.tomerdev.mercfrontcore.server.command.MercFrontCoreCommand;
import dev.tomerdev.mercfrontcore.server.net.BfPacketRouter;
import dev.tomerdev.mercfrontcore.util.ClassRankCompat;
import dev.tomerdev.mercfrontcore.setup.GunExtraOptionsIndex;
import dev.tomerdev.mercfrontcore.setup.GunModifierIndex;
import dev.tomerdev.mercfrontcore.setup.LoadoutIndex;
import dev.tomerdev.mercfrontcore.util.LoadoutXpCompat;
import dev.tomerdev.mercfrontcore.util.MatchCompat;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class MercFrontCoreServerEvents {
    private static final Field COMMAND_NODE_CHILDREN_FIELD = mercfrontcore$findCommandNodeField("children");
    private static final Field COMMAND_NODE_LITERALS_FIELD = mercfrontcore$findCommandNodeField("literals");
    private static final Field COMMAND_NODE_ARGUMENTS_FIELD = mercfrontcore$findCommandNodeField("arguments");
    private static final int VENDOR_SYNC_WINDOW_TICKS = 120;
    private static final int VENDOR_SYNC_ACTIVE_INTERVAL = 5;
    private static final int VENDOR_SYNC_MAINTENANCE_INTERVAL = 40;
    private static final Set<UUID> PENDING_LOADOUT_SYNC = ConcurrentHashMap.newKeySet();
    private static final Set<UUID> PENDING_BF_ROUTER_ATTACH = ConcurrentHashMap.newKeySet();
    private static final Map<UUID, Integer> PENDING_VENDOR_TRACK_SYNC = new ConcurrentHashMap<>();
    private static final Map<UUID, GameStatus> LAST_GAME_STATUS = new ConcurrentHashMap<>();
    private static final Map<UUID, String> LAST_ENFORCED_LOADOUT_LOCK = new ConcurrentHashMap<>();
    private static int permanentSkinTick = 0;
    private static int vendorSyncTick = 0;

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        WinnerSkinDropManager.resetSession();
        PlayerXpStore.getInstance().clearSession();
        LAST_GAME_STATUS.clear();
        LAST_ENFORCED_LOADOUT_LOCK.clear();
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
        WinnerSkinDropManager.resetSession();
        LAST_GAME_STATUS.clear();
        LAST_ENFORCED_LOADOUT_LOCK.clear();
        saveOnlinePlayerXp(event.getServer());
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

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRegisterCommandsLowest(RegisterCommandsEvent event) {
        mercfrontcore$replaceNativeAssetsEdit(event.getDispatcher().getRoot());
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayerEntity player)) {
            return;
        }
        boolean dedicatedServer = mercfrontcore$isDedicatedServer(player.getServer());

        var dataHandler = getServerManager() != null && getServerManager().getPlayerDataHandler() instanceof ServerPlayerDataHandler serverDataHandler
            ? serverDataHandler
            : null;
        if (dataHandler != null) {
            var profile = dataHandler.getCloudProfile(player);
            var storedXp = PlayerXpStore.getInstance().loadOrCreate(
                player.getServer(),
                player.getUuid(),
                player.getNameForScoreboard(),
                profile.getExp(),
                profile.getPrestigeLevel(),
                MatchXpManager.snapshotClassExp(profile)
            );
            profile.setExp(storedXp.exp());
            profile.setPrestigeLevel(storedXp.prestige());
            for (int i = 0; i < storedXp.classExp().size(); i++) {
                profile.setClassExp(i, storedXp.classExp().get(i));
            }
            var cachedProfile = dataHandler.getCloudProfile(player.getUuid());
            if (cachedProfile != profile) {
                cachedProfile.setExp(storedXp.exp());
                cachedProfile.setPrestigeLevel(storedXp.prestige());
                for (int i = 0; i < storedXp.classExp().size(); i++) {
                    cachedProfile.setClassExp(i, storedXp.classExp().get(i));
                }
            }
        }

        if (ProxyCompatibility.shouldBlockForwardedPlayers()) {
            MercFrontCore.LOGGER.warn(
                "enforceDirectConnection=true is set, but hard-blocking proxy traffic is not implemented in remake baseline."
            );
        }

        AddonCommonData.getInstance().refreshLiveProfile(player);
        PlayerGunSkinStore.getInstance().applyToPlayer(player);
        PacketDistributor.sendToPlayer(player, SetClassRanksPacket.fromCurrentConfig());
        PacketDistributor.sendToPlayer(player, new SetProfileOverridesPacket(
            Map.copyOf(AddonCommonData.getInstance().getProfileOverrides())
        ));
        PacketDistributor.sendToPlayer(player, new GunModifiersPacket(Map.copyOf(GunModifier.ACTIVE), true));
        PacketDistributor.sendToPlayer(player, PlayerGunSkinStore.getInstance().toPacket(player.getUuid()));
        if (dataHandler != null) {
            var currentProfile = dataHandler.getCloudProfile(player);
            syncProfileXpToPlayer(player, player.getUuid(), currentProfile);
            for (ServerPlayerEntity online : player.getServer().getPlayerManager().getPlayerList()) {
                if (online.getUuid().equals(player.getUuid())) {
                    continue;
                }
                syncProfileXpToPlayer(player, online.getUuid(), dataHandler.getCloudProfile(online));
                syncProfileXpToPlayer(online, player.getUuid(), currentProfile);
            }
        }
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
        if (dedicatedServer) {
            PENDING_BF_ROUTER_ATTACH.add(player.getUuid());
        } else {
            PENDING_BF_ROUTER_ATTACH.remove(player.getUuid());
        }
        PENDING_VENDOR_TRACK_SYNC.put(player.getUuid(), VENDOR_SYNC_WINDOW_TICKS);
    }

    @SubscribeEvent
    public void onServerTickPost(ServerTickEvent.Post event) {
        forceRefreshAfkTrackers(event.getServer().getPlayerManager().getPlayerList());
        pulsePermanentGunSkins(event.getServer().getPlayerManager().getPlayerList());
        processPostGameTransitions(event.getServer());
        enforceLockedLoadouts(event.getServer());
        vendorSyncTick++;

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
            LAST_ENFORCED_LOADOUT_LOCK.remove(player.getUuid());
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
        BFServerManager manager = getServerManager();
        if (manager == null) {
            return;
        }

        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            if (player == null || player.isDisconnected()) {
                continue;
            }

            int remaining = PENDING_VENDOR_TRACK_SYNC.getOrDefault(player.getUuid(), 0);
            if (remaining > 0) {
                PENDING_VENDOR_TRACK_SYNC.put(player.getUuid(), remaining - 1);
            } else {
                PENDING_VENDOR_TRACK_SYNC.remove(player.getUuid());
            }

            boolean shouldPulse = remaining > 0 && (remaining % VENDOR_SYNC_ACTIVE_INTERVAL) == 0;
            if (!shouldPulse && (vendorSyncTick % VENDOR_SYNC_MAINTENANCE_INTERVAL) != 0) {
                continue;
            }

            try {
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

    private static boolean mercfrontcore$isDedicatedServer(net.minecraft.server.MinecraftServer server) {
        if (server == null) {
            return false;
        }
        try {
            Object result = server.getClass().getMethod("isDedicatedServer").invoke(server);
            if (result instanceof Boolean value) {
                return value;
            }
        } catch (Throwable ignored) {
        }
        try {
            Object result = server.getClass().getMethod("isDedicated").invoke(server);
            return result instanceof Boolean value && value;
        } catch (Throwable ignored) {
            return false;
        }
    }

    private static void processPostGameTransitions(net.minecraft.server.MinecraftServer server) {
        BFServerManager manager = getServerManager();
        if (manager == null) {
            LAST_GAME_STATUS.clear();
            return;
        }

        Set<UUID> liveGameIds = ConcurrentHashMap.newKeySet();
        for (var gameAsset : manager.getGames().values()) {
            if (gameAsset == null) {
                continue;
            }

            AbstractGame<?, ?, ?> game = gameAsset.getGame();
            if (game == null) {
                continue;
            }

            UUID gameId = game.getUUID();
            liveGameIds.add(gameId);

            GameStatus currentStatus = game.getStatus();
            GameStatus previousStatus = LAST_GAME_STATUS.put(gameId, currentStatus);
            if (previousStatus == GameStatus.GAME && currentStatus == GameStatus.POST_GAME) {
                processPostGameTransition(server, manager, game);
            }
        }

        LAST_GAME_STATUS.keySet().removeIf(uuid -> !liveGameIds.contains(uuid));
    }

    private static void processPostGameTransition(
        net.minecraft.server.MinecraftServer server,
        BFServerManager manager,
        AbstractGame<?, ?, ?> game
    ) {
        if (server.getOverworld() == null) {
            return;
        }

        try {
            Set<UUID> players = Set.copyOf(game.getPlayerManager().getPlayerUUIDs());
            if (players.isEmpty()) {
                return;
            }

            WinningTeamData winningTeam = game.getPlayerManager().getWinningTeam(server.getOverworld(), players, null);
            if (winningTeam == null) {
                MercFrontCore.LOGGER.info("Post-game transition found no winning team for game {}", game.getUUID());
                return;
            }

            ServerPlayerDataHandler dataHandler = manager.getPlayerDataHandler() instanceof ServerPlayerDataHandler serverDataHandler
                ? serverDataHandler
                : null;
            for (UUID playerUuid : players) {
                if (dataHandler != null) {
                    ServerPlayerEntity player = server.getPlayerManager().getPlayer(playerUuid);
                    if (player != null && !player.isDisconnected()) {
                        MatchXpManager.MatchXpAwardResult xpAward = MatchXpManager.awardMatchXp(game, dataHandler, player, winningTeam);
                        if (xpAward != null) {
                            syncProfileXpToAllPlayers(playerUuid, dataHandler.getCloudProfile(player));
                            MercFrontCore.LOGGER.info(
                                "Awarded {} BF XP to {} for game {} ({} -> {})",
                                xpAward.gainedXp(),
                                player.getNameForScoreboard(),
                                game.getUUID(),
                                xpAward.beforeXp(),
                                xpAward.afterXp()
                            );
                        }
                    }
                }

                boolean victory = false;
                if (winningTeam.team != null && winningTeam.team.getPlayers().contains(playerUuid)) {
                    victory = true;
                } else if (winningTeam.topPlayers != null && winningTeam.topPlayers.contains(playerUuid)) {
                    victory = true;
                }
                WinnerSkinDropManager.maybeAwardWinnerSkin(game, manager, playerUuid, victory);
            }
        } catch (Throwable t) {
            MercFrontCore.LOGGER.warn("Failed post-game transition processing for game {}", game.getUUID(), t);
        }
    }

    private static void enforceLockedLoadouts(net.minecraft.server.MinecraftServer server) {
        BFServerManager manager = getServerManager();
        if (manager == null || !(manager.getPlayerDataHandler() instanceof ServerPlayerDataHandler dataHandler)) {
            LAST_ENFORCED_LOADOUT_LOCK.clear();
            return;
        }

        Set<UUID> seenPlayers = ConcurrentHashMap.newKeySet();
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            if (player == null || player.isDisconnected() || player.isCreative()) {
                continue;
            }

            AbstractGame<?, ?, ?> game = manager.getGameWithPlayer(player.getUuid());
            if (game == null) {
                LAST_ENFORCED_LOADOUT_LOCK.remove(player.getUuid());
                continue;
            }
            if (game.getStatus() != GameStatus.PRE_GAME && game.getStatus() != GameStatus.GAME) {
                LAST_ENFORCED_LOADOUT_LOCK.remove(player.getUuid());
                continue;
            }

            GameTeam team = game.getPlayerManager().getPlayerTeam(player.getUuid());
            if (team == null) {
                LAST_ENFORCED_LOADOUT_LOCK.remove(player.getUuid());
                continue;
            }

            int classOrdinal = game.getPlayerStatData(player.getUuid()).getInteger(BFStats.CLASS.getKey(), -1);
            int classLevel = game.getPlayerStatData(player.getUuid()).getInteger(BFStats.CLASS_INDEX.getKey(), -1);
            if (classOrdinal < 0 || classLevel < 0 || classOrdinal >= MatchClass.values().length) {
                LAST_ENFORCED_LOADOUT_LOCK.remove(player.getUuid());
                continue;
            }

            MatchClass matchClass = MatchClass.values()[classOrdinal];
            Loadout loadout = team.getDivisionData(game).getLoadout(matchClass, classLevel);
            PlayerCloudData profile = dataHandler.getCloudProfile(player);
            if (!ClassRankCompat.canUseClass(profile, matchClass)) {
                seenPlayers.add(player.getUuid());
                String stateKey = "rank:" + matchClass + ":" + ClassRankCompat.getRequiredRank(matchClass).getID() + ":" + profile.getRank().getID();
                if (!stateKey.equals(LAST_ENFORCED_LOADOUT_LOCK.put(player.getUuid(), stateKey))) {
                    MercFrontCore.LOGGER.info(
                        "Tick-enforced locked class for {} class={} requiredRank={} currentRank={}",
                        player.getNameForScoreboard(),
                        matchClass,
                        ClassRankCompat.describeRank(ClassRankCompat.getRequiredRank(matchClass)),
                        ClassRankCompat.describeRank(profile.getRank())
                    );
                    BFUtils.sendNoticeMessage(
                        player,
                        net.minecraft.text.Text.translatable(
                            "bf.message.gamemode.class.error.rank",
                            net.minecraft.text.Text.literal(ClassRankCompat.getRequiredRank(matchClass).getTitle()).formatted(net.minecraft.util.Formatting.GRAY)
                        ).formatted(net.minecraft.util.Formatting.RED)
                    );
                }

                game.getPlayerStatData(player.getUuid()).setInteger(BFStats.CLASS.getKey(), -1);
                game.getPlayerStatData(player.getUuid()).setInteger(BFStats.CLASS_INDEX.getKey(), -1);
                game.getPlayerStatData(player.getUuid()).setInteger(BFStats.CLASS_ALIVE.getKey(), -1);
                player.getInventory().clear();
                player.currentScreenHandler.sendContentUpdates();
                player.playerScreenHandler.sendContentUpdates();
                continue;
            }
            int minimumXp = LoadoutXpCompat.resolveMinimumXp(game, team, matchClass, classLevel, loadout);
            if (minimumXp <= 0) {
                LAST_ENFORCED_LOADOUT_LOCK.remove(player.getUuid());
                continue;
            }

            int classXp = LoadoutXpCompat.getEffectiveXp(profile, matchClass);
            if (classXp >= minimumXp) {
                LAST_ENFORCED_LOADOUT_LOCK.remove(player.getUuid());
                continue;
            }

            seenPlayers.add(player.getUuid());
            String stateKey = matchClass + ":" + classLevel + ":" + minimumXp + ":" + classXp;
            if (!stateKey.equals(LAST_ENFORCED_LOADOUT_LOCK.put(player.getUuid(), stateKey))) {
                MercFrontCore.LOGGER.info(
                    "Tick-enforced locked loadout for {} class={} level={} minimumXp={} classXp={}",
                    player.getNameForScoreboard(),
                    matchClass,
                    classLevel,
                    minimumXp,
                    classXp
                );
                BFUtils.sendNoticeMessage(player, LoadoutXpCompat.createMinimumXpMessage(minimumXp, classXp));
            }

            game.getPlayerStatData(player.getUuid()).setInteger(BFStats.CLASS.getKey(), -1);
            game.getPlayerStatData(player.getUuid()).setInteger(BFStats.CLASS_INDEX.getKey(), -1);
            game.getPlayerStatData(player.getUuid()).setInteger(BFStats.CLASS_ALIVE.getKey(), -1);
            player.getInventory().clear();
            player.currentScreenHandler.sendContentUpdates();
            player.playerScreenHandler.sendContentUpdates();
        }

        LAST_ENFORCED_LOADOUT_LOCK.keySet().removeIf(uuid -> !seenPlayers.contains(uuid) && server.getPlayerManager().getPlayer(uuid) == null);
    }

    private static void syncProfileXpToAllPlayers(UUID playerUuid, PlayerCloudData profile) {
        PacketDistributor.sendToAllPlayers(createProfileXpSyncPacket(playerUuid, profile));
    }

    private static void syncProfileXpToPlayer(ServerPlayerEntity receiver, UUID playerUuid, PlayerCloudData profile) {
        PacketDistributor.sendToPlayer(receiver, createProfileXpSyncPacket(playerUuid, profile));
    }

    private static ProfileXpSyncPacket createProfileXpSyncPacket(UUID playerUuid, PlayerCloudData profile) {
        return new ProfileXpSyncPacket(
            playerUuid,
            profile.getExp(),
            profile.getPrestigeLevel(),
            MatchXpManager.snapshotClassExp(profile)
        );
    }

    private static void saveOnlinePlayerXp(net.minecraft.server.MinecraftServer server) {
        BFServerManager manager = getServerManager();
        if (manager == null || !(manager.getPlayerDataHandler() instanceof ServerPlayerDataHandler dataHandler)) {
            return;
        }
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            if (AddonCommonData.getInstance().getProfileOverrides().containsKey(player.getUuid())) {
                continue;
            }
            var profile = dataHandler.getCloudProfile(player);
            PlayerXpStore.getInstance().save(
                server,
                player.getUuid(),
                player.getNameForScoreboard(),
                new PlayerXpStore.PlayerXpData(
                    profile.getExp(),
                    profile.getPrestigeLevel(),
                    MatchXpManager.snapshotClassExp(profile),
                    player.getNameForScoreboard(),
                    java.util.List.of()
                )
            );
        }
    }

    private static void mercfrontcore$replaceNativeAssetsEdit(CommandNode<ServerCommandSource> root) {
        CommandNode<ServerCommandSource> assetsNode = root.getChild("assets");
        if (assetsNode == null) {
            MercFrontCore.LOGGER.warn("Could not patch native /assets edit branch: /assets command was not found.");
            return;
        }

        CommandNode<ServerCommandSource> editNode = assetsNode.getChild("edit");
        if (editNode == null) {
            MercFrontCore.LOGGER.warn("Could not patch native /assets edit branch: /assets edit was not found.");
            return;
        }

        CommandNode<ServerCommandSource> assetTypeNode = editNode.getChild("assetType");
        if (assetTypeNode == null) {
            MercFrontCore.LOGGER.warn("Could not patch native /assets edit branch: assetType node was not found.");
            return;
        }

        CommandNode<ServerCommandSource> assetNameNode = assetTypeNode.getChild("assetName");
        if (assetNameNode == null) {
            MercFrontCore.LOGGER.warn("Could not patch native /assets edit branch: assetName node was not found.");
            return;
        }

        ArgumentCommandNode<ServerCommandSource, ?> replacement = argument("args", StringArgumentType.greedyString())
            .suggests(AssetCommandHelper::suggestAssetArgs)
            .executes(AssetCommandHelper::executeEdit)
            .build();

        if (!mercfrontcore$replaceChildNode(assetNameNode, "args", replacement)) {
            MercFrontCore.LOGGER.warn("Failed to replace native /assets edit args branch.");
        }
    }

    @SuppressWarnings("unchecked")
    private static boolean mercfrontcore$replaceChildNode(
        CommandNode<ServerCommandSource> parent,
        String childName,
        CommandNode<ServerCommandSource> replacement
    ) {
        if (COMMAND_NODE_CHILDREN_FIELD == null || COMMAND_NODE_LITERALS_FIELD == null || COMMAND_NODE_ARGUMENTS_FIELD == null) {
            return false;
        }

        try {
            java.util.Map<String, CommandNode<ServerCommandSource>> children =
                (java.util.Map<String, CommandNode<ServerCommandSource>>) COMMAND_NODE_CHILDREN_FIELD.get(parent);
            java.util.Map<String, LiteralCommandNode<ServerCommandSource>> literals =
                (java.util.Map<String, LiteralCommandNode<ServerCommandSource>>) COMMAND_NODE_LITERALS_FIELD.get(parent);
            java.util.Map<String, ArgumentCommandNode<ServerCommandSource, ?>> arguments =
                (java.util.Map<String, ArgumentCommandNode<ServerCommandSource, ?>>) COMMAND_NODE_ARGUMENTS_FIELD.get(parent);

            children.put(childName, replacement);
            literals.remove(childName);
            arguments.remove(childName);
            if (replacement instanceof LiteralCommandNode<ServerCommandSource> literalNode) {
                literals.put(childName, literalNode);
            } else if (replacement instanceof ArgumentCommandNode<ServerCommandSource, ?> argumentNode) {
                arguments.put(childName, argumentNode);
            }
            MercFrontCore.LOGGER.info("Replaced native /assets {} branch.", childName);
            return true;
        } catch (ReflectiveOperationException e) {
            MercFrontCore.LOGGER.warn("Failed to replace child node '{}' on Brigadier tree.", childName, e);
            return false;
        }
    }

    private static Field mercfrontcore$findCommandNodeField(String name) {
        try {
            Field field = CommandNode.class.getDeclaredField(name);
            field.setAccessible(true);
            return field;
        } catch (ReflectiveOperationException e) {
            MercFrontCore.LOGGER.warn("Unable to access Brigadier CommandNode.{}", name.toLowerCase(Locale.ROOT), e);
            return null;
        }
    }

}
