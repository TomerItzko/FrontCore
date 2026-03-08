/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.event.tick.ClientTickable;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.world.ShakeManager;
import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.common.entity.ArtilleryVehicleEntity;
import com.boehmod.blockfront.common.gun.GunFireConfig;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.net.packet.BFVehicleFireBulletPacket;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.unnamed.BF_623;
import com.boehmod.blockfront.unnamed.BF_631;
import com.boehmod.blockfront.unnamed.BF_632;
import com.boehmod.blockfront.unnamed.BF_633;
import com.boehmod.blockfront.unnamed.BF_946;
import com.boehmod.blockfront.util.GunUtils;
import com.boehmod.blockfront.util.PacketUtils;
import com.boehmod.blockfront.util.math.ShakeNodeData;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.jetbrains.annotations.NotNull;

public class BF_43
extends ClientTickable {
    @Override
    public void run(@NotNull ClientTickEvent.Post event, @NotNull Random random, @NotNull Minecraft minecraft, @NotNull ClientPlayerDataHandler dataHandler, @NotNull BFClientManager manager, @Nullable LocalPlayer player, @Nullable ClientLevel level, @NotNull BFClientPlayerData playerData, @NotNull PlayerCloudData cloudData, @NotNull Vec3 pos, @NotNull BlockPos blockPos, @Nullable AbstractGame<?, ?, ?> game, boolean bl, float renderTime) {
        if (player == null || level == null) {
            return;
        }
        Entity entity = player.getVehicle();
        if (!(entity instanceof AbstractVehicleEntity)) {
            return;
        }
        AbstractVehicleEntity abstractVehicleEntity = (AbstractVehicleEntity)entity;
        boolean bl2 = minecraft.mouseHandler.isLeftPressed();
        BF_623 bF_623 = abstractVehicleEntity.method_2324((Entity)player);
        if (bF_623 == null) {
            return;
        }
        for (BF_633<?> bF_633 : bF_623.method_2388()) {
            int n;
            BF_631 bF_631;
            if (!(bF_633 instanceof BF_631) || (bF_631 = (BF_631)bF_633).method_2442() != BF_632.BULLET || !bl2 || bF_631.field_2734 > 0) continue;
            GunItem gunItem = (GunItem)bF_631.method_2459().get();
            GunFireConfig gunFireConfig = gunItem.getDefaultFireConfig();
            bF_631.field_2734 = gunFireConfig.getFireRate();
            if (!(entity instanceof ArtilleryVehicleEntity)) continue;
            ArtilleryVehicleEntity artilleryVehicleEntity = (ArtilleryVehicleEntity)entity;
            SynchedEntityData synchedEntityData = artilleryVehicleEntity.getEntityData();
            ArtilleryVehicleEntity.BF_620 bF_620 = artilleryVehicleEntity.method_2358(bF_631);
            if (bF_620 == null || (n = bF_620.method_2361(synchedEntityData)) <= 0) continue;
            BF_43.method_173(minecraft, dataHandler, manager, player, level, gunItem);
            bF_620.method_2364(synchedEntityData, n - 1);
        }
    }

    private static void method_173(@NotNull Minecraft minecraft, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull BFClientManager bFClientManager, @NotNull LocalPlayer localPlayer, @NotNull ClientLevel clientLevel, GunItem gunItem) {
        BF_946 bF_946 = new BF_946(minecraft, clientPlayerDataHandler, gunItem.getDamageConfig());
        int n = (int)clientLevel.getGameTime();
        long l = clientLevel.random.nextLong();
        RandomSource randomSource = RandomSource.create((long)l);
        bF_946.method_5790(bFClientManager, clientLevel, randomSource, ThreadLocalRandom.current(), localPlayer, gunItem, n, l);
        PacketUtils.sendToServer(new BFVehicleFireBulletPacket());
        GunUtils.method_1422(minecraft, bFClientManager, clientPlayerDataHandler, localPlayer, clientLevel, new ItemStack((ItemLike)gunItem), gunItem, localPlayer.position(), true, false, false, 0);
        ShakeNodeData shakeNodeData = gunItem.getShakeNodeData();
        ShakeManager.applyShake(shakeNodeData, 1.0f);
    }
}

