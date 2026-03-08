/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.screen.armory;

import com.boehmod.bflib.cloud.common.CloudRegistry;
import com.boehmod.bflib.cloud.common.item.CloudItem;
import com.boehmod.bflib.cloud.common.item.CloudItemStack;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.gui.widget.BFButton;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.item.CloudItemRenderer;
import com.boehmod.blockfront.client.render.item.CloudItemRenderers;
import com.boehmod.blockfront.client.screen.BFMenuScreen;
import com.boehmod.blockfront.client.screen.title.ArmoryTitleScreen;
import com.boehmod.blockfront.common.gun.GunSoundConfig;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.item.ItemSkinIndex;
import com.boehmod.blockfront.util.BFStyles;
import com.boehmod.blockfront.util.InventorySoundUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public final class ArmoryInspectScreen
extends BFMenuScreen {
    private static final Component field_952 = Component.translatable((String)"bf.screen.armory.inspect");
    public static float field_949 = 0.0f;
    public static float field_950 = 0.0f;
    @NotNull
    private final CloudItemStack field_951;
    public float field_947 = 0.0f;
    public float field_948 = 0.0f;

    public ArmoryInspectScreen(@NotNull CloudItemStack cloudItemStack) {
        super(field_952);
        GunItem gunItem;
        DeferredHolder<SoundEvent, SoundEvent> deferredHolder;
        Object object;
        ItemStack itemStack;
        this.field_951 = cloudItemStack;
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        CloudRegistry cloudRegistry = bFClientManager.getCloudRegistry();
        SoundManager soundManager = this.minecraft.getSoundManager();
        CloudItem cloudItem = this.field_951.getCloudItem(cloudRegistry);
        if (cloudItem != null && !(itemStack = ItemSkinIndex.method_1722(cloudItem, cloudItemStack)).isEmpty() && (object = itemStack.getItem()) instanceof GunItem && (deferredHolder = ((GunSoundConfig)(object = (gunItem = (GunItem)object).getSoundConfig(itemStack))).getReload()) != null) {
            soundManager.play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)deferredHolder.get()), (float)1.0f, (float)1.0f));
        }
        InventorySoundUtils.playInspectFull(this.minecraft, cloudRegistry, cloudItemStack);
    }

    @Override
    public void tick() {
        Item item;
        super.tick();
        CloudRegistry cloudRegistry = this.manager.getCloudRegistry();
        Vec3 vec3 = new Vec3((double)(field_949 - this.field_947), 0.0, (double)(field_950 - this.field_948)).scale(10.0);
        CloudItem cloudItem = this.field_951.getCloudItem(cloudRegistry);
        if (cloudItem != null && (item = ItemSkinIndex.getItem(cloudItem)) instanceof GunItem) {
            GunItem gunItem = (GunItem)item;
            double d = vec3.x + vec3.z;
            gunItem.field_3634 += Mth.abs((float)((float)d)) * 0.001f;
            gunItem.field_3635 = gunItem.field_3634;
            gunItem.field_3634 = Mth.lerp((float)0.1f, (float)gunItem.field_3634, (float)0.0f);
        }
        this.field_947 = field_949;
        this.field_948 = field_950;
    }

    @Override
    public void method_758() {
        super.method_758();
        int n = 20;
        Button.OnPress onPress = button -> this.minecraft.setScreen((Screen)new ArmoryTitleScreen());
        this.addRenderableWidget((GuiEventListener)new BFButton(5, 18, 20, 20, (Component)Component.empty(), onPress).texture(RETURN_ICON).size(20, 20).displayType(BFButton.DisplayType.NONE).tip((Component)Component.translatable((String)"bf.menu.button.back")));
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
        super.render(guiGraphics, n, n2, f);
        CloudRegistry cloudRegistry = this.manager.getCloudRegistry();
        int n3 = this.width / 2;
        int n4 = this.height / 2;
        CloudItem cloudItem = this.field_951.getCloudItem(cloudRegistry);
        if (cloudItem == null) {
            return;
        }
        PoseStack poseStack = guiGraphics.pose();
        int n5 = cloudItem.getRarity().getColor();
        Style style = Style.EMPTY.withColor(n5);
        BFRendering.centeredTexture(poseStack, guiGraphics, BFMenuScreen.BACKSHADOW, n3, n4, 200, 200);
        CloudItemRenderer<?> cloudItemRenderer = CloudItemRenderers.getRenderer(cloudItem);
        cloudItemRenderer.method_1747(cloudItem, this.field_951, this.minecraft, poseStack, guiGraphics, this.width, this.height, n, n2, f);
        Optional optional = this.field_951.getNameTag();
        String string2 = optional.map(string -> String.valueOf(ChatFormatting.ITALIC) + " (" + string + ")").orElse("");
        String string3 = this.field_951.getDisplayName(cloudRegistry) + string2;
        MutableComponent mutableComponent = Component.literal((String)string3).withStyle(style).withStyle(BFStyles.BOLD);
        BFRendering.centeredComponent2d(poseStack, this.font, guiGraphics, (Component)mutableComponent, n3, this.height - 55, 1.5f);
    }

    public CloudItemStack method_717() {
        return this.field_951;
    }
}

