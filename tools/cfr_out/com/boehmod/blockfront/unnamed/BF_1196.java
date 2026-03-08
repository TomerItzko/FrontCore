/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.AABB
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.entity.BFProjectileEntity;
import com.boehmod.blockfront.common.gun.bullet.EntityCollisionEntry;
import com.boehmod.blockfront.unnamed.BF_1194;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BF_1196
implements BF_1194 {
    @NotNull
    private final Level field_6884;
    private final int field_6885;

    public BF_1196(@NotNull Level level, int n) {
        this.field_6884 = level;
        this.field_6885 = n;
    }

    @Override
    @NotNull
    public List<EntityCollisionEntry> method_5833(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull AABB aABB, @Nullable LivingEntity livingEntity) {
        ObjectArrayList objectArrayList = new ObjectArrayList();
        List list = this.field_6884.getEntities((Entity)livingEntity, aABB);
        for (Entity entity : list) {
            if (entity instanceof LivingEntity) {
                objectArrayList.add(EntityCollisionEntry.method_5817(bFAbstractManager, this.field_6885, entity, livingEntity));
                continue;
            }
            if (!(entity instanceof BFProjectileEntity)) continue;
            objectArrayList.add(EntityCollisionEntry.method_5817(bFAbstractManager, this.field_6885, entity, livingEntity));
        }
        return objectArrayList;
    }

    @Override
    @Nullable
    public EntityCollisionEntry method_5832(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, int n, @Nullable LivingEntity livingEntity) {
        Entity entity = this.field_6884.getEntity(n);
        if (entity instanceof LivingEntity && entity.isAlive()) {
            return EntityCollisionEntry.method_5817(bFAbstractManager, this.field_6885, entity, livingEntity);
        }
        return null;
    }
}

