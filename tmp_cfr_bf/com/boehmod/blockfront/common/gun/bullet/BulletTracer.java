/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.gun.bullet;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.common.gun.GunSoundConfig;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.unnamed.BF_39;
import com.boehmod.blockfront.unnamed.BF_390;
import com.boehmod.blockfront.util.RandomUtils;
import com.boehmod.blockfront.util.SerializationUtils;
import com.boehmod.blockfront.util.math.MathUtils;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class BulletTracer {
    private static final float field_1636 = 8.0f;
    private static final float field_1637 = 2.5f;
    private static final float field_1638 = 7.525f;
    private static final float field_1639 = 0.65f;
    public static final float field_6402 = 0.5f;
    @Nullable
    private static SoundInstance field_6367 = null;
    private final float field_1640;
    @NotNull
    private final Vec3 field_1645;
    private final float tracerWidth;
    private final float field_1642;
    private final int field_1643;
    private boolean field_1633 = false;
    private final boolean field_1634;
    @NotNull
    private final Vec3 field_1646;
    @NotNull
    private Vec3 startPos;
    @NotNull
    private Vec3 endPos;
    private int field_1644;
    private boolean field_1635 = false;
    @NotNull
    private final Holder<Item> field_6368;

    public BulletTracer(@NotNull Holder<Item> holder, @NotNull Vec3 vec3, @NotNull Vec3 vec32, float f, float f2, int n) {
        this.field_6368 = holder;
        this.field_1646 = vec3;
        this.startPos = vec3;
        this.endPos = vec3;
        this.field_1645 = vec32;
        this.tracerWidth = f;
        this.field_1642 = f2;
        this.field_1643 = n;
        this.field_1640 = (float)vec3.distanceTo(this.field_1645);
        this.field_1634 = this.field_1640 >= 128.0f;
    }

    public BulletTracer(@NotNull Holder<Item> holder, @NotNull Vec3 vec3, @NotNull Vec3 vec32) {
        this(holder, vec3, vec32, 2.5f, 7.525f, -3659);
    }

    public BulletTracer(@NotNull Holder<Item> holder, @NotNull Vec3 vec3, @NotNull Vec3 vec32, float f) {
        this(holder, vec3, vec32, f, 7.525f, -3659);
    }

    public BulletTracer(@NotNull Holder<Item> holder, @NotNull Vec3 vec3, @NotNull Vec3 vec32, int n) {
        this(holder, vec3, vec32, 2.5f, 7.525f, n);
    }

    @NotNull
    public static BulletTracer method_1172(@NotNull RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        return new BulletTracer(SerializationUtils.readItem(registryFriendlyByteBuf), registryFriendlyByteBuf.readVec3(), registryFriendlyByteBuf.readVec3(), registryFriendlyByteBuf.readFloat(), registryFriendlyByteBuf.readFloat(), registryFriendlyByteBuf.readInt());
    }

    @NotNull
    public BulletTracer method_1168() {
        this.field_1635 = true;
        return this;
    }

    @OnlyIn(value=Dist.CLIENT)
    public boolean method_1171(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull LocalPlayer localPlayer, @NotNull ClientLevel clientLevel, @NotNull Random random) {
        this.endPos = this.startPos;
        this.startPos = MathUtils.moveTowards(this.startPos, this.field_1645, this.field_1642);
        ++this.field_1644;
        if (!this.field_1635) {
            this.method_1170(minecraft, bFClientManager, localPlayer, clientLevel, random);
        }
        return this.method_1178();
    }

    public boolean method_1178() {
        return this.method_1176() >= 1.0f || this.field_1644 >= 60;
    }

    @OnlyIn(value=Dist.CLIENT)
    private void method_1170(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull LocalPlayer localPlayer, @NotNull ClientLevel clientLevel, @NotNull Random random) {
        SoundManager soundManager = minecraft.getSoundManager();
        float f = Mth.sqrt((float)((float)localPlayer.distanceToSqr(this.startPos)));
        if (f <= 8.0f && !this.field_1633) {
            this.field_1633 = true;
            float f2 = (float)this.startPos.distanceTo(this.field_1645);
            if (f2 > 10.0f) {
                DeferredHolder<SoundEvent, SoundEvent> deferredHolder;
                GunItem gunItem = (GunItem)this.field_6368.value();
                GunSoundConfig gunSoundConfig = gunItem.getSoundConfig(new ItemStack((ItemLike)gunItem));
                BF_39.field_187 += 0.05f;
                if (random.nextFloat() < 0.65f) {
                    this.method_5517(bFClientManager, clientLevel, soundManager, random);
                }
                if ((deferredHolder = gunSoundConfig.getBulletPass()) != null) {
                    float f3 = 0.9f + 0.2f * random.nextFloat();
                    BF_390 bF_390 = new BF_390(this, (SoundEvent)deferredHolder.get(), SoundSource.AMBIENT, f3, 5.0f);
                    soundManager.play((SoundInstance)bF_390);
                }
            }
        }
    }

    @OnlyIn(value=Dist.CLIENT)
    private void method_5517(@NotNull BFClientManager bFClientManager, @NotNull ClientLevel clientLevel, @NotNull SoundManager soundManager, @NotNull Random random) {
        DeferredHolder<SoundEvent, SoundEvent> deferredHolder;
        int n;
        SoundEvent soundEvent = (SoundEvent)RandomUtils.randomFromList(BFSounds.BULLET_SNAPS).get();
        for (n = 0; n < 2; ++n) {
            clientLevel.playLocalSound(this.startPos.x, this.startPos.y, this.startPos.z, soundEvent, SoundSource.AMBIENT, 15.0f, 1.0f, false);
        }
        n = bFClientManager.getSkyTracker().getPercentage() <= 0.4f ? 1 : 0;
        DeferredHolder<SoundEvent, SoundEvent> deferredHolder2 = deferredHolder = n != 0 ? BFSounds.ITEM_GUN_BULLET_SNAP_STEREO_INDOORS : BFSounds.ITEM_GUN_BULLET_SNAP_STEREO_OUTDOORS;
        if (field_6367 != null) {
            soundManager.stop(field_6367);
        }
        float f = 0.9f + 0.2f * random.nextFloat();
        field_6367 = SimpleSoundInstance.forLocalAmbience((SoundEvent)((SoundEvent)deferredHolder.get()), (float)f, (float)0.5f);
        soundManager.play(field_6367);
    }

    public void method_1173(@NotNull RegistryFriendlyByteBuf registryFriendlyByteBuf) {
        SerializationUtils.writeItem(registryFriendlyByteBuf, this.field_6368);
        registryFriendlyByteBuf.writeVec3(this.field_1646);
        registryFriendlyByteBuf.writeVec3(this.field_1645);
        registryFriendlyByteBuf.writeFloat(this.tracerWidth);
        registryFriendlyByteBuf.writeFloat(this.field_1642);
        registryFriendlyByteBuf.writeInt(this.field_1643);
    }

    public int getColor() {
        return this.field_1643;
    }

    @NotNull
    public Vec3 getLerpedPosition(float t) {
        return MathUtils.lerp(this.startPos, this.endPos, t);
    }

    @NotNull
    public Vec3 getStartPos() {
        return this.startPos;
    }

    @NotNull
    public Vec3 method_1175() {
        return this.startPos.subtract(this.endPos);
    }

    public float getTracerWidth() {
        return this.tracerWidth;
    }

    public boolean method_1179() {
        return this.field_1634;
    }

    public float method_1176() {
        return 1.0f - (float)this.startPos.distanceTo(this.field_1645) / this.field_1640;
    }
}

