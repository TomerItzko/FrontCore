/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.fds.tag.FDSTagCompound
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Position
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.AABB
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game.impl.conq;

import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.common.entity.MatchEntity;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.util.RegistryUtils;
import com.boehmod.blockfront.util.math.FDSPose;
import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

public final class VehicleSpawn
extends FDSPose {
    private static final int field_3282 = 30;
    @NotNull
    private final UUID field_3284 = UUID.randomUUID();
    public Supplier<? extends EntityType<?>> entityType;
    public boolean isAllies;
    private int field_3283 = 30;

    public VehicleSpawn() {
    }

    public VehicleSpawn(double x, double y, double z, float pitch, float yaw, boolean isAllies, @NotNull Supplier<? extends EntityType<?>> entityType) {
        super(x, y, z, pitch, yaw);
        this.isAllies = isAllies;
        this.entityType = entityType;
    }

    public VehicleSpawn(@NotNull Player player, boolean isAllies, @NotNull Supplier<? extends EntityType<?>> entityType) {
        super(player);
        this.isAllies = isAllies;
        this.entityType = entityType;
    }

    @NotNull
    public static VehicleSpawn method_3191(@NotNull String string, @NotNull FDSTagCompound fDSTagCompound) {
        VehicleSpawn vehicleSpawn = new VehicleSpawn();
        vehicleSpawn.readFromFDS(fDSTagCompound.getTagCompound(string));
        return vehicleSpawn;
    }

    public void method_3189(@NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull Level level) {
        if (this.field_3283-- <= 0) {
            this.field_3283 = 30;
            if (!this.method_3190(abstractGame, level)) {
                this.method_3192(abstractGame, level);
            }
        }
    }

    public void method_3192(@NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull Level level) {
        if (!level.isLoaded(BlockPos.containing((Position)this.position))) {
            return;
        }
        Entity entity = this.entityType.get().create(level);
        if (entity == null) {
            return;
        }
        if (entity instanceof MatchEntity) {
            MatchEntity matchEntity = (MatchEntity)entity;
            matchEntity.setGame(abstractGame);
            if (entity instanceof AbstractVehicleEntity) {
                AbstractVehicleEntity abstractVehicleEntity = (AbstractVehicleEntity)entity;
                abstractVehicleEntity.method_2344(this.field_3284);
                abstractVehicleEntity.method_2321(this.isAllies ? abstractGame.getAlliesDivision() : abstractGame.getAxisDivision());
            }
        }
        level.addFreshEntity(entity);
        entity.setPos(this.position);
        entity.setYRot(this.rotation.y);
    }

    private boolean method_3190(@NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull Level level) {
        BlockPos blockPos = ((AbstractGamePlayerManager)abstractGame.getPlayerManager()).getLobbySpawn().asBlockPos();
        AABB aABB = new AABB(blockPos).inflate((double)abstractGame.entityBoundSize());
        for (AbstractVehicleEntity abstractVehicleEntity : level.getEntitiesOfClass(AbstractVehicleEntity.class, aABB)) {
            if (abstractVehicleEntity.method_2330() || !this.field_3284.equals(abstractVehicleEntity.method_2349())) continue;
            return true;
        }
        return false;
    }

    @Override
    public void writeToFDS(@NotNull FDSTagCompound fDSTagCompound) {
        super.writeToFDS(fDSTagCompound);
        fDSTagCompound.setBoolean("isAllies", this.isAllies);
        fDSTagCompound.setString("entity", RegistryUtils.getEntityTypeId(this.entityType));
    }

    @Override
    public void readFromFDS(@NotNull FDSTagCompound fDSTagCompound) {
        super.readFromFDS(fDSTagCompound);
        this.isAllies = fDSTagCompound.getBoolean("isAllies");
        this.entityType = RegistryUtils.retrieveEntityType(fDSTagCompound.getString("entity"));
    }

    @Override
    public void writeNamedFDS(@NotNull String nameTag, @NotNull FDSTagCompound root) {
        FDSTagCompound fDSTagCompound = new FDSTagCompound(nameTag);
        this.writeToFDS(fDSTagCompound);
        root.setTagCompound(nameTag, fDSTagCompound);
    }

    @Override
    @NotNull
    public VehicleSpawn copy() {
        return new VehicleSpawn(this.position.x, this.position.y, this.position.z, this.rotation.x, this.rotation.y, this.isAllies, this.entityType);
    }

    @Override
    @NotNull
    public /* synthetic */ FDSPose copy() {
        return this.copy();
    }
}

