/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game.impl.boot;

import com.boehmod.bflib.cloud.common.player.achievement.CloudAchievement;
import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.cloud.packet.common.server.PacketPlayerDataIncreaseValue;
import com.boehmod.blockfront.cloud.common.AbstractConnectionManager;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.AbstractGameStage;
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.game.TeamJoinType;
import com.boehmod.blockfront.game.impl.boot.BootcampGame;
import com.boehmod.blockfront.game.impl.boot.BootcampIdleStage;
import com.boehmod.blockfront.game.impl.boot.BootcampPlayerManager;
import com.boehmod.blockfront.game.impl.boot.BootcampSoundPoint;
import com.boehmod.blockfront.game.impl.boot.BootcampWeaponPoint;
import com.boehmod.blockfront.util.BFUtils;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class BootcampGameStage
extends AbstractGameStage<BootcampGame, BootcampPlayerManager> {
    public static final Component field_3355 = Component.translatable((String)"bf.message.disconnect.bootcamp.complete");

    @Override
    public void stageStarted(@NotNull BootcampGame bootcampGame, @NotNull BootcampPlayerManager bootcampPlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        bootcampGame.spawnNpcs(serverLevel);
    }

    @Override
    public void onSecondPassed(@NotNull BootcampGame bootcampGame, @NotNull BootcampPlayerManager bootcampPlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        for (BootcampSoundPoint object2 : bootcampGame.getSoundPoints()) {
            object2.update(bootcampGame, serverLevel, set);
        }
        for (BootcampWeaponPoint bootcampWeaponPoint : bootcampGame.getWeaponPoints()) {
            bootcampWeaponPoint.update(serverLevel, bootcampGame, set);
        }
        List<ServerPlayer> list = bootcampGame.getFinishPoint().playersInSurroundingArea(serverLevel, 5, set);
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            ServerPlayer serverPlayer = (ServerPlayer)iterator.next();
            if (!(serverPlayer instanceof ServerPlayer)) continue;
            ServerPlayer serverPlayer2 = serverPlayer;
            this.onPlayerFinish(bFAbstractManager, playerDataHandler, bootcampGame, serverPlayer2, set);
        }
    }

    @Override
    public boolean isStageOver(@NotNull BootcampGame bootcampGame, @NotNull BootcampPlayerManager bootcampPlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        return set.isEmpty();
    }

    @Override
    public void stageEnded(@NotNull BootcampGame bootcampGame, @NotNull BootcampPlayerManager bootcampPlayerManager, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
    }

    @Override
    public void method_3911(@NotNull BootcampGame bootcampGame, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
    }

    @Override
    @NotNull
    public AbstractGameStage<BootcampGame, BootcampPlayerManager> createNextStage(@NotNull BootcampGame bootcampGame) {
        return new BootcampIdleStage();
    }

    @Override
    @NotNull
    public GameStatus getStatus() {
        return GameStatus.GAME;
    }

    @Override
    public void onPlayerJoin(@NotNull PlayerDataHandler<?> playerDataHandler, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull TeamJoinType teamJoinType, @NotNull ServerLevel serverLevel, @NotNull ServerPlayer serverPlayer, @NotNull BootcampGame bootcampGame, @NotNull UUID uUID) {
    }

    private void onPlayerFinish(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull BootcampGame bootcampGame, @NotNull ServerPlayer serverPlayer, @NotNull Set<UUID> set) {
        UUID uUID = serverPlayer.getUUID();
        PlayerCloudData playerCloudData = playerDataHandler.getCloudProfile((Player)serverPlayer);
        CloudAchievement cloudAchievement = bootcampGame.getVictoryAchievement();
        Object obj = bFAbstractManager.getConnectionManager();
        BFUtils.awardAchievement(bFAbstractManager, uUID, cloudAchievement);
        if (!playerCloudData.hasCompletedBootcamp()) {
            ((AbstractConnectionManager)obj).sendPacket((IPacket)new PacketPlayerDataIncreaseValue(uUID, "bootcamp", 1));
        }
        BFUtils.disconnectServerPlayer(serverPlayer, field_3355);
        MutableComponent mutableComponent = Component.literal((String)serverPlayer.getScoreboardName()).withColor(0xFFFFFF);
        BFUtils.sendNoticeMessage(set, (Component)Component.translatable((String)"bf.message.ingame.bootcamp.complete", (Object[])new Object[]{mutableComponent}));
    }

    @Override
    public /* synthetic */ void method_3911(@NotNull AbstractGame abstractGame, @NotNull ServerLevel serverLevel, @NotNull Set set) {
        this.method_3911((BootcampGame)abstractGame, serverLevel, (Set<UUID>)set);
    }

    @Override
    public /* synthetic */ void stageEnded(@NotNull AbstractGame game, @NotNull AbstractGamePlayerManager playerManager, @NotNull BFAbstractManager modManager, @NotNull PlayerDataHandler dataHandler, @NotNull ServerLevel level, @NotNull Set players) {
        this.stageEnded((BootcampGame)game, (BootcampPlayerManager)playerManager, modManager, dataHandler, level, (Set<UUID>)players);
    }

    @Override
    public /* synthetic */ void stageStarted(@NotNull AbstractGame game, @NotNull AbstractGamePlayerManager playerManager, @NotNull BFAbstractManager modManager, @NotNull PlayerDataHandler dataHandler, @NotNull ServerLevel level, @NotNull Set players) {
        this.stageStarted((BootcampGame)game, (BootcampPlayerManager)playerManager, modManager, dataHandler, level, (Set<UUID>)players);
    }
}

