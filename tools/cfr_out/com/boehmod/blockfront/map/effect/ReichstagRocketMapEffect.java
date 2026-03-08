/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.fds.tag.FDSTagCompound
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.Camera
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.renderer.MultiBufferSource$BufferSource
 *  net.minecraft.client.renderer.block.BlockRenderDispatcher
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Position
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  net.neoforged.neoforge.client.event.RenderLevelStageEvent
 *  net.neoforged.neoforge.client.event.RenderLevelStageEvent$Stage
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.boehmod.blockfront.map.effect;

import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.entity.RocketEntity;
import com.boehmod.blockfront.common.entity.base.IProducedProjectileEntity;
import com.boehmod.blockfront.common.gun.GunFireConfig;
import com.boehmod.blockfront.common.gun.GunTriggerSpawnType;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.map.effect.PositionedMapEffect;
import com.boehmod.blockfront.registry.BFItems;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.ClientUtils;
import com.boehmod.blockfront.util.RegistryUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ReichstagRocketMapEffect
extends PositionedMapEffect {
    private int interval;
    private float pitch;
    private float yaw;
    @Nullable
    private Supplier<SoundEvent> fireSound;
    private int fireRate;
    private int amount;
    private int field_3177 = 0;
    private float pitchWitch;
    private float yawWitch = 0.0f;
    @NotNull
    private ItemStack field_3170 = new ItemStack((ItemLike)BFItems.GUN_PANZERSCHRECK.get());
    private int field_3178 = 0;
    private int field_3179;

    public ReichstagRocketMapEffect() {
        this(Vec3.ZERO, 0, 0.0f, 0.0f, 0, 0, (Supplier<SoundEvent>)BFSounds.AMBIENT_WAR_ARTILLERY_KATYUSHA);
    }

    public ReichstagRocketMapEffect(@NotNull Vec3 vec3, int n, float f, float f2, int n2, int n3, @NotNull Supplier<SoundEvent> supplier) {
        super(vec3);
        this.interval = n;
        this.pitch = f;
        this.yaw = f2;
        this.fireRate = n2;
        this.amount = n3;
        this.field_3179 = n3;
        this.fireSound = supplier;
    }

    @NotNull
    public ReichstagRocketMapEffect method_3112(float f, float f2) {
        this.pitchWitch = f;
        this.yawWitch = f2;
        return this;
    }

    @NotNull
    public ReichstagRocketMapEffect method_3113(@NotNull ItemStack itemStack) {
        this.field_3170 = itemStack;
        return this;
    }

    @Override
    public void updateGame(@NotNull ServerLevel level, @NotNull BFAbstractManager<?, ?, ?> manager, AbstractGame<?, ?, ?> game, @NotNull Random random, @NotNull Set<UUID> players) {
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public void updateGameClient(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull Random random, @NotNull AbstractGame<?, ?, ?> game, @NotNull LocalPlayer player, @NotNull ClientLevel level, float delta) {
        if (this.field_3177++ >= this.interval) {
            if (this.field_3179 > 0) {
                if (this.field_3178++ >= this.fireRate) {
                    this.field_3178 = 0;
                    --this.field_3179;
                    BlockPos blockPos = BlockPos.containing((Position)this.position);
                    if (level.isLoaded(blockPos)) {
                        this.method_3114(minecraft, manager, level);
                    }
                }
            } else {
                this.field_3177 = 0;
                this.field_3179 = this.amount;
            }
        }
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public void render(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull LocalPlayer player, @NotNull ClientLevel level, @NotNull BlockRenderDispatcher dispatcher, @NotNull MultiBufferSource.BufferSource buffer, @NotNull Random random, @NotNull RenderLevelStageEvent renderEvent, @NotNull GuiGraphics graphics, @NotNull PoseStack poseStack, @NotNull Camera camera, float renderTime, float delta) {
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public void renderDebug(@NotNull Minecraft minecraft, @NotNull RenderLevelStageEvent renderEvent, // Could not load outer class - annotation placement on inner may be incorrect
     @NotNull MultiBufferSource.BufferSource buffer, @NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull Font font, @NotNull Camera camera) {
        BFRendering.billboardTexture(poseStack, camera, graphics, BFRes.loc("textures/misc/debug/rocket.png"), this.position, 64, false);
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
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
        super.writeToFDS(fDSTagCompound);
        fDSTagCompound.setInteger("interval", this.interval);
        fDSTagCompound.setFloat("pitch", this.pitch);
        fDSTagCompound.setFloat("yaw", this.yaw);
        fDSTagCompound.setInteger("fireRate", this.fireRate);
        fDSTagCompound.setInteger("amount", this.amount);
        if (this.fireSound != null && (string = RegistryUtils.getSoundEventId(this.fireSound.get())) != null) {
            fDSTagCompound.setString("fireSound", string);
        }
        fDSTagCompound.setFloat("pitchWidth", this.pitchWitch);
        fDSTagCompound.setFloat("yawWidth", this.yawWitch);
    }

    @Override
    public void readFromFDS(@NotNull FDSTagCompound fDSTagCompound) {
        super.readFromFDS(fDSTagCompound);
        this.interval = fDSTagCompound.getInteger("interval");
        this.pitch = fDSTagCompound.getFloat("pitch");
        this.yaw = fDSTagCompound.getFloat("yaw");
        this.fireRate = fDSTagCompound.getInteger("fireRate");
        this.amount = fDSTagCompound.getInteger("amount");
        String string = fDSTagCompound.getString("fireSound");
        if (string != null) {
            this.fireSound = RegistryUtils.retrieveSoundEvent(string);
        }
        this.pitchWitch = fDSTagCompound.getFloat("pitchWidth");
        this.yawWitch = fDSTagCompound.getFloat("yawWidth");
    }

    @OnlyIn(value=Dist.CLIENT)
    public void method_3114(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientLevel clientLevel) {
        GunItem gunItem;
        Object object;
        float f;
        if (this.fireSound != null) {
            f = (float)((double)0.9f + (double)0.2f * Math.random());
            clientLevel.playLocalSound(this.position.x, this.position.y, this.position.z, this.fireSound.get(), SoundSource.AMBIENT, 100.0f, f, false);
        }
        f = (float)((double)this.pitchWitch * Math.random());
        float f2 = (float)((double)this.yawWitch * Math.random());
        float f3 = this.pitch + (Math.random() < 0.5 ? f : -f);
        float f4 = this.yaw + (Math.random() < 0.5 ? f2 : -f2);
        if (!this.field_3170.isEmpty() && (object = this.field_3170.getItem()) instanceof GunItem && ((GunFireConfig)(object = (gunItem = (GunItem)object).getDefaultFireConfig())).method_4023() == GunTriggerSpawnType.ENTITY) {
            ClientUtils.spawnParticle(minecraft, bFClientManager, clientLevel, ParticleTypes.FLASH, this.position.x + (double)0.1f, this.position.y, this.position.z + (double)0.1f, 0.0, 0.0, 0.0);
            Supplier<EntityType<? extends IProducedProjectileEntity>> supplier = ((GunFireConfig)object).method_4024();
            if (supplier != null) {
                Entity entity = supplier.get().create((Level)clientLevel);
                if (entity == null) {
                    return;
                }
                if (entity instanceof RocketEntity) {
                    RocketEntity rocketEntity = (RocketEntity)entity;
                    rocketEntity.method_1956();
                }
                ((IProducedProjectileEntity)entity).method_1935(this.position, 6.0f, this.field_3170, f4, f3);
                entity.setPos(this.position);
                ClientUtils.spawnEntity(entity, clientLevel);
            }
        }
    }
}

