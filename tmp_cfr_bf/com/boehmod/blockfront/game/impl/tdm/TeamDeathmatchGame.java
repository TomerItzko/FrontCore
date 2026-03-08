/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game.impl.tdm;

import com.boehmod.bflib.cloud.common.player.achievement.CloudAchievement;
import com.boehmod.bflib.cloud.common.player.achievement.CloudAchievements;
import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.assets.AssetCommandBuilder;
import com.boehmod.blockfront.assets.AssetCommandValidators;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.match.MatchCallout;
import com.boehmod.blockfront.common.match.MatchClass;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGameStage;
import com.boehmod.blockfront.game.GameSequence;
import com.boehmod.blockfront.game.GameStageManager;
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.TeamJoinType;
import com.boehmod.blockfront.game.impl.tdm.TeamDeathmatchGameClient;
import com.boehmod.blockfront.game.impl.tdm.TeamDeathmatchIdleStage;
import com.boehmod.blockfront.game.impl.tdm.TeamDeathmatchPlayerManager;
import com.boehmod.blockfront.game.tag.IAllowsCallouts;
import com.boehmod.blockfront.game.tag.IAllowsRegeneration;
import com.boehmod.blockfront.game.tag.IAllowsRespawning;
import com.boehmod.blockfront.game.tag.IAllowsSoundboard;
import com.boehmod.blockfront.game.tag.IAnnounceFirstBlood;
import com.boehmod.blockfront.game.tag.ICanSwitchTeams;
import com.boehmod.blockfront.game.tag.IHasClasses;
import com.boehmod.blockfront.game.tag.IHasKillStreaks;
import com.boehmod.blockfront.game.tag.IUseKillIcons;
import com.boehmod.blockfront.util.BFLog;
import com.boehmod.blockfront.util.CommandUtils;
import com.boehmod.blockfront.util.math.FDSPose;
import java.util.EnumSet;
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

