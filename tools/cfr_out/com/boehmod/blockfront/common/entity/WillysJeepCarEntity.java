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
import com.boehmod.blockfront.common.entity.vehicle.CommonVehicleControl;
import com.boehmod.blockfront.registry.BFBlockEntityTypes;
import com.boehmod.blockfront.registry.BFBlocks;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.unnamed.BF_623;
import com.boehmod.blockfront.unnamed.BF_624;
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

public class WillysJeepCarEntity
extends ArtilleryVehicleEntity {
    public BF_693 field_3022;

    public WillysJeepCarEntity(EntityType<? extends WillysJeepCarEntity> entityType, Level level) {
        super((EntityType<? extends ArtilleryVehicleEntity>)entityType, level, (BlockEntityType<? extends BFBlockEntity>)((BlockEntityType)BFBlockEntityTypes.WILLYS_JEEP.get()), ((Block)BFBlocks.WILLYS_JEEP.get()).defaultBlockState(), (BlockEntityType<? extends BFBlockEntity>)((BlockEntityType)BFBlockEntityTypes.WILLYS_JEEP_DESTROYED.get()), ((Block)BFBlocks.WILLYS_JEEP_DESTROYED.get()).defaultBlockState());
    }

    @Override
    @NotNull
    protected BF_624<AbstractVehicleEntity> method_2317() {
        BF_623 bF_623 = new BF_623().method_2387().method_2391().method_2382(35.0f, 120.0f).method_2385(BF_623.field_2666).method_2390(new Vector3f(0.5f, 0.15f, -0.3f));
        BF_623 bF_6232 = new BF_623().method_2382(35.0f, 120.0f).method_2385(BF_623.field_2668).method_2390(new Vector3f(-0.5f, 0.15f, -0.3f));
        BF_623 bF_6233 = new BF_623().method_2382(35.0f, 200.0f).method_2385(BF_623.field_2668).method_2390(new Vector3f(0.15f, 0.55f, -1.75f));
        return new BF_624().method_2406(true).method_2398(new CommonVehicleControl()).method_2408(1.5f).method_2395(0.02f, 0.01f).method_2413(0.95f).method_2409(0.3f, 0.075f).method_2414(0.25f, 0.25f).method_2416(1.85f, 1.85f).method_2415(0.6f).method_2420(8.0f).method_2419(1.25f).method_2417(0.85f).method_2399(bF_623).method_2399(bF_6232).method_2399(bF_6233);
    }

    @Override
    public BF_693 method_2318() {
        if (this.field_3022 == null) {
            this.field_3022 = new BF_693(this).method_3034(null, null);
            this.field_3022.method_3038(BFSounds.ENTITY_VEHICLE_WILLYS_JEEP_HORN, BFSounds.ENTITY_VEHICLE_WILLYS_JEEP_HORN_DISTANT);
            this.field_3022.method_3037(BFSounds.ENTITY_VEHICLE_DOOR);
            this.field_3022.method_3034(BFSounds.ENTITY_VEHICLE_LANCIA_1ZM_WHEELS, BFSounds.ENTITY_VEHICLE_LANCIA_1ZM_WHEELS_TURN);
            this.field_3022.method_3039(BFSounds.ENTITY_VEHICLE_LANCIA_1ZM_ENGINE_FORWARD, BFSounds.ENTITY_VEHICLE_LANCIA_1ZM_ENGINE_FORWARD_DISTANT, BFSounds.ENTITY_VEHICLE_LANCIA_1ZM_ENGINE_FORWARD_START, BFSounds.ENTITY_VEHICLE_LANCIA_1ZM_ENGINE_FORWARD_START_DISTANT, BFSounds.ENTITY_VEHICLE_LANCIA_1ZM_ENGINE_FORWARD_STOP, BFSounds.ENTITY_VEHICLE_LANCIA_1ZM_ENGINE_FORWARD_STOP_DISTANT);
            this.field_3022.method_3040(BFSounds.ENTITY_VEHICLE_LANCIA_1ZM_ENGINE_IDLE);
            this.field_3022.method_3042(BFSounds.ENTITY_VEHICLE_LANCIA_1ZM_ENGINE_START, BFSounds.ENTITY_VEHICLE_LANCIA_1ZM_ENGINE_STOP);
            this.field_3022.method_3036(BFSounds.ENTITY_VEHICLE_LANCIA_1ZM_TURRET_TURN, null, null, null);
        }
        return this.field_3022;
    }

    @Override
    public Packet<ClientGamePacketListener> method_2322(ServerEntity serverEntity) {
        return new ClientboundAddEntityPacket((Entity)this, serverEntity);
    }
}

