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
import com.boehmod.blockfront.game.tag.IHasInfectedEntities;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.unnamed.BF_227;
import com.boehmod.blockfront.util.BFRes;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import net.minecraft.ChatFormatting;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class InfectedEntity
extends IntegratedGameEntity {
    private static final EntityDataAccessor<Integer> field_2453 = SynchedEntityData.defineId(InfectedEntity.class, (EntityDataSerializer)EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> field_2454 = SynchedEntityData.defineId(InfectedEntity.class, (EntityDataSerializer)EntityDataSerializers.INT);
    public static final int field_6371 = 20;
    private int field_2452 = 0;
    private boolean field_2449 = false;
    private int field_2451 = 0;

    public InfectedEntity(EntityType<? extends InfectedEntity> entityType, Level level) {
        super(entityType, level);
        this.method_2075(ThreadLocalRandom.current().nextInt(5));
    }

    @NotNull
    public static AttributeSupplier.Builder getMobAttributes() {
        return Mob.createMobAttributes().add(Attributes.FOLLOW_RANGE, 128.0).add(Attributes.ATTACK_DAMAGE, 9.0).add(Attributes.MOVEMENT_SPEED, 0.6).add(Attributes.MAX_HEALTH, 100.0);
    }

    public void method_2069() {
        this.field_2449 = true;
        this.field_2452 = 0;
    }

    @NotNull
    public ResourceLocation method_2073() {
        return BFRes.loc("textures/models/entities/infected/" + this.method_2077() + "/" + this.method_2072() + ".png");
    }

    @OnlyIn(value=Dist.CLIENT)
    private void method_2068(@NotNull Level level) {
    }

    public void method_2065(@NotNull PlayerDataHandler<?> playerDataHandler, float f) {
        this.goalSelector.addGoal(2, (Goal)new MeleeAttackGoal((PathfinderMob)this, (double)Math.min(f, 0.6f), true));
        Predicate<LivingEntity> predicate = this.method_2064(playerDataHandler);
        this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, 0, false, false, predicate));
    }

    public String method_2077() {
        return "generic" + this.method_2070();
    }

    public int method_2071() {
        return 2;
    }

    public String getSkin() {
        return "default";
    }

    public int method_2070() {
        return (Integer)this.entityData.get(field_2453);
    }

    public void method_2075(int n) {
        this.entityData.set(field_2453, (Object)n);
    }

    public int method_2072() {
        return (Integer)this.entityData.get(field_2454);
    }

    public void method_2076(int n) {
        this.entityData.set(field_2454, (Object)n);
    }

    @NotNull
    public Predicate<LivingEntity> method_2064(@NotNull PlayerDataHandler<?> playerDataHandler) {
        return livingEntity -> {
            if (livingEntity instanceof Player) {
                Player player = (Player)livingEntity;
                BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
                assert (bFAbstractManager != null) : "Mod manager is null!";
                UUID uUID = player.getUUID();
                AbstractGame<?, ?, ?> abstractGame = bFAbstractManager.getGameWithPlayer(uUID);
                Object d = playerDataHandler.getPlayerData(player);
                return player.isAlive() && !((BFAbstractPlayerData)d).isOutOfGame() && abstractGame != null && abstractGame.equals(this.game);
            }
            return false;
        };
    }

    @OnlyIn(value=Dist.CLIENT)
    public void method_2067(BF_227 bF_227, float f) {
        float f2 = Mth.sin((float)(f / 12.0f));
        float f3 = 0.4f * f2;
        float f4 = Mth.sin((float)(f / 4.0f));
        float f5 = 0.2f * f4;
        float f6 = Mth.sin((float)(f / 7.0f));
        float f7 = 0.1f * f6;
        bF_227.head.xRot = bF_227.hat.xRot = f5;
        bF_227.head.zRot = bF_227.hat.zRot = f7;
        bF_227.leftArm.xRot = bF_227.leftSleeve.xRot = -1.4f + f3;
        bF_227.rightArm.xRot = bF_227.rightSleeve.xRot = -1.4f - f3;
    }

    @NotNull
    public ResourceLocation method_2074() {
        return BFRes.loc("textures/models/entities/infected/" + this.method_2077() + "/" + this.method_2072() + "_bright.png");
    }

    @NotNull
    public String getScoreboardName() {
        return String.valueOf(ChatFormatting.RED) + "Infected";
    }

    public boolean canAttack(LivingEntity livingEntity) {
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is null!";
        if (!livingEntity.isAlive() || livingEntity.tickCount <= 40) {
            return false;
        }
        if (livingEntity instanceof Player) {
            Player player = (Player)livingEntity;
            Object obj = bFAbstractManager.getPlayerDataHandler();
            Object d = ((PlayerDataHandler)obj).getPlayerData(player);
            return !((BFAbstractPlayerData)d).isOutOfGame();
        }
        return false;
    }

    public void playAmbientSound() {
        super.playAmbientSound();
        Level level = this.level();
        if (!level.isClientSide()) {
            this.method_2069();
        }
    }

    protected SoundEvent getAmbientSound() {
        return (SoundEvent)BFSounds.ENTITY_INFECTED_HURT.get();
    }

    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return (SoundEvent)BFSounds.ENTITY_INFECTED_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return (SoundEvent)BFSounds.ENTITY_INFECTED_DEATH.get();
    }

    @Override
    public void baseTick() {
        super.baseTick();
        Level level = this.level();
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is null!";
        Object obj = bFAbstractManager.getPlayerDataHandler();
        if (!level.isClientSide()) {
            Player player;
            Object d;
            LivingEntity livingEntity = this.getTarget();
            if (livingEntity instanceof Player && (((BFAbstractPlayerData)(d = ((PlayerDataHandler)obj).getPlayerData(player = (Player)livingEntity))).isOutOfGame() || !player.isAlive())) {
                this.setTarget(null);
                return;
            }
            if (this.tickCount % 2 == 0) {
                if (this.field_2449) {
                    if (this.field_2452 < this.method_2071()) {
                        ++this.field_2452;
                    } else {
                        this.field_2449 = false;
                    }
                } else if (this.field_2452 > 0) {
                    --this.field_2452;
                }
            }
            this.method_2076(this.field_2452);
        } else {
            this.method_2068(level);
        }
    }

    @Override
    public void method_1937(@NotNull ServerLevel serverLevel) {
        super.method_1937(serverLevel);
        if (this.field_2451-- <= 0) {
            this.field_2451 = 20;
            AbstractGame abstractGame = this.game;
            if (abstractGame instanceof IHasInfectedEntities) {
                IHasInfectedEntities iHasInfectedEntities = (IHasInfectedEntities)((Object)abstractGame);
                iHasInfectedEntities.method_3425(serverLevel, this);
            }
        }
    }

    @NotNull
    public Packet<ClientGamePacketListener> getAddEntityPacket(@NotNull ServerEntity serverEntity) {
        return new ClientboundAddEntityPacket((Entity)this, serverEntity);
    }

    public void defineSynchedData(@NotNull SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(field_2453, (Object)0).define(field_2454, (Object)0);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(8, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0f));
    }
}

