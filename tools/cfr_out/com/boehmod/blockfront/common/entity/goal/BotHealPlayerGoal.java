/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.ai.goal.Goal$Flag
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.entity.goal;

import com.boehmod.blockfront.common.entity.BotEntity;
import com.boehmod.blockfront.common.entity.goal.BotGoal;
import com.boehmod.blockfront.common.match.MatchClass;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.registry.BFParticleTypes;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFUtils;
import java.util.EnumSet;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class BotHealPlayerGoal
extends BotGoal {
    private static final int field_2424 = 40;
    public static final double field_2420 = 1.5;
    public static final float field_2422 = 2.0f;
    public static final float field_2423 = 5.0f;
    @Nullable
    protected LivingEntity field_2421;
    private int field_2425;
    private int field_2426 = 40;

    public BotHealPlayerGoal(@NotNull BotEntity botEntity) {
        super(botEntity);
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.botEntity.getTarget() != null) {
            return false;
        }
        AbstractGame<?, ?, ?> abstractGame = this.botEntity.getGame();
        if (abstractGame == null) {
            return false;
        }
        if (this.botEntity.getCurrentClass() != MatchClass.CLASS_MEDIC) {
            return false;
        }
        if (this.field_2425 > 0) {
            --this.field_2425;
            return false;
        }
        Object obj = abstractGame.getPlayerManager();
        BlockPos blockPos = this.botEntity.getOnPos();
        Level level = this.botEntity.level();
        this.field_2421 = level.getNearestPlayer((double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), 5.0, entity -> {
            if (entity instanceof Player) {
                Player player = (Player)entity;
                UUID uUID = player.getUUID();
                if (!obj.getPlayerUUIDs().contains(uUID)) {
                    return false;
                }
                GameTeam gameTeam = obj.getPlayerTeam(uUID);
                if (gameTeam == null) {
                    return false;
                }
                return gameTeam.equals(this.botEntity.getTeam()) && player.getHealth() < player.getMaxHealth();
            }
            if (entity instanceof BotEntity) {
                BotEntity botEntity = (BotEntity)entity;
                return botEntity.method_2031().equals(this.botEntity.method_2031());
            }
            return false;
        });
        return this.field_2421 != null;
    }

    public void stop() {
        super.stop();
        this.field_2421 = null;
        this.field_2425 = 100;
    }

    public void tick() {
        super.tick();
        float f = this.botEntity.distanceTo((Entity)this.field_2421);
        this.botEntity.getLookControl().setLookAt((Entity)this.field_2421, (float)(this.botEntity.getMaxHeadYRot() + 20), (float)this.botEntity.getMaxHeadXRot());
        if ((double)f < 1.5) {
            this.botEntity.getNavigation().stop();
        } else {
            this.botEntity.getNavigation().moveTo((Entity)this.field_2421, (double)0.55f);
        }
        if (f <= 2.0f && this.field_2426-- <= 0) {
            this.field_2426 = 40;
            this.method_2051();
        }
    }

    public void method_2051() {
        if (this.field_2421 == null) {
            return;
        }
        this.field_2421.heal(4.0f);
        this.botEntity.playSound((SoundEvent)BFSounds.ITEM_MEDICALBAG_HEAL.get(), 1.0f, 1.0f);
        BFUtils.method_2964(this.field_2421, (ParticleOptions)BFParticleTypes.HEAL.get(), this.field_2421.getRandom(), 0.0f, 0.0f, 0.0f, 5);
        LivingEntity livingEntity = this.field_2421;
        if (livingEntity instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)livingEntity;
            livingEntity = Component.literal((String)this.botEntity.getHumanName());
            GameTeam gameTeam = this.botEntity.getTeam();
            if (gameTeam != null) {
                livingEntity = livingEntity.copy().withStyle(gameTeam.getStyleText());
            }
            MutableComponent mutableComponent = Component.translatable((String)"bf.message.gamemode.medicalsyringe.heal", (Object[])new Object[]{livingEntity}).withStyle(ChatFormatting.GRAY);
            BFUtils.sendNoticeMessage(serverPlayer, (Component)mutableComponent);
        }
    }
}

