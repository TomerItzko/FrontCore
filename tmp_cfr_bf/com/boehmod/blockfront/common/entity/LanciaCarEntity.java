/*
 * Decompiled with CFR.
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
import com.boehmod.blockfront.unnamed.BF_630;
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
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class LanciaCarEntity
extends ArtilleryVehicleEntity {
    public BF_693 field_3014;

    public LanciaCarEntity(EntityType<? extends LanciaCarEntity> entityType, Level level) {
        super((EntityType<? extends ArtilleryVehicleEntity>)entityType, level, (BlockEntityType<? extends BFBlockEntity>)((BlockEntityType)BFBlockEntityTypes.LANCIA_1ZM.get()), ((Block)BFBlocks.LANCIA_1ZM.get()).defaultBlockState(), (BlockEntityType<? extends BFBlockEntity>)((BlockEntityType)BFBlockEntityTypes.LANCIA_1ZM_DESTROYED.get()), ((Block)BFBlocks.LANCIA_1ZM_DESTROYED.get()).defaultBlockState());
    }

    @Override
    @NotNull
    protected BF_624<AbstractVehicleEntity> method_2317() {
        BF_633 bF_633 = new BF_631("turret", this).method_2441().method_2445(64).method_2443(3.5f).method_2464(0);
        BF_623 bF_623 = new BF_623().method_2380().method_2387().method_2393().method_2392().method_2391().method_2382(15.0f, 45.0f).method_2390(new Vector3f(0.0f, 2.25f, -1.0f)).method_2384("tank_driver").method_2385(BF_623.field_2665).method_2389(0.6f);
        Vec3 vec3 = new Vec3(0.0, (double)0.1f, 0.0);
        return new BF_624().method_2406(true).method_2398(new CommonVehicleControl()).method_2397(64).method_2408(1.5f).method_2395(0.05f, 0.03f).method_2413(0.95f).method_2409(0.2f, 0.075f).method_2414(0.25f, 0.25f).method_2416(1.85f, 1.85f).method_2415(0.6f).method_2420(8.0f).method_2419(1.25f).method_2417(0.85f).method_2399(bF_623).method_2401(bF_633).method_2400(new BF_630(new Vec3((double)2.1f, (double)0.1f, (double)1.05f)).method_2434(vec3)).method_2400(new BF_630(new Vec3((double)2.1f, (double)0.1f, (double)-1.05f)).method_2434(vec3)).method_2400(new BF_630(new Vec3((double)-1.4f, (double)0.1f, (double)1.05f)).method_2434(vec3)).method_2400(new BF_630(new Vec3((double)-1.4f, (double)0.1f, (double)-1.05f)).method_2434(vec3));
    }

    @Override
    public BF_693 method_2318() {
        if (this.field_3014 == null) {
            this.field_3014 = new BF_693(this).method_3034(null, null);
            this.field_3014.method_3037(BFSounds.ENTITY_VEHICLE_DOOR);
            this.field_3014.method_3034(BFSounds.ENTITY_VEHICLE_LANCIA_1ZM_WHEELS, BFSounds.ENTITY_VEHICLE_LANCIA_1ZM_WHEELS_TURN);
            this.field_3014.method_3039(BFSounds.ENTITY_VEHICLE_LANCIA_1ZM_ENGINE_FORWARD, BFSounds.ENTITY_VEHICLE_LANCIA_1ZM_ENGINE_FORWARD_DISTANT, BFSounds.ENTITY_VEHICLE_LANCIA_1ZM_ENGINE_FORWARD_START, BFSounds.ENTITY_VEHICLE_LANCIA_1ZM_ENGINE_FORWARD_START_DISTANT, BFSounds.ENTITY_VEHICLE_LANCIA_1ZM_ENGINE_FORWARD_STOP, BFSounds.ENTITY_VEHICLE_LANCIA_1ZM_ENGINE_FORWARD_STOP_DISTANT);
            this.field_3014.method_3040(BFSounds.ENTITY_VEHICLE_LANCIA_1ZM_ENGINE_IDLE);
            this.field_3014.method_3042(BFSounds.ENTITY_VEHICLE_LANCIA_1ZM_ENGINE_START, BFSounds.ENTITY_VEHICLE_LANCIA_1ZM_ENGINE_STOP);
            this.field_3014.method_3036(BFSounds.ENTITY_VEHICLE_LANCIA_1ZM_TURRET_TURN, null, null, null);
        }
        return this.field_3014;
    }

    @Override
    public Packet<ClientGamePacketListener> method_2322(ServerEntity serverEntity) {
        return new ClientboundAddEntityPacket((Entity)this, serverEntity);
    }
}

