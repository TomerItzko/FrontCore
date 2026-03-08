/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  javax.annotation.Nullable
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.resources.sounds.SimpleSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.client.sounds.SoundManager
 *  net.minecraft.core.BlockPos
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ClipContext
 *  net.minecraft.world.level.ClipContext$Block
 *  net.minecraft.world.level.ClipContext$Fluid
 *  net.minecraft.world.level.LevelReader
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.HitResult$Type
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.client.event.ClientTickEvent$Post
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.event.tick;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.BFKeyMappings;
import com.boehmod.blockfront.client.event.BFRenderHandSubscriber;
import com.boehmod.blockfront.client.event.tick.ClientTickable;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.effect.WeatherEffectType;
import com.boehmod.blockfront.client.render.geo.gun.MuzzleFlashGunGeoPart;
import com.boehmod.blockfront.client.render.minimap.MinimapRendering;
import com.boehmod.blockfront.client.settings.BFClientSettings;
import com.boehmod.blockfront.client.sound.GunHeatSound;
import com.boehmod.blockfront.client.world.ShakeManager;
import com.boehmod.blockfront.common.block.base.IBarrierBlock;
import com.boehmod.blockfront.common.block.base.IBlockStepSoundable;
import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.common.entity.ArtilleryVehicleEntity;
import com.boehmod.blockfront.common.gun.GunSoundConfig;
import com.boehmod.blockfront.common.gun.GunSpreadTarget;
import com.boehmod.blockfront.common.item.BFWeaponItem;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.item.RadioItem;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.map.MapEnvironment;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.unnamed.BF_179;
import com.boehmod.blockfront.unnamed.BF_180;
import com.boehmod.blockfront.unnamed.BF_393;
import com.boehmod.blockfront.unnamed.BF_398;
import com.boehmod.blockfront.util.math.ShakeNodePresets;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public final class PlayerTickable
extends ClientTickable {
    @NotNull
    public static final List<BF_180> field_160 = new ObjectArrayList();
    private static final float field_138 = 45.0f;
    private static final double field_161 = (double)0.8f;
    private static final float field_139 = 0.4f;
    public static final float field_140 = 0.15f;
    public static final float field_141 = 12.0f;
    public static final Vec3 field_159 = new Vec3(0.5, 0.0, 0.0);
    public static final double field_162 = 0.1;
    public static final double field_163 = 0.1;
    public static float FLASH_TIMER;
    public static float FLASH_TIMER_PREV;
    public static float field_147;
    public static float field_148;
    public static float field_149;
    public static float field_150;
    public static float field_151;
    public static float field_152;
    public static float field_6606;
    public static float field_6607;
    public static boolean field_136;
    public static float field_153;
    public static float field_154;
    public static float inspectionBlurPrev;
    public static float inspectionBlur;
    public static float field_157;
    public static float field_158;
    public static float field_126;
    public static float field_127;
    public static float field_6418;
    public static GunSpreadTarget field_6605;
    @Nullable
    public BF_398 field_134 = null;
    @Nullable
    public BF_393 field_132 = null;
    @Nullable
    public GunItem field_135 = null;
    @Nullable
    public GunHeatSound field_133 = null;
    public float field_128 = 0.0f;
    private float field_129;
    private float field_130 = 0.0f;
    private float field_131 = 0.0f;
    private boolean field_137 = false;

    private static void method_162(@NotNull LocalPlayer localPlayer, @NotNull ClientLevel clientLevel, @NotNull SoundManager soundManager, @NotNull Random random) {
        boolean bl;
        GunItem gunItem;
        Item item;
        ItemStack itemStack = localPlayer.getMainHandItem();
        field_154 = field_153;
        if (itemStack.isEmpty() || !((item = itemStack.getItem()) instanceof GunItem) || !(gunItem = (GunItem)item).hasBipod()) {
            field_153 = 0.0f;
            PlayerTickable.method_164(false, soundManager, random);
            return;
        }
        boolean bl2 = false;
        float f = Mth.abs((float)localPlayer.getXRot());
        boolean bl3 = bl = f > 45.0f;
        if (localPlayer.isCrouching() && !bl) {
            float f2;
            Vec3 vec3 = localPlayer.getEyePosition();
            Vec3 vec32 = vec3.add(field_159.yRot(f2 = (float)Math.toRadians(-localPlayer.getYHeadRot() - 90.0f)));
            BlockHitResult blockHitResult = clientLevel.clip(new ClipContext(vec32, vec32.add(0.0, (double)-0.8f, 0.0), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, (Entity)localPlayer));
            bl2 = blockHitResult.getType() == HitResult.Type.BLOCK;
        }
        bl2 = bl2 && !((double)field_149 >= 0.1) && !((double)inspectionBlurPrev >= 0.1);
        PlayerTickable.method_164(bl2, soundManager, random);
        field_153 = Mth.lerp((float)0.4f, (float)field_153, (float)(bl2 ? 1.0f : 0.0f));
    }

    private static void method_164(boolean bl, @NotNull SoundManager soundManager, @NotNull Random random) {
        if (bl && !field_136) {
            field_136 = true;
            ShakeManager.applyShake(ShakeNodePresets.field_1921);
            float f = 0.9f + 0.1f * random.nextFloat();
            soundManager.play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.ITEM_GUN_SHARED_DEPLOY.get()), (float)1.0f, (float)f));
        } else if (!bl && field_136) {
            field_136 = false;
            ShakeManager.applyShake(ShakeNodePresets.field_1920);
            float f = 0.9f + 0.1f * random.nextFloat();
            soundManager.play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.ITEM_GUN_SHARED_UNDEPLOY.get()), (float)1.0f, (float)f));
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void run(@NotNull ClientTickEvent.Post event, @NotNull Random random, @NotNull Minecraft minecraft, @NotNull ClientPlayerDataHandler dataHandler, @NotNull BFClientManager manager, @Nullable LocalPlayer player, @Nullable ClientLevel level, @NotNull BFClientPlayerData playerData, @NotNull PlayerCloudData cloudData, @NotNull Vec3 pos, @NotNull BlockPos blockPos, @Nullable AbstractGame<?, ?, ?> game, boolean bl, float renderTime) {
        Block block;
        BlockState blockState;
        Object object;
        Object object2;
        if (player == null || level == null) {
            return;
        }
        SoundManager soundManager = minecraft.getSoundManager();
        ItemStack itemStack = player.getMainHandItem();
        Item item = itemStack.getItem();
        this.method_163(soundManager, itemStack);
        if (player.onGround()) {
            if (!this.field_137) {
                this.field_137 = true;
                if (this.field_131 >= 1.0f) {
                    soundManager.play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.ENTITY_PLAYER_FOLEY_LAND.get()), (float)(0.9f + 0.2f * random.nextFloat()), (float)0.3f));
                    ShakeManager.applyShake(ShakeNodePresets.field_1922);
                    BlockPos blockPos2 = player.getOnPos();
                    BlockState blockState2 = level.getBlockState(blockPos2);
                    object2 = blockState2.getSoundType((LevelReader)level, blockPos2, null);
                    soundManager.play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)object2.getStepSound(), (float)1.0f, (float)0.3f));
                    soundManager.play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)object2.getHitSound(), (float)1.0f, (float)0.3f));
                }
            }
        } else {
            this.field_137 = false;
        }
        this.field_131 = player.fallDistance;
        boolean bl2 = BFKeyMappings.GUN_INSPECT.isDown() && item instanceof BFWeaponItem && !GunItem.isReloading(itemStack) && !GunItem.field_4019;
        inspectionBlur = inspectionBlurPrev;
        inspectionBlurPrev = Mth.lerp((float)0.15f, (float)inspectionBlurPrev, (float)(bl2 ? 1.0f : 0.0f));
        field_148 = field_147;
        field_150 = field_149;
        field_152 = field_151;
        field_6607 = field_6606;
        BFRenderHandSubscriber.field_363 = BFRenderHandSubscriber.field_362;
        BFRenderHandSubscriber.field_344 = BFRenderHandSubscriber.field_343;
        BFRenderHandSubscriber.field_346 = BFRenderHandSubscriber.field_345;
        BFRenderHandSubscriber.field_362 = Mth.lerp((float)0.15f, (float)BFRenderHandSubscriber.field_362, (float)Mth.clamp((float)BFRenderHandSubscriber.field_359, (float)-12.0f, (float)12.0f));
        BFRenderHandSubscriber.field_343 = Mth.lerp((float)0.15f, (float)BFRenderHandSubscriber.field_343, (float)Mth.clamp((float)BFRenderHandSubscriber.field_360, (float)-12.0f, (float)12.0f));
        BFRenderHandSubscriber.field_345 = Mth.lerp((float)0.15f, (float)BFRenderHandSubscriber.field_345, (float)Mth.clamp((float)BFRenderHandSubscriber.field_361, (float)-12.0f, (float)12.0f));
        float f = 0.0f;
        if (!player.isCreative() && (object = minecraft.hitResult) instanceof BlockHitResult) {
            object2 = (BlockHitResult)object;
            double d = minecraft.hitResult.getLocation().distanceTo(player.getEyePosition());
            boolean bl3 = false;
            blockState = level.getBlockState(object2.getBlockPos());
            block = blockState.getBlock();
            boolean bl4 = blockState.isSolid();
            if (block instanceof IBarrierBlock || !bl4) {
                bl3 = true;
            }
            if (!bl4 || !blockState.canOcclude()) {
                bl3 = true;
            }
            float f2 = 1.0f;
            if (!bl3 && d <= 1.0) {
                f = (float)(1.0 - d);
            }
        }
        object2 = MuzzleFlashGunGeoPart.field_1734;
        synchronized (object2) {
            MuzzleFlashGunGeoPart.field_1734.removeIf(BFRenderHandSubscriber.BF_71::method_426);
        }
        PlayerTickable.method_162(player, level, soundManager, random);
        field_6418 = Mth.lerp((float)0.1f, (float)field_6418, (float)1.0f);
        field_149 = Mth.lerp((float)0.25f, (float)field_149, (float)f);
        if (BFClientSettings.UI_RENDER_GAME_MINIMAP.isEnabled()) {
            MinimapRendering.update(minecraft, player, level);
        }
        if ((object = player.getVehicle()) instanceof AbstractVehicleEntity) {
            object2 = (AbstractVehicleEntity)object;
            if ((object = ((AbstractVehicleEntity)object2).method_2324((Entity)player)) != null) {
                field_147 = Mth.lerp((float)0.1f, (float)field_147, (float)((AbstractVehicleEntity)object2).method_2311());
                ArtilleryVehicleEntity.field_2637 = minecraft.mouseHandler.isRightPressed() && object.field_2673 > 0.0f;
            }
        } else {
            ArtilleryVehicleEntity.field_2637 = false;
            field_147 = Mth.lerp((float)0.1f, (float)field_147, (float)0.0f);
        }
        boolean bl5 = item instanceof GunItem && GunItem.isReloading(itemStack);
        field_151 = Mth.lerp((float)0.2f, (float)field_151, (float)(bl5 ? 1.0f : 0.0f));
        boolean bl6 = player.isSprinting();
        field_6606 = Mth.lerp((float)0.2f, (float)field_6606, (float)(bl6 ? 1.0f : 0.0f));
        FLASH_TIMER_PREV = FLASH_TIMER;
        FLASH_TIMER = Mth.lerp((float)0.02f, (float)FLASH_TIMER, (float)0.0f);
        if (this.field_128 > player.moveDist + 2.0f) {
            this.field_128 = player.moveDist + 1.0f;
        }
        field_126 = field_157;
        field_127 = field_158;
        field_157 += (this.field_129 - player.getXRot()) * 3.0f;
        field_158 += (this.field_130 - player.yHeadRot) * 3.0f;
        this.field_129 = player.getXRot();
        this.field_130 = player.getYRot();
        field_157 *= 0.5f;
        field_158 *= 0.5f;
        boolean bl7 = !itemStack.isEmpty() && item instanceof RadioItem;
        RadioItem.field_3967 = RadioItem.field_3966;
        RadioItem.field_3966 = Mth.lerp((float)0.5f, (float)RadioItem.field_3966, (float)(bl7 ? 1.0f : 0.0f));
        if (player.moveDist > this.field_128) {
            this.field_128 = player.moveDist + 1.0f;
            SoundEvent soundEvent = null;
            blockState = player.getOnPos();
            block = level.getBlockState((BlockPos)blockState);
            BlockState blockState3 = level.getBlockState(blockState.above());
            Block block2 = block.getBlock();
            if (block2 instanceof IBlockStepSoundable) {
                IBlockStepSoundable iBlockStepSoundable = (IBlockStepSoundable)block2;
                soundEvent = iBlockStepSoundable.getStepSound();
            }
            if ((block2 = blockState3.getBlock()) instanceof IBlockStepSoundable) {
                IBlockStepSoundable iBlockStepSoundable = (IBlockStepSoundable)block2;
                soundEvent = iBlockStepSoundable.getStepSound();
            }
            if (soundEvent != null) {
                float f3 = player.isSprinting() ? 1.0f : 0.35f;
                player.playSound(soundEvent, f3, 1.0f);
            }
        }
        if (game != null) {
            MapEnvironment mapEnvironment = game.getMapEnvironment();
            field_160.removeIf(BF_180::method_742);
            if (mapEnvironment.getParticleEffects().contains((Object)WeatherEffectType.RAINSTORM) && !bl && random.nextFloat() <= 0.4f && player.getXRot() < -25.0f) {
                field_160.add(new BF_179(250.0f * random.nextFloat(), 250.0f * random.nextFloat()));
            }
        }
    }

    private void method_163(@NotNull SoundManager soundManager, @NotNull ItemStack itemStack) {
        Object object;
        DeferredHolder<SoundEvent, SoundEvent> deferredHolder;
        if (this.field_134 == null) {
            this.field_134 = new BF_398((SoundEvent)BFSounds.ENTITY_PLAYER_FOLEY_ARM.get());
        }
        if (!soundManager.isActive((SoundInstance)this.field_134)) {
            soundManager.play((SoundInstance)this.field_134);
        }
        if (this.field_133 == null) {
            this.field_133 = new GunHeatSound((SoundEvent)BFSounds.ITEM_GUN_SHARED_OVERHEAT.get(), SoundSource.PLAYERS);
        }
        if (!soundManager.isActive((SoundInstance)this.field_133)) {
            soundManager.play((SoundInstance)this.field_133);
        }
        if (!itemStack.isEmpty() && (deferredHolder = itemStack.getItem()) instanceof GunItem) {
            object = (GunItem)deferredHolder;
            this.field_135 = object;
        } else {
            this.field_135 = null;
        }
        if (this.field_135 == null) {
            if (this.field_132 != null && soundManager.isActive((SoundInstance)this.field_132)) {
                soundManager.stop((SoundInstance)this.field_132);
                this.field_132 = null;
            }
        } else {
            if (this.field_132 != null && this.field_132.method_1403() != this.field_135) {
                soundManager.stop((SoundInstance)this.field_132);
                this.field_132 = null;
            }
            if (!(this.field_132 != null && soundManager.isActive((SoundInstance)this.field_132) || (deferredHolder = ((GunSoundConfig)(object = this.field_135.getSoundConfig(itemStack))).getArm()) == null)) {
                this.field_132 = new BF_393((SoundEvent)deferredHolder.get(), this.field_135);
                soundManager.play((SoundInstance)this.field_132);
            }
        }
    }

    static {
        field_148 = 0.0f;
        field_150 = 0.0f;
        field_152 = 0.0f;
        field_6607 = 0.0f;
        field_136 = false;
        field_154 = 0.0f;
        inspectionBlur = 0.0f;
        field_127 = 0.0f;
        field_6418 = 0.0f;
        field_6605 = GunSpreadTarget.IDLE;
    }
}

