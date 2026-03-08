/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.util;

import com.boehmod.bflib.cloud.common.ChatGraphic;
import com.boehmod.bflib.cloud.common.player.PlayerRank;
import com.boehmod.bflib.cloud.common.player.achievement.CloudAchievement;
import com.boehmod.bflib.cloud.common.player.achievement.CloudAchievements;
import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.cloud.packet.common.server.PacketPlayerDataIncreaseValue;
import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.bflib.common.util.ModLibraryMathHelper;
import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.client.gui.BFNotificationType;
import com.boehmod.blockfront.cloud.common.AbstractConnectionManager;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.common.entity.BFProjectileEntity;
import com.boehmod.blockfront.common.entity.BombEntity;
import com.boehmod.blockfront.common.entity.BotEntity;
import com.boehmod.blockfront.common.entity.GrenadeEntity;
import com.boehmod.blockfront.common.entity.HumanEntity;
import com.boehmod.blockfront.common.entity.InfectedEntity;
import com.boehmod.blockfront.common.entity.NextbotEntity;
import com.boehmod.blockfront.common.entity.VendorEntity;
import com.boehmod.blockfront.common.gun.GunMagType;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.match.DivisionData;
import com.boehmod.blockfront.common.match.Loadout;
import com.boehmod.blockfront.common.match.MatchClass;
import com.boehmod.blockfront.common.match.kill.KillFeedEntry;
import com.boehmod.blockfront.common.match.kill.KillMessage;
import com.boehmod.blockfront.common.net.BFPopup;
import com.boehmod.blockfront.common.net.packet.BFChatGraphicPacket;
import com.boehmod.blockfront.common.net.packet.BFClearRagdollsPacket;
import com.boehmod.blockfront.common.net.packet.BFClientNotificationPacket;
import com.boehmod.blockfront.common.net.packet.BFGameKillFeedPacket;
import com.boehmod.blockfront.common.net.packet.BFGameKilledMessagePacket;
import com.boehmod.blockfront.common.net.packet.BFPopupMessagePacket;
import com.boehmod.blockfront.common.net.packet.BFQueueMusicPacket;
import com.boehmod.blockfront.common.net.packet.BFSequencePacket;
import com.boehmod.blockfront.common.net.packet.BFSoundPacket;
import com.boehmod.blockfront.common.net.packet.BFSoundPositionPacket;
import com.boehmod.blockfront.common.player.BFAbstractPlayerData;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.common.player.a;
import com.boehmod.blockfront.common.stat.BFAudibleStat;
import com.boehmod.blockfront.common.stat.BFStat;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.common.world.damage.GrenadeDamageSource;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameMusic;
import com.boehmod.blockfront.game.GameSequence;
import com.boehmod.blockfront.game.GameSequenceSettings;
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.tag.IHasBots;
import com.boehmod.blockfront.registry.BFEntityTypes;
import com.boehmod.blockfront.server.BFServerManager;
import com.boehmod.blockfront.server.player.BFChatFilter;
import com.boehmod.blockfront.server.player.BFServerPlayerData;
import com.boehmod.blockfront.server.player.ServerPlayerDataHandler;
import com.boehmod.blockfront.util.EnvironmentUtils;
import com.boehmod.blockfront.util.PacketUtils;
import com.boehmod.blockfront.util.RandomUtils;
import com.boehmod.blockfront.util.StringUtils;
import com.boehmod.blockfront.util.math.FDSPose;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Camera;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

public final class BFUtils {
    @NotNull
    public static final String field_6928 = "matchId";
    @NotNull
    public static final String field_6929 = "disableMount";
    @NotNull
    public static final Component OBJECTIVE_PREFIX = Component.literal((String)String.valueOf('\ue008')).withColor(0xFFFFFF).append(" ");
    @NotNull
    public static final Component OBJECTIVE_UP_PREFIX = Component.literal((String)String.valueOf('\ue010')).withColor(0xFFFFFF).append(" ");
    @NotNull
    public static final Component COMMAND_PREFIX = Component.literal((String)String.valueOf('\ue013')).withColor(0xFFFFFF).append(" ");
    @NotNull
    public static final Component ADMIN_PREFIX = Component.translatable((String)"bf.message.admin").withColor(0xFFFFFF).append(" ");
    @NotNull
    public static final Component DEAD_PREFIX = Component.translatable((String)"bf.message.dead").withStyle(ChatFormatting.DARK_RED);
    @NotNull
    public static final Component TEAM_PREFIX = Component.translatable((String)"bf.message.team").withStyle(ChatFormatting.GRAY);
    private static final int field_2957 = 4;
    @NotNull
    public static final String TEAM_TYPING_PREFIX = "[Team]";

    public static boolean canSeeSky(@NotNull BlockAndTintGetter view, @NotNull BlockPos blockPos) {
        float f = 0.0f;
        float f2 = 0.0f;
        for (int i = -4; i < 4; ++i) {
            for (int j = -4; j < 4; ++j) {
                BlockPos blockPos2 = blockPos.offset(i, 2, j);
                if (!view.getBlockState(blockPos2).isAir()) continue;
                if (view.canSeeSky(blockPos2)) {
                    f2 += 1.0f;
                }
                f += 1.0f;
            }
        }
        return f2 / f <= 0.5f;
    }

    public static void spawnLightningBolt(@NotNull Level level, @NotNull Vec3 pos) {
        LightningBolt lightningBolt = (LightningBolt)EntityType.LIGHTNING_BOLT.create(level);
        if (lightningBolt == null) {
            return;
        }
        lightningBolt.setPos(pos);
        lightningBolt.moveTo(pos.x, pos.y, pos.z, 0.0f, 0.0f);
        lightningBolt.setVisualOnly(true);
        lightningBolt.setDamage(0.0f);
        level.addFreshEntity((Entity)lightningBolt);
    }

    public static void sendSequencePacket(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull GameSequence sequence, @Nullable GameSequenceSettings musicData, @NotNull Set<UUID> players) {
        players.forEach(uuid -> {
            ServerPlayer serverPlayer = BFUtils.getPlayerByUUID(uuid);
            if (serverPlayer != null) {
                BFUtils.sendSequencePacket(manager, sequence, musicData, serverPlayer);
            }
        });
    }

    public static void sendSequencePacket(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull GameSequence sequence, @Nullable GameSequenceSettings musicData, @NotNull UUID player) {
        ServerPlayer serverPlayer = BFUtils.getPlayerByUUID(player);
        if (serverPlayer != null) {
            BFUtils.sendSequencePacket(manager, sequence, musicData, serverPlayer);
        }
    }

    public static void sendSequencePacket(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull GameSequence sequence, @Nullable GameSequenceSettings musicData, @NotNull ServerPlayer player) {
        Object obj = manager.getPlayerDataHandler();
        Object d = ((PlayerDataHandler)obj).getPlayerData((Player)player);
        if (d instanceof BFServerPlayerData) {
            BFServerPlayerData bFServerPlayerData = (BFServerPlayerData)d;
            bFServerPlayerData.getSequenceManager().setCurrentSequence(sequence);
        }
        PacketUtils.sendToPlayer(new BFSequencePacket(sequence, Optional.ofNullable(musicData)), player);
    }

    public static void discardPlayerGrenades(@NotNull ServerLevel level, @NotNull AbstractGame<?, ?, ?> game, @NotNull AbstractGamePlayerManager<?> manager, @NotNull ServerPlayer player) {
        FDSPose fDSPose = manager.getLobbySpawn();
        if (fDSPose == null) {
            return;
        }
        BlockPos blockPos = fDSPose.asBlockPos();
        AABB aABB = new AABB(blockPos).inflate((double)game.entityBoundSize());
        List list = level.getEntitiesOfClass(GrenadeEntity.class, aABB);
        for (GrenadeEntity grenadeEntity : list) {
            if (!player.equals((Object)grenadeEntity.getOwner())) continue;
            grenadeEntity.discard();
        }
    }

