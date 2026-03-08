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
import com.boehmod.blockfront.client.net.ConnectionMode;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.intro.BFIntroScreen;
import com.boehmod.blockfront.client.settings.BFClientSettings;
import com.boehmod.blockfront.client.settings.BFClientSettingsDisk;
import com.boehmod.blockfront.cloud.client.ClientConnectionManager;
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

public class ContentWarningIntroScreen
extends BFIntroScreen {
    private static final Component field_839 = Component.translatable((String)"bf.message.accept");
    private static final Component field_840 = Component.translatable((String)"bf.message.decline");
    private static final Component field_841 = Component.translatable((String)"bf.message.intro.content.title");
    private static final Component field_842 = field_839.copy().withColor(ColorReferences.COLOR_TEAM_ALLIES_SOLID);
    private static final Component field_843 = field_840.copy().withColor(ColorReferences.COLOR_THEME_RED_SOLID);
    private static final Component field_844 = Component.translatable((String)"bf.message.intro.content.message", (Object[])new Object[]{field_842, field_843});
    private static final Component field_845 = Component.translatable((String)"bf.message.intro.content.tip.accept").withColor(ColorReferences.COLOR_TEAM_ALLIES_HOVERED_SOLID);
    private static final Component field_846 = Component.translatable((String)"bf.message.intro.content.tip.decline");
    @NotNull
    public static final ResourceLocation field_838 = BFRes.loc("textures/gui/background/intro/background_content.png");

    @Override
    @NotNull
    protected List<BF_162> method_657() {
        ObjectArrayList objectArrayList = new ObjectArrayList();
        objectArrayList.add(new BF_162(field_839, button -> {
            BFClientSettings.INTRO_CONTENT.method_1513(true);
            BFClientSettingsDisk.write(this.manager);
            this.minecraft.setScreen(ContentWarningIntroScreen.method_655());
        }).method_668(field_845).method_666(ColorReferences.COLOR_TEAM_ALLIES_SOLID, ColorReferences.COLOR_TEAM_ALLIES_HOVERED_SOLID).method_665(0xFFFFFF));
        objectArrayList.add(new BF_162(field_840, button -> {
            ((ClientConnectionManager)this.manager.getConnectionManager()).setConnectionMode(ConnectionMode.UNDECIDED);
            this.minecraft.setScreen(ContentWarningIntroScreen.method_655());
        }).method_668(field_846));
        return objectArrayList;
    }

    @Override
    @Nullable
    protected SoundEvent method_656() {
        return (SoundEvent)BFSounds.GUI_INTRO_CONTENT.get();
    }

    @Override
    @Nullable
    protected ResourceLocation method_658() {
        return field_838;
    }

    @Override
    public void renderBackground(@Nonnull GuiGraphics guiGraphics, int n, int n2, float f) {
        super.renderBackground(guiGraphics, n, n2, f);
        PoseStack poseStack = guiGraphics.pose();
        int n3 = this.width / 2;
        int n4 = this.height / 2;
        BFRendering.centeredComponent2dWithShadow(poseStack, this.font, guiGraphics, (Component)field_841.copy().withStyle(BFStyles.BOLD), n3, n4 - 45, 2.0f);
        List list = this.font.split((FormattedText)field_844, (int)((float)this.width * 0.75f));
        BFRendering.renderTextLines(poseStack, this.font, (MultiBufferSource)guiGraphics.bufferSource(), (List<FormattedCharSequence>)list, n3, n4 - 10, 10, true);
        this.method_654(guiGraphics, 2, 4);
    }
}

