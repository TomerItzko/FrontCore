/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.map.effect;

import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.net.packet.BFWorldFlashPacket;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.map.effect.AbstractMapEffect;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.PacketUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.jetbrains.annotations.NotNull;

public class LightningMapEffect
extends AbstractMapEffect {
    private static final int field_3098 = 2400;
    @NotNull
    private Vec2 field_3100;
    @NotNull
    private Vec2 field_3101;
    private int field_3099 = (int)(2400.0 * Math.random());

    public LightningMapEffect(@NotNull Vec2 vec2, @NotNull Vec2 vec22) {
        this.field_3100 = vec2;
        this.field_3101 = vec22;
    }

    public LightningMapEffect() {
        this(Vec2.ZERO, Vec2.ZERO);
    }

    private void method_3085(@NotNull Random random, @NotNull ServerLevel serverLevel, @NotNull AbstractGame<?, ?, ?> abstractGame) {
        Vec2 vec2 = new Vec2(this.field_3100.x + random.nextFloat() * (this.field_3101.x - this.field_3100.x), this.field_3100.y + random.nextFloat() * (this.field_3101.y - this.field_3100.y));
        BlockPos blockPos = BFUtils.method_2949(serverLevel, new BlockPos((int)vec2.x, 0, (int)vec2.y));
        Vec3 vec3 = blockPos.getBottomCenter();
        BFUtils.spawnLightningBolt((Level)serverLevel, vec3);
        Vec3 vec32 = vec3.add(0.0, 5.0, 0.0);
        BFUtils.playSound((Level)serverLevel, vec32, (SoundEvent)BFSounds.AMBIENT_WEATHER_LIGHTNING_NOSE.get(), SoundSource.AMBIENT, 5.0f, 1.0f);
        BFUtils.playSound((Level)serverLevel, vec32, (SoundEvent)BFSounds.AMBIENT_WEATHER_LIGHTNING_STEREO.get(), SoundSource.AMBIENT, 5.0f, 1.0f);
        BFUtils.playSound((Level)serverLevel, vec32, (SoundEvent)BFSounds.AMBIENT_WEATHER_LIGHTNING.get(), SoundSource.AMBIENT, 5.0f, 1.0f);
        BFUtils.playSound((Level)serverLevel, vec32, (SoundEvent)BFSounds.AMBIENT_LSP_THUNDER.get(), SoundSource.AMBIENT, 5.0f, 1.0f);
        PacketUtils.sendToGamePlayers(new BFWorldFlashPacket(2), abstractGame);
    }

    @Override
    public void updateGame(@NotNull ServerLevel level, @NotNull BFAbstractManager<?, ?, ?> manager, @NotNull AbstractGame<?, ?, ?> game, @NotNull Random random, @NotNull Set<UUID> players) {
        if (this.field_3099-- <= 0) {
            this.field_3099 = (int)(2400.0 * Math.random());
            this.method_3085(random, level, game);
        }
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public void updateGameClient(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull Random random, @NotNull AbstractGame<?, ?, ?> game, @NotNull LocalPlayer player, @NotNull ClientLevel level, float delta) {
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public void render(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull LocalPlayer player, @NotNull ClientLevel level, @NotNull BlockRenderDispatcher dispatcher, @NotNull MultiBufferSource.BufferSource buffer, @NotNull Random random, @NotNull RenderLevelStageEvent renderEvent, @NotNull GuiGraphics graphics, @NotNull PoseStack poseStack, @NotNull Camera camera, float renderTime, float delta) {
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public void renderDebug(@NotNull Minecraft minecraft, @NotNull RenderLevelStageEvent renderEvent, // Could not load outer class - annotation placement on inner may be incorrect
    @NotNull MultiBufferSource.BufferSource buffer, @NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull Font font, @NotNull Camera camera) {
        DebugRenderer.renderFilledBox((PoseStack)poseStack, (MultiBufferSource)buffer, (double)this.field_3100.x, (double)255.0, (double)this.field_3100.y, (double)this.field_3101.x, (double)255.0, (double)this.field_3101.y, (float)1.0f, (float)0.0f, (float)0.0f, (float)1.0f);
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
        fDSTagCompound.setFloat("startX", this.field_3100.x);
        fDSTagCompound.setFloat("startY", this.field_3100.y);
        fDSTagCompound.setFloat("endX", this.field_3101.x);
        fDSTagCompound.setFloat("endY", this.field_3101.y);
    }

    @Override
    public void readFromFDS(@NotNull FDSTagCompound fDSTagCompound) {
        this.field_3100 = new Vec2(fDSTagCompound.getFloat("startX"), fDSTagCompound.getFloat("startY"));
        this.field_3101 = new Vec2(fDSTagCompound.getFloat("endX"), fDSTagCompound.getFloat("endY"));
    }
}

