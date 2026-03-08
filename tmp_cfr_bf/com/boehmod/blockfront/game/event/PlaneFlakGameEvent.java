/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game.event;

import com.boehmod.blockfront.common.gun.bullet.BulletTracer;
import com.boehmod.blockfront.common.net.packet.BFBulletTracerToClientPacket;
import com.boehmod.blockfront.common.net.packet.BFWorldFlashPacket;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.event.GameEvent;
import com.boehmod.blockfront.registry.BFItems;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.PacketUtils;
import com.boehmod.blockfront.util.math.FDSPose;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PlaneFlakGameEvent
extends GameEvent {
    private static final float field_3333 = 5.0f;
    private static final int field_3334 = 2;
    private final boolean field_3331;
    private final Vec3 field_3329;
    private int field_3335 = 0;
    private int field_3336 = 0;
    private Vec3 field_3330;
    private boolean field_3332 = false;

    public PlaneFlakGameEvent(@NotNull AbstractGamePlayerManager<?> abstractGamePlayerManager, int n, int n2, @NotNull ThreadLocalRandom threadLocalRandom) {
        super(null, n, n2);
        FDSPose fDSPose = abstractGamePlayerManager.getLobbySpawn();
        Vec3 vec3 = new Vec3(fDSPose.position.x, 0.0, fDSPose.position.z);
        float f = 100.0f;
        this.field_3331 = threadLocalRandom.nextFloat() < 0.2f;
        this.field_3329 = this.field_3330 = vec3.add((double)(-100.0f + threadLocalRandom.nextFloat(200.0f)), 0.0, (double)(-100.0f + threadLocalRandom.nextFloat(200.0f)));
    }

    @Override
    public boolean method_3444(@NotNull ServerLevel serverLevel, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull Set<UUID> set) {
        float f = Mth.sin((float)((float)this.field_3336 / 30.0f));
        float f2 = Mth.sin((float)((float)this.field_3336 / 28.0f));
        f *= f;
        f2 *= f2;
        float f3 = 120.0f;
        float f4 = f3 * f;
        float f5 = f3 * f2;
        this.field_3330 = this.field_3329.add((double)f4, 280.0, (double)f5);
        if (!this.field_3332 && this.field_3331) {
            this.field_3332 = true;
            Vec3 vec3 = this.field_3330.add(0.0, 30.0, 0.0);
            BFUtils.playPositionedSound(set, (SoundEvent)BFSounds.MATCH_EVENT_FLAK_PLANE.get(), SoundSource.AMBIENT, 50.0f, vec3);
            BFUtils.playPositionedSound(set, (SoundEvent)BFSounds.MATCH_EVENT_FLAK_PLANE_STEREO.get(), SoundSource.AMBIENT, 50.0f, vec3);
        }
        return super.method_3444(serverLevel, abstractGame, set);
    }

    @Override
    void method_3446(@NotNull ServerLevel serverLevel, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull Set<UUID> set) {
    }

    @Override
    void method_3447(@NotNull ServerLevel serverLevel, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull Set<UUID> set) {
        ++this.field_3336;
        if (this.field_3335++ >= 2) {
            this.field_3335 = 0;
            BFUtils.playPositionedSound(set, (SoundEvent)BFSounds.MATCH_EVENT_FLAK.get(), SoundSource.AMBIENT, 8.0f, this.field_3329);
            BFUtils.playPositionedSound(set, (SoundEvent)BFSounds.MATCH_EVENT_FLAK_DISTANT.get(), SoundSource.AMBIENT, 20.0f, this.field_3329);
            BFUtils.playPositionedSound(set, (SoundEvent)BFSounds.MATCH_EVENT_FLAK_DISTANT_STEREO.get(), SoundSource.AMBIENT, 20.0f, this.field_3329);
            BulletTracer bulletTracer = new BulletTracer((Holder<Item>)BFItems.GUN_BROWNING30, this.field_3329.add(4.0, 4.0, 4.0), this.field_3330.add(4.0, 4.0, 4.0), 5.0f);
            BulletTracer bulletTracer2 = new BulletTracer((Holder<Item>)BFItems.GUN_BROWNING30, this.field_3329, this.field_3330, 5.0f);
            PacketUtils.sendToGamePlayers(new BFBulletTracerToClientPacket(0, bulletTracer), abstractGame);
            PacketUtils.sendToGamePlayers(new BFBulletTracerToClientPacket(0, bulletTracer2), abstractGame);
        }
    }

    @Override
    void method_3445(@NotNull Level level, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull Set<UUID> set) {
        if (this.field_3331) {
            BFUtils.playPositionedSound(set, (SoundEvent)BFSounds.MATCH_EVENT_FLAK_PLANE_HIT.get(), SoundSource.AMBIENT, 50.0f, this.field_3330.add(0.0, 30.0, 0.0));
            PacketUtils.sendToGamePlayers(new BFWorldFlashPacket(2), abstractGame);
        }
    }

    @Override
    @Nullable
    public Component getMessage() {
        return null;
    }
}

