/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.item.CloudItems
 *  com.boehmod.bflib.cloud.common.player.achievement.CloudAchievements
 *  javax.annotation.Nullable
 *  net.minecraft.Util
 *  net.minecraft.data.DataGenerator
 *  net.minecraft.data.DataProvider
 *  net.minecraft.data.PackOutput
 *  net.minecraft.network.chat.Component
 *  net.neoforged.bus.api.IEventBus
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.ModList
 *  net.neoforged.fml.common.Mod
 *  net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
 *  net.neoforged.neoforge.common.NeoForge
 *  net.neoforged.neoforge.common.data.ExistingFileHelper
 *  net.neoforged.neoforge.data.event.GatherDataEvent
 *  net.neoforged.neoforge.event.RegisterCommandsEvent
 *  net.neoforged.neoforge.registries.NewRegistryEvent
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront;

import com.boehmod.bflib.cloud.common.item.CloudItems;
import com.boehmod.bflib.cloud.common.player.achievement.CloudAchievements;
import com.boehmod.blockfront.client.BFClient;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.setup.BFCommandSetup;
import com.boehmod.blockfront.common.setup.BFEntityAttributeSetup;
import com.boehmod.blockfront.common.setup.BFEventSetup;
import com.boehmod.blockfront.common.setup.BFOldRegistryMigration;
import com.boehmod.blockfront.common.setup.BFPacketSetup;
import com.boehmod.blockfront.registry.BFAttachments;
import com.boehmod.blockfront.registry.BFBlockAttributes;
import com.boehmod.blockfront.registry.BFBlockEntityTypes;
import com.boehmod.blockfront.registry.BFBlockSoundAttributes;
import com.boehmod.blockfront.registry.BFBlockTraversableAttributes;
import com.boehmod.blockfront.registry.BFBlocks;
import com.boehmod.blockfront.registry.BFBotVoices;
import com.boehmod.blockfront.registry.BFCreativeTabs;
import com.boehmod.blockfront.registry.BFDataComponents;
import com.boehmod.blockfront.registry.BFEntityTypes;
import com.boehmod.blockfront.registry.BFItems;
import com.boehmod.blockfront.registry.BFParticleTypes;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.registry.custom.BlockAttribute;
import com.boehmod.blockfront.registry.custom.BlockSoundAttribute;
import com.boehmod.blockfront.registry.custom.BlockTraversableAttribute;
import com.boehmod.blockfront.registry.custom.BotVoice;
import com.boehmod.blockfront.registry.gen.provider.BFBlockStateProvider;
import com.boehmod.blockfront.registry.gen.provider.BFBlockTagsProvider;
import com.boehmod.blockfront.registry.gen.provider.BFItemModelProvider;
import com.boehmod.blockfront.registry.gen.provider.BFLanguageProvider;
import com.boehmod.blockfront.server.BFServer;
import com.boehmod.blockfront.util.BFLog;
import com.boehmod.blockfront.util.EnvironmentUtils;
import com.boehmod.blockfront.util.ReleaseStage;
import java.io.File;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import org.jetbrains.annotations.NotNull;

@Mod(value="bf")
public final class BlockFront {
    @NotNull
    public static final String DISPLAY_NAME = "BlockFront";
    @NotNull
    public static final String MOD_ID = "bf";
    @NotNull
    public static final String VERSION = ModList.get().getModFileById("bf").versionString();
    @NotNull
    public static final Component VERSION_COMPONENT = Component.literal((String)VERSION);
    @NotNull
    public static final ReleaseStage RELEASE_STAGE = ReleaseStage.BETA;
    @NotNull
    public static final Component DISPLAY_NAME_COMPONENT = Component.literal((String)"BlockFront");
    @NotNull
    public static final Component RELEASE_STAGE_COMPONENT = RELEASE_STAGE.getComponent();
    @NotNull
    public static final String CLOUD_URL = "cloud.blockfrontmc.com";
    public static final int CLOUD_PORT = 1924;
    @NotNull
    public static final String DATA_FOLDER = "BlockFront".toLowerCase(Locale.ROOT) + "/";
    @NotNull
    private static BlockFront INSTANCE;
    @Nullable
    private BFAbstractManager<?, ?, ?> modManager;

