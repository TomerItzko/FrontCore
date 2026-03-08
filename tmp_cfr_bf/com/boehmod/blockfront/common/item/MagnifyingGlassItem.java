/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.item;

import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.item.BFConsumableItem;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.impl.ttt.TTTPlayerRole;
import com.boehmod.blockfront.util.BFRes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public final class MagnifyingGlassItem
extends BFConsumableItem {
    private static final int field_3648 = 60;
    private static final ResourceLocation field_3647 = BFRes.loc("textures/gui/game/troubletown/detective.png");

    public MagnifyingGlassItem(@NotNull String string, Item.Properties properties) {
        super(string, properties);
    }

    @Override
    public boolean method_4072(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull Level level, @NotNull Player player, @NotNull BFConsumableItem bFConsumableItem, @NotNull ItemStack itemStack) {
        return true;
    }

    @Override
    public int getTotalTicksUntilAction() {
        return 60;
    }

    @Override
    @NotNull
    public String getTranslation() {
        return "bf.message.magnifying.glass.inspecting";
    }

    @Override
    public int getColor() {
        return TTTPlayerRole.DETECTIVE.getColor();
    }

    @Override
    @NotNull
    public ResourceLocation getIcon() {
        return field_3647;
    }

    @Override
    public int method_4074() {
        return 16;
    }

    @Override
    @NotNull
    public SoundEvent method_4075() {
        return SoundEvents.BOOK_PAGE_TURN;
    }
}

