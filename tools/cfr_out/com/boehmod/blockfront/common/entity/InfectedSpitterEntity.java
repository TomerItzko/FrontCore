/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientGamePacketListener
 *  net.minecraft.network.protocol.game.ClientboundAddEntityPacket
 *  net.minecraft.server.level.ServerEntity
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
 *  net.minecraft.world.entity.ai.goal.RangedAttackGoal
 *  net.minecraft.world.entity.monster.RangedAttackMob
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.entity;

import com.boehmod.blockfront.common.entity.AcidBallEntity;
import com.boehmod.blockfront.common.entity.InfectedEntity;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.registry.BFEntityTypes;
import com.boehmod.blockfront.registry.BFSounds;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.ChatFormatting;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class InfectedSpitterEntity
extends InfectedEntity
implements RangedAttackMob {
    public InfectedSpitterEntity(EntityType<? extends InfectedSpitterEntity> entityType, Level level) {
        super((EntityType<? extends InfectedEntity>)entityType, level);
    }

    public static AttributeSupplier.Builder getMobAttributes() {
        return InfectedEntity.getMobAttributes();
    }

    @Override
    public String method_2077() {
        return "spitter";
    }

    @Override
    public int method_2071() {
        return 4;
    }

    @Override
    public void method_2065(@NotNull PlayerDataHandler<?> playerDataHandler, float f) {
        super.method_2065(playerDataHandler, f);
        this.goalSelector.addGoal(1, (Goal)new RangedAttackGoal((RangedAttackMob)this, (double)Math.min(f, 0.6f), 50, 10.0f));
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(8, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0f));
    }

    public void performRangedAttack(@NotNull LivingEntity livingEntity, float f) {
        if (!this.hasLineOfSight((Entity)livingEntity)) {
            return;
        }
        this.method_2069();
        Level level = this.level();
        AcidBallEntity acidBallEntity = (AcidBallEntity)((EntityType)BFEntityTypes.ACID_BALL.get()).create(level);
        if (acidBallEntity != null) {
            acidBallEntity.teleportTo(this.getX(), this.getEyeY(), this.getZ());
            double d = livingEntity.getEyeY() - (double)1.1f;
            double d2 = livingEntity.getX() - this.getX();
            double d3 = d - acidBallEntity.getY();
            double d4 = livingEntity.getZ() - this.getZ();
            float f2 = Mth.sqrt((float)((float)(d2 * d2 + d4 * d4))) * 0.2f;
            acidBallEntity.shoot(d2, d3 + (double)f2, d4, 0.8f, 1.0f);
            level.addFreshEntity((Entity)acidBallEntity);
            this.playSound((SoundEvent)BFSounds.ENTITY_INFECTED_SPITTER_SPIT.get(), 1.5f, 0.8f + 0.05f * (float)ThreadLocalRandom.current().nextInt(4));
        }
    }

    @Override
    @NotNull
    public Packet<ClientGamePacketListener> getAddEntityPacket(@NotNull ServerEntity serverEntity) {
        return new ClientboundAddEntityPacket((Entity)this, serverEntity);
    }

    @Override
    @NotNull
    public String getScoreboardName() {
        return String.valueOf(ChatFormatting.RED) + "Infected Spitter";
    }
}

