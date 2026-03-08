/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.entity.goal;

import com.boehmod.blockfront.common.entity.BotEntity;
import com.boehmod.blockfront.common.entity.GrenadeEntity;
import com.boehmod.blockfront.common.entity.goal.ProjectileBotGoal;
import com.boehmod.blockfront.common.item.GrenadeFragItem;
import com.boehmod.blockfront.registry.BFParticleTypes;
import com.boehmod.blockfront.registry.custom.BotVoice;
import com.boehmod.blockfront.util.BFUtils;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GrenadeBotGoal
extends ProjectileBotGoal<GrenadeEntity> {
    public GrenadeBotGoal(@NotNull BotEntity botEntity, float f) {
        super(botEntity, f);
    }

    @Override
    @NotNull
    protected Class<GrenadeEntity> getEntityClass() {
        return GrenadeEntity.class;
    }

    @Override
    protected boolean method_2038(@NotNull GrenadeEntity grenadeEntity) {
        Player player = grenadeEntity.getOwner();
        if (player == null) {
            return true;
        }
        return !this.botEntity.method_1990(player);
    }

    @Override
    protected boolean method_2039(GrenadeEntity grenadeEntity) {
        if (grenadeEntity.tickCount < 10 || !this.botEntity.hasLineOfSight((Entity)grenadeEntity)) {
            return false;
        }
        ItemStack itemStack = grenadeEntity.getItem();
        if (itemStack.isEmpty()) {
            return false;
        }
        Object object = itemStack.getItem();
        if (!(object instanceof GrenadeFragItem)) {
            return false;
        }
        GrenadeFragItem grenadeFragItem = (GrenadeFragItem)object;
        object = grenadeFragItem.method_4087();
        if (object != GrenadeFragItem.Type.FRAG && object != GrenadeFragItem.Type.FIRE) {
            return false;
        }
        this.botEntity.method_2011().ifPresent(deferredHolder -> this.botEntity.method_1993(((BotVoice)deferredHolder.get()).grenadeSound()));
        return true;
    }

    @Override
    public void start() {
        super.start();
        BFUtils.method_2964((LivingEntity)this.botEntity, (ParticleOptions)BFParticleTypes.ALERT.get(), this.botEntity.getRandom(), 0.0f, 1.0f, 0.0f, 5);
    }

    @Override
    protected /* synthetic */ boolean method_2039(Entity entity) {
        return this.method_2039((GrenadeEntity)entity);
    }

    @Override
    protected /* synthetic */ boolean method_2038(@NotNull Entity entity) {
        return this.method_2038((GrenadeEntity)entity);
    }
}

