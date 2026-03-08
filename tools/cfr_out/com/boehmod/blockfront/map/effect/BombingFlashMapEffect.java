/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.fds.tag.FDSTagCompound
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Camera
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.renderer.MultiBufferSource$BufferSource
 *  net.minecraft.client.renderer.block.BlockRenderDispatcher
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.Mth
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.client.event.RenderLevelStageEvent
 *  net.neoforged.neoforge.client.event.RenderLevelStageEvent$Stage
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.map.effect;

import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.map.effect.PositionedMapEffect;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.RegistryUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.jetbrains.annotations.NotNull;

public class BombingFlashMapEffect
extends PositionedMapEffect {
    private static final ResourceLocation field_3145 = BFRes.loc("textures/misc/world/bombing.png");
    public static final float field_3146 = 8000.0f;
    public static final float field_3147 = 4000.0f;
    public static final int field_3150 = 0xFFFFFF;
    public static final float field_3148 = 1.0f;
    public float field_3144;
    public float field_3141 = 1.0f;
    public int field_3142 = 0xFFFFFF;
    public boolean field_3143 = false;
    private Supplier<SoundEvent> field_3149;

    public BombingFlashMapEffect() {
        this(Vec3.ZERO, 0.0f, (Supplier<SoundEvent>)BFSounds.AMBIENT_WAR_ARTILLERY_DISTANT);
    }

    public BombingFlashMapEffect(Vec3 vec3, float f, Supplier<SoundEvent> supplier) {
        super(vec3);
        this.field_3144 = f;
        this.field_3149 = supplier;
    }

    public BombingFlashMapEffect method_3103(Supplier<SoundEvent> supplier) {
        this.field_3149 = supplier;
        return this;
    }

    public BombingFlashMapEffect method_3101(float f) {
        this.field_3141 = f;
        return this;
    }

    public BombingFlashMapEffect method_3102(int n) {
        this.field_3142 = n;
        return this;
    }

    @Override
    public void updateGame(@NotNull ServerLevel level, @NotNull BFAbstractManager<?, ?, ?> manager, AbstractGame<?, ?, ?> game, @NotNull Random random, @NotNull Set<UUID> players) {
    }

    @Override
    public void updateGameClient(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull Random random, @NotNull AbstractGame<?, ?, ?> game, @NotNull LocalPlayer player, @NotNull ClientLevel level, float delta) {
    }

    @Override
    public void render(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull LocalPlayer player, @NotNull ClientLevel level, @NotNull BlockRenderDispatcher dispatcher, @NotNull MultiBufferSource.BufferSource buffer, @NotNull Random random, @NotNull RenderLevelStageEvent renderEvent, @NotNull GuiGraphics graphics, @NotNull PoseStack poseStack, @NotNull Camera camera, float renderTime, float delta) {
        renderTime = (float)((double)renderTime + this.position.x + this.position.y + this.position.z);
        float f = Mth.sin((float)(renderTime / 25.0f));
        float f2 = Mth.sin((float)(renderTime / 12.0f));
        float f3 = f * f2;
        float f4 = 8000.0f * this.field_3141;
        float f5 = 4000.0f * this.field_3141;
        BFRendering.tintedWorldTexture(poseStack, graphics, field_3145, this.position.x, this.position.y, this.position.z, f4, f5, f3, this.field_3142, true, 0.0f, this.field_3144);
        if ((double)f3 >= 0.2) {
            if (!this.field_3143) {
                this.field_3143 = true;
                float f6 = 0.8f + 0.1f * (float)random.nextInt(4);
                minecraft.submit(() -> level.playLocalSound(this.position.x, this.position.y, this.position.z, this.field_3149.get(), SoundSource.AMBIENT, 30.0f, f6, false));
            }
        } else {
            this.field_3143 = false;
        }
    }

    @Override
    public void renderDebug(@NotNull Minecraft minecraft, @NotNull RenderLevelStageEvent renderEvent, // Could not load outer class - annotation placement on inner may be incorrect
     @NotNull MultiBufferSource.BufferSource buffer, @NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull Font font, @NotNull Camera camera) {
        BFRendering.billboardComponent(poseStack, camera, font, graphics, (Component)Component.literal((String)"Bombing Flash Effect"), this.position.x, this.position.y - (double)0.6f, this.position.z);
        String string = String.format("X: '%s', Y: '%s', Z: '%s'", String.valueOf(ChatFormatting.GRAY) + this.position.x + String.valueOf(ChatFormatting.RESET), String.valueOf(ChatFormatting.GRAY) + this.position.y + String.valueOf(ChatFormatting.RESET), String.valueOf(ChatFormatting.GRAY) + this.position.z + String.valueOf(ChatFormatting.RESET));
        BFRendering.billboardComponent(poseStack, camera, font, graphics, (Component)Component.literal((String)string), this.position.x, this.position.y - (double)1.1f, this.position.z);
        String string2 = String.format("Sound: '%s'", String.valueOf(ChatFormatting.GRAY) + String.valueOf(BuiltInRegistries.SOUND_EVENT.getKey((Object)this.field_3149.get())) + String.valueOf(ChatFormatting.RESET));
        BFRendering.billboardComponent(poseStack, camera, font, graphics, (Component)Component.literal((String)string2), this.position.x, this.position.y - (double)1.6f, this.position.z);
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
        fDSTagCompound.setFloat("rotation", this.field_3144);
        fDSTagCompound.setInteger("color", this.field_3142);
        fDSTagCompound.setFloat("scale", this.field_3141);
        String string = RegistryUtils.getSoundEventId(this.field_3149.get());
        if (string != null) {
            fDSTagCompound.setString("sound", string);
        }
    }

    @Override
    public void readFromFDS(@NotNull FDSTagCompound fDSTagCompound) {
        super.readFromFDS(fDSTagCompound);
        this.field_3144 = fDSTagCompound.getFloat("rotation", 0.0f);
        this.field_3142 = fDSTagCompound.getInteger("color", 0xFFFFFF);
        this.field_3141 = fDSTagCompound.getFloat("scale", 1.0f);
        String string = fDSTagCompound.getString("sound");
        if (string != null) {
            this.field_3149 = RegistryUtils.retrieveSoundEvent(string);
        }
    }
}

