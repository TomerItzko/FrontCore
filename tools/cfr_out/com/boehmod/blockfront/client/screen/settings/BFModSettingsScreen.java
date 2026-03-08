/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.PopupType
 *  com.boehmod.bflib.common.ColorReferences
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  javax.annotation.Nullable
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.gui.screens.options.OptionsScreen
 *  net.minecraft.network.chat.Component
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.screen.settings;

import com.boehmod.bflib.cloud.common.PopupType;
import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.gui.BFNotification;
import com.boehmod.blockfront.client.gui.widget.BFButton;
import com.boehmod.blockfront.client.gui.widget.PopupButton;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.BFMenuScreen;
import com.boehmod.blockfront.client.screen.SidebarScreen;
import com.boehmod.blockfront.client.settings.BFClientSetting;
import com.boehmod.blockfront.client.settings.BFClientSettingCategories;
import com.boehmod.blockfront.client.settings.BFClientSettingCategory;
import com.boehmod.blockfront.client.settings.BFClientSettings;
import com.boehmod.blockfront.client.settings.BFClientSettingsDisk;
import com.boehmod.blockfront.unnamed.BF_205;
import com.boehmod.blockfront.unnamed.BF_209;
import com.boehmod.blockfront.unnamed.BF_218;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class BFModSettingsScreen
extends BFMenuScreen {
    private static final Component field_1285 = Component.translatable((String)"bf.message.okay");
    private static final Component field_1278 = Component.translatable((String)"bf.settings.save.prompt.title").withColor(0xFFFFFF);
    private static final Component field_1279 = Component.translatable((String)"bf.settings.save.prompt.message");
    private static final Component field_1280 = Component.translatable((String)"bf.menu.button.back");
    private static final Component field_1281 = Component.translatable((String)"bf.settings.save");
    private static final Component field_1282 = Component.translatable((String)"bf.settings.discard");
    private static final Component field_1283 = Component.translatable((String)"bf.settings.save.message");
    private static final Component field_1284 = Component.translatable((String)"bf.settings.discard.message");
    @NotNull
    protected final Screen field_1286;
    @Nullable
    private BF_209<BF_218> field_1277 = null;
    @NotNull
    private final BFClientSettingCategory field_1276;
    @Nullable
    private BFButton field_1287;
    @Nullable
    private BFButton field_1288;

    public BFModSettingsScreen(@NotNull Screen screen) {
        this(screen, BFClientSettingCategories.NOTIFICATIONS);
    }

    public BFModSettingsScreen(@NotNull Screen screen, @NotNull BFClientSettingCategory bFClientSettingCategory) {
        super((Component)Component.translatable((String)bFClientSettingCategory.getName()));
        this.field_1286 = screen;
        this.field_1276 = bFClientSettingCategory;
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void init() {
        super.init();
        int n = this.width / 2;
        int n2 = 40;
        int n3 = 2;
        int n4 = 7;
        int n5 = 0;
        ObjectArrayList objectArrayList = new ObjectArrayList();
        objectArrayList.add(new BFButton(n, 40, 0, 7, (Component)Component.literal((String)"Minecraft").withStyle(ChatFormatting.BOLD), button -> this.minecraft.setScreen((Screen)new OptionsScreen((Screen)this, this.minecraft.options))).size(12, 12).alignment(BFButton.Alignment.CENTER_BOTTOM).method_389(0.5f).method_368(0.0f, 4.0f).displayType(BFButton.DisplayType.NONE));
        for (BFClientSettingCategory object : BFClientSettingCategories.INSTANCES) {
            if (!object.method_1517()) continue;
            objectArrayList.add(new BFButton(n, 40, 0, 7, (Component)Component.translatable((String)object.getName()).withStyle(ChatFormatting.BOLD), button -> this.minecraft.setScreen((Screen)new BFModSettingsScreen(this.field_1286, object))).size(12, 12).alignment(BFButton.Alignment.CENTER_BOTTOM).method_389(0.5f).method_368(0.0f, 4.0f).displayType(BFButton.DisplayType.NONE));
        }
        for (BFButton bFButton : objectArrayList) {
            bFButton.setWidth((this.font.width(bFButton.getMessage().getString()) + 15) / 2 + 4);
            n5 += bFButton.getWidth() + 2;
        }
        int n6 = n - n5 / 2;
        boolean bl = false;
        for (BFButton bFButton : objectArrayList) {
            void var8_13;
            bFButton.setX(n6 + var8_13);
            this.addRenderableWidget((GuiEventListener)bFButton);
            var8_13 += bFButton.getWidth() + 2;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.field_1287 != null) {
            this.field_1287.visible = BFClientSettings.isUnsaved;
        }
        if (this.field_1288 != null) {
            this.field_1288.visible = BFClientSettings.isUnsaved;
        }
        if (BFModSettingsScreen.method_769(this.minecraft)) {
            this.method_930();
        }
    }

    @Override
    public void renderBackground(@NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
        super.renderBackground(guiGraphics, n, n2, f);
        if (this.field_1277 != null) {
            BFRendering.rectangleWithDarkShadow(guiGraphics, this.field_1277.method_559(), this.field_1277.method_560(), this.field_1277.method_558(), this.field_1277.height(), BFRendering.translucentBlack());
        }
    }

    @Override
    public void method_758() {
        super.method_758();
        int n = 20;
        BFButton bFButton = new BFButton(5, 18, 20, 20, (Component)Component.empty(), button2 -> {
            if (BFClientSettings.isUnsaved) {
                BFClientManager.showPopup(field_1278, field_1279, PopupType.FAIL, new PopupButton(field_1285, null, button -> this.minecraft.setScreen((Screen)this)));
            } else {
                this.method_930();
            }
        });
        this.addRenderableWidget((GuiEventListener)bFButton);
        bFButton.texture(RETURN_ICON);
        bFButton.size(20, 20);
        bFButton.displayType(BFButton.DisplayType.NONE);
        bFButton.tip(field_1280);
        this.addRenderableWidget((GuiEventListener)bFButton);
        int n2 = this.width / 2;
        int n3 = 11;
        int n4 = 65;
        int n5 = this.height - 43 - 11;
        this.field_1288 = new BFButton(n2 - 67, n5, 65, 11, field_1282, button -> this.method_932()).method_368(0.0f, 1.0f).textStyle(BFButton.TextStyle.SHADOW).displayType(BFButton.DisplayType.SHADOW);
        this.addRenderableWidget((GuiEventListener)this.field_1288);
        this.field_1288.visible = BFClientSettings.isUnsaved;
        this.field_1287 = new BFButton(n2 + 2, n5, 65, 11, field_1281, button -> this.method_931()).method_368(0.0f, 1.0f).textStyle(BFButton.TextStyle.SHADOW).displayType(BFButton.DisplayType.SHADOW).method_395(ColorReferences.COLOR_TEAM_ALLIES_SOLID).method_397(ColorReferences.COLOR_TEAM_ALLIES_HOVERED_SOLID);
        this.addRenderableWidget((GuiEventListener)this.field_1287);
        this.field_1287.visible = BFClientSettings.isUnsaved;
    }

    private void method_931() {
        BFClientSettingsDisk.write(this.manager);
        BFClientSettings.isUnsaved = false;
        BFNotification.show(this.minecraft, field_1283);
    }

    private void method_932() {
        BFClientSettingsDisk.read(this.manager);
        BFClientSettings.isUnsaved = false;
        BFNotification.show(this.minecraft, field_1284);
    }

    public void method_930() {
        BFClientSettingsDisk.read(this.manager);
        BFClientSettingsDisk.write(this.manager);
        Screen screen = this.field_1286;
        if (screen instanceof SidebarScreen) {
            SidebarScreen sidebarScreen = (SidebarScreen)screen;
            sidebarScreen.field_1030 = true;
        }
        this.minecraft.setScreen(this.field_1286);
    }

    @Override
    public void method_774() {
        super.method_774();
        int n = this.width / 2;
        int n2 = n - 160;
        int n3 = 4;
        int n4 = n2 + 25 + 4;
        int n5 = 51;
        int n6 = 272;
        int n7 = this.height - 108;
        this.field_1277 = new BF_209(n4, 51, 272, n7, this);
        this.field_1277.method_556(true);
        for (BFClientSetting bFClientSetting : this.field_1276.method_1519()) {
            this.field_1277.method_950(new BF_205(bFClientSetting));
        }
        this.method_764(this.field_1277);
    }
}

