/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.player.achievement.CloudAchievement
 *  com.boehmod.bflib.cloud.common.player.achievement.CloudAchievements
 *  com.boehmod.bflib.fds.tag.FDSTagCompound
 *  javax.annotation.Nullable
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game.impl.ffa;

import com.boehmod.bflib.cloud.common.player.achievement.CloudAchievement;
import com.boehmod.bflib.cloud.common.player.achievement.CloudAchievements;
import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.assets.AssetCommandBuilder;
import com.boehmod.blockfront.assets.AssetCommandValidators;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.match.MatchClass;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGameStage;
import com.boehmod.blockfront.game.GameSequence;
import com.boehmod.blockfront.game.GameStageManager;
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.TeamJoinType;
import com.boehmod.blockfront.game.impl.ffa.FreeForAllGameClient;
import com.boehmod.blockfront.game.impl.ffa.FreeForAllIdleStage;
import com.boehmod.blockfront.game.impl.ffa.FreeForAllPlayerManager;
import com.boehmod.blockfront.game.tag.IAllowsRegeneration;
import com.boehmod.blockfront.game.tag.IAllowsRespawning;
import com.boehmod.blockfront.game.tag.IAllowsSoundboard;
import com.boehmod.blockfront.game.tag.IAnnounceFirstBlood;
import com.boehmod.blockfront.game.tag.IHasClasses;
import com.boehmod.blockfront.game.tag.IHasKillStreaks;
import com.boehmod.blockfront.game.tag.IUseKillIcons;
import com.boehmod.blockfront.util.BFLog;
import com.boehmod.blockfront.util.CommandUtils;
import com.boehmod.blockfront.util.math.FDSPose;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public final class FreeForAllGame
extends AbstractGame<FreeForAllGame, FreeForAllPlayerManager, GameStageManager<FreeForAllGame, FreeForAllPlayerManager>>
implements IHasClasses,
IAnnounceFirstBlood,
IAllowsRegeneration,
IUseKillIcons,
IHasKillStreaks,
IAllowsSoundboard,
IAllowsRespawning {
    public static final int field_3490 = 30;
    @NotNull
    private final AssetCommandBuilder command = new AssetCommandBuilder().subCommand("spawn", new AssetCommandBuilder().subCommand("add", new AssetCommandBuilder((commandContext, stringArray) -> {
        Player player = (Player)((CommandSourceStack)commandContext.getSource()).source;
        ((FreeForAllPlayerManager)this.getPlayerManager()).method_3571(new FDSPose(player));
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Player spawn added. (" + ((FreeForAllPlayerManager)this.getPlayerManager()).method_3566().size() + ")")));
    }).validator(AssetCommandValidators.ONLY_PLAYERS)).subCommand("clear", new AssetCommandBuilder((commandContext, stringArray) -> {
        ((FreeForAllPlayerManager)this.getPlayerManager()).method_3570();
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)"Player spawns cleared."));
    })));
    @Nullable
    private GameSequence outroSequence = null;

    public FreeForAllGame(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager) {
        super(bFAbstractManager, "ffa", "Free For All");
    }

    @Nullable
    public GameSequence getOutroSequence() {
        return this.outroSequence;
    }

    public void setOutroSequence(@Nullable GameSequence sequence) {
        this.outroSequence = sequence;
    }

    @NotNull
    public FreeForAllGameClient createGameClient(@NotNull BFClientManager bFClientManager) {
        return new FreeForAllGameClient(bFClientManager, this, (ClientPlayerDataHandler)this.dataHandler);
    }

    @Override
    @NotNull
    protected FreeForAllPlayerManager createPlayerManager() {
        return new FreeForAllPlayerManager(this, this.dataHandler);
    }

    @Override
    @NotNull
    public AbstractGameStage<FreeForAllGame, FreeForAllPlayerManager> createFirstStage() {
        return new FreeForAllIdleStage();
    }

    @Override
    @NotNull
    public Set<AbstractGame.ErrorType> getErrorTypes() {
        return EnumSet.of(AbstractGame.ErrorType.MAP_TYPE, AbstractGame.ErrorType.LOBBY);
    }

    @Override
    public void getErrorMessages(@NotNull List<MutableComponent> messages) {
        if (((FreeForAllPlayerManager)this.playerManager).method_3566().isEmpty()) {
            messages.add(Component.literal((String)("Player spawns in game '" + this.name + "' are missing.")));
        }
        super.getErrorMessages(messages);
    }

    @Override
    public void updateStageManager(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull PlayerDataHandler<?> dataHandler, @NotNull ServerLevel level, @NotNull Set<UUID> players) {
        this.stageManager.update(manager, dataHandler, (FreeForAllPlayerManager)this.playerManager, level, players);
    }

    @Override
    protected boolean isMatchSuccess() {
        return true;
    }

    @Override
    public boolean playerJoinTeam(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull TeamJoinType joinType, @NotNull ServerLevel level, @NotNull ServerPlayer player) {
        GameTeam gameTeam = ((FreeForAllPlayerManager)this.playerManager).getNextJoiningTeam();
        if (gameTeam == null) {
            BFLog.log("No team available for player " + player.getScoreboardName() + " to join.", new Object[0]);
            return false;
        }
        return ((FreeForAllPlayerManager)this.playerManager).playerJoinTeam(manager, joinType, level, player, gameTeam);
    }

    @Override
    public void specificReset(@Nullable Level level) {
    }

    @Override
    public void writeSpecificFDS(@NotNull FDSTagCompound root) {
        List<FDSPose> list = ((FreeForAllPlayerManager)this.playerManager).method_3566();
        int n = list.size();
        root.setInteger("randomSpawnSize", n);
        for (int i = 0; i < n; ++i) {
            list.get(i).writeNamedFDS("randomSpawn" + i, root);
        }
        root.setBoolean("hasOutroSequence", this.outroSequence != null);
        if (this.outroSequence != null) {
            FDSTagCompound fDSTagCompound = new FDSTagCompound("outroSequence");
            this.outroSequence.writeToFDS(fDSTagCompound);
            root.setTagCompound("outroSequence", fDSTagCompound);
        }
    }

    @Override
    public void readSpecificFDS(@NotNull FDSTagCompound root) {
        FDSTagCompound fDSTagCompound;
        int n;
        ((FreeForAllPlayerManager)this.playerManager).method_3570();
        int n2 = root.getInteger("randomSpawnSize");
        for (n = 0; n < n2; ++n) {
            ((FreeForAllPlayerManager)this.playerManager).method_3571(FDSPose.readNamedFDS("randomSpawn" + n, root));
        }
        n = root.getBoolean("hasOutroSequence", false) ? 1 : 0;
        if (n != 0 && (fDSTagCompound = root.getTagCompound("outroSequence")) != null) {
            this.outroSequence = new GameSequence();
            this.outroSequence.readFromFDS(fDSTagCompound);
        }
    }

    @Override
    @NotNull
    public AssetCommandBuilder getCommand() {
        return super.getCommand().inherit(this.command);
    }

    @Override
    public boolean shouldRespawnAutomatically(@NotNull Player player) {
        return true;
    }

    @Override
    public int getMinimumPlayers() {
        return 2;
    }

    @Override
    public boolean shouldAnnounceRageQuits() {
        return false;
    }

    @Override
    public boolean shouldShowDeadMessages() {
        return true;
    }

    @Override
    @NotNull
    public CloudAchievement getVictoryAchievement() {
        return CloudAchievements.ACH_MATCH_WIN_FFA;
    }

    @Override
    public boolean shouldUseStamina(@NotNull Player player) {
        return true;
    }

    @Override
    public boolean playerCanRegenerate(@NotNull Player player) {
        return player.getHealth() < player.getMaxHealth();
    }

    @Override
    public float method_3418(@NotNull Player player) {
        return 1.0f;
    }

    @Override
    public int method_3419(@NotNull Player player) {
        return 40;
    }

    @Override
    public int getMaximumPlayerSounds(@NotNull ServerPlayer serverPlayer) {
        return 8;
    }

    @Override
    public int getSoundboardCooldown() {
        return 160;
    }

    @Override
    @NotNull
    public Set<MatchClass> getBannedClasses() {
        return EnumSet.of(MatchClass.CLASS_ANTI_TANK, MatchClass.CLASS_COMMANDER);
    }

    @Override
    public boolean method_3401() {
        return false;
    }

    @Override
    public boolean method_3402() {
        return true;
    }

    @Override
    public boolean method_3403() {
        return false;
    }

    @Override
    public boolean method_3404() {
        return true;
    }

    @Override
    public int method_3400(@NotNull MatchClass matchClass) {
        return switch (matchClass) {
            case MatchClass.CLASS_ASSAULT -> 3;
            case MatchClass.CLASS_SUPPORT, MatchClass.CLASS_MEDIC, MatchClass.CLASS_SNIPER -> 2;
            case MatchClass.CLASS_GUNNER, MatchClass.CLASS_SPECIALIST, MatchClass.CLASS_ANTI_TANK, MatchClass.CLASS_COMMANDER -> 1;
            default -> 0;
        };
    }

    @Override
    public int getSpawnProLength() {
        return 150;
    }

    @Override
    public boolean method_3434(@NotNull UUID uUID) {
        return true;
    }

    @Override
    public boolean method_3435(@NotNull UUID uUID) {
        return true;
    }

    @Override
    public boolean method_3430() {
        return true;
    }

    @Override
    public boolean shouldAnnounceFirstBlood(@NotNull ServerPlayer player) {
        return this.status == GameStatus.GAME;
    }
}

