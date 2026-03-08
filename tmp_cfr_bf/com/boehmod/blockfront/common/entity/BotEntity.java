/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.entity;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.common.entity.IntegratedGameEntity;
import com.boehmod.blockfront.common.entity.goal.BotCapturePointGoal;
import com.boehmod.blockfront.common.entity.goal.BotFireGunGoal;
import com.boehmod.blockfront.common.entity.goal.BotHealPlayerGoal;
import com.boehmod.blockfront.common.entity.goal.BotReloadGoal;
import com.boehmod.blockfront.common.entity.goal.FlamethowerFireBotGoal;
import com.boehmod.blockfront.common.entity.goal.GrenadeBotGoal;
import com.boehmod.blockfront.common.match.BFCountry;
import com.boehmod.blockfront.common.match.BFRace;
import com.boehmod.blockfront.common.match.DivisionData;
import com.boehmod.blockfront.common.match.MatchClass;
import com.boehmod.blockfront.common.net.packet.BFEntitySoundPacket;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.common.world.damage.FirearmDamageSource;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.tag.IAllowsWarCry;
import com.boehmod.blockfront.game.tag.IHasBots;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.registry.custom.BotVoice;
import com.boehmod.blockfront.unnamed.BF_566;
import com.boehmod.blockfront.unnamed.BF_576;
import com.boehmod.blockfront.unnamed.BF_623;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.PacketUtils;
import it.unimi.dsi.fastutil.Pair;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.Nullable;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public class BotEntity
extends IntegratedGameEntity {
    private static final int field_2376 = 10;
    private static final EntityDataAccessor<String> field_2371 = SynchedEntityData.defineId(BotEntity.class, (EntityDataSerializer)EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> field_2374 = SynchedEntityData.defineId(BotEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> field_2375 = SynchedEntityData.defineId(BotEntity.class, (EntityDataSerializer)EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> field_2385 = SynchedEntityData.defineId(BotEntity.class, (EntityDataSerializer)EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> field_2386 = SynchedEntityData.defineId(BotEntity.class, (EntityDataSerializer)EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> field_2387 = SynchedEntityData.defineId(BotEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<String> DATA_HUMAN_NAME = SynchedEntityData.defineId(BotEntity.class, (EntityDataSerializer)EntityDataSerializers.STRING);
    private static final EntityDataAccessor<ItemStack> field_2389 = SynchedEntityData.defineId(BotEntity.class, (EntityDataSerializer)EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<ItemStack> field_2391 = SynchedEntityData.defineId(BotEntity.class, (EntityDataSerializer)EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Integer> DATA_CURRENT_CLASS = SynchedEntityData.defineId(BotEntity.class, (EntityDataSerializer)EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> field_2393 = SynchedEntityData.defineId(BotEntity.class, (EntityDataSerializer)EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<BlockPos> field_2394 = SynchedEntityData.defineId(BotEntity.class, (EntityDataSerializer)EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<Integer> DATA_RACE = SynchedEntityData.defineId(BotEntity.class, (EntityDataSerializer)EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> field_2396 = SynchedEntityData.defineId(BotEntity.class, (EntityDataSerializer)EntityDataSerializers.FLOAT);
    public final Map<LivingEntity, BF_576.BF_577> field_2390 = new HashMap<LivingEntity, BF_576.BF_577>();
    private final int field_2377 = ThreadLocalRandom.current().nextInt(5);
    private final float field_2370 = 1.0f - (float)((double)0.1f * Math.random());
    public boolean field_2361 = false;
    public boolean field_2363 = false;
    public float field_2372;
    public float field_2373 = 0.0f;
    public boolean field_2365 = false;
    public int field_2378 = 0;
    private int field_2379 = 0;
    @Nullable
    private AbstractSoundInstance field_2358;
    @Nullable
    private SimpleSoundInstance field_2360;
    @NotNull
    private final BF_566 field_2357 = new BF_566(this);
    @Nullable
    private ResourceLocation field_2362 = null;
    @Nullable
    private GameTeam team;
    private boolean field_2367 = false;
    private int field_2380 = (int)(40.0 + 300.0 * Math.random());
    private int field_2381 = 120;
    private int field_2382 = 0;
    private int field_2383 = 0;
    private boolean field_2368 = false;
    private int field_2384 = 0;
    @Nullable
    private DeferredHolder<BotVoice, ? extends BotVoice> field_2356 = null;
    @Nullable
    private ResourceLocation field_2364 = null;
    @Nullable
    private ResourceLocation field_2366 = null;

    public BotEntity(@NotNull EntityType<? extends BotEntity> entityType, @NotNull Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder getMobAttributes() {
        return Mob.createMobAttributes().add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.ATTACK_DAMAGE, 0.5).add(Attributes.MOVEMENT_SPEED, 0.6).add(Attributes.MAX_HEALTH, 16.0);
    }

    @NotNull
    protected PathNavigation createNavigation(@NotNull Level level) {
        GroundPathNavigation groundPathNavigation = new GroundPathNavigation((Mob)this, level);
        groundPathNavigation.setCanOpenDoors(true);
        groundPathNavigation.setCanFloat(true);
        groundPathNavigation.setCanPassDoors(true);
        groundPathNavigation.setMaxVisitedNodesMultiplier(10.0f);
        return groundPathNavigation;
    }

    @Nullable
    public AbstractSoundInstance method_1980() {
        return this.field_2358;
    }

    public void method_1988(@NotNull AbstractSoundInstance abstractSoundInstance) {
        this.field_2358 = abstractSoundInstance;
    }

    @Nullable
    public SimpleSoundInstance method_2012() {
        return this.field_2360;
    }

    public void method_2018(@NotNull SimpleSoundInstance simpleSoundInstance) {
        this.field_2360 = simpleSoundInstance;
    }

    public int method_1998() {
        return this.field_2377;
    }

    public boolean method_2000() {
        return this.field_2357.method_2034();
    }

    public Pair<Vec3, Double> method_1979() {
        Vec3 vec3 = this.field_2357.method_2035();
        if (vec3 != null) {
            return Pair.of((Object)vec3, (Object)this.field_2357.method_2036());
        }
        return null;
    }

    public void method_1992(@NotNull Vec3 vec3, double d, float f, float f2) {
        double d2 = vec3.x - this.getX();
        double d3 = vec3.z - this.getZ();
        double d4 = d - this.getEyeY();
        double d5 = Math.sqrt(d2 * d2 + d3 * d3);
        float f3 = (float)(Mth.atan2((double)d3, (double)d2) * 180.0 / 3.1415927410125732) - 90.0f;
        float f4 = (float)(-(Mth.atan2((double)d4, (double)d5) * 180.0 / 3.1415927410125732));
        this.setXRot(this.method_2015(this.getXRot(), f4, f2));
        this.setYRot(this.method_2015(this.getYRot(), f3, f));
    }

    private float method_2015(float f, float f2, float f3) {
        float f4 = Mth.wrapDegrees((float)(f2 - f));
        if (f4 > f3) {
            f4 = f3;
        }
        if (f4 < -f3) {
            f4 = -f3;
        }
        return f + f4;
    }

    public void handleEntityEvent(byte by) {
        if (by != 60) {
            super.handleEntityEvent(by);
        }
    }

    @Override
    public void baseTick() {
        super.baseTick();
        Level level = this.level();
        this.method_1996();
        if (level.isClientSide()) {
            this.method_1991(level);
            return;
        }
        this.field_2357.method_2033(this.random);
        this.method_1994();
        if (++this.field_2384 >= 10) {
            this.field_2384 = 0;
            this.method_1995();
        }
        if (this.field_2380 > 0) {
            --this.field_2380;
        }
        if (this.field_2381 > 0) {
            --this.field_2381;
        }
        this.method_2025(0);
        this.method_2027(Math.max(0, this.method_2005() - 1));
        if (this.field_2379 > 0) {
            --this.field_2379;
            if (this.field_2379 == 0) {
                this.method_2028(0);
            }
        }
        if (this.team != null && !this.method_2031().equals(this.team.getName())) {
            this.method_2022(this.team.getName());
        }
    }

    private void method_1994() {
        if (this.field_2365 && this.field_2378-- <= 0) {
            this.field_2365 = false;
            if (this.game != null && this.team != null) {
                IAllowsWarCry.method_3440(this.game, this.team.getDivisionData(this.game), (LivingEntity)this, this.getUUID());
                this.method_2028(60);
            }
        }
    }

    private void method_1995() {
        Iterator<Map.Entry<LivingEntity, BF_576.BF_577>> iterator = this.field_2390.entrySet().iterator();
        while (iterator.hasNext()) {
            int n;
            Map.Entry<LivingEntity, BF_576.BF_577> entry = iterator.next();
            LivingEntity livingEntity = entry.getKey();
            BF_576.BF_577 bF_5772 = entry.getValue();
            if (livingEntity == null || !livingEntity.isAlive()) {
                iterator.remove();
                continue;
            }
            float f = this.distanceTo((Entity)livingEntity);
            int n2 = n = f < 16.0f ? 120 : 90;
            if (this.hasLineOfSight((Entity)livingEntity) && BFUtils.method_2989((LivingEntity)this, livingEntity, n)) {
                bF_5772.method_2060();
                bF_5772.method_2058();
            } else {
                bF_5772.method_2059();
            }
            if (!bF_5772.method_2062()) continue;
            iterator.remove();
        }
        float f = this.field_2390.values().stream().map(bF_577 -> Float.valueOf((float)bF_577.method_2061() / 2.0f)).max(Float::compare).orElse(Float.valueOf(0.0f)).floatValue();
        this.method_2021(f);
    }

    @OnlyIn(value=Dist.CLIENT)
    private void method_1991(@NotNull Level level) {
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        ClientLevel clientLevel = (ClientLevel)level;
        if (this.field_2382 > 0) {
            --this.field_2382;
        }
        if (this.method_2007() > 0 && this.field_2383++ >= ((double)threadLocalRandom.nextFloat() < 0.5 ? 2 : 3)) {
            this.field_2383 = 0;
            this.field_2368 = !this.field_2368;
        }
        float f = threadLocalRandom.nextFloat();
        float f2 = this.field_2363 ? 0.05f : 0.02f;
        if (f < f2) {
            this.method_1997();
            if (!this.field_2363) {
                boolean bl = this.field_2361 = !this.field_2361;
                if ((double)threadLocalRandom.nextFloat() < 0.5) {
                    this.field_2363 = true;
                }
            } else {
                this.field_2363 = false;
            }
        }
        float f3 = this.field_2363 ? (this.field_2361 ? -1.0f : 1.0f) : 0.0f;
        this.field_2373 = this.field_2372;
        this.field_2372 = Mth.lerp((float)0.2f, (float)this.field_2372, (float)f3);
    }

    private void method_1996() {
        this.setItemInHand(InteractionHand.MAIN_HAND, this.method_2014());
    }

    @NotNull
    public Packet<ClientGamePacketListener> getAddEntityPacket(@NotNull ServerEntity serverEntity) {
        return new ClientboundAddEntityPacket((Entity)this, serverEntity);
    }

    public void defineSynchedData(@NotNull SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(field_2371, (Object)"").define(DATA_HUMAN_NAME, (Object)"Bot").define(field_2374, (Object)false).define(field_2387, (Object)false).define(field_2375, (Object)0).define(field_2385, (Object)0).define(field_2386, (Object)0).define(field_2389, (Object)ItemStack.EMPTY).define(field_2391, (Object)ItemStack.EMPTY).define(DATA_CURRENT_CLASS, (Object)MatchClass.CLASS_RIFLEMAN.ordinal()).define(field_2393, (Object)(Math.random() < 0.5 ? 1 : 0)).define(DATA_RACE, (Object)BFRace.CAUCASIAN.ordinal()).define(field_2396, (Object)Float.valueOf(0.0f)).define(field_2394, (Object)new BlockPos(0, 0, 0));
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, (Goal)new FloatGoal((Mob)this));
        this.goalSelector.addGoal(0, (Goal)new FlamethowerFireBotGoal(this, 4.0f));
        this.goalSelector.addGoal(0, (Goal)new GrenadeBotGoal(this, 8.0f));
        this.goalSelector.addGoal(1, (Goal)new BotFireGunGoal(this, 0.4f, 16.0f, 8.0f));
        this.goalSelector.addGoal(2, (Goal)new BotHealPlayerGoal(this));
        this.goalSelector.addGoal(3, (Goal)new BotReloadGoal(this));
        this.goalSelector.addGoal(4, (Goal)new BotCapturePointGoal(this, 0.5));
        this.targetSelector.addGoal(1, (Goal)new HurtByTargetGoal((PathfinderMob)this, new Class[0]));
        this.targetSelector.addGoal(5, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, true));
        this.targetSelector.addGoal(5, (Goal)new NearestAttackableTargetGoal((Mob)this, BotEntity.class, true));
    }

    public boolean canAttack(LivingEntity livingEntity) {
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is null!";
        if (!livingEntity.isAlive() || livingEntity.tickCount <= 40) {
            return false;
        }
        if (livingEntity instanceof BotEntity) {
            BotEntity botEntity = (BotEntity)livingEntity;
            GameTeam gameTeam = botEntity.getTeam();
            return gameTeam != null && !gameTeam.equals(this.getTeam());
        }
        if (livingEntity instanceof Player) {
            AbstractGame<?, ?, ?> abstractGame;
            Player player = (Player)livingEntity;
            if (player.isCreative()) {
                return false;
            }
            Object obj = bFAbstractManager.getPlayerDataHandler();
            UUID uUID = player.getUUID();
            Object d = ((PlayerDataHandler)obj).getPlayerData(player);
            Object object = player.getVehicle();
            if (object instanceof AbstractVehicleEntity && (object = ((AbstractVehicleEntity)((Object)(abstractGame = (AbstractVehicleEntity)object))).method_2324((Entity)player)) != null && (((BF_623)object).field_2662 || ((BF_623)object).field_2661)) {
                return false;
            }
            if (BFUtils.isPlayerUnavailable(player, d)) {
                return false;
            }
            abstractGame = bFAbstractManager.getGameWithPlayer(uUID);
            if (abstractGame != null && abstractGame == this.game) {
                object = ((AbstractGamePlayerManager)abstractGame.getPlayerManager()).getPlayerTeam(uUID);
                return object == null || !((GameTeam)object).equals(this.getTeam());
            }
            return true;
        }
        return false;
    }

    public void die(@NotNull DamageSource damageSource) {
        super.die(damageSource);
        Level level = this.level();
        if (this.random.nextFloat() < 0.75f) {
            int n = this.random.nextInt(1000);
            SoundEvent soundEvent = n == 0 ? (SoundEvent)BFSounds.TEAM_SHARED_SCREAM_WILHELM.get() : (SoundEvent)BFSounds.TEAM_SHARED_SCREAM.get();
            SoundEvent soundEvent2 = damageSource.is(DamageTypeTags.IS_EXPLOSION) ? (SoundEvent)BFSounds.TEAM_SHARED_SCREAM_EXPLOSION.get() : soundEvent;
            this.playSound(soundEvent2, 1.5f, this.field_2370);
        }
        List list = level.getEntitiesOfClass(BotEntity.class, this.getBoundingBox().inflate(8.0, 1.0, 8.0));
        for (SoundEvent soundEvent2 : list) {
            String string;
            if (soundEvent2 == null || soundEvent2.equals((Object)this) || !(string = soundEvent2.method_2031()).equals(this.method_2031()) || !(this.random.nextFloat() < 0.2f)) continue;
            soundEvent2.method_2011().ifPresent(arg_0 -> BotEntity.method_2016((BotEntity)soundEvent2, arg_0));
        }
    }

    public boolean hurt(@NotNull DamageSource damageSource, float f) {
        if (damageSource instanceof FirearmDamageSource) {
            FirearmDamageSource firearmDamageSource = (FirearmDamageSource)damageSource;
            this.method_2011().ifPresent(deferredHolder -> this.method_1993(((BotVoice)deferredHolder.get()).hurtSound()));
            Entity entity = firearmDamageSource.getEntity();
            if (entity instanceof BotEntity) {
                BotEntity botEntity = (BotEntity)entity;
                botEntity.method_2011().ifPresent(deferredHolder -> botEntity.method_1993(((BotVoice)deferredHolder.get()).tauntSound()));
            }
        }
        return super.hurt(damageSource, f);
    }

    @NotNull
    public String getScoreboardName() {
        return this.getHumanName();
    }

    public void method_1993(@NotNull DeferredHolder<SoundEvent, SoundEvent> deferredHolder) {
        int n = 30;
        if (((SoundEvent)deferredHolder.get()).toString().contains("bot.go")) {
            if (this.field_2381 > 0) {
                return;
            }
            n = 15;
        }
        this.field_2381 = (int)(40.0 + 300.0 * Math.random());
        if (this.field_2380 > 0) {
            return;
        }
        this.field_2380 = 40;
        if (this.game != null) {
            PacketUtils.sendToGamePlayers(new BFEntitySoundPacket((Holder<SoundEvent>)deferredHolder, SoundSource.HOSTILE, this.getId(), this.field_2370), this.game);
        }
        this.method_2028(n);
    }

    public void method_1997() {
        this.field_2382 = 4;
    }

    public int method_1999() {
        return this.field_2382;
    }

    @Override
    public void method_1937(@NotNull ServerLevel serverLevel) {
        super.method_1937(serverLevel);
        AbstractGame abstractGame = this.game;
        if (abstractGame instanceof IHasBots) {
            IHasBots iHasBots = (IHasBots)((Object)abstractGame);
            iHasBots.method_3389(this);
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putString("name", this.getHumanName());
        ItemStack itemStack = this.method_2014();
        compoundTag.putBoolean("hasPrimary", !itemStack.isEmpty());
        if (!itemStack.isEmpty()) {
            compoundTag.put("primary", itemStack.save((HolderLookup.Provider)this.registryAccess(), (Tag)new CompoundTag()));
        }
        compoundTag.putInt("class", this.getCurrentClass().ordinal());
        compoundTag.putBoolean("respawn", this.field_2367);
        compoundTag.putString("team", this.method_2031());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        GameTeam gameTeam;
        super.readAdditionalSaveData(compoundTag);
        this.setHumanName(compoundTag.getString("name"));
        if (compoundTag.getBoolean("hasPrimary")) {
            this.method_2019(ItemStack.parseOptional((HolderLookup.Provider)this.registryAccess(), (CompoundTag)compoundTag.getCompound("primary")));
        }
        this.setCurrentClass(MatchClass.values()[compoundTag.getInt("class")]);
        this.field_2367 = compoundTag.getBoolean("respawn");
        this.method_2022(compoundTag.getString("team"));
        if (this.game != null && (gameTeam = ((AbstractGamePlayerManager)this.game.getPlayerManager()).getTeamByName(this.method_2031())) != null) {
            this.setTeam(this.game, gameTeam);
        }
    }

    @Nullable
    public ResourceLocation method_1984(@NotNull AbstractGame<?, ?, ?> abstractGame) {
        if (this.field_2364 != null) {
            return this.field_2364;
        }
        this.field_2364 = this.method_1986(abstractGame, this.getCurrentClass().getKey());
        return this.field_2364;
    }

    @Nullable
    public ResourceLocation method_2017(@NotNull AbstractGame<?, ?, ?> abstractGame) {
        if (this.field_2366 != null) {
            return this.field_2366;
        }
        this.field_2366 = this.method_1986(abstractGame, "backpack");
        return this.field_2366;
    }

    @Nullable
    private ResourceLocation method_1986(@NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull String string) {
        GameTeam gameTeam = ((AbstractGamePlayerManager)abstractGame.getPlayerManager()).getTeamByName(this.method_2031());
        if (gameTeam == null) {
            return null;
        }
        DivisionData divisionData = gameTeam.getDivisionData(abstractGame);
        return BFRes.loc("textures/skins/game/nations/" + divisionData.getCountry().getTag() + "/" + divisionData.getSkin() + "/" + string + ".png");
    }

    public ResourceLocation method_2024() {
        if (this.field_2362 == null) {
            this.field_2362 = BFRes.loc("textures/models/entities/bot/" + BFRace.values()[this.method_2003()].getSkin() + "/" + this.method_1998() + ".png");
        }
        return this.field_2362;
    }

    public boolean method_2002() {
        return this.field_2368;
    }

    public String getSkin() {
        return "default";
    }

    @Nullable
    public GameTeam getTeam() {
        return this.team;
    }

    public boolean method_1990(@NotNull Player player) {
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is null!";
        UUID uUID = player.getUUID();
        AbstractGame<?, ?, ?> abstractGame = bFAbstractManager.getGameWithPlayer(uUID);
        if (abstractGame == null) {
            return false;
        }
        GameTeam gameTeam = ((AbstractGamePlayerManager)abstractGame.getPlayerManager()).getPlayerTeam(uUID);
        return gameTeam != null && gameTeam.getName().equalsIgnoreCase(this.method_2031());
    }

    public void setTeam(@NotNull AbstractGame<?, ?, ?> game, @NotNull GameTeam team) {
        this.team = team;
        BFCountry bFCountry = team.isAxis() ? game.getAxisDivision().getCountry() : game.getAlliesDivision().getCountry();
        this.field_2356 = bFCountry.getRandomBotVoice();
    }

    public final float method_1977() {
        return ((Float)this.entityData.get(field_2396)).floatValue();
    }

    public void method_2021(float f) {
        this.entityData.set(field_2396, (Object)Float.valueOf(f));
    }

    public ItemStack method_2014() {
        return (ItemStack)this.entityData.get(field_2389);
    }

    public void method_2019(ItemStack itemStack) {
        this.entityData.set(field_2389, (Object)itemStack);
    }

    public String method_2031() {
        return (String)this.entityData.get(field_2371);
    }

    public void method_2022(String string) {
        this.entityData.set(field_2371, (Object)string);
    }

    public String getHumanName() {
        return (String)this.entityData.get(DATA_HUMAN_NAME);
    }

    public void setHumanName(String humanName) {
        this.entityData.set(DATA_HUMAN_NAME, (Object)humanName);
    }

    public int method_2001() {
        return (Integer)this.entityData.get(field_2375);
    }

    public void method_2025(int n) {
        this.entityData.set(field_2375, (Object)n);
    }

    public int method_2003() {
        return (Integer)this.entityData.get(DATA_RACE);
    }

    public void method_2026(int n) {
        this.entityData.set(DATA_RACE, (Object)n);
    }

    public int method_2005() {
        return (Integer)this.entityData.get(field_2385);
    }

    public void method_2027(int n) {
        this.entityData.set(field_2385, (Object)n);
    }

    public boolean method_2004() {
        return this.method_2005() > 0;
    }

    public int method_2007() {
        return (Integer)this.entityData.get(field_2386);
    }

    public void method_2028(int n) {
        this.field_2379 = n;
        this.entityData.set(field_2386, (Object)n);
    }

    public boolean method_2006() {
        return (Boolean)this.entityData.get(field_2374);
    }

    public void method_1974(boolean bl) {
        this.entityData.set(field_2374, (Object)bl);
    }

    public boolean method_2008() {
        return (Boolean)this.entityData.get(field_2387);
    }

    public void method_1975(boolean bl) {
        this.entityData.set(field_2387, (Object)bl);
    }

    public void method_1976(boolean bl) {
        this.field_2367 = bl;
    }

    public boolean method_2009() {
        return this.field_2367;
    }

    @NotNull
    public MatchClass getCurrentClass() {
        return MatchClass.values()[(Integer)this.entityData.get(DATA_CURRENT_CLASS)];
    }

    public void setCurrentClass(@NotNull MatchClass matchClass) {
        this.entityData.set(DATA_CURRENT_CLASS, (Object)matchClass.ordinal());
    }

    public boolean method_2030() {
        return (Boolean)this.entityData.get(field_2393);
    }

    public void method_1989(BlockPos blockPos) {
        if (!((BlockPos)this.entityData.get(field_2394)).equals((Object)blockPos)) {
            this.entityData.set(field_2394, (Object)blockPos);
        }
    }

    public BlockPos method_2013() {
        return (BlockPos)this.entityData.get(field_2394);
    }

    public Optional<DeferredHolder<BotVoice, ? extends BotVoice>> method_2011() {
        return Optional.ofNullable(this.field_2356);
    }

    public void knockback(double d, double d2, double d3) {
    }

    public void method_2029(int n) {
        this.field_2365 = true;
        this.field_2378 = n;
    }

    private static /* synthetic */ void method_2016(BotEntity botEntity, DeferredHolder deferredHolder) {
        botEntity.method_1993(((BotVoice)deferredHolder.get()).hurtSound());
    }
}

