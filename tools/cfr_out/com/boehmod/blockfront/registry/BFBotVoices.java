/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  net.neoforged.neoforge.registries.DeferredRegister
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.registry;

import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.registry.custom.BotVoice;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

public class BFBotVoices {
    @NotNull
    public static final DeferredRegister<BotVoice> DR = DeferredRegister.create(BotVoice.REGISTRY, (String)"bf");
    public static final DeferredHolder<BotVoice, ? extends BotVoice> UNITED_STATES_MALE = DR.register("united_states_male", () -> new BotVoice(BFSounds.TEAM_US_BOT_GO, BFSounds.TEAM_US_BOT_GRENADE, BFSounds.TEAM_US_BOT_HURT, BFSounds.TEAM_US_BOT_RELOADING, BFSounds.TEAM_US_BOT_SPOTTED, BFSounds.TEAM_US_BOT_TAUNT, BFSounds.TEAM_US_BOT_INTRO));
    public static final DeferredHolder<BotVoice, ? extends BotVoice> GERMANY_MALE = DR.register("germany_male", () -> new BotVoice(BFSounds.TEAM_GER_BOT_GO, BFSounds.TEAM_GER_BOT_GRENADE, BFSounds.TEAM_GER_BOT_HURT, BFSounds.TEAM_GER_BOT_RELOADING, BFSounds.TEAM_GER_BOT_SPOTTED, BFSounds.TEAM_GER_BOT_TAUNT, BFSounds.TEAM_GER_BOT_INTRO));
    public static final DeferredHolder<BotVoice, ? extends BotVoice> FRANCE_MALE = DR.register("france_male", () -> new BotVoice(BFSounds.TEAM_FR_BOT_GO, BFSounds.TEAM_FR_BOT_GRENADE, BFSounds.TEAM_FR_BOT_HURT, BFSounds.TEAM_FR_BOT_RELOADING, BFSounds.TEAM_FR_BOT_SPOTTED, BFSounds.TEAM_FR_BOT_TAUNT, BFSounds.TEAM_FR_BOT_INTRO));
    public static final DeferredHolder<BotVoice, ? extends BotVoice> JAPAN_MALE = DR.register("japan_male", () -> new BotVoice(BFSounds.TEAM_JPN_BOT_GO, BFSounds.TEAM_JPN_BOT_GRENADE, BFSounds.TEAM_JPN_BOT_HURT, BFSounds.TEAM_JPN_BOT_RELOADING, BFSounds.TEAM_JPN_BOT_SPOTTED, BFSounds.TEAM_JPN_BOT_TAUNT, BFSounds.TEAM_JPN_BOT_INTRO));
    public static final DeferredHolder<BotVoice, ? extends BotVoice> GB_MALE_ENGLISH = DR.register("gb_male_english", () -> new BotVoice(BFSounds.TEAM_GB_BOT_GO, BFSounds.TEAM_GB_BOT_GRENADE, BFSounds.TEAM_GB_BOT_HURT, BFSounds.TEAM_GB_BOT_RELOADING, BFSounds.TEAM_GB_BOT_SPOTTED, BFSounds.TEAM_GB_BOT_TAUNT, BFSounds.TEAM_GB_BOT_INTRO));
    public static final DeferredHolder<BotVoice, ? extends BotVoice> GB_MALE_SCOTTISH = DR.register("gb_male_scottish", () -> new BotVoice(BFSounds.TEAM_GB_SCOT_BOT_GO, BFSounds.TEAM_GB_SCOT_BOT_GRENADE, BFSounds.TEAM_GB_SCOT_BOT_HURT, BFSounds.TEAM_GB_SCOT_BOT_RELOADING, BFSounds.TEAM_GB_SCOT_BOT_SPOTTED, BFSounds.TEAM_GB_SCOT_BOT_TAUNT, BFSounds.TEAM_GB_SCOT_BOT_INTRO));
    public static final DeferredHolder<BotVoice, ? extends BotVoice> SOVIET_UNION_MALE = DR.register("soviet_union_male", () -> new BotVoice(BFSounds.TEAM_RUS_BOT_GO, BFSounds.TEAM_RUS_BOT_GRENADE, BFSounds.TEAM_RUS_BOT_HURT, BFSounds.TEAM_RUS_BOT_RELOADING, BFSounds.TEAM_RUS_BOT_SPOTTED, BFSounds.TEAM_RUS_BOT_TAUNT, BFSounds.TEAM_RUS_BOT_INTRO));
    public static final DeferredHolder<BotVoice, ? extends BotVoice> ITALY_MALE = DR.register("italy_male", () -> new BotVoice(BFSounds.TEAM_IT_BOT_GO, BFSounds.TEAM_IT_BOT_GRENADE, BFSounds.TEAM_IT_BOT_HURT, BFSounds.TEAM_IT_BOT_RELOADING, BFSounds.TEAM_IT_BOT_SPOTTED, BFSounds.TEAM_IT_BOT_TAUNT, BFSounds.TEAM_IT_BOT_INTRO));
    public static final DeferredHolder<BotVoice, ? extends BotVoice> POLAND_MALE = DR.register("poland_male", () -> new BotVoice(BFSounds.TEAM_POL_BOT_GO, BFSounds.TEAM_POL_BOT_GRENADE, BFSounds.TEAM_POL_BOT_HURT, BFSounds.TEAM_POL_BOT_RELOADING, BFSounds.TEAM_POL_BOT_SPOTTED, BFSounds.TEAM_POL_BOT_TAUNT, BFSounds.TEAM_POL_BOT_INTRO));
}

