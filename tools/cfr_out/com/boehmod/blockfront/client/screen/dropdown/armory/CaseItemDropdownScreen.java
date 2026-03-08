/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.item.CloudItemStack
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.network.chat.Component
 */
package com.boehmod.blockfront.client.screen.dropdown.armory;

import com.boehmod.bflib.cloud.common.item.CloudItemStack;
import com.boehmod.blockfront.client.screen.armory.ArmoryOpenCaseScreen;
import com.boehmod.blockfront.client.screen.dropdown.armory.ArmoryDropdownScreen;
import com.boehmod.blockfront.client.screen.prompt.confirm.BFConfirmPromptScreen;
import com.boehmod.blockfront.cloud.PlayerCloudInventory;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class CaseItemDropdownScreen
extends ArmoryDropdownScreen {
    private static final Component field_733 = Component.translatable((String)"bf.message.open");
    private static final Component field_734 = Component.translatable((String)"bf.message.delete");
    private static final Component field_735 = Component.translatable((String)"bf.screen.dropdown.item.case");
    private static final Component field_736 = Component.translatable((String)"bf.message.prompt.item.delete.case.title");
    private static final Component field_737 = Component.translatable((String)"bf.message.prompt.item.delete.case");

    public CaseItemDropdownScreen(Screen screen, int n, int n2, int n3, int n4, CloudItemStack cloudItemStack) {
        super(screen, n, n2, n3, n4, field_735, cloudItemStack);
        this.method_744(field_733);
        this.method_744(field_734);
    }

    @Override
    public void method_748(int n) {
        switch (n) {
            case 1: {
                this.minecraft.setScreen((Screen)new ArmoryOpenCaseScreen(this.field_709));
                break;
            }
            case 2: {
                BFConfirmPromptScreen bFConfirmPromptScreen = new BFConfirmPromptScreen(this.field_1036, field_736, bl -> {
                    if (bl) {
                        PlayerCloudData playerCloudData = this.playerDataHandler.getCloudData(this.minecraft);
                        ((PlayerCloudInventory)playerCloudData.getInventory()).deleteItem(this.manager, this.field_709);
                    }
                });
                bFConfirmPromptScreen.method_1084(field_737);
                this.minecraft.setScreen((Screen)bFConfirmPromptScreen);
            }
        }
    }
}

