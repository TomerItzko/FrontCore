/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.network.protocol.game.ClientGamePacketListener
 *  net.minecraft.network.protocol.game.ClientboundAddEntityPacket
 *  net.minecraft.server.level.ServerEntity
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  org.jetbrains.annotations.NotNull
 *  org.joml.Vector3f
 */
package com.boehmod.blockfront.common.entity;

import com.boehmod.blockfront.common.block.entity.BFBlockEntity;
import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.common.entity.ArtilleryVehicleEntity;
import com.boehmod.blockfront.registry.BFBlockEntityTypes;
import com.boehmod.blockfront.registry.BFBlocks;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.unnamed.BF_623;
import com.boehmod.blockfront.unnamed.BF_624;
import com.boehmod.blockfront.unnamed.BF_631;
import com.boehmod.blockfront.unnamed.BF_633;
import com.boehmod.blockfront.unnamed.BF_693;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class FlakGunEntity
extends ArtilleryVehicleEntity {
    public BF_693 field_3012;

    public FlakGunEntity(EntityType<? extends FlakGunEntity> entityType, Level level) {
        super((EntityType<? extends ArtilleryVehicleEntity>)entityType, level, (BlockEntityType<? extends BFBlockEntity>)((BlockEntityType)BFBlockEntityTypes.FLAK88_SHIELD.get()), ((Block)BFBlocks.FLAK88_SHIELD.get()).defaultBlockState(), (BlockEntityType<? extends BFBlockEntity>)((BlockEntityType)BFBlockEntityTypes.FLAK88_SHIELD_DESTROYED.get()), ((Block)BFBlocks.FLAK88_SHIELD_DESTROYED.get()).defaultBlockState());
    }

    @Override
    @NotNull
    protected BF_624<AbstractVehicleEntity> method_2317() {
        BF_633 bF_633 = new BF_631("turret0", this).method_2441().method_2445(64).method_2452(new Vector3f(0.0f, 1.8f, 0.0f)).method_2443(2.0f).method_2464(0).method_2466(1);
        BF_623 bF_623 = new BF_623().method_2383(bF_633).method_2387().method_2380().method_2391().method_2382(10.0f, 45.0f).method_2390(new Vector3f(0.0f, 0.0f, 0.0f)).method_2386(new Vector3f(-0.95f, 0.1f, -0.65f)).method_2385(BF_623.field_2665).method_2389(0.8f);
        BF_623 bF_6232 = new BF_623().method_2383(bF_633).method_2387().method_2380().method_2382(10.0f, 45.0f).method_2390(new Vector3f(0.0f, 0.0f, 0.0f)).method_2386(new Vector3f(0.8f, 0.2f, 0.4f)).method_2385(BF_623.field_2665).method_2389(0.8f);
        return new BF_624().method_2397(64).method_2408(1.5f).method_2395(0.025f, 0.015f).method_2409(0.15f, 0.075f).method_2414(0.1f, 0.1f).method_2416(1.85f, 1.85f).method_2420(8.0f).method_2419(1.5f).method_2417(0.75f).method_2399(bF_623).method_2399(bF_6232).method_2401(bF_633);
    }

    @Override
    public BF_693 method_2318() {
        if (this.field_3012 == null) {
            this.field_3012 = new BF_693(this);
        }
        this.field_3012.method_3036(BFSounds.ENTITY_VEHICLE_FLAK88_TURRET_MOVE, BFSounds.ENTITY_VEHICLE_FLAK88_TURRET_MOVE_START, BFSounds.ENTITY_VEHICLE_FLAK88_TURRET_MOVE_STOP, BFSounds.ENTITY_VEHICLE_FLAK88_TURRET_RELOAD);
        this.field_3012.method_3035(BFSounds.ENTITY_VEHICLE_FLAK88_TURRET_FIRE, BFSounds.ENTITY_VEHICLE_SHERMAN_TURRET_FIRE_ADD, BFSounds.ENTITY_VEHICLE_SHERMAN_TURRET_FIRE_DISTANT, BFSounds.ENTITY_VEHICLE_FLAK88_TURRET_FIRE_HIFI);
        this.field_3012.method_3043(null, null);
        this.field_3012.method_3034(null, null);
        this.field_3012.method_3042(null, null);
        this.field_3012.method_3039(null, null, null, null, null, null);
        this.field_3012.method_3034(null, null);
        return this.field_3012;
    }

    @Override
    public Packet<ClientGamePacketListener> method_2322(ServerEntity serverEntity) {
        return new ClientboundAddEntityPacket((Entity)this, serverEntity);
    }
}

