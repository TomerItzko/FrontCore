/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.fds.tag.FDSTagCompound
 *  com.mojang.blaze3d.vertex.PoseStack
 *  javax.annotation.Nullable
 *  net.minecraft.client.Camera
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.renderer.MultiBufferSource$BufferSource
 *  net.minecraft.client.renderer.block.BlockRenderDispatcher
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.core.BlockPos
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.block.entity.BlockEntityType
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  net.neoforged.neoforge.client.event.RenderLevelStageEvent
 *  net.neoforged.neoforge.client.event.RenderLevelStageEvent$Stage
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.map.effect;

import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.block.entity.BFBlockEntity;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.map.effect.AbstractMapEffect;
import com.boehmod.blockfront.map.effect.data.AmbientVehiclePath;
import com.boehmod.blockfront.registry.BFBlockEntityTypes;
import com.boehmod.blockfront.registry.BFBlocks;
import com.boehmod.blockfront.unnamed.BF_397;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.RegistryUtils;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.jetbrains.annotations.NotNull;

public class AmbientVehicleMapEffect
extends AbstractMapEffect {
    public final AmbientVehiclePath path = new AmbientVehiclePath();
    public float field_3113;
    public float field_3117;
    public float field_3118 = 0.0f;
    public float field_3119;
    public float field_3120 = 0.0f;
    public float soundVolume = 1.5f;
    public boolean field_3114 = false;
    public boolean pauseWhenComplete = false;
    public boolean field_3116 = false;
    @Nullable
    public String animationOnFinish = null;
    @Nullable
    public Supplier<SoundEvent> sound = null;
    @Nullable
    public Supplier<SoundEvent> soundPaused = null;
    @Nullable
    public Supplier<SoundEvent> soundOnFinish = null;
    @Nullable
    private BF_397 field_3109 = null;
    @Nullable
    private BF_397 field_3111 = null;
    @Nullable
    private Supplier<? extends BlockEntityType<?>> entityType;
    @Nullable
    private Supplier<? extends Block> blockType;
    @Nullable
    private BlockEntity field_3110 = null;
    @Nullable
    private String animation = null;

    public AmbientVehicleMapEffect() {
        this((Supplier<? extends BlockEntityType<?>>)BFBlockEntityTypes.P51_MUSTANG, (Supplier<? extends Block>)BFBlocks.P51_MUSTANG, 0.0f);
    }

    @Override
    public void updateGame(@NotNull ServerLevel level, @NotNull BFAbstractManager<?, ?, ?> manager, AbstractGame<?, ?, ?> game, @NotNull Random random, @NotNull Set<UUID> players) {
    }

    public AmbientVehicleMapEffect(@NotNull Supplier<? extends BlockEntityType<?>> supplier, @NotNull Supplier<? extends Block> supplier2, float f) {
        this.entityType = supplier;
        this.blockType = supplier2;
        this.field_3113 = f;
    }

    public static double method_3097(double d, double d2, double d3) {
        return d + (d2 - d) * d3;
    }

    public AmbientVehicleMapEffect setAnimation(@NotNull String animation) {
        this.animation = animation;
        return this;
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public void updateGameClient(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull Random random, @NotNull AbstractGame<?, ?, ?> game, @NotNull LocalPlayer player, @NotNull ClientLevel level, float delta) {
        BlockEntity blockEntity;
        Object object;
        Object object2;
        Vec3 vec3;
        if (this.field_3110 == null && this.entityType != null && this.blockType != null) {
            this.field_3110 = this.entityType.get().create(BlockPos.ZERO, this.blockType.get().defaultBlockState());
            vec3 = this.field_3110;
            if (vec3 instanceof BFBlockEntity) {
                object2 = (BFBlockEntity)vec3;
                ((BFBlockEntity)((Object)object2)).method_1887();
                if (this.animation != null) {
                    ((BFBlockEntity)((Object)object2)).method_1888(this.animation);
                }
            }
        }
        object2 = this.path.method_3376();
        vec3 = this.path.field_3421;
        if (this.path.method_3375() == null || object2 == null) {
            return;
        }
        if (this.sound != null) {
            object = minecraft.getSoundManager();
            if (this.field_3109 == null || !object.isActive((SoundInstance)this.field_3109)) {
                this.field_3109 = new BF_397(this.path, this.sound.get(), vec3, false, this.soundVolume, 1.0f);
                object.play((SoundInstance)this.field_3109);
            }
        }
        if (this.soundPaused != null) {
            object = minecraft.getSoundManager();
            if (this.field_3111 == null || !object.isActive((SoundInstance)this.field_3111)) {
                this.field_3111 = new BF_397(this.path, this.soundPaused.get(), vec3, true, this.soundVolume, 1.0f);
                object.play((SoundInstance)this.field_3111);
            }
        }
        this.field_3118 = this.field_3117;
        this.field_3120 = this.field_3119;
        if (!this.path.method_3371()) {
            this.field_3114 = false;
            object = object2.subtract(vec3).normalize();
            float f = (float)Math.toDegrees(Math.asin(-((Vec3)object).y));
            float f2 = (float)Math.toDegrees(Math.atan2(((Vec3)object).x, ((Vec3)object).z));
            float f3 = MathUtils.wrapDegrees(f2 - this.field_3119);
            float f4 = MathUtils.wrapDegrees(f - this.field_3117);
            this.field_3117 += f4 * 0.1f;
            this.field_3119 += f3 * 0.1f;
        } else {
            this.field_3116 = true;
            if (!this.field_3114) {
                this.field_3114 = true;
                if (this.soundOnFinish != null) {
                    level.playLocalSound(vec3.x, vec3.y, vec3.z, this.soundOnFinish.get(), SoundSource.AMBIENT, 3.0f, 1.0f, false);
                }
            }
        }
        if (!this.pauseWhenComplete || !this.field_3116) {
            this.path.method_3373();
        }
        if (this.path.method_3378() && (blockEntity = this.field_3110) instanceof BFBlockEntity) {
            object = (BFBlockEntity)blockEntity;
            if (this.animationOnFinish != null) {
                ((BFBlockEntity)((Object)object)).queueAnimation(this.animationOnFinish);
            }
        }
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public void render(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull LocalPlayer player, @NotNull ClientLevel level, @NotNull BlockRenderDispatcher dispatcher, @NotNull MultiBufferSource.BufferSource buffer, @NotNull Random random, @NotNull RenderLevelStageEvent renderEvent, @NotNull GuiGraphics graphics, @NotNull PoseStack poseStack, @NotNull Camera camera, float renderTime, float delta) {
        if (this.field_3110 == null) {
            return;
        }
        Vec3 vec3 = this.path.method_3365(delta);
        float f = MathUtils.lerpf1(this.field_3117, this.field_3118, delta);
        float f2 = MathUtils.lerpf1(this.field_3119, this.field_3120, delta);
        BFRendering.blockEntity(minecraft, level, poseStack, this.field_3110, vec3.x, vec3.y, vec3.z, -f, f2, delta);
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public void renderDebug(@NotNull Minecraft minecraft, @NotNull RenderLevelStageEvent renderEvent, // Could not load outer class - annotation placement on inner may be incorrect
     @NotNull MultiBufferSource.BufferSource buffer, @NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull Font font, @NotNull Camera camera) {
        int n = 0;
        Vec3 vec3 = null;
        for (Vec3 vec32 : this.path.field_3419) {
            String string = "";
            int n2 = 65280;
            if (this.path.method_3376().equals((Object)vec32)) {
                string = "_current";
                n2 = 0xFF0000;
            }
            if (this.path.method_3375().equals((Object)vec32)) {
                string = "_previous";
            }
            if (vec3 != null) {
                BFRendering.billboardLine(camera, poseStack, vec3, vec32, 1.0f, n2);
            }
            BFRendering.billboardTexture(poseStack, camera, graphics, BFRes.loc("textures/misc/debug/vehicle_path" + string + ".png"), vec32, 64, false);
            String string2 = String.format("Vehicle Pos (%s/%s) %s", n, this.path.field_3419.size(), string);
            BFRendering.billboardComponent(poseStack, camera, font, graphics, (Component)Component.literal((String)string2), vec32.x, vec32.y + 1.0, vec32.z);
            ++n;
            vec3 = vec32;
        }
    }

    @Override
    public boolean requiresFancyGraphics() {
        return false;
    }

    @Override
    public void reset() {
    }

    @Override
    public // Could not load outer class - annotation placement on inner may be incorrect
    @NotNull RenderLevelStageEvent.Stage getRenderStage() {
        return RenderLevelStageEvent.Stage.AFTER_PARTICLES;
    }

    @Override
    public void writeToFDS(@NotNull FDSTagCompound fDSTagCompound) {
        String string;
        if (this.entityType != null && (string = RegistryUtils.getBlockEntityTypeId(this.entityType)) != null) {
            fDSTagCompound.setString("entityType", string);
        }
        if (this.blockType != null) {
            fDSTagCompound.setString("blockType", RegistryUtils.getBlockId(this.blockType.get()));
        }
        this.path.writeToFDS(fDSTagCompound);
        fDSTagCompound.setBoolean("pauseWhenComplete", this.pauseWhenComplete);
        if (this.animationOnFinish != null) {
            fDSTagCompound.setString("animationOnFinish", this.animationOnFinish);
        }
        if (this.animation != null) {
            fDSTagCompound.setString("animation", this.animation);
        }
        if (this.sound != null && this.sound.get() != null && (string = RegistryUtils.getSoundEventId(this.sound.get())) != null) {
            fDSTagCompound.setString("sound", string);
        }
        if (this.soundPaused != null && this.soundPaused.get() != null && (string = RegistryUtils.getSoundEventId(this.soundPaused.get())) != null) {
            fDSTagCompound.setString("soundPaused", string);
        }
        if (this.soundOnFinish != null && this.soundOnFinish.get() != null && (string = RegistryUtils.getSoundEventId(this.soundOnFinish.get())) != null) {
            fDSTagCompound.setString("soundOnFinish", string);
        }
        fDSTagCompound.setFloat("soundVolume", this.soundVolume);
    }

    @Override
    public void readFromFDS(@NotNull FDSTagCompound fDSTagCompound) {
        String string;
        String string2;
        String string3;
        String string4;
        String string5 = fDSTagCompound.getString("entityType");
        if (string5 != null) {
            this.entityType = RegistryUtils.retrieveBlockEntity(string5);
        }
        if ((string4 = fDSTagCompound.getString("blockType")) != null) {
            this.blockType = RegistryUtils.retrieveBlock(string4);
        }
        this.path.readFromFDS(fDSTagCompound);
        this.pauseWhenComplete = fDSTagCompound.getBoolean("pauseWhenComplete");
        this.animationOnFinish = fDSTagCompound.getString("animationOnFinish");
        if (fDSTagCompound.hasTag("animation")) {
            this.animation = fDSTagCompound.getString("animation");
        }
        if ((string3 = fDSTagCompound.getString("sound")) != null) {
            this.sound = RegistryUtils.retrieveSoundEvent(string3);
        }
        if ((string2 = fDSTagCompound.getString("soundPaused")) != null) {
            this.soundPaused = RegistryUtils.retrieveSoundEvent(string2);
        }
        if ((string = fDSTagCompound.getString("soundOnFinish")) != null) {
            this.soundOnFinish = RegistryUtils.retrieveSoundEvent(string);
        }
        this.soundVolume = fDSTagCompound.getFloat("soundVolume");
    }
}

