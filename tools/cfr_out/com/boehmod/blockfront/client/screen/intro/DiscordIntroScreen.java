/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.common.ColorReferences
 *  com.mojang.blaze3d.vertex.PoseStack
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  javax.annotation.Nonnull
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.FormattedText
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.util.FormattedCharSequence
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.boehmod.blockfront.client.screen.intro;

import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.intro.BFIntroScreen;
import com.boehmod.blockfront.client.screen.title.BFTitleScreen;
import com.boehmod.blockfront.client.settings.BFClientSettings;
import com.boehmod.blockfront.client.settings.BFClientSettingsDisk;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.unnamed.BF_162;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.BFStyles;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DiscordIntroScreen
extends BFIntroScreen {
    private static final Component field_850 = Component.translatable((String)"bf.message.intro.discord.title");
    private static final Component field_851 = Component.translatable((String)"bf.message.intro.discord.message");
    private static final Component field_852 = Component.translatable((String)"bf.message.intro.discord.join.button.tip").withColor(ColorReferences.COLOR_THEME_DISCORD_HOVERED_SOLID);
    private static final Component field_853 = Component.translatable((String)"bf.message.intro.discord.skip.button.tip");
    private static final Component field_848 = Component.translatable((String)"bf.message.join.now");
    private static final Component field_849 = Component.translatable((String)"bf.message.skip");
    @NotNull
    public static final ResourceLocation field_847 = BFRes.loc("textures/gui/background/intro/background_discord.png");

    public void tick() {
        super.tick();
    }

    @Override
    @NotNull
    protected List<BF_162> method_657() {
        ObjectArrayList objectArrayList = new ObjectArrayList();
        objectArrayList.add(new BF_162(field_848, button -> {
            BFTitleScreen.openUrl(this.minecraft, "https://discord.blockfrontmc.com/");
            BFClientSettings.INTRO_DISCORD.method_1513(true);
            BFClientSettingsDisk.write(this.manager);
            this.minecraft.setScreen(DiscordIntroScreen.method_655());
        }).method_668(field_852).method_666(ColorReferences.COLOR_THEME_DISCORD_SOLID, ColorReferences.COLOR_THEME_DISCORD_HOVERED_SOLID).method_665(0xFFFFFF));
        objectArrayList.add(new BF_162(field_849, button -> {
            BFClientSettings.INTRO_DISCORD.method_1513(true);
            BFClientSettingsDisk.write(this.manager);
            this.minecraft.setScreen(DiscordIntroScreen.method_655());
        }).method_668(field_853));
        return objectArrayList;
    }

    @Override
    @Nullable
    protected SoundEvent method_656() {
        return (SoundEvent)BFSounds.GUI_INTRO_DISCORD.get();
    }

    @Override
    @Nullable
    protected ResourceLocation method_658() {
        return field_847;
    }

    @Override
    public void renderBackground(@Nonnull GuiGraphics guiGraphics, int n, int n2, float f) {
        super.renderBackground(guiGraphics, n, n2, f);
        PoseStack poseStack = guiGraphics.pose();
        int n3 = this.width / 2;
        int n4 = this.height / 2;
        BFRendering.centeredComponent2dWithShadow(poseStack, this.font, guiGraphics, (Component)field_850.copy().withStyle(BFStyles.BOLD), n3, n4 - 45, 2.0f);
        List list = this.font.split((FormattedText)field_851, (int)((float)this.width * 0.75f));
        BFRendering.renderTextLines(poseStack, this.font, (MultiBufferSource)guiGraphics.bufferSource(), (List<FormattedCharSequence>)list, n3, n4 - 10, 10, true);
        this.method_654(guiGraphics, 3, 4);
    }
}

