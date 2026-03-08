/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.boehmod.bflib.fds.FDSUtils
 *  com.boehmod.bflib.fds.tag.FDSTagCompound
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.settings;

import com.boehmod.bflib.fds.FDSUtils;
import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.settings.BFClientSettings;
import java.io.File;
import org.jetbrains.annotations.NotNull;

public class BFClientSettingsDisk {
    public static void write(@NotNull BFClientManager manager) {
        String string = manager.getDataPath() + "client/";
        File file = new File(string + "settings.json");
        FDSTagCompound fDSTagCompound = new FDSTagCompound("settings");
        BFClientSettings.INSTANCES.forEach(setting -> setting.writeFDS(fDSTagCompound));
        FDSUtils.writeToFile((File)file, (FDSTagCompound)fDSTagCompound);
        BFClientSettings.isUnsaved = false;
    }

    public static void read(@NotNull BFClientManager manager) {
        String string = manager.getDataPath() + "client/";
        File file = new File(string + "settings.json");
        FDSTagCompound fDSTagCompound = FDSUtils.readFromFile((File)file);
        fDSTagCompound.tag = "settings";
        BFClientSettings.INSTANCES.forEach(setting -> setting.readFDS(fDSTagCompound));
    }
}