    public BlockFront(@NotNull IEventBus modBus) {
        INSTANCE = this;
        BFLog.log("Initializing BlockFront v" + VERSION + " (" + String.valueOf((Object)RELEASE_STAGE) + ") for OS '" + Util.getPlatform().telemetryName() + "'.", new Object[0]);
        BFLog.log("Class " + CloudItems.ITEM_GUN_BAR.getName() + " exists!", new Object[0]);
        BFLog.log("Achievement " + CloudAchievements.ACH_STATS_HEADSHOT_10.getName() + " exists!", new Object[0]);
        BFLog.log("Registering universal events...", new Object[0]);
        NeoForge.EVENT_BUS.register((Object)this);
        modBus.addListener(this::commonSetup);
        modBus.addListener(this::gatherData);
        modBus.addListener(this::addNewRegistries);
        modBus.addListener(BFPacketSetup::register);
        modBus.addListener(BFEntityAttributeSetup::register);
        BFLog.log("Registering data components...", new Object[0]);
        BFDataComponents.DR.register(modBus);
        BFLog.log("Registering player data components...", new Object[0]);
        BFAttachments.DR.register(modBus);
        BFLog.log("Registering blocks...", new Object[0]);
        BFBlocks.DR.register(modBus);
        BFLog.log("Registering generated blocks...", new Object[0]);
        BFBlocks.DR_GEN.register(modBus);
        BFLog.log("Registering items...", new Object[0]);
        BFItems.DR.register(modBus);
        BFLog.log("Registering block entities...", new Object[0]);
        BFBlockEntityTypes.DR.register(modBus);
        BFLog.log("Registering sounds...", new Object[0]);
        BFSounds.DR.register(modBus);
        BFLog.log("Registering entity types...", new Object[0]);
        BFEntityTypes.DR.register(modBus);
        BFLog.log("Registering particles...", new Object[0]);
        BFParticleTypes.DR.register(modBus);
        BFLog.log("Registering creative tabs...", new Object[0]);
        BFCreativeTabs.DR.register(modBus);
        BFLog.log("Registering bot voices...", new Object[0]);
        BFBotVoices.DR.register(modBus);
        BFLog.log("Registering block sound attributes...", new Object[0]);
        BFBlockSoundAttributes.DR.register(modBus);
        BFLog.log("Registering block traversable attributes...", new Object[0]);
        BFBlockTraversableAttributes.DR.register(modBus);
        BFLog.log("Registering block attributes...", new Object[0]);
        BFBlockAttributes.DR.register(modBus);
        BFLog.log("Migrating old objects...", new Object[0]);
        BFOldRegistryMigration.init();
        if (EnvironmentUtils.isClient()) {
            modBus.addListener(clientSetupEvent -> BFClient.init(this, clientSetupEvent));
            modBus.addListener(BFClient::registerDimensionEffects);
            modBus.addListener(BFClient::registerClientExtensions);
            modBus.addListener(BFClient::registerBlockColorHandlers);
            modBus.addListener(BFClient::registerItemColorHandlers);
        }
        if (EnvironmentUtils.isServer()) {
            modBus.addListener(serverSetupEvent -> BFServer.init(this, serverSetupEvent));
        }
        BFLog.log("Successfully initialized BlockFront v" + VERSION + " (" + String.valueOf((Object)RELEASE_STAGE) + ")", new Object[0]);
    }

    @NotNull
    public static BlockFront getInstance() {
        return INSTANCE;
    }

    private void commonSetup(@NotNull FMLCommonSetupEvent event) {
        BFLog.log("Entering common setup procedure...", new Object[0]);
        if (new File(DATA_FOLDER).mkdirs()) {
            BFLog.log(String.format("Successfully created '%s' directory!", DATA_FOLDER), new Object[0]);
        } else {
            BFLog.log(String.format("'%s' directory already exists!", DATA_FOLDER), new Object[0]);
        }
        BFEventSetup.register(NeoForge.EVENT_BUS);
        BFLog.log("Completed common setup procedure!", new Object[0]);
    }

    private void gatherData(@NotNull GatherDataEvent event) {
        DataGenerator dataGenerator = event.getGenerator();
        PackOutput packOutput = event.getGenerator().getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture completableFuture = event.getLookupProvider();
        boolean bl = event.includeClient();
        dataGenerator.addProvider(bl, (DataProvider)new BFBlockStateProvider(packOutput, existingFileHelper));
        dataGenerator.addProvider(bl, (DataProvider)new BFItemModelProvider(packOutput, existingFileHelper));
        dataGenerator.addProvider(event.includeServer(), (DataProvider)new BFBlockTagsProvider(packOutput, completableFuture, existingFileHelper));
        dataGenerator.addProvider(bl, (DataProvider)new BFLanguageProvider(packOutput));
    }

    private void addNewRegistries(@NotNull NewRegistryEvent event) {
        BFLog.log("Registering new registries...", new Object[0]);
        event.register(BlockSoundAttribute.REGISTRY);
        event.register(BlockTraversableAttribute.REGISTRY);
        event.register(BlockAttribute.REGISTRY);
        event.register(BotVoice.REGISTRY);
        BFLog.log("Successfully registered new registries!", new Object[0]);
    }

    @SubscribeEvent
    public void registerCommands(@NotNull RegisterCommandsEvent event) {
        BFLog.log("Registering commands...", new Object[0]);
        BFCommandSetup.register(event);
        BFLog.log("Successfully registered commands!", new Object[0]);
    }

    @Nullable
    public BFAbstractManager<?, ?, ?> getManager() {
        return this.modManager;
    }

    public void setManager(@NotNull BFAbstractManager<?, ?, ?> manager) {
        this.modManager = manager;
    }
}

