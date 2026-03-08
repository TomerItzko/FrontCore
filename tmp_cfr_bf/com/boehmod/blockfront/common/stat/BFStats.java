/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.stat;

import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.cloud.packet.common.server.PacketPlayerDataIncreaseValue;
import com.boehmod.bflib.cloud.packet.common.server.PacketPlayerDataSetValue;
import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.cloud.common.AbstractConnectionManager;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.stat.BFAudibleStat;
import com.boehmod.blockfront.common.stat.BFStat;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFUtils;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BFStats {
    public static final int field_3816 = 2400;
    public static final int field_3817 = 150;
    public static final BFStat GAMES = new BFStat("games").method_3922(true);
    public static final BFStat PLAYED_INTRO = new BFStat("playedIntro");
    public static final BFStat SAID_GG = new BFStat("saidGG");
    public static final BFStat SPAWN_PRO = new BFStat("spawnPro");
    public static final BFStat PLAY_TIME = new BFStat("playTime");
    public static final BFStat CLASS = new BFStat("class");
    public static final BFStat CLASS_INDEX = new BFStat("classIndex");
    public static final BFStat CLASS_ALIVE = new BFStat("classAlive");
    public static final BFStat SCORE = new BFAudibleStat("score", "bf.popup.message.score", 40, ChatFormatting.AQUA);
    public static final BFStat ITEM_PICKED_UP = new BFStat("itemPickedUp");
    public static final BFStat FIRST_BLOOD = new BFStat("firstBlood").method_3922(true);
    public static final BFStat HEAD_SHOTS = new BFAudibleStat("headShots", "bf.popup.message.headshot", 40, ChatFormatting.GREEN, BFSounds.ITEM_GUN_BULLET_IMPACT_FLESH_HEAD).method_3922(true);
    public static final BFStat NO_SCOPES = new BFAudibleStat("noScopes", "bf.popup.message.noscope", 40, ChatFormatting.YELLOW).method_3922(true);
    public static final BFStat CPOINTS = new BFStat("cpoints");
    public static final BFStat HEALTIME = new BFStat("healtime");
    public static final BFStat POINTS = new BFStat("points");
    public static final BFStat MATCH_KARMA = new BFStat("matchKarma").method_3922(true);
    public static final BFStat SOUNDBOARD_SOUNDS = new BFStat("soundboardSounds");
    public static final BFStat ROUND_WINS = new BFStat("roundWins");
    public static final BFStat RADIO_DELAY = new BFStat("radioDelay");
    public static final BFStat TEAM_SWITCH = new BFStat("teamSwitch");
    public static final BFStat WAR_CRY = new BFStat("warCry");
    public static final BFStat WAR_CRY_COMMANDER = new BFStat("warCryCommander");
    public static final BFStat CAPTURE_POINT_SPAWN = new BFStat("capturePointSpawn");
    public static final BFStat GG_STAGE = new BFStat("ggStage");
    public static final BFStat KILLS = new BFAudibleStat("kills", "bf.popup.message.kills", 40, ChatFormatting.YELLOW, BFSounds.MATCH_POPUP_KILLS).method_3922(true).setTrigger((bFAbstractManager, bFStat, abstractGame, uUID, n, n2) -> BFStats.method_3924(bFAbstractManager, bFStat, abstractGame, uUID, n));
    public static final BFStat KILLS_STREAK = new BFStat("kills_streak").method_3922(true).setTrigger((bFAbstractManager, bFStat, abstractGame, uUID, n, n2) -> ((AbstractConnectionManager)bFAbstractManager.getConnectionManager()).sendPacket((IPacket)new PacketPlayerDataSetValue(uUID, bFStat.getKey(), n2)));
    public static final BFStat KILLS_VEHICLES = new BFAudibleStat("kills_vehicles", "bf.popup.message.kills.vehicle", 60, ChatFormatting.GREEN, BFSounds.MATCH_POPUP_KILLS).method_3922(true);
    public static final BFStat KILLS_INFECTED = new BFAudibleStat("kills_infected", "bf.popup.message.kills.infected", 40, ChatFormatting.YELLOW, BFSounds.MATCH_POPUP_KILLS).method_3922(true);
    public static final BFStat KILLS_INFECTED_STREAK = new BFStat("kills_infected_streak").method_3922(true);
    public static final BFStat KILLS_BOTS = new BFAudibleStat("kills_bots", "bf.popup.message.kills.bots", 40, ChatFormatting.GREEN, BFSounds.MATCH_POPUP_KILLS).method_3922(true);
    public static final BFStat KILLS_BOTS_STREAK = new BFStat("kills_bots_streak").method_3922(true);
    public static final BFStat KILLS_FIRE = new BFAudibleStat("kills_fire", "bf.popup.message.kills.fire", 40, ChatFormatting.GOLD, null).method_3922(true);
    public static final BFStat DEATHS = new BFAudibleStat("deaths", "bf.popup.message.deaths", 40, ChatFormatting.RED, BFSounds.MATCH_POPUP_KILLS).method_3922(true);
    public static final BFStat ASSISTS = new BFAudibleStat("assists", "bf.popup.message.assists", 40, ChatFormatting.AQUA, BFSounds.MATCH_POPUP_KILLS).method_3922(true).setTrigger((bFAbstractManager, bFStat, abstractGame, uUID, n, n2) -> BFStats.method_3924(bFAbstractManager, bFStat, abstractGame, uUID, n));
    public static final BFStat DEATHS_STREAK = new BFStat("deaths_streak").method_3922(true).setTrigger((bFAbstractManager, bFStat, abstractGame, uUID, n, n2) -> ((AbstractConnectionManager)bFAbstractManager.getConnectionManager()).sendPacket((IPacket)new PacketPlayerDataSetValue(uUID, bFStat.getKey(), n2)));
    public static final BFStat BACK_STABS = new BFAudibleStat("backStabs", "bf.popup.message.backstabs", 60, ChatFormatting.RED, BFSounds.MATCH_POPUP_KILLS).method_3922(true).setTrigger((bFAbstractManager, bFStat, abstractGame, uUID, n, n2) -> BFStats.method_3924(bFAbstractManager, bFStat, abstractGame, uUID, n));
    public static final BFStat INFECTED_ROUNDS_WON = new BFAudibleStat("infected_rounds_won", "bf.popup.message.infected.rounds.won", 60, ChatFormatting.RED, BFSounds.MATCH_POPUP_KILLS).method_3922(true);
    public static final BFStat INFECTED_MATCHES_WON = new BFAudibleStat("infected_matches_won", "bf.popup.message.infected.matches.won", 60, ChatFormatting.RED, BFSounds.MATCH_POPUP_KILLS).method_3922(true);
    public static final BFStat CHAR = new BFStat("char");
    public static final BFStat TROPHIES = new BFStat("trophies").method_3922(true);
    public static final String field_3807 = "Allies";
    public static final String field_3808 = "Axis";
    public static final Component NOTICE_PREFIX = Component.literal((String)String.valueOf('\ue004')).withColor(0xFFFFFF).append(" ");

    private static void method_3924(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull BFStat bFStat, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull UUID uUID, int n) {
        int n2;
        FDSTagCompound fDSTagCompound;
        ServerPlayer serverPlayer = BFUtils.getPlayerByUUID(uUID);
        if (serverPlayer == null) {
            return;
        }
        ItemStack itemStack = serverPlayer.getMainHandItem();
        FDSTagCompound fDSTagCompound2 = new FDSTagCompound("extra");
        if (!itemStack.isEmpty()) {
            fDSTagCompound = BuiltInRegistries.ITEM.getKey((Object)itemStack.getItem());
            String string = fDSTagCompound.toString();
            fDSTagCompound2.setString("item", string);
        }
        if ((n2 = (fDSTagCompound = abstractGame.getPlayerStatData(uUID)).getInteger(CLASS_ALIVE.getKey(), -1)) != -1) {
            fDSTagCompound2.setInteger("class", n2);
        }
        PacketPlayerDataIncreaseValue packetPlayerDataIncreaseValue = new PacketPlayerDataIncreaseValue(fDSTagCompound2, uUID, bFStat.getKey(), n);
        ((AbstractConnectionManager)bFAbstractManager.getConnectionManager()).sendPacket((IPacket)packetPlayerDataIncreaseValue);
    }
}

