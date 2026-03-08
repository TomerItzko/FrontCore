/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.item;

import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.item.SyringeItem;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFUtils;
import java.util.Set;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class AdrenalineSyringeItem
extends SyringeItem {
    @NotNull
    private static final Component field_3639 = Component.translatable((String)"bf.popup.message.adrenaline.syringe.adrenalized").withStyle(ChatFormatting.GREEN);

    public AdrenalineSyringeItem(@NotNull String string, Item.Properties properties) {
        super(string, properties);
    }

    @Override
    boolean method_4079(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull Player player, @NotNull Player player2, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull AbstractGamePlayerManager<?> abstractGamePlayerManager, @NotNull Set<UUID> set) {
        UUID uUID = player.getUUID();
        UUID uUID2 = player2.getUUID();
        if (set.contains(uUID2)) {
            return BFUtils.isSameTeam(bFAbstractManager, uUID, uUID2);
        }
        return false;
    }

    @Override
    boolean method_4082(@NotNull Player player, @NotNull Player player2) {
        return false;
    }

    @Override
    public void method_4083(@NotNull ItemStack itemStack, @NotNull Player player, @NotNull Player player2) {
        player2.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 120, 1, false, false));
        player2.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 120, 1, false, false));
        player.playSound((SoundEvent)BFSounds.ITEM_SYRINGE_USE.get(), 1.0f, 1.0f);
    }

    @Override
    @NotNull
    Component method_4081(@NotNull Player player) {
        return field_3639;
    }

    @Override
    @NotNull
    Component method_4080(@NotNull Component component) {
        return Component.translatable((String)"bf.message.gamemode.adrenalinesyringe.adrenalize", (Object[])new Object[]{component}).withStyle(ChatFormatting.GRAY);
    }
}

