/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.cloud.common.CloudRegistry
 *  com.boehmod.bflib.cloud.common.RequestType
 *  com.boehmod.bflib.cloud.common.item.CloudItem
 *  com.boehmod.bflib.cloud.common.item.CloudItemStack
 *  com.boehmod.bflib.cloud.packet.IPacket
 *  com.boehmod.bflib.common.ColorReferences
 *  com.boehmod.bflib.fds.tag.FDSTagCompound
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.PoseStack
 *  io.netty.buffer.ByteBuf
 *  it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  it.unimi.dsi.fastutil.objects.ObjectList
 *  javax.annotation.Nullable
 *  javax.annotation.OverridingMethodsMustInvokeSuper
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Camera
 *  net.minecraft.client.CameraType
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.OptionInstance
 *  net.minecraft.client.Options
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.ChatComponent
 *  net.minecraft.client.gui.components.DebugScreenOverlay
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.multiplayer.ClientPacketListener
 *  net.minecraft.client.multiplayer.PlayerInfo
 *  net.minecraft.client.player.AbstractClientPlayer
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.MultiBufferSource$BufferSource
 *  net.minecraft.client.renderer.culling.Frustum
 *  net.minecraft.client.renderer.entity.EntityRenderDispatcher
 *  net.minecraft.client.resources.sounds.SimpleSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.core.BlockPos
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.FormattedText
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.chat.Style
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.util.Mth
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.entity.HumanoidArm
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.client.event.RenderLevelStageEvent
 *  net.neoforged.neoforge.client.event.RenderNameTagEvent
 *  org.apache.commons.lang3.function.TriFunction
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game;

