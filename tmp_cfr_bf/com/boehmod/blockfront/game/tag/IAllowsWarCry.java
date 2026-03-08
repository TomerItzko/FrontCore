/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game.tag;

import com.boehmod.blockfront.common.match.BFCountry;
import com.boehmod.blockfront.common.match.DivisionData;
import com.boehmod.blockfront.common.net.packet.BFEntitySoundPacket;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.PacketUtils;
import java.util.Iterator;
import java.util.UUID;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public interface IAllowsWarCry {
    public static boolean method_3439(@NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerPlayer serverPlayer) {
        Object obj = playerDataHandler.getPlayerData((Player)serverPlayer);
        return !BFUtils.isPlayerUnavailable((Player)serverPlayer, obj);
    }

    public static void method_3440(@NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull DivisionData divisionData, @NotNull LivingEntity livingEntity, @NotNull UUID uUID) {
        Object object;
        BFCountry bFCountry = divisionData.getCountry();
        DeferredHolder<SoundEvent, SoundEvent> deferredHolder = bFCountry.getWarCrySound();
        if (livingEntity instanceof ServerPlayer) {
            object = (ServerPlayer)livingEntity;
            if (deferredHolder != null) {
                BFUtils.playSound((ServerPlayer)object, (SoundEvent)deferredHolder.get(), SoundSource.PLAYERS, 2.0f);
                BFUtils.setPlayerStat(abstractGame, uUID, BFStats.WAR_CRY, 0);
                object.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 120, 1, false, false));
            }
        }
        object = ((AbstractGamePlayerManager)abstractGame.getPlayerManager()).getPlayerUUIDs();
        object.remove(uUID);
        float f = (float)((double)0.9f + (double)0.2f * Math.random());
        int n = livingEntity.getId();
        BFEntitySoundPacket bFEntitySoundPacket = null;
        if (deferredHolder != null) {
            bFEntitySoundPacket = new BFEntitySoundPacket((Holder<SoundEvent>)deferredHolder, SoundSource.PLAYERS, n, f, 2.0f);
        }
        DeferredHolder<SoundEvent, SoundEvent> deferredHolder2 = bFCountry.getWarCrySoundDistant();
        BFEntitySoundPacket bFEntitySoundPacket2 = null;
        if (deferredHolder2 != null) {
            bFEntitySoundPacket2 = new BFEntitySoundPacket((Holder<SoundEvent>)deferredHolder2, SoundSource.PLAYERS, n, f, 5.0f);
        }
        DeferredHolder<SoundEvent, SoundEvent> deferredHolder3 = bFCountry.getWarCryCrowdSound();
        if (Math.random() < 0.5 && deferredHolder3 != null) {
            float f2 = (float)((double)0.9f + (double)0.2f * Math.random());
            int n2 = 32;
            int n3 = (int)(Math.random() * 32.0) - 16;
            int n4 = (int)(Math.random() * 32.0) - 16;
            if (Math.random() < 0.5) {
                n3 = -n3;
            }
            if (Math.random() < 0.5) {
                n4 = -n4;
            }
            Vec3 vec3 = livingEntity.position().add((double)n3, 0.0, (double)n4);
            BFUtils.playSound(livingEntity.level(), vec3, (SoundEvent)deferredHolder3.get(), SoundSource.PLAYERS, 6.0f, f2);
        }
        Iterator iterator = object.iterator();
        while (iterator.hasNext()) {
            UUID uUID2 = (UUID)iterator.next();
            ServerPlayer serverPlayer = BFUtils.getPlayerByUUID(uUID2);
            if (serverPlayer == null) continue;
            if (bFEntitySoundPacket != null) {
                PacketUtils.sendToPlayer(bFEntitySoundPacket, serverPlayer);
            }
            if (bFEntitySoundPacket2 == null) continue;
            PacketUtils.sendToPlayer(bFEntitySoundPacket2, serverPlayer);
        }
    }

    public boolean method_3442(@NotNull Level var1, @NotNull ServerPlayer var2, @NotNull UUID var3);

    public void method_3441(@NotNull Level var1, @NotNull ServerPlayer var2, @NotNull UUID var3);

    public float method_3443();
}

