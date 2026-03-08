/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.server.ac.bullet;

import com.boehmod.blockfront.cloud.BFFeatureFlags;
import com.boehmod.blockfront.common.gun.GunFireConfig;
import com.boehmod.blockfront.common.gun.GunScopeConfig;
import com.boehmod.blockfront.common.gun.GunSpreadConfig;
import com.boehmod.blockfront.common.gun.bullet.EntityCollisionEntry;
import com.boehmod.blockfront.common.gun.bullet.LivingBulletCollision;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.server.BFServerManager;
import com.boehmod.blockfront.server.ac.bullet.ServerCollisionTracker;
import com.boehmod.blockfront.server.player.BFServerPlayerData;
import com.boehmod.blockfront.server.player.ServerPlayerDataHandler;
import com.boehmod.blockfront.unnamed.BF_1191;
import com.boehmod.blockfront.unnamed.BF_1199;
import com.boehmod.blockfront.unnamed.BF_1204;
import com.boehmod.blockfront.unnamed.BF_1223;
import com.boehmod.blockfront.unnamed.BF_948;
import com.boehmod.blockfront.util.BFUtils;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Position;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class ShotValidationManager {
    public static final double field_6862 = 2.0;
    @NotNull
    private final ServerCollisionTracker field_6863;
    private final int field_6864;
    private static final int field_6865 = 3;
    private static final long field_7093 = 20L;
    @NotNull
    private static final Long2ObjectMap<List<BF_1223>> field_7092 = new Long2ObjectOpenHashMap();

    public ShotValidationManager(@NotNull ServerCollisionTracker serverCollisionTracker) {
        this(serverCollisionTracker, 3);
    }

    public ShotValidationManager(@NotNull ServerCollisionTracker serverCollisionTracker, int n) {
        this.field_6863 = serverCollisionTracker;
        this.field_6864 = n;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean checkClientAll(@NotNull BFServerManager manager, @NotNull ServerPlayerDataHandler dataHandler, @NotNull ServerLevel level, @NotNull ServerPlayer player, @NotNull List<LivingBulletCollision> hits, Vec3 position, Vec2 direction, float spread, float backPitch, long tick, long seed) {
        BFServerPlayerData bFServerPlayerData = (BFServerPlayerData)dataHandler.getPlayerData((Player)player);
        boolean bl = bFServerPlayerData.getSuspiciousShotManager().method_5851();
        long l2 = this.field_6863.method_5842();
        boolean bl2 = tick > l2;
        ItemStack itemStack = player.getItemInHand(InteractionHand.MAIN_HAND);
        Item item = itemStack.getItem();
        if (!(item instanceof GunItem)) {
            if (bl) {
                BFUtils.sendAdminMessage(level, manager, (Component)Component.literal((String)("Player " + player.getScoreboardName() + " attempted to validate a shot without holding a gun item.")).withStyle(ChatFormatting.RED));
            }
            return false;
        }
        GunItem gunItem = (GunItem)item;
        if (BFFeatureFlags.serverShotValidationSpread && !this.checkClientSpread(manager, level, player, bl, spread, gunItem, itemStack, bFServerPlayerData)) {
            return false;
        }
        if (!player.getEyePosition().closerThan((Position)position, 2.0)) {
            if (bl) {
                float f = (float)player.getEyePosition().distanceTo(position);
                BFUtils.sendAdminMessage(level, manager, (Component)Component.literal((String)("Player " + player.getScoreboardName() + " attempted to validate a shot from an invalid position: " + String.format("%.2f", Float.valueOf(f)) + " blocks away. (" + position.x + ", " + position.y + ", " + position.z + ")")).withStyle(ChatFormatting.RED));
            }
            return false;
        }
        if (!this.checkClientFireRate(manager, level, player, bl, itemStack, gunItem, tick, bFServerPlayerData, !bl2)) {
            return false;
        }
        if (bl2) {
            long l3 = tick - l2;
            if (l3 > 20L) {
                if (bl) {
                    BFUtils.sendAdminMessage(level, manager, (Component)Component.literal((String)("Player " + player.getScoreboardName() + " attempted to validate a shot too far in the future: " + tick + " (" + l3 + " ticks ahead). Current tick: " + l2 + ". Max prediction: 20 ticks")).withStyle(ChatFormatting.RED));
                }
                return false;
            }
            BF_1223 bF_1223 = new BF_1223(tick, player, manager, dataHandler, hits, position, direction, spread, backPitch, seed);
            Long2ObjectMap<List<BF_1223>> long2ObjectMap = field_7092;
            synchronized (long2ObjectMap) {
                ((List)field_7092.computeIfAbsent(tick, l -> new ObjectArrayList())).add(bF_1223);
            }
            if (bl) {
                BFUtils.sendAdminMessage(level, manager, (Component)Component.literal((String)("Player " + player.getScoreboardName() + " submitted a future-time hit (tick " + tick + ", " + l3 + " ticks ahead). Stored for validation when time is reached.")).withStyle(ChatFormatting.YELLOW));
            }
            return true;
        }
        long l4 = this.field_6863.method_5843();
        if (tick < l4) {
            if (bl) {
                BFUtils.sendAdminMessage(level, manager, (Component)Component.literal((String)("Player " + player.getScoreboardName() + " attempted to validate a shot with an old tick: " + tick + ". Oldest tick: " + l4)).withStyle(ChatFormatting.RED));
            }
            return false;
        }
        if (!this.field_6863.method_5841(tick)) {
            if (bl) {
                BFUtils.sendAdminMessage(level, manager, (Component)Component.literal((String)("Player " + player.getScoreboardName() + " attempted to validate a shot with no data for tick: " + tick)).withStyle(ChatFormatting.RED));
            }
            return false;
        }
        int n = player.getId();
        EntityCollisionEntry entityCollisionEntry = this.field_6863.method_5837(tick, n);
        if (entityCollisionEntry != null && !entityCollisionEntry.canShoot()) {
            if (bl) {
                BFUtils.sendAdminMessage(level, manager, (Component)Component.literal((String)("Player " + player.getScoreboardName() + " attempted to validate a shot with no data for tick: " + tick)).withStyle(ChatFormatting.RED));
            }
            return false;
        }
        try {
            BF_1191 bF_1191 = new BF_1191(player, dataHandler, gunItem.getDamageConfig(), this.field_6863, position, direction, spread, backPitch, tick, this.field_6864);
            RandomSource randomSource = RandomSource.create((long)seed);
            Random random = new Random(seed);
            IntList intList = bF_1191.method_5799(manager, level, randomSource, random, (LivingEntity)player, gunItem, tick, seed);
            boolean bl3 = this.checkClientHits(manager, level, player, bl, hits, intList);
            if (!bl3) {
                ((BFServerPlayerData)dataHandler.getPlayerData((Player)player)).getSuspiciousShotManager().addSuspiciousShot();
            } else if (bl) {
                BFUtils.sendAdminMessage(level, manager, (Component)Component.literal((String)("Player " + player.getScoreboardName() + " successfully validated a shot with " + hits.size() + " client hits and " + intList.size() + " server hits.")).withStyle(ChatFormatting.GREEN));
            }
            return bl3;
        }
        catch (Exception exception) {
            return false;
        }
    }

    private boolean checkClientHits(BFServerManager manager, ServerLevel level, ServerPlayer player, boolean bl, @NotNull List<LivingBulletCollision> list, @NotNull IntList intList) {
        LivingBulletCollision livingBulletCollision2;
        IntOpenHashSet intOpenHashSet = new IntOpenHashSet();
        for (LivingBulletCollision livingBulletCollision2 : list) {
            intOpenHashSet.add(livingBulletCollision2.entityId());
        }
        IntOpenHashSet intOpenHashSet2 = new IntOpenHashSet((IntCollection)intList);
        livingBulletCollision2 = intOpenHashSet.iterator();
        while (livingBulletCollision2.hasNext()) {
            int n = (Integer)livingBulletCollision2.next();
            if (intOpenHashSet2.contains(n)) continue;
            if (bl) {
                BFUtils.sendAdminMessage(level, manager, (Component)Component.literal((String)("Player " + player.getScoreboardName() + " reported a hit on entity ID " + n + " that was not found in the server's hit results. Server hits: " + String.valueOf(intList))).withStyle(ChatFormatting.RED));
            }
            return false;
        }
        if (intOpenHashSet2.size() > intOpenHashSet.size()) {
            return true;
        }
        boolean bl2 = intOpenHashSet.equals((Object)intOpenHashSet2);
        if (!bl2 && bl) {
            BFUtils.sendAdminMessage(level, manager, (Component)Component.literal((String)("Player " + player.getScoreboardName() + " reported hits that do not match the server's hit results. Client hits: " + String.valueOf(intOpenHashSet) + ", Server hits: " + String.valueOf(intOpenHashSet2))).withStyle(ChatFormatting.RED));
        }
        return bl2;
    }

    public boolean checkClientSpread(@NotNull BFServerManager bFServerManager, @NotNull ServerLevel serverLevel, @NotNull ServerPlayer serverPlayer, boolean bl, float f, @NotNull GunItem gunItem, @NotNull ItemStack itemStack, @NotNull BFServerPlayerData bFServerPlayerData) {
        float f2;
        GunScopeConfig gunScopeConfig;
        GunSpreadConfig gunSpreadConfig = gunItem.getAimConfig();
        float f3 = BF_1204.method_5877(gunSpreadConfig, gunScopeConfig = gunItem.getScopeConfig(itemStack), gunItem.hasBipod(), bFServerPlayerData.method_842());
        if (!BF_1204.method_5874(f, f3, f2 = BF_1204.method_5878(gunScopeConfig, gunSpreadConfig))) {
            if (bl) {
                BFUtils.sendAdminMessage(serverLevel, bFServerManager, (Component)Component.literal((String)("Player " + serverPlayer.getScoreboardName() + " reported an invalid spread: " + String.format("%.4f", Float.valueOf(f)) + ". Expected range: [" + String.format("%.4f", Float.valueOf(f3)) + ", " + String.format("%.4f", Float.valueOf(f2)) + "]")).withStyle(ChatFormatting.RED));
            }
            return false;
        }
        if (bl) {
            BFUtils.sendAdminMessage(serverLevel, bFServerManager, (Component)Component.literal((String)("Player " + serverPlayer.getScoreboardName() + " reported a valid spread: " + String.format("%.4f", Float.valueOf(f)) + ". Expected range: [" + String.format("%.4f", Float.valueOf(f3)) + ", " + String.format("%.4f", Float.valueOf(f2)) + "]")).withStyle(ChatFormatting.GRAY));
        }
        return true;
    }

    private boolean checkClientFireRate(@NotNull BFServerManager bFServerManager, @NotNull ServerLevel serverLevel, @NotNull ServerPlayer serverPlayer, boolean bl, @NotNull ItemStack itemStack, @NotNull GunItem gunItem, long l, @NotNull BFServerPlayerData bFServerPlayerData) {
        return this.checkClientFireRate(bFServerManager, serverLevel, serverPlayer, bl, itemStack, gunItem, l, bFServerPlayerData, true);
    }

    private boolean checkClientFireRate(@NotNull BFServerManager bFServerManager, @NotNull ServerLevel serverLevel, @NotNull ServerPlayer serverPlayer, boolean bl, @NotNull ItemStack itemStack, @NotNull GunItem gunItem, long l, @NotNull BFServerPlayerData bFServerPlayerData, boolean bl2) {
        BF_1199 bF_1199 = bFServerPlayerData.method_5892();
        if (!bF_1199.method_5861(itemStack, l, bl2)) {
            if (bl) {
                Object object;
                String string;
                GunFireConfig gunFireConfig = gunItem.getDefaultFireConfig();
                int n = gunFireConfig.getFireRate();
                int n2 = gunFireConfig.method_4026();
                long l2 = bF_1199.method_5863();
                if (n2 > 1) {
                    string = "multi-pellet fire rate limit";
                    long l3 = l2 >= 0L ? l - l2 : 0L;
                    object = String.format("Expected minimum %d ticks between trigger pulls for %d-pellet weapon, but only %d ticks passed", n, n2, l3);
                } else {
                    string = "fire rate limit";
                    long l4 = l2 >= 0L ? l - l2 : 0L;
                    object = "Expected minimum " + n + " ticks between shots, but only " + l4 + " ticks passed";
                }
                BFUtils.sendAdminMessage(serverLevel, bFServerManager, (Component)Component.literal((String)("Player " + serverPlayer.getScoreboardName() + " violated " + string + ": " + (String)object + ". Weapon: " + itemStack.getItem().getDescriptionId())).withStyle(ChatFormatting.RED));
            }
            return false;
        }
        if (bl) {
            GunFireConfig gunFireConfig = gunItem.getDefaultFireConfig();
            int n = gunFireConfig.method_4026();
            String string = n > 1 ? "multi-pellet fire rate" : "fire rate";
            BFUtils.sendAdminMessage(serverLevel, bFServerManager, (Component)Component.literal((String)("Player " + serverPlayer.getScoreboardName() + " passed " + string + " validation for " + itemStack.getItem().getDescriptionId() + (String)(n > 1 ? " (" + n + " pellets)" : ""))).withStyle(ChatFormatting.GRAY));
        }
        return true;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void runChecks(@NotNull ServerCollisionTracker serverTracker) {
        long l = serverTracker.method_5842();
        Long2ObjectMap<List<BF_1223>> long2ObjectMap = field_7092;
        synchronized (long2ObjectMap) {
            ArrayList<Long> arrayList = new ArrayList<Long>();
            for (Long l2 : field_7092.keySet()) {
                if (l2 > l) continue;
                arrayList.add(l2);
            }
            Iterator iterator = arrayList.iterator();
            while (iterator.hasNext()) {
                long l3 = (Long)iterator.next();
                List list = (List)field_7092.remove(l3);
                if (list == null || list.isEmpty()) continue;
                for (BF_1223 bF_1223 : list) {
                    ShotValidationManager shotValidationManager;
                    boolean bl;
                    if (!bF_1223.serverPlayer().isAlive() || !(bl = (shotValidationManager = new ShotValidationManager(serverTracker)).checkClientAll(bF_1223.serverModManager(), bF_1223.playerDataHandler(), bF_1223.serverPlayer().serverLevel(), bF_1223.serverPlayer(), bF_1223.clientHits(), bF_1223.position(), bF_1223.direction(), bF_1223.reportedItemSpread(), bF_1223.shootBackPitch(), bF_1223.shootTick(), bF_1223.randomSeed()))) continue;
                    BFServerManager bFServerManager = bF_1223.serverModManager();
                    AbstractGame<?, ?, ?> abstractGame = bFServerManager.getGameWithPlayer((Player)bF_1223.serverPlayer());
                    BF_948.method_3944(bFServerManager, bF_1223.playerDataHandler(), bF_1223.serverPlayer().level(), (LivingEntity)bF_1223.serverPlayer(), abstractGame, bF_1223.clientHits());
                }
            }
        }
    }

    public static long method_5999() {
        return 20L;
    }
}

