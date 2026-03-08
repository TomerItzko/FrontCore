/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game.impl.ttt;

import com.boehmod.bflib.cloud.common.player.achievement.CloudAchievement;
import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.assets.AssetCommandBuilder;
import com.boehmod.blockfront.assets.AssetCommandValidators;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.item.BFConsumableItem;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.match.Loadout;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.common.stat.BFStat;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGameStage;
import com.boehmod.blockfront.game.GameShopItem;
import com.boehmod.blockfront.game.GameStageManager;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.TeamJoinType;
import com.boehmod.blockfront.game.impl.ttt.TTTDeadBody;
import com.boehmod.blockfront.game.impl.ttt.TTTPlayerRole;
import com.boehmod.blockfront.game.impl.ttt.TTTPlayerTeam;
import com.boehmod.blockfront.game.impl.ttt.TroubleTownGameClient;
import com.boehmod.blockfront.game.impl.ttt.TroubleTownIdleStage;
import com.boehmod.blockfront.game.impl.ttt.TroubleTownPlayerManager;
import com.boehmod.blockfront.game.impl.ttt.randomat.RandomatEvent;
import com.boehmod.blockfront.game.impl.ttt.randomat.RandomatRegistry;
import com.boehmod.blockfront.game.tag.IAllowsSoundboard;
import com.boehmod.blockfront.game.tag.IHasConsumables;
import com.boehmod.blockfront.game.tag.IHasItemShop;
import com.boehmod.blockfront.game.tag.IModifiesDamage;
import com.boehmod.blockfront.registry.BFItems;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFLog;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.CommandUtils;
import com.boehmod.blockfront.util.RandomUtils;
import com.boehmod.blockfront.util.math.FDSPose;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.io.IOException;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public final class TroubleTownGame
extends AbstractGame<TroubleTownGame, TroubleTownPlayerManager, GameStageManager<TroubleTownGame, TroubleTownPlayerManager>>
implements IModifiesDamage,
IHasItemShop,
IHasConsumables,
IAllowsSoundboard {
    public static final Loadout LOADOUT = new Loadout(null, null, new ItemStack((ItemLike)BFItems.MELEE_ITEM_KNIFE_M1905.get()), null, null, null, null, null);
    public final List<FDSPose> weaponSpawns = new ObjectArrayList();
    private final List<DeferredHolder<Item, ? extends GunItem>> ITEM_DROPS = ObjectArrayList.of((Object[])new DeferredHolder[]{BFItems.GUN_LUGER, BFItems.GUN_WALTHER_P38, BFItems.GUN_MAUSER_C96, BFItems.GUN_MP40, BFItems.GUN_STG44, BFItems.GUN_MG34, BFItems.GUN_MAUSER_M712, BFItems.GUN_GEWEHR_43, BFItems.GUN_COLT, BFItems.GUN_BERETTA_M1934, BFItems.GUN_M1928A1_THOMPSON, BFItems.GUN_BAR, BFItems.GUN_M1_GARAND, BFItems.GUN_M1_CARBINE});
    private final String field_3463 = "Role";
    private final AssetCommandBuilder command = new AssetCommandBuilder().subCommand("wp", new AssetCommandBuilder().subCommand("add", new AssetCommandBuilder((commandContext, stringArray) -> {
        Player player = (Player)((CommandSourceStack)commandContext.getSource()).source;
        FDSPose fDSPose = new FDSPose(player);
        this.weaponSpawns.add(fDSPose);
        CommandUtils.sendBfa((CommandSource)player, (Component)Component.literal((String)("Weapon spawn added. (" + this.weaponSpawns.size() + ")")));
    }).validator(AssetCommandValidators.ONLY_PLAYERS)).subCommand("clear", new AssetCommandBuilder((commandContext, stringArray) -> {
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Clearing all " + this.weaponSpawns.size() + " weapon spawns.")));
        this.weaponSpawns.clear();
    }))).subCommand("spawn", new AssetCommandBuilder().subCommand("add", new AssetCommandBuilder((commandContext, stringArray) -> {
        Player player = (Player)((CommandSourceStack)commandContext.getSource()).source;
        ((TroubleTownPlayerManager)this.getPlayerManager()).playerSpawns.add(new FDSPose(player));
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Player spawn added. (" + ((TroubleTownPlayerManager)this.getPlayerManager()).playerSpawns.size() + ")")));
    }).validator(AssetCommandValidators.ONLY_PLAYERS)).subCommand("clear", new AssetCommandBuilder((commandContext, stringArray) -> {
        ((TroubleTownPlayerManager)this.getPlayerManager()).playerSpawns.clear();
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)"Player spawns cleared."));
    })));
    public int field_3469 = 0;
    @NotNull
    public ObjectList<TTTDeadBody> field_3468 = new ObjectArrayList();
    @NotNull
    public ObjectList<RandomatEvent> activeRandomatEvents = new ObjectArrayList();

    public TroubleTownGame(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager) {
        super(bFAbstractManager, "ttt", "Trouble in Terrorist Town");
    }

    @NotNull
    public TroubleTownGameClient createGameClient(@NotNull BFClientManager bFClientManager) {
        return new TroubleTownGameClient(bFClientManager, this, (ClientPlayerDataHandler)this.dataHandler);
    }

    @Override
    @NotNull
    protected TroubleTownPlayerManager createPlayerManager() {
        return new TroubleTownPlayerManager(this, this.dataHandler);
    }

    @Override
    @NotNull
    public AbstractGameStage<TroubleTownGame, TroubleTownPlayerManager> createFirstStage() {
        return new TroubleTownIdleStage();
    }

    @Override
    @NotNull
    public Set<AbstractGame.ErrorType> getErrorTypes() {
        return EnumSet.of(AbstractGame.ErrorType.MAP_TYPE, AbstractGame.ErrorType.LOBBY);
    }

    @Override
    public void getErrorMessages(@NotNull List<MutableComponent> messages) {
        if (this.weaponSpawns.isEmpty()) {
            messages.add(Component.literal((String)("Weapon spawns in game '" + this.name + "' are missing.")));
        }
        if (((TroubleTownPlayerManager)this.playerManager).playerSpawns.isEmpty()) {
            messages.add(Component.literal((String)("Player spawns in game '" + this.name + "' are missing.")));
        }
        super.getErrorMessages(messages);
    }

    @Override
    public void updateStageManager(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull PlayerDataHandler<?> dataHandler, @NotNull ServerLevel level, @NotNull Set<UUID> players) {
        this.stageManager.update(manager, dataHandler, (TroubleTownPlayerManager)this.playerManager, level, players);
        for (RandomatEvent randomatEvent : this.activeRandomatEvents) {
            randomatEvent.update(this, dataHandler, (Level)level, players);
        }
    }

    @Override
    protected boolean isMatchSuccess() {
        return true;
    }

    public void method_3521(@NotNull Level level, @NotNull Set<UUID> set) {
        for (RandomatEvent randomatEvent : this.activeRandomatEvents) {
            randomatEvent.finish(this, level, set);
        }
        this.activeRandomatEvents.clear();
    }

    public void activateRandomat(@NotNull ServerPlayer player, @NotNull ServerLevel level, @NotNull Set<UUID> players) {
        RandomatEvent randomatEvent = RandomatRegistry.getRandomEvent();
        randomatEvent.activate(this, (Player)player, (Level)level, players);
        MutableComponent mutableComponent = Component.literal((String)player.getScoreboardName()).withStyle(ChatFormatting.GOLD);
        MutableComponent mutableComponent2 = Component.translatable((String)randomatEvent.getName()).withStyle(ChatFormatting.GOLD);
        MutableComponent mutableComponent3 = Component.translatable((String)"bf.message.gamemode.ttt.randomat.activate", (Object[])new Object[]{mutableComponent, mutableComponent2});
        BFUtils.playSound(players, (SoundEvent)BFSounds.ITEM_RANDOMAT_ACTIVATE_ALL.get(), SoundSource.MASTER);
        BFUtils.sendNoticeMessage(players, (Component)mutableComponent3);
        this.activeRandomatEvents.add((Object)randomatEvent);
    }

    @Override
    public boolean playerJoinTeam(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull TeamJoinType joinType, @NotNull ServerLevel level, @NotNull ServerPlayer player) {
        GameTeam gameTeam = ((TroubleTownPlayerManager)this.playerManager).getNextJoiningTeam();
        if (gameTeam == null) {
            BFLog.log("No team available for player " + player.getScoreboardName() + " to join.", new Object[0]);
            return false;
        }
        return ((TroubleTownPlayerManager)this.playerManager).playerJoinTeam(manager, joinType, level, player, gameTeam);
    }

    @Override
    public void specificReset(@Nullable Level level) {
        this.field_3469 = 0;
    }

    @Override
    public void writeAll(@NotNull ByteBuf buf, boolean writeMap) throws IOException {
        super.writeAll(buf, writeMap);
        buf.writeInt(this.field_3468.size());
        for (TTTDeadBody tTTDeadBody : this.field_3468) {
            tTTDeadBody.write(buf);
        }
    }

    @Override
    public void readAll(@NotNull ByteBuf buf) throws IOException {
        super.readAll(buf);
        this.field_3468.clear();
        int n = buf.readInt();
        for (int i = 0; i < n; ++i) {
            TTTDeadBody tTTDeadBody = new TTTDeadBody();
            tTTDeadBody.read(buf);
            this.field_3468.add((Object)tTTDeadBody);
        }
    }

    @Override
    public void writeSpecificFDS(@NotNull FDSTagCompound root) {
        ((TroubleTownPlayerManager)this.playerManager).playerSpawns.removeIf(Objects::isNull);
        this.weaponSpawns.removeIf(Objects::isNull);
        FDSPose.method_3009(root, "randomSpawns", ((TroubleTownPlayerManager)this.playerManager).playerSpawns);
        FDSPose.method_3009(root, "weaponSpawns", this.weaponSpawns);
    }

    @Override
    public void readSpecificFDS(@NotNull FDSTagCompound root) {
        ((TroubleTownPlayerManager)this.playerManager).playerSpawns.clear();
        ((TroubleTownPlayerManager)this.playerManager).playerSpawns.addAll(FDSPose.method_3008(root, "randomSpawns"));
        this.weaponSpawns.clear();
        this.weaponSpawns.addAll(FDSPose.method_3008(root, "weaponSpawns"));
    }

    @Override
    @NotNull
    public AssetCommandBuilder getCommand() {
        return this.baseCommand.inherit(this.command);
    }

    @Override
    public float getFireRateMultiplier() {
        return 2.0f;
    }

    @Override
    public boolean shouldRespawnAutomatically(@NotNull Player player) {
        return true;
    }

    @Override
    public int getMinimumPlayers() {
        return 2;
    }

    @Override
    public boolean shouldAnnounceRageQuits() {
        return false;
    }

    @Override
    public boolean shouldShowDeadMessages() {
        return false;
    }

    @Override
    @Nullable
    public CloudAchievement getVictoryAchievement() {
        return null;
    }

    @Override
    public boolean shouldUseStamina(@NotNull Player player) {
        return true;
    }

    @Override
    public int getMaximumPlayerSounds(@NotNull ServerPlayer serverPlayer) {
        return 8;
    }

    @Override
    public int getSoundboardCooldown() {
        return 160;
    }

    public void assignRoles(@NotNull ServerLevel level, @NotNull Set<UUID> players) {
        ObjectArrayList objectArrayList = new ObjectArrayList(players);
        Collections.shuffle(objectArrayList);
        int n = 1;
        int n2 = 1;
        boolean bl = false;
        if (players.size() >= 14) {
            n = 4;
            n2 = 2;
        }
        if (players.size() >= 12) {
            n = 3;
            n2 = 1;
        } else if (players.size() >= 8) {
            n = 2;
        }
        int n3 = n;
        int n4 = n2;
        MutableComponent mutableComponent = Component.literal((String)String.valueOf(n3)).withColor(13048088);
        MutableComponent mutableComponent2 = Component.literal((String)String.valueOf(n4)).withColor(1580540);
        MutableComponent mutableComponent3 = Component.translatable((String)"bf.message.gamemode.ttt.roles.all", (Object[])new Object[]{mutableComponent, mutableComponent2}).withStyle(ChatFormatting.GRAY);
        for (UUID uUID : objectArrayList) {
            ServerPlayer serverPlayer = BFUtils.getPlayerByUUID(uUID);
            if (serverPlayer == null) {
                return;
            }
            Object d = this.dataHandler.getPlayerData((Player)serverPlayer);
            if (BFUtils.isPlayerUnavailable((Player)serverPlayer, d)) continue;
            DeferredHolder<SoundEvent, SoundEvent> deferredHolder = BFSounds.MATCH_GAMEMODE_TTT_ROLE_INNOCENT;
            TTTPlayerRole tTTPlayerRole = TTTPlayerRole.INNOCENT;
            Loadout loadout = null;
            int n5 = 0;
            if (n > 0) {
                --n;
                tTTPlayerRole = TTTPlayerRole.TRAITOR;
                deferredHolder = BFSounds.MATCH_GAMEMODE_TTT_ROLE_TRAITOR;
                n5 = 1;
            } else if (n2 > 0) {
                --n2;
                tTTPlayerRole = TTTPlayerRole.DETECTIVE;
                deferredHolder = BFSounds.MATCH_GAMEMODE_TTT_ROLE_DETECTIVE;
                loadout = TroubleTownPlayerManager.DETECTIVE_LOADOUT;
                n5 = 2;
            } else if (!bl) {
                bl = true;
                tTTPlayerRole = TTTPlayerRole.JESTER;
                deferredHolder = BFSounds.MATCH_MEMES_LOL;
            }
            this.setRoleStat(uUID, tTTPlayerRole);
            BFUtils.sendFancyMessage(uUID, (Component)Component.empty(), (Component)Component.empty());
            BFUtils.sendNoticeMessage(uUID, tTTPlayerRole.getTitle());
            BFUtils.sendFancyMessage(uUID, (Component)Component.empty(), (Component)Component.empty());
            BFUtils.sendFancyMessage(uUID, (Component)Component.empty(), tTTPlayerRole.getDescription());
            BFUtils.sendFancyMessage(uUID, (Component)Component.empty(), (Component)Component.empty());
            BFUtils.sendFancyMessage(uUID, (Component)Component.empty(), (Component)mutableComponent3);
            BFUtils.sendFancyMessage(uUID, (Component)Component.empty(), (Component)Component.empty());
            BFUtils.setPlayerStat(this, uUID, BFStats.POINTS, n5);
            BFUtils.giveLoadout(level, serverPlayer, LOADOUT, true);
            if (loadout != null) {
                BFUtils.giveLoadout(level, serverPlayer, loadout, true);
            }
            BFUtils.playSound(uUID, (SoundEvent)deferredHolder.get(), SoundSource.MUSIC);
            BFUtils.playSound(uUID, (SoundEvent)BFSounds.MATCH_GAMEMODE_TTT_ROLE_REVEAL.get(), SoundSource.MUSIC);
        }
    }

    public void setRoleStat(@NotNull UUID player, @NotNull TTTPlayerRole role) {
        FDSTagCompound fDSTagCompound = this.getPlayerStatData(player);
        fDSTagCompound.setString(this.field_3463, role.getKey());
    }

    @CheckForNull
    public TTTPlayerTeam method_3514() {
        TroubleTownPlayerManager troubleTownPlayerManager = (TroubleTownPlayerManager)this.getPlayerManager();
        Set<UUID> set = troubleTownPlayerManager.method_2795();
        if (set.isEmpty()) {
            return TTTPlayerTeam.BAD;
        }
        TTTPlayerTeam tTTPlayerTeam = null;
        for (UUID uUID : set) {
            TTTPlayerRole tTTPlayerRole = this.getPlayerRole(uUID);
            if (tTTPlayerRole == null) continue;
            if (tTTPlayerTeam == null) {
                tTTPlayerTeam = tTTPlayerRole.getTeam();
                continue;
            }
            if (tTTPlayerRole.getTeam() == tTTPlayerTeam) continue;
            return null;
        }
        return tTTPlayerTeam;
    }

    public void method_3527(@NotNull ServerPlayer serverPlayer) {
        ObjectArrayList objectArrayList = new ObjectArrayList();
        for (TTTDeadBody tTTDeadBody : this.field_3468) {
            double d;
            if (tTTDeadBody.isIdentified() || !((d = (double)Mth.sqrt((float)((float)serverPlayer.distanceToSqr(tTTDeadBody.position)))) <= 2.0)) continue;
            objectArrayList.add(tTTDeadBody);
        }
        if (objectArrayList.isEmpty()) {
            MutableComponent mutableComponent = Component.translatable((String)"bf.message.gamemode.ttt.magnify.fail").withStyle(ChatFormatting.GRAY);
            BFUtils.sendNoticeMessage(serverPlayer, (Component)mutableComponent);
        } else {
            for (TTTDeadBody tTTDeadBody : objectArrayList) {
                tTTDeadBody.method_3510(this, (Player)serverPlayer);
            }
        }
    }

    public Set<UUID> method_3516(@NotNull TTTPlayerRole tTTPlayerRole, @NotNull Set<UUID> set) {
        ObjectOpenHashSet objectOpenHashSet = new ObjectOpenHashSet();
        for (UUID uUID : set) {
            if (!this.getPlayerRole(uUID).equals((Object)tTTPlayerRole)) continue;
            objectOpenHashSet.add(uUID);
        }
        return objectOpenHashSet;
    }

    public TTTPlayerRole getPlayerRole(@NotNull UUID player) {
        String string;
        FDSTagCompound fDSTagCompound = this.getPlayerStatData(player);
        if (fDSTagCompound.hasTag(this.field_3463) && (string = fDSTagCompound.getString(this.field_3463)) != null) {
            return TTTPlayerRole.fromKey(string);
        }
        return TTTPlayerRole.PENDING;
    }

    public boolean isPlayerInnocent(@NotNull UUID palyer) {
        return this.playerRoleEquals(palyer, TTTPlayerRole.INNOCENT.getKey());
    }

    public boolean isPlayerTraitor(@NotNull UUID player) {
        return this.playerRoleEquals(player, TTTPlayerRole.TRAITOR.getKey());
    }

    public boolean isPlayerDetective(@NotNull UUID player) {
        return this.playerRoleEquals(player, TTTPlayerRole.DETECTIVE.getKey());
    }

    private boolean playerRoleEquals(@NotNull UUID player, @NotNull String roleKey) {
        FDSTagCompound fDSTagCompound = this.getPlayerStatData(player);
        if (fDSTagCompound.hasTag(this.field_3463)) {
            String string = fDSTagCompound.getString(this.field_3463);
            return string != null && string.equals(roleKey);
        }
        return false;
    }

    @Override
    public float getDamageMultiplier() {
        return 0.5f;
    }

    @Override
    public boolean canUseConsumable(@NotNull Level level, @NotNull Player player, @NotNull BFConsumableItem bFConsumableItem, @NotNull ItemStack itemStack) {
        TTTPlayerRole tTTPlayerRole = this.getPlayerRole(player.getUUID());
        return tTTPlayerRole == TTTPlayerRole.DETECTIVE && (bFConsumableItem == BFItems.MAGNIFYING_GLASS.get() || bFConsumableItem == BFItems.RANDOMAT.get());
    }

    @Override
    public void method_3427(@NotNull ServerLevel serverLevel, @NotNull Player player, @NotNull BFConsumableItem bFConsumableItem, @NotNull ItemStack itemStack) {
        if (!(player instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer serverPlayer = (ServerPlayer)player;
        if (bFConsumableItem == BFItems.MAGNIFYING_GLASS.get()) {
            this.method_3527(serverPlayer);
        }
        if (bFConsumableItem == BFItems.RANDOMAT.get()) {
            itemStack.shrink(1);
            this.activateRandomat(serverPlayer, serverLevel, ((TroubleTownPlayerManager)this.getPlayerManager()).getPlayerUUIDs());
        }
    }

    public void method_3526(@NotNull ServerLevel serverLevel) {
        for (FDSPose fDSPose : this.weaponSpawns) {
            GunItem gunItem = (GunItem)RandomUtils.randomFromList(this.ITEM_DROPS).get();
            ItemEntity itemEntity = new ItemEntity((Level)serverLevel, fDSPose.position.x, fDSPose.position.y + 0.5, fDSPose.position.z, new ItemStack((ItemLike)gunItem));
            serverLevel.addFreshEntity((Entity)itemEntity);
        }
    }

    @Override
    public List<GameShopItem> getShopItems(@NotNull UUID player) {
        return this.getPlayerRole(player).getRoleItems();
    }

    @Override
    public BFStat getPointsStat() {
        return BFStats.POINTS;
    }

    @Override
    public MutableComponent method_3414() {
        MutableComponent mutableComponent = Component.literal((String)"M").withStyle(ChatFormatting.GRAY);
        return Component.translatable((String)"bf.message.gamemode.tip.store", (Object[])new Object[]{mutableComponent}).withColor(0xFFFFFF);
    }
}

