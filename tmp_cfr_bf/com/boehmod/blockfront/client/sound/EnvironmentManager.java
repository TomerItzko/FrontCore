/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.sound;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.map.MapEnvironment;
import com.boehmod.blockfront.unnamed.BF_399;
import com.boehmod.blockfront.util.BFLog;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EnvironmentManager {
    private static final float field_3267 = 16.0f;
    private static final int field_3268 = 3;
    private static final float field_6990 = 0.005f;
    @Nullable
    private final BF_399[] field_3263 = new BF_399[3];
    @Nullable
    private final BF_399[] field_3265 = new BF_399[3];
    @NotNull
    private final Vec3[] field_3264 = new Vec3[]{new Vec3(16.0, 0.0, 0.0), new Vec3(-8.0, 0.0, 16.0 * Math.sqrt(3.0) / 2.0), new Vec3(-8.0, 0.0, -16.0 * Math.sqrt(3.0) / 2.0)};
    private boolean field_6420 = false;
    private float field_6991 = 0.0f;

    public void method_5866(@NotNull Minecraft minecraft) {
        BFLog.log("Stopping all sound instances for the environment manager.", new Object[0]);
        SoundManager soundManager = minecraft.getSoundManager();
        Arrays.stream(this.field_3263).filter(Objects::nonNull).forEach(arg_0 -> ((SoundManager)soundManager).stop(arg_0));
        Arrays.stream(this.field_3265).filter(Objects::nonNull).forEach(arg_0 -> ((SoundManager)soundManager).stop(arg_0));
        Arrays.fill((Object[])this.field_3263, null);
        Arrays.fill((Object[])this.field_3265, null);
    }

    public float method_5928() {
        return this.field_6991;
    }

    public void method_3178(@NotNull Minecraft minecraft, @NotNull AbstractGame<?, ?, ?> abstractGame) {
        boolean bl;
        boolean bl2 = bl = minecraft.options.getSoundSourceVolume(SoundSource.MASTER) <= 0.0f || minecraft.options.getSoundSourceVolume(SoundSource.AMBIENT) <= 0.0f;
        if (this.field_6420 != bl) {
            this.field_6420 = bl;
            this.method_5866(minecraft);
        }
        if (bl) {
            return;
        }
        this.field_6991 += 0.005f;
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        SoundManager soundManager = minecraft.getSoundManager();
        float f = bFClientManager.getSkyTracker().getPercentage();
        MapEnvironment mapEnvironment = abstractGame.getMapEnvironment();
        Supplier<SoundEvent> supplier = mapEnvironment.getExteriorSound();
        Supplier<SoundEvent> supplier2 = mapEnvironment.getInteriorSound();
        for (int i = 0; i < 3; ++i) {
            Vec3 vec3 = this.field_3264[i];
            float f2 = 0.7f + (float)i * 0.25f;
            BF_399 bF_399 = this.field_3263[i];
            BF_399 bF_3992 = this.field_3265[i];
            if (bF_399 == null) {
                this.field_3263[i] = bF_399 = new BF_399(minecraft, abstractGame, supplier.get(), SoundSource.AMBIENT, vec3, f2, this, i);
            }
            if (bF_3992 == null) {
                this.field_3265[i] = bF_3992 = new BF_399(minecraft, abstractGame, supplier2.get(), SoundSource.AMBIENT, vec3, f2, this, i);
            }
            if (!soundManager.isActive((SoundInstance)bF_399)) {
                BFLog.log("Playing exterior ambient audio for offset index " + i, new Object[0]);
                soundManager.play((SoundInstance)bF_399);
            }
            if (!soundManager.isActive((SoundInstance)bF_3992)) {
                BFLog.log("Playing interior ambient audio for offset index " + i, new Object[0]);
                soundManager.play((SoundInstance)bF_3992);
            }
            bF_399.method_1410(f);
            bF_3992.method_1410(1.0f - f);
        }
    }

    public void method_5927(@Nonnull PoseStack poseStack, @Nonnull GuiGraphics guiGraphics, @Nonnull Camera camera) {
        for (BF_399 bF_399 : this.field_3263) {
            if (bF_399 == null) continue;
            bF_399.method_5925(poseStack, guiGraphics, camera);
        }
        for (BF_399 bF_399 : this.field_3265) {
            if (bF_399 == null) continue;
            bF_399.method_5925(poseStack, guiGraphics, camera);
        }
    }
}

