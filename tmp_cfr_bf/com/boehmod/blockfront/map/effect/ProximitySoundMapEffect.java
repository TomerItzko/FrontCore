/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.map.effect;

import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.map.effect.PositionedMapEffect;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.unnamed.BF_1171;
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
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ProximitySoundMapEffect
extends PositionedMapEffect {
    @Nullable
    public Supplier<SoundEvent> field_6716;
    private float field_6717 = 1.0f;
    private float field_6718 = 1.0f;
    private float field_6719 = 16.0f;
    @OnlyIn(value=Dist.CLIENT)
    @Nullable
    private BF_1171 field_6715;

    public ProximitySoundMapEffect() {
        this((Supplier<SoundEvent>)BFSounds.AMBIENT_LSP_ALARM_0, Vec3.ZERO);
    }

    public ProximitySoundMapEffect(@NotNull Supplier<SoundEvent> supplier, @NotNull Vec3 vec3) {
        super(vec3);
        this.field_6716 = supplier;
    }

    @NotNull
    public ProximitySoundMapEffect method_5654(float f) {
        this.field_6718 = f;
        return this;
    }

    @NotNull
    public ProximitySoundMapEffect method_5655(float f) {
        this.field_6717 = f;
        return this;
    }

    @NotNull
    public ProximitySoundMapEffect method_5656(float f) {
        this.field_6719 = f;
        return this;
    }

    @Override
    public void updateGame(@NotNull ServerLevel level, @NotNull BFAbstractManager<?, ?, ?> manager, AbstractGame<?, ?, ?> game, @NotNull Random random, @NotNull Set<UUID> players) {
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public void updateGameClient(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull Random random, @NotNull AbstractGame<?, ?, ?> game, @NotNull LocalPlayer player, @NotNull ClientLevel level, float delta) {
        boolean bl;
        SoundManager soundManager = minecraft.getSoundManager();
        boolean bl2 = bl = Mth.sqrt((float)((float)player.distanceToSqr(this.position))) <= this.field_6719;
        if (!bl) {
            if (this.field_6715 != null && soundManager.isActive((SoundInstance)this.field_6715)) {
                soundManager.stop((SoundInstance)this.field_6715);
                this.field_6715 = null;
            }
            return;
        }
        if (this.field_6715 == null && this.field_6716 != null) {
            this.field_6715 = new BF_1171(this.field_6716.get(), this.position, this.field_6717, this.field_6718, game.getUUID());
        }
        if (this.field_6715 != null && !soundManager.isActive((SoundInstance)this.field_6715)) {
            soundManager.play((SoundInstance)this.field_6715);
        }
    }

    @Override
    public void render(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull LocalPlayer player, @NotNull ClientLevel level, @NotNull BlockRenderDispatcher dispatcher, // Could not load outer class - annotation placement on inner may be incorrect
     @NotNull MultiBufferSource.BufferSource buffer, @NotNull Random random, @NotNull RenderLevelStageEvent renderEvent, @NotNull GuiGraphics graphics, @NotNull PoseStack poseStack, @NotNull Camera camera, float renderTime, float delta) {
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
    @NotNull
    public RenderLevelStageEvent.Stage getRenderStage() {
        return RenderLevelStageEvent.Stage.AFTER_PARTICLES;
    }

    @Override
    public void writeToFDS(@NotNull FDSTagCompound fDSTagCompound) {
        String string;
        super.writeToFDS(fDSTagCompound);
        if (this.field_6716 != null && (string = RegistryUtils.getSoundEventId(this.field_6716.get())) != null) {
            fDSTagCompound.setString("sound", string);
        }
        fDSTagCompound.setFloat("volume", this.field_6718);
        fDSTagCompound.setFloat("pitch", this.field_6717);
        fDSTagCompound.setFloat("activationDistance", this.field_6719);
    }

    @Override
    public void readFromFDS(@NotNull FDSTagCompound fDSTagCompound) {
        super.readFromFDS(fDSTagCompound);
        String string = fDSTagCompound.getString("sound");
        if (string != null) {
            this.field_6716 = RegistryUtils.retrieveSoundEvent(string);
        }
        this.field_6718 = fDSTagCompound.getFloat("volume");
        this.field_6717 = fDSTagCompound.getFloat("pitch");
        this.field_6719 = fDSTagCompound.getFloat("activationDistance");
    }
}

