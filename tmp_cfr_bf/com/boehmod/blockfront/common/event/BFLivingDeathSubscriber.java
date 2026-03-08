/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.event;

import com.boehmod.bflib.cloud.common.player.achievement.CloudAchievements;
import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.entity.BotEntity;
import com.boehmod.blockfront.common.entity.InfectedEntity;
import com.boehmod.blockfront.common.entity.IntegratedGameEntity;
import com.boehmod.blockfront.common.gun.GunDamageConfigs;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.net.packet.BFExplosionPacket;
import com.boehmod.blockfront.common.net.packet.BFHitMarkerPacket;
import com.boehmod.blockfront.common.net.packet.BFSpawnCorpsePacket;
import com.boehmod.blockfront.common.player.BFAbstractPlayerData;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.common.world.ExplosionType;
import com.boehmod.blockfront.common.world.damage.BFDamageSource;
import com.boehmod.blockfront.common.world.damage.GrenadeDamageSource;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.tag.IHasBots;
import com.boehmod.blockfront.game.tag.IHasDominations;
import com.boehmod.blockfront.game.tag.IHasInfectedEntities;
import com.boehmod.blockfront.registry.BFItems;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.PacketUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.lang.runtime.SwitchBootstraps;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public final class BFLivingDeathSubscriber {
    public static final UUID SCOOTER_UUID = UUID.fromString("b8a1b294-3a53-4f78-98ee-2b75e6e41f44");
    public static final float field_3027 = 20.0f;
    public static final float field_3028 = 8.0f;

    public static void onLivingDeath(@NotNull LivingDeathEvent event) {
        Object object;
        Object object2;
        Object object3;
        Object object4;
        Object object5;
        Object object6;
        LivingEntity livingEntity = event.getEntity();
        DamageSource damageSource = event.getSource();
        Level level = livingEntity.level();
        if (!(level instanceof ServerLevel)) {
            return;
        }
        ServerLevel serverLevel = (ServerLevel)level;
        level = damageSource.getEntity();
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is null!";
        Object obj = bFAbstractManager.getPlayerDataHandler();
        if (level instanceof ServerPlayer) {
            object6 = (ServerPlayer)level;
            object5 = new BFHitMarkerPacket(true);
            PacketUtils.sendToPlayer((CustomPacketPayload)object5, (ServerPlayer)object6);
        }
        if (damageSource instanceof GrenadeDamageSource) {
            object6 = serverLevel.players().iterator();
            while (object6.hasNext()) {
                object5 = (ServerPlayer)object6.next();
                if (!(object5 instanceof ServerPlayer)) continue;
                object4 = object5;
                PacketUtils.sendToPlayer(new BFExplosionPacket(ExplosionType.GORE_EXPLOSION, livingEntity.getEyePosition()), (ServerPlayer)object4);
            }
        }
        if (damageSource instanceof BFDamageSource && !(object5 = ((BFDamageSource)((Object)(object6 = (BFDamageSource)damageSource))).getItemUsing()).isEmpty() && (object3 = object5.getItem()) instanceof GunItem && ((GunItem)(object4 = (GunItem)object3)).getDamageConfig() == GunDamageConfigs.ATR) {
            for (Object object7 : serverLevel.players()) {
                if (!(object7 instanceof ServerPlayer)) continue;
                object2 = object7;
                object = new BFExplosionPacket(ExplosionType.GORE_EXPLOSION, livingEntity.getEyePosition());
                PacketUtils.sendToPlayer((CustomPacketPayload)object, (ServerPlayer)object2);
            }
        }
        if (livingEntity instanceof ServerPlayer) {
            Object object7;
            object6 = (ServerPlayer)livingEntity;
            object5 = object6.getUUID();
            object4 = object6.getInventory();
            object3 = bFAbstractManager.getGameWithPlayer((UUID)object5);
            object7 = ((PlayerDataHandler)obj).getPlayerData((Player)object6);
            if (object3 != null) {
                ServerPlayer serverPlayer;
                object2 = ((AbstractGame)object3).getPlayerManager();
                BFUtils.initPlayerForGame(obj, serverLevel, (ServerPlayer)object6);
                ((BFAbstractPlayerData)object7).setOutOfGame(true);
                ((BFAbstractPlayerData)object7).setRespawnTimer(((AbstractGamePlayerManager)object2).method_2770((ServerPlayer)object6));
                object = ((AbstractGamePlayerManager)object2).getPlayerUUIDs();
                ((AbstractGame)object3).getCombatManager().method_2808(bFAbstractManager, event, (Set<UUID>)object);
                Level level2 = level;
                int n = 0;
                switch (SwitchBootstraps.typeSwitch("typeSwitch", new Object[]{ServerPlayer.class, BotEntity.class, InfectedEntity.class}, (Object)level2, n)) {
                    case 0: {
                        serverPlayer = (ServerPlayer)level2;
                        UUID uUID = serverPlayer.getUUID();
                        if (!bFAbstractManager.gameHasPlayer((UUID)object5, uUID)) break;
                        ((AbstractGamePlayerManager)object2).handlePlayerDeath(bFAbstractManager, serverLevel, (ServerPlayer)object6, (UUID)object5, serverPlayer, uUID, damageSource, (Set<UUID>)object);
                        if (object3 instanceof IHasDominations) {
                            ((AbstractGame)object3).getDominatingManager().handleKill((PlayerDataHandler<?>)obj, (UUID)object5, uUID, (Set<UUID>)object);
                        }
                        if (!((UUID)object5).equals(SCOOTER_UUID)) break;
                        BFUtils.awardAchievement(bFAbstractManager, uUID, CloudAchievements.ACH_USER_SHOT_THE_SHERIFF);
                        break;
                    }
                    case 1: {
                        AbstractGame<?, ?, ?> abstractGame;
                        BotEntity botEntity = (BotEntity)level2;
                        if (botEntity.getGame() == null || !((abstractGame = botEntity.getGame()) instanceof IHasBots)) break;
                        IHasBots iHasBots = (IHasBots)((Object)abstractGame);
                        iHasBots.method_3391(botEntity, (Player)object6);
                        break;
                    }
                    case 2: {
                        InfectedEntity infectedEntity = (InfectedEntity)level2;
                        AbstractGame<?, ?, ?> abstractGame = infectedEntity.getGame();
                        if (!(abstractGame instanceof IHasInfectedEntities)) break;
                        IHasInfectedEntities iHasInfectedEntities = (IHasInfectedEntities)((Object)abstractGame);
                        iHasInfectedEntities.method_3423(infectedEntity, (Player)object6);
                        break;
                    }
                    default: {
                        ((AbstractGamePlayerManager)object2).handlePlayerDeath(bFAbstractManager, serverLevel, (ServerPlayer)object6, (UUID)object5, null, null, damageSource, (Set<UUID>)object);
                    }
                }
                if (((BFAbstractPlayerData)object7).isOutOfGame()) {
                    level2 = object6.blockPosition();
                    ObjectArrayList objectArrayList = ((Inventory)object4).items.stream().filter(itemStack -> !itemStack.isEmpty()).map(arg_0 -> BFLivingDeathSubscriber.method_3051((ServerPlayer)object6, (BlockPos)level2, arg_0)).collect(Collectors.toCollection(ObjectArrayList::new));
                    object4.clearContent();
                    livingEntity.setHealth(livingEntity.getMaxHealth());
                    event.setCanceled(true);
                    serverPlayer = (LivingDropsEvent)NeoForge.EVENT_BUS.post((Event)new LivingDropsEvent((LivingEntity)object6, damageSource, (Collection)objectArrayList, false));
                    if (!serverPlayer.isCanceled()) {
                        objectArrayList.forEach(arg_0 -> ((ServerLevel)serverLevel).addFreshEntity(arg_0));
                    }
                }
            }
            ((BFAbstractPlayerData)object7).reset();
        }
        BFLivingDeathSubscriber.method_3052(damageSource, livingEntity);
        if (livingEntity instanceof IntegratedGameEntity && ((IntegratedGameEntity)(object6 = (IntegratedGameEntity)livingEntity)).getGame() != null) {
            object5 = ((IntegratedGameEntity)object6).getGame();
            object4 = ((AbstractGamePlayerManager)((AbstractGame)object5).getPlayerManager()).getPlayerUUIDs();
            ((AbstractGame)object5).getCombatManager().method_2808(bFAbstractManager, event, (Set<UUID>)object4);
        }
        if (livingEntity instanceof BotEntity && (object4 = ((IntegratedGameEntity)(object6 = (BotEntity)livingEntity)).getGame()) instanceof IHasBots) {
            object5 = (IHasBots)object4;
            object5.method_3387(bFAbstractManager, serverLevel, (BotEntity)object6, damageSource);
        }
        if (livingEntity instanceof InfectedEntity && (object4 = ((IntegratedGameEntity)(object6 = (InfectedEntity)livingEntity)).getGame()) instanceof IHasInfectedEntities) {
            object5 = (IHasInfectedEntities)object4;
            object5.method_3422(bFAbstractManager, serverLevel, (InfectedEntity)object6, damageSource);
        }
    }

    private static void method_3052(@NotNull DamageSource damageSource, @NotNull LivingEntity livingEntity) {
        Object object;
        Object object2;
        Vec3 vec3 = damageSource.getSourcePosition();
        if (vec3 == null) {
            vec3 = livingEntity.getEyePosition();
        }
        boolean bl = damageSource.is(DamageTypeTags.IS_EXPLOSION);
        boolean bl2 = damageSource.is(DamageTypeTags.IS_FIRE);
        float f = bl ? 7.0f : 3.0f;
        Vec3 vec32 = livingEntity.getEyePosition().subtract(vec3.x, vec3.y, vec3.z);
        float f2 = (float)livingEntity.getEyePosition().distanceTo(vec3);
        boolean bl3 = bl && f2 <= 6.0f;
        ItemStack itemStack = damageSource.getWeaponItem();
        if (itemStack != null && (object2 = itemStack.getItem()) instanceof GunItem && ((GunItem)(object = (GunItem)object2)).getDamageConfig() == GunDamageConfigs.ATR) {
            bl3 = true;
        }
        if (itemStack != null && itemStack.getItem() == BFItems.MELEE_ITEM_SWORD_SHIN_GUNTO.get()) {
            bl3 = true;
        }
        object = vec32.normalize().toVector3f().mul(f);
        if (bl && f2 <= 8.0f) {
            float f3 = 20.0f * (1.0f - f2 / 8.0f);
            object.add(0.0f, f3, 0.0f);
        }
        object2 = new BFSpawnCorpsePacket.Context(bl2, bl3, bl, (Vector3f)object);
        BFSpawnCorpsePacket bFSpawnCorpsePacket = new BFSpawnCorpsePacket(livingEntity.getId(), (BFSpawnCorpsePacket.Context)object2);
        PacketUtils.sendToAllPlayers(bFSpawnCorpsePacket);
    }

    private static /* synthetic */ ItemEntity method_3051(ServerPlayer serverPlayer, BlockPos blockPos, ItemStack itemStack) {
        return new ItemEntity(serverPlayer.level(), (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), itemStack.copy());
    }
}