    public static void discardMatchEntities(@Nullable ServerLevel level, @NotNull AbstractGame<?, ?, ?> game, @NotNull AbstractGamePlayerManager<?> playerHandler) {
        ObjectArrayList objectArrayList = new ObjectArrayList();
        if (level != null) {
            FDSPose fDSPose = playerHandler.getLobbySpawn();
            if (fDSPose == null) {
                return;
            }
            BlockPos blockPos = fDSPose.asBlockPos();
            AABB aABB = new AABB(blockPos).inflate((double)game.entityBoundSize());
            objectArrayList.addAll(level.getEntitiesOfClass(ItemEntity.class, aABB));
            objectArrayList.addAll(level.getEntitiesOfClass(BFProjectileEntity.class, aABB));
            objectArrayList.addAll(level.getEntitiesOfClass(HumanEntity.class, aABB));
            objectArrayList.addAll(level.getEntitiesOfClass(BotEntity.class, aABB));
            objectArrayList.addAll(level.getEntitiesOfClass(InfectedEntity.class, aABB));
            objectArrayList.addAll(level.getEntitiesOfClass(VendorEntity.class, aABB));
            objectArrayList.addAll(level.getEntitiesOfClass(Horse.class, aABB));
            objectArrayList.addAll(level.getEntitiesOfClass(NextbotEntity.class, aABB));
            objectArrayList.addAll(level.getEntitiesOfClass(BombEntity.class, aABB));
            objectArrayList.addAll(level.getEntitiesOfClass(GrenadeEntity.class, aABB));
            List list = level.getEntitiesOfClass(AbstractVehicleEntity.class, aABB);
            list.forEach(Entity::ejectPassengers);
            objectArrayList.addAll(list);
        }
        if (!objectArrayList.isEmpty()) {
            objectArrayList.forEach(Entity::discard);
        }
    }

    public static void clearInventories(@NotNull Set<UUID> players) {
        players.forEach(BFUtils::clearInventory);
    }

    public static void clearInventory(@NotNull UUID player) {
        ServerPlayer serverPlayer = BFUtils.getPlayerByUUID(player);
        if (serverPlayer == null) {
            return;
        }
        serverPlayer.getInventory().clearContent();
        serverPlayer.containerMenu.broadcastChanges();
    }

    public static void queueMusic(@NotNull Set<UUID> players, @NotNull GameMusic music) {
        players.forEach(uuid -> BFUtils.queueMusic(uuid, music));
    }

    public static void queueMusic(@NotNull UUID player, @NotNull GameMusic music) {
        ServerPlayer serverPlayer = BFUtils.getPlayerByUUID(player);
        if (serverPlayer == null) {
            return;
        }
        BFQueueMusicPacket bFQueueMusicPacket = new BFQueueMusicPacket(music);
        PacketUtils.sendToPlayer(bFQueueMusicPacket, serverPlayer);
    }

    public static void playSound(@NotNull UUID uuid, @NotNull SoundEvent sound, @NotNull SoundSource source) {
        BFUtils.playSound(uuid, sound, source, 1.0f, 1.0f);
    }

    public static void playSound(@NotNull ServerPlayer player, @NotNull SoundEvent sound, @NotNull SoundSource source) {
        BFUtils.playSound(player, sound, source, 1.0f, 1.0f);
    }

    public static void playSound(@NotNull ServerPlayer player, @NotNull SoundEvent sound, @NotNull SoundSource source, float volume, float pitch) {
        BFSoundPacket bFSoundPacket = new BFSoundPacket((Holder<SoundEvent>)BuiltInRegistries.SOUND_EVENT.wrapAsHolder((Object)sound), source, volume, pitch);
        PacketUtils.sendToPlayer(bFSoundPacket, player);
    }

    public static void playSound(@NotNull ServerPlayer player, @NotNull SoundEvent sound, @NotNull SoundSource source, float volume) {
        BFUtils.playSound(player, sound, source, volume, 1.0f);
    }

    public static void playSound(@NotNull UUID uuid, @NotNull SoundEvent sound, @NotNull SoundSource source, float volume) {
        BFUtils.playSound(uuid, sound, source, volume, 1.0f);
    }

    public static void playSound(@NotNull UUID uuid, @NotNull SoundEvent sound, @NotNull SoundSource source, float volume, float pitch) {
        ServerPlayer serverPlayer = BFUtils.getPlayerByUUID(uuid);
        if (serverPlayer != null) {
            BFUtils.playSound(serverPlayer, sound, source, volume, pitch);
        }
    }

    public static void playSound(@NotNull Set<UUID> uuids, @NotNull SoundEvent sound, @NotNull SoundSource source) {
        uuids.forEach(uuid -> BFUtils.playSound(uuid, sound, source));
    }

    public static void playSound(Set<UUID> uuids, SoundEvent sound, SoundSource source, float pitch) {
        BFUtils.playSound(uuids, sound, source, pitch, 1.0f);
    }

    public static void playSound(@NotNull Set<UUID> uuids, SoundEvent sound, SoundSource source, float volume, float pitch) {
        uuids.forEach(uuid -> BFUtils.playSound(uuid, sound, source, volume, pitch));
    }

    public static void playPositionedSound(@NotNull ServerPlayer player, @NotNull SoundEvent sound, @NotNull SoundSource source, float volume, @NotNull Vec3 position) {
        PacketUtils.sendToPlayer(new BFSoundPositionPacket((Holder<SoundEvent>)BuiltInRegistries.SOUND_EVENT.wrapAsHolder((Object)sound), source, volume, position), player);
    }

    public static void playPositionedSound(@NotNull Set<UUID> players, @NotNull SoundEvent sound, @NotNull SoundSource source, float volume, @NotNull Vec3 position) {
        players.forEach(uuid -> BFUtils.playPositionedSound(uuid, sound, source, volume, position));
    }

    public static void playPositionedSound(@NotNull UUID player, @NotNull SoundEvent sound, @NotNull SoundSource source, float volume, @NotNull Vec3 position) {
        ServerPlayer serverPlayer = BFUtils.getPlayerByUUID(player);
        if (serverPlayer == null) {
            return;
        }
        BFUtils.playPositionedSound(serverPlayer, sound, source, volume, position);
    }

    public static void playSound(@NotNull Level level, @NotNull Vec3 pos, @NotNull SoundEvent sound, @NotNull SoundSource category, float volume, float pitch) {
        level.playSound(null, pos.x, pos.y, pos.z, sound, category, volume, pitch);
    }

    public static void playSound(@NotNull Level level, double x, double y, double z, @NotNull SoundEvent sound, @NotNull SoundSource source, float volume, float pitch) {
        level.playSound(null, x, y, z, sound, source, volume, pitch);
    }

    public static void sendPopupMessage(@NotNull ServerPlayer player, @NotNull BFPopup popup) {
        PacketUtils.sendToPlayer(new BFPopupMessagePacket(popup), player);
    }

    public static void sendPopupMessage(@NotNull UUID player, @NotNull BFPopup popup) {
        ServerPlayer serverPlayer = BFUtils.getPlayerByUUID(player);
        if (serverPlayer != null) {
            BFUtils.sendPopupMessage(serverPlayer, popup);
        }
    }

    public static void sendKilledMessagePacket(@NotNull ServerPlayer player, @NotNull KillMessage message) {
        PacketUtils.sendToPlayer(new BFGameKilledMessagePacket(message), player);
    }

    public static void broadcastKillFeedEntry(@NotNull AbstractGamePlayerManager<?> manager, @NotNull KillFeedEntry entry) {
        BFGameKillFeedPacket bFGameKillFeedPacket = new BFGameKillFeedPacket(entry);
        manager.getPlayerUUIDs().forEach(uuid -> {
            ServerPlayer serverPlayer = BFUtils.getPlayerByUUID(uuid);
            if (serverPlayer == null) {
                return;
            }
            PacketUtils.sendToPlayer(bFGameKillFeedPacket, serverPlayer);
        });
    }

    public static void initPlayersForGame(@NotNull ServerLevel level, @NotNull Set<UUID> players, @NotNull PlayerDataHandler<?> dataHandler) {
        players.forEach(uuid -> {
            ServerPlayer serverPlayer = BFUtils.getPlayerByUUID(uuid);
            if (serverPlayer != null) {
                BFUtils.initPlayerForGame(dataHandler, level, serverPlayer);
            }
        });
    }

    public static void initPlayerForGame(@NotNull PlayerDataHandler<?> dataHandler, @NotNull ServerLevel level, @NotNull ServerPlayer player) {
        if (level.isClientSide()) {
            return;
        }
        player.removeAllEffects();
        player.resetFallDistance();
        player.clearFire();
        player.setHealth(player.getMaxHealth());
        player.setInvisible(false);
        player.setGameMode(GameType.ADVENTURE);
        Object obj = dataHandler.getPlayerData((Player)player);
        ((BFAbstractPlayerData)obj).setOutOfGame(false);
        ((BFAbstractPlayerData)obj).setRespawnTimer(0);
        ((a)((BFAbstractPlayerData)obj).method_843()).method_820(false);
    }

    public static SortedSet<UUID> sortStatData(@NotNull AbstractGame<?, ?, ?> game, @NotNull Set<UUID> players, @NotNull BFStat stat) {
        return BFUtils.sortStatData(game, players, stat.getKey());
    }

