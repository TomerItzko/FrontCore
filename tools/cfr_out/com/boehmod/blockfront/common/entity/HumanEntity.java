/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientGamePacketListener
 *  net.minecraft.network.protocol.game.ClientboundAddEntityPacket
 *  net.minecraft.network.syncher.EntityDataAccessor
 *  net.minecraft.network.syncher.EntityDataSerializer
 *  net.minecraft.network.syncher.EntityDataSerializers
 *  net.minecraft.network.syncher.SynchedEntityData
 *  net.minecraft.network.syncher.SynchedEntityData$Builder
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerEntity
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.ai.attributes.AttributeSupplier$Builder
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.entity;

import com.boehmod.blockfront.common.entity.IntegratedGameEntity;
import com.boehmod.blockfront.game.impl.boot.BootcampNpcSpawn;
import com.boehmod.blockfront.unnamed.BF_564;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.BFUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Locale;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public final class HumanEntity
extends IntegratedGameEntity {
    private static final EntityDataAccessor<String> field_2350 = SynchedEntityData.defineId(HumanEntity.class, (EntityDataSerializer)EntityDataSerializers.STRING);
    public int field_2351 = 0;
    private List<BootcampNpcSpawn.BF_758> field_2349 = new ObjectArrayList();

    public HumanEntity(EntityType<? extends HumanEntity> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder getMobAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.5).add(Attributes.MAX_HEALTH, 20.0);
    }

    public void method_1969(@NotNull Level level, @NotNull BootcampNpcSpawn bootcampNpcSpawn) {
        this.method_1971(bootcampNpcSpawn.skin);
        this.setPos(bootcampNpcSpawn.position);
        this.setRot(bootcampNpcSpawn.rotation.y, bootcampNpcSpawn.rotation.x);
        this.setYHeadRot(bootcampNpcSpawn.field_3248);
        BFUtils.setInventoryFromLoadout(level, (LivingEntity)this, bootcampNpcSpawn.loadout);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, (Goal)new BF_564(this));
        this.goalSelector.addGoal(2, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0f));
    }

    public void defineSynchedData(@NotNull SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(field_2350, (Object)"us_soldier_0");
    }

    @NotNull
    public Packet<ClientGamePacketListener> getAddEntityPacket(@NotNull ServerEntity serverEntity) {
        return new ClientboundAddEntityPacket((Entity)this, serverEntity);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putString("skin", this.getSkin());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.method_1971(compoundTag.getString("skin"));
    }

    public String getSkin() {
        return (String)this.entityData.get(field_2350);
    }

    public void method_1971(@NotNull String string) {
        this.entityData.set(field_2350, (Object)string);
    }

    public List<BootcampNpcSpawn.BF_758> method_1973() {
        return this.field_2349;
    }

    public void method_1970(@NotNull List<BootcampNpcSpawn.BF_758> list) {
        this.field_2349 = list;
    }

    public ResourceLocation method_1972() {
        return BFRes.loc("textures/models/entities/human/" + this.getSkin().toLowerCase(Locale.ROOT) + ".png");
    }
}

