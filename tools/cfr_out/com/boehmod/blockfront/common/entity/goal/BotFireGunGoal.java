/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.Pair
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.ai.goal.Goal$Flag
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.entity.goal;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.entity.BotEntity;
import com.boehmod.blockfront.common.entity.goal.BotGoal;
import com.boehmod.blockfront.common.gun.GunFireConfig;
import com.boehmod.blockfront.common.gun.GunSoundConfig;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.registry.custom.BotVoice;
import com.boehmod.blockfront.unnamed.BF_948;
import it.unimi.dsi.fastutil.Pair;
import java.util.EnumSet;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class BotFireGunGoal
extends BotGoal {
    private static final float field_2415 = 30.0f;
    private final double field_2410;
    private final double field_2411;
    private final float field_2416;
    private boolean field_2412;
    private boolean field_2413;
    private int field_2417 = -1;
    private int field_2418 = 5;
    private int field_2419 = 0;

    public BotFireGunGoal(@NotNull BotEntity botEntity, float f, float f2, float f3) {
        super(botEntity);
        this.field_2416 = f;
        this.field_2410 = f2;
        this.field_2411 = f3;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.botEntity.method_2008()) {
            return false;
        }
        ItemStack itemStack = this.botEntity.getMainHandItem();
        return itemStack.getItem() instanceof GunItem && GunItem.getAmmoLoaded(itemStack) > 0 && this.botEntity.method_2000();
    }

    public boolean canContinueToUse() {
        return this.canUse();
    }

    public void start() {
        super.start();
        this.botEntity.method_2011().ifPresent(deferredHolder -> this.botEntity.method_1993(((BotVoice)deferredHolder.get()).spottedSound()));
    }

    public void tick() {
        super.tick();
        Level level = this.botEntity.level();
        RandomSource randomSource = level.random;
        Pair<Vec3, Double> pair = this.botEntity.method_1979();
        if (pair != null) {
            Item item;
            ItemStack itemStack;
            BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
            assert (bFAbstractManager != null) : "Mod manager is null!";
            Object obj = bFAbstractManager.getPlayerDataHandler();
            Vec3 vec3 = (Vec3)pair.first();
            double d = (Double)pair.second();
            LivingEntity livingEntity = this.botEntity.getTarget();
            if (livingEntity != null) {
                this.botEntity.method_1989(livingEntity.getOnPos());
            }
            this.botEntity.getLookControl().setLookAt(vec3.x, d, vec3.z, 30.0f, 30.0f);
            this.botEntity.method_1992(vec3, d, 30.0f, 30.0f);
            if (this.field_2417 <= 0 && randomSource.nextFloat() < 0.1f) {
                if (randomSource.nextFloat() < 0.3f) {
                    boolean bl = this.field_2412 = !this.field_2412;
                }
                if (randomSource.nextFloat() < 0.3f) {
                    this.field_2413 = !this.field_2413;
                }
                this.field_2417 = 5;
            }
            if (this.field_2419 > 0) {
                --this.field_2419;
                return;
            }
            if (this.field_2418 > 0) {
                --this.field_2418;
                return;
            }
            --this.field_2417;
            float f = (float)this.botEntity.position().distanceToSqr(vec3);
            if (this.field_2417 <= 0) {
                boolean bl = false;
                if ((double)f > this.field_2410) {
                    bl = true;
                    this.field_2413 = false;
                } else if ((double)f < this.field_2411) {
                    bl = true;
                    this.field_2413 = true;
                }
                this.botEntity.getMoveControl().strafe(bl ? (this.field_2413 ? -this.field_2416 : this.field_2416) : 0.0f, this.field_2412 ? this.field_2416 : -this.field_2416);
                if (randomSource.nextFloat() < 0.2f) {
                    this.field_2417 = 10;
                }
            }
            if (!(itemStack = this.botEntity.getMainHandItem()).isEmpty() && (item = itemStack.getItem()) instanceof GunItem) {
                GunItem gunItem = (GunItem)item;
                int n = GunItem.getAmmoLoaded(itemStack);
                if (n > 0) {
                    this.method_2048(bFAbstractManager, (PlayerDataHandler<?>)obj, itemStack, gunItem);
                }
            }
        } else {
            this.field_2419 = 15;
            this.field_2418 = 5;
        }
    }

    private void method_2048(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ItemStack itemStack, @NotNull GunItem gunItem) {
        Object object = this.botEntity.level();
        if (!(object instanceof ServerLevel)) {
            return;
        }
        ServerLevel serverLevel = (ServerLevel)object;
        object = gunItem.getSoundConfig(itemStack);
        RandomSource randomSource = serverLevel.random;
        GunFireConfig gunFireConfig = gunItem.getDefaultFireConfig();
        this.field_2418 = gunFireConfig.getFireRate() / 2;
        if (((GunSoundConfig)object).getReloadBolt() != null) {
            int n = this.field_2418 = (double)randomSource.nextFloat() < 0.2 ? 50 : 30;
        }
        if (gunItem.method_4156(itemStack, (Level)serverLevel, (LivingEntity)this.botEntity)) {
            this.botEntity.method_2025(2);
            int n = gunFireConfig.method_4026();
            for (int i = 0; i < n; ++i) {
                int n2 = (int)serverLevel.getGameTime();
                long l = serverLevel.random.nextLong();
                new BF_948(playerDataHandler, gunItem.getDamageConfig()).method_5790(bFAbstractManager, serverLevel, randomSource, (Random)ThreadLocalRandom.current(), (LivingEntity)this.botEntity, gunItem, (long)n2, l);
            }
            if ((double)randomSource.nextFloat() < 0.05) {
                this.field_2419 = ThreadLocalRandom.current().nextInt(20);
            }
            this.botEntity.method_2027(this.field_2418 + this.field_2419 + 20);
        }
    }
}

