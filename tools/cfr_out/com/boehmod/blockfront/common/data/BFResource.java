/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.JsonElement
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.DynamicOps
 *  com.mojang.serialization.JsonOps
 *  it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.packs.resources.PreparableReloadListener
 *  net.minecraft.server.packs.resources.ReloadableResourceManager
 *  net.minecraft.server.packs.resources.Resource
 *  net.minecraft.server.packs.resources.ResourceManager
 *  net.minecraft.server.packs.resources.SimplePreparableReloadListener
 *  net.minecraft.util.profiling.InactiveProfiler
 *  net.minecraft.util.profiling.ProfilerFiller
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.common.data;

import com.boehmod.blockfront.util.BFLog;
import com.boehmod.blockfront.util.BFRes;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.InactiveProfiler;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

public abstract class BFResource<A>
extends SimplePreparableReloadListener<Map<ResourceLocation, A>> {
    @NotNull
    private final String name;
    @NotNull
    private final String filename;
    @NotNull
    private final Map<ResourceLocation, A> field_2091;
    @NotNull
    private final Codec<A> codec;
    private final A fallback;
    @NotNull
    Function<A, String> field_2090;

    public BFResource(@NotNull ResourceManager resourceManager, @NotNull String string, @NotNull String string2, @NotNull Codec<A> codec, @NotNull A fallback, @NotNull Function<A, String> function) {
        if (resourceManager instanceof ReloadableResourceManager) {
            ReloadableResourceManager reloadableResourceManager = (ReloadableResourceManager)resourceManager;
            BFLog.log("Registering reload listener for asset loader '" + string + "'", new Object[0]);
            reloadableResourceManager.registerReloadListener((PreparableReloadListener)this);
        }
        this.name = string;
        this.filename = string2;
        this.codec = codec;
        this.fallback = fallback;
        this.field_2090 = function;
        this.field_2091 = this.prepare(resourceManager, (ProfilerFiller)InactiveProfiler.INSTANCE);
    }

    @NotNull
    public A method_1647(@NotNull String string) {
        return this.field_2091.getOrDefault(BFRes.loc(string), this.fallback);
    }

    @NotNull
    public Collection<A> method_1652() {
        return Collections.unmodifiableCollection(this.field_2091.values());
    }

    @NotNull
    protected Map<ResourceLocation, A> prepare(@NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
        Gson gson = new Gson();
        ResourceLocation resourceLocation = BFRes.loc(this.filename);
        List list = resourceManager.getResourceStack(resourceLocation);
        Object2ObjectLinkedOpenHashMap object2ObjectLinkedOpenHashMap = new Object2ObjectLinkedOpenHashMap();
        for (Resource resource : list) {
            this.method_1646(gson, (Map<ResourceLocation, A>)object2ObjectLinkedOpenHashMap, resource);
        }
        BFLog.log("Successfully loaded %s asset(s) for %s", object2ObjectLinkedOpenHashMap.size(), this.name);
        return object2ObjectLinkedOpenHashMap;
    }

    private void method_1646(@NotNull Gson gson, @NotNull Map<ResourceLocation, A> map, @NotNull Resource resource) {
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(resource.open());
            ((List)Codec.list(this.codec).parse((DynamicOps)JsonOps.INSTANCE, (Object)((JsonElement)gson.fromJson((Reader)inputStreamReader, JsonElement.class))).getOrThrow(string -> {
                BFLog.logError("Failed to parse (%s)", resource.toString(), string);
                return new RuntimeException((String)string);
            })).forEach(object -> {
                String string = this.field_2090.apply(object);
                if (!ResourceLocation.isValidNamespace((String)string)) {
                    BFLog.logError("Ignoring asset '%s' for %s (%s)", resource.toString(), this.name, "Non [a-z0-9_.-] character in namespace of location");
                    return;
                }
                map.put(BFRes.loc(string), object);
            });
        }
        catch (IOException iOException) {
            BFLog.log("Failed to read asset '%s' (%s)", resource.toString(), iOException);
        }
    }

    protected void apply(@NotNull Map<ResourceLocation, A> map, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
        this.field_2091.clear();
        this.field_2091.putAll(map);
    }

    protected /* synthetic */ void apply(@NotNull Object object, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
        this.apply((Map)object, resourceManager, profilerFiller);
    }

    @NotNull
    protected /* synthetic */ Object prepare(@NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profilerFiller) {
        return this.prepare(resourceManager, profilerFiller);
    }
}

