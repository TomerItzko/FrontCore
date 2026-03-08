/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.event.tick.GunAimingTickable;
import com.boehmod.blockfront.client.event.tick.PlayerTickable;
import com.boehmod.blockfront.common.item.GunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class BF_398
extends AbstractTickableSoundInstance {
    private float field_1820;
    private float field_1821 = 0.0f;

    public BF_398(@NotNull SoundEvent soundEvent) {
        super(soundEvent, SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
        this.looping = true;
        this.attenuation = SoundInstance.Attenuation.NONE;
        this.volume = 0.0f;
    }

    public boolean canPlaySound() {
        return Minecraft.getInstance().player != null;
    }

    public boolean canStartSilent() {
        return true;
    }

    public void tick() {
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if (localPlayer == null) {
            return;
        }
        if (bFClientManager.getCinematics().isSequencePlaying()) {
            this.volume = 0.0f;
            return;
        }
        this.method_1407(localPlayer);
        this.method_1408(localPlayer);
    }

    private void method_1407(@NotNull LocalPlayer localPlayer) {
        Vec3 vec3 = localPlayer.position();
        this.x = vec3.x;
        this.y = vec3.y;
        this.z = vec3.z;
    }

    private void method_1408(@NotNull LocalPlayer localPlayer) {
        float f = localPlayer.getXRot();
        float f2 = localPlayer.getYRot();
        float f3 = this.field_1820 - f;
        float f4 = this.field_1821 - f2;
        boolean bl = !localPlayer.isCreative();
        this.method_1409(localPlayer);
        if (bl) {
            this.method_1406(f3, f4);
        }
        if (this.volume > 1.0f) {
            this.volume = 1.0f;
        }
        this.volume = Mth.lerp((float)0.2f, (float)this.volume, (float)0.0f);
        this.field_1820 = f;
        this.field_1821 = f2;
    }

    private void method_1409(@NotNull LocalPlayer localPlayer) {
        ItemStack itemStack = localPlayer.getMainHandItem();
        if (!itemStack.isEmpty() && itemStack.getItem() instanceof GunItem && GunItem.isReloading(itemStack)) {
            this.volume += 0.005f;
        }
    }

    private void method_1406(float f, float f2) {
        if (this.volume >= 1.0f) {
            return;
        }
        float f3 = Mth.abs((float)(PlayerTickable.inspectionBlurPrev - PlayerTickable.inspectionBlur));
        this.volume += 0.002f * PlayerTickable.inspectionBlurPrev;
        float f4 = Mth.abs((float)(f * 0.005f)) + Mth.abs((float)(f2 * 0.005f)) + f3;
        this.volume += f4;
        this.volume += Mth.abs((float)(GunAimingTickable.field_167 - GunAimingTickable.field_168));
    }
}