    public static SortedSet<UUID> sortStatData(@NotNull AbstractGame<?, ?, ?> game, @NotNull Set<UUID> players, @NotNull String key) {
        if (players.size() <= 1) {
            return new ObjectLinkedOpenHashSet(players);
        }
        ObjectLinkedOpenHashSet objectLinkedOpenHashSet = new ObjectLinkedOpenHashSet();
        ObjectArrayList objectArrayList = new ObjectArrayList(players);
        while (!objectArrayList.isEmpty()) {
            UUID uUID = (UUID)objectArrayList.getFirst();
            for (UUID uUID2 : objectArrayList) {
                if (game.getPlayerStatData(uUID2).getInteger(key) <= game.getPlayerStatData(uUID).getInteger(key)) continue;
                uUID = uUID2;
            }
            objectLinkedOpenHashSet.add((Object)uUID);
            objectArrayList.remove((Object)uUID);
        }
        return objectLinkedOpenHashSet;
    }

    @NotNull
    public static ObjectSortedSet<UUID> topPlayers(@NotNull AbstractGame<?, ?, ?> game, int max, @NotNull BFStat stat, @NotNull Set<UUID> players) {
        return BFUtils.topPlayers(game, max, stat.getKey(), players);
    }

    @NotNull
    public static ObjectSortedSet<UUID> topPlayers(@NotNull AbstractGame<?, ?, ?> game, int max, @NotNull String key, @NotNull Set<UUID> players) {
        if (players.isEmpty() || max <= 0) {
            return new ObjectLinkedOpenHashSet();
        }
        SortedSet<UUID> sortedSet = BFUtils.sortStatData(game, players, key);
        ObjectLinkedOpenHashSet objectLinkedOpenHashSet = new ObjectLinkedOpenHashSet();
        int n = 0;
        for (UUID uUID : sortedSet) {
            if (n >= max) break;
            objectLinkedOpenHashSet.add((Object)uUID);
            ++n;
        }
        return objectLinkedOpenHashSet;
    }

    @NotNull
    public static ObjectSortedSet<UUID> topPlayers(@NotNull AbstractGame<?, ?, ?> game, int max, @NotNull BFStat stat, @NotNull GameTeam team) {
        return BFUtils.topPlayers(game, max, stat, team.getPlayers());
    }

    @NotNull
    public static Set<UUID> randomUUIDs(@NotNull Set<UUID> uuids, int amount) {
        if (uuids.isEmpty() || amount <= 0) {
            return Collections.emptySet();
        }
        ObjectArrayList objectArrayList = new ObjectArrayList(uuids);
        Collections.shuffle(objectArrayList);
        ObjectLinkedOpenHashSet objectLinkedOpenHashSet = new ObjectLinkedOpenHashSet();
        for (int i = 0; i < Math.min(amount, objectArrayList.size()); ++i) {
            objectLinkedOpenHashSet.add((Object)((UUID)objectArrayList.get(i)));
        }
        return objectLinkedOpenHashSet;
    }

    public static void method_2870(@NotNull PlayerDataHandler<?> playerDataHandler, @NotNull Set<UUID> set) {
        set.forEach(uUID -> {
            ServerPlayer serverPlayer = BFUtils.getPlayerByUUID(uUID);
            if (serverPlayer != null) {
                BFUtils.method_2876(playerDataHandler, serverPlayer);
            }
        });
    }

    public static void method_2977(@NotNull PlayerDataHandler<?> playerDataHandler, @NotNull Set<UUID> set) {
        set.forEach(uUID -> {
            ServerPlayer serverPlayer = BFUtils.getPlayerByUUID(uUID);
            if (serverPlayer != null) {
                BFUtils.method_2979(playerDataHandler, serverPlayer);
            }
        });
    }

    public static void method_2876(@NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerPlayer serverPlayer) {
        ((BFAbstractPlayerData)playerDataHandler.getPlayerData((Player)serverPlayer)).method_832(true);
    }

    public static void method_2979(@NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerPlayer serverPlayer) {
        Object obj = playerDataHandler.getPlayerData((Player)serverPlayer);
        if (((BFAbstractPlayerData)obj).method_849() || ((BFAbstractPlayerData)obj).getPose() != null) {
            ((BFAbstractPlayerData)obj).method_832(false);
            ((BFAbstractPlayerData)obj).setPose(null);
        }
    }

    public static void method_2919(@NotNull Set<UUID> set, @NotNull Component component) {
        set.forEach(uUID -> BFUtils.method_2929(uUID, component));
    }

    public static void method_2929(@NotNull UUID uUID, @NotNull Component component) {
        ServerPlayer serverPlayer = BFUtils.getPlayerByUUID(uUID);
        if (serverPlayer != null) {
            BFUtils.disconnectServerPlayer(serverPlayer, component);
        }
    }

    public static void disconnectServerPlayer(@NotNull ServerPlayer player, @NotNull Component reason) {
        player.connection.disconnect(reason);
    }

    @Nullable
    public static ServerPlayer getPlayerByUUID(@NotNull UUID uuid) {
        if (!EnvironmentUtils.isServer()) {
            return null;
        }
        MinecraftServer minecraftServer = EnvironmentUtils.getServer();
        if (minecraftServer == null) {
            return null;
        }
        return minecraftServer.getPlayerList().getPlayer(uuid);
    }

    public static void teleportToRandomPose(@NotNull PlayerDataHandler<?> dataHandler, @NotNull UUID player, @NotNull List<FDSPose> poses) {
        BFUtils.teleportPlayerAndSync(dataHandler, player, RandomUtils.randomFromList(poses));
    }

    public static void teleportPlayerAndSync(@NotNull PlayerDataHandler<?> dataHandler, @NotNull UUID uuid, @NotNull FDSPose pose) {
        ServerPlayer serverPlayer = BFUtils.getPlayerByUUID(uuid);
        if (serverPlayer != null) {
            BFUtils.teleportPlayer(dataHandler, serverPlayer, pose);
            serverPlayer.containerMenu.sendAllDataToRemote();
        }
    }

    public static void teleportPlayer(@NotNull PlayerDataHandler<?> dataHandler, @NotNull ServerPlayer player, @NotNull FDSPose pose) {
        player.teleportTo(player.serverLevel(), pose.position.x, pose.position.y, pose.position.z, pose.rotation.x, pose.rotation.y);
        Object obj = dataHandler.getPlayerData((Player)player);
        if (((BFAbstractPlayerData)obj).method_849()) {
            ((BFAbstractPlayerData)obj).setPose(pose);
        }
    }

    public static void sendChatGraphic(@NotNull ChatGraphic chatGraphic, @NotNull Set<UUID> players, @NotNull Component message) {
        players.forEach(uuid -> BFUtils.sendChatGraphic(chatGraphic, uuid, message));
    }

    public static void sendChatGraphic(@NotNull ChatGraphic chatGraphic, @NotNull UUID player, @NotNull Component message) {
        ServerPlayer serverPlayer = BFUtils.getPlayerByUUID(player);
        if (serverPlayer != null) {
            BFUtils.sendChatGraphic(chatGraphic, serverPlayer, message);
        }
    }

    public static void sendChatGraphic(@NotNull ChatGraphic chatGraphic, @NotNull ServerPlayer player, @NotNull Component message) {
        PacketUtils.sendToPlayer(new BFChatGraphicPacket(chatGraphic, message), player);
    }

    public static void sendNoticeMessage(@NotNull ServerPlayer player, @NotNull Component message) {
        BFUtils.sendFancyMessage(player, BFStats.NOTICE_PREFIX, message);
    }

    public static void sendNoticeMessage(@NotNull UUID uuid, @NotNull Component message) {
        BFUtils.sendFancyMessage(uuid, BFStats.NOTICE_PREFIX, message);
    }

    public static void sendFancyMessage(@NotNull UUID playerUUID, @NotNull Component header, @NotNull Component message) {
        ServerPlayer serverPlayer = BFUtils.getPlayerByUUID(playerUUID);
        if (serverPlayer == null) {
            return;
        }
        BFUtils.sendFancyMessage(serverPlayer, header, message);
    }

