/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.TextColor
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.phys.Vec2
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.boehmod.blockfront.game.impl.ttt;

import com.boehmod.blockfront.game.GameShopItem;
import com.boehmod.blockfront.game.impl.ttt.TTTPlayerTeam;
import com.boehmod.blockfront.registry.BFItems;
import com.boehmod.blockfront.util.BFRes;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum TTTPlayerRole {
    PENDING("Pending", 0x7F7F7F, 0xBFBFBF, TTTPlayerTeam.NEUTRAL, "", "", null, false, false),
    INNOCENT("Innocent", 1755161, 9558928, TTTPlayerTeam.GOOD, "bf.message.gamemode.ttt.role.innocent.title", "bf.message.gamemode.ttt.role.innocent.desc", BFRes.loc("textures/gui/game/troubletown/innocent.png"), false, false),
    TRAITOR("Traitor", 13048088, 14197410, TTTPlayerTeam.BAD, "bf.message.gamemode.ttt.role.traitor.title", "bf.message.gamemode.ttt.role.traitor.desc", BFRes.loc("textures/gui/game/troubletown/traitor.png"), true, true),
    DETECTIVE("Detective", 1580540, 12435449, TTTPlayerTeam.GOOD, "bf.message.gamemode.ttt.role.detective.title", "bf.message.gamemode.ttt.role.detective.desc", BFRes.loc("textures/gui/game/troubletown/detective.png"), true, false),
    JESTER("Jester", 12783302, 16702463, TTTPlayerTeam.GOOD, "bf.message.gamemode.ttt.role.jester.title", "bf.message.gamemode.ttt.role.jester.desc", BFRes.loc("textures/gui/game/troubletown/jester.png"), true, true);

    @NotNull
    private final String key;
    private final int color;
    private final int subColor;
    @NotNull
    private final TTTPlayerTeam team;
    @NotNull
    private final Component title;
    @NotNull
    private final Component description;
    @Nullable
    private final ResourceLocation texture;
    @NotNull
    private final List<GameShopItem> roleItems = new ObjectArrayList();
    private final boolean hideRank;
    private final boolean displayOnNameplate;

    private TTTPlayerRole(String key, @NotNull int color, @NotNull int subColor, @NotNull TTTPlayerTeam team, @Nullable String title, String description, ResourceLocation texture, boolean displayOnNameplate, boolean hideRank) {
        this.key = key;
        this.color = color;
        this.subColor = subColor;
        this.team = team;
        this.title = Component.translatable((String)title).withColor(TextColor.fromRgb((int)color).getValue());
        this.description = Component.translatable((String)description).withColor(subColor).withStyle(new ChatFormatting[0]);
        this.texture = texture;
        this.hideRank = hideRank;
        this.displayOnNameplate = displayOnNameplate;
    }

    @NotNull
    public static TTTPlayerRole fromKey(String key) {
        for (TTTPlayerRole tTTPlayerRole : TTTPlayerRole.values()) {
            if (!tTTPlayerRole.getKey().equalsIgnoreCase(key)) continue;
            return tTTPlayerRole;
        }
        return PENDING;
    }

    public boolean displayOnNameplate() {
        return this.displayOnNameplate;
    }

    @Nullable
    public ResourceLocation getTexture() {
        return this.texture;
    }

    public void addRoleItem(@NotNull GameShopItem shopItem) {
        this.roleItems.add(shopItem);
    }

    @NotNull
    public TTTPlayerTeam getTeam() {
        return this.team;
    }

    @NotNull
    public String getKey() {
        return this.key;
    }

    public int getColor() {
        return this.color;
    }

    public int getSubColor() {
        return this.subColor;
    }

    @NotNull
    public Component getTitle() {
        return this.title;
    }

    @NotNull
    public Component getDescription() {
        return this.description;
    }

    @NotNull
    public List<GameShopItem> getRoleItems() {
        return this.roleItems;
    }

    public boolean hideRank() {
        return this.hideRank;
    }

    @Nullable
    public GameShopItem getRoleItem(int index) {
        if (index >= this.roleItems.size()) {
            return null;
        }
        return this.roleItems.get(index);
    }

    static {
        TRAITOR.addRoleItem(new GameShopItem((Item)BFItems.GRENADE_FLASHBANG.get(), 1, new Vec2(0.0f, 2.0f)));
        TRAITOR.addRoleItem(new GameShopItem((Item)BFItems.GRENADE_FRAG_MK2.get(), 1, new Vec2(-1.0f, 1.0f)));
        TRAITOR.addRoleItem(new GameShopItem((Item)BFItems.GRENADE_SMOKE.get(), 1, new Vec2(0.0f, 1.0f)));
        TRAITOR.addRoleItem(new GameShopItem((Item)BFItems.GRENADE_FIRE.get(), 1, new Vec2(1.0f, 1.0f)));
        TRAITOR.addRoleItem(new GameShopItem((Item)BFItems.MEDIC_BAG.get(), 1, new Vec2(-1.0f, 0.0f)));
        TRAITOR.addRoleItem(new GameShopItem((Item)BFItems.AMMO_BOX.get(), 1, new Vec2(1.0f, 0.0f)));
        TRAITOR.addRoleItem(new GameShopItem((Item)BFItems.GUN_MELON_CANNON.get(), 3, new Vec2(-1.0f, -1.0f)));
        TRAITOR.addRoleItem(new GameShopItem((Item)BFItems.GRENADE_HOLY.get(), 5, new Vec2(0.0f, -1.0f)));
        TRAITOR.addRoleItem(new GameShopItem((Item)BFItems.SUPER_HAPPY_FUN_BOMB.get(), 6, new Vec2(1.0f, -1.0f)));
        DETECTIVE.addRoleItem(new GameShopItem((Item)BFItems.MEDIC_BAG.get(), 1, new Vec2(-1.0f, 1.0f)));
        DETECTIVE.addRoleItem(new GameShopItem((Item)BFItems.AMMO_BOX.get(), 2, new Vec2(0.0f, 1.0f)));
        DETECTIVE.addRoleItem(new GameShopItem((Item)BFItems.RANDOMAT.get(), 1, new Vec2(1.0f, 1.0f)).setCount(1));
    }
}

