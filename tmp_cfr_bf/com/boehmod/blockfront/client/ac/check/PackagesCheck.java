/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.ac.check;

import com.boehmod.blockfront.client.ac.BFClientAntiCheat;
import com.boehmod.blockfront.client.ac.check.IAntiCheatCheck;
import com.boehmod.blockfront.util.BFLog;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

public class PackagesCheck
implements IAntiCheatCheck<BFClientAntiCheat> {
    private static final int field_6266 = 20;
    private static final Set<String> ALLOWED_PACKAGES = new HashSet<String>();
    private int field_6267 = 0;

    @Override
    public boolean run(@NotNull Minecraft minecraft, @NotNull BFClientAntiCheat bFClientAntiCheat) {
        Package[] packageArray;
        if (this.field_6267-- > 0) {
            return false;
        }
        this.field_6267 = 20;
        int n = 0;
        for (Package package_ : packageArray = ClassLoader.getSystemClassLoader().getDefinedPackages()) {
            String string = package_.getName();
            boolean bl = ALLOWED_PACKAGES.stream().anyMatch(string::startsWith);
            if (bl) continue;
            BFLog.log("[AC] Detected unknown package '" + string + "'.", new Object[0]);
            ++n;
        }
        return n > 0;
    }

    @Override
    @NotNull
    public BFClientAntiCheat.Stage getStage() {
        return BFClientAntiCheat.Stage.RUNTIME;
    }

    static {
        ALLOWED_PACKAGES.add("net.neoforged");
        ALLOWED_PACKAGES.add("net.minecraft");
        ALLOWED_PACKAGES.add("com.mojang");
        ALLOWED_PACKAGES.add("com.boehmod");
        ALLOWED_PACKAGES.add("com.google.gson");
        ALLOWED_PACKAGES.add("com.google.common");
        ALLOWED_PACKAGES.add("org.apache.maven");
        ALLOWED_PACKAGES.add("software.bernie");
        ALLOWED_PACKAGES.add("net.labymod.opus");
        ALLOWED_PACKAGES.add("cpw.mods");
        ALLOWED_PACKAGES.add("org.objectweb.asm");
        ALLOWED_PACKAGES.add("it.unimi.dsi");
        ALLOWED_PACKAGES.add("org.openjdk");
        ALLOWED_PACKAGES.add("org.prismlauncher");
        ALLOWED_PACKAGES.add("io.github.zekerzhayard.forgewrapper");
        ALLOWED_PACKAGES.add("org.multimc");
        ALLOWED_PACKAGES.add("de.jcm.discordgamesdk");
        ALLOWED_PACKAGES.add("com.intellij");
        ALLOWED_PACKAGES.add("net.covers1624.devlogin");
        ALLOWED_PACKAGES.add("com.modrinth");
    }
}

