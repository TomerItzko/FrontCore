/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.gun;

import com.boehmod.blockfront.common.entity.base.IProducedProjectileEntity;
import com.boehmod.blockfront.common.gun.GunFireMode;
import com.boehmod.blockfront.common.gun.GunTriggerSpawnType;
import java.util.function.Supplier;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class GunFireConfig {
    @Nullable
    Supplier<EntityType<? extends IProducedProjectileEntity>> field_3916 = null;
    private final GunTriggerSpawnType type;
    @NotNull
    private final GunFireMode mode;
    private final int fireRate;
    private final int field_3915;

    public GunFireConfig(@NotNull GunFireMode gunFireMode, int n) {
        this(gunFireMode, n, 1);
    }

    public GunFireConfig(@NotNull GunFireMode mode, int fireRate, int n) {
        this.mode = mode;
        this.fireRate = fireRate;
        this.type = GunTriggerSpawnType.BULLET;
        this.field_3915 = n;
    }

    public GunFireConfig(@NotNull GunFireMode gunFireMode, int n, @Nullable Supplier<EntityType<? extends IProducedProjectileEntity>> supplier) {
        this.mode = gunFireMode;
        this.fireRate = n;
        this.field_3916 = supplier;
        this.field_3915 = 1;
        this.type = GunTriggerSpawnType.ENTITY;
    }

    @NotNull
    public GunFireMode getMode() {
        return this.mode;
    }

    public int getFireRate() {
        return this.fireRate;
    }

    @Nullable
    public Supplier<EntityType<? extends IProducedProjectileEntity>> method_4024() {
        return this.field_3916;
    }

    public GunTriggerSpawnType method_4023() {
        return this.type;
    }

    public int method_4026() {
        return this.field_3915;
    }
}

