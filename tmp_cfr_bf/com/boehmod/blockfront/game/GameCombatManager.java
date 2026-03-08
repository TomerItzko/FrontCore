/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game;

import com.boehmod.bflib.cloud.common.player.achievement.CloudAchievements;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.entity.BotEntity;
import com.boehmod.blockfront.common.entity.InfectedEntity;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.item.MeleeItem;
import com.boehmod.blockfront.common.match.kill.KillFeedEntry;
import com.boehmod.blockfront.common.match.kill.KillMessage;
import com.boehmod.blockfront.common.match.kill.KillSectionAttribute;
import com.boehmod.blockfront.common.match.kill.KillSectionDistance;
import com.boehmod.blockfront.common.match.kill.KillSectionPlayer;
import com.boehmod.blockfront.common.match.kill.KillSectionWeapon;
import com.boehmod.blockfront.common.net.BFPopup;
import com.boehmod.blockfront.common.player.BFAbstractPlayerData;
import com.boehmod.blockfront.common.player.PlayerDataHandler;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.common.world.damage.BFDamageSource;
import com.boehmod.blockfront.common.world.damage.FirearmDamageSource;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.tag.IHasDominations;
import com.boehmod.blockfront.game.tag.IHasKillStreaks;
import com.boehmod.blockfront.game.tag.IUseKillIcons;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.StringUtils;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.lang.runtime.SwitchBootstraps;
import java.util.Calendar;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

public class GameCombatManager<G extends AbstractGame<G, ?, ?>> {
    private static final int field_2923 = 45;
    private static final int field_2925 = 5;
    private static final int field_2926 = 40;
    private static final int field_2927 = 40;
    public static final int field_2928 = 32;
    @NotNull
    private final G game;

    public GameCombatManager(@NotNull G game) {
        this.game = game;
    }

    public void method_2808(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull LivingDeathEvent livingDeathEvent, @NotNull Set<UUID> set) {
        IUseKillIcons iUseKillIcons;
        LivingEntity livingEntity = livingDeathEvent.getEntity();
        DamageSource damageSource = livingDeathEvent.getSource();
        Entity entity = damageSource.getEntity();
        Object obj = bFAbstractManager.getPlayerDataHandler();
        G g = this.game;
        if (g instanceof IUseKillIcons && (iUseKillIcons = (IUseKillIcons)g).method_3430()) {
            this.addKillFeedEntry(bFAbstractManager, (PlayerDataHandler<?>)obj, livingEntity, damageSource, true, true);
        }
        if (((AbstractGame)this.game).getStatus() != GameStatus.GAME) {
            return;
        }
        if (livingEntity instanceof ServerPlayer) {
            iUseKillIcons = (ServerPlayer)livingEntity;
            this.method_2803(bFAbstractManager, (PlayerDataHandler<?>)obj, (ServerPlayer)iUseKillIcons, entity, set);
        }
        if (entity instanceof ServerPlayer) {
            iUseKillIcons = (ServerPlayer)entity;
            this.method_2804(bFAbstractManager, (PlayerDataHandler<?>)obj, damageSource, (ServerPlayer)iUseKillIcons, iUseKillIcons.getUUID(), livingEntity, set);
        }
    }

    private void method_2803(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull ServerPlayer serverPlayer, @Nullable Entity entity, @NotNull Set<UUID> set) {
        UUID uUID = serverPlayer.getUUID();
        BFUtils.incrementPlayerStat(bFAbstractManager, this.game, uUID, BFStats.DEATHS);
        BFUtils.incrementPlayerStat(bFAbstractManager, this.game, uUID, BFStats.DEATHS_STREAK);
        if (this.game instanceof IHasKillStreaks) {
            this.method_5492(bFAbstractManager, entity, serverPlayer, uUID, set);
        }
        BFUtils.setPlayerStat(this.game, uUID, BFStats.KILLS_STREAK, 0);
        BFUtils.setPlayerStat(this.game, uUID, BFStats.KILLS_BOTS_STREAK, 0);
        BFUtils.setPlayerStat(this.game, uUID, BFStats.KILLS_INFECTED_STREAK, 0);
        Object obj = playerDataHandler.getPlayerData((Player)serverPlayer);
        ((BFAbstractPlayerData)obj).method_844(Calendar.getInstance().getTime().getTime());
    }

