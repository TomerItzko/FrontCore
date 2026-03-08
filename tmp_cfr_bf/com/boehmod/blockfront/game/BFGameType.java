/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game;

import com.boehmod.bflib.cloud.common.mm.SearchGame;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.impl.boot.BootcampGame;
import com.boehmod.blockfront.game.impl.conq.ConquestGame;
import com.boehmod.blockfront.game.impl.dom.DominationGame;
import com.boehmod.blockfront.game.impl.ffa.FreeForAllGame;
import com.boehmod.blockfront.game.impl.gg.GunGame;
import com.boehmod.blockfront.game.impl.inf.InfectedGame;
import com.boehmod.blockfront.game.impl.tdm.TeamDeathmatchGame;
import com.boehmod.blockfront.game.impl.ttt.TroubleTownGame;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFRes;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.NotNull;

public final class BFGameType {
    @NotNull
    public static final Map<String, BFGameType> NAME_MAP = new Object2ObjectLinkedOpenHashMap();
    @NotNull
    public static final BFGameType BOOTCAMP = new BFGameType(Category.SOLO, "bf.gamemode.boot", "boot", SearchGame.BOOT, BootcampGame.class).description((Component)Component.translatable((String)"bf.gamemode.boot.description"));
    @NotNull
    public static final BFGameType DOMINATION = new BFGameType(Category.VERSUS, "bf.gamemode.dom", "dom", SearchGame.DOMINATION, DominationGame.class).description((Component)Component.translatable((String)"bf.gamemode.dom.description"));
    @NotNull
    public static final BFGameType CONQUEST = new BFGameType(Category.VERSUS, "bf.gamemode.conq", "conq", SearchGame.CONQUEST, ConquestGame.class).description((Component)Component.translatable((String)"bf.gamemode.conq.description")).hidden().experimental();
    @NotNull
    public static final BFGameType TEAM_DEATHMATCH = new BFGameType(Category.VERSUS, "bf.gamemode.tdm", "tdm", SearchGame.TEAM_DEATHMATCH, TeamDeathmatchGame.class).description((Component)Component.translatable((String)"bf.gamemode.tdm.description"));
    @NotNull
    public static final BFGameType GUN_GAME = new BFGameType(Category.VERSUS, "bf.gamemode.gg", "gg", SearchGame.GUN_GAME, GunGame.class).description((Component)Component.translatable((String)"bf.gamemode.gg.description"));
    @NotNull
    public static final BFGameType FREE_FOR_ALL = new BFGameType(Category.VERSUS, "bf.gamemode.ffa", "ffa", SearchGame.FREE_FOR_ALL, FreeForAllGame.class).description((Component)Component.translatable((String)"bf.gamemode.ffa.description"));
    @NotNull
    public static final BFGameType INFECTED = new BFGameType(Category.COOP, "bf.gamemode.inf", "inf", SearchGame.INFECT, InfectedGame.class).description((Component)Component.translatable((String)"bf.gamemode.inf.description")).selectSound((Supplier<SoundEvent>)BFSounds.ENTITY_INFECTED_DEATH).experimental();
    @NotNull
    public static final BFGameType TROUBLE_TOWN = new BFGameType(Category.MISC, "bf.gamemode.ttt", "ttt", SearchGame.TROUBLE_TOWN, TroubleTownGame.class).description((Component)Component.translatable((String)"bf.gamemode.ttt.description")).experimental();
    @NotNull
    private final Category category;
    @NotNull
    private final Component displayName;
    @NotNull
    private final String name;
    @NotNull
    private final SearchGame searchGame;
    @NotNull
    private final List<BFGameType> alternativeTypes = new ObjectArrayList();
    @NotNull
    private final Class<? extends AbstractGame<?, ?, ?>> gameClass;
    @NotNull
    private Component description = Component.empty();
    private boolean isExperimental;
    private boolean isHidden = false;
    @Nullable
    private Supplier<SoundEvent> selectSound = null;
    @NotNull
    private final ResourceLocation texture;
    @NotNull
    private final ResourceLocation iconTexture;

