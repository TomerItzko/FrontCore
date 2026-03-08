/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.floats.FloatFloatPair
 *  javax.annotation.Nullable
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.component.DataComponentType
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.client.gui.layer.MatchGuiLayer;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.entity.BotEntity;
import com.boehmod.blockfront.common.gun.GunDamageConfig;
import com.boehmod.blockfront.common.gun.bullet.BlockBulletCollision;
import com.boehmod.blockfront.common.gun.bullet.LivingBulletCollision;
import com.boehmod.blockfront.common.item.BFWeaponItem;
import com.boehmod.blockfront.common.item.ChestArmorItem;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.player.BFAbstractPlayerData;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.common.player.a;
import com.boehmod.blockfront.common.world.damage.FirearmDamageSource;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.tag.IModifiesDamage;
import com.boehmod.blockfront.registry.BFBlockAttributes;
import com.boehmod.blockfront.registry.BFDataComponents;
import com.boehmod.blockfront.registry.custom.BlockAttribute;
import com.boehmod.blockfront.registry.custom.BlockSoundAttribute;
import com.boehmod.blockfront.unnamed.BF_1180;
import com.boehmod.blockfront.util.BFUtils;
import it.unimi.dsi.fastutil.floats.FloatFloatPair;
import java.lang.runtime.SwitchBootstraps;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public final class BF_668 {
    private static final int field_6711 = 3;
    private static final double field_6709 = 0.25;
    private static final float field_6710 = 4.0f;

    public static void method_2572(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull Level level, @NotNull LivingEntity livingEntity, @Nullable ItemStack itemStack, @NotNull LivingEntity livingEntity2, @NotNull LivingBulletCollision livingBulletCollision) {
        Item item;
        if (itemStack == null || !((item = itemStack.getItem()) instanceof GunItem)) {
            return;
        }
        GunItem gunItem = (GunItem)item;
        if (!BFUtils.entitiesCanAttack(bFAbstractManager, playerDataHandler, livingEntity, livingEntity2)) {
            return;
        }
        float f = livingBulletCollision.damageReduction();
        FirearmDamageSource firearmDamageSource = new FirearmDamageSource(level, (Entity)livingEntity, itemStack, livingBulletCollision.headShot(), f > 0.0f, livingBulletCollision.collateral(), livingBulletCollision.noScope());
        float f2 = livingEntity.distanceTo((Entity)livingEntity2);
        GunDamageConfig gunDamageConfig = gunItem.getDamageConfig();
        FloatFloatPair floatFloatPair = gunDamageConfig.method_3950(f2);
        float f3 = (firearmDamageSource.isHeadshot() ? floatFloatPair.rightFloat() : floatFloatPair.leftFloat()) - f;
        if (livingEntity instanceof BotEntity) {
            f3 /= 5.0f;
        }
        if (f3 <= 0.0f) {
            return;
        }
        if (livingEntity2 instanceof Player) {
            Player player = (Player)livingEntity2;
            UUID uUID = player.getUUID();
            Object obj = playerDataHandler.getPlayerData(player);
            if (((BFAbstractPlayerData)obj).isOutOfGame() || player.getHealth() <= 0.0f || !player.isAlive()) {
                return;
            }
            AbstractGame<?, ?, ?> abstractGame = bFAbstractManager.getGameWithPlayer(uUID);
            if (abstractGame instanceof IModifiesDamage) {
                IModifiesDamage iModifiesDamage = (IModifiesDamage)((Object)abstractGame);
                f3 *= iModifiesDamage.getDamageMultiplier();
            }
        }
        if (f3 > 0.0f) {
            livingEntity2.hurtTime = 0;
            livingEntity2.invulnerableTime = 0;
            if (livingEntity2.hurt((DamageSource)firearmDamageSource, f3)) {
                livingEntity2.setDeltaMovement(0.0, 0.0, 0.0);
                if (livingBulletCollision.backpack()) {
                    BF_668.method_2571(playerDataHandler, level, livingEntity, itemStack, livingEntity2);
                }
                BF_668.method_5647(level, livingEntity2.getBoundingBox().inflate(4.0), BFDataComponents.BLOOD_AMOUNT.get());
            }
        }
    }

    private static void method_2571(@NotNull PlayerDataHandler<?> playerDataHandler, @NotNull Level level, @NotNull LivingEntity livingEntity, @NotNull ItemStack itemStack, @NotNull LivingEntity livingEntity2) {
        ChestArmorItem chestArmorItem;
        ItemStack itemStack2 = livingEntity2.getItemBySlot(EquipmentSlot.CHEST);
        Item item = itemStack2.getItem();
        if (item instanceof ChestArmorItem && (chestArmorItem = (ChestArmorItem)item).method_4239() && livingEntity2 instanceof ServerPlayer) {
            item = (ServerPlayer)livingEntity2;
            Object obj = playerDataHandler.getPlayerData((Player)item);
            if (livingEntity2.getHealth() <= 0.0f) {
                ((a)((BFAbstractPlayerData)obj).method_843()).method_824(level, (ServerPlayer)item);
                return;
            }
            ((a)((BFAbstractPlayerData)obj).method_843()).method_821((ServerPlayer)item, livingEntity, itemStack);
        }
    }

    public static void method_5646(@NotNull Level level, @NotNull BlockBulletCollision blockBulletCollision) {
        BlockPos blockPos = blockBulletCollision.blockPos();
        BlockState blockState = level.getBlockState(blockPos);
        BlockAttribute blockAttribute = BFBlockAttributes.method_4614(level, blockState, blockPos);
        DeferredHolder<BlockSoundAttribute, ? extends BlockSoundAttribute> deferredHolder = blockAttribute.method_4246();
        if (deferredHolder != null) {
            BlockSoundAttribute blockSoundAttribute = (BlockSoundAttribute)deferredHolder.get();
            Vec3 vec3 = blockBulletCollision.hitVec();
            MatchGuiLayer.BF_114.BF_115 bF_115 = blockSoundAttribute.method_4248();
            int n = 0;
            switch (SwitchBootstraps.enumSwitch("enumSwitch", new Object[]{"BLOOD", "DIRT"}, (MatchGuiLayer.BF_114.BF_115)bF_115, n)) {
                case 0: {
                    BF_668.method_5648(level, vec3, 4.0f);
                    break;
                }
                case 1: {
                    BF_668.method_5649(level, vec3, 4.0f);
                    break;
                }
            }
        }
    }

    private static void method_5647(@NotNull Level level, @NotNull AABB aABB, @NotNull DataComponentType<Integer> dataComponentType) {
        for (LivingEntity livingEntity : level.getEntitiesOfClass(LivingEntity.class, aABB)) {
            ItemStack itemStack = livingEntity.getMainHandItem();
            if (!(itemStack.getItem() instanceof BFWeaponItem)) continue;
            int n = (Integer)itemStack.getOrDefault(dataComponentType, (Object)0);
            double d = 0.25 / (double)(n + 1);
            if (!(level.random.nextDouble() < d)) continue;
            itemStack.set(dataComponentType, (Object)Math.min(n + 1, 3));
        }
    }

    public static void method_5648(@NotNull Level level, @NotNull Vec3 vec3, float f) {
        BF_1180.method_5651(level, vec3, f, BFDataComponents.BLOOD_AMOUNT.get(), 0.25);
    }

    public static void method_5649(@NotNull Level level, @NotNull Vec3 vec3, float f) {
        BF_1180.method_5651(level, vec3, f, BFDataComponents.MUD_AMOUNT.get(), 0.25);
    }
}

