/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.item;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.event.tick.GunAimingTickable;
import com.boehmod.blockfront.client.event.tick.PlayerTickable;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.ClipTextRenderers;
import com.boehmod.blockfront.client.render.geo.gun.EjectGunGeoPart;
import com.boehmod.blockfront.client.settings.BFClientSettings;
import com.boehmod.blockfront.client.world.ShakeManager;
import com.boehmod.blockfront.common.block.BFButtonBlock;
import com.boehmod.blockfront.common.block.CrateGunBlock;
import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.common.entity.base.IProducedProjectileEntity;
import com.boehmod.blockfront.common.event.impl.GunEvent;
import com.boehmod.blockfront.common.gun.GunBarrelType;
import com.boehmod.blockfront.common.gun.GunBipodMode;
import com.boehmod.blockfront.common.gun.GunCameraConfig;
import com.boehmod.blockfront.common.gun.GunDamageConfig;
import com.boehmod.blockfront.common.gun.GunFireConfig;
import com.boehmod.blockfront.common.gun.GunFireMode;
import com.boehmod.blockfront.common.gun.GunMagType;
import com.boehmod.blockfront.common.gun.GunReloadConfig;
import com.boehmod.blockfront.common.gun.GunScopeConfig;
import com.boehmod.blockfront.common.gun.GunSoundConfig;
import com.boehmod.blockfront.common.item.BFWeaponItem;
import com.boehmod.blockfront.common.item.ChestArmorItem;
import com.boehmod.blockfront.common.item.IModifyPlayerModel;
import com.boehmod.blockfront.common.item.base.IHasCrosshair;
import com.boehmod.blockfront.common.item.base.IHasWeight;
import com.boehmod.blockfront.common.net.packet.BFGunReloadClientPacket;
import com.boehmod.blockfront.common.net.packet.BFGunSoundPacket;
import com.boehmod.blockfront.common.net.packet.BFGunTriggerPacket;
import com.boehmod.blockfront.common.particle.BulletParticleTypes;
import com.boehmod.blockfront.common.particle.BulletParticles;
import com.boehmod.blockfront.common.player.BFAbstractPlayerData;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.registry.BFDataComponents;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.unnamed.BF_624;
import com.boehmod.blockfront.util.GunUtils;
import com.boehmod.blockfront.util.PacketUtils;
import com.boehmod.blockfront.util.math.MathUtils;
import com.boehmod.blockfront.util.math.ShakeNodeData;
import com.boehmod.blockfront.util.math.ShakeNodePresets;
import it.unimi.dsi.fastutil.floats.FloatFloatPair;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.Animation;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.keyframe.event.CustomInstructionKeyframeEvent;
import software.bernie.geckolib.animation.keyframe.event.SoundKeyframeEvent;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.util.GeckoLibUtil;

