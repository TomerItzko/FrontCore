/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.EntityType$Builder
 *  net.minecraft.world.entity.MobCategory
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  net.neoforged.neoforge.registries.DeferredRegister
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.registry;

import com.boehmod.blockfront.common.entity.AcidBallEntity;
import com.boehmod.blockfront.common.entity.AirstrikeRocketEntity;
import com.boehmod.blockfront.common.entity.AmmoCrateEntity;
import com.boehmod.blockfront.common.entity.AntiAirRocketEntity;
import com.boehmod.blockfront.common.entity.BombEntity;
import com.boehmod.blockfront.common.entity.BotEntity;
import com.boehmod.blockfront.common.entity.CameraEntity;
import com.boehmod.blockfront.common.entity.ChihaTankEntity;
import com.boehmod.blockfront.common.entity.CitroenTractionAvantEntity;
import com.boehmod.blockfront.common.entity.DecoyGrenadeEntity;
import com.boehmod.blockfront.common.entity.FireGrenadeEntity;
import com.boehmod.blockfront.common.entity.FlakGunEntity;
import com.boehmod.blockfront.common.entity.FlamethrowerFireEntity;
import com.boehmod.blockfront.common.entity.FlashGrenadeEntity;
import com.boehmod.blockfront.common.entity.GrenadeEntity;
import com.boehmod.blockfront.common.entity.HolyGrenadeEntity;
import com.boehmod.blockfront.common.entity.HumanEntity;
import com.boehmod.blockfront.common.entity.InfectedDogEntity;
import com.boehmod.blockfront.common.entity.InfectedEntity;
import com.boehmod.blockfront.common.entity.InfectedSpitterEntity;
import com.boehmod.blockfront.common.entity.InfectedStalkerEntity;
import com.boehmod.blockfront.common.entity.KubelwagenCarEntity;
import com.boehmod.blockfront.common.entity.LanciaCarEntity;
import com.boehmod.blockfront.common.entity.LandmineEntity;
import com.boehmod.blockfront.common.entity.MedicalBagEntity;
import com.boehmod.blockfront.common.entity.MelonRocketEntity;
import com.boehmod.blockfront.common.entity.MichaelNextbotEntity;
import com.boehmod.blockfront.common.entity.MolotovEntity;
import com.boehmod.blockfront.common.entity.ObungaNextbotEntity;
import com.boehmod.blockfront.common.entity.PanzerIvTankEntity;
import com.boehmod.blockfront.common.entity.PanzerknackerEntity;
import com.boehmod.blockfront.common.entity.PelicanTankEntity;
import com.boehmod.blockfront.common.entity.PrecisionAirstrikeRocketEntity;
import com.boehmod.blockfront.common.entity.RenaultAhnEntity;
import com.boehmod.blockfront.common.entity.RenaultFtTankEntity;
import com.boehmod.blockfront.common.entity.RocketEntity;
import com.boehmod.blockfront.common.entity.ShermanTankEntity;
import com.boehmod.blockfront.common.entity.SmokeGrenadeEntity;
import com.boehmod.blockfront.common.entity.T34TankEntity;
import com.boehmod.blockfront.common.entity.TankRocketEntity;
import com.boehmod.blockfront.common.entity.TigerAusfTankEntity;
import com.boehmod.blockfront.common.entity.Type96GunEntity;
import com.boehmod.blockfront.common.entity.VendorEntity;
import com.boehmod.blockfront.common.entity.WillysJeepCarEntity;
import com.boehmod.blockfront.util.BFRes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