import com.boehmod.bflib.cloud.common.CloudRegistry;
import com.boehmod.bflib.cloud.common.RequestType;
import com.boehmod.bflib.cloud.common.item.CloudItem;
import com.boehmod.bflib.cloud.common.item.CloudItemStack;
import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.ac.BFClientAntiCheat;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.game.element.ClientGameElement;
import com.boehmod.blockfront.client.render.game.scoreboard.ScoreboardHeader;
import com.boehmod.blockfront.client.render.minimap.MinimapWaypoint;
import com.boehmod.blockfront.client.screen.match.AbstractMatchSelectClassScreen;
import com.boehmod.blockfront.client.screen.match.MatchSelectClassScreen;
import com.boehmod.blockfront.client.screen.match.PlayerItemShopScreen;
import com.boehmod.blockfront.client.screen.match.summary.MatchMapVoteSummaryScreen;
import com.boehmod.blockfront.client.screen.match.summary.MatchRankSummaryScreen;
import com.boehmod.blockfront.client.screen.match.summary.MatchSummaryScreen;
import com.boehmod.blockfront.client.screen.match.summary.MatchTopPlayersSummaryScreen;
import com.boehmod.blockfront.client.screen.title.LobbyTitleScreen;
import com.boehmod.blockfront.client.settings.BFClientSettings;
import com.boehmod.blockfront.cloud.PlayerCloudInventory;
import com.boehmod.blockfront.cloud.client.ClientConnectionManager;
import com.boehmod.blockfront.cloud.common.CloudRequestManager;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.match.BFCountry;
import com.boehmod.blockfront.common.match.DivisionData;
import com.boehmod.blockfront.common.match.Loadout;
import com.boehmod.blockfront.common.match.MatchClass;
import com.boehmod.blockfront.common.match.kill.KillFeedEntry;
import com.boehmod.blockfront.common.match.kill.KillMessage;
import com.boehmod.blockfront.common.net.BFPopup;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.common.stat.BFStat;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractCapturePoint;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameCinematics;
import com.boehmod.blockfront.game.GameRadioPoint;
import com.boehmod.blockfront.game.GameSequence;
import com.boehmod.blockfront.game.GameStageTimer;
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.game.GameSummaryManager;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.impl.conq.ConquestCapturePoint;
import com.boehmod.blockfront.game.impl.conq.ConquestGame;
import com.boehmod.blockfront.game.impl.conq.VehicleSpawn;
import com.boehmod.blockfront.game.tag.IHasClasses;
import com.boehmod.blockfront.game.tag.IHasItemShop;
import com.boehmod.blockfront.map.MapEnvironment;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.FormatUtils;
import com.boehmod.blockfront.util.StringUtils;
import com.boehmod.blockfront.util.math.FDSPose;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.UUID;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractGameClient<G extends AbstractGame<G, P, ?>, P extends AbstractGamePlayerManager<G>> {
    private static final int field_2895 = 200;
    private static final ResourceLocation STOPWATCH_TEXTURE = BFRes.loc("textures/gui/stopwatch.png");
    private static final ResourceLocation PLAYER_ICON_TEXTURE = BFRes.loc("textures/gui/menu/icons/player.png");
    private static final ResourceLocation FRIENDS_ICON_TEXTURE = BFRes.loc("textures/gui/menu/icons/friends.png");
    private static final int field_2896 = 8;
    private static final Component field_2888 = Component.translatable((String)"bf.message.match.title.waiting");
    private static final int field_2897 = 3;
    private static final int field_2898 = 350;
    private static final int field_2899 = 25;
    private static final int field_2900 = 23;
    private boolean field_2872 = true;
    private static final Component field_2890 = Component.translatable((String)"bf.message.debug.hitbox").withStyle(ChatFormatting.RED);
    private static final Component field_2892 = Component.translatable((String)"bf.message.debug.overlay").withStyle(ChatFormatting.RED);
    private static final Component field_2893 = Component.translatable((String)"bf.message.right.hand").withStyle(ChatFormatting.RED);
    private static final Component field_2894 = Component.translatable((String)"bf.message.shader").withStyle(ChatFormatting.RED);
    @NotNull
    protected final G game;
    private static final int field_2901 = 220;
    private int field_2902 = 220;
    @NotNull
    private final List<Component> tips = this.getTips();
    @NotNull
    private final List<KillFeedEntry> killFeedEntries = new ObjectArrayList();
    @NotNull
    private final List<ClientGameElement<G, P>> gameElements;
    @NotNull
    private final List<BFPopup> popups = new ObjectArrayList();
    @NotNull
    private final List<MinimapWaypoint> minimapWaypoints = new ObjectArrayList();
    @NotNull
    private final Scoreboard scoreboard;
    @NotNull
    private final List<GameRadioPoint> radioPoints = new ObjectArrayList();
    @NotNull
    private final GameStageTimer stageTimer = new GameStageTimer();
    private final ScoreboardHeader scoreboardHeader = new ScoreboardHeader();
    public int field_2903 = 0;
    private int field_2883 = 0;
    private int field_2884 = 0;
    public float field_2877;
    public float field_2878 = 0.0f;
    public float field_2879;
    public float field_2880 = 0.0f;
    private int field_2885 = 0;
    protected int field_2886 = 0;
    private int field_2887 = 0;
    @Nullable
    private KillMessage killMessage = null;
    private float field_2881;
    private float field_2882 = 20.0f;
    private boolean field_2873 = false;
    @NotNull
    protected final ClientPlayerDataHandler dataHandler;
    @NotNull
    private final GameSummaryManager summaryManager;

    @NotNull
    protected abstract List<Component> getTips();

    public AbstractGameClient(@NotNull BFClientManager manager, @NotNull G game, @NotNull ClientPlayerDataHandler dataHandler) {
        this.game = game;
        this.dataHandler = dataHandler;
        this.scoreboard = this.getScoreboard();
        this.summaryManager = new GameSummaryManager(manager, this);
        this.gameElements = this.getGameElements();
    }

    private static float method_2714(@NotNull LocalPlayer localPlayer) {
        return localPlayer.isShiftKeyDown() ? 0.4f : 0.3f;
    }

    @NotNull
    protected abstract List<ClientGameElement<G, P>> getGameElements();

    public abstract boolean method_2713(@NotNull AbstractClientPlayer var1);

    public float method_2675() {
        return this.field_2881;
    }

    @NotNull
    public GameStageTimer method_2678() {
        return this.stageTimer;
    }

    public float method_2743(float f) {
        return MathUtils.lerpf1(this.field_2881, this.field_2882, f);
    }

    public void method_2731(float f) {
        this.field_2881 -= f;
        if (this.field_2881 < 0.0f) {
            this.field_2881 = 0.0f;
        }
    }

    @NotNull
    public Collection<MinimapWaypoint> method_2727() {
        return Collections.unmodifiableList(this.minimapWaypoints);
    }

    @OverridingMethodsMustInvokeSuper
    public void update(@NotNull Minecraft minecraft, @NotNull Random random, @NotNull RandomSource randomSource, @NotNull LocalPlayer player, @NotNull ClientLevel level, @NotNull BFClientManager manager, @NotNull BFClientPlayerData playerData, @NotNull Set<UUID> players, float f, @NotNull Vec3 vec3, @NotNull BlockPos blockPos) {
        if (this.method_2707(minecraft, player, level, manager, playerData)) {
            return;
        }
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(() -> manager.setGameShader((AbstractGame<?, ?, ?>)this.game));
        } else {
            manager.setGameShader((AbstractGame<?, ?, ?>)this.game);
        }
        this.method_2728(minecraft, player);
        this.method_2705(minecraft, player);
        this.scoreboardHeader.onUpdate();
        this.advanceSummary(minecraft);
        this.updateGameElements(minecraft, player);
        this.field_2880 = this.field_2879;
        boolean bl = playerData.isOutOfGame() && playerData.getRespawnTimer() <= 20;
        this.field_2879 = MathUtils.moveTowards(this.field_2879, bl ? 1.0f : 0.0f, bl ? 0.05f : 0.15f);
        if (!manager.getCinematics().isSequencePlaying()) {
            this.method_2739(minecraft);
        }
        this.method_2732(player);
        this.method_2733(player);
        if (this.killMessage != null) {
            this.killMessage.onUpdate();
        }
        this.popups.removeIf(BFPopup::method_3812);
        this.killFeedEntries.removeIf(killFeedEntry -> killFeedEntry.method_3209(minecraft, (AbstractGame<?, ?, ?>)this.game));
        this.method_2708(minecraft, player, level, players);
        this.method_2734(player);
        this.radioPoints.removeIf(gameRadioPoint -> gameRadioPoint.method_3180(player));
        this.updateMapEnvironment(minecraft, player, level, manager, random, randomSource, f, vec3, blockPos);
        this.method_2681(manager, players);
    }

    private void method_2705(@NotNull Minecraft minecraft, @NotNull LocalPlayer localPlayer) {
        if (((AbstractGame)this.game).status == GameStatus.POST_GAME) {
            return;
        }
        if (this.field_2902-- > 0) {
            return;
        }
        this.field_2902 = 220;
        if (this.tips.isEmpty()) {
            return;
        }
        Component component = this.tips.removeFirst();
        GameTeam gameTeam = ((AbstractGamePlayerManager)((AbstractGame)this.game).playerManager).getPlayerTeam(localPlayer.getUUID());
        if (gameTeam != null) {
            MutableComponent mutableComponent = Component.literal((String)"\ue013").withColor(0xFFFFFF);
            MutableComponent mutableComponent2 = Component.literal((String)" Tip: ").withStyle(ChatFormatting.DARK_GRAY);
            MutableComponent mutableComponent3 = component.copy().withStyle(ChatFormatting.GRAY);
            ChatComponent chatComponent = minecraft.gui.getChat();
            chatComponent.addMessage((Component)Component.empty());
            chatComponent.addMessage((Component)mutableComponent.append((Component)mutableComponent2).append((Component)mutableComponent3));
            chatComponent.addMessage((Component)Component.empty());
            minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.MATCH_TIP.get()), (float)1.0f));
        }
    }

    private void method_2732(@NotNull LocalPlayer localPlayer) {
        this.field_2878 = this.field_2877;
        --this.field_2884;
        this.field_2877 = BFClientSettings.UI_CONSTANT_HOTBAR.isEnabled() ? 0.0f : (this.field_2884 <= 0 ? Math.min(this.field_2877 + 0.3f, 1.0f) : Math.max(this.field_2877 - 0.2f, 0.0f));
        Inventory inventory = localPlayer.getInventory();
        if (this.field_2883 != inventory.selected) {
            this.field_2883 = inventory.selected;
            this.field_2884 = 30;
        }
    }

    private void method_2681(@NotNull BFClientManager bFClientManager, @NotNull Set<UUID> set) {
        if (this.field_2886++ >= 200) {
            this.field_2886 = 0;
            CloudRequestManager cloudRequestManager = ((ClientConnectionManager)bFClientManager.getConnectionManager()).getRequester();
            set.forEach(uUID -> {
                cloudRequestManager.push(RequestType.PLAYER_DATA, (UUID)uUID);
                cloudRequestManager.push(RequestType.PLAYER_INVENTORY, (UUID)uUID);
                cloudRequestManager.push(RequestType.PLAYER_INVENTORY_DEFAULTS, (UUID)uUID);
                cloudRequestManager.push(RequestType.PLAYER_INVENTORY_SHOWCASE, (UUID)uUID);
            });
        }
    }

    private void method_2739(@NotNull Minecraft minecraft) {
        IHasClasses iHasClasses;
        UUID uUID = minecraft.getUser().getProfileId();
        Object object = this.game;
        if (object instanceof IHasClasses && (iHasClasses = (IHasClasses)object).method_3404()) {
            boolean bl;
            object = ((AbstractGamePlayerManager)((AbstractGame)this.game).playerManager).getPlayerTeam(Objects.requireNonNull(uUID));
            FDSTagCompound fDSTagCompound = ((AbstractGame)this.game).getPlayerStatData(uUID);
            boolean bl2 = bl = fDSTagCompound.getInteger(BFStats.CLASS.getKey(), -1) != -1;
            if (this.field_2903 > 0) {
                --this.field_2903;
            }
            if (object != null && minecraft.screen == null && !bl && this.field_2903 <= 0) {
                Screen screen = this.method_2686((GameTeam)object);
                minecraft.setScreen(screen);
                this.field_2903 = 5;
            }
        }
    }

    private void advanceSummary(@NotNull Minecraft minecraft) {
        if (this.summaryManager.hasScreens()) {
            this.summaryManager.advanceScreen(minecraft);
        }
    }

    private void updateGameElements(@NotNull Minecraft minecraft, @NotNull LocalPlayer localPlayer) {
        for (ClientGameElement clientGameElement : this.gameElements) {
            clientGameElement.update(minecraft, this.game, ((AbstractGame)this.game).playerManager, this, localPlayer);
        }
    }

    private void updateMapEnvironment(@NotNull Minecraft minecraft, @NotNull LocalPlayer player, @NotNull ClientLevel level, @NotNull BFClientManager manager, @NotNull Random random, @NotNull RandomSource randomSource, float delta, @NotNull Vec3 cameraPos, @NotNull BlockPos cameraBlockPos) {
        MapEnvironment mapEnvironment = ((AbstractGame)this.game).getMapEnvironment();
        mapEnvironment.updateClient(minecraft, manager, (AbstractGame<?, ?, ?>)this.game, player, level, random, randomSource, delta, cameraPos, cameraBlockPos);
    }

    private void method_2708(@NotNull Minecraft minecraft, @NotNull LocalPlayer localPlayer, @NotNull ClientLevel clientLevel, @NotNull Set<UUID> set) {
        if (this.field_2887-- > 0) {
            return;
        }
        this.field_2887 = 20;
        this.minimapWaypoints.clear();
        this.minimapWaypoints.addAll(this.getMinimapWaypoints(minecraft, set, localPlayer, clientLevel));
    }

    private boolean method_2707(@NotNull Minecraft minecraft, @NotNull LocalPlayer localPlayer, @NotNull ClientLevel clientLevel, @NotNull BFClientManager bFClientManager, @NotNull BFClientPlayerData bFClientPlayerData) {
        if (this.method_2720() && ((BFClientAntiCheat)bFClientManager.getAntiCheat()).isActive()) {
            bFClientManager.resetGameState(minecraft, bFClientPlayerData, this.dataHandler, localPlayer, clientLevel);
            clientLevel.disconnect();
            minecraft.disconnect((Screen)new LobbyTitleScreen());
            return true;
        }
        return false;
    }

    private void method_2728(@NotNull Minecraft minecraft, @NotNull LocalPlayer localPlayer) {
        Options options = minecraft.options;
        if (!(options.getCameraType().isFirstPerson() || this.method_2730((Player)localPlayer) || localPlayer.isCreative())) {
            options.setCameraType(CameraType.FIRST_PERSON);
        }
    }

    @NotNull
    protected Screen method_2686(@NotNull GameTeam gameTeam) {
        return new MatchSelectClassScreen<G, AbstractGameClient>(null, gameTeam, this.game, this);
    }

    private void method_2733(@NotNull LocalPlayer localPlayer) {
        this.field_2882 = this.field_2881;
        if (((AbstractGame)this.game).shouldUseStamina((Player)localPlayer)) {
            if (this.field_2881 <= 0.0f) {
                localPlayer.setSprinting(false);
                localPlayer.getFoodData().setFoodLevel(1);
            } else if (this.field_2881 > 5.0f) {
                localPlayer.getFoodData().setFoodLevel(20);
            }
            if (localPlayer.isSprinting()) {
                if (this.field_2881 > 0.0f) {
                    this.field_2881 -= this.method_2726(localPlayer);
                }
            } else if ((double)this.field_2881 < 20.0) {
                this.field_2881 += AbstractGameClient.method_2714(localPlayer);
            } else if ((double)this.field_2881 > 20.0) {
                this.field_2881 = 20.0f;
            }
        } else {
            this.field_2881 = 20.0f;
            localPlayer.getFoodData().setFoodLevel(20);
        }
    }

    protected float method_2726(@NotNull LocalPlayer localPlayer) {
        return 0.1f;
    }

    private void method_2734(@NotNull LocalPlayer localPlayer) {
        Inventory inventory = localPlayer.getInventory();
        int n = inventory.selected;
        if (BFUtils.method_2998((Player)localPlayer) || !this.method_2719()) {
            return;
        }
        boolean bl = inventory.getItem(n).isEmpty();
        if (this.field_2885 != n || bl) {
            if (bl) {
                int n2 = n;
                if (this.field_2885 > n || this.field_2885 == 0 && n == 8) {
                    while ((n2 = (n2 - 1 + 9) % 9) != n && inventory.getItem(n2).isEmpty()) {
                    }
                } else {
                    while ((n2 = (n2 + 1) % 9) != n && inventory.getItem(n2).isEmpty()) {
                    }
                }
                if (!inventory.getItem(n2).isEmpty()) {
                    inventory.selected = n2;
                }
            }
            if (!localPlayer.getMainHandItem().isEmpty() && !inventory.getItem(this.field_2885).isEmpty()) {
                GunItem.field_4019 = false;
            }
        }
        this.field_2885 = inventory.selected;
    }

    public void render(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull LocalPlayer player, @NotNull ClientLevel level, @NotNull BFClientPlayerData playerData, @NotNull GuiGraphics graphics, @NotNull Font font, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, @NotNull Set<UUID> players, int width, int height, int midX, int midY, float renderTime, float delta) {
        GameSequence gameSequence;
        GameCinematics gameCinematics = manager.getCinematics();
        gameCinematics.method_2210(minecraft, poseStack, graphics, font, width, height, delta);
        if (gameCinematics.isSequencePlaying() && (gameSequence = gameCinematics.getSequence()) != null && !gameSequence.method_2177()) {
            return;
        }
        this.renderWaitingText(poseStack, graphics, font, players, midX);
        this.renderSpecific(minecraft, manager, player, level, playerData, graphics, font, poseStack, bufferSource, players, width, height, midX, midY, renderTime, delta);
        this.renderGameElements(graphics, font, poseStack, midX, delta);
    }

    public void method_2692(@NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, BFClientPlayerData bFClientPlayerData, float f, float f2, int n) {
        ResourceLocation resourceLocation = bFClientPlayerData.method_1154();
        if (resourceLocation != null) {
            poseStack.pushPose();
            poseStack.translate(f, f2, 0.0f);
            guiGraphics.blit(resourceLocation, 0, 0, n, n, 8.0f, 8.0f, 8, 8, 64, 64);
            guiGraphics.blit(resourceLocation, 0, 0, n, n, 40.0f, 8.0f, 8, 8, 64, 64);
            poseStack.popPose();
        }
    }

    private void renderGameElements(@NotNull GuiGraphics guiGraphics, @NotNull Font font, @NotNull PoseStack poseStack, int n, float f) {
        int n2 = this.gameElements.stream().mapToInt(clientGameElement -> clientGameElement.method_490(font)).sum() + 3 * (this.gameElements.size() - 1);
        int n3 = n - n2 / 2;
        for (ClientGameElement<G, P> clientGameElement2 : this.gameElements) {
            clientGameElement2.render(guiGraphics, poseStack, font, n3, 4, f);
            n3 += clientGameElement2.method_490(font) + 3;
        }
    }

    public abstract void renderSpecific(@NotNull Minecraft var1, @NotNull BFClientManager var2, @NotNull LocalPlayer var3, @NotNull ClientLevel var4, @NotNull BFClientPlayerData var5, @NotNull GuiGraphics var6, @NotNull Font var7, @NotNull PoseStack var8, @NotNull MultiBufferSource var9, @NotNull Set<UUID> var10, int var11, int var12, int var13, int var14, float var15, float var16);

    public void method_2704(Minecraft minecraft, @NotNull LocalPlayer localPlayer) {
        OptionInstance optionInstance;
        EntityRenderDispatcher entityRenderDispatcher;
        DebugScreenOverlay debugScreenOverlay = minecraft.getDebugOverlay();
        if (debugScreenOverlay.showDebugScreen() && !localPlayer.isCreative()) {
            localPlayer.sendSystemMessage(field_2892);
            debugScreenOverlay.toggleOverlay();
        }
        if (!minecraft.gameRenderer.effectActive) {
            minecraft.gameRenderer.effectActive = true;
            if (this.field_2872) {
                this.field_2872 = false;
            } else {
                minecraft.gui.getChat().addMessage(field_2894);
            }
        }
        if ((entityRenderDispatcher = minecraft.getEntityRenderDispatcher()).shouldRenderHitBoxes() && !localPlayer.isCreative()) {
            localPlayer.sendSystemMessage(field_2890);
            entityRenderDispatcher.setRenderHitBoxes(false);
        }
        if ((optionInstance = minecraft.options.mainHand()).get() != HumanoidArm.RIGHT) {
            optionInstance.set((Object)HumanoidArm.RIGHT);
            minecraft.options.broadcastOptions();
            minecraft.gui.getChat().addMessage(field_2893);
        }
    }

    @NotNull
    public List<GameRadioPoint> getRadioPoints() {
        return Collections.unmodifiableList(this.radioPoints);
    }

    public void method_5959() {
        this.radioPoints.removeIf(gameRadioPoint -> gameRadioPoint.method_5957().isWaypoint());
    }

    public void addRadioPoint(@NotNull GameRadioPoint point) {
        this.radioPoints.add(point);
    }

    public abstract boolean method_2730(@NotNull Player var1);

    public void renderWaitingText(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, @NotNull Font font, @NotNull Set<UUID> players, int midX) {
        MutableComponent mutableComponent;
        switch (((AbstractGame)this.game).getStatus()) {
            case PRE_GAME: {
                String string = String.format("(%d/%d)", players.size(), ((AbstractGame)this.game).getMinimumPlayers());
                MutableComponent mutableComponent2 = Component.literal((String)string).withStyle(ChatFormatting.GRAY);
                MutableComponent mutableComponent3 = Component.literal((String)StringUtils.makeFancy(field_2888.getString()));
                MutableComponent mutableComponent4 = mutableComponent3.append(" ").append((Component)mutableComponent2);
                break;
            }
            case POST_GAME: {
                MutableComponent mutableComponent4 = null;
                break;
            }
            default: {
                MutableComponent mutableComponent4 = mutableComponent = null;
            }
        }
        if (mutableComponent != null) {
            BFRendering.centeredComponent2dWithShadow(poseStack, font, graphics, mutableComponent, midX + 1, 23);
        }
    }

    @NotNull
    public abstract Scoreboard getScoreboard();

    public void method_2695(@NotNull PoseStack poseStack, @NotNull GuiGraphics guiGraphics, @NotNull Font font, @NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull LocalPlayer localPlayer, @NotNull PlayerCloudData playerCloudData, int n, int n2, float f) {
        Object p;
        GameTeam gameTeam;
        int n3 = 350;
        int n4 = n2 / 2;
        if (BFClientSettings.UI_RENDER_TAB_BLUR.isEnabled()) {
            BFRendering.backgroundBlur(minecraft);
        }
        if ((gameTeam = ((AbstractGamePlayerManager)(p = ((AbstractGame)this.game).playerManager)).getPlayerTeam(localPlayer.getUUID())) == null) {
            return;
        }
        int n5 = 50;
        int n6 = gameTeam.getColor();
        int n7 = gameTeam.getIconColor();
        if (BFClientSettings.UI_RENDER_TAB_BACKGROUND.isEnabled()) {
            BFRendering.rectangleGradient(guiGraphics, 0, n4, n, n4, ColorReferences.COLOR_BLACK_TRANSPARENT, ColorReferences.COLOR_BLACK_SOLID);
            BFRendering.rectangle(guiGraphics, 0, 0, n, n2, ColorReferences.COLOR_BLACK_SOLID, 0.25f);
            BFRendering.rectangle(guiGraphics, 0, 0, n, n2, n6, 0.15f);
        }
        int n8 = 10;
        int n9 = 16;
        BFRendering.centeredTintedTexture(poseStack, guiGraphics, STOPWATCH_TEXTURE, n - 10 - 16, 25, 16, 16, 0.0f, 1.0f, n7);
        MutableComponent mutableComponent = Component.literal((String)this.stageTimer.getComponent().getString()).withColor(n7);
        int n10 = font.width((FormattedText)mutableComponent) * 2;
        BFRendering.component2d(poseStack, font, guiGraphics, (Component)mutableComponent, n - 10 - 16 - n10 - 10, 18, 2.0f);
        BFRendering.rectangle(guiGraphics, 0, 49, n, 1, 0x11FFFFFF);
        BFRendering.rectangleGradient(guiGraphics, 0, 50, n, 8, ColorReferences.COLOR_BLACK_SOLID - -872415232, ColorReferences.COLOR_BLACK_TRANSPARENT);
        this.scoreboardHeader.render(poseStack, font, guiGraphics, (AbstractGame<?, ?, ?>)this.game, 0, 0, n, 49, n6, n7, f);
        List<GameTeam> list = ((AbstractGamePlayerManager)p).getTeams();
        int n11 = list.size();
        if (n11 == 0) {
            return;
        }
        n10 = (n - 350) / 2;
        int n12 = 13;
        boolean bl = true;
        int n13 = 5;
        int n14 = n2 - 50 - 13 - 2;
        int n15 = (n11 + 1) * 5;
        int n16 = (n14 - n15) / n11;
        int n17 = 68;
        BFRendering.renderTabListEntry(poseStack, minecraft, bFClientManager, this.dataHandler, playerCloudData, guiGraphics, font, this.game, this, null, Style.EMPTY, this.scoreboard.getColumns(), n10, 51.0f, 350.0f, 13.0f, 3);
        float f2 = (Mth.sin((float)(BFRendering.getRenderTime() / 120.0f)) + 1.0f) / 2.0f;
        int n18 = n - 350;
        int n19 = n18 / 2;
        for (GameTeam gameTeam2 : list) {
            int n20;
            SortedSet<UUID> sortedSet = BFUtils.sortStatData(this.game, gameTeam2.getPlayers(), ((AbstractGame)this.game).getPrimaryStat());
            int n21 = sortedSet.size() * 11;
            boolean bl2 = n21 > n16;
            int n22 = 0;
            if (bl2) {
                n20 = n21 - n16;
                n22 = (int)(f2 * (float)n20);
            }
            n20 = n17 + 15;
            int n23 = gameTeam2.getIconColor();
            DivisionData divisionData = gameTeam2.getDivisionData((AbstractGame<?, ?, ?>)this.game);
            BFRendering.centeredTextureSquare(poseStack, guiGraphics, divisionData.getCountry().getNationIcon(), n19 / 2, n20, 25);
            int n24 = n20 + 25 - 5;
            for (String string : FormatUtils.parseMarkup(font, divisionData.getCountry().getName(), n19)) {
                MutableComponent mutableComponent2 = Component.literal((String)string.trim()).withColor(n23);
                BFRendering.centeredString(font, guiGraphics, (Component)mutableComponent2, n19 / 2, n24);
                n24 += 10;
            }
            poseStack.pushPose();
            BFRendering.enableScissor(guiGraphics, n10, n17, 350, n16);
            this.renderTabListEntries(poseStack, minecraft, bFClientManager, localPlayer, this.dataHandler, playerCloudData, guiGraphics, font, gameTeam2, n10, n17 - n22, sortedSet);
            guiGraphics.disableScissor();
            poseStack.popPose();
            n17 += n16 + 5;
        }
    }

    private void renderTabListEntries(@NotNull PoseStack poseStack, @NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull LocalPlayer player, @NotNull ClientPlayerDataHandler dataHandler, @NotNull PlayerCloudData cloudProfile, @NotNull GuiGraphics graphics, @NotNull Font font, @NotNull GameTeam team, int n, int n2, Set<UUID> players) {
        int n3 = 11;
        ClientPacketListener clientPacketListener = player.connection;
        Style style = team.getStyleText();
        int n4 = 0;
        for (UUID uUID : players) {
            FDSTagCompound fDSTagCompound = ((AbstractGame)this.game).getPlayerStatData(uUID);
            BFClientPlayerData bFClientPlayerData = (BFClientPlayerData)dataHandler.getPlayerData(uUID);
            PlayerInfo playerInfo = clientPacketListener.getPlayerInfo(uUID);
            ObjectArrayList objectArrayList = new ObjectArrayList();
            for (TriFunction<BFClientPlayerData, PlayerInfo, FDSTagCompound, String> triFunction : this.scoreboard.entries.values()) {
                objectArrayList.add((String)triFunction.apply((Object)bFClientPlayerData, (Object)playerInfo, (Object)fDSTagCompound));
            }
            BFRendering.renderTabListEntry(poseStack, minecraft, manager, dataHandler, cloudProfile, graphics, font, this.game, this, uUID, style, (Collection<String>)objectArrayList, n, n2 + n4, 350.0f, 10.0f, 1);
            n4 += 11;
        }
    }

    @OverridingMethodsMustInvokeSuper
    public void renderDebug(@NotNull AbstractGamePlayerManager<?> playerManager, @NotNull Minecraft minecraft, @NotNull ClientLevel level, @NotNull LocalPlayer player, @NotNull RenderLevelStageEvent renderEvent, @NotNull MultiBufferSource.BufferSource bufferSource, PoseStack poseStack, @NotNull Frustum frustum, Font font, @NotNull GuiGraphics graphics, Camera camera, boolean renderGameInfo, float f, float f2) {
        this.getRadioPoints().forEach(radioPoint -> radioPoint.renderDebug(camera, player, poseStack, graphics, playerManager));
        if (renderGameInfo) {
            this.renderGameDebugInfo(poseStack, graphics, font, camera);
        }
    }

    private void renderGameDebugInfo(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, Font font, Camera camera) {
        MutableComponent mutableComponent;
        MutableComponent mutableComponent2;
        Object p = ((AbstractGame)this.game).playerManager;
        if (((AbstractGamePlayerManager)p).lobbySpawn != null) {
            mutableComponent2 = Component.literal((String)"LOBBY SPAWN").withStyle(ChatFormatting.YELLOW);
            BFRendering.component(poseStack, font, camera, graphics, (Component)mutableComponent2, ((AbstractGamePlayerManager)p).lobbySpawn.position.x, ((AbstractGamePlayerManager)p).lobbySpawn.position.y + 0.5, ((AbstractGamePlayerManager)p).lobbySpawn.position.z);
            BFRendering.billboardTexture(poseStack, camera, graphics, FRIENDS_ICON_TEXTURE, ((AbstractGamePlayerManager)p).lobbySpawn.position.x, ((AbstractGamePlayerManager)p).lobbySpawn.position.y + 1.25, ((AbstractGamePlayerManager)p).lobbySpawn.position.z, 64);
        }
        mutableComponent2 = Component.literal((String)"SPAWN POINT");
        for (GameTeam gameTeam : ((AbstractGamePlayerManager)p).getTeams()) {
            mutableComponent = Component.literal((String)gameTeam.getName()).withStyle(gameTeam.getStyleText());
            Object object = Component.literal((String)"Team: ").append((Component)mutableComponent);
            for (FDSPose fDSPose : gameTeam.getPlayerSpawns()) {
                BFRendering.component(poseStack, font, camera, graphics, (Component)mutableComponent2, fDSPose.position.x, fDSPose.position.y + (double)1.2f, fDSPose.position.z);
                BFRendering.component(poseStack, font, camera, graphics, (Component)object, fDSPose.position.x, fDSPose.position.y + (double)0.8f, fDSPose.position.z);
                BFRendering.billboardTexture(poseStack, camera, graphics, PLAYER_ICON_TEXTURE, fDSPose.position.x, fDSPose.position.y + 1.75, fDSPose.position.z, 64);
            }
        }
        G g = this.game;
        if (g instanceof ConquestGame) {
            MutableComponent mutableComponent3;
            MutableComponent mutableComponent4;
            MutableComponent mutableComponent5;
            ConquestGame conquestGame = (ConquestGame)g;
            MutableComponent mutableComponent6 = Component.literal((String)"VEHICLE SPAWN POINT");
            for (Object object : conquestGame.getVehicleSpawns()) {
                Object object2 = ((VehicleSpawn)object).isAllies ? ((AbstractGamePlayerManager)p).getTeamByName("Allies") : ((AbstractGamePlayerManager)p).getTeamByName("Axis");
                if (object2 == null) continue;
                MutableComponent mutableComponent7 = Component.literal((String)((GameTeam)object2).getName()).withStyle(((GameTeam)object2).getStyleText());
                mutableComponent5 = Component.literal((String)"Team: ").append((Component)mutableComponent7);
                mutableComponent4 = Component.literal((String)((VehicleSpawn)object).entityType.get().toString());
                mutableComponent3 = Component.literal((String)"Vehicle: ").append((Component)mutableComponent4);
                BFRendering.component(poseStack, font, camera, graphics, (Component)mutableComponent6, ((VehicleSpawn)object).position.x, ((VehicleSpawn)object).position.y + (double)1.7f, ((VehicleSpawn)object).position.z);
                BFRendering.component(poseStack, font, camera, graphics, (Component)mutableComponent5, ((VehicleSpawn)object).position.x, ((VehicleSpawn)object).position.y + (double)1.3f, ((VehicleSpawn)object).position.z);
                BFRendering.component(poseStack, font, camera, graphics, (Component)mutableComponent3, ((VehicleSpawn)object).position.x, ((VehicleSpawn)object).position.y + (double)0.9f, ((VehicleSpawn)object).position.z);
                BFRendering.billboardTexture(poseStack, camera, graphics, PLAYER_ICON_TEXTURE, ((VehicleSpawn)object).position.x, ((VehicleSpawn)object).position.y + 2.25, ((VehicleSpawn)object).position.z, 64);
            }
            mutableComponent = Component.literal((String)"CP SPAWN POINT");
            for (Object object2 : conquestGame.getCapturePoints()) {
                GameTeam gameTeam = ((AbstractCapturePoint)object2).getCbTeam();
                mutableComponent5 = Component.literal((String)(gameTeam != null ? gameTeam.getName() : "None"));
                mutableComponent4 = Component.literal((String)"Team: ").append((Component)mutableComponent5);
                mutableComponent3 = Component.literal((String)((ConquestCapturePoint)object2).name);
                MutableComponent mutableComponent8 = Component.literal((String)"Capture Point: ").append((Component)mutableComponent3);
                BFRendering.component(poseStack, font, camera, graphics, (Component)mutableComponent, ((ConquestCapturePoint)object2).position.x, ((ConquestCapturePoint)object2).position.y + (double)1.7f, ((ConquestCapturePoint)object2).position.z);
                BFRendering.component(poseStack, font, camera, graphics, (Component)mutableComponent4, ((ConquestCapturePoint)object2).position.x, ((ConquestCapturePoint)object2).position.y + (double)1.3f, ((ConquestCapturePoint)object2).position.z);
                BFRendering.component(poseStack, font, camera, graphics, (Component)mutableComponent8, ((ConquestCapturePoint)object2).position.x, ((ConquestCapturePoint)object2).position.y + (double)0.9f, ((ConquestCapturePoint)object2).position.z);
                BFRendering.billboardTexture(poseStack, camera, graphics, PLAYER_ICON_TEXTURE, ((ConquestCapturePoint)object2).position.x, ((ConquestCapturePoint)object2).position.y + 2.25, ((ConquestCapturePoint)object2).position.z, 64);
            }
        }
    }

    public abstract boolean method_2718();

    public abstract boolean method_2709(@NotNull Minecraft var1, @NotNull LivingEntity var2, @NotNull LocalPlayer var3, @NotNull ClientLevel var4);

    public abstract boolean method_2719();

    @NotNull
    public G getGame() {
        return this.game;
    }

    @NotNull
    public List<MinimapWaypoint> getMinimapWaypoints(@NotNull Minecraft minecraft, @NotNull Set<UUID> players, @NotNull LocalPlayer player, @NotNull ClientLevel level) {
        ObjectArrayList objectArrayList = new ObjectArrayList();
        objectArrayList.addAll(this.getSpecificMinimapWaypoints(minecraft, players, player, level));
        return objectArrayList;
    }

    @NotNull
    public abstract List<MinimapWaypoint> getSpecificMinimapWaypoints(@NotNull Minecraft var1, @NotNull Set<UUID> var2, @NotNull LocalPlayer var3, @NotNull ClientLevel var4);

    public abstract void method_2710(@NotNull Minecraft var1, @NotNull RenderNameTagEvent var2, @NotNull LocalPlayer var3, @NotNull ClientLevel var4);

    @Nullable
    public ResourceLocation method_2696(@NotNull UUID uUID, @Nullable String string, @NotNull Set<UUID> set) {
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        CloudRegistry cloudRegistry = bFClientManager.getCloudRegistry();
        PlayerCloudData playerCloudData = this.dataHandler.getCloudProfile(uUID);
        PlayerCloudInventory playerCloudInventory = (PlayerCloudInventory)playerCloudData.getInventory();
        if (!set.contains(uUID)) {
            return null;
        }
        ResourceLocation resourceLocation = null;
        GameTeam gameTeam = ((AbstractGamePlayerManager)((AbstractGame)this.game).playerManager).getPlayerTeam(uUID);
        if (gameTeam != null) {
            Object object;
            DivisionData divisionData = ((AbstractGame)this.game).getAlliesDivision();
            DivisionData divisionData2 = ((AbstractGame)this.game).getAxisDivision();
            switch (gameTeam.getName()) {
                case "Allies": {
                    resourceLocation = BFRes.loc("textures/skins/game/nations/" + divisionData.getCountry().getTag() + "/" + divisionData.getSkin() + "/" + string + ".png");
                    break;
                }
                case "Axis": {
                    resourceLocation = BFRes.loc("textures/skins/game/nations/" + divisionData2.getCountry().getTag() + "/" + divisionData2.getSkin() + "/" + string + ".png");
                }
            }
            switch (gameTeam.getName()) {
                case "Allies": {
                    Object object2 = divisionData.getCountry();
                    break;
                }
                case "Axis": {
                    Object object2 = divisionData2.getCountry();
                    break;
                }
                default: {
                    Object object2 = object = null;
                }
            }
            if (object != null) {
                CloudItem cloudItem;
                String string2 = ((BFCountry)((Object)object)).getTag();
                CloudItemStack cloudItemStack = playerCloudInventory.method_1677((BFCountry)((Object)object));
                if (cloudItemStack != null && (cloudItem = cloudItemStack.getCloudItem(cloudRegistry)) != null) {
                    resourceLocation = BFRes.loc("textures/skins/game/nations/" + string2 + "/" + cloudItem.getSuffixForDisplay() + "/" + string + ".png");
                }
            }
        }
        return resourceLocation;
    }

    public void method_2729(@NotNull Minecraft minecraft, @NotNull LocalPlayer localPlayer) {
        Object object;
        UUID uUID = localPlayer.getUUID();
        BFClientPlayerData bFClientPlayerData = (BFClientPlayerData)this.dataHandler.getPlayerData((Player)localPlayer);
        Object object2 = this.game;
        if (object2 instanceof IHasItemShop) {
            object = (IHasItemShop)object2;
            if (BFUtils.isPlayerUnavailable((Player)localPlayer, bFClientPlayerData)) {
                return;
            }
            if (!object.getShopItems(uUID).isEmpty()) {
                minecraft.setScreen((Screen)new PlayerItemShopScreen((IHasItemShop)object));
            }
        }
        if (this.game instanceof IHasClasses) {
            object = ((AbstractGamePlayerManager)((AbstractGame)this.game).playerManager).getPlayerTeam(uUID);
            if (!(minecraft.screen instanceof AbstractMatchSelectClassScreen) && object != null) {
                object2 = this.method_2686((GameTeam)object);
                minecraft.setScreen(object2);
            }
        }
    }

    @NotNull
    public Map<MatchClass, ObjectList<Loadout>> method_2685(@NotNull GameTeam gameTeam) {
        return gameTeam.getDivisionData((AbstractGame<?, ?, ?>)this.game).getLoadouts();
    }

    public void addKillFeedEntry(@NotNull Minecraft minecraft, @NotNull KillFeedEntry entry) {
        entry.detectType(minecraft);
        this.killFeedEntries.add(entry);
        if (this.killFeedEntries.size() > 5) {
            this.killFeedEntries.removeFirst();
        }
    }

    public void addPopup(@NotNull BFPopup popup) {
        this.popups.add(popup);
        while (this.popups.size() > 8) {
            this.popups.removeFirst();
        }
    }

    @Nullable
    public KillMessage getKillMessage() {
        return this.killMessage;
    }

    public void setKillMessage(@NotNull KillMessage killMessage) {
        this.killMessage = killMessage;
    }

    public void readFromFDS(@NotNull FDSTagCompound fDSTagCompound) {
        if (fDSTagCompound.getBoolean("hasTimer", false)) {
            this.stageTimer.readFromFDS(fDSTagCompound);
        }
        this.field_2873 = fDSTagCompound.getBoolean("isOfficial", false);
    }

    public void read(@NotNull ByteBuf byteBuf) throws IOException {
        if (IPacket.readBoolean((ByteBuf)byteBuf)) {
            this.stageTimer.read(byteBuf);
        }
        this.field_2873 = IPacket.readBoolean((ByteBuf)byteBuf);
    }

    public boolean method_2720() {
        return this.field_2873;
    }

    @NotNull
    public List<KillFeedEntry> getKillFeedEntries() {
        return Collections.unmodifiableList(this.killFeedEntries);
    }

    @NotNull
    public List<BFPopup> getPopups() {
        return Collections.unmodifiableList(this.popups);
    }

    @NotNull
    public abstract Collection<? extends MatchSummaryScreen> getSummaryScreens(boolean var1);

    @NotNull
    protected Collection<? extends MatchSummaryScreen> getSummaryScreens(G game, boolean onlyVote) {
        if (onlyVote) {
            return List.of(new MatchMapVoteSummaryScreen((AbstractGame<?, ?, ?>)game));
        }
        return List.of(new MatchRankSummaryScreen(((AbstractGame)game).getUUID(), this), new MatchTopPlayersSummaryScreen((AbstractGame<?, ?, ?>)game, this.getScoreStat()), new MatchMapVoteSummaryScreen((AbstractGame<?, ?, ?>)game));
    }

    @NotNull
    protected BFStat getScoreStat() {
        return BFStats.SCORE;
    }

    @NotNull
    public GameSummaryManager getSummaryManager() {
        return this.summaryManager;
    }

    public void stop() {
    }

    public static class Scoreboard {
        @NotNull
        private final Map<String, TriFunction<BFClientPlayerData, PlayerInfo, FDSTagCompound, String>> entries = new Object2ObjectLinkedOpenHashMap();

        @NotNull
        public Scoreboard column(@NotNull String name, @NotNull TriFunction<BFClientPlayerData, PlayerInfo, FDSTagCompound, String> func) {
            this.entries.put(name, func);
            return this;
        }

        @NotNull
        public Set<String> getColumns() {
            return this.entries.keySet();
        }
    }
}