    private void method_5492(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @Nullable Entity entity, @NotNull ServerPlayer serverPlayer, UUID uUID, @NotNull Set<UUID> set) {
        int n = BFUtils.getPlayerStat(this.game, uUID, BFStats.KILLS_STREAK);
        if (n < 5) {
            return;
        }
        BFUtils.playSound(set, (SoundEvent)BFSounds.MATCH_GAMEMODE_TDM_STREAK_END.get(), SoundSource.MUSIC);
        BFUtils.awardAchievement(bFAbstractManager, uUID, CloudAchievements.ACH_USER_LOSE_STREAK);
        MutableComponent mutableComponent = Component.literal((String)serverPlayer.getScoreboardName()).withStyle(ChatFormatting.WHITE);
        MutableComponent mutableComponent2 = Component.literal((String)String.valueOf(n)).withStyle(ChatFormatting.WHITE);
        if (entity instanceof ServerPlayer) {
            ServerPlayer serverPlayer2 = (ServerPlayer)entity;
            if (serverPlayer2.equals((Object)serverPlayer)) {
                MutableComponent mutableComponent3 = Component.translatable((String)"bf.message.gamemode.combat.streak.ended.self", (Object[])new Object[]{mutableComponent, mutableComponent2}).withStyle(ChatFormatting.RED);
                BFUtils.sendNoticeMessage(set, (Component)mutableComponent3);
                BFUtils.awardAchievement(bFAbstractManager, uUID, CloudAchievements.ACH_USER_END_STREAK_SELF);
            } else {
                MutableComponent mutableComponent4 = Component.literal((String)serverPlayer2.getScoreboardName()).withStyle(ChatFormatting.WHITE);
                MutableComponent mutableComponent5 = Component.translatable((String)"bf.message.gamemode.combat.streak.ended", (Object[])new Object[]{mutableComponent4, mutableComponent, mutableComponent2}).withStyle(ChatFormatting.RED);
                BFUtils.sendNoticeMessage(set, (Component)mutableComponent5);
                BFUtils.awardAchievement(bFAbstractManager, serverPlayer2.getUUID(), CloudAchievements.ACH_USER_END_STREAK);
            }
        } else {
            MutableComponent mutableComponent6 = Component.translatable((String)"bf.message.gamemode.combat.streak.ended.other", (Object[])new Object[]{mutableComponent, mutableComponent2}).withStyle(ChatFormatting.RED);
            BFUtils.sendNoticeMessage(set, (Component)mutableComponent6);
        }
    }