    public static void sendFancyMessage(@NotNull ServerPlayer player, @NotNull Component header, @NotNull Component message) {
        Style style;
        MutableComponent mutableComponent = Component.empty();
        if (header.getString().equalsIgnoreCase("Cloud")) {
            header = Component.literal((String)String.valueOf('\ue012'));
        }
        mutableComponent.append((Component)header.copy());
        StringBuilder stringBuilder = new StringBuilder();
        String string = "                                                                 ";
        if (message.getString().startsWith("/c")) {
            style = message.getStyle();
            int n = string.length() * 4;
            message = Component.literal((String)message.getString().replaceAll("/c", "").trim()).setStyle(style);
            int n2 = message.getString().length() * 5;
            int n3 = n / 2 - n2 / 2;
            int n4 = n3 / 5;
            stringBuilder.append(" ".repeat(Math.max(0, n4)));
        }
        style = mutableComponent.withStyle(ChatFormatting.GRAY);
        player.sendSystemMessage((Component)style.append((Component)Component.literal((String)stringBuilder.toString())).append(message));
    }

    public static void sendAdminMessage(@NotNull ServerLevel level, @NotNull BFServerManager manager, @NotNull Component message) {
        if (!EnvironmentUtils.isServer()) {
            return;
        }
        ServerPlayerDataHandler serverPlayerDataHandler = (ServerPlayerDataHandler)manager.getPlayerDataHandler();
        level.getServer().getPlayerList().getPlayers().stream().filter(player -> serverPlayerDataHandler.getCloudProfile((Player)player).hasPermission("staff.ingame.admin")).forEach(player -> BFUtils.sendNoticeMessage(player, message));
    }

    public static void sendNoticeIcon(@NotNull Set<UUID> players) {
        BFUtils.sendNoticeMessage(players, (Component)Component.empty());
    }

    public static void sendNoticeMessage(@NotNull Set<UUID> players, @NotNull Component message) {
        BFUtils.sendFancyMessage(players, BFStats.NOTICE_PREFIX, message);
    }

    public static void sendNoticeMessage(@NotNull Set<UUID> players, @NotNull String message) {
        BFUtils.sendFancyMessage(players, BFStats.NOTICE_PREFIX, (Component)Component.literal((String)message));
    }

    public static void sendFancyMessage(@NotNull Set<UUID> players, @NotNull Component prefix, @NotNull String message) {
        players.forEach(uuid -> {
            ServerPlayer serverPlayer = BFUtils.getPlayerByUUID(uuid);
            if (serverPlayer == null) {
                return;
            }
            BFUtils.sendFancyMessage(serverPlayer, prefix, (Component)Component.literal((String)message));
        });
    }

    public static void sendFancyMessage(@NotNull Set<UUID> players, @NotNull Component prefix, @NotNull Component message) {
        players.forEach(uuid -> {
            ServerPlayer serverPlayer = BFUtils.getPlayerByUUID(uuid);
            if (serverPlayer == null) {
                return;
            }
            BFUtils.sendFancyMessage(serverPlayer, prefix, message);
        });
    }

    public static void sendFancyMessage(@NotNull GameTeam team, @NotNull Component prefix, @NotNull Component message) {
        team.getPlayers().forEach(uuid -> {
            ServerPlayer serverPlayer = BFUtils.getPlayerByUUID(uuid);
            if (serverPlayer == null) {
                return;
            }
            BFUtils.sendFancyMessage(serverPlayer, prefix, message);
        });
    }

    public static void sendNoticeMessage(@NotNull GameTeam team, @NotNull Component message) {
        team.getPlayers().forEach(uuid -> {
            ServerPlayer serverPlayer = BFUtils.getPlayerByUUID(uuid);
            if (serverPlayer == null) {
                return;
            }
            BFUtils.sendNoticeMessage(serverPlayer, message);
        });
    }

    public static void sendNotification(@NotNull GameTeam team, @NotNull Component title, @NotNull Component message, @NotNull BFNotificationType type) {
        team.getPlayers().forEach(uuid -> BFUtils.sendNotification(uuid, title, message, type));
    }

    public static void sendNotification(@NotNull UUID uuid, @NotNull Component title, @NotNull Component message, @NotNull BFNotificationType type) {
        ServerPlayer serverPlayer = BFUtils.getPlayerByUUID(uuid);
        if (serverPlayer != null) {
            PacketUtils.sendToPlayer(new BFClientNotificationPacket(title, message, type), serverPlayer);
        }
    }

    public static int getPlayerStat(@NotNull AbstractGame<?, ?, ?> game, @NotNull UUID player, @NotNull BFStat stat) {
        return game.getPlayerStatData(player).getInteger(stat.getKey());
    }

    public static int getPlayerStat(@NotNull AbstractGame<?, ?, ?> game, @NotNull UUID player, @NotNull BFStat stat, int defaultValue) {
        return game.getPlayerStatData(player).getInteger(stat.getKey(), defaultValue);
    }

    public static void setPlayerStat(@NotNull AbstractGame<?, ?, ?> game, @NotNull UUID uuid, @NotNull BFStat stat, int value) {
        game.getPlayerStatData(uuid).setInteger(stat.getKey(), value);
    }

    public static void incrementPlayerStat(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull AbstractGame<?, ?, ?> game, @NotNull Set<UUID> players, @NotNull BFStat stat) {
        BFUtils.triggerPlayerStat(manager, game, players, stat, 1);
    }

    public static void triggerPlayerStat(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull AbstractGame<?, ?, ?> game, @NotNull Set<UUID> players, @NotNull BFStat stat, int change) {
        players.forEach(uuid -> BFUtils.triggerPlayerStat(manager, game, uuid, stat, change));
    }

    public static void incrementPlayerStat(BFAbstractManager<?, ?, ?> manager, @NotNull AbstractGame<?, ?, ?> game, @NotNull UUID player, @NotNull BFStat stat) {
        BFUtils.triggerPlayerStat(manager, game, player, stat, 1);
    }

    public static void triggerPlayerStat(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull AbstractGame<?, ?, ?> game, @NotNull UUID player, @NotNull BFStat stat, int change) {
        int n = BFUtils.getPlayerStat(game, player, stat);
        BFUtils.setPlayerStat(game, player, stat, n + change);
        if (stat instanceof BFAudibleStat) {
            BFAudibleStat bFAudibleStat = (BFAudibleStat)stat;
            bFAudibleStat.method_3918(player, change);
        }
        if (manager.isCloudConnectionVerified() && stat.method_3923()) {
            stat.trigger(manager, game, player, change, n + change);
        }
    }

    public static void decrementPlayerStat(@NotNull AbstractGame<?, ?, ?> game, @NotNull UUID player, @NotNull BFStat stat) {
        BFUtils.decreasePlayerStat(game, player, stat, 1);
    }

    public static void decreasePlayerStat(@NotNull AbstractGame<?, ?, ?> game, @NotNull UUID player, @NotNull BFStat stat, int change) {
        int n = BFUtils.getPlayerStat(game, player, stat);
        BFUtils.setPlayerStat(game, player, stat, n - change);
    }

    public static void method_2996(@NotNull Set<UUID> set) {
        set.forEach(uUID -> {
            ServerPlayer serverPlayer = BFUtils.getPlayerByUUID(uUID);
            if (serverPlayer != null) {
                PacketUtils.sendToPlayer(new BFClearRagdollsPacket(), serverPlayer);
            }
        });
    }

    public static void giveLoadout(@NotNull ServerLevel level, @NotNull ServerPlayer player, @NotNull Loadout loadout) {
        BFUtils.giveLoadout(level, player, loadout, false);
    }

    public static void giveLoadout(@NotNull ServerLevel level, @NotNull ServerPlayer player, @NotNull Loadout loadout, boolean keepUnset) {
        ItemStack itemStack;
        ItemStack itemStack2;
        ItemStack itemStack3;
        ItemStack itemStack4;
        if (level.isClientSide()) {
            return;
        }
        Inventory inventory = player.getInventory();
        if (!keepUnset) {
            inventory.clearContent();
        }
        int n = 3;
        ItemStack itemStack5 = loadout.getPrimary();
        if (itemStack5 != null && !itemStack5.isEmpty()) {
            inventory.setItem(0, itemStack5.copy());
        }
        if ((itemStack4 = loadout.getSecondary()) != null && !itemStack4.isEmpty()) {
            inventory.setItem(1, itemStack4.copy());
        }
        if ((itemStack3 = loadout.getMelee()) != null && !itemStack3.isEmpty()) {
            inventory.setItem(2, itemStack3.copy());
        }
        if ((itemStack2 = loadout.getOffHand()) != null && !itemStack2.isEmpty()) {
            player.setItemInHand(InteractionHand.OFF_HAND, itemStack2.copy());
        }
        if ((itemStack = loadout.getChest()) != null && !itemStack.isEmpty()) {
            EquipmentSlot equipmentSlot = player.getEquipmentSlotForItem(itemStack);
            player.setItemSlot(equipmentSlot, itemStack);
        }
        for (ItemStack itemStack6 : loadout.getExtra()) {
            if (itemStack6 == null || itemStack6.isEmpty()) continue;
            while (!inventory.getItem(n).isEmpty()) {
                ++n;
            }
            if (n > 6) break;
            inventory.setItem(n, itemStack6.copy());
        }
        for (ItemStack itemStack6 : loadout.getItemStackList()) {
            ArmorItem armorItem;
            ItemStack itemStack7 = itemStack6.copy();
            Item item = itemStack6.getItem();
            if (!(item instanceof ArmorItem) || (item = (armorItem = (ArmorItem)item).getEquipmentSlot(itemStack7)) == null) continue;
            player.setItemSlot((EquipmentSlot)item, itemStack6.copy());
        }
        player.containerMenu.broadcastChanges();
    }

