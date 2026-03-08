package dev.tomerdev.mercfrontcore.server.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.minecraft.server.network.ServerPlayerEntity;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import dev.tomerdev.mercfrontcore.MercFrontCore;
import dev.tomerdev.mercfrontcore.data.AddonCommonData;
import dev.tomerdev.mercfrontcore.data.GunModifier;
import dev.tomerdev.mercfrontcore.data.GunModifierFiles;
import dev.tomerdev.mercfrontcore.data.LoadoutEditorStore;
import dev.tomerdev.mercfrontcore.data.LoadoutStore;
import dev.tomerdev.mercfrontcore.net.packet.GunExtraOptionsPacket;
import dev.tomerdev.mercfrontcore.net.packet.GunModifiersPacket;
import dev.tomerdev.mercfrontcore.net.packet.LoadoutsPacket;
import dev.tomerdev.mercfrontcore.net.packet.SetProfileOverridesPacket;
import dev.tomerdev.mercfrontcore.server.ProxyCompatibility;
import dev.tomerdev.mercfrontcore.server.command.MercFrontCoreCommand;
import dev.tomerdev.mercfrontcore.setup.GunExtraOptionsIndex;
import dev.tomerdev.mercfrontcore.setup.GunModifierIndex;
import dev.tomerdev.mercfrontcore.setup.LoadoutIndex;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class MercFrontCoreServerEvents {
    private static final Set<UUID> PENDING_LOADOUT_SYNC = ConcurrentHashMap.newKeySet();

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        int loaded = AddonCommonData.getInstance().loadProfileOverrides(event.getServer());
        MercFrontCore.LOGGER.info("Loaded {} profile overrides.", loaded);
        int loadoutsLoaded = LoadoutStore.getInstance().load(event.getServer());
        MercFrontCore.LOGGER.info("Loaded {} loadouts.", loadoutsLoaded);
        int editorLoadoutsLoaded = LoadoutEditorStore.getInstance().loadAndApply(event.getServer());
        MercFrontCore.LOGGER.info("Loaded {} BF loadout-editor entries.", editorLoadoutsLoaded);

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
    public void onServerStopping(ServerStoppingEvent event) {
        boolean saved = AddonCommonData.getInstance().saveProfileOverrides(event.getServer());
        if (!saved) {
            MercFrontCore.LOGGER.warn("Failed to save profile overrides during shutdown.");
        }
        boolean loadoutsSaved = LoadoutStore.getInstance().save(event.getServer());
        if (!loadoutsSaved) {
            MercFrontCore.LOGGER.warn("Failed to save loadouts during shutdown.");
        }
        boolean editorLoadoutsSaved = LoadoutEditorStore.getInstance().saveCurrent(event.getServer());
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

        PacketDistributor.sendToPlayer(player, new SetProfileOverridesPacket(
            Map.copyOf(AddonCommonData.getInstance().getProfileOverrides())
        ));
        PacketDistributor.sendToPlayer(player, new GunModifiersPacket(Map.copyOf(GunModifier.ACTIVE), true));
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
    }

    @SubscribeEvent
    public void onServerTickPost(ServerTickEvent.Post event) {
        if (PENDING_LOADOUT_SYNC.isEmpty()) {
            return;
        }

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

}