    private void method_2804(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull DamageSource damageSource, @NotNull ServerPlayer serverPlayer, @NotNull UUID uUID, @NotNull LivingEntity livingEntity, @NotNull Set<UUID> set) {
        LivingEntity livingEntity2 = livingEntity;
        Objects.requireNonNull(livingEntity2);
        LivingEntity livingEntity3 = livingEntity2;
        int n = 0;
        switch (SwitchBootstraps.typeSwitch("typeSwitch", new Object[]{ServerPlayer.class, BotEntity.class, InfectedEntity.class}, (Object)livingEntity3, n)) {
            case 0: {
                boolean bl;
                BFDamageSource bFDamageSource;
                ServerPlayer serverPlayer2 = (ServerPlayer)livingEntity3;
                boolean bl2 = serverPlayer2.equals((Object)serverPlayer);
                if (bl2) break;
                Object obj = playerDataHandler.getPlayerData((Player)serverPlayer);
                BFUtils.incrementPlayerStat(bFAbstractManager, this.game, uUID, BFStats.KILLS);
                if (!((BFAbstractPlayerData)obj).isOutOfGame()) {
                    this.method_2807(bFAbstractManager, serverPlayer2, serverPlayer, uUID, set);
                }
                BFUtils.setPlayerStat(this.game, uUID, BFStats.DEATHS_STREAK, 0);
                ItemStack itemStack = serverPlayer.getMainHandItem();
                boolean bl3 = itemStack.getItem() instanceof MeleeItem;
                boolean bl4 = !(damageSource instanceof BFDamageSource) || (bFDamageSource = (BFDamageSource)damageSource).getItemUsing().equals(itemStack);
                boolean bl5 = BFUtils.isBehind(livingEntity, (LivingEntity)serverPlayer, 45.0f);
                if (bl5 && bl3 && bl4) {
                    BFUtils.incrementPlayerStat(bFAbstractManager, this.game, uUID, BFStats.BACK_STABS);
                }
                if (bl = damageSource.is(DamageTypeTags.IS_FIRE)) {
                    BFUtils.incrementPlayerStat(bFAbstractManager, this.game, uUID, BFStats.KILLS_FIRE);
                }
                ((AbstractGame)this.game).onFirstBlood(bFAbstractManager, serverPlayer, uUID);
                double d = Mth.sqrt((float)livingEntity.distanceTo((Entity)serverPlayer));
                if (!(d >= 40.0)) break;
                BFUtils.sendPopupMessage(serverPlayer, new BFPopup((Component)Component.translatable((String)"bf.popup.message.longshot", (Object[])new Object[]{(int)d + "m"}).withStyle(ChatFormatting.YELLOW), 40));
                BFUtils.triggerPlayerStat(bFAbstractManager, this.game, uUID, BFStats.SCORE, 3);
                break;
            }
            case 1: {
                BotEntity botEntity = (BotEntity)livingEntity3;
                BFUtils.incrementPlayerStat(bFAbstractManager, this.game, uUID, BFStats.KILLS_BOTS);
                BFUtils.incrementPlayerStat(bFAbstractManager, this.game, uUID, BFStats.KILLS_BOTS_STREAK);
                break;
            }
            case 2: {
                InfectedEntity infectedEntity = (InfectedEntity)livingEntity3;
                BFUtils.incrementPlayerStat(bFAbstractManager, this.game, uUID, BFStats.KILLS_INFECTED);
                BFUtils.incrementPlayerStat(bFAbstractManager, this.game, uUID, BFStats.KILLS_INFECTED_STREAK);
                break;
            }
        }
    }

    private void method_2807(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull ServerPlayer serverPlayer, @NotNull ServerPlayer serverPlayer2, @NotNull UUID uUID, @NotNull Set<UUID> set) {
        int n;
        BFUtils.incrementPlayerStat(bFAbstractManager, this.game, uUID, BFStats.KILLS_STREAK);
        if (this.game instanceof IHasKillStreaks && (n = BFUtils.getPlayerStat(this.game, uUID, BFStats.KILLS_STREAK)) != 0 && n % 5 == 0) {
            DeferredHolder<SoundEvent, SoundEvent> deferredHolder = switch (n) {
                case 5 -> BFSounds.MATCH_GAMEMODE_TDM_STREAK_5;
                case 10 -> BFSounds.MATCH_GAMEMODE_TDM_STREAK_10;
                case 15 -> BFSounds.MATCH_GAMEMODE_TDM_STREAK_15;
                case 20 -> BFSounds.MATCH_GAMEMODE_TDM_STREAK_20;
                case 25 -> BFSounds.MATCH_GAMEMODE_TDM_STREAK_25;
                case 30 -> BFSounds.MATCH_GAMEMODE_TDM_STREAK_30;
                default -> BFSounds.MATCH_GAMEMODE_TDM_STREAK_MAX;
            };
            MutableComponent mutableComponent = Component.literal((String)serverPlayer2.getScoreboardName()).withStyle(ChatFormatting.WHITE);
            MutableComponent mutableComponent2 = Component.literal((String)StringUtils.formatLong(n)).withStyle(ChatFormatting.WHITE);
            MutableComponent mutableComponent3 = Component.literal((String)serverPlayer.getScoreboardName()).withStyle(ChatFormatting.WHITE);
            MutableComponent mutableComponent4 = Component.translatable((String)"bf.message.gamemode.combat.streak.increased", (Object[])new Object[]{mutableComponent, mutableComponent2, mutableComponent3}).withStyle(ChatFormatting.RED);
            BFUtils.sendNoticeMessage(set, (Component)mutableComponent4);
            BFUtils.playSound(set, (SoundEvent)deferredHolder.get(), SoundSource.MUSIC);
            BFUtils.triggerPlayerStat(bFAbstractManager, this.game, uUID, BFStats.SCORE, n);
        }
    }

