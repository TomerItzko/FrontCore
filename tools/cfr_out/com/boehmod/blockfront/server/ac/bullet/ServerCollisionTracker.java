/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.ints.Int2ObjectMap
 *  it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.boehmod.blockfront.server.ac.bullet;

import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.entity.BFProjectileEntity;
import com.boehmod.blockfront.common.gun.bullet.EntityCollisionEntry;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ServerCollisionTracker {
    private static final int field_6895 = 60;
    @NotNull
    private final Map<Long, Int2ObjectMap<EntityCollisionEntry>> field_6892 = new LinkedHashMap<Long, Int2ObjectMap<EntityCollisionEntry>>(){

        @Override
        protected boolean removeEldestEntry(Map.Entry<Long, Int2ObjectMap<EntityCollisionEntry>> entry) {
            return this.size() > 60;
        }
    };
    private long currentTick = 0L;

    public void update(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull Iterable<Entity> entities, long currentTick, @Nullable LivingEntity livingEntity) {
        this.currentTick = currentTick;
        Int2ObjectOpenHashMap int2ObjectOpenHashMap = new Int2ObjectOpenHashMap();
        for (Entity entity : entities) {
            Object object;
            if (entity instanceof LivingEntity && !(object = (LivingEntity)entity).isDeadOrDying()) {
                EntityCollisionEntry entityCollisionEntry = EntityCollisionEntry.method_5817(manager, this.currentTick, entity, livingEntity);
                int2ObjectOpenHashMap.put(entity.getId(), (Object)entityCollisionEntry);
            }
            if (!(entity instanceof BFProjectileEntity)) continue;
            object = EntityCollisionEntry.method_5817(manager, this.currentTick, entity, livingEntity);
            int2ObjectOpenHashMap.put(entity.getId(), object);
        }
        this.field_6892.put(this.currentTick, (Int2ObjectMap<EntityCollisionEntry>)int2ObjectOpenHashMap);
    }

    @Nullable
    public EntityCollisionEntry method_5837(long l, int n) {
        Int2ObjectMap<EntityCollisionEntry> int2ObjectMap = this.field_6892.get(l);
        return int2ObjectMap != null ? (EntityCollisionEntry)int2ObjectMap.get(n) : null;
    }

    @Nullable
    public EntityCollisionEntry method_5838(long l, int n, int n2) {
        EntityCollisionEntry entityCollisionEntry = this.method_5837(l, n);
        if (entityCollisionEntry != null) {
            return entityCollisionEntry;
        }
        EntityCollisionEntry entityCollisionEntry2 = null;
        int n3 = Integer.MAX_VALUE;
        for (int i = 1; i <= n2; ++i) {
            EntityCollisionEntry entityCollisionEntry3 = this.method_5837(l + (long)i, n);
            EntityCollisionEntry entityCollisionEntry4 = this.method_5837(l - (long)i, n);
            if (entityCollisionEntry3 != null && i < n3) {
                entityCollisionEntry2 = entityCollisionEntry3;
                n3 = i;
            }
            if (entityCollisionEntry4 == null || i >= n3) continue;
            entityCollisionEntry2 = entityCollisionEntry4;
            n3 = i;
        }
        return entityCollisionEntry2;
    }

    public boolean method_5841(long l) {
        return this.field_6892.containsKey(l);
    }

    public long method_5842() {
        return this.currentTick;
    }

    public long method_5843() {
        return this.field_6892.isEmpty() ? -1L : this.field_6892.keySet().iterator().next();
    }

    public void method_5840() {
        this.field_6892.clear();
    }

    public int method_5844() {
        Int2ObjectMap<EntityCollisionEntry> int2ObjectMap = this.field_6892.get(this.currentTick);
        return int2ObjectMap != null ? int2ObjectMap.size() : 0;
    }

    @NotNull
    public Collection<EntityCollisionEntry> method_5836(long l) {
        Int2ObjectMap<EntityCollisionEntry> int2ObjectMap = this.field_6892.get(l);
        return int2ObjectMap != null ? int2ObjectMap.values() : Collections.emptyList();
    }
}

