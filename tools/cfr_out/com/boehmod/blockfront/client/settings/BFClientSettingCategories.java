/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.settings;

import com.boehmod.blockfront.client.settings.BFClientSettingCategory;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class BFClientSettingCategories {
    @NotNull
    public static final List<BFClientSettingCategory> INSTANCES = new ObjectArrayList();
    public static final BFClientSettingCategory NOTIFICATIONS = BFClientSettingCategories.register(new BFClientSettingCategory("bf.settings.category.notifications"));
    public static final BFClientSettingCategory CONTENT = BFClientSettingCategories.register(new BFClientSettingCategory("bf.settings.category.content"));
    public static final BFClientSettingCategory AUDIO = BFClientSettingCategories.register(new BFClientSettingCategory("bf.settings.category.audio"));
    public static final BFClientSettingCategory CROSSHAIR = BFClientSettingCategories.register(new BFClientSettingCategory("bf.settings.category.crosshair"));
    public static final BFClientSettingCategory UI = BFClientSettingCategories.register(new BFClientSettingCategory("bf.settings.category.ui"));
    public static final BFClientSettingCategory EXPERIMENTAL = BFClientSettingCategories.register(new BFClientSettingCategory("bf.settings.category.experimental"));
    public static final BFClientSettingCategory DEBUG = BFClientSettingCategories.register(new BFClientSettingCategory("bf.settings.category.debug"));
    public static final BFClientSettingCategory RULES = BFClientSettingCategories.register(new BFClientSettingCategory("bf.settings.category.rules", false));

    @NotNull
    public static BFClientSettingCategory register(@NotNull BFClientSettingCategory category) {
        INSTANCES.add(category);
        return category;
    }
}

