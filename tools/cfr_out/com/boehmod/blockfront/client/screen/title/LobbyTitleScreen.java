/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.mm.MatchParty
 *  com.boehmod.bflib.cloud.common.player.PlayerDataType
 *  com.boehmod.bflib.common.ColorReferences
 *  it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet
 *  javax.annotation.Nonnull
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  org.apache.commons.lang3.tuple.Pair
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.screen.title;

import com.boehmod.bflib.cloud.common.mm.MatchParty;
import com.boehmod.bflib.cloud.common.player.PlayerDataType;
import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.gui.widget.BFButton;
import com.boehmod.blockfront.client.gui.widget.DailyChallengesWidget;
import com.boehmod.blockfront.client.gui.widget.DiscordWidget;
import com.boehmod.blockfront.client.gui.widget.EventsWidget;
import com.boehmod.blockfront.client.gui.widget.PlayerAnimationWidget;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.player.FakePlayer;
import com.boehmod.blockfront.client.screen.title.BFTitleScreen;
import com.boehmod.blockfront.client.screen.title.overlay.RankupScreen;
import com.boehmod.blockfront.common.match.DivisionData;
import com.boehmod.blockfront.common.match.Loadout;
import com.boehmod.blockfront.common.match.MatchClass;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.util.BFLog;
import com.boehmod.blockfront.util.BFRes;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

public final class LobbyTitleScreen
extends BFTitleScreen {
    private static final Component field_923 = Component.translatable((String)"bf.screen.menu.lobby");
    private static final Component field_924 = Component.translatable((String)"bf.menu.button.nav.text.lobby");
    @NotNull
    private static final Set<UUID> field_926 = new ObjectLinkedOpenHashSet();
    public static final int field_920 = 241;
    public static final int field_921 = 136;
    @NotNull
    public static final ResourceLocation field_919 = BFRes.loc("textures/skins/game/nations/us/infantry/backpack.png");
    @Nonnull
    private static final ResourceLocation field_6963 = BFRes.loc("textures/gui/patreon.png");
    @Nonnull
    private static final Component field_6964 = Component.translatable((String)"bf.menu.patreon.visit.tooltip").withColor(16345172);
    private static int field_922 = -1;

    public LobbyTitleScreen() {
        super(field_923, field_924);
    }

    public static void updateDisplayTextures(@NotNull ClientPlayerDataHandler dataHandler) {
        BFLog.log("Updating display textures...", new Object[0]);
        DivisionData divisionData = DivisionData.US_AIRBORNE;
        for (UUID uUID : field_926) {
            BFClientPlayerData bFClientPlayerData = (BFClientPlayerData)dataHandler.getPlayerData(uUID);
            Optional<Pair<Loadout, MatchClass>> optional = divisionData.getRandomLoadout(true);
            if (!optional.isPresent()) continue;
            Pair<Loadout, MatchClass> pair = optional.get();
            MatchClass matchClass = (MatchClass)((Object)pair.getValue());
            ResourceLocation resourceLocation = BFRes.loc("textures/skins/game/nations/us/infantry/" + matchClass.getKey() + ".png");
            bFClientPlayerData.method_1143(resourceLocation, field_919);
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
        super.render(guiGraphics, n, n2, f);
        PlayerCloudData playerCloudData = this.playerDataHandler.getCloudData(this.minecraft);
        int n3 = playerCloudData.getParty().map(matchParty -> matchParty.getPartyUUIDs().hashCode()).orElse(-1);
        if (n3 != field_922) {
            field_922 = n3;
            this.minecraft.setScreen((Screen)new LobbyTitleScreen());
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (RankupScreen.newRank) {
            this.minecraft.setScreen((Screen)new RankupScreen(this));
            RankupScreen.newRank = false;
        }
    }

    @Override
    public void method_774() {
        super.method_774();
        int n = this.width / 2;
        int n2 = 15;
        int n3 = this.width - 15 - 7;
        int n4 = 55;
        PlayerCloudData playerCloudData = this.playerDataHandler.getCloudData(this.minecraft);
        int n5 = n - 173;
        this.method_699(n5, playerCloudData);
        this.method_764(new DailyChallengesWidget(n3, 55, 15, 15, this));
        this.method_764(new DiscordWidget(n3, 73, 15, 15, this));
        BFButton bFButton = new BFButton(n3, 91, 15, 15, (Component)Component.empty(), button -> BFTitleScreen.openUrl(this.minecraft, "https://patreon.blockfrontmc.com/")).tip(field_6964).texture(field_6963).size(15, 15).method_395(ColorReferences.COLOR_THEME_PATREON_SOLID).method_397(ColorReferences.COLOR_THEME_PATREON_HOVERED_SOLID);
        this.addRenderableWidget((GuiEventListener)bFButton);
        this.method_764(new EventsWidget(n3, 109, 15, 15, this));
    }

    private void method_699(int n, @NotNull PlayerCloudData playerCloudData) {
        ObjectLinkedOpenHashSet objectLinkedOpenHashSet;
        int n2 = n + 114;
        Optional optional = playerCloudData.getParty();
        if (optional.isPresent()) {
            objectLinkedOpenHashSet = new ObjectLinkedOpenHashSet();
            objectLinkedOpenHashSet.addAll(((MatchParty)optional.get()).getPlayers());
        } else {
            objectLinkedOpenHashSet = field_926;
        }
        int n3 = 241 / objectLinkedOpenHashSet.size();
        int n4 = 0;
        for (UUID uUID : objectLinkedOpenHashSet) {
            int n5 = n2 + n3 * n4;
            BFClientPlayerData bFClientPlayerData = (BFClientPlayerData)this.playerDataHandler.getPlayerData(uUID);
            boolean bl = bFClientPlayerData.getUUID().equals(this.minecraft.getUser().getProfileId());
            this.method_764(new PlayerAnimationWidget(n5, 56, n3, 136, this, bl, n4, uUID));
            ++n4;
        }
    }

    static {
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        Minecraft minecraft = Minecraft.getInstance();
        ClientPlayerDataHandler clientPlayerDataHandler = (ClientPlayerDataHandler)bFClientManager.getPlayerDataHandler();
        PlayerCloudData playerCloudData = clientPlayerDataHandler.getCloudData(minecraft);
        UUID uUID = playerCloudData.getUUID();
        field_926.add(uUID);
        for (int i = 0; i < 4; ++i) {
            UUID uUID2 = UUID.randomUUID();
            BFClientPlayerData bFClientPlayerData = (BFClientPlayerData)clientPlayerDataHandler.getPlayerData(uUID2);
            PlayerCloudData playerCloudData2 = clientPlayerDataHandler.getCloudProfile(uUID2);
            playerCloudData2.setPlayerDataType(PlayerDataType.DISPLAY);
            playerCloudData2.setUsername(FakePlayer.DIVISION.getCountry().getRandomUsername());
            bFClientPlayerData.method_837(PlayerDataType.DISPLAY);
            field_926.add(uUID2);
        }
        LobbyTitleScreen.updateDisplayTextures(clientPlayerDataHandler);
    }
}

