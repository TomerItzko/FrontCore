/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.screen.armory;

import com.boehmod.bflib.cloud.common.item.CloudItemStack;
import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.gui.widget.BFButton;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.BFMenuScreen;
import com.boehmod.blockfront.client.screen.armory.IReceivesArmorySelection;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.unnamed.BF_120;
import com.boehmod.blockfront.unnamed.BF_207;
import com.boehmod.blockfront.unnamed.BF_218;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.NotNull;

public class ArmorySelectScreen
extends BFMenuScreen {
    private static final Component field_1503 = Component.translatable((String)"bf.menu.button.back");
    private static final Component field_1504 = Component.translatable((String)"bf.menu.inventory.prompt.title");
    private static final Component field_1505 = Component.translatable((String)"bf.message.select");
    private static final Component field_1506 = Component.translatable((String)"bf.message.inventory.prompt.title").withStyle(ChatFormatting.BOLD);
    private static final int field_1501 = 50;
    @NotNull
    private final List<CloudItemStack> field_1497 = new ObjectArrayList();
    private final int field_1502;
    private boolean field_1498 = false;
    private BF_207<BF_247> field_1499;
    private Button field_1500;
    @NotNull
    private final Screen field_1507;

    public ArmorySelectScreen(@NotNull Screen screen, @NotNull List<CloudItemStack> list, int n) {
        super(field_1504);
        this.field_1507 = screen;
        this.field_1497.clear();
        this.field_1497.addAll(list);
        this.field_1502 = n;
    }

    @Override
    public void init() {
        int n = this.width / 4 * 3;
        this.field_1499 = new BF_207(this.width / 2 - n / 2, this.height / 2 - 25 + 10, n, 50, this);
        super.init();
        if (this.field_1498) {
            this.minecraft.setScreen(this.field_1507);
            return;
        }
        this.field_1498 = true;
    }

    @Override
    public void method_758() {
        super.method_758();
        int n = 120;
        int n2 = 20;
        int n3 = this.width / 2 - 60;
        int n4 = this.height - 70;
        this.field_1500 = new BFButton(n3, n4, 120, 20, field_1505, button -> {
            ObjectArrayList objectArrayList = new ObjectArrayList();
            Screen screen = this.field_1507;
            if (screen instanceof IReceivesArmorySelection) {
                IReceivesArmorySelection iReceivesArmorySelection = (IReceivesArmorySelection)screen;
                for (BF_247 bF_247 : this.field_1499.method_962()) {
                    if (!(bF_247 instanceof BF_247)) continue;
                    BF_247 bF_2472 = bF_247;
                    if (!bF_2472.field_1510) continue;
                    objectArrayList.add(bF_2472.field_1513);
                }
                this.minecraft.setScreen(this.field_1507);
                iReceivesArmorySelection.acceptSelected((List<CloudItemStack>)objectArrayList);
            }
        });
        this.addRenderableWidget((GuiEventListener)this.field_1500);
        int n5 = 20;
        Button.OnPress onPress = button -> this.minecraft.setScreen(this.field_1507);
        this.addRenderableWidget((GuiEventListener)new BFButton(5, 18, 20, 20, (Component)Component.empty(), onPress).texture(RETURN_ICON).size(20, 20).displayType(BFButton.DisplayType.NONE).tip(field_1503));
    }

    @Override
    public void method_774() {
        super.method_774();
        for (CloudItemStack cloudItemStack : this.field_1497) {
            this.field_1499.method_950(new BF_247(this, cloudItemStack, 50, 50));
            this.field_1499.method_947();
        }
        this.method_764(this.field_1499);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
        super.render(guiGraphics, n, n2, f);
        ClientPlayerDataHandler clientPlayerDataHandler = (ClientPlayerDataHandler)this.manager.getPlayerDataHandler();
        PoseStack poseStack = guiGraphics.pose();
        float f2 = BFRendering.getRenderTime();
        int n3 = this.width / 2;
        int n4 = this.method_1073();
        PlayerCloudData playerCloudData = clientPlayerDataHandler.getCloudData(this.minecraft);
        if (this.field_1500 != null) {
            boolean bl = this.field_1500.active = n4 > 0 && n4 <= this.field_1502;
        }
        if (this.field_1499 != null) {
            BFRendering.rectangleWithDarkShadow(guiGraphics, this.field_1499.method_559() - 1, this.field_1499.method_560() - 1, this.field_1499.method_558() + 2, this.field_1499.height() + 2, BFRendering.translucentBlack());
            this.field_1499.render(this.minecraft, this.manager, clientPlayerDataHandler, poseStack, guiGraphics, this.font, playerCloudData, n, n2, f2, f);
        }
        MutableComponent mutableComponent = Component.literal((String)String.valueOf(this.field_1502)).withColor(ColorReferences.COLOR_THEME_YELLOW_SOLID);
        MutableComponent mutableComponent2 = Component.translatable((String)"bf.message.inventory.prompt.description", (Object[])new Object[]{mutableComponent}).withStyle(ChatFormatting.GRAY);
        BFRendering.centeredString(this.font, guiGraphics, field_1506, n3, 60);
        BFRendering.centeredString(this.font, guiGraphics, (Component)mutableComponent2, n3, 80);
    }

    private int method_1073() {
        return (int)this.field_1499.method_962().stream().filter(bF_247 -> {
            if (!(bF_247 instanceof BF_247)) return false;
            BF_247 bF_2472 = bF_247;
            if (!bF_2472.field_1510) return false;
            return true;
        }).count();
    }

    public static class BF_247
    extends BF_218 {
        @NotNull
        private final CloudItemStack field_1513;
        @NotNull
        private final ArmorySelectScreen field_1509;
        @NotNull
        private final BF_120 field_1508;
        private final int field_1511;
        private final int field_1512;
        private boolean field_1510 = false;

        public BF_247(@NotNull ArmorySelectScreen armorySelectScreen, @NotNull CloudItemStack cloudItemStack, int n, int n2) {
            this.field_1509 = armorySelectScreen;
            this.field_1513 = cloudItemStack;
            this.field_1511 = n;
            this.field_1512 = n2;
            this.field_1508 = new BF_120(armorySelectScreen, cloudItemStack, 0, 0, n, n2, false, false);
        }

        @Override
        public void method_982(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, int n, int n2, int n3) {
            super.method_982(minecraft, bFClientManager, clientPlayerDataHandler, n, n2, n3);
            if (this.method_990()) {
                this.field_1510 = !this.field_1510;
                minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.GUI_BUTTON_PRESS.get()), (float)1.0f));
            }
        }

        @Override
        public void method_987(@NotNull Minecraft minecraft) {
        }

        @Override
        public void method_981(@NotNull PoseStack poseStack, @NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull GuiGraphics guiGraphics, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull Font font, int n, int n2, float f, float f2) {
            super.method_981(poseStack, minecraft, bFClientManager, guiGraphics, clientPlayerDataHandler, font, n, n2, f, f2);
            poseStack.pushPose();
            poseStack.translate((float)this.field_1357, (float)this.field_1358, 0.0f);
            this.field_1508.method_524(minecraft, guiGraphics, font, n, n2, f2);
            poseStack.popPose();
            if (this.field_1510) {
                BFRendering.rectangle(guiGraphics, this.field_1357, this.field_1358, this.method_989(), 1, ColorReferences.COLOR_THEME_YELLOW_SOLID);
                BFRendering.rectangle(guiGraphics, this.field_1357, this.field_1358 + this.height() - 1, this.method_989(), 1, ColorReferences.COLOR_THEME_YELLOW_SOLID);
                BFRendering.rectangle(guiGraphics, this.field_1357, this.field_1358, 1, this.height(), ColorReferences.COLOR_THEME_YELLOW_SOLID);
                BFRendering.rectangle(guiGraphics, this.field_1357 + this.method_989() - 1, this.field_1358, 1, this.height(), ColorReferences.COLOR_THEME_YELLOW_SOLID);
            }
        }

        @Override
        public boolean method_990() {
            if (this.field_1510) {
                return true;
            }
            return this.field_1509.method_1073() < this.field_1509.field_1502;
        }

        @Override
        public int height() {
            return this.field_1512;
        }

        @Override
        public int method_989() {
            return this.field_1511;
        }
    }
}

