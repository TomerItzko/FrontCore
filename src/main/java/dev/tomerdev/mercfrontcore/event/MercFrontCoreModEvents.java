package dev.tomerdev.mercfrontcore.event;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import dev.tomerdev.mercfrontcore.net.packet.ClearProfileOverridesPacket;
import dev.tomerdev.mercfrontcore.net.packet.GiveGunPacket;
import dev.tomerdev.mercfrontcore.net.packet.GunExtraOptionsPacket;
import dev.tomerdev.mercfrontcore.net.packet.GunModifiersPacket;
import dev.tomerdev.mercfrontcore.net.packet.LeaderboardRequestPacket;
import dev.tomerdev.mercfrontcore.net.packet.LeaderboardResponsePacket;
import dev.tomerdev.mercfrontcore.net.packet.LoadoutsPacket;
import dev.tomerdev.mercfrontcore.net.packet.MapEffectPositionPacket;
import dev.tomerdev.mercfrontcore.net.packet.NewProfileOverridesPacket;
import dev.tomerdev.mercfrontcore.net.packet.PlayerGunSkinStatePacket;
import dev.tomerdev.mercfrontcore.net.packet.ProfileXpSyncPacket;
import dev.tomerdev.mercfrontcore.net.packet.SelectPlayerGunSkinPacket;
import dev.tomerdev.mercfrontcore.net.packet.SetClassRanksPacket;
import dev.tomerdev.mercfrontcore.net.packet.SetProfileOverridesPacket;
import dev.tomerdev.mercfrontcore.net.packet.SetProfileOverridesPropertyPacket;
import dev.tomerdev.mercfrontcore.net.packet.ViewSpawnsPacket;

public final class MercFrontCoreModEvents {
    private MercFrontCoreModEvents() {
    }

    public static void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(
            SetProfileOverridesPacket.ID,
            SetProfileOverridesPacket.PACKET_CODEC,
            SetProfileOverridesPacket::handleClient
        );
        registrar.playToClient(
            NewProfileOverridesPacket.ID,
            NewProfileOverridesPacket.PACKET_CODEC,
            NewProfileOverridesPacket::handleClient
        );
        registrar.playToClient(
            ClearProfileOverridesPacket.ID,
            ClearProfileOverridesPacket.PACKET_CODEC,
            ClearProfileOverridesPacket::handleClient
        );
        registrar.playToClient(
            SetProfileOverridesPropertyPacket.ID,
            SetProfileOverridesPropertyPacket.PACKET_CODEC,
            SetProfileOverridesPropertyPacket::handleClient
        );
        registrar.playToClient(
            GunExtraOptionsPacket.ID,
            GunExtraOptionsPacket.PACKET_CODEC,
            GunExtraOptionsPacket::handleClient
        );
        registrar.playToClient(
            PlayerGunSkinStatePacket.ID,
            PlayerGunSkinStatePacket.PACKET_CODEC,
            PlayerGunSkinStatePacket::handleClient
        );
        registrar.playToClient(
            ProfileXpSyncPacket.ID,
            ProfileXpSyncPacket.PACKET_CODEC,
            ProfileXpSyncPacket::handleClient
        );
        registrar.playToClient(
            SetClassRanksPacket.ID,
            SetClassRanksPacket.PACKET_CODEC,
            SetClassRanksPacket::handleClient
        );
        registrar.playToClient(
            LeaderboardResponsePacket.ID,
            LeaderboardResponsePacket.PACKET_CODEC,
            LeaderboardResponsePacket::handleClient
        );
        registrar.playToServer(
            GiveGunPacket.ID,
            GiveGunPacket.PACKET_CODEC,
            GiveGunPacket::handleServer
        );
        registrar.playToServer(
            SelectPlayerGunSkinPacket.ID,
            SelectPlayerGunSkinPacket.PACKET_CODEC,
            SelectPlayerGunSkinPacket::handleServer
        );
        registrar.playBidirectional(
            GunModifiersPacket.ID,
            GunModifiersPacket.PACKET_CODEC,
            new DirectionalPayloadHandler<>(GunModifiersPacket::handleClient, GunModifiersPacket::handleServer)
        );
        registrar.playBidirectional(
            LoadoutsPacket.ID,
            LoadoutsPacket.PACKET_CODEC,
            new DirectionalPayloadHandler<>(LoadoutsPacket::handleClient, LoadoutsPacket::handleServer)
        );
        registrar.playToServer(
            MapEffectPositionPacket.ID,
            MapEffectPositionPacket.PACKET_CODEC,
            MapEffectPositionPacket::handleServer
        );
        registrar.playToServer(
            LeaderboardRequestPacket.ID,
            LeaderboardRequestPacket.PACKET_CODEC,
            LeaderboardRequestPacket::handleServer
        );
        registrar.playToClient(
            ViewSpawnsPacket.ID,
            ViewSpawnsPacket.PACKET_CODEC,
            ViewSpawnsPacket::handleClient
        );
    }
}
