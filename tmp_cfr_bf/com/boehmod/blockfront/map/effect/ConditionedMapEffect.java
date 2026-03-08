/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.map.effect;

import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.map.effect.MapEffectCondition;
import com.boehmod.blockfront.map.effect.PositionedMapEffect;
import com.boehmod.blockfront.util.BFLog;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public abstract class ConditionedMapEffect
extends PositionedMapEffect {
    @NotNull
    private String effectId;
    @NotNull
    private MapEffectCondition conditionType;
    private boolean isReset = false;

    public ConditionedMapEffect() {
        this(Vec3.ZERO, "Unknown", MapEffectCondition.DOMINATION_HALF_SCORE);
    }

    public ConditionedMapEffect(@NotNull Vec3 vec3, @NotNull String string, @NotNull MapEffectCondition mapEffectCondition) {
        super(vec3);
        this.effectId = string;
        this.conditionType = mapEffectCondition;
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public void updateGameClient(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull Random random, @NotNull AbstractGame<?, ?, ?> game, @NotNull LocalPlayer player, @NotNull ClientLevel level, float delta) {
        if (this.isReset) {
            return;
        }
        if (this.conditionType.condition.apply(game).booleanValue()) {
            this.method_3124(minecraft, level, game);
        }
    }

    @OnlyIn(value=Dist.CLIENT)
    public void method_3124(@NotNull Minecraft minecraft, @NotNull ClientLevel clientLevel, @NotNull AbstractGame<?, ?, ?> abstractGame) {
        this.isReset = true;
        this.onConditionMet(minecraft, clientLevel, abstractGame);
    }

    @OnlyIn(value=Dist.CLIENT)
    public abstract void onConditionMet(@NotNull Minecraft var1, @NotNull ClientLevel var2, @NotNull AbstractGame<?, ?, ?> var3);

    @OnlyIn(value=Dist.CLIENT)
    public void reset(@NotNull Minecraft minecraft) {
        BFLog.log("Resetting map effect '" + this.effectId + "'", new Object[0]);
        this.isReset = false;
        this.resetInternal(minecraft);
    }

    @OnlyIn(value=Dist.CLIENT)
    public abstract void resetInternal(@NotNull Minecraft var1);

    @NotNull
    public String getEffectId() {
        return this.effectId;
    }

    public boolean isReset() {
        return this.isReset;
    }

    @Override
    public void writeToFDS(@NotNull FDSTagCompound fDSTagCompound) {
        super.writeToFDS(fDSTagCompound);
        fDSTagCompound.setInteger("conditionType", this.conditionType.ordinal());
        fDSTagCompound.setString("effectId", this.effectId);
    }

    @Override
    public void readFromFDS(@NotNull FDSTagCompound fDSTagCompound) {
        super.readFromFDS(fDSTagCompound);
        this.conditionType = MapEffectCondition.values()[fDSTagCompound.getInteger("conditionType")];
        this.effectId = fDSTagCompound.getString("effectId");
    }
}

