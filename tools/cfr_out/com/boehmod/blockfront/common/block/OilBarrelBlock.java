/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.Direction
 *  net.minecraft.core.Vec3i
 *  net.minecraft.world.level.BlockGetter
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.SimpleWaterloggedBlock
 *  net.minecraft.world.level.block.state.BlockBehaviour$Properties
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.Vec3
 *  net.minecraft.world.phys.shapes.CollisionContext
 *  net.minecraft.world.phys.shapes.VoxelShape
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.block;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.common.block.PropBlock;
import com.boehmod.blockfront.common.block.base.IShootAction;
import com.boehmod.blockfront.registry.BFParticleTypes;
import com.boehmod.blockfront.unnamed.BF_1162;
import com.boehmod.blockfront.unnamed.BF_1177;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public final class OilBarrelBlock
extends PropBlock
implements IShootAction,
SimpleWaterloggedBlock {
    @NotNull
    private static final VoxelShape field_6680 = Block.box((double)1.0, (double)0.0, (double)1.0, (double)15.0, (double)24.0, (double)15.0);

    public OilBarrelBlock(@NotNull BlockBehaviour.Properties properties) {
        super(properties);
    }

    @NotNull
    public VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        return field_6680;
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public void onBulletHit(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull Level level, int n, Vec3 vec3, @NotNull BlockPos blockPos, @NotNull BlockState blockState, Direction direction) {
        if (!Minecraft.useFancyGraphics()) {
            return;
        }
        BF_1177 bF_1177 = manager.method_5643();
        float f = 0.1f + (float)(Math.random() * (double)0.1f);
        Vec3i vec3i = direction.getNormal();
        Vec3 vec32 = new Vec3((double)((float)vec3i.getX() * f), (double)((float)vec3i.getY() * f), (double)((float)vec3i.getZ() * f));
        int n2 = (1 + (int)(Math.random() * 3.0)) * 20;
        BF_1162 bF_1162 = new BF_1162(vec3, BFParticleTypes.OIL_PARTICLE, n2).method_5620(vec32);
        bF_1177.method_5637(bF_1162);
    }

    @Override
    public boolean showParticle() {
        return true;
    }
}

