/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.net.packet;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.item.RadioItem;
import com.boehmod.blockfront.common.match.radio.AirstrikeRadioCommand;
import com.boehmod.blockfront.common.match.radio.PrecisionAirstrikeRadioCommand;
import com.boehmod.blockfront.common.match.radio.ReinforcementsRadioCommand;
import com.boehmod.blockfront.common.net.packet.BFRadioPointPacket;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameEventType;
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.event.GameEvent;
import com.boehmod.blockfront.game.event.ReinforcementsMgRadioCommand;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.PacketUtils;
import com.boehmod.blockfront.util.math.FDSPose;
import com.boehmod.blockfront.util.math.MathUtils;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record BFRadioCommandPacket(@NotNull GameEventType gameEventType, @NotNull BlockPos blockPos) implements CustomPacketPayload
{
    @NotNull
    public static final CustomPacketPayload.Type<BFRadioCommandPacket> TYPE = new CustomPacketPayload.Type(BFRes.loc("packet_radio_command"));
    @NotNull
    public static final StreamCodec<FriendlyByteBuf, BFRadioCommandPacket> CODEC = CustomPacketPayload.codec(BFRadioCommandPacket::method_4410, BFRadioCommandPacket::new);
    @NotNull
    private static final Component field_4257 = Component.translatable((String)"bf.message.gamemode.radio.command.fail.spawnclose");
    private static final float field_4253 = 32.0f;
    private static final int field_7043 = 600;

    public BFRadioCommandPacket(@NotNull FriendlyByteBuf friendlyByteBuf) {
        this((GameEventType)friendlyByteBuf.readEnum(GameEventType.class), friendlyByteBuf.readBlockPos());
    }

    public void method_4410(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeEnum((Enum)this.gameEventType);
        friendlyByteBuf.writeBlockPos(this.blockPos);
    }

    @NotNull
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void method_4408(BFRadioCommandPacket bFRadioCommandPacket, @NotNull IPayloadContext iPayloadContext) {
        Player player = iPayloadContext.player();
        if (!(player instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer serverPlayer = (ServerPlayer)player;
        UUID uUID = serverPlayer.getUUID();
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is null!";
        AbstractGame<?, ?, ?> abstractGame = bFAbstractManager.getGameWithPlayer(uUID);
        if (abstractGame == null || !abstractGame.getStatus().equals((Object)GameStatus.GAME)) {
            return;
        }
        Object obj = abstractGame.getPlayerManager();
        GameTeam gameTeam = ((AbstractGamePlayerManager)obj).getPlayerTeam(uUID);
        if (gameTeam == null) {
            return;
        }
        int n = gameTeam.getObjectInt(BFStats.RADIO_DELAY, 0);
        GameEventType gameEventType = bFRadioCommandPacket.gameEventType;
        BlockPos blockPos = bFRadioCommandPacket.blockPos;
        if (n > 0 && gameEventType.hasTimeLimit()) {
            BFUtils.playSound(serverPlayer, (SoundEvent)BFSounds.MISC_MORSECODE.get(), SoundSource.MASTER);
            MutableComponent mutableComponent = Component.literal((String)BFRendering.formatTime(n)).withColor(0xFFFFFF);
            BFUtils.sendFancyMessage(serverPlayer, BFUtils.COMMAND_PREFIX, (Component)Component.translatable((String)"bf.message.gamemode.radio.command.fail.time", (Object[])new Object[]{mutableComponent}));
            return;
        }
        ItemStack itemStack = serverPlayer.getMainHandItem();
        if (!itemStack.isEmpty() && itemStack.getItem() instanceof RadioItem) {
            if (gameEventType.hasSpawnProtection() && BFRadioCommandPacket.method_4409(blockPos, serverPlayer, obj)) {
                return;
            }
            if (gameEventType.hasTimeLimit()) {
                gameTeam.setObject(BFStats.RADIO_DELAY, 2400);
            }
            GameEvent gameEvent = null;
            BFUtils.playSound(serverPlayer, (SoundEvent)BFSounds.MISC_MORSECODE.get(), SoundSource.MASTER);
            switch (gameEventType) {
                case AIR_STRIKE: {
                    gameEvent = new AirstrikeRadioCommand(serverPlayer, blockPos.getCenter(), 20, 200);
                    break;
                }
                case PRECISION_AIR_STRIKE: {
                    gameEvent = new PrecisionAirstrikeRadioCommand(serverPlayer, blockPos.getCenter(), 20, 20);
                    break;
                }
                case REINFORCEMENTS: {
                    gameEvent = new ReinforcementsRadioCommand(serverPlayer, 5);
                    break;
                }
                case REINFORCEMENTS_MG: {
                    gameEvent = new ReinforcementsMgRadioCommand(serverPlayer, 5);
                }
            }
            MutableComponent mutableComponent = Component.literal((String)gameEventType.getIconName()).withColor(0xFFFFFF);
            MutableComponent mutableComponent2 = Component.translatable((String)"bf.message.gamemode.radio.command.toteam", (Object[])new Object[]{mutableComponent}).withStyle(ChatFormatting.GRAY);
            BFUtils.sendFancyMessage(gameTeam, BFUtils.OBJECTIVE_PREFIX, (Component)mutableComponent2);
            for (UUID uUID2 : gameTeam.getPlayers()) {
                ServerPlayer serverPlayer2 = BFUtils.getPlayerByUUID(uUID2);
                if (serverPlayer2 == null) continue;
                BFUtils.playSound(serverPlayer2, (SoundEvent)BFSounds.MISC_MORSECODE.get(), SoundSource.MASTER, 1.25f);
                BFRadioPointPacket bFRadioPointPacket = new BFRadioPointPacket(gameEventType.getIconName(), 600, new Vec3((double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ()), gameEventType);
                PacketUtils.sendToPlayer(bFRadioPointPacket, serverPlayer2);
            }
            if (gameEvent != null) {
                abstractGame.addGameEvent(gameEvent);
                Component component = gameEvent.getMessage();
                if (component != null) {
                    BFUtils.sendFancyMessage(serverPlayer, BFUtils.COMMAND_PREFIX, gameEvent.getMessage());
                }
            }
        }
    }

    private static boolean method_4409(@NotNull BlockPos blockPos, @NotNull ServerPlayer serverPlayer, @NotNull AbstractGamePlayerManager<?> abstractGamePlayerManager) {
        for (GameTeam gameTeam : abstractGamePlayerManager.getTeams()) {
            for (FDSPose fDSPose : gameTeam.getPlayerSpawns()) {
                if (!MathUtils.withinDistance(blockPos, fDSPose.position, 32.0)) continue;
                BFUtils.sendFancyMessage(serverPlayer, BFUtils.COMMAND_PREFIX, field_4257);
                return true;
            }
        }
        return false;
    }
}

