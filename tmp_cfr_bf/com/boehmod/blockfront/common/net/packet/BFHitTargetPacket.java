/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.common.stat.BFStat;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.BFUtils;
import java.util.Calendar;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public final class BFHitTargetPacket
implements CustomPacketPayload {
    @NotNull
    public static final CustomPacketPayload.Type<BFHitTargetPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_hit_target"));
    @NotNull
    public static final StreamCodec<FriendlyByteBuf, BFHitTargetPacket> CODEC = CustomPacketPayload.codec(BFHitTargetPacket::method_4388, BFHitTargetPacket::new);

    public BFHitTargetPacket() {
    }

    public BFHitTargetPacket(@NotNull FriendlyByteBuf friendlyByteBuf) {
    }

    public void method_4388(@NotNull FriendlyByteBuf friendlyByteBuf) {
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4387(BFHitTargetPacket bFHitTargetPacket, @NotNull IPayloadContext iPayloadContext) {
        Player player = iPayloadContext.player();
        if (!(player instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer serverPlayer = (ServerPlayer)player;
        UUID uUID = player.getUUID();
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is null!";
        AbstractGame<?, ?, ?> abstractGame = bFAbstractManager.getGameWithPlayer(uUID);
        if (abstractGame == null) {
            return;
        }
        Object obj = bFAbstractManager.getPlayerDataHandler();
        BFStat bFStat = new BFStat("targetsHitLast");
        BFStat bFStat2 = new BFStat("targetsHit");
        BFStat bFStat3 = new BFStat("targetsHitBad");
        BFStat bFStat4 = new BFStat("targetsHitOk");
        BFStat bFStat5 = new BFStat("targetsHitGood");
        BFStat bFStat6 = new BFStat("targetsHitBest");
        BFUtils.setPlayerStat(abstractGame, uUID, bFStat2, BFUtils.getPlayerStat(abstractGame, uUID, bFStat2) + 1);
        PlayerCloudData playerCloudData = ((PlayerDataHandler)obj).getCloudProfile(player);
        long l = BFUtils.getPlayerStat(abstractGame, uUID, bFStat);
        long l2 = (Calendar.getInstance().getTime().getTime() - l) / 1000L;
        if (l2 <= 4L) {
            return;
        }
        long l3 = Calendar.getInstance().getTime().getTime();
        BFUtils.setPlayerStat(abstractGame, uUID, bFStat, (int)l3);
        int n = 0;
        int n2 = BFUtils.getPlayerStat(abstractGame, uUID, bFStat2);
        if (n2 > 3) {
            switch (n2) {
                case 6: {
                    if (BFUtils.getPlayerStat(abstractGame, uUID, bFStat3) != 0) break;
                    BFUtils.setPlayerStat(abstractGame, uUID, bFStat3, 1);
                    BFUtils.playSound(serverPlayer, (SoundEvent)BFSounds.MATCH_GAMEMODE_BOOT_INSTRUCTOR_TARGET_BAD.get(), SoundSource.NEUTRAL);
                    n = 2;
                    break;
                }
                case 12: {
                    if (BFUtils.getPlayerStat(abstractGame, uUID, bFStat4) != 0) break;
                    BFUtils.setPlayerStat(abstractGame, uUID, bFStat4, 1);
                    BFUtils.playSound(serverPlayer, (SoundEvent)BFSounds.MATCH_GAMEMODE_BOOT_INSTRUCTOR_TARGET_OK.get(), SoundSource.NEUTRAL);
                    n = 4;
                    break;
                }
                case 24: {
                    if (BFUtils.getPlayerStat(abstractGame, uUID, bFStat5) != 0) break;
                    BFUtils.setPlayerStat(abstractGame, uUID, bFStat5, 1);
                    BFUtils.playSound(serverPlayer, (SoundEvent)BFSounds.MATCH_GAMEMODE_BOOT_INSTRUCTOR_TARGET_GOOD.get(), SoundSource.NEUTRAL);
                    n = 6;
                    break;
                }
                case 32: {
                    if (BFUtils.getPlayerStat(abstractGame, uUID, bFStat6) != 0) break;
                    BFUtils.setPlayerStat(abstractGame, uUID, bFStat6, 1);
                    BFUtils.playSound(serverPlayer, (SoundEvent)BFSounds.MATCH_GAMEMODE_BOOT_INSTRUCTOR_TARGET_BEST.get(), SoundSource.NEUTRAL);
                    n = 8;
                }
            }
        }
        if (n > 0 && !playerCloudData.hasCompletedBootcamp()) {
            BFUtils.triggerPlayerStat(bFAbstractManager, abstractGame, uUID, BFStats.SCORE, n);
        }
    }
}

