/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.boehmod.blockfront.common.gun.bullet;

import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.entity.BotEntity;
import com.boehmod.blockfront.common.entity.InfectedEntity;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.util.BFUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record EntityCollisionEntry(long tickIndex, int entityId, @NotNull Vec3 position, @NotNull AABB boundingBox, @NotNull Vec3 velocity, float yRot, float xRot, boolean isValid, @Nullable AABB headshotRegion, @Nullable AABB backpackRegion, boolean canShoot) {
    @NotNull
    public static final Vec3 HEADSHOT_EYE_OFFSET = new Vec3(0.0, (double)0.05f, 0.0);
    @NotNull
    public static final Vec3 BACKPACK_EYE_OFFSET = new Vec3(0.0, -0.5, 0.0);
    @NotNull
    public static final Vec3 BACKPACK_BACK_OFFSET = new Vec3(-0.45, 0.0, 0.0);
    @NotNull
    public static final Vec3 HEADSHOT_REGION_SCALE = new Vec3(0.31, 0.31, 0.31);
    @NotNull
    public static final Vec3 BACKPACK_REGION_SCALE = new Vec3(0.31, 0.31, 0.31);

    @NotNull
    public static EntityCollisionEntry method_5817(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, long l, @NotNull Entity entity, @Nullable LivingEntity livingEntity) {
        AABB aABB = null;
        AABB aABB2 = null;
        boolean bl = true;
        boolean bl2 = true;
        if (entity instanceof LivingEntity) {
            Object object;
            BotEntity botEntity;
            LivingEntity livingEntity2 = (LivingEntity)entity;
            aABB = EntityCollisionEntry.createHeadshotRegion(livingEntity2);
            aABB2 = EntityCollisionEntry.createBackpackRegion(livingEntity2);
            if (livingEntity != null) {
                bl = BFUtils.entitiesCanAttack(bFAbstractManager, bFAbstractManager.getPlayerDataHandler(), livingEntity, livingEntity2);
                if (livingEntity instanceof Player) {
                    botEntity = (Player)livingEntity;
                    if (livingEntity2 instanceof BotEntity && (!(object = (BotEntity)livingEntity2).isAlive() || object.isDeadOrDying() || ((BotEntity)object).method_1990((Player)botEntity))) {
                        bl = false;
                    }
                }
            }
            if (livingEntity2 instanceof BotEntity && (botEntity = (BotEntity)livingEntity2).method_2006()) {
                bl2 = false;
            }
            if (livingEntity2 instanceof Player) {
                ItemStack itemStack;
                botEntity = (Player)livingEntity2;
                object = ((PlayerDataHandler)bFAbstractManager.getPlayerDataHandler()).getPlayerData((Player)botEntity);
                if (BFUtils.isPlayerUnavailable((Player)botEntity, object)) {
                    bl = false;
                }
                if ((itemStack = botEntity.getMainHandItem()).getItem() instanceof GunItem && GunItem.isReloading(itemStack)) {
                    bl2 = false;
                }
            }
        }
        return new EntityCollisionEntry(l, entity.getId(), entity.position(), entity.getBoundingBox(), entity.getDeltaMovement(), entity.getYRot(), entity.getXRot(), bl, aABB, aABB2, bl2);
    }

    @NotNull
    public EntityCollisionEntry interpolate(@NotNull EntityCollisionEntry other, float t) {
        long l;
        if (this.entityId != other.entityId) {
            throw new IllegalArgumentException("Cannot interpolate between different entities");
        }
        Vec3 vec3 = this.position.lerp(other.position, (double)t);
        Vec3 vec32 = this.velocity.lerp(other.velocity, (double)t);
        AABB aABB = new AABB(this.boundingBox.minX + (other.boundingBox.minX - this.boundingBox.minX) * (double)t, this.boundingBox.minY + (other.boundingBox.minY - this.boundingBox.minY) * (double)t, this.boundingBox.minZ + (other.boundingBox.minZ - this.boundingBox.minZ) * (double)t, this.boundingBox.maxX + (other.boundingBox.maxX - this.boundingBox.maxX) * (double)t, this.boundingBox.maxY + (other.boundingBox.maxY - this.boundingBox.maxY) * (double)t, this.boundingBox.maxZ + (other.boundingBox.maxZ - this.boundingBox.maxZ) * (double)t);
        long l2 = l = t > 0.5f ? other.tickIndex : this.tickIndex;
        AABB aABB2 = this.headshotRegion != null && other.headshotRegion != null ? new AABB(this.headshotRegion.minX + (other.headshotRegion.minX - this.headshotRegion.minX) * (double)t, this.headshotRegion.minY + (other.headshotRegion.minY - this.headshotRegion.minY) * (double)t, this.headshotRegion.minZ + (other.headshotRegion.minZ - this.headshotRegion.minZ) * (double)t, this.headshotRegion.maxX + (other.headshotRegion.maxX - this.headshotRegion.maxX) * (double)t, this.headshotRegion.maxY + (other.headshotRegion.maxY - this.headshotRegion.maxY) * (double)t, this.headshotRegion.maxZ + (other.headshotRegion.maxZ - this.headshotRegion.maxZ) * (double)t) : (t > 0.5f ? other.headshotRegion : this.headshotRegion);
        AABB aABB3 = this.backpackRegion != null && other.backpackRegion != null ? new AABB(this.backpackRegion.minX + (other.backpackRegion.minX - this.backpackRegion.minX) * (double)t, this.backpackRegion.minY + (other.backpackRegion.minY - this.backpackRegion.minY) * (double)t, this.backpackRegion.minZ + (other.backpackRegion.minZ - this.backpackRegion.minZ) * (double)t, this.backpackRegion.maxX + (other.backpackRegion.maxX - this.backpackRegion.maxX) * (double)t, this.backpackRegion.maxY + (other.backpackRegion.maxY - this.backpackRegion.maxY) * (double)t, this.backpackRegion.maxZ + (other.backpackRegion.maxZ - this.backpackRegion.maxZ) * (double)t) : (t > 0.5f ? other.backpackRegion : this.backpackRegion);
        return new EntityCollisionEntry(l, this.entityId, vec3, aABB, vec32, this.yRot + (other.yRot - this.yRot) * t, this.xRot + (other.xRot - this.xRot) * t, this.isValid && other.isValid, aABB2, aABB3, this.canShoot && other.canShoot);
    }

    @Nullable
    private static AABB createHeadshotRegion(@NotNull LivingEntity entity) {
        if (!EntityCollisionEntry.hasHeadshotRegion(entity)) {
            return null;
        }
        Vec3 vec3 = entity.getEyePosition().add(HEADSHOT_EYE_OFFSET);
        return new AABB(vec3.subtract(HEADSHOT_REGION_SCALE), vec3.add(HEADSHOT_REGION_SCALE));
    }

    @Nullable
    private static AABB createBackpackRegion(@NotNull LivingEntity entity) {
        if (!(entity instanceof Player) && !(entity instanceof BotEntity)) {
            return null;
        }
        Vec3 vec3 = entity.getEyePosition().add(BACKPACK_EYE_OFFSET);
        Vec3 vec32 = BACKPACK_BACK_OFFSET.yRot(-entity.yBodyRot * ((float)Math.PI / 180) - 1.5707964f);
        Vec3 vec33 = vec3.add(vec32);
        return new AABB(vec33.subtract(BACKPACK_REGION_SCALE), vec33.add(BACKPACK_REGION_SCALE));
    }

    private static boolean hasHeadshotRegion(@NotNull LivingEntity entity) {
        return entity instanceof Player || entity instanceof BotEntity || entity instanceof InfectedEntity;
    }
}

