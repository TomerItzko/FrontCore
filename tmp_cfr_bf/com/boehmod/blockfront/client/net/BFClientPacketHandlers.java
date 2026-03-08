/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.net;

import com.boehmod.bflib.cloud.common.ChatGraphic;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.ac.BFClientAntiCheat;
import com.boehmod.blockfront.client.ac.BFClientScreenshot;
import com.boehmod.blockfront.client.event.BFClientTickSubscriber;
import com.boehmod.blockfront.client.event.tick.PlayerTickable;
import com.boehmod.blockfront.client.gui.BFNotification;
import com.boehmod.blockfront.client.gui.layer.CrosshairGuiLayer;
import com.boehmod.blockfront.client.gui.layer.HealthEffectsGuiLayer;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.screen.BFScreenshotScreen;
import com.boehmod.blockfront.client.screen.match.AbstractMatchSelectClassScreen;
import com.boehmod.blockfront.client.settings.BFClientSettings;
import com.boehmod.blockfront.client.sound.entity.EntitySoundInstance;
import com.boehmod.blockfront.client.world.ShakeManager;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.block.entity.AbstractCrateGunBlockEntity;
import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.common.entity.BotEntity;
import com.boehmod.blockfront.common.gun.bullet.BlockBulletCollision;
import com.boehmod.blockfront.common.gun.bullet.LivingBulletCollision;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.match.MatchCallout;
import com.boehmod.blockfront.common.match.kill.KillMessage;
import com.boehmod.blockfront.common.net.packet.BFBulletCollisionBlockClientPacket;
import com.boehmod.blockfront.common.net.packet.BFBulletCollisionLivingClientPacket;
import com.boehmod.blockfront.common.net.packet.BFBulletDamageEffectsPacket;
import com.boehmod.blockfront.common.net.packet.BFBulletTracerToClientPacket;
import com.boehmod.blockfront.common.net.packet.BFCalloutWaypointPacket;
import com.boehmod.blockfront.common.net.packet.BFCapturePointSpawnErrorPacket;
import com.boehmod.blockfront.common.net.packet.BFChatGraphicPacket;
import com.boehmod.blockfront.common.net.packet.BFCinematicEffectFlashPcket;
import com.boehmod.blockfront.common.net.packet.BFClearRagdollsPacket;
import com.boehmod.blockfront.common.net.packet.BFClientNotificationPacket;
import com.boehmod.blockfront.common.net.packet.BFCrateEntityPacket;
import com.boehmod.blockfront.common.net.packet.BFDamageIndicatorPacket;
import com.boehmod.blockfront.common.net.packet.BFDebugBoxPacket;
import com.boehmod.blockfront.common.net.packet.BFDebugLinePacket;
import com.boehmod.blockfront.common.net.packet.BFEntitySoundPacket;
import com.boehmod.blockfront.common.net.packet.BFExplosionPacket;
import com.boehmod.blockfront.common.net.packet.BFFrozenPacket;
import com.boehmod.blockfront.common.net.packet.BFGameKillFeedPacket;
import com.boehmod.blockfront.common.net.packet.BFGameKilledMessagePacket;
import com.boehmod.blockfront.common.net.packet.BFGameMapVoteInfoPacket;
import com.boehmod.blockfront.common.net.packet.BFGamePacket;
import com.boehmod.blockfront.common.net.packet.BFGrenadeFlashPacket;
import com.boehmod.blockfront.common.net.packet.BFGunReloadClientPacket;
import com.boehmod.blockfront.common.net.packet.BFGunSoundPacket;
import com.boehmod.blockfront.common.net.packet.BFHitMarkerPacket;
import com.boehmod.blockfront.common.net.packet.BFPlayerDataPacket;
import com.boehmod.blockfront.common.net.packet.BFPopupMessagePacket;
import com.boehmod.blockfront.common.net.packet.BFPositionedShakeNodePacket;
import com.boehmod.blockfront.common.net.packet.BFQueueMusicPacket;
import com.boehmod.blockfront.common.net.packet.BFRadioPointPacket;
import com.boehmod.blockfront.common.net.packet.BFScreenshotDisplayPacket;
import com.boehmod.blockfront.common.net.packet.BFScreenshotRequestPacket;
import com.boehmod.blockfront.common.net.packet.BFSequencePacket;
import com.boehmod.blockfront.common.net.packet.BFSequencePositionUpdatedPacket;
import com.boehmod.blockfront.common.net.packet.BFShakeNodePacket;
import com.boehmod.blockfront.common.net.packet.BFSoundPacket;
import com.boehmod.blockfront.common.net.packet.BFSoundPositionPacket;
import com.boehmod.blockfront.common.net.packet.BFSpawnCorpsePacket;
import com.boehmod.blockfront.common.net.packet.BFVehicleFireEffectPacket;
import com.boehmod.blockfront.common.net.packet.BFVictoryPacket;
import com.boehmod.blockfront.common.net.packet.BFWorldFlashPacket;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGameClient;
import com.boehmod.blockfront.game.BFGameType;
import com.boehmod.blockfront.game.GameEventType;
import com.boehmod.blockfront.game.GameRadioPoint;
import com.boehmod.blockfront.game.GameSequence;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.unnamed.BF_1179;
import com.boehmod.blockfront.unnamed.BF_39;
import com.boehmod.blockfront.unnamed.BF_553;
import com.boehmod.blockfront.util.BFLog;
import com.boehmod.blockfront.util.BulletUtils;
import com.boehmod.blockfront.util.EnvironmentUtils;
import com.boehmod.blockfront.util.ExplosionUtils;
import com.boehmod.blockfront.util.FormatUtils;
import com.boehmod.blockfront.util.GunUtils;
import com.boehmod.blockfront.util.math.FDSPose;
import com.boehmod.blockfront.util.math.ShakeNodePresets;
import com.mojang.blaze3d.platform.NativeImage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Position;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public class BFClientPacketHandlers {
    private static final Component SCREENSHOT_REQUEST_ERROR = Component.translatable((String)"bf.message.screenshot.request.error");
    private static final int field_2797 = 3;
    public static final int field_6708 = 2;

    public static void frozen(@NotNull BFFrozenPacket packet, @NotNull IPayloadContext context) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer localPlayer = minecraft.player;
        ClientLevel clientLevel = minecraft.level;
        if (localPlayer == null || clientLevel == null) {
            return;
        }
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        FDSPose fDSPose = packet.blockLocation();
        boolean bl = fDSPose != null;
        ClientPlayerDataHandler clientPlayerDataHandler = (ClientPlayerDataHandler)bFClientManager.getPlayerDataHandler();
        BFClientPlayerData bFClientPlayerData = clientPlayerDataHandler.getPlayerData(minecraft);
        if (!clientLevel.isClientSide() && !minecraft.hasSingleplayerServer()) {
            bFClientPlayerData.method_832(bl);
        }
        if (bl) {
            localPlayer.teleportTo(fDSPose.position.x, fDSPose.position.y, fDSPose.position.z);
            if (localPlayer.getHealth() > 0.0f) {
                bFClientPlayerData.setPose(fDSPose);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Unable to fully structure code
     */
    public static void game(@NotNull BFGamePacket packet, @NotNull IPayloadContext context) {
        block11: {
            block10: {
                var2_2 = Minecraft.getInstance();
                var3_3 = var2_2.player;
                var4_4 = var2_2.level;
                var5_5 = BFClientManager.getInstance();
                if (!BFClientPacketHandlers.field_2798 && var5_5 == null) {
                    throw new AssertionError((Object)"Client mod manager is null!");
                }
                var6_6 = packet.method_4319();
                var7_7 = (ClientPlayerDataHandler)var5_5.getPlayerDataHandler();
                var8_8 = var5_5.getGame();
                try {
                    if (!var6_6) ** GOTO lbl47
                    var9_9 = packet.method_4316();
                    if (var8_8 != null) ** GOTO lbl39
                    var10_10 = packet.method_4315();
                    if (var10_10 != null) break block10;
                    packet.freeBuf();
                    return;
                }
                catch (Throwable var14_17) {
                    packet.freeBuf();
                    throw var14_17;
                }
            }
            var11_13 = BFGameType.getByName(var10_10);
            if (var11_13 != null) break block11;
            packet.freeBuf();
            return;
        }
        try {
            var12_14 = var11_13.getGameClass().getDeclaredConstructor(new Class[]{BFAbstractManager.class}).newInstance(new Object[]{var5_5});
            var12_14.readAll(var9_9);
            var5_5.setCurrentGame(var2_2, var7_7, var3_3, var4_4, var12_14);
            var13_16 = var5_5.getGameClient();
            if (var13_16 == null) ** GOTO lbl49
            var13_16.read(var9_9);
        }
        catch (IOException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException var12_15) {
            throw new RuntimeException(var12_15);
        }
lbl39:
        // 1 sources

        try {
            var8_8.readAll(var9_9);
            var10_11 = var5_5.getGameClient();
            if (var10_11 == null) ** GOTO lbl49
            var10_11.read(var9_9);
        }
        catch (IOException var10_12) {
            throw new RuntimeException(var10_12);
        }
lbl47:
        // 1 sources

        if (var8_8 != null) {
            var5_5.setCurrentGame(var2_2, var7_7, var3_3, var4_4, null);
        }
lbl49:
        // 7 sources

        packet.freeBuf();
    }

    public static void gameKillFeed(@NotNull BFGameKillFeedPacket packet, @NotNull IPayloadContext context) {
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        AbstractGame<?, ?, ?> abstractGame = bFClientManager.getGame();
        if (abstractGame == null) {
            return;
        }
        AbstractGameClient<?, ?> abstractGameClient = bFClientManager.getGameClient();
        if (abstractGameClient != null) {
            abstractGameClient.addKillFeedEntry(bFClientManager.getMinecraft(), packet.entry());
        }
    }

    public static void gameKilledMessage(@NotNull BFGameKilledMessagePacket packet, @NotNull IPayloadContext context) {
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        AbstractGame<?, ?, ?> abstractGame = bFClientManager.getGame();
        if (abstractGame == null) {
            return;
        }
        KillMessage killMessage = new KillMessage(packet.playerUUID(), packet.itemUsed(), packet.userHealth());
        AbstractGameClient<?, ?> abstractGameClient = bFClientManager.getGameClient();
        if (abstractGameClient != null) {
            abstractGameClient.setKillMessage(killMessage);
        }
    }

    public static void playerData(@NotNull BFPlayerDataPacket packet, @NotNull IPayloadContext context) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel clientLevel = minecraft.level;
        if (clientLevel == null) {
            return;
        }
        UUID uUID = packet.playerData().getUUID();
        for (AbstractClientPlayer abstractClientPlayer : clientLevel.players()) {
            if (!abstractClientPlayer.getUUID().equals(uUID)) continue;
            abstractClientPlayer.refreshDimensions();
            break;
        }
    }

    public static void gunReloadClient(@NotNull BFGunReloadClientPacket packet, @NotNull IPayloadContext context) {
        Item item;
        ItemStack itemStack;
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer localPlayer = minecraft.player;
        if (localPlayer == null) {
            return;
        }
        ItemStack itemStack2 = itemStack = packet.field_6781 ? localPlayer.getMainHandItem() : localPlayer.getOffhandItem();
        if (!itemStack.isEmpty() && (item = itemStack.getItem()) instanceof GunItem) {
            GunItem gunItem = (GunItem)item;
            gunItem.reloadClient(minecraft, itemStack);
        }
    }

    public static void gunSound(@NotNull BFGunSoundPacket packet, @NotNull IPayloadContext context) {
        Item item;
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer localPlayer = minecraft.player;
        ClientLevel clientLevel = minecraft.level;
        if (localPlayer == null || clientLevel == null) {
            return;
        }
        ItemStack itemStack = packet.itemStack();
        if (itemStack.isEmpty() || !((item = itemStack.getItem()) instanceof GunItem)) {
            return;
        }
        GunItem gunItem = (GunItem)item;
        int n = packet.entityID();
        if (n != 0 && !packet.force() && localPlayer.getId() == n) {
            return;
        }
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        ClientPlayerDataHandler clientPlayerDataHandler = (ClientPlayerDataHandler)bFClientManager.getPlayerDataHandler();
        GunUtils.method_1422(minecraft, bFClientManager, clientPlayerDataHandler, localPlayer, clientLevel, itemStack, gunItem, packet.position(), false, packet.lastRound(), packet.decoy(), n);
    }

    public static void bulletCollisionLivingClient(@NotNull BFBulletCollisionLivingClientPacket packet, @NotNull IPayloadContext context) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel clientLevel = minecraft.level;
        LocalPlayer localPlayer = minecraft.player;
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        if (clientLevel == null || localPlayer == null) {
            return;
        }
        for (LivingBulletCollision livingBulletCollision : packet.entityBulletHit()) {
            BFClientManager bFClientManager = BFClientManager.getInstance();
            assert (bFClientManager != null) : "Client mod manager is null!";
            Entity entity = clientLevel.getEntity(livingBulletCollision.entityId());
            if (!(entity instanceof LivingEntity)) continue;
            LivingEntity livingEntity = (LivingEntity)entity;
            BulletUtils.method_155(minecraft, bFClientManager, clientLevel, clientLevel.getRandom(), threadLocalRandom, (LivingEntity)localPlayer, livingEntity, livingBulletCollision.hitVec(), livingBulletCollision.direction(), livingBulletCollision.headShot());
        }
    }

    public static void bulletCollisionBlockClient(@NotNull BFBulletCollisionBlockClientPacket packet, @NotNull IPayloadContext context) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel clientLevel = minecraft.level;
        LocalPlayer localPlayer = minecraft.player;
        if (clientLevel == null || localPlayer == null) {
            return;
        }
        int n = packet.entityID();
        if (n == localPlayer.getId()) {
            return;
        }
        Entity entity = clientLevel.getEntity(n);
        if (entity != null || n == 0) {
            LivingEntity livingEntity;
            BFClientManager bFClientManager = BFClientManager.getInstance();
            assert (bFClientManager != null) : "Client mod manager is null!";
            LivingEntity livingEntity2 = entity instanceof LivingEntity ? (livingEntity = (LivingEntity)entity) : null;
            livingEntity = clientLevel.getRandom();
            for (BlockBulletCollision blockBulletCollision : packet.blockBulletHits()) {
                BulletUtils.doBlockBulletImpact(minecraft, bFClientManager, clientLevel, (RandomSource)livingEntity, localPlayer, livingEntity2, blockBulletCollision, packet.entityID());
            }
        }
    }

    public static void grenadeFlash(@NotNull BFGrenadeFlashPacket packet, @NotNull IPayloadContext context) {
        PlayerTickable.FLASH_TIMER = packet.flashTime();
        Minecraft minecraft = Minecraft.getInstance();
        SoundManager soundManager = minecraft.getSoundManager();
        soundManager.stop();
        soundManager.play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.ITEM_GRENADE_FLASH_BLIND.get()), (float)1.0f));
    }

    public static void sound(@NotNull BFSoundPacket packet, @NotNull IPayloadContext context) {
        SimpleSoundInstance simpleSoundInstance = new SimpleSoundInstance(((SoundEvent)packet.sound().value()).getLocation(), packet.soundSource(), packet.volume(), packet.pitch(), SoundInstance.createUnseededRandom(), false, 0, SoundInstance.Attenuation.NONE, 0.0, 0.0, 0.0, true);
        Minecraft.getInstance().getSoundManager().play((SoundInstance)simpleSoundInstance);
    }

    public static void soundPosition(@NotNull BFSoundPositionPacket packet, @NotNull IPayloadContext context) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel clientLevel = minecraft.level;
        if (clientLevel != null) {
            Vec3 vec3 = packet.position();
            clientLevel.playLocalSound(vec3.x, vec3.y, vec3.z, (SoundEvent)packet.sound().value(), packet.soundSource(), packet.volume(), packet.pitch(), false);
        }
    }

    public static void clientNotification(@NotNull BFClientNotificationPacket packet, @NotNull IPayloadContext context) {
        BFNotification.show(Minecraft.getInstance(), packet.title(), packet.subMessage(), packet.toastType(), true);
    }

    public static void popupMessage(@NotNull BFPopupMessagePacket packet, @NotNull IPayloadContext context) {
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        AbstractGame<?, ?, ?> abstractGame = bFClientManager.getGame();
        if (abstractGame == null) {
            return;
        }
        AbstractGameClient<?, ?> abstractGameClient = bFClientManager.getGameClient();
        if (abstractGameClient != null) {
            abstractGameClient.addPopup(packet.gamePopups());
        }
    }

    public static void bulletTracerToClient(@NotNull BFBulletTracerToClientPacket packet, @NotNull IPayloadContext context) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer localPlayer = minecraft.player;
        if (localPlayer == null || localPlayer.getId() == packet.entityId()) {
            return;
        }
        BFClientTickSubscriber.BULLETS.add(packet.bulletTracer());
    }

    public static void spawnCorpse(@NotNull BFSpawnCorpsePacket packet, @NotNull IPayloadContext context) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel clientLevel = minecraft.level;
        LocalPlayer localPlayer = minecraft.player;
        if (clientLevel == null || localPlayer == null) {
            return;
        }
        Entity entity = clientLevel.getEntity(packet.playerId());
        if (!(entity instanceof AbstractClientPlayer) && !(entity instanceof BotEntity)) {
            return;
        }
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Mod manager is null!";
        bFClientManager.getCorpseManager().spawnCorpse(minecraft, clientLevel, localPlayer, (LivingEntity)entity, packet.context(), ThreadLocalRandom.current());
    }

    public static void worldFlash(@NotNull BFWorldFlashPacket packet, @NotNull IPayloadContext context) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel clientLevel = minecraft.level;
        if (clientLevel == null) {
            return;
        }
        clientLevel.setSkyFlashTime(packet.flashTime());
    }

    public static void radioPoint(@NotNull BFRadioPointPacket packet, @NotNull IPayloadContext context) {
        GameEventType gameEventType;
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        AbstractGame<?, ?, ?> abstractGame = bFClientManager.getGame();
        if (abstractGame == null) {
            return;
        }
        AbstractGameClient<?, ?> abstractGameClient = bFClientManager.getGameClient();
        if (abstractGameClient == null) {
            return;
        }
        int n = 5;
        List<GameRadioPoint> list = abstractGameClient.getRadioPoints();
        if (list.size() >= 5) {
            list.removeFirst();
        }
        if ((gameEventType = packet.gameEventType()).isWaypoint()) {
            abstractGameClient.method_5959();
        }
        abstractGameClient.addRadioPoint(new GameRadioPoint(packet.icon(), packet.time(), packet.position(), gameEventType));
    }

    public static void entitySound(@NotNull BFEntitySoundPacket packet, @NotNull IPayloadContext context) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel clientLevel = minecraft.level;
        if (clientLevel == null) {
            return;
        }
        Entity entity = clientLevel.getEntity(packet.livingEntityID());
        if (entity instanceof BotEntity && !BFClientSettings.AUDIO_BOTS.isEnabled()) {
            return;
        }
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)entity;
            EntitySoundInstance<LivingEntity> entitySoundInstance = new EntitySoundInstance<LivingEntity>(livingEntity, (SoundEvent)packet.soundEvent().value(), packet.soundSource(), packet.pitch(), packet.volume());
            minecraft.getSoundManager().play(entitySoundInstance);
        }
    }

    public static void explosion(@NotNull BFExplosionPacket packet, @NotNull IPayloadContext context) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel clientLevel = minecraft.level;
        if (clientLevel != null) {
            BFClientManager bFClientManager = BFClientManager.getInstance();
            assert (bFClientManager != null) : "Client mod manager is null!";
            ExplosionUtils.explode(minecraft, bFClientManager, clientLevel, packet.explosionType(), packet.position());
        }
    }

    public static void crateEntity(@NotNull BFCrateEntityPacket packet, @NotNull IPayloadContext context) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel clientLevel = minecraft.level;
        if (clientLevel == null) {
            return;
        }
        BlockEntity blockEntity = clientLevel.getBlockEntity(packet.blockPos());
        if (blockEntity instanceof AbstractCrateGunBlockEntity) {
            AbstractCrateGunBlockEntity abstractCrateGunBlockEntity = (AbstractCrateGunBlockEntity)blockEntity;
            abstractCrateGunBlockEntity.putItem(packet.itemStack());
        }
    }

    public static void calloutWaypoint(@NotNull BFCalloutWaypointPacket packet, @NotNull IPayloadContext context) {
        MatchCallout matchCallout = packet.calloutType();
        UUID uUID = packet.playerUUID();
        if (matchCallout.getWaypoint() == null) {
            return;
        }
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        ClientPlayerDataHandler clientPlayerDataHandler = (ClientPlayerDataHandler)bFClientManager.getPlayerDataHandler();
        ((BFClientPlayerData)clientPlayerDataHandler.getPlayerData(uUID)).setCurrentCallout(matchCallout);
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel clientLevel = minecraft.level;
        if (clientLevel == null) {
            return;
        }
        Player player = clientLevel.getPlayerByUUID(uUID);
        if (player != null) {
            Vec3 vec3 = player.position();
            SoundManager soundManager = minecraft.getSoundManager();
            Holder.Reference<SoundEvent> reference = matchCallout.getSoundEvent();
            if (reference != null) {
                SimpleSoundInstance simpleSoundInstance = new SimpleSoundInstance((SoundEvent)reference.value(), SoundSource.NEUTRAL, 12.0f, 2.0f, clientLevel.getRandom(), vec3.x, vec3.y, vec3.z);
                soundManager.play((SoundInstance)simpleSoundInstance);
            }
        }
    }

    public static void clearRagdolls(@NotNull BFClearRagdollsPacket packet, @NotNull IPayloadContext context) {
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        bFClientManager.getCorpseManager().clear();
    }

    public static void capturePointSpawnError(@NotNull BFCapturePointSpawnErrorPacket packet, @NotNull IPayloadContext context) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.GUI_LOBBY_CANCEL.get()), (float)1.0f, (float)1.0f));
        Screen screen = minecraft.screen;
        if (screen instanceof AbstractMatchSelectClassScreen) {
            AbstractMatchSelectClassScreen abstractMatchSelectClassScreen = (AbstractMatchSelectClassScreen)screen;
            abstractMatchSelectClassScreen.showErrorMessage(packet.message());
        } else {
            minecraft.gui.getChat().addMessage(packet.message());
        }
    }

    public static void chatGraphic(@NotNull BFChatGraphicPacket packet, @NotNull IPayloadContext context) {
        ChatGraphic chatGraphic = packet.graphic();
        Component component = packet.component();
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        LocalPlayer localPlayer = minecraft.player;
        if (localPlayer == null) {
            return;
        }
        MutableComponent mutableComponent = Component.empty();
        mutableComponent.append(String.valueOf(chatGraphic.getCharacter())).withColor(0xFFFFFF);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" ".repeat(Math.max(0, chatGraphic.getWidth())));
        int n = minecraft.gui.getChat().getWidth() - font.width(stringBuilder.toString()) - 5;
        ObjectList<String> objectList = FormatUtils.parseMarkup(font, component.getString(), n);
        int n2 = Math.max(chatGraphic.getHeight(), objectList.size());
        mutableComponent.append(String.valueOf('\n'));
        for (int i = 0; i < n2; ++i) {
            if (i > 0) {
                mutableComponent.append(String.valueOf('\n'));
            }
            mutableComponent.append(stringBuilder.toString());
            if (i >= objectList.size()) continue;
            mutableComponent.append((Component)Component.literal((String)((String)objectList.get(i))).withStyle(component.getStyle()));
        }
        localPlayer.sendSystemMessage((Component)mutableComponent);
    }

    public static void shakeNode(@NotNull BFShakeNodePacket packet, @NotNull IPayloadContext context) {
        ShakeManager.applyShake(packet.node());
    }

    public static void positionedShakeNode(@NotNull BFPositionedShakeNodePacket packet, @NotNull IPayloadContext context) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer localPlayer = minecraft.player;
        if (localPlayer == null) {
            return;
        }
        ShakeManager.applyShake(packet.data(), localPlayer, new Vec3(packet.position()), packet.radius());
    }

    public static void vehicleFireEffect(@NotNull BFVehicleFireEffectPacket packet, @NotNull IPayloadContext context) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel clientLevel = minecraft.level;
        if (clientLevel == null) {
            return;
        }
        Entity entity = clientLevel.getEntity(packet.entityId());
        if (entity instanceof AbstractVehicleEntity) {
            AbstractVehicleEntity abstractVehicleEntity = (AbstractVehicleEntity)entity;
            abstractVehicleEntity.method_2354(packet.partName());
        }
    }

    public static void hitMarker(@NotNull BFHitMarkerPacket packet, @NotNull IPayloadContext context) {
        if (!BFClientSettings.CROSSHAIR_HITMARKER.isEnabled()) {
            return;
        }
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        Minecraft minecraft = Minecraft.getInstance();
        SoundManager soundManager = minecraft.getSoundManager();
        AbstractGame<?, ?, ?> abstractGame = bFClientManager.getGame();
        if (abstractGame == null) {
            return;
        }
        if (CrosshairGuiLayer.hitMarkerTimer == 0) {
            for (int i = 0; i < 3; ++i) {
                soundManager.playDelayed((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.MISC_HITMARKER.get()), (float)1.0f, (float)6.0f), 2);
            }
        }
        CrosshairGuiLayer.hitMarkerTimer = 2;
        CrosshairGuiLayer.isKill = packet.isKill();
    }

    public static void debugLine(@NotNull BFDebugLinePacket packet, @NotNull IPayloadContext context) {
        BFClientTickSubscriber.DEBUG_LINES.add(packet.debugLine());
    }

    public static void debugBox(@NotNull BFDebugBoxPacket packet, @NotNull IPayloadContext context) {
        BFClientTickSubscriber.DEBUG_BOXES.add(packet.debugBox());
    }

    public static void damageIndicator(@NotNull BFDamageIndicatorPacket packet, @NotNull IPayloadContext context) {
        HealthEffectsGuiLayer.DAMAGE_INDICATORS.add((Object)new HealthEffectsGuiLayer.DamageIndicator(packet.alpha(), packet.position()));
    }

    public static void bulletDamageEffects(@NotNull BFBulletDamageEffectsPacket packet, @NotNull IPayloadContext context) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel clientLevel = minecraft.level;
        LocalPlayer localPlayer = minecraft.player;
        if (clientLevel == null || localPlayer == null) {
            return;
        }
        Vec3 vec3 = localPlayer.getEyePosition();
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        float f = 0.9f + threadLocalRandom.nextFloat() * 0.2f;
        for (int i = 0; i < 2; ++i) {
            clientLevel.playLocalSound(vec3.x + 2.0, vec3.y - 1.0, vec3.z - 2.0, (SoundEvent)BFSounds.ITEM_GUN_BULLET_IMPACT_FLESH_LOCAL.get(), SoundSource.BLOCKS, 15.0f, f, false);
            clientLevel.playLocalSound(vec3.x - 2.0, vec3.y - 1.0, vec3.z + 2.0, (SoundEvent)BFSounds.ITEM_GUN_BULLET_IMPACT_FLESH_LOCAL.get(), SoundSource.BLOCKS, 15.0f, f, false);
        }
        BF_39.field_187 = 1.0f;
        ShakeManager.applyShake(ShakeNodePresets.BULLET_DAMAGE);
    }

    public static void screenshotRequest(@NotNull BFScreenshotRequestPacket bFScreenshotRequestPacket, @NotNull IPayloadContext iPayloadContext) {
        if (!BFClientSettings.INTRO_RULES.isEnabled()) {
            BFLog.log("[Screenshot] Received screenshot request from server, but the player has not accepted the rules! Ignoring...", new Object[0]);
            BFLog.log("[Screenshot] More information can be found here: 'https://www.blockfrontmc.com/privacy'.", new Object[0]);
            return;
        }
        if (EnvironmentUtils.isClient()) {
            BFLog.log("[Screenshot] Received screenshot request from server! Initializing...", new Object[0]);
            BFLog.log("[Screenshot] More information can be found here: 'https://www.blockfrontmc.com/privacy'.", new Object[0]);
            BFClientManager bFClientManager = BFClientManager.getInstance();
            assert (bFClientManager != null) : "Client mod manager is null!";
            BFClientAntiCheat bFClientAntiCheat = (BFClientAntiCheat)bFClientManager.getAntiCheat();
            ((BFClientScreenshot)bFClientAntiCheat.getScreenshotManager()).method_3932();
        }
    }

    public static void screenshotDisplay(@NotNull BFScreenshotDisplayPacket bFScreenshotDisplayPacket, @NotNull IPayloadContext iPayloadContext) {
        Minecraft minecraft = Minecraft.getInstance();
        try {
            ByteBuf byteBuf = Unpooled.wrappedBuffer((byte[])bFScreenshotDisplayPacket.bytes());
            ByteBufInputStream byteBufInputStream = new ByteBufInputStream(byteBuf);
            NativeImage nativeImage = NativeImage.read((InputStream)byteBufInputStream);
            minecraft.setScreen((Screen)new BFScreenshotScreen(nativeImage));
        }
        catch (IOException iOException) {
            minecraft.gui.getChat().addMessage(SCREENSHOT_REQUEST_ERROR);
            BFLog.logThrowable("Error while receiving screenshot.", iOException, new Object[0]);
        }
    }

    public static void queueMusic(BFQueueMusicPacket bFQueueMusicPacket, @NotNull IPayloadContext iPayloadContext) {
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        Minecraft minecraft = Minecraft.getInstance();
        bFQueueMusicPacket.queuedMusicBuilder().method_1537(bFClientManager.getMusicManager(), minecraft);
    }

    public static void sequence(BFSequencePacket bFSequencePacket, @NotNull IPayloadContext iPayloadContext) {
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        Minecraft minecraft = Minecraft.getInstance();
        GameSequence gameSequence = bFSequencePacket.sequence();
        if (gameSequence != null) {
            bFClientManager.getCinematics().method_2201(minecraft, gameSequence, bFSequencePacket.settings().orElse(null));
        }
    }

    public static void sequencePositionUpdated(BFSequencePositionUpdatedPacket bFSequencePositionUpdatedPacket, @NotNull IPayloadContext iPayloadContext) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel clientLevel = minecraft.level;
        LocalPlayer localPlayer = minecraft.player;
        if (clientLevel == null || localPlayer == null) {
            return;
        }
        BlockPos blockPos = BlockPos.containing((Position)minecraft.gameRenderer.getMainCamera().getPosition());
        ChunkPos chunkPos = new ChunkPos(blockPos);
        LevelRenderer levelRenderer = minecraft.levelRenderer;
        int n = (Integer)minecraft.options.renderDistance().get();
        for (int i = -n; i <= n; ++i) {
            for (int j = -n; j <= n; ++j) {
                int n2 = chunkPos.x + i;
                int n3 = chunkPos.z + j;
                for (int k = clientLevel.getMinSection(); k < clientLevel.getMaxSection(); ++k) {
                    levelRenderer.setSectionDirtyWithNeighbors(n2, k, n3);
                }
            }
        }
        levelRenderer.allChanged();
    }

    public static void victory(BFVictoryPacket bFVictoryPacket, @NotNull IPayloadContext iPayloadContext) {
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        AbstractGame<?, ?, ?> abstractGame = bFClientManager.getGame();
        if (abstractGame != null) {
            bFClientManager.getCinematics().method_2205(new BF_553(abstractGame, bFVictoryPacket.subtitle(), bFVictoryPacket.victory()));
        }
    }

    public static void gameMapVoteInfo(BFGameMapVoteInfoPacket bFGameMapVoteInfoPacket, IPayloadContext iPayloadContext) {
        try {
            BFClientManager bFClientManager = BFClientManager.getInstance();
            assert (bFClientManager != null) : "Client mod manager is null!";
            AbstractGame<?, ?, ?> abstractGame = bFClientManager.getGame();
            if (abstractGame == null) {
                return;
            }
            ByteBuf byteBuf = bFGameMapVoteInfoPacket.method_5538();
            abstractGame.getMapVoteManager().read(byteBuf);
            bFGameMapVoteInfoPacket.method_5540();
        }
        catch (Exception exception) {
            BFLog.logError("Failed to handle map vote info packet", exception);
        }
    }

    public static void cinematicEffectFlash(BFCinematicEffectFlashPcket bFCinematicEffectFlashPcket, @NotNull IPayloadContext iPayloadContext) {
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        bFClientManager.getCinematics().method_2198(new BF_1179(bFCinematicEffectFlashPcket.color(), bFCinematicEffectFlashPcket.speed(), bFCinematicEffectFlashPcket.speedFlipped(), bFCinematicEffectFlashPcket.targetAlpha()));
    }
}

