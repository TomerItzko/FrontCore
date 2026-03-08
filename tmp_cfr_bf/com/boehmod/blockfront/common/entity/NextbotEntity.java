/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.entity;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.entity.IntegratedGameEntity;
import com.boehmod.blockfront.common.player.BFAbstractPlayerData;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.game.AbstractGame;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import net.minecraft.core.Holder;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public abstract class NextbotEntity
extends IntegratedGameEntity {
    private final Predicate<LivingEntity> field_2742 = livingEntity -> {
        if (livingEntity instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)livingEntity;
            BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
            assert (bFAbstractManager != null) : "Mod manager is null!";
            Object obj = bFAbstractManager.getPlayerDataHandler();
            UUID uUID = serverPlayer.getUUID();
            Object d = ((PlayerDataHandler)obj).getPlayerData((Player)serverPlayer);
            if (((BFAbstractPlayerData)d).isOutOfGame() || !serverPlayer.isAlive()) {
                return false;
            }
            AbstractGame<?, ?, ?> abstractGame = bFAbstractManager.getGameWithPlayer(uUID);
            if (abstractGame == null || this.game == null) {
                return true;
            }
            return abstractGame.getUUID().equals(this.game.getUUID());
        }
        return false;
    };
    private int funnySoundTimer = 0;
    private int field_2745 = 0;
    private int field_2746 = 0;

    public NextbotEntity(EntityType<? extends NextbotEntity> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder getMobAttributes() {
        return Mob.createMobAttributes().add(Attributes.FOLLOW_RANGE, 256.0).add(Attributes.ATTACK_DAMAGE, 100.0).add(Attributes.MOVEMENT_SPEED, 1.0).add(Attributes.ATTACK_KNOCKBACK, 5.0).add(Attributes.MAX_HEALTH, 800.0);
    }

    protected void registerGoals() {
        super.registerGoals();
        ((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(true);
        this.targetSelector.addGoal(0, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, false, this.field_2742));
        this.goalSelector.addGoal(0, (Goal)new MeleeAttackGoal((PathfinderMob)this, (double)0.43f, true));
    }

    @Override
    public void baseTick() {
        List list;
        super.baseTick();
        Level level = this.level();
        if (level.isClientSide) {
            return;
        }
        if (this.field_2745-- <= 0) {
            this.field_2745 = 3;
            list = level.getNearbyPlayers(TargetingConditions.forCombat(), (LivingEntity)this, this.getBoundingBox().inflate(1.5, 1.5, 1.5));
            for (Player player : list) {
                player.hurt(level.damageSources().mobAttack((LivingEntity)this), 50.0f);
            }
        }
        if (this.funnySoundTimer-- <= 0) {
            this.funnySoundTimer = this.getFunnySoundInterval();
            level.playSeededSound(null, (Entity)this, this.getFunnySound(), SoundSource.HOSTILE, 3.0f, 1.0f, 0L);
        }
        if (this.field_2746-- <= 0) {
            this.field_2746 = 10;
            list = this.getTarget();
            if (list == null) {
                double d = -1.0;
                Player player = null;
                for (Player player2 : level.players()) {
                    double d2 = Mth.sqrt((float)player2.distanceTo((Entity)this));
                    if (d != -1.0 && !(d2 <= d) || !this.canAttack((LivingEntity)player2)) continue;
                    d = d2;
                    player = player2;
                }
                if (player != null) {
                    this.setTarget((LivingEntity)player);
                }
            } else if (!this.canAttack((LivingEntity)list)) {
                this.setTarget(null);
            }
        }
    }

    public float maxUpStep() {
        return 3.0f;
    }

    public boolean canAttack(@NotNull LivingEntity livingEntity) {
        if (!super.canAttack(livingEntity)) {
            return false;
        }
        return this.field_2742.test(livingEntity);
    }

    public abstract String getTexture();

    public abstract Holder<SoundEvent> getFunnySound();

    public abstract int getFunnySoundInterval();

    @NotNull
    public Packet<ClientGamePacketListener> getAddEntityPacket(@NotNull ServerEntity serverEntity) {
        return new ClientboundAddEntityPacket((Entity)this, serverEntity);
    }
}

