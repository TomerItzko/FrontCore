/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.screen.title;

import com.boehmod.bflib.cloud.common.RequestType;
import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.gui.widget.BFButton;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.title.BFTitleScreen;
import com.boehmod.blockfront.client.screen.title.ClanScoreboardTitleScreen;
import com.boehmod.blockfront.client.screen.title.PlayerScoreboardTitleScreen;
import com.boehmod.blockfront.client.settings.BFClientSettings;
import com.boehmod.blockfront.cloud.client.ClientConnectionManager;
import com.boehmod.blockfront.unnamed.BF_208;
import com.boehmod.blockfront.unnamed.BF_209;
import com.boehmod.blockfront.unnamed.BF_216;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.BFStyles;
import com.boehmod.blockfront.util.FormatUtils;
import com.boehmod.blockfront.util.StringUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.Calendar;
import java.util.Map;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public abstract class ScoreboardTitleScreen
extends BFTitleScreen {
    private static final Component field_1418 = Component.translatable((String)"bf.screen.menu.scoreboard");
    private static final Component field_1419 = Component.translatable((String)"bf.menu.button.nav.text.scoreboard");
    private static final Component field_1420 = Component.translatable((String)"bf.menu.button.scoreboard.players");
    private static final Component field_1421 = Component.translatable((String)"bf.menu.button.scoreboard.clans");
    @NotNull
    private static final ResourceLocation field_1412 = BFRes.loc("textures/gui/menu/icons/armory_filter_armor.png");
    @NotNull
    private static final ResourceLocation field_1413 = BFRes.loc("textures/gui/menu/icons/armory_filter_card.png");
    @NotNull
    private static final ResourceLocation field_1414 = BFRes.loc("textures/gui/menu/icons/refresh_black.png");
    private static final Component field_1422 = Component.translatable((String)"bf.message.achievement.nodata").withStyle(ChatFormatting.RED).withStyle(BFStyles.BOLD);
    private static final int field_1416 = 64;
    private static final int field_1417 = 16;
    public BF_208<BF_216> field_1415;

    public ScoreboardTitleScreen() {
        super(field_1418, field_1419);
        ((ClientConnectionManager)this.manager.getConnectionManager()).getRequester().push(RequestType.CLOUD_STATS);
    }

    @Override
    public void method_758() {
        super.method_758();
        int n = this.width / 2 - 173;
        int n2 = n + 233 + 114 + 3;
        this.addRenderableWidget((GuiEventListener)new BFButton(n2, 56, 16, 16, (Component)Component.empty(), button -> this.minecraft.setScreen((Screen)new PlayerScoreboardTitleScreen())).displayType(BFButton.DisplayType.NONE).tip(field_1420).texture(field_1412).size(16).enabled(bFButton -> this.minecraft.screen instanceof ClanScoreboardTitleScreen));
        this.addRenderableWidget((GuiEventListener)new BFButton(n2, 75, 16, 16, (Component)Component.empty(), button -> this.minecraft.setScreen((Screen)new ClanScoreboardTitleScreen())).displayType(BFButton.DisplayType.NONE).tip(field_1421).texture(field_1413).size(16).enabled(bFButton -> this.minecraft.screen instanceof PlayerScoreboardTitleScreen));
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
        super.render(guiGraphics, n, n2, f);
        long l = BFClientSettings.SCOREBOARD_RESET_TIME - Calendar.getInstance().getTime().getTime();
        if (l < 0L) {
            l = 0L;
        }
        PoseStack poseStack = guiGraphics.pose();
        String string = StringUtils.formatDuration(l);
        int n3 = this.field_1415.method_559();
        int n4 = this.field_1415.method_560() + this.field_1415.height() + 3;
        int n5 = this.field_1415.method_558();
        int n6 = 12;
        BFRendering.rectangleWithDarkShadow(guiGraphics, n3, n4, n5, 12, ColorReferences.COLOR_THEME_YELLOW_SOLID);
        int n7 = 12;
        int n8 = this.font.width(string);
        int n9 = n3 + n5 / 2 + 6;
        int n10 = n4 + 2;
        BFRendering.centeredString(this.font, guiGraphics, (Component)Component.literal((String)string), n9, n10, 0);
        int n11 = n9 - n8 / 2 - 12 - 3;
        BFRendering.texture(poseStack, guiGraphics, field_1414, n11, n4, 12, 12);
    }

    @Override
    public void renderBackground(@NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
        super.renderBackground(guiGraphics, n, n2, f);
        float f2 = BFRendering.getRenderTime();
        PoseStack poseStack = guiGraphics.pose();
        BFRendering.rectangleWithDarkShadow(guiGraphics, this.field_1415.method_559(), this.field_1415.method_560(), this.field_1415.method_558(), this.field_1415.height(), BFRendering.translucentBlack());
        if (this.field_1415.method_946() <= 0) {
            int n3 = this.field_1415.method_559();
            int n4 = this.field_1415.method_560();
            int n5 = this.field_1415.method_558();
            int n6 = this.field_1415.height();
            int n7 = (int)((float)n3 + (float)n5 / 2.0f);
            int n8 = (int)((float)n4 + (float)n6 / 2.0f);
            BFRendering.runningTexture(poseStack, guiGraphics, (float)n7 - 32.0f, (float)n8 - 32.0f - 10.0f, 64, BFRendering.RED_CHAT_COLOR, f2);
            BFRendering.centeredComponent2d(poseStack, this.font, guiGraphics, field_1422, n7, n8 + 30);
        }
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void method_774() {
        super.method_774();
        int n = this.width / 2 - 173;
        int n2 = n + 114;
        int n3 = 56;
        int n4 = 233;
        int n5 = 121;
        this.field_1415 = new BF_209<BF_216>(n2, 56, 233, 121, this);
        this.method_764(this.field_1415);
        this.method_1034();
    }

    public void method_1034() {
        this.field_1415.method_948();
        BF_216.Type type = this.getType();
        boolean bl = false;
        int n = 0;
        for (Map.Entry entry : FormatUtils.sortEntriesDescending(this.method_1036())) {
            BF_216 bF_216 = new BF_216((UUID)entry.getKey(), (Integer)entry.getValue(), ++n, type);
            bl = !bl;
            this.field_1415.method_950(bF_216.method_972(bl));
        }
    }

    @NotNull
    public abstract Object2IntMap<UUID> method_1036();

    @NotNull
    public abstract BF_216.Type getType();
}

