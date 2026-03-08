/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.item;

import com.boehmod.bflib.cloud.common.CloudRegistry;
import com.boehmod.bflib.cloud.common.item.CloudItem;
import com.boehmod.bflib.cloud.common.item.CloudItemRarity;
import com.boehmod.bflib.cloud.common.item.CloudItemStack;
import com.boehmod.bflib.cloud.common.item.CloudResourceLocation;
import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.render.geo.BFWeaponItemRenderer;
import com.boehmod.blockfront.cloud.PlayerCloudInventory;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.gun.GunSpreadConfig;
import com.boehmod.blockfront.common.item.BFCommonItem;
import com.boehmod.blockfront.common.item.ItemSkinIndex;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.registry.BFDataComponents;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.EnvironmentUtils;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.keyframe.event.CustomInstructionKeyframeEvent;
import software.bernie.geckolib.animation.keyframe.event.SoundKeyframeEvent;

public abstract class BFWeaponItem<T extends BFWeaponItem<T>>
extends BFCommonItem
implements GeoItem {
    public static final int field_3636 = 1200;
    public static final int field_6774 = 16;
    private float wobbleIntensity = 1.0f;
    public float field_3634 = 0.0f;
    public float field_3635 = 0.0f;
    @NotNull
    private final String field_3628;
    @NotNull
    private final ResourceLocation field_3629;
    @NotNull
    private final ResourceLocation field_3630;
    @NotNull
    private final ResourceLocation field_3631;
    @NotNull
    private GunSpreadConfig aimConfig = new GunSpreadConfig(15.0f, 0.1f, 0.015f, 0.0f, 0.025f, 0.3f, 20.0f);

    public BFWeaponItem(@NotNull String string, @NotNull Item.Properties properties) {
        super(string, properties);
        this.field_3628 = string;
        this.field_3629 = BFRes.loc("textures/item/" + this.field_3628 + ".png");
        this.field_3630 = BFRes.loc("animations/item/" + this.field_3628 + ".animation.json");
        this.field_3631 = BFRes.loc("geo/item/" + this.field_3628 + ".geo.json");
        if (!EnvironmentUtils.isClient()) {
            return;
        }
        ItemProperties.register((Item)this, (ResourceLocation)ResourceLocation.withDefaultNamespace((String)"skin"), (itemStack, clientLevel, livingEntity, n) -> BFWeaponItem.method_3771(itemStack));
    }

    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider(this){
            private BFWeaponItemRenderer<?> field_3638;

            public BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                if (this.field_3638 == null) {
                    this.field_3638 = new BFWeaponItemRenderer();
                }
                return this.field_3638;
            }
        });
    }

    public static void method_3765(@NotNull ItemStack itemStack, @NotNull String string) {
        itemStack.set(BFDataComponents.ORIGINAL_OWNER, (Object)string);
    }

    @NotNull
    public static String method_3761(@NotNull ItemStack itemStack) {
        return (String)itemStack.getOrDefault(BFDataComponents.ORIGINAL_OWNER, (Object)"");
    }

    public static void method_3762(@NotNull ItemStack itemStack, float f) {
        itemStack.set(BFDataComponents.SKIN_ID, (Object)Float.valueOf(f));
    }

    public static void method_3764(@NotNull ItemStack itemStack, int n, int n2) {
        switch (n) {
            case 0: {
                itemStack.set(BFDataComponents.STICKER0, (Object)n2);
                break;
            }
            case 1: {
                itemStack.set(BFDataComponents.STICKER1, (Object)n2);
                break;
            }
            case 2: {
                itemStack.set(BFDataComponents.STICKER2, (Object)n2);
            }
        }
    }

    public static float method_3771(@NotNull ItemStack itemStack) {
        return ((Float)itemStack.getOrDefault(BFDataComponents.SKIN_ID, (Object)Float.valueOf(0.0f))).floatValue();
    }

    public static int getStickerIndex(@NotNull ItemStack itemStack, int sticker) {
        return switch (sticker) {
            case 0 -> (Integer)itemStack.getOrDefault(BFDataComponents.STICKER0, (Object)-1);
            case 1 -> (Integer)itemStack.getOrDefault(BFDataComponents.STICKER1, (Object)-1);
            case 2 -> (Integer)itemStack.getOrDefault(BFDataComponents.STICKER2, (Object)-1);
            default -> -1;
        };
    }

    public static void method_3774(@NotNull ItemStack itemStack, @NotNull String string) {
        itemStack.set(BFDataComponents.NAME_TAG, (Object)string);
    }

    @NotNull
    public static String method_3772(@NotNull ItemStack itemStack) {
        return (String)itemStack.getOrDefault(BFDataComponents.NAME_TAG, (Object)"");
    }

    @NotNull
    public T spread(@NotNull GunSpreadConfig gunSpreadConfig) {
        this.aimConfig = gunSpreadConfig;
        return (T)((Object)this);
    }

    @NotNull
    public GunSpreadConfig getAimConfig() {
        return this.aimConfig;
    }

    @NotNull
    public abstract Vec3 method_3778();

    protected void method_3767(@NotNull ItemStack itemStack, @NotNull Entity entity) {
        itemStack.set(BFDataComponents.HAS_TAG.get(), (Object)true);
        if (!(entity instanceof Player)) {
            return;
        }
        Player player = (Player)entity;
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is null!";
        CloudRegistry cloudRegistry = bFAbstractManager.getCloudRegistry();
        Object obj = bFAbstractManager.getPlayerDataHandler();
        PlayerCloudData playerCloudData = ((PlayerDataHandler)obj).getCloudProfile(player);
        PlayerCloudInventory playerCloudInventory = (PlayerCloudInventory)playerCloudData.getInventory();
        BFWeaponItem.method_3765(itemStack, player.getScoreboardName());
        ResourceLocation resourceLocation = BuiltInRegistries.ITEM.getKey((Object)itemStack.getItem());
        CloudResourceLocation cloudResourceLocation = BFRes.toCloud(resourceLocation);
        CloudItemStack cloudItemStack = playerCloudInventory.method_5486(cloudRegistry, cloudResourceLocation);
        if (cloudItemStack != null) {
            CloudItem cloudItem = cloudItemStack.getCloudItem(cloudRegistry);
            if (cloudItem != null) {
                ItemSkinIndex.method_5941(cloudItem, itemStack);
            }
            ItemSkinIndex.method_5942(cloudItemStack, itemStack);
        }
    }

    public void inventoryTick(ItemStack itemStack, @NotNull Level level, @NotNull Entity entity, int slotId, boolean isSelected) {
        boolean bl = (Boolean)itemStack.getOrDefault(BFDataComponents.HAS_TAG.get(), (Object)false);
        if (!bl) {
            this.method_3767(itemStack, entity);
        }
        if (level.isClientSide()) {
            if (isSelected) {
                this.field_3635 = this.field_3634;
                this.field_3634 = Mth.lerp((float)0.1f, (float)this.field_3634, (float)0.0f);
                float f = Mth.abs((float)(entity.getYHeadRot() - entity.yRotO));
                if (f < 0.0f) {
                    f = -f;
                }
                while (f > 340.0f) {
                    f -= 340.0f;
                }
                if (f > 0.0f) {
                    this.field_3634 += f / 400.0f;
                }
            }
        } else if (entity.isInWaterOrRain() && level.random.nextInt(40) == 0) {
            this.method_5727(itemStack, 1);
        }
    }

    private void method_5727(@NotNull ItemStack itemStack, int n) {
        int n2;
        int n3 = (Integer)itemStack.getOrDefault(BFDataComponents.BLOOD_AMOUNT, (Object)0);
        if (n3 > 0) {
            itemStack.set(BFDataComponents.BLOOD_AMOUNT, (Object)Math.max(n3 - n, 0));
        }
        if ((n2 = ((Integer)itemStack.getOrDefault(BFDataComponents.MUD_AMOUNT, (Object)0)).intValue()) > 0) {
            itemStack.set(BFDataComponents.MUD_AMOUNT, (Object)Math.max(n2 - n, 0));
        }
    }

    @OnlyIn(value=Dist.CLIENT)
    @NotNull
    public Component getName(ItemStack itemStack) {
        String string;
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        Minecraft minecraft = Minecraft.getInstance();
        Item item = itemStack.getItem();
        if (minecraft.level == null) {
            return super.getName(itemStack);
        }
        MutableComponent mutableComponent = Component.literal((String)super.getName(itemStack).getString());
        String string2 = BFWeaponItem.method_3772(itemStack);
        if (!string2.isEmpty()) {
            mutableComponent = Component.literal((String)("\"" + string2 + "\"")).withStyle(ChatFormatting.ITALIC);
        }
        if ((string = BFWeaponItem.method_3761(itemStack)).isEmpty()) {
            return mutableComponent;
        }
        mutableComponent = Component.translatable((String)"bf.item.gun.name", (Object[])new Object[]{string, mutableComponent});
        if (BFWeaponItem.method_3771(itemStack) > 0.0f) {
            ResourceLocation resourceLocation = BuiltInRegistries.ITEM.getKey((Object)item);
            CloudResourceLocation cloudResourceLocation = BFRes.toCloud(resourceLocation);
            CloudItem cloudItem = CloudItem.getItemFromSkinId((CloudRegistry)bFClientManager.getCloudRegistry(), (CloudResourceLocation)cloudResourceLocation, (float)BFWeaponItem.method_3771(itemStack));
            if (cloudItem != null) {
                mutableComponent.append((Component)Component.literal((String)(" " + cloudItem.getSuffix())));
                CloudItemRarity cloudItemRarity = cloudItem.getRarity();
                mutableComponent.withColor(cloudItemRarity.getColor());
            }
        }
        return mutableComponent;
    }

    public boolean isPerspectiveAware() {
        return true;
    }

    @NotNull
    public ResourceLocation method_3779() {
        return this.field_3629;
    }

    @NotNull
    public ResourceLocation method_3780() {
        return this.field_3630;
    }

    @NotNull
    public ResourceLocation method_3781() {
        return this.field_3631;
    }

    @NotNull
    public String method_3757() {
        return this.field_3628;
    }

    @OnlyIn(value=Dist.CLIENT)
    protected abstract PlayState method_3768(@NotNull AnimationState<T> var1);

    @OnlyIn(value=Dist.CLIENT)
    protected abstract void method_3769(@NotNull SoundKeyframeEvent<T> var1);

    @OnlyIn(value=Dist.CLIENT)
    protected abstract void method_3776(@NotNull CustomInstructionKeyframeEvent<T> var1);

    public float getWobbleIntensity() {
        return this.wobbleIntensity;
    }

    public T wobbleIntensity(float wobbleIntensity) {
        this.wobbleIntensity = wobbleIntensity;
        return (T)((Object)this);
    }
}

