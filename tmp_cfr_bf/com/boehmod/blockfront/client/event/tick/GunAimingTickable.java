/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.event.tick;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.event.BFRenderFrameSubscriber;
import com.boehmod.blockfront.client.event.BFRenderHandSubscriber;
import com.boehmod.blockfront.client.event.tick.ClientTickable;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.geo.gun.EjectGunGeoPart;
import com.boehmod.blockfront.client.settings.BFClientSettings;
import com.boehmod.blockfront.client.world.ShakeManager;
import com.boehmod.blockfront.common.gun.GunScopeConfig;
import com.boehmod.blockfront.common.gun.GunSoundConfig;
import com.boehmod.blockfront.common.item.BinocularsItem;
import com.boehmod.blockfront.common.item.GrenadeFragItem;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.item.MedicalSyringeItem;
import com.boehmod.blockfront.common.item.MeleeItem;
import com.boehmod.blockfront.common.item.RadioItem;
import com.boehmod.blockfront.common.item.base.IHasWeight;
import com.boehmod.blockfront.common.net.packet.BFAimingPacket;
import com.boehmod.blockfront.common.player.BFAbstractPlayerData;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.unnamed.BF_441;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.PacketUtils;
import com.boehmod.blockfront.util.math.MathUtils;
import com.boehmod.blockfront.util.math.ShakeNodeData;
import com.boehmod.blockfront.util.math.ShakeNodePresets;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.lang.runtime.SwitchBootstraps;
import java.util.Objects;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public final class GunAimingTickable
extends ClientTickable {
    @NotNull
    private static final ObjectList<BF_441> field_177 = new ObjectArrayList();
    public static final float field_164 = 1.0f;
    private static final float field_165 = 0.0f;
    private static final double field_178 = 0.6;
    private static final float field_166 = 3.0f;
    public static final int field_6916 = 10;
    public static double field_179 = 0.0;
    public static boolean field_174 = false;
    public static float field_175;
    public static float field_176;
    public static float field_167;
    public static float field_168;
    public static float field_172;
    public static float field_173;
    @Nullable
    private static ItemStack field_170;
    private static float field_169;
    private static float field_171;

    public static void method_167(@NotNull BF_441 bF_441) {
        field_177.add((Object)bF_441);
    }

    private static void method_171(@NotNull ItemStack itemStack, boolean bl) {
        if (GunItem.field_4055 > 0) {
            --GunItem.field_4055;
        }
        float f = bl && GunItem.isReloading(itemStack) ? 1.0f : 0.0f;
        field_176 = field_175;
        field_175 = Mth.lerp((float)0.15f, (float)field_175, (float)f);
    }

    private static void method_166(@NotNull BFAbstractPlayerData<?, ?, ?, ?> bFAbstractPlayerData) {
        if (GunItem.field_4057 > 0) {
            --GunItem.field_4057;
        }
        if (GunItem.field_4056++ >= 5) {
            boolean bl;
            GunItem.field_4056 = 0;
            boolean bl2 = bl = GunItem.field_4019 || GunItem.field_4057 > 0;
            if (bFAbstractPlayerData.method_842() != bl) {
                PacketUtils.sendToServer(new BFAimingPacket(GunItem.field_4019 || GunItem.field_4057 > 0));
            }
        }
    }

    @Override
    public void run(@NotNull ClientTickEvent.Post event, @NotNull Random random, @NotNull Minecraft minecraft, @NotNull ClientPlayerDataHandler dataHandler, @NotNull BFClientManager manager, @Nullable LocalPlayer player, @Nullable ClientLevel level, @NotNull BFClientPlayerData playerData, @NotNull PlayerCloudData cloudData, @NotNull Vec3 pos, @NotNull BlockPos blockPos, @Nullable AbstractGame<?, ?, ?> game, boolean bl, float renderTime) {
        float f;
        boolean bl2;
        GunItem gunItem;
        if (player == null || level == null) {
            return;
        }
        SoundManager soundManager = minecraft.getSoundManager();
        field_177.removeIf(bF_441 -> bF_441.method_1544(soundManager));
        ItemStack itemStack = player.getMainHandItem();
        Item item = itemStack.getItem();
        GunItem gunItem2 = item instanceof GunItem ? (gunItem = (GunItem)item) : null;
        boolean bl3 = bl2 = gunItem2 != null;
        if (gunItem2 != null) {
            gunItem2.method_4140(minecraft, manager, dataHandler, player, level, itemStack, playerData);
        }
        if (!bl2) {
            BFRenderHandSubscriber.field_359 = Mth.lerp((float)0.5f, (float)BFRenderHandSubscriber.field_359, (float)0.0f);
            BFRenderHandSubscriber.field_360 = Mth.lerp((float)0.5f, (float)BFRenderHandSubscriber.field_360, (float)0.0f);
            BFRenderHandSubscriber.field_361 = Mth.lerp((float)0.5f, (float)BFRenderHandSubscriber.field_361, (float)0.0f);
        }
        EjectGunGeoPart.method_1262();
        GunAimingTickable.method_166(playerData);
        if (minecraft.screen != null) {
            GunItem.field_4019 = false;
        }
        ItemStack itemStack2 = field_170;
        field_170 = itemStack;
        if (itemStack2 != null && !field_170.isEmpty() && field_170.getItem() != itemStack2.getItem()) {
            this.method_168(minecraft, field_170, field_170.getItem(), random);
            GunItem.field_4019 = false;
            return;
        }
        if (GunItem.field_4019 && gunItem2 != null) {
            if (!field_174) {
                field_174 = true;
                field_179 = (Double)minecraft.options.sensitivity().get();
                float f2 = gunItem2.getScopeConfig((ItemStack)itemStack).field_3843 != null ? BFClientSettings.CROSSHAIR_SCOPE_SENS.getValue() : BFClientSettings.CROSSHAIR_AIM_SENS.getValue();
                minecraft.options.sensitivity().set((Object)((Double)minecraft.options.sensitivity().get() * (double)f2));
            }
        } else if (field_174) {
            field_174 = false;
            minecraft.options.sensitivity().set((Object)field_179);
            minecraft.options.save();
        }
        field_168 = field_167;
        if (gunItem2 != null) {
            GunScopeConfig gunScopeConfig = gunItem2.getScopeConfig(itemStack);
            field_167 = MathUtils.moveTowards(field_167, GunItem.field_4019 ? 1.0f : 0.0f, gunScopeConfig.field_3852);
            f = field_167 / gunScopeConfig.field_3851;
            double d = 1.0 - 0.6 * (double)f;
            if (gunScopeConfig.field_3845 && player.onGround()) {
                player.setDeltaMovement(player.getDeltaMovement().multiply(d, 1.0, d));
            }
        } else {
            field_167 = 0.0f;
        }
        float f3 = player.getXRot();
        f = player.getYRot();
        float f4 = MathUtils.wrapDegrees(field_169 - f3);
        float f5 = MathUtils.wrapDegrees(field_171 - f);
        field_169 = f3;
        field_171 = f;
        field_173 = field_172;
        if (!BFUtils.isPlayerUnavailable((Player)player, playerData)) {
            float f6 = Mth.abs((float)(f4 * 0.005f)) + Mth.abs((float)(f5 * 0.005f));
            field_172 += f6;
        }
        field_172 = Mth.lerp((float)0.2f, (float)field_172, (float)0.0f);
        GunItem.field_4042 = GunItem.field_4041;
        GunItem.field_4041 = Mth.lerp((float)0.4f, (float)GunItem.field_4041, (float)(gunItem2 != null && player.isCrouching() && !GunItem.field_4019 ? 1.0f : 0.0f));
        if (playerData.isOutOfGame()) {
            minecraft.hitResult = player.pick(0.0, 0.0f, false);
        }
        if (item instanceof IHasWeight) {
            IHasWeight iHasWeight = (IHasWeight)item;
            this.method_169(player, itemStack, iHasWeight);
        }
        if (player.isSprinting()) {
            GunItem.field_4019 = false;
        }
        GunAimingTickable.method_171(itemStack, gunItem2 != null);
    }

    public void method_168(@NotNull Minecraft minecraft, @NotNull ItemStack itemStack, @NotNull Item item, @NotNull Random random) {
        SoundEvent soundEvent = null;
        ShakeNodeData shakeNodeData = null;
        Item item2 = item;
        Objects.requireNonNull(item2);
        Item item3 = item2;
        int n = 0;
        switch (SwitchBootstraps.typeSwitch("typeSwitch", new Object[]{GunItem.class, RadioItem.class, MeleeItem.class, GrenadeFragItem.class, MedicalSyringeItem.class, BinocularsItem.class}, (Object)item3, n)) {
            case 0: {
                GunItem gunItem = (GunItem)item3;
                gunItem.method_4230(itemStack);
                GunSoundConfig gunSoundConfig = gunItem.getSoundConfig(itemStack);
                DeferredHolder<SoundEvent, SoundEvent> deferredHolder = gunSoundConfig.getEquip();
                if (deferredHolder != null) {
                    soundEvent = (SoundEvent)deferredHolder.get();
                }
                shakeNodeData = ShakeNodePresets.field_1924;
                break;
            }
            case 1: {
                RadioItem radioItem = (RadioItem)item3;
                radioItem.method_3782("equip");
                soundEvent = (SoundEvent)BFSounds.ITEM_RADIO_EQUIP.get();
                shakeNodeData = ShakeNodePresets.field_1929;
                break;
            }
            case 2: {
                MeleeItem meleeItem = (MeleeItem)item3;
                soundEvent = (SoundEvent)BFSounds.ITEM_KNIFE_EQUIP.get();
                shakeNodeData = ShakeNodePresets.field_1924;
                break;
            }
            case 3: {
                GrenadeFragItem grenadeFragItem = (GrenadeFragItem)item3;
                soundEvent = grenadeFragItem.method_4096();
                shakeNodeData = ShakeNodePresets.field_1925;
                break;
            }
            case 4: {
                MedicalSyringeItem medicalSyringeItem = (MedicalSyringeItem)item3;
                soundEvent = (SoundEvent)BFSounds.ITEM_SYRINGE_EQUIP.get();
                shakeNodeData = ShakeNodePresets.field_1927;
                break;
            }
            case 5: {
                BinocularsItem binocularsItem = (BinocularsItem)item3;
                soundEvent = (SoundEvent)BFSounds.ITEM_BINOCULARS_EQUIP.get();
                shakeNodeData = ShakeNodePresets.field_1930;
                break;
            }
        }
        if (soundEvent != null) {
            float f = 0.9f + 0.2f * random.nextFloat();
            SimpleSoundInstance simpleSoundInstance = SimpleSoundInstance.forUI((SoundEvent)soundEvent, (float)f, (float)1.0f);
            minecraft.getSoundManager().play((SoundInstance)simpleSoundInstance);
        }
        if (shakeNodeData != null) {
            ShakeManager.applyShake(shakeNodeData);
        }
        BFRenderFrameSubscriber.field_340 = 1.0f;
        BFRenderFrameSubscriber.field_341 = 1.0f;
        GunItem.field_4059 = 10;
    }

    private void method_169(@NotNull LocalPlayer localPlayer, @NotNull ItemStack itemStack, @NotNull IHasWeight iHasWeight) {
        float f = iHasWeight.getWeight(itemStack);
        if (f > 0.0f && iHasWeight.method_3728((Player)localPlayer, itemStack)) {
            Vec3 vec3 = localPlayer.getDeltaMovement();
            if (localPlayer.onGround()) {
                float f2 = 1.0f - f;
                localPlayer.setDeltaMovement(vec3.multiply((double)f2, 1.0, (double)f2));
            } else {
                float f3 = 1.0f - f / 3.0f;
                localPlayer.setDeltaMovement(vec3.multiply((double)f3, 1.0, (double)f3));
            }
        }
    }

    static {
        field_176 = 0.0f;
        field_170 = null;
        field_171 = 0.0f;
    }
}

