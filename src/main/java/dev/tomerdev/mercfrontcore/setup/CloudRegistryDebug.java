package dev.tomerdev.mercfrontcore.setup;

public final class CloudRegistryDebug {
    private CloudRegistryDebug() {
    }

    public static String describe() {
        com.boehmod.bflib.cloud.common.CloudRegistry registry = CloudRegistryAccess.resolveRegistryObject();
        if (registry == null) {
            return "unresolved";
        }
        int count = 0;
        for (Object ignored : registry.getItems()) {
            count++;
        }
        return "resolved items=" + count + " type=" + registry.getClass().getName();
    }
}
