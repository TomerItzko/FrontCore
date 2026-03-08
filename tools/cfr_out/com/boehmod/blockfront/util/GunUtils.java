/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nonnull
 *  javax.annotation.Nullable
 *  net.minecraft.client.Camera
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.resources.sounds.AbstractSoundInstance
 *  net.minecraft.client.resources.sounds.SimpleSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance$Attenuation
 *  net.minecraft.client.sounds.SoundManager
 *  net.minecraft.core.particles.SimpleParticleType
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.Mth
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.levelgen.Heightmap$Types
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.util;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.event.BFRenderHandSubscriber;
import com.boehmod.blockfront.client.event.tick.GunAimingTickable;
import com.boehmod.blockfront.client.event.tick.PlayerTickable;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.geo.gun.MuzzleFlashGunGeoPart;
import com.boehmod.blockfront.client.world.SkyTracker;
import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.common.entity.BotEntity;
import com.boehmod.blockfront.common.gun.GunBarrelType;
import com.boehmod.blockfront.common.gun.GunFireConfig;
import com.boehmod.blockfront.common.gun.GunSoundConfig;
import com.boehmod.blockfront.common.gun.GunTriggerSpawnType;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.net.packet.BFRocketLauncherPacket;
import com.boehmod.blockfront.game.AbstractGameClient;
import com.boehmod.blockfront.registry.BFParticleTypes;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.unnamed.BF_1163;
import com.boehmod.blockfront.unnamed.BF_1212;
import com.boehmod.blockfront.unnamed.BF_441;
import com.boehmod.blockfront.unnamed.BF_623;
import com.boehmod.blockfront.unnamed.BF_946;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.ClientUtils;
import com.boehmod.blockfront.util.PacketUtils;
import com.boehmod.blockfront.util.math.MathUtils;
import java.util.Random;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public class GunUtils {
    private static final float field_1841 = 12.0f;
    private static final float field_1842 = 8.0f;
    private static final float field_1843 = 96.0f;
    private static final float field_1844 = 2.0f;
    public static final float field_6678 = 1.0f;
    private static final float field_1845 = 0.0f;
    private static final float field_1846 = 16.0f;
    private static final int field_1851 = 2;
    private static final int field_6370 = 4;
    private static final int field_1852 = 6;
    private static final float field_1847 = 0.2f;
    private static final float field_1848 = 0.25f;
    public static final float field_1849 = 0.02f;
    public static final int field_6528 = 2;
    public static final float field_6831 = 0.5f;
    public static final float field_7040 = 0.99f;
    public static final float field_7041 = 0.02f;

    public static void method_1424(@NotNull Minecraft minecraft, @NotNull Random random, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull GunItem gunItem, @NotNull LocalPlayer localPlayer, @NotNull ClientLevel clientLevel, @NotNull ItemStack itemStack) {
        int n = GunItem.getAmmoLoaded(itemStack);
        GunItem.setAmmoLoaded(itemStack, n - gunItem.method_4181());
        if (Minecraft.useFancyGraphics()) {
            switch (gunItem.method_4117()) {
                case "default": {
                    MuzzleFlashGunGeoPart.field_1734.add(new BFRenderHandSubscriber.BF_73(random, 4));
                    break;
                }
                case "fire": {
                    MuzzleFlashGunGeoPart.field_1734.add(new BFRenderHandSubscriber.BF_72(4, random));
                }
            }
        }
        GunUtils.method_1422(minecraft, bFClientManager, clientPlayerDataHandler, localPlayer, clientLevel, itemStack, gunItem, localPlayer.position(), true, GunItem.getAmmoLoaded(itemStack) <= 0, false, 0);
        Object object = gunItem.getDefaultFireConfig();
        if (((GunFireConfig)object).method_4023() == GunTriggerSpawnType.ENTITY) {
            PacketUtils.sendToServer(new BFRocketLauncherPacket());
        } else {
            minecraft.crosshairPickEntity = null;
            RandomSource randomSource = clientLevel.random;
            int n2 = (int)clientLevel.getGameTime();
            long l = randomSource.nextLong();
            int n3 = ((GunFireConfig)object).method_4026();
            for (int i = 0; i < n3; ++i) {
                RandomSource randomSource2 = RandomSource.create((long)(l + (long)i));
                new BF_946(minecraft, clientPlayerDataHandler, gunItem.getDamageConfig()).method_5790(bFClientManager, clientLevel, randomSource2, random, localPlayer, gunItem, n2, l + (long)i);
            }
        }
        BF_1163.method_5621(gunItem.getCameraConfig());
        GunItem.field_4057 = 40;
        AbstractGameClient<?, ?> abstractGameClient = bFClientManager.getGameClient();
        if (abstractGameClient != null) {
            float f = gunItem.method_4169();
            switch (gunItem.getBipod()) {
                case ANY: {
                    abstractGameClient.method_2731(f);
                    break;
                }
                case BIPOD_ONLY: {
                    abstractGameClient.method_2731(f * PlayerTickable.field_153);
                    break;
                }
                case NO_BIPOD_ONLY: {
                    abstractGameClient.method_2731(f * (1.0f - PlayerTickable.field_153));
                }
            }
        }
    }

    private static void method_1417(@Nullable BFClientPlayerData bFClientPlayerData, @NotNull GunSoundConfig gunSoundConfig, @NotNull Vec3 vec3, @Nullable Entity entity, boolean bl, @NotNull SoundManager soundManager, @NotNull RandomSource randomSource, int n, float f) {
        Object object;
        BF_1212 bF_1212;
        if (!bl && f < 0.02f) {
            return;
        }
        DeferredHolder<SoundEvent, SoundEvent> deferredHolder = gunSoundConfig.getFire();
        if (deferredHolder == null) {
            return;
        }
        SoundEvent soundEvent = (SoundEvent)deferredHolder.get();
        if (bl) {
            float f2 = 0.99f + 0.02f * randomSource.nextFloat();
            bF_1212 = new BF_1212(soundEvent, SoundSource.MASTER, SoundInstance.Attenuation.NONE, f, f2, randomSource, 0.0, 0.0, 0.0);
        } else {
            bF_1212 = new BF_1212(soundEvent, SoundSource.PLAYERS, SoundInstance.Attenuation.LINEAR, f, 1.0f, randomSource, vec3.x, vec3.y, vec3.z);
        }
        if (bFClientPlayerData != null) {
            BF_1212 bF_12122 = bFClientPlayerData.method_1135();
            if (bF_12122 != null) {
                if (bF_12122 instanceof BF_1212) {
                    object = bF_12122;
                    ((BF_1212)((Object)object)).method_5954();
                } else {
                    soundManager.stop((SoundInstance)bF_12122);
                }
            }
            bFClientPlayerData.method_1142(bF_1212);
        }
        if (entity instanceof BotEntity) {
            BotEntity botEntity = (BotEntity)entity;
            object = botEntity.method_1980();
            if (object != null) {
                if (object instanceof BF_1212) {
                    BF_1212 bF_12123 = (BF_1212)((Object)object);
                    bF_12123.method_5954();
                } else {
                    soundManager.stop((SoundInstance)object);
                }
            }
            botEntity.method_1988((AbstractSoundInstance)bF_1212);
        }
        GunUtils.method_1426(soundManager, (AbstractSoundInstance)bF_1212, bl, n);
    }

    public static void method_1422(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull LocalPlayer localPlayer, @NotNull ClientLevel clientLevel, @NotNull ItemStack itemStack, @NotNull GunItem gunItem, @NotNull Vec3 vec3, boolean bl, boolean bl2, boolean bl3, int n) {
        AbstractVehicleEntity abstractVehicleEntity;
        SoundEvent soundEvent;
        Object object;
        Object object2;
        SoundManager soundManager = minecraft.getSoundManager();
        RandomSource randomSource = RandomSource.create();
        Entity entity = clientLevel.getEntity(n);
        if (entity == null && !bl) {
            return;
        }
        if (bl && !minecraft.options.getCameraType().isFirstPerson()) {
            bl = false;
            entity = minecraft.player;
        }
        if (!bl3 && gunItem.method_4191()) {
            GunUtils.method_1423(minecraft, bFClientManager, localPlayer, clientLevel, entity);
        }
        BFClientPlayerData bFClientPlayerData = null;
        if (entity instanceof Player) {
            object2 = (Player)entity;
            bFClientPlayerData = (BFClientPlayerData)clientPlayerDataHandler.getPlayerData((Player)object2);
            bFClientPlayerData.field_1165 = 1.0f;
            bFClientPlayerData.method_1156(20);
        } else if (bl) {
            bFClientPlayerData = clientPlayerDataHandler.getPlayerData(minecraft);
            bFClientPlayerData.field_1165 = 1.0f;
            bFClientPlayerData.method_1156(20);
        }
        object2 = bFClientManager.getSkyTracker();
        GunSoundConfig gunSoundConfig = gunItem.getSoundConfig(itemStack);
        float f = (float)vec3.distanceTo(localPlayer.getEyePosition());
        int n2 = bl ? 0 : MathUtils.roundDiv5(f);
        float f2 = MathUtils.normalRevClampSlope(0.0f, 16.0f, f);
        float f3 = bl ? 1.0f : 2.0f * f2 * 0.25f;
        float f4 = Math.min(0.8f, MathUtils.normalRevClampSlope(8.0f, 96.0f, f));
        float f5 = 12.0f * f4;
        boolean bl4 = clientLevel.getHeight(Heightmap.Types.MOTION_BLOCKING, (int)vec3.x, (int)vec3.z) > Mth.floor((float)((float)vec3.y + 1.0f));
        boolean bl5 = false;
        if (bl) {
            bl4 = ((SkyTracker)object2).getPercentage() <= 0.4f;
            bl5 = ((SkyTracker)object2).method_5781() >= 0.5f;
            object = gunSoundConfig.getReloadBolt();
            if (object != null) {
                minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)object.get()), (float)1.0f, (float)1.0f));
            }
        }
        if (gunItem.method_4192()) {
            GunUtils.method_1416(minecraft, bFClientPlayerData, gunSoundConfig, localPlayer, vec3, bl, soundManager, randomSource, entity, f3, f5, n2, bl4, bl5);
        }
        GunUtils.method_1417(bFClientPlayerData, gunSoundConfig, vec3, entity, bl, soundManager, randomSource, n2, f3);
        if (entity != null && ((GunBarrelType)(object = gunItem.getBarrelTypeOrDefault(itemStack))).method_4100() && !bl && !bl3) {
            soundEvent = new Vec3(1.0, 0.0, 0.0).zRot(entity.getXRot() * ((float)Math.PI / 180)).yRot(-(entity.getYHeadRot() + 90.0f) * ((float)Math.PI / 180));
            abstractVehicleEntity = entity.getEyePosition().add((Vec3)soundEvent);
            ClientUtils.spawnParticle(minecraft, bFClientManager, clientLevel, (SimpleParticleType)BFParticleTypes.GUN_FLASH.get(), ((Vec3)abstractVehicleEntity).x, ((Vec3)abstractVehicleEntity).y, ((Vec3)abstractVehicleEntity).z, 0.0, 0.0, 0.0);
        }
        if (bl2 && (object = gunSoundConfig.getLastRound()) != null) {
            soundEvent = (SoundEvent)object.get();
            abstractVehicleEntity = bl ? SimpleSoundInstance.forUI((SoundEvent)soundEvent, (float)1.0f, (float)1.0f) : new SimpleSoundInstance(soundEvent, SoundSource.PLAYERS, f3, 1.0f, randomSource, vec3.x, vec3.y, vec3.z);
            soundManager.playDelayed((SoundInstance)abstractVehicleEntity, 1);
        }
        if (entity != null && !bl) {
            gunItem.method_4134(gunSoundConfig, minecraft, clientLevel, vec3, entity.getYRot());
        }
        if ((object = gunSoundConfig.getReloadBolt()) != null && !bl) {
            clientLevel.playLocalSound(vec3.x, vec3.y, vec3.z, (SoundEvent)object.get(), SoundSource.PLAYERS, f3, 1.0f, false);
        }
        if ((soundEvent = localPlayer.getVehicle()) instanceof AbstractVehicleEntity) {
            BF_623 bF_623;
            abstractVehicleEntity = (AbstractVehicleEntity)soundEvent;
            if ((bl || entity != null && soundEvent.hasPassenger(entity)) && (bF_623 = abstractVehicleEntity.method_2324((Entity)(bl ? localPlayer : entity))) != null && bF_623.field_2661) {
                float f6 = 0.8f + 0.4f * randomSource.nextFloat();
                SimpleSoundInstance simpleSoundInstance = SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.ITEM_GUN_SHARED_BULLET_VEHICLE.get()), (float)f6, (float)0.2f);
                soundManager.playDelayed((SoundInstance)simpleSoundInstance, 8);
                DeferredHolder<SoundEvent, SoundEvent> deferredHolder = gunSoundConfig.getEchoCloseIndoorsSelf();
                if (deferredHolder != null) {
                    SimpleSoundInstance simpleSoundInstance2 = SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)deferredHolder.get()), (float)f6, (float)f3);
                    soundManager.play((SoundInstance)simpleSoundInstance2);
                }
            }
        }
    }

    private static void method_1423(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull LocalPlayer localPlayer, @NotNull ClientLevel clientLevel, @Nullable Entity entity) {
        Vec3 vec3 = entity != null ? entity.getEyePosition() : localPlayer.getEyePosition();
        float f = entity != null ? entity.getYHeadRot() : localPlayer.getYHeadRot();
        float f2 = entity != null ? entity.getXRot() : localPlayer.getXRot();
        for (int i = 0; i < 6; ++i) {
            Vec3 vec32 = new Vec3((double)(1.4f + 0.85f * (float)i), 0.0, 0.0).zRot((f2 + 90.0f) * ((float)Math.PI / 180) - 1.5707964f).yRot(-f * ((float)Math.PI / 180) - 1.5707964f);
            Vec3 vec33 = vec3.add(vec32);
            ClientUtils.spawnParticle(minecraft, bFClientManager, clientLevel, (SimpleParticleType)BFParticleTypes.BULLET_IMPACT_SMOKE.get(), vec33.x, vec33.y, vec33.z, 0.0, 0.0, 0.0);
        }
    }

    public static void method_1416(@Nonnull Minecraft minecraft, @Nullable BFClientPlayerData bFClientPlayerData, @NotNull GunSoundConfig gunSoundConfig, @NotNull LocalPlayer localPlayer, @NotNull Vec3 vec3, boolean bl, @NotNull SoundManager soundManager, @NotNull RandomSource randomSource, @Nullable Entity entity, float f, float f2, int n, boolean bl2, boolean bl3) {
        GunUtils.method_1428(entity, bFClientPlayerData, gunSoundConfig, vec3, soundManager, randomSource, f, f2, n, bl, bl2, bl3);
        GunUtils.method_1421(gunSoundConfig, vec3, bl, soundManager, randomSource, f, f2, n);
        GunUtils.method_1427(soundManager, randomSource, vec3, bl, f, n);
        if (bl) {
            GunUtils.method_1425(soundManager, gunSoundConfig, randomSource);
        }
        if (entity != null && !bl) {
            GunUtils.method_1418(minecraft, gunSoundConfig, localPlayer, vec3, soundManager, randomSource, entity, f2, n);
        } else {
            GunUtils.method_1419(gunSoundConfig, soundManager, f, bl, n);
            GunUtils.method_1420(gunSoundConfig, soundManager, randomSource, f, bl, n);
        }
    }

    private static void method_1425(@NotNull SoundManager soundManager, @NotNull GunSoundConfig gunSoundConfig, @NotNull RandomSource randomSource) {
        DeferredHolder<SoundEvent, SoundEvent> deferredHolder = gunSoundConfig.getRattle();
        if (deferredHolder == null) {
            return;
        }
        float f = 0.9f + 0.2f * randomSource.nextFloat();
        soundManager.play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)deferredHolder.get()), (float)f, (float)0.25f));
    }

    private static void method_1427(@NotNull SoundManager soundManager, @NotNull RandomSource randomSource, @NotNull Vec3 vec3, boolean bl, float f, int n) {
        block3: {
            block2: {
                if (!bl) break block2;
                for (int i = 0; i < 2; ++i) {
                    SimpleSoundInstance simpleSoundInstance = SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.ITEM_GUN_SHARED_NOSE_STEREO.get()), (float)1.0f, (float)1.0f);
                    GunUtils.method_1426(soundManager, (AbstractSoundInstance)simpleSoundInstance, true, 0);
                }
                break block3;
            }
            if (!(f >= 0.02f)) break block3;
            for (int i = 0; i < 4; ++i) {
                SimpleSoundInstance simpleSoundInstance = new SimpleSoundInstance((SoundEvent)BFSounds.ITEM_GUN_SHARED_NOSE.get(), SoundSource.PLAYERS, f, 1.0f, randomSource, vec3.x, vec3.y, vec3.z);
                GunUtils.method_1426(soundManager, (AbstractSoundInstance)simpleSoundInstance, false, n);
            }
        }
    }

    private static void method_1428(Entity entity, @Nullable BFClientPlayerData bFClientPlayerData, @NotNull GunSoundConfig gunSoundConfig, @NotNull Vec3 vec3, @NotNull SoundManager soundManager, @NotNull RandomSource randomSource, float f, float f2, int n, boolean bl, boolean bl2, boolean bl3) {
        Object object;
        SimpleSoundInstance simpleSoundInstance;
        SimpleSoundInstance simpleSoundInstance2;
        SimpleSoundInstance simpleSoundInstance3;
        float f3 = 1.0f;
        float f4 = 0.03f * (float)randomSource.nextInt(5);
        float f5 = 0.85f + f4;
        if (!bl && f2 > 0.0f) {
            SimpleSoundInstance simpleSoundInstance4 = simpleSoundInstance3 = bl2 ? gunSoundConfig.getEchoDistantIndoors() : gunSoundConfig.getEchoDistantMono();
            if (simpleSoundInstance3 != null) {
                simpleSoundInstance2 = new SimpleSoundInstance((SoundEvent)simpleSoundInstance3.get(), SoundSource.PLAYERS, f2, f5, randomSource, vec3.x, vec3.y, vec3.z);
                GunUtils.method_1426(soundManager, (AbstractSoundInstance)simpleSoundInstance2, false, n);
            }
            if (!bl2 && (simpleSoundInstance2 = gunSoundConfig.getEchoDistantStereo()) != null) {
                simpleSoundInstance = SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)simpleSoundInstance2.get()), (float)1.0f, (float)(f2 / 12.0f));
                if (bFClientPlayerData != null) {
                    object = bFClientPlayerData.method_1151();
                    if (object != null) {
                        GunAimingTickable.method_167(new BF_441(n, (SoundInstance)object));
                    }
                    bFClientPlayerData.method_1152(simpleSoundInstance);
                }
                GunUtils.method_1426(soundManager, (AbstractSoundInstance)simpleSoundInstance, false, n);
            }
        }
        if (f > 0.02f || bl) {
            DeferredHolder<SoundEvent, SoundEvent> deferredHolder = simpleSoundInstance3 = bl3 ? gunSoundConfig.getEchoCloseIndoorsOpen() : gunSoundConfig.getEchoCloseIndoorsSelf();
            Object object2 = bl2 ? (bl ? simpleSoundInstance3 : gunSoundConfig.getEchoCloseIndoors()) : (simpleSoundInstance2 = bl ? gunSoundConfig.getEchoCloseSelf() : gunSoundConfig.getEchoClose());
            if (simpleSoundInstance2 != null) {
                simpleSoundInstance = new SimpleSoundInstance((SoundEvent)simpleSoundInstance2.get(), SoundSource.PLAYERS, f, f5, randomSource, vec3.x, vec3.y, vec3.z);
                if (bFClientPlayerData != null) {
                    object = bFClientPlayerData.method_1147();
                    if (object != null) {
                        GunAimingTickable.method_167(new BF_441(n, (SoundInstance)object));
                    }
                    bFClientPlayerData.method_1150(simpleSoundInstance);
                }
                if (entity instanceof BotEntity) {
                    object = (BotEntity)entity;
                    SimpleSoundInstance simpleSoundInstance5 = ((BotEntity)object).method_2012();
                    if (simpleSoundInstance5 != null) {
                        GunAimingTickable.method_167(new BF_441(n, (SoundInstance)simpleSoundInstance5));
                    }
                    ((BotEntity)object).method_2018(simpleSoundInstance);
                }
                GunUtils.method_1426(soundManager, (AbstractSoundInstance)simpleSoundInstance, bl, n);
            }
        }
    }

    private static void method_1421(@NotNull GunSoundConfig gunSoundConfig, @NotNull Vec3 vec3, boolean bl, @NotNull SoundManager soundManager, @NotNull RandomSource randomSource, float f, float f2, int n) {
        SoundEvent soundEvent;
        DeferredHolder<SoundEvent, SoundEvent> deferredHolder;
        if (bl && (deferredHolder = gunSoundConfig.getCorebassClose()) != null) {
            soundEvent = (SoundEvent)deferredHolder.get();
            for (int i = 0; i < 2; ++i) {
                GunUtils.method_1426(soundManager, (AbstractSoundInstance)SimpleSoundInstance.forUI((SoundEvent)soundEvent, (float)1.0f, (float)f), true, n);
            }
        }
        if (!bl && f2 > 0.0f && (deferredHolder = gunSoundConfig.getCorebassDistant()) != null) {
            soundEvent = new SimpleSoundInstance((SoundEvent)deferredHolder.get(), SoundSource.PLAYERS, f2, 1.0f, randomSource, vec3.x, vec3.y, vec3.z);
            GunUtils.method_1426(soundManager, (AbstractSoundInstance)soundEvent, false, n);
        }
    }

    private static void method_1419(@NotNull GunSoundConfig gunSoundConfig, @NotNull SoundManager soundManager, float f, boolean bl, int n) {
        if (f <= 0.0f) {
            return;
        }
        DeferredHolder<SoundEvent, SoundEvent> deferredHolder = gunSoundConfig.getLowFreq();
        if (deferredHolder != null) {
            GunUtils.method_1426(soundManager, (AbstractSoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)deferredHolder.get()), (float)1.0f, (float)f), bl, n);
        }
    }

    private static void method_1420(@NotNull GunSoundConfig gunSoundConfig, @NotNull SoundManager soundManager, @NotNull RandomSource randomSource, float f, boolean bl, int n) {
        if (f <= 0.0f) {
            return;
        }
        DeferredHolder<SoundEvent, SoundEvent> deferredHolder = gunSoundConfig.getAdd();
        if (deferredHolder == null) {
            return;
        }
        float f2 = 0.9f + 0.2f * randomSource.nextFloat();
        GunUtils.method_1426(soundManager, (AbstractSoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)deferredHolder.get()), (float)f2, (float)f), bl, n);
    }

    private static void method_1418(@Nonnull Minecraft minecraft, @NotNull GunSoundConfig gunSoundConfig, @NotNull LocalPlayer localPlayer, @NotNull Vec3 vec3, @NotNull SoundManager soundManager, @NotNull RandomSource randomSource, @NotNull Entity entity, float f, int n) {
        if (!Minecraft.useFancyGraphics() || !localPlayer.hasLineOfSight(entity)) {
            return;
        }
        DeferredHolder<SoundEvent, SoundEvent> deferredHolder = gunSoundConfig.getTowards();
        if (deferredHolder == null) {
            return;
        }
        SoundEvent soundEvent = (SoundEvent)deferredHolder.get();
        Camera camera = minecraft.gameRenderer.getMainCamera();
        float f2 = BFUtils.method_5932(entity, camera);
        float f3 = f * f2;
        SimpleSoundInstance simpleSoundInstance = new SimpleSoundInstance(soundEvent, SoundSource.PLAYERS, f3, 1.0f, randomSource, vec3.x, vec3.y, vec3.z);
        GunUtils.method_1426(soundManager, (AbstractSoundInstance)simpleSoundInstance, false, n);
    }

    public static void method_1426(@NotNull SoundManager soundManager, @NotNull AbstractSoundInstance abstractSoundInstance, boolean bl, int n) {
        if (bl || n <= 0) {
            soundManager.play((SoundInstance)abstractSoundInstance);
        } else {
            soundManager.playDelayed((SoundInstance)abstractSoundInstance, n);
        }
    }
}

