/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.CloudRegistry
 *  com.boehmod.bflib.cloud.common.item.CloudItem
 *  com.boehmod.bflib.cloud.common.item.CloudItemRarity
 *  com.boehmod.bflib.cloud.common.item.CloudItemStack
 *  com.boehmod.bflib.cloud.common.item.types.CloudItemCase
 *  com.boehmod.bflib.cloud.packet.IPacket
 *  com.boehmod.bflib.cloud.packet.common.inventory.PacketInventoryOpenCase
 *  com.mojang.blaze3d.vertex.PoseStack
 *  it.unimi.dsi.fastutil.objects.ObjectList
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.resources.sounds.SimpleSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.util.Mth
 *  net.minecraft.world.item.ItemStack
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.screen.armory;

import com.boehmod.bflib.cloud.common.CloudRegistry;
import com.boehmod.bflib.cloud.common.item.CloudItem;
import com.boehmod.bflib.cloud.common.item.CloudItemRarity;
import com.boehmod.bflib.cloud.common.item.CloudItemStack;
import com.boehmod.bflib.cloud.common.item.types.CloudItemCase;
import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.cloud.packet.common.inventory.PacketInventoryOpenCase;
import com.boehmod.blockfront.client.gui.widget.BFButton;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.BFMenuScreen;
import com.boehmod.blockfront.client.screen.prompt.confirm.BFConfirmPromptScreen;
import com.boehmod.blockfront.client.screen.title.ArmoryTitleScreen;
import com.boehmod.blockfront.cloud.client.ClientConnectionManager;
import com.boehmod.blockfront.common.gun.GunSoundConfig;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.item.ItemSkinIndex;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public final class ArmoryOpenCaseScreen
extends BFMenuScreen {
    private static final Component field_971 = Component.translatable((String)"bf.screen.opencase");
    private static final Component field_972 = Component.translatable((String)"bf.menu.armory.case.button.open").withStyle(ChatFormatting.BOLD);
    private static final Component field_973 = Component.translatable((String)"bf.menu.armory.case.prompt.title");
    private static final Component field_974 = Component.translatable((String)"bf.menu.button.back");
    private static final Component field_975 = Component.translatable((String)"bf.message.continue");
    private static final ResourceLocation field_964 = BFRes.loc("textures/gui/effects/starburst.png");
    private final CloudItemStack field_976;
    private CloudItemStack field_977;
    private BFButton field_978;
    private boolean field_965 = false;
    private boolean field_966 = false;
    private float field_969;
    private float field_970 = 0.0f;
    private boolean field_967 = false;
    private boolean field_968 = false;

    public ArmoryOpenCaseScreen(@NotNull CloudItemStack cloudItemStack) {
        super(field_971);
        this.field_976 = cloudItemStack;
    }

    @Override
    public void init() {
        super.init();
        CloudRegistry cloudRegistry = this.manager.getCloudRegistry();
        ArmoryOpenCaseScreen armoryOpenCaseScreen = this;
        int n = 65;
        int n2 = 10;
        this.addRenderableWidget((GuiEventListener)new BFButton(5, 18, 20, 20, (Component)Component.empty(), button -> this.minecraft.setScreen((Screen)new ArmoryTitleScreen())).texture(RETURN_ICON).size(20, 20).displayType(BFButton.DisplayType.NONE).tip(field_974));
        this.field_978 = new BFButton(this.width / 2 - 32, this.height - 60, 65, 10, field_972, button -> {
            if (this.field_977 == null) {
                CloudItemCase cloudItemCase = (CloudItemCase)this.field_976.getCloudItem(cloudRegistry);
                if (cloudItemCase != null && !this.field_965) {
                    CloudItem cloudItem = cloudItemCase.getKey();
                    String string = cloudItem.getDisplayName();
                    CloudItemRarity cloudItemRarity = cloudItem.getRarity();
                    MutableComponent mutableComponent = Component.literal((String)string).withColor(cloudItemRarity.getColor());
                    MutableComponent mutableComponent2 = Component.translatable((String)"bf.menu.armory.case.prompt", (Object[])new Object[]{mutableComponent});
                    Object t = new BFConfirmPromptScreen((Screen)armoryOpenCaseScreen, field_973, bl -> {
                        if (bl) {
                            ((ClientConnectionManager)this.manager.getConnectionManager()).sendPacket((IPacket)new PacketInventoryOpenCase(this.field_976.getUUID()));
                            this.field_978.active = false;
                            this.field_965 = true;
                        } else {
                            this.minecraft.setScreen((Screen)new ArmoryTitleScreen());
                        }
                    }).method_1084((Component)mutableComponent2);
                    this.minecraft.setScreen(t);
                }
            } else {
                this.minecraft.setScreen((Screen)new ArmoryTitleScreen());
            }
        });
        this.field_978.method_395(56320);
        this.field_978.method_397(56320);
        this.addRenderableWidget((GuiEventListener)this.field_978);
        if (this.field_965) {
            this.field_978.active = false;
            this.field_978.visible = false;
        }
    }

    public void method_729(@NotNull ObjectList<CloudItemStack> objectList) {
        this.minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.GUI_INVENTORY_CRATE_OPEN.get()), (float)1.0f));
        this.field_977 = (CloudItemStack)objectList.get(objectList.size() - 4);
        this.field_966 = true;
        this.field_965 = false;
    }

    @Override
    public void tick() {
        super.tick();
        CloudRegistry cloudRegistry = this.manager.getCloudRegistry();
        this.field_970 = this.field_969;
        if (this.field_966) {
            if (!this.field_967) {
                if (this.field_969 < 1.0f) {
                    this.field_969 += 0.05f;
                } else {
                    this.field_967 = true;
                    this.field_978.active = true;
                    this.field_978.visible = true;
                    this.field_978.setMessage(field_975);
                }
            } else {
                if (!this.field_968) {
                    GunItem gunItem;
                    DeferredHolder<SoundEvent, SoundEvent> deferredHolder;
                    Object object;
                    ItemStack itemStack;
                    this.field_968 = true;
                    CloudItem cloudItem = this.field_977.getCloudItem(cloudRegistry);
                    if (cloudItem != null && !(itemStack = ItemSkinIndex.method_1722(cloudItem, this.field_977)).isEmpty() && (object = itemStack.getItem()) instanceof GunItem && (deferredHolder = ((GunSoundConfig)(object = (gunItem = (GunItem)object).getSoundConfig(itemStack))).getReload()) != null) {
                        this.minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)deferredHolder.get()), (float)1.0f, (float)1.0f));
                    }
                }
                if (this.field_969 > 0.0f) {
                    this.field_969 -= 0.1f;
                }
            }
        }
        if (this.field_969 < 0.0f) {
            this.field_969 = 0.0f;
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
        super.render(guiGraphics, n, n2, f);
        CloudRegistry cloudRegistry = this.manager.getCloudRegistry();
        float f2 = BFRendering.getRenderTime();
        PoseStack poseStack = guiGraphics.pose();
        int n3 = this.width / 2;
        int n4 = this.height / 2;
        int n5 = n3 - 237;
        int n6 = n5 + 114;
        int n7 = 25;
        int n8 = 237;
        if (!this.field_967) {
            var14_14 = this.field_976.getCloudItem(cloudRegistry);
            if (var14_14 != null) {
                float f3 = Mth.sin((float)(f2 / 60.0f)) + 1.0f;
                float f4 = 180.0f + 40.0f * f3;
                BFRendering.centeredTexture(poseStack, guiGraphics, field_964, (float)n3, (float)n4, f4, f4, f2 / 20.0f);
                String string = this.field_976.getDisplayName(cloudRegistry);
                int n9 = this.font.width(string);
                BFRendering.method_305(poseStack, this.font, guiGraphics, (Component)Component.literal((String)string).withStyle(ChatFormatting.YELLOW), (float)n6 + (float)n8 / 2.0f, n7 + 25, n9, 10);
                float f5 = Mth.sin((float)(f2 / 45.0f));
                ItemStack itemStack = ItemSkinIndex.method_1722(var14_14, this.field_976);
                BFRendering.item(poseStack, guiGraphics, itemStack, n3, (float)n4 + 5.0f * f5, 8.0f);
            }
        } else if (this.field_977 != null) {
            BFRendering.centeredTexture(poseStack, guiGraphics, BFMenuScreen.BACKSHADOW, n3, n4, 400, 400);
            var14_14 = this.field_977.getDisplayName(cloudRegistry);
            int n10 = this.font.width((String)var14_14);
            CloudItem cloudItem = this.field_977.getCloudItem(cloudRegistry);
            if (cloudItem != null) {
                CloudItemRarity cloudItemRarity = cloudItem.getRarity();
                int n11 = cloudItemRarity.getColor();
                MutableComponent mutableComponent = Component.literal((String)var14_14).withColor(n11);
                BFRendering.method_305(poseStack, this.font, guiGraphics, (Component)mutableComponent, (float)n6 + (float)n8 / 2.0f, n7 + 25, n10, 10);
                float f6 = Mth.sin((float)(f2 / 45.0f));
                ItemStack itemStack = ItemSkinIndex.method_1722(cloudItem, this.field_977);
                BFRendering.item(poseStack, guiGraphics, itemStack, n3, (float)n4 + 5.0f * f6, 8.0f);
            }
        }
        float f7 = MathUtils.lerpf1(this.field_969, this.field_970, f);
        poseStack.pushPose();
        poseStack.translate(0.0f, 0.0f, 100.0f);
        BFRendering.rectangle(guiGraphics, 0, 0, this.width, this.height, 0xFFFFFF, f7);
        poseStack.popPose();
    }
}

