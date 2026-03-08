/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.player.PlayerRank
 *  javax.annotation.Nullable
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.player.Player
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.match;

import com.boehmod.bflib.cloud.common.player.PlayerRank;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.util.BFRes;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public enum MatchClass {
    CLASS_RIFLEMAN("rifleman", "bf.message.gamemode.class.rifleman.title", "bf.message.gamemode.class.rifleman.desc", true),
    CLASS_LIGHT_INFANTRY("light_infantry", "bf.message.gamemode.class.light.infantry.title", "bf.message.gamemode.class.light.infantry.desc", true),
    CLASS_ASSAULT("assault", "bf.message.gamemode.class.assault.title", "bf.message.gamemode.class.assault.desc", true),
    CLASS_SUPPORT("support", "bf.message.gamemode.class.support.title", "bf.message.gamemode.class.support.desc", false, PlayerRank.PRIVATE_SECOND_CLASS),
    CLASS_MEDIC("medic", "bf.message.gamemode.class.medic.title", "bf.message.gamemode.class.medic.desc", true, PlayerRank.CORPORAL),
    CLASS_SNIPER("sniper", "bf.message.gamemode.class.sniper.title", "bf.message.gamemode.class.sniper.desc", true, PlayerRank.SERGEANT_FIRST_CLASS),
    CLASS_GUNNER("gunner", "bf.message.gamemode.class.gunner.title", "bf.message.gamemode.class.gunner.desc", false, PlayerRank.FIRST_SERGEANT),
    CLASS_ANTI_TANK("anti_tank", "bf.message.gamemode.class.anti.tank.title", "bf.message.gamemode.class.anti.tank.desc", false, PlayerRank.CAPTAIN),
    CLASS_SPECIALIST("specialist", "bf.message.gamemode.class.specialist.title", "bf.message.gamemode.class.specialist.desc", true, PlayerRank.LIEUTENANT_COLONEL),
    CLASS_COMMANDER("commander", "bf.message.gamemode.class.commander.title", "bf.message.gamemode.class.commander.desc", false, PlayerRank.BRIGADIER_GENERAL);

    @NotNull
    private final String key;
    @NotNull
    private final String displayTitle;
    @NotNull
    private final String description;
    private final boolean botFriendly;
    @NotNull
    private final ResourceLocation icon;
    @NotNull
    private PlayerRank minRankRequired = PlayerRank.RECRUIT;

    private MatchClass(@NotNull String key, String displayTitle, String description, boolean botFriendly) {
        this.key = key;
        this.displayTitle = displayTitle;
        this.description = description;
        this.botFriendly = botFriendly;
        this.icon = BFRes.loc("textures/gui/game/loadout/" + key + ".png");
    }

    private MatchClass(@NotNull String key, String displayTitle, @NotNull String description, boolean botFriendly, PlayerRank minRankRequired) {
        this(key, displayTitle, description, botFriendly);
        this.minRankRequired = minRankRequired;
    }

    @NotNull
    public String getKey() {
        return this.key;
    }

    @NotNull
    public String getDisplayTitle() {
        return this.displayTitle;
    }

    @NotNull
    public String getDescription() {
        return this.description;
    }

    @NotNull
    public ResourceLocation getIcon() {
        return this.icon;
    }

    public boolean isBotFriendly() {
        return this.botFriendly;
    }

    @NotNull
    public PlayerRank getMinRankRequired() {
        return this.minRankRequired;
    }

    public boolean canPlayerUse(@NotNull PlayerDataHandler<?> dataHandler, @NotNull Player player) {
        PlayerCloudData playerCloudData = dataHandler.getCloudProfile(player);
        PlayerRank playerRank = playerCloudData.getRank();
        return playerRank.getID() >= this.minRankRequired.getID() || playerCloudData.getPrestigeLevel() > 0;
    }

    public boolean isAllowed(@NotNull AbstractGame<?, ?, ?> game) {
        return !game.getBannedClasses().contains((Object)this);
    }

    @Nullable
    public static MatchClass fromIndex(int index) {
        if (index < 0 || index >= MatchClass.values().length) {
            return null;
        }
        return MatchClass.values()[index];
    }
}

