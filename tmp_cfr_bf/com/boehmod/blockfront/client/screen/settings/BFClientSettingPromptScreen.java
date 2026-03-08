/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.screen.settings;

import com.boehmod.blockfront.client.screen.prompt.text.BFTextPromptScreen;
import com.boehmod.blockfront.client.settings.BFClientSettingPrompt;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class BFClientSettingPromptScreen
extends BFTextPromptScreen {
    @NotNull
    private final BFClientSettingPrompt field_1531;

    public BFClientSettingPromptScreen(@NotNull Screen screen, @NotNull BFClientSettingPrompt bFClientSettingPrompt) {
        super(screen, (Component)Component.translatable((String)bFClientSettingPrompt.getName()));
        this.field_1531 = bFClientSettingPrompt;
    }

    @Override
    public void confirm() {
        this.field_1531.method_1524(this.field_1534.getValue());
        this.method_1081();
    }
}

