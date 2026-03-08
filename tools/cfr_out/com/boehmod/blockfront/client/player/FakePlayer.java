/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.CloudRegistry
 *  com.boehmod.bflib.cloud.common.item.CloudItem
 *  com.boehmod.bflib.cloud.common.item.CloudItemStack
 *  com.boehmod.bflib.cloud.common.item.types.CloudItemGun
 *  com.boehmod.bflib.cloud.common.item.types.CloudItemMelee
 *  com.boehmod.bflib.cloud.common.player.PlayerDataType
 *  com.mojang.authlib.GameProfile
 *  it.unimi.dsi.fastutil.floats.FloatFloatPair
 *  javax.annotation.Nullable
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.multiplayer.PlayerInfo
 *  net.minecraft.client.player.RemotePlayer
 *  net.minecraft.util.Mth
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.phys.Vec2
 *  net.minecraft.world.phys.Vec3
 *  org.apache.commons.lang3.tuple.Pair
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.player;

import com.boehmod.bflib.cloud.common.CloudRegistry;
import com.boehmod.bflib.cloud.common.item.CloudItem;
import com.boehmod.bflib.cloud.common.item.CloudItemStack;
import com.boehmod.bflib.cloud.common.item.types.CloudItemGun;
import com.boehmod.bflib.cloud.common.item.types.CloudItemMelee;
import com.boehmod.bflib.cloud.common.player.PlayerDataType;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.env.FakeLevel;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.cloud.PlayerCloudInventory;
import com.boehmod.blockfront.common.item.ItemSkinIndex;
import com.boehmod.blockfront.common.match.DivisionData;
import com.boehmod.blockfront.common.match.Loadout;
import com.boehmod.blockfront.common.match.MatchClass;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.authlib.GameProfile;
import it.unimi.dsi.fastutil.floats.FloatFloatPair;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

public class FakePlayer
extends RemotePlayer {
    @NotNull
    public static final DivisionData DIVISION = DivisionData.getRandom();
    @NotNull
    private final PlayerInfo playerInfo;
    private final float field_443;
    private int field_444 = 20;
    private Vec2 field_446 = Vec2.ZERO;
    private Vec2 field_447 = Vec2.ZERO;
    private Vec2 field_448 = Vec2.ZERO;
    private int field_445 = 0;

    public FakePlayer(@NotNull FakeLevel fakeLevel, @NotNull GameProfile gameProfile) {
        super((ClientLevel)fakeLevel, gameProfile);
        this.playerInfo = new PlayerInfo(gameProfile, true);
        this.field_443 = MathUtils.randomLerp(0.4f, 0.8f);
    }

    public boolean method_473() {
        return false;
    }

    @NotNull
    public FloatFloatPair method_471(float f) {
        float f2 = MathUtils.lerpf1(this.field_448.x, this.field_447.x, f);
        float f3 = MathUtils.lerpf1(this.field_448.y, this.field_447.y, f);
        return FloatFloatPair.of((float)f2, (float)f3);
    }

    public boolean isInvisibleTo(@Nullable Player player) {
        return false;
    }

    public void calculateEntityAnimation(boolean bl) {
    }

    public void tick() {
        super.tick();
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        ClientPlayerDataHandler clientPlayerDataHandler = (ClientPlayerDataHandler)bFClientManager.getPlayerDataHandler();
        PlayerCloudData playerCloudData = clientPlayerDataHandler.getCloudProfile(this.uuid);
        this.walkAnimation.update(1.5f, 0.0f);
        this.walkAnimation.setSpeed(this.field_443);
        this.setPos(-99.0, -99.0, -99.0);
        this.xRotO = 0.0f;
        this.yRotO = 0.0f;
        this.yBodyRot = 0.0f;
        this.yBodyRotO = 0.0f;
        this.yHeadRot = 0.0f;
        this.yHeadRotO = 0.0f;
        this.setDeltaMovement(Vec3.ZERO);
        this.field_447 = new Vec2(this.field_448.x, this.field_448.y);
        float f = Mth.lerp((float)0.3f, (float)this.field_448.x, (float)this.field_446.x);
        float f2 = Mth.lerp((float)0.3f, (float)this.field_448.y, (float)this.field_446.y);
        this.field_448 = new Vec2(f, f2);
        if (playerCloudData.getPlayerDataType() == PlayerDataType.DISPLAY && this.field_445-- <= 0) {
            if (this.random.nextBoolean()) {
                float f3 = 20.0f;
                float f4 = 25.0f;
                this.field_445 = 20 + this.random.nextInt(60);
                this.field_446 = new Vec2(-2.0f + MathUtils.randomLerp(-20.0f, 20.0f), MathUtils.randomLerp(-25.0f, 25.0f));
            } else {
                this.field_446 = Vec2.ZERO;
            }
        }
        --this.field_444;
        if (playerCloudData.getPlayerDataType() == PlayerDataType.DISPLAY) {
            if (this.getMainHandItem().isEmpty()) {
                Optional<Pair<Loadout, MatchClass>> optional = DIVISION.getRandomLoadout(true);
                optional.ifPresent(pair -> {
                    Loadout loadout = (Loadout)pair.getLeft();
                    this.setItemInHand(InteractionHand.MAIN_HAND, loadout.getPrimary());
                });
            }
        } else if (this.field_444 <= 0) {
            this.field_444 = 20;
            this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
            this.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
            PlayerCloudInventory playerCloudInventory = (PlayerCloudInventory)playerCloudData.getInventory();
            CloudRegistry cloudRegistry = bFClientManager.getCloudRegistry();
            for (CloudItemStack cloudItemStack : playerCloudInventory.getItems()) {
                CloudItem cloudItem = cloudItemStack.getCloudItem(cloudRegistry);
                if (cloudItem == null) continue;
                ItemStack itemStack = ItemSkinIndex.method_1722(cloudItem, cloudItemStack);
                boolean bl = playerCloudInventory.isItemEquipped(cloudRegistry, cloudItemStack);
                if (cloudItem instanceof CloudItemMelee && this.getItemInHand(InteractionHand.OFF_HAND).isEmpty() && bl) {
                    this.setItemInHand(InteractionHand.OFF_HAND, itemStack);
                    continue;
                }
                if (!(cloudItem instanceof CloudItemGun) || !this.getItemInHand(InteractionHand.MAIN_HAND).isEmpty() || !bl) continue;
                this.setItemInHand(InteractionHand.MAIN_HAND, itemStack);
            }
        }
    }

    @NotNull
    protected PlayerInfo getPlayerInfo() {
        return this.playerInfo;
    }
}

