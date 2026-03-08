/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.player.PlayerGroup
 *  com.boehmod.bflib.cloud.common.player.PunishmentType
 *  com.boehmod.bflib.cloud.common.player.status.OnlineStatus
 *  com.boehmod.bflib.cloud.common.player.status.PlayerStatus
 *  com.boehmod.bflib.common.util.ModLibraryMathHelper
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.resources.language.I18n
 *  net.minecraft.client.resources.sounds.SimpleSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.network.chat.Component
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.screen.profile;

import com.boehmod.bflib.cloud.common.player.PlayerGroup;
import com.boehmod.bflib.cloud.common.player.PunishmentType;
import com.boehmod.bflib.cloud.common.player.status.OnlineStatus;
import com.boehmod.bflib.cloud.common.player.status.PlayerStatus;
import com.boehmod.bflib.common.util.ModLibraryMathHelper;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.profile.ProfileScreen;
import com.boehmod.blockfront.client.settings.BFClientSettings;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.unnamed.BF_209;
import com.boehmod.blockfront.unnamed.BF_218;
import com.boehmod.blockfront.util.PlayerGroupUtils;
import com.boehmod.blockfront.util.StringUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import org.jetbrains.annotations.NotNull;

public final class ProfileStatsScreen
extends ProfileScreen {
    private static final Component field_1496 = Component.translatable((String)"bf.message.none").withStyle(ChatFormatting.GRAY);
    @NotNull
    private final BF_209<BF_218> field_1495 = new BF_209(0, 84, 235, 148, this);

    public ProfileStatsScreen(@NotNull Screen screen, @NotNull PlayerCloudData profile) {
        super(screen, profile, (Component)Component.translatable((String)"bf.screen.profile.stats", (Object[])new Object[]{profile.getUsername()}));
    }

    @Override
    public void renderBackground(@NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
        super.renderBackground(guiGraphics, n, n2, f);
        BFRendering.rectangleWithDarkShadow(guiGraphics, this.field_1495.method_559(), this.field_1495.method_560(), this.field_1495.method_558(), this.field_1495.height(), BFRendering.translucentBlack());
    }

    @Override
    public void method_774() {
        super.method_774();
        int n = this.width / 2 - 160;
        int n2 = 4;
        this.field_1495.method_551(n + 25 + 4);
        this.field_1495.method_552(60);
        this.field_1495.method_553(272);
        this.field_1495.method_554(this.height - 115);
        this.field_1495.method_556(true);
        this.method_764(this.field_1495);
        this.field_1495.method_960(Collections.singletonList("&cLoading Player Statistics..."));
        ObjectArrayList objectArrayList = new ObjectArrayList();
        double d = (double)this.playerCloudData.getKills() / (double)(this.playerCloudData.getDeaths() == 0 ? 1 : this.playerCloudData.getDeaths());
        double d2 = (double)this.playerCloudData.getHeadShots() / (double)(this.playerCloudData.getKills() == 0 ? 1 : this.playerCloudData.getKills());
        double d3 = ModLibraryMathHelper.round((double)d, (int)1);
        double d4 = ModLibraryMathHelper.round((double)d2, (int)1);
        Component component = field_1496;
        Optional<PlayerGroup> optional = this.playerCloudData.getGroup();
        if (optional.isPresent()) {
            component = PlayerGroupUtils.getComponent(optional.get());
        }
        if (BFClientSettings.PLAYER_SCORES.containsKey((Object)this.playerCloudData.getUUID())) {
            this.minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)SoundEvents.PLAYER_LEVELUP, (float)1.0f));
            objectArrayList.add("");
            objectArrayList.add(String.valueOf(ChatFormatting.YELLOW) + String.valueOf(ChatFormatting.BOLD) + I18n.get((String)"bf.message.profile.details.scoreboard.featured", (Object[])new Object[0]));
            objectArrayList.add(String.valueOf(ChatFormatting.YELLOW) + I18n.get((String)"bf.message.profile.details.scoreboard.featured.message", (Object[])new Object[0]));
        }
        objectArrayList.add("");
        objectArrayList.add(String.valueOf(ChatFormatting.GRAY) + this.playerCloudData.getUsername() + " - " + I18n.get((String)"bf.message.profile.details", (Object[])new Object[0]));
        if (this.playerCloudData.hasActivePunishment(PunishmentType.BAN_CLOUD)) {
            objectArrayList.add("");
            objectArrayList.add(String.valueOf(ChatFormatting.RED) + I18n.get((String)"bf.profile.main.banned", (Object[])new Object[0]));
            objectArrayList.add(String.valueOf(ChatFormatting.GRAY) + I18n.get((String)"bf.profile.main.banned.info", (Object[])new Object[0]));
        }
        PlayerStatus playerStatus = this.playerCloudData.getStatus();
        OnlineStatus onlineStatus = playerStatus.getOnlineStatus();
        objectArrayList.add("");
        objectArrayList.add(onlineStatus == OnlineStatus.ONLINE ? String.valueOf(ChatFormatting.GREEN) + String.valueOf(ChatFormatting.BOLD) + I18n.get((String)"bf.message.online", (Object[])new Object[0]) : String.valueOf(ChatFormatting.RED) + String.valueOf(ChatFormatting.BOLD) + I18n.get((String)"bf.message.offline", (Object[])new Object[0]));
        objectArrayList.add("");
        objectArrayList.add(String.valueOf(ChatFormatting.WHITE) + I18n.get((String)"bf.message.profile.details.boosters", (Object[])new Object[]{String.valueOf(ChatFormatting.GREEN) + this.playerCloudData.getActiveBoosters().size()}));
        objectArrayList.add(String.valueOf(ChatFormatting.WHITE) + I18n.get((String)"bf.message.profile.details.group", (Object[])new Object[]{component.getString()}));
        objectArrayList.add(String.valueOf(ChatFormatting.WHITE) + I18n.get((String)"bf.message.profile.details.karma", (Object[])new Object[]{String.valueOf(ChatFormatting.LIGHT_PURPLE) + StringUtils.formatLong(this.playerCloudData.getMatchKarma())}));
        objectArrayList.add(String.valueOf(ChatFormatting.WHITE) + I18n.get((String)"bf.message.profile.details.time", (Object[])new Object[]{String.valueOf(ChatFormatting.GRAY) + ModLibraryMathHelper.round((double)(this.playerCloudData.getMinutesPlayed() / 60.0), (int)1) + "h"}));
        objectArrayList.add(String.valueOf(ChatFormatting.WHITE) + I18n.get((String)"bf.message.profile.details.games", (Object[])new Object[]{String.valueOf(ChatFormatting.GRAY) + StringUtils.formatLong(this.playerCloudData.getTotalGames())}));
        objectArrayList.add("");
        objectArrayList.add(String.valueOf(ChatFormatting.WHITE) + I18n.get((String)"bf.message.profile.details.exp", (Object[])new Object[]{String.valueOf(ChatFormatting.AQUA) + StringUtils.formatLong(this.playerCloudData.getExp())}));
        objectArrayList.add(String.valueOf(ChatFormatting.WHITE) + I18n.get((String)"bf.message.profile.details.kd", (Object[])new Object[]{String.valueOf(d3 == 0.0 ? ChatFormatting.GRAY : (d3 > 1.0 ? ChatFormatting.GREEN : ChatFormatting.RED)) + d3}));
        objectArrayList.add(String.valueOf(ChatFormatting.WHITE) + I18n.get((String)"bf.message.profile.details.headshots", (Object[])new Object[]{String.valueOf(ChatFormatting.GREEN) + StringUtils.formatLong(this.playerCloudData.getHeadShots())}));
        objectArrayList.add(String.valueOf(ChatFormatting.WHITE) + I18n.get((String)"bf.message.profile.details.headshots.ratio", (Object[])new Object[]{String.valueOf(d4 == 0.0 ? ChatFormatting.GRAY : (d4 > 1.0 ? ChatFormatting.GREEN : ChatFormatting.RED)) + d4}));
        objectArrayList.add(String.valueOf(ChatFormatting.WHITE) + I18n.get((String)"bf.message.profile.details.kills", (Object[])new Object[]{String.valueOf(ChatFormatting.GREEN) + StringUtils.formatLong(this.playerCloudData.getKills())}));
        objectArrayList.add(String.valueOf(ChatFormatting.WHITE) + I18n.get((String)"bf.message.profile.details.kills.streak", (Object[])new Object[]{String.valueOf(ChatFormatting.GREEN) + StringUtils.formatLong(this.playerCloudData.getKillStreak())}));
        objectArrayList.add(String.valueOf(ChatFormatting.WHITE) + I18n.get((String)"bf.message.profile.details.kills.fire", (Object[])new Object[]{String.valueOf(ChatFormatting.GREEN) + StringUtils.formatLong(this.playerCloudData.getFireKills())}));
        objectArrayList.add(String.valueOf(ChatFormatting.WHITE) + I18n.get((String)"bf.message.profile.details.backstabs", (Object[])new Object[]{String.valueOf(ChatFormatting.GREEN) + StringUtils.formatLong(this.playerCloudData.getBackStabs())}));
        objectArrayList.add(String.valueOf(ChatFormatting.WHITE) + I18n.get((String)"bf.message.profile.details.assists", (Object[])new Object[]{String.valueOf(ChatFormatting.YELLOW) + StringUtils.formatLong(this.playerCloudData.getAssists())}));
        objectArrayList.add(String.valueOf(ChatFormatting.WHITE) + I18n.get((String)"bf.message.profile.details.noscopes", (Object[])new Object[]{String.valueOf(ChatFormatting.GREEN) + StringUtils.formatLong(this.playerCloudData.getNoScopes())}));
        objectArrayList.add(String.valueOf(ChatFormatting.WHITE) + I18n.get((String)"bf.message.profile.details.deaths", (Object[])new Object[]{String.valueOf(ChatFormatting.RED) + StringUtils.formatLong(this.playerCloudData.getDeaths())}));
        objectArrayList.add(String.valueOf(ChatFormatting.WHITE) + I18n.get((String)"bf.message.profile.details.deaths.streak", (Object[])new Object[]{String.valueOf(ChatFormatting.RED) + StringUtils.formatLong(this.playerCloudData.getDeathStreak())}));
        objectArrayList.add(String.valueOf(ChatFormatting.WHITE) + I18n.get((String)"bf.message.profile.details.fb", (Object[])new Object[]{String.valueOf(ChatFormatting.DARK_RED) + StringUtils.formatLong(this.playerCloudData.getFirstBloods())}));
        objectArrayList.add(String.valueOf(ChatFormatting.WHITE) + I18n.get((String)"bf.message.profile.details.prestige", (Object[])new Object[]{String.valueOf(ChatFormatting.YELLOW) + StringUtils.formatLong(this.playerCloudData.getPrestigeLevel())}));
        objectArrayList.add(String.valueOf(ChatFormatting.WHITE) + I18n.get((String)"bf.message.profile.details.bootcamp", (Object[])new Object[]{this.playerCloudData.hasCompletedBootcamp() ? String.valueOf(ChatFormatting.GREEN) + "Yes" : String.valueOf(ChatFormatting.RED) + "No"}));
        objectArrayList.add("");
        objectArrayList.add(String.valueOf(ChatFormatting.WHITE) + I18n.get((String)"bf.message.profile.details.infected.rounds.won", (Object[])new Object[]{String.valueOf(ChatFormatting.RED) + StringUtils.formatLong(this.playerCloudData.getInfectedRoundsWon())}));
        objectArrayList.add(String.valueOf(ChatFormatting.WHITE) + I18n.get((String)"bf.message.profile.details.infected.matches.won", (Object[])new Object[]{String.valueOf(ChatFormatting.RED) + StringUtils.formatLong(this.playerCloudData.getInfectedMatchesWon())}));
        objectArrayList.add("");
        objectArrayList.add(String.valueOf(ChatFormatting.WHITE) + I18n.get((String)"bf.message.profile.details.kills.bots", (Object[])new Object[]{String.valueOf(ChatFormatting.BLUE) + StringUtils.formatLong(this.playerCloudData.getBotKills())}));
        objectArrayList.add(String.valueOf(ChatFormatting.WHITE) + I18n.get((String)"bf.message.profile.details.kills.infected", (Object[])new Object[]{String.valueOf(ChatFormatting.GREEN) + StringUtils.formatLong(this.playerCloudData.getInfectedKills())}));
        objectArrayList.add(String.valueOf(ChatFormatting.WHITE) + I18n.get((String)"bf.message.profile.details.kills.vehicles", (Object[])new Object[]{String.valueOf(ChatFormatting.GRAY) + StringUtils.formatLong(this.playerCloudData.getVehicleKills())}));
        objectArrayList.add("");
        objectArrayList.add(String.valueOf(ChatFormatting.RED) + I18n.get((String)"bf.message.profile.details.punishments", (Object[])new Object[0]));
        objectArrayList.add(String.valueOf(ChatFormatting.WHITE) + I18n.get((String)"bf.message.profile.details.punishments.warnings", (Object[])new Object[]{String.valueOf(ChatFormatting.RED) + this.playerCloudData.getPunishmentCount(PunishmentType.WARNING)}));
        objectArrayList.add(String.valueOf(ChatFormatting.WHITE) + I18n.get((String)"bf.message.profile.details.punishments.mutes", (Object[])new Object[]{String.valueOf(ChatFormatting.RED) + this.playerCloudData.getPunishmentCount(PunishmentType.MUTE)}));
        objectArrayList.add(String.valueOf(ChatFormatting.WHITE) + I18n.get((String)"bf.message.profile.details.punishments.bans.ac", (Object[])new Object[]{String.valueOf(ChatFormatting.RED) + this.playerCloudData.getPunishmentCount(PunishmentType.BAN_MM)}));
        objectArrayList.add(String.valueOf(ChatFormatting.WHITE) + I18n.get((String)"bf.message.profile.details.punishments.bans.cc", (Object[])new Object[]{String.valueOf(ChatFormatting.RED) + this.playerCloudData.getPunishmentCount(PunishmentType.BAN_MM)}));
        objectArrayList.add("");
        this.field_1495.method_960((List<String>)objectArrayList);
    }
}

