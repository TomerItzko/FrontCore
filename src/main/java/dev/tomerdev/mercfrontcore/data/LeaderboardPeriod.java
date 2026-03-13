package dev.tomerdev.mercfrontcore.data;

import net.minecraft.text.Text;

public enum LeaderboardPeriod {
    WEEKLY("weekly", "Weekly"),
    MONTHLY("monthly", "Monthly"),
    ALL_TIME("all_time", "All Time");

    private final String id;
    private final String displayName;

    LeaderboardPeriod(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public String id() {
        return id;
    }

    public Text displayText() {
        return Text.literal(displayName);
    }

    public static LeaderboardPeriod fromId(String id) {
        for (LeaderboardPeriod value : values()) {
            if (value.id.equalsIgnoreCase(id)) {
                return value;
            }
        }
        if ("all".equalsIgnoreCase(id) || "alltime".equalsIgnoreCase(id)) {
            return ALL_TIME;
        }
        return ALL_TIME;
    }
}
