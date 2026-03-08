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

public class PatreonIntroScreen
extends BFIntroScreen {
    private static final Component field_6979 = Component.translatable((String)"bf.message.intro.patreon.title");
    private static final Component field_6980 = Component.translatable((String)"bf.message.intro.patreon.message");
    private static final Component field_6981 = Component.translatable((String)"bf.message.intro.patreon.visit.button.tip").withColor(ColorReferences.COLOR_THEME_PATREON_HOVERED_SOLID);
    private static final Component field_6982 = Component.translatable((String)"bf.message.intro.patreon.skip.button.tip");
    private static final Component field_6983 = Component.translatable((String)"bf.message.visit");
    private static final Component field_6984 = Component.translatable((String)"bf.message.skip");
    @NotNull
    private static final ResourceLocation field_6978 = BFRes.loc("textures/gui/background/intro/background_patreon.png");

    public void tick() {
        super.tick();
    }

    @Override
    @NotNull
    protected List<BF_162> method_657() {
        ObjectArrayList objectArrayList = new ObjectArrayList();
        objectArrayList.add(new BF_162(field_6983, button -> {
            BFTitleScreen.openUrl(this.minecraft, "https://patreon.blockfrontmc.com/");
            BFClientSettings.INTRO_PATREON.method_1513(true);
            BFClientSettingsDisk.write(this.manager);
            this.minecraft.setScreen(PatreonIntroScreen.method_655());
        }).method_668(field_6981).method_666(ColorReferences.COLOR_THEME_PATREON_SOLID, ColorReferences.COLOR_THEME_PATREON_HOVERED_SOLID).method_665(0xFFFFFF));
        objectArrayList.add(new BF_162(field_6984, button -> {
            BFClientSettings.INTRO_PATREON.method_1513(true);
            BFClientSettingsDisk.write(this.manager);
            this.minecraft.setScreen(PatreonIntroScreen.method_655());
        }).method_668(field_6982));
        return objectArrayList;
    }

    @Override
    @Nullable
    protected SoundEvent method_656() {
        return (SoundEvent)BFSounds.GUI_INTRO_PATREON.get();
    }

    @Override
    @Nullable
    protected ResourceLocation method_658() {
        return field_6978;
    }

    @Override
    public void renderBackground(@Nonnull GuiGraphics guiGraphics, int n, int n2, float f) {
        super.renderBackground(guiGraphics, n, n2, f);
        PoseStack poseStack = guiGraphics.pose();
        int n3 = this.width / 2;
        int n4 = this.height / 2;
        BFRendering.centeredComponent2dWithShadow(poseStack, this.font, guiGraphics, (Component)field_6979.copy().withStyle(BFStyles.BOLD), n3, n4 - 45, 2.0f);
        List list = this.font.split((FormattedText)field_6980, (int)((float)this.width * 0.75f));
        BFRendering.renderTextLines(poseStack, this.font, (MultiBufferSource)guiGraphics.bufferSource(), (List<FormattedCharSequence>)list, n3, n4 - 10, 10, true);
        this.method_654(guiGraphics, 4, 4);
    }
}