    public void addKillFeedEntry(@NotNull BFAbstractManager<?, ?, ?> manager, @NotNull PlayerDataHandler<?> dataHandler, @NotNull LivingEntity entity, @NotNull DamageSource source, boolean broadcast, boolean sendKilledMessage) {
        Object object;
        Object object2;
        UUID uUID;
        Object object3;
        Object object4;
        Object p = ((AbstractGame)this.game).getPlayerManager();
        KillFeedEntry killFeedEntry = new KillFeedEntry();
        MutableComponent mutableComponent = Component.literal((String)entity.getScoreboardName());
        MutableComponent mutableComponent2 = null;
        ObjectArrayList objectArrayList = new ObjectArrayList();
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = false;
        boolean bl4 = false;
        int n = 0;
        ItemStack itemStack = null;
        Entity entity2 = source.getEntity();
        if (entity2 instanceof LivingEntity) {
            object4 = (LivingEntity)entity2;
            itemStack = object4.getMainHandItem();
            mutableComponent2 = Component.literal((String)object4.getScoreboardName());
            if (object4 instanceof ServerPlayer) {
                object3 = (ServerPlayer)object4;
                uUID = object3.getUUID();
                GameTeam gameTeam = ((AbstractGamePlayerManager)p).getPlayerTeam(uUID);
                if (gameTeam != null) {
                    mutableComponent2.setStyle(gameTeam.getStyleText());
                }
                if (this.game instanceof IHasDominations && entity instanceof ServerPlayer) {
                    object2 = (ServerPlayer)entity;
                    UUID uUID2 = object2.getUUID();
                    if (((AbstractGame)this.game).getDominatingManager().playerIsDominated(uUID2, uUID)) {
                        objectArrayList.add(new KillSectionAttribute("dominating"));
                    }
                }
                killFeedEntry.method_3218(uUID);
            } else if (object4 instanceof BotEntity && ((BotEntity)(object = (BotEntity)object4)).getTeam() != null) {
                mutableComponent2.setStyle(((BotEntity)object).getTeam().getStyleText());
            }
        }
        if (source instanceof FirearmDamageSource) {
            object4 = (FirearmDamageSource)source;
            bl = ((FirearmDamageSource)((Object)object4)).isHeadshot();
            bl2 = ((FirearmDamageSource)((Object)object4)).isWallbang();
            bl4 = ((FirearmDamageSource)((Object)object4)).isNoScope();
            itemStack = ((BFDamageSource)((Object)object4)).getItemUsing();
            bl3 = ((FirearmDamageSource)((Object)object4)).isCollateral();
            if (entity2 instanceof ServerPlayer) {
                object = (ServerPlayer)entity2;
                if (entity instanceof ServerPlayer) {
                    uUID = object.getUUID();
                    if (bl) {
                        BFUtils.incrementPlayerStat(manager, this.game, uUID, BFStats.HEAD_SHOTS);
                    }
                    if (bl4) {
                        BFUtils.incrementPlayerStat(manager, this.game, uUID, BFStats.NO_SCOPES);
                    }
                }
            }
        } else if (source instanceof BFDamageSource) {
            object3 = (BFDamageSource)source;
            itemStack = object3.getItemUsing();
        }
        if (entity instanceof ServerPlayer) {
            object4 = (ServerPlayer)entity;
            object3 = object4.getUUID();
            object = ((AbstractGamePlayerManager)p).getPlayerTeam((UUID)object3);
            if (object != null) {
                mutableComponent.setStyle(((GameTeam)object).getStyleText());
            }
            killFeedEntry.method_3217((UUID)object3);
        }
        if (entity instanceof BotEntity && ((BotEntity)(object4 = (BotEntity)entity)).getTeam() != null) {
            mutableComponent.setStyle(((BotEntity)object4).getTeam().getStyleText());
        }
        if (sendKilledMessage && mutableComponent2 != null && entity2 instanceof ServerPlayer) {
            object4 = (ServerPlayer)entity2;
            if (entity instanceof ServerPlayer) {
                object3 = (ServerPlayer)entity;
                object = object4.getUUID();
                uUID = object4.getMainHandItem();
                float f = object4.getHealth();
                object2 = new KillMessage((UUID)object, (ItemStack)uUID, f);
                BFUtils.sendKilledMessagePacket((ServerPlayer)object3, (KillMessage)object2);
            }
        }
        if (mutableComponent2 == null) {
            mutableComponent2 = Component.literal((String)"World").withStyle(ChatFormatting.GRAY);
        }
        killFeedEntry.addSection(new KillSectionPlayer((Component)mutableComponent2));
        this.method_2805(manager, dataHandler, entity, killFeedEntry, entity2);
        if (itemStack != null && !itemStack.isEmpty()) {
            killFeedEntry.addSection(new KillSectionWeapon(itemStack));
        }
        if (bl4) {
            objectArrayList.add(new KillSectionAttribute("noscope"));
        }
        if (bl2) {
            objectArrayList.add(new KillSectionAttribute("wallbang"));
        }
        if (bl3) {
            if (bl) {
                objectArrayList.add(new KillSectionAttribute("collateral_headshot"));
            } else {
                objectArrayList.add(new KillSectionAttribute("collateral"));
            }
        }
        if (bl) {
            objectArrayList.add(new KillSectionAttribute("headshot"));
        }
        if (source.is(DamageTypeTags.IS_FIRE)) {
            objectArrayList.add(new KillSectionAttribute("fire"));
        }
        if (!objectArrayList.isEmpty()) {
            objectArrayList.forEach(killFeedEntry::addSection);
        } else if (itemStack == null || itemStack.isEmpty()) {
            killFeedEntry.addSection(new KillSectionAttribute("generic"));
        }
        killFeedEntry.addSection(new KillSectionPlayer((Component)mutableComponent));
        object3 = source.getEntity();
        if (object3 instanceof ServerPlayer) {
            object4 = (ServerPlayer)object3;
            if (itemStack != null && itemStack.getItem() instanceof GunItem) {
                n = (int)object4.distanceTo((Entity)entity);
            }
        }
        if (n >= 32) {
            object4 = Component.literal((String)(" " + StringUtils.formatLong(n) + "m")).withStyle(ChatFormatting.YELLOW);
            killFeedEntry.addSection(new KillSectionDistance((Component)object4));
        }
        if (broadcast) {
            BFUtils.broadcastKillFeedEntry(p, killFeedEntry);
        }
    }