public final class BFEntityTypes {
    @NotNull
    public static final DeferredRegister<EntityType<?>> DR = DeferredRegister.create((ResourceKey)Registries.ENTITY_TYPE, (String)"bf");
    public static final String field_4974 = "grenade_frag";
    public static final String field_4975 = "grenade_flash";
    public static final String field_4976 = "grenade_smoke";
    public static final String field_4977 = "grenade_fire";
    public static final String field_4978 = "grenade_molotov";
    public static final String field_4948 = "grenade_decoy";
    public static final String field_4949 = "grenade_holy";
    public static final String field_4950 = "medical_bag";
    public static final String field_4951 = "ammo_box";
    public static final String field_4952 = "landmine";
    public static final String field_4953 = "bomb";
    public static final String field_4955 = "camera";
    public static final String field_4956 = "rocket";
    public static final String field_4957 = "tank_rocket";
    public static final String field_4958 = "airstrike_rocket";
    public static final String field_4959 = "precision_airstrike_rocket";
    public static final String field_4960 = "anti_air_rocket";
    public static final String field_4961 = "melon_rocket";
    public static final String field_4962 = "human";
    public static final String field_4963 = "bot";
    public static final String field_4964 = "infected";
    public static final String field_4965 = "gun_dealer";
    public static final String field_4966 = "infected_dog";
    public static final String field_4967 = "infected_spitter";
    public static final String field_4968 = "infected_stalker";
    public static final String field_4969 = "obunga";
    public static final String field_4970 = "michael";
    public static final String field_4971 = "panzerknacker";
    public static final String field_4972 = "acid_ball";
    public static final String field_4973 = "flame_thrower_fire";
    public static final String field_4979 = "sherman_tank";
    public static final String field_4980 = "renault_ahn";
    public static final String field_4981 = "citroen_11cv_traction_avant";
    public static final String field_4982 = "tiger_ausf_h_tank";
    public static final String field_4983 = "t34_tank";
    public static final String field_4984 = "chiha_tank";
    public static final String field_4985 = "lancia_1zm_car";
    public static final String field_4986 = "flak88_gun";
    public static final String field_4987 = "type96_gun";
    public static final String field_4988 = "panzeriv_tank";
    public static final String field_4989 = "7tp_pelican_tank";
    public static final String field_4990 = "willys_jeep_car";
    public static final String field_4991 = "kubelwagen_car";
    public static final String field_6961 = "renault_ft_tank";
    public static final DeferredHolder<EntityType<?>, EntityType<CameraEntity>> CAMERA = DR.register("camera", () -> EntityType.Builder.of(CameraEntity::new, (MobCategory)MobCategory.MISC).sized(0.0f, 0.0f).setTrackingRange(10).setUpdateInterval(20).noSave().noSummon().build(BFRes.loc(field_4955).toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<BombEntity>> BOMB = DR.register("bomb", () -> EntityType.Builder.of(BombEntity::new, (MobCategory)MobCategory.MISC).sized(0.5f, 0.2f).setTrackingRange(10).setUpdateInterval(Integer.MAX_VALUE).build(BFRes.loc(field_4953).toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<VendorEntity>> GUN_DEALER = DR.register("gun_dealer", () -> EntityType.Builder.of(VendorEntity::new, (MobCategory)MobCategory.MISC).sized(0.6f, 1.8f).setTrackingRange(64).setUpdateInterval(40).build(BFRes.loc(field_4965).toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<FlamethrowerFireEntity>> FLAME_THROWER_FIRE = DR.register("flame_thrower_fire", () -> EntityType.Builder.of(FlamethrowerFireEntity::new, (MobCategory)MobCategory.MISC).sized(0.1f, 0.1f).setTrackingRange(10).setUpdateInterval(4).noSave().noSummon().build(BFRes.loc(field_4973).toString()));
    private static final int field_5037 = 4;
    private static final int field_5038 = 8;
    public static final DeferredHolder<EntityType<?>, EntityType<BotEntity>> BOT = DR.register("bot", () -> EntityType.Builder.of(BotEntity::new, (MobCategory)MobCategory.MISC).sized(0.6f, 1.8f).setTrackingRange(64).clientTrackingRange(8).build(BFRes.loc(field_4963).toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<ObungaNextbotEntity>> OBUNGA = DR.register("obunga", () -> EntityType.Builder.of(ObungaNextbotEntity::new, (MobCategory)MobCategory.MISC).sized(0.4f, 0.4f).setTrackingRange(64).clientTrackingRange(8).build(BFRes.loc(field_4969).toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<MichaelNextbotEntity>> MICHAEL = DR.register("michael", () -> EntityType.Builder.of(MichaelNextbotEntity::new, (MobCategory)MobCategory.MISC).sized(0.6f, 1.8f).setTrackingRange(64).clientTrackingRange(8).build(BFRes.loc(field_4970).toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<PanzerknackerEntity>> PANZERKNACKER = DR.register("panzerknacker", () -> EntityType.Builder.of(PanzerknackerEntity::new, (MobCategory)MobCategory.MISC).sized(0.6f, 1.8f).setTrackingRange(64).clientTrackingRange(8).build(BFRes.loc(field_4971).toString()));
    private static final int field_5039 = 2;
    public static final DeferredHolder<EntityType<?>, EntityType<GrenadeEntity>> GRENADE_FRAG = DR.register("grenade_frag", () -> EntityType.Builder.of(GrenadeEntity::new, (MobCategory)MobCategory.MISC).sized(0.4f, 0.4f).setTrackingRange(10).noSave().clientTrackingRange(4).setUpdateInterval(2).build(BFRes.loc(field_4974).toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<FlashGrenadeEntity>> GRENADE_FLASH = DR.register("grenade_flash", () -> EntityType.Builder.of(FlashGrenadeEntity::new, (MobCategory)MobCategory.MISC).sized(0.4f, 0.4f).setTrackingRange(10).noSave().clientTrackingRange(4).setUpdateInterval(2).build(BFRes.loc(field_4975).toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<SmokeGrenadeEntity>> GRENADE_SMOKE = DR.register("grenade_smoke", () -> EntityType.Builder.of(SmokeGrenadeEntity::new, (MobCategory)MobCategory.MISC).sized(0.4f, 0.4f).setTrackingRange(10).noSave().clientTrackingRange(4).setUpdateInterval(2).build(BFRes.loc(field_4976).toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<FireGrenadeEntity>> GRENADE_FIRE = DR.register("grenade_fire", () -> EntityType.Builder.of(FireGrenadeEntity::new, (MobCategory)MobCategory.MISC).sized(0.4f, 0.4f).setTrackingRange(10).noSave().clientTrackingRange(4).setUpdateInterval(2).build(BFRes.loc(field_4977).toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<MolotovEntity>> GRENADE_MOLOTOV = DR.register("grenade_molotov", () -> EntityType.Builder.of(MolotovEntity::new, (MobCategory)MobCategory.MISC).sized(0.4f, 0.4f).setTrackingRange(10).noSave().clientTrackingRange(4).setUpdateInterval(2).build(BFRes.loc(field_4978).toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<DecoyGrenadeEntity>> GRENADE_DECOY = DR.register("grenade_decoy", () -> EntityType.Builder.of(DecoyGrenadeEntity::new, (MobCategory)MobCategory.MISC).sized(0.4f, 0.4f).setTrackingRange(10).noSave().clientTrackingRange(4).setUpdateInterval(2).build(BFRes.loc(field_4948).toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<HolyGrenadeEntity>> GRENADE_HOLY = DR.register("grenade_holy", () -> EntityType.Builder.of(HolyGrenadeEntity::new, (MobCategory)MobCategory.MISC).sized(0.4f, 0.4f).setTrackingRange(10).noSave().clientTrackingRange(4).setUpdateInterval(2).build(BFRes.loc(field_4949).toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<MedicalBagEntity>> MEDICAL_BAG = DR.register("medical_bag", () -> EntityType.Builder.of(MedicalBagEntity::new, (MobCategory)MobCategory.MISC).sized(0.4f, 0.4f).setTrackingRange(10).clientTrackingRange(4).setUpdateInterval(2).build(BFRes.loc(field_4950).toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<AmmoCrateEntity>> AMMO_BOX = DR.register("ammo_box", () -> EntityType.Builder.of(AmmoCrateEntity::new, (MobCategory)MobCategory.MISC).sized(0.4f, 0.4f).setTrackingRange(10).clientTrackingRange(4).setUpdateInterval(2).build(BFRes.loc(field_4951).toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<LandmineEntity>> LANDMINE = DR.register("landmine", () -> EntityType.Builder.of(LandmineEntity::new, (MobCategory)MobCategory.MISC).sized(0.5f, 0.3f).setTrackingRange(10).clientTrackingRange(4).setUpdateInterval(2).build(BFRes.loc(field_4952).toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<AcidBallEntity>> ACID_BALL = DR.register("acid_ball", () -> EntityType.Builder.of(AcidBallEntity::new, (MobCategory)MobCategory.MISC).sized(0.2f, 0.2f).setTrackingRange(10).clientTrackingRange(4).setUpdateInterval(2).build(BFRes.loc(field_4972).toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<RocketEntity>> ROCKET = DR.register("rocket", () -> EntityType.Builder.of(RocketEntity::new, (MobCategory)MobCategory.MISC).sized(0.4f, 0.4f).setTrackingRange(10).noSave().clientTrackingRange(4).setUpdateInterval(2).build(BFRes.loc(field_4956).toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<TankRocketEntity>> TANK_ROCKET = DR.register("tank_rocket", () -> EntityType.Builder.of(TankRocketEntity::new, (MobCategory)MobCategory.MISC).sized(0.4f, 0.4f).setTrackingRange(10).noSave().clientTrackingRange(4).setUpdateInterval(2).build(BFRes.loc(field_4957).toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<MelonRocketEntity>> MELON_ROCKET = DR.register("melon_rocket", () -> EntityType.Builder.of(MelonRocketEntity::new, (MobCategory)MobCategory.MISC).sized(0.2f, 0.2f).clientTrackingRange(4).updateInterval(20).noSave().noSummon().clientTrackingRange(4).setUpdateInterval(2).build(BFRes.loc(field_4961).toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<AirstrikeRocketEntity>> AIRSTRIKE_ROCKET = DR.register("airstrike_rocket", () -> EntityType.Builder.of(AirstrikeRocketEntity::new, (MobCategory)MobCategory.MISC).sized(0.4f, 0.4f).setTrackingRange(10).noSave().noSummon().clientTrackingRange(4).setUpdateInterval(2).build(BFRes.loc(field_4958).toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<PrecisionAirstrikeRocketEntity>> PRECISION_AIRSTRIKE_ROCKET = DR.register("precision_airstrike_rocket", () -> EntityType.Builder.of(PrecisionAirstrikeRocketEntity::new, (MobCategory)MobCategory.MISC).sized(0.4f, 0.4f).setTrackingRange(10).noSave().noSummon().clientTrackingRange(4).setUpdateInterval(2).build(BFRes.loc(field_4959).toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<AntiAirRocketEntity>> ANTI_AIR_ROCKET = DR.register("anti_air_rocket", () -> EntityType.Builder.of(AntiAirRocketEntity::new, (MobCategory)MobCategory.MISC).sized(0.4f, 0.4f).setTrackingRange(10).noSave().noSummon().clientTrackingRange(4).setUpdateInterval(2).build(BFRes.loc(field_4960).toString()));
    private static final int field_5040 = 2;
    public static final DeferredHolder<EntityType<?>, EntityType<HumanEntity>> HUMAN = DR.register("human", () -> EntityType.Builder.of(HumanEntity::new, (MobCategory)MobCategory.MISC).sized(0.6f, 1.8f).setTrackingRange(64).clientTrackingRange(8).setUpdateInterval(2).build(BFRes.loc(field_4962).toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<InfectedEntity>> INFECTED = DR.register("infected", () -> EntityType.Builder.of(InfectedEntity::new, (MobCategory)MobCategory.MISC).sized(0.6f, 1.8f).setTrackingRange(64).clientTrackingRange(8).setUpdateInterval(2).build(BFRes.loc(field_4964).toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<InfectedDogEntity>> INFECTED_DOG = DR.register("infected_dog", () -> EntityType.Builder.of(InfectedDogEntity::new, (MobCategory)MobCategory.MISC).sized(0.6f, 1.5f).setTrackingRange(64).clientTrackingRange(8).setUpdateInterval(2).build(BFRes.loc(field_4966).toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<InfectedSpitterEntity>> INFECTED_SPITTER = DR.register("infected_spitter", () -> EntityType.Builder.of(InfectedSpitterEntity::new, (MobCategory)MobCategory.MISC).sized(0.6f, 1.5f).setTrackingRange(64).clientTrackingRange(8).setUpdateInterval(2).build(BFRes.loc(field_4967).toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<InfectedStalkerEntity>> INFECTED_STALKER = DR.register("infected_stalker", () -> EntityType.Builder.of(InfectedStalkerEntity::new, (MobCategory)MobCategory.MISC).sized(0.6f, 1.5f).setTrackingRange(64).clientTrackingRange(8).setUpdateInterval(2).build(BFRes.loc(field_4968).toString()));
    private static final int field_5041 = 1;
    private static final int field_5042 = 10;
    public static final DeferredHolder<EntityType<?>, EntityType<ShermanTankEntity>> SHERMAN_TANK = DR.register("sherman_tank", () -> EntityType.Builder.of(ShermanTankEntity::new, (MobCategory)MobCategory.MISC).sized(2.5f, 2.5f).setUpdateInterval(1).clientTrackingRange(10).build(BFRes.loc(field_4979).toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<RenaultFtTankEntity>> RENAULT_FT_TANK = DR.register("renault_ft_tank", () -> EntityType.Builder.of(RenaultFtTankEntity::new, (MobCategory)MobCategory.MISC).sized(2.5f, 2.5f).setUpdateInterval(1).clientTrackingRange(10).build(BFRes.loc(field_6961).toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<RenaultAhnEntity>> RENAULT_AHN = DR.register("renault_ahn", () -> EntityType.Builder.of(RenaultAhnEntity::new, (MobCategory)MobCategory.MISC).sized(2.5f, 2.5f).setUpdateInterval(1).clientTrackingRange(10).build(BFRes.loc(field_4980).toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<CitroenTractionAvantEntity>> CITROEN_11CV_TRACTION_AVANT = DR.register("citroen_11cv_traction_avant", () -> EntityType.Builder.of(CitroenTractionAvantEntity::new, (MobCategory)MobCategory.MISC).sized(2.5f, 2.5f).setUpdateInterval(1).clientTrackingRange(10).build(BFRes.loc(field_4981).toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<TigerAusfTankEntity>> TIGER_AUSF_H_TANK = DR.register("tiger_ausf_h_tank", () -> EntityType.Builder.of(TigerAusfTankEntity::new, (MobCategory)MobCategory.MISC).sized(2.75f, 2.5f).setUpdateInterval(1).clientTrackingRange(10).build(BFRes.loc(field_4979).toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<T34TankEntity>> T34_TANK = DR.register("t34_tank", () -> EntityType.Builder.of(T34TankEntity::new, (MobCategory)MobCategory.MISC).sized(2.5f, 2.5f).setUpdateInterval(1).clientTrackingRange(10).build(BFRes.loc(field_4983).toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<ChihaTankEntity>> CHIHA_TANK = DR.register("chiha_tank", () -> EntityType.Builder.of(ChihaTankEntity::new, (MobCategory)MobCategory.MISC).sized(2.5f, 2.5f).setUpdateInterval(1).clientTrackingRange(10).build(BFRes.loc(field_4984).toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<LanciaCarEntity>> LANCIA_1ZM_CAR = DR.register("lancia_1zm_car", () -> EntityType.Builder.of(LanciaCarEntity::new, (MobCategory)MobCategory.MISC).sized(2.5f, 2.5f).setUpdateInterval(1).clientTrackingRange(10).build(BFRes.loc(field_4985).toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<FlakGunEntity>> FLAK88_GUN = DR.register("flak88_gun", () -> EntityType.Builder.of(FlakGunEntity::new, (MobCategory)MobCategory.MISC).sized(2.5f, 2.5f).setUpdateInterval(1).clientTrackingRange(10).build(BFRes.loc(field_4986).toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<Type96GunEntity>> TYPE96_GUN = DR.register("type96_gun", () -> EntityType.Builder.of(Type96GunEntity::new, (MobCategory)MobCategory.MISC).sized(2.5f, 2.5f).setUpdateInterval(1).clientTrackingRange(10).build(BFRes.loc(field_4987).toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<PanzerIvTankEntity>> PANZERIV_TANK = DR.register("panzeriv_tank", () -> EntityType.Builder.of(PanzerIvTankEntity::new, (MobCategory)MobCategory.MISC).sized(2.5f, 2.5f).setUpdateInterval(1).clientTrackingRange(10).build(BFRes.loc(field_4988).toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<PelicanTankEntity>> _7TP_PELICAN_TANK = DR.register("7tp_pelican_tank", () -> EntityType.Builder.of(PelicanTankEntity::new, (MobCategory)MobCategory.MISC).sized(2.5f, 2.5f).setUpdateInterval(1).clientTrackingRange(10).build(BFRes.loc(field_4979).toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<WillysJeepCarEntity>> WILLYS_JEEP_CAR = DR.register("willys_jeep_car", () -> EntityType.Builder.of(WillysJeepCarEntity::new, (MobCategory)MobCategory.MISC).sized(2.0f, 1.25f).setUpdateInterval(1).clientTrackingRange(10).build(BFRes.loc(field_4990).toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<KubelwagenCarEntity>> KUBELWAGEN_CAR = DR.register("kubelwagen_car", () -> EntityType.Builder.of(KubelwagenCarEntity::new, (MobCategory)MobCategory.MISC).sized(2.0f, 1.25f).setUpdateInterval(1).clientTrackingRange(10).build(BFRes.loc(field_4991).toString()));
}

