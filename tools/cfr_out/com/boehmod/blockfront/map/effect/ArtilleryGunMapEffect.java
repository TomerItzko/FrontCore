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
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.core.particles.ParticleTypes
 *  net.minecraft.core.particles.SimpleParticleType
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
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
import com.boehmod.blockfront.client.world.ShakeManager;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.block.entity.BFBlockEntity;
import com.boehmod.blockfront.common.entity.base.IProducedProjectileEntity;
import com.boehmod.blockfront.common.gun.GunFireConfig;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.map.effect.PositionedMapEffect;
import com.boehmod.blockfront.registry.BFBlockEntityTypes;
import com.boehmod.blockfront.registry.BFBlocks;
import com.boehmod.blockfront.registry.BFItems;
import com.boehmod.blockfront.registry.BFParticleTypes;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.ClientUtils;
import com.boehmod.blockfront.util.RegistryUtils;
import com.boehmod.blockfront.util.math.MathUtils;
import com.boehmod.blockfront.util.math.ShakeNodePresets;
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
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.jetbrains.annotations.NotNull;

public class ArtilleryGunMapEffect
extends PositionedMapEffect {
    private static final int field_3133 = 50;
    @Nullable
    public Supplier<SoundEvent> field_3140;
    @Nullable
    private BlockEntity field_3128 = null;
    @Nullable
    private Supplier<? extends BlockEntityType<?>> field_3138;
    @Nullable
    private Supplier<? extends Block> field_3139;
    @Nullable
    private String field_3127 = null;
    private int field_3134 = 0;
    private int field_3135 = 0;
    private int field_3136 = 0;
    private float field_3130;
    private float field_3132 = 0.0f;
    private float field_3129 = 0.0f;
    private boolean field_3131 = false;
    private int field_3137 = 30;

    public ArtilleryGunMapEffect() {
        this(Vec3.ZERO, (Supplier<? extends BlockEntityType<?>>)BFBlockEntityTypes._150MM_TBTSK_C36_GUN, (Supplier<? extends Block>)BFBlocks._150MM_TBTSK_C36_GUN);
    }

    @Override
    public void updateGame(@NotNull ServerLevel level, @NotNull BFAbstractManager<?, ?, ?> manager, AbstractGame<?, ?, ?> game, @NotNull Random random, @NotNull Set<UUID> players) {
    }

    public ArtilleryGunMapEffect(@NotNull Vec3 vec3, @NotNull Supplier<? extends BlockEntityType<?>> supplier, @NotNull Supplier<? extends Block> supplier2) {
        super(vec3);
        this.field_3138 = supplier;
        this.field_3139 = supplier2;
    }

    public ArtilleryGunMapEffect method_3099(@NotNull String string) {
        this.field_3127 = string;
        return this;
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public void updateGameClient(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull Random random, @NotNull AbstractGame<?, ?, ?> game, @NotNull LocalPlayer player, @NotNull ClientLevel level, float delta) {
        BFBlockEntity bFBlockEntity;
        BlockEntity blockEntity;
        this.field_3129 = 45.0f;
        if (this.field_3128 == null && this.field_3138 != null && this.field_3139 != null && (blockEntity = (this.field_3128 = this.field_3138.get().create(BlockPos.ZERO, this.field_3139.get().defaultBlockState()))) instanceof BFBlockEntity) {
            bFBlockEntity = (BFBlockEntity)blockEntity;
            bFBlockEntity.method_1887();
            if (this.field_3127 != null) {
                bFBlockEntity.method_1888(this.field_3127);
            }
        }
        this.field_3132 = this.field_3130;
        if (this.field_3130 == (float)this.field_3135) {
            if (this.field_3134-- <= 0) {
                this.field_3134 = (10 + random.nextInt(10)) * 20;
                this.field_3135 = random.nextInt(this.field_3137 * 2) - this.field_3137;
                blockEntity = this.field_3128;
                if (blockEntity instanceof BFBlockEntity) {
                    bFBlockEntity = (BFBlockEntity)blockEntity;
                    bFBlockEntity.queueAnimation("move");
                }
                level.playLocalSound(this.position.x, this.position.y, this.position.z, (SoundEvent)BFSounds.ENTITY_VEHICLE_FLAK88_TURRET_MOVE_START.get(), SoundSource.AMBIENT, 3.0f, 0.5f, false);
            }
        } else {
            this.field_3130 = MathUtils.moveTowards(this.field_3130, this.field_3135, 0.4f);
            if (this.field_3130 == (float)this.field_3135) {
                this.field_3136 = 40;
                this.field_3131 = false;
                blockEntity = this.field_3128;
                if (blockEntity instanceof BFBlockEntity) {
                    bFBlockEntity = (BFBlockEntity)blockEntity;
                    bFBlockEntity.queueAnimation("move");
                }
                level.playLocalSound(this.position.x, this.position.y, this.position.z, (SoundEvent)BFSounds.ENTITY_VEHICLE_FLAK88_TURRET_MOVE_STOP.get(), SoundSource.AMBIENT, 3.0f, 0.5f, false);
            }
        }
        if (this.field_3136 == 10 && this.field_3140 != null) {
            level.playLocalSound(this.position.x, this.position.y, this.position.z, this.field_3140.get(), SoundSource.AMBIENT, 3.0f, 0.9f + random.nextFloat(0.2f), false);
        }
        if (this.field_3136-- <= 0 && !this.field_3131) {
            this.field_3131 = true;
            this.method_3100(minecraft, manager, player, level);
        }
    }

    @OnlyIn(value=Dist.CLIENT)
    private void method_3100(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull LocalPlayer localPlayer, @NotNull ClientLevel clientLevel) {
        Entity entity;
        Vec3 vec3;
        int n;
        BlockEntity blockEntity = this.field_3128;
        if (blockEntity instanceof BFBlockEntity) {
            BFBlockEntity bFBlockEntity = (BFBlockEntity)blockEntity;
            bFBlockEntity.queueAnimation("fire");
        }
        float f = this.field_3129 + this.field_3130;
        blockEntity = new Vec3(5.0, 0.0, 0.0).yRot(f * ((float)Math.PI / 180) - 1.5707964f);
        Vec3 vec32 = this.position.add((Vec3)blockEntity);
        clientLevel.playLocalSound(vec32.x, vec32.y, vec32.z, (SoundEvent)BFSounds.AMBIENT_EXPLOSION_ARTILLERY_AMBIENCE.get(), SoundSource.AMBIENT, 6.0f, 0.3f, false);
        clientLevel.playLocalSound(vec32.x, vec32.y, vec32.z, (SoundEvent)BFSounds.AMBIENT_EXPLOSION_ARTILLERY_TBTSK_FIRE.get(), SoundSource.AMBIENT, 15.0f, 1.0f, false);
        clientLevel.playLocalSound(vec32.x, vec32.y, vec32.z, (SoundEvent)BFSounds.AMBIENT_EXPLOSION_ARTILLERY_TBTSK_FIRE_STEREO.get(), SoundSource.AMBIENT, 15.0f, 1.0f, false);
        for (n = 0; n < 2; ++n) {
            clientLevel.playLocalSound(vec32.x, vec32.y, vec32.z, (SoundEvent)BFSounds.ITEM_GUN_SHARED_LFE_RPG.get(), SoundSource.AMBIENT, 15.0f, 1.0f, false);
        }
        ClientUtils.spawnParticleCircle(minecraft, bFClientManager, clientLevel, (ParticleOptions)BFParticleTypes.POOF_PARTICLE.get(), vec32.add(0.0, (double)0.1f, 0.0), 1.0f, 5.0f, 40, 0.25f);
        ClientUtils.spawnParticleCircle(minecraft, bFClientManager, clientLevel, (ParticleOptions)ParticleTypes.CLOUD, vec32.add(0.0, (double)0.1f, 0.0), 1.0f, 5.0f, 60, 0.25f);
        ClientUtils.spawnParticleCircle(minecraft, bFClientManager, clientLevel, (ParticleOptions)BFParticleTypes.BULLET_IMPACT_SMOKE.get(), vec32.add(0.0, (double)0.1f, 0.0), 1.0f, 5.0f, 60, 0.5f);
        ShakeManager.applyShake(ShakeNodePresets.EXPLOSION, localPlayer, this.position, 80.0f);
        for (n = 0; n < 12; ++n) {
            vec3 = new Vec3((double)(8.4f + 0.85f * (float)n), 0.0, 0.0);
            vec3 = vec3.add(0.0, 2.0, 0.0).yRot(f * ((float)Math.PI / 180) - 1.5707964f);
            entity = this.position.add(vec3);
            ClientUtils.spawnParticle(minecraft, bFClientManager, clientLevel, (SimpleParticleType)BFParticleTypes.BULLET_IMPACT_SMOKE.get(), entity.x, entity.y, entity.z, 0.0, 0.0, 0.0);
        }
        GunFireConfig gunFireConfig = ((GunItem)BFItems.GUN_PANZERSCHRECK.get()).getDefaultFireConfig();
        vec3 = gunFireConfig.method_4024();
        if (vec3 != null && (entity = vec3.get().create((Level)clientLevel)) != null) {
            Vec3 vec33 = this.position.add(0.0, 2.0, 0.0);
            ((IProducedProjectileEntity)entity).method_1935(vec33, 2.0f, new ItemStack((ItemLike)BFItems.GUN_PANZERSCHRECK.get()), -f, 0.0f);
            entity.setPos(vec33);
            entity.setDeltaMovement(entity.getDeltaMovement().scale(4.0));
            ClientUtils.spawnEntity(entity, clientLevel);
            clientLevel.playLocalSound(this.position.x, this.position.y, this.position.z, (SoundEvent)BFSounds.ITEM_GUN_SHARED_ECHO_DISTANT_ROCKET.get(), SoundSource.AMBIENT, 10.0f, 1.0f, false);
        }
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public void render(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull LocalPlayer player, @NotNull ClientLevel level, @NotNull BlockRenderDispatcher dispatcher, @NotNull MultiBufferSource.BufferSource buffer, @NotNull Random random, @NotNull RenderLevelStageEvent renderEvent, @NotNull GuiGraphics graphics, @NotNull PoseStack poseStack, @NotNull Camera camera, float renderTime, float delta) {
        if (this.field_3128 == null) {
            return;
        }
        float f = this.field_3129 + MathUtils.lerpf1(this.field_3130, this.field_3132, delta);
        BFRendering.blockEntity(minecraft, level, poseStack, this.field_3128, this.position.x, this.position.y, this.position.z, 0.0f, f, delta);
    }

    @Override
    public void renderDebug(@NotNull Minecraft minecraft, @NotNull RenderLevelStageEvent renderEvent, // Could not load outer class - annotation placement on inner may be incorrect
     @NotNull MultiBufferSource.BufferSource buffer, @NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull Font font, @NotNull Camera camera) {
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
        return RenderLevelStageEvent.Stage.AFTER_ENTITIES;
    }

    @Override
    public void writeToFDS(@NotNull FDSTagCompound fDSTagCompound) {
        String string;
        super.writeToFDS(fDSTagCompound);
        fDSTagCompound.setFloat("rotation", this.field_3129);
        fDSTagCompound.setInteger("maxYaw", this.field_3137);
        if (this.field_3138 != null) {
            fDSTagCompound.setString("entityType", RegistryUtils.getBlockEntityTypeId(this.field_3138));
        }
        if (this.field_3139 != null) {
            fDSTagCompound.setString("blockType", RegistryUtils.getBlockId(this.field_3139.get()));
        }
        if (this.field_3127 != null) {
            fDSTagCompound.setString("animation", this.field_3127);
        }
        if (this.field_3140 != null && (string = RegistryUtils.getSoundEventId(this.field_3140.get())) != null) {
            fDSTagCompound.setString("commanderSound", string);
        }
    }

    @Override
    public void readFromFDS(@NotNull FDSTagCompound fDSTagCompound) {
        String string;
        String string2;
        String string3;
        super.readFromFDS(fDSTagCompound);
        this.field_3129 = fDSTagCompound.getFloat("rotation");
        this.field_3137 = fDSTagCompound.getInteger("maxYaw", 50);
        String string4 = fDSTagCompound.getString("entityType");
        if (string4 != null) {
            this.field_3138 = RegistryUtils.retrieveBlockEntity(string4);
        }
        if ((string3 = fDSTagCompound.getString("blockType")) != null) {
            this.field_3139 = RegistryUtils.retrieveBlock(string3);
        }
        if ((string2 = fDSTagCompound.getString("animation")) != null) {
            this.field_3127 = string2;
        }
        if ((string = fDSTagCompound.getString("commanderSound")) != null) {
            this.field_3140 = RegistryUtils.retrieveSoundEvent(string);
        }
    }
}

