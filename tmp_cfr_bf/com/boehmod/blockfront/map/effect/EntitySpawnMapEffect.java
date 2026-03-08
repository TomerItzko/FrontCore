/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.map.effect;

import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.map.effect.PositionedMapEffect;
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
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Position;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Saddleable;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EntitySpawnMapEffect
extends PositionedMapEffect {
    private boolean field_6938 = false;
    @NotNull
    public Supplier<? extends EntityType<?>> field_6936;
    private final int field_6939;
    @Nullable
    private BlockPos field_6937;
    @Nullable
    private LeashFenceKnotEntity field_6935;

    public EntitySpawnMapEffect() {
        this(Vec3.ZERO, () -> EntityType.CHICKEN, 1);
    }

    public EntitySpawnMapEffect(@NotNull Vec3 vec3, @NotNull Supplier<? extends EntityType<?>> supplier, int n) {
        super(vec3);
        this.field_6936 = supplier;
        this.field_6939 = n;
    }

    @NotNull
    public EntitySpawnMapEffect method_5870(@Nullable BlockPos blockPos) {
        this.field_6937 = blockPos;
        return this;
    }

    private boolean method_5871(@NotNull ServerLevel serverLevel, @NotNull AbstractGame<?, ?, ?> abstractGame) {
        if (this.field_6936.get() == null) {
            return false;
        }
        EntityType<?> entityType = this.field_6936.get();
        BlockPos blockPos = BlockPos.containing((Position)this.position);
        if (this.field_6937 != null) {
            this.field_6935 = LeashFenceKnotEntity.getOrCreateKnot((Level)serverLevel, (BlockPos)this.field_6937);
            this.field_6935.getPersistentData().putUUID("matchId", abstractGame.getUUID());
        }
        for (int i = 0; i < this.field_6939; ++i) {
            Leashable leashable;
            Entity entity = entityType.spawn(serverLevel, null, null, blockPos, MobSpawnType.COMMAND, false, false);
            if (entity == null) continue;
            entity.setPos(this.position);
            CompoundTag compoundTag = entity.getPersistentData();
            compoundTag.putUUID("matchId", abstractGame.getUUID());
            compoundTag.putBoolean("disableMount", true);
            if (this.field_6935 != null && entity instanceof Leashable) {
                leashable = (Leashable)entity;
                leashable.setLeashedTo((Entity)this.field_6935, true);
                serverLevel.gameEvent((Holder)GameEvent.BLOCK_ATTACH, this.field_6937, GameEvent.Context.of((Entity)entity));
            }
            if (!(entity instanceof Saddleable)) continue;
            leashable = (Saddleable)entity;
            leashable.equipSaddle(new ItemStack((ItemLike)Items.SADDLE), null);
        }
        return true;
    }

    @Override
    public void updateGame(@NotNull ServerLevel level, @NotNull BFAbstractManager<?, ?, ?> manager, AbstractGame<?, ?, ?> game, @NotNull Random random, @NotNull Set<UUID> players) {
        if (!this.field_6938) {
            this.field_6938 = this.method_5871(level, game);
        }
    }

    @Override
    public void updateGameClient(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull Random random, @NotNull AbstractGame<?, ?, ?> game, @NotNull LocalPlayer player, @NotNull ClientLevel level, float delta) {
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
        this.field_6938 = false;
    }

    @Override
    @NotNull
    public RenderLevelStageEvent.Stage getRenderStage() {
        return RenderLevelStageEvent.Stage.AFTER_PARTICLES;
    }

    @Override
    public void writeToFDS(@NotNull FDSTagCompound fDSTagCompound) {
        super.writeToFDS(fDSTagCompound);
        fDSTagCompound.setString("entity", RegistryUtils.getEntityTypeId(this.field_6936));
        fDSTagCompound.setBoolean("hasLeash", this.field_6937 != null);
        if (this.field_6937 != null) {
            fDSTagCompound.setInteger("leashX", this.field_6937.getX());
            fDSTagCompound.setInteger("leashY", this.field_6937.getY());
            fDSTagCompound.setInteger("leashZ", this.field_6937.getZ());
        }
    }

    @Override
    public void readFromFDS(@NotNull FDSTagCompound fDSTagCompound) {
        super.readFromFDS(fDSTagCompound);
        this.field_6936 = RegistryUtils.retrieveEntityType(fDSTagCompound.getString("entity"));
        this.field_6937 = fDSTagCompound.getBoolean("hasLeash") ? new BlockPos(fDSTagCompound.getInteger("leashX"), fDSTagCompound.getInteger("leashY"), fDSTagCompound.getInteger("leashZ")) : null;
    }
}

