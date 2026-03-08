/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.client.sounds.SoundManager
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.boehmod.blockfront.common.entity;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.sound.entity.TankRocketSoundInstance;
import com.boehmod.blockfront.common.entity.RocketEntity;
import com.boehmod.blockfront.registry.BFSounds;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TankRocketEntity
extends RocketEntity {
    private static final int field_2778 = 120;
    @Nullable
    private TankRocketSoundInstance field_2777 = null;

    public TankRocketEntity(@NotNull EntityType<? extends RocketEntity> entityType, @NotNull Level level) {
        super(entityType, level);
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    protected void method_2502(@NotNull ThreadLocalRandom threadLocalRandom, @NotNull Level level, @NotNull Vec3 vec3) {
        super.method_2502(threadLocalRandom, level, vec3);
        Minecraft minecraft = Minecraft.getInstance();
        if (this.field_2777 == null) {
            SoundManager soundManager = minecraft.getSoundManager();
            float f = 0.95f + threadLocalRandom.nextFloat() * 0.1f;
            this.field_2777 = new TankRocketSoundInstance(this, (SoundEvent)BFSounds.ENTITY_ROCKET_TANK_FIRE.get(), SoundSource.PLAYERS, f, 15.0f);
            soundManager.play((SoundInstance)this.field_2777);
        }
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    protected void method_2503(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientLevel clientLevel, @NotNull Vec3 vec3) {
        super.method_2503(minecraft, bFClientManager, clientLevel, vec3);
    }

    @Override
    public SoundEvent method_2511() {
        return (SoundEvent)BFSounds.ENTITY_ROCKET_TANK_FLYBY.get();
    }

    @Override
    @NotNull
    public SoundEvent method_2512() {
        return (SoundEvent)BFSounds.ENTITY_ROCKET_TANK_FIRE.get();
    }

    @Override
    public int method_2508() {
        return 120;
    }

    @Override
    protected boolean method_2507() {
        return false;
    }

    @Override
    protected boolean method_2506() {
        return false;
    }
}

