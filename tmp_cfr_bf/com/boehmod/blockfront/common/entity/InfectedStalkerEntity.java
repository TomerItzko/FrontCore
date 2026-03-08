/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.entity;

import com.boehmod.blockfront.common.entity.InfectedEntity;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.registry.BFParticleTypes;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.unnamed.BF_227;
import com.boehmod.blockfront.util.BFRes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class InfectedStalkerEntity
extends InfectedEntity {
    private static final EntityDataAccessor<Boolean> field_2741 = SynchedEntityData.defineId(InfectedStalkerEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    private int field_2740 = 0;

    public InfectedStalkerEntity(EntityType<? extends InfectedStalkerEntity> entityType, Level level) {
        super((EntityType<? extends InfectedEntity>)entityType, level);
    }

    public static AttributeSupplier.Builder getMobAttributes() {
        return InfectedEntity.getMobAttributes();
    }

    public boolean method_2477() {
        return (Boolean)this.entityData.get(field_2741);
    }

    public void method_2475() {
        Level level = this.level();
        this.entityData.set(field_2741, (Object)true);
        Vec3 vec3 = this.getEyePosition();
        for (int i = 0; i < 5; ++i) {
            level.addParticle((ParticleOptions)ParticleTypes.LAVA, true, vec3.x, vec3.y, vec3.z, 0.0, 0.0, 0.0);
        }
        level.addParticle((ParticleOptions)BFParticleTypes.GRENADE_FLASH.get(), true, vec3.x, vec3.y, vec3.z, 0.0, 0.0, 0.0);
        this.playSound((SoundEvent)BFSounds.MISC_GORE_NOSE.get());
        this.playSound((SoundEvent)BFSounds.MISC_GORE.get());
    }

    public void method_2476() {
        Vec3 vec3 = this.getEyePosition();
        if (Math.random() < 0.2) {
            this.level().addParticle((ParticleOptions)ParticleTypes.LAVA, vec3.x, vec3.y, vec3.z, 0.0, 0.0, 0.0);
        }
    }

    public void tick() {
        if (this.level().isClientSide() && this.isAlive() && this.method_2477()) {
            this.method_2476();
            ++this.field_2740;
        }
        super.tick();
    }

    public boolean hurt(@NotNull DamageSource damageSource, float f) {
        if (super.hurt(damageSource, f)) {
            this.method_2475();
            return true;
        }
        return false;
    }

    public boolean doHurtTarget(@NotNull Entity entity) {
        boolean bl = super.doHurtTarget(entity);
        if (bl) {
            entity.setRemainingFireTicks(3);
            this.method_2475();
        }
        return bl;
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putBoolean("ignited", this.method_2477());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        if (compoundTag.getBoolean("ignited")) {
            this.method_2475();
        }
    }

    @Override
    public void defineSynchedData(@NotNull SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(field_2741, (Object)false);
    }

    @Override
    public String method_2077() {
        return "stalker";
    }

    @Override
    public int method_2071() {
        return 0;
    }

    @Override
    public void method_2065(@NotNull PlayerDataHandler<?> playerDataHandler, float f) {
        super.method_2065(playerDataHandler, f);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(8, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0f));
    }

    @Override
    @NotNull
    public Packet<ClientGamePacketListener> getAddEntityPacket(@NotNull ServerEntity serverEntity) {
        return new ClientboundAddEntityPacket((Entity)this, serverEntity);
    }

    @Override
    @NotNull
    public String getScoreboardName() {
        return String.valueOf(ChatFormatting.RED) + "Infected Stalker";
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public void method_2067(BF_227 bF_227, float f) {
        boolean bl = this.method_2477();
        float f2 = bl ? 1.2f : 0.4f;
        float f3 = Mth.sin((float)(f / 12.0f));
        float f4 = f2 * f3;
        bF_227.leftArm.xRot = bF_227.leftSleeve.xRot = -1.4f + f4;
        bF_227.rightArm.xRot = bF_227.rightSleeve.xRot = -1.4f - f4;
        if (bl) {
            float f5 = Mth.sin((float)(f / 10.0f));
            float f6 = Mth.sin((float)(f / 15.0f));
            float f7 = f5 * 1.2f;
            float f8 = f6 * 1.5f;
            bF_227.leftArm.yRot = bF_227.leftSleeve.yRot = -1.1f + f7;
            bF_227.rightArm.yRot = bF_227.rightSleeve.yRot = 1.1f + f8;
            float f9 = Mth.sin((float)(f / 2.0f));
            float f10 = Mth.sin((float)(f / 1.5f));
            bF_227.head.xRot = bF_227.hat.xRot -= 0.2f;
            bF_227.head.yRot = bF_227.hat.yRot = f9 / 8.0f;
            bF_227.head.zRot = bF_227.hat.zRot = f10 / 10.0f;
        } else {
            float f11 = Mth.sin((float)(f / 4.0f));
            float f12 = 0.2f * f11;
            float f13 = Mth.sin((float)(f / 7.0f));
            float f14 = 0.1f * f13;
            bF_227.head.xRot = bF_227.hat.xRot = f12;
            bF_227.head.zRot = bF_227.hat.zRot = f14;
        }
    }

    @Override
    @NotNull
    public ResourceLocation method_2073() {
        return BFRes.loc("textures/models/entities/infected/stalker/stalker" + (String)(this.method_2477() ? "_ignited_" + this.field_2740 % 2 : "") + ".png");
    }

    @Override
    @NotNull
    public ResourceLocation method_2074() {
        return BFRes.loc("textures/models/entities/infected/stalker/stalker" + (String)(this.method_2477() ? "_ignited_" + this.field_2740 % 2 : "") + "_bright.png");
    }
}