public final class TeamDeathmatchGame
extends AbstractGame<TeamDeathmatchGame, TeamDeathmatchPlayerManager, GameStageManager<TeamDeathmatchGame, TeamDeathmatchPlayerManager>>
implements IAllowsCallouts<TeamDeathmatchGame>,
IHasClasses,
IAnnounceFirstBlood,
IAllowsRegeneration,
IUseKillIcons,
IHasKillStreaks,
IAllowsSoundboard,
IAllowsRespawning,
ICanSwitchTeams {
    public static final int field_3452 = 50;
    private final AssetCommandBuilder command = new AssetCommandBuilder().subCommand("spawn", new AssetCommandBuilder().subCommand("add", new AssetCommandBuilder((commandContext, stringArray) -> {
        String string = stringArray[0];
        Player player = (Player)((CommandSourceStack)commandContext.getSource()).source;
        FDSPose fDSPose = new FDSPose(player);
        GameTeam gameTeam = ((TeamDeathmatchPlayerManager)this.getPlayerManager()).getTeamByName(string);
        if (gameTeam == null) {
            CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Team " + string + " was not found!")));
            return;
        }
        gameTeam.addPlayerSpawn(fDSPose);
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)(gameTeam.getName() + " team spawn added. (" + gameTeam.getPlayerSpawns().size() + ")")));
    }).validator(AssetCommandValidators.count(new String[]{"team"})).validator(AssetCommandValidators.ONLY_PLAYERS)).subCommand("clear", new AssetCommandBuilder((commandContext, stringArray) -> {
        String string = stringArray[0];
        GameTeam gameTeam = ((TeamDeathmatchPlayerManager)this.getPlayerManager()).getTeamByName(string);
        if (gameTeam == null) {
            CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Team " + string + " was not found!")));
            return;
        }
        gameTeam.clearPlayerSpawns();
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)(gameTeam.getName() + " team's spawns cleared.")));
    }).validator(AssetCommandValidators.count(new String[]{"team"}))));
    @Nullable
    private GameSequence field_3449 = null;

    public TeamDeathmatchGame(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager) {
        super(bFAbstractManager, "tdm", "Team Deathmatch");
    }

    @Nullable
    public GameSequence method_3485() {
        return this.field_3449;
    }

    public void method_3484(@Nullable GameSequence gameSequence) {
        this.field_3449 = gameSequence;
    }

    @NotNull
    public TeamDeathmatchGameClient createGameClient(@NotNull BFClientManager bFClientManager) {
        return new TeamDeathmatchGameClient(bFClientManager, this, (ClientPlayerDataHandler)this.dataHandler);
    }

    @Override
    @NotNull
    protected TeamDeathmatchPlayerManager createPlayerManager() {
        return new TeamDeathmatchPlayerManager(this, this.dataHandler);
    }

    @Override
    @NotNull
    public AbstractGameStage<TeamDeathmatchGame, TeamDeathmatchPlayerManager> createFirstStage() {
        return new TeamDeathmatchIdleStage();
    }

    @Override
    public void updateStageManager(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull PlayerDataHandler<?> dataHandler, @NotNull ServerLevel level, @NotNull Set<UUID> players) {
        this.stageManager.update(manager, dataHandler, (TeamDeathmatchPlayerManager)this.playerManager, level, players);
    }

    @Override
    protected boolean isMatchSuccess() {
        return true;
    }

    @Override
    public boolean playerJoinTeam(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull TeamJoinType joinType, @NotNull ServerLevel level, @NotNull ServerPlayer player) {
        GameTeam gameTeam = ((TeamDeathmatchPlayerManager)this.playerManager).getNextJoiningTeam();
        if (gameTeam == null) {
            BFLog.log("No team available for player " + player.getScoreboardName() + " to join.", new Object[0]);
            return false;
        }
        return ((TeamDeathmatchPlayerManager)this.playerManager).playerJoinTeam(manager, joinType, level, player, gameTeam);
    }

    @Override
    public void specificReset(@Nullable Level level) {
    }

    @Override
    public void writeSpecificFDS(@NotNull FDSTagCompound root) {
        root.setBoolean("hasOutroSequence", this.field_3449 != null);
        if (this.field_3449 != null) {
            FDSTagCompound fDSTagCompound = new FDSTagCompound("outroSequence");
            this.field_3449.writeToFDS(fDSTagCompound);
            root.setTagCompound("outroSequence", fDSTagCompound);
        }
    }

    @Override
    public void readSpecificFDS(@NotNull FDSTagCompound root) {
        FDSTagCompound fDSTagCompound;
        boolean bl = root.getBoolean("hasOutroSequence", false);
        if (bl && (fDSTagCompound = root.getTagCompound("outroSequence")) != null) {
            this.field_3449 = new GameSequence();
            this.field_3449.readFromFDS(fDSTagCompound);
        }
    }

    @Override
    @NotNull
    public AssetCommandBuilder getCommand() {
        return this.baseCommand.inherit(this.command);
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
        return CloudAchievements.ACH_MATCH_WIN_TDM;
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
    public Component getSwitchTeamMessage(@NotNull ServerPlayer player) {
        GameTeam gameTeam = ((TeamDeathmatchPlayerManager)this.playerManager).getTeamByName("Axis");
        assert (gameTeam != null);
        GameTeam gameTeam2 = ((TeamDeathmatchPlayerManager)this.playerManager).getTeamByName("Allies");
        assert (gameTeam2 != null);
        MutableComponent mutableComponent = null;
        if ((double)gameTeam.getObjectInt(BFStats.SCORE) > 37.5) {
            mutableComponent = Component.translatable((String)"bf.message.gamemode.switchteams.error.scores");
        }
        if ((double)gameTeam2.getObjectInt(BFStats.SCORE) > 37.5) {
            mutableComponent = Component.translatable((String)"bf.message.gamemode.switchteams.error.scores");
        }
        if (mutableComponent != null) {
            return mutableComponent;
        }
        return this.getSwitchTeamMessage((Player)player, gameTeam.numPlayers(), gameTeam2.numPlayers());
    }

    @Override
    public void playerSwitchTeam(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull ServerLevel level, @NotNull ServerPlayer player, @NotNull UUID uuid) {
        this.playerSwitchTeamInternal(manager, level, player, uuid);
    }

    @Override
    public int getTeamSwitchCooldown() {
        return 2400;
    }

    @Override
    @NotNull
    public Set<MatchClass> getBannedClasses() {
        return EnumSet.of(MatchClass.CLASS_COMMANDER);
    }

    @Override
    public boolean method_3401() {
        return true;
    }

    @Override
    public boolean method_3402() {
        return true;
    }

    @Override
    public boolean method_3403() {
        return true;
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
    public void method_3396(@NotNull ServerPlayer serverPlayer, @NotNull UUID uUID, @NotNull MatchCallout matchCallout) {
        this.method_3395(this, uUID, matchCallout);
    }

    @Override
    public boolean shouldAnnounceFirstBlood(@NotNull ServerPlayer player) {
        return this.status == GameStatus.GAME;
    }
}

