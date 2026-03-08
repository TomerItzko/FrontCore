/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.level.Explosion
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.SoundType
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.entity;

import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.common.entity.GrenadeEntity;
import com.boehmod.blockfront.common.net.packet.BFPositionedShakeNodePacket;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.unnamed.BF_624;
import com.boehmod.blockfront.util.PacketUtils;
import com.boehmod.blockfront.util.math.ShakeNodePresets;
import java.util.List;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public final class LandmineEntity
extends GrenadeEntity {
    public boolean field_2793 = false;
    private int field_2794 = 2400;
    private int field_2795 = 0;

    public LandmineEntity(EntityType<LandmineEntity> entityType, Level level) {
        super((EntityType<? extends GrenadeEntity>)entityType, level);
    }

    public void method_2523() {
        List<Entity> list = this.method_2520(0.5f);
        boolean bl = false;
        for (Entity entity : list) {
            AbstractVehicleEntity abstractVehicleEntity;
            if (!(entity instanceof AbstractVehicleEntity) || !this.method_2521(abstractVehicleEntity = (AbstractVehicleEntity)entity)) continue;
            bl = true;
            break;
        }
        if (bl) {
            this.method_1957();
        }
    }

    private boolean method_2521(@NotNull AbstractVehicleEntity abstractVehicleEntity) {
        return !abstractVehicleEntity.method_2330() && (this.owner == null || !abstractVehicleEntity.method_2327(this.owner));
    }

    public List<Entity> method_2520(float f) {
        return this.level().getEntitiesOfClass(Entity.class, this.getBoundingBox().inflate((double)f));
    }

    public List<AbstractVehicleEntity> method_2522(float f) {
        return this.level().getEntitiesOfClass(AbstractVehicleEntity.class, this.getBoundingBox().inflate((double)f));
    }

    public boolean ignoreExplosion(@NotNull Explosion explosion) {
        return false;
    }

    @Override
    protected float method_1938() {
        return 0.4f;
    }

    @Override
    protected float method_1939() {
        return 0.4f;
    }

    @Override
    public void method_2281() {
    }

    @Override
    public int method_2283() {
        return 1;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide && this.field_2795-- <= 0) {
            this.field_2795 = 10;
            if (this.tickCount > 40) {
                this.method_2523();
            }
        }
        if (this.field_2794-- <= 0) {
            this.discard();
        }
    }

    @Override
    public void method_1957() {
        if (!this.field_2340) {
            this.method_2524();
        }
        super.method_1957();
    }

    private void method_2524() {
        List<AbstractVehicleEntity> list = this.method_2522(1.0f);
        for (AbstractVehicleEntity abstractVehicleEntity : list) {
            if (!this.method_2521(abstractVehicleEntity)) continue;
            BF_624<AbstractVehicleEntity> bF_624 = abstractVehicleEntity.method_2343();
            abstractVehicleEntity.method_2329(this.getItem(), this.owner, bF_624.field_2685);
            PacketUtils.sendToAllPlayers(new BFPositionedShakeNodePacket(ShakeNodePresets.EXPLOSION_SMALL, this.position().toVector3f(), 32.0f));
        }
    }

    @Override
    protected DeferredHolder<SoundEvent, SoundEvent> method_2279(@NotNull SoundType soundType) {
        return BFSounds.ITEM_LANDMINE_BOUNCE;
    }

    @Override
    public int method_2284() {
        return Integer.MAX_VALUE;
    }
}

