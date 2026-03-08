/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.fds.tag.FDSTagCompound
 *  com.mojang.blaze3d.vertex.PoseStack
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
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
 *  net.minecraft.client.resources.sounds.SimpleSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.core.BlockPos
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.util.Mth
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.neoforge.client.event.RenderLevelStageEvent
 *  net.neoforged.neoforge.client.event.RenderNameTagEvent
 *  net.neoforged.neoforge.common.util.TriState
 *  org.apache.commons.lang3.function.TriFunction
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.boehmod.blockfront.game.impl.boot;

import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.BFKeyMappings;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.game.element.ClientGameElement;
import com.boehmod.blockfront.client.render.minimap.MinimapWaypoint;
import com.boehmod.blockfront.client.screen.match.summary.MatchSummaryScreen;
import com.boehmod.blockfront.common.entity.HumanEntity;
import com.boehmod.blockfront.common.gun.GunMagType;
import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.common.stat.BFStats;
import com.boehmod.blockfront.game.AbstractGameClient;
import com.boehmod.blockfront.game.AbstractGamePlayerManager;
import com.boehmod.blockfront.game.impl.boot.BootcampGame;
import com.boehmod.blockfront.game.impl.boot.BootcampPlayerManager;
import com.boehmod.blockfront.game.impl.boot.BootcampTexturePoint;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.unnamed.BF_604;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.StringUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
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
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;
import net.neoforged.neoforge.common.util.TriState;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class BootcampGameClient
extends AbstractGameClient<BootcampGame, BootcampPlayerManager> {
    private static final ResourceLocation field_3351 = BFRes.loc("textures/skins/game/nations/us/recruit/0.png");
    private boolean field_3352 = false;

    @Override
    @NotNull
    protected List<Component> getTips() {
        return List.of();
    }

    public BootcampGameClient(@NotNull BFClientManager bFClientManager, @NotNull BootcampGame bootcampGame, @NotNull ClientPlayerDataHandler clientPlayerDataHandler) {
        super(bFClientManager, bootcampGame, clientPlayerDataHandler);
        bFClientManager.getCinematics().method_2205(new BF_604(bootcampGame));
    }

    @Override
    @NotNull
    protected List<ClientGameElement<BootcampGame, BootcampPlayerManager>> getGameElements() {
        return List.of();
    }

    @Override
    public boolean method_2713(@NotNull AbstractClientPlayer abstractClientPlayer) {
        return false;
    }

    @Override
    public void update(@NotNull Minecraft minecraft, @NotNull Random random, @NotNull RandomSource randomSource, @NotNull LocalPlayer player, @NotNull ClientLevel level, @NotNull BFClientManager manager, @NotNull BFClientPlayerData playerData, @NotNull Set<UUID> players, float f, @NotNull Vec3 vec3, @NotNull BlockPos blockPos) {
        super.update(minecraft, random, randomSource, player, level, manager, playerData, players, f, vec3, blockPos);
        if (!this.field_3352) {
            this.field_3352 = true;
            minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.MUSIC_BOOTCAMP.get()), (float)1.0f));
        }
    }

    @Override
    public void renderSpecific(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull LocalPlayer player, @NotNull ClientLevel level, @NotNull BFClientPlayerData playerData, @NotNull GuiGraphics graphics, @NotNull Font font, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, @NotNull Set<UUID> players, int width, int height, int midX, int midY, float renderTime, float delta) {
        Object object;
        ItemStack itemStack = player.getMainHandItem();
        if (!itemStack.isEmpty() && (object = itemStack.getItem()) instanceof GunItem) {
            GunItem gunItem = (GunItem)object;
            object = gunItem.getMagTypeOrDefault(itemStack);
            int n = ((GunMagType)object).capacity();
            if (GunItem.getAmmoLoaded(itemStack) < n) {
                float f = Mth.sin((float)(renderTime / 5.0f));
                float f2 = 1.0f - f;
                MutableComponent mutableComponent = Component.literal((String)BFKeyMappings.GUN_RELOAD.getKey().getDisplayName().getString().toUpperCase(Locale.ROOT)).withStyle(ChatFormatting.GRAY);
                BFRendering.centeredComponent2d(poseStack, font, graphics, (Component)Component.translatable((String)"bf.item.gun.reload.tip", (Object[])new Object[]{mutableComponent}), midX, midY + 40, 2.0f + f2 * 0.1f);
            }
        }
    }

    @Override
    public void renderDebug(@NotNull AbstractGamePlayerManager<?> playerManager, @NotNull Minecraft minecraft, @NotNull ClientLevel level, @NotNull LocalPlayer player, @NotNull RenderLevelStageEvent renderEvent, // Could not load outer class - annotation placement on inner may be incorrect
    @NotNull MultiBufferSource.BufferSource bufferSource, PoseStack poseStack, @NotNull Frustum frustum, Font font, @NotNull GuiGraphics graphics, Camera camera, boolean renderGameInfo, float f, float f2) {
        super.renderDebug(playerManager, minecraft, level, player, renderEvent, bufferSource, poseStack, frustum, font, graphics, camera, renderGameInfo, f, f2);
        ((BootcampGame)this.game).method_3252().forEach(bootcampTexturePoint -> BFRendering.bootcampTexturePoint(bootcampTexturePoint, poseStack, graphics, camera, level, player, f));
    }

    @Override
    public boolean method_2709(@NotNull Minecraft minecraft, @NotNull LivingEntity livingEntity, @NotNull LocalPlayer localPlayer, @NotNull ClientLevel clientLevel) {
        return true;
    }

    @Override
    public boolean method_2719() {
        return true;
    }

    @Override
    @NotNull
    public List<MinimapWaypoint> getSpecificMinimapWaypoints(@NotNull Minecraft minecraft, @NotNull Set<UUID> players, @NotNull LocalPlayer player, @NotNull ClientLevel level) {
        ObjectArrayList objectArrayList = new ObjectArrayList();
        UUID uUID = minecraft.getUser().getProfileId();
        for (Entity object : level.entitiesForRendering()) {
            boolean bl = false;
            if (object instanceof Player) {
                Player player2 = (Player)object;
                UUID uUID2 = player2.getUUID();
                bl = true;
                if (!players.contains(uUID2) || uUID2.equals(uUID)) continue;
            }
            if (!bl && !(object instanceof HumanEntity)) continue;
            float f = object.getYRot() - 180.0f;
            objectArrayList.add(new MinimapWaypoint(MinimapWaypoint.TEXTURE_PLAYER, object.position()).method_353(4).method_352(f));
        }
        for (BootcampTexturePoint bootcampTexturePoint : ((BootcampGame)this.game).method_3252()) {
            objectArrayList.add(new MinimapWaypoint(BFRes.loc("textures/misc/waypoints/" + bootcampTexturePoint.field_3278 + ".png"), bootcampTexturePoint.position).method_353(4));
        }
        return objectArrayList;
    }

    @Override
    public boolean method_2718() {
        return false;
    }

    @Override
    public void method_2710(@NotNull Minecraft minecraft, @NotNull RenderNameTagEvent renderNameTagEvent, @NotNull LocalPlayer localPlayer, @NotNull ClientLevel clientLevel) {
        Player player;
        Entity entity = renderNameTagEvent.getEntity();
        if (entity instanceof Player && ((player = (Player)entity).distanceTo((Entity)localPlayer) > 5.0f || !player.hasLineOfSight((Entity)localPlayer))) {
            renderNameTagEvent.setCanRender(TriState.FALSE);
        }
    }

    @Override
    public boolean method_2730(@NotNull Player player) {
        return true;
    }

    @Override
    @NotNull
    public AbstractGameClient.Scoreboard getScoreboard() {
        return new AbstractGameClient.Scoreboard().column("PING", (TriFunction<BFClientPlayerData, PlayerInfo, FDSTagCompound, String>)((TriFunction)(bFClientPlayerData, playerInfo, fDSTagCompound) -> playerInfo != null ? StringUtils.formatLong(playerInfo.getLatency()) : "???")).column("SCORE", (TriFunction<BFClientPlayerData, PlayerInfo, FDSTagCompound, String>)((TriFunction)(bFClientPlayerData, playerInfo, fDSTagCompound) -> StringUtils.formatLong(fDSTagCompound.getInteger(BFStats.SCORE.getKey()))));
    }

    @Override
    public ResourceLocation method_2696(@NotNull UUID uUID, @Nullable String string, @NotNull Set<UUID> set) {
        return field_3351;
    }

    @Override
    @NotNull
    public Collection<? extends MatchSummaryScreen> getSummaryScreens(boolean bl) {
        return this.getSummaryScreens((BootcampGame)this.game, bl);
    }
}