    public static void setInventoryFromLoadout(@NotNull Level level, @NotNull LivingEntity entity, @NotNull Loadout loadout) {
        if (level.isClientSide()) {
            return;
        }
        if (loadout.getPrimary() != null) {
            entity.setItemInHand(InteractionHand.MAIN_HAND, loadout.getPrimary());
        }
        if (loadout.getSecondary() != null) {
            entity.setItemInHand(InteractionHand.MAIN_HAND, loadout.getSecondary());
        }
        if (loadout.getMelee() != null) {
            entity.setItemInHand(InteractionHand.MAIN_HAND, loadout.getMelee());
        }
        if (loadout.getOffHand() != null) {
            entity.setItemInHand(InteractionHand.OFF_HAND, loadout.getOffHand());
        }
        for (ItemStack itemStack : loadout.getItemStackList()) {
            ArmorItem armorItem;
            ItemStack itemStack2 = itemStack.copy();
            Item item = itemStack2.getItem();
            if (!(item instanceof ArmorItem) || (item = (armorItem = (ArmorItem)item).getEquipmentSlot(itemStack2)) == null) continue;
            switch (1.field_2958[item.ordinal()]) {
                case 1: {
                    entity.setItemSlot(EquipmentSlot.HEAD, itemStack2);
                    break;
                }
                case 2: {
                    entity.setItemSlot(EquipmentSlot.CHEST, itemStack2);
                    break;
                }
                case 3: {
                    entity.setItemSlot(EquipmentSlot.LEGS, itemStack2);
                    break;
                }
                case 4: {
                    entity.setItemSlot(EquipmentSlot.FEET, itemStack2);
                }
            }
        }
    }

