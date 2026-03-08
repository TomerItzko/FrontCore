/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.entity.goal;

import com.boehmod.blockfront.common.entity.BotEntity;
import com.boehmod.blockfront.common.entity.FlamethrowerFireEntity;
import com.boehmod.blockfront.common.entity.goal.ProjectileBotGoal;
import com.boehmod.blockfront.registry.custom.BotVoice;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class FlamethowerFireBotGoal
extends ProjectileBotGoal<FlamethrowerFireEntity> {
    public FlamethowerFireBotGoal(@NotNull BotEntity botEntity, float f) {
        super(botEntity, f);
    }

    @Override
    @NotNull
    protected Class<FlamethrowerFireEntity> getEntityClass() {
        return FlamethrowerFireEntity.class;
    }

    @Override
    protected boolean method_2038(@NotNull FlamethrowerFireEntity flamethrowerFireEntity) {
        return true;
    }

    @Override
    protected boolean method_2039(FlamethrowerFireEntity flamethrowerFireEntity) {
        this.botEntity.method_1975(true);
        this.botEntity.method_2011().ifPresent(deferredHolder -> this.botEntity.method_1993(((BotVoice)deferredHolder.get()).hurtSound()));
        return true;
    }

    @Override
    protected /* synthetic */ boolean method_2039(Entity entity) {
        return this.method_2039((FlamethrowerFireEntity)entity);
    }

    @Override
    protected /* synthetic */ boolean method_2038(@NotNull Entity entity) {
        return this.method_2038((FlamethrowerFireEntity)entity);
    }
}

