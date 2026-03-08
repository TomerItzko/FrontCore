/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.match;

import com.boehmod.blockfront.registry.BFBotVoices;
import com.boehmod.blockfront.registry.BFItems;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.registry.custom.BotVoice;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.RandomUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public enum BFCountry implements StringRepresentable
{
    UNITED_STATES("us", "United States"),
    GERMANY("ger", "Germany"),
    GREAT_BRITAIN("gb", "Great Britain"),
    SOVIET_UNION("ussr", "Soviet Union"),
    FRANCE("fr", "France"),
    POLAND("pol", "Poland"),
    JAPAN("jpn", "Japan"),
    ITALY("it", "Italy");

    @NotNull
    private final List<DeferredHolder<BotVoice, ? extends BotVoice>> botVoices = new ObjectArrayList();
    @NotNull
    private final List<String> usernames = new ObjectArrayList();
    @NotNull
    private final List<Supplier<? extends Item>> miscItems = new ObjectArrayList();
    @NotNull
    private final String tag;
    @NotNull
    private final String name;
    private final ResourceLocation nationIcon;
    @Nullable
    private DeferredHolder<SoundEvent, SoundEvent> warCrySound = BFSounds.TEAM_SHARED_WARCRY;
    @Nullable
    private DeferredHolder<SoundEvent, SoundEvent> warCrySoundDistant = BFSounds.TEAM_SHARED_WARCRY_DISTANT;
    @Nullable
    private DeferredHolder<SoundEvent, SoundEvent> warCryCrowdSound = BFSounds.TEAM_SHARED_WARCRY_CROWD;
    @Nullable
    private DeferredHolder<SoundEvent, SoundEvent> warCrySoundCommander = BFSounds.TEAM_SHARED_WHISTLE;

    private BFCountry(String tag, String name) {
        this.tag = tag;
        this.name = name;
        this.nationIcon = BFRes.loc("textures/misc/flag_" + tag + ".png");
    }

    @Nullable
    public static BFCountry fromTag(@NotNull String tag) {
        for (BFCountry bFCountry : BFCountry.values()) {
            if (!bFCountry.tag.equals(tag)) continue;
            return bFCountry;
        }
        return null;
    }

    public void registerName(@NotNull String username) {
        this.usernames.add(username);
    }

    @NotNull
    public String getRandomUsername() {
        return this.usernames.get(ThreadLocalRandom.current().nextInt(this.usernames.size()));
    }

    public void registerMiscItem(@NotNull Supplier<? extends Item> supplier) {
        this.miscItems.add(supplier);
    }

    @NotNull
    public List<Supplier<? extends Item>> getMiscItems() {
        return Collections.unmodifiableList(this.miscItems);
    }

    @Nullable
    public DeferredHolder<BotVoice, ? extends BotVoice> getRandomBotVoice() {
        if (this.botVoices.isEmpty()) {
            return null;
        }
        return RandomUtils.randomFromList(this.botVoices);
    }

    public void registerBotVoice(@NotNull DeferredHolder<BotVoice, ? extends BotVoice> botVoice) {
        this.botVoices.add(botVoice);
    }

    public void registerWarCrySounds(@Nullable DeferredHolder<SoundEvent, SoundEvent> sound, @Nullable DeferredHolder<SoundEvent, SoundEvent> soundDistant, @Nullable DeferredHolder<SoundEvent, SoundEvent> crowdSound, @Nullable DeferredHolder<SoundEvent, SoundEvent> soundCommander) {
        this.warCrySound = sound;
        this.warCrySoundDistant = soundDistant;
        this.warCryCrowdSound = crowdSound;
        this.warCrySoundCommander = soundCommander;
    }

    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> getWarCrySound() {
        return this.warCrySound;
    }

    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> getWarCrySoundDistant() {
        return this.warCrySoundDistant;
    }

    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> getWarCrySoundCommander() {
        return this.warCrySoundCommander;
    }

    @Nullable
    public DeferredHolder<SoundEvent, SoundEvent> getWarCryCrowdSound() {
        return this.warCryCrowdSound;
    }

    @NotNull
    public String getTag() {
        return this.tag;
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    @NotNull
    public ResourceLocation getNationIcon() {
        return this.nationIcon;
    }

    @NotNull
    public String getSerializedName() {
        return this.tag;
    }

    static {
        UNITED_STATES.registerBotVoice(BFBotVoices.UNITED_STATES_MALE);
        UNITED_STATES.registerMiscItem((Supplier<? extends Item>)BFItems.GUN_M2_FLAMETHROWER);
        UNITED_STATES.registerMiscItem((Supplier<? extends Item>)BFItems.GUN_LEWISGUN);
        UNITED_STATES.registerName("John");
        UNITED_STATES.registerName("Roy");
        UNITED_STATES.registerName("Billy");
        UNITED_STATES.registerName("Peter");
        UNITED_STATES.registerName("Joe");
        UNITED_STATES.registerName("Bruce");
        UNITED_STATES.registerName("Willie");
        UNITED_STATES.registerName("Henry");
        UNITED_STATES.registerName("Carl");
        UNITED_STATES.registerName("Jack");
        UNITED_STATES.registerName("Arthur");
        UNITED_STATES.registerName("Wayne");
        UNITED_STATES.registerName("Terry");
        UNITED_STATES.registerName("Lawrence");
        UNITED_STATES.registerName("Douglas");
        UNITED_STATES.registerName("Steven");
        GERMANY.registerBotVoice(BFBotVoices.GERMANY_MALE);
        GERMANY.registerMiscItem((Supplier<? extends Item>)BFItems.GUN_FLAMMENWERFER_34);
        GERMANY.registerMiscItem((Supplier<? extends Item>)BFItems.GUN_M30);
        GERMANY.registerMiscItem((Supplier<? extends Item>)BFItems.GUN_BECKER);
        GERMANY.registerMiscItem((Supplier<? extends Item>)BFItems.GUN_PANZERBUCHSE39);
        GERMANY.registerMiscItem((Supplier<? extends Item>)BFItems.GUN_MG34);
        GERMANY.registerName("Hans");
        GERMANY.registerName("Michael");
        GERMANY.registerName("Helmut");
        GERMANY.registerName("Gerhard");
        GERMANY.registerName("Reiner");
        GERMANY.registerName("Rolf");
        GERMANY.registerName("Klaus");
        GERMANY.registerName("Werner");
        GERMANY.registerName("Karl");
        GERMANY.registerName("Berndt");
        GERMANY.registerName("Horst");
        GERMANY.registerName("Peter");
        GERMANY.registerName("Holger");
        GERMANY.registerName("Joachim");
        GERMANY.registerName("Herbert");
        GERMANY.registerName("Jens");
        FRANCE.registerBotVoice(BFBotVoices.FRANCE_MALE);
        FRANCE.registerMiscItem((Supplier<? extends Item>)BFItems.GUN_LEWISGUN);
        FRANCE.registerName("Jean");
        FRANCE.registerName("Michel");
        FRANCE.registerName("Claude");
        FRANCE.registerName("Andre");
        FRANCE.registerName("Pierre");
        FRANCE.registerName("Jacques");
        FRANCE.registerName("Bernard");
        FRANCE.registerName("Gerard");
        FRANCE.registerName("Daniel");
        FRANCE.registerName("Rene");
        FRANCE.registerName("Robert");
        FRANCE.registerName("Henri");
        FRANCE.registerName("Roger");
        FRANCE.registerName("Marcel");
        FRANCE.registerName("Georges");
        FRANCE.registerName("Maurice");
        JAPAN.registerBotVoice(BFBotVoices.JAPAN_MALE);
        JAPAN.registerWarCrySounds(BFSounds.TEAM_JPN_BANZAI, BFSounds.TEAM_JPN_BANZAI_DISTANT, null, BFSounds.TEAM_JPN_BANZAI_COMMANDER);
        JAPAN.registerMiscItem((Supplier<? extends Item>)BFItems.GUN_TYPE96);
        JAPAN.registerName("Koichi");
        JAPAN.registerName("Shimamura");
        JAPAN.registerName("Kamimura");
        JAPAN.registerName("Murakami");
        JAPAN.registerName("Kanji");
        JAPAN.registerName("Akiyama");
        JAPAN.registerName("Dewa");
        JAPAN.registerName("Kataoka");
        JAPAN.registerName("Misu");
        JAPAN.registerName("Uryu");
        JAPAN.registerName("Kabayama");
        JAPAN.registerName("Matsumura");
        JAPAN.registerName("Nashiba");
        JAPAN.registerName("Shibayama");
        JAPAN.registerName("Yamaya");
        JAPAN.registerName("Tetsutaro");
        GREAT_BRITAIN.registerBotVoice(BFBotVoices.GB_MALE_ENGLISH);
        GREAT_BRITAIN.registerBotVoice(BFBotVoices.GB_MALE_SCOTTISH);
        GREAT_BRITAIN.registerMiscItem((Supplier<? extends Item>)BFItems.GUN_LEE_ENFIELD_TURNER);
        GREAT_BRITAIN.registerMiscItem((Supplier<? extends Item>)BFItems.GUN_LEWISGUN);
        GREAT_BRITAIN.registerName("Noah");
        GREAT_BRITAIN.registerName("Leo");
        GREAT_BRITAIN.registerName("Archie");
        GREAT_BRITAIN.registerName("Freddie");
        GREAT_BRITAIN.registerName("Oliver");
        GREAT_BRITAIN.registerName("Oscar");
        GREAT_BRITAIN.registerName("Arthur");
        GREAT_BRITAIN.registerName("Alfie");
        GREAT_BRITAIN.registerName("Theor");
        GREAT_BRITAIN.registerName("George");
        GREAT_BRITAIN.registerName("Charlie");
        GREAT_BRITAIN.registerName("Thomas");
        GREAT_BRITAIN.registerName("Henry");
        GREAT_BRITAIN.registerName("Jack");
        GREAT_BRITAIN.registerName("William");
        GREAT_BRITAIN.registerName("Alexander");
        SOVIET_UNION.registerBotVoice(BFBotVoices.SOVIET_UNION_MALE);
        SOVIET_UNION.registerMiscItem((Supplier<? extends Item>)BFItems.GUN_LEWISGUN);
        SOVIET_UNION.registerName("Voroshilov");
        SOVIET_UNION.registerName("Mikhail");
        SOVIET_UNION.registerName("Alexander");
        SOVIET_UNION.registerName("Semyon");
        SOVIET_UNION.registerName("Vasily");
        SOVIET_UNION.registerName("Grigory");
        SOVIET_UNION.registerName("Georgy");
        SOVIET_UNION.registerName("Zhukov");
        SOVIET_UNION.registerName("Blyukher");
        SOVIET_UNION.registerName("Joseph");
        SOVIET_UNION.registerName("Ivan");
        SOVIET_UNION.registerName("Leonid");
        SOVIET_UNION.registerName("Rodion");
        SOVIET_UNION.registerName("Fyodor");
        SOVIET_UNION.registerName("Kirill");
        SOVIET_UNION.registerName("Nikolai");
        ITALY.registerBotVoice(BFBotVoices.ITALY_MALE);
        ITALY.registerMiscItem((Supplier<? extends Item>)BFItems.GUN_BREDA_SAFAT);
        ITALY.registerName("Abramo");
        ITALY.registerName("Alessandro");
        ITALY.registerName("Alessio");
        ITALY.registerName("Brando");
        ITALY.registerName("Daniel");
        ITALY.registerName("Davide");
        ITALY.registerName("Diego");
        ITALY.registerName("Emanuele");
        ITALY.registerName("Federico");
        ITALY.registerName("Filippo");
        ITALY.registerName("Gabriel");
        ITALY.registerName("Jacopo");
        ITALY.registerName("Leonardo");
        ITALY.registerName("Lorenzo");
        ITALY.registerName("Manuel");
        ITALY.registerName("Stefano");
        POLAND.registerBotVoice(BFBotVoices.POLAND_MALE);
        POLAND.registerName("Zygmunt");
        POLAND.registerName("W\u0142adys\u0142aw");
        POLAND.registerName("Czes\u0142aw");
        POLAND.registerName("Stanis\u0142aw");
        POLAND.registerName("J\u00f3zef");
        POLAND.registerName("Jan");
        POLAND.registerName("Kazimierz");
        POLAND.registerName("Tomasz");
        POLAND.registerName("Andrzej");
        POLAND.registerName("Ludwik");
        POLAND.registerName("Zdzis\u0142aw");
        POLAND.registerName("Henryk");
        POLAND.registerName("Marian");
        POLAND.registerName("Boles\u0142aw");
        POLAND.registerName("Walerian");
        POLAND.registerName("Stefan");
        POLAND.registerName("Antoni");
        POLAND.registerName("Franciszek");
        POLAND.registerName("Edward");
        POLAND.registerName("Roman");
    }
}

