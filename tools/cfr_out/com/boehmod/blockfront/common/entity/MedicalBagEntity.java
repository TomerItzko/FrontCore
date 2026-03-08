/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.ints.Int2IntMap
 *  it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.entity;

import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.entity.PlayerDeployableEntity;
import com.boehmod.blockfront.common.net.BFPopup;
import com.boehmod.blockfront.common.net.packet.BFCinematicEffectFlashPcket;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.PacketUtils;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public final class MedicalBagEntity
extends PlayerDeployableEntity {
    public static final Component PLAYER_HEALED_POPUP_MESSAGE = Component.translatable((String)"bf.popup.message.medic.bag.player.healed").withStyle(ChatFormatting.GREEN);
    public static final Component MEDIC_BAG_HEAL_MESSAGE = Component.translatable((String)"bf.message.gamemode.medic.bag.heal");
    public static final float field_2569 = 5.0f;
    @NotNull
    private final Int2IntMap field_2568 = new Int2IntOpenHashMap();

    public MedicalBagEntity(@NotNull EntityType<? extends MedicalBagEntity> entityType, @NotNull Level level) {
        super((EntityType<? extends PlayerDeployableEntity>)entityType, level);
    }

    @Override
    void method_2515(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull ServerPlayer serverPlayer, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull AbstractGamePlayerManager<?> abstractGamePlayerManager) {
        ServerPlayer serverPlayer2;
        if (this.owner == null) {
            return;
        }
        UUID uUID = serverPlayer.getUUID();
        UUID uUID2 = this.owner.getUUID();
        if (!(serverPlayer.getHealth() < serverPlayer.getMaxHealth())) {
            return;
        }
        BFUtils.playPositionedSound(serverPlayer, (SoundEvent)BFSounds.ITEM_MEDICALBAG_HEAL.get(), SoundSource.NEUTRAL, 1.0f, this.position());
        if (BFUtils.method_2911(abstractGamePlayerManager, uUID, uUID2)) {
            Player player;
            if (!uUID.equals(uUID2) && this.method_2263(serverPlayer.getId())) {
                BFUtils.triggerPlayerStat(bFAbstractManager, abstractGame, uUID2, BFStats.SCORE, 1);
            }
            if ((player = this.owner) instanceof ServerPlayer) {
                serverPlayer2 = (ServerPlayer)player;
                BFUtils.sendPopupMessage(serverPlayer2, new BFPopup(PLAYER_HEALED_POPUP_MESSAGE, 40));
            }
            this.method_2264(serverPlayer.getId());
        }
        serverPlayer.heal(5.0f);
        serverPlayer.sendSystemMessage(MEDIC_BAG_HEAL_MESSAGE);
        if (serverPlayer instanceof ServerPlayer) {
            serverPlayer2 = serverPlayer;
            int n = ChatFormatting.GREEN.getColor() + -16777216;
            PacketUtils.sendToPlayer(new BFCinematicEffectFlashPcket(n, 1.0f, 0.015f, 0.2f), serverPlayer2);
        }
    }

    public void method_2264(int n) {
        int n2 = this.field_2568.containsKey(n) ? this.field_2568.get(n) : 0;
        this.field_2568.put(n, n2 + 1);
    }

    public boolean method_2263(int n) {
        return !this.field_2568.containsKey(n) || this.field_2568.get(n) < 3;
    }
}

