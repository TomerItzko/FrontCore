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
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game.impl.boot;

import com.boehmod.bflib.cloud.common.player.achievement.CloudAchievement;
import com.boehmod.bflib.cloud.common.player.achievement.CloudAchievements;
import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.entity.HumanEntity;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGameStage;
import com.boehmod.blockfront.game.GameSequence;
import com.boehmod.blockfront.game.GameStageManager;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.TeamJoinType;
import com.boehmod.blockfront.game.impl.boot.BootcampGameClient;
import com.boehmod.blockfront.game.impl.boot.BootcampIdleStage;
import com.boehmod.blockfront.game.impl.boot.BootcampNpcSpawn;
import com.boehmod.blockfront.game.impl.boot.BootcampPlayerManager;
import com.boehmod.blockfront.game.impl.boot.BootcampSoundPoint;
import com.boehmod.blockfront.game.impl.boot.BootcampTexturePoint;
import com.boehmod.blockfront.game.impl.boot.BootcampWeaponPoint;
import com.boehmod.blockfront.game.tag.IAllowsRegeneration;
import com.boehmod.blockfront.registry.BFEntityTypes;
import com.boehmod.blockfront.util.BFLog;
import com.boehmod.blockfront.util.math.FDSPose;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.io.IOException;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public final class BootcampGame
extends AbstractGame<BootcampGame, BootcampPlayerManager, GameStageManager<BootcampGame, BootcampPlayerManager>>
implements IAllowsRegeneration {
    @NotNull
    private final List<BootcampNpcSpawn> npcSpawns = new ObjectArrayList();
    @NotNull
    private final List<BootcampSoundPoint> soundPoints = new ObjectArrayList();
    @NotNull
    private final List<BootcampWeaponPoint> weaponPoints = new ObjectArrayList();
    @NotNull
    private final List<BootcampTexturePoint> texturePoints = new ObjectArrayList();
    @NotNull
    private FDSPose finishPoint = new FDSPose(0.0, 0.0, 0.0, 0.0f, 0.0f);
    @Nullable
    private GameSequence introSequence = null;
    private boolean spawnedNpcs = false;

    public BootcampGame(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager) {
        super(bFAbstractManager, "boot", "Bootcamp");
    }

    @NotNull
    public BootcampGameClient createGameClient(@NotNull BFClientManager bFClientManager) {
        return new BootcampGameClient(bFClientManager, this, (ClientPlayerDataHandler)this.dataHandler);
    }

    @Override
    @NotNull
    protected BootcampPlayerManager createPlayerManager() {
        return new BootcampPlayerManager(this, this.dataHandler);
    }

    @NotNull
    public FDSPose getFinishPoint() {
        return this.finishPoint;
    }

    public void spawnNpcs(@NotNull ServerLevel level) {
        if (this.spawnedNpcs) {
            return;
        }
        this.spawnedNpcs = true;
        this.npcSpawns.forEach(npcSpawn -> {
            level.getChunkAt(npcSpawn.asBlockPos());
            HumanEntity humanEntity = (HumanEntity)((EntityType)BFEntityTypes.HUMAN.get()).create((Level)level);
            if (humanEntity != null) {
                humanEntity.moveTo(npcSpawn.position, 0.0f, 0.0f);
                humanEntity.setGame(this);
                humanEntity.method_1969((Level)level, (BootcampNpcSpawn)npcSpawn);
                humanEntity.method_1970(npcSpawn.field_3246);
                level.addFreshEntity((Entity)humanEntity);
            }
        });
    }

    @Override
    @NotNull
    public AbstractGameStage<BootcampGame, BootcampPlayerManager> createFirstStage() {
        return new BootcampIdleStage();
    }

    @Override
    public void getErrorMessages(@NotNull List<MutableComponent> messages) {
        if (this.introSequence == null) {
            messages.add(Component.literal((String)("Intro sequence in game '" + this.name + "' is missing.")));
        }
        super.getErrorMessages(messages);
    }

    @Override
    @NotNull
    public Set<AbstractGame.ErrorType> getErrorTypes() {
        return EnumSet.of(AbstractGame.ErrorType.MAP_TYPE, AbstractGame.ErrorType.LOBBY);
    }

    @Override
    public void updateStageManager(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull PlayerDataHandler<?> dataHandler, @NotNull ServerLevel level, @NotNull Set<UUID> players) {
        this.stageManager.update(manager, dataHandler, (BootcampPlayerManager)this.playerManager, level, players);
    }

    @Override
    protected boolean isMatchSuccess() {
        return false;
    }

    @Override
    public boolean playerJoinTeam(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull TeamJoinType joinType, @NotNull ServerLevel level, @NotNull ServerPlayer player) {
        GameTeam gameTeam = ((BootcampPlayerManager)this.playerManager).getTeamByName("Recruits");
        if (gameTeam == null) {
            BFLog.log("No team available for player " + player.getScoreboardName() + " to join.", new Object[0]);
            return false;
        }
        return ((BootcampPlayerManager)this.playerManager).playerJoinTeam(manager, joinType, level, player, gameTeam);
    }

    @Override
    public void specificReset(@Nullable Level level) {
        this.spawnedNpcs = false;
    }

    @Override
    public void writeAll(@NotNull ByteBuf buf, boolean writeMap) throws IOException {
        super.writeAll(buf, writeMap);
        buf.writeInt(this.texturePoints.size());
        for (BootcampTexturePoint bootcampTexturePoint : this.texturePoints) {
            bootcampTexturePoint.write(buf);
        }
    }

    @Override
    public void readAll(@NotNull ByteBuf buf) throws IOException {
        super.readAll(buf);
        this.texturePoints.clear();
        int n = buf.readInt();
        for (int i = 0; i < n; ++i) {
            BootcampTexturePoint bootcampTexturePoint = new BootcampTexturePoint();
            bootcampTexturePoint.read(buf);
            this.texturePoints.add(bootcampTexturePoint);
        }
    }

    @Override
    public void writeSpecificFDS(@NotNull FDSTagCompound root) {
        int n;
        int n2;
        int n3;
        this.finishPoint.writeNamedFDS("finishPoint", root);
        int n4 = this.npcSpawns.size();
        root.setInteger("npcSpawnCount", n4);
        for (n3 = 0; n3 < n4; ++n3) {
            this.npcSpawns.get(n3).writeNamedFDS("npcSpawn" + n3, root);
        }
        n3 = this.soundPoints.size();
        root.setInteger("soundPointCount", n3);
        for (n2 = 0; n2 < n3; ++n2) {
            this.soundPoints.get(n2).writeNamedFDS("soundPoint" + n2, root);
        }
        n2 = this.weaponPoints.size();
        root.setInteger("weaponPointCount", n2);
        for (n = 0; n < n2; ++n) {
            this.weaponPoints.get(n).writeNamedFDS("weaponPoint" + n, root);
        }
        n = this.texturePoints.size();
        root.setInteger("texturePointCount", n);
        for (int i = 0; i < n; ++i) {
            this.texturePoints.get(i).writeNamedFDS("texturePoint" + i, root);
        }
        root.setBoolean("hasIntroSequence", this.introSequence != null);
        if (this.introSequence != null) {
            FDSTagCompound fDSTagCompound = new FDSTagCompound("introSequence");
            this.introSequence.writeToFDS(fDSTagCompound);
            root.setTagCompound("introSequence", fDSTagCompound);
        }
    }

    @Override
    public void readSpecificFDS(@NotNull FDSTagCompound root) {
        FDSTagCompound fDSTagCompound;
        int n;
        int n2;
        int n3;
        int n4;
        this.finishPoint = FDSPose.readNamedFDS("finishPoint", root);
        int n5 = root.getInteger("npcSpawnCount");
        for (n4 = 0; n4 < n5; ++n4) {
            this.npcSpawns.add(BootcampNpcSpawn.method_3151("npcSpawn" + n4, root));
        }
        n4 = root.getInteger("soundPointCount");
        for (n3 = 0; n3 < n4; ++n3) {
            this.soundPoints.add(BootcampSoundPoint.method_3184("soundPoint" + n3, root));
        }
        n3 = root.getInteger("weaponPointCount");
        for (n2 = 0; n2 < n3; ++n2) {
            this.weaponPoints.add(BootcampWeaponPoint.method_3194("weaponPoint" + n2, root));
        }
        n2 = root.getInteger("texturePointCount");
        for (n = 0; n < n2; ++n) {
            this.texturePoints.add(BootcampTexturePoint.method_3186("texturePoint" + n, root));
        }
        n = root.getBoolean("hasIntroSequence", false) ? 1 : 0;
        if (n != 0 && (fDSTagCompound = root.getTagCompound("introSequence")) != null) {
            this.introSequence = new GameSequence();
            this.introSequence.readFromFDS(fDSTagCompound);
        }
    }

    @Nullable
    public GameSequence getIntroSequence() {
        return this.introSequence;
    }

    public void setIntroSequence(@Nullable GameSequence sequence) {
        this.introSequence = sequence;
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
        return CloudAchievements.ACH_USER_BOOTCAMP;
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
        return 20;
    }

    @NotNull
    public List<BootcampTexturePoint> method_3252() {
        return Collections.unmodifiableList(this.texturePoints);
    }

    @NotNull
    public List<BootcampSoundPoint> getSoundPoints() {
        return Collections.unmodifiableList(this.soundPoints);
    }

    @NotNull
    public List<BootcampWeaponPoint> getWeaponPoints() {
        return Collections.unmodifiableList(this.weaponPoints);
    }
}

