/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.map.effect;

import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.common.BFAbstractManager;
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

public class RocketMapEffect
extends PositionedMapEffect {
    private int field_3187;
    private float field_3183;
    private float field_3184;
    private int field_3188 = 0;
    private float field_3185;
    private float field_3186 = 0.0f;
    @NotNull
    private ItemStack field_3182 = new ItemStack((ItemLike)BFItems.GUN_PANZERSCHRECK.get());

    public RocketMapEffect() {
        this(Vec3.ZERO, 0, 0.0f, 0.0f);
    }

    public RocketMapEffect(@NotNull Vec3 vec3, int n, float f, float f2) {
        super(vec3);
        this.field_3187 = n;
        this.field_3183 = f;
        this.field_3184 = f2;
    }

    public RocketMapEffect method_3115(float f, float f2) {
        this.field_3185 = f;
        this.field_3186 = f2;
        return this;
    }

    public RocketMapEffect method_3116(@NotNull ItemStack itemStack) {
        this.field_3182 = itemStack;
        return this;
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public void updateGameClient(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull Random random, @NotNull AbstractGame<?, ?, ?> game, @NotNull LocalPlayer player, @NotNull ClientLevel level, float delta) {
        if (this.field_3188++ >= this.field_3187) {
            this.field_3188 = 0;
            BlockPos blockPos = BlockPos.containing((Position)this.position);
            if (level.isLoaded(blockPos)) {
                this.method_3117(minecraft, manager, level);
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
    public void updateGame(@NotNull ServerLevel level, @NotNull BFAbstractManager<?, ?, ?> manager, AbstractGame<?, ?, ?> game, @NotNull Random random, @NotNull Set<UUID> players) {
    }

    @OnlyIn(value=Dist.CLIENT)
    public void method_3117(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientLevel clientLevel) {
        Object object;
        float f = (float)((double)this.field_3185 * Math.random());
        float f2 = (float)((double)this.field_3186 * Math.random());
        float f3 = this.field_3183 + (Math.random() < 0.5 ? f : -f);
        float f4 = this.field_3184 + (Math.random() < 0.5 ? f2 : -f2);
        if (this.field_3182.isEmpty() || !((object = this.field_3182.getItem()) instanceof GunItem)) {
            return;
        }
        GunItem gunItem = (GunItem)object;
        object = gunItem.getDefaultFireConfig();
        if (((GunFireConfig)object).method_4023() == GunTriggerSpawnType.ENTITY) {
            Entity entity;
            ClientUtils.spawnParticle(minecraft, bFClientManager, clientLevel, ParticleTypes.FLASH, this.position.x + (double)0.1f, this.position.y, this.position.z + (double)0.1f, 0.0, 0.0, 0.0);
            Supplier<EntityType<? extends IProducedProjectileEntity>> supplier = ((GunFireConfig)object).method_4024();
            if (supplier != null && (entity = supplier.get().create((Level)clientLevel)) != null) {
                ((IProducedProjectileEntity)entity).method_1935(this.position, 2.0f, this.field_3182, f4, f3);
                entity.setPos(this.position);
                ClientUtils.spawnEntity(entity, clientLevel);
                clientLevel.playLocalSound(this.position.x, this.position.y, this.position.z, (SoundEvent)BFSounds.ITEM_GUN_SHARED_ECHO_DISTANT_ROCKET.get(), SoundSource.AMBIENT, 10.0f, 1.0f, false);
            }
        }
    }

    @Override
    public // Could not load outer class - annotation placement on inner may be incorrect
    @NotNull RenderLevelStageEvent.Stage getRenderStage() {
        return RenderLevelStageEvent.Stage.AFTER_PARTICLES;
    }

    @Override
    public void writeToFDS(@NotNull FDSTagCompound fDSTagCompound) {
        super.writeToFDS(fDSTagCompound);
        fDSTagCompound.setInteger("interval", this.field_3187);
        fDSTagCompound.setFloat("pitch", this.field_3183);
        fDSTagCompound.setFloat("yaw", this.field_3184);
        fDSTagCompound.setFloat("pitchWidth", this.field_3185);
        fDSTagCompound.setFloat("yawWidth", this.field_3186);
    }

    @Override
    public void readFromFDS(@NotNull FDSTagCompound fDSTagCompound) {
        super.readFromFDS(fDSTagCompound);
        this.field_3187 = fDSTagCompound.getInteger("interval");
        this.field_3183 = fDSTagCompound.getFloat("pitch");
        this.field_3184 = fDSTagCompound.getFloat("yaw");
        this.field_3185 = fDSTagCompound.getFloat("pitchWidth");
        this.field_3186 = fDSTagCompound.getFloat("yawWidth");
    }
}

