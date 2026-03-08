/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.item;

import com.boehmod.blockfront.client.event.BFRenderFrameSubscriber;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.item.BFConsumableItem;
import com.boehmod.blockfront.common.item.IRenderConsumeInfo;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.unnamed.BF_809;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public final class BombItem
extends BFConsumableItem
implements IRenderConsumeInfo {
    private static final int field_3646 = 80;

    public BombItem(@NotNull String string, @NotNull Item.Properties properties) {
        super(string, properties);
    }

    @Override
    public void method_4073(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull ServerLevel serverLevel, @NotNull Player player, @NotNull BFConsumableItem bFConsumableItem, @NotNull ItemStack itemStack) {
        AbstractGame<?, ?, ?> abstractGame = bFAbstractManager.getGameWithPlayer(player.getUUID());
        if (abstractGame instanceof BF_809) {
            BF_809 bF_809 = (BF_809)((Object)abstractGame);
            bF_809.method_3385((Level)serverLevel, player, itemStack);
        }
    }

    @Override
    public boolean method_4072(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull Level level, @NotNull Player player, @NotNull BFConsumableItem bFConsumableItem, @NotNull ItemStack itemStack) {
        if (abstractGame instanceof BF_809) {
            BF_809 bF_809 = (BF_809)((Object)abstractGame);
            return bF_809.method_3383(level, player);
        }
        return true;
    }

    @Override
    public int getTotalTicksUntilAction() {
        return 80;
    }

    @Override
    @NotNull
    public String getTranslation() {
        return "bf.message.planting";
    }

    @Override
    public int getColor() {
        return 8271921;
    }

    @Override
    @NotNull
    public ResourceLocation getIcon() {
        return BFRenderFrameSubscriber.AXIS_ICON_TEXTURE;
    }

    @Override
    public int method_4074() {
        return 4;
    }

    @Override
    @NotNull
    public SoundEvent method_4075() {
        return (SoundEvent)BFSounds.ITEM_BOMB_BUTTON.get();
    }
}

