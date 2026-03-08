/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.tags.DamageTypeTags
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.event;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.common.entity.BotEntity;
import com.boehmod.blockfront.common.entity.InfectedEntity;
import com.boehmod.blockfront.common.player.BFAbstractPlayerData;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.tag.IAllowsRespawning;
import com.boehmod.blockfront.game.tag.IHasBots;
import com.boehmod.blockfront.game.tag.IHasInfectedEntities;
import com.boehmod.blockfront.unnamed.BF_623;
import com.boehmod.blockfront.unnamed.BF_631;
import com.boehmod.blockfront.unnamed.BF_632;
import com.boehmod.blockfront.unnamed.BF_633;
import com.boehmod.blockfront.util.BFUtils;
import java.util.UUID;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import org.jetbrains.annotations.NotNull;

public final class BFLivingIncomingDamageSubscriber {
    public static void onLivingIncomingDamage(@NotNull LivingIncomingDamageEvent event) {
        AbstractGame<?, ?, ?> abstractGame;
        Object object;
        Object object3;
        Object object4;
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is null!";
        Object obj = bFAbstractManager.getPlayerDataHandler();
        LivingEntity livingEntity = event.getEntity();
        if (livingEntity.level().isClientSide) {
            return;
        }
        DamageSource damageSource = event.getSource();
        Entity entity = damageSource.getEntity();
        if (entity != null && (object4 = entity.getVehicle()) instanceof AbstractVehicleEntity) {
            object3 = (AbstractVehicleEntity)object4;
            object4 = ((AbstractVehicleEntity)object3).method_2324(entity);
            boolean bl = true;
            if (object4 != null) {
                if (!damageSource.is(DamageTypeTags.IS_EXPLOSION) && (((BF_623)object4).field_2661 || ((BF_623)object4).field_2662)) {
                    bl = false;
                }
                for (BF_633 object22 : ((BF_623)object4).method_2388()) {
                    if (!(object22 instanceof BF_631) || ((BF_631)(object = (BF_631)object22)).method_2442() != BF_632.BULLET) continue;
                    bl = true;
                    break;
                }
            }
            if (!bl) {
                event.setCanceled(true);
                return;
            }
        }
        if (entity != null && (object4 = livingEntity.getVehicle()) instanceof AbstractVehicleEntity) {
            object3 = (AbstractVehicleEntity)object4;
            if (event.getAmount() < Float.MAX_VALUE && ((AbstractVehicleEntity)object3).method_2352((Entity)livingEntity)) {
                event.setCanceled(true);
                return;
            }
        }
        if (livingEntity instanceof Player) {
            object3 = (Player)livingEntity;
            object4 = object3.getUUID();
            Object d = ((PlayerDataHandler)obj).getPlayerData((Player)object3);
            if (((BFAbstractPlayerData)d).isOutOfGame()) {
                event.setCanceled(true);
                return;
            }
            abstractGame = bFAbstractManager.getGameWithPlayer((UUID)object4);
            if (abstractGame != null) {
                Object p = abstractGame.getPlayerManager();
                if (abstractGame instanceof IAllowsRespawning && BFUtils.getPlayerStat(abstractGame, (UUID)object4, BFStats.SPAWN_PRO) > 0) {
                    event.setCanceled(true);
                    return;
                }
                object = damageSource.getEntity();
                if (!((AbstractGamePlayerManager)p).method_2778((Player)object3, (UUID)object4, damageSource, (Entity)object)) {
                    event.setCanceled(true);
                    return;
                }
                if (object instanceof Player) {
                    IAllowsRespawning iAllowsRespawning;
                    Player player = (Player)object;
                    UUID uUID = player.getUUID();
                    if (abstractGame instanceof IAllowsRespawning && (iAllowsRespawning = (IAllowsRespawning)((Object)abstractGame)).method_3434(uUID)) {
                        BFUtils.setPlayerStat(abstractGame, player.getUUID(), BFStats.SPAWN_PRO, 0);
                    }
                }
            }
        }
        if (entity instanceof Player) {
            object3 = (Player)entity;
            object4 = object3.getUUID();
            Object d = ((PlayerDataHandler)obj).getPlayerData((Player)object3);
            if ((((Player)object3).tickCount <= 20 || ((BFAbstractPlayerData)d).isOutOfGame() || object3.equals((Object)event.getEntity())) && !damageSource.is(DamageTypeTags.IS_EXPLOSION) && !damageSource.is(DamageTypeTags.IS_FIRE)) {
                event.setCanceled(true);
                return;
            }
            abstractGame = bFAbstractManager.getGameWithPlayer((UUID)object4);
            if (abstractGame != null) {
                IHasInfectedEntities iHasInfectedEntities;
                if (!((AbstractGamePlayerManager)abstractGame.getPlayerManager()).method_2775(livingEntity, damageSource)) {
                    event.setCanceled(true);
                    return;
                }
                if (livingEntity instanceof BotEntity) {
                    BotEntity botEntity = (BotEntity)livingEntity;
                    if (abstractGame instanceof IHasBots && !(object = (IHasBots)((Object)abstractGame)).method_3390(botEntity, damageSource)) {
                        event.setCanceled(true);
                        return;
                    }
                }
                if (livingEntity instanceof InfectedEntity && abstractGame instanceof IHasInfectedEntities && !(iHasInfectedEntities = (IHasInfectedEntities)((Object)abstractGame)).method_3421(bFAbstractManager, (InfectedEntity)livingEntity, damageSource, event.getAmount())) {
                    event.setCanceled(true);
                }
            }
        }
    }
}

