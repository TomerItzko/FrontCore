/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.gun.bullet.EntityCollisionEntry;
import com.boehmod.blockfront.server.ac.bullet.ServerCollisionTracker;
import com.boehmod.blockfront.unnamed.BF_1194;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BF_1195
implements BF_1194 {
    @NotNull
    private final ServerCollisionTracker field_6881;
    private final long field_6882;
    private final int field_6883;

    public BF_1195(@NotNull ServerCollisionTracker serverCollisionTracker, long l, int n) {
        this.field_6881 = serverCollisionTracker;
        this.field_6882 = l;
        this.field_6883 = n;
    }

    @Override
    @NotNull
    public List<EntityCollisionEntry> method_5833(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull AABB aABB, @Nullable LivingEntity livingEntity) {
        ObjectArrayList objectArrayList = new ObjectArrayList();
        for (long i = this.field_6882 - (long)this.field_6883; i <= this.field_6882 + (long)this.field_6883; ++i) {
            if (!this.field_6881.method_5841(i)) continue;
            Collection<EntityCollisionEntry> collection = this.field_6881.method_5836(i);
            for (EntityCollisionEntry entityCollisionEntry : collection) {
                if (livingEntity != null && entityCollisionEntry.entityId() == livingEntity.getId() || !entityCollisionEntry.isValid() || !entityCollisionEntry.boundingBox().intersects(aABB)) continue;
                objectArrayList.add(entityCollisionEntry);
            }
        }
        return objectArrayList;
    }

    @Override
    @Nullable
    public EntityCollisionEntry method_5832(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, int n, @Nullable LivingEntity livingEntity) {
        return this.field_6881.method_5838(this.field_6882, n, this.field_6883);
    }
}

