package dev.tomerdev.mercfrontcore.data;

import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.WinningTeamData;
import com.boehmod.blockfront.server.player.ServerPlayerDataHandler;
import com.boehmod.blockfront.common.match.MatchClass;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.server.network.ServerPlayerEntity;
import dev.tomerdev.mercfrontcore.MercFrontCore;
import dev.tomerdev.mercfrontcore.config.MercFrontCoreConfig;
import dev.tomerdev.mercfrontcore.config.MercFrontCoreConfigManager;

public final class MatchXpManager {
    private MatchXpManager() {
    }

    public static MatchXpAwardResult awardMatchXp(
        AbstractGame<?, ?, ?> game,
        ServerPlayerDataHandler dataHandler,
        ServerPlayerEntity player,
        WinningTeamData winningTeam
    ) {
        MercFrontCoreConfig.ExperienceSettings config = MercFrontCoreConfigManager.get().experience;
        if (!config.enableNativeMatchXp) {
            return null;
        }

        UUID playerUuid = player.getUuid();
        if (AddonCommonData.getInstance().getProfileOverrides().containsKey(playerUuid)) {
            MercFrontCore.LOGGER.debug("Skipped native BF XP for {} because a profile override is active", player.getNameForScoreboard());
            return null;
        }

        FDSTagCompound stats = game.getPlayerStatData(playerUuid);
        boolean victory = isWinner(playerUuid, winningTeam);
        int gainedXp = calculateRankXp(stats, victory, config);
        int classGainedXp = calculateClassXp(stats, config);
        if (gainedXp <= 0 && classGainedXp <= 0) {
            return null;
        }

        PlayerCloudData attachedProfile = dataHandler.getCloudProfile(player);
        PlayerCloudData cachedProfile = dataHandler.getCloudProfile(playerUuid);
        int beforeXp = attachedProfile.getExp();
        int afterXp = beforeXp + gainedXp;

        if (gainedXp > 0) {
            attachedProfile.setExp(afterXp);
            if (cachedProfile != attachedProfile) {
                cachedProfile.setExp(afterXp);
            }
        }
        int classOrdinal = resolveAwardClass(stats);
        if (classOrdinal >= 0 && classGainedXp > 0) {
            attachedProfile.addEXPForClass(classOrdinal, classGainedXp);
            if (cachedProfile != attachedProfile) {
                cachedProfile.addEXPForClass(classOrdinal, classGainedXp);
            }
        }
        PlayerXpStore.getInstance().save(
            player.getServer(),
            playerUuid,
            player.getNameForScoreboard(),
            new PlayerXpStore.PlayerXpData(
                afterXp,
                attachedProfile.getPrestigeLevel(),
                snapshotClassExp(attachedProfile),
                player.getNameForScoreboard(),
                List.of()
            )
        );

        return new MatchXpAwardResult(beforeXp, afterXp, gainedXp, classGainedXp, victory);
    }

    private static int calculateRankXp(
        FDSTagCompound stats,
        boolean victory,
        MercFrontCoreConfig.ExperienceSettings config
    ) {
        int xp = config.participationXp;
        if (victory) {
            xp += config.victoryXp;
        }

        xp += stats.getInteger(BFStats.DEATHS.getKey(), 0) * config.deathXp;
        xp += stats.getInteger(BFStats.ASSISTS.getKey(), 0) * config.assistXp;
        xp += stats.getInteger(BFStats.KILLS_BOTS.getKey(), 0) * config.botKillXp;
        xp += stats.getInteger(BFStats.KILLS.getKey(), 0) * config.playerKillXp;
        xp += stats.getInteger(BFStats.KILLS_FIRE.getKey(), 0) * config.fireKillXp;
        xp += stats.getInteger(BFStats.KILLS_VEHICLES.getKey(), 0) * config.vehicleKillXp;
        xp += stats.getInteger(BFStats.HEAD_SHOTS.getKey(), 0) * config.headshotXp;
        xp += stats.getInteger(BFStats.NO_SCOPES.getKey(), 0) * config.noScopeXp;
        xp += stats.getInteger(BFStats.BACK_STABS.getKey(), 0) * config.backStabXp;
        xp += stats.getInteger(BFStats.FIRST_BLOOD.getKey(), 0) * config.firstBloodXp;
        xp += stats.getInteger(BFStats.INFECTED_ROUNDS_WON.getKey(), 0) * config.infectedRoundWinXp;
        xp += stats.getInteger(BFStats.INFECTED_MATCHES_WON.getKey(), 0) * config.infectedMatchWinXp;

        return Math.max(0, xp);
    }

    private static int calculateClassXp(
        FDSTagCompound stats,
        MercFrontCoreConfig.ExperienceSettings config
    ) {
        int xp = 0;
        xp += stats.getInteger(BFStats.KILLS.getKey(), 0) * config.classPlayerKillXp;
        xp += stats.getInteger(BFStats.ASSISTS.getKey(), 0) * config.classAssistXp;
        return Math.max(0, xp);
    }

    private static boolean isWinner(UUID playerUuid, WinningTeamData winningTeam) {
        if (winningTeam.team != null && winningTeam.team.getPlayers().contains(playerUuid)) {
            return true;
        }
        return winningTeam.topPlayers != null && winningTeam.topPlayers.contains(playerUuid);
    }

    public static List<Integer> snapshotClassExp(PlayerCloudData profile) {
        List<Integer> values = new ArrayList<>(MatchClass.values().length);
        for (int i = 0; i < MatchClass.values().length; i++) {
            values.add(Math.max(0, profile.getExpForClass(i)));
        }
        return List.copyOf(values);
    }

    private static int resolveAwardClass(FDSTagCompound stats) {
        int classAlive = stats.getInteger(BFStats.CLASS_ALIVE.getKey(), -1);
        if (classAlive >= 0 && classAlive < MatchClass.values().length) {
            return classAlive;
        }
        int selectedClass = stats.getInteger(BFStats.CLASS.getKey(), -1);
        if (selectedClass >= 0 && selectedClass < MatchClass.values().length) {
            return selectedClass;
        }
        return -1;
    }

    public record MatchXpAwardResult(int beforeXp, int afterXp, int gainedXp, int classGainedXp, boolean victory) {
    }
}
