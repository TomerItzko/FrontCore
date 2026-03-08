/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.screen.profile;

import com.boehmod.bflib.cloud.common.CloudRegistry;
import com.boehmod.bflib.cloud.common.item.CloudItem;
import com.boehmod.bflib.cloud.common.item.CloudItemStack;
import com.boehmod.bflib.cloud.common.item.CloudItemType;
import com.boehmod.bflib.cloud.common.item.types.AbstractCloudItemCoin;
import com.boehmod.bflib.cloud.common.player.PlayerRank;
import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.item.CloudItemRenderer;
import com.boehmod.blockfront.client.render.item.CloudItemRenderers;
import com.boehmod.blockfront.client.screen.profile.ProfileScreen;
import com.boehmod.blockfront.cloud.PlayerCloudInventory;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.unnamed.BF_120;
import com.boehmod.blockfront.util.ClanUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

public final class ProfileMainScreen
extends ProfileScreen {
    private static final Component field_1451 = Component.translatable((String)"bf.message.none").withStyle(ChatFormatting.RED);
    private static final Component field_1452 = Component.translatable((String)"bf.message.profile.base.showcase");
    private static final Component field_1453 = Component.translatable((String)"bf.profile.showcase.empty").withStyle(ChatFormatting.GRAY);
    public static final int field_6348 = 5;
    public static final int field_6349 = 40;
    public static final int field_6350 = 40;
    public static final int field_6351 = 4;
    public static final int field_6352 = 133;
    @NotNull
    private final List<BF_120> field_1450 = new ObjectArrayList();

    public ProfileMainScreen(@NotNull Screen screen, PlayerCloudData playerCloudData) {
        super(screen, playerCloudData, (Component)Component.translatable((String)"bf.screen.profile.main", (Object[])new Object[]{playerCloudData.getUsername()}));
    }

    public ProfileMainScreen(@NotNull Screen screen) {
        this(screen, ((ClientPlayerDataHandler)BFClientManager.getInstance().getPlayerDataHandler()).getCloudData(Minecraft.getInstance()));
    }

    @Override
    public void init() {
        super.init();
        PlayerCloudInventory playerCloudInventory = (PlayerCloudInventory)this.playerCloudData.getInventory();
        int n = this.width / 2;
        int n2 = 40;
        int n3 = 40;
        int n4 = 4;
        List list = playerCloudInventory.getShowcasedItems().stream().limit(5L).toList();
        int n5 = list.size();
        int n6 = 44 * n5;
        int n7 = n - n6 / 2;
        int n8 = 133;
        int n9 = n7 + 2;
        this.field_1450.clear();
        for (UUID uUID : list) {
            Optional optional = playerCloudInventory.getStackFromUUID(uUID);
            if (!optional.isPresent()) continue;
            CloudItemStack cloudItemStack = (CloudItemStack)optional.get();
            this.field_1450.add(new BF_120(this, cloudItemStack, n9, 133, 40, 40, false, true).method_527(true));
            n9 += 44;
        }
    }

    @Override
    public void tick() {
        super.tick();
        CloudRegistry cloudRegistry = this.manager.getCloudRegistry();
        for (BF_120 bF_120 : this.field_1450) {
            bF_120.updateParticles(this.minecraft, cloudRegistry);
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
        Object object;
        CloudItem cloudItem;
        super.render(guiGraphics, n, n2, f);
        CloudRegistry cloudRegistry = this.manager.getCloudRegistry();
        PoseStack poseStack = guiGraphics.pose();
        int n3 = this.width / 2;
        int n4 = n3 - 100;
        int n5 = 60;
        int n6 = 40;
        int n7 = 72;
        PlayerCloudInventory playerCloudInventory = (PlayerCloudInventory)this.playerCloudData.getInventory();
        BFRendering.rectangle(guiGraphics, n4, 72, 40, 40, ColorReferences.COLOR_WHITE_SOLID);
        BFRendering.rectangle(guiGraphics, n4 + 1, 73, 38, 38, ColorReferences.COLOR_BLACK_SOLID);
        BFRendering.playerHead(this.minecraft, this.manager, poseStack, guiGraphics, this.playerCloudData.getMcProfile(), n4 + 2, 74.0f, 36);
        CloudItemStack cloudItemStack = playerCloudInventory.method_1673(CloudItemType.COIN);
        if (cloudItemStack != null && (cloudItem = cloudItemStack.getCloudItem(cloudRegistry)) instanceof AbstractCloudItemCoin) {
            AbstractCloudItemCoin abstractCloudItemCoin = (AbstractCloudItemCoin)cloudItem;
            int n8 = 16;
            object = CloudItemRenderers.getRenderer(abstractCloudItemCoin);
            ((CloudItemRenderer)object).method_1745(abstractCloudItemCoin, cloudItemStack, poseStack, this.minecraft, guiGraphics, n4, 72.0f, 16.0f, 16.0f, 1.0f);
        }
        int n9 = n4 + 40 + 5;
        cloudItem = field_1451;
        object = this.playerCloudData.getMood();
        if (((Optional)object).isPresent()) {
            String string = (String)((Optional)object).get();
            cloudItem = Component.literal((String)("\"" + string + "\"")).withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC);
        }
        int n10 = this.playerCloudData.getPrestigeLevel();
        PlayerRank playerRank = this.playerCloudData.getRank();
        boolean bl = this.playerCloudData.canPrestige();
        MutableComponent mutableComponent = Component.literal((String)playerRank.getTitle()).withColor(bl ? ColorReferences.COLOR_THEME_PRESTIGE_SOLID : ColorReferences.COLOR_WHITE_SOLID);
        MutableComponent mutableComponent2 = Component.literal((String)(n10 > 0 ? "P" + n10 + " " : "")).withColor(ColorReferences.COLOR_THEME_PRESTIGE_SOLID);
        MutableComponent mutableComponent3 = ClanUtils.getClanPrefix(this.playerCloudData);
        MutableComponent mutableComponent4 = mutableComponent3.append((Component)this.playerCloudData.method_1715());
        MutableComponent mutableComponent5 = this.playerCloudData.method_1718().copy().withStyle(this.playerCloudData.getStatus().getOnlineStatus().isOnline() ? ChatFormatting.GREEN : ChatFormatting.RED);
        BFRendering.drawString(this.font, guiGraphics, (Component)Component.translatable((String)"bf.message.profile.base.username", (Object[])new Object[]{mutableComponent4}), n9, 73);
        BFRendering.drawString(this.font, guiGraphics, (Component)Component.translatable((String)"bf.message.profile.base.rank", (Object[])new Object[]{mutableComponent2.append((Component)mutableComponent)}), n9, 83);
        BFRendering.drawString(this.font, guiGraphics, (Component)Component.translatable((String)"bf.message.profile.base.status", (Object[])new Object[]{mutableComponent5}), n9, 93);
        BFRendering.drawString(this.font, guiGraphics, (Component)Component.translatable((String)"bf.message.profile.base.mood", (Object[])new Object[]{cloudItem}), n9, 103);
        BFRendering.centeredString(this.font, guiGraphics, field_1452, n3, 120);
        if (this.field_1450.isEmpty()) {
            BFRendering.centeredString(this.font, guiGraphics, field_1453, n3, 150);
        } else {
            for (BF_120 bF_120 : this.field_1450) {
                bF_120.field_555 = bF_120.field_554;
                bF_120.method_524(this.minecraft, guiGraphics, this.font, n, n2, f);
            }
        }
    }
}

