/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.game.AbstractGame;
import java.util.UUID;
import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.TickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class BF_1171
extends AbstractSoundInstance
implements TickableSoundInstance {
    @NotNull
    private final UUID field_6677;

    public BF_1171(@NotNull SoundEvent soundEvent, @NotNull Vec3 vec3, float f, float f2, @NotNull UUID uUID) {
        super(soundEvent, SoundSource.AMBIENT, SoundInstance.createUnseededRandom());
        this.pitch = f;
        this.volume = f2;
        this.field_6677 = uUID;
        this.x = vec3.x;
        this.y = vec3.y;
        this.z = vec3.z;
        this.looping = true;
        this.attenuation = SoundInstance.Attenuation.LINEAR;
    }

    public boolean canStartSilent() {
        return true;
    }

    public boolean isStopped() {
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        AbstractGame<?, ?, ?> abstractGame = bFClientManager.getGame();
        return abstractGame == null || !abstractGame.getUUID().equals(this.field_6677);
    }

    public void tick() {
    }
}

