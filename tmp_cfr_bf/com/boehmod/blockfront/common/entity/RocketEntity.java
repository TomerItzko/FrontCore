/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.entity;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.sound.entity.EntitySoundInstance;
import com.boehmod.blockfront.client.world.ShakeManager;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.common.entity.BFProjectileEntity;
import com.boehmod.blockfront.common.entity.base.IProducedProjectileEntity;
import com.boehmod.blockfront.common.net.packet.BFPositionedShakeNodePacket;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.registry.BFParticleTypes;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.ClientUtils;
import com.boehmod.blockfront.util.PacketUtils;
import com.boehmod.blockfront.util.math.ShakeNodePresets;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RocketEntity
extends BFProjectileEntity
implements IProducedProjectileEntity {
    private static final int field_2772 = 20;
    private static final int field_2773 = 60;
    private static final float field_2769 = 0.1f;
    private static final float field_2770 = 1.5f;
    private static final float field_2771 = 1.0f;
    public static final int field_6698 = 2;
    @NotNull
    private final IntSet field_2764 = new IntOpenHashSet();
    private int field_2774 = 20;
    private int field_2775 = 0;
    private boolean field_2767 = false;
    @Nullable
    private SoundEvent field_2765;
    @Nullable
    private EntitySoundInstance<?> field_2763 = null;
    @Nullable
    private EntitySoundInstance<?> field_2766 = null;

    public RocketEntity(@NotNull EntityType<? extends RocketEntity> entityType, @NotNull Level level) {
        super(entityType, level);
    }

    @Nullable
    public SoundEvent method_2511() {
        return (SoundEvent)BFSounds.ENTITY_ROCKET_FLYBY.get();
    }

    @NotNull
    public SoundEvent method_2512() {
        return (SoundEvent)BFSounds.ENTITY_ROCKET_LOOP.get();
    }

    protected boolean method_2506() {
        return true;
    }

    public int method_2508() {
        return 60;
    }

    protected boolean method_2507() {
        return true;
    }

    public void method_2504(@NotNull SoundEvent soundEvent) {
        this.field_2765 = soundEvent;
    }

    @Override
    public void method_1934(@NotNull Player player, float f, @NotNull ItemStack itemStack, float f2, float f3) {
        this.owner = player;
        this.method_1951(itemStack);
        float f4 = f;
        int n = (int)(player.getDeltaMovement().x * 100.0) + 7;
        if (n < 0) {
            f4 += 0.3f;
        }
        this.method_1955(player.getEyePosition(), player.getYRot(), player.getXRot(), f, f4);
    }

    @Override
    public void method_1935(@NotNull Vec3 vec3, float f, @NotNull ItemStack itemStack, float f2, float f3) {
        this.method_1951(itemStack);
        this.method_1955(vec3, f2, f3, f, f);
    }

    public void method_2505(@NotNull Player player, @NotNull Vec3 vec3, @NotNull ItemStack itemStack, float f, float f2, float f3) {
        this.owner = player;
        this.method_1951(itemStack);
        this.method_1955(vec3, f2, f3, f, f);
    }

    private void method_2510(@NotNull Level level, @NotNull Vec3 vec3) {
        double d;
        if (this.field_2765 == null) {
            return;
        }
        double d2 = 0.0;
        for (int i = 0; i < 150; ++i) {
            BlockPos blockPos = BlockPos.containing((Position)vec3).offset(0, -i, 0);
            if (level.canSeeSky(blockPos)) continue;
            d2 = i;
            break;
        }
        if (d2 <= (d = -((this.getY() - this.yOld) * 20.0)) * 1.5 && !this.field_2767) {
            this.field_2767 = true;
            level.playLocalSound(vec3.x, vec3.y, vec3.z, this.field_2765, SoundSource.BLOCKS, 20.0f, 1.0f, false);
        }
    }

    @OnlyIn(value=Dist.CLIENT)
    @OverridingMethodsMustInvokeSuper
    protected void method_2502(@NotNull ThreadLocalRandom threadLocalRandom, @NotNull Level level, @NotNull Vec3 vec3) {
        if (!(level instanceof ClientLevel)) {
            return;
        }
        ClientLevel clientLevel = (ClientLevel)level;
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer localPlayer = minecraft.player;
        if (localPlayer == null) {
            return;
        }
        if (!localPlayer.equals((Object)this.owner)) {
            SoundManager soundManager = minecraft.getSoundManager();
            this.method_2503(minecraft, bFClientManager, clientLevel, vec3);
            if (this.field_2763 == null || this.field_2763.isStopped()) {
                this.field_2763 = new EntitySoundInstance<RocketEntity>(this, this.method_2512(), SoundSource.NEUTRAL, 1.0f, 1.5f);
                soundManager.play(this.field_2763);
            }
            if (this.tickCount > 5) {
                this.method_2501(threadLocalRandom, localPlayer, soundManager);
            }
        }
    }

    @OnlyIn(value=Dist.CLIENT)
    protected void method_2503(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientLevel clientLevel, @NotNull Vec3 vec3) {
        Vec3 vec32 = new Vec3(this.xOld, this.yOld, this.zOld);
        Vec3 vec33 = vec3.subtract(vec32);
        double d = vec33.length();
        if (d > 0.1) {
            int n = Math.max(3, (int)(d * 3.0));
            for (int i = 0; i < n; ++i) {
                double d2 = (double)i / (double)n;
                double d3 = vec32.x + vec33.x * d2;
                double d4 = vec32.y + vec33.y * d2;
                double d5 = vec32.z + vec33.z * d2;
                ClientUtils.spawnParticle(minecraft, bFClientManager, clientLevel, (SimpleParticleType)BFParticleTypes.POOF_PARTICLE.get(), d3, d4, d5, 0.0, 0.0, 0.0);
            }
        } else {
            ClientUtils.spawnParticle(minecraft, bFClientManager, clientLevel, (SimpleParticleType)BFParticleTypes.POOF_PARTICLE.get(), vec3.x, vec3.y, vec3.z, 0.0, 0.0, 0.0);
        }
    }

    @OnlyIn(value=Dist.CLIENT)
    private void method_2501(@NotNull ThreadLocalRandom threadLocalRandom, @NotNull LocalPlayer localPlayer, @NotNull SoundManager soundManager) {
        SoundEvent soundEvent = this.method_2511();
        if (soundEvent == null) {
            return;
        }
        float f = this.distanceTo((Entity)localPlayer);
        if (!(f <= 15.0f)) {
            return;
        }
        if (this.field_2766 == null) {
            this.field_2766 = new EntitySoundInstance<RocketEntity>(this, soundEvent, SoundSource.NEUTRAL, 0.9f + 0.2f * threadLocalRandom.nextFloat(0.2f), 5.0f);
            soundManager.play(this.field_2766);
            ShakeManager.applyShake(ShakeNodePresets.field_1919);
        }
    }

    private void method_2500(@NotNull ThreadLocalRandom threadLocalRandom) {
        float f = -0.1f;
        float f2 = -0.05f;
        float f3 = 0.01f;
        float f4 = 0.01f;
        int n = 20;
        int n2 = 10;
        float f5 = -0.1f + 0.01f * (float)threadLocalRandom.nextInt(20);
        float f6 = -0.05f + 0.01f * (float)threadLocalRandom.nextInt(10);
        this.setDeltaMovement(this.getDeltaMovement().add((double)f5, (double)f6, (double)f6));
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected boolean method_2513(@NotNull Entity entity) {
        if (this.field_2764.contains(entity.getId())) {
            return false;
        }
        if (!(entity instanceof AbstractVehicleEntity)) return false;
        AbstractVehicleEntity abstractVehicleEntity = (AbstractVehicleEntity)entity;
        if (!abstractVehicleEntity.method_2343().field_2681) return false;
        return true;
    }

    private void method_2509(@NotNull Level level) {
        Vec3 vec3 = this.position();
        this.playSound((SoundEvent)BFSounds.ENTITY_ROCKET_BOUNCE.get(), 5.0f, (float)((double)0.9f + (double)0.1f * Math.random()));
        this.playSound((SoundEvent)BFSounds.ENTITY_ROCKET_RICOCHET.get(), 5.0f, (float)((double)0.9f + (double)0.1f * Math.random()));
        level.addParticle((ParticleOptions)BFParticleTypes.ELECTRIC_SPARK_PARTICLE.get(), true, vec3.x, vec3.y, vec3.z, 0.0, 0.0, 0.0);
        level.addParticle((ParticleOptions)BFParticleTypes.BULLET_SPARK_PARTICLE.get(), true, vec3.x, vec3.y, vec3.z, 0.0, 0.0, 0.0);
        Vec3 vec32 = this.getDeltaMovement();
        Vec3 vec33 = switch (this.getMotionDirection().getOpposite()) {
            default -> throw new MatchException(null, null);
            case Direction.UP, Direction.DOWN -> new Vec3(vec32.x, (double)-0.1f * vec32.y, vec32.z);
            case Direction.NORTH, Direction.SOUTH -> new Vec3(vec32.x, vec32.y, (double)-0.1f * vec32.z);
            case Direction.EAST, Direction.WEST -> new Vec3((double)-0.1f * vec32.x, vec32.y, vec32.z);
        };
        this.setDeltaMovement(vec33.add(0.0, Math.max(1.0, Math.abs(vec32.y) * 2.0), 0.0));
    }

    public boolean canCollideWith(@NotNull Entity entity) {
        if (entity instanceof Player) {
            Player player = (Player)entity;
            BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
            assert (bFAbstractManager != null) : "Mod manager is null!";
            return !BFUtils.isPlayerUnavailable(player, ((PlayerDataHandler)bFAbstractManager.getPlayerDataHandler()).getPlayerData(player));
        }
        return true;
    }

    protected void onHitEntity(@NotNull EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        if (entity.isRemoved()) {
            return;
        }
        this.method_1957();
    }

    @Override
    public void tick() {
        super.tick();
        Level level = this.level();
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        Vec3 vec3 = this.position();
        this.method_2510(level, vec3);
        if (this.field_2774-- <= 0 && this.method_2506()) {
            this.method_2500(threadLocalRandom);
        }
        if (this.field_2775++ >= this.method_2508()) {
            if (this.method_2507()) {
                this.method_1957();
            } else {
                this.discard();
            }
        }
        if (level.isClientSide()) {
            this.method_2502(threadLocalRandom, level, vec3);
        }
    }

    @Override
    protected void onHit(@NotNull HitResult hitResult) {
        Level level = this.level();
        if (level.isClientSide && !ClientUtils.isBfEntity((Entity)this)) {
            return;
        }
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is null!";
        Object obj = bFAbstractManager.getPlayerDataHandler();
        if (hitResult instanceof EntityHitResult) {
            Object object;
            EntityHitResult entityHitResult = (EntityHitResult)hitResult;
            if (this.tickCount <= 2) {
                return;
            }
            Entity entity = entityHitResult.getEntity();
            if (entity.isRemoved()) {
                return;
            }
            if (entity instanceof LivingEntity && (object = (LivingEntity)entity).isDeadOrDying()) {
                return;
            }
            if (entity instanceof Player && BFUtils.isPlayerUnavailable((Player)(object = (Player)entity), ((PlayerDataHandler)obj).getPlayerData((Player)object))) {
                return;
            }
            if (this.field_2764.contains(entity.getId())) {
                return;
            }
            if (this.method_2513(entity) && Math.random() < (double)0.1f) {
                this.field_2764.add(entity.getId());
                this.method_2509(level);
                return;
            }
            if (entity instanceof AbstractVehicleEntity) {
                object = (AbstractVehicleEntity)entity;
                if (!level.isClientSide) {
                    PacketUtils.sendToAllPlayers(new BFPositionedShakeNodePacket(ShakeNodePresets.EXPLOSION_SMALL, this.position().toVector3f(), 32.0f));
                }
                ((AbstractVehicleEntity)object).method_2329(this.getItem(), this.owner, this.method_1940());
            }
        }
        this.setDeltaMovement(0.0, 0.0, 0.0);
        if (!this.field_2340) {
            this.method_1957();
        }
    }

    @Override
    protected boolean method_1943() {
        return true;
    }

    @Override
    public float method_1940() {
        return 1.0f;
    }

    @Override
    public float method_1941() {
        return 5.0f;
    }
}

