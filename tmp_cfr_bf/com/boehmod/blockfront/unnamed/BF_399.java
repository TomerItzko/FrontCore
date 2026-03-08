/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.event.tick.PlayerTickable;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.sound.EnvironmentManager;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.UUID;
import javax.annotation.Nonnull;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class BF_399
extends AbstractTickableSoundInstance {
    private static final float field_6404 = 0.025f;
    @Nonnull
    private static final ResourceLocation field_6987 = BFRes.loc("textures/misc/debug/sound_ambient.png");
    private static final int field_6988 = 32;
    @NotNull
    private final Minecraft field_6403;
    @NotNull
    private final UUID field_1827;
    private float field_1825 = 0.0f;
    private float field_1826 = 0.0f;
    @NotNull
    private final Vec3 field_1828;
    @NotNull
    private final EnvironmentManager field_6986;
    private final int field_6989;

    public BF_399(@NotNull Minecraft minecraft, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull SoundEvent soundEvent, @NotNull SoundSource soundSource, @NotNull Vec3 vec3, float f, @NotNull EnvironmentManager environmentManager, int n) {
        super(soundEvent, soundSource, SoundInstance.createUnseededRandom());
        this.field_6403 = minecraft;
        this.field_1827 = abstractGame.getUUID();
        this.pitch = f;
        this.field_1828 = vec3;
        this.field_6986 = environmentManager;
        this.field_6989 = n;
        this.looping = true;
        this.volume = 0.0f;
        this.attenuation = SoundInstance.Attenuation.NONE;
    }

    public void method_1410(float f) {
        this.field_1826 = f;
    }

    public boolean canPlaySound() {
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        AbstractGame<?, ?, ?> abstractGame = bFClientManager.getGame();
        return abstractGame != null && abstractGame.getUUID().equals(this.field_1827);
    }

    public boolean canStartSilent() {
        return true;
    }

    public void tick() {
        this.field_1825 = MathUtils.moveTowards(this.field_1825, this.field_1826, 0.025f);
        this.volume = Mth.clamp((float)(this.field_1825 * PlayerTickable.field_6418), (float)0.001f, (float)1.0f);
        Entity entity = this.field_6403.getCameraEntity();
        if (entity != null) {
            float f = this.field_6986.method_5928() + (float)this.field_6989 * 2.0f * (float)Math.PI / 3.0f;
            Vec3 vec3 = new Vec3((double)((float)(this.field_1828.x * Math.cos(f) - this.field_1828.z * Math.sin(f))), this.field_1828.y, (double)((float)(this.field_1828.x * Math.sin(f) + this.field_1828.z * Math.cos(f))));
            Vec3 vec32 = entity.position().add(vec3);
            this.x = vec32.x;
            this.y = vec32.y + 5.0;
            this.z = vec32.z;
        }
    }

    public void method_5925(@Nonnull PoseStack poseStack, @Nonnull GuiGraphics guiGraphics, @Nonnull Camera camera) {
        BFRendering.billboardTexture(poseStack, camera, guiGraphics, field_6987, this.x, this.y, this.z, 32);
    }
}

