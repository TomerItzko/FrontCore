/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.item;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.item.BFCommonItem;
import com.boehmod.blockfront.common.net.packet.BFExplosionPacket;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.common.world.ExplosionType;
import com.boehmod.blockfront.registry.BFDataComponents;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.PacketUtils;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class SuperHappyFunBombItem
extends BFCommonItem {
    private static final float field_3972 = 6.0f;

    public SuperHappyFunBombItem(String string, Item.Properties properties) {
        super(string, properties);
    }

    private static void method_4078(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull Entity entity) {
        Vec3 vec3 = entity.position();
        itemStack.shrink(1);
        Explosion explosion = BFUtils.explosionFromPlayer(level, entity, vec3, itemStack, 6.0f);
        explosion.explode();
        explosion.finalizeExplosion(true);
        explosion.clearToBlow();
        BFExplosionPacket bFExplosionPacket = new BFExplosionPacket(ExplosionType.GENERIC, vec3);
        PacketUtils.sendToAllPlayers(bFExplosionPacket);
    }

    public void inventoryTick(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull Entity entity, int n, boolean bl) {
        super.inventoryTick(itemStack, level, entity, n, bl);
        if (!entity.isAlive() || level.isClientSide()) {
            return;
        }
        if (entity instanceof Player) {
            Player player = (Player)entity;
            BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
            assert (bFAbstractManager != null) : "Mod manager is null!";
            Object obj = bFAbstractManager.getPlayerDataHandler();
            Object d = ((PlayerDataHandler)obj).getPlayerData(player);
            if (BFUtils.isPlayerUnavailable(player, d)) {
                return;
            }
        }
        boolean bl2 = (Boolean)itemStack.getOrDefault(BFDataComponents.ACTIVE, (Object)false);
        int n2 = (Integer)itemStack.getOrDefault(BFDataComponents.EXPLODE_TIME, (Object)0);
        if (bl2) {
            if (n2 > 0) {
                itemStack.set(BFDataComponents.EXPLODE_TIME, (Object)(n2 - 1));
            }
            if (n2 == 0) {
                SuperHappyFunBombItem.method_4078(itemStack, level, entity);
            }
        }
    }

    @NotNull
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        boolean bl = (Boolean)itemStack.getOrDefault(BFDataComponents.ACTIVE, (Object)false);
        if (bl) {
            return InteractionResultHolder.fail((Object)itemStack);
        }
        if (!itemStack.isEmpty() && itemStack.getItem() instanceof SuperHappyFunBombItem) {
            itemStack.set(BFDataComponents.EXPLODE_TIME, (Object)43);
            itemStack.set(BFDataComponents.ACTIVE, (Object)true);
            player.playSound((SoundEvent)BFSounds.ITEM_GRENADE_THROW.get());
        }
        return InteractionResultHolder.consume((Object)itemStack);
    }
}

