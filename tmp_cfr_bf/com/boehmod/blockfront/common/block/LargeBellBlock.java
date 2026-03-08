/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.block;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.common.block.PropBlock;
import com.boehmod.blockfront.common.block.base.IShootAction;
import com.boehmod.blockfront.registry.BFSounds;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class LargeBellBlock
extends PropBlock
implements IShootAction {
    @NotNull
    private static final VoxelShape field_2101 = Block.box((double)2.0, (double)2.0, (double)2.0, (double)14.0, (double)15.0, (double)14.0);
    private static final int field_2102 = 15000;
    @NotNull
    private final Object2LongMap<BlockPos> field_2100 = new Object2LongOpenHashMap();

    public LargeBellBlock(@NotNull BlockBehaviour.Properties properties) {
        super(properties);
    }

    @NotNull
    public VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        return field_2101;
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public void onBulletHit(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull Level level, int n, Vec3 vec3, @NotNull BlockPos blockPos, @NotNull BlockState blockState, Direction direction) {
        long l;
        long l2 = System.currentTimeMillis();
        if (l2 - (l = this.field_2100.getOrDefault((Object)blockPos, 0L)) < 15000L) {
            return;
        }
        this.field_2100.put((Object)blockPos, l2);
        float f = 0.9f + 0.2f * level.random.nextFloat();
        level.playLocalSound(blockPos, (SoundEvent)BFSounds.BLOCK_BELL_BELL.get(), SoundSource.BLOCKS, 5.0f, f, false);
        level.playLocalSound(blockPos, (SoundEvent)BFSounds.BLOCK_BELL_BELL_FAR.get(), SoundSource.BLOCKS, 40.0f, f, false);
    }

    @Override
    public boolean showParticle() {
        return false;
    }
}

