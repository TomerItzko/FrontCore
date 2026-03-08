/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.item;

import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.item.SyringeItem;
import com.boehmod.blockfront.common.net.packet.BFCinematicEffectFlashPcket;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.registry.BFParticleTypes;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.PacketUtils;
import java.util.Set;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MedicalSyringeItem
extends SyringeItem {
    @NotNull
    private static final Component field_3956 = Component.translatable((String)"bf.popup.message.medic.bag.player.healed").withStyle(ChatFormatting.GREEN);

    public MedicalSyringeItem(@NotNull String string, Item.Properties properties) {
        super(string, properties);
    }

    @Override
    public boolean method_4079(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull Player player, @NotNull Player player2, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull AbstractGamePlayerManager<?> abstractGamePlayerManager, @NotNull Set<UUID> set) {
        UUID uUID = player.getUUID();
        UUID uUID2 = player2.getUUID();
        if (set.contains(uUID2) && BFUtils.isSameTeam(bFAbstractManager, uUID, uUID2)) {
            return player2.getHealth() < player2.getMaxHealth();
        }
        return false;
    }

    @Override
    boolean method_4082(@NotNull Player player, @NotNull Player player2) {
        return false;
    }

    @Override
    public void method_4083(@NotNull ItemStack itemStack, @NotNull Player player, @NotNull Player player2) {
        player2.setHealth(player2.getMaxHealth());
        player.playSound((SoundEvent)BFSounds.ITEM_SYRINGE_USE.get(), 1.0f, 1.0f);
        BFUtils.method_2964((LivingEntity)player2, (ParticleOptions)BFParticleTypes.HEAL.get(), player2.getRandom(), 0.0f, 0.0f, 0.0f, 5);
        if (player2 instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)player2;
            int n = ChatFormatting.GREEN.getColor() + -16777216;
            PacketUtils.sendToPlayer(new BFCinematicEffectFlashPcket(n, 1.0f, 0.015f, 0.2f), serverPlayer);
        }
    }

    @Override
    @NotNull
    Component method_4081(@NotNull Player player) {
        return field_3956;
    }

    @Override
    @NotNull
    Component method_4080(@NotNull Component component) {
        return Component.translatable((String)"bf.message.gamemode.medicalsyringe.heal", (Object[])new Object[]{component}).withStyle(ChatFormatting.GRAY);
    }
}

