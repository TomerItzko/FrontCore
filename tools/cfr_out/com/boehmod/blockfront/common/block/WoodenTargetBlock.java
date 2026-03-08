/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.block;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.common.block.HorizontalPropBlock;
import com.boehmod.blockfront.common.block.base.IShootAction;
import com.boehmod.blockfront.common.net.packet.BFHitTargetPacket;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.PacketUtils;
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

public class WoodenTargetBlock
extends HorizontalPropBlock
implements IShootAction {
    public WoodenTargetBlock(@NotNull BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public void onBulletHit(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull Level level, int n, Vec3 vec3, @NotNull BlockPos blockPos, @NotNull BlockState blockState, Direction direction) {
        float f = 1.0f + 0.05f * (float)level.random.nextInt(8);
        Vec3 vec32 = blockPos.getCenter();
        level.playLocalSound(vec32.x, vec32.y, vec32.z, (SoundEvent)BFSounds.BLOCK_TARGET_HIT.get(), SoundSource.BLOCKS, 5.0f, f, true);
        level.playLocalSound(vec32.x, vec32.y, vec32.z, (SoundEvent)BFSounds.BLOCK_TARGET_HIT_DISTANT.get(), SoundSource.BLOCKS, 12.0f, f, true);
        if (n == 0) {
            PacketUtils.sendToServer(new BFHitTargetPacket());
        }
    }

    @Override
    public boolean showParticle() {
        return false;
    }
}

