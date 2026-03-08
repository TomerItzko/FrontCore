/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.fds.tag.FDSTagCompound
 *  com.mojang.blaze3d.vertex.PoseStack
 *  it.unimi.dsi.fastutil.objects.Object2IntMap
 *  it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  javax.annotation.Nullable
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Camera
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.multiplayer.PlayerInfo
 *  net.minecraft.client.player.AbstractClientPlayer
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.client.renderer.MultiBufferSource
 *  net.minecraft.client.renderer.culling.Frustum
 *  net.minecraft.core.BlockPos
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.Mth
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.phys.BlockHitResult
 *  net.minecraft.world.phys.HitResult
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.client.event.RenderLevelStageEvent
 *  net.neoforged.neoforge.client.event.RenderNameTagEvent
 *  net.neoforged.neoforge.common.util.TriState
 *  org.apache.commons.lang3.function.TriFunction
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.game.impl.inf;

import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.game.element.ClientGameElement;
import com.boehmod.blockfront.client.render.game.element.InfectedPointsGameElement;
import com.boehmod.blockfront.client.render.game.element.InfectedRemainingGameElement;
import com.boehmod.blockfront.client.render.game.element.InfectedWaveGameElement;
import com.boehmod.blockfront.client.render.game.element.TeamGameElement;
import com.boehmod.blockfront.client.render.game.element.TimeGameElement;
import com.boehmod.blockfront.client.render.minimap.MinimapWaypoint;
import com.boehmod.blockfront.client.screen.match.summary.MatchSummaryScreen;
import com.boehmod.blockfront.common.entity.BotEntity;
import com.boehmod.blockfront.common.entity.InfectedEntity;
import com.boehmod.blockfront.common.entity.VendorEntity;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGameClient;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.GameTeam;
import com.boehmod.blockfront.game.impl.inf.InfectedCharacter;
import com.boehmod.blockfront.game.impl.inf.InfectedCharacters;
import com.boehmod.blockfront.game.impl.inf.InfectedDoor;
import com.boehmod.blockfront.game.impl.inf.InfectedGame;
import com.boehmod.blockfront.game.impl.inf.InfectedPlayerManager;
import com.boehmod.blockfront.game.impl.inf.ZombieSpawn;
import com.boehmod.blockfront.game.impl.inf.ZombieZone;
import com.boehmod.blockfront.unnamed.BF_552;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.BFUtils;
import com.boehmod.blockfront.util.StringUtils;
import com.boehmod.blockfront.util.math.FDSPose;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;
import net.neoforged.neoforge.common.util.TriState;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;

