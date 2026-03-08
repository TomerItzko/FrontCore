/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.client.sound.entity.ArtilleryVehicleSoundInstance;
import com.boehmod.blockfront.client.sound.entity.VehicleSoundInstance;
import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.common.entity.ArtilleryVehicleEntity;
import com.boehmod.blockfront.common.entity.RocketEntity;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.net.packet.BFGunSoundPacket;
import com.boehmod.blockfront.common.net.packet.BFPositionedShakeNodePacket;
import com.boehmod.blockfront.common.net.packet.BFVehicleFireEffectPacket;
import com.boehmod.blockfront.registry.BFBlocks;
import com.boehmod.blockfront.registry.BFEntityTypes;
import com.boehmod.blockfront.registry.BFItems;
import com.boehmod.blockfront.registry.BFParticleTypes;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.unnamed.BF_624;
import com.boehmod.blockfront.unnamed.BF_632;
import com.boehmod.blockfront.unnamed.BF_633;
import com.boehmod.blockfront.unnamed.BF_693;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.PacketUtils;
import com.boehmod.blockfront.util.math.MathUtils;
import com.boehmod.blockfront.util.math.ShakeNodeData;
import com.boehmod.blockfront.util.math.ShakeNodePresets;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class BF_631
extends BF_633<ArtilleryVehicleEntity> {
    public static final ResourceLocation field_2719 = BFRes.loc("textures/misc/debug/vehicle_tank.png");
    public boolean field_2717;
    @Nullable
    public ShakeNodeData field_2711 = ShakeNodePresets.field_1915;
    @Nullable
    public ShakeNodeData field_2712 = ShakeNodePresets.field_1916;
    public float field_2724 = 1.5f;
    @Nullable
    public EntityType<? extends RocketEntity> field_2716 = (EntityType)BFEntityTypes.TANK_ROCKET.get();
    @OnlyIn(value=Dist.CLIENT)
    public VehicleSoundInstance<ArtilleryVehicleEntity> field_2714;
    public float field_2725;
    public float field_2726 = 0.0f;
    private float field_2727 = 10.0f;
    private static final float field_2728 = 3.0f;
    private static final float field_2729 = 0.01f;
    protected boolean field_2718 = false;
    protected boolean field_2720 = false;
    private float field_2730;
    private float field_2731 = 0.0f;
    private int field_2732 = 0;
    public boolean field_2721 = false;
    public float field_2722 = 75.0f;
    public float field_2723 = 105.0f;
    private int field_2733 = 1;
    private BF_632 field_2715 = BF_632.SHELL;
    private DeferredHolder<Item, ? extends GunItem> field_2713 = BFItems.GUN_BROWNING30;
    public int field_2734 = 0;

    public BF_631(@NotNull String string, @NotNull ArtilleryVehicleEntity artilleryVehicleEntity) {
        super(string, artilleryVehicleEntity);
    }

    public BF_631 method_2444(float f, float f2) {
        this.field_2721 = true;
        this.field_2722 = f;
        this.field_2723 = f2;
        return this;
    }

    public BF_631 method_2448(@NotNull BF_632 bF_632) {
        this.field_2715 = bF_632;
        return this;
    }

    public BF_631 method_2451(@NotNull DeferredHolder<Item, ? extends GunItem> deferredHolder) {
        this.field_2713 = deferredHolder;
        return this;
    }

    public BF_631 method_2445(int n) {
        this.field_2733 = n;
        return this;
    }

    public BF_631 method_2452(@NotNull Vector3f vector3f) {
        this.field_2738 = vector3f;
        return this;
    }

    public BF_631 method_2450(EntityType<? extends RocketEntity> entityType) {
        this.field_2716 = entityType;
        return this;
    }

    public BF_631 method_2441() {
        this.field_2717 = true;
        return this;
    }

    public BF_631 method_2443(float f) {
        this.field_2724 = f;
        return this;
    }

    public BF_631 method_2446(@Nullable ShakeNodeData shakeNodeData, @Nullable ShakeNodeData shakeNodeData2) {
        this.field_2711 = shakeNodeData;
        this.field_2712 = shakeNodeData2;
        return this;
    }

    public BF_631 method_2455(float f) {
        this.field_2727 = f;
        return this;
    }

    public float method_2440() {
        return this.field_2727;
    }

    public BF_632 method_2442() {
        return this.field_2715;
    }

    public DeferredHolder<Item, ? extends GunItem> method_2459() {
        return this.field_2713;
    }

    public int method_2453() {
        return this.field_2732;
    }

    @Override
    public void method_2467(@NotNull ArtilleryVehicleEntity artilleryVehicleEntity) {
    }

    @Override
    public void method_2468(@NotNull ArtilleryVehicleEntity artilleryVehicleEntity) {
        this.field_2731 = this.field_2730;
        this.field_2730 = Mth.lerp((float)0.2f, (float)this.field_2730, (float)0.0f);
        if (this.field_2732 > 0) {
            --this.field_2732;
        }
        if (this.field_2734 > 0) {
            --this.field_2734;
        }
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public void method_2470(@NotNull Minecraft minecraft) {
        SoundManager soundManager = minecraft.getSoundManager();
        DeferredHolder<SoundEvent, SoundEvent> deferredHolder = ((ArtilleryVehicleEntity)this.field_2735).method_2318().field_2962;
        Vec3 vec3 = ((ArtilleryVehicleEntity)this.field_2735).position();
        if (!(deferredHolder == null || this.field_2714 != null && soundManager.isActive(this.field_2714))) {
            this.field_2714 = new ArtilleryVehicleSoundInstance((ArtilleryVehicleEntity)this.field_2735, this, (SoundEvent)deferredHolder.get(), 2.5f, 1.0f, vec3);
            soundManager.play((SoundInstance)this.field_2714);
        }
    }

    @Override
    @NotNull
    public ResourceLocation method_2469() {
        return field_2719;
    }

    @Override
    public float method_2471(float f) {
        return MathUtils.lerpf1(this.field_2725, this.field_2726, f);
    }

    public void method_2461(@NotNull Level level) {
        this.field_2730 = 1.0f;
        BF_632 bF_632 = this.method_2442();
        if (bF_632 == BF_632.SHELL) {
            this.field_2732 = 2;
            Vec3 vec3 = ((ArtilleryVehicleEntity)this.field_2735).position();
            BFUtils.method_2970(level, (ParticleOptions)BFParticleTypes.POOF_PARTICLE.get(), vec3.add(0.0, (double)0.1f, 0.0), 2.0, 8.0, 40, 0.25f);
            BFUtils.method_2970(level, (ParticleOptions)ParticleTypes.CLOUD, vec3.add(0.0, (double)0.1f, 0.0), 2.0, 8.0, 60, 0.25f);
            BFUtils.method_2970(level, (ParticleOptions)BFParticleTypes.BULLET_IMPACT_SMOKE.get(), vec3.add(0.0, (double)0.1f, 0.0), 2.0, 8.0, 60, 0.5f);
        } else if (bF_632 == BF_632.BULLET) {
            this.field_2732 = 1;
        }
    }

    public float method_2462(float f) {
        return this.field_2727 * MathUtils.lerpf1(this.field_2730, this.field_2731, f);
    }

    public void method_2449(@NotNull EntityDataAccessor<Float> entityDataAccessor) {
        SynchedEntityData synchedEntityData = ((ArtilleryVehicleEntity)this.field_2735).getEntityData();
        float f = ((Float)synchedEntityData.get(entityDataAccessor)).floatValue();
        this.field_2726 = this.field_2725;
        this.field_2725 = f;
        BF_693 bF_693 = ((ArtilleryVehicleEntity)this.field_2735).method_2318();
        boolean bl = false;
        boolean bl2 = false;
        Entity entity = this.method_2465();
        if (entity != null && this.field_2717) {
            float f2;
            float f3 = ((ArtilleryVehicleEntity)this.field_2735).getYRot();
            float f4 = entity.getYRot();
            float f5 = f3 - f4;
            for (f2 = f5 + f; f2 > 180.0f; f2 -= 360.0f) {
            }
            while (f2 < -180.0f) {
                f2 += 360.0f;
            }
            float f6 = Math.min(this.field_2724, Math.abs(f2));
            f6 *= Math.signum(f2);
            if (Math.abs(f2) > 0.01f) {
                bl = true;
                float f7 = f - f6;
                if (this.field_2721) {
                    float f8 = f3 * ((float)Math.PI / 180);
                    f7 = Mth.clamp((float)f7, (float)(f8 - this.field_2722), (float)(f8 + this.field_2722));
                }
                synchedEntityData.set(entityDataAccessor, (Object)Float.valueOf(f7));
            }
            boolean bl3 = bl2 = Math.abs(f2) > 3.0f;
        }
        if (!bl) {
            this.field_2718 = false;
            if (bF_693.field_2964 != null && !this.field_2720) {
                this.field_2720 = true;
                ((ArtilleryVehicleEntity)this.field_2735).playSound((SoundEvent)bF_693.field_2964.get(), 1.5f, 1.0f);
            }
        } else if (bl2) {
            this.field_2720 = false;
            if (bF_693.field_2963 != null && !this.field_2718) {
                this.field_2718 = true;
                ((ArtilleryVehicleEntity)this.field_2735).playSound((SoundEvent)bF_693.field_2963.get(), 1.5f, 1.0f);
            }
        }
        ArtilleryVehicleEntity.BF_620 bF_620 = ((ArtilleryVehicleEntity)this.field_2735).method_2358(this);
        bF_620.method_2365(((ArtilleryVehicleEntity)this.field_2735).getEntityData(), bl2 && bl);
    }

    public void method_2457(@NotNull EntityDataAccessor<Float> entityDataAccessor) {
        this.field_2726 = this.field_2725;
        this.field_2725 = ((Float)((ArtilleryVehicleEntity)this.field_2735).getEntityData().get(entityDataAccessor)).floatValue();
    }

    public void method_2458(@NotNull ServerPlayer serverPlayer) {
        PacketUtils.sendToAllPlayers(new BFVehicleFireEffectPacket(((ArtilleryVehicleEntity)this.field_2735).getId(), this.field_2736));
        ItemStack itemStack = new ItemStack(this.field_2713);
        PacketUtils.sendToAllPlayers(new BFGunSoundPacket(itemStack, serverPlayer.position(), serverPlayer.getId(), false));
    }

    public void method_2460(@NotNull ServerPlayer serverPlayer) {
        SynchedEntityData synchedEntityData;
        if (this.field_2716 == null) {
            return;
        }
        ArtilleryVehicleEntity.BF_620 bF_620 = ((ArtilleryVehicleEntity)this.field_2735).method_2358(this);
        int n = bF_620.method_2361(synchedEntityData = ((ArtilleryVehicleEntity)this.field_2735).getEntityData());
        if (n <= 0 || ((ArtilleryVehicleEntity)this.field_2735).field_2641 > 0) {
            return;
        }
        Level level = ((ArtilleryVehicleEntity)this.field_2735).level();
        RocketEntity rocketEntity = (RocketEntity)this.field_2716.create(level);
        if (rocketEntity == null) {
            return;
        }
        List list = ((ArtilleryVehicleEntity)this.field_2735).getPassengers();
        BF_624<AbstractVehicleEntity> bF_624 = ((ArtilleryVehicleEntity)this.field_2735).method_2343();
        BF_693 bF_693 = ((ArtilleryVehicleEntity)this.field_2735).method_2318();
        ((ArtilleryVehicleEntity)this.field_2735).field_2641 = bF_624.field_2701;
        float f = this.method_2463();
        float f2 = ((ArtilleryVehicleEntity)this.field_2735).getYRot();
        Vec3 vec3 = ((ArtilleryVehicleEntity)this.field_2735).position();
        Vector3f vector3f = new Vector3f((Vector3fc)this.field_2738).rotateY(-f2 * ((float)Math.PI / 180));
        float f3 = f2 + f;
        float f4 = serverPlayer.getXRot();
        float f5 = serverPlayer.getYRot();
        float f6 = Math.abs(f5 - f3);
        if (Math.abs(f6) < 15.0f) {
            f3 = f5;
        }
        double d = -Mth.sin((float)(f3 * ((float)Math.PI / 180))) * 4.0f;
        double d2 = -Mth.sin((float)((f4 += 6.0f * ((ArtilleryVehicleEntity)this.field_2735).method_2316().method_2367()) * ((float)Math.PI / 180))) * 4.0f;
        double d3 = Mth.cos((float)(f3 * ((float)Math.PI / 180))) * 4.0f;
        Vec3 vec32 = vec3.add((double)vector3f.x, (double)vector3f.y, (double)vector3f.z).add(d, d2, d3);
        rocketEntity.method_2505((Player)serverPlayer, vec32, new ItemStack((ItemLike)((Block)BFBlocks.SHERMAN_TANK.get()).asItem()), 7.0f, f3, f4);
        rocketEntity.setNoGravity(true);
        level.addFreshEntity((Entity)rocketEntity);
        ((ArtilleryVehicleEntity)this.field_2735).playSound((SoundEvent)bF_693.field_2970.get(), 2.0f, 1.0f);
        ((ArtilleryVehicleEntity)this.field_2735).playSound((SoundEvent)bF_693.field_2966.get(), 3.0f, 1.0f);
        ((ArtilleryVehicleEntity)this.field_2735).playSound((SoundEvent)bF_693.field_2969.get(), 3.0f, 1.0f);
        ((ArtilleryVehicleEntity)this.field_2735).playSound((SoundEvent)bF_693.field_2967.get(), 3.0f, 1.0f);
        ((ArtilleryVehicleEntity)this.field_2735).playSound((SoundEvent)bF_693.field_2968.get(), 8.0f, 1.0f);
        for (int i = 0; i < 2; ++i) {
            ((ArtilleryVehicleEntity)this.field_2735).playSound((SoundEvent)BFSounds.AMBIENT_EXPLOSION_GRENADE_EXPLODE_BASS.get(), 6.0f, 1.25f);
        }
        ((ArtilleryVehicleEntity)this.field_2735).playSound((SoundEvent)BFSounds.AMBIENT_EXPLOSION_ARTILLERY_AMBIENCE_DISTANT.get(), 8.0f, 1.25f);
        bF_620.method_2364(synchedEntityData, n - 1);
        PacketUtils.sendToAllPlayers(new BFVehicleFireEffectPacket(((ArtilleryVehicleEntity)this.field_2735).getId(), this.field_2736));
        Vector3f vector3f2 = ((ArtilleryVehicleEntity)this.field_2735).position().toVector3f();
        BFPositionedShakeNodePacket bFPositionedShakeNodePacket = null;
        BFPositionedShakeNodePacket bFPositionedShakeNodePacket2 = null;
        if (this.field_2711 != null) {
            bFPositionedShakeNodePacket = new BFPositionedShakeNodePacket(this.field_2711, vector3f2, 5.0f);
        }
        if (this.field_2712 != null) {
            bFPositionedShakeNodePacket2 = new BFPositionedShakeNodePacket(this.field_2712, vector3f2, 32.0f);
        }
        SoundEvent soundEvent = (SoundEvent)bF_693.field_2965.get();
        for (Player player : level.players()) {
            if (!(player instanceof ServerPlayer)) continue;
            ServerPlayer serverPlayer2 = (ServerPlayer)player;
            if (list.contains(serverPlayer2)) {
                serverPlayer2.playNotifySound(soundEvent, SoundSource.NEUTRAL, 3.0f, 1.0f);
                if (bFPositionedShakeNodePacket == null) continue;
                PacketUtils.sendToPlayer(bFPositionedShakeNodePacket, serverPlayer2);
                continue;
            }
            if (bFPositionedShakeNodePacket2 == null) continue;
            PacketUtils.sendToPlayer(bFPositionedShakeNodePacket2, serverPlayer2);
        }
    }

    public int method_2454() {
        return this.field_2733;
    }

    @Override
    public /* synthetic */ void method_2468(@NotNull AbstractVehicleEntity abstractVehicleEntity) {
        this.method_2468((ArtilleryVehicleEntity)abstractVehicleEntity);
    }

    @Override
    public /* synthetic */ void method_2467(@NotNull AbstractVehicleEntity abstractVehicleEntity) {
        this.method_2467((ArtilleryVehicleEntity)abstractVehicleEntity);
    }
}

