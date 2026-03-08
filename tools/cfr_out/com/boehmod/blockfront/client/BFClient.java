/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.LayeredDraw$Layer
 *  net.minecraft.client.renderer.BiomeColors
 *  net.minecraft.client.renderer.DimensionSpecialEffects
 *  net.minecraft.client.renderer.entity.ItemRenderer
 *  net.minecraft.client.renderer.entity.ThrownItemRenderer
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Holder
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.level.BlockAndTintGetter
 *  net.minecraft.world.level.FoliageColor
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.level.dimension.BuiltinDimensionTypes
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
 *  net.neoforged.neoforge.client.event.EntityRenderersEvent$RegisterLayerDefinitions
 *  net.neoforged.neoforge.client.event.EntityRenderersEvent$RegisterRenderers
 *  net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent
 *  net.neoforged.neoforge.client.event.RegisterColorHandlersEvent$Block
 *  net.neoforged.neoforge.client.event.RegisterColorHandlersEvent$Item
 *  net.neoforged.neoforge.client.event.RegisterDimensionSpecialEffectsEvent
 *  net.neoforged.neoforge.client.event.RegisterGuiLayersEvent
 *  net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent
 *  net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent
 *  net.neoforged.neoforge.client.event.RegisterShadersEvent
 *  net.neoforged.neoforge.client.extensions.common.IClientItemExtensions
 *  net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.BFKeyMappings;
import com.boehmod.blockfront.client.gui.layer.CrosshairGuiLayer;
import com.boehmod.blockfront.client.gui.layer.EquipmentGuiLayer;
import com.boehmod.blockfront.client.gui.layer.HealthEffectsGuiLayer;
import com.boehmod.blockfront.client.gui.layer.MatchGuiLayer;
import com.boehmod.blockfront.client.gui.layer.VehicleGuiLayer;
import com.boehmod.blockfront.client.render.block.BigCrateBlockRenderer;
import com.boehmod.blockfront.client.render.block.DecorationBlockRenderer;
import com.boehmod.blockfront.client.render.block.DefaultGeoBlockRenderer;
import com.boehmod.blockfront.client.render.block.SwingingLogBlockRenderer;
import com.boehmod.blockfront.client.render.block.TankBlockRenderer;
import com.boehmod.blockfront.client.render.entity.AcidBallEntityRenderer;
import com.boehmod.blockfront.client.render.entity.AmmoCrateEntityRenderer;
import com.boehmod.blockfront.client.render.entity.BombEntityRenderer;
import com.boehmod.blockfront.client.render.entity.BotRenderer;
import com.boehmod.blockfront.client.render.entity.FlamethowerFireEntityRenderer;
import com.boehmod.blockfront.client.render.entity.HumanEntityRenderer;
import com.boehmod.blockfront.client.render.entity.InfectedDogEntityRenderer;
import com.boehmod.blockfront.client.render.entity.InfectedEntityRenderer;
import com.boehmod.blockfront.client.render.entity.InvisibleEntityRenderer;
import com.boehmod.blockfront.client.render.entity.LandmineEntityRenderer;
import com.boehmod.blockfront.client.render.entity.MedicalBagEntityRenderer;
import com.boehmod.blockfront.client.render.entity.MelonRocketEntityRenderer;
import com.boehmod.blockfront.client.render.entity.NextbotEntityRenderer;
import com.boehmod.blockfront.client.render.entity.PanzerknackerEntityRenderer;
import com.boehmod.blockfront.client.render.entity.RocketEntityRenderer;
import com.boehmod.blockfront.client.render.entity.TankRocketEntityRenderer;
import com.boehmod.blockfront.client.render.entity.VehicleEntityRenderer;
import com.boehmod.blockfront.client.render.entity.VendorEntityRenderer;
import com.boehmod.blockfront.client.settings.BFClientSettingsDisk;
import com.boehmod.blockfront.client.setup.BFClientReloadListenersSetup;
import com.boehmod.blockfront.client.setup.BFClientRenderLayersSetup;
import com.boehmod.blockfront.client.setup.BFClipTextRendererSetup;
import com.boehmod.blockfront.client.setup.BFNameTagRendererSetup;
import com.boehmod.blockfront.client.setup.BFParticleProvidersSetup;
import com.boehmod.blockfront.client.setup.BFShadersSetup;
import com.boehmod.blockfront.client.world.BFOverworldEffects;
import com.boehmod.blockfront.common.block.entity.CrateGunBlockEntity;
import com.boehmod.blockfront.common.item.BinocularsItem;
import com.boehmod.blockfront.registry.BFBlockEntityTypes;
import com.boehmod.blockfront.registry.BFEntityTypes;
import com.boehmod.blockfront.registry.BFItems;
import com.boehmod.blockfront.registry.gen.WoodTypeGenerator;
import com.boehmod.blockfront.util.BFLog;
import com.boehmod.blockfront.util.BFRes;
import java.io.IOException;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid="bf", value={Dist.CLIENT})
public class BFClient {
    @Nullable
    public static BFClientManager getManager() {
        return (BFClientManager)BlockFront.getInstance().getManager();
    }

