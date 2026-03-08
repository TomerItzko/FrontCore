/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.util.math.MathUtils;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class BF_389
extends AbstractTickableSoundInstance {
    @NotNull
    private final Entity field_1809;
    private final boolean field_1810;

    public BF_389(@NotNull Entity entity, @NotNull SoundEvent soundEvent, @NotNull SoundSource soundSource, boolean bl) {
        super(soundEvent, soundSource, SoundInstance.createUnseededRandom());
        this.field_1809 = entity;
        this.looping = true;
        this.volume = 0.0f;
        this.field_1810 = bl;
        Vec3 vec3 = entity.position();
        this.x = vec3.x;
        this.y = vec3.y;
        this.z = vec3.z;
    }

    public boolean canPlaySound() {
        return !this.field_1809.isSilent();
    }

    public boolean canStartSilent() {
        return true;
    }

    public void tick() {
        float f;
        LivingEntity livingEntity;
        Entity entity;
        if (this.field_1809.isRemoved() || !this.field_1809.isAlive() || (entity = this.field_1809) instanceof LivingEntity && (livingEntity = (LivingEntity)entity).getHealth() <= 0.0f) {
            this.stop();
            return;
        }
        if (this.field_1810) {
            double d = this.field_1809.getX() - this.field_1809.xOld;
            double d2 = this.field_1809.getZ() - this.field_1809.zOld;
            float f2 = (float)Math.sqrt(d * d + d2 * d2);
            f = this.field_1809.onGround() && !this.field_1809.isShiftKeyDown() && f2 > 0.01f ? 0.25f : 0.0f;
        } else {
            f = 0.25f;
        }
        this.volume = MathUtils.moveTowards(this.volume, f, 0.05f);
        Vec3 vec3 = this.field_1809.position();
        this.x = vec3.x;
        this.y = vec3.y;
        this.z = vec3.z;
    }
}

