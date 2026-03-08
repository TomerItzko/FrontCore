/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.gui.layer;

import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.BFKeyMappings;
import com.boehmod.blockfront.client.gui.layer.BFAbstractGuiLayer;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.minimap.MinimapRendering;
import com.boehmod.blockfront.client.render.minimap.MinimapWaypoint;
import com.boehmod.blockfront.client.settings.BFClientSettings;
import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.common.entity.ArtilleryVehicleEntity;
import com.boehmod.blockfront.common.gun.GunMagType;
import com.boehmod.blockfront.common.item.BFUtilityItem;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.item.MeleeItem;
import com.boehmod.blockfront.common.item.RadioItem;
import com.boehmod.blockfront.common.match.DivisionData;
import com.boehmod.blockfront.common.match.kill.KillFeedEntry;
import com.boehmod.blockfront.common.match.kill.KillMessage;
import com.boehmod.blockfront.common.net.BFPopup;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGameClient;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.tag.IAllowsWarCry;
import com.boehmod.blockfront.game.tag.IUseKillIcons;
import com.boehmod.blockfront.unnamed.BF_29;
import com.boehmod.blockfront.unnamed.BF_39;
import com.boehmod.blockfront.unnamed.BF_623;
import com.boehmod.blockfront.unnamed.BF_631;
import com.boehmod.blockfront.unnamed.BF_633;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.EllipsisUtils;
import com.boehmod.blockfront.util.StringUtils;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.lang.runtime.SwitchBootstraps;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class MatchGuiLayer
extends BFAbstractGuiLayer {
    public static final int field_492 = 6;
    public static final int field_493 = 100;
    private static final int field_494 = 100;
    public static final float field_489 = 0.75f;
    private static final Component field_498 = Component.translatable((String)"bf.message.dead.countdown.soon");
    @NotNull
    private static final ResourceLocation field_6355 = BFRes.loc("textures/gui/game/health_bar.png");
    @NotNull
    private static final ResourceLocation field_487 = BFRes.loc("textures/gui/game/health.png");
    @NotNull
    private static final ResourceLocation field_6997 = BFRes.loc("textures/gui/game/health_vehicle.png");
    private static final Component field_500 = Component.translatable((String)"bf.message.reloading");
    private static final Component field_502 = Component.translatable((String)"bf.message.none").withStyle(ChatFormatting.GRAY);
    private static final Component field_503 = Component.translatable((String)"bf.item.radio.ready");
    private static final Component field_504 = Component.translatable((String)"bf.item.radio.resupplying");
    private static final Component field_505 = Component.translatable((String)"bf.message.ammo").withStyle(ChatFormatting.BOLD);
    @NotNull
    private static final ObjectList<BF_113> field_486 = new ObjectArrayList();
    private static final int field_495 = 16;
    private static final int field_496 = 2;
    private static final int field_497 = 512;
    private static final float field_490 = 1.0f;
    private static final float field_491 = 1.25f;
    private static final int field_507 = 24;
    private static final int field_508 = 24;
    private static final int field_509 = 82;
    private static final int field_510 = 15;
    private static final int field_511 = 102;
    private static final int field_512 = 15;
    private static final int field_513 = 64;
    private static final int field_514 = 102;
    private static final int field_515 = 15;
    public static int field_516;
    public static int field_517;
    public static int field_518;

    private static void method_496(@NotNull GuiGraphics guiGraphics, @NotNull Font font, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull LocalPlayer localPlayer, @NotNull BFClientPlayerData bFClientPlayerData, int n, int n2) {
        if (BFUtils.isPlayerUnavailable((Player)localPlayer, bFClientPlayerData)) {
            return;
        }
        if (!(abstractGame instanceof IAllowsWarCry)) {
            return;
        }
        FDSTagCompound fDSTagCompound = abstractGame.getPlayerStatData(localPlayer.getUUID());
        int n3 = fDSTagCompound.getInteger(BFStats.WAR_CRY.getKey());
        if (n3 <= 0) {
            return;
        }
        PoseStack poseStack = guiGraphics.pose();
        String string = BFKeyMappings.MATCH_WARCRY.getKey().getDisplayName().getString().toUpperCase(Locale.ROOT);
        MutableComponent mutableComponent = Component.literal((String)string).withColor(0xFFFFFF);
        MutableComponent mutableComponent2 = Component.literal((String)String.valueOf(n3 / 20)).withStyle(ChatFormatting.GRAY);
        MutableComponent mutableComponent3 = Component.translatable((String)"bf.message.ingame.warcry", (Object[])new Object[]{mutableComponent, mutableComponent2}).withStyle(ChatFormatting.RED);
        BFRendering.centeredComponent2dWithShadow(poseStack, font, guiGraphics, (Component)mutableComponent3, n, n2);
    }

    public void method_505(@NotNull Font font, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull AbstractGameClient<?, ?> abstractGameClient, @NotNull GuiGraphics guiGraphics, @NotNull PoseStack poseStack, @NotNull Minecraft minecraft, @NotNull LocalPlayer localPlayer, int n, int n2, float f, float f2) {
        int n3 = BFRendering.translucentBlack();
        GameTeam gameTeam = ((AbstractGamePlayerManager)abstractGame.getPlayerManager()).getPlayerTeam(localPlayer.getUUID());
        if (gameTeam == null) {
            return;
        }
        DivisionData divisionData = gameTeam.getDivisionData(abstractGame);
        int n4 = 99;
        int n5 = 30;
        float f3 = (float)n4 / 2.0f;
        float f4 = (float)n5 / 2.0f;
        int n6 = n - (n4 + 6);
        int n7 = n2 - 140;
        int n8 = 31;
        float f5 = 0.55f;
        Inventory inventory = localPlayer.getInventory();
        int n9 = inventory.selected;
        Style style = gameTeam.getStyleText();
        TextColor textColor = style.getColor();
        int n10 = textColor != null ? textColor.getValue() : ColorReferences.COLOR_WHITE_SOLID;
        float f6 = MathUtils.lerpf1(abstractGameClient.field_2877, abstractGameClient.field_2878, f2);
        if (f6 < 1.0f && localPlayer.getVehicle() == null) {
            float f7 = (float)(n4 + 6 + 10) * f6;
            poseStack.pushPose();
            poseStack.translate(f7, 0.0f, 0.0f);
            ItemStack itemStack = inventory.getItem(0);
            if (n9 == 0) {
                BFRendering.rectangle(guiGraphics, n6 + 1, n7 + 1, n4 - 2, n5 - 2, n10, 0.55f);
            }
            BFRendering.rectangle(guiGraphics, n6 + 1, n7, n4 - 2, 1, n3);
            BFRendering.rectangle(guiGraphics, n6, n7 + 1, n4, n5 - 2, n3);
            BFRendering.rectangle(guiGraphics, n6 + 1, n7 + n5 - 1, n4 - 2, 1, n3);
            BFRendering.drawStringWithShadow(font, guiGraphics, (Component)Component.literal((String)"1").withStyle(style), n6 + 2, n7 + 2);
            BFRendering.item(poseStack, guiGraphics, itemStack, (float)n6 + f3, (float)n7 + f4, 3.5f);
            itemStack = inventory.getItem(1);
            n7 += 31;
            if (n9 == 1) {
                BFRendering.rectangle(guiGraphics, n6 + 1, n7 + 1, n4 - 2, n5 - 2, n10, 0.55f);
            }
            BFRendering.rectangle(guiGraphics, n6 + 1, n7, n4 - 2, 1, n3);
            BFRendering.rectangle(guiGraphics, n6, n7 + 1, n4, n5 - 2, n3);
            BFRendering.rectangle(guiGraphics, n6 + 1, n7 + n5 - 1, n4 - 2, 1, n3);
            BFRendering.drawStringWithShadow(font, guiGraphics, (Component)Component.literal((String)"2").withStyle(style), n6 + 2, n7 + 2);
            BFRendering.item(poseStack, guiGraphics, itemStack, (float)n6 + f3, (float)n7 + f4, 2.5f);
            itemStack = inventory.getItem(2);
            n7 += 31;
            if (n9 == 2) {
                BFRendering.rectangle(guiGraphics, n6 + 1, n7 + 1, n4 - 2, n5 - 2, n10, 0.55f);
            }
            BFRendering.rectangle(guiGraphics, n6 + 1, n7, n4 - 2, 1, n3);
            BFRendering.rectangle(guiGraphics, n6, n7 + 1, n4, n5 - 2, n3);
            BFRendering.rectangle(guiGraphics, n6 + 1, n7 + n5 - 1, n4 - 2, 1, n3);
            BFRendering.drawStringWithShadow(font, guiGraphics, (Component)Component.literal((String)"3").withStyle(style), n6 + 2, n7 + 2);
            if (!itemStack.isEmpty()) {
                BFRendering.item(poseStack, guiGraphics, itemStack, (float)n6 + f3, (float)n7 + f4, 3.1f);
            }
            n7 += 31;
            for (int i = 0; i < 4; ++i) {
                MatchGuiLayer.method_506(font, guiGraphics, poseStack, inventory, i, n9, n6, n7, n10, 0.55f, style);
                n6 += 25;
            }
            poseStack.popPose();
        }
        int n11 = n - 105;
        int n12 = n2 - 21;
        this.method_504(minecraft, localPlayer, poseStack, guiGraphics, font, abstractGame, inventory, n, n2, n3, n10, n11, n12, 82, 15, f, f2);
        this.renderNationIcon(poseStack, guiGraphics, divisionData, n, n2);
        this.method_497(poseStack, guiGraphics, font, abstractGameClient, localPlayer, n2 - 15, 15, 102, n3, f2);
        this.method_498(poseStack, guiGraphics, font, localPlayer, n3, n2, 15);
        this.method_500(poseStack, localPlayer, guiGraphics, abstractGame, n11, n12, f2);
    }

    private static void method_506(@NotNull Font font, @NotNull GuiGraphics guiGraphics, @NotNull PoseStack poseStack, Inventory inventory, int n, int n2, int n3, int n4, int n5, float f, Style style) {
        int n6 = BFRendering.translucentBlack();
        float f2 = 12.0f;
        float f3 = 12.0f;
        ItemStack itemStack = inventory.getItem(3 + n);
        int n7 = itemStack.getCount();
        if (n2 == 3 + n) {
            BFRendering.rectangle(guiGraphics, n3 + 1, n4 + 1, 22, 22, n5, f);
        }
        BFRendering.rectangle(guiGraphics, n3 + 1, n4, 22, 1, n6);
        BFRendering.rectangle(guiGraphics, n3, n4 + 1, 24, 22, n6);
        BFRendering.rectangle(guiGraphics, n3 + 1, n4 + 24 - 1, 22, 1, n6);
        float f4 = 1.0f;
        if (!itemStack.isEmpty() && itemStack.getItem() instanceof BFUtilityItem) {
            f4 = 1.25f;
        }
        BFRendering.item(poseStack, guiGraphics, itemStack, (float)n3 + 12.0f, (float)n4 + 12.0f, f4);
        poseStack.translate(0.0f, 0.0f, 512.0f);
        MutableComponent mutableComponent = Component.literal((String)String.valueOf(4 + n)).withStyle(style);
        BFRendering.drawStringWithShadow(font, guiGraphics, (Component)mutableComponent, n3 + 2, n4 + 2);
        if (n7 > 1) {
            MutableComponent mutableComponent2 = Component.literal((String)StringUtils.formatLong(n7));
            int n8 = n3 + 24 - 7;
            int n9 = n4 + 24 - 9;
            BFRendering.drawStringWithShadow(font, guiGraphics, (Component)mutableComponent2, n8, n9);
        }
        poseStack.translate(0.0f, 0.0f, -512.0f);
    }

    private void method_504(@NotNull Minecraft minecraft, @NotNull LocalPlayer localPlayer, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, @NotNull Font font, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull Inventory inventory, int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8, float f, float f2) {
        Item item;
        BFRendering.rectangle(guiGraphics, n5 + 1, n6, n7 - 2, 1, n3);
        BFRendering.rectangle(guiGraphics, n5, n6 + 1, n7, n8 - 2, n3);
        BFRendering.rectangle(guiGraphics, n5 + 1, n6 + n8 - 1, n7 - 2, 1, n3);
        ItemStack itemStack = inventory.getSelected();
        if (itemStack.isEmpty()) {
            return;
        }
        Item item2 = item = itemStack.getItem();
        Objects.requireNonNull(item2);
        Item item3 = item2;
        int n9 = 0;
        switch (SwitchBootstraps.typeSwitch("typeSwitch", new Object[]{GunItem.class, MeleeItem.class, RadioItem.class}, (Object)item3, n9)) {
            case 0: {
                GunItem gunItem = (GunItem)item3;
                this.method_502(minecraft, font, poseStack, guiGraphics, gunItem, itemStack, n, n2, n4, n5, n6, n7, n8, f, f2);
                break;
            }
            case 1: {
                MeleeItem meleeItem = (MeleeItem)item3;
                float f3 = localPlayer.getAttackStrengthScale(f2);
                if (!(f3 < 1.0f)) break;
                float f4 = (float)n7 * f3;
                BFRendering.rectangle(poseStack, guiGraphics, (float)(n5 + 1), (float)n6, f4 - 2.0f, 1.0f, ColorReferences.COLOR_WHITE_SOLID);
                break;
            }
            case 2: {
                RadioItem radioItem = (RadioItem)item3;
                this.method_507(localPlayer, poseStack, guiGraphics, font, abstractGame, n2, n4, n5, n6, n7, f);
                break;
            }
        }
    }

    private void method_507(@NotNull LocalPlayer localPlayer, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, @NotNull Font font, @NotNull AbstractGame<?, ?, ?> abstractGame, int n, int n2, float f, float f2, int n3, float f3) {
        Object obj = abstractGame.getPlayerManager();
        GameTeam gameTeam = ((AbstractGamePlayerManager)obj).getPlayerTeam(localPlayer.getUUID());
        if (gameTeam == null) {
            return;
        }
        int n4 = gameTeam.getObjectInt(BFStats.RADIO_DELAY, 0);
        int n5 = n3 - 2;
        float f4 = (float)n4 * (float)n5 / 2400.0f;
        BFRendering.rectangle(poseStack, guiGraphics, f + 1.0f, f2, f4, 1.0f, ColorReferences.COLOR_WHITE_SOLID);
        float f5 = f + (float)n3 / 2.0f;
        BFRendering.centeredComponent2dWithShadow(poseStack, font, guiGraphics, (Component)(n4 <= 0 ? field_503 : field_504.copy().append((Component)Component.literal((String)EllipsisUtils.cyclingEllipsis(f3)))), f5, (float)(n - 18), n2);
    }

    private void method_502(@NotNull Minecraft minecraft, @NotNull Font font, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, @NotNull GunItem gunItem, @NotNull ItemStack itemStack, int n, int n2, int n3, float f, float f2, int n4, int n5, float f3, float f4) {
        GunMagType gunMagType = gunItem.getMagTypeOrDefault(itemStack);
        int n6 = gunMagType.capacity();
        Style style = Style.EMPTY.withColor(ChatFormatting.WHITE);
        if (field_516 < n6 / 2) {
            style = Style.EMPTY.withColor(ColorReferences.COLOR_THEME_YELLOW_SOLID);
            if (field_516 < n6 / 4) {
                style = Style.EMPTY.withColor(ChatFormatting.RED);
            }
        }
        if (!GunItem.isReloading(itemStack)) {
            gunItem.getClipTextRenderer().method_4116(minecraft, poseStack, guiGraphics, font, gunItem, itemStack, style, n3, f, f2, n4, n5, n, n2, f4);
        } else {
            float f5 = gunItem.getReloadTime(itemStack);
            float f6 = gunItem.method_4152(itemStack, f4);
            float f7 = f6 * (float)n4 / f5;
            MutableComponent mutableComponent = Component.literal((String)EllipsisUtils.cyclingEllipsis(f3));
            BFRendering.rectangle(poseStack, guiGraphics, f + 1.0f, f2, f7 - 2.0f, 1.0f, ColorReferences.COLOR_WHITE_SOLID);
            BFRendering.centeredComponent2dWithShadow(poseStack, font, guiGraphics, (Component)field_500.copy().append((Component)mutableComponent), f + (float)n4 / 2.0f, f2 + 4.0f, n3);
        }
    }

    private void renderNationIcon(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull DivisionData division, int width, int height) {
        BFRendering.texture(poseStack, graphics, division.getCountry().getNationIcon(), (float)width - 21.5f, (float)height - 20.5f, 14.0f, 14.0f);
    }

    private void renderMinimap(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull Font font, @NotNull LocalPlayer player, @NotNull Collection<MinimapWaypoint> waypoints) {
        MinimapRendering.render(poseStack, graphics, font, player, waypoints, 6, 6, 0.75f, 100, 100);
    }

    private void method_497(@NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, @NotNull Font font, @NotNull AbstractGameClient<?, ?> abstractGameClient, @NotNull LocalPlayer localPlayer, int n, int n2, int n3, int n4, float f) {
        float f2 = abstractGameClient.method_2743(f);
        float f3 = MathUtils.lerpf1(BF_39.field_183, BF_39.field_184, f);
        int n5 = 6;
        int n6 = n - 6;
        float f4 = 64.0f * f3 / 20.0f;
        BFRendering.rectangle(guiGraphics, 7, n6, n3 - 2, 1, n4);
        BFRendering.rectangle(guiGraphics, 6, n6 + 1, n3, n2 - 2, n4);
        BFRendering.rectangle(guiGraphics, 7, n6 + n2 - 1, n3 - 2, 1, n4);
        BFRendering.texture(poseStack, guiGraphics, field_487, 6, n6, n2, n2);
        int n7 = Math.max((int)f3, 1);
        MutableComponent mutableComponent = Component.literal((String)String.valueOf(n7)).withStyle(ChatFormatting.BOLD);
        BFRendering.centeredComponent2dWithShadow(poseStack, font, guiGraphics, (Component)mutableComponent, 30, n6 + 4);
        BFRendering.rectangle(guiGraphics, 40, n6 + 4, 64, 7, n4);
        int n8 = 40;
        int n9 = n6 + 4;
        int n10 = 7;
        BFRendering.enableScissor(guiGraphics, 40, n9, (int)f4, 7);
        BFRendering.texture(poseStack, guiGraphics, field_6355, 40, n9, 64, 7);
        guiGraphics.disableScissor();
        if (BF_39.field_183 <= 5.0f) {
            BFRendering.rectangle(poseStack, guiGraphics, 40.0f, (float)(n - 2), f4, 7.0f, 13721420);
        }
        int n11 = localPlayer.getFoodData().getFoodLevel();
        if (f2 < 20.0f) {
            int n12 = n3 - 2;
            int n13 = n11 <= 1 ? ColorReferences.COLOR_BLACK_SOLID + BFRendering.RED_CHAT_COLOR : ColorReferences.COLOR_WHITE_SOLID;
            BFRendering.rectangle(poseStack, guiGraphics, 7.0f, (float)n6, (float)n12 * f2 / 20.0f, 1.0f, n13);
        }
    }

    private void method_498(@NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, @NotNull Font font, @NotNull LocalPlayer localPlayer, int n, int n2, int n3) {
        Object object = localPlayer.getVehicle();
        if (!(object instanceof AbstractVehicleEntity)) {
            return;
        }
        AbstractVehicleEntity abstractVehicleEntity = (AbstractVehicleEntity)object;
        object = abstractVehicleEntity.method_2343();
        float f = abstractVehicleEntity.method_2314();
        int n4 = 102;
        int n5 = 15;
        int n6 = n2 - 15 - 6 - (n3 + 1);
        float f2 = 64.0f * (f / object.field_2685);
        BFRendering.rectangle(guiGraphics, 6, n6, 102, 15, n);
        BFRendering.texture(poseStack, guiGraphics, field_6997, 6, n6, 15, 15);
        MutableComponent mutableComponent = Component.literal((String)String.valueOf((int)f)).withStyle(ChatFormatting.BOLD);
        BFRendering.centeredComponent2dWithShadow(poseStack, font, guiGraphics, (Component)mutableComponent, 30, n6 + 4);
        BFRendering.rectangle(guiGraphics, 40, n6 + 4, 64, 7, n);
        int n7 = 40;
        int n8 = n6 + 4;
        int n9 = 7;
        BFRendering.enableScissor(guiGraphics, 40, n8, (int)f2, 7);
        BFRendering.texture(poseStack, guiGraphics, field_6355, 40, n8, 64, 7);
        guiGraphics.disableScissor();
        int n10 = 109;
        int n11 = n3 * 2 + 1;
        float f3 = abstractVehicleEntity.method_2313() * 20.0f;
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        BFRendering.rectangle(guiGraphics, 109, n6, n11, n11, n);
        BFRendering.centeredComponent2d(poseStack, font, guiGraphics, (Component)Component.literal((String)decimalFormat.format((double)f3 * 2.237)), 109 + n11 / 2, n6 + 5, 1.5f);
        BFRendering.centeredString(font, guiGraphics, (Component)Component.literal((String)"mph").withStyle(ChatFormatting.BOLD), 109 + n11 / 2, n6 + 18);
        BF_623 bF_623 = abstractVehicleEntity.method_2324((Entity)localPlayer);
        if (bF_623 != null) {
            Object object2;
            BF_631 bF_631 = null;
            for (BF_633<?> bF_633 : bF_623.method_2388()) {
                if (!(bF_633 instanceof BF_631)) continue;
                object2 = (BF_631)bF_633;
                bF_631 = object2;
                break;
            }
            if (bF_631 != null) {
                boolean bl = false;
                int n12 = 0;
                if (abstractVehicleEntity instanceof ArtilleryVehicleEntity) {
                    object2 = (ArtilleryVehicleEntity)abstractVehicleEntity;
                    SynchedEntityData synchedEntityData = object2.getEntityData();
                    bl = true;
                    n12 = ((ArtilleryVehicleEntity)object2).method_2358(bF_631).method_2361(synchedEntityData);
                }
                if (bl) {
                    BFRendering.rectangle(guiGraphics, 109 + n11 + 1, n6, n11, n11, n);
                    BFRendering.centeredComponent2d(poseStack, font, guiGraphics, (Component)Component.literal((String)String.valueOf(n12)), 109 + n11 + 1 + n11 / 2, n6 + 5, 1.5f);
                    BFRendering.centeredString(font, guiGraphics, field_505, 109 + n11 + 1 + n11 / 2, n6 + 18);
                }
            }
        }
        int n13 = 99;
        int n14 = 20;
        int n15 = guiGraphics.guiWidth() - 99 - 6;
        int n16 = n2 - 42;
        int n17 = 0;
        int n18 = object.field_2678.size();
        for (int i = 0; i < n18; ++i) {
            BF_623 bF_6232 = object.field_2678.get(i);
            int n19 = n16 - 21 * n17;
            BFRendering.rectangle(guiGraphics, n15, n19, 99, 20, n);
            BFRendering.texture(poseStack, guiGraphics, bF_6232.getIcon(), n15, n19, 20, 20, 2.0f);
            MutableComponent mutableComponent2 = field_502.copy();
            if (i < abstractVehicleEntity.getPassengers().size()) {
                Entity entity = (Entity)abstractVehicleEntity.getPassengers().get(i);
                mutableComponent2 = Component.literal((String)entity.getScoreboardName());
            }
            int n20 = n15 + 49;
            int n21 = n19 + 10 - 3;
            BFRendering.centeredString(font, guiGraphics, (Component)mutableComponent2, n20, n21);
            ++n17;
        }
    }

    private void method_500(@NotNull PoseStack poseStack, @NotNull LocalPlayer localPlayer, @NotNull GuiGraphics guiGraphics, @NotNull AbstractGame<?, ?, ?> abstractGame, int n, int n2, float f) {
        FDSTagCompound fDSTagCompound = abstractGame.getPlayerStatData(localPlayer.getUUID());
        int n3 = fDSTagCompound.getInteger(BFStats.KILLS_STREAK.getKey());
        int n4 = n - 16;
        int n5 = n2 - 1;
        int n6 = 15;
        n4 -= Math.min(n3, 15) * 2;
        if (field_486.size() > n3) {
            field_486.clear();
        } else {
            while (field_486.size() < n3) {
                field_486.add((Object)new BF_113());
            }
        }
        int n7 = n4;
        int n8 = 0;
        int n9 = field_486.size();
        for (int i = 0; i < n9; ++i) {
            ((BF_113)field_486.get(i)).method_508(poseStack, guiGraphics, (n7 += 2) - 2, n5 + (i % 2 == 0 ? 2 : 0) + n8, 16, f);
            if (i == 0 || i % 15 != 0) continue;
            n7 = n4;
            n8 += 4;
        }
    }

    private void renderKillFeed(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, Font font, @NotNull AbstractGame<?, ?, ?> game, @NotNull AbstractGameClient<? extends AbstractGame<?, ?, ?>, ?> gameClient, boolean debug, int n, float delta) {
        List<KillFeedEntry> list = gameClient.getKillFeedEntries();
        int n2 = list.size();
        for (int i = 0; i < n2; ++i) {
            poseStack.pushPose();
            poseStack.translate(6.0f, (float)(n + i * 16), 0.0f);
            list.get(i).render(graphics, font, game, debug, delta);
            poseStack.popPose();
        }
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, @NotNull DeltaTracker delta, @NotNull BFClientManager manager) {
        AbstractGameClient<?, ?> abstractGameClient;
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        LocalPlayer localPlayer = minecraft.player;
        ClientLevel clientLevel = minecraft.level;
        float f = BFRendering.getRenderTime();
        if (!BFClientSettings.UI_RENDER_HUD.isEnabled() || minecraft.options.hideGui || clientLevel == null || localPlayer == null) {
            return;
        }
        PoseStack poseStack = graphics.pose();
        MultiBufferSource.BufferSource bufferSource = graphics.bufferSource();
        AbstractGame<?, ?, ?> abstractGame = manager.getGame();
        if (abstractGame == null) {
            return;
        }
        float f2 = delta.getGameTimeDeltaPartialTick(true);
        int n = graphics.guiWidth();
        int n2 = graphics.guiHeight();
        int n3 = n / 2;
        int n4 = n2 / 2;
        ClientPlayerDataHandler clientPlayerDataHandler = (ClientPlayerDataHandler)manager.getPlayerDataHandler();
        ItemStack itemStack = localPlayer.getMainHandItem();
        BFClientPlayerData bFClientPlayerData = clientPlayerDataHandler.getPlayerData(minecraft);
        PlayerCloudData playerCloudData = clientPlayerDataHandler.getCloudData(minecraft);
        if (BFClientSettings.UI_RENDER_TAB_DEATH_MOTION_SICKNESS.isEnabled() && bFClientPlayerData.isOutOfGame() || localPlayer.isDeadOrDying()) {
            BFRendering.rectangle(graphics, 0, 0, n, n2, ColorReferences.COLOR_BLACK_SOLID);
        }
        float f3 = Math.max(0.0f, MathUtils.lerpf1(BF_29.field_119, BF_29.field_120, f2) - 1.0f);
        BFRendering.rectangle(graphics, 0, 0, n, n2, 0, f3);
        if (f3 >= 0.04f && !abstractGame.shouldRespawnAutomatically((Player)localPlayer)) {
            int n5 = MathUtils.withAlphaf(13721420, f3);
            BFRendering.centeredString(font, graphics, field_498, n3, n4 - 20, n5);
        }
        if (!itemStack.isEmpty() && itemStack.getItem() instanceof GunItem) {
            field_517 = field_516;
            field_516 = GunItem.getAmmoLoaded(itemStack);
            field_518 = GunItem.getAmmo(itemStack);
        }
        if ((abstractGameClient = manager.getGameClient()) == null) {
            return;
        }
        Set<UUID> set = ((AbstractGamePlayerManager)abstractGame.getPlayerManager()).getPlayerUUIDs();
        this.method_501(minecraft, manager, clientPlayerDataHandler, graphics, font, abstractGameClient, n, n2, f2);
        MatchGuiLayer.method_496(graphics, font, abstractGame, localPlayer, bFClientPlayerData, n3, n4);
        abstractGameClient.render(minecraft, manager, localPlayer, clientLevel, bFClientPlayerData, graphics, font, poseStack, (MultiBufferSource)bufferSource, set, n, n2, n3, n4, f, f2);
        this.method_503(minecraft, localPlayer, abstractGameClient, poseStack, graphics, font, abstractGame, f2, n, n3, n4);
        if (!manager.getCinematics().isSequencePlaying()) {
            float f4 = MathUtils.lerpf1(abstractGameClient.field_2879, abstractGameClient.field_2880, f2);
            poseStack.pushPose();
            poseStack.translate(0.0f, 0.0f, 400.0f);
            BFRendering.rectangle(graphics, 0, 0, n, n2, ColorReferences.COLOR_BLACK_SOLID, f4);
            poseStack.popPose();
            if (!bFClientPlayerData.isOutOfGame()) {
                this.method_505(font, abstractGame, abstractGameClient, graphics, poseStack, minecraft, localPlayer, n, n2, f, f2);
            }
        }
        if (minecraft.options.keyPlayerList.isDown()) {
            abstractGameClient.method_2695(poseStack, graphics, font, minecraft, manager, localPlayer, playerCloudData, n, n2, f2);
        }
    }

    private void method_503(@NotNull Minecraft minecraft, @NotNull LocalPlayer localPlayer, @NotNull AbstractGameClient<? extends AbstractGame<?, ?, ?>, ?> abstractGameClient, @NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, @NotNull Font font, @NotNull AbstractGame<?, ?, ?> abstractGame, float f, int n, int n2, int n3) {
        boolean bl = minecraft.getDebugOverlay().showDebugScreen();
        Collection<MinimapWaypoint> collection = abstractGameClient.method_2727();
        poseStack.pushPose();
        if (bl) {
            poseStack.translate(0.0f, 210.0f, 0.0f);
        }
        if (BFClientSettings.UI_RENDER_GAME_MINIMAP.isEnabled()) {
            this.renderMinimap(poseStack, guiGraphics, font, localPlayer, collection);
        }
        int n4 = 23;
        if (BFClientSettings.UI_RENDER_GAME_MINIMAP.isEnabled()) {
            n4 += 106;
        }
        if (abstractGame instanceof IUseKillIcons && BFClientSettings.UI_RENDER_KILLFEED.isEnabled()) {
            this.renderKillFeed(poseStack, guiGraphics, font, abstractGame, abstractGameClient, bl, n4, f);
        }
        poseStack.popPose();
        int n5 = 0;
        for (BFPopup bFPopup : abstractGameClient.getPopups()) {
            int n6 = n2 + n / 4;
            int n7 = n3 / 2 + (bFPopup.getHeight() + 3) * n5++;
            bFPopup.method_3809(guiGraphics, font, n6, n7, f);
        }
    }

    private void method_501(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull GuiGraphics guiGraphics, Font font, @NotNull AbstractGameClient<?, ?> abstractGameClient, int n, int n2, float f) {
        KillMessage killMessage = abstractGameClient.getKillMessage();
        if (killMessage != null) {
            killMessage.render(minecraft, bFClientManager, clientPlayerDataHandler, guiGraphics, font, n, n2, f);
        }
    }

    public static class BF_113 {
        private static final ResourceLocation field_519 = BFRes.loc("textures/gui/kill.png");
        private static final ResourceLocation field_520 = BFRes.loc("textures/gui/kill_flash.png");
        private float field_521 = 0.0f;
        private float field_522 = 1.0f;

        public void method_508(@NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, int n, int n2, int n3, float f) {
            poseStack.pushPose();
            poseStack.translate(0.0f, 25.0f * (1.0f - this.field_521), 0.0f);
            float f2 = f / 2.0f;
            this.field_521 = Mth.lerp((float)f2, (float)this.field_521, (float)1.0f);
            this.field_522 = Mth.lerp((float)(f2 / 4.0f), (float)this.field_522, (float)0.0f);
            BFRendering.texture(poseStack, guiGraphics, field_519, n, n2, n3, n3, this.field_521);
            BFRendering.texture(poseStack, guiGraphics, field_520, n, n2, n3, n3, this.field_522 * this.field_521);
            poseStack.popPose();
        }

        public float method_509() {
            return this.field_521;
        }
    }

    public static class BF_114 {
        @NotNull
        private final ResourceLocation field_525;
        private float field_523;
        private float field_524;

        public BF_114(@NotNull Random random, @NotNull BF_115 bF_115) {
            this.field_524 = this.field_523 = 0.8f - 0.2f * random.nextFloat();
            int n = random.nextInt(bF_115.textureCount);
            this.field_525 = BFRes.loc("textures/gui/overlay/" + bF_115.field_526 + n + ".png");
        }

        public void method_510(@NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
            float f2 = MathUtils.lerpf1(this.field_523, this.field_524, f);
            if (f2 > 0.0f) {
                BFRendering.texture(poseStack, guiGraphics, this.field_525, 0, 0, n, n2, f2);
            }
        }

        public boolean method_511() {
            this.field_524 = this.field_523;
            this.field_523 = MathUtils.moveTowards(this.field_523, 0.0f, 0.005f);
            return this.field_523 + this.field_524 == 0.0f;
        }

        public static enum BF_115 {
            BLOOD("blood", 7),
            DIRT("dirt", 5),
            SNOW("snow", 5);

            private final String field_526;
            private final int textureCount;

            private BF_115(String string2, int n2) {
                this.field_526 = string2;
                this.textureCount = n2;
            }
        }
    }
}

