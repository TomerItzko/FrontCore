/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.entity;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.block.entity.BFBlockEntity;
import com.boehmod.blockfront.common.entity.ArtilleryVehicleEntity;
import com.boehmod.blockfront.common.entity.BotEntity;
import com.boehmod.blockfront.common.entity.MatchEntity;
import com.boehmod.blockfront.common.match.DivisionData;
import com.boehmod.blockfront.common.net.packet.BFExplosionPacket;
import com.boehmod.blockfront.common.net.packet.BFHitMarkerPacket;
import com.boehmod.blockfront.common.player.BFAbstractPlayerData;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.common.world.ExplosionType;
import com.boehmod.blockfront.common.world.damage.BFDamageSource;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.unnamed.BF_622;
import com.boehmod.blockfront.unnamed.BF_623;
import com.boehmod.blockfront.unnamed.BF_624;
import com.boehmod.blockfront.unnamed.BF_629;
import com.boehmod.blockfront.unnamed.BF_631;
import com.boehmod.blockfront.unnamed.BF_632;
import com.boehmod.blockfront.unnamed.BF_633;
import com.boehmod.blockfront.unnamed.BF_694;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.PacketUtils;
import com.boehmod.blockfront.util.math.MathUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.BlockUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractVehicleEntity
extends MatchEntity {
    private static final int field_2626 = 25;
    private static final EntityDataAccessor<Float> field_2633 = SynchedEntityData.defineId(AbstractVehicleEntity.class, (EntityDataSerializer)EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> field_2634 = SynchedEntityData.defineId(AbstractVehicleEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<String> field_2635 = SynchedEntityData.defineId(AbstractVehicleEntity.class, (EntityDataSerializer)EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> field_2604 = SynchedEntityData.defineId(AbstractVehicleEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> field_2605 = SynchedEntityData.defineId(AbstractVehicleEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> field_2606 = SynchedEntityData.defineId(AbstractVehicleEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> field_2607 = SynchedEntityData.defineId(AbstractVehicleEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> field_2608 = SynchedEntityData.defineId(AbstractVehicleEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> field_2609 = SynchedEntityData.defineId(AbstractVehicleEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> field_2611 = SynchedEntityData.defineId(AbstractVehicleEntity.class, (EntityDataSerializer)EntityDataSerializers.FLOAT);
    @NotNull
    public final BF_622 field_2617 = new BF_622();
    @NotNull
    private final BFBlockEntity field_2616;
    @NotNull
    private final BFBlockEntity field_2619;
    private int field_2627;
    private double field_2610;
    private double field_2612;
    private double field_2613;
    private double field_2614;
    private double field_2615;
    private boolean field_2621 = false;
    private int field_2628 = 1200;
    private int field_2629 = 0;
    private int field_2630 = 1200;
    private int field_2631 = 25;
    private boolean field_2622 = false;
    private float field_2624;
    private float field_2625 = 0.0f;
    @Nullable
    private DivisionData field_2620 = null;
    @Nullable
    private UUID field_2632 = null;
    @Nullable
    private BF_624<AbstractVehicleEntity> field_2618;

    public AbstractVehicleEntity(@NotNull EntityType<? extends AbstractVehicleEntity> entityType, @NotNull Level level, @NotNull BlockEntityType<? extends BFBlockEntity> blockEntityType, @NotNull BlockState blockState, @NotNull BlockEntityType<? extends BFBlockEntity> blockEntityType2, @NotNull BlockState blockState2) {
        super(entityType, level);
        this.field_2616 = (BFBlockEntity)blockEntityType.create(BlockPos.ZERO, blockState);
        this.field_2619 = (BFBlockEntity)blockEntityType2.create(BlockPos.ZERO, blockState2);
        if (this.field_2616 != null) {
            this.field_2616.setVehicleEntity(this);
        }
        if (this.field_2619 != null) {
            this.field_2619.setVehicleEntity(this);
        }
        this.blocksBuilding = true;
    }

    public static boolean method_2326(@NotNull Entity entity, @NotNull Entity entity2) {
        return (entity2.canBeCollidedWith() || entity2.isPushable()) && !entity.isPassengerOfSameVehicle(entity2);
    }

    @Nullable
    public UUID method_2349() {
        return this.field_2632;
    }

    public void method_2344(@Nullable UUID uUID) {
        this.field_2632 = uUID;
    }

    public void method_2321(@Nullable DivisionData divisionData) {
        this.field_2620 = divisionData;
    }

    @NotNull
    public BFBlockEntity method_2315() {
        return this.field_2616;
    }

    @NotNull
    public BFBlockEntity method_2342() {
        return this.field_2619;
    }

    @NotNull
    protected abstract BF_624<AbstractVehicleEntity> method_2317();

    public abstract BF_694<?> method_2318();

    public abstract Packet<ClientGamePacketListener> method_2322(ServerEntity var1);

    public abstract float method_2311();

    @NotNull
    public BF_624<AbstractVehicleEntity> method_2343() {
        if (this.field_2618 == null) {
            this.field_2618 = this.method_2317();
        }
        return this.field_2618;
    }

    @NotNull
    public BF_622 method_2316() {
        return this.field_2617;
    }

    public void method_2345() {
        if (this.method_2330() || this.game == null) {
            return;
        }
        if (!this.getPassengers().isEmpty()) {
            this.field_2628 = 0;
            this.field_2621 = true;
        } else if (this.field_2621 && this.field_2628++ >= this.method_2336() && this.field_2629++ >= this.method_2338()) {
            this.field_2629 = 0;
            this.method_2329(null, null, 1.0f);
        }
    }

    @Nullable
    private String method_2303() {
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is null!";
        for (Entity entity : this.getPassengers()) {
            GameTeam gameTeam;
            Player player;
            UUID uUID;
            AbstractGame<?, ?, ?> abstractGame;
            if (!(entity instanceof Player) || (abstractGame = bFAbstractManager.getGameWithPlayer(uUID = (player = (Player)entity).getUUID())) == null || (gameTeam = ((AbstractGamePlayerManager)abstractGame.getPlayerManager()).getPlayerTeam(uUID)) == null) continue;
            return gameTeam.getName();
        }
        return null;
    }

    public void method_2346() {
        if (this.field_2630-- > 0) {
            return;
        }
        Level level = this.level();
        this.discard();
        for (Player player : level.players()) {
            if (!(player instanceof ServerPlayer)) continue;
            ServerPlayer serverPlayer = (ServerPlayer)player;
            BFExplosionPacket bFExplosionPacket = new BFExplosionPacket(ExplosionType.VEHICLE_EXPLOSION, new Vec3(this.getX(), this.getY(), this.getZ()));
            PacketUtils.sendToPlayer(bFExplosionPacket, serverPlayer);
        }
    }

    public boolean method_2330() {
        return this.method_2314() <= 0.0f;
    }

    public float method_2313() {
        BF_624<AbstractVehicleEntity> bF_624 = this.method_2343();
        if (bF_624.field_2675 == null) {
            return 0.0f;
        }
        Level level = this.level();
        return level.isClientSide && this.method_2341() ? bF_624.field_2675.field_2593 : ((Float)this.entityData.get(field_2611)).floatValue();
    }

    public void method_2350(float f) {
        this.entityData.set(field_2611, (Object)Float.valueOf(f));
    }

    public float method_2314() {
        return ((Float)this.entityData.get(field_2633)).floatValue();
    }

    public void method_2351(float f) {
        this.entityData.set(field_2633, (Object)Float.valueOf(f));
    }

    public boolean method_2331() {
        return !this.method_2330() && this.method_2314() < this.method_2343().field_2685 * 0.25f;
    }

    public boolean method_2332() {
        return (Boolean)this.entityData.get(field_2634);
    }

    public void method_2305(boolean bl) {
        if (bl == this.method_2332()) {
            return;
        }
        this.entityData.set(field_2634, (Object)bl);
    }

    public boolean method_2327(@NotNull Player player) {
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is not null!";
        UUID uUID = player.getUUID();
        AbstractGame<?, ?, ?> abstractGame = bFAbstractManager.getGameWithPlayer(uUID);
        if (abstractGame == null) {
            return false;
        }
        Object obj = abstractGame.getPlayerManager();
        GameTeam gameTeam = ((AbstractGamePlayerManager)obj).getPlayerTeam(uUID);
        return gameTeam != null && gameTeam.getName().equals(this.method_2304());
    }

    public boolean method_2352(@NotNull Entity entity) {
        BF_623 bF_623 = this.method_2324(entity);
        return bF_623 != null && bF_623.field_2662;
    }

    public boolean method_2353(@NotNull Entity entity) {
        BF_623 bF_623 = this.method_2324(entity);
        return bF_623 != null && bF_623.field_2661;
    }

    @Nullable
    public Entity method_2319() {
        BF_624<AbstractVehicleEntity> bF_624 = this.method_2343();
        List list = this.getPassengers();
        int n = bF_624.method_2407();
        if (n > -1 && n < list.size()) {
            return (Entity)list.get(n);
        }
        return null;
    }

    protected void method_2325(@NotNull Entity entity, @NotNull BF_623 bF_623) {
        float f = 0.0f;
        if (this.hasPassenger(entity)) {
            int n = this.getPassengers().indexOf(entity);
            for (BF_633 bF_633 : this.method_2343().field_2679) {
                if (bF_633.field_2739 != n && !bF_633.field_2737.contains(n)) continue;
                f += bF_633.method_2463();
            }
        }
        float f2 = this.getYRot() + f;
        entity.setYBodyRot(f2);
        float f3 = Mth.wrapDegrees((float)(entity.getYRot() - f2 + bF_623.field_2670));
        float f4 = Mth.clamp((float)f3, (float)(-bF_623.field_2672), (float)bF_623.field_2672);
        float f5 = Mth.wrapDegrees((float)entity.getXRot());
        float f6 = Mth.clamp((float)f5, (float)(-bF_623.field_2671), (float)bF_623.field_2671);
        entity.yRotO += f4 - f3;
        entity.setYRot(entity.getYRot() + f4 - f3);
        entity.setYHeadRot(entity.getYRot());
        entity.xRotO += f6 - f5;
        entity.setXRot(entity.getXRot() + f6 - f5);
    }

    public void method_2329(@Nullable ItemStack itemStack, @Nullable Player player, float f) {
        BF_694<?> bF_694 = this.method_2318();
        this.playSound((SoundEvent)bF_694.field_2982.get(), 2.0f, 1.0f);
        this.playSound((SoundEvent)bF_694.field_2983.get(), 15.0f, 1.0f);
        if (bF_694.field_2984 != null) {
            SoundEvent soundEvent = (SoundEvent)bF_694.field_2984.get();
            for (Entity entity : this.getPassengers()) {
                if (!(entity instanceof ServerPlayer)) continue;
                ServerPlayer serverPlayer = (ServerPlayer)entity;
                serverPlayer.playNotifySound(soundEvent, SoundSource.NEUTRAL, 15.0f, 1.0f);
            }
        }
        if (!this.method_2330()) {
            boolean bl = false;
            float f2 = Math.max(this.method_2314() - f, 0.0f);
            this.method_2351(f2);
            if (f2 <= 0.0f) {
                this.method_2328(itemStack, player);
                bl = true;
            }
            if (player instanceof ServerPlayer) {
                Entity entity;
                entity = (ServerPlayer)player;
                PacketUtils.sendToPlayer(new BFHitMarkerPacket(bl), (ServerPlayer)entity);
            }
        }
    }

    public void method_2328(@Nullable ItemStack itemStack, @Nullable Player player) {
        ObjectListIterator objectListIterator;
        UUID uUID;
        DamageSources damageSources;
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is null!";
        Level level = this.level();
        PacketUtils.sendToAllPlayers(new BFExplosionPacket(ExplosionType.VEHICLE_EXPLOSION, this.position()));
        if (player != null && (damageSources = bFAbstractManager.getGameWithPlayer(uUID = player.getUUID())) != null) {
            BFUtils.incrementPlayerStat(bFAbstractManager, damageSources, uUID, BFStats.KILLS_VEHICLES);
        }
        uUID = new ObjectArrayList((Collection)this.getPassengers());
        this.ejectPassengers();
        damageSources = level.damageSources();
        DamageSource damageSource = damageSources.generic();
        if (player != null) {
            objectListIterator = damageSources.playerAttack(player);
            damageSource = itemStack != null ? new BFDamageSource((Holder<DamageType>)objectListIterator.typeHolder(), (Entity)player, itemStack) : objectListIterator;
        }
        objectListIterator = uUID.iterator();
        while (objectListIterator.hasNext()) {
            Entity entity = (Entity)objectListIterator.next();
            entity.hurt(damageSource, Float.MAX_VALUE);
        }
    }

    public String method_2304() {
        return (String)this.entityData.get(field_2635);
    }

    public void method_2347() {
        String string = this.method_2303();
        this.entityData.set(field_2635, (Object)(string == null ? "" : string));
    }

    public boolean method_2333() {
        BF_624<AbstractVehicleEntity> bF_624 = this.method_2343();
        if (bF_624.field_2675 == null) {
            return false;
        }
        Level level = this.level();
        return level.isClientSide && this.method_2341() ? bF_624.field_2675.field_2587 : (Boolean)this.entityData.get(field_2604);
    }

    public void method_2306(boolean bl) {
        this.entityData.set(field_2604, (Object)bl);
    }

    public boolean method_2334() {
        BF_624<AbstractVehicleEntity> bF_624 = this.method_2343();
        if (bF_624.field_2675 == null) {
            return false;
        }
        Level level = this.level();
        return level.isClientSide && this.method_2341() ? bF_624.field_2675.field_2588 : (Boolean)this.entityData.get(field_2605);
    }

    public void method_2307(boolean bl) {
        this.entityData.set(field_2605, (Object)bl);
    }

    public boolean method_2335() {
        BF_624<AbstractVehicleEntity> bF_624 = this.method_2343();
        if (bF_624.field_2675 == null) {
            return false;
        }
        Level level = this.level();
        return level.isClientSide && this.method_2341() ? bF_624.field_2675.field_2589 : (Boolean)this.entityData.get(field_2606);
    }

    public void method_2308(boolean bl) {
        this.entityData.set(field_2606, (Object)bl);
    }

    public boolean method_2337() {
        BF_624<AbstractVehicleEntity> bF_624 = this.method_2343();
        if (bF_624.field_2675 == null) {
            return false;
        }
        Level level = this.level();
        return level.isClientSide && this.method_2341() ? bF_624.field_2675.field_2590 : (Boolean)this.entityData.get(field_2607);
    }

    public void method_2309(boolean bl) {
        this.entityData.set(field_2607, (Object)bl);
    }

    public boolean method_2339() {
        BF_624<AbstractVehicleEntity> bF_624 = this.method_2343();
        if (bF_624.field_2675 == null) {
            return false;
        }
        Level level = this.level();
        return level.isClientSide && this.method_2341() ? bF_624.field_2675.field_2591 : (Boolean)this.entityData.get(field_2608);
    }

    public void method_2310(boolean bl) {
        this.entityData.set(field_2608, (Object)bl);
    }

    public boolean method_2340() {
        BF_624<AbstractVehicleEntity> bF_624 = this.method_2343();
        if (bF_624.field_2675 == null) {
            return false;
        }
        Level level = this.level();
        return level.isClientSide && this.method_2341() ? bF_624.field_2675.field_2592 : (Boolean)this.entityData.get(field_2609);
    }

    public void method_2312(boolean bl) {
        this.entityData.set(field_2609, (Object)bl);
    }

    public float maxUpStep() {
        return 1.0f;
    }

    @OnlyIn(value=Dist.CLIENT)
    public boolean method_2341() {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer localPlayer = minecraft.player;
        return localPlayer != null && localPlayer.equals((Object)this.method_2319());
    }

    @Override
    public void defineSynchedData(@NotNull SynchedEntityData.Builder builder) {
        BF_624<AbstractVehicleEntity> bF_624 = this.method_2343();
        builder.define(field_2633, (Object)Float.valueOf(bF_624.field_2685)).define(field_2634, (Object)false).define(field_2635, (Object)"").define(field_2604, (Object)false).define(field_2605, (Object)false).define(field_2606, (Object)false).define(field_2607, (Object)false).define(field_2608, (Object)false).define(field_2609, (Object)false).define(field_2611, (Object)Float.valueOf(0.0f));
    }

    @Override
    public void tick() {
        Object object;
        Object object22;
        Iterator<Object> iterator;
        super.tick();
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is not null!";
        this.method_2348();
        Level level = this.level();
        BF_624<AbstractVehicleEntity> bF_624 = this.method_2343();
        List list = this.getPassengers();
        boolean bl = this.method_2330();
        Entity entity = this.method_2319();
        this.method_2305(entity != null);
        if (entity == null) {
            this.method_2306(false);
            this.method_2307(false);
            this.method_2308(false);
            this.method_2309(false);
            this.method_2310(false);
            this.method_2350(0.0f);
        }
        this.field_2617.method_2375(this);
        if (level.isClientSide) {
            this.field_2625 = this.field_2624;
            this.field_2624 = Mth.lerp((float)0.2f, (float)this.field_2624, (float)0.0f);
            iterator = this.method_2318();
            ((BF_694)((Object)iterator)).method_3045(Minecraft.getInstance());
            for (BF_633<AbstractVehicleEntity> bF_633 : bF_624.field_2679) {
                bF_633.method_2468(this);
            }
            for (BF_629<AbstractVehicleEntity> bF_629 : bF_624.field_2680) {
                bF_629.method_2437(this);
            }
            if (this.method_2330()) {
                if (this.tickCount % 4 == 0) {
                    float f = (float)(this.getX() + (double)((this.random.nextBoolean() ? 1.0f : -1.0f) * this.random.nextFloat()));
                    float f2 = (float)(this.getZ() + (double)((this.random.nextBoolean() ? 1.0f : -1.0f) * this.random.nextFloat()));
                    level.addParticle((ParticleOptions)ParticleTypes.CAMPFIRE_COSY_SMOKE, true, (double)f, this.getY() + 0.5, (double)f2, 0.0, (double)0.1f, 0.0);
                    level.addParticle((ParticleOptions)ParticleTypes.CAMPFIRE_COSY_SMOKE, true, (double)f, this.getY(), (double)(f2 + 0.5f), 0.0, (double)0.1f, 0.0);
                }
                if (this.tickCount % 2 == 0) {
                    float object4 = (float)(this.getX() + (double)((this.random.nextBoolean() ? 1.0f : -1.0f) * this.random.nextFloat()));
                    float f = (float)(this.getZ() + (double)((this.random.nextBoolean() ? 1.0f : -1.0f) * this.random.nextFloat()));
                    level.addParticle((ParticleOptions)ParticleTypes.LARGE_SMOKE, true, (double)object4, this.getY() + 0.5, (double)f, 0.0, (double)0.1f, 0.0);
                    level.addParticle((ParticleOptions)ParticleTypes.LARGE_SMOKE, true, (double)object4, this.getY(), (double)(f + 0.5f), 0.0, (double)0.1f, 0.0);
                }
                level.addParticle((ParticleOptions)ParticleTypes.LAVA, true, this.getX(), this.getY() + 1.5, this.getZ(), 0.0, (double)0.1f, 0.0);
            }
        } else {
            this.method_2347();
            this.method_2345();
            if (!this.field_2622) {
                this.field_2622 = true;
                for (BF_633 bF_633 : bF_624.field_2679) {
                    if (!(bF_633 instanceof BF_631)) continue;
                    BF_631 bF_631 = (BF_631)bF_633;
                    object22 = this;
                    if (!(object22 instanceof ArtilleryVehicleEntity)) continue;
                    object = (ArtilleryVehicleEntity)object22;
                    object22 = ((ArtilleryVehicleEntity)object).method_2358(bF_631);
                    ((ArtilleryVehicleEntity.BF_620)object22).method_2364(((ArtilleryVehicleEntity)object).entityData, bF_631.method_2454());
                }
            }
            if (this.method_2331() && this.field_2631-- <= 0) {
                this.field_2631 = 25;
                iterator = this.method_2318().field_2979;
                DeferredHolder<SoundEvent, SoundEvent> deferredHolder = this.method_2318().field_2980;
                if (iterator != null) {
                    this.playSound((SoundEvent)iterator.get(), 1.0f, 1.0f);
                }
                if (deferredHolder != null) {
                    SoundEvent soundEvent = (SoundEvent)deferredHolder.get();
                    for (Object object22 : list) {
                        if (!(object22 instanceof ServerPlayer)) continue;
                        ServerPlayer serverPlayer = (ServerPlayer)object22;
                        serverPlayer.playNotifySound(soundEvent, SoundSource.AMBIENT, 1.0f, 1.0f);
                    }
                }
            }
            for (BF_633<AbstractVehicleEntity> bF_633 : bF_624.field_2679) {
                bF_633.method_2467(this);
            }
            for (BF_629 bF_629 : bF_624.field_2680) {
                bF_629.method_2436(this);
            }
            if (bl) {
                this.method_2346();
            }
        }
        if (this.isControlledByLocalInstance()) {
            if (level.isClientSide) {
                this.method_2320(bF_624, entity);
            }
        } else {
            this.setDeltaMovement(Vec3.ZERO);
        }
        if (bF_624.field_2675 != null) {
            bF_624.field_2675.method_2296(this);
        }
        iterator = bFAbstractManager.getPlayerDataHandler();
        for (Entity entity2 : list) {
            if (!(entity2 instanceof ServerPlayer)) continue;
            object = (ServerPlayer)entity2;
            object.getAbilities().mayfly = true;
            object22 = ((PlayerDataHandler)((Object)iterator)).getPlayerData((Player)object);
            if (!BFUtils.isPlayerUnavailable((Player)object, object22)) continue;
            entity2.stopRiding();
        }
        if (bl || this.isRemoved()) {
            this.ejectPassengers();
        }
        this.checkInsideBlocks();
    }

    @OnlyIn(value=Dist.CLIENT)
    public void method_2320(@NotNull BF_624<AbstractVehicleEntity> bF_624, @Nullable Entity entity) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer localPlayer = minecraft.player;
        if (localPlayer != null && bF_624.field_2675 != null) {
            bF_624.field_2675.method_2294(localPlayer, this, localPlayer.equals((Object)entity));
        }
    }

    @Nullable
    public BF_623 method_2324(@NotNull Entity entity) {
        BF_624<AbstractVehicleEntity> bF_624 = this.method_2343();
        if (this.hasPassenger(entity)) {
            int n = this.getPassengers().indexOf(entity);
            return bF_624.method_2396(n);
        }
        return null;
    }

    private void method_2348() {
        Vec3 vec3 = this.position();
        if (this.isControlledByLocalInstance()) {
            this.field_2627 = 0;
            this.syncPacketPositionCodec(this.getX(), this.getY(), this.getZ());
        }
        if (this.field_2627 > 0) {
            float f = this.getXRot();
            float f2 = this.getYRot();
            double d = vec3.x + (this.field_2610 - vec3.x) / (double)this.field_2627;
            double d2 = vec3.y + (this.field_2612 - vec3.y) / (double)this.field_2627;
            double d3 = vec3.z + (this.field_2613 - vec3.z) / (double)this.field_2627;
            double d4 = Mth.wrapDegrees((double)(this.field_2614 - (double)f2));
            this.setYRot(f2 + (float)d4 / (float)this.field_2627);
            this.setXRot(f + (float)(this.field_2615 - (double)f) / (float)this.field_2627);
            --this.field_2627;
            this.setPos(d, d2, d3);
            this.setRot(f2, f);
        }
    }

    public int method_2336() {
        return 1200;
    }

    public int method_2338() {
        return 60;
    }

    private boolean method_2323(@NotNull ServerPlayer serverPlayer, @NotNull String string) {
        GameTeam gameTeam;
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is null!";
        Object obj = bFAbstractManager.getPlayerDataHandler();
        UUID uUID = serverPlayer.getUUID();
        Object d = ((PlayerDataHandler)obj).getPlayerData((Player)serverPlayer);
        if (((BFAbstractPlayerData)d).field_1173 > 0 || BFUtils.isPlayerUnavailable((Player)serverPlayer, d)) {
            return false;
        }
        AbstractGame<?, ?, ?> abstractGame = bFAbstractManager.getGameWithPlayer(uUID);
        if (abstractGame != null && (gameTeam = ((AbstractGamePlayerManager)abstractGame.getPlayerManager()).getPlayerTeam(uUID)) != null) {
            if (!string.isEmpty() && !gameTeam.getName().equalsIgnoreCase(string)) {
                return false;
            }
            DivisionData divisionData = gameTeam.getDivisionData(abstractGame);
            if (this.field_2620 == null || divisionData.id == this.field_2620.id) {
                return true;
            }
            MutableComponent mutableComponent = Component.literal((String)"You can't enter this vehicle because you're not on the right team!").withStyle(ChatFormatting.RED);
            BFUtils.playSound(serverPlayer, (SoundEvent)BFSounds.GUI_LOBBY_CANCEL.get(), SoundSource.MASTER);
            BFUtils.sendNoticeMessage(serverPlayer, (Component)mutableComponent);
            return false;
        }
        return true;
    }

    public void method_2354(@NotNull String string) {
        BF_633<AbstractVehicleEntity> bF_633 = this.method_2343().method_2403(string);
        if (bF_633 instanceof BF_631) {
            BF_631 bF_631 = (BF_631)bF_633;
            bF_631.method_2461(this.level());
            if (bF_631.method_2442() == BF_632.SHELL) {
                this.field_2624 = 1.0f;
            }
        }
    }

    public float method_2355(float f) {
        return MathUtils.lerpf1(this.field_2624, this.field_2625, f);
    }

    @Nullable
    public LivingEntity getControllingPassenger() {
        LivingEntity livingEntity;
        Entity entity = this.method_2319();
        return entity instanceof LivingEntity ? (livingEntity = (LivingEntity)entity) : null;
    }

    public void lerpTo(double d, double d2, double d3, float f, float f2, int n) {
        super.lerpTo(d, d2, d3, f, f2, n);
        this.field_2610 = d;
        this.field_2612 = d2;
        this.field_2613 = d3;
        this.field_2614 = f;
        this.field_2615 = f2;
        this.field_2627 = 10;
    }

    protected void addPassenger(@NotNull Entity entity) {
        super.addPassenger(entity);
        BF_694<?> bF_694 = this.method_2318();
        if (this.isControlledByLocalInstance() && this.field_2627 > 0) {
            this.field_2627 = 0;
            this.absMoveTo(this.field_2610, this.field_2612, this.field_2613, (float)this.field_2614, (float)this.field_2615);
        }
        this.level().playLocalSound((Entity)this, (SoundEvent)bF_694.field_2981.get(), SoundSource.NEUTRAL, 1.0f, 0.8f + 0.2f * this.random.nextFloat());
    }

    protected void removePassenger(@NotNull Entity entity) {
        super.removePassenger(entity);
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is null!";
        Object obj = bFAbstractManager.getPlayerDataHandler();
        if (entity instanceof Player) {
            Player player = (Player)entity;
            if (!player.isCreative()) {
                player.getAbilities().mayfly = false;
            }
            Object d = ((PlayerDataHandler)obj).getPlayerData(player);
            ((BFAbstractPlayerData)d).field_1173 = 60;
        }
    }

    @NotNull
    public InteractionResult interact(@NotNull Player player, @NotNull InteractionHand interactionHand) {
        if (player.isSecondaryUseActive()) {
            return InteractionResult.PASS;
        }
        Level level = this.level();
        if (!level.isClientSide) {
            return player.startRiding((Entity)this) ? InteractionResult.CONSUME : InteractionResult.PASS;
        }
        return InteractionResult.SUCCESS;
    }

    public void positionRider(@NotNull Entity entity, @NotNull Entity.MoveFunction moveFunction) {
        List list = this.getPassengers();
        BF_623 bF_623 = this.method_2324(entity);
        if (bF_623 == null) {
            return;
        }
        int n = list.indexOf(entity);
        float f = 0.0f;
        List list2 = this.method_2343().field_2679;
        for (BF_633 bF_633 : list2) {
            if (bF_633.field_2739 != n && !bF_633.field_2737.contains(n)) continue;
            f += bF_633.method_2463();
        }
        float f2 = this.getYRot();
        float f3 = -this.field_2617.method_2367() / 5.0f;
        float f4 = this.field_2617.method_2368() / 5.0f;
        Vec3 vec3 = new Vec3(bF_623.field_2674).xRot(f3).zRot(f4).yRot(-f2 * ((float)Math.PI / 180));
        Vec3 vec32 = new Vec3(bF_623.field_2669).xRot(f3).zRot(f4).yRot(-(f + f2) * ((float)Math.PI / 180));
        Vec3 vec33 = this.position().add(vec3).add(vec32);
        entity.setPos(vec33);
        this.method_2325(entity, bF_623);
        if (entity instanceof Animal) {
            Animal animal = (Animal)entity;
            if (list.size() > 1) {
                int n2 = entity.getId() % 2 == 0 ? 90 : 270;
                entity.setYBodyRot(animal.yBodyRot + (float)n2);
                entity.setYHeadRot(entity.getYHeadRot() + (float)n2);
            }
        }
    }

    public void onPassengerTurned(@NotNull Entity entity) {
        BF_623 bF_623 = this.method_2324(entity);
        if (bF_623 != null) {
            this.method_2325(entity, bF_623);
        }
    }

    protected boolean canAddPassenger(@NotNull Entity entity) {
        Object object;
        if (this.method_2330()) {
            return false;
        }
        String string = this.method_2304();
        if (entity instanceof ServerPlayer) {
            object = (ServerPlayer)entity;
            if (!this.method_2323((ServerPlayer)object, string)) {
                return false;
            }
        } else if (entity instanceof BotEntity) {
            String string2;
            BotEntity botEntity = (BotEntity)entity;
            if (!(string.isEmpty() || (string2 = botEntity.method_2031()) != null && string2.equalsIgnoreCase(string))) {
                return false;
            }
        }
        object = this.method_2343();
        return this.getPassengers().size() < object.field_2678.size();
    }

    @NotNull
    public Direction getMotionDirection() {
        return this.getDirection().getClockWise();
    }

    public boolean isPickable() {
        return !this.isRemoved();
    }

    @NotNull
    public Vec3 getRelativePortalPosition(@NotNull Direction.Axis axis, @NotNull BlockUtil.FoundRectangle foundRectangle) {
        return LivingEntity.resetForwardDirectionOfRelativePortalPosition((Vec3)super.getRelativePortalPosition(axis, foundRectangle));
    }

    @NotNull
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.EVENTS;
    }

    public boolean canCollideWith(@NotNull Entity entity) {
        return AbstractVehicleEntity.method_2326(this, entity);
    }

    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
    }

    @NotNull
    public Packet<ClientGamePacketListener> getAddEntityPacket(@NotNull ServerEntity serverEntity) {
        return this.method_2322(serverEntity);
    }

    @NotNull
    public AABB getBoundingBoxForCulling() {
        return super.getBoundingBoxForCulling().inflate(3.0);
    }
}

