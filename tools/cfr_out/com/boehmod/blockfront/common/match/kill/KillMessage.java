/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.CloudRegistry
 *  com.boehmod.bflib.cloud.common.item.CloudItem
 *  com.boehmod.bflib.cloud.common.item.CloudItemStack
 *  com.boehmod.bflib.cloud.common.item.CloudItemType
 *  com.boehmod.bflib.cloud.common.item.types.AbstractCloudItemCoin
 *  com.boehmod.bflib.cloud.common.item.types.CloudItemCallingCard
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.FormattedText
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.world.item.ItemStack
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.match.kill;

import com.boehmod.bflib.cloud.common.CloudRegistry;
import com.boehmod.bflib.cloud.common.item.CloudItem;
import com.boehmod.bflib.cloud.common.item.CloudItemStack;
import com.boehmod.bflib.cloud.common.item.CloudItemType;
import com.boehmod.bflib.cloud.common.item.types.AbstractCloudItemCoin;
import com.boehmod.bflib.cloud.common.item.types.CloudItemCallingCard;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.item.CloudItemRenderer;
import com.boehmod.blockfront.client.render.item.CloudItemRenderers;
import com.boehmod.blockfront.cloud.PlayerCloudInventory;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class KillMessage {
    public static final int field_3663 = 37;
    public static final int field_3664 = 200;
    public static final int field_3665 = 43;
    public static final int field_3666 = 11;
    public static final int field_3667 = 4;
    public static final int field_3668 = 3;
    @NotNull
    private final UUID uuid;
    @NotNull
    private final ItemStack itemUsing;
    private final float health;
    private int field_3661 = 100;
    private float field_3659;
    private float field_3660 = 0.0f;

    public KillMessage(@NotNull UUID uUID, @NotNull ItemStack itemStack, float f) {
        this.uuid = uUID;
        this.itemUsing = itemStack;
        this.health = f;
    }

    public void onUpdate() {
        if (this.field_3661 > 0) {
            --this.field_3661;
        }
        this.field_3660 = this.field_3659;
        this.field_3659 = Mth.clamp((float)Mth.lerp((float)0.2f, (float)this.field_3659, (float)(this.field_3661 <= 0 ? 0.0f : 1.0f)), (float)0.0f, (float)1.0f);
        if ((double)this.field_3659 <= 0.05) {
            this.field_3659 = 0.0f;
        }
    }

    public void render(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull GuiGraphics guiGraphics, Font font, int n, int n2, float f) {
        CloudItem cloudItem;
        if (this.field_3659 <= 0.0f) {
            return;
        }
        PoseStack poseStack = guiGraphics.pose();
        CloudRegistry cloudRegistry = bFClientManager.getCloudRegistry();
        poseStack.pushPose();
        float f2 = MathUtils.lerpf1(this.field_3659, this.field_3660, f);
        poseStack.translate(0.0f, (float)n2 - 93.0f * f2, 0.0f);
        int n3 = n / 2 - 100;
        int n4 = n / 2;
        PlayerCloudData playerCloudData = clientPlayerDataHandler.getCloudProfile(this.uuid);
        BFClientPlayerData bFClientPlayerData = (BFClientPlayerData)clientPlayerDataHandler.getPlayerData(this.uuid);
        PlayerCloudInventory playerCloudInventory = (PlayerCloudInventory)playerCloudData.getInventory();
        ResourceLocation resourceLocation = BFRendering.DEFAULT_CALLINGCARD_TEXTURE;
        CloudItemStack cloudItemStack = playerCloudInventory.method_1673(CloudItemType.CARD);
        if (cloudItemStack != null && (cloudItem = cloudItemStack.getCloudItem(cloudRegistry)) instanceof CloudItemCallingCard) {
            CloudItemCallingCard cloudItemCallingCard = (CloudItemCallingCard)cloudItem;
            resourceLocation = BFRes.loc("textures/gui/callingcard/" + cloudItemCallingCard.getSuffixForDisplay() + ".png");
        }
        BFRendering.rectangle(guiGraphics, n3, 0, 200, 43, BFRendering.translucentBlack());
        BFRendering.texture(poseStack, guiGraphics, resourceLocation, n3 + 1, 1, 198, 41, 0.0f, 0.4f);
        BFRendering.rectangle(guiGraphics, n3 + 1, 5, 158, 33, BFRendering.translucentBlack());
        BFRendering.rectangle(guiGraphics, n3 + 200 - 41, 2, 39, 39, BFRendering.translucentBlack());
        float f3 = n3 + 200 - 40;
        float f4 = 3.0f;
        BFRendering.playerHead(minecraft, bFClientManager, poseStack, guiGraphics, playerCloudData.getMcProfile(), f3, 3.0f, 37);
        MutableComponent mutableComponent = bFClientManager.getGameClient();
        if (mutableComponent != null) {
            mutableComponent.method_2692(poseStack, guiGraphics, bFClientPlayerData, f3, 3.0f, 37);
        }
        int n5 = n3 + 3;
        MutableComponent mutableComponent2 = playerCloudData.method_1715();
        mutableComponent = Component.literal((String)String.format("%.2f", Float.valueOf(this.health))).withStyle(ChatFormatting.GREEN);
        BFRendering.drawStringWithShadow(font, guiGraphics, (Component)Component.translatable((String)"bf.killmessage.by", (Object[])new Object[]{mutableComponent2}), n5, 8);
        BFRendering.drawStringWithShadow(font, guiGraphics, (Component)Component.translatable((String)"bf.killmessage.hp", (Object[])new Object[]{mutableComponent}), n5, 19);
        MutableComponent mutableComponent3 = Component.translatable((String)"bf.killmessage.using", (Object[])new Object[]{this.itemUsing.getItem().getName(this.itemUsing)});
        BFRendering.drawStringWithShadow(font, guiGraphics, (Component)mutableComponent3, n5, 29);
        playerCloudData.getMood().ifPresent(string -> {
            MutableComponent mutableComponent = Component.literal((String)string).withStyle(ChatFormatting.ITALIC);
            int n2 = 11;
            int n3 = font.width((FormattedText)mutableComponent) + 4;
            int n4 = n4 - n3 / 2;
            int n5 = 46;
            BFRendering.rectangle(guiGraphics, n4 + 1, 45, n3 - 2, 1, BFRendering.translucentBlack());
            BFRendering.rectangle(guiGraphics, n4, 46, n3, 11, BFRendering.translucentBlack());
            BFRendering.rectangle(guiGraphics, n4 + 1, 57, n3 - 2, 1, BFRendering.translucentBlack());
            BFRendering.centeredComponent2dWithShadow(poseStack, font, guiGraphics, (Component)mutableComponent, n4, 48);
        });
        CloudItemStack cloudItemStack2 = playerCloudInventory.method_1673(CloudItemType.COIN);
        if (cloudItemStack2 != null) {
            int n6 = 16;
            Object object = cloudItemStack2.getCloudItem(cloudRegistry);
            if (object instanceof AbstractCloudItemCoin) {
                AbstractCloudItemCoin abstractCloudItemCoin = (AbstractCloudItemCoin)object;
                object = CloudItemRenderers.getRenderer(abstractCloudItemCoin);
                ((CloudItemRenderer)object).method_1745(abstractCloudItemCoin, cloudItemStack2, poseStack, minecraft, guiGraphics, n3, 0.0f, 16.0f, 16.0f, 1.0f);
            }
        }
        poseStack.popPose();
    }

    @NotNull
    public UUID getUUID() {
        return this.uuid;
    }

    @NotNull
    public ItemStack getItemUsing() {
        return this.itemUsing;
    }

    public float getHealth() {
        return this.health;
    }
}

