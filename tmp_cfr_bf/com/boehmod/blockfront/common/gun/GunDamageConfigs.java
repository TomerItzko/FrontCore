/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.common.gun;

import com.boehmod.blockfront.common.gun.GunDamageConfig;

public class GunDamageConfigs {
    public static GunDamageConfig NONE = new GunDamageConfig(0.0f, 0.0f);
    public static GunDamageConfig PISTOL = new GunDamageConfig(8.0f, 12.0f).add(8.0f, 5.0f, 8.0f);
    public static GunDamageConfig REVOLVER = new GunDamageConfig(13.0f, 20.0f).add(8.0f, 11.0f, 18.0f).add(16.0f, 9.0f, 16.0f);
    public static GunDamageConfig WELROD = new GunDamageConfig(10.0f, 20.0f);
    public static GunDamageConfig RIFLE = new GunDamageConfig(20.0f, 24.0f).add(8.0f, 17.0f, 21.0f);
    public static GunDamageConfig LT_RIFLE = new GunDamageConfig(12.0f, 20.0f).add(8.0f, 9.0f, 19.0f);
    public static GunDamageConfig SEMI_CARBINE = new GunDamageConfig(10.0f, 18.0f).add(8.0f, 9.0f, 12.0f);
    public static GunDamageConfig SMG = new GunDamageConfig(6.0f, 13.0f);
    public static GunDamageConfig SMG_RAPID = new GunDamageConfig(5.0f, 9.0f);
    public static GunDamageConfig ASSAULT = new GunDamageConfig(7.0f, 14.0f);
    public static GunDamageConfig AUTO_RIFLE = new GunDamageConfig(8.0f, 14.0f);
    public static GunDamageConfig ATR = new GunDamageConfig(40.0f, 80.0f);
    public static GunDamageConfig LMG = new GunDamageConfig(9.0f, 15.0f);
    public static GunDamageConfig HMG = new GunDamageConfig(16.0f, 18.0f);
    public static GunDamageConfig SHOTGUN = new GunDamageConfig(3.0f, 7.0f);
}

