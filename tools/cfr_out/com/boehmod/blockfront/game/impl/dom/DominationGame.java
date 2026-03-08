/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.player.achievement.CloudAchievement
 *  com.boehmod.bflib.cloud.common.player.achievement.CloudAchievements
 *  com.boehmod.bflib.fds.tag.FDSTagCompound
 *  io.netty.buffer.ByteBuf
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  javax.annotation.Nullable
 *  net.minecraft.ChatFormatting
 *  net.minecraft.commands.CommandSource
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraft.core.BlockPos
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.damagesource.DamageSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.registries.DeferredItem
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game.impl.dom;

import com.boehmod.bflib.cloud.common.player.achievement.CloudAchievement;
import com.boehmod.bflib.cloud.common.player.achievement.CloudAchievements;
import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.assets.AssetCommandBuilder;
import com.boehmod.blockfront.assets.AssetCommandValidators;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.sound.BFMusicType;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.block.entity.AbstractCrateGunBlockEntity;
import com.boehmod.blockfront.common.entity.BotEntity;
import com.boehmod.blockfront.common.item.ChestArmorItem;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.match.DivisionData;
import com.boehmod.blockfront.common.match.MatchCallout;
import com.boehmod.blockfront.common.match.MatchClass;
import com.boehmod.blockfront.common.net.BFPopup;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractCapturePoint;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGameStage;
import com.boehmod.blockfront.game.AmmoPoint;
import com.boehmod.blockfront.game.GameMusic;
import com.boehmod.blockfront.game.GameSequence;
import com.boehmod.blockfront.game.GameSequenceSettings;
import com.boehmod.blockfront.game.GameStageManager;
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.TeamJoinType;
import com.boehmod.blockfront.game.impl.dom.DominationCapturePoint;
import com.boehmod.blockfront.game.impl.dom.DominationGameClient;
import com.boehmod.blockfront.game.impl.dom.DominationIdleStage;
import com.boehmod.blockfront.game.impl.dom.DominationPlayerManager;
import com.boehmod.blockfront.game.tag.IAllowsCallouts;
import com.boehmod.blockfront.game.tag.IAllowsRegeneration;
import com.boehmod.blockfront.game.tag.IAllowsRespawning;
import com.boehmod.blockfront.game.tag.IAllowsWarCry;
import com.boehmod.blockfront.game.tag.IAnnounceFirstBlood;
import com.boehmod.blockfront.game.tag.ICanSwitchTeams;
import com.boehmod.blockfront.game.tag.IHasBots;
import com.boehmod.blockfront.game.tag.IHasCapturePoints;
import com.boehmod.blockfront.game.tag.IHasClasses;
import com.boehmod.blockfront.game.tag.IHasCrateGuns;
import com.boehmod.blockfront.game.tag.IHasDominations;
import com.boehmod.blockfront.game.tag.IHasKillStreaks;
import com.boehmod.blockfront.game.tag.ISendBattleReports;
import com.boehmod.blockfront.game.tag.IUseKillIcons;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFLog;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.CommandUtils;
import com.boehmod.blockfront.util.math.FDSPose;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.NotNull;

