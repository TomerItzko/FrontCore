/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.core.BlockPos
 *  net.minecraft.util.Mth
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.client.event.ClientTickEvent$Post
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.event.tick.ClientTickable;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.common.gun.GunScopeConfig;
import com.boehmod.blockfront.common.gun.GunSpreadConfig;
import com.boehmod.blockfront.common.gun.GunSpreadConfigs;
import com.boehmod.blockfront.common.item.BFWeaponItem;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.unnamed.BF_1204;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BF_1164
extends ClientTickable {
    @Override
    public void run(@NotNull ClientTickEvent.Post event, @NotNull Random random, @NotNull Minecraft minecraft, @NotNull ClientPlayerDataHandler dataHandler, @NotNull BFClientManager manager, @Nullable LocalPlayer player, @Nullable ClientLevel level, @NotNull BFClientPlayerData playerData, @NotNull PlayerCloudData cloudData, @NotNull Vec3 pos, @NotNull BlockPos blockPos, @Nullable AbstractGame<?, ?, ?> game, boolean bl, float renderTime) {
        GunScopeConfig gunScopeConfig;
        GunSpreadConfig gunSpreadConfig;
        if (player == null || level == null) {
            return;
        }
        ItemStack itemStack = player.getMainHandItem();
        Item item = itemStack.getItem();
        BFWeaponItem bFWeaponItem = item instanceof BFWeaponItem ? (BFWeaponItem)item : null;
        GunSpreadConfig gunSpreadConfig2 = gunSpreadConfig = bFWeaponItem != null ? bFWeaponItem.getAimConfig() : GunSpreadConfigs.PISTOL;
        if (bFWeaponItem instanceof GunItem) {
            GunItem gunItem = (GunItem)bFWeaponItem;
            gunScopeConfig = gunItem.getScopeConfig(itemStack);
        } else {
            gunScopeConfig = null;
        }
        GunScopeConfig gunScopeConfig2 = gunScopeConfig;
        this.method_5852(gunScopeConfig2, gunSpreadConfig);
    }

    private void method_5852(@Nullable GunScopeConfig gunScopeConfig, @NotNull GunSpreadConfig gunSpreadConfig) {
        GunSpreadConfig.prevSpread = GunSpreadConfig.currentSpread;
        float f = BF_1204.method_5876(gunSpreadConfig, gunScopeConfig, GunItem.field_4019);
        GunSpreadConfig.currentSpread = Mth.lerp((float)0.6f, (float)GunSpreadConfig.currentSpread, (float)f);
    }
}