    public static void init(@NotNull BlockFront bf, @NotNull FMLClientSetupEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        BFClientManager bFClientManager = new BFClientManager(minecraft);
        bf.setManager(bFClientManager);
        event.enqueueWork(() -> BFClient.setup(bFClientManager));
    }

    public static void registerClientExtensions(@Nonnull RegisterClientExtensionsEvent event) {
        event.registerItem((IClientItemExtensions)new BinocularsItem.ClientExtension(), new Holder[]{BFItems.BINOCULARS});
    }

    public static void registerDimensionEffects(@NotNull RegisterDimensionSpecialEffectsEvent event) {
        BFLog.log("Registering dimension special effects...", new Object[0]);
        event.register(BuiltinDimensionTypes.OVERWORLD_EFFECTS, (DimensionSpecialEffects)new BFOverworldEffects());
        BFLog.log("Dimension special effects registration complete!", new Object[0]);
    }

    public static void registerBlockColorHandlers(@NotNull RegisterColorHandlersEvent.Block event) {
        event.register((blockState, blockAndTintGetter, blockPos, n) -> blockAndTintGetter != null && blockPos != null ? BiomeColors.getAverageFoliageColor((BlockAndTintGetter)blockAndTintGetter, (BlockPos)blockPos) : FoliageColor.getDefaultColor(), WoodTypeGenerator.INSTANCES.stream().filter(woodTypeGenerator -> woodTypeGenerator.doDatagen).map(woodTypeGenerator -> (Block)Objects.requireNonNull(woodTypeGenerator.leaves).get()).collect(Collectors.toSet()).toArray(new Block[0]));
    }

    public static void registerItemColorHandlers(@NotNull RegisterColorHandlersEvent.Item event) {
        event.register((itemStack, n) -> FoliageColor.getDefaultColor(), (ItemLike[])WoodTypeGenerator.INSTANCES.stream().filter(woodTypeGenerator -> woodTypeGenerator.doDatagen).map(woodTypeGenerator -> (Block)Objects.requireNonNull(woodTypeGenerator.leaves).get()).collect(Collectors.toSet()).toArray(new Block[0]));
    }

    private static void setup(@NotNull BFClientManager manager) {
        BFLog.log("Entering client setup procedure...", new Object[0]);
        BFClipTextRendererSetup.register();
        BFClientSettingsDisk.read(manager);
        BFClientSettingsDisk.write(manager);
        BFNameTagRendererSetup.register();
        BFLog.log("Client setup procedure complete!", new Object[0]);
    }

