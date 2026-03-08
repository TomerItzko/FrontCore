/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.screen.profile;

import com.boehmod.bflib.cloud.common.CloudRegistry;
import com.boehmod.bflib.cloud.common.player.achievement.CloudAchievement;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.profile.ProfileScreen;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.unnamed.BF_209;
import com.boehmod.blockfront.unnamed.BF_210;
import com.boehmod.blockfront.unnamed.BF_218;
import com.boehmod.blockfront.util.BFStyles;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public final class ProfileAchievementsScreen
extends ProfileScreen {
    private static final Component field_1424 = Component.translatable((String)"bf.message.achievement.nodata").withStyle(ChatFormatting.RED).withStyle(BFStyles.BOLD);
    @NotNull
    private final BF_209<BF_218> field_1423 = new BF_209(0, 84, 235, 148, this);

    public ProfileAchievementsScreen(@NotNull Screen screen, @NotNull PlayerCloudData playerCloudData) {
        super(screen, playerCloudData, (Component)Component.translatable((String)"bf.screen.profile.achievements", (Object[])new Object[]{playerCloudData.getUsername()}));
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
        super.render(guiGraphics, n, n2, f);
        float f2 = BFRendering.getRenderTime();
        int n3 = this.width / 2;
        int n4 = this.height / 2;
        int n5 = BFRendering.RED_CHAT_COLOR;
        if (this.field_1423.method_962().isEmpty()) {
            int n6 = 64;
            BFRendering.runningTexture(guiGraphics, n3 - 32, n4 - 32 - 14, 64, n5, f2);
            BFRendering.centeredString(this.font, guiGraphics, field_1424, n3, n4 + 26);
        }
    }

    @Override
    public void renderBackground(@NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
        super.renderBackground(guiGraphics, n, n2, f);
        BFRendering.rectangleWithDarkShadow(guiGraphics, this.field_1423.method_559(), this.field_1423.method_560(), this.field_1423.method_558(), this.field_1423.height(), BFRendering.translucentBlack());
    }

    @Override
    public void method_774() {
        super.method_774();
        int n = this.width / 2 - 160;
        int n2 = 4;
        this.field_1423.method_948();
        this.field_1423.method_551(n + 25 + 4);
        this.field_1423.method_552(60);
        this.field_1423.method_553(272);
        this.field_1423.method_554(this.height - 115);
        this.field_1423.method_556(true);
        this.method_764(this.field_1423);
        CloudRegistry cloudRegistry = this.manager.getCloudRegistry();
        for (CloudAchievement cloudAchievement : cloudRegistry.getAchievements()) {
            boolean bl = this.playerCloudData.hasAchievement(cloudAchievement);
            this.field_1423.method_950(new BF_210(cloudAchievement, bl));
        }
    }
}

