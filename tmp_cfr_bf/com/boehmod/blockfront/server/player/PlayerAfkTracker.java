/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.server.player;

import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.server.player.BFServerPlayerData;
import com.boehmod.blockfront.util.BFUtils;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class PlayerAfkTracker {
    @NotNull
    private static final Component field_4363 = Component.translatable((String)"bf.message.gamemode.afk.kick").withStyle(ChatFormatting.RED);
    @NotNull
    private static final Component field_4364 = Component.translatable((String)"bf.message.gamemode.afk.reset").withStyle(ChatFormatting.RED);
    private static final int field_4365 = 1200;
    private static final int field_4366 = 6000;
    private static final int field_4367 = 5;
    private int field_4368 = 0;
    @Nullable
    private Vec3 field_4360 = null;
    private boolean field_4362 = false;
    @NotNull
    private final BFServerPlayerData playerData;

    public PlayerAfkTracker(@NotNull BFServerPlayerData palyerData) {
        this.playerData = palyerData;
    }

    public void playerMoved() {
        this.field_4362 = true;
    }

    private void method_4609(@NotNull ServerPlayer serverPlayer) {
        this.field_4360 = null;
        if (this.field_4368 == 0) {
            return;
        }
        if (this.field_4368 >= 1200) {
            BFUtils.sendNoticeMessage(serverPlayer, field_4364);
            serverPlayer.playNotifySound(SoundEvents.VAULT_DEACTIVATE, SoundSource.MASTER, 1.0f, 1.0f);
        }
        this.field_4368 = 0;
    }

    private void method_4606(@NotNull ServerPlayer serverPlayer, @NotNull AbstractGame<?, ?, ?> abstractGame) {
        String string = serverPlayer.getScoreboardName();
        MutableComponent mutableComponent = Component.literal((String)string).withColor(0xFFFFFF);
        MutableComponent mutableComponent2 = Component.translatable((String)"bf.message.gamemode.afk.kick.all", (Object[])new Object[]{mutableComponent}).withStyle(ChatFormatting.RED);
        BFUtils.sendNoticeMessage(((AbstractGamePlayerManager)abstractGame.getPlayerManager()).getPlayerUUIDs(), (Component)mutableComponent2);
        serverPlayer.connection.disconnect(field_4363);
    }

    private void method_4610(@NotNull ServerPlayer serverPlayer) {
        int n = this.field_4368 / 1200;
        MutableComponent mutableComponent = Component.literal((String)String.valueOf(5)).withColor(0xFFFFFF);
        MutableComponent mutableComponent2 = Component.literal((String)String.valueOf(n)).withColor(0xFFFFFF);
        MutableComponent mutableComponent3 = Component.translatable((String)"bf.message.gamemode.afk.time", (Object[])new Object[]{mutableComponent2, mutableComponent}).withStyle(ChatFormatting.RED);
        BFUtils.sendNoticeMessage(serverPlayer, (Component)mutableComponent3);
        serverPlayer.playNotifySound(SoundEvents.VAULT_ACTIVATE, SoundSource.MASTER, 1.0f, 1.0f);
    }

    public void update(@NotNull ServerPlayer serverPlayer, @Nullable AbstractGame<?, ?, ?> abstractGame) {
        if (abstractGame == null) {
            this.field_4368 = 0;
        } else if (!abstractGame.isPaused()) {
            boolean bl;
            Vec3 vec3 = serverPlayer.position();
            boolean bl2 = false;
            if (this.field_4360 != null) {
                double d = this.field_4360.distanceTo(vec3);
                bl2 = d > 5.0;
            }
            boolean bl3 = bl = !this.playerData.method_849() && !this.playerData.getSequenceManager().method_5455() && !this.playerData.isOutOfGame() && !serverPlayer.isCreative() && serverPlayer.getVehicle() == null;
            if (bl) {
                ++this.field_4368;
            }
            if (this.field_4368 >= 1200 && bl2 && this.field_4362) {
                this.method_4609(serverPlayer);
            } else if (this.field_4368 < 1200 && this.field_4362) {
                this.method_4609(serverPlayer);
            }
            if (this.field_4368 > 0 && this.field_4368 % 1200 == 0) {
                this.field_4360 = new Vec3(vec3.x, vec3.y, vec3.z);
                this.method_4610(serverPlayer);
            }
            if (this.field_4368 >= 6000) {
                this.method_4606(serverPlayer, abstractGame);
            }
            this.field_4362 = false;
        }
    }
}

