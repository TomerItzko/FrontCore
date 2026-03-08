/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.player.achievement.CloudAchievement
 *  com.boehmod.bflib.fds.tag.FDSTagCompound
 *  io.netty.buffer.ByteBuf
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  it.unimi.dsi.fastutil.objects.ObjectList
 *  it.unimi.dsi.fastutil.objects.ObjectLists
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
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec2
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.registries.DeferredItem
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game.impl.conq;

import com.boehmod.bflib.cloud.common.player.achievement.CloudAchievement;
import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.assets.AssetCommandBuilder;
import com.boehmod.blockfront.assets.AssetCommandValidators;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.block.entity.AbstractCrateGunBlockEntity;
import com.boehmod.blockfront.common.entity.BotEntity;
import com.boehmod.blockfront.common.item.ChestArmorItem;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.match.DivisionData;
import com.boehmod.blockfront.common.match.MatchCallout;
import com.boehmod.blockfront.common.match.MatchClass;
import com.boehmod.blockfront.common.net.BFPopup;
import com.boehmod.blockfront.common.player.BFAbstractPlayerData;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractCapturePoint;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGameStage;
import com.boehmod.blockfront.game.AmmoPoint;
import com.boehmod.blockfront.game.GameStageManager;
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.TeamJoinType;
import com.boehmod.blockfront.game.impl.conq.ConquestBoundary;
import com.boehmod.blockfront.game.impl.conq.ConquestCapturePoint;
import com.boehmod.blockfront.game.impl.conq.ConquestGameClient;
import com.boehmod.blockfront.game.impl.conq.ConquestIdleStage;
import com.boehmod.blockfront.game.impl.conq.ConquestPlayerManager;
import com.boehmod.blockfront.game.impl.conq.VehicleSpawn;
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
import com.boehmod.blockfront.game.tag.ISendBattleReports;
import com.boehmod.blockfront.game.tag.IUseKillIcons;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.unnamed.BF_797;
import com.boehmod.blockfront.util.BFLog;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.CommandUtils;
import com.boehmod.blockfront.util.RegistryUtils;
import com.boehmod.blockfront.util.math.FDSPose;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectLists;
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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.NotNull;