public final class InfectedGameClient
extends AbstractGameClient<InfectedGame, InfectedPlayerManager> {
    private static final ResourceLocation WAYPOINT_STORE = BFRes.loc("textures/gui/compass/waypoint_store.png");
    private static final ResourceLocation field_3539 = BFRes.loc("textures/misc/debug/infected_spawn.png");
    private static final ResourceLocation field_3540 = BFRes.loc("textures/misc/debug/infected_door.png");
    private static final ResourceLocation field_3541 = BFRes.loc("textures/misc/debug/infected_store.png");
    @NotNull
    private final Object2IntMap<UUID> field_3542 = new Object2IntOpenHashMap();

    @Override
    @NotNull
    protected List<Component> getTips() {
        ObjectArrayList objectArrayList = new ObjectArrayList();
        MutableComponent mutableComponent = Component.literal((String)String.valueOf(30));
        objectArrayList.add(Component.translatable((String)"bf.message.gamemode.infected.tip.0", (Object[])new Object[]{mutableComponent}));
        objectArrayList.add(Component.translatable((String)"bf.message.gamemode.infected.tip.1"));
        objectArrayList.add(Component.translatable((String)"bf.message.gamemode.infected.tip.2"));
        objectArrayList.add(Component.translatable((String)"bf.message.gamemode.infected.tip.3"));
        return objectArrayList;
    }

    public InfectedGameClient(@NotNull BFClientManager bFClientManager, @NotNull InfectedGame infectedGame, @NotNull ClientPlayerDataHandler clientPlayerDataHandler) {
        super(bFClientManager, infectedGame, clientPlayerDataHandler);
        bFClientManager.getCinematics().method_2205(new BF_552(infectedGame));
    }

    @Override
    @NotNull
    protected List<ClientGameElement<InfectedGame, InfectedPlayerManager>> getGameElements() {
        return List.of(new TeamGameElement(), new InfectedRemainingGameElement(), new InfectedWaveGameElement(), new InfectedPointsGameElement(), new TimeGameElement());
    }

    @Override
    public boolean method_2713(@NotNull AbstractClientPlayer abstractClientPlayer) {
        return false;
    }

    @Override
    public void update(@NotNull Minecraft minecraft, @NotNull Random random, @NotNull RandomSource randomSource, @NotNull LocalPlayer player, @NotNull ClientLevel level, @NotNull BFClientManager manager, @NotNull BFClientPlayerData playerData, @NotNull Set<UUID> players, float f, @NotNull Vec3 vec3, @NotNull BlockPos blockPos) {
        super.update(minecraft, random, randomSource, player, level, manager, playerData, players, f, vec3, blockPos);
        for (UUID uUID : players) {
            int n = BFUtils.getPlayerStat(this.game, uUID, BFStats.POINTS);
            int n2 = this.field_3542.getOrDefault((Object)uUID, 0);
            int n3 = (int)Mth.lerp((float)0.1f, (float)n2, (float)n);
            if (Math.abs(n3 - n2) < 1) {
                this.field_3542.put((Object)uUID, n);
                continue;
            }
            this.field_3542.put((Object)uUID, n3);
        }
    }

    @Override
    public void renderSpecific(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull LocalPlayer player, @NotNull ClientLevel level, @NotNull BFClientPlayerData playerData, @NotNull GuiGraphics graphics, @NotNull Font font, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, @NotNull Set<UUID> players, int width, int height, int midX, int midY, float renderTime, float delta) {
        this.method_3669(minecraft, player, level, graphics, font, midX, midY);
        this.method_3670(graphics, font, poseStack, players, width);
    }

    private void method_3670(@NotNull GuiGraphics guiGraphics, @NotNull Font font, @NotNull PoseStack poseStack, @NotNull Set<UUID> set, int n) {
        int n2 = 64;
        int n3 = 16;
        int n4 = 5;
        int n5 = 2;
        int n6 = n - 64 - 5;
        int n7 = 25;
        int n8 = BFRendering.translucentBlack();
        int n9 = 0;
        for (UUID uUID : set) {
            int n10 = BFUtils.getPlayerStat(this.game, uUID, BFStats.POINTS);
            MutableComponent mutableComponent = Component.literal((String)"$").append(StringUtils.formatLong(n10)).withStyle(ChatFormatting.GREEN);
            int n11 = 25 + 18 * n9;
            BFRendering.rectangle(guiGraphics, n6 + 1, n11, 62, 1, n8);
            BFRendering.rectangle(guiGraphics, n6, n11 + 1, 64, 14, n8);
            BFRendering.rectangle(guiGraphics, n6 + 1, n11 + 16 - 1, 62, 1, n8);
            boolean bl = true;
            int n12 = 14;
            int n13 = n6 + 1;
            int n14 = n11 + 1;
            BFRendering.rectangle(guiGraphics, n13, n14, 14, 14, n8);
            int n15 = 12;
            ResourceLocation resourceLocation = this.method_2696(uUID, null, set);
            if (resourceLocation != null) {
                poseStack.pushPose();
                poseStack.translate((float)(n13 + 1), (float)(n14 + 1), 0.0f);
                guiGraphics.blit(resourceLocation, 0, 0, 12, 12, 8.0f, 8.0f, 8, 8, 64, 64);
                guiGraphics.blit(resourceLocation, 0, 0, 12, 12, 40.0f, 8.0f, 8, 8, 64, 64);
                poseStack.popPose();
            }
            int n16 = n6 + 39;
            int n17 = n11 + 8 - 4;
            BFRendering.centeredComponent2dWithShadow(poseStack, font, guiGraphics, (Component)mutableComponent, (float)n16, (float)n17 + 0.5f);
            ++n9;
        }
    }

    private void method_3669(@NotNull Minecraft minecraft, @NotNull LocalPlayer localPlayer, @NotNull ClientLevel clientLevel, @NotNull GuiGraphics guiGraphics, @NotNull Font font, int n, int n2) {
        HitResult hitResult = minecraft.hitResult;
        if (!(hitResult instanceof BlockHitResult)) {
            return;
        }
        BlockHitResult blockHitResult = (BlockHitResult)hitResult;
        hitResult = blockHitResult.getBlockPos();
        BlockState blockState = clientLevel.getBlockState((BlockPos)hitResult);
        if (blockState.getBlock() == Blocks.IRON_DOOR) {
            int n3 = BFUtils.getPlayerStat(this.game, localPlayer.getUUID(), BFStats.POINTS);
            ((InfectedGame)this.game).doors.stream().filter(arg_0 -> InfectedGameClient.method_3671((BlockPos)hitResult, arg_0)).filter(infectedDoor -> !infectedDoor.isOpen()).forEach(infectedDoor -> {
                boolean bl = n3 >= infectedDoor.getCost();
                MutableComponent mutableComponent = Component.literal((String)("$" + infectedDoor.getCost())).withStyle(bl ? ChatFormatting.GREEN : ChatFormatting.DARK_RED);
                BFRendering.centeredString(font, guiGraphics, (Component)Component.translatable((String)"bf.message.gamemode.infected.door.open.ui", (Object[])new Object[]{mutableComponent}), n, n2 + 20);
            });
        }
    }

    @Override
    public void renderDebug(@NotNull AbstractGamePlayerManager<?> playerManager, @NotNull Minecraft minecraft, @NotNull ClientLevel level, @NotNull LocalPlayer player, @NotNull RenderLevelStageEvent renderEvent, // Could not load outer class - annotation placement on inner may be incorrect
    @NotNull MultiBufferSource.BufferSource bufferSource, PoseStack poseStack, @NotNull Frustum frustum, Font font, @NotNull GuiGraphics graphics, Camera camera, boolean renderGameInfo, float f, float f2) {
        super.renderDebug(playerManager, minecraft, level, player, renderEvent, bufferSource, poseStack, frustum, font, graphics, camera, renderGameInfo, f, f2);
        for (Object object : level.entitiesForRendering()) {
            if (!(object instanceof VendorEntity)) continue;
            BFRendering.billboardTexture(poseStack, camera, graphics, WAYPOINT_STORE, object.position().add(0.0, 2.5, 0.0), 16, true);
        }
        if (player.isCreative() && minecraft.getDebugOverlay().showDebugScreen()) {
            Object object3;
            Object object2;
            ObjectArrayList objectArrayList = new ObjectArrayList();
            for (FDSPose object5 : ((InfectedGame)this.game).vendorSpawns) {
                BFRendering.billboardTexture(poseStack, camera, graphics, field_3541, object5.getX() + 0.5, object5.getY() + 0.5, object5.getZ() + 0.5, 32, false);
            }
            for (InfectedDoor infectedDoor : ((InfectedGame)this.game).doors) {
                BlockPos blockPos = infectedDoor.getPosition();
                BFRendering.billboardTexture(poseStack, camera, graphics, field_3540, (double)((float)blockPos.getX() + 0.5f), (double)((float)blockPos.getY() + 0.5f), (double)((float)blockPos.getZ() + 0.5f), 32, false);
                if (objectArrayList.contains(infectedDoor.getId())) continue;
                objectArrayList.add(infectedDoor.getId());
                BFRendering.billboardComponent(poseStack, camera, font, graphics, (Component)Component.literal((String)"Zombie Zone Door"), blockPos.getX(), blockPos.getY() + 2, blockPos.getZ());
                StringBuilder stringBuilder = new StringBuilder();
                for (String string : infectedDoor.getZones()) {
                    String string2 = string + ", ";
                    stringBuilder.append(string2);
                }
                object2 = stringBuilder.toString();
                String string = String.format("Door ID: '%s'", String.valueOf(ChatFormatting.GRAY) + infectedDoor.getId() + String.valueOf(ChatFormatting.RESET));
                BFRendering.billboardComponent(poseStack, camera, font, graphics, (Component)Component.literal((String)string), blockPos.getX(), (float)blockPos.getY() + 1.5f, blockPos.getZ());
                String string3 = String.format("Door Zones: '%s'", String.valueOf(ChatFormatting.GRAY) + (String)object2 + String.valueOf(ChatFormatting.RESET));
                BFRendering.billboardComponent(poseStack, camera, font, graphics, (Component)Component.literal((String)string3), blockPos.getX(), (float)blockPos.getY() + 1.0f, blockPos.getZ());
                object3 = String.format("X: '%s', Y: '%s', Z: '%s'", String.valueOf(ChatFormatting.GRAY) + (float)blockPos.getX() + String.valueOf(ChatFormatting.RESET), String.valueOf(ChatFormatting.GRAY) + (float)blockPos.getY() + String.valueOf(ChatFormatting.RESET), String.valueOf(ChatFormatting.GRAY) + (float)blockPos.getZ() + String.valueOf(ChatFormatting.RESET));
                BFRendering.billboardComponent(poseStack, camera, font, graphics, (Component)Component.literal((String)object3), blockPos.getX(), (float)blockPos.getY() + 0.5f, blockPos.getZ());
            }
            int n = -16776961;
            int n2 = 0xFF00FF;
            for (ZombieZone zombieZone : ((InfectedGame)this.game).zombieZones) {
                object2 = new ObjectArrayList();
                for (ZombieSpawn zombieSpawn : zombieZone.getSpawns()) {
                    object3 = zombieSpawn.position();
                    object2.add(new Vec3(((FDSPose)object3).getX() + 0.5, ((FDSPose)object3).getY() + 0.5, ((FDSPose)object3).getZ() + 0.5));
                    BFRendering.billboardTexture(poseStack, camera, graphics, field_3539, ((FDSPose)object3).getX() + 0.5, ((FDSPose)object3).getY() + 0.5, ((FDSPose)object3).getZ() + 0.5, 32, false);
                    MutableComponent mutableComponent = Component.literal((String)"Zombie Spawn");
                    if (zombieZone.isActive()) {
                        mutableComponent.append((Component)Component.literal((String)" ACTIVE").withStyle(ChatFormatting.GREEN));
                    }
                    BFRendering.billboardComponent(poseStack, camera, font, graphics, (Component)mutableComponent, ((FDSPose)object3).getX(), ((FDSPose)object3).getY() + 2.0, ((FDSPose)object3).getZ());
                    String string = String.format("Zone: '%s'", String.valueOf(ChatFormatting.GRAY) + zombieZone.getId() + String.valueOf(ChatFormatting.RESET));
                    BFRendering.billboardComponent(poseStack, camera, font, graphics, (Component)Component.literal((String)string), ((FDSPose)object3).getX(), ((FDSPose)object3).getY() + 1.5, ((FDSPose)object3).getZ());
                    String string4 = String.format("X: '%s', Y: '%s', Z: '%s'", String.valueOf(ChatFormatting.GRAY) + (float)((FDSPose)object3).getX() + String.valueOf(ChatFormatting.RESET), String.valueOf(ChatFormatting.GRAY) + (float)((FDSPose)object3).getY() + String.valueOf(ChatFormatting.RESET), String.valueOf(ChatFormatting.GRAY) + (float)((FDSPose)object3).getZ() + String.valueOf(ChatFormatting.RESET));
                    BFRendering.billboardComponent(poseStack, camera, font, graphics, (Component)Component.literal((String)string4), ((FDSPose)object3).getX(), ((FDSPose)object3).getY() + 1.0, ((FDSPose)object3).getZ());
                }
                Vec3 vec3 = MathUtils.averageMc((List<Vec3>)object2);
                vec3.add(0.0, 2.0, 0.0);
                BFRendering.component(poseStack, font, camera, graphics, (Component)Component.literal((String)("ZONE '" + zombieZone.getId() + "'")).withStyle(zombieZone.isActive() ? ChatFormatting.GREEN : ChatFormatting.RED), vec3.x, vec3.y + 1.0, vec3.z, 2.0f);
                for (Object object3 : zombieZone.getSpawns()) {
                    BFRendering.billboardLine(camera, poseStack, vec3, ((ZombieSpawn)object3).position().position.add(0.5, 0.5, 0.5), 1.0f, zombieZone.isActive() ? 0xFF00FF : -16776961);
                }
            }
        }
    }

    @Override
    public boolean method_2709(@NotNull Minecraft minecraft, @NotNull LivingEntity livingEntity, @NotNull LocalPlayer localPlayer, @NotNull ClientLevel clientLevel) {
        Object object;
        UUID uUID;
        GameTeam gameTeam;
        InfectedPlayerManager infectedPlayerManager = (InfectedPlayerManager)((InfectedGame)this.game).getPlayerManager();
        GameTeam gameTeam2 = infectedPlayerManager.getPlayerTeam(localPlayer.getUUID());
        String string = null;
        if (livingEntity instanceof Player && (gameTeam = infectedPlayerManager.getPlayerTeam(uUID = (object = (Player)livingEntity).getUUID())) != null) {
            string = gameTeam.getName();
        }
        if (livingEntity instanceof BotEntity) {
            object = (BotEntity)livingEntity;
            string = ((BotEntity)object).method_2031();
        }
        if (gameTeam2 != null && string != null) {
            return gameTeam2.getName().equalsIgnoreCase(string);
        }
        return false;
    }

    @Override
    public boolean method_2719() {
        return true;
    }

    @Override
    @NotNull
    public List<MinimapWaypoint> getSpecificMinimapWaypoints(@NotNull Minecraft minecraft, @NotNull Set<UUID> players, @NotNull LocalPlayer player, @NotNull ClientLevel level) {
        ObjectArrayList objectArrayList = new ObjectArrayList();
        for (Entity entity : level.entitiesForRendering()) {
            ResourceLocation resourceLocation = null;
            if (entity instanceof Player && players.contains(entity.getUUID()) && !entity.getScoreboardName().equals(minecraft.getUser().getName())) {
                resourceLocation = MinimapWaypoint.TEXTURE_PLAYER;
            }
            if (entity instanceof InfectedEntity) {
                resourceLocation = MinimapWaypoint.TEXTURE_ENEMY;
            }
            if (entity instanceof VendorEntity) {
                resourceLocation = WAYPOINT_STORE;
            }
            if (resourceLocation == null) continue;
            float f = entity.getYRot() - 180.0f;
            objectArrayList.add(new MinimapWaypoint(resourceLocation, entity.position()).method_353(4).method_352(f));
        }
        return objectArrayList;
    }

    @Override
    public boolean method_2718() {
        return false;
    }

    @Override
    public void method_2710(@NotNull Minecraft minecraft, @NotNull RenderNameTagEvent renderNameTagEvent, @NotNull LocalPlayer localPlayer, @NotNull ClientLevel clientLevel) {
        if (renderNameTagEvent.getEntity() instanceof Player) {
            renderNameTagEvent.setCanRender(TriState.TRUE);
            return;
        }
        renderNameTagEvent.setCanRender(TriState.FALSE);
    }

    @Override
    public boolean method_2730(@NotNull Player player) {
        return true;
    }

    @Override
    @NotNull
    public AbstractGameClient.Scoreboard getScoreboard() {
        return new AbstractGameClient.Scoreboard().column("PING", (TriFunction<BFClientPlayerData, PlayerInfo, FDSTagCompound, String>)((TriFunction)(bFClientPlayerData, playerInfo, fDSTagCompound) -> playerInfo != null ? StringUtils.formatLong(playerInfo.getLatency()) : "???")).column("CASH", (TriFunction<BFClientPlayerData, PlayerInfo, FDSTagCompound, String>)((TriFunction)(bFClientPlayerData, playerInfo, fDSTagCompound) -> {
            String string;
            int n = fDSTagCompound.getInteger(BFStats.POINTS.getKey());
            if (n >= 1000) {
                int n2 = n / 1000;
                int n3 = n % 1000 / 100;
                string = "$" + n2 + "." + n3 + "K";
            } else {
                string = "$" + StringUtils.formatLong(n);
            }
            return string;
        })).column("K", (TriFunction<BFClientPlayerData, PlayerInfo, FDSTagCompound, String>)((TriFunction)(bFClientPlayerData, playerInfo, fDSTagCompound) -> StringUtils.formatLong(fDSTagCompound.getInteger(BFStats.KILLS_INFECTED.getKey())))).column("SCORE", (TriFunction<BFClientPlayerData, PlayerInfo, FDSTagCompound, String>)((TriFunction)(bFClientPlayerData, playerInfo, fDSTagCompound) -> StringUtils.formatLong(fDSTagCompound.getInteger(BFStats.SCORE.getKey()))));
    }

    @Override
    public ResourceLocation method_2696(@NotNull UUID uUID, @Nullable String string, @NotNull Set<UUID> set) {
        if (string != null && string.equalsIgnoreCase("backpack")) {
            return null;
        }
        int n = BFUtils.getPlayerStat(this.game, uUID, BFStats.CHAR);
        InfectedCharacter infectedCharacter = InfectedCharacters.get(n);
        return infectedCharacter.getTexture();
    }

    @Override
    @NotNull
    public Collection<? extends MatchSummaryScreen> getSummaryScreens(boolean bl) {
        return this.getSummaryScreens((InfectedGame)this.game, bl);
    }

    private static /* synthetic */ boolean method_3671(BlockPos blockPos, InfectedDoor infectedDoor) {
        return infectedDoor.getPosition().equals((Object)blockPos) || infectedDoor.getPosition().offset(0, 1, 0).equals((Object)blockPos) || infectedDoor.getPosition().offset(0, -1, 0).equals((Object)blockPos);
    }
}

