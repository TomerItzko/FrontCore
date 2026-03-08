/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.fds.tag.FDSTagCompound
 *  net.minecraft.core.Holder
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.codec.StreamCodec
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload
 *  net.minecraft.network.protocol.common.custom.CustomPacketPayload$Type
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.neoforged.neoforge.network.handling.IPayloadContext
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.entity.BotEntity;
import com.boehmod.blockfront.common.match.BFCountry;
import com.boehmod.blockfront.common.match.MatchCallout;
import com.boehmod.blockfront.common.match.MatchClass;
import com.boehmod.blockfront.common.net.packet.BFEntitySoundPacket;
import com.boehmod.blockfront.common.player.BFAbstractPlayerData;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.tag.IAllowsCallouts;
import com.boehmod.blockfront.game.tag.IAllowsWarCry;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.PacketUtils;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.core.Holder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public record BFGameCalloutPacket(@NotNull MatchCallout callout) implements CustomPacketPayload
{
    @NotNull
    public static final CustomPacketPayload.Type<BFGameCalloutPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_game_callout"));
    @NotNull
    public static final StreamCodec<FriendlyByteBuf, BFGameCalloutPacket> CODEC = CustomPacketPayload.codec(BFGameCalloutPacket::method_4324, BFGameCalloutPacket::new);
    private static final int field_4162 = 160;

    public BFGameCalloutPacket(@NotNull FriendlyByteBuf friendlyByteBuf) {
        this((MatchCallout)friendlyByteBuf.readEnum(MatchCallout.class));
    }

    public void method_4324(@NotNull FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeEnum((Enum)this.callout);
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4322(@NotNull BFGameCalloutPacket bFGameCalloutPacket, @NotNull IPayloadContext iPayloadContext) {
        Player player = iPayloadContext.player();
        if (!(player instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer serverPlayer = (ServerPlayer)player;
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is null!";
        Object obj = bFAbstractManager.getPlayerDataHandler();
        UUID uUID = serverPlayer.getUUID();
        Object d = ((PlayerDataHandler)obj).getPlayerData((Player)serverPlayer);
        if (((BFAbstractPlayerData)d).isOutOfGame()) {
            return;
        }
        AbstractGame<?, ?, ?> abstractGame = bFAbstractManager.getGameWithPlayer(uUID);
        if (abstractGame instanceof IAllowsCallouts) {
            IAllowsCallouts iAllowsCallouts = (IAllowsCallouts)((Object)abstractGame);
            ServerLevel serverLevel = serverPlayer.serverLevel();
            MutableComponent mutableComponent = Component.literal((String)"[Team] ").append((Component)Component.translatable((String)bFGameCalloutPacket.callout.getTranslation()));
            BFUtils.handleGamePlayerChat(bFAbstractManager, obj, abstractGame, serverLevel, serverPlayer, (Component)mutableComponent, uUID);
            iAllowsCallouts.method_3396(serverPlayer, uUID, bFGameCalloutPacket.callout);
            if (bFGameCalloutPacket.callout == MatchCallout.FORWARD && abstractGame instanceof IAllowsWarCry) {
                IAllowsWarCry iAllowsWarCry = (IAllowsWarCry)((Object)abstractGame);
                BFGameCalloutPacket.method_4321(abstractGame, iAllowsWarCry, serverLevel, serverPlayer, d, uUID);
            }
        }
    }

    private static void method_4321(@NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull IAllowsWarCry iAllowsWarCry, @NotNull ServerLevel serverLevel, @NotNull ServerPlayer serverPlayer, @NotNull BFAbstractPlayerData<?, ?, ?, ?> bFAbstractPlayerData, @NotNull UUID uUID) {
        if (BFUtils.isPlayerUnavailable((Player)serverPlayer, bFAbstractPlayerData)) {
            return;
        }
        FDSTagCompound fDSTagCompound = abstractGame.getPlayerStatData(uUID);
        int n = fDSTagCompound.getInteger(BFStats.WAR_CRY_COMMANDER.getKey(), 0);
        if (n > 0) {
            return;
        }
        int n2 = fDSTagCompound.getInteger(BFStats.CLASS.getKey(), -1);
        if (n2 != MatchClass.CLASS_COMMANDER.ordinal()) {
            return;
        }
        GameTeam gameTeam = ((AbstractGamePlayerManager)abstractGame.getPlayerManager()).getPlayerTeam(uUID);
        if (gameTeam == null) {
            return;
        }
        BFCountry bFCountry = gameTeam.getDivisionData(abstractGame).getCountry();
        DeferredHolder<SoundEvent, SoundEvent> deferredHolder = bFCountry.getWarCrySoundCommander();
        if (deferredHolder != null) {
            BFEntitySoundPacket bFEntitySoundPacket = new BFEntitySoundPacket((Holder<SoundEvent>)deferredHolder, SoundSource.PLAYERS, serverPlayer.getId(), 1.0f, 2.0f);
            PacketUtils.sendToGamePlayers(bFEntitySoundPacket, abstractGame);
        }
        BFUtils.setPlayerStat(abstractGame, uUID, BFStats.WAR_CRY_COMMANDER, 1200);
        float f = iAllowsWarCry.method_3443();
        for (UUID object : gameTeam.getPlayers()) {
            Object object2 = BFUtils.getPlayerByUUID(object);
            if (object2 == null || !(object2.distanceTo((Entity)serverPlayer) <= f)) continue;
            BFUtils.setPlayerStat(abstractGame, object, BFStats.WAR_CRY, 160);
        }
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        for (Object object2 : serverLevel.getEntitiesOfClass(BotEntity.class, serverPlayer.getBoundingBox().inflate((double)f))) {
            if (!gameTeam.equals(((BotEntity)object2).getTeam()) || !(object2.distanceTo((Entity)serverPlayer) <= f)) continue;
            ((BotEntity)object2).method_2029(20 + (int)(80.0f * threadLocalRandom.nextFloat()));
        }
    }
}

