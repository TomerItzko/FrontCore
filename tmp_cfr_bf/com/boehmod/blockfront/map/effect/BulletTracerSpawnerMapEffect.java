/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.map.effect;

import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.event.BFClientTickSubscriber;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.gun.bullet.BulletTracer;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.map.effect.PositionedMapEffect;
import com.boehmod.blockfront.registry.BFItems;
import com.boehmod.blockfront.registry.BFParticleTypes;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.ClientUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.jetbrains.annotations.NotNull;

public class BulletTracerSpawnerMapEffect
extends PositionedMapEffect {
    @NotNull
    private static final ResourceLocation field_6933 = BFRes.loc("textures/misc/debug/bullet_tracer_start.png");
    @NotNull
    private static final ResourceLocation field_6934 = BFRes.loc("textures/misc/debug/bullet_tracer_end.png");
    private Vec3 field_3152;
    private float field_3154 = 0.08f;
    private int field_3155 = 0;
    private boolean field_3153 = false;
    @NotNull
    private Vec3 field_3151 = new Vec3(8.0, 8.0, 8.0);

    public BulletTracerSpawnerMapEffect() {
        this(Vec3.ZERO, Vec3.ZERO);
    }

    public BulletTracerSpawnerMapEffect(Vec3 vec3, Vec3 vec32) {
        super(vec3);
        this.field_3152 = vec32;
    }

    @NotNull
    public BulletTracerSpawnerMapEffect method_3105(@NotNull Vec3 vec3) {
        this.field_3151 = vec3;
        return this;
    }

    @Override
    public void updateGame(@NotNull ServerLevel level, @NotNull BFAbstractManager<?, ?, ?> manager, AbstractGame<?, ?, ?> game, @NotNull Random random, @NotNull Set<UUID> players) {
    }

    @Override
    public void updateGameClient(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull Random random, @NotNull AbstractGame<?, ?, ?> game, @NotNull LocalPlayer player, @NotNull ClientLevel level, float delta) {
        if (random.nextFloat() <= this.field_3154) {
            this.field_3155 = 1;
            ClientUtils.spawnParticle(minecraft, manager, level, (SimpleParticleType)BFParticleTypes.GUN_FLASH.get(), this.position.x, this.position.y, this.position.z, 0.0, 0.0, 0.0);
            int n = (int)((double)random.nextInt((int)(this.field_3151.x + this.field_3151.x)) + this.field_3151.x);
            int n2 = (int)((double)random.nextInt((int)(this.field_3151.y + this.field_3151.y)) + this.field_3151.y);
            int n3 = (int)((double)random.nextInt((int)(this.field_3151.z + this.field_3151.z)) + this.field_3151.z);
            int n4 = (int)((double)random.nextInt((int)(this.field_3151.x + this.field_3151.x)) + this.field_3151.x) - n;
            int n5 = (int)((double)random.nextInt((int)(this.field_3151.y + this.field_3151.y)) + this.field_3151.y) - n2;
            int n6 = (int)((double)random.nextInt((int)(this.field_3151.z + this.field_3151.z)) + this.field_3151.z) - n3;
            Vec3 vec3 = this.field_3152.add((double)n4, (double)n5, (double)n6);
            BulletTracer bulletTracer = new BulletTracer((Holder<Item>)BFItems.GUN_BROWNING30, this.position, vec3);
            BFClientTickSubscriber.BULLETS.add(bulletTracer);
            if (this.field_3153) {
                level.playLocalSound(this.position.x, this.position.y, this.position.z, (SoundEvent)BFSounds.ITEM_GUN_SHARED_COREBASS_RIFLE_DISTANT.get(), SoundSource.AMBIENT, 10.0f, 1.0f, false);
            }
        } else if (this.field_3155 > 0) {
            --this.field_3155;
        }
    }

    @Override
    public void render(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull LocalPlayer player, @NotNull ClientLevel level, @NotNull BlockRenderDispatcher dispatcher, @NotNull MultiBufferSource.BufferSource buffer, @NotNull Random random, @NotNull RenderLevelStageEvent renderEvent, @NotNull GuiGraphics graphics, @NotNull PoseStack poseStack, @NotNull Camera camera, float renderTime, float delta) {
        if (this.field_3155 > 0) {
            BFRendering.billboardTexture(poseStack, camera, graphics, BFRes.loc("textures/misc/muzzleflash/flash" + random.nextInt(3) + ".png"), this.position.x, this.position.y, this.position.z, 48);
        }
    }

    @Override
    public void renderDebug(@NotNull Minecraft minecraft, @NotNull RenderLevelStageEvent renderEvent, // Could not load outer class - annotation placement on inner may be incorrect
     @NotNull MultiBufferSource.BufferSource buffer, @NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull Font font, @NotNull Camera camera) {
        BFRendering.billboardTexture(poseStack, camera, graphics, field_6933, this.position, 64, false);
        BFRendering.billboardTexture(poseStack, camera, graphics, field_6934, this.field_3152, 64, false);
        BFRendering.billboardComponent(poseStack, camera, font, graphics, (Component)Component.literal((String)"Bullet Tracer Spawner"), this.position.x, this.position.y - (double)0.6f, this.position.z);
        String string = String.format("Chance: '%s'", String.valueOf(ChatFormatting.GRAY) + this.field_3154 + String.valueOf(ChatFormatting.RESET));
        BFRendering.billboardComponent(poseStack, camera, font, graphics, (Component)Component.literal((String)string), this.position.x, this.position.y - (double)1.1f, this.position.z);
        String string2 = String.format("X: '%s', Y: '%s', Z: '%s'", String.valueOf(ChatFormatting.GRAY) + this.position.x + String.valueOf(ChatFormatting.RESET), String.valueOf(ChatFormatting.GRAY) + this.position.y + String.valueOf(ChatFormatting.RESET), String.valueOf(ChatFormatting.GRAY) + this.position.z + String.valueOf(ChatFormatting.RESET));
        BFRendering.billboardComponent(poseStack, camera, font, graphics, (Component)Component.literal((String)string2), this.position.x, this.position.y - (double)1.6f, this.position.z);
        BFRendering.billboardLine(camera, poseStack, new Vec3((double)((float)this.position.x), (double)((float)this.position.y), (double)((float)this.position.z)), new Vec3((double)((float)this.field_3152.x), (double)((float)this.field_3152.y), (double)((float)this.field_3152.z)), 1.0f, 0xFFFFFF);
        Vec3 vec3 = this.field_3152;
        Vec3 vec32 = vec3.add(this.field_3151.x, 0.0, 0.0);
        Vec3 vec33 = vec3.add(-this.field_3151.x, 0.0, 0.0);
        Vec3 vec34 = vec3.add(0.0, this.field_3151.y, 0.0);
        Vec3 vec35 = vec3.add(0.0, -this.field_3151.y, 0.0);
        Vec3 vec36 = vec3.add(0.0, 0.0, this.field_3151.z);
        Vec3 vec37 = vec3.add(0.0, 0.0, -this.field_3151.z);
        BFRendering.billboardLine(camera, poseStack, this.position, vec32, 1.0f, 0xFFFFFF);
        BFRendering.billboardLine(camera, poseStack, this.position, vec33, 1.0f, 0xFFFFFF);
        BFRendering.billboardLine(camera, poseStack, this.position, vec34, 1.0f, 0xFFFFFF);
        BFRendering.billboardLine(camera, poseStack, this.position, vec35, 1.0f, 0xFFFFFF);
        BFRendering.billboardLine(camera, poseStack, this.position, vec36, 1.0f, 0xFFFFFF);
        BFRendering.billboardLine(camera, poseStack, this.position, vec37, 1.0f, 0xFFFFFF);
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
        super.writeToFDS(fDSTagCompound);
        fDSTagCompound.setDouble("endPosX", this.field_3152.x);
        fDSTagCompound.setDouble("endPosY", this.field_3152.y);
        fDSTagCompound.setDouble("endPosZ", this.field_3152.z);
        fDSTagCompound.setFloat("chance", this.field_3154);
        fDSTagCompound.setBoolean("playSound", this.field_3153);
        fDSTagCompound.setDouble("spreadX", this.field_3151.x);
        fDSTagCompound.setDouble("spreadY", this.field_3151.y);
        fDSTagCompound.setDouble("spreadZ", this.field_3151.z);
    }

    @Override
    public void readFromFDS(@NotNull FDSTagCompound fDSTagCompound) {
        super.readFromFDS(fDSTagCompound);
        this.field_3152 = new Vec3(fDSTagCompound.getDouble("endPosX"), fDSTagCompound.getDouble("endPosY"), fDSTagCompound.getDouble("endPosZ"));
        this.field_3154 = fDSTagCompound.getFloat("chance");
        this.field_3153 = fDSTagCompound.getBoolean("playSound");
        this.field_3151 = new Vec3(fDSTagCompound.getDouble("spreadX"), fDSTagCompound.getDouble("spreadY"), fDSTagCompound.getDouble("spreadZ"));
    }
}

