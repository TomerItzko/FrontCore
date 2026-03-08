/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.event;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.common.entity.BotEntity;
import com.boehmod.blockfront.common.net.packet.BFBulletDamageEffectsPacket;
import com.boehmod.blockfront.common.net.packet.BFDamageIndicatorPacket;
import com.boehmod.blockfront.common.net.packet.BFHitMarkerPacket;
import com.boehmod.blockfront.common.player.BFAbstractPlayerData;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.common.world.damage.FirearmDamageSource;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameHolder;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.unnamed.BF_668;
import com.boehmod.blockfront.util.PacketUtils;
import java.util.UUID;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import org.jetbrains.annotations.NotNull;

public final class BFLivingDamageSubscriber {
    public static void onLivingDamagePre(@NotNull LivingDamageEvent.Pre event) {
        GameTeam gameTeam;
        AbstractGame<?, ?, ?> abstractGame;
        Object object;
        GameHolder gameHolder;
        Object object2;
        LivingEntity livingEntity = event.getEntity();
        DamageContainer damageContainer = event.getContainer();
        DamageSource damageSource = damageContainer.getSource();
        Entity entity = damageSource.getEntity();
        float f = damageContainer.getOriginalDamage();
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is null!";
        Object obj = bFAbstractManager.getPlayerDataHandler();
        if (entity != null && (object2 = livingEntity.getVehicle()) instanceof AbstractVehicleEntity) {
            gameHolder = (AbstractVehicleEntity)object2;
            if (f < Float.MAX_VALUE && ((AbstractVehicleEntity)gameHolder).method_2352((Entity)livingEntity)) {
                damageContainer.setNewDamage(0.0f);
                return;
            }
        }
        if (damageSource.is(DamageTypeTags.IS_FALL) && livingEntity.tickCount < 20) {
            damageContainer.setNewDamage(0.0f);
            return;
        }
        if (livingEntity instanceof ServerPlayer) {
            gameHolder = (ServerPlayer)livingEntity;
            object2 = damageSource.getSourcePosition();
            if (object2 != null) {
                float f2 = Math.min(1.0f, f / 5.0f);
                object = new BFDamageIndicatorPacket((Vec3)object2, f2);
                PacketUtils.sendToPlayer((CustomPacketPayload)object, (ServerPlayer)gameHolder);
            }
            if (damageSource instanceof FirearmDamageSource) {
                BFBulletDamageEffectsPacket bFBulletDamageEffectsPacket = new BFBulletDamageEffectsPacket();
                PacketUtils.sendToPlayer(bFBulletDamageEffectsPacket, (ServerPlayer)gameHolder);
            }
        }
        if (entity instanceof BotEntity) {
            gameHolder = (BotEntity)entity;
            object2 = ((BotEntity)gameHolder).getTeam();
            if (livingEntity instanceof Player) {
                Player player = (Player)livingEntity;
                abstractGame = bFAbstractManager.getGameWithPlayer(player.getUUID());
                if (abstractGame != null && (gameTeam = ((AbstractGamePlayerManager)abstractGame.getPlayerManager()).getPlayerTeam(player.getUUID())) != null && gameTeam.equals(object2)) {
                    damageContainer.setNewDamage(0.0f);
                    return;
                }
            } else if (livingEntity instanceof BotEntity && (abstractGame = ((BotEntity)(object = (BotEntity)livingEntity)).getTeam()) != null && ((GameTeam)((Object)abstractGame)).equals(object2)) {
                damageContainer.setNewDamage(0.0f);
                return;
            }
        }
        if (entity instanceof Player) {
            gameHolder = (Player)entity;
            object2 = gameHolder.getUUID();
            AbstractGame<?, ?, ?> abstractGame2 = bFAbstractManager.getGameWithPlayer((UUID)object2);
            if (abstractGame2 != null && damageSource instanceof FirearmDamageSource) {
                damageContainer.setNewDamage(f * abstractGame2.getFirearmDamageMultiplier());
            }
            float f3 = damageContainer.getNewDamage();
            if (livingEntity instanceof Player) {
                abstractGame = (Player)livingEntity;
                if (f3 > 0.0f) {
                    gameTeam = ((PlayerDataHandler)obj).getPlayerData((Player)abstractGame);
                    ((BFAbstractPlayerData)((Object)gameTeam)).method_840((UUID)object2, f3);
                }
            }
            if (f3 > 0.0f) {
                BF_668.method_5648(livingEntity.level(), livingEntity.position(), 1.5f);
            }
            if (gameHolder instanceof ServerPlayer) {
                abstractGame = (ServerPlayer)gameHolder;
                PacketUtils.sendToPlayer(new BFHitMarkerPacket(false), (ServerPlayer)abstractGame);
            }
        }
    }
}

