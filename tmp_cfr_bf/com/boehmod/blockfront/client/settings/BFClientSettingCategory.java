/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.settings;

import com.boehmod.blockfront.client.settings.BFClientSetting;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class BFClientSettingCategory {
    private final String name;
    private final List<BFClientSetting> field_2010 = new ObjectArrayList();
    private final boolean field_2011;

    public BFClientSettingCategory(@NotNull String name, boolean bl) {
        this.name = name;
        this.field_2011 = bl;
    }

    public BFClientSettingCategory(@NotNull String string) {
        this(string, true);
    }

    public void method_1518(@NotNull BFClientSetting bFClientSetting) {
        this.field_2010.add(bFClientSetting);
    }

    public String getName() {
        return this.name;
    }

    public List<BFClientSetting> method_1519() {
        return Collections.unmodifiableList(this.field_2010);
    }

    public boolean method_1517() {
        return this.field_2011;
    }
}

