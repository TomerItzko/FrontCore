/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.RequestType
 *  com.boehmod.bflib.cloud.common.item.CloudItemType
 *  net.minecraft.client.User
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.network.chat.Component
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.screen.title;

import com.boehmod.bflib.cloud.common.RequestType;
import com.boehmod.bflib.cloud.common.item.CloudItemType;
import com.boehmod.blockfront.client.gui.widget.BFButton;
import com.boehmod.blockfront.client.gui.widget.NewItemWidget;
import com.boehmod.blockfront.client.screen.title.BFTitleScreen;
import com.boehmod.blockfront.cloud.client.ClientConnectionManager;
import com.boehmod.blockfront.cloud.common.CloudRequestManager;
import com.boehmod.blockfront.util.BFRes;
import java.util.Locale;
import java.util.UUID;
import net.minecraft.client.User;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public final class ArmoryTitleScreen
extends BFTitleScreen {
    private static final Component field_945 = Component.translatable((String)"bf.screen.menu.armory");
    private static final Component field_946 = Component.translatable((String)"bf.menu.button.nav.text.armory");
    public static final int field_940 = 16;
    public static final int field_941 = 233;
    public static final int field_942 = 136;
    public static final int field_943 = 114;
    public static final int field_944 = 16;
    @NotNull
    public static CloudItemType field_939 = CloudItemType.ALL;

    public ArmoryTitleScreen() {
        super(field_945, field_946);
    }

    @Override
    public void init() {
        super.init();
        User user = this.minecraft.getUser();
        UUID uUID = user.getProfileId();
        ClientConnectionManager clientConnectionManager = (ClientConnectionManager)this.manager.getConnectionManager();
        CloudRequestManager cloudRequestManager = clientConnectionManager.getRequester();
        cloudRequestManager.push(RequestType.PLAYER_INVENTORY_DEFAULTS, uUID);
        cloudRequestManager.push(RequestType.PLAYER_INVENTORY_SHOWCASE, uUID);
    }

    @Override
    public void method_758() {
        super.method_758();
        int n = this.width / 2 - 173;
        int n2 = n + 233 + 114 + 3;
        int n3 = 0;
        for (CloudItemType cloudItemType : CloudItemType.values()) {
            if (!cloudItemType.isArmoryFilter()) continue;
            this.addRenderableWidget((GuiEventListener)new BFButton(n2, 56 + 19 * n3++, 16, 16, (Component)Component.empty(), button -> this.method_712(cloudItemType)).displayType(BFButton.DisplayType.NONE).tip((Component)Component.literal((String)cloudItemType.name())).texture(BFRes.loc("textures/gui/menu/icons/armory_filter_" + cloudItemType.toString().toLowerCase(Locale.ROOT) + ".png")).size(16).enabled(bFButton -> field_939 != cloudItemType));
        }
    }

    @Override
    public void method_774() {
        super.method_774();
        int n = this.width / 2 - 173;
        int n2 = n + 114;
        int n3 = 56;
        int n4 = 233;
        int n5 = 136;
        this.method_764(new NewItemWidget(n2, 56, 233, 136, this));
    }

    public void method_712(@NotNull CloudItemType cloudItemType) {
        field_939 = cloudItemType;
        if (this.minecraft.screen instanceof ArmoryTitleScreen) {
            NewItemWidget.field_583 = 0;
            this.minecraft.setScreen((Screen)new ArmoryTitleScreen());
        }
    }
}

