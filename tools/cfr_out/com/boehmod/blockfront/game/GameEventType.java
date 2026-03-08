/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game;

import com.boehmod.blockfront.util.BFRes;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public enum GameEventType {
    WAYPOINT_OBSERVE("observe", false, false, true, "bf.message.radio.waypoint.observe"),
    WAYPOINT_MOVE("move", false, false, true, "bf.message.radio.waypoint.move"),
    WAYPOINT_ATTACK("attack", false, false, true, "bf.message.radio.waypoint.attack"),
    AIR_STRIKE("airstrike", true, true, false, "bf.message.radio.airstrike"),
    PRECISION_AIR_STRIKE("precisionstrike", true, true, false, "bf.message.radio.airstrike.precision"),
    REINFORCEMENTS("reinforcements", true, false, false, "bf.message.radio.reinforcements"),
    REINFORCEMENTS_MG("reinforcementsmg", true, false, false, "bf.message.radio.reinforcements.mg");

    private final boolean hasTimeLimit;
    private final boolean spawnProtected;
    @NotNull
    private final String locale;
    @NotNull
    private final String iconName;
    @NotNull
    private final ResourceLocation icon;
    private final boolean isWaypoint;

    private GameEventType(String string2, boolean bl, @NotNull boolean bl2, boolean bl3, String string3) {
        this.hasTimeLimit = bl;
        this.spawnProtected = bl2;
        this.isWaypoint = bl3;
        this.locale = string3;
        this.iconName = string2;
        this.icon = BFRes.loc("textures/gui/game/radio/" + string2 + ".png");
    }

    @NotNull
    public String getIconName() {
        return this.iconName;
    }

    public boolean hasSpawnProtection() {
        return this.spawnProtected;
    }

    public boolean hasTimeLimit() {
        return this.hasTimeLimit;
    }

    public boolean isWaypoint() {
        return this.isWaypoint;
    }

    @NotNull
    public String getLocale() {
        return this.locale;
    }

    @NotNull
    public ResourceLocation getIcon() {
        return this.icon;
    }
}

