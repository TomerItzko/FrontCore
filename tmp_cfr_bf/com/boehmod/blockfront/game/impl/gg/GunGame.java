/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game.impl.gg;

import com.boehmod.bflib.cloud.common.player.achievement.CloudAchievement;
import com.boehmod.bflib.cloud.common.player.achievement.CloudAchievements;
import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.assets.AssetCommandBuilder;
import com.boehmod.blockfront.assets.AssetCommandValidators;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGameStage;
import com.boehmod.blockfront.game.GameSequence;
import com.boehmod.blockfront.game.GameStageManager;
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.TeamJoinType;
import com.boehmod.blockfront.game.impl.gg.GunGameClient;
import com.boehmod.blockfront.game.impl.gg.GunGameIdleStage;
import com.boehmod.blockfront.game.impl.gg.GunGameItemStages;
import com.boehmod.blockfront.game.impl.gg.GunGamePlayerManager;
import com.boehmod.blockfront.game.tag.IAllowsRegeneration;
import com.boehmod.blockfront.game.tag.IAllowsRespawning;
import com.boehmod.blockfront.game.tag.IAllowsSoundboard;
import com.boehmod.blockfront.game.tag.IAnnounceFirstBlood;
import com.boehmod.blockfront.game.tag.ICanSwitchTeams;
import com.boehmod.blockfront.game.tag.IHasKillStreaks;
import com.boehmod.blockfront.game.tag.IUseKillIcons;
import com.boehmod.blockfront.util.BFLog;
import com.boehmod.blockfront.util.CommandUtils;
import com.boehmod.blockfront.util.RegistryUtils;
import com.boehmod.blockfront.util.math.FDSPose;
import io.netty.buffer.ByteBuf;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public final class GunGame
extends AbstractGame<GunGame, GunGamePlayerManager, GameStageManager<GunGame, GunGamePlayerManager>>
implements IAnnounceFirstBlood,
IAllowsRegeneration,
IUseKillIcons,
IHasKillStreaks,
IAllowsSoundboard,
IAllowsRespawning,
ICanSwitchTeams {
    public static final int field_3500 = 50;
    private final AssetCommandBuilder command = new AssetCommandBuilder().subCommand("spawn", new AssetCommandBuilder().subCommand("add", new AssetCommandBuilder((commandContext, stringArray) -> {
        String string = stringArray[0];
        Player player = (Player)((CommandSourceStack)commandContext.getSource()).source;
        FDSPose fDSPose = new FDSPose(player);
        GameTeam gameTeam = ((GunGamePlayerManager)this.getPlayerManager()).getTeamByName(string);
        if (gameTeam == null) {
            CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Team " + string + " was not found!")));
            return;
        }
        gameTeam.addPlayerSpawn(fDSPose);
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)(gameTeam.getName() + " team spawn added. (" + gameTeam.getPlayerSpawns().size() + ")")));
    }).validator(AssetCommandValidators.count(new String[]{"team"})).validator(AssetCommandValidators.ONLY_PLAYERS)).subCommand("clear", new AssetCommandBuilder((commandContext, stringArray) -> {
        String string = stringArray[0];
        GameTeam gameTeam = ((GunGamePlayerManager)this.getPlayerManager()).getTeamByName(string);
        if (gameTeam == null) {
            CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Team " + string + " was not found!")));
            return;
        }
        gameTeam.clearPlayerSpawns();
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)(gameTeam.getName() + " team's spawns cleared.")));
    }).validator(AssetCommandValidators.count(new String[]{"team"}))));
    @NotNull
    public final List<Supplier<? extends GunItem>> GUN_STAGES = GunGameItemStages.getRandomList();
    @Nullable
    private GameSequence field_3497 = null;

    public GunGame(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager) {
        super(bFAbstractManager, "gg", "Gun Game");
    }

    @Nullable
    public GameSequence method_3596() {
        return this.field_3497;
    }

    public void method_3594(@Nullable GameSequence gameSequence) {
        this.field_3497 = gameSequence;
    }

    @NotNull
    public GunGameClient createGameClient(@NotNull BFClientManager bFClientManager) {
        return new GunGameClient(bFClientManager, this, (ClientPlayerDataHandler)this.dataHandler);
    }

    @Override
    @NotNull
    protected GunGamePlayerManager createPlayerManager() {
        return new GunGamePlayerManager(this, this.dataHandler);
    }

    @Nullable
    public GunItem getCurrentGun(int stage) {
        if (this.GUN_STAGES.isEmpty()) {
            return null;
        }
        if (stage < 0 || stage >= this.GUN_STAGES.size()) {
            return null;
        }
        Supplier<? extends GunItem> supplier = this.GUN_STAGES.get(stage);
        if (supplier == null) {
            return null;
        }
        return supplier.get();
    }

    @Nullable
    public GunItem getNextGun(int stage) {
        if (this.GUN_STAGES.isEmpty()) {
            return null;
        }
        int n = stage + 1;
        if (n >= this.GUN_STAGES.size()) {
            return null;
        }
        return this.GUN_STAGES.get(n).get();
    }

    @Nullable
    public GunItem getPrevGun(int stage) {
        if (this.GUN_STAGES.isEmpty()) {
            return null;
        }
        int n = stage - 1;
        if (n < 0) {
            return null;
        }
        return this.GUN_STAGES.get(n).get();
    }

    @Override
    public void writeAll(@NotNull ByteBuf buf, boolean writeMap) throws IOException {
        super.writeAll(buf, writeMap);
        IPacket.writeBoolean((ByteBuf)buf, (boolean)writeMap);
        if (writeMap) {
            int n = this.GUN_STAGES.size();
            buf.writeInt(n);
            for (Supplier<? extends GunItem> supplier : this.GUN_STAGES) {
                IPacket.writeString((ByteBuf)buf, (String)RegistryUtils.getItemId(supplier.get()));
            }
        }
    }

    @Override
    public void readAll(@NotNull ByteBuf buf) throws IOException {
        super.readAll(buf);
        if (IPacket.readBoolean((ByteBuf)buf)) {
            this.GUN_STAGES.clear();
            int n = buf.readInt();
            for (int i = 0; i < n; ++i) {
                Supplier<Item> supplier = RegistryUtils.retrieveItem(IPacket.readString((ByteBuf)buf));
                Item item = supplier.get();
                if (!(item instanceof GunItem)) continue;
                GunItem gunItem = (GunItem)item;
                this.GUN_STAGES.add(() -> gunItem);
            }
        }
    }

    @Override
    @NotNull
    public AssetCommandBuilder getCommand() {
        return super.getCommand().inherit(this.command);
    }

    @Override
    @NotNull
    public AbstractGameStage<GunGame, GunGamePlayerManager> createFirstStage() {
        return new GunGameIdleStage();
    }

    @Override
    public void updateStageManager(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull PlayerDataHandler<?> dataHandler, @NotNull ServerLevel level, @NotNull Set<UUID> players) {
        this.stageManager.update(manager, dataHandler, (GunGamePlayerManager)this.playerManager, level, players);
    }

    @Override
    protected boolean isMatchSuccess() {
        return true;
    }

    @Override
    public boolean playerJoinTeam(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull TeamJoinType joinType, @NotNull ServerLevel level, @NotNull ServerPlayer player) {
        GameTeam gameTeam = ((GunGamePlayerManager)this.playerManager).getNextJoiningTeam();
        if (gameTeam == null) {
            BFLog.log("No team available for player " + player.getScoreboardName() + " to join.", new Object[0]);
            return false;
        }
        return ((GunGamePlayerManager)this.playerManager).playerJoinTeam(manager, joinType, level, player, gameTeam);
    }

    @Override
    public void specificReset(@Nullable Level level) {
        this.GUN_STAGES.clear();
        this.GUN_STAGES.addAll(GunGameItemStages.getRandomList());
    }

    @Override
    public void writeSpecificFDS(@NotNull FDSTagCompound root) {
        root.setBoolean("hasOutroSequence", this.field_3497 != null);
        if (this.field_3497 != null) {
            FDSTagCompound fDSTagCompound = new FDSTagCompound("outroSequence");
            this.field_3497.writeToFDS(fDSTagCompound);
            root.setTagCompound("outroSequence", fDSTagCompound);
        }
    }

    @Override
    public void readSpecificFDS(@NotNull FDSTagCompound root) {
        FDSTagCompound fDSTagCompound;
        boolean bl = root.getBoolean("hasOutroSequence", false);
        if (bl && (fDSTagCompound = root.getTagCompound("outroSequence")) != null) {
            this.field_3497 = new GameSequence();
            this.field_3497.readFromFDS(fDSTagCompound);
        }
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
        return CloudAchievements.ACH_MATCH_WIN_GG;
    }

    @Override
    public boolean shouldUseStamina(@NotNull Player player) {
        return false;
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
        GameTeam gameTeam = ((GunGamePlayerManager)this.playerManager).getTeamByName("Axis");
        GameTeam gameTeam2 = ((GunGamePlayerManager)this.playerManager).getTeamByName("Allies");
        assert (gameTeam != null);
        assert (gameTeam2 != null);
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