public final class GunItem
extends BFWeaponItem<GunItem>
implements IHasCrosshair,
IHasWeight,
IModifyPlayerModel {
    private static final Component field_6407 = Component.translatable((String)"bf.item.gun.scoped");
    private static final Component field_6408 = Component.translatable((String)"bf.item.gun.reloading");
    @NotNull
    public static final String field_4011 = "gun_controller";
    public static final int field_4054 = 5;
    @NotNull
    public static final List<GunItem> field_4014 = new ObjectArrayList();
    @NotNull
    private static final Map<String, DeferredHolder<SoundEvent, SoundEvent>> field_4067 = new Object2ObjectOpenHashMap();
    @NotNull
    private static final Vec3 field_4002 = new Vec3(-15.0, 20.0, 10.0);
    private static final float field_6993 = 0.001f;
    private static final float field_6994 = 0.0025f;
    private static final float field_6995 = 0.005f;
    public static int field_4055 = 0;
    public static int field_4056 = 5;
    public static int field_4057 = 0;
    public static float field_4041;
    public static float field_4042;
    public static boolean field_4019;
    public float field_4043;
    public float field_4044 = 0.0f;
    public float field_4045 = 0.0f;
    private float field_4046 = 0.0f;
    private GunBipodMode bipodMode = GunBipodMode.ANY;
    @Nullable
    private DeferredItem<? extends ChestArmorItem> chestArmor;
    public static int field_4059;
    public static boolean field_4020;
    public static boolean field_4021;
    @NotNull
    private final Map<String, GunMagType> magIdMap = new Object2ObjectOpenHashMap();
    @NotNull
    private final Map<String, GunBarrelType> barrelIdMap = new Object2ObjectOpenHashMap();
    @NotNull
    private final AnimatableInstanceCache field_4017 = GeckoLibUtil.createInstanceCache((GeoAnimatable)this);
    @NotNull
    private final List<String> field_4015 = new ObjectArrayList();
    @NotNull
    private final Map<GunScopeConfig.Type, GunScopeConfig> scopes = new Object2ObjectOpenHashMap();
    private int field_4060 = 0;
    @NotNull
    private GunDamageConfig damageConfig = new GunDamageConfig(1.0f, 2.0f);
    @NotNull
    private BulletParticles bulletParticles = BulletParticleTypes.BASIC;
    @NotNull
    private GunCameraConfig cameraConfig = new GunCameraConfig(0.2f, 5.0f, 4.0f, 0.05f, 0.1f, 0.15f, 0.25f, 0.05f, 0.3f);
    private int maxHitDist = 256;
    private boolean field_4022 = true;
    private boolean bipod = false;
    private boolean isSecondary = true;
    private boolean field_4025 = false;
    private boolean field_4026 = true;
    private boolean field_4027 = true;
    private GunFireConfig[] fireConfigs;
    private int currentFireConfig = 0;
    private GunFireConfig defaultFireConfig;
    private boolean canRefillCammo = true;
    private boolean field_4029 = false;
    @NotNull
    private GunSoundConfig soundConfig = new GunSoundConfig();
    private int field_4063 = -3659;
    private float field_4047 = 1.0f;
    private boolean hasEquipSound = false;
    @Nullable
    private DeferredHolder<SoundEvent, SoundEvent> equipSound = null;
    private boolean field_4031 = false;
    private boolean field_4032 = false;
    private boolean field_4033 = false;
    private float muzzleFlashSize = 1.0f;
    @NotNull
    private Vector3f field_4018 = new Vector3f();
    private int field_4064 = 2;
    private float weight = 0.0f;
    private int triggerTimer;
    private int field_4065 = 0;
    private float field_4052 = 0.0f;
    @Nullable
    private GunReloadConfig reloadConfig = null;
    @NotNull
    private ShakeNodeData shakeNodeData = ShakeNodePresets.field_6673;
    @NotNull
    private String bulletEject = "rifle";
    private int field_4066 = 1;
    private boolean field_4035 = true;
    private boolean field_4036 = false;
    private boolean field_4037 = true;
    private boolean field_4038 = false;
    @NotNull
    private String field_4013 = "default";
    @NotNull
    private ClipTextRenderers.Renderer clipTextRenderer = ClipTextRenderers.DEFAULT;
    private boolean field_4039 = false;

    public GunItem(@NotNull String string, @NotNull Item.Properties properties) {
        super(string, properties.stacksTo(1));
        field_4014.add(this);
        this.barrelIdMap.put("default", GunBarrelType.DEFAULT);
    }

    @NotNull
    public static String getMagType(@NotNull ItemStack itemStack) {
        return (String)itemStack.getOrDefault(BFDataComponents.MAG_TYPE, (Object)"default");
    }

    @NotNull
    public static String getBarrelType(@NotNull ItemStack itemStack) {
        return (String)itemStack.getOrDefault(BFDataComponents.BARREL_TYPE, (Object)"default");
    }

    @NotNull
    public static ItemStack setMagType(@NotNull ItemStack itemStack, @NotNull String type) {
        itemStack.set(BFDataComponents.MAG_TYPE, (Object)type);
        return itemStack;
    }

    @NotNull
    public static ItemStack setBarrelType(@NotNull ItemStack itemStack, @NotNull String value) {
        itemStack.set(BFDataComponents.BARREL_TYPE, (Object)value);
        return itemStack;
    }

    public static boolean getScope(@NotNull ItemStack itemStack) {
        return (Boolean)itemStack.getOrDefault(BFDataComponents.SCOPE, (Object)false);
    }

    @NotNull
    public static ItemStack setScope(@NotNull ItemStack itemStack, boolean value) {
        itemStack.set(BFDataComponents.SCOPE, (Object)value);
        return itemStack;
    }

    public static float getHeat(@NotNull ItemStack itemStack) {
        return ((Float)itemStack.getOrDefault(BFDataComponents.HEAT, (Object)Float.valueOf(0.0f))).floatValue();
    }

    public static void setHeat(@NotNull ItemStack itemStack, float value) {
        itemStack.set(BFDataComponents.HEAT, (Object)Float.valueOf(value));
    }

    public static int getAmmoLoaded(@NotNull ItemStack itemStack) {
        return (Integer)itemStack.getOrDefault(BFDataComponents.AMMO_LOADED, (Object)0);
    }

    public static void setAmmoLoaded(@NotNull ItemStack itemStack, int value) {
        itemStack.set(BFDataComponents.AMMO_LOADED, (Object)Math.max(0, value));
    }

    public static boolean hasAmmo(@NotNull ItemStack itemStack) {
        return GunItem.getAmmoLoaded(itemStack) > 0;
    }

    public static int getAmmo(@NotNull ItemStack itemStack) {
        return (Integer)itemStack.getOrDefault(BFDataComponents.AMMO, (Object)0);
    }

    public static void setAmmo(@NotNull ItemStack itemStack, int value) {
        itemStack.set(BFDataComponents.AMMO, (Object)value);
    }

    public static void restockAmmo(@NotNull ItemStack itemStack) {
        GunItem.setAmmo(itemStack, GunItem.getMaxAmmo(itemStack));
    }

    public static int getMaxAmmo(@NotNull ItemStack itemStack) {
        return (Integer)itemStack.getOrDefault(BFDataComponents.MAX_AMMO, (Object)0);
    }

    public static void setMaxAmmo(@NotNull ItemStack itemStack, int value) {
        itemStack.set(BFDataComponents.MAX_AMMO, (Object)value);
    }

    public static void resetReloadTick(@NotNull ItemStack itemStack) {
        GunItem.setReloadTick(itemStack, 0);
    }

    public static void setReloadTick(@NotNull ItemStack itemStack, int value) {
        itemStack.set(BFDataComponents.RELOAD_TICK, (Object)value);
    }

    public static boolean isReloading(@NotNull ItemStack itemStack) {
        return GunItem.getReloadTick(itemStack) > 0;
    }

    public static int getReloadTick(@NotNull ItemStack itemStack) {
        return (Integer)itemStack.getOrDefault(BFDataComponents.RELOAD_TICK, (Object)0);
    }

    public static boolean hasMuzzleFlash(@NotNull ItemStack itemStack) {
        return (Integer)itemStack.getOrDefault(BFDataComponents.MUZZLE_FLASH, (Object)0) > 0;
    }

    public static void updateMuzzleFlash(@NotNull ItemStack itemStack) {
        itemStack.set(BFDataComponents.MUZZLE_FLASH, (Object)((Integer)itemStack.getOrDefault(BFDataComponents.MUZZLE_FLASH, (Object)0) - 1));
    }

    public static void setMuzzleFlash(@NotNull ItemStack itemStack, int value) {
        itemStack.set(BFDataComponents.MUZZLE_FLASH, (Object)value);
    }

    @OnlyIn(value=Dist.CLIENT)
    private static void method_4144(@NotNull LocalPlayer localPlayer, @NotNull ClientLevel clientLevel, float f) {
        if (!(Math.random() < (double)f)) {
            return;
        }
        float f2 = field_4019 ? 0.0f : 0.3f;
        Vec3 vec3 = new Vec3(1.25, 0.0, (double)f2).zRot((localPlayer.getXRot() + 90.0f) * ((float)Math.PI / 180) - 1.5707964f).yRot(-localPlayer.getYRot() * ((float)Math.PI / 180) - 1.5707964f);
        Vec3 vec32 = localPlayer.getEyePosition(0.0f).add(vec3);
        clientLevel.addParticle((ParticleOptions)ParticleTypes.SMOKE, true, vec32.x, vec32.y, vec32.z, 0.0, 0.0, 0.0);
    }

    @NotNull
    public GunBarrelType getBarrelTypeOrDefault(@NotNull ItemStack itemStack) {
        return this.barrelIdMap.getOrDefault(GunItem.getBarrelType(itemStack), GunBarrelType.DEFAULT);
    }

    @NotNull
    public GunMagType getMagTypeOrDefault(@NotNull ItemStack itemStack) {
        return this.magIdMap.getOrDefault(GunItem.getMagType(itemStack), GunMagType.DEFAULT);
    }

    public int getReloadTime(@NotNull ItemStack itemStack) {
        float f = 0.0f;
        if (this.reloadConfig != null) {
            f = this.reloadConfig.getReloadTime(this, itemStack);
        }
        return (int)f;
    }

    @NotNull
    public GunItem method_4124(float f) {
        this.field_4046 = f;
        return this;
    }

    @NotNull
    public GunItem bipodMode(GunBipodMode bipodMode) {
        this.bipodMode = bipodMode;
        return this;
    }

    @NotNull
    public GunItem reload(@NotNull GunReloadConfig config) {
        this.reloadConfig = config;
        return this;
    }

    public GunItem chestArmor(@NotNull DeferredItem<? extends ChestArmorItem> item) {
        this.chestArmor = item;
        return this;
    }

    @Nullable
    public DeferredItem<? extends ChestArmorItem> getChestArmor() {
        return this.chestArmor;
    }

    @NotNull
    public GunItem scope(@NotNull GunScopeConfig.Type type, @NotNull GunScopeConfig config) {
        this.scopes.put(type, config);
        return this;
    }

    @NotNull
    public GunScopeConfig getScopeConfig(@NotNull ItemStack itemStack) {
        return this.scopes.getOrDefault((Object)(GunItem.getScope(itemStack) ? GunScopeConfig.Type.SCOPE : GunScopeConfig.Type.DEFAULT), GunScopeConfig.DEFAULT);
    }

    @NotNull
    public GunItem shake(@NotNull ShakeNodeData shakeNodeData) {
        this.shakeNodeData = shakeNodeData;
        return this;
    }

    @OnlyIn(value=Dist.CLIENT)
    @NotNull
    public ClipTextRenderers.Renderer getClipTextRenderer() {
        return this.clipTextRenderer;
    }

    @OnlyIn(value=Dist.CLIENT)
    public void setClipTextRenderer(@NotNull ClipTextRenderers.Renderer renderer) {
        this.clipTextRenderer = renderer;
    }

    @NotNull
    public GunItem canRefillAmmo(boolean canRefillAmmo) {
        this.canRefillCammo = canRefillAmmo;
        return this;
    }

    @NotNull
    public GunItem method_4137(String string) {
        this.field_4013 = string;
        return this;
    }

    @NotNull
    public GunItem method_4178(boolean bl) {
        this.field_4026 = bl;
        return this;
    }

    @NotNull
    public GunItem method_4203(boolean bl) {
        this.field_4027 = bl;
        return this;
    }

    @NotNull
    public GunItem method_4213(boolean bl) {
        this.field_4022 = bl;
        return this;
    }

    @NotNull
    public GunItem method_4170() {
        this.field_4035 = false;
        return this;
    }

    @NotNull
    public GunItem method_4125(int n, float f) {
        this.field_4063 = n;
        this.field_4047 = f;
        return this;
    }

    @NotNull
    public GunItem holdCloser() {
        this.field_4038 = true;
        return this;
    }

    public boolean method_4184() {
        return this.field_4038;
    }

    @NotNull
    public GunItem damage(@NotNull GunDamageConfig gunDamageConfig) {
        this.damageConfig = gunDamageConfig;
        return this;
    }

    @NotNull
    public GunItem sound(@NotNull GunSoundConfig gunSoundConfig) {
        this.soundConfig = gunSoundConfig;
        return this;
    }

    @NotNull
    public GunItem secondary() {
        this.isSecondary = false;
        return this;
    }

    @NotNull
    public GunItem method_4214() {
        this.field_4025 = true;
        return this;
    }

    @NotNull
    public GunFireConfig getDefaultFireConfig() {
        return this.defaultFireConfig;
    }

    @NotNull
    public GunItem fireModes(@NotNull GunFireConfig[] configs) {
        this.fireConfigs = configs;
        this.currentFireConfig = 0;
        this.defaultFireConfig = configs[0];
        return this;
    }

    public boolean canRefillAmmo() {
        return this.canRefillCammo;
    }

    public void method_4179() {
        this.currentFireConfig = this.currentFireConfig >= this.fireConfigs.length - 1 ? 0 : this.currentFireConfig + 1;
        this.defaultFireConfig = this.fireConfigs[this.currentFireConfig];
    }

    @NotNull
    public GunItem defaultMag(int clip, int reserve) {
        return this.mag("default", new GunMagType(true, GunMagType.DEFAULT_NAME, clip, reserve));
    }

    @NotNull
    public GunItem mag(String string, GunMagType gunMagType) {
        this.magIdMap.put(string, gunMagType);
        return this;
    }

    @NotNull
    public GunItem barrel(@NotNull String string, @NotNull GunBarrelType gunBarrelType) {
        this.barrelIdMap.put(string, gunBarrelType);
        return this;
    }

    @NotNull
    public GunItem method_4221() {
        this.field_4039 = true;
        return this;
    }

    @NotNull
    public GunItem method_4206(int n) {
        this.maxHitDist = n;
        return this;
    }

    @NotNull
    public GunItem method_4216(int n) {
        this.field_4064 = n;
        return this;
    }

    @NotNull
    public GunItem weight(float weight) {
        this.weight = weight > 1.0f ? 1.0f : (weight < 0.0f ? 0.0f : weight);
        return this;
    }

    @NotNull
    public GunItem bipod() {
        this.bipod = true;
        return this;
    }

    @OnlyIn(value=Dist.CLIENT)
    public void method_4140(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull LocalPlayer localPlayer, @NotNull ClientLevel clientLevel, @NotNull ItemStack itemStack, @NotNull BFClientPlayerData bFClientPlayerData) {
        AbstractVehicleEntity abstractVehicleEntity;
        float f;
        boolean bl = localPlayer.getMainHandItem() == itemStack;
        boolean bl2 = GunItem.isReloading(itemStack);
        GunScopeConfig gunScopeConfig = this.getScopeConfig(itemStack);
        float f2 = GunItem.getReloadTick(itemStack);
        this.field_4065 = this.triggerTimer;
        this.field_4052 = f2;
        this.field_4044 = this.field_4043;
        int n = GunItem.getAmmoLoaded(itemStack);
        if (n > this.field_4060) {
            this.field_4031 = false;
        }
        this.field_4060 = n;
        if (this.triggerTimer > 0) {
            --this.triggerTimer;
        }
        if (bl2) {
            this.field_3634 += 0.01f;
        }
        if ((f = GunItem.getHeat(itemStack)) > 0.0f && !bl2) {
            GunItem.method_4144(localPlayer, clientLevel, f);
        }
        if (f2 > 0.0f) {
            field_4019 = false;
            return;
        }
        Entity entity = localPlayer.getVehicle();
        if (entity instanceof AbstractVehicleEntity && (abstractVehicleEntity = (AbstractVehicleEntity)entity).method_2353((Entity)localPlayer)) {
            field_4019 = false;
            return;
        }
        boolean bl3 = minecraft.screen != null && minecraft.mouseHandler.isMouseGrabbed();
        boolean bl4 = minecraft.mouseHandler.isLeftPressed() && !bl3;
        boolean bl5 = minecraft.mouseHandler.isRightPressed() && !bl3;
        GunSoundConfig gunSoundConfig = this.getSoundConfig(itemStack);
        if (field_4059 <= 0) {
            if (!this.field_4025 || GunAimingTickable.field_167 >= 1.0f) {
                if (!(!bl4 && (gunSoundConfig.getPre() == null || this.field_4037) || bFClientPlayerData.method_849() || bFClientPlayerData.isOutOfGame() || localPlayer.isEyeInFluidType((FluidType)NeoForgeMod.WATER_TYPE.value()))) {
                    if (this.method_4197(localPlayer)) {
                        this.fireClient(minecraft, bFClientManager, clientPlayerDataHandler, itemStack, localPlayer, clientLevel);
                    }
                } else {
                    field_4020 = false;
                    this.field_4036 = false;
                }
            }
            this.method_4142(minecraft, localPlayer, clientLevel, gunScopeConfig, itemStack, bl5, bl, bl2);
        }
        if (!bl4 || n <= 0) {
            this.method_4207((LivingEntity)localPlayer);
        }
    }

    private void method_4207(@NotNull LivingEntity livingEntity) {
        if (!this.field_4029) {
            return;
        }
        if (this.soundConfig.getFlamethrowerPost() != null) {
            livingEntity.playSound((SoundEvent)this.soundConfig.getFlamethrowerPost().get(), 1.0f, 1.0f);
        }
        this.field_4029 = false;
    }

    @OnlyIn(value=Dist.CLIENT)
    private void method_4142(@NotNull Minecraft minecraft, @NotNull LocalPlayer localPlayer, @NotNull ClientLevel clientLevel, @NotNull GunScopeConfig gunScopeConfig, @NotNull ItemStack itemStack, boolean bl, boolean bl2, boolean bl3) {
        BlockHitResult blockHitResult;
        Block block;
        boolean bl4 = false;
        HitResult hitResult = minecraft.hitResult;
        if (hitResult instanceof BlockHitResult && ((block = (hitResult = clientLevel.getBlockState((blockHitResult = (BlockHitResult)hitResult).getBlockPos())).getBlock()) instanceof DoorBlock || block instanceof LeverBlock || block instanceof ButtonBlock || block instanceof FenceGateBlock || block instanceof CrateGunBlock || block instanceof ChestBlock || block instanceof BFButtonBlock)) {
            bl4 = true;
        }
        if (!BFClientSettings.EXPERIMENTAL_TOGGLE_AIM.isEnabled()) {
            boolean bl5 = field_4019;
            if (gunScopeConfig.field_3845 && bl2 && !bl3 && !localPlayer.isSprinting()) {
                if (bl) {
                    if (!bl4) {
                        field_4019 = true;
                    }
                } else {
                    field_4019 = false;
                }
                if (bl4) {
                    field_4019 = false;
                }
            }
            if (field_4019 != bl5) {
                this.method_4143(minecraft, itemStack);
                if (this.field_4022) {
                    ShakeManager.applyShake(ShakeNodePresets.field_1904);
                }
            }
        } else if (bl && gunScopeConfig.field_3845 && bl2) {
            if (!field_4021) {
                field_4021 = true;
                if (bl4) {
                    return;
                }
                if (!bl3 && !localPlayer.isSprinting()) {
                    boolean bl6 = field_4019 = !field_4019;
                    if (this.field_4022) {
                        ShakeManager.applyShake(ShakeNodePresets.field_1904);
                    }
                    this.method_4143(minecraft, itemStack);
                }
            }
        } else {
            field_4021 = false;
        }
    }

    @OnlyIn(value=Dist.CLIENT)
    private void method_4143(@NotNull Minecraft minecraft, @NotNull ItemStack itemStack) {
        DeferredHolder<SoundEvent, SoundEvent> deferredHolder;
        GunSoundConfig gunSoundConfig = this.getSoundConfig(itemStack);
        DeferredHolder<SoundEvent, SoundEvent> deferredHolder2 = deferredHolder = field_4019 ? gunSoundConfig.getZoomIn() : gunSoundConfig.getZoomOut();
        if (deferredHolder != null) {
            SimpleSoundInstance simpleSoundInstance = SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)deferredHolder.get()), (float)1.0f, (float)1.0f);
            minecraft.getSoundManager().play((SoundInstance)simpleSoundInstance);
        }
    }

    @OnlyIn(value=Dist.CLIENT)
    private boolean method_4197(@NotNull LocalPlayer localPlayer) {
        Object object = localPlayer.getVehicle();
        if (object instanceof AbstractVehicleEntity) {
            AbstractVehicleEntity abstractVehicleEntity = (AbstractVehicleEntity)object;
            object = abstractVehicleEntity.method_2343();
            List list = abstractVehicleEntity.getPassengers();
            if (list.contains(localPlayer) && ((BF_624)object).method_2396((int)list.indexOf((Object)localPlayer)).field_2663) {
                return false;
            }
        }
        return !this.defaultFireConfig.getMode().equals((Object)GunFireMode.SEMI) || !field_4020;
    }

    @OnlyIn(value=Dist.CLIENT)
    public void reloadClient(@NotNull Minecraft minecraft, @NotNull ItemStack itemStack) {
        SoundEvent soundEvent = this.getReloadSound(itemStack);
        if (soundEvent != null) {
            minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)soundEvent, (float)1.0f, (float)1.0f));
        }
        if (this.reloadConfig != null) {
            this.reloadConfig.addReloadAnimations(this, itemStack, this.field_4015);
        }
        if (!minecraft.options.getCameraType().isFirstPerson()) {
            this.field_4015.clear();
        }
    }

    public void method_4229(@NotNull String string) {
        this.field_4015.add(string);
    }

    public boolean method_4145(@NotNull LivingEntity livingEntity, @NotNull Level level, @NotNull ItemStack itemStack, boolean bl) {
        GunMagType gunMagType = this.getMagTypeOrDefault(itemStack);
        int n = gunMagType.capacity();
        if (level.isClientSide() || GunItem.getAmmo(itemStack) <= 0 || GunItem.isReloading(itemStack)) {
            return false;
        }
        if (GunItem.getAmmoLoaded(itemStack) >= n) {
            return false;
        }
        if (this.triggerTimer > 0) {
            return false;
        }
        int n2 = this.getReloadTime(itemStack);
        if (livingEntity instanceof ServerPlayer) {
            SoundEvent soundEvent;
            ServerPlayer serverPlayer = (ServerPlayer)livingEntity;
            if (!serverPlayer.isShiftKeyDown() && (soundEvent = this.getReloadSound(itemStack)) != null) {
                level.playSound((Player)serverPlayer, livingEntity.blockPosition(), soundEvent, SoundSource.PLAYERS, 0.25f, 1.0f);
            }
            PacketUtils.sendToPlayer(new BFGunReloadClientPacket(bl), serverPlayer);
        }
        GunItem.setReloadTick(itemStack, n2);
        return true;
    }

    public void finishReload(@NotNull ItemStack itemStack) {
        int n = GunItem.getAmmo(itemStack);
        GunMagType gunMagType = this.getMagTypeOrDefault(itemStack);
        int n2 = gunMagType.capacity();
        if (n <= 0) {
            return;
        }
        int n3 = GunItem.getAmmoLoaded(itemStack);
        int n4 = n + n3;
        if (n4 <= n2) {
            GunItem.setAmmoLoaded(itemStack, n4);
            GunItem.setAmmo(itemStack, 0);
        } else {
            int n5 = n4 - n2;
            GunItem.setAmmoLoaded(itemStack, n2);
            GunItem.setAmmo(itemStack, n5);
        }
    }

    @OnlyIn(value=Dist.CLIENT)
    public void fireClient(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull ItemStack itemStack, @NotNull LocalPlayer localPlayer, @NotNull ClientLevel clientLevel) {
        boolean bl;
        if (localPlayer.isSpectator() || localPlayer.noPhysics) {
            return;
        }
        if (PlayerTickable.field_149 >= 0.15f || PlayerTickable.inspectionBlurPrev >= 0.15f) {
            return;
        }
        boolean bl2 = this.field_4036 && !this.field_4037;
        boolean bl3 = bl = localPlayer.isSprinting() && !bl2;
        if (GunItem.hasAmmo(itemStack) && localPlayer.getHealth() > 0.0f && this.triggerTimer <= 0 && !GunItem.isReloading(itemStack) && !bl) {
            GunSoundConfig gunSoundConfig = this.getSoundConfig(itemStack);
            DeferredHolder<SoundEvent, SoundEvent> deferredHolder = gunSoundConfig.getPre();
            if (deferredHolder != null && !this.field_4036) {
                this.field_4036 = true;
                this.field_4037 = false;
                this.triggerTimer = gunSoundConfig.getTriggerDelay();
                minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)deferredHolder.get()), (float)1.0f, (float)5.0f));
            } else {
                PlayerTickable.field_6418 = 0.0f;
                float f = GunItem.getHeat(itemStack);
                int n = (int)(5.0f * f);
                int n2 = this.defaultFireConfig.getMode() == GunFireMode.SEMI && deferredHolder != null ? gunSoundConfig.getTriggerDelay() : 0;
                this.triggerTimer = this.defaultFireConfig.getFireRate() - n2;
                GunEvent.BF_671 bF_671 = (GunEvent.BF_671)NeoForge.EVENT_BUS.post((Event)new GunEvent.BF_671(this, itemStack, this.triggerTimer));
                this.triggerTimer = this.field_4065 = bF_671.method_2573() + n;
                if (minecraft.options.getCameraType().isFirstPerson()) {
                    this.field_4015.add("fire");
                }
                if (this.field_4022) {
                    f = field_4019 || PlayerTickable.field_153 > 0.0f ? 0.5f : 1.0f;
                    ShakeManager.applyShake(this.shakeNodeData, f);
                }
                GunBarrelType gunBarrelType = this.getBarrelTypeOrDefault(itemStack);
                if (this.field_4026 && gunBarrelType.method_4100()) {
                    field_4055 = this.field_4064;
                }
                this.field_3634 += 0.1f;
                field_4020 = true;
                this.field_4043 += this.field_4045;
                ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
                GunUtils.method_1424(minecraft, threadLocalRandom, bFClientManager, clientPlayerDataHandler, this, localPlayer, clientLevel, itemStack);
                this.field_4029 = true;
                PacketUtils.sendToServer(new BFGunTriggerPacket());
                if (deferredHolder != null) {
                    if (!gunSoundConfig.method_3990()) {
                        this.field_4036 = false;
                    }
                    this.field_4037 = true;
                }
            }
        }
    }

    public ItemStack method_4155(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull LivingEntity livingEntity) {
        if (level.isClientSide()) {
            return itemStack;
        }
        int n = GunItem.getAmmoLoaded(itemStack);
        GunItem.setAmmoLoaded(itemStack, n - this.method_4181());
        Vec3 vec3 = livingEntity.getEyePosition();
        int n2 = livingEntity.getId();
        level.players().stream().filter(player -> player.getId() != n2).forEach(player -> PacketUtils.sendToPlayer(new BFGunSoundPacket(itemStack, vec3, n2, GunItem.getAmmoLoaded(itemStack) <= 0), (ServerPlayer)player));
        NeoForge.EVENT_BUS.post((Event)new GunEvent.EntityTrigger(this, itemStack, livingEntity));
        GunItem.setHeat(itemStack, GunItem.getHeat(itemStack) + 0.005f);
        return itemStack;
    }

    public int method_4181() {
        return this.field_4066;
    }

    @NotNull
    public GunItem method_4223(int n) {
        this.field_4066 = n;
        return this;
    }

    @NotNull
    public GunItem method_4215(float f) {
        this.field_4045 = f;
        return this;
    }

    public float method_4231(float f) {
        return (MathUtils.lerpf1(this.field_4043, this.field_4044, f) % 360.0f + 360.0f) % 360.0f;
    }

    public void method_4180() {
        this.field_4043 = 0.0f;
    }

    @OnlyIn(value=Dist.CLIENT)
    public void method_4134(@NotNull GunSoundConfig gunSoundConfig, @NotNull Minecraft minecraft, @NotNull ClientLevel clientLevel, @NotNull Vec3 vec3, float f) {
        SoundType soundType;
        BlockState blockState;
        if (!this.field_4035) {
            return;
        }
        Vec3 vec32 = Vec3.ZERO.add(1.0, 0.0, 1.0).yRot(-f * ((float)Math.PI / 180) - 1.5707964f).add(vec3);
        BlockHitResult blockHitResult = clientLevel.clip(new ClipContext(vec32, vec32.add(0.0, -16.0, 0.0), ClipContext.Block.VISUAL, ClipContext.Fluid.ANY, CollisionContext.empty()));
        Vec3 vec33 = blockHitResult.getLocation();
        BlockPos blockPos = blockHitResult.getBlockPos();
        DeferredHolder<SoundEvent, SoundEvent> deferredHolder = gunSoundConfig.method_3976(clientLevel, blockPos, blockState = clientLevel.getBlockState(blockPos), soundType = blockState.getSoundType((LevelReader)clientLevel, blockPos, null));
        if (deferredHolder == null) {
            return;
        }
        RandomSource randomSource = RandomSource.create();
        float f2 = 0.9f + 0.1f * (float)ThreadLocalRandom.current().nextInt(2);
        SimpleSoundInstance simpleSoundInstance = new SimpleSoundInstance((SoundEvent)deferredHolder.get(), SoundSource.PLAYERS, 1.0f, f2, randomSource, vec33.x, vec33.y, vec33.z);
        int n = (int)(3.0 * vec33.distanceTo(vec3));
        minecraft.getSoundManager().playDelayed((SoundInstance)simpleSoundInstance, n);
    }

    @NotNull
    public GunItem method_4162(@NotNull Vector3f vector3f, boolean bl) {
        this.field_4033 = true;
        this.field_4018 = vector3f;
        this.field_4032 = bl;
        return this;
    }

    @Nullable
    public SoundEvent getReloadSound(@NotNull ItemStack itemStack) {
        GunSoundConfig gunSoundConfig = this.getSoundConfig(itemStack);
        DeferredHolder<SoundEvent, SoundEvent> deferredHolder = GunItem.getAmmoLoaded(itemStack) <= 0 ? gunSoundConfig.getReloadEmpty() : gunSoundConfig.getReload();
        return deferredHolder != null ? (SoundEvent)deferredHolder.get() : null;
    }

    public float getMuzzleFlashSize() {
        return this.muzzleFlashSize;
    }

    @NotNull
    public GunItem muzzleFlashSize(float muzzleFlashSize) {
        this.muzzleFlashSize = muzzleFlashSize;
        return this;
    }

    public boolean method_4156(@NotNull ItemStack itemStack, @NotNull Level level, @NotNull LivingEntity livingEntity) {
        if (GunItem.hasAmmo(itemStack) && livingEntity.isAlive() && livingEntity.getHealth() > 0.0f) {
            Object object;
            ItemStack itemStack2 = this.method_4155(itemStack, level, livingEntity);
            if (livingEntity instanceof Player) {
                object = (Player)livingEntity;
                object.getInventory().setItem(object.getInventory().selected, itemStack2);
            } else {
                livingEntity.setItemInHand(InteractionHand.MAIN_HAND, itemStack2);
            }
            object = this.getBarrelTypeOrDefault(itemStack);
            if (this.field_4026 && ((GunBarrelType)object).method_4100()) {
                GunItem.setMuzzleFlash(itemStack2, 1);
            }
            return true;
        }
        return false;
    }

    public void method_4128(@NotNull PlayerDataHandler<?> playerDataHandler, @NotNull Level level, @NotNull Player player) {
        Supplier<EntityType<? extends IProducedProjectileEntity>> supplier = this.defaultFireConfig.method_4024();
        if (supplier == null) {
            return;
        }
        RandomSource randomSource = RandomSource.create();
        Object obj = playerDataHandler.getPlayerData(player);
        if (((BFAbstractPlayerData)obj).method_849() || ((BFAbstractPlayerData)obj).isOutOfGame()) {
            return;
        }
        float f = 0.0f;
        float f2 = 0.0f;
        int n = this.defaultFireConfig.method_4026();
        for (int i = 0; i < n; ++i) {
            Entity entity;
            if (randomSource.nextInt(5) > 0) {
                float f3 = (float)randomSource.nextInt(1) / 100.0f;
                float f4 = randomSource.nextBoolean() ? f3 : -f3;
                f += f4 * 1.5f;
            }
            f2 -= (float)randomSource.nextInt(1) / 100.0f;
            EntityType<? extends IProducedProjectileEntity> entityType = supplier.get();
            if (entityType == null || (entity = entityType.create(level)) == null) continue;
            ((IProducedProjectileEntity)entity).method_1934(player, 3.0f, new ItemStack((ItemLike)this), f, f2);
            level.addFreshEntity(entity);
        }
    }

    @NotNull
    public GunItem bulletEject(@NotNull String bulletEject) {
        this.bulletEject = bulletEject;
        return this;
    }

    public boolean isSecondary() {
        return this.isSecondary;
    }

    public GunFireConfig[] getFireConfigs() {
        return this.fireConfigs;
    }

    public boolean hasBipod() {
        return this.bipod;
    }

    public int getMaxHitDist() {
        return this.maxHitDist;
    }

    @NotNull
    public GunDamageConfig getDamageConfig() {
        return this.damageConfig;
    }

    @NotNull
    public GunItem particles(@NotNull BulletParticles bulletParticles) {
        this.bulletParticles = bulletParticles;
        return this;
    }

    @NotNull
    public BulletParticles getBulletParticles() {
        return this.bulletParticles;
    }

    public int method_4183() {
        return this.field_4063;
    }

    public float method_4168() {
        return this.field_4047;
    }

    @NotNull
    public Vector3f method_4195() {
        return this.field_4018;
    }

    public boolean method_4188() {
        return this.field_4033;
    }

    @NotNull
    public GunItem method_4161(@NotNull Vector3f vector3f) {
        return this.method_4162(vector3f, false);
    }

    public boolean method_4189() {
        return this.field_4032;
    }

    public boolean method_4190() {
        return this.field_4031;
    }

    public boolean shouldCauseReequipAnimation(@NotNull ItemStack itemStack, @NotNull ItemStack itemStack2, boolean bl) {
        return true;
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    @NotNull
    public Component getName(ItemStack itemStack) {
        GunBarrelType gunBarrelType;
        GunMagType gunMagType;
        Component component = super.getName(itemStack);
        if (GunItem.getScope(itemStack)) {
            component = component.copy().append(" ").append(field_6407);
        }
        if ((gunMagType = this.getMagTypeOrDefault(itemStack)) != GunMagType.DEFAULT && !gunMagType.isDefault()) {
            gunBarrelType = Component.translatable((String)"bf.item.gun.mag", (Object[])new Object[]{gunMagType.displayName()});
            component = component.copy().append((Component)gunBarrelType);
        }
        if ((gunBarrelType = this.getBarrelTypeOrDefault(itemStack)) != GunBarrelType.DEFAULT) {
            MutableComponent mutableComponent = Component.translatable((String)"bf.item.gun.barrel", (Object[])new Object[]{gunBarrelType.getDisplayName()});
            component = component.copy().append((Component)mutableComponent);
        }
        if (GunItem.isReloading(itemStack)) {
            component = component.copy().append(" ").append(field_6408);
        }
        return component;
    }

    public void appendHoverText(@NotNull ItemStack itemStack, @NotNull Item.TooltipContext context, @NotNull List<Component> entries, @NotNull TooltipFlag flag) {
        super.appendHoverText(itemStack, context, entries, flag);
        if (!Minecraft.getInstance().options.advancedItemTooltips) {
            return;
        }
        GunBarrelType gunBarrelType = this.getBarrelTypeOrDefault(itemStack);
        GunMagType gunMagType = this.getMagTypeOrDefault(itemStack);
        int n = gunMagType.capacity();
        int n2 = gunMagType.maxAmmo();
        int n3 = GunItem.getAmmo(itemStack);
        int n4 = this.defaultFireConfig.getFireRate();
        GunDamageConfig gunDamageConfig = this.getDamageConfig();
        entries.add((Component)Component.empty());
        entries.add((Component)Component.literal((String)"Damage Config Entries:"));
        entries.add((Component)Component.empty());
        for (Map.Entry<Float, FloatFloatPair> entry : gunDamageConfig.getEntries().entrySet()) {
            FloatFloatPair floatFloatPair = entry.getValue();
            entries.add((Component)Component.literal((String)(String.valueOf(ChatFormatting.GRAY) + "Distance >= " + String.valueOf(ChatFormatting.DARK_GRAY) + String.valueOf(entry.getKey()) + String.valueOf(ChatFormatting.GRAY) + " Blocks")));
            entries.add((Component)Component.literal((String)(String.valueOf(ChatFormatting.GRAY) + "Body: " + String.valueOf(ChatFormatting.DARK_GRAY) + floatFloatPair.leftFloat())));
            entries.add((Component)Component.literal((String)(String.valueOf(ChatFormatting.GRAY) + "Headshot: " + String.valueOf(ChatFormatting.DARK_GRAY) + floatFloatPair.rightFloat())));
            entries.add((Component)Component.empty());
        }
        entries.add((Component)Component.literal((String)"Mag Type"));
        entries.add((Component)Component.literal((String)(String.valueOf(GunItem.getAmmoLoaded(itemStack)) + String.valueOf(ChatFormatting.GRAY) + " / " + String.valueOf(ChatFormatting.WHITE) + n + String.valueOf(ChatFormatting.GRAY) + " - " + String.valueOf(ChatFormatting.WHITE) + n3)));
        entries.add((Component)gunMagType.displayName().copy().withStyle(ChatFormatting.GRAY));
        entries.add((Component)Component.literal((String)(String.valueOf(ChatFormatting.GRAY) + "Capacity: " + String.valueOf(ChatFormatting.DARK_GRAY) + n)));
        entries.add((Component)Component.empty());
        entries.add((Component)Component.literal((String)"Barrel Type"));
        entries.add((Component)gunBarrelType.getDisplayName().copy().withStyle(ChatFormatting.GRAY));
        entries.add((Component)Component.empty());
        entries.add((Component)Component.literal((String)"Other Info"));
        entries.add((Component)Component.literal((String)(String.valueOf(ChatFormatting.GRAY) + "Max Hit Dist: " + String.valueOf(ChatFormatting.DARK_GRAY) + this.maxHitDist + " Blocks")));
        entries.add((Component)Component.literal((String)(String.valueOf(ChatFormatting.GRAY) + "Fire-rate: " + String.valueOf(ChatFormatting.DARK_GRAY) + n4 + " (" + n4 / 2 + " ticks)")));
        entries.add((Component)Component.literal((String)(String.valueOf(ChatFormatting.GRAY) + "Max Ammo Count: " + String.valueOf(ChatFormatting.DARK_GRAY) + n2)));
        entries.add((Component)Component.literal((String)(String.valueOf(ChatFormatting.GRAY) + "Clip Capacity: " + String.valueOf(ChatFormatting.DARK_GRAY) + n)));
        entries.add((Component)Component.literal((String)(String.valueOf(ChatFormatting.GRAY) + "Weight: " + String.valueOf(ChatFormatting.DARK_GRAY) + this.weight)));
        entries.add((Component)Component.empty());
    }

    @Override
    @NotNull
    public Vec3 method_3778() {
        return field_4002;
    }

    @Override
    protected void method_3767(@NotNull ItemStack itemStack, @NotNull Entity entity) {
        super.method_3767(itemStack, entity);
        GunMagType gunMagType = this.getMagTypeOrDefault(itemStack);
        int n = gunMagType.capacity();
        GunItem.setAmmo(itemStack, gunMagType.maxAmmo());
        GunItem.setMaxAmmo(itemStack, gunMagType.maxAmmo());
        GunItem.setAmmoLoaded(itemStack, n);
        Level level = entity.level();
        if (level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level;
            GeoItem.getOrAssignId((ItemStack)itemStack, (ServerLevel)serverLevel);
        }
        GunItem.resetReloadTick(itemStack);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, @NotNull Level level, @NotNull Entity entity, int n, boolean bl) {
        super.inventoryTick(itemStack, level, entity, n, bl);
        if (entity instanceof Player) {
            Player player = (Player)entity;
            if (bl) {
                player.swinging = false;
            }
        }
        if (GunItem.hasMuzzleFlash(itemStack)) {
            GunItem.updateMuzzleFlash(itemStack);
        }
        if (!level.isClientSide()) {
            float f = 0.001f;
            float f2 = GunItem.getHeat(itemStack);
            if (entity.isInWaterOrRain()) {
                f = 0.0025f;
            }
            if (entity.isInWater()) {
                f = 0.005f;
            }
            if (f2 > 0.0f && (f2 -= f) < 0.0f) {
                f2 = 0.0f;
            }
            GunItem.setHeat(itemStack, f2);
        }
        if (!bl || level.isClientSide()) {
            return;
        }
        int n2 = GunItem.getReloadTick(itemStack);
        if (n2 > 0) {
            GunItem.setReloadTick(itemStack, n2 - 1);
            if (GunItem.getReloadTick(itemStack) <= 0) {
                this.finishReload(itemStack);
            }
        }
    }

    public float method_4232(float f) {
        return MathUtils.lerpf1(this.triggerTimer, this.field_4065, f);
    }

    public float method_4152(@NotNull ItemStack itemStack, float f) {
        return MathUtils.lerpf1(GunItem.getReloadTick(itemStack), this.field_4052, f);
    }

    @NotNull
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand interactionHand) {
        return new InteractionResultHolder(InteractionResult.FAIL, (Object)player.getItemInHand(interactionHand));
    }

    @Override
    public boolean method_3728(@NotNull Player player, @NotNull ItemStack itemStack) {
        return !player.isCreative();
    }

    @Override
    public float getWeight(@NotNull ItemStack itemStack) {
        return this.weight;
    }

    public boolean hurtEnemy(@NotNull ItemStack itemStack, @NotNull LivingEntity livingEntity, @NotNull LivingEntity livingEntity2) {
        return false;
    }

    public boolean onLeftClickEntity(@NotNull ItemStack itemStack, @NotNull Player player, @NotNull Entity entity) {
        return false;
    }

    public boolean canAttackBlock(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull Player player) {
        return false;
    }

    @Override
    public void modifyPlayerModel(@NotNull Minecraft minecraft, boolean bl, @NotNull PlayerModel<?> model, @NotNull BFClientPlayerData playerData, @NotNull AbstractClientPlayer player, float f, float f2) {
        float f3;
        boolean bl2;
        ItemStack itemStack = player.getMainHandItem();
        if (itemStack.getItem() instanceof GunItem && GunItem.isReloading(itemStack)) {
            float f4 = Mth.sin((float)(f / 30.0f));
            float f5 = Mth.sin((float)(f / 25.0f));
            float f6 = Mth.sin((float)(f / 5.0f));
            float f7 = Mth.sin((float)(f / 15.0f));
            model.head.xRot = 0.9f;
            model.head.yRot = 0.2f * (f4 *= f7);
            model.rightArm.xRot = 0.2f * (f5 *= f6 * f7) - 0.5f;
            model.rightArm.yRot = -0.4f;
            model.rightArm.zRot = 0.0f;
            model.leftArm.xRot = -0.6f;
            model.leftArm.yRot = 0.6f + 0.2f * f5;
            model.leftArm.zRot = 0.6f;
            return;
        }
        if (!bl) {
            bl2 = playerData.method_842();
            player.swinging = false;
            player.attackAnim = 0.0f;
        } else {
            bl2 = field_4019 || field_4057 > 0;
        }
        model.rightArm.xRot = bl2 ? -1.55f + model.head.xRot : -0.5f;
        model.rightArm.yRot = bl2 ? model.head.yRot : -1.2f;
        model.rightArm.zRot = bl2 ? 0.0f : -0.4f;
        float f8 = f3 = bl2 ? -0.4f : 0.0f;
        if (this.isSecondary()) {
            model.leftArm.xRot = bl2 ? -1.6f + model.head.xRot : -0.2f;
            model.leftArm.yRot = bl2 ? 0.9f + model.head.yRot : -0.5f;
            model.leftArm.zRot = bl2 ? model.head.zRot : -0.4f;
            model.head.zRot = f3;
        } else if (!bl2 || !this.method_4184()) {
            model.leftArm.xRot = bl2 ? -1.6f + model.head.xRot : -0.4f;
            model.leftArm.yRot = bl2 ? 0.9f + model.head.yRot : -0.5f;
            model.leftArm.zRot = bl2 ? model.head.zRot : 0.6f;
            model.head.zRot = f3;
        }
        if (Minecraft.useFancyGraphics() && bl2) {
            float f9 = 1.5f * MathUtils.lerpf1(playerData.field_1165, playerData.field_1166, f2);
            model.rightArm.z += f9;
            model.rightSleeve.z += f9;
            if (!this.method_4184()) {
                model.leftArm.z += f9;
                model.leftSleeve.z += f9;
            }
            float f10 = Mth.sin((float)f) * f9;
            float f11 = Mth.sin((float)(f * 1.2f)) * f9;
            float f12 = 0.01f * f10;
            float f13 = 0.01f * f11;
            model.rightArm.xRot += f12;
            model.rightSleeve.xRot += f12;
            model.rightArm.zRot += f13;
            model.rightSleeve.zRot += f13;
            if (!this.method_4184()) {
                model.leftArm.xRot += f12;
                model.leftArm.xRot += f12;
                model.leftArm.zRot += f13;
                model.leftArm.zRot += f13;
            }
        }
    }

    @OnlyIn(value=Dist.CLIENT)
    public void registerControllers(@NotNull AnimatableManager.ControllerRegistrar controllerRegistrar) {
        AnimationController animationController = new AnimationController((GeoAnimatable)this, field_4011, 0, this::method_3768).setSoundKeyframeHandler(this::method_3769).setCustomInstructionKeyframeHandler(this::method_3776);
        controllerRegistrar.add(animationController);
    }

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.field_4017;
    }

    public boolean shouldPlayAnimsWhileGamePaused() {
        return true;
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    protected PlayState method_3768(@NotNull AnimationState<GunItem> animationState) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) {
            return PlayState.STOP;
        }
        ItemDisplayContext itemDisplayContext = (ItemDisplayContext)animationState.getData(DataTickets.ITEM_RENDER_PERSPECTIVE);
        if (itemDisplayContext == null || !itemDisplayContext.firstPerson()) {
            return PlayState.STOP;
        }
        GunItem gunItem = (GunItem)animationState.getAnimatable();
        String string = gunItem.method_3757();
        ItemStack itemStack = (ItemStack)animationState.getData(DataTickets.ITEMSTACK);
        if (itemStack == null) {
            return PlayState.STOP;
        }
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        ClientPlayerDataHandler clientPlayerDataHandler = (ClientPlayerDataHandler)bFClientManager.getPlayerDataHandler();
        boolean bl = this.reloadConfig != null && this.reloadConfig.hasEmptyDuration() && GunItem.getAmmoLoaded(itemStack) <= 0;
        boolean bl2 = clientPlayerDataHandler.getPlayerData(minecraft).isOutOfGame();
        String string2 = "animation." + string + ".";
        AnimationController animationController = animationState.getController();
        if (!this.field_4015.isEmpty()) {
            animationController.forceAnimationReset();
            RawAnimation rawAnimation = RawAnimation.begin();
            boolean bl3 = false;
            for (String string3 : this.field_4015) {
                if (string3.contains("fire") && bl) continue;
                rawAnimation.then(string2 + string3, Animation.LoopType.PLAY_ONCE);
                if (!string3.contains("reload")) continue;
                bl3 = true;
            }
            this.field_4015.clear();
            rawAnimation.thenPlayAndHold(string2 + (bl3 ? "idle" : (bl ? "idle_empty" : "idle")));
            animationState.resetCurrentAnimation();
            animationState.setAnimation(rawAnimation);
        } else if (animationController.getAnimationState() == AnimationController.State.STOPPED || bl2) {
            animationState.resetCurrentAnimation();
            animationState.setAnimation(RawAnimation.begin().thenPlayAndHold(string2 + (bl ? "idle_empty" : "idle")));
        }
        return PlayState.CONTINUE;
    }

    public GunSoundConfig getSoundConfig(@NotNull ItemStack itemStack) {
        GunBarrelType gunBarrelType = this.getBarrelTypeOrDefault(itemStack);
        GunSoundConfig gunSoundConfig = gunBarrelType.getSoundConfig();
        if (gunSoundConfig != null) {
            return gunSoundConfig;
        }
        return this.soundConfig;
    }

    public GunItem equipSound(@Nullable DeferredHolder<SoundEvent, SoundEvent> sound) {
        this.hasEquipSound = true;
        this.equipSound = sound;
        return this;
    }

    @OnlyIn(value=Dist.CLIENT)
    public void method_4230(@NotNull ItemStack itemStack) {
        if (this.hasEquipSound && !GunItem.isReloading(itemStack)) {
            this.method_4229("equip");
        }
        boolean bl = this.reloadConfig != null && this.reloadConfig.hasEmptyDuration() && GunItem.getAmmoLoaded(itemStack) <= 0;
        this.method_4229(bl ? "idle_empty" : "idle");
        if (this.equipSound != null) {
            float f = 0.9f + 0.2f * ThreadLocalRandom.current().nextFloat();
            SimpleSoundInstance simpleSoundInstance = SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)this.equipSound.get()), (float)f, (float)1.0f);
            Minecraft.getInstance().getSoundManager().play((SoundInstance)simpleSoundInstance);
        }
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    protected void method_3769(@NotNull SoundKeyframeEvent<GunItem> soundKeyframeEvent) {
        DeferredHolder<SoundEvent, SoundEvent> deferredHolder = field_4067.get(soundKeyframeEvent.getKeyframeData().getSound());
        if (deferredHolder != null) {
            Minecraft minecraft = Minecraft.getInstance();
            SoundManager soundManager = minecraft.getSoundManager();
            float f = 0.95f + 0.1f * ThreadLocalRandom.current().nextFloat();
            soundManager.play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)deferredHolder.get()), (float)f, (float)0.5f));
        }
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    protected void method_3776(@NotNull CustomInstructionKeyframeEvent<GunItem> customInstructionKeyframeEvent) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer localPlayer = minecraft.player;
        ClientLevel clientLevel = minecraft.level;
        if (localPlayer == null || clientLevel == null) {
            return;
        }
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        String string = customInstructionKeyframeEvent.getKeyframeData().getInstructions();
        Vec3 vec3 = localPlayer.getEyePosition();
        float f = localPlayer.getYRot();
        ItemStack itemStack = localPlayer.getMainHandItem();
        GunSoundConfig gunSoundConfig = this.getSoundConfig(itemStack);
        switch (string) {
            case "eject_bullet;": {
                EjectGunGeoPart.method_1261(threadLocalRandom, this.bulletEject, false);
                this.method_4134(gunSoundConfig, minecraft, clientLevel, vec3, f);
                break;
            }
            case "eject_bullet_flipped;": {
                EjectGunGeoPart.method_1261(threadLocalRandom, this.bulletEject, true);
                this.method_4134(gunSoundConfig, minecraft, clientLevel, vec3, f - 90.0f);
                break;
            }
            case "eject_bullet_audio;": {
                this.method_4134(gunSoundConfig, minecraft, clientLevel, vec3, f);
                break;
            }
            case "eject_bullet_audio_flipped;": {
                this.method_4134(gunSoundConfig, minecraft, clientLevel, vec3, f - 90.0f);
                break;
            }
            case "disable_dynamic_bullets;": {
                this.field_4031 = true;
                break;
            }
            case "reset_mag_rotation;": {
                this.method_4180();
                break;
            }
            default: {
                if (!string.startsWith("shake(") || !string.endsWith(");")) break;
                ShakeManager.applyInstruction(string);
            }
        }
    }

    public boolean method_4191() {
        return this.field_4039;
    }

    public boolean method_4192() {
        return this.field_4027;
    }

    @NotNull
    public String method_4117() {
        return this.field_4013;
    }

    @NotNull
    public ShakeNodeData getShakeNodeData() {
        return this.shakeNodeData;
    }

    public float method_4169() {
        return this.field_4046;
    }

    @NotNull
    public GunBipodMode getBipod() {
        return this.bipodMode;
    }

    @NotNull
    public GunCameraConfig getCameraConfig() {
        return this.cameraConfig;
    }

    @NotNull
    public GunItem camera(@NotNull GunCameraConfig gunCameraConfig) {
        this.cameraConfig = gunCameraConfig;
        return this;
    }

    static {
        field_4042 = 0.0f;
        field_4019 = false;
        field_4059 = 0;
        field_4020 = false;
        field_4021 = false;
        field_4067.put("reload_pump", BFSounds.ITEM_GUN_SHARED_RELOAD_PUMP);
        field_4067.put("reload_pump_back", BFSounds.ITEM_GUN_SHARED_RELOAD_PUMP_BACK);
        field_4067.put("reload_pump_forward", BFSounds.ITEM_GUN_SHARED_RELOAD_PUMP_FORWARD);
        field_4067.put("reload_bolt_back", BFSounds.ITEM_GUN_SHARED_RELOAD_BOLT_BACK);
        field_4067.put("reload_bolt_forward", BFSounds.ITEM_GUN_SHARED_RELOAD_BOLT_FORWARD);
        field_4067.put("reload_shell", BFSounds.ITEM_GUN_SHARED_RELOAD_SHELL);
        field_4067.put("reload_bullet_rifle", BFSounds.ITEM_GUN_SHARED_RELOAD_BULLET_RIFLE);
        field_4067.put("reload_winchester_lever", BFSounds.ITEM_GUN_SHARED_RELOAD_WINCHESTER_LEVER);
    }
}