    private void method_2805(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager, @NotNull PlayerDataHandler<?> playerDataHandler, @NotNull LivingEntity livingEntity, @NotNull KillFeedEntry killFeedEntry, @Nullable Entity entity) {
        if (!(livingEntity instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer serverPlayer = (ServerPlayer)livingEntity;
        UUID uUID = serverPlayer.getUUID();
        Object obj = playerDataHandler.getPlayerData((Player)serverPlayer);
        Object2FloatOpenHashMap object2FloatOpenHashMap = new Object2FloatOpenHashMap(((BFAbstractPlayerData)obj).method_857());
        object2FloatOpenHashMap.remove(uUID);
        if (entity != null) {
            object2FloatOpenHashMap.remove(entity.getUUID());
        }
        if (object2FloatOpenHashMap.isEmpty()) {
            return;
        }
        ObjectArrayList objectArrayList = new ObjectArrayList(object2FloatOpenHashMap.entrySet());
        objectArrayList.sort(Map.Entry.comparingByValue());
        if (objectArrayList.isEmpty()) {
            return;
        }
        UUID uUID2 = (UUID)((Map.Entry)objectArrayList.getFirst()).getKey();
        ServerPlayer serverPlayer2 = BFUtils.getPlayerByUUID(uUID2);
        if (serverPlayer2 == null) {
            return;
        }
        Style style = Style.EMPTY.withColor(ChatFormatting.GRAY);
        GameTeam gameTeam = ((AbstractGamePlayerManager)((AbstractGame)this.game).getPlayerManager()).getPlayerTeam(uUID2);
        if (gameTeam != null) {
            style = gameTeam.getStyleText();
        }
        MutableComponent mutableComponent = Component.literal((String)" +").withColor(0xFFFFFF);
        MutableComponent mutableComponent2 = Component.literal((String)serverPlayer2.getScoreboardName()).withStyle(style);
        killFeedEntry.addSection(new KillSectionDistance((Component)mutableComponent));
        killFeedEntry.addSection(new KillSectionPlayer((Component)mutableComponent2));
        if (((AbstractGame)this.game).getStatus() == GameStatus.GAME) {
            BFUtils.incrementPlayerStat(bFAbstractManager, this.game, uUID2, BFStats.ASSISTS);
        }
    }
}

