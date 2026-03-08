/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.CloudRegistry
 *  com.boehmod.bflib.cloud.common.item.types.CloudItemArmour
 *  com.boehmod.bflib.cloud.common.player.PlayerDataType
 *  javax.annotation.Nullable
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.resources.sounds.SimpleSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.client.sounds.SoundManager
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.EquipmentSlot
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.level.Level
 *  net.neoforged.neoforge.registries.DeferredHolder
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.boehmod.blockfront.client.player;

import com.boehmod.bflib.cloud.common.CloudRegistry;
import com.boehmod.bflib.cloud.common.item.types.CloudItemArmour;
import com.boehmod.bflib.cloud.common.player.PlayerDataType;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.ClientFlamethrowerIgniteManager;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.player.FakePlayer;
import com.boehmod.blockfront.client.render.entity.BFPlayerRenderer;
import com.boehmod.blockfront.client.render.model.BFPlayerRenderLayer;
import com.boehmod.blockfront.client.screen.armory.ArmoryInspectScreen;
import com.boehmod.blockfront.client.screen.match.AbstractMatchSelectClassScreen;
import com.boehmod.blockfront.cloud.PlayerCloudInventory;
import com.boehmod.blockfront.common.item.ChestArmorItem;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.match.DivisionData;
import com.boehmod.blockfront.common.match.MatchCallout;
import com.boehmod.blockfront.common.match.MatchClass;
import com.boehmod.blockfront.common.player.BFAbstractPlayerData;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGameClient;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.unnamed.BF_1212;
import com.boehmod.blockfront.unnamed.BF_197;
import com.boehmod.blockfront.unnamed.BF_389;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.math.MathUtils;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BFClientPlayerData
extends BFAbstractPlayerData<BFClientManager, Level, Player, ClientFlamethrowerIgniteManager> {
    @NotNull
    private static final String field_1605 = "textures/skins/game/nations/";
    @NotNull
    public static final String field_1606 = "backpack";
    @NotNull
    private ResourceLocation waypointTexture = MatchCallout.MEDIC.getTexture();
    private int field_1616 = 0;
    private static final int field_1617 = 300;
    private int calloutTimer = 0;
    private int field_1619 = 0;
    @javax.annotation.Nullable
    private BF_1212 field_1608;
    @javax.annotation.Nullable
    private BF_389 field_1607;
    @javax.annotation.Nullable
    private Item field_1609 = null;
    @javax.annotation.Nullable
    private SimpleSoundInstance field_1610;
    @javax.annotation.Nullable
    private SimpleSoundInstance field_1614;
    @javax.annotation.Nullable
    private ResourceLocation field_1612;
    @javax.annotation.Nullable
    private ResourceLocation field_1613;
    private int field_1620 = 0;
    private int field_1621 = 0;
    private float field_1615 = 0.0f;

    public BFClientPlayerData(@NotNull UUID uUID) {
        super(uUID);
    }

    @Override
    @NotNull
    public ClientFlamethrowerIgniteManager method_836() {
        return new ClientFlamethrowerIgniteManager(this);
    }

    @NotNull
    public ResourceLocation getWaypointTexture() {
        return this.waypointTexture;
    }

    public void method_1156(int n) {
        this.field_1616 = n;
    }

    public int method_1131() {
        return this.field_1616;
    }

    private void method_1144(@NotNull Player player) {
        if (this.field_1160) {
            player.zza = 0.0f;
            player.xxa = 0.0f;
        }
    }

    private void method_1145() {
        this.field_1166 = this.field_1165;
        this.field_1165 = Mth.lerp((float)0.4f, (float)this.field_1165, (float)0.0f);
        if (this.calloutTimer > 0) {
            --this.calloutTimer;
        }
        if (this.field_1616 > 0) {
            --this.field_1616;
        }
    }

    public void method_1140(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull Random random, @NotNull ClientLevel clientLevel, @NotNull LocalPlayer localPlayer, @javax.annotation.Nullable AbstractGame<?, ?, ?> abstractGame, @javax.annotation.Nullable AbstractGameClient<?, ?> abstractGameClient, @NotNull Player player) {
        boolean bl = player.equals((Object)localPlayer);
        ((ClientFlamethrowerIgniteManager)this.field_1152).method_1102(clientLevel, player, random);
        if (this.calloutCooldown > 0) {
            --this.calloutCooldown;
        }
        if (BFUtils.isPlayerUnavailable(player, this)) {
            this.field_1619 = 10;
        } else if (this.field_1619 > 0) {
            --this.field_1619;
        }
        this.method_1144(player);
        this.method_1145();
        this.method_1138(abstractGame, bl, localPlayer, player);
        this.method_1139(minecraft, bFClientManager, clientPlayerDataHandler, abstractGame, abstractGameClient);
        this.method_1141(minecraft, player);
    }

    public int method_1132() {
        return this.field_1619;
    }

    public void method_1157(int n) {
        this.field_1619 = n;
    }

    private void method_1141(@NotNull Minecraft minecraft, @NotNull Player player) {
        if (player instanceof FakePlayer) {
            return;
        }
        SoundManager soundManager = minecraft.getSoundManager();
        ItemStack itemStack = player.getItemBySlot(EquipmentSlot.CHEST);
        Item item = itemStack.getItem();
        if (item != this.field_1609) {
            ChestArmorItem chestArmorItem;
            DeferredHolder<SoundEvent, SoundEvent> deferredHolder;
            this.field_1609 = item;
            if (this.field_1607 != null && soundManager.isActive((SoundInstance)this.field_1607)) {
                soundManager.stop((SoundInstance)this.field_1607);
                this.field_1607 = null;
            }
            if (item instanceof ChestArmorItem && (deferredHolder = (chestArmorItem = (ChestArmorItem)item).method_4240()) != null) {
                this.field_1607 = new BF_389((Entity)player, (SoundEvent)deferredHolder.get(), SoundSource.PLAYERS, chestArmorItem.method_4238());
                soundManager.play((SoundInstance)this.field_1607);
            }
        }
        if ((this.field_1609 == null || this.field_1609 == Items.AIR) && this.field_1607 != null && soundManager.isActive((SoundInstance)this.field_1607)) {
            soundManager.stop((SoundInstance)this.field_1607);
            this.field_1607 = null;
        }
    }

    private void method_1139(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, @Nullable AbstractGame<?, ?, ?> abstractGame, @Nullable AbstractGameClient<?, ?> abstractGameClient) {
        if (this.method_835() == PlayerDataType.DISPLAY) {
            return;
        }
        if (this.field_1620-- <= 0) {
            this.field_1620 = 80;
            if (abstractGame != null && abstractGameClient != null) {
                this.method_1149(minecraft, bFClientManager, clientPlayerDataHandler, abstractGame, abstractGameClient);
            } else {
                this.field_1612 = null;
                this.field_1613 = null;
            }
        }
    }

    public void method_1143(@javax.annotation.Nullable ResourceLocation resourceLocation, @javax.annotation.Nullable ResourceLocation resourceLocation2) {
        this.field_1612 = resourceLocation;
        this.field_1613 = resourceLocation2;
    }

    public void method_1146() {
        this.field_1620 = 0;
    }

    private void method_1149(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull AbstractGameClient<?, ?> abstractGameClient) {
        Object object;
        Object object2;
        GameTeam gameTeam;
        Object object3;
        Object obj = abstractGame.getPlayerManager();
        Set<UUID> set = ((AbstractGamePlayerManager)obj).getPlayerUUIDs();
        CloudRegistry cloudRegistry = bFClientManager.getCloudRegistry();
        int n = abstractGame.getPlayerStatData(this.uuid).getInteger(BFStats.CLASS.getKey(), -1);
        this.field_1612 = BFPlayerRenderer.method_1358(this.uuid, abstractGameClient, n != -1 ? MatchClass.values()[n].getKey() : "rifleman", set);
        this.field_1613 = BFPlayerRenderer.method_1358(this.uuid, abstractGameClient, field_1606, set);
        Object object4 = minecraft.screen;
        if (object4 instanceof AbstractMatchSelectClassScreen) {
            object3 = (AbstractMatchSelectClassScreen)((Object)object4);
            if (this.uuid.equals(minecraft.getUser().getProfileId())) {
                object4 = object3.method_895();
                gameTeam = ((AbstractGamePlayerManager)obj).getPlayerTeam(this.uuid);
                if (object4 != null && gameTeam != null && (object2 = ((BF_197)object4).method_905()) != null) {
                    object = gameTeam.getDivisionData(abstractGame);
                    String string = ((BF_197)object4).method_906().getKey();
                    this.field_1612 = BFPlayerRenderLayer.method_1279(field_1605 + ((DivisionData)object).getCountry().getTag() + "/" + ((DivisionData)object).getSkin() + "/" + string + ".png");
                }
            }
        }
        if ((object4 = minecraft.screen) instanceof ArmoryInspectScreen && (object2 = (object4 = ((ArmoryInspectScreen)((Object)(object3 = (ArmoryInspectScreen)((Object)object4)))).method_717()).getCloudItem(cloudRegistry)) instanceof CloudItemArmour) {
            gameTeam = (CloudItemArmour)object2;
            object2 = gameTeam.getNation();
            this.field_1612 = BFPlayerRenderLayer.method_1279(field_1605 + object2.getTag() + "/" + gameTeam.getSuffix().toLowerCase(Locale.ROOT) + "/rifleman.png");
        }
        object3 = clientPlayerDataHandler.getCloudProfile(this.uuid);
        object4 = (PlayerCloudInventory)object3.getInventory();
        if (this.field_1612 == null && (gameTeam = ((PlayerCloudInventory)((Object)object4)).method_1679()) != null && (object = gameTeam.getCloudItem(cloudRegistry)) instanceof CloudItemArmour) {
            object2 = (CloudItemArmour)object;
            this.field_1612 = BFPlayerRenderLayer.method_1279(field_1605 + object2.getNation().getTag() + "/" + object2.getSuffix().toLowerCase(Locale.ROOT) + "/rifleman.png");
        }
    }

    private void method_1138(@javax.annotation.Nullable AbstractGame<?, ?, ?> abstractGame, boolean bl, @javax.annotation.Nullable LocalPlayer localPlayer, @NotNull Player player) {
        if (!bl) {
            this.field_1168 = this.field_1167;
            this.field_1167 = Mth.lerp((float)0.5f, (float)this.field_1167, (float)this.field_1615);
            if (this.field_1621++ >= 10) {
                this.method_1136(abstractGame, localPlayer, player);
                this.field_1621 = 0;
            }
        } else {
            this.field_1168 = 0.0f;
            this.field_1167 = 0.0f;
        }
    }

    private void method_1136(@javax.annotation.Nullable AbstractGame<?, ?, ?> abstractGame, @javax.annotation.Nullable LocalPlayer localPlayer, @NotNull Player player) {
        this.field_1615 = localPlayer != null && this.method_1137(abstractGame, localPlayer, player) ? MathUtils.yawDifference((Entity)localPlayer, (Entity)player) : 0.0f;
    }

    private boolean method_1137(@javax.annotation.Nullable AbstractGame<?, ?, ?> abstractGame, @NotNull LocalPlayer localPlayer, @NotNull Player player) {
        if (!this.method_842()) {
            return false;
        }
        if (!localPlayer.hasLineOfSight((Entity)player)) {
            return false;
        }
        ItemStack itemStack = player.getMainHandItem();
        if (itemStack.isEmpty() || !(itemStack.getItem() instanceof GunItem)) {
            return false;
        }
        if (!GunItem.getScope(itemStack)) {
            return false;
        }
        if (abstractGame != null) {
            Object obj = abstractGame.getPlayerManager();
            GameTeam gameTeam = ((AbstractGamePlayerManager)obj).getPlayerTeam(localPlayer.getUUID());
            GameTeam gameTeam2 = ((AbstractGamePlayerManager)obj).getPlayerTeam(this.uuid);
            return gameTeam == null || !gameTeam.equals(gameTeam2);
        }
        return true;
    }

    @javax.annotation.Nullable
    public ResourceLocation method_1154() {
        return this.field_1612;
    }

    @javax.annotation.Nullable
    public ResourceLocation method_1155() {
        return this.field_1613;
    }

    public void setCurrentCallout(@NotNull MatchCallout callout) {
        this.calloutTimer = 300;
        ResourceLocation resourceLocation = callout.getWaypointTexture();
        if (resourceLocation != null) {
            this.waypointTexture = resourceLocation;
        }
    }

    public int getCalloutTimer() {
        return this.calloutTimer;
    }

    @javax.annotation.Nullable
    public BF_1212 method_1135() {
        return this.field_1608;
    }

    public void method_1142(@NotNull BF_1212 bF_1212) {
        this.field_1608 = bF_1212;
    }

    @javax.annotation.Nullable
    public SimpleSoundInstance method_1147() {
        return this.field_1610;
    }

    public void method_1150(@NotNull SimpleSoundInstance simpleSoundInstance) {
        this.field_1610 = simpleSoundInstance;
    }

    @javax.annotation.Nullable
    public SimpleSoundInstance method_1151() {
        return this.field_1614;
    }

    public void method_1152(@NotNull SimpleSoundInstance simpleSoundInstance) {
        this.field_1614 = simpleSoundInstance;
    }
}

