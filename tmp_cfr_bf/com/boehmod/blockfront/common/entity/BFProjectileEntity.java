/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.entity;

import com.boehmod.bflib.cloud.common.player.achievement.CloudAchievements;
import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.block.ConfigurableDoorBlock;
import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.common.entity.AirstrikeRocketEntity;
import com.boehmod.blockfront.common.entity.GrenadeEntity;
import com.boehmod.blockfront.common.net.packet.BFExplosionPacket;
import com.boehmod.blockfront.common.world.ExplosionType;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.registry.BFItems;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.ClientUtils;
import com.boehmod.blockfront.util.ExplosionUtils;
import com.boehmod.blockfront.util.PacketUtils;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public abstract class BFProjectileEntity
extends ThrowableProjectile
implements ItemSupplier {
    private static final EntityDataAccessor<ItemStack> field_2338 = SynchedEntityData.defineId(BFProjectileEntity.class, (EntityDataSerializer)EntityDataSerializers.ITEM_STACK);
    private static final float field_2343 = 5.0f;
    @Nullable
    protected Player owner;
    protected boolean field_2340 = false;
    private boolean field_2341 = true;

    protected BFProjectileEntity(@NotNull EntityType<? extends ThrowableProjectile> entityType, @NotNull Level level) {
        super(entityType, level);
    }

    public BFProjectileEntity method_1951(@NotNull ItemStack itemStack) {
        this.entityData.set(field_2338, (Object)itemStack);
        return this;
    }

    @NotNull
    public ItemStack getItem() {
        return (ItemStack)this.entityData.get(field_2338);
    }

    @OverridingMethodsMustInvokeSuper
    public void method_1949(@NotNull ServerPlayer serverPlayer, float f, @NotNull ItemStack itemStack) {
        this.method_1951(itemStack);
        this.owner = serverPlayer;
        this.method_1951(itemStack);
        float f2 = f;
        if (serverPlayer.getDeltaMovement().y > 0.0) {
            f2 += 0.3f;
        }
        f = Math.min(f, this.method_1938());
        f2 = Math.min(f2, this.method_1939());
        this.method_1955(serverPlayer.getEyePosition(), serverPlayer.getYRot(), serverPlayer.getXRot(), f, f2);
    }

    private static void method_1953(@NotNull Level level, @NotNull BlockPos blockPos) {
        ConfigurableDoorBlock configurableDoorBlock;
        boolean bl;
        BlockState blockState = level.getBlockState(blockPos);
        Block block = blockState.getBlock();
        boolean bl2 = bl = block instanceof ConfigurableDoorBlock && (configurableDoorBlock = (ConfigurableDoorBlock)block).isStrong();
        if (block instanceof DoorBlock) {
            configurableDoorBlock = (DoorBlock)block;
            if (!(block.equals(Blocks.IRON_DOOR) || bl || ((Boolean)blockState.getValue((Property)DoorBlock.OPEN)).booleanValue())) {
                configurableDoorBlock.setOpen(null, level, blockState, blockPos, true);
                level.sendBlockUpdated(blockPos, blockState, blockState, 1);
                level.playLocalSound((double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR, SoundSource.BLOCKS, 1.0f, 0.75f, false);
            }
        }
    }

    protected float method_1938() {
        return 1.5f;
    }

    protected float method_1939() {
        return 1.5f;
    }

    public void method_1956() {
        this.field_2341 = false;
    }

    protected void method_1955(@NotNull Vec3 vec3, float f, float f2, float f3, float f4) {
        this.setRot(f, f2);
        float f5 = -Mth.sin((float)(f * ((float)Math.PI / 180)));
        float f6 = Mth.cos((float)(f * ((float)Math.PI / 180)));
        float f7 = Mth.cos((float)(f2 * ((float)Math.PI / 180)));
        float f8 = Mth.sin((float)(f2 * ((float)Math.PI / 180)));
        Vec3 vec32 = this.getDeltaMovement();
        if (f4 == 0.0f && f3 > 0.0f) {
            f4 = f3;
        }
        this.setDeltaMovement(vec32.x + (double)(f3 * f5 * f7), vec32.y - (double)(f4 * f8), vec32.z + (double)(f3 * f6 * f7));
        double d = -f2 * 2.0f / 90.0f;
        this.teleportTo(vec3.x + (double)f5 * 0.8, vec3.y + d, vec3.z + (double)f6 * 0.8);
        Vec3 vec33 = this.position();
        this.xOld = vec33.x;
        this.yOld = vec33.y;
        this.zOld = vec33.z;
    }

    public void method_1958(@NotNull LivingEntity livingEntity) {
        if (livingEntity instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)livingEntity;
            this.method_1948(serverPlayer);
        }
        this.method_1957();
    }

    private void method_1948(@NotNull ServerPlayer serverPlayer) {
        if (this.onGround()) {
            return;
        }
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is null!";
        UUID uUID = serverPlayer.getUUID();
        AbstractGame<?, ?, ?> abstractGame = bFAbstractManager.getGameWithPlayer(uUID);
        if (abstractGame == null) {
            return;
        }
        if (this.owner == null) {
            return;
        }
        UUID uUID2 = this.owner.getUUID();
        AbstractGame<?, ?, ?> abstractGame2 = bFAbstractManager.getGameWithPlayer(uUID2);
        if (!abstractGame.equals(abstractGame2)) {
            return;
        }
        if (BFUtils.isSameTeam(bFAbstractManager, uUID, uUID2)) {
            return;
        }
        BFUtils.awardAchievement(bFAbstractManager, uUID, CloudAchievements.ACH_USER_SHOOT_GRENADE);
    }

    public void method_1957() {
        ExplosionType explosionType;
        if (this.field_2340) {
            return;
        }
        this.field_2340 = true;
        Level level = this.level();
        BlockPos blockPos = this.blockPosition();
        Vec3 vec3 = Vec3.atCenterOf((Vec3i)blockPos);
        ExplosionType explosionType2 = explosionType = this instanceof AirstrikeRocketEntity ? ExplosionType.ARTILLERY_EXPLOSION : ExplosionType.GENERIC;
        if (this.field_2341) {
            if (level.isClientSide()) {
                this.method_1952(level, explosionType, vec3);
            } else {
                this.method_1954(level, vec3, explosionType, blockPos);
            }
        }
        this.discard();
    }

    @OnlyIn(value=Dist.CLIENT)
    private void method_1952(@NotNull Level level, @NotNull ExplosionType explosionType, @NotNull Vec3 vec3) {
        Minecraft minecraft = Minecraft.getInstance();
        if (!(level instanceof ClientLevel)) {
            return;
        }
        ClientLevel clientLevel = (ClientLevel)level;
        if (ClientUtils.isBfEntity((Entity)this)) {
            BFClientManager bFClientManager = BFClientManager.getInstance();
            assert (bFClientManager != null) : "Client mod manager is null!";
            ExplosionUtils.explode(minecraft, bFClientManager, clientLevel, explosionType, vec3);
        }
    }

    private void method_1954(@NotNull Level level, @NotNull Vec3 vec3, @NotNull ExplosionType explosionType, @NotNull BlockPos blockPos) {
        AbstractVehicleEntity abstractVehicleEntity;
        Entity entity2;
        Object object;
        Object object22;
        UUID uUID = this.owner != null ? this.owner.getUUID() : null;
        AABB aABB = this.getBoundingBox();
        if (this.method_1945()) {
            List list = level.getEntities((Entity)this, aABB.inflate(5.0, 5.0, 5.0), entity -> entity instanceof GrenadeEntity);
            object22 = list.iterator();
            while (object22.hasNext()) {
                object = (Entity)object22.next();
                ((GrenadeEntity)((Object)object)).method_1957();
            }
        }
        if (this.method_1943()) {
            float f = this.method_1941();
            object22 = level.getEntities((Entity)this, aABB.inflate((double)f, (double)f, (double)f), entity -> entity instanceof AbstractVehicleEntity);
            object = object22.iterator();
            while (object.hasNext()) {
                entity2 = (Entity)object.next();
                if (!level.clip(new ClipContext(entity2.position(), this.position(), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity2)).getType().equals((Object)HitResult.Type.MISS)) continue;
                abstractVehicleEntity = (AbstractVehicleEntity)entity2;
                float f2 = this.distanceTo(abstractVehicleEntity);
                float f3 = this.method_1940() * (1.0f - f2 / f);
                float f4 = Math.max(f3, 0.0f);
                if (!(f4 > 0.0f)) continue;
                abstractVehicleEntity.method_2329(this.getItem(), this.owner, f4);
            }
        }
        this.method_1950((Entity)this.owner, vec3, this.method_1942());
        for (Object object22 : level.players()) {
            object = new BFExplosionPacket(explosionType, vec3);
            if (!(object22 instanceof ServerPlayer)) continue;
            entity2 = (ServerPlayer)object22;
            PacketUtils.sendToPlayer((CustomPacketPayload)object, (ServerPlayer)entity2);
        }
        if (uUID != null) {
            int n = this.method_1944();
            for (int i = -n; i < n; ++i) {
                for (int j = -n; j < n; ++j) {
                    for (int k = -n; k < n; ++k) {
                        abstractVehicleEntity = new BlockPos(blockPos.getX() + i, blockPos.getY() + j, blockPos.getZ() + k);
                        BFProjectileEntity.method_1953(level, (BlockPos)abstractVehicleEntity);
                    }
                }
            }
        }
    }

    protected abstract boolean method_1943();

    private boolean method_1945() {
        return true;
    }

    public final boolean method_1946() {
        return this.field_2340;
    }

    protected void method_1950(@Nullable Entity entity, @NotNull Vec3 vec3, float f) {
        ItemStack itemStack = this.getItem();
        if (entity == null || itemStack.isEmpty()) {
            return;
        }
        Explosion explosion = BFUtils.explosionFromPlayer(this.level(), (Entity)this, vec3, itemStack, f);
        explosion.explode();
        explosion.finalizeExplosion(true);
        explosion.clearToBlow();
    }

    public abstract float method_1940();

    public abstract float method_1941();

    protected int method_1944() {
        return 4;
    }

    protected float method_1942() {
        return 5.5f;
    }

    @Nullable
    public Player getOwner() {
        return this.owner;
    }

    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putBoolean("exploded", this.field_2340);
    }

    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.field_2340 = compoundTag.getBoolean("exploded");
    }

    public void tick() {
        super.tick();
        Level level = this.level();
        if (!level.isClientSide && this.owner == null) {
            this.discard();
        }
    }

    protected void onHit(@NotNull HitResult hitResult) {
    }

    public void defineSynchedData(// Could not load outer class - annotation placement on inner may be incorrect
    @NotNull SynchedEntityData.Builder builder) {
        builder.define(field_2338, (Object)new ItemStack((ItemLike)BFItems.GRENADE_FRAG_MK2.get()));
    }

    @NotNull
    public Packet<ClientGamePacketListener> getAddEntityPacket(@NotNull ServerEntity serverEntity) {
        return new ClientboundAddEntityPacket((Entity)this, serverEntity);
    }
}

