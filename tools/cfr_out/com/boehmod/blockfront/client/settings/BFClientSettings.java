/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.NotificationType
 *  it.unimi.dsi.fastutil.objects.Object2IntMap
 *  it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
 *  it.unimi.dsi.fastutil.objects.Object2ObjectMap
 *  it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.settings;

import com.boehmod.bflib.cloud.common.NotificationType;
import com.boehmod.blockfront.client.gui.BFCrosshair;
import com.boehmod.blockfront.client.settings.BFClientSetting;
import com.boehmod.blockfront.client.settings.BFClientSettingCategories;
import com.boehmod.blockfront.client.settings.BFClientSettingResource;
import com.boehmod.blockfront.client.settings.BFClientSettingSlider;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public class BFClientSettings {
    @NotNull
    public static final List<BFClientSetting> INSTANCES = new ObjectArrayList();
    @NotNull
    public static final Object2IntMap<UUID> PLAYER_SCORES = new Object2IntOpenHashMap();
    @NotNull
    public static final Object2IntMap<UUID> CLAN_SCORES = new Object2IntOpenHashMap();
    @NotNull
    public static final Object2IntMap<String> GAME_PLAYER_COUNT = new Object2IntOpenHashMap();
    @NotNull
    public static final Object2ObjectMap<NotificationType, BFClientSetting> NOTIFICATION_MAP = new Object2ObjectOpenHashMap();
    public static final BFClientSettingResource CROSSHAIR_NAME = new BFClientSettingResource("bf.settings.crosshair.name", "crosshair.name", BFCrosshair.DEFAULT.name(), BFClientSettingCategories.CROSSHAIR);
    public static final BFClientSetting CROSSHAIR_RENDER = new BFClientSetting("bf.settings.crosshair.render", "crosshair.render", BFClientSettingCategories.CROSSHAIR, true);
    public static final BFClientSetting CROSSHAIR_RENDER_AIMING = new BFClientSetting("bf.settings.crosshair.render.aiming", "crosshair.render.aiming", BFClientSettingCategories.CROSSHAIR, false);
    public static final BFClientSetting CROSSHAIR_DOT = new BFClientSetting("bf.settings.crosshair.dot", "crosshair.dot", BFClientSettingCategories.CROSSHAIR, true);
    public static final BFClientSetting CROSSHAIR_LINES = new BFClientSetting("bf.settings.crosshair.lines", "crosshair.lines", BFClientSettingCategories.CROSSHAIR, true);
    public static final BFClientSetting CROSSHAIR_STATIC = new BFClientSetting("bf.settings.crosshair.static", "crosshair.static", BFClientSettingCategories.CROSSHAIR, false);
    public static final BFClientSetting CROSSHAIR_HITMARKER = new BFClientSetting("bf.settings.crosshair.hitmarker", "crosshair.hitmarker", BFClientSettingCategories.CROSSHAIR, true);
    public static final BFClientSettingSlider CROSSHAIR_ALPHA = new BFClientSettingSlider("bf.settings.crosshair.alpha", "crosshair.alpha", BFClientSettingCategories.CROSSHAIR, 1.0f);
    public static final BFClientSettingSlider CROSSHAIR_AIM_SENS = new BFClientSettingSlider("bf.settings.crosshair.aim.sens", "crosshair.aim.sens", BFClientSettingCategories.CROSSHAIR, 0.5f);
    public static final BFClientSettingSlider CROSSHAIR_SCOPE_SENS = new BFClientSettingSlider("bf.settings.crosshair.scope.sens", "crosshair.scope.sens", BFClientSettingCategories.CROSSHAIR, 0.5f);
    public static final BFClientSetting CROSSHAIR_DYNAMIC = new BFClientSetting("bf.settings.crosshair.dynamic", "crosshair.dynamic", BFClientSettingCategories.CROSSHAIR, false);
    public static final BFClientSetting CROSSHAIR_SPREADFADE = new BFClientSetting("bf.settings.crosshair.spreadfade", "crosshair.spreadfade", BFClientSettingCategories.CROSSHAIR, true);
    public static final BFClientSetting UI_RENDER_HUD = new BFClientSetting("bf.settings.ui.render.hud", "ui.render.hud", BFClientSettingCategories.UI, true);
    public static final BFClientSetting UI_RENDER_WAYPOINTS = new BFClientSetting("bf.settings.ui.render.waypoints", "ui.render.waypoints", BFClientSettingCategories.UI, true);
    public static final BFClientSetting UI_CONSTANT_HOTBAR = new BFClientSetting("bf.settings.ui.constant.hotbar", "ui.constant.hotbar", BFClientSettingCategories.UI, false);
    public static final BFClientSetting UI_RENDER_GAME_MINIMAP = new BFClientSetting("bf.settings.ui.render.game.minimap", "ui.render.game.minimap", BFClientSettingCategories.UI, false);
    public static final BFClientSetting UI_RENDER_KILLFEED = new BFClientSetting("bf.settings.ui.render.killfeed", "ui.render.killfeed", BFClientSettingCategories.UI, true);
    public static final BFClientSetting UI_RENDER_DAMAGEINDICATORS = new BFClientSetting("bf.settings.ui.render.damageindicators", "ui.render.damageindicators", BFClientSettingCategories.UI, true);
    public static final BFClientSetting UI_RENDER_WEAPON_SILHOUETTE_TOGGLE = new BFClientSetting("bf.settings.ui.weapon.silhouette", "ui.render.weapon.silhouette.toggle", BFClientSettingCategories.UI, false);
    public static final BFClientSetting UI_RENDER_TAB_BACKGROUND = new BFClientSetting("bf.settings.ui.tab.background", "ui.render.tab.background", BFClientSettingCategories.UI, true);
    public static final BFClientSetting UI_RENDER_TAB_BLUR = new BFClientSetting("bf.settings.ui.tab.blur", "ui.render.tab.blur", BFClientSettingCategories.UI, true);
    public static final BFClientSetting UI_RENDER_TAB_DEATH_MOTION_SICKNESS = new BFClientSetting("bf.settings.ui.tab.death.motion.sickness", "ui.render.tab.death.motion.sickness", BFClientSettingCategories.UI, false);
    public static final BFClientSetting CONTENT_GORE = new BFClientSetting("bf.settings.ui.content.gore", "content.gore", BFClientSettingCategories.CONTENT, true);
    public static final BFClientSetting NOTIFICATIONS_FRIENDS_POKE = BFClientSettings.addNotification(NotificationType.FRIEND_POKE, new BFClientSetting("bf.settings.notifications.friend.pokes", "notifications.friends.poke", BFClientSettingCategories.NOTIFICATIONS, true));
    public static final BFClientSetting NOTIFICATIONS_FRIENDS_REQUESTS = BFClientSettings.addNotification(NotificationType.FRIEND_REQUESTS, new BFClientSetting("bf.settings.notifications.friend.requests", "notifications.friends.requests", BFClientSettingCategories.NOTIFICATIONS, true));
    public static final BFClientSetting NOTIFICATIONS_FRIENDS_LOGINS = BFClientSettings.addNotification(NotificationType.FRIEND_LOGINS, new BFClientSetting("bf.settings.notifications.friend.logins", "notifications.friends.logins", BFClientSettingCategories.NOTIFICATIONS, true));
    public static final BFClientSetting NOTIFICATIONS_FRIENDS_RANK_UPS = BFClientSettings.addNotification(NotificationType.FRIEND_RANK_UPS, new BFClientSetting("bf.settings.notifications.friend.rank.ups", "notifications.friends.rank.ups", BFClientSettingCategories.NOTIFICATIONS, true));
    public static final BFClientSetting NOTIFICATIONS_FRIENDS_PRESTIGE = BFClientSettings.addNotification(NotificationType.FRIEND_PRESTIGE, new BFClientSetting("bf.settings.notifications.friend.prestige", "notifications.friends.prestige", BFClientSettingCategories.NOTIFICATIONS, true));
    public static final BFClientSetting NOTIFICATIONS_RANK_UPS = BFClientSettings.addNotification(NotificationType.RANK_UPS, new BFClientSetting("bf.settings.notifications.rank.ups", "notifications.rank.ups", BFClientSettingCategories.NOTIFICATIONS, true));
    public static final BFClientSetting NOTIFICATIONS_ACHIEVEMENTS = BFClientSettings.addNotification(NotificationType.ACHIEVEMENTS, new BFClientSetting("bf.settings.notifications.achievements", "notifications.achievements", BFClientSettingCategories.NOTIFICATIONS, true));
    public static final BFClientSetting NOTIFICATIONS_DAILY_CHALLENGES = BFClientSettings.addNotification(NotificationType.DAILY_CHALLENGE, new BFClientSetting("bf.settings.notifications.daily.challenges", "notifications.daily.challenges", BFClientSettingCategories.NOTIFICATIONS, true));
    public static final BFClientSetting NOTIFICATIONS_PARTY_INVITES = BFClientSettings.addNotification(NotificationType.PARTY_INVITES, new BFClientSetting("bf.settings.notifications.party.invites", "notifications.party.invites", BFClientSettingCategories.NOTIFICATIONS, true));
    public static final BFClientSetting AUDIO_BOTS = new BFClientSetting("bf.settings.audio.bots", "audio.bots", BFClientSettingCategories.AUDIO, true);
    public static final BFClientSetting AUDIO_CAPTURE_POINT_ALARM = new BFClientSetting("bf.settings.audio.capture.point.alarm", "audio.capture.point.alarm", BFClientSettingCategories.AUDIO, false);
    public static final BFClientSetting EXPERIMENTAL_BULLET_HOLES = new BFClientSetting("bf.settings.experimental.bullet.holes", "experimental.bullet.holes", BFClientSettingCategories.EXPERIMENTAL, true);
    public static final BFClientSetting EXPERIMENTAL_TOGGLE_AIM = new BFClientSetting("bf.settings.experimental.toggle.aim", "experimental.toggle.aim", BFClientSettingCategories.EXPERIMENTAL, true);
    public static final BFClientSetting EXPERIMENTAL_TOGGLE_BOBBING = new BFClientSetting("bf.settings.experimental.toggle.bobbing", "experimental.toggle.bobbing", BFClientSettingCategories.EXPERIMENTAL, false);
    public static final BFClientSetting EXPERIMENTAL_TOGGLE_IMMERSIVE_BOBBING = new BFClientSetting("bf.settings.experimental.toggle.immersive.bobbing", "experimental.toggle.immersive.bobbing", BFClientSettingCategories.EXPERIMENTAL, false);
    public static final BFClientSettingSlider EXPERIMENTAL_IMMERSIVE_BOBBING_SCALE = new BFClientSettingSlider("bf.settings.experimental.immersive.bobbing.scale", "experimental.immersive.bobbing.scale", BFClientSettingCategories.EXPERIMENTAL, 0.5f);
    public static final BFClientSetting EXPERIMENTAL_TOGGLE_MUZZLEFLASH = new BFClientSetting("bf.settings.experimental.toggle.muzzleflash", "experimental.toggle.muzzleflash", BFClientSettingCategories.EXPERIMENTAL, true);
    public static final BFClientSetting EXPERIMENTAL_TOGGLE_NAMETAGS = new BFClientSetting("bf.settings.experimental.toggle.nametags", "experimental.toggle.nametags", BFClientSettingCategories.EXPERIMENTAL, true);
    public static final BFClientSetting EXPERIMENTAL_INSPECTION_BLUR = new BFClientSetting("bf.settings.experimental.inspection.blur", "experimental.inspection.blur", BFClientSettingCategories.EXPERIMENTAL, true);
    public static final BFClientSetting EXPERIMENTAL_BULLET_EJECT_PUFF = new BFClientSetting("bf.settings.experimental.bullet.eject.puff", "experimental.bullet.eject.puff", BFClientSettingCategories.EXPERIMENTAL, false);
    public static final BFClientSetting EXPERIMENTAL_TOGGLE_SMOKE_SOFT = new BFClientSetting("bf.settings.experimental.toggle.smoke.soft", "experimental.toggle.smoke.soft", BFClientSettingCategories.EXPERIMENTAL, true);
    public static final BFClientSetting DEBUG_TOGGLE_ARMORY_UUID = new BFClientSetting("bf.settings.debug.toggle.armory.uuid", "debug.toggle.armory.uuid", BFClientSettingCategories.DEBUG, false);
    public static final BFClientSetting INTRO_RULES = new BFClientSetting("bf.settings.intro.rules", "intro.rules", BFClientSettingCategories.RULES, false);
    public static final BFClientSetting INTRO_CONTENT = new BFClientSetting("bf.settings.intro.content", "intro.content", BFClientSettingCategories.RULES, false);
    public static final BFClientSetting INTRO_DISCORD = new BFClientSetting("bf.settings.intro.discord", "intro.discord", BFClientSettingCategories.RULES, false);
    public static final BFClientSetting INTRO_PATREON = new BFClientSetting("bf.settings.intro.patreon", "intro.patreon", BFClientSettingCategories.RULES, false);
    public static long SCOREBOARD_RESET_TIME = Calendar.getInstance().getTime().getTime();
    public static boolean isUnsaved = false;

    @NotNull
    public static BFClientSetting addNotification(@NotNull NotificationType notificationType, @NotNull BFClientSetting setting) {
        NOTIFICATION_MAP.put((Object)notificationType, (Object)setting);
        return setting;
    }

    public static boolean shouldSendNotifications(@NotNull NotificationType type) {
        if (!NOTIFICATION_MAP.containsKey((Object)type)) {
            return true;
        }
        return ((BFClientSetting)NOTIFICATION_MAP.get((Object)type)).isEnabled();
    }
}

