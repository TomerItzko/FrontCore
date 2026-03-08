/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.CubeMap
 *  org.jetbrains.annotations.Nullable
 */
package com.boehmod.blockfront.client.gui;

import com.boehmod.blockfront.util.BFRes;
import net.minecraft.client.renderer.CubeMap;
import org.jetbrains.annotations.Nullable;

public class BFVehiclePanoramas {
    public static final CubeMap TANK_DRIVER = new CubeMap(BFRes.loc("textures/gui/vehicle/panorama/tank_driver/panorama"));
    public static final CubeMap TANK_SHERMAN_GUNNER = new CubeMap(BFRes.loc("textures/gui/vehicle/panorama/tank_sherman_gunner/panorama"));
    public static final CubeMap TANK_CHIHA_GUNNER = new CubeMap(BFRes.loc("textures/gui/vehicle/panorama/tank_chiha_gunner/panorama"));
    public static final CubeMap TANK_PANZERIV_GUNNER = new CubeMap(BFRes.loc("textures/gui/vehicle/panorama/tank_panzeriv_gunner/panorama"));

    @Nullable
    public static CubeMap getCubeMap(String name) {
        return switch (name) {
            case "tank_driver" -> TANK_DRIVER;
            case "tank_sherman_gunner" -> TANK_SHERMAN_GUNNER;
            case "tank_chiha_gunner" -> TANK_CHIHA_GUNNER;
            case "tank_panzeriv_gunner" -> TANK_PANZERIV_GUNNER;
            default -> null;
        };
    }
}

