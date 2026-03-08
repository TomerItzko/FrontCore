/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.screen.match.MatchCalloutScreen;
import com.boehmod.blockfront.client.world.ShakeManager;
import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.net.packet.BFGunReloadPacket;
import com.boehmod.blockfront.common.net.packet.BFHornPacket;
import com.boehmod.blockfront.common.net.packet.BFWarCryPacket;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGameClient;
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.game.tag.IAllowsCallouts;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.PacketUtils;
import com.boehmod.blockfront.util.math.ShakeNodePresets;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.jetbrains.annotations.NotNull;

public class BFKeyMappings {
    @NotNull
    public static final List<Basic> INSTANCES = new ObjectArrayList();
    public static final KeyMapping GUN_RELOAD = BFKeyMappings.register(new WithConsumer("bf.key.gun.reload", 82, "bf.key.categories.weapon", minecraft -> PacketUtils.sendToServer(new BFGunReloadPacket())));
    public static final KeyMapping GUN_FIREMODE = BFKeyMappings.register(new WithConsumer("bf.key.gun.firemode", 71, "bf.key.categories.weapon", minecraft -> {
        Item item;
        if (minecraft.player == null || minecraft.screen != null) {
            return;
        }
        ItemStack itemStack = minecraft.player.getMainHandItem();
        if (!itemStack.isEmpty() && (item = itemStack.getItem()) instanceof GunItem) {
            GunItem gunItem = (GunItem)item;
            if (!GunItem.isReloading(itemStack) && gunItem.getFireConfigs().length > 1) {
                minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.ITEM_GUN_SHARED_FIREMODE.get()), (float)1.0f, (float)1.0f));
                gunItem.method_4179();
                ShakeManager.applyShake(ShakeNodePresets.field_1905);
            }
        }
    }));
    public static final KeyMapping GUN_INSPECT = BFKeyMappings.register(new Basic("bf.key.gun.inspect", 70, "bf.key.categories.weapon"));
    public static final KeyMapping MATCH_WARCRY = BFKeyMappings.register(new WithConsumer("bf.key.match.warcry", 67, "bf.key.categories.match", minecraft -> PacketUtils.sendToServer(new BFWarCryPacket())));
    public static final KeyMapping MATCH_TEAMCHAT = BFKeyMappings.register(new WithConsumer("bf.key.match.teamchat", 89, "bf.key.categories.match", minecraft -> minecraft.setScreen((Screen)new ChatScreen("[Team] "))));
    public static final KeyMapping CALLOUT = BFKeyMappings.register(new WithConsumer("bf.key.callout", 88, "bf.key.categories.match", minecraft -> {
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        ClientPlayerDataHandler clientPlayerDataHandler = (ClientPlayerDataHandler)bFClientManager.getPlayerDataHandler();
        BFClientPlayerData bFClientPlayerData = clientPlayerDataHandler.getPlayerData((Minecraft)minecraft);
        if (bFClientPlayerData.isOutOfGame()) {
            return;
        }
        LocalPlayer localPlayer = minecraft.player;
        if (localPlayer == null || BFUtils.isPlayerUnavailable((Player)localPlayer, bFClientPlayerData)) {
            return;
        }
        AbstractGame<?, ?, ?> abstractGame = bFClientManager.getGame();
        if (abstractGame instanceof IAllowsCallouts && abstractGame.getStatus() == GameStatus.GAME && minecraft.screen == null) {
            minecraft.setScreen((Screen)new MatchCalloutScreen());
        }
    }));
    public static final KeyMapping VEHICLE_HORN = BFKeyMappings.register(new Updatable("bf.key.vehicle.horn", 72, "bf.key.categories.vehicle", 60, minecraft -> {
        if (minecraft.player != null && minecraft.player.getVehicle() instanceof AbstractVehicleEntity) {
            PacketUtils.sendToServer(new BFHornPacket());
        }
    }));
    public static final KeyMapping MATCH_CLASS_SELECTION = BFKeyMappings.register(new WithConsumer("bf.key.match.class.selection", 77, "bf.key.categories.match", minecraft -> {
        MATCH_CLASS_SELECTION.setDown(false);
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        AbstractGameClient<?, ?> abstractGameClient = bFClientManager.getGameClient();
        if (abstractGameClient == null) {
            return;
        }
        LocalPlayer localPlayer = minecraft.player;
        if (localPlayer != null) {
            abstractGameClient.method_2729((Minecraft)minecraft, localPlayer);
        }
    }));

    public static void register(@NotNull RegisterKeyMappingsEvent event) {
        event.register(GUN_RELOAD);
        event.register(GUN_FIREMODE);
        event.register(GUN_INSPECT);
        event.register(MATCH_TEAMCHAT);
        event.register(CALLOUT);
        event.register(MATCH_WARCRY);
        event.register(VEHICLE_HORN);
        event.register(MATCH_CLASS_SELECTION);
    }

    public static Basic register(@NotNull Basic basic) {
        INSTANCES.add(basic);
        return basic;
    }

    public static void update(@NotNull Minecraft minecraft) {
        for (Basic basic : INSTANCES) {
            basic.onUpdate();
            if (basic.consumeClick()) {
                if (!basic.method_5121()) continue;
                basic.method_5119(minecraft);
                continue;
            }
            if (basic.isDown() || !basic.method_5122()) continue;
            basic.method_5120(minecraft);
        }
    }

    public static class Basic
    extends KeyMapping {
        public boolean field_4911 = true;

        public Basic(@NotNull String string, int n, @NotNull String string2) {
            super(string, n, string2);
        }

        public void onUpdate() {
        }

        public boolean method_5121() {
            return true;
        }

        public void method_5119(Minecraft minecraft) {
        }

        public void method_5120(Minecraft minecraft) {
        }

        public boolean consumeClick() {
            boolean bl = super.consumeClick();
            if (bl) {
                this.field_4911 = false;
            }
            return bl;
        }

        public boolean method_5122() {
            boolean bl = !this.field_4911;
            this.field_4911 = true;
            return bl;
        }
    }

    public static class WithConsumer
    extends Basic {
        private final Consumer<Minecraft> field_4910;

        public WithConsumer(@NotNull String string, int n, @NotNull String string2, @NotNull Consumer<Minecraft> consumer) {
            super(string, n, string2);
            this.field_4910 = consumer;
        }

        @Override
        public void method_5119(Minecraft minecraft) {
            this.field_4910.accept(minecraft);
        }
    }

    public static class Updatable
    extends WithConsumer {
        private final int field_4912;
        private int field_4913 = 0;

        public Updatable(@NotNull String string, int n, @NotNull String string2, int n2, @NotNull Consumer<Minecraft> consumer) {
            super(string, n, string2, consumer);
            this.field_4912 = n2;
        }

        @Override
        public void onUpdate() {
            if (this.field_4913 > 0) {
                --this.field_4913;
            }
        }

        @Override
        public boolean method_5121() {
            return this.field_4913 <= 0;
        }

        @Override
        public void method_5119(Minecraft minecraft) {
            super.method_5119(minecraft);
            this.field_4913 = this.field_4912;
        }
    }
}

