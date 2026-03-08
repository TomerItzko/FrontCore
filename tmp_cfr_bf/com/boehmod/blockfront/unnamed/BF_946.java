/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.event.BFClientTickSubscriber;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.common.gun.GunDamageConfig;
import com.boehmod.blockfront.common.gun.GunSpreadConfig;
import com.boehmod.blockfront.common.gun.bullet.AbstractBulletCollision;
import com.boehmod.blockfront.common.gun.bullet.BlockBulletCollision;
import com.boehmod.blockfront.common.gun.bullet.BulletTracer;
import com.boehmod.blockfront.common.gun.bullet.LivingBulletCollision;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.net.packet.BFBulletCollisionBlockPacket;
import com.boehmod.blockfront.common.net.packet.BFBulletCollisionLivingPacket;
import com.boehmod.blockfront.unnamed.BF_1163;
import com.boehmod.blockfront.unnamed.BF_1191;
import com.boehmod.blockfront.unnamed.BF_1194;
import com.boehmod.blockfront.unnamed.BF_1196;
import com.boehmod.blockfront.util.BulletUtils;
import com.boehmod.blockfront.util.PacketUtils;
import com.boehmod.blockfront.util.debug.DebugBox;
import com.boehmod.blockfront.util.debug.DebugLine;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class BF_946
extends AbstractBulletCollision<ClientLevel, LocalPlayer, ClientPlayerDataHandler, BFClientManager> {
    @NotNull
    private final Minecraft field_6406;
    private static final int field_3837 = 8;
    private static final int field_3838 = 8;

    public BF_946(@NotNull Minecraft minecraft, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull GunDamageConfig gunDamageConfig) {
        super(clientPlayerDataHandler, gunDamageConfig);
        this.field_6406 = minecraft;
    }

    @Override
    public void method_4268(@NotNull LocalPlayer localPlayer, @NotNull GunItem gunItem, @NotNull RandomSource randomSource, @NotNull Vec3 vec3, @NotNull Vec3 vec32) {
        int n = gunItem.method_4183();
        if (n == -1 && randomSource.nextFloat() >= gunItem.method_4168()) {
            return;
        }
        BulletTracer bulletTracer = new BulletTracer((Holder<Item>)gunItem.builtInRegistryHolder(), vec3, vec32, n).method_1168();
        BFClientTickSubscriber.BULLETS.add(bulletTracer);
        BFClientTickSubscriber.DEBUG_LINES.add(new DebugLine(vec3.add(0.0, (double)0.1f, 0.0), vec32.add(0.0, (double)0.1f, 0.0), 200, true, -838669));
    }

    @Override
    public void method_4264(@NotNull BFClientManager bFClientManager, @NotNull ClientLevel clientLevel, @NotNull LocalPlayer localPlayer, @NotNull List<LivingBulletCollision> list, RandomSource randomSource, @NotNull Random random, long l, long l2) {
        Minecraft minecraft = Minecraft.getInstance();
        Vec2 vec2 = new Vec2(localPlayer.getXRot(), localPlayer.getYRot());
        Vec3 vec3 = this.method_5791(localPlayer);
        BFBulletCollisionLivingPacket bFBulletCollisionLivingPacket = new BFBulletCollisionLivingPacket(list, vec3, vec2, GunSpreadConfig.currentSpread, BF_1163.field_6611, l, l2);
        PacketUtils.sendToServer(bFBulletCollisionLivingPacket);
        int n = list.size();
        for (int i = 0; i < n; i += 8) {
            int n2 = Math.min(n, i + 8);
            List<LivingBulletCollision> list2 = list.subList(i, n2);
            for (LivingBulletCollision livingBulletCollision : list2) {
                Entity entity = clientLevel.getEntity(livingBulletCollision.entityId());
                if (!(entity instanceof LivingEntity)) continue;
                LivingEntity livingEntity = (LivingEntity)entity;
                BulletUtils.method_155(minecraft, bFClientManager, clientLevel, randomSource, random, (LivingEntity)localPlayer, livingEntity, livingBulletCollision.hitVec(), livingBulletCollision.direction(), livingBulletCollision.headShot());
            }
        }
    }

    @Override
    public void method_4263(@NotNull BFClientManager bFClientManager, @NotNull ClientLevel clientLevel, @NotNull LocalPlayer localPlayer, @NotNull List<BlockBulletCollision> list) {
        Minecraft minecraft = Minecraft.getInstance();
        RandomSource randomSource = clientLevel.getRandom();
        int n = list.size();
        for (int i = 0; i < n; i += 8) {
            int n2 = Math.min(n, i + 8);
            List<BlockBulletCollision> list2 = list.subList(i, n2);
            BFBulletCollisionBlockPacket bFBulletCollisionBlockPacket = new BFBulletCollisionBlockPacket(list2);
            PacketUtils.sendToServer(bFBulletCollisionBlockPacket);
            for (BlockBulletCollision blockBulletCollision : list2) {
                BulletUtils.doBlockBulletImpact(minecraft, bFClientManager, clientLevel, randomSource, localPlayer, (LivingEntity)localPlayer, blockBulletCollision, 0);
            }
        }
    }

    @Override
    public Vec3 method_4269(@NotNull LocalPlayer localPlayer, @NotNull RandomSource randomSource) {
        Vec2 vec2 = new Vec2(localPlayer.getXRot(), localPlayer.getYRot());
        return BF_1191.method_5802(vec2, randomSource, GunSpreadConfig.currentSpread, BF_1163.field_6611);
    }

    @Override
    @NotNull
    public Vec3 method_4266(@NotNull LocalPlayer localPlayer) {
        if (!GunItem.field_4019) {
            return field_4104.zRot((localPlayer.getXRot() + 90.0f - BF_1163.field_6611) * ((float)Math.PI / 180) - 1.5707964f).yRot(-localPlayer.getYRot() * ((float)Math.PI / 180) - 1.5707964f);
        }
        return field_4106;
    }

    @Override
    public void method_4261(@NotNull DebugLine debugLine) {
        if (!this.field_6406.getDebugOverlay().showDebugScreen()) {
            return;
        }
        BFClientTickSubscriber.DEBUG_LINES.add(debugLine);
    }

    @Override
    public void method_5788(@NotNull DebugBox debugBox) {
        if (!this.field_6406.getDebugOverlay().showDebugScreen()) {
            return;
        }
        BFClientTickSubscriber.DEBUG_BOXES.add(debugBox);
    }

    @Override
    @NotNull
    protected BF_1194 method_5789(@NotNull BFClientManager bFClientManager, @NotNull ClientLevel clientLevel) {
        return new BF_1196((Level)clientLevel, (int)clientLevel.getGameTime());
    }

    @Override
    @NotNull
    public /* synthetic */ Vec3 method_4266(@NotNull LivingEntity livingEntity) {
        return this.method_4266((LocalPlayer)livingEntity);
    }
}

