/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.setup;

import com.boehmod.blockfront.client.particle.AcidGroundSplatParticle;
import com.boehmod.blockfront.client.particle.AcidSplatParticle;
import com.boehmod.blockfront.client.particle.BloodGroundSplatParticle;
import com.boehmod.blockfront.client.particle.BloodImpactSmokeParticle;
import com.boehmod.blockfront.client.particle.BloodSplatParticle;
import com.boehmod.blockfront.client.particle.BulletImpactFlashBasicParticle;
import com.boehmod.blockfront.client.particle.BulletImpactFlashStrongParticle;
import com.boehmod.blockfront.client.particle.BulletImpactSmokeParticle;
import com.boehmod.blockfront.client.particle.BulletParticle;
import com.boehmod.blockfront.client.particle.BulletSparkParticle;
import com.boehmod.blockfront.client.particle.CloudParticle;
import com.boehmod.blockfront.client.particle.ElectricSparkParticle;
import com.boehmod.blockfront.client.particle.FallingLeafParticle;
import com.boehmod.blockfront.client.particle.FallingSnowParticle;
import com.boehmod.blockfront.client.particle.FarSmokeParticle;
import com.boehmod.blockfront.client.particle.GrenadeFlashParticle;
import com.boehmod.blockfront.client.particle.GunFlashParticle;
import com.boehmod.blockfront.client.particle.LongGrenadeSmokeParticle;
import com.boehmod.blockfront.client.particle.OilParticle;
import com.boehmod.blockfront.client.particle.PoofParticle;
import com.boehmod.blockfront.client.particle.PowerfulBloodSplatParticle;
import com.boehmod.blockfront.client.particle.RaindropParticle;
import com.boehmod.blockfront.client.particle.ShrapnelSmokeParticle;
import com.boehmod.blockfront.registry.BFParticleTypes;
import com.boehmod.blockfront.unnamed.BF_1209;
import com.boehmod.blockfront.unnamed.BF_1215;
import com.boehmod.blockfront.unnamed.BF_1217;
import com.boehmod.blockfront.unnamed.BF_1218;
import com.boehmod.blockfront.unnamed.BF_1221;
import net.minecraft.client.particle.HeartParticle;
import net.minecraft.core.particles.ParticleType;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import org.jetbrains.annotations.NotNull;

public class BFParticleProvidersSetup {
    public static void register(@NotNull RegisterParticleProvidersEvent event) {
        event.registerSpriteSet((ParticleType)BFParticleTypes.FAR_SMOKE_PARTICLE.get(), FarSmokeParticle.Provider::new);
        event.registerSpriteSet((ParticleType)BFParticleTypes.BLOOD_SPLAT_PARTICLE.get(), BloodSplatParticle.Provider::new);
        event.registerSpriteSet((ParticleType)BFParticleTypes.BLOOD_GROUND_SPLAT_PARTICLE.get(), BloodGroundSplatParticle.Provider::new);
        event.registerSpriteSet((ParticleType)BFParticleTypes.FALLING_LEAF_PARTICLE.get(), FallingLeafParticle.Provider::new);
        event.registerSpriteSet((ParticleType)BFParticleTypes.ACID_SPLAT_PARTICLE.get(), AcidSplatParticle.Provider::new);
        event.registerSpriteSet((ParticleType)BFParticleTypes.ACID_GROUND_SPLAT_PARTICLE.get(), AcidGroundSplatParticle.Provider::new);
        event.registerSpriteSet((ParticleType)BFParticleTypes.GRENADE_FLASH.get(), GrenadeFlashParticle.Provider::new);
        event.registerSpriteSet((ParticleType)BFParticleTypes.LONG_GRENADE_SMOKE.get(), LongGrenadeSmokeParticle.Provider::new);
        event.registerSpriteSet((ParticleType)BFParticleTypes.BULLET_PARTICLE.get(), BulletParticle.Provider::new);
        event.registerSpriteSet((ParticleType)BFParticleTypes.SURRENDER_PAPER.get(), BF_1218.BF_1219::new);
        event.registerSpriteSet((ParticleType)BFParticleTypes.PAPER.get(), BF_1217.BF_265::new);
        event.registerSpriteSet((ParticleType)BFParticleTypes.POWERFUL_BLOOD_SPLAT_PARTICLE.get(), PowerfulBloodSplatParticle.Provider::new);
        event.registerSpriteSet((ParticleType)BFParticleTypes.ELECTRIC_SPARK_PARTICLE.get(), ElectricSparkParticle.Provider::new);
        event.registerSpriteSet((ParticleType)BFParticleTypes.BULLET_SPARK_PARTICLE.get(), BulletSparkParticle.Provider::new);
        event.registerSpriteSet((ParticleType)BFParticleTypes.BULLET_IMPACT_SMOKE.get(), BulletImpactSmokeParticle.Provider::new);
        event.registerSpriteSet((ParticleType)BFParticleTypes.BULLET_IMPACT_SMOKE_SNOW.get(), BulletImpactSmokeParticle.Provider::new);
        event.registerSpriteSet((ParticleType)BFParticleTypes.BULLET_IMPACT_LARGE_SMOKE.get(), BF_1221.BF_1222::new);
        event.registerSpriteSet((ParticleType)BFParticleTypes.BULLET_IMPACT_SNOW.get(), BF_1209.BF_1210::new);
        event.registerSpriteSet((ParticleType)BFParticleTypes.SHRAPNEL_SMOKE.get(), ShrapnelSmokeParticle.Provider::new);
        event.registerSpriteSet((ParticleType)BFParticleTypes.BLOOD_IMPACT_SMOKE.get(), BloodImpactSmokeParticle.Provider::new);
        event.registerSpriteSet((ParticleType)BFParticleTypes.ALERT.get(), HeartParticle.AngryVillagerProvider::new);
        event.registerSpriteSet((ParticleType)BFParticleTypes.HEAL.get(), HeartParticle.AngryVillagerProvider::new);
        event.registerSpriteSet((ParticleType)BFParticleTypes.BULLET_IMPACT_FLASH_BASIC.get(), BulletImpactFlashBasicParticle.Provider::new);
        event.registerSpriteSet((ParticleType)BFParticleTypes.BULLET_IMPACT_FLASH_STRONG.get(), BulletImpactFlashStrongParticle.Provider::new);
        event.registerSpriteSet((ParticleType)BFParticleTypes.GUN_FLASH.get(), GunFlashParticle.Provider::new);
        event.registerSpriteSet((ParticleType)BFParticleTypes.FALLING_SNOW_PARTICLE.get(), FallingSnowParticle.Provider::new);
        event.registerSpriteSet((ParticleType)BFParticleTypes.FALLING_SNOW_FAST_PARTICLE.get(), BF_1215.BF_1216::new);
        event.registerSpriteSet((ParticleType)BFParticleTypes.POOF_PARTICLE.get(), PoofParticle.Provider::new);
        event.registerSpriteSet((ParticleType)BFParticleTypes.CLOUD_PARTICLE.get(), CloudParticle.Provider::new);
        event.registerSpriteSet((ParticleType)BFParticleTypes.RAINDROP.get(), RaindropParticle.Provider::new);
        event.registerSpriteSet((ParticleType)BFParticleTypes.OIL_PARTICLE.get(), OilParticle.Provider::new);
    }
}

