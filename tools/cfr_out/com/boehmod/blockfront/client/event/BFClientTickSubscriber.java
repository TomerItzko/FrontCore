/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  javax.annotation.Nullable
 *  net.minecraft.Util
 *  net.minecraft.client.Camera
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.multiplayer.ClientLevel
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.core.BlockPos
 *  net.minecraft.util.Mth
 *  net.minecraft.world.level.levelgen.Heightmap$Types
 *  net.minecraft.world.phys.Vec3
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.common.EventBusSubscriber
 *  net.neoforged.neoforge.client.event.ClientTickEvent$Post
 *  net.neoforged.neoforge.client.event.ClientTickEvent$Pre
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.event;

import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.event.tick.ClientTickable;
import com.boehmod.blockfront.client.event.tick.DebugBoxTickable;
import com.boehmod.blockfront.client.event.tick.DebugLineTickable;
import com.boehmod.blockfront.client.event.tick.FallingBlockTickable;
import com.boehmod.blockfront.client.event.tick.GunAimingTickable;
import com.boehmod.blockfront.client.event.tick.GunSpreadTickable;
import com.boehmod.blockfront.client.event.tick.MenuMusicTickable;
import com.boehmod.blockfront.client.event.tick.PlayerTickable;
import com.boehmod.blockfront.client.event.tick.RenderingEnvironmentTickable;
import com.boehmod.blockfront.client.event.tick.VideoSettingsTickable;
import com.boehmod.blockfront.client.player.BFClientPlayerData;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.common.entity.AbstractVehicleEntity;
import com.boehmod.blockfront.common.gun.bullet.BulletTracer;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.unnamed.BF_1163;
import com.boehmod.blockfront.unnamed.BF_1164;
import com.boehmod.blockfront.unnamed.BF_29;
import com.boehmod.blockfront.unnamed.BF_33;
import com.boehmod.blockfront.unnamed.BF_36;
import com.boehmod.blockfront.unnamed.BF_39;
import com.boehmod.blockfront.unnamed.BF_43;
import com.boehmod.blockfront.util.debug.DebugBox;
import com.boehmod.blockfront.util.debug.DebugLine;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid="bf", value={Dist.CLIENT})
public final class BFClientTickSubscriber {
    @NotNull
    public static final List<BulletTracer> BULLETS = new ObjectArrayList();
    @NotNull
    public static final List<DebugLine> DEBUG_LINES = new ObjectArrayList();
    @NotNull
    public static final List<DebugBox> DEBUG_BOXES = new ObjectArrayList();
    @NotNull
    private static final List<ClientTickable> TICK_RUNNERS = (List)Util.make((Object)new ObjectArrayList(), list -> {
        list.add((Object)new BF_36());
        list.add((Object)new GunSpreadTickable());
        list.add((Object)new BF_1163());
        list.add((Object)new BF_29());
        list.add((Object)new BF_33());
        list.add((Object)new BF_39());
        list.add((Object)new BF_1164());
        list.add((Object)new GunAimingTickable());
        list.add((Object)new PlayerTickable());
        list.add((Object)new MenuMusicTickable());
        list.add((Object)new VideoSettingsTickable());
        list.add((Object)new RenderingEnvironmentTickable());
        list.add((Object)new FallingBlockTickable());
        list.add((Object)new DebugLineTickable());
        list.add((Object)new DebugBoxTickable());
        list.add((Object)new BF_43());
    });
    public static int muzzleFlashIndex = 0;

    @SubscribeEvent
    public static void onTickPre(@NotNull ClientTickEvent.Pre event) {
        Minecraft minecraft = Minecraft.getInstance();
        BFClientTickSubscriber.updateVehicleGui(minecraft, minecraft.player);
    }

    @SubscribeEvent
    public static void onTickPost(@NotNull ClientTickEvent.Post event) {
        boolean bl;
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        LocalPlayer localPlayer = minecraft.player;
        ClientLevel clientLevel = minecraft.level;
        float f = BFRendering.getRenderTime();
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        ClientPlayerDataHandler clientPlayerDataHandler = (ClientPlayerDataHandler)bFClientManager.getPlayerDataHandler();
        BFClientPlayerData bFClientPlayerData = clientPlayerDataHandler.getPlayerData(minecraft);
        AbstractGame<?, ?, ?> abstractGame = bFClientManager.getGame();
        BFClientTickSubscriber.updateVehicleGui(minecraft, localPlayer);
        boolean bl2 = bl = clientLevel != null && localPlayer != null;
        if (++muzzleFlashIndex > 3) {
            muzzleFlashIndex = 0;
        }
        PlayerCloudData playerCloudData = clientPlayerDataHandler.getCloudData(minecraft);
        boolean bl3 = bl && clientLevel.getHeight(Heightmap.Types.MOTION_BLOCKING, localPlayer.getBlockX(), localPlayer.getBlockZ()) > Mth.floor((float)((float)localPlayer.getY()));
        Camera camera = minecraft.gameRenderer.getMainCamera();
        Vec3 vec3 = camera.getPosition();
        BlockPos blockPos = camera.getBlockPosition();
        TICK_RUNNERS.forEach(clientTickable -> clientTickable.run(event, threadLocalRandom, minecraft, clientPlayerDataHandler, bFClientManager, localPlayer, clientLevel, bFClientPlayerData, playerCloudData, vec3, blockPos, abstractGame, bl3, f));
    }

    private static void updateVehicleGui(@NotNull Minecraft minecraft, @Nullable LocalPlayer player) {
        if (player != null && player.getVehicle() instanceof AbstractVehicleEntity) {
            minecraft.options.hideGui = false;
        }
    }
}

