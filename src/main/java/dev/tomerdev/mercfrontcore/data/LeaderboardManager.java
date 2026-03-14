package dev.tomerdev.mercfrontcore.data;

import com.boehmod.bflib.cloud.common.player.PlayerRank;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import dev.tomerdev.mercfrontcore.net.packet.LeaderboardResponsePacket;
import net.minecraft.server.MinecraftServer;

public final class LeaderboardManager {
    private static final long WEEKLY_SECONDS = 7L * 24L * 60L * 60L;
    private static final long MONTHLY_SECONDS = 30L * 24L * 60L * 60L;

    private LeaderboardManager() {
    }

    public static List<LeaderboardResponsePacket.Entry> build(MinecraftServer server, LeaderboardPeriod period) {
        long now = Instant.now().getEpochSecond();
        List<LeaderboardResponsePacket.Entry> entries = new ArrayList<>();
        for (PlayerXpStore.StoredPlayerXp stored : PlayerXpStore.getInstance().readAllPlayers(server)) {
            int value = switch (period) {
                case ALL_TIME -> stored.data().exp();
                case WEEKLY -> sumHistory(stored.data().xpHistory(), now - WEEKLY_SECONDS);
                case MONTHLY -> sumHistory(stored.data().xpHistory(), now - MONTHLY_SECONDS);
            };
            if (value <= 0) {
                continue;
            }
            String username = stored.data().username() == null || stored.data().username().isBlank()
                ? stored.uuid().toString()
                : stored.data().username();
            PlayerRank rank = PlayerRank.getRankFromEXP(stored.data().exp());
            entries.add(new LeaderboardResponsePacket.Entry(username, stored.uuid(), value, rank.getTexture()));
        }
        entries.sort(
            Comparator.comparingInt(LeaderboardResponsePacket.Entry::xp).reversed()
                .thenComparing(LeaderboardResponsePacket.Entry::username, String.CASE_INSENSITIVE_ORDER)
        );
        return List.copyOf(entries);
    }

    private static int sumHistory(List<PlayerXpStore.XpGainEntry> history, long cutoff) {
        int sum = 0;
        for (PlayerXpStore.XpGainEntry entry : history) {
            if (entry.timestamp() >= cutoff) {
                sum += entry.xp();
            }
        }
        return Math.max(0, sum);
    }
}