    public static void broadcastPlayerPing(@NotNull ServerPlayer player) {
        player.server.getPlayerList().broadcastAll((Packet)new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LATENCY, player));
    }

    public static void handlePlayerRageQuit(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull PlayerDataHandler<?> dataHandler, @NotNull AbstractGame<?, ?, ?> game, @NotNull Set<UUID> players, @NotNull ServerPlayer player) {
        UUID uUID = player.getUUID();
        Object obj = dataHandler.getPlayerData((Player)player);
        long l = (Calendar.getInstance().getTime().getTime() - ((BFAbstractPlayerData)obj).method_852()) / 1000L;
        if (game.shouldAnnounceRageQuits() && l <= 5L && game.getStatus() == GameStatus.GAME && ((BFAbstractPlayerData)obj).method_852() != 0L) {
            MutableComponent mutableComponent = Component.literal((String)player.getScoreboardName()).withColor(0xFFFFFF);
            BFUtils.sendNoticeMessage(players, (Component)Component.translatable((String)"bf.message.gamemode.ragequit", (Object[])new Object[]{mutableComponent}).withStyle(ChatFormatting.RED));
            BFUtils.awardAchievement(manager, uUID, CloudAchievements.ACH_USER_RAGEQUIT);
        }
        ((BFAbstractPlayerData)obj).method_844(0L);
    }

    public static void awardAchievement(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull UUID uuid, @NotNull CloudAchievement achievement) {
        ((AbstractConnectionManager)manager.getConnectionManager()).sendPacket((IPacket)new PacketPlayerDataIncreaseValue(uuid, "achievement", achievement.getId()));
    }

    public static void method_2999(@NotNull ServerPlayer serverPlayer) {
        Inventory inventory = serverPlayer.getInventory();
        int n = inventory.items.size();
        for (int i = 0; i < n; ++i) {
            ItemStack itemStack = (ItemStack)inventory.items.get(i);
            if (itemStack.isEmpty() || !(itemStack.getItem() instanceof GunItem)) continue;
            GunItem.restockAmmo(itemStack);
            serverPlayer.containerMenu.sendAllDataToRemote();
        }
    }

    public static void method_2944(@NotNull ServerLevel serverLevel, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull Set<UUID> set) {
        set.forEach(uUID -> BFUtils.method_2945(serverLevel, abstractGame, uUID));
    }

    private static void method_2945(@NotNull ServerLevel serverLevel, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull UUID uUID) {
        GameTeam gameTeam = ((AbstractGamePlayerManager)abstractGame.getPlayerManager()).getPlayerTeam(uUID);
        FDSTagCompound fDSTagCompound = abstractGame.getPlayerStatData(uUID);
        int n = fDSTagCompound.getInteger(BFStats.CLASS.getKey(), -1);
        int n2 = fDSTagCompound.getInteger(BFStats.CLASS_INDEX.getKey(), -1);
        ServerPlayer serverPlayer = BFUtils.getPlayerByUUID(uUID);
        if (serverPlayer != null && gameTeam != null && n != -1 && n2 != -1) {
            MatchClass matchClass = MatchClass.values()[n];
            BFUtils.giveLoadout(serverLevel, serverPlayer, BFUtils.method_2898(abstractGame, gameTeam, matchClass, n2));
        }
    }

    public static Loadout method_2898(@NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull GameTeam gameTeam, @NotNull MatchClass matchClass, int n) {
        DivisionData divisionData = gameTeam.getDivisionData(abstractGame);
        return divisionData.getLoadout(matchClass, n);
    }

    public static int method_2899(@NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull MatchClass matchClass, @NotNull GameTeam gameTeam) {
        int n = 0;
        for (UUID uUID : gameTeam.getPlayers()) {
            int n2 = abstractGame.getPlayerStatData(uUID).getInteger(BFStats.CLASS.getKey());
            if (n2 != matchClass.ordinal()) continue;
            ++n;
        }
        return n;
    }

    public static void initPlayerForGame(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull PlayerDataHandler<?> dataHandler, @NotNull ServerLevel level, @NotNull ServerPlayer player) {
        UUID uUID = player.getUUID();
        BFUtils.initPlayerForGame(dataHandler, level, player);
        manager.initPlayerForGame(manager, dataHandler, level, player, uUID);
    }

    public static boolean method_2967(@NotNull ItemStack itemStack) {
        Object object = itemStack.getItem();
        if (!(object instanceof GunItem)) {
            return false;
        }
        GunItem gunItem = (GunItem)object;
        if (!gunItem.canRefillAmmo()) {
            return false;
        }
        object = gunItem.getMagTypeOrDefault(itemStack);
        if (GunItem.getAmmo(itemStack) != ((GunMagType)object).maxAmmo()) {
            GunItem.setAmmo(itemStack, ((GunMagType)object).maxAmmo());
            return true;
        }
        return false;
    }

    public static boolean method_2998(@NotNull Player player) {
        NonNullList nonNullList = player.getInventory().items;
        for (ItemStack itemStack : nonNullList.subList(0, 9)) {
            if (itemStack != null && itemStack.isEmpty()) continue;
            return false;
        }
        return true;
    }

    public static boolean playerHasUsedGuns(@NotNull ServerPlayer player) {
        for (ItemStack itemStack : player.getInventory().items) {
            Object object;
            if (itemStack.isEmpty() || !((object = itemStack.getItem()) instanceof GunItem)) continue;
            GunItem gunItem = (GunItem)object;
            object = gunItem.getMagTypeOrDefault(itemStack);
            int n = ((GunMagType)object).capacity();
            int n2 = ((GunMagType)object).maxAmmo();
            if (GunItem.getAmmo(itemStack) == n2 && GunItem.getAmmoLoaded(itemStack) == n) continue;
            return true;
        }
        return false;
    }

    public static boolean method_2868(@NotNull PlayerDataHandler<?> playerDataHandler, @NotNull GameTeam gameTeam) {
        for (UUID uUID : gameTeam.getPlayers()) {
            Object obj;
            ServerPlayer serverPlayer = BFUtils.getPlayerByUUID(uUID);
            if (serverPlayer == null || BFUtils.isPlayerUnavailable((Player)serverPlayer, obj = playerDataHandler.getPlayerData((Player)serverPlayer))) continue;
            return false;
        }
        return true;
    }

    public static void method_2950(@NotNull ServerLevel serverLevel, @NotNull ServerPlayer serverPlayer, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull GameTeam gameTeam) {
        FDSTagCompound fDSTagCompound = abstractGame.getPlayerStatData(serverPlayer.getUUID());
        int n = fDSTagCompound.getInteger(BFStats.CLASS.getKey(), -1);
        int n2 = fDSTagCompound.getInteger(BFStats.CLASS_INDEX.getKey(), -1);
        if (n != -1 && n2 != -1) {
            MatchClass matchClass = MatchClass.values()[n];
            fDSTagCompound.setInteger(BFStats.CLASS_ALIVE.getKey(), n);
            BFUtils.giveLoadout(serverLevel, serverPlayer, BFUtils.method_2898(abstractGame, gameTeam, matchClass, n2));
        }
    }

    public static void method_2943(@NotNull ServerLevel serverLevel, @NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull GameTeam gameTeam) {
        for (UUID uUID : gameTeam.getPlayers()) {
            ServerPlayer serverPlayer = BFUtils.getPlayerByUUID(uUID);
            if (serverPlayer == null) continue;
            BFUtils.method_2950(serverLevel, serverPlayer, abstractGame, gameTeam);
        }
    }

    public static boolean isSameTeam(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull UUID uuid1, @NotNull UUID uuid2) {
        AbstractGame<?, ?, ?> abstractGame = manager.getGameWithPlayer(uuid1);
        if (abstractGame == null) {
            return false;
        }
        Object obj = abstractGame.getPlayerManager();
        GameTeam gameTeam = ((AbstractGamePlayerManager)obj).getPlayerTeam(uuid1);
        GameTeam gameTeam2 = ((AbstractGamePlayerManager)obj).getPlayerTeam(uuid2);
        return gameTeam != null && gameTeam.equals(gameTeam2);
    }

    public static boolean method_2911(@NotNull AbstractGamePlayerManager<?> abstractGamePlayerManager, @NotNull UUID uUID, @NotNull UUID uUID2) {
        GameTeam gameTeam = abstractGamePlayerManager.getPlayerTeam(uUID);
        GameTeam gameTeam2 = abstractGamePlayerManager.getPlayerTeam(uUID2);
        return gameTeam != null && gameTeam.equals(gameTeam2);
    }

    public static BlockPos method_2949(@NotNull ServerLevel serverLevel, @NotNull BlockPos blockPos) {
        BlockPos blockPos2 = serverLevel.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, blockPos);
        if (blockPos2.getY() == serverLevel.getMinBuildHeight() - 1) {
            blockPos2 = blockPos2.above(2);
        }
        return blockPos2;
    }

    public static void method_2964(@NotNull LivingEntity livingEntity, @NotNull ParticleOptions particleOptions, @NotNull RandomSource randomSource, float f, float f2, float f3, int n) {
        Level level = livingEntity.level();
        if (!(level instanceof ServerLevel)) {
            return;
        }
        ServerLevel serverLevel = (ServerLevel)level;
        for (int i = 0; i < n; ++i) {
            double d = randomSource.nextGaussian() * 0.02;
            double d2 = randomSource.nextGaussian() * 0.02;
            double d3 = randomSource.nextGaussian() * 0.02;
            serverLevel.sendParticles(particleOptions, livingEntity.getRandomX(1.0) + (double)f, livingEntity.getRandomY() + (double)f2, livingEntity.getRandomZ(1.0) + (double)f3, 1, d, d2, d3, 1.0);
        }
    }

    public static void method_2970(@NotNull Level level, @NotNull ParticleOptions particleOptions, @NotNull Vec3 vec3, double d, double d2, int n, float f) {
        for (int i = 0; i < n; ++i) {
            double d3 = d + (d2 - d) * Math.pow(level.random.nextDouble(), 2.0);
            double d4 = Math.PI * 2 / (double)n * (double)i;
            double d5 = vec3.x + d3 * Math.cos(d4);
            double d6 = vec3.z + d3 * Math.sin(d4);
            double d7 = (double)f * Math.cos(d4);
            double d8 = (double)f * Math.sin(d4);
            level.addParticle(particleOptions, true, d5, vec3.y, d6, d7, 0.0, d8);
        }
    }

    public static void method_2971(@NotNull Level level, @NotNull ParticleOptions particleOptions, @NotNull Vec3 vec3, float f, float f2, int n, float f3) {
        for (int i = 0; i < n; ++i) {
            double d = (double)f + (double)(f2 - f) * Math.cbrt(level.random.nextDouble());
            double d2 = Math.PI * 2 * level.random.nextDouble();
            double d3 = Math.acos(2.0 * level.random.nextDouble() - 1.0);
            double d4 = vec3.x + d * Math.sin(d3) * Math.cos(d2);
            double d5 = vec3.y + d * Math.sin(d3) * Math.sin(d2);
            double d6 = vec3.z + d * Math.cos(d3);
            double d7 = (double)f3 * Math.sin(d3) * Math.cos(d2);
            double d8 = (double)f3 * Math.sin(d3) * Math.sin(d2);
            double d9 = (double)f3 * Math.cos(d3);
            level.addParticle(particleOptions, true, d4, d5, d6, d7, d8, d9);
        }
    }

    public static boolean isBehind(@NotNull LivingEntity attacker, @NotNull LivingEntity victim, float lenience) {
        float f = attacker.getYHeadRot();
        float f2 = victim.getYHeadRot();
        float f3 = Math.abs((f = (f + 360.0f) % 360.0f) - (f2 = (f2 + 360.0f) % 360.0f));
        f3 = f3 > 180.0f ? 360.0f - f3 : f3;
        return f3 <= lenience;
    }

    public static boolean method_2989(@NotNull LivingEntity livingEntity, @NotNull LivingEntity livingEntity2, float f) {
        Vec3 vec3;
        Vec3 vec32 = livingEntity2.getEyePosition();
        Vec3 vec33 = livingEntity.getEyePosition();
        float f2 = livingEntity.getYHeadRot();
        float f3 = livingEntity.getXRot();
        Vec3 vec34 = new Vec3((double)(-Mth.sin((float)(f2 * ((float)Math.PI / 180))) * Mth.cos((float)(f3 * ((float)Math.PI / 180)))), (double)(-Mth.sin((float)(f3 * ((float)Math.PI / 180)))), (double)(Mth.cos((float)(f2 * ((float)Math.PI / 180))) * Mth.cos((float)(f3 * ((float)Math.PI / 180)))));
        double d = vec34.dot(vec3 = vec32.subtract(vec33).normalize());
        double d2 = Math.acos(d);
        return d2 <= Math.toRadians(f / 2.0f);
    }

    public static float method_5932(@NotNull Entity entity, @NotNull Camera camera) {
        return BFUtils.method_2963(entity, camera.getPosition());
    }

    public static float method_5933(@NotNull Entity entity, @NotNull Entity entity2) {
        return BFUtils.method_2963(entity, entity2.getEyePosition());
    }

    public static float method_2963(@NotNull Entity entity, @NotNull Vec3 vec3) {
        float f;
        float f2;
        float f3 = (float)(vec3.x() - entity.getX());
        float f4 = f3 * f3 + (f2 = (float)(vec3.y() - entity.getY())) * f2 + (f = (float)(vec3.z() - entity.getZ())) * f;
        if (f4 < 1.0E-5f) {
            return 1.0f;
        }
        float f5 = (float)Math.sqrt(f4);
        float f6 = f3 / f5;
        float f7 = f2 / f5;
        float f8 = f / f5;
        float f9 = (float)Math.toRadians(entity.getYHeadRot());
        float f10 = (float)Math.toRadians(entity.getXRot());
        float f11 = -((float)Math.sin(f9) * (float)Math.cos(f10));
        float f12 = -((float)Math.sin(f10));
        float f13 = (float)Math.cos(f9) * (float)Math.cos(f10);
        float f14 = f11 * f6 + f12 * f7 + f13 * f8;
        float f15 = (f14 + 1.0f) / 2.0f;
        return f15 * f15;
    }

    public static boolean entitiesCanAttack(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull PlayerDataHandler<?> dataHandler, @NotNull LivingEntity attacker, @NotNull LivingEntity attacking) {
        Entity entity;
        Object object;
        Object object2;
        Object object3;
        if (!attacking.isAlive() || attacking.getHealth() <= 0.0f || attacking.tickCount <= 20) {
            return false;
        }
        if (attacker instanceof ServerPlayer) {
            object3 = (ServerPlayer)attacker;
            if (attacking instanceof ServerPlayer) {
                object2 = (ServerPlayer)attacking;
                object = manager.getGameWithPlayer(object3.getUUID());
                entity = manager.getGameWithPlayer(object2.getUUID());
                if (object != null && entity != null && !((AbstractGame)object).equals(entity)) {
                    return false;
                }
            }
        }
        if ((object2 = attacking.getVehicle()) instanceof AbstractVehicleEntity && ((AbstractVehicleEntity)(object3 = (AbstractVehicleEntity)object2)).method_2352((Entity)attacking)) {
            return false;
        }
        if (attacking instanceof Player) {
            object3 = (Player)attacking;
            object2 = dataHandler.getPlayerData((Player)object3);
            if (object2.isOutOfGame()) {
                return false;
            }
            entity = object3.getVehicle();
            if (entity instanceof AbstractVehicleEntity && ((AbstractVehicleEntity)(object = (AbstractVehicleEntity)entity)).method_2352((Entity)object3)) {
                return false;
            }
        }
        if (attacker instanceof BotEntity) {
            object3 = (BotEntity)attacker;
            return ((BotEntity)object3).canAttack(attacking);
        }
        return !attacking.isSpectator() && !attacking.equals((Object)attacker);
    }

    public static boolean isPlayerUnavailable(@NotNull Player player, @NotNull BFAbstractPlayerData<?, ?, ?, ?> data) {
        return data.isOutOfGame() || !player.isAlive() || player.isSpectator() || player.isDeadOrDying();
    }

    public static Explosion explosionFromPlayer(@NotNull Level level, @Nullable Entity entity, @NotNull Vec3 position, @NotNull ItemStack itemStack, float radius) {
        Entity entity2;
        if (entity instanceof BFProjectileEntity) {
            BFProjectileEntity bFProjectileEntity = (BFProjectileEntity)entity;
            entity2 = bFProjectileEntity.getOwner();
        } else {
            entity2 = entity;
        }
        GrenadeDamageSource grenadeDamageSource = new GrenadeDamageSource(level, entity2, itemStack, position);
        return new Explosion(level, entity, (DamageSource)grenadeDamageSource, null, position.x, position.y, position.z, radius, false, Explosion.BlockInteraction.KEEP, (ParticleOptions)ParticleTypes.EXPLOSION, (ParticleOptions)ParticleTypes.EXPLOSION_EMITTER, (Holder)SoundEvents.GENERIC_EXPLODE);
    }

    @Nullable
    public static <T extends Entity> T method_2972(@NotNull Level level, @NotNull Entity entity, @NotNull Class<T> clazz, float f) {
        AABB aABB = entity.getBoundingBox().inflate((double)f);
        List list = level.getEntitiesOfClass(clazz, aABB);
        if (!list.isEmpty()) {
            return (T)((Entity)list.getFirst());
        }
        return null;
    }

    @NotNull
    public static Set<UUID> sortBySkillLevel(@NotNull PlayerDataHandler<?> dataHandler, @NotNull Set<UUID> players) {
        Object2FloatOpenHashMap object2FloatOpenHashMap = new Object2FloatOpenHashMap();
        for (UUID uUID : players) {
            PlayerCloudData playerCloudData = dataHandler.getCloudProfile(uUID);
            float f = (float)playerCloudData.getKills() / Math.max(1.0f, (float)playerCloudData.getDeaths());
            object2FloatOpenHashMap.put((Object)uUID, f);
        }
        return object2FloatOpenHashMap.object2FloatEntrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).map(Map.Entry::getKey).collect(Collectors.toSet());
    }

    public static void handleGamePlayerChat(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull PlayerDataHandler<?> dataHandler, @NotNull AbstractGame<?, ?, ?> game, @NotNull ServerLevel level, @NotNull ServerPlayer player, @NotNull Component message, @NotNull UUID uuid) {
        if (!game.shouldAllowPlayerMessage(manager, player, message)) {
            return;
        }
        String string = message.getString();
        boolean bl = string.startsWith(TEAM_TYPING_PREFIX);
        Object obj = dataHandler.getPlayerData((Player)player);
        PlayerCloudData playerCloudData = dataHandler.getCloudProfile((Player)player);
        if (manager.isMatchMakingEnabled() && !BFUtils.filterMessage(manager, level, player, string, obj, playerCloudData)) {
            return;
        }
        BFUtils.handlePostGameKarma(manager, game, message, player, uuid);
        Set<UUID> set = ((AbstractGamePlayerManager)game.getPlayerManager()).getPlayerUUIDs();
        Component component = BFUtils.getFormattedPlayerMessage(dataHandler, game, player, uuid, player.getScoreboardName(), message, obj);
        boolean bl2 = BFUtils.isPlayerUnavailable((Player)player, obj);
        for (UUID uUID : set) {
            ServerPlayer serverPlayer = BFUtils.getPlayerByUUID(uUID);
            if (serverPlayer == null || !game.shouldShowPlayerMessage(uuid, serverPlayer, uUID, bl, bl2)) continue;
            serverPlayer.sendSystemMessage(component);
        }
    }

    private static boolean filterMessage(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull ServerLevel level, @NotNull ServerPlayer player, @NotNull String message, @NotNull BFAbstractPlayerData<?, ?, ?, ?> playerData, @NotNull PlayerCloudData cloudData) {
        Component component = BFChatFilter.checkMessage(manager, player, level, playerData, cloudData, message);
        if (component != null) {
            BFUtils.sendChatGraphic(ChatGraphic.WARNING, player, component);
            BFUtils.playSound(player, (SoundEvent)SoundEvents.NOTE_BLOCK_HARP.value(), SoundSource.MASTER, 1.0f, 2.0f);
            BFUtils.playSound(player, (SoundEvent)SoundEvents.NOTE_BLOCK_BASS.value(), SoundSource.MASTER, 1.0f, 2.0f);
            return false;
        }
        return true;
    }

    @NotNull
    private static Component getFormattedPlayerMessage(@NotNull PlayerDataHandler<?> dataHandler, @NotNull AbstractGame<?, ?, ?> game, @NotNull ServerPlayer player, @NotNull UUID uuid, @NotNull String playerName, @NotNull Component message, @NotNull BFAbstractPlayerData<?, ?, ?, ?> bFAbstractPlayerData) {
        String string = message.getString();
        string = string.replace("<" + playerName + "> ", "");
        Object obj = game.getPlayerManager();
        boolean bl = string.startsWith(TEAM_TYPING_PREFIX);
        string = string.replace("[Team] ", "");
        boolean bl2 = BFUtils.isPlayerUnavailable((Player)player, bFAbstractPlayerData);
        PlayerCloudData playerCloudData = dataHandler.getCloudProfile((Player)player);
        MutableComponent mutableComponent = playerCloudData.method_1715();
        MutableComponent mutableComponent2 = BFUtils.createPlayerStatsComponent(playerCloudData);
        PlayerRank playerRank = playerCloudData.getRank();
        MutableComponent mutableComponent3 = Component.literal((String)playerRank.getTitle()).withColor(0xFFFFFF);
        MutableComponent mutableComponent4 = Component.literal((String)String.valueOf(playerRank.getGraphic())).withStyle(Style.EMPTY.withColor(ChatFormatting.WHITE).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (Object)mutableComponent3)));
        MutableComponent mutableComponent5 = Component.empty().append((Component)mutableComponent).withStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (Object)mutableComponent2)));
        GameTeam gameTeam = ((AbstractGamePlayerManager)obj).getPlayerTeam(uuid);
        MutableComponent mutableComponent6 = Component.literal((String)": ").withStyle(gameTeam != null ? gameTeam.getStyleText() : Style.EMPTY);
        string = string.replaceAll("\\p{C}", "?");
        MutableComponent mutableComponent7 = Component.empty().append((Component)mutableComponent4).append(" ").append((Component)mutableComponent5).append((Component)mutableComponent6).append(string);
        if (gameTeam != null) {
            mutableComponent7 = mutableComponent7.copy().withStyle(gameTeam.getStyleIcon());
        }
        if (bl2) {
            mutableComponent7 = Component.empty().append(DEAD_PREFIX).append(" ").append((Component)mutableComponent7);
        } else if (bl) {
            mutableComponent7 = Component.empty().append(TEAM_PREFIX).append(" ").append((Component)mutableComponent7);
        }
        return mutableComponent7;
    }

    @NotNull
    private static MutableComponent createPlayerStatsComponent(@NotNull PlayerCloudData cloudData) {
        MutableComponent mutableComponent = Component.empty();
        mutableComponent.append((Component)Component.literal((String)("Player " + cloudData.getUsername() + " Stats:")));
        mutableComponent.append("\n");
        mutableComponent.append("\n").append((Component)Component.translatable((String)"bf.message.profile.details.exp", (Object[])new Object[]{Component.literal((String)StringUtils.formatLong(cloudData.getExp())).withStyle(ChatFormatting.YELLOW)}));
        mutableComponent.append("\n").append((Component)Component.translatable((String)"bf.message.profile.details.karma", (Object[])new Object[]{Component.literal((String)StringUtils.formatLong(cloudData.getMatchKarma())).withStyle(ChatFormatting.LIGHT_PURPLE)}));
        mutableComponent.append("\n").append((Component)Component.translatable((String)"bf.message.profile.details.time", (Object[])new Object[]{Component.literal((String)(ModLibraryMathHelper.round((double)(cloudData.getMinutesPlayed() / 60.0), (int)1) + "h")).withStyle(ChatFormatting.GRAY)}));
        mutableComponent.append("\n").append((Component)Component.translatable((String)"bf.message.profile.details.games", (Object[])new Object[]{Component.literal((String)StringUtils.formatLong(cloudData.getTotalGames())).withStyle(ChatFormatting.GRAY)}));
        mutableComponent.append("\n");
        mutableComponent.append("\n").append((Component)Component.translatable((String)"bf.message.profile.details.headshots", (Object[])new Object[]{Component.literal((String)StringUtils.formatLong(cloudData.getHeadShots())).withStyle(ChatFormatting.GREEN)}));
        mutableComponent.append("\n").append((Component)Component.translatable((String)"bf.message.profile.details.kills", (Object[])new Object[]{Component.literal((String)StringUtils.formatLong(cloudData.getKills())).withStyle(ChatFormatting.GREEN)}));
        mutableComponent.append("\n").append((Component)Component.translatable((String)"bf.message.profile.details.assists", (Object[])new Object[]{Component.literal((String)StringUtils.formatLong(cloudData.getAssists())).withStyle(ChatFormatting.YELLOW)}));
        mutableComponent.append("\n").append((Component)Component.translatable((String)"bf.message.profile.details.kills.streak", (Object[])new Object[]{Component.literal((String)StringUtils.formatLong(cloudData.getKillStreak())).withStyle(ChatFormatting.GREEN)}));
        mutableComponent.append("\n").append((Component)Component.translatable((String)"bf.message.profile.details.kills.fire", (Object[])new Object[]{Component.literal((String)StringUtils.formatLong(cloudData.getFireKills())).withStyle(ChatFormatting.GOLD)}));
        mutableComponent.append("\n").append((Component)Component.translatable((String)"bf.message.profile.details.deaths", (Object[])new Object[]{Component.literal((String)StringUtils.formatLong(cloudData.getDeaths())).withStyle(ChatFormatting.RED)}));
        mutableComponent.append("\n").append((Component)Component.translatable((String)"bf.message.profile.details.deaths.streak", (Object[])new Object[]{Component.literal((String)StringUtils.formatLong(cloudData.getDeathStreak())).withStyle(ChatFormatting.RED)}));
        mutableComponent.append("\n").append((Component)Component.translatable((String)"bf.message.profile.details.fb", (Object[])new Object[]{Component.literal((String)StringUtils.formatLong(cloudData.getFirstBloods())).withStyle(ChatFormatting.DARK_RED)}));
        int n = cloudData.getPrestigeLevel();
        if (n > 0) {
            MutableComponent mutableComponent2 = Component.literal((String)StringUtils.formatLong(n)).withColor(ColorReferences.COLOR_THEME_PRESTIGE_SOLID);
            mutableComponent.append("\n").append((Component)Component.translatable((String)"bf.message.profile.details.prestige", (Object[])new Object[]{mutableComponent2}));
        }
        mutableComponent.append("\n");
        return mutableComponent;
    }

    private static void handlePostGameKarma(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull AbstractGame<?, ?, ?> game, @NotNull Component message, @NotNull ServerPlayer player, @NotNull UUID uuid) {
        if (game.getStatus() != GameStatus.POST_GAME || BFUtils.getPlayerStat(game, uuid, BFStats.SAID_GG) == 1) {
            return;
        }
        if (BFUtils.isKarmaMessage(message)) {
            int n = 3;
            MutableComponent mutableComponent = Component.translatable((String)"bf.popup.message.karma", (Object[])new Object[]{Component.literal((String)String.valueOf(3)).withColor(0xFFFFFF)}).withStyle(ChatFormatting.LIGHT_PURPLE);
            BFUtils.incrementPlayerStat(manager, game, uuid, BFStats.SAID_GG);
            BFUtils.triggerPlayerStat(manager, game, uuid, BFStats.MATCH_KARMA, 3);
            BFUtils.sendNoticeMessage(player, (Component)mutableComponent);
            BFUtils.sendPopupMessage(player, new BFPopup((Component)mutableComponent, 80));
        }
    }

    private static boolean isKarmaMessage(@NotNull Component message) {
        String string = message.getString().toLowerCase(Locale.ROOT);
        return string.contains("gg") || string.contains("wp") || string.contains("good") && string.contains("game");
    }

    public static void method_2908(@NotNull AbstractGame<?, ?, ?> abstractGame, @NotNull ServerLevel serverLevel, @NotNull GameTeam gameTeam, boolean bl) {
        Optional<Pair<Loadout, MatchClass>> optional = gameTeam.getDivisionData(abstractGame).getRandomLoadout(true, BFUtils::method_2976);
        optional.ifPresent(pair -> BFUtils.spawnBot(abstractGame, serverLevel, gameTeam, (Pair<Loadout, MatchClass>)pair, bl));
    }

    public static void spawnBot(@NotNull AbstractGame<?, ?, ?> game, @NotNull ServerLevel level, @NotNull GameTeam team, @NotNull Pair<Loadout, MatchClass> loadout, boolean bl) {
        BFUtils.spawnBot(game, level, team, loadout, bl, 16.0f);
    }

    public static void spawnBot(@NotNull AbstractGame<?, ?, ?> game, @NotNull ServerLevel level, @NotNull GameTeam team, @NotNull Pair<Loadout, MatchClass> loadout, boolean bl, float maxHealth) {
        Loadout loadout2 = (Loadout)loadout.getLeft();
        MatchClass matchClass = (MatchClass)((Object)loadout.getRight());
        FDSPose fDSPose = team.randomSpawn(game);
        level.getChunkAt(fDSPose.asBlockPos());
        BotEntity botEntity = (BotEntity)((EntityType)BFEntityTypes.BOT.get()).create((Level)level);
        if (botEntity == null) {
            return;
        }
        DivisionData divisionData = team.getDivisionData(game);
        ItemStack itemStack = loadout2.getPrimary().copy();
        if (itemStack.isEmpty()) {
            itemStack = loadout2.getSecondary().copy();
        }
        botEntity.moveTo(fDSPose.position.x, fDSPose.position.y, fDSPose.position.z, 0.0f, 0.0f);
        botEntity.setGame(game);
        botEntity.setTeam(game, team);
        botEntity.method_2022(team.getName());
        botEntity.method_2026(divisionData.getRandomRace().ordinal());
        botEntity.setHumanName(divisionData.getCountry().getRandomUsername());
        botEntity.method_2019(itemStack);
        botEntity.setCurrentClass(matchClass);
        botEntity.method_1976(bl);
        AttributeInstance attributeInstance = botEntity.getAttribute(Attributes.MAX_HEALTH);
        if (attributeInstance != null) {
            attributeInstance.setBaseValue((double)maxHealth);
        }
        botEntity.setHealth(maxHealth);
        level.addFreshEntity((Entity)botEntity);
        if (game instanceof IHasBots) {
            IHasBots iHasBots = (IHasBots)((Object)game);
            iHasBots.method_3392(botEntity, (Level)level);
        }
    }

    private static boolean method_2976(Pair<Loadout, MatchClass> pair) {
        Item item;
        Object object;
        Item item2;
        Loadout loadout = (Loadout)pair.getLeft();
        ItemStack itemStack = loadout.getPrimary();
        if (itemStack != null && (item2 = itemStack.getItem()) instanceof GunItem && ((GunItem)(object = (GunItem)item2)).getDefaultFireConfig().method_4026() > 1) {
            return false;
        }
        object = loadout.getSecondary();
        if (object != null && (item = object.getItem()) instanceof GunItem) {
            item2 = (GunItem)item;
            return item2.getDefaultFireConfig().method_4026() <= 1;
        }
        return true;
    }
}

