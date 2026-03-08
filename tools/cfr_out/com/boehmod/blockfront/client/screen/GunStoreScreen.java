/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.common.ColorReferences
 *  com.mojang.blaze3d.vertex.PoseStack
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  it.unimi.dsi.fastutil.objects.ObjectList
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.resources.sounds.SimpleSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.util.Mth
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.screen;

import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.gui.widget.BFButton;
import com.boehmod.blockfront.client.player.FakePlayer;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.BFMenuScreen;
import com.boehmod.blockfront.client.screen.BFScreen;
import com.boehmod.blockfront.common.match.DivisionData;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameShopItem;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.unnamed.BF_199;
import com.boehmod.blockfront.unnamed.BF_201;
import com.boehmod.blockfront.util.FormatUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

public abstract class GunStoreScreen
extends BFScreen {
    private static final Component TITLE = Component.translatable((String)"bf.screen.ingame.gun.store");
    public static final MutableComponent SELECTED_MESSAGE = Component.translatable((String)"bf.message.selected").withStyle(ChatFormatting.BOLD);
    protected final AbstractGame<?, ?, ?> field_1266;
    protected final List<BF_199> field_1262 = new ObjectArrayList();
    protected final Screen field_1269;
    protected final List<GameShopItem> field_1263;
    protected BF_199 field_1264 = null;
    protected int field_1267 = 20;

    protected GunStoreScreen(Screen screen, List<GameShopItem> list, AbstractGame<?, ?, ?> abstractGame) {
        super(TITLE);
        this.field_1263 = list;
        this.field_1269 = screen;
        this.field_1266 = abstractGame;
    }

    public void method_922(GameShopItem gameShopItem, String[] stringArray) {
        BF_199 bF_199 = new BF_199(0, 0, gameShopItem, stringArray, 0, 0);
        this.field_1262.add(bF_199);
    }

    public void init() {
        super.init();
        int n = this.width / 2;
        BFButton bFButton = new BFButton(n - 150, 18, 40, 10, (Component)Component.literal((String)"< Back"), button -> this.minecraft.setScreen(this.field_1269));
        bFButton.displayType(BFButton.DisplayType.NONE);
        this.addRenderableWidget((GuiEventListener)bFButton);
        this.method_921();
    }

    public void method_921() {
        int n = this.width / 2;
        int n2 = this.height / 2;
        int n3 = n - 140;
        int n4 = n2 - 72;
        int n5 = 100;
        int n6 = 20;
        int n7 = this.field_1262.size();
        for (int i = 0; i < n7; ++i) {
            BF_199 bF_199 = this.field_1262.get(i);
            bF_199.method_917(n3);
            bF_199.method_918(n4 + i * 22);
            bF_199.method_919(100);
            bF_199.method_920(20);
        }
    }

    public boolean keyPressed(int n, int n2, int n3) {
        assert (this.minecraft != null);
        this.minecraft.setScreen(this.field_1269);
        return true;
    }

    public void tick() {
        assert (this.minecraft != null);
        if (this.field_1267-- <= 0) {
            this.minecraft.setScreen((Screen)new BF_201(null, this.field_1263, this.field_1266));
        }
    }

    public boolean isPauseScreen() {
        return false;
    }

    public boolean mouseClicked(double d, double d2, int n) {
        assert (this.minecraft != null);
        int n2 = this.field_1262.size();
        for (int i = 0; i < n2; ++i) {
            BF_199 bF_199 = this.field_1262.get(i);
            if (!bF_199.method_914((int)d, (int)d2) || n != 0) continue;
            this.method_924(bF_199.method_912(), i + 1);
            this.minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.GUI_BUTTON_PRESS.get()), (float)1.0f));
        }
        return super.mouseClicked(d, d2, n);
    }

    public void render(GuiGraphics guiGraphics, int n, int n2, float f) {
        Object object;
        float f2 = BFRendering.getRenderTime();
        PoseStack poseStack = guiGraphics.pose();
        int n3 = this.width / 2;
        int n4 = this.height / 2;
        BFRendering.rectangleWithDarkShadow(guiGraphics, 0, 0, this.width, 30, BFRendering.translucentBlack());
        BFRendering.rectangleWithDarkShadow(guiGraphics, 0, this.height - 30, this.width, 30, BFRendering.translucentBlack());
        MutableComponent mutableComponent = Component.translatable((String)"bf.message.weapon.vendor").withStyle(ChatFormatting.BOLD);
        BFRendering.centeredString(this.font, guiGraphics, (Component)mutableComponent, n3, 10);
        super.render(guiGraphics, n, n2, f);
        int n5 = this.field_1262.size() * 22;
        BFRendering.rectangleWithDarkShadow(guiGraphics, n3 - 150, n4 - 80, 120, n5 + 15, BFRendering.translucentBlack());
        this.field_1262.forEach(bF_199 -> {
            bF_199.method_916(guiGraphics, this.font);
            if (bF_199.method_914(n, n2)) {
                this.field_1264 = bF_199;
                bF_199.method_915(this.manager, guiGraphics, 1, f2);
            }
        });
        int n6 = n3 - 25;
        int n7 = n4 - 80;
        int n8 = 100;
        int n9 = 160;
        BFRendering.rectangleWithDarkShadow(guiGraphics, n6, n7, 100, 160, BFRendering.translucentBlack());
        BFRendering.drawString(this.font, guiGraphics, (Component)SELECTED_MESSAGE, n3 - 20, n4 - 75);
        int n10 = 50;
        int n11 = 80;
        if (this.field_1264 != null) {
            Item item = this.field_1264.method_912();
            FakePlayer fakePlayer = BFRendering.ENVIRONMENT.getPlayer(this.minecraft);
            if (fakePlayer.getMainHandItem().isEmpty() || fakePlayer.getMainHandItem().getItem() != item) {
                fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack((ItemLike)item));
            }
            BFRendering.enableScissor(guiGraphics, n6, n7, 100, 160);
            if (this.field_1266 != null) {
                assert (this.minecraft.player != null);
                object = ((AbstractGamePlayerManager)this.field_1266.getPlayerManager()).getPlayerTeam(this.minecraft.player.getUUID());
                if (object != null) {
                    DivisionData divisionData = ((GameTeam)object).getDivisionData(this.field_1266);
                    BFRendering.centeredTexture(poseStack, guiGraphics, divisionData.getCountry().getNationIcon(), n6 + 50, n7 + 80 - 10, 85, 85, 0.0f, 0.5f);
                    BFRendering.centeredTexture(poseStack, guiGraphics, BFMenuScreen.BACKSHADOW, n6 + 50, n7 + 80 + 20, 100, 100);
                    BFRendering.centeredTexture(poseStack, guiGraphics, BFMenuScreen.BACKSHADOW, n6 + 50, n7 + 80 + 20, 100, 100);
                    BFRendering.rectangle(guiGraphics, n6, n7, 1, 160, BFRendering.translucentBlack());
                    BFRendering.rectangle(guiGraphics, n6 + 100 - 1, n7, 1, 160, BFRendering.translucentBlack());
                    poseStack.pushPose();
                    poseStack.translate(0.0f, 0.0f, 100.0f);
                    int n12 = 6 * (item.hashCode() % 5);
                    float f3 = Mth.sin((float)(f2 / 50.0f));
                    BFRendering.entity(this.playerDataHandler, poseStack, guiGraphics, this.minecraft, (LivingEntity)fakePlayer, n6 + 50, n7 + 160 + 60, 90.0f, 0.0f, (float)(-30 + n12) + f3, 0.0f);
                    poseStack.popPose();
                    guiGraphics.disableScissor();
                    int n13 = 60;
                    BFRendering.rectangleGradient(guiGraphics, n6, n7 + 100, 100, 60, ColorReferences.COLOR_BLACK_TRANSPARENT, ColorReferences.COLOR_BLACK_SOLID);
                    BFRendering.drawString(this.font, guiGraphics, item.getName(item.getDefaultInstance()), n3 - 20, n4 - 65);
                    ObjectList<String> objectList = FormatUtils.parseMarkup(this.font, FormatUtils.joinWithSpaces((List<String>)new ObjectArrayList(Arrays.asList(this.field_1264.method_913()))), 190);
                    int n14 = objectList.size();
                    for (int i = 0; i < n14; ++i) {
                        BFRendering.component2d(poseStack, this.font, guiGraphics, (Component)Component.literal((String)((String)objectList.get(i))), n3 - 22, n4 + 45 + i * 5, 0.5f);
                    }
                }
            }
        }
        for (FakePlayer fakePlayer : this.renderables) {
            if (!(fakePlayer instanceof BFButton)) continue;
            object = (BFButton)((Object)fakePlayer);
            ((BFButton)((Object)object)).method_379(this.minecraft, poseStack, this.font, guiGraphics, n, n2, f2);
        }
    }

    public abstract void method_924(@NotNull Item var1, int var2);
}

