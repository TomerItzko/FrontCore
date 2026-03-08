/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.gun.bullet;

import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.gun.GunDamageConfig;
import com.boehmod.blockfront.common.gun.bullet.BlockBulletCollision;
import com.boehmod.blockfront.common.gun.bullet.EntityBulletHit;
import com.boehmod.blockfront.common.gun.bullet.EntityCollisionEntry;
import com.boehmod.blockfront.common.gun.bullet.LivingBulletCollision;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.registry.BFBlockAttributes;
import com.boehmod.blockfront.registry.custom.BlockAttribute;
import com.boehmod.blockfront.registry.custom.BlockTraversableAttribute;
import com.boehmod.blockfront.unnamed.BF_1194;
import com.boehmod.blockfront.unnamed.BF_905;
import com.boehmod.blockfront.util.BFLog;
import com.boehmod.blockfront.util.debug.DebugBox;
import com.boehmod.blockfront.util.debug.DebugLine;
import com.boehmod.blockfront.util.math.MathUtils;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractBulletCollision<L extends Level, E extends LivingEntity, H extends PlayerDataHandler<?>, M extends BFAbstractManager<H, ?, ?>> {
    public static final int field_4114 = 200;
    private static final int field_4115 = 8;
    @NotNull
    protected static final Vec3 field_4104 = new Vec3(1.0, (double)-0.1f, (double)0.3f);
    @NotNull
    protected static final Vec3 field_4106 = new Vec3(0.0, -0.25, 0.0);
    @NotNull
    protected final List<BlockBulletCollision> field_4109 = new ObjectArrayList();
    @NotNull
    protected final List<Integer> field_4110 = new IntArrayList();
    @NotNull
    protected final List<LivingBulletCollision> field_4111 = new ObjectArrayList();
    @NotNull
    protected final GunDamageConfig field_4108;
    protected float field_4113 = 0.0f;
    @Nullable
    protected Vec3 field_4107 = null;
    protected double field_4105 = 0.0;
    protected final H field_4112;

    public AbstractBulletCollision(@NotNull H h, @NotNull GunDamageConfig gunDamageConfig) {
        this.field_4112 = h;
        this.field_4108 = gunDamageConfig;
    }

    public void method_5790(@NotNull M m, @NotNull L l, @NotNull RandomSource randomSource, @NotNull Random random, @NotNull E e, @NotNull GunItem gunItem, long l2, long l3) {
        Vec3 vec3 = this.method_4269(e, randomSource);
        Vec3 vec32 = this.method_5791(e);
        Vec3 vec33 = vec32.add(vec3.scale((double)gunItem.getMaxHitDist()));
        ClipContext clipContext = new ClipContext(vec32, vec33, ClipContext.Block.COLLIDER, ClipContext.Fluid.WATER, e);
        this.method_4271(m, l, e, clipContext, vec3);
        this.method_4263(m, l, e, this.field_4109);
        this.method_4264(m, l, e, this.field_4111, randomSource, random, l2, l3);
        if (this.field_4107 != null) {
            Vec3 vec34 = this.method_4266(e);
            Vec3 vec35 = e.getEyePosition().add(vec34);
            this.method_4268(e, gunItem, randomSource, vec35, this.field_4107);
        }
    }

    @NotNull
    protected Vec3 method_5791(@NotNull E e) {
        return e.getEyePosition();
    }

    @NotNull
    protected abstract Vec3 method_4266(@NotNull E var1);

    protected abstract void method_4268(@NotNull E var1, @NotNull GunItem var2, @NotNull RandomSource var3, @NotNull Vec3 var4, @NotNull Vec3 var5);

    protected abstract void method_4264(@NotNull M var1, @NotNull L var2, @NotNull E var3, List<LivingBulletCollision> var4, RandomSource var5, @NotNull Random var6, long var7, long var9);

    protected abstract void method_4263(@NotNull M var1, @NotNull L var2, @NotNull E var3, @NotNull List<BlockBulletCollision> var4);

    protected abstract Vec3 method_4269(@NotNull E var1, @NotNull RandomSource var2);

    private void method_4271(@NotNull M m, @NotNull L l, @NotNull E e, @NotNull ClipContext clipContext2, @NotNull Vec3 vec3) {
        Vec3 vec32 = clipContext2.getTo();
        Vec3 vec33 = clipContext2.getFrom();
        double d = vec32.distanceTo(vec33);
        BlockGetter.traverseBlocks((Vec3)vec33, (Vec3)vec32, (Object)clipContext2, (clipContext, blockPos) -> this.method_4273(m, l, e, vec3, vec33, vec32, d), AbstractBulletCollision::method_4280);
    }

    @NotNull
    private static BlockHitResult method_4280(@NotNull ClipContext clipContext) {
        Vec3 vec3 = clipContext.getTo();
        Vec3 vec32 = clipContext.getFrom().subtract(vec3);
        BlockPos blockPos = BlockPos.containing((Position)vec3);
        return BlockHitResult.miss((Vec3)vec3, (Direction)Direction.getNearest((double)vec32.x, (double)vec32.y, (double)vec32.z), (BlockPos)blockPos);
    }

    @Nullable
    private BlockHitResult method_4273(@NotNull M m, @NotNull L l, @NotNull E e, @NotNull Vec3 vec3, @NotNull Vec3 vec32, @NotNull Vec3 vec33, double d) {
        BlockAttribute blockAttribute;
        DeferredHolder<BlockTraversableAttribute, ? extends BlockTraversableAttribute> deferredHolder;
        Vec3 vec34 = vec33.subtract(vec32).normalize();
        Vec3 vec35 = vec32.add(vec34.scale(this.field_4105));
        ClipContext clipContext = new ClipContext(vec35, vec33, ClipContext.Block.COLLIDER, ClipContext.Fluid.WATER, CollisionContext.empty());
        BlockHitResult blockHitResult = BF_905.method_3755(l, clipContext);
        BlockPos blockPos = blockHitResult.getBlockPos();
        BlockState blockState = l.getBlockState(blockPos);
        Vec3 vec36 = blockHitResult.getLocation();
        Direction direction = blockHitResult.getDirection();
        float f = (float)vec36.distanceTo(e.position());
        float f2 = this.field_4108.method_3950(f).leftFloat();
        this.field_4107 = vec36;
        int n = MathUtils.lerpColor(65280, 0xFF0000, Mth.clamp((float)(this.field_4113 / f2), (float)0.0f, (float)1.0f));
        this.method_4261(new DebugLine(vec35, vec36, 200, true, n));
        this.method_4270(m, l, e, vec3, vec35, vec36, d);
        if (this.field_4105 < d && this.field_4113 < f2 && this.field_4109.size() < 8 && (deferredHolder = (blockAttribute = BFBlockAttributes.method_4614(l, blockState, blockPos)).method_4245()) != null) {
            BlockTraversableAttribute blockTraversableAttribute = (BlockTraversableAttribute)deferredHolder.get();
            if (!blockState.isAir() && blockTraversableAttribute.reduction() != -1.0f) {
                this.method_4272(m, l, e, vec3, d, blockTraversableAttribute, vec36, direction, vec34, blockPos, vec35, blockState, blockHitResult);
                return null;
            }
        }
        this.method_4261(new DebugLine(vec35.subtract(0.0, 0.5, 0.0), vec35.add(0.0, 0.5, 0.0), 200, true, 0xFFFFFF));
        this.method_4261(new DebugLine(vec36.subtract(0.0, 0.5, 0.0), vec36.add(0.0, 0.5, 0.0), 200, true, 0xFFFFFF));
        this.method_4275(l, blockState, blockPos, direction, blockHitResult);
        return blockHitResult;
    }

    private void method_4272(@NotNull M m, @NotNull L l, @NotNull E e, @NotNull Vec3 vec3, double d, @NotNull BlockTraversableAttribute blockTraversableAttribute, @NotNull Vec3 vec32, @NotNull Direction direction, @NotNull Vec3 vec33, @NotNull BlockPos blockPos, @NotNull Vec3 vec34, @NotNull BlockState blockState, @NotNull BlockHitResult blockHitResult) {
        this.field_4113 += blockTraversableAttribute.reduction();
        this.method_4270(m, l, e, vec3, vec34, vec32, d);
        Vec3 vec35 = this.method_4276(l, vec32, vec33, blockState, blockPos);
        this.method_4261(new DebugLine(vec34.subtract(0.0, 0.5, 0.0), vec34.add(0.0, 0.5, 0.0), 200, true, 0xFFFFFF));
        this.method_4261(new DebugLine(vec35.subtract(0.0, 0.5, 0.0), vec35.add(0.0, 0.5, 0.0), 200, true, 0xFFFFFF));
        double d2 = vec34.distanceTo(vec32);
        double d3 = vec32.distanceTo(vec35);
        this.field_4105 += d2 + d3;
        this.method_4275(l, blockState, blockPos, direction, blockHitResult);
    }

    @NotNull
    private Vec3 method_4276(@NotNull L l, @NotNull Vec3 vec3, @NotNull Vec3 vec32, @NotNull BlockState blockState, @NotNull BlockPos blockPos) {
        VoxelShape voxelShape = blockState.getCollisionShape(l, blockPos);
        Vec3 vec33 = vec3.add(vec32.normalize().scale(0.001));
        Optional<Vec3> optional = this.method_4279(vec33, vec32, voxelShape, blockPos);
        return optional.orElse(vec3);
    }

    @NotNull
    private Optional<Vec3> method_4279(@NotNull Vec3 vec3, @NotNull Vec3 vec32, @NotNull VoxelShape voxelShape, @NotNull BlockPos blockPos) {
        double d = Double.POSITIVE_INFINITY;
        Vec3 vec33 = null;
        for (AABB aABB : voxelShape.toAabbs()) {
            Vec3 vec34;
            double d2;
            Optional<Vec3> optional = MathUtils.intersect(vec3, vec32, aABB.move(blockPos));
            if (!optional.isPresent() || !((d2 = vec3.distanceToSqr(vec34 = optional.get())) < d)) continue;
            d = d2;
            vec33 = vec34;
        }
        return Optional.ofNullable(vec33);
    }

    private void method_4275(@NotNull L l, @NotNull BlockState blockState, @NotNull BlockPos blockPos, @NotNull Direction direction, @NotNull BlockHitResult blockHitResult) {
        boolean bl;
        if (blockState.isAir()) {
            return;
        }
        BlockPos blockPos2 = blockPos.relative(direction, 1);
        BlockState blockState2 = l.getBlockState(blockPos2);
        VoxelShape voxelShape = blockState2.getCollisionShape(l, blockPos);
        boolean bl2 = bl = voxelShape.isEmpty() || voxelShape != Shapes.block();
        if (blockState2.isAir() || blockState2.liquid() || bl) {
            this.field_4109.add(new BlockBulletCollision(blockPos, direction, blockHitResult.getLocation()));
        }
    }

    private void method_4270(@NotNull M m, @NotNull L l, @NotNull E e, @NotNull Vec3 vec3, @NotNull Vec3 vec32, @NotNull Vec3 vec33, double d) {
        AABB aABB = e.getBoundingBox().expandTowards(vec3.scale(d));
        this.method_4261(new DebugLine(vec32.add(0.0, (double)0.1f, 0.0), vec33.add(0.0, (double)0.1f, 0.0), 200, true, 0x2222FF));
        ObjectArrayList objectArrayList = new ObjectArrayList();
        try {
            BF_1194 bF_1194 = this.method_5789(m, l);
            Object object = bF_1194.method_5833((BFAbstractManager<?, ?, ?>)m, aABB, (LivingEntity)e);
            Iterator<EntityCollisionEntry> iterator = object.iterator();
            while (iterator.hasNext()) {
                Vec3 vec34;
                EntityCollisionEntry entityCollisionEntry = iterator.next();
                if (!entityCollisionEntry.isValid() || (vec34 = (Vec3)entityCollisionEntry.boundingBox().clip(vec32, vec33).orElse(null)) == null) continue;
                double d2 = vec32.distanceToSqr(vec34);
                objectArrayList.add(new EntityBulletHit(entityCollisionEntry, vec34, d2));
            }
        }
        catch (ConcurrentModificationException concurrentModificationException) {
            BFLog.log("Concurrent modification exception while processing potential entity hits", new Object[0]);
        }
        objectArrayList.sort(Comparator.comparingDouble(EntityBulletHit::distanceSqr));
        for (Object object : objectArrayList) {
            this.method_5792(vec32, vec33, e, ((EntityBulletHit)object).entitySnapshot());
        }
    }

    private void method_5792(@NotNull Vec3 vec3, @NotNull Vec3 vec32, @NotNull E e, @NotNull EntityCollisionEntry entityCollisionEntry) {
        this.method_5793(vec3, vec32, e, entityCollisionEntry);
    }

    private void method_5793(@NotNull Vec3 vec3, @NotNull Vec3 vec32, @NotNull E e, @NotNull EntityCollisionEntry entityCollisionEntry) {
        int n = entityCollisionEntry.entityId();
        Vec3 vec33 = entityCollisionEntry.boundingBox().clip(vec3, vec32).orElse(null);
        int n2 = 65535;
        this.method_5788(new DebugBox(entityCollisionEntry.boundingBox(), 200, true, 65535));
        ItemStack itemStack = e.getMainHandItem();
        boolean bl = false;
        if (itemStack.getItem() instanceof GunItem && e instanceof Player && GunItem.getScope(itemStack) && !GunItem.field_4019) {
            bl = true;
        }
        if (vec33 != null && !this.field_4110.contains(n)) {
            Object object;
            boolean bl2;
            AABB aABB = entityCollisionEntry.headshotRegion();
            AABB aABB2 = entityCollisionEntry.backpackRegion();
            boolean bl3 = aABB != null && aABB.contains(vec33);
            boolean bl4 = bl2 = aABB2 != null && aABB2.contains(vec33);
            if (bl3) {
                object = new DebugBox(aABB, 200, true, 0xFF0000);
                this.method_5788((DebugBox)object);
            }
            if (bl2) {
                object = new DebugBox(aABB2, 200, true, 65280);
                this.method_5788((DebugBox)object);
            }
            object = e.getDirection();
            LivingBulletCollision livingBulletCollision = new LivingBulletCollision(n, this.field_4113, bl3, !this.field_4111.isEmpty(), bl, bl2, (Direction)object, vec33);
            this.field_4110.add(n);
            this.field_4111.add(livingBulletCollision);
        }
    }

    protected abstract void method_4261(@NotNull DebugLine var1);

    protected abstract void method_5788(@NotNull DebugBox var1);

    @NotNull
    protected abstract BF_1194 method_5789(@NotNull M var1, @NotNull L var2);
}

