package dev.tomerdev.mercfrontcore.setup;

import com.boehmod.bflib.cloud.common.CloudRegistry;
import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.common.BFAbstractManager;

final class CloudRegistryAccess {
    private CloudRegistryAccess() {
    }

    static CloudRegistry resolveRegistryObject() {
        try {
            BlockFront blockFront = BlockFront.getInstance();
            if (blockFront == null) {
                return null;
            }
            BFAbstractManager<?, ?, ?> manager = blockFront.getManager();
            if (manager == null) {
                return null;
            }
            return manager.getCloudRegistry();
        } catch (Throwable ignored) {
            return null;
        }
    }
}
