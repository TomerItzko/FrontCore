/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.ints.IntArrayList
 *  it.unimi.dsi.fastutil.ints.IntList
 *  net.minecraft.core.Holder
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec2
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.gun.GunDamageConfig;
import com.boehmod.blockfront.common.gun.bullet.AbstractBulletCollision;
import com.boehmod.blockfront.common.gun.bullet.BlockBulletCollision;
import com.boehmod.blockfront.common.gun.bullet.BulletTracer;
import com.boehmod.blockfront.common.gun.bullet.LivingBulletCollision;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.net.packet.BFBulletTracerToClientPacket;
import com.boehmod.blockfront.common.net.packet.BFDebugBoxPacket;
import com.boehmod.blockfront.common.net.packet.BFDebugLinePacket;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.server.BFServerManager;
import com.boehmod.blockfront.server.ac.bullet.ServerCollisionTracker;
import com.boehmod.blockfront.server.player.ServerPlayerDataHandler;
import com.boehmod.blockfront.unnamed.BF_1194;
import com.boehmod.blockfront.unnamed.BF_1195;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.PacketUtils;
import com.boehmod.blockfront.util.debug.DebugBox;
import com.boehmod.blockfront.util.debug.DebugLine;
import com.boehmod.blockfront.util.math.MathUtils;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class BF_1191
extends AbstractBulletCollision<ServerLevel, LivingEntity, ServerPlayerDataHandler, BFServerManager> {
    private static final int field_6846 = 10747841;
    @NotNull
    private final ServerCollisionTracker field_6841;
    private final long field_6847;
    private final int field_6848;
    private final float field_6843;
    private final float field_6844;
    @NotNull
    private final Vec3 field_6840;
    @NotNull
    private final Vec2 field_6845;
    @NotNull
    private final ServerPlayer field_6842;

    public BF_1191(@NotNull ServerPlayer serverPlayer, @NotNull ServerPlayerDataHandler serverPlayerDataHandler, @NotNull GunDamageConfig gunDamageConfig, @NotNull ServerCollisionTracker serverCollisionTracker, @NotNull Vec3 vec3, @NotNull Vec2 vec2, float f, float f2, long l, int n) {
        super(serverPlayerDataHandler, gunDamageConfig);
        this.field_6842 = serverPlayer;
        this.field_6841 = serverCollisionTracker;
        this.field_6840 = vec3;
        this.field_6845 = vec2;
        this.field_6847 = l;
        this.field_6848 = n;
        this.field_6843 = f;
        this.field_6844 = f2;
    }

    @NotNull
    public IntList method_5799(@NotNull BFServerManager bFServerManager, @NotNull ServerLevel serverLevel, @NotNull RandomSource randomSource, @NotNull Random random, @NotNull LivingEntity livingEntity, @NotNull GunItem gunItem, long l, long l2) {
        this.method_5803();
        this.method_5790(bFServerManager, serverLevel, randomSource, random, livingEntity, gunItem, l, l2);
        return this.method_5797();
    }

    @NotNull
    private IntList method_5797() {
        IntArrayList intArrayList = new IntArrayList();
        for (LivingBulletCollision livingBulletCollision : this.field_4111) {
            intArrayList.add(livingBulletCollision.entityId());
        }
        return intArrayList;
    }

    private void method_5803() {
        this.field_4111.clear();
        this.field_4110.clear();
        this.field_4109.clear();
        this.field_4113 = 0.0f;
        this.field_4107 = null;
        this.field_4105 = 0.0;
    }

    @Override
    public void method_4268(@NotNull LivingEntity livingEntity, @NotNull GunItem gunItem, @NotNull RandomSource randomSource, @NotNull Vec3 vec3, @NotNull Vec3 vec32) {
        block5: {
            int n = gunItem.method_4183();
            if (n == -1 && randomSource.nextFloat() >= gunItem.method_4168()) {
                return;
            }
            BulletTracer bulletTracer = new BulletTracer((Holder<Item>)gunItem.builtInRegistryHolder(), vec3, vec32, n);
            BFBulletTracerToClientPacket bFBulletTracerToClientPacket = new BFBulletTracerToClientPacket(this.field_6842.getId(), bulletTracer);
            BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
            if (bFAbstractManager == null) break block5;
            AbstractGame<?, ?, ?> abstractGame = bFAbstractManager.getGameWithPlayer((Player)this.field_6842);
            if (abstractGame != null) {
                for (UUID uUID : ((AbstractGamePlayerManager)abstractGame.getPlayerManager()).getPlayerUUIDs()) {
                    ServerPlayer serverPlayer = BFUtils.getPlayerByUUID(uUID);
                    if (serverPlayer == null || serverPlayer.equals((Object)this.field_6842)) continue;
                    PacketUtils.sendToPlayer(bFBulletTracerToClientPacket, serverPlayer);
                }
            } else {
                Level level = this.field_6842.level();
                for (Player player : level.players()) {
                    ServerPlayer serverPlayer;
                    if (!(player instanceof ServerPlayer) || (serverPlayer = (ServerPlayer)player).equals((Object)this.field_6842)) continue;
                    PacketUtils.sendToPlayer(bFBulletTracerToClientPacket, serverPlayer);
                }
            }
        }
    }

    @Override
    public void method_4264(@NotNull BFServerManager bFServerManager, @NotNull ServerLevel serverLevel, @NotNull LivingEntity livingEntity, @NotNull List<LivingBulletCollision> list, RandomSource randomSource, @NotNull Random random, long l, long l2) {
    }

    @Override
    public void method_4263(@NotNull BFServerManager bFServerManager, @NotNull ServerLevel serverLevel, @NotNull LivingEntity livingEntity, @NotNull List<BlockBulletCollision> list) {
    }

    @Override
    @NotNull
    protected Vec3 method_5791(@NotNull LivingEntity livingEntity) {
        return this.field_6840;
    }

    @Override
    public Vec3 method_4269(@NotNull LivingEntity livingEntity, @NotNull RandomSource randomSource) {
        return BF_1191.method_5802(this.field_6845, randomSource, this.field_6843, this.field_6844);
    }

    @Override
    @NotNull
    public Vec3 method_4266(@NotNull LivingEntity livingEntity) {
        return field_4104.zRot((livingEntity.getXRot() + 90.0f) * ((float)Math.PI / 180) - 1.5707964f).yRot(-livingEntity.getYRot() * ((float)Math.PI / 180) - 1.5707964f);
    }

    @Override
    public void method_4261(@NotNull DebugLine debugLine) {
        if (this.field_6842.isCreative()) {
            BFDebugLinePacket bFDebugLinePacket = new BFDebugLinePacket(debugLine.withColor(10747841));
            PacketUtils.sendToPlayer(bFDebugLinePacket, this.field_6842);
        }
    }

    @Override
    public void method_5788(@NotNull DebugBox debugBox) {
        if (this.field_6842.isCreative()) {
            BFDebugBoxPacket bFDebugBoxPacket = new BFDebugBoxPacket(debugBox.withColor(10747841));
            PacketUtils.sendToPlayer(bFDebugBoxPacket, this.field_6842);
        }
    }

    @Override
    @NotNull
    protected BF_1194 method_5789(@NotNull BFServerManager bFServerManager, @NotNull ServerLevel serverLevel) {
        return new BF_1195(this.field_6841, this.field_6847, this.field_6848);
    }

    @NotNull
    public static Vec3 method_5802(@NotNull Vec2 vec2, @NotNull RandomSource randomSource, float f, float f2) {
        float f3 = (float)((double)f * randomSource.nextGaussian());
        float f4 = (float)((double)f * randomSource.nextGaussian() - (double)f2);
        return MathUtils.method_5814(vec2, f4, f3);
    }
}

