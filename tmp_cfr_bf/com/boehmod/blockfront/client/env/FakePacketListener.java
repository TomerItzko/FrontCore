/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.env;

import com.boehmod.blockfront.client.env.FakeRegistry;
import com.boehmod.blockfront.client.screen.title.LobbyTitleScreen;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.telemetry.WorldSessionTelemetryManager;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.ServerLinks;
import net.minecraft.world.flag.FeatureFlagSet;
import net.neoforged.neoforge.network.connection.ConnectionType;
import org.jetbrains.annotations.NotNull;

public class FakePacketListener
extends ClientPacketListener {
    private static final Map<String, String> REPORT_DETAILS = new HashMap<String, String>();
    private static RegistryAccess.Frozen registryAccess;

    public FakePacketListener(@NotNull Minecraft minecraft) {
        super(minecraft, new Connection(PacketFlow.CLIENTBOUND), FakePacketListener.createCookie(minecraft));
    }

    @NotNull
    public static CommonListenerCookie createCookie(@NotNull Minecraft minecraft) {
        return new CommonListenerCookie(minecraft.getGameProfile(), FakePacketListener.getTelemetryManager(minecraft), FakePacketListener.getRegistryAccess().freeze(), FeatureFlagSet.of(), "local", FakePacketListener.getServerData(), (Screen)new LobbyTitleScreen(), null, null, false, REPORT_DETAILS, ServerLinks.EMPTY, ConnectionType.OTHER);
    }

    @NotNull
    public static RegistryAccess.Frozen getRegistryAccess() {
        if (registryAccess != null) {
            return registryAccess;
        }
        ObjectArrayList objectArrayList = new ObjectArrayList();
        objectArrayList.add(new FakeRegistry(Registries.DAMAGE_TYPE));
        objectArrayList.add(new FakeRegistry(Registries.BIOME));
        registryAccess = new RegistryAccess.ImmutableRegistryAccess((List)objectArrayList).freeze();
        return registryAccess;
    }

    @NotNull
    public static ServerData getServerData() {
        return new ServerData("Local", "localhost", ServerData.Type.OTHER);
    }

    @NotNull
    public static WorldSessionTelemetryManager getTelemetryManager(@NotNull Minecraft minecraft) {
        return minecraft.getTelemetryManager().createWorldSessionManager(false, Duration.ZERO, null);
    }

    @NotNull
    public RegistryAccess.Frozen registryAccess() {
        return registryAccess;
    }
}

