/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.util;

import com.boehmod.bflib.cloud.common.item.CloudResourceLocation;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class BFRes {
    @NotNull
    public static ResourceLocation loc(@NotNull String path) {
        return ResourceLocation.fromNamespaceAndPath((String)"bf", (String)path);
    }

    @NotNull
    public static ResourceLocation fromCloud(@NotNull CloudResourceLocation crl) {
        return ResourceLocation.fromNamespaceAndPath((String)crl.namespace(), (String)crl.path());
    }

    @NotNull
    public static CloudResourceLocation toCloud(@NotNull ResourceLocation loc) {
        return new CloudResourceLocation(loc.getNamespace(), loc.getPath());
    }
}

