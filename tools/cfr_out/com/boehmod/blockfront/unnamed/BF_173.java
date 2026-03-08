/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.common.ColorReferences
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.math.Axis
 *  it.unimi.dsi.fastutil.objects.ObjectList
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.resources.language.I18n
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.util.Mth
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.BFVersionChecker;
import com.boehmod.blockfront.client.ac.BFClientAntiCheat;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.menu.MenuRenderer;
import com.boehmod.blockfront.client.screen.BFMenuScreen;
import com.boehmod.blockfront.client.screen.profile.ProfileScreen;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.registry.BFItems;
import com.boehmod.blockfront.util.EllipsisUtils;
import com.boehmod.blockfront.util.FormatUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

public class BF_173
extends MenuRenderer {
    private static final Component field_983 = Component.translatable((String)"bf.message.party.hosting");
    private static final Component field_984 = Component.translatable((String)"bf.message.party.in");
    private static final Component field_987 = Component.translatable((String)"bf.menu.text.punished.title").withStyle(ChatFormatting.RED);
    private static final ItemStack field_979 = new ItemStack((ItemLike)BFItems.GUI_LOGO.get());
    private static final int field_981 = 250;
    private static final float field_980 = 1.5f;
    private static final int field_982 = 50;

    @Override
    public void renderBackground(@NotNull BFClientManager manager, @NotNull ClientPlayerDataHandler dataHandler, @NotNull Minecraft minecraft, @NotNull BFMenuScreen screen, @NotNull PoseStack poseStack, @NotNull Font font, @NotNull GuiGraphics graphics, int width, int height, int mouseX, int mouseY, float renderTime, float delta) {
        BFClientAntiCheat bFClientAntiCheat = (BFClientAntiCheat)manager.getAntiCheat();
        BFVersionChecker bFVersionChecker = manager.getVersionChecker();
        UUID uUID = minecraft.getUser().getProfileId();
        PlayerCloudData playerCloudData = dataHandler.getCloudData(minecraft);
        Optional optional = playerCloudData.getParty();
        int n = width / 2;
        int n2 = 40;
        int n3 = 20;
        int n4 = 325;
        int n5 = 8;
        poseStack.pushPose();
        if (minecraft.screen instanceof ProfileScreen) {
            n5 = 15;
        }
        BFRendering.rectangle(graphics, 0, 40, width, n5, BFRendering.translucentBlack());
        BFRendering.rectangle(graphics, 0, 40 + n5, width, 1, BFRendering.translucentBlack(), 0.25f);
        BFRendering.rectangleWithDarkShadow(graphics, 0, 0, width, 40, BFRendering.translucentBlack());
        BFRendering.rectangleWithDarkShadow(graphics, 0, height - 40, width, 40, BFRendering.translucentBlack());
        float f = width - 55;
        if (manager.getMatchMaking().isSearching()) {
            MutableComponent mutableComponent = Component.literal((String)EllipsisUtils.cyclingEllipsis(renderTime));
            MutableComponent mutableComponent2 = Component.translatable((String)"bf.message.party.searching").append((Component)mutableComponent).withColor(ColorReferences.COLOR_TEAM_ALLIES_HOVERED_SOLID);
            BFRendering.method_305(poseStack, font, graphics, (Component)mutableComponent2, f, 40.0f, 70, 10);
        } else {
            optional.ifPresent(matchParty -> {
                boolean bl = matchParty.isHost(uUID);
                Component component = bl ? field_983 : field_984;
                BFRendering.method_305(poseStack, font, graphics, component, f, 40.0f, 70, 10);
            });
        }
        BFRendering.enableScissor(graphics, n - 162, 0, 325, 40);
        BF_173.method_5484(poseStack, font, graphics, width, n, playerCloudData);
        graphics.disableScissor();
        BFRendering.rectangleGradient(graphics, 0, height - 50, width, 50, ColorReferences.COLOR_BLACK_TRANSPARENT, -1442840576);
        poseStack.popPose();
    }

    private static void method_5484(@NotNull PoseStack poseStack, @NotNull Font font, @NotNull GuiGraphics guiGraphics, int n, int n2, PlayerCloudData playerCloudData) {
        boolean bl;
        boolean bl2 = bl = !playerCloudData.getActivePunishments().isEmpty();
        if (bl) {
            BFRendering.centeredString(font, guiGraphics, field_987, n2, 10);
            String string = I18n.get((String)"bf.menu.text.punished.par1", (Object[])new Object[]{String.valueOf(ChatFormatting.RED) + String.valueOf(playerCloudData.getActivePunishments().size()) + String.valueOf(ChatFormatting.GRAY)});
            ObjectList<String> objectList = FormatUtils.parseMarkup(font, string, 350);
            int n3 = 0;
            for (String string2 : objectList) {
                BFRendering.centeredComponent2d(poseStack, font, guiGraphics, (Component)Component.literal((String)string2).withStyle(ChatFormatting.GRAY), n2, 20 + 5 * n3, 0.5f);
                ++n3;
            }
        }
    }

    @Override
    public void renderBelow(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, PlayerCloudData cloudData, @NotNull BFMenuScreen screen, @NotNull GuiGraphics graphics, @NotNull PoseStack poseStack, @NotNull Font font, int width, int height, int mouseX, int mouseY, float renderTime, float delta) {
        boolean bl = !cloudData.getActivePunishments().isEmpty();
        poseStack.pushPose();
        poseStack.translate(0.0f, 0.0f, 50.0f);
        int n = width / 2;
        if (!bl) {
            BFRendering.item(poseStack, graphics, field_979, n, 32.0f, 0.19f);
            BF_173.method_738(graphics, poseStack, font, renderTime, n);
        }
        poseStack.popPose();
    }

    private static void method_738(@NotNull GuiGraphics guiGraphics, @NotNull PoseStack poseStack, @NotNull Font font, float f, int n) {
        ObjectList<String> objectList = FormatUtils.parseMarkup(font, BFMenuScreen.splashText, 250);
        poseStack.pushPose();
        poseStack.translate((float)n + 40.0f, 27.0f, 50.0f);
        poseStack.mulPose(Axis.ZP.rotationDegrees(-2.5f));
        float f2 = (Mth.sin((float)(f / 30.0f)) + 1.0f) / 2.0f;
        float f3 = 1.5f - 0.4f * (float)objectList.size() + 0.1f * f2;
        poseStack.scale(f3, f3, f3);
        for (String string : objectList) {
            BFRendering.centeredComponent2dWithShadow(poseStack, font, guiGraphics, (Component)Component.literal((String)string), 0, 0, ColorReferences.COLOR_THEME_YELLOW_SOLID);
            poseStack.translate(0.0f, 8.0f, 0.0f);
        }
        poseStack.popPose();
    }

    @Override
    public void renderAbove(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull BFMenuScreen screen, @NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull Font font, int width, int height, int mouseX, int mouseY, float renderTime, float delta) {
    }

    @Override
    public void tick(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull BFMenuScreen screen, float renderTime) {
    }
}

