/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.entity.goal;

import com.boehmod.blockfront.common.entity.BotEntity;
import com.boehmod.blockfront.common.entity.goal.BotGoal;
import java.util.EnumSet;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public abstract class ProjectileBotGoal<T extends Entity>
extends BotGoal {
    private static final int field_2407 = 20;
    public static final float field_2405 = 0.6f;
    private int field_2408 = 0;
    protected final float field_2406;
    @NotNull
    protected final PathNavigation field_2403;
    @Nullable
    protected T field_2404;
    @Nullable
    protected Path field_2401;
    @NotNull
    protected final Predicate<T> field_2402 = entity -> this.method_2038(entity) && entity.isAlive();

    public ProjectileBotGoal(@NotNull BotEntity botEntity, float f) {
        super(botEntity);
        this.field_2406 = f;
        this.field_2403 = botEntity.getNavigation();
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @NotNull
    protected abstract Class<T> getEntityClass();

    protected abstract boolean method_2038(@NotNull T var1);

    protected abstract boolean method_2039(T var1);

    @Override
    public boolean canUse() {
        Entity entity2;
        Level level = this.botEntity.level();
        this.field_2404 = null;
        double d = 0.0;
        for (Entity entity2 : level.getEntitiesOfClass(this.getEntityClass(), AABB.unitCubeFromLowerCorner((Vec3)this.botEntity.position()).inflate((double)this.field_2406, (double)this.field_2406, (double)this.field_2406), this.field_2402)) {
            if (!this.method_2039(entity2)) continue;
            double d2 = entity2.distanceTo((Entity)this.botEntity);
            if (this.field_2404 != null && !(d2 < d)) continue;
            this.field_2404 = entity2;
            d = d2;
        }
        if (this.field_2404 == null) {
            return false;
        }
        Vec3 vec3 = this.field_2404.position();
        entity2 = DefaultRandomPos.getPosAway((PathfinderMob)this.botEntity, (int)16, (int)7, (Vec3)vec3);
        if (entity2 == null || entity2.distanceTo(vec3) < (double)this.field_2404.distanceTo((Entity)this.botEntity)) {
            return false;
        }
        this.field_2401 = this.field_2403.createPath(entity2.x, entity2.y, entity2.z, 0);
        return this.field_2401 != null;
    }

    public boolean canContinueToUse() {
        if (this.field_2404 == null || this.field_2404.isRemoved() || this.botEntity.distanceTo((Entity)this.field_2404) > this.field_2406) {
            return false;
        }
        if (--this.field_2408 <= 0) {
            this.field_2408 = 20;
            Vec3 vec3 = DefaultRandomPos.getPosAway((PathfinderMob)this.botEntity, (int)16, (int)7, (Vec3)this.field_2404.position());
            if (vec3 != null) {
                this.field_2401 = this.field_2403.createPath(vec3.x, vec3.y, vec3.z, 0);
                if (this.field_2401 != null) {
                    this.field_2403.moveTo(this.field_2401, (double)0.6f);
                }
            }
        }
        return !this.field_2403.isDone();
    }

    public void start() {
        super.start();
        this.botEntity.method_1975(true);
        this.field_2403.moveTo(this.field_2401, (double)0.6f);
    }

    public void stop() {
        super.stop();
        this.field_2404 = null;
        this.botEntity.method_1975(false);
    }
}

