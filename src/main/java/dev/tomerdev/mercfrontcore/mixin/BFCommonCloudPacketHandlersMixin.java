package dev.tomerdev.mercfrontcore.mixin;

import com.boehmod.bflib.cloud.connection.Connection;
import com.boehmod.bflib.cloud.packet.common.requests.PacketRequestedPlayerData;
import com.boehmod.bflib.cloud.packet.common.requests.PacketRequestedPlayerDataSet;
import dev.tomerdev.mercfrontcore.data.AddonCommonData;
import java.io.IOException;
import java.util.UUID;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "com.boehmod.blockfront.cloud.common.BFCommonCloudPacketHandlers")
public abstract class BFCommonCloudPacketHandlersMixin {
    @Inject(method = "requestedPlayerData", at = @At("TAIL"))
    private static void mercfrontcore$reapplyRequestedPlayerData(
        PacketRequestedPlayerData packet,
        Connection<?> connection,
        CallbackInfo ci
    ) throws IOException {
        AddonCommonData.getInstance().reapplyProfileOverride(packet.uuid());
    }

    @Inject(method = "requestedPlayerDataSet", at = @At("TAIL"))
    private static void mercfrontcore$reapplyRequestedPlayerDataSet(
        PacketRequestedPlayerDataSet packet,
        Connection<?> connection,
        CallbackInfo ci
    ) throws IOException {
        for (UUID uuid : packet.dataSet().keySet()) {
            AddonCommonData.getInstance().reapplyProfileOverride(uuid);
        }
    }
}
