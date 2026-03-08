/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.item;

import com.boehmod.blockfront.client.event.BFRenderFrameSubscriber;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.entity.BombEntity;
import com.boehmod.blockfront.common.item.BFConsumableItem;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.unnamed.BF_809;
import com.boehmod.blockfront.util.BFUtils;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public final class BombDefuseKitItem
extends BFConsumableItem {
    private static final int field_3645 = 100;
    private static final float field_3644 = 0.75f;

    public BombDefuseKitItem(@NotNull String string, @NotNull Item.Properties properties) {
        super(string, properties);
    }

    @Override
    public void method_4073(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull ServerLevel serverLevel, @NotNull Player player, @NotNull BFConsumableItem bFConsumableItem, @NotNull ItemStack itemStack) {
        UUID uUID = player.getUUID();
        AbstractGame<?, ?, ?> abstractGame = bFAbstractManager.getGameWithPlayer(uUID);
        if (abstractGame instanceof BF_809) {
            BF_809 bF_809 = (BF_809)((Object)abstractGame);
            BombEntity bombEntity = BFUtils.method_2972((Level)serverLevel, (Entity)player, BombEntity.class, 0.75f);
            if (bombEntity != null && !bombEntity.method_1965() && bF_809.method_3384((Level)serverLevel, player, bombEntity) && player instanceof ServerPlayer) {
                ServerPlayer serverPlayer = (ServerPlayer)player;
                bombEntity.method_1963();
                bF_809.method_3381(bombEntity, serverPlayer, uUID);
            }
        }
    }

    @Override
    public boolean method_4072(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull Level level, @NotNull Player player, @NotNull BFConsumableItem bFConsumableItem, @NotNull ItemStack itemStack) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) {
            return false;
        }
        BombEntity bombEntity = BFUtils.method_2972(level, (Entity)player, BombEntity.class, 0.75f);
        if (abstractGame instanceof BF_809) {
            BF_809 bF_809 = (BF_809)((Object)abstractGame);
            return bombEntity != null && !bombEntity.method_1965() && bF_809.method_3384(level, player, bombEntity);
        }
        return bombEntity != null;
    }

    @Override
    public int getTotalTicksUntilAction() {
        return 100;
    }

    @Override
    @NotNull
    public String getTranslation() {
        return "bf.message.bomb.defusal.kit.inspecting";
    }

    @Override
    public int getColor() {
        return 8159560;
    }

    @Override
    @NotNull
    public ResourceLocation getIcon() {
        return BFRenderFrameSubscriber.ALLIES_ICON_TEXTURE;
    }

    @Override
    public int method_4074() {
        return 20;
    }

    @Override
    @NotNull
    public SoundEvent method_4075() {
        return SoundEvents.SNOW_GOLEM_SHEAR;
    }
}

