/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.entity;

import com.boehmod.blockfront.common.block.entity.BFBlockEntity;
import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.common.net.packet.BFVehicleFireShellPacket;
import com.boehmod.blockfront.unnamed.BF_624;
import com.boehmod.blockfront.unnamed.BF_631;
import com.boehmod.blockfront.unnamed.BF_633;
import com.boehmod.blockfront.unnamed.BF_693;
import com.boehmod.blockfront.util.PacketUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public abstract class ArtilleryVehicleEntity
extends AbstractVehicleEntity {
    private static final List<BF_620> field_2636 = new ObjectArrayList<BF_620>(){
        {
            for (int i = 0; i < 5; ++i) {
                this.add(new BF_620());
            }
        }
    };
    public static boolean field_2637 = false;
    public int field_2641 = 0;
    private boolean field_2638 = false;
    private boolean field_2639 = false;
    private boolean field_2640 = false;

    public ArtilleryVehicleEntity(EntityType<? extends ArtilleryVehicleEntity> entityType, Level level, BlockEntityType<? extends BFBlockEntity> blockEntityType, BlockState blockState, BlockEntityType<? extends BFBlockEntity> blockEntityType2, BlockState blockState2) {
        super(entityType, level, blockEntityType, blockState, blockEntityType2, blockState2);
    }

    public BF_620 method_2358(@NotNull BF_631 bF_631) {
        BF_624<AbstractVehicleEntity> bF_624 = this.method_2343();
        int n = 0;
        for (BF_633 bF_633 : bF_624.field_2679) {
            if (bF_633 == bF_631) {
                return ArtilleryVehicleEntity.method_2357(n);
            }
            ++n;
        }
        return null;
    }

    public static BF_620 method_2357(int n) {
        return field_2636.get(n);
    }

    @Override
    public void tick() {
        super.tick();
        Level level = this.level();
        BF_624<AbstractVehicleEntity> bF_624 = this.method_2343();
        int n = 0;
        for (BF_633 bF_633 : bF_624.field_2679) {
            if (!(bF_633 instanceof BF_631)) continue;
            BF_631 bF_631 = (BF_631)bF_633;
            BF_620 bF_620 = ArtilleryVehicleEntity.method_2357(n);
            bF_631.method_2449(bF_620.field_2642);
            ++n;
        }
        if (level.isClientSide) {
            this.method_2359();
        } else if (this.field_2641 > 0) {
            --this.field_2641;
        }
    }

    @OnlyIn(value=Dist.CLIENT)
    public void method_2359() {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer localPlayer = minecraft.player;
        BF_693 bF_693 = this.method_2318();
        if (minecraft.mouseHandler.isLeftPressed()) {
            if (!this.field_2638) {
                this.field_2638 = true;
                PacketUtils.sendToServer(new BFVehicleFireShellPacket());
            }
        } else {
            this.field_2638 = false;
        }
        if (localPlayer != null && this.hasPassenger((Entity)localPlayer)) {
            SoundManager soundManager = minecraft.getSoundManager();
            if (field_2637) {
                this.field_2640 = false;
                if (!this.field_2639) {
                    this.field_2639 = true;
                    soundManager.play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)bF_693.field_2971.get()), (float)1.0f, (float)1.0f));
                }
            } else {
                this.field_2639 = false;
                if (!this.field_2640) {
                    this.field_2640 = true;
                    soundManager.play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)bF_693.field_2972.get()), (float)1.0f, (float)1.0f));
                }
            }
        }
        int n = 0;
        for (BF_633 bF_633 : this.method_2343().field_2679) {
            if (!(bF_633 instanceof BF_631)) continue;
            BF_631 bF_631 = (BF_631)bF_633;
            BF_620 bF_620 = ArtilleryVehicleEntity.method_2357(n);
            bF_631.method_2457(bF_620.field_2642);
            ++n;
        }
    }

    public abstract BF_693 method_2318();

    @Override
    public void defineSynchedData(@NotNull SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        for (BF_620 bF_620 : field_2636) {
            bF_620.defineSynchedData(builder);
        }
    }

    @Override
    public float method_2311() {
        BF_624<AbstractVehicleEntity> bF_624 = this.method_2343();
        float f = this.method_2313();
        float f2 = f < 0.0f ? bF_624.field_2692 : bF_624.field_2691;
        return f / f2;
    }

    public static class BF_620 {
        EntityDataAccessor<Float> field_2642 = SynchedEntityData.defineId(ArtilleryVehicleEntity.class, (EntityDataSerializer)EntityDataSerializers.FLOAT);
        EntityDataAccessor<Integer> field_2643 = SynchedEntityData.defineId(ArtilleryVehicleEntity.class, (EntityDataSerializer)EntityDataSerializers.INT);
        EntityDataAccessor<Boolean> field_2644 = SynchedEntityData.defineId(ArtilleryVehicleEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);

        public void defineSynchedData(@NotNull SynchedEntityData.Builder builder) {
            builder.define(this.field_2642, (Object)Float.valueOf(0.0f)).define(this.field_2643, (Object)32).define(this.field_2644, (Object)false);
        }

        public float method_2360(@NotNull SynchedEntityData synchedEntityData) {
            return ((Float)synchedEntityData.get(this.field_2642)).floatValue();
        }

        public void method_2363(@NotNull SynchedEntityData synchedEntityData, float f) {
            synchedEntityData.set(this.field_2642, (Object)Float.valueOf(f));
        }

        public int method_2361(@NotNull SynchedEntityData synchedEntityData) {
            return (Integer)synchedEntityData.get(this.field_2643);
        }

        public void method_2364(@NotNull SynchedEntityData synchedEntityData, int n) {
            synchedEntityData.set(this.field_2643, (Object)n);
        }

        public boolean method_2362(@NotNull SynchedEntityData synchedEntityData) {
            return (Boolean)synchedEntityData.get(this.field_2644);
        }

        public void method_2365(@NotNull SynchedEntityData synchedEntityData, boolean bl) {
            synchedEntityData.set(this.field_2644, (Object)bl);
        }
    }
}

