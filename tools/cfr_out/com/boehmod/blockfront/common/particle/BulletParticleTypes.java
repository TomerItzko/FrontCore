/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.particle;

import com.boehmod.blockfront.common.particle.BulletParticles;
import com.boehmod.blockfront.registry.BFParticleTypes;
import org.jetbrains.annotations.NotNull;

public class BulletParticleTypes {
    @NotNull
    public static final BulletParticles BASIC = new BulletParticles();
    @NotNull
    public static final BulletParticles STRONG = new BulletParticles().setImpactFlash(BFParticleTypes.BULLET_IMPACT_FLASH_STRONG);
}