    @SubscribeEvent
    public static void registerRenderers(@NotNull EntityRenderersEvent.RegisterRenderers event) {
        BFLog.log("Entering client renderer registry procedure...", new Object[0]);
        Minecraft minecraft = Minecraft.getInstance();
        ItemRenderer itemRenderer = minecraft.getItemRenderer();
        event.registerEntityRenderer((EntityType)BFEntityTypes.GRENADE_FRAG.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer((EntityType)BFEntityTypes.GRENADE_FLASH.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer((EntityType)BFEntityTypes.GRENADE_SMOKE.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer((EntityType)BFEntityTypes.GRENADE_FIRE.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer((EntityType)BFEntityTypes.GRENADE_MOLOTOV.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer((EntityType)BFEntityTypes.GRENADE_DECOY.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer((EntityType)BFEntityTypes.GRENADE_HOLY.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer((EntityType)BFEntityTypes.MEDICAL_BAG.get(), context -> new MedicalBagEntityRenderer(context, itemRenderer, (DeferredHolder<Item, Item>)BFItems.MEDIC_BAG));
        event.registerEntityRenderer((EntityType)BFEntityTypes.AMMO_BOX.get(), context -> new AmmoCrateEntityRenderer(context, itemRenderer, (DeferredHolder<Item, ? extends Item>)BFItems.AMMO_BOX));
        event.registerEntityRenderer((EntityType)BFEntityTypes.LANDMINE.get(), context -> new LandmineEntityRenderer(context, itemRenderer, (DeferredHolder<Item, ? extends Item>)BFItems.LANDMINE));
        event.registerEntityRenderer((EntityType)BFEntityTypes.ROCKET.get(), RocketEntityRenderer::new);
        event.registerEntityRenderer((EntityType)BFEntityTypes.TANK_ROCKET.get(), TankRocketEntityRenderer::new);
        event.registerEntityRenderer((EntityType)BFEntityTypes.AIRSTRIKE_ROCKET.get(), RocketEntityRenderer::new);
        event.registerEntityRenderer((EntityType)BFEntityTypes.PRECISION_AIRSTRIKE_ROCKET.get(), RocketEntityRenderer::new);
        event.registerEntityRenderer((EntityType)BFEntityTypes.MELON_ROCKET.get(), MelonRocketEntityRenderer::new);
        event.registerEntityRenderer((EntityType)BFEntityTypes.ANTI_AIR_ROCKET.get(), RocketEntityRenderer::new);
        event.registerEntityRenderer((EntityType)BFEntityTypes.BOMB.get(), context -> new BombEntityRenderer(context, itemRenderer));
        event.registerEntityRenderer((EntityType)BFEntityTypes.CAMERA.get(), InvisibleEntityRenderer::new);
        event.registerEntityRenderer((EntityType)BFEntityTypes.ACID_BALL.get(), context -> new AcidBallEntityRenderer(context, itemRenderer));
        event.registerEntityRenderer((EntityType)BFEntityTypes.HUMAN.get(), HumanEntityRenderer::new);
        event.registerEntityRenderer((EntityType)BFEntityTypes.BOT.get(), BotRenderer::new);
        event.registerEntityRenderer((EntityType)BFEntityTypes.INFECTED.get(), InfectedEntityRenderer::new);
        event.registerEntityRenderer((EntityType)BFEntityTypes.GUN_DEALER.get(), VendorEntityRenderer::new);
        event.registerEntityRenderer((EntityType)BFEntityTypes.INFECTED_DOG.get(), InfectedDogEntityRenderer::new);
        event.registerEntityRenderer((EntityType)BFEntityTypes.INFECTED_SPITTER.get(), InfectedEntityRenderer::new);
        event.registerEntityRenderer((EntityType)BFEntityTypes.INFECTED_STALKER.get(), InfectedEntityRenderer::new);
        event.registerEntityRenderer((EntityType)BFEntityTypes.FLAME_THROWER_FIRE.get(), FlamethowerFireEntityRenderer::new);
        event.registerEntityRenderer((EntityType)BFEntityTypes.OBUNGA.get(), NextbotEntityRenderer::new);
        event.registerEntityRenderer((EntityType)BFEntityTypes.MICHAEL.get(), NextbotEntityRenderer::new);
        event.registerEntityRenderer((EntityType)BFEntityTypes.PANZERKNACKER.get(), PanzerknackerEntityRenderer::new);
        event.registerEntityRenderer((EntityType)BFEntityTypes.WILLYS_JEEP_CAR.get(), VehicleEntityRenderer::new);
        event.registerEntityRenderer((EntityType)BFEntityTypes.CHIHA_TANK.get(), VehicleEntityRenderer::new);
        event.registerEntityRenderer((EntityType)BFEntityTypes.LANCIA_1ZM_CAR.get(), VehicleEntityRenderer::new);
        event.registerEntityRenderer((EntityType)BFEntityTypes.FLAK88_GUN.get(), VehicleEntityRenderer::new);
        event.registerEntityRenderer((EntityType)BFEntityTypes.TYPE96_GUN.get(), VehicleEntityRenderer::new);
        event.registerEntityRenderer((EntityType)BFEntityTypes.PANZERIV_TANK.get(), VehicleEntityRenderer::new);
        event.registerEntityRenderer((EntityType)BFEntityTypes.SHERMAN_TANK.get(), VehicleEntityRenderer::new);
        event.registerEntityRenderer((EntityType)BFEntityTypes.RENAULT_AHN.get(), VehicleEntityRenderer::new);
        event.registerEntityRenderer((EntityType)BFEntityTypes.CITROEN_11CV_TRACTION_AVANT.get(), VehicleEntityRenderer::new);
        event.registerEntityRenderer((EntityType)BFEntityTypes.TIGER_AUSF_H_TANK.get(), VehicleEntityRenderer::new);
        event.registerEntityRenderer((EntityType)BFEntityTypes.T34_TANK.get(), VehicleEntityRenderer::new);
        event.registerEntityRenderer((EntityType)BFEntityTypes._7TP_PELICAN_TANK.get(), VehicleEntityRenderer::new);
        event.registerEntityRenderer((EntityType)BFEntityTypes.KUBELWAGEN_CAR.get(), VehicleEntityRenderer::new);
        event.registerEntityRenderer((EntityType)BFEntityTypes.RENAULT_FT_TANK.get(), VehicleEntityRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType)BFBlockEntityTypes.GERMAN_RADAR.get(), DefaultGeoBlockRenderer::new);
        for (DeferredHolder<BlockEntityType<?>, BlockEntityType<? extends CrateGunBlockEntity>> deferredHolder : BFBlockEntityTypes.CRATES.values()) {
            event.registerBlockEntityRenderer((BlockEntityType)deferredHolder.get(), BigCrateBlockRenderer::new);
        }
        event.registerBlockEntityRenderer((BlockEntityType)BFBlockEntityTypes.DECORATION_BLOCK.get(), DecorationBlockRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType)BFBlockEntityTypes.SWINGING_LOG.get(), SwingingLogBlockRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType)BFBlockEntityTypes.BICYCLE.get(), DefaultGeoBlockRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType)BFBlockEntityTypes._150MM_TBTSK_C36_GUN.get(), TankBlockRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType)BFBlockEntityTypes.LCVP.get(), TankBlockRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType)BFBlockEntityTypes.LCVP_OPEN.get(), TankBlockRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType)BFBlockEntityTypes.P51_MUSTANG.get(), TankBlockRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType)BFBlockEntityTypes.JUNKERS_JU87.get(), TankBlockRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType)BFBlockEntityTypes.A6M_ZERO.get(), TankBlockRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType)BFBlockEntityTypes.A6M_ZERO_PARKED.get(), TankBlockRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType)BFBlockEntityTypes.REGGIANE_RE2005.get(), TankBlockRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType)BFBlockEntityTypes.SHERMAN_TANK.get(), TankBlockRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType)BFBlockEntityTypes.SHERMAN_TANK_DESTROYED.get(), TankBlockRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType)BFBlockEntityTypes.SHERMAN_TANK_SNORKEL.get(), TankBlockRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType)BFBlockEntityTypes.SHERMAN_TANK_SNORKEL_DESTROYED.get(), TankBlockRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType)BFBlockEntityTypes.TIGER_AUSF_H_TANK.get(), TankBlockRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType)BFBlockEntityTypes.PANZERIV_TANK.get(), TankBlockRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType)BFBlockEntityTypes.PANZERIV_TANK_DESTROYED.get(), TankBlockRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType)BFBlockEntityTypes.CHIHA_TANK.get(), TankBlockRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType)BFBlockEntityTypes.CHIHA_TANK_DESTROYED.get(), TankBlockRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType)BFBlockEntityTypes.KUROGANE.get(), TankBlockRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType)BFBlockEntityTypes.KUBEL.get(), TankBlockRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType)BFBlockEntityTypes.RENAULT_AHN.get(), TankBlockRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType)BFBlockEntityTypes.CITROEN_11CV_TRACTION_AVANT.get(), TankBlockRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType)BFBlockEntityTypes.CITROEN_11CV_TRACTION_AVANT_DESTROYED.get(), TankBlockRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType)BFBlockEntityTypes.LANCIA_1ZM.get(), TankBlockRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType)BFBlockEntityTypes.LANCIA_1ZM_DESTROYED.get(), TankBlockRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType)BFBlockEntityTypes.WILLYS_JEEP.get(), TankBlockRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType)BFBlockEntityTypes.WILLYS_JEEP_DESTROYED.get(), TankBlockRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType)BFBlockEntityTypes.T34_TANK.get(), TankBlockRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType)BFBlockEntityTypes.T34_TANK_DESTROYED.get(), TankBlockRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType)BFBlockEntityTypes._7TP_PELICAN.get(), TankBlockRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType)BFBlockEntityTypes._7TP_PELICAN_DESTROYED.get(), TankBlockRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType)BFBlockEntityTypes.PZL11C.get(), TankBlockRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType)BFBlockEntityTypes.RENAULT_FT_TANK.get(), TankBlockRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType)BFBlockEntityTypes.WALL_FLAG_TALL.get(), DefaultGeoBlockRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType)BFBlockEntityTypes.FLAK88_SHIELD.get(), TankBlockRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType)BFBlockEntityTypes.FLAK88_SHIELD_DESTROYED.get(), TankBlockRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType)BFBlockEntityTypes.FLAK88_NOSHIELD.get(), TankBlockRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType)BFBlockEntityTypes.FLAK88_NOSHIELD_DESTROYED.get(), TankBlockRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType)BFBlockEntityTypes.TYPE96_SHIELD.get(), TankBlockRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType)BFBlockEntityTypes.TYPE96_SHIELD_DESTROYED.get(), TankBlockRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType)BFBlockEntityTypes.TYPE96_NOSHIELD.get(), TankBlockRenderer::new);
        event.registerBlockEntityRenderer((BlockEntityType)BFBlockEntityTypes.TYPE96_NOSHIELD_DESTROYED.get(), TankBlockRenderer::new);
        BFLog.log("Client renderer registry procedure complete!", new Object[0]);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(@NotNull EntityRenderersEvent.RegisterLayerDefinitions event) {
        BFLog.log("Entering client renderer layers registry procedure...", new Object[0]);
        BFClientRenderLayersSetup.register(event);
        BFLog.log("Client renderer layers registry procedure complete!", new Object[0]);
    }

    @SubscribeEvent
    public static void registerGuiLayers(@NotNull RegisterGuiLayersEvent event) {
        BFLog.log("Entering overlays registry procedure...", new Object[0]);
        event.registerBelowAll(BFRes.loc("mod_health_effects"), (LayeredDraw.Layer)new HealthEffectsGuiLayer());
        event.registerBelowAll(BFRes.loc("mod_vehicle"), (LayeredDraw.Layer)new VehicleGuiLayer());
        event.registerBelowAll(BFRes.loc("mod_client_game"), (LayeredDraw.Layer)new MatchGuiLayer());
        event.registerBelowAll(BFRes.loc("mod_equipment"), (LayeredDraw.Layer)new EquipmentGuiLayer());
        event.registerBelowAll(BFRes.loc("mod_crosshair"), (LayeredDraw.Layer)new CrosshairGuiLayer());
        BFLog.log("Client overlays registry procedure complete!", new Object[0]);
    }

    @SubscribeEvent
    public static void registerClientReloadListeners(@NotNull RegisterClientReloadListenersEvent event) {
        BFLog.log("Entering reload listener registry procedure...", new Object[0]);
        BFClientReloadListenersSetup.register(event);
        BFLog.log("Reload listener registry procedure complete!", new Object[0]);
    }

    @SubscribeEvent
    public static void registerKeyMappings(@NotNull RegisterKeyMappingsEvent event) {
        BFLog.log("Entering key mappings registry procedure...", new Object[0]);
        BFKeyMappings.register(event);
        BFLog.log("Client key mappings registry procedure complete!", new Object[0]);
    }

    @SubscribeEvent
    public static void registerParticleProviders(@NotNull RegisterParticleProvidersEvent event) {
        BFLog.log("Entering particle providers registry procedure...", new Object[0]);
        BFParticleProvidersSetup.register(event);
        BFLog.log("Client particle providers registry procedure complete!", new Object[0]);
    }

    @SubscribeEvent
    public static void registerShaders(@NotNull RegisterShadersEvent event) throws IOException {
        BFLog.log("Entering shaders registry procedure...", new Object[0]);
        BFShadersSetup.register(event);
        BFLog.log("Client shaders registry procedure complete!", new Object[0]);
    }
}

