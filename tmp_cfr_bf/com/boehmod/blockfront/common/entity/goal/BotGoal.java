/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.entity.goal;

import com.boehmod.blockfront.common.entity.BotEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import org.jetbrains.annotations.NotNull;

public class BotGoal
extends Goal {
    @NotNull
    protected final BotEntity botEntity;

    public BotGoal(@NotNull BotEntity botEntity) {
        this.botEntity = botEntity;
    }

    @NotNull
    public final BotEntity getBotEntity() {
        return this.botEntity;
    }

    public boolean canUse() {
        return false;
    }
}