    public BFGameType(@NotNull Category category, @NotNull String componentName, @NotNull String name, @NotNull SearchGame searchGame, @NotNull Class<? extends AbstractGame<?, ?, ?>> gameClass) {
        NAME_MAP.put(name, this);
        this.gameClass = gameClass;
        this.category = category;
        this.displayName = Component.translatable((String)componentName);
        this.name = name;
        this.searchGame = searchGame;
        this.isExperimental = false;
        this.texture = BFRes.loc("textures/gui/gamemode/gamemode_" + name + ".png");
        this.iconTexture = BFRes.loc("textures/gui/gamemode/gamemode_" + name + "_icon.png");
    }

    @NotNull
    public ResourceLocation getIconTexture() {
        return this.iconTexture;
    }

    @NotNull
    public ResourceLocation getTexture() {
        return this.texture;
    }

    @NotNull
    public static Collection<BFGameType> values() {
        return NAME_MAP.values();
    }

    @Nullable
    public static BFGameType getByName(@NotNull String name) {
        return NAME_MAP.get(name);
    }

    @NotNull
    public static List<BFGameType> getByCategory(@NotNull Category category) {
        ObjectArrayList objectArrayList = new ObjectArrayList();
        for (BFGameType bFGameType : NAME_MAP.values()) {
            if (bFGameType.category != category && category != Category.ALL) continue;
            objectArrayList.add(bFGameType);
        }
        return objectArrayList;
    }

    @NotNull
    public Class<? extends AbstractGame<?, ?, ?>> getGameClass() {
        return this.gameClass;
    }

    @NotNull
    public SearchGame getSearchGame() {
        return this.searchGame;
    }

    public void addAlternativeType(@NotNull BFGameType type) {
        this.alternativeTypes.add(type);
    }

    @NotNull
    public Component getDisplayName() {
        return this.displayName;
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    @NotNull
    public BFGameType experimental() {
        this.isExperimental = true;
        return this;
    }

    public boolean isExperimental() {
        return this.isExperimental;
    }

    @NotNull
    public BFGameType hidden() {
        this.isHidden = true;
        return this;
    }

    public boolean isHidden() {
        return this.isHidden;
    }

    @NotNull
    public Component getDescription() {
        return this.description;
    }

    public BFGameType description(@NotNull Component description) {
        this.description = description;
        return this;
    }

    @NotNull
    public List<BFGameType> getAlternativeTypes() {
        return this.alternativeTypes;
    }

    @Nullable
    public Supplier<SoundEvent> getSelectSound() {
        return this.selectSound;
    }

    public BFGameType selectSound(@NotNull Supplier<SoundEvent> sound) {
        this.selectSound = sound;
        return this;
    }

    static {
        DOMINATION.addAlternativeType(TEAM_DEATHMATCH);
        TEAM_DEATHMATCH.addAlternativeType(DOMINATION);
        FREE_FOR_ALL.addAlternativeType(DOMINATION);
        FREE_FOR_ALL.addAlternativeType(TEAM_DEATHMATCH);
    }

    public static enum Category {
        ALL((Component)Component.translatable((String)"bf.message.gamemode.category.all"), (Component)Component.translatable((String)"bf.message.gamemode.category.all.tip")),
        SOLO((Component)Component.translatable((String)"bf.message.gamemode.category.solo"), (Component)Component.translatable((String)"bf.message.gamemode.category.solo.tip")),
        VERSUS((Component)Component.translatable((String)"bf.message.gamemode.category.versus"), (Component)Component.translatable((String)"bf.message.gamemode.category.versus.tip")),
        COOP((Component)Component.translatable((String)"bf.message.gamemode.category.coop"), (Component)Component.translatable((String)"bf.message.gamemode.category.coop.tip")),
        MISC((Component)Component.translatable((String)"bf.message.gamemode.category.misc"), (Component)Component.translatable((String)"bf.message.gamemode.category.misc.tip"));

        @NotNull
        private final Component title;
        @NotNull
        private final Component tip;

        private Category(Component title, Component tip) {
            this.title = title;
            this.tip = tip;
        }

        @NotNull
        public Component getTitle() {
            return this.title;
        }

        @NotNull
        public Component getTip() {
            return this.tip;
        }
    }
}

