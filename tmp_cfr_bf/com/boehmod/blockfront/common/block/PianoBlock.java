/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.block;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.common.block.PropBlock;
import com.boehmod.blockfront.common.block.base.IShootAction;
import com.boehmod.blockfront.registry.BFSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class PianoBlock
extends PropBlock
implements IShootAction {
    public PianoBlock(@NotNull BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public void onBulletHit(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull Level level, int n, Vec3 vec3, @NotNull BlockPos blockPos, @NotNull BlockState blockState, Direction direction) {
        level.playLocalSound((double)((float)blockPos.getX() + 0.5f), (double)((float)blockPos.getY() + 0.5f), (double)((float)blockPos.getZ() + 0.5f), (SoundEvent)BFSounds.BLOCK_PIANO_HIT.get(), SoundSource.BLOCKS, 5.0f, 0.8f + 0.1f * (float)level.random.nextInt(4), false);
    }

    @Override
    public boolean showParticle() {
        return false;
    }
}