public final class ConquestGame
extends AbstractGame<ConquestGame, ConquestPlayerManager, GameStageManager<ConquestGame, ConquestPlayerManager>>
implements ISendBattleReports,
IHasBots,
IAllowsCallouts<ConquestGame>,
IHasCapturePoints<ConquestPlayerManager, ConquestCapturePoint>,
IHasClasses,
IHasCrateGuns,
IAnnounceFirstBlood,
IAllowsRegeneration,
IHasDominations,
IUseKillIcons,
IAllowsRespawning,
ICanSwitchTeams,
IAllowsWarCry {
    private static final Component field_6365 = Component.translatable((String)"bf.popup.message.cpoint.defended").withStyle(ChatFormatting.GOLD);
    public static final int field_3364 = 500;
    public static final int field_3361 = 20;
    @NotNull
    public final ObjectList<ConquestCapturePoint> capturePoints = new ObjectArrayList();
    @NotNull
    private final ObjectList<AmmoPoint> field_3360 = new ObjectArrayList();
    @NotNull
    private final ObjectList<VehicleSpawn> vehicleSpawns = new ObjectArrayList();
    public ConquestBoundary field_3356 = new ConquestBoundary();
    private final AssetCommandBuilder command = new AssetCommandBuilder().subCommand("boundary", new AssetCommandBuilder().subCommand("add", new AssetCommandBuilder((commandContext, stringArray) -> {
        Player player = (Player)((CommandSourceStack)commandContext.getSource()).source;
        this.field_3356.addWallPoint(new Vec2((float)player.getX(), (float)player.getZ()));
        CommandUtils.sendBfa((CommandSource)player, (Component)Component.literal((String)("Added new point to boundary wall. (Vertices: " + this.field_3356.numWallPoints() + ", Segments: " + this.field_3356.numWallLines() + ")")));
    }).validator(AssetCommandValidators.ONLY_PLAYERS)).subCommand("reset", new AssetCommandBuilder((commandContext, stringArray) -> {
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)"Reset the boundary wall."));
        this.field_3356 = new ConquestBoundary();
    }))).subCommand("cpoint", new AssetCommandBuilder().subCommand("add", new AssetCommandBuilder((commandContext, stringArray) -> {
        Player player = (Player)((CommandSourceStack)commandContext.getSource()).source;
        String string = stringArray[0];
        ConquestCapturePoint conquestCapturePoint = new ConquestCapturePoint((ConquestPlayerManager)this.playerManager, player, string);
        this.capturePoints.add((Object)conquestCapturePoint);
        CommandUtils.sendBfa((CommandSource)player, (Component)Component.literal((String)("Added capture point " + string + ". (" + this.capturePoints.size() + ")")));
    }).validator(AssetCommandValidators.count(new String[]{"name"})).validator(AssetCommandValidators.ONLY_PLAYERS)).subCommand("spawn", new AssetCommandBuilder().subCommand("add", new AssetCommandBuilder((commandContext, stringArray) -> {
        Player player = (Player)((CommandSourceStack)commandContext.getSource()).source;
        String string = stringArray[0];
        ConquestCapturePoint conquestCapturePoint = this.method_3268(string);
        if (conquestCapturePoint != null) {
            conquestCapturePoint.method_3292(player.position());
            CommandUtils.sendBfa((CommandSource)player, (Component)Component.literal((String)("Added spawn point to capture point " + string + ". (" + conquestCapturePoint.method_3288().size() + ")")));
        } else {
            CommandUtils.sendBfa((CommandSource)player, (Component)Component.literal((String)("Capture point " + string + " was not found!")));
        }
    }).validator(AssetCommandValidators.count(new String[]{"name"})).validator(AssetCommandValidators.ONLY_PLAYERS)).subCommand("clear", new AssetCommandBuilder((commandContext, stringArray) -> {
        Player player = (Player)((CommandSourceStack)commandContext.getSource()).source;
        String string = stringArray[0];
        ConquestCapturePoint conquestCapturePoint = this.method_3268(string);
        if (conquestCapturePoint != null) {
            conquestCapturePoint.method_3291();
            CommandUtils.sendBfa((CommandSource)player, (Component)Component.literal((String)("Cleared spawn points from capture point " + string + ". (" + conquestCapturePoint.method_3288().size() + ")")));
        } else {
            CommandUtils.sendBfa((CommandSource)player, (Component)Component.literal((String)("Capture point " + string + " was not found!")));
        }
    }).validator(AssetCommandValidators.count(new String[]{"name"})).validator(AssetCommandValidators.ONLY_PLAYERS))).subCommand("clear", new AssetCommandBuilder((commandContext, stringArray) -> {
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Clearing all " + this.capturePoints.size() + " capture points.")));
        this.capturePoints.clear();
    }))).subCommand("apoint", new AssetCommandBuilder().subCommand("add", new AssetCommandBuilder((commandContext, stringArray) -> {
        Player player = (Player)((CommandSourceStack)commandContext.getSource()).source;
        AmmoPoint ammoPoint = new AmmoPoint(player);
        this.field_3360.add((Object)ammoPoint);
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Added ammo point. (" + this.field_3360.size() + ")")));
    }).validator(AssetCommandValidators.ONLY_PLAYERS)).subCommand("clear", new AssetCommandBuilder((commandContext, stringArray) -> {
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Clearing all " + this.field_3360.size() + " ammo points.")));
        this.field_3360.clear();
    }))).subCommand("vpoint", new AssetCommandBuilder().subCommand("add", new AssetCommandBuilder((commandContext, stringArray) -> {
        String string = stringArray[0];
        String string2 = stringArray[1];
        try {
            boolean bl = string.equalsIgnoreCase("Allies");
            Supplier<EntityType<?>> supplier = RegistryUtils.retrieveEntityType(string2);
            Player player = (Player)((CommandSourceStack)commandContext.getSource()).source;
            VehicleSpawn vehicleSpawn = new VehicleSpawn(player, bl, supplier);
            this.vehicleSpawns.add((Object)vehicleSpawn);
            CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Added " + (bl ? "Allied" : "Axis") + " vehicle point. (" + this.vehicleSpawns.size() + ")")));
        }
        catch (IllegalStateException illegalStateException) {
            CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Vehicle type '" + string2 + "' was not found!")));
        }
    }).validator(AssetCommandValidators.count(new String[]{"team", "vehicle"})).validator(AssetCommandValidators.ONLY_PLAYERS)).subCommand("clear", new AssetCommandBuilder((commandContext, stringArray) -> {
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Clearing all " + this.vehicleSpawns.size() + " vehicle points.")));
        this.vehicleSpawns.clear();
    }))).subCommand("spawn", new AssetCommandBuilder().subCommand("add", new AssetCommandBuilder((commandContext, stringArray) -> {
        String string = stringArray[0];
        Player player = (Player)((CommandSourceStack)commandContext.getSource()).source;
        FDSPose fDSPose = new FDSPose(player);
        GameTeam gameTeam = ((ConquestPlayerManager)this.getPlayerManager()).getTeamByName(string);
        if (gameTeam == null) {
            CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Team " + string + " was not found!")));
            return;
        }
        gameTeam.addPlayerSpawn(fDSPose);
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)(gameTeam.getName() + " team spawn added. (" + gameTeam.getPlayerSpawns().size() + ")")));
    }).validator(AssetCommandValidators.count(new String[]{"team"})).validator(AssetCommandValidators.ONLY_PLAYERS)).subCommand("clear", new AssetCommandBuilder((commandContext, stringArray) -> {
        String string = stringArray[0];
        GameTeam gameTeam = ((ConquestPlayerManager)this.getPlayerManager()).getTeamByName(string);
        if (gameTeam == null) {
            CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Team " + string + " was not found!")));
            return;
        }
        gameTeam.clearPlayerSpawns();
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)(gameTeam.getName() + " team's spawns cleared.")));
    }).validator(AssetCommandValidators.count(new String[]{"team"}))));
    private int field_3362 = 0;

    public ConquestGame(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager) {
        super(bFAbstractManager, "conq", "Conquest");
    }

    @NotNull
    public ConquestGameClient createGameClient(@NotNull BFClientManager bFClientManager) {
        return new ConquestGameClient(bFClientManager, this, (ClientPlayerDataHandler)this.dataHandler);
    }

    @Override
    @NotNull
    protected ConquestPlayerManager createPlayerManager() {
        return new ConquestPlayerManager(this, this.dataHandler);
    }

    @Nullable
    ConquestCapturePoint method_3268(@NotNull String string) {
        for (ConquestCapturePoint conquestCapturePoint : this.capturePoints) {
            if (!conquestCapturePoint.name.equalsIgnoreCase(string)) continue;
            return conquestCapturePoint;
        }
        return null;
    }

    @Override
    @NotNull
    public AbstractGameStage<ConquestGame, ConquestPlayerManager> createFirstStage() {
        return new ConquestIdleStage();
    }

    @Override
    public void updateStageManager(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull PlayerDataHandler<?> dataHandler, @NotNull ServerLevel level, @NotNull Set<UUID> players) {
        this.stageManager.update(manager, dataHandler, (ConquestPlayerManager)this.playerManager, level, players);
        if (this.field_3362-- <= 0) {
            this.field_3362 = 20;
            if (!this.field_3356.method_3132()) {
                for (UUID uUID : players) {
                    this.method_3275(dataHandler, uUID);
                }
            }
        }
    }

    @Override
    protected boolean isMatchSuccess() {
        return true;
    }

    private void method_3275(@NotNull PlayerDataHandler<?> playerDataHandler, @NotNull UUID uUID) {
        ServerPlayer serverPlayer = BFUtils.getPlayerByUUID(uUID);
        if (serverPlayer == null) {
            return;
        }
        Object obj = playerDataHandler.getPlayerData((Player)serverPlayer);
        if (((BFAbstractPlayerData)obj).isOutOfGame()) {
            return;
        }
        Vec3 vec3 = serverPlayer.position();
        if (this.field_3356.method_3128((float)vec3.x, (float)vec3.z)) {
            BFUtils.setPlayerStat(this, uUID, BF_797.field_3382, 5);
            return;
        }
        int n = BFUtils.getPlayerStat(this, uUID, BF_797.field_3382);
        if (n <= 0) {
            serverPlayer.kill();
            MutableComponent mutableComponent = Component.translatable((String)"bf.message.gamemode.conquest.boundary.kill").withStyle(ChatFormatting.RED);
            BFUtils.sendNoticeMessage(uUID, (Component)mutableComponent);
        } else {
            BFUtils.setPlayerStat(this, uUID, BF_797.field_3382, n - 1);
            MutableComponent mutableComponent = Component.literal((String)String.valueOf(n)).withColor(0xFFFFFF);
            MutableComponent mutableComponent2 = Component.translatable((String)"bf.message.gamemode.conquest.boundary.warning", (Object[])new Object[]{mutableComponent}).withStyle(ChatFormatting.RED);
            BFUtils.sendNoticeMessage(uUID, (Component)mutableComponent2);
        }
    }

    public void method_3272() {
        int n = 0;
        int n2 = 0;
        GameTeam gameTeam = ((ConquestPlayerManager)this.playerManager).getTeamByName("Axis");
        GameTeam gameTeam2 = ((ConquestPlayerManager)this.playerManager).getTeamByName("Allies");
        assert (gameTeam != null);
        assert (gameTeam2 != null);
        for (ConquestCapturePoint conquestCapturePoint : this.capturePoints) {
            if (gameTeam.equals(conquestCapturePoint.getCbTeam())) {
                ++n;
                continue;
            }
            if (!gameTeam2.equals(conquestCapturePoint.getCbTeam())) continue;
            ++n2;
        }
        int n3 = this.capturePoints.size() / 2;
        if (n >= n3) {
            gameTeam2.decreaseObjectInt(BF_797.field_3381, 1);
        } else if (n2 >= n3) {
            gameTeam.decreaseObjectInt(BF_797.field_3381, 1);
        }
    }

    @Override
    public boolean playerJoinTeam(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull TeamJoinType joinType, @NotNull ServerLevel level, @NotNull ServerPlayer player) {
        GameTeam gameTeam = ((ConquestPlayerManager)this.playerManager).getNextJoiningTeam();
        if (gameTeam == null) {
            BFLog.log("No team available for player " + player.getScoreboardName() + " to join.", new Object[0]);
            return false;
        }
        return ((ConquestPlayerManager)this.playerManager).playerJoinTeam(manager, joinType, level, player, gameTeam);
    }

    @Override
    public void specificReset(@Nullable Level level) {
        this.method_3273();
        for (GameTeam gameTeam : ((ConquestPlayerManager)this.playerManager).getTeams()) {
            gameTeam.setObject(BF_797.field_3381, 500);
        }
    }

    @Override
    public void writeAll(@NotNull ByteBuf buf, boolean writeMap) throws IOException {
        super.writeAll(buf, writeMap);
        buf.writeInt(this.capturePoints.size());
        for (FDSPose fDSPose : this.capturePoints) {
            ((ConquestCapturePoint)fDSPose).write(buf);
        }
        buf.writeInt(this.field_3360.size());
        for (FDSPose fDSPose : this.field_3360) {
            fDSPose.write(buf);
        }
        buf.writeInt(this.vehicleSpawns.size());
        for (FDSPose fDSPose : this.vehicleSpawns) {
            fDSPose.write(buf);
        }
        this.field_3356.write(buf);
    }

    @Override
    public void readAll(@NotNull ByteBuf buf) throws IOException {
        int n;
        int n2;
        super.readAll(buf);
        int n3 = buf.readInt();
        if (this.capturePoints.isEmpty()) {
            for (n2 = 0; n2 < n3; ++n2) {
                this.capturePoints.add((Object)new ConquestCapturePoint((ConquestPlayerManager)this.playerManager, buf));
            }
        } else {
            for (n2 = 0; n2 < n3; ++n2) {
                ((ConquestCapturePoint)this.capturePoints.get(n2)).read(buf);
            }
        }
        this.field_3360.clear();
        n2 = buf.readInt();
        for (n = 0; n < n2; ++n) {
            AmmoPoint ammoPoint = new AmmoPoint();
            ammoPoint.read(buf);
            this.field_3360.add((Object)ammoPoint);
        }
        this.vehicleSpawns.clear();
        n = buf.readInt();
        for (int i = 0; i < n; ++i) {
            VehicleSpawn vehicleSpawn = new VehicleSpawn();
            vehicleSpawn.read(buf);
            this.vehicleSpawns.add((Object)vehicleSpawn);
        }
        this.field_3356.read(buf);
    }

    @Override
    public void writeSpecificFDS(@NotNull FDSTagCompound root) {
        int n;
        int n2;
        int n3 = this.capturePoints.size();
        root.setInteger("cpSize", n3);
        for (n2 = 0; n2 < n3; ++n2) {
            ((ConquestCapturePoint)this.capturePoints.get(n2)).writeNamedFDS("cp" + n2, root);
        }
        n2 = this.field_3360.size();
        root.setInteger("apSize", n2);
        for (n = 0; n < n2; ++n) {
            ((AmmoPoint)this.field_3360.get(n)).writeNamedFDS("ap" + n, root);
        }
        n = this.vehicleSpawns.size();
        root.setInteger("vpSize", n);
        for (int i = 0; i < n; ++i) {
            ((VehicleSpawn)this.vehicleSpawns.get(i)).writeNamedFDS("vp" + i, root);
        }
        this.field_3356.writeToFDS(root);
    }

    @Override
    public void readSpecificFDS(@NotNull FDSTagCompound root) {
        int n;
        int n2;
        this.capturePoints.clear();
        int n3 = root.getInteger("cpSize");
        for (n2 = 0; n2 < n3; ++n2) {
            this.capturePoints.add((Object)new ConquestCapturePoint((ConquestPlayerManager)this.playerManager, "cp" + n2, root));
        }
        this.field_3360.clear();
        n2 = root.getInteger("apSize");
        for (n = 0; n < n2; ++n) {
            AmmoPoint ammoPoint = AmmoPoint.newFromFDS("ap" + n, root);
            this.field_3360.add((Object)ammoPoint);
        }
        this.vehicleSpawns.clear();
        n = root.getInteger("vpSize");
        for (int i = 0; i < n; ++i) {
            VehicleSpawn vehicleSpawn = VehicleSpawn.method_3191("vp" + i, root);
            this.vehicleSpawns.add((Object)vehicleSpawn);
        }
        this.field_3356 = new ConquestBoundary();
        this.field_3356.readFromFDS(root);
    }

    public void method_3273() {
        this.capturePoints.forEach(AbstractCapturePoint::reset);
    }

    @Nullable
    public ConquestCapturePoint method_3269(@NotNull Level level, @NotNull Player player, @NotNull Set<UUID> set) {
        for (ConquestCapturePoint conquestCapturePoint : this.capturePoints) {
            if (!conquestCapturePoint.method_3024(level, this.method_3398(conquestCapturePoint), set).contains(player)) continue;
            return conquestCapturePoint;
        }
        return null;
    }

    @Nullable
    public ConquestCapturePoint method_5510(@NotNull BotEntity botEntity, @NotNull GameTeam gameTeam) {
        Vec3 vec3 = botEntity.position();
        ConquestCapturePoint conquestCapturePoint = null;
        for (ConquestCapturePoint conquestCapturePoint2 : this.capturePoints) {
            if (!conquestCapturePoint2.method_5506((LivingEntity)botEntity) || conquestCapturePoint != null && !(conquestCapturePoint2.position.distanceTo(vec3) < conquestCapturePoint.position.distanceTo(vec3)) || gameTeam.equals(conquestCapturePoint2.getCbTeam())) continue;
            conquestCapturePoint = conquestCapturePoint2;
        }
        return conquestCapturePoint;
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
    public CloudAchievement getVictoryAchievement() {
        return null;
    }

    @Override
    public void getErrorMessages(@NotNull List<MutableComponent> messages) {
        if (this.vehicleSpawns.isEmpty()) {
            messages.add(Component.literal((String)("Vehicle points in game '" + this.name + "' are missing.")));
        }
        for (ConquestCapturePoint conquestCapturePoint : this.capturePoints) {
            if (!conquestCapturePoint.method_3288().isEmpty()) continue;
            messages.add(Component.literal((String)("Capture point '" + conquestCapturePoint.name + "' in game '" + this.name + "' is missing spawn points.")));
        }
        if (this.field_3356.method_3132()) {
            messages.add(Component.literal((String)("Boundary in game '" + this.name + "' is empty.")));
        }
        super.getErrorMessages(messages);
    }

    @Override
    public void method_3394(@NotNull ServerLevel serverLevel) {
        int n;
        BFUtils.discardMatchEntities(serverLevel, this, this.playerManager);
        GameTeam gameTeam = ((ConquestPlayerManager)this.playerManager).getTeamByName("Axis");
        GameTeam gameTeam2 = ((ConquestPlayerManager)this.playerManager).getTeamByName("Allies");
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
            gameTeam = ((ConquestPlayerManager)this.playerManager).getTeamByName("Axis");
            object2 = ((ConquestPlayerManager)this.playerManager).getTeamByName("Allies");
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
        if ((object2 = damageSource.getEntity()) instanceof ServerPlayer && (object = ((ConquestPlayerManager)this.playerManager).getPlayerTeam((UUID)(object2 = (gameTeam = (ServerPlayer)object2).getUUID()))) != null) {
            BFUtils.incrementPlayerStat(bFAbstractManager, this, (UUID)object2, BFStats.SCORE);
            for (ConquestCapturePoint conquestCapturePoint : this.capturePoints) {
                if (!conquestCapturePoint.isInRadius((Entity)botEntity, this.method_3398(conquestCapturePoint)) || !((GameTeam)object).equals(conquestCapturePoint.getCbTeam())) continue;
                BFUtils.sendPopupMessage((ServerPlayer)gameTeam, new BFPopup(field_6365, 40));
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
        ConquestCapturePoint conquestCapturePoint = this.method_5510(botEntity, gameTeam);
        if (conquestCapturePoint != null) {
            return conquestCapturePoint.asBlockPos();
        }
        String string = gameTeam.isAllies() ? "Axis" : "Allies";
        GameTeam gameTeam2 = ((ConquestPlayerManager)this.playerManager).getTeamByName(string);
        if (gameTeam2 == null) {
            return null;
        }
        return gameTeam2.randomSpawn(this).asBlockPos();
    }

    @Override
    public boolean method_3390(@NotNull BotEntity botEntity, @NotNull DamageSource damageSource) {
        Object object = damageSource.getEntity();
        if (object instanceof BotEntity) {
            BotEntity botEntity2 = (BotEntity)object;
            return !((GameTeam)(object = botEntity2.getTeam())).equals(botEntity.getTeam());
        }
        object = damageSource.getEntity();
        if (object instanceof Player) {
            Player player = (Player)object;
            return (object = ((ConquestPlayerManager)this.playerManager).getPlayerTeam(player.getUUID())) != null && !((GameTeam)object).equals(botEntity.getTeam());
        }
        return false;
    }

    @Override
    public int method_3393(@NotNull GameTeam gameTeam) {
        return 16;
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
        EquipmentSlot equipmentSlot;
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
            equipmentSlot = serverPlayer.getEquipmentSlotForItem((ItemStack)vec3);
            serverPlayer.setItemSlot(equipmentSlot, (ItemStack)vec3);
        }
        abstractCrateGunBlockEntity.putItem(ItemStack.EMPTY);
        vec3 = serverPlayer.position();
        BFUtils.playSound(level, vec3, (SoundEvent)BFSounds.MATCH_CLASSES_CHANGE.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
        BFUtils.playSound(level, vec3, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.0f, 1.0f);
        BFUtils.triggerPlayerStat(bFAbstractManager, this, uUID, BFStats.SCORE, 2);
        equipmentSlot = Component.translatable((String)"bf.popup.message.crate").withStyle(ChatFormatting.YELLOW);
        BFUtils.sendPopupMessage(serverPlayer, new BFPopup((Component)equipmentSlot, 40));
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
        GameTeam gameTeam = ((ConquestPlayerManager)this.playerManager).getTeamByName("Axis");
        GameTeam gameTeam2 = ((ConquestPlayerManager)this.playerManager).getTeamByName("Allies");
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
            case MatchClass.CLASS_ASSAULT -> 9;
            case MatchClass.CLASS_SUPPORT, MatchClass.CLASS_MEDIC, MatchClass.CLASS_SNIPER -> 6;
            case MatchClass.CLASS_GUNNER, MatchClass.CLASS_SPECIALIST -> 3;
            case MatchClass.CLASS_ANTI_TANK -> 2;
            case MatchClass.CLASS_COMMANDER -> 1;
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
    public int method_3398(@NotNull ConquestCapturePoint conquestCapturePoint) {
        return 8;
    }

    @Override
    public void method_3397(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull Level level, @NotNull ConquestCapturePoint conquestCapturePoint, @NotNull String string, @NotNull String string2, @NotNull Set<UUID> set) {
        ConquestPlayerManager conquestPlayerManager = (ConquestPlayerManager)this.getPlayerManager();
        GameTeam gameTeam = conquestPlayerManager.getTeamByName(string);
        assert (gameTeam != null);
        Set<UUID> set2 = gameTeam.getPlayers();
        GameTeam gameTeam2 = conquestPlayerManager.getTeamByName(string2);
        BFUtils.playSound(set2, (SoundEvent)BFSounds.MATCH_GAMEMODE_DOM_POINT_CAPTURED.get(), SoundSource.MUSIC, 1.0f);
        BFUtils.playSound(level, conquestCapturePoint.position.x, conquestCapturePoint.position.y, conquestCapturePoint.position.z, (SoundEvent)BFSounds.MATCH_CAPTUREPOINT_CAPTURED.get(), SoundSource.NEUTRAL, 2.0f, 1.0f);
        if (gameTeam2 != null) {
            BFUtils.playSound(gameTeam2.getPlayers(), (SoundEvent)BFSounds.MATCH_GAMEMODE_DOM_POINT_LOST.get(), SoundSource.MUSIC, 1.0f);
        }
        DivisionData divisionData = gameTeam.getDivisionData(this);
        MutableComponent mutableComponent = Component.literal((String)conquestCapturePoint.name.toUpperCase(Locale.ROOT)).withStyle(ChatFormatting.GRAY);
        MutableComponent mutableComponent2 = Component.literal((String)divisionData.getCountry().getName()).withStyle(ChatFormatting.GRAY);
        MutableComponent mutableComponent3 = Component.translatable((String)"bf.message.gamemode.notification.point.captured.team", (Object[])new Object[]{mutableComponent2, mutableComponent}).withStyle(gameTeam.getStyleText());
        BFUtils.sendFancyMessage(gameTeam, BFUtils.OBJECTIVE_UP_PREFIX, (Component)mutableComponent3);
        for (GameTeam object2 : conquestPlayerManager.getTeams()) {
            if (object2.equals(gameTeam)) continue;
            BFUtils.sendFancyMessage(object2, BFUtils.OBJECTIVE_PREFIX, (Component)mutableComponent3);
        }
        List<Player> list = conquestCapturePoint.method_3024(level, this.method_3398(conquestCapturePoint), set);
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            Player player = (Player)iterator.next();
            UUID uUID = player.getUUID();
            BFUtils.triggerPlayerStat(bFAbstractManager, this, uUID, BFStats.SCORE, 3);
            BFUtils.incrementPlayerStat(bFAbstractManager, this, uUID, BFStats.CPOINTS);
        }
    }

    @Override
    public List<ConquestCapturePoint> getCapturePoints() {
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
        return 64.0f;
    }

    @Override
    public boolean shouldAnnounceFirstBlood(@NotNull ServerPlayer player) {
        return this.status == GameStatus.GAME;
    }

    @NotNull
    public ObjectList<AmmoPoint> method_3271() {
        return ObjectLists.unmodifiable(this.field_3360);
    }

    @NotNull
    public ObjectList<VehicleSpawn> getVehicleSpawns() {
        return ObjectLists.unmodifiable(this.vehicleSpawns);
    }
}

