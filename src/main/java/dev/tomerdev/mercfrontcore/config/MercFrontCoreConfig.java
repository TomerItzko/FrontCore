package dev.tomerdev.mercfrontcore.config;

public final class MercFrontCoreConfig {
    public ProxySettings proxy = new ProxySettings();
    public AudioSettings audio = new AudioSettings();
    public RewardSettings rewards = new RewardSettings();
    public ExperienceSettings experience = new ExperienceSettings();
    public ClassRankSettings classRanks = new ClassRankSettings();
    public LeaveMatchSettings leaveMatch = new LeaveMatchSettings();

    public static final class ProxySettings {
        public boolean enableProxyCompatibility = true;
        public boolean enforceDirectConnection = false;
        public boolean trustForwardedIdentity = true;
    }

    public static final class AudioSettings {
        public float gunSfxVolume = 1.0f;
        public float grenadeSfxVolume = 1.0f;
    }

    public static final class RewardSettings {
        public boolean enableWinnerSkinDrops = true;
        public float winnerSkinDropChance = 0.25f;
    }

    public static final class ExperienceSettings {
        public boolean enableNativeMatchXp = true;
        public int deathXp = 1;
        public int assistXp = 1;
        public int botKillXp = 2;
        public int playerKillXp = 5;
        public int fireKillXp = 6;
        public int vehicleKillXp = 10;
        public int headshotXp = 5;
        public int noScopeXp = 6;
        public int backStabXp = 8;
        public int firstBloodXp = 15;
        public int participationXp = 50;
        public int victoryXp = 100;
        public int infectedRoundWinXp = 50;
        public int infectedMatchWinXp = 1200;
        public int classPlayerKillXp = 5;
        public int classAssistXp = 2;
    }

    public static final class ClassRankSettings {
        public String rifleman = "RECRUIT";
        public String lightInfantry = "RECRUIT";
        public String assault = "RECRUIT";
        public String support = "PRIVATE_SECOND_CLASS";
        public String medic = "CORPORAL";
        public String sniper = "SERGEANT_FIRST_CLASS";
        public String gunner = "FIRST_SERGEANT";
        public String antiTank = "CAPTAIN";
        public String specialist = "LIEUTENANT_COLONEL";
        public String commander = "BRIGADIER_GENERAL";
    }

    public static final class LeaveMatchSettings {
        public double x = 0.5;
        public double y = 100.0;
        public double z = 0.5;
        public float yaw = 0.0f;
        public float pitch = 0.0f;
    }
}
