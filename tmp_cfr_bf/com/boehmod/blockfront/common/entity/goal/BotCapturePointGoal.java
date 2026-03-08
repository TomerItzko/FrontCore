/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.entity.goal;

import com.boehmod.blockfront.common.entity.BotEntity;
import com.boehmod.blockfront.common.entity.goal.BotGoal;
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.game.tag.IHasBots;
import com.boehmod.blockfront.registry.custom.BotVoice;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class BotCapturePointGoal
extends BotGoal {
    private static final int field_2440 = 20;
    private static final int field_2436 = 5;
    private static final int field_2437 = 3;
    private static final int field_2438 = 7;
    private static final float field_2433 = 2.0f;
    private static final float field_2434 = 3.0f;
    private static final float field_2435 = 0.2f;
    private static final double field_2428 = 0.15;
    @NotNull
    protected final PathNavigation field_2431;
    private final double field_2429;
    private BlockPos field_2430;
    private int field_2439 = 0;
    private Vec3 field_2442;
    private boolean field_2432 = true;

    public BotCapturePointGoal(@NotNull BotEntity botEntity, double d) {
        super(botEntity);
        this.field_2429 = d;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        this.field_2431 = botEntity.getNavigation();
    }

    @Override
    public boolean canUse() {
        if (--this.field_2439 > 0) {
            return false;
        }
        if (this.botEntity.getTarget() != null) {
            return false;
        }
        Vec3 vec3 = this.botEntity.getGame();
        if (vec3 instanceof IHasBots) {
            IHasBots iHasBots = (IHasBots)vec3;
            this.field_2430 = iHasBots.method_3388(this.botEntity);
            if (this.field_2430 == null) {
                return false;
            }
            vec3 = Vec3.atBottomCenterOf((Vec3i)this.field_2430);
            double d = Math.sqrt(this.botEntity.distanceToSqr(vec3));
            this.field_2439 = Math.max(5, (int)(20.0 * Math.min(1.0, d / 100.0)));
            if (d <= 2.0 + Math.random() * 1.0) {
                return false;
            }
            int n = (int)Math.max(3.0, Math.min(7.0, d / 4.0));
            this.field_2442 = LandRandomPos.getPosTowards((PathfinderMob)this.botEntity, (int)((int)d), (int)n, (Vec3)vec3);
            return this.field_2442 != null && this.field_2432;
        }
        return false;
    }

    public boolean canContinueToUse() {
        if (this.botEntity.method_2008() || this.botEntity.getTarget() != null) {
            this.field_2431.stop();
            this.field_2432 = true;
            return false;
        }
        if (this.botEntity.getGame() != null && this.botEntity.getGame().getStatus() == GameStatus.POST_GAME) {
            this.field_2432 = true;
            return false;
        }
        return !this.field_2431.isDone();
    }

    public void stop() {
        super.stop();
        this.field_2430 = null;
        this.field_2442 = null;
        this.field_2432 = true;
    }

    public void start() {
        super.start();
        if (this.botEntity.getTarget() != null || this.field_2430 == null || this.field_2442 == null || !this.field_2432) {
            return;
        }
        double d = this.field_2429 * (0.85 + Math.random() * 0.15 * 2.0);
        this.field_2431.moveTo(this.field_2442.x, this.field_2442.y, this.field_2442.z, d);
        this.botEntity.method_1989(this.field_2430);
        this.field_2432 = false;
        if (Math.random() < (double)0.2f) {
            this.botEntity.method_2011().ifPresent(deferredHolder -> this.botEntity.method_1993(((BotVoice)deferredHolder.get()).goSound()));
        }
    }
}

