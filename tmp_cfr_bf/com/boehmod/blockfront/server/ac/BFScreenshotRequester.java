/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.server.ac;

import com.boehmod.blockfront.common.net.packet.BFScreenshotDisplayPacket;
import com.boehmod.blockfront.common.net.packet.BFScreenshotRequestPacket;
import com.boehmod.blockfront.util.BFLog;
import com.boehmod.blockfront.util.PacketUtils;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class BFScreenshotRequester {
    private static final int field_4352 = 400;
    @NotNull
    private final ServerPlayer field_4349;
    @NotNull
    private BF_1074 field_4348 = BF_1074.IDLE;
    private long field_4351;
    @Nullable
    private ServerPlayer field_4350;

    public BFScreenshotRequester(@NotNull ServerPlayer serverPlayer) {
        this.field_4349 = serverPlayer;
    }

    @NotNull
    public BF_1074 method_4591() {
        return this.field_4348;
    }

    public void doRequest(@NotNull ServerPlayer player) {
        MutableComponent mutableComponent = Component.literal((String)this.field_4349.getScoreboardName()).withStyle(ChatFormatting.GRAY);
        if (this.field_4348 != BF_1074.IDLE) {
            MutableComponent mutableComponent2 = Component.translatable((String)"bf.message.screenshot.request.error.already", (Object[])new Object[]{mutableComponent});
            player.sendSystemMessage((Component)mutableComponent2);
            return;
        }
        this.field_4348 = BF_1074.REQUESTED;
        this.field_4351 = 400L;
        this.field_4350 = player;
        PacketUtils.sendToPlayer(new BFScreenshotRequestPacket(), this.field_4349);
        MutableComponent mutableComponent3 = Component.translatable((String)"bf.message.screenshot.request.sent", (Object[])new Object[]{mutableComponent});
        player.sendSystemMessage((Component)mutableComponent3);
        BFLog.log("[Screenshot] Requested screenshot from player '%s' for requesting player '%s'.", this.field_4349.getScoreboardName(), player.getScoreboardName());
    }

    public void method_4592(byte @NotNull [] byArray) {
        String string = this.field_4349.getScoreboardName();
        BFLog.log("[Screenshot] Received %d bytes of screenshot data from player '%s'.", byArray.length, string);
        if (this.field_4348 != BF_1074.REQUESTED) {
            BFLog.log("[Screenshot] Error! Received a screenshot from player '%s' when not expected. Stage: %s.", string, this.field_4348.toString());
            return;
        }
        this.field_4348 = BF_1074.IDLE;
        if (this.field_4350 != null) {
            BFLog.log("[Screenshot] Sending complete screenshot data from player '%s' to requesting player '%s'.", string, this.field_4350.getScoreboardName());
            PacketUtils.sendToPlayer(new BFScreenshotDisplayPacket(byArray), this.field_4350);
        }
    }

    public void onUpdate() {
        if (this.field_4348 == BF_1074.IDLE) {
            return;
        }
        if (this.field_4351-- < 0L) {
            this.field_4348 = BF_1074.IDLE;
            if (this.field_4350 != null) {
                MutableComponent mutableComponent = Component.literal((String)this.field_4349.getScoreboardName()).withStyle(ChatFormatting.GRAY);
                MutableComponent mutableComponent2 = Component.translatable((String)"bf.message.screenshot.request.error.timeout", (Object[])new Object[]{mutableComponent});
                this.field_4350.sendSystemMessage((Component)mutableComponent2);
            }
        }
    }

    public static enum BF_1074 {
        IDLE,
        REQUESTED;

    }
}

