/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.item;

import com.boehmod.blockfront.common.entity.AcidBallEntity;
import com.boehmod.blockfront.common.item.BFUtilityItem;
import com.boehmod.blockfront.registry.BFEntityTypes;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class AcidBallItem
extends BFUtilityItem {
    public AcidBallItem(@NotNull String string, Item.Properties properties) {
        super(string, properties);
    }

    @NotNull
    public InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand interactionHand) {
        AcidBallEntity acidBallEntity;
        ItemStack itemStack = player.getItemInHand(interactionHand);
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5f, 0.4f / (threadLocalRandom.nextFloat() * 0.4f + 0.8f));
        boolean bl = level.isClientSide();
        if (!bl && (acidBallEntity = (AcidBallEntity)((EntityType)BFEntityTypes.ACID_BALL.get()).create(level)) != null) {
            acidBallEntity.setPos(player.getX(), player.getEyeY(), player.getZ());
            acidBallEntity.setItem(itemStack);
            acidBallEntity.shootFromRotation((Entity)player, player.getXRot(), player.getYRot(), 0.0f, 1.5f, 1.0f);
            level.addFreshEntity((Entity)acidBallEntity);
        }
        player.awardStat(Stats.ITEM_USED.get((Object)this));
        return InteractionResultHolder.sidedSuccess((Object)itemStack, (boolean)bl);
    }
}

