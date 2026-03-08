/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.common.entity.HumanEntity;
import com.boehmod.blockfront.game.impl.boot.BootcampNpcSpawn;
import java.util.List;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import org.jetbrains.annotations.NotNull;

public final class BF_564
extends Goal {
    private final HumanEntity field_2352;
    private final PathNavigation field_2353;
    private boolean field_2354 = false;
    private int field_2355;

    public BF_564(@NotNull HumanEntity humanEntity) {
        this.field_2352 = humanEntity;
        this.field_2353 = humanEntity.getNavigation();
    }

    public void start() {
        this.field_2355 = 0;
    }

    public boolean canUse() {
        return !this.field_2352.method_1973().isEmpty();
    }

    public void tick() {
        BootcampNpcSpawn.BF_758 bF_758;
        super.tick();
        List<BootcampNpcSpawn.BF_758> list = this.field_2352.method_1973();
        int n = list.size();
        if (this.field_2352.field_2351 < 0) {
            this.field_2354 = false;
            this.field_2352.field_2351 = 0;
        }
        if (this.field_2352.field_2351 >= n) {
            this.field_2354 = true;
            this.field_2352.field_2351 = n - 1;
        }
        if ((bF_758 = list.get(this.field_2352.field_2351)) != null) {
            if (this.field_2352.distanceToSqr(bF_758.field_3249.x, bF_758.field_3249.y, bF_758.field_3249.z) <= 4.0) {
                this.field_2352.field_2351 = this.field_2352.field_2351 + (this.field_2354 ? -1 : 1);
                return;
            }
            if (--this.field_2355 <= 0) {
                this.field_2355 = 10;
                this.field_2353.moveTo(bF_758.field_3249.x, bF_758.field_3249.y, bF_758.field_3249.z, 0.45);
            }
        }
    }

    public boolean canContinueToUse() {
        return !this.field_2353.isDone();
    }

    public void stop() {
        super.stop();
        this.field_2353.stop();
    }
}

