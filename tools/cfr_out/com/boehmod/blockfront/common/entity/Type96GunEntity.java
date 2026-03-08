/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientGamePacketListener
 *  net.minecraft.network.protocol.game.ClientboundAddEntityPacket
 *  net.minecraft.server.level.ServerEntity
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.NotNull
 *  org.joml.Vector3f
 */
package com.boehmod.blockfront.common.entity;

import com.boehmod.blockfront.common.block.entity.BFBlockEntity;
import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.common.entity.ArtilleryVehicleEntity;
import com.boehmod.blockfront.common.entity.RocketEntity;
import com.boehmod.blockfront.registry.BFBlockEntityTypes;
import com.boehmod.blockfront.registry.BFBlocks;
import com.boehmod.blockfront.registry.BFEntityTypes;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.unnamed.BF_623;
import com.boehmod.blockfront.unnamed.BF_624;
import com.boehmod.blockfront.unnamed.BF_628;
import com.boehmod.blockfront.unnamed.BF_630;
import com.boehmod.blockfront.unnamed.BF_631;
import com.boehmod.blockfront.unnamed.BF_633;
import com.boehmod.blockfront.unnamed.BF_693;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class Type96GunEntity
extends ArtilleryVehicleEntity {
    public BF_693 field_3021;

    public Type96GunEntity(EntityType<? extends Type96GunEntity> entityType, Level level) {
        super((EntityType<? extends ArtilleryVehicleEntity>)entityType, level, (BlockEntityType<? extends BFBlockEntity>)((BlockEntityType)BFBlockEntityTypes.TYPE96_SHIELD.get()), ((Block)BFBlocks.TYPE96_SHIELD.get()).defaultBlockState(), (BlockEntityType<? extends BFBlockEntity>)((BlockEntityType)BFBlockEntityTypes.TYPE96_SHIELD_DESTROYED.get()), ((Block)BFBlocks.TYPE96_SHIELD_DESTROYED.get()).defaultBlockState());
    }

    @Override
    @NotNull
    protected BF_624<AbstractVehicleEntity> method_2317() {
        BF_633 bF_633 = new BF_631("head", this).method_2441().method_2443(2.0f).method_2445(64).method_2450((EntityType<? extends RocketEntity>)((EntityType)BFEntityTypes.ANTI_AIR_ROCKET.get())).method_2464(0).method_2466(1);
        BF_623 bF_623 = new BF_623().method_2387().method_2391().method_2380().method_2382(10.0f, 45.0f).method_2390(new Vector3f(0.0f, 0.0f, 0.0f)).method_2386(new Vector3f(-1.7f, 0.3f, -1.25f)).method_2385(BF_623.field_2665).method_2389(0.8f);
        BF_623 bF_6232 = new BF_623().method_2387().method_2380().method_2382(10.0f, 45.0f).method_2390(new Vector3f(0.0f, 0.0f, 0.0f)).method_2386(new Vector3f(1.7f, 0.3f, -1.25f)).method_2385(BF_623.field_2668).method_2389(0.8f);
        Vec3 vec3 = new Vec3(0.0, (double)0.1f, 0.0);
        return new BF_624().method_2397(64).method_2408(1.5f).method_2410(5).method_2395(0.025f, 0.015f).method_2409(0.15f, 0.075f).method_2414(0.1f, 0.1f).method_2416(1.85f, 1.85f).method_2420(8.0f).method_2419(1.5f).method_2417(0.75f).method_2399(bF_623).method_2399(bF_6232).method_2401(bF_633).method_2400(new BF_628(ParticleTypes.LARGE_SMOKE, new Vec3((double)-2.6f, (double)2.4f, 0.0)).method_2434(vec3)).method_2400(new BF_630(new Vec3((double)2.2f, (double)0.1f, (double)1.65f)).method_2434(vec3)).method_2400(new BF_630(new Vec3((double)-2.2f, (double)0.1f, (double)1.65f)).method_2434(vec3)).method_2400(new BF_630(new Vec3((double)2.2f, (double)0.1f, (double)-1.65f)).method_2434(vec3)).method_2400(new BF_630(new Vec3((double)-2.2f, (double)0.1f, (double)-1.65f)).method_2434(vec3)).method_2400(new BF_630(new Vec3((double)1.2f, (double)0.1f, (double)1.65f)).method_2434(vec3)).method_2400(new BF_630(new Vec3((double)-1.2f, (double)0.1f, (double)1.65f)).method_2434(vec3)).method_2400(new BF_630(new Vec3((double)1.2f, (double)0.1f, (double)-1.65f)).method_2434(vec3)).method_2400(new BF_630(new Vec3((double)-1.2f, (double)0.1f, (double)-1.65f)).method_2434(vec3));
    }

    @Override
    public BF_693 method_2318() {
        if (this.field_3021 == null) {
            this.field_3021 = new BF_693(this);
        }
        this.field_3021.method_3036(BFSounds.ENTITY_VEHICLE_FLAK88_TURRET_MOVE, BFSounds.ENTITY_VEHICLE_FLAK88_TURRET_MOVE_START, BFSounds.ENTITY_VEHICLE_FLAK88_TURRET_MOVE_STOP, BFSounds.ENTITY_VEHICLE_FLAK88_TURRET_RELOAD);
        this.field_3021.method_3035(BFSounds.ENTITY_VEHICLE_FLAK88_TURRET_FIRE, BFSounds.ENTITY_VEHICLE_SHERMAN_TURRET_FIRE_ADD, BFSounds.ENTITY_VEHICLE_SHERMAN_TURRET_FIRE_DISTANT, BFSounds.ENTITY_VEHICLE_FLAK88_TURRET_FIRE_HIFI);
        this.field_3021.method_3033(null);
        this.field_3021.method_3043(null, null);
        this.field_3021.method_3034(null, null);
        this.field_3021.method_3042(null, null);
        this.field_3021.method_3039(null, null, null, null, null, null);
        this.field_3021.method_3034(null, null);
        return this.field_3021;
    }

    @Override
    public Packet<ClientGamePacketListener> method_2322(ServerEntity serverEntity) {
        return new ClientboundAddEntityPacket((Entity)this, serverEntity);
    }
}

