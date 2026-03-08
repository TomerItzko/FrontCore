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

public class RulesIntroScreen
extends BFIntroScreen {
    private static final Component field_892 = Component.translatable((String)"bf.message.intro.rules.title");
    private static final Component field_893 = Component.translatable((String)"bf.message.intro.rules.message");
    private static final Component field_894 = Component.translatable((String)"bf.message.intro.accept.rules.button.tip").withColor(ColorReferences.COLOR_TEAM_ALLIES_HOVERED_SOLID);
    private static final Component field_895 = Component.translatable((String)"bf.message.intro.rules.button");
    private static final Component field_896 = Component.translatable((String)"bf.message.intro.rules.button.tip");
    private static final Component field_897 = Component.translatable((String)"bf.message.intro.privacy.button");
    private static final Component field_898 = Component.translatable((String)"bf.message.intro.privacy.button.tip");
    private static final Component field_899 = Component.translatable((String)"bf.message.accept");
    @NotNull
    public static final ResourceLocation field_891 = BFRes.loc("textures/gui/background/intro/background_rules.png");

    public void tick() {
        super.tick();
    }

    @Override
    @NotNull
    protected List<BF_162> method_657() {
        ObjectArrayList objectArrayList = new ObjectArrayList();
        objectArrayList.add(new BF_162(field_899, button -> {
            BFClientSettings.INTRO_RULES.method_1513(true);
            BFClientSettingsDisk.write(this.manager);
            this.minecraft.setScreen(RulesIntroScreen.method_655());
        }).method_668(field_894).method_666(ColorReferences.COLOR_TEAM_ALLIES_SOLID, ColorReferences.COLOR_TEAM_ALLIES_HOVERED_SOLID).method_665(0xFFFFFF));
        objectArrayList.add(new BF_162(field_895, button -> BFTitleScreen.openUrl(this.minecraft, "https://www.blockfrontmc.com/rules")).method_668(field_896));
        objectArrayList.add(new BF_162(field_897, button -> BFTitleScreen.openUrl(this.minecraft, "https://www.blockfrontmc.com/privacy")).method_668(field_898));
        return objectArrayList;
    }

    @Override
    @Nullable
    protected SoundEvent method_656() {
        return (SoundEvent)BFSounds.GUI_INTRO_RULES.get();
    }

    @Override
    @Nullable
    protected ResourceLocation method_658() {
        return field_891;
    }

    @Override
    public void renderBackground(@Nonnull GuiGraphics guiGraphics, int n, int n2, float f) {
        super.renderBackground(guiGraphics, n, n2, f);
        PoseStack poseStack = guiGraphics.pose();
        int n3 = this.width / 2;
        int n4 = this.height / 2;
        BFRendering.centeredComponent2dWithShadow(poseStack, this.font, guiGraphics, (Component)field_892.copy().withStyle(BFStyles.BOLD), n3, n4 - 45, 2.0f);
        List list = this.font.split((FormattedText)field_893, (int)((float)this.width * 0.75f));
        BFRendering.renderTextLines(poseStack, this.font, (MultiBufferSource)guiGraphics.bufferSource(), (List<FormattedCharSequence>)list, n3, n4 - 10, 10, true);
        this.method_654(guiGraphics, 1, 4);
    }
}

