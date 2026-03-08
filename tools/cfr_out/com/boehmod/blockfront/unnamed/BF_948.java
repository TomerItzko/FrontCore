/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.core.Holder
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.common.entity.BFProjectileEntity;
import com.boehmod.blockfront.common.gun.GunDamageConfig;
import com.boehmod.blockfront.common.gun.bullet.AbstractBulletCollision;
import com.boehmod.blockfront.common.gun.bullet.BlockBulletCollision;
import com.boehmod.blockfront.common.gun.bullet.BulletTracer;
import com.boehmod.blockfront.common.gun.bullet.LivingBulletCollision;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.net.packet.BFBulletCollisionBlockClientPacket;
import com.boehmod.blockfront.common.net.packet.BFBulletCollisionLivingClientPacket;
import com.boehmod.blockfront.common.net.packet.BFBulletTracerToClientPacket;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.GameHolder;
import com.boehmod.blockfront.unnamed.BF_1194;
import com.boehmod.blockfront.unnamed.BF_1196;
import com.boehmod.blockfront.unnamed.BF_623;
import com.boehmod.blockfront.unnamed.BF_631;
import com.boehmod.blockfront.unnamed.BF_632;
import com.boehmod.blockfront.unnamed.BF_633;
import com.boehmod.blockfront.unnamed.BF_668;
import com.boehmod.blockfront.util.PacketUtils;
import com.boehmod.blockfront.util.debug.DebugBox;
import com.boehmod.blockfront.util.debug.DebugLine;
import com.boehmod.blockfront.util.math.MathUtils;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class BF_948<H extends PlayerDataHandler<?>>
extends AbstractBulletCollision<ServerLevel, LivingEntity, H, BFAbstractManager<H, ?, ?>> {
    @Nullable
    private ServerLevel field_6839;

    public BF_948(@NotNull H h, @NotNull GunDamageConfig gunDamageConfig) {
        super(h, gunDamageConfig);
    }

    public static void method_3948(@NotNull ServerLevel serverLevel, @NotNull LivingEntity livingEntity, @Nullable AbstractGame<?, ?, ?> abstractGame, @NotNull List<BlockBulletCollision> list) {
        BFBulletCollisionBlockClientPacket bFBulletCollisionBlockClientPacket = new BFBulletCollisionBlockClientPacket(livingEntity.getId(), list);
        if (abstractGame != null) {
            PacketUtils.sendToGamePlayers(bFBulletCollisionBlockClientPacket, abstractGame);
        } else {
            PacketUtils.sendToAllPlayers(bFBulletCollisionBlockClientPacket);
        }
        for (BlockBulletCollision blockBulletCollision : list) {
            BF_668.method_5646((Level)serverLevel, blockBulletCollision);
        }
    }

    public static void method_3944(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull Level level, @NotNull LivingEntity livingEntity, @Nullable AbstractGame<?, ?, ?> abstractGame, @NotNull List<LivingBulletCollision> list) {
        Object object;
        BF_623 bF_623;
        BFBulletCollisionLivingClientPacket bFBulletCollisionLivingClientPacket = new BFBulletCollisionLivingClientPacket(list);
        ItemStack itemStack = livingEntity.getItemInHand(InteractionHand.MAIN_HAND);
        Entity object22 = livingEntity.getVehicle();
        if (object22 instanceof AbstractVehicleEntity && (bF_623 = ((AbstractVehicleEntity)(object = (AbstractVehicleEntity)object22)).method_2324((Entity)livingEntity)) != null) {
            for (BF_633<?> bF_633 : bF_623.method_2388()) {
                BF_631 bF_631;
                if (!(bF_633 instanceof BF_631) || (bF_631 = (BF_631)bF_633).method_2442() != BF_632.BULLET) continue;
                itemStack = new ItemStack(bF_631.method_2459());
            }
        }
        if (abstractGame != null) {
            PacketUtils.sendToGamePlayers(bFBulletCollisionLivingClientPacket, abstractGame);
        } else {
            PacketUtils.sendToAllPlayers(bFBulletCollisionLivingClientPacket);
        }
        for (LivingBulletCollision livingBulletCollision : list) {
            if (!BF_948.method_3945(bFAbstractManager, playerDataHandler, level, livingEntity, livingBulletCollision, itemStack)) continue;
            return;
        }
    }

    private static boolean method_3945(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull Level level, @NotNull LivingEntity livingEntity, @NotNull LivingBulletCollision livingBulletCollision, @NotNull ItemStack itemStack) {
        int n = livingBulletCollision.entityId();
        Entity entity = level.getEntity(n);
        if (entity == null || !entity.isAlive()) {
            return true;
        }
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity2 = (LivingEntity)entity;
            BF_668.method_2572(bFAbstractManager, playerDataHandler, level, livingEntity, itemStack, livingEntity2, livingBulletCollision);
        } else if (entity instanceof BFProjectileEntity) {
            BFProjectileEntity bFProjectileEntity = (BFProjectileEntity)entity;
            bFProjectileEntity.method_1958(livingEntity);
        }
        return false;
    }

    @Override
    public void method_4268(@NotNull LivingEntity livingEntity, @NotNull GunItem gunItem, @NotNull RandomSource randomSource, @NotNull Vec3 vec3, @NotNull Vec3 vec32) {
        if (!(livingEntity instanceof GameHolder)) {
            return;
        }
        GameHolder gameHolder = (GameHolder)livingEntity;
        if (randomSource.nextFloat() >= gunItem.method_4168()) {
            return;
        }
        AbstractGame<?, ?, ?> abstractGame = gameHolder.getGame();
        BulletTracer bulletTracer = new BulletTracer((Holder<Item>)gunItem.builtInRegistryHolder(), vec3, vec32, gunItem.method_4183());
        BFBulletTracerToClientPacket bFBulletTracerToClientPacket = new BFBulletTracerToClientPacket(livingEntity.getId(), bulletTracer);
        if (abstractGame != null) {
            PacketUtils.sendToGamePlayers(bFBulletTracerToClientPacket, abstractGame);
        } else {
            PacketUtils.sendToAllPlayers(bFBulletTracerToClientPacket);
        }
        this.method_4261(new DebugLine(vec3.add(0.0, (double)0.1f, 0.0), vec32.add(0.0, (double)0.1f, 0.0), 200, true, -838669));
    }

    @Override
    public void method_4264(@NotNull BFAbstractManager<H, ?, ?> bFAbstractManager, @NotNull ServerLevel serverLevel, @NotNull LivingEntity livingEntity, @NotNull List<LivingBulletCollision> list, RandomSource randomSource, @NotNull Random random, long l, long l2) {
        if (livingEntity instanceof GameHolder) {
            GameHolder gameHolder = (GameHolder)livingEntity;
            BF_948.method_3944(bFAbstractManager, this.field_4112, (Level)serverLevel, livingEntity, gameHolder.getGame(), list);
        }
    }

    @Override
    public void method_4263(@NotNull BFAbstractManager<H, ?, ?> bFAbstractManager, @NotNull ServerLevel serverLevel, @NotNull LivingEntity livingEntity, @NotNull List<BlockBulletCollision> list) {
        if (livingEntity instanceof GameHolder) {
            GameHolder gameHolder = (GameHolder)livingEntity;
            BF_948.method_3948(serverLevel, livingEntity, gameHolder.getGame(), list);
        }
    }

    @Override
    public Vec3 method_4269(@NotNull LivingEntity livingEntity, @NotNull RandomSource randomSource) {
        float f = 0.0f;
        ItemStack itemStack = livingEntity.getItemInHand(InteractionHand.MAIN_HAND);
        Item item = itemStack.getItem();
        if (item instanceof GunItem) {
            GunItem gunItem = (GunItem)item;
            f = gunItem.getAimConfig().walkingSpread();
        }
        float f2 = (float)((double)f * randomSource.nextGaussian());
        float f3 = (float)((double)f * randomSource.nextGaussian());
        return MathUtils.lookingVec((Entity)livingEntity, f3, f2);
    }

    @Override
    @NotNull
    public Vec3 method_4266(@NotNull LivingEntity livingEntity) {
        return field_4104.zRot((livingEntity.getXRot() + 90.0f) * ((float)Math.PI / 180) - 1.5707964f).yRot(-livingEntity.getYRot() * ((float)Math.PI / 180) - 1.5707964f);
    }

    @Override
    public void method_4261(@NotNull DebugLine debugLine) {
    }

    @Override
    public void method_5788(@NotNull DebugBox debugBox) {
    }

    @Override
    @NotNull
    protected BF_1194 method_5789(@NotNull BFAbstractManager<H, ?, ?> bFAbstractManager, @NotNull ServerLevel serverLevel) {
        if (this.field_6839 == null) {
            throw new IllegalStateException("No level set for ServerBulletSpawner - call spawnBullet first");
        }
        return new BF_1196((Level)this.field_6839, (int)serverLevel.getGameTime());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void method_5790(@NotNull BFAbstractManager<H, ?, ?> bFAbstractManager, @NotNull ServerLevel serverLevel, @NotNull RandomSource randomSource, @NotNull Random random, @NotNull LivingEntity livingEntity, @NotNull GunItem gunItem, long l, long l2) {
        this.field_6839 = serverLevel;
        try {
            super.method_5790(bFAbstractManager, serverLevel, randomSource, random, livingEntity, gunItem, l, l2);
        }
        finally {
            this.field_6839 = null;
        }
    }
}

