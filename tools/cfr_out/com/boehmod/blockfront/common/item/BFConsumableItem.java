/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.item;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.item.BFUtilityItem;
import com.boehmod.blockfront.common.item.IRenderConsumeInfo;
import com.boehmod.blockfront.common.net.packet.BFUseItemPacket;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.tag.IHasConsumables;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.PacketUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public abstract class BFConsumableItem
extends BFUtilityItem
implements IRenderConsumeInfo {
    public static int ticksUntilAction = 0;

    public BFConsumableItem(@NotNull String string, @NotNull Item.Properties properties) {
        super(string, properties);
    }

    @OnlyIn(value=Dist.CLIENT)
    public abstract boolean method_4072(@NotNull BFAbstractManager<?, ?, ?> var1, @NotNull AbstractGame<?, ?, ?> var2, @NotNull Level var3, @NotNull Player var4, @NotNull BFConsumableItem var5, @NotNull ItemStack var6);

    public void method_4073(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull ServerLevel serverLevel, @NotNull Player player, @NotNull BFConsumableItem bFConsumableItem, @NotNull ItemStack itemStack) {
        IHasConsumables iHasConsumables;
        AbstractGame<?, ?, ?> abstractGame = bFAbstractManager.getGameWithPlayer(player.getUUID());
        if (abstractGame instanceof IHasConsumables && (iHasConsumables = (IHasConsumables)((Object)abstractGame)).canUseConsumable((Level)serverLevel, player, bFConsumableItem, itemStack)) {
            iHasConsumables.method_3427(serverLevel, player, bFConsumableItem, itemStack);
        }
    }

    public abstract int method_4074();

    @NotNull
    public abstract SoundEvent method_4075();

    @OnlyIn(value=Dist.CLIENT)
    public void inventoryTick(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull Entity entity, int n, boolean bl) {
        IHasConsumables iHasConsumables;
        boolean bl2;
        Player player;
        block14: {
            block13: {
                super.inventoryTick(itemStack, level, entity, n, bl);
                if (!(entity instanceof Player)) break block13;
                player = (Player)entity;
                if (level.isClientSide() && bl) break block14;
            }
            return;
        }
        Vec3 vec3 = player.getDeltaMovement();
        Vec3 vec32 = player.position();
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        ClientPlayerDataHandler clientPlayerDataHandler = (ClientPlayerDataHandler)bFClientManager.getPlayerDataHandler();
        AbstractGame<?, ?, ?> abstractGame = bFClientManager.getGame();
        BFClientPlayerData bFClientPlayerData = (BFClientPlayerData)clientPlayerDataHandler.getPlayerData(player);
        boolean bl3 = bl2 = abstractGame == null || this.method_4072(bFClientManager, abstractGame, level, player, this, itemStack);
        if (BFUtils.isPlayerUnavailable(player, bFClientPlayerData)) {
            bl2 = false;
        }
        if (abstractGame instanceof IHasConsumables && !(iHasConsumables = (IHasConsumables)((Object)abstractGame)).canUseConsumable(level, player, this, itemStack)) {
            bl2 = false;
        }
        if (!bl2) {
            ticksUntilAction = 0;
            return;
        }
        if (Minecraft.getInstance().mouseHandler.isRightPressed()) {
            entity.setDeltaMovement(0.0, vec3.y, 0.0);
            entity.setPos(entity.xOld, entity.yOld, entity.zOld);
            if (ticksUntilAction == 4) {
                level.playLocalSound(vec32.x, vec32.y, vec32.z, SoundEvents.ENDER_DRAGON_FLAP, SoundSource.BLOCKS, 1.0f, 1.5f, false);
            }
            if (ticksUntilAction % this.method_4074() == 0 && ticksUntilAction != 0) {
                level.playLocalSound(vec32.x, vec32.y, vec32.z, this.method_4075(), SoundSource.BLOCKS, 1.0f, (float)((double)0.8f + (double)0.4f * Math.random()), false);
            }
            if (ticksUntilAction++ >= this.getTotalTicksUntilAction()) {
                level.playLocalSound(vec32.x, vec32.y, vec32.z, SoundEvents.ENDER_DRAGON_FLAP, SoundSource.BLOCKS, 1.0f, 1.2f, false);
                PacketUtils.sendToServer(new BFUseItemPacket());
                ticksUntilAction = -10;
            }
        } else {
            ticksUntilAction = 0;
        }
    }

    public boolean shouldCauseReequipAnimation(@NotNull ItemStack itemStack, @NotNull ItemStack itemStack2, boolean bl) {
        return ticksUntilAction <= 0 || ticksUntilAction % this.method_4074() == 0;
    }

    @Override
    public int getTicksUntilAction() {
        return ticksUntilAction;
    }

    @Override
    public abstract int getTotalTicksUntilAction();

    @Override
    @NotNull
    public abstract String getTranslation();

    @Override
    public abstract int getColor();

    @Override
    @NotNull
    public abstract ResourceLocation getIcon();
}

