/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.core.Holder$Reference
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundEvents
 *  org.jetbrains.annotations.NotNull
 *  org.joml.Vector2i
 */
package com.boehmod.blockfront.common.match;

import com.boehmod.blockfront.util.BFRes;
import javax.annotation.Nullable;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

public enum MatchCallout {
    NO("callout_no", "bf.message.gamemode.callout.no", new Vector2i(-1, 1)),
    FORWARD("callout_forward", "bf.message.gamemode.callout.forward", new Vector2i(0, 1)),
    YES("callout_yes", "bf.message.gamemode.callout.yes", new Vector2i(1, 1)),
    ENEMY("callout_enemy", "bf.message.gamemode.callout.enemy", new Vector2i(-2, 0)),
    AMMO("callout_ammo", "bf.message.gamemode.callout.ammo", "ammo", (Holder.Reference<SoundEvent>)SoundEvents.NOTE_BLOCK_PLING, new Vector2i(-1, 0)),
    MEDIC("callout_medic", "bf.message.gamemode.callout.medic", "medic", (Holder.Reference<SoundEvent>)SoundEvents.NOTE_BLOCK_PLING, new Vector2i(1, 0)),
    THANK_YOU("callout_thank_you", "bf.message.gamemode.callout.thank_you", new Vector2i(2, 0)),
    GOOD_WORK("callout_good_work", "bf.message.gamemode.callout.good_work", new Vector2i(-1, -1)),
    RETREAT("callout_retreat", "bf.message.gamemode.callout.retreat", new Vector2i(0, -1)),
    SUPPRESS("callout_suppress", "bf.message.gamemode.callout.suppress", new Vector2i(1, -1));

    @NotNull
    private final String translation;
    @Nullable
    private final String waypoint;
    @Nullable
    private final ResourceLocation waypointTexture;
    @Nullable
    private final Holder.Reference<SoundEvent> soundEvent;
    @NotNull
    private final Vector2i buttonPosition;
    @NotNull
    private final ResourceLocation texture;

    private MatchCallout(@Nullable String textureName, @Nullable String translation, @NotNull String waypoint, Holder.Reference<SoundEvent> soundEvent, Vector2i buttonPosition) {
        this.translation = translation;
        this.waypoint = waypoint;
        this.soundEvent = soundEvent;
        this.buttonPosition = buttonPosition;
        this.texture = BFRes.loc("textures/gui/callout/" + textureName + ".png");
        this.waypointTexture = waypoint != null ? BFRes.loc("textures/gui/callout/waypoint/" + waypoint + ".png") : null;
    }

    private MatchCallout(@NotNull String textureName, String translation, Vector2i buttonPosition) {
        this(textureName, translation, null, null, buttonPosition);
    }

    @NotNull
    public Vector2i getButtonPosition() {
        return this.buttonPosition;
    }

    @NotNull
    public ResourceLocation getTexture() {
        return this.texture;
    }

    @Nullable
    public ResourceLocation getWaypointTexture() {
        return this.waypointTexture;
    }

    @NotNull
    public String getTranslation() {
        return this.translation;
    }

    @Nullable
    public String getWaypoint() {
        return this.waypoint;
    }

    @Nullable
    public Holder.Reference<SoundEvent> getSoundEvent() {
        return this.soundEvent;
    }
}

