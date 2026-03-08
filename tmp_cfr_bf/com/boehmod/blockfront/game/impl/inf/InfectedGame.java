/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game.impl.inf;

import com.boehmod.bflib.cloud.common.player.achievement.CloudAchievement;
import com.boehmod.bflib.cloud.common.player.achievement.CloudAchievements;
import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.assets.AssetCommandBuilder;
import com.boehmod.blockfront.assets.AssetCommandValidators;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.entity.InfectedDogEntity;
import com.boehmod.blockfront.common.entity.InfectedEntity;
import com.boehmod.blockfront.common.entity.VendorEntity;
import com.boehmod.blockfront.common.gun.GunSoundConfig;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.net.BFPopup;
import com.boehmod.blockfront.common.player.BFAbstractPlayerData;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.common.world.damage.FirearmDamageSource;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGameStage;
import com.boehmod.blockfront.game.GameShopItem;
import com.boehmod.blockfront.game.GameShopItems;
import com.boehmod.blockfront.game.GameStageManager;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.TeamJoinType;
import com.boehmod.blockfront.game.impl.inf.InfectedDoor;
import com.boehmod.blockfront.game.impl.inf.InfectedGameClient;
import com.boehmod.blockfront.game.impl.inf.InfectedIdleStage;
import com.boehmod.blockfront.game.impl.inf.InfectedPlayerManager;
import com.boehmod.blockfront.game.impl.inf.InfectedWaveType;
import com.boehmod.blockfront.game.impl.inf.ZombieSpawn;
import com.boehmod.blockfront.game.impl.inf.ZombieZone;
import com.boehmod.blockfront.game.tag.IAllowsRegeneration;
import com.boehmod.blockfront.game.tag.IHasGunStore;
import com.boehmod.blockfront.game.tag.IHasInfectedEntities;
import com.boehmod.blockfront.game.tag.IUseKillIcons;
import com.boehmod.blockfront.registry.BFEntityTypes;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFLog;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.CommandUtils;
import com.boehmod.blockfront.util.RandomUtils;
import com.boehmod.blockfront.util.RegistryUtils;
import com.boehmod.blockfront.util.math.FDSPose;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public final class InfectedGame
extends AbstractGame<InfectedGame, InfectedPlayerManager, GameStageManager<InfectedGame, InfectedPlayerManager>>
implements IHasGunStore,
IAllowsRegeneration,
IHasInfectedEntities,
IUseKillIcons {
    private static final Component field_6372 = Component.translatable((String)"bf.message.gamemode.infected.store.resupply").withStyle(ChatFormatting.GREEN);
    private static final Component field_6373 = Component.translatable((String)"bf.message.gamemode.infected.store.relocate").withStyle(ChatFormatting.YELLOW);
    private static final int field_3531 = 6;
    public static final int field_3532 = 5;
    public static final int field_3533 = 30;
    public static final int field_6374 = 60;
    @NotNull
    public final List<ZombieZone> zombieZones = new ObjectArrayList();
    @NotNull
    public final List<InfectedDoor> doors = new ObjectArrayList();
    @NotNull
    public final List<FDSPose> vendorSpawns = new ObjectArrayList();
    @NotNull
    public final List<GameShopItem> shopItems = new ObjectArrayList();
    @NotNull
    private final IntSet field_3528 = new IntOpenHashSet();
    @NotNull
    private final AssetCommandBuilder command = new AssetCommandBuilder().subCommand("vendor", new AssetCommandBuilder().subCommand("add", new AssetCommandBuilder((commandContext, stringArray) -> {
        Player player = (Player)((CommandSourceStack)commandContext.getSource()).source;
        FDSPose fDSPose = new FDSPose(player);
        this.vendorSpawns.add(fDSPose);
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Successfully added new vendor spawn. (" + this.vendorSpawns.size() + ")")));
    }).validator(AssetCommandValidators.ONLY_PLAYERS)).subCommand("clear", new AssetCommandBuilder((commandContext, stringArray) -> {
        this.vendorSpawns.clear();
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Successfully cleared all vendor spawns. (" + this.vendorSpawns.size() + ")")));
    }))).subCommand("spawn", new AssetCommandBuilder().subCommand("add", new AssetCommandBuilder((commandContext, stringArray) -> {
        Player player = (Player)((CommandSourceStack)commandContext.getSource()).source;
        FDSPose fDSPose = new FDSPose(player);
        ((InfectedPlayerManager)this.getPlayerManager()).method_3681(fDSPose);
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Successfully added new random spawn. (" + ((InfectedPlayerManager)this.getPlayerManager()).method_3676().size() + ")")));
    }).validator(AssetCommandValidators.ONLY_PLAYERS)).subCommand("clear", new AssetCommandBuilder((commandContext, stringArray) -> {
        ((InfectedPlayerManager)this.getPlayerManager()).method_3680();
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)"Successfully cleared all random spawns."));
    }))).subCommand("doors", new AssetCommandBuilder((commandContext, stringArray) -> {
        for (InfectedDoor infectedDoor : this.doors) {
            CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)String.format("Door '%s':", infectedDoor.getId())));
            int n = 0;
            List<String> list = infectedDoor.getZones();
            if (!list.isEmpty()) {
                for (String string : list) {
                    CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)String.format("- %s. Zone '%s'", n++, string)));
                }
                continue;
            }
            CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)"(Door has no zone triggers)"));
        }
    })).subCommand("door", new AssetCommandBuilder().subCommand("add", new AssetCommandBuilder((commandContext, stringArray) -> {
        int n;
        Player player = (Player)((CommandSourceStack)commandContext.getSource()).source;
        Level level = player.level();
        String string = stringArray[0];
        String string2 = stringArray[1];
        try {
            n = Integer.parseInt(string2);
        }
        catch (NumberFormatException numberFormatException) {
            CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Failed to create new door! (The door cost '" + string2 + "' is invalid)")));
            return;
        }
        BlockState blockState = level.getBlockState(player.blockPosition());
        if (blockState.getBlock() != Blocks.IRON_DOOR) {
            CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)"Failed to create new door! (Invalid door position! No iron door found.)"));
            return;
        }
        this.doors.add(new InfectedDoor(player.blockPosition(), string, (ObjectList<String>)new ObjectArrayList(), n));
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Created new zombie door '" + string + "' with cost '" + n + "'. (" + this.doors.size() + ")")));
    }).validator(AssetCommandValidators.count(new String[]{"door", "cost"})).validator(AssetCommandValidators.ONLY_PLAYERS)).subCommand("clear", new AssetCommandBuilder((commandContext, stringArray) -> {
        this.doors.clear();
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Successfully cleared all zombie doors. (" + this.doors.size() + ")")));
    }).validator(AssetCommandValidators.ONLY_PLAYERS)).subCommand("zone", new AssetCommandBuilder().subCommand("add", new AssetCommandBuilder((commandContext, stringArray) -> {
        String string = stringArray[0];
        String string2 = stringArray[1];
        List<InfectedDoor> list = this.method_3633(string);
        if (list.isEmpty()) {
            CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Failed to add zone '" + string2 + "' to door. (No doors for ID '" + string + "' exist)")));
            return;
        }
        list.forEach(infectedDoor -> infectedDoor.getZones().add(string2));
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Successfully added zone '" + string2 + "' to " + list.size() + " doors.")));
    }).validator(AssetCommandValidators.count(new String[]{"door", "zone"}))).subCommand("remove", new AssetCommandBuilder((commandContext, stringArray) -> {
        String string = stringArray[0];
        String string2 = stringArray[1];
        List<InfectedDoor> list = this.method_3633(string);
        if (list.isEmpty()) {
            CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Failed to remove zone '" + string2 + "' from door. (No doors for ID '" + string + "' exist)")));
            return;
        }
        list.forEach(infectedDoor -> infectedDoor.getZones().remove(string2));
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Successfully removed zone '" + string2 + "' to " + list.size() + " doors.")));
    }).validator(AssetCommandValidators.count(new String[]{"door", "zone"}))).subCommand("clear", new AssetCommandBuilder((commandContext, stringArray) -> {
        String string = stringArray[0];
        List<InfectedDoor> list = this.method_3633(string);
        if (list.isEmpty()) {
            CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Failed to clear zones from door. (No doors for ID '" + string + "' exist)")));
            return;
        }
        list.forEach(infectedDoor -> infectedDoor.getZones().clear());
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Successfully cleared zones from " + list.size() + " doors.")));
    }).validator(AssetCommandValidators.count(new String[]{"door"}))))).subCommand("zones", new AssetCommandBuilder((commandContext, stringArray) -> {
        for (ZombieZone zombieZone : this.zombieZones) {
            CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)String.format("Zone '%s':", zombieZone.getId())));
        }
    })).subCommand("zone", new AssetCommandBuilder().subCommand("add", new AssetCommandBuilder((commandContext, stringArray) -> {
        String string = stringArray[0];
        if (this.getZone(string).isPresent()) {
            CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Failed to create new zone! (The zone '" + string + "' already exists)")));
            return;
        }
        this.zombieZones.add(new ZombieZone(string));
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Successfully added a new zone '" + string + "'. (" + this.zombieZones.size() + ")")));
    }).validator(AssetCommandValidators.count(new String[]{"name"}))).subCommand("remove", new AssetCommandBuilder((commandContext, stringArray) -> {
        String string = stringArray[0];
        if (this.getZone(string).isEmpty()) {
            CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Failed to remove zone! (The zone '" + string + "' doesn't exists)")));
            return;
        }
        this.zombieZones.removeIf(zombieZone -> zombieZone.getId().equals(string));
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Successfully removed zone '" + string + "'. (" + this.zombieZones.size() + ")")));
    }).validator(AssetCommandValidators.count(new String[]{"name"}))).subCommand("edit", new AssetCommandBuilder().subCommand("spawn", new AssetCommandBuilder().subCommand("add", new AssetCommandBuilder((commandContext, stringArray) -> {
        Player player = (Player)((CommandSourceStack)commandContext.getSource()).source;
        String string = stringArray[0];
        Optional<ZombieZone> optional = this.getZone(string);
        if (optional.isEmpty()) {
            CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Failed to add zombie spawn to zone! (The zone '" + string + "' doesn't exists)")));
            return;
        }
        ZombieZone zombieZone = optional.get();
        zombieZone.getSpawns().add(new ZombieSpawn(string, new FDSPose(player)));
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Successfully added zombie spawn to zone '" + string + "'. (" + zombieZone.getSpawns().size() + ")")));
    }).validator(AssetCommandValidators.count(new String[]{"zone"})).validator(AssetCommandValidators.ONLY_PLAYERS)).subCommand("clear", new AssetCommandBuilder((commandContext, stringArray) -> {
        String string = stringArray[0];
        Optional<ZombieZone> optional = this.getZone(string);
        if (optional.isEmpty()) {
            CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Failed to clear zombie spawns from zone! (The zone '" + string + "' doesn't exists)")));
            return;
        }
        ZombieZone zombieZone = optional.get();
        zombieZone.getSpawns().clear();
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Successfully cleared zombie spawns from zone '" + string + "'. (" + zombieZone.getSpawns().size() + ")")));
    }).validator(AssetCommandValidators.count(new String[]{"zone"}))))).subCommand("list", new AssetCommandBuilder((commandContext, stringArray) -> {
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)"All Zones:"));
        for (ZombieZone zombieZone : this.zombieZones) {
            CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)String.format("- '%s' - (%d spawns)", zombieZone.getId(), zombieZone.getSpawns().size())));
        }
    })));
    @Nullable
    public VendorEntity vendorEntity = null;
    public int field_3534 = 0;
    public int currentRound = 1;
    public int numInfectedToSpawn = 5;
    public int field_3537 = 0;
    public boolean field_3529 = false;
    @NotNull
    public InfectedWaveType waveType = InfectedWaveType.GENERIC;

    public InfectedGame(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager) {
        super(bFAbstractManager, "inf", "Infected");
    }

    private void method_3627(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull ServerLevel serverLevel, @NotNull InfectedEntity infectedEntity, @NotNull Set<UUID> set, @NotNull Set<UUID> set2) {
        set2.forEach(uUID -> {
            BFUtils.incrementPlayerStat(bFAbstractManager, this, uUID, BFStats.SCORE);
            BFUtils.sendPopupMessage(uUID, new BFPopup((Component)Component.translatable((String)"bf.message.gamemode.infected.popup.wave.survive").withStyle(ChatFormatting.DARK_RED), 40));
        });
        BFUtils.spawnLightningBolt(infectedEntity.level(), infectedEntity.position());
        serverLevel.playSound(null, (Entity)infectedEntity, SoundEvents.WITHER_DEATH, SoundSource.AMBIENT, 10.0f, 0.5f);
        BFUtils.playSound(set, (SoundEvent)this.waveType.getRoundEndSound().get(), SoundSource.MUSIC);
        MutableComponent mutableComponent = Component.literal((String)String.valueOf(this.currentRound)).withStyle(ChatFormatting.RED);
        MutableComponent mutableComponent2 = Component.literal((String)String.valueOf(set2.size())).withStyle(ChatFormatting.RED);
        MutableComponent mutableComponent3 = Component.translatable((String)"bf.message.gamemode.infected.wave.complete", (Object[])new Object[]{mutableComponent, mutableComponent2}).withStyle(ChatFormatting.DARK_RED);
        BFUtils.sendNoticeMessage(set, (Component)mutableComponent3);
        set.forEach(uUID -> {
            Object d = this.dataHandler.getPlayerData((UUID)uUID);
            ServerPlayer serverPlayer = BFUtils.getPlayerByUUID(uUID);
            if (((BFAbstractPlayerData)d).isOutOfGame()) {
                if (serverPlayer != null) {
                    BFUtils.initPlayerForGame(bFAbstractManager, this.dataHandler, serverLevel, serverPlayer);
                }
            } else {
                BFUtils.incrementPlayerStat(bFAbstractManager, this, uUID, BFStats.INFECTED_ROUNDS_WON);
            }
        });
    }

    public void relocateVendor(@NotNull ServerLevel level, @NotNull FDSPose pose) {
        if (this.vendorEntity != null) {
            this.vendorEntity.discard();
            this.vendorEntity = null;
        }
        this.vendorEntity = (VendorEntity)((EntityType)BFEntityTypes.GUN_DEALER.get()).create((Level)level);
        if (this.vendorEntity != null) {
            this.vendorEntity.setGame(this);
            level.addFreshEntity((Entity)this.vendorEntity);
            this.vendorEntity.teleportTo(level, pose.position.x, pose.position.y, pose.position.z, Set.of(), pose.rotation.x, pose.rotation.y);
        }
    }

    public void method_3651(ServerLevel serverLevel) {
        EntityType entityType;
        FDSPose fDSPose = this.method_3652();
        serverLevel.getChunkAt(fDSPose.asBlockPos());
        if (this.waveType == InfectedWaveType.DOGS) {
            entityType = (EntityType)BFEntityTypes.INFECTED_DOG.get();
        } else {
            entityType = (EntityType)BFEntityTypes.INFECTED.get();
            if (this.currentRound >= 5 && Math.random() < 0.2) {
                entityType = (EntityType)BFEntityTypes.INFECTED_SPITTER.get();
            }
            if (this.currentRound >= 10 && Math.random() < 0.1) {
                entityType = (EntityType)BFEntityTypes.INFECTED_STALKER.get();
            }
        }
        InfectedEntity infectedEntity = (InfectedEntity)entityType.create((Level)serverLevel);
        if (infectedEntity != null) {
            infectedEntity.setGame(this);
            infectedEntity.moveTo(fDSPose.position, 0.0f, 0.0f);
            serverLevel.addFreshEntity((Entity)infectedEntity);
            this.method_3424(infectedEntity, (Level)serverLevel);
            BFUtils.spawnLightningBolt((Level)serverLevel, infectedEntity.position());
            this.field_3528.add(infectedEntity.getId());
        }
        this.field_3537 = this.field_3528.size();
    }

    @NotNull
    public Optional<ZombieZone> getZone(@NotNull String id) {
        return this.zombieZones.stream().filter(zone -> zone.getId().equals(id)).findFirst();
    }

    @NotNull
    public List<InfectedDoor> method_3633(@NotNull String string) {
        return this.doors.stream().filter(infectedDoor -> infectedDoor.getId().equals(string)).collect(Collectors.toList());
    }

    @NotNull
    private FDSPose method_3652() {
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        List<ZombieZone> list = this.method_3623().stream().filter(zombieZone -> !zombieZone.getSpawns().isEmpty()).toList();
        if (list.isEmpty()) {
            throw new IllegalStateException("No active zombie zones with zombie spawns available");
        }
        ZombieZone zombieZone2 = list.get(threadLocalRandom.nextInt(list.size()));
        return zombieZone2.getRandomSpawn().position();
    }

    @NotNull
    private List<ZombieZone> method_3623() {
        return this.zombieZones.stream().filter(ZombieZone::isActive).collect(Collectors.toList());
    }

    @NotNull
    public Set<UUID> method_3638(@NotNull Set<UUID> set) {
        ObjectOpenHashSet objectOpenHashSet = new ObjectOpenHashSet();
        for (UUID uUID : set) {
            if (((BFAbstractPlayerData)this.dataHandler.getPlayerData(uUID)).isOutOfGame()) continue;
            objectOpenHashSet.add(uUID);
        }
        return objectOpenHashSet;
    }

    public boolean method_3639(@NotNull ServerLevel serverLevel) {
        if (this.numInfectedToSpawn <= 0) {
            return false;
        }
        if (this.field_3534-- <= 0) {
            this.field_3534 = this.method_3643();
            if (this.method_3624(serverLevel).size() < this.method_3642()) {
                int n = (int)(1.0f + (float)this.method_3426() * 0.2f);
                for (int i = 0; i < n && this.numInfectedToSpawn > 0; ++i) {
                    --this.numInfectedToSpawn;
                    this.method_3651(serverLevel);
                }
            }
        }
        return true;
    }

    private int method_3642() {
        return 60;
    }

    public void advanceRound(@NotNull ServerLevel level, @NotNull Set<UUID> players) {
        this.field_3534 = 0;
        ++this.currentRound;
        this.field_3528.clear();
        this.waveType = this.currentRound % 5 == 0 ? InfectedWaveType.DOGS : InfectedWaveType.GENERIC;
        this.numInfectedToSpawn = (int)Math.min(150.0f, (float)(5 * this.method_3426()) * this.waveType.getCountMultiplier());
        this.waveType.getRoundCallable().method_3683(this, (Level)level, this.numInfectedToSpawn, players);
        BFUtils.playSound(players, (SoundEvent)this.waveType.getRoundStartSound().get(), SoundSource.MUSIC);
        if (Math.random() < (double)0.2f) {
            BFUtils.playSound(players, (SoundEvent)BFSounds.ENTITY_GUNDEALER_RESUPPLY.get(), SoundSource.MASTER);
            BFUtils.sendNoticeMessage(players, field_6372);
            this.refreshShopItems();
        } else if (Math.random() < 0.1 && this.field_3529) {
            BFUtils.playSound(players, (SoundEvent)BFSounds.ENTITY_GUNDEALER_RELOCATE.get(), SoundSource.MASTER);
            BFUtils.sendNoticeMessage(players, field_6373);
            this.relocateVendor(level, RandomUtils.randomFromList(this.vendorSpawns));
        }
    }

    private int method_3643() {
        return Math.max(5, 40 - 2 * this.currentRound) / 20;
    }

    private float method_3629(@NotNull InfectedEntity infectedEntity) {
        return Math.min(5.0f + 0.25f * (float)this.method_3426(), infectedEntity.getMaxHealth());
    }

    @NotNull
    public IntSet method_3624(@NotNull ServerLevel serverLevel) {
        IntOpenHashSet intOpenHashSet = new IntOpenHashSet();
        IntIterator intIterator = this.field_3528.iterator();
        while (intIterator.hasNext()) {
            int n = (Integer)intIterator.next();
            Entity entity = serverLevel.getEntity(n);
            if (entity == null || !entity.isAlive()) continue;
            intOpenHashSet.add(n);
        }
        return intOpenHashSet;
    }

    private boolean method_3641(@NotNull Player player, int n) {
        for (UUID uUID : ((InfectedPlayerManager)this.getPlayerManager()).getPlayerUUIDs()) {
            if (player.getUUID().equals(uUID) || BFUtils.getPlayerStat(this, uUID, BFStats.CHAR) != n) continue;
            return true;
        }
        return false;
    }

    public int method_3647(@NotNull Player player) {
        int n = 0;
        while (this.method_3641(player, n)) {
            ++n;
        }
        return n;
    }

    public void refreshShopItems() {
        this.shopItems.clear();
        while (this.shopItems.size() < 6) {
            GameShopItem gameShopItem = RandomUtils.randomFromList(GameShopItems.ENTRIES);
            if (this.shopItems.contains(gameShopItem)) continue;
            this.shopItems.add(gameShopItem);
        }
        if (this.vendorEntity != null) {
            this.vendorEntity.setItemInHand(InteractionHand.MAIN_HAND, this.shopItems.getFirst().getItemStack().copy());
        }
    }

    @NotNull
    public InfectedGameClient createGameClient(@NotNull BFClientManager bFClientManager) {
        return new InfectedGameClient(bFClientManager, this, (ClientPlayerDataHandler)this.dataHandler);
    }

    @Override
    @NotNull
    protected InfectedPlayerManager createPlayerManager() {
        return new InfectedPlayerManager(this, this.dataHandler);
    }

    @Override
    @NotNull
    public AbstractGameStage<InfectedGame, InfectedPlayerManager> createFirstStage() {
        return new InfectedIdleStage();
    }

    @Override
    @NotNull
    public Set<AbstractGame.ErrorType> getErrorTypes() {
        return EnumSet.of(AbstractGame.ErrorType.MAP_TYPE, AbstractGame.ErrorType.LOBBY);
    }

    @Override
    public void getErrorMessages(@NotNull List<MutableComponent> messages) {
        if (this.zombieZones.isEmpty()) {
            messages.add(Component.literal((String)("Zones in game '" + this.name + "' are missing.")));
        }
        boolean bl = false;
        for (ZombieZone object : this.zombieZones) {
            if (object.getSpawns().isEmpty()) continue;
            bl = true;
            break;
        }
        if (!bl) {
            messages.add(Component.literal((String)("Game '" + this.name + "' has no zombie spawns in any of its zones.")));
        }
        if (this.getZone("main").isEmpty()) {
            messages.add(Component.literal((String)("A 'main' zone for game '" + this.name + "' is missing. One is required.")));
        }
        for (InfectedDoor infectedDoor : this.doors) {
            if (!infectedDoor.getZones().isEmpty()) continue;
            messages.add(Component.literal((String)("Door '" + infectedDoor.getId() + "' does not have any affiliated zones.")));
        }
        super.getErrorMessages(messages);
    }

    @Override
    public void updateStageManager(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull PlayerDataHandler<?> dataHandler, @NotNull ServerLevel level, @NotNull Set<UUID> players) {
        this.stageManager.update(manager, dataHandler, (InfectedPlayerManager)this.playerManager, level, players);
    }

    @Override
    protected boolean isMatchSuccess() {
        return this.currentRound >= 5;
    }

    @Override
    @NotNull
    public AssetCommandBuilder getCommand() {
        return super.getCommand().inherit(this.command);
    }

    @Override
    public int method_3426() {
        return this.currentRound + (this.method_3623().size() - 1);
    }

    @Override
    public boolean playerJoinTeam(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull TeamJoinType joinType, @NotNull ServerLevel level, @NotNull ServerPlayer player) {
        GameTeam gameTeam = ((InfectedPlayerManager)this.playerManager).getTeamByName("Survivors");
        if (gameTeam == null) {
            BFLog.log("No team available for player " + player.getScoreboardName() + " to join.", new Object[0]);
            return false;
        }
        return ((InfectedPlayerManager)this.playerManager).playerJoinTeam(manager, joinType, level, player, gameTeam);
    }

    @Override
    public void specificReset(@Nullable Level level) {
        this.refreshShopItems();
        this.field_3529 = false;
        this.waveType = InfectedWaveType.GENERIC;
        this.zombieZones.forEach(ZombieZone::setInactive);
        this.doors.forEach(InfectedDoor::close);
        if (level != null) {
            for (InfectedDoor infectedDoor : this.doors) {
                infectedDoor.method_3697(level, false);
            }
        }
        this.field_3534 = 0;
        this.currentRound = 1;
        this.numInfectedToSpawn = 5;
        this.field_3528.clear();
    }

    @Override
    public void writeAll(@NotNull ByteBuf buf, boolean writeMap) throws IOException {
        super.writeAll(buf, writeMap);
        IPacket.writeEnum((ByteBuf)buf, (Enum)this.waveType);
        buf.writeInt(this.numInfectedToSpawn);
        buf.writeInt(this.field_3537);
        buf.writeInt(this.currentRound);
        buf.writeInt(this.doors.size());
        for (InfectedDoor object : this.doors) {
            object.write(buf);
        }
        buf.writeInt(this.zombieZones.size());
        for (ZombieZone zombieZone : this.zombieZones) {
            zombieZone.write(buf);
        }
        buf.writeInt(this.shopItems.size());
        for (GameShopItem gameShopItem : this.shopItems) {
            ResourceLocation resourceLocation = BuiltInRegistries.ITEM.getKey((Object)gameShopItem.getItemStack().getItem());
            IPacket.writeString((ByteBuf)buf, (String)resourceLocation.toString());
            buf.writeInt(gameShopItem.getPrice());
        }
    }

    @Override
    public void readAll(@NotNull ByteBuf buf) throws IOException {
        int n;
        int n2;
        super.readAll(buf);
        this.waveType = (InfectedWaveType)IPacket.readEnum((ByteBuf)buf, InfectedWaveType.class);
        this.numInfectedToSpawn = buf.readInt();
        this.field_3537 = buf.readInt();
        this.currentRound = buf.readInt();
        this.doors.clear();
        int n3 = buf.readInt();
        for (n2 = 0; n2 < n3; ++n2) {
            InfectedDoor infectedDoor = new InfectedDoor();
            infectedDoor.read(buf);
            this.doors.add(infectedDoor);
        }
        this.zombieZones.clear();
        n2 = buf.readInt();
        for (n = 0; n < n2; ++n) {
            ZombieZone zombieZone = new ZombieZone();
            zombieZone.read(buf);
            this.zombieZones.add(zombieZone);
        }
        this.shopItems.clear();
        n = buf.readInt();
        for (int i = 0; i < n; ++i) {
            String string = IPacket.readString((ByteBuf)buf);
            int n4 = buf.readInt();
            Supplier<Item> supplier = RegistryUtils.retrieveItem(string);
            this.shopItems.add(new GameShopItem(supplier.get(), n4));
        }
    }

    @Override
    public void writeSpecificFDS(@NotNull FDSTagCompound root) {
        int n;
        int n2;
        int n3;
        List<FDSPose> list = ((InfectedPlayerManager)this.playerManager).method_3676();
        int n4 = list.size();
        root.setInteger("randomSpawnSize", n4);
        for (n3 = 0; n3 < n4; ++n3) {
            list.get(n3).writeNamedFDS("randomSpawn" + n3, root);
        }
        n3 = this.doors.size();
        root.setInteger("doorCount", n3);
        for (n2 = 0; n2 < n3; ++n2) {
            FDSTagCompound fDSTagCompound = new FDSTagCompound("door" + n2);
            this.doors.get(n2).writeToFDS(fDSTagCompound);
            root.setTagCompound("door" + n2, fDSTagCompound);
        }
        n2 = this.zombieZones.size();
        root.setInteger("zoneCount", n2);
        for (n = 0; n < n2; ++n) {
            FDSTagCompound fDSTagCompound = new FDSTagCompound("zone" + n);
            this.zombieZones.get(n).writeToFDS(fDSTagCompound);
            root.setTagCompound("zone" + n, fDSTagCompound);
        }
        n = this.vendorSpawns.size();
        root.setInteger("gunStoreCount", n);
        for (int i = 0; i < n; ++i) {
            this.vendorSpawns.get(i).writeNamedFDS("gunStore" + i, root);
        }
    }

    @Override
    public void readSpecificFDS(@NotNull FDSTagCompound root) {
        int n;
        InfectedDoor infectedDoor;
        int n2;
        int n3;
        int n4 = root.getInteger("randomSpawnSize");
        for (n3 = 0; n3 < n4; ++n3) {
            ((InfectedPlayerManager)this.playerManager).method_3681(FDSPose.readNamedFDS("randomSpawn" + n3, root));
        }
        this.doors.clear();
        this.zombieZones.clear();
        this.vendorSpawns.clear();
        n3 = root.getInteger("doorCount");
        for (n2 = 0; n2 < n3; ++n2) {
            FDSTagCompound fDSTagCompound = root.getTagCompound("door" + n2);
            if (fDSTagCompound == null) continue;
            infectedDoor = new InfectedDoor();
            infectedDoor.readFromFDS(fDSTagCompound);
            this.doors.add(infectedDoor);
        }
        n2 = root.getInteger("zoneCount");
        for (n = 0; n < n2; ++n) {
            infectedDoor = root.getTagCompound("zone" + n);
            if (infectedDoor == null) continue;
            ZombieZone zombieZone = new ZombieZone();
            zombieZone.readFromFDS((FDSTagCompound)infectedDoor);
            this.zombieZones.add(zombieZone);
        }
        if (root.hasTag("gunStoreCount")) {
            n = root.getInteger("gunStoreCount");
            for (int i = 0; i < n; ++i) {
                this.vendorSpawns.add(FDSPose.readNamedFDS("gunStore" + i, root));
            }
        }
    }

    @Override
    public boolean shouldRespawnAutomatically(@NotNull Player player) {
        return false;
    }

    @Override
    public int getMinimumPlayers() {
        return 1;
    }

    @Override
    public boolean shouldUseStamina(@NotNull Player player) {
        return false;
    }

    @Override
    public boolean shouldAnnounceRageQuits() {
        return true;
    }

    @Override
    public boolean shouldShowDeadMessages() {
        return true;
    }

    @Override
    @NotNull
    public CloudAchievement getVictoryAchievement() {
        return CloudAchievements.ACH_MATCH_WIN_INF;
    }

    @Override
    public void method_3424(@NotNull InfectedEntity infectedEntity, @NotNull Level level) {
        this.waveType.getRoundCallable().method_3682(this.dataHandler, this, infectedEntity, level);
        infectedEntity.setHealth(this.method_3629(infectedEntity));
    }

    @Override
    public void method_3422(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull ServerLevel serverLevel, @NotNull InfectedEntity infectedEntity, @NotNull DamageSource damageSource) {
        Set<UUID> set = ((InfectedPlayerManager)this.playerManager).getPlayerUUIDs();
        Set<UUID> set2 = this.method_3638(set);
        Object object = damageSource.getEntity();
        if (object instanceof Player) {
            Player player = (Player)object;
            object = player.getUUID();
            BFUtils.incrementPlayerStat(bFAbstractManager, this, (UUID)object, BFStats.SCORE);
            if (damageSource instanceof FirearmDamageSource) {
                FirearmDamageSource firearmDamageSource = (FirearmDamageSource)damageSource;
                BFUtils.triggerPlayerStat(bFAbstractManager, this, (UUID)object, BFStats.POINTS, firearmDamageSource.isHeadshot() ? 20 : 10);
            } else {
                BFUtils.triggerPlayerStat(bFAbstractManager, this, (UUID)object, BFStats.POINTS, 35);
            }
        }
        if (infectedEntity instanceof InfectedDogEntity) {
            BFUtils.spawnLightningBolt(infectedEntity.level(), infectedEntity.position());
            serverLevel.addParticle((ParticleOptions)ParticleTypes.FLASH, infectedEntity.getX(), infectedEntity.getY(), infectedEntity.getZ(), 0.0, 0.0, 0.0);
            serverLevel.playSound(null, (Entity)infectedEntity, SoundEvents.WITHER_DEATH, SoundSource.AMBIENT, 10.0f, 1.5f);
        }
        this.field_3537 = this.method_3624(serverLevel).size();
        if (this.field_3537 == 0 && this.numInfectedToSpawn <= 0) {
            this.method_3627(bFAbstractManager, serverLevel, infectedEntity, set, set2);
        }
    }

    @Override
    public void method_3423(@NotNull InfectedEntity infectedEntity, @NotNull Player player) {
    }

    @Override
    public void method_3425(@NotNull ServerLevel serverLevel, @NotNull InfectedEntity infectedEntity) {
        if (infectedEntity.getTarget() == null) {
            Predicate<LivingEntity> predicate = infectedEntity.method_2064(this.dataHandler);
            for (ServerPlayer serverPlayer : serverLevel.players()) {
                if (!predicate.test((LivingEntity)serverPlayer)) continue;
                infectedEntity.setTarget((LivingEntity)serverPlayer);
                break;
            }
        }
    }

    @Override
    public boolean method_3421(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull InfectedEntity infectedEntity, @NotNull DamageSource damageSource, float f) {
        Object object = damageSource.getEntity();
        if (object instanceof Player) {
            Player player = (Player)object;
            object = player.getUUID();
            if (f < infectedEntity.getHealth()) {
                BFUtils.triggerPlayerStat(bFAbstractManager, this, (UUID)object, BFStats.POINTS, 2);
            }
        }
        return true;
    }

    @Override
    public boolean playerCanRegenerate(@NotNull Player player) {
        return player.getHealth() < player.getMaxHealth();
    }

    @Override
    public float method_3418(@NotNull Player player) {
        return 1.0f;
    }

    @Override
    public int method_3419(@NotNull Player player) {
        return 20;
    }

    @Override
    public boolean method_3416(@NotNull Player player, @NotNull GameShopItem gameShopItem) {
        int n = BFUtils.getPlayerStat(this, player.getUUID(), BFStats.POINTS);
        return n >= gameShopItem.getPrice();
    }

    @Override
    public void method_3415(@NotNull ServerPlayer serverPlayer, @NotNull GameShopItem gameShopItem) {
        GunItem gunItem;
        Level level = serverPlayer.level();
        UUID uUID = serverPlayer.getUUID();
        if (!this.method_3416((Player)serverPlayer, gameShopItem)) {
            MutableComponent mutableComponent = Component.translatable((String)"bf.message.gamemode.infected.store.buy.fail", (Object[])new Object[]{Component.translatable((String)gameShopItem.getItemStack().getDescriptionId()).withStyle(ChatFormatting.GRAY)}).withStyle(ChatFormatting.RED);
            BFUtils.sendNoticeMessage(serverPlayer, (Component)mutableComponent);
            return;
        }
        this.field_3529 = true;
        Vec3 vec3 = serverPlayer.position();
        float f = serverPlayer.getEyeHeight();
        level.playSound(null, vec3.x, vec3.y + (double)f, vec3.z, (SoundEvent)BFSounds.ENTITY_GUNDEALER_SALE.get(), SoundSource.AMBIENT, 1.0f, 1.0f);
        ItemStack itemStack = gameShopItem.getItemStack();
        Object object = itemStack.getItem();
        if (object instanceof GunItem) {
            gunItem = (GunItem)object;
            DeferredHolder<SoundEvent, SoundEvent> deferredHolder = ((GunSoundConfig)(object = gunItem.getSoundConfig(itemStack))).getReload();
            if (deferredHolder != null) {
                level.playSound((Player)serverPlayer, vec3.x, vec3.y + (double)f, vec3.z, (SoundEvent)deferredHolder.get(), SoundSource.AMBIENT, 1.0f, 1.0f);
            }
            serverPlayer.getInventory().setItem(gunItem.isSecondary() ? 0 : 1, itemStack.copy());
        }
        BFUtils.decreasePlayerStat(this, uUID, BFStats.POINTS, gameShopItem.getPrice());
        gunItem = Component.translatable((String)"bf.message.gamemode.infected.store.buy", (Object[])new Object[]{Component.translatable((String)itemStack.getDescriptionId()).withStyle(ChatFormatting.GRAY), Component.literal((String)String.valueOf(gameShopItem.getPrice()))}).withStyle(ChatFormatting.GREEN);
        BFUtils.sendNoticeMessage(serverPlayer, (Component)gunItem);
    }

    @Override
    @Nullable
    public GameShopItem method_3417(@NotNull Player player, @NotNull Item item) {
        return this.shopItems.stream().filter(gameShopItem -> gameShopItem.getItemStack().getItem() == item).findFirst().orElse(null);
    }

    @Override
    public boolean method_3430() {
        return true;
    }
}