public final class DominationGame
extends AbstractGame<DominationGame, DominationPlayerManager, GameStageManager<DominationGame, DominationPlayerManager>>
implements ISendBattleReports,
IHasBots,
IAllowsCallouts<DominationGame>,
IHasCapturePoints<DominationPlayerManager, DominationCapturePoint>,
IHasClasses,
IHasCrateGuns,
IAnnounceFirstBlood,
IAllowsRegeneration,
IHasDominations,
IUseKillIcons,
IHasKillStreaks,
IAllowsRespawning,
ICanSwitchTeams,
IAllowsWarCry {
    private static final Component MESSAGE_CPOINT_DEFENDED = Component.translatable((String)"bf.popup.message.cpoint.defended").withStyle(ChatFormatting.GOLD);
    private static final Component MESSAGE_CRATE = Component.translatable((String)"bf.popup.message.crate").withStyle(ChatFormatting.YELLOW);
    private static final Component SWITCHTEAMS_ERROR_SCORES_AXIS = Component.translatable((String)"bf.message.gamemode.switchteams.error.scores");
    private static final Component SWITCHTEAMS_ERROR_SCORES_ALLIES = Component.translatable((String)"bf.message.gamemode.switchteams.error.scores");
    public static final int field_3398 = 500;
    public static final int field_3399 = 20;
    @NotNull
    private final List<DominationCapturePoint> capturePoints = new ObjectArrayList();
    @NotNull
    private final List<AmmoPoint> ammoPoints = new ObjectArrayList();
    @Nullable
    private GameSequence introSequence = null;
    @Nullable
    private GameSequence outroSequence = null;
    private final AssetCommandBuilder command = new AssetCommandBuilder().subCommand("cpoint", new AssetCommandBuilder().subCommand("add", new AssetCommandBuilder((commandContext, stringArray) -> {
        ServerPlayer serverPlayer = ((CommandSourceStack)commandContext.getSource()).getPlayer();
        CommandSource commandSource = ((CommandSourceStack)commandContext.getSource()).source;
        if (serverPlayer == null) {
            return;
        }
        String string = stringArray[0];
        DominationCapturePoint dominationCapturePoint = new DominationCapturePoint((DominationPlayerManager)this.playerManager, (Player)serverPlayer, string);
        this.capturePoints.add(dominationCapturePoint);
        CommandUtils.sendBfa(commandSource, (Component)Component.literal((String)("Added capture point " + string + ". (" + this.capturePoints.size() + ")")));
    }).validator(AssetCommandValidators.count(new String[]{"name"})).validator(AssetCommandValidators.ONLY_PLAYERS)).subCommand("clear", new AssetCommandBuilder((commandContext, stringArray) -> {
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Clearing all " + this.capturePoints.size() + " capture points.")));
        this.capturePoints.clear();
    })).subCommand("move", new AssetCommandBuilder((commandContext, stringArray) -> {
        ServerPlayer serverPlayer = ((CommandSourceStack)commandContext.getSource()).getPlayer();
        CommandSource commandSource = ((CommandSourceStack)commandContext.getSource()).source;
        if (serverPlayer == null) {
            return;
        }
        String string = stringArray[0];
        for (DominationCapturePoint dominationCapturePoint : this.capturePoints) {
            if (!dominationCapturePoint.name.equalsIgnoreCase(string)) continue;
            dominationCapturePoint.setFrom(new FDSPose((Player)serverPlayer));
            CommandUtils.sendBfa(commandSource, (Component)Component.literal((String)("Moved capture point " + string + ".")));
            return;
        }
        CommandUtils.sendBfa(commandSource, (Component)Component.literal((String)("Capture point " + string + " was not found!")));
    }).validator(AssetCommandValidators.count(new String[]{"name"})).validator(AssetCommandValidators.ONLY_PLAYERS))).subCommand("apoint", new AssetCommandBuilder().subCommand("add", new AssetCommandBuilder((commandContext, stringArray) -> {
        Player player = (Player)((CommandSourceStack)commandContext.getSource()).source;
        AmmoPoint ammoPoint = new AmmoPoint(player);
        this.ammoPoints.add(ammoPoint);
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Added ammo point. (" + this.ammoPoints.size() + ")")));
    }).validator(AssetCommandValidators.ONLY_PLAYERS)).subCommand("clear", new AssetCommandBuilder((commandContext, stringArray) -> {
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Clearing all " + this.ammoPoints.size() + " ammo points.")));
        this.ammoPoints.clear();
    }))).subCommand("spawn", new AssetCommandBuilder().subCommand("add", new AssetCommandBuilder((commandContext, stringArray) -> {
        String string = stringArray[0];
        Player player = (Player)((CommandSourceStack)commandContext.getSource()).source;
        FDSPose fDSPose = new FDSPose(player);
        GameTeam gameTeam = ((DominationPlayerManager)this.getPlayerManager()).getTeamByName(string);
        if (gameTeam == null) {
            CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Team " + string + " was not found!")));
            return;
        }
        gameTeam.addPlayerSpawn(fDSPose);
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)(gameTeam.getName() + " team spawn added. (" + gameTeam.getPlayerSpawns().size() + ")")));
    }).validator(AssetCommandValidators.count(new String[]{"team"})).validator(AssetCommandValidators.ONLY_PLAYERS)).subCommand("clear", new AssetCommandBuilder((commandContext, stringArray) -> {
        String string = stringArray[0];
        GameTeam gameTeam = ((DominationPlayerManager)this.getPlayerManager()).getTeamByName(string);
        if (gameTeam == null) {
            CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Team " + string + " was not found!")));
            return;
        }
        gameTeam.clearPlayerSpawns();
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)(gameTeam.getName() + " team's spawns cleared.")));
    }).validator(AssetCommandValidators.count(new String[]{"team"}))));
    public int field_3400 = 0;

    public DominationGame(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager) {
        super(bFAbstractManager, "dom", "Domination");
    }

    public static void method_3330(@NotNull DominationGame dominationGame, @NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull UUID uUID, boolean bl) {
        if (BFUtils.getPlayerStat(dominationGame, uUID, BFStats.PLAYED_INTRO) != 0) {
            return;
        }
        BFUtils.setPlayerStat(dominationGame, uUID, BFStats.PLAYED_INTRO, 1);
        BFUtils.queueMusic(uUID, GameMusic.create().method_1541(BFMusicType.WARMUP));
        GameSequence gameSequence = dominationGame.getIntroSequence();
        if (gameSequence != null) {
            GameSequenceSettings gameSequenceSettings = new GameSequenceSettings();
            if (bl) {
                gameSequenceSettings.setStopMusicOnFinish(true);
            }
            BFUtils.sendSequencePacket(bFAbstractManager, gameSequence, gameSequenceSettings, uUID);
        }
    }

    @NotNull
    public DominationGameClient createGameClient(@NotNull BFClientManager bFClientManager) {
        return new DominationGameClient(bFClientManager, this, (ClientPlayerDataHandler)this.dataHandler);
    }

    @Override
    @NotNull
    protected DominationPlayerManager createPlayerManager() {
        return new DominationPlayerManager(this, this.dataHandler);
    }

    @Override
    @NotNull
    public AbstractGameStage<DominationGame, DominationPlayerManager> createFirstStage() {
        return new DominationIdleStage();
    }

    @Override
    public void getErrorMessages(@NotNull List<MutableComponent> messages) {
        if (this.capturePoints.isEmpty()) {
            messages.add(Component.literal((String)("Capture points in game '" + this.name + "' are missing.")));
        }
        if (this.introSequence == null) {
            messages.add(Component.literal((String)("Intro sequence in game '" + this.name + "' is missing.")));
        }
        if (this.outroSequence == null) {
            messages.add(Component.literal((String)("Outro sequence in game '" + this.name + "' is missing.")));
        }
        super.getErrorMessages(messages);
    }

    @Override
    public void updateStageManager(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull PlayerDataHandler<?> dataHandler, @NotNull ServerLevel level, @NotNull Set<UUID> players) {
        this.stageManager.update(manager, dataHandler, (DominationPlayerManager)this.playerManager, level, players);
    }

    @Override
    protected boolean isMatchSuccess() {
        return true;
    }

    public void method_3333(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerLevel serverLevel, @NotNull Set<UUID> set) {
        this.capturePoints.forEach(dominationCapturePoint -> dominationCapturePoint.onUpdate(bFAbstractManager, playerDataHandler, this, serverLevel, set));
        if (this.field_3400++ > 3) {
            this.field_3400 = 0;
            for (DominationCapturePoint dominationCapturePoint2 : this.capturePoints) {
                GameTeam gameTeam = dominationCapturePoint2.getCbTeam();
                if (gameTeam == null) continue;
                int n = gameTeam.getObjectInt(BFStats.SCORE);
                gameTeam.setObject(BFStats.SCORE, n + 1);
            }
        }
    }

    @Override
    public boolean playerJoinTeam(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull TeamJoinType joinType, @NotNull ServerLevel level, @NotNull ServerPlayer player) {
        GameTeam gameTeam = ((DominationPlayerManager)this.playerManager).getNextJoiningTeam();
        if (gameTeam == null) {
            BFLog.log("No team available for player " + player.getScoreboardName() + " to join.", new Object[0]);
            return false;
        }
        return ((DominationPlayerManager)this.playerManager).playerJoinTeam(manager, joinType, level, player, gameTeam);
    }

    @Override
    public void specificReset(@Nullable Level level) {
        this.resetCapturePoints();
    }

    @Override
    public void writeAll(@NotNull ByteBuf buf, boolean writeMap) throws IOException {
        int n;
        super.writeAll(buf, writeMap);
        int n2 = this.capturePoints.size();
        buf.writeInt(n2);
        for (n = 0; n < n2; ++n) {
            DominationCapturePoint dominationCapturePoint = this.capturePoints.get(n);
            dominationCapturePoint.write(buf);
        }
        n = this.ammoPoints.size();
        buf.writeInt(n);
        for (int i = 0; i < n; ++i) {
            this.ammoPoints.get(i).write(buf);
        }
    }

    @Override
    public void readAll(@NotNull ByteBuf buf) throws IOException {
        int n;
        super.readAll(buf);
        int n2 = buf.readInt();
        if (this.capturePoints.isEmpty()) {
            for (n = 0; n < n2; ++n) {
                this.capturePoints.add(new DominationCapturePoint((DominationPlayerManager)this.playerManager, buf));
            }
        } else {
            for (DominationCapturePoint dominationCapturePoint : this.capturePoints) {
                dominationCapturePoint.read(buf);
            }
        }
        this.ammoPoints.clear();
        n = buf.readInt();
        for (int i = 0; i < n; ++i) {
            AmmoPoint ammoPoint = new AmmoPoint();
            ammoPoint.read(buf);
            this.ammoPoints.add(ammoPoint);
        }
    }

    @Override
    public void writeSpecificFDS(@NotNull FDSTagCompound root) {
        int n;
        int n2 = this.capturePoints.size();
        root.setInteger("cpSize", n2);
        for (n = 0; n < n2; ++n) {
            this.capturePoints.get(n).writeNamedFDS("cp" + n, root);
        }
        n = this.ammoPoints.size();
        root.setInteger("apSize", n);
        for (int i = 0; i < n; ++i) {
            this.ammoPoints.get(i).writeNamedFDS("ap" + i, root);
        }
        root.setBoolean("hasIntroSequence", this.introSequence != null);
        if (this.introSequence != null) {
            FDSTagCompound fDSTagCompound = new FDSTagCompound("introSequence");
            this.introSequence.writeToFDS(fDSTagCompound);
            root.setTagCompound("introSequence", fDSTagCompound);
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
        boolean bl;
        AmmoPoint ammoPoint;
        int n;
        int n2;
        this.capturePoints.clear();
        int n3 = root.getInteger("cpSize");
        for (n2 = 0; n2 < n3; ++n2) {
            DominationCapturePoint dominationCapturePoint = new DominationCapturePoint((DominationPlayerManager)this.playerManager, "cp" + n2, root);
            this.capturePoints.add(dominationCapturePoint);
        }
        this.ammoPoints.clear();
        n2 = root.getInteger("apSize");
        for (n = 0; n < n2; ++n) {
            ammoPoint = AmmoPoint.newFromFDS("ap" + n, root);
            if (ammoPoint == null) continue;
            this.ammoPoints.add(ammoPoint);
        }
        n = root.getBoolean("hasIntroSequence", false) ? 1 : 0;
        if (n != 0 && (ammoPoint = root.getTagCompound("introSequence")) != null) {
            this.introSequence = new GameSequence();
            this.introSequence.readFromFDS((FDSTagCompound)ammoPoint);
        }
        if ((bl = root.getBoolean("hasOutroSequence", false)) && (fDSTagCompound = root.getTagCompound("outroSequence")) != null) {
            this.outroSequence = new GameSequence();
            this.outroSequence.readFromFDS(fDSTagCompound);
        }
    }

    public void resetCapturePoints() {
        this.capturePoints.forEach(AbstractCapturePoint::reset);
    }

    @Nullable
    public GameSequence getIntroSequence() {
        return this.introSequence;
    }

    @Nullable
    public GameSequence getOutroSequence() {
        return this.outroSequence;
    }

    public void setIntroSequence(@Nullable GameSequence sequence) {
        this.introSequence = sequence;
    }

    public void setOutroSequence(@Nullable GameSequence sequence) {
        this.outroSequence = sequence;
    }

    @Nullable
    public DominationCapturePoint method_3331(@NotNull Player player, @NotNull Set<UUID> set) {
        for (DominationCapturePoint dominationCapturePoint : this.capturePoints) {
            if (!dominationCapturePoint.method_3024(player.level(), this.method_3398(dominationCapturePoint), set).contains(player)) continue;
            return dominationCapturePoint;
        }
        return null;
    }

    @Nullable
    public DominationCapturePoint method_5514(@NotNull BotEntity botEntity, @NotNull GameTeam gameTeam) {
        Vec3 vec3 = botEntity.position();
        DominationCapturePoint dominationCapturePoint = null;
        for (DominationCapturePoint dominationCapturePoint2 : this.capturePoints) {
            if (!dominationCapturePoint2.method_5506((LivingEntity)botEntity) || dominationCapturePoint != null && !(dominationCapturePoint2.position.distanceTo(vec3) < dominationCapturePoint.position.distanceTo(vec3)) || gameTeam.equals(dominationCapturePoint2.getCbTeam())) continue;
            dominationCapturePoint = dominationCapturePoint2;
        }
        return dominationCapturePoint;
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
        return 1;
    }

    @Override
    public boolean shouldUseStamina(@NotNull Player player) {
        return true;
    }

    @Override
    public boolean shouldAnnounceRageQuits() {
        return true;
    }

    @Override
    public boolean shouldShowDeadMessages() {
        return true;
    }

    @Override
    @NotNull
    public CloudAchievement getVictoryAchievement() {
        return CloudAchievements.ACH_MATCH_WIN_DOM;
    }

    @Override
    public void method_3394(@NotNull ServerLevel serverLevel) {
        int n;
        BFUtils.discardMatchEntities(serverLevel, this, this.playerManager);
        GameTeam gameTeam = ((DominationPlayerManager)this.playerManager).getTeamByName("Axis");
        GameTeam gameTeam2 = ((DominationPlayerManager)this.playerManager).getTeamByName("Allies");
        assert (gameTeam != null);
        assert (gameTeam2 != null);
        int n2 = this.method_3393(gameTeam2);
        int n3 = this.method_3393(gameTeam);
        for (n = 0; n < n2; ++n) {
            BFUtils.method_2908(this, serverLevel, gameTeam2, true);
        }
        for (n = 0; n < n3; ++n) {
            BFUtils.method_2908(this, serverLevel, gameTeam, true);
        }
    }

    @Override
    public void method_3392(@NotNull BotEntity botEntity, @NotNull Level level) {
    }

    @Override
    public void method_3387(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull ServerLevel serverLevel, @NotNull BotEntity botEntity, @NotNull DamageSource damageSource) {
        Object object;
        Object object2;
        GameTeam gameTeam;
        GameTeam gameTeam2 = botEntity.getTeam();
        if (gameTeam2 != null && botEntity.method_2009()) {
            gameTeam = ((DominationPlayerManager)this.playerManager).getTeamByName("Axis");
            object2 = ((DominationPlayerManager)this.playerManager).getTeamByName("Allies");
            assert (gameTeam != null);
            assert (object2 != null);
            switch (gameTeam2.getName()) {
                case "Allies": {
                    BFUtils.method_2908(this, serverLevel, (GameTeam)object2, true);
                    break;
                }
                case "Axis": {
                    BFUtils.method_2908(this, serverLevel, gameTeam, true);
                }
            }
        }
        if ((object2 = damageSource.getEntity()) instanceof ServerPlayer && (object = ((DominationPlayerManager)this.playerManager).getPlayerTeam((UUID)(object2 = (gameTeam = (ServerPlayer)object2).getUUID()))) != null) {
            BFUtils.incrementPlayerStat(bFAbstractManager, this, (UUID)object2, BFStats.SCORE);
            for (DominationCapturePoint dominationCapturePoint : this.capturePoints) {
                if (!dominationCapturePoint.isInRadius((Entity)botEntity, this.method_3398(dominationCapturePoint)) || !((GameTeam)object).equals(dominationCapturePoint.getCbTeam())) continue;
                BFUtils.sendPopupMessage((ServerPlayer)gameTeam, new BFPopup(MESSAGE_CPOINT_DEFENDED, 40));
                BFUtils.triggerPlayerStat(bFAbstractManager, this, (UUID)object2, BFStats.SCORE, 2);
            }
        }
    }

    @Override
    public void method_3391(@NotNull BotEntity botEntity, @NotNull Player player) {
    }

    @Override
    public void method_3389(@NotNull BotEntity botEntity) {
    }

    @Override
    @Nullable
    public BlockPos method_3388(@NotNull BotEntity botEntity) {
        GameTeam gameTeam = botEntity.getTeam();
        if (gameTeam == null) {
            return null;
        }
        DominationCapturePoint dominationCapturePoint = this.method_5514(botEntity, gameTeam);
        if (dominationCapturePoint != null) {
            return dominationCapturePoint.asBlockPos();
        }
        GameTeam gameTeam2 = ((DominationPlayerManager)this.playerManager).getTeamByName(gameTeam.isAllies() ? "Axis" : "Allies");
        if (gameTeam2 != null) {
            return gameTeam2.randomSpawn(this).asBlockPos();
        }
        return null;
    }

    @Override
    public boolean method_3390(@NotNull BotEntity botEntity, @NotNull DamageSource damageSource) {
        Object object = damageSource.getEntity();
        if (object instanceof BotEntity) {
            BotEntity botEntity2 = (BotEntity)object;
            return (object = botEntity2.getTeam()) != null && !((GameTeam)object).equals(botEntity.getTeam());
        }
        object = damageSource.getEntity();
        if (object instanceof Player) {
            Player player = (Player)object;
            return (object = ((DominationPlayerManager)this.playerManager).getPlayerTeam(player.getUUID())) != null && !((GameTeam)object).equals(botEntity.getTeam());
        }
        return false;
    }

    @Override
    public int method_3393(@NotNull GameTeam gameTeam) {
        return 8;
    }

    @Override
    public boolean playerCanRegenerate(@NotNull Player player) {
        return player.getHealth() > player.getMaxHealth() / 2.0f;
    }

    @Override
    public float method_3418(@NotNull Player player) {
        return 0.5f;
    }

    @Override
    public int method_3419(@NotNull Player player) {
        return 80;
    }

    @Override
    public boolean method_3406(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull Level level, @NotNull ServerPlayer serverPlayer, @NotNull AbstractCrateGunBlockEntity abstractCrateGunBlockEntity) {
        Vec3 vec3;
        DeferredItem<? extends ChestArmorItem> deferredItem;
        UUID uUID = serverPlayer.getUUID();
        ItemStack itemStack = abstractCrateGunBlockEntity.getHeldItem();
        if (itemStack.isEmpty() || !((deferredItem = itemStack.getItem()) instanceof GunItem)) {
            return false;
        }
        GunItem gunItem = (GunItem)deferredItem;
        serverPlayer.getInventory().setItem(gunItem.isSecondary() ? 0 : 1, itemStack);
        deferredItem = gunItem.getChestArmor();
        if (deferredItem != null) {
            vec3 = new ItemStack((ItemLike)deferredItem.get());
            EquipmentSlot equipmentSlot = serverPlayer.getEquipmentSlotForItem((ItemStack)vec3);
            serverPlayer.setItemSlot(equipmentSlot, (ItemStack)vec3);
        }
        abstractCrateGunBlockEntity.putItem(ItemStack.EMPTY);
        vec3 = serverPlayer.position();
        BFUtils.playSound(level, vec3, (SoundEvent)BFSounds.MATCH_CLASSES_CHANGE.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
        BFUtils.playSound(level, vec3, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.0f, 1.0f);
        BFUtils.triggerPlayerStat(bFAbstractManager, this, uUID, BFStats.SCORE, 2);
        BFUtils.sendPopupMessage(serverPlayer, new BFPopup(MESSAGE_CRATE, 40));
        return true;
    }

    @Override
    public void putCrateItem(@NotNull AbstractCrateGunBlockEntity crate) {
        ObjectArrayList objectArrayList = new ObjectArrayList();
        objectArrayList.addAll(this.getAlliesDivision().getCountry().getMiscItems());
        objectArrayList.addAll(this.getAxisDivision().getCountry().getMiscItems());
        ItemStack itemStack = crate.getHeldItem();
        if (itemStack.isEmpty() && !objectArrayList.isEmpty()) {
            Collections.shuffle(objectArrayList);
            crate.putItem(new ItemStack((ItemLike)((Supplier)objectArrayList.getFirst()).get()));
        }
    }

    @Override
    public int getCrateRefreshInterval(@NotNull AbstractCrateGunBlockEntity abstractCrateGunBlockEntity) {
        return 3600;
    }

    @Override
    public Component getSwitchTeamMessage(@NotNull ServerPlayer player) {
        GameTeam gameTeam = ((DominationPlayerManager)this.playerManager).getTeamByName("Axis");
        GameTeam gameTeam2 = ((DominationPlayerManager)this.playerManager).getTeamByName("Allies");
        assert (gameTeam != null);
        assert (gameTeam2 != null);
        Component component = null;
        if ((double)gameTeam.getObjectInt(BFStats.SCORE) > 375.0) {
            component = SWITCHTEAMS_ERROR_SCORES_AXIS;
        }
        if ((double)gameTeam2.getObjectInt(BFStats.SCORE) > 375.0) {
            component = SWITCHTEAMS_ERROR_SCORES_ALLIES;
        }
        if (component != null) {
            return component;
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
    public int getDominationThreshold() {
        return 4;
    }

    @Override
    public void method_3396(@NotNull ServerPlayer serverPlayer, @NotNull UUID uUID, @NotNull MatchCallout matchCallout) {
        this.method_3395(this, uUID, matchCallout);
    }

    @Override
    public int method_3398(@NotNull DominationCapturePoint dominationCapturePoint) {
        return 3;
    }

    @Override
    public void method_3397(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull Level level, @NotNull DominationCapturePoint dominationCapturePoint, @NotNull String string, @NotNull String string2, @NotNull Set<UUID> set) {
        DominationPlayerManager dominationPlayerManager = (DominationPlayerManager)this.getPlayerManager();
        GameTeam gameTeam = dominationPlayerManager.getTeamByName(string);
        assert (gameTeam != null);
        Set<UUID> set2 = gameTeam.getPlayers();
        GameTeam gameTeam2 = dominationPlayerManager.getTeamByName(string2);
        BFUtils.playSound(set2, (SoundEvent)BFSounds.MATCH_GAMEMODE_DOM_POINT_CAPTURED.get(), SoundSource.MUSIC, 1.0f);
        BFUtils.playSound(level, dominationCapturePoint.position, (SoundEvent)BFSounds.MATCH_CAPTUREPOINT_CAPTURED.get(), SoundSource.NEUTRAL, 2.0f, 1.0f);
        if (gameTeam2 != null) {
            BFUtils.playSound(gameTeam2.getPlayers(), (SoundEvent)BFSounds.MATCH_GAMEMODE_DOM_POINT_LOST.get(), SoundSource.MUSIC, 1.0f);
        }
        DivisionData divisionData = gameTeam.getDivisionData(this);
        MutableComponent mutableComponent = Component.literal((String)dominationCapturePoint.name.toUpperCase(Locale.ROOT)).withStyle(ChatFormatting.GRAY);
        MutableComponent mutableComponent2 = Component.literal((String)divisionData.getCountry().getName()).withStyle(ChatFormatting.GRAY);
        MutableComponent mutableComponent3 = Component.translatable((String)"bf.message.gamemode.notification.point.captured.team", (Object[])new Object[]{mutableComponent2, mutableComponent}).withStyle(gameTeam.getStyleText());
        BFUtils.sendFancyMessage(gameTeam, BFUtils.OBJECTIVE_UP_PREFIX, (Component)mutableComponent3);
        for (GameTeam object2 : dominationPlayerManager.getTeams()) {
            if (object2.equals(gameTeam)) continue;
            BFUtils.sendFancyMessage(object2, BFUtils.OBJECTIVE_PREFIX, (Component)mutableComponent3);
        }
        List<Player> list = dominationCapturePoint.method_3024(level, this.method_3398(dominationCapturePoint), set);
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            Player player = (Player)iterator.next();
            UUID uUID = player.getUUID();
            BFUtils.triggerPlayerStat(bFAbstractManager, this, uUID, BFStats.SCORE, 3);
            BFUtils.incrementPlayerStat(bFAbstractManager, this, uUID, BFStats.CPOINTS);
        }
    }

    @Override
    public List<DominationCapturePoint> getCapturePoints() {
        return Collections.unmodifiableList(this.capturePoints);
    }

    @Override
    public boolean method_3442(@NotNull Level level, @NotNull ServerPlayer serverPlayer, @NotNull UUID uUID) {
        int n = BFUtils.getPlayerStat(this, uUID, BFStats.WAR_CRY);
        return n > 0;
    }

    @Override
    public void method_3441(@NotNull Level level, @NotNull ServerPlayer serverPlayer, @NotNull UUID uUID) {
    }

    @Override
    public float method_3443() {
        return 32.0f;
    }

    @Override
    public boolean shouldAnnounceFirstBlood(@NotNull ServerPlayer player) {
        return this.status == GameStatus.GAME;
    }

    @NotNull
    public List<AmmoPoint> method_3325() {
        return Collections.unmodifiableList(this.ammoPoints);
    }
}

