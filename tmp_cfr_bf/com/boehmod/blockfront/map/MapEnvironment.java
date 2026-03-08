/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.map;

import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.BlockFront;
import com.boehmod.blockfront.assets.AssetCommandBuilder;
import com.boehmod.blockfront.assets.AssetCommandValidators;
import com.boehmod.blockfront.assets.AssetRegistry;
import com.boehmod.blockfront.assets.AssetStore;
import com.boehmod.blockfront.assets.impl.MapAsset;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.render.effect.AbstractParticleEffect;
import com.boehmod.blockfront.client.render.effect.BFParticleEffects;
import com.boehmod.blockfront.client.render.effect.WeatherEffectType;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.entity.BotEntity;
import com.boehmod.blockfront.game.AbstractGame;
import com.boehmod.blockfront.game.GameStatus;
import com.boehmod.blockfront.map.effect.AbstractMapEffect;
import com.boehmod.blockfront.map.effect.ConditionedMapEffect;
import com.boehmod.blockfront.map.effect.EntitySpawnMapEffect;
import com.boehmod.blockfront.map.effect.MapEffectRegistry;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.util.CommandUtils;
import com.boehmod.blockfront.util.RegistryUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.awt.Color;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MapEnvironment {
    public static final String DEFAULT_NAME = "default";
    private int time;
    @NotNull
    private Supplier<SoundEvent> exteriorSound = BFSounds.AMBIENT_LOOP_WIND;
    @NotNull
    private Supplier<SoundEvent> interiorSound = BFSounds.AMBIENT_LOOP_INTERIOR_HOUSE;
    @NotNull
    private final EnumSet<WeatherEffectType> weatherEffects = EnumSet.noneOf(WeatherEffectType.class);
    @NotNull
    private final List<AbstractMapEffect> mapEffects = new ObjectArrayList();
    @Nullable
    private ResourceLocation shader = null;
    private final String name;
    private boolean hasCustomFogDensity = false;
    private float nearFogDensity = 0.0f;
    private float farFogDensity = 0.0f;
    private boolean hasCustomSkyColor = false;
    private int customSkyColor = 0xFFFFFF;
    private boolean hasCustomWaterColor = false;
    private int customWaterColor = 0xFFFFFF;
    private boolean hasCustomFogColor = false;
    private int customFogColor = 0xFFFFFF;
    private boolean hasCustomLightColor = false;
    private int customLightColor = 0xFFFFFF;
    private boolean disableClouds = false;
    private boolean disableSky = false;
    @NotNull
    private final AssetCommandBuilder command = new AssetCommandBuilder().subCommand("clouds", new AssetCommandBuilder((commandContext, stringArray) -> {
        String string = stringArray[0];
        this.disableClouds = string.equals("disable");
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Successfully " + (this.disableClouds ? "disabled" : "enabled") + " clouds.")));
    }).validator(AssetCommandValidators.onlyAllowed(new String[]{"enable", "disable"}))).subCommand("sky", new AssetCommandBuilder((commandContext, stringArray) -> {
        String string = stringArray[0];
        this.disableSky = string.equals("disable");
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Successfully " + (this.disableSky ? "disabled" : "enabled") + " sky.")));
    }).validator(AssetCommandValidators.onlyAllowed(new String[]{"enable", "disable"}))).subCommand("fogDensity", new AssetCommandBuilder().subCommand("clear", new AssetCommandBuilder((commandContext, stringArray) -> {
        this.clearCustomFogDensity();
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)"Successfully cleared fog density."));
    })).subCommand("set", new AssetCommandBuilder((commandContext, stringArray) -> {
        float f = Float.parseFloat(stringArray[0]);
        float f2 = Float.parseFloat(stringArray[1]);
        this.setCustomFogDensity(f, f2);
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Successfully set fog density to near: " + f + ", far: " + f2 + ".")));
    }).validator(AssetCommandValidators.count(new String[]{"near", "far"})))).subCommand("shader", new AssetCommandBuilder().subCommand("clear", new AssetCommandBuilder((commandContext, stringArray) -> {
        this.clearShader();
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)"Successfully cleared shader."));
    })).subCommand("set", new AssetCommandBuilder((commandContext, stringArray) -> {
        String string = stringArray[0];
        ResourceLocation resourceLocation = ResourceLocation.tryParse((String)string);
        if (resourceLocation == null) {
            CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Invalid shader name: " + string)).withStyle(ChatFormatting.RED));
            return;
        }
        this.setShader(resourceLocation);
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Successfully set shader to: " + string + ".")));
    }).validator(AssetCommandValidators.count(new String[]{"shader"})))).subCommand("skyColor", new AssetCommandBuilder().subCommand("clear", new AssetCommandBuilder((commandContext, stringArray) -> {
        this.clearCustomSkyColor();
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)"Successfully cleared custom sky color."));
    })).subCommand("set", new AssetCommandBuilder((commandContext, stringArray) -> {
        String string = stringArray[0];
        int n = Color.decode(string).getRGB();
        this.setCustomSkyColor(n);
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Successfully set custom sky color to: " + string + ".")));
    }).validator(AssetCommandValidators.count(new String[]{"color"})))).subCommand("waterColor", new AssetCommandBuilder().subCommand("clear", new AssetCommandBuilder((commandContext, stringArray) -> {
        this.clearCustomWaterColor();
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)"Successfully cleared custom water color."));
    })).subCommand("set", new AssetCommandBuilder((commandContext, stringArray) -> {
        String string = stringArray[0];
        int n = Color.decode(string).getRGB();
        this.setCustomWaterColor(n);
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Successfully set custom water color to: " + string + ".")));
    }).validator(AssetCommandValidators.count(new String[]{"color"})))).subCommand("fogColor", new AssetCommandBuilder().subCommand("clear", new AssetCommandBuilder((commandContext, stringArray) -> {
        this.clearCustomFogColor();
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)"Successfully cleared custom fog color."));
    })).subCommand("set", new AssetCommandBuilder((commandContext, stringArray) -> {
        String string = stringArray[0];
        int n = Color.decode(string).getRGB();
        this.setCustomFogColor(n);
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Successfully set custom fog color to: " + string + ".")));
    }).validator(AssetCommandValidators.count(new String[]{"color"})))).subCommand("lightColor", new AssetCommandBuilder().subCommand("clear", new AssetCommandBuilder((commandContext, stringArray) -> {
        this.clearCustomLightColor();
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)"Successfully cleared custom light color."));
    })).subCommand("set", new AssetCommandBuilder((commandContext, stringArray) -> {
        String string = stringArray[0];
        int n = Color.decode(string).getRGB();
        this.setCustomLightColor(n);
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Successfully set custom light color to: " + string + ".")));
    }).validator(AssetCommandValidators.count(new String[]{"color"})))).subCommand("time", new AssetCommandBuilder((commandContext, stringArray) -> {
        int n = Integer.parseInt(stringArray[0]);
        this.setTime(n);
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Successfully set time to: " + n + ".")));
    }).validator(AssetCommandValidators.count(new String[]{"time"}))).subCommand("exteriorSound", new AssetCommandBuilder((commandContext, stringArray) -> {
        String string = stringArray[0];
        Supplier<SoundEvent> supplier = RegistryUtils.retrieveSoundEvent(string);
        if (supplier == null) {
            CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Invalid sound key: " + string)).withStyle(ChatFormatting.RED));
            return;
        }
        this.setExteriorSound(supplier);
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Successfully set exterior sound to: " + string + ".")));
    }).validator(AssetCommandValidators.count(new String[]{"sound"}))).subCommand("interiorSound", new AssetCommandBuilder((commandContext, stringArray) -> {
        String string = stringArray[0];
        Supplier<SoundEvent> supplier = RegistryUtils.retrieveSoundEvent(string);
        if (supplier == null) {
            CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Invalid sound key: " + string)).withStyle(ChatFormatting.RED));
            return;
        }
        this.setInteriorSound(supplier);
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Successfully set interior sound to: " + string + ".")));
    }).validator(AssetCommandValidators.count(new String[]{"sound"}))).subCommand("weather", new AssetCommandBuilder().subCommand("add", new AssetCommandBuilder((commandContext, stringArray) -> {
        WeatherEffectType weatherEffectType = WeatherEffectType.fromId(stringArray[0]);
        if (weatherEffectType == null) {
            CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Invalid weather type: " + stringArray[0])).withStyle(ChatFormatting.RED));
            return;
        }
        this.addParticleEffect(weatherEffectType);
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Successfully added weather effect: " + weatherEffectType.name() + ".")));
    }).validator(AssetCommandValidators.count(new String[]{"weather"}))).subCommand("clear", new AssetCommandBuilder((commandContext, stringArray) -> {
        int n = this.weatherEffects.size();
        this.clearParticleEffects();
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Successfully cleared all " + n + " weather effects.")));
    }))).subCommand("entitySpawn", new AssetCommandBuilder().subCommand("add", new AssetCommandBuilder((commandContext, stringArray) -> {
        String string = stringArray[0];
        int n = Integer.parseInt(stringArray[1]);
        Supplier<EntityType<?>> supplier = RegistryUtils.retrieveEntityType(string);
        ServerPlayer serverPlayer = ((CommandSourceStack)commandContext.getSource()).getPlayer();
        assert (serverPlayer != null);
        Vec3 vec3 = serverPlayer.position();
        EntitySpawnMapEffect entitySpawnMapEffect = new EntitySpawnMapEffect(vec3, supplier, n);
        this.addMapEffect(entitySpawnMapEffect);
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Successfully added entity spawn effect for " + string + " with count: " + n + ".")));
    }).validator(AssetCommandValidators.ONLY_PLAYERS).validator(AssetCommandValidators.count(new String[]{"entity", "count"}))).subCommand("addLeashed", new AssetCommandBuilder((commandContext, stringArray) -> {
        String string = stringArray[0];
        int n = Integer.parseInt(stringArray[1]);
        int n2 = Integer.parseInt(stringArray[2]);
        int n3 = Integer.parseInt(stringArray[3]);
        int n4 = Integer.parseInt(stringArray[4]);
        BlockPos blockPos = new BlockPos(n2, n3, n4);
        Supplier<EntityType<?>> supplier = RegistryUtils.retrieveEntityType(string);
        ServerPlayer serverPlayer = ((CommandSourceStack)commandContext.getSource()).getPlayer();
        assert (serverPlayer != null);
        Vec3 vec3 = serverPlayer.position();
        EntitySpawnMapEffect entitySpawnMapEffect = new EntitySpawnMapEffect(vec3, supplier, n).method_5870(blockPos);
        this.addMapEffect(entitySpawnMapEffect);
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Successfully added leashed entity spawn effect for " + string + " with count: " + n + ".")));
    }).validator(AssetCommandValidators.ONLY_PLAYERS).validator(AssetCommandValidators.count(new String[]{"entity", "count", "leashX", "leashY", "leashZ"}))).subCommand("clear", new AssetCommandBuilder((commandContext, stringArray) -> {
        this.mapEffects.removeIf(abstractMapEffect -> abstractMapEffect instanceof EntitySpawnMapEffect);
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)"Successfully cleared all entity spawn effects."));
    })).subCommand("clone", new AssetCommandBuilder((commandContext, stringArray) -> {
        String string = stringArray[0];
        String string2 = stringArray[1];
        BFAbstractManager<?, ?, ?> bFAbstractManager = BlockFront.getInstance().getManager();
        assert (bFAbstractManager != null) : "Mod manager is null!";
        AssetStore assetStore = bFAbstractManager.getAssetStore();
        AssetRegistry<MapAsset> assetRegistry = assetStore.getRegistry(MapAsset.class);
        MapAsset mapAsset = assetRegistry.getByName(string);
        if (mapAsset == null) {
            CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Map type '" + string + "' does not exist.")).withStyle(ChatFormatting.RED));
            return;
        }
        ObjectArrayList objectArrayList = new ObjectArrayList();
        for (AbstractMapEffect abstractMapEffect : this.mapEffects) {
            if (!(abstractMapEffect instanceof EntitySpawnMapEffect)) continue;
            EntitySpawnMapEffect entitySpawnMapEffect = (EntitySpawnMapEffect)abstractMapEffect;
            objectArrayList.add((Object)entitySpawnMapEffect);
        }
        if (objectArrayList.isEmpty()) {
            CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)"No entity spawn effects found to clone.").withStyle(ChatFormatting.RED));
            return;
        }
        MapEnvironment mapEnvironment = mapAsset.getEnvironments().get(string2);
        if (mapEnvironment == null) {
            CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Environment '" + string2 + "' does not exist in map type '" + string + "'.")).withStyle(ChatFormatting.RED));
            return;
        }
        for (EntitySpawnMapEffect entitySpawnMapEffect : objectArrayList) {
            mapEnvironment.addMapEffect(entitySpawnMapEffect);
        }
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.literal((String)("Successfully cloned " + objectArrayList.size() + " entity spawn effects to environment '" + string2 + "' in map type '" + string + "'.")));
    }).validator(AssetCommandValidators.count(new String[]{"map", "environment"}))));

    public MapEnvironment(@NotNull String name) {
        this.name = name;
    }

    public MapEnvironment(@NotNull ByteBuf buf) throws IOException {
        int n;
        this.name = IPacket.readString((ByteBuf)buf);
        this.time = buf.readInt();
        this.exteriorSound = RegistryUtils.retrieveSoundEvent(IPacket.readString((ByteBuf)buf));
        this.interiorSound = RegistryUtils.retrieveSoundEvent(IPacket.readString((ByteBuf)buf));
        this.hasCustomFogDensity = buf.readBoolean();
        this.nearFogDensity = buf.readFloat();
        this.farFogDensity = buf.readFloat();
        this.hasCustomSkyColor = buf.readBoolean();
        this.customSkyColor = buf.readInt();
        this.hasCustomWaterColor = buf.readBoolean();
        this.customWaterColor = buf.readInt();
        this.hasCustomFogColor = buf.readBoolean();
        this.customFogColor = buf.readInt();
        this.hasCustomLightColor = buf.readBoolean();
        this.customLightColor = buf.readInt();
        this.disableClouds = buf.readBoolean();
        this.disableSky = buf.readBoolean();
        if (buf.readBoolean()) {
            this.shader = ResourceLocation.tryParse((String)IPacket.readString((ByteBuf)buf));
        }
        int n2 = buf.readInt();
        for (n = 0; n < n2; ++n) {
            WeatherEffectType weatherEffectType = (WeatherEffectType)IPacket.readEnum((ByteBuf)buf, WeatherEffectType.class);
            this.weatherEffects.add(weatherEffectType);
        }
        this.mapEffects.clear();
        n = buf.readInt();
        for (int i = 0; i < n; ++i) {
            byte by = buf.readByte();
            Class<?> clazz = MapEffectRegistry.getEffect(by);
            if (clazz == null) continue;
            try {
                AbstractMapEffect abstractMapEffect = (AbstractMapEffect)clazz.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
                FDSTagCompound fDSTagCompound = new FDSTagCompound("mapEffect" + i);
                fDSTagCompound.readData(buf);
                abstractMapEffect.readFromFDS(fDSTagCompound);
                this.mapEffects.add(abstractMapEffect);
                continue;
            }
            catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException reflectiveOperationException) {
                throw new RuntimeException(reflectiveOperationException);
            }
        }
    }

    public MapEnvironment(@NotNull FDSTagCompound root) {
        int n;
        String string;
        this.name = root.getString("name", DEFAULT_NAME);
        this.time = root.getInteger("time");
        String string2 = root.getString("exteriorSound");
        if (string2 != null) {
            this.exteriorSound = RegistryUtils.retrieveSoundEvent(string2);
        }
        if ((string = root.getString("interiorSound")) != null) {
            this.interiorSound = RegistryUtils.retrieveSoundEvent(string);
        }
        this.hasCustomFogDensity = root.getBoolean("hasCustomFogDensity", false);
        this.nearFogDensity = root.getFloat("nearFogDensity", 0.0f);
        this.farFogDensity = root.getFloat("farFogDensity", 0.0f);
        this.hasCustomSkyColor = root.getBoolean("hasCustomSkyColor", false);
        this.customSkyColor = root.getInteger("customSkyColor", 0xFFFFFF);
        this.hasCustomWaterColor = root.getBoolean("hasCustomWaterColor", false);
        this.customWaterColor = root.getInteger("customWaterColor", 0xFFFFFF);
        this.hasCustomFogColor = root.getBoolean("hasCustomFogColor", false);
        this.customFogColor = root.getInteger("customFogColor", 0xFFFFFF);
        this.hasCustomLightColor = root.getBoolean("hasCustomLightColor", false);
        this.customLightColor = root.getInteger("customLightColor", 0xFFFFFF);
        this.disableClouds = root.getBoolean("disableClouds", false);
        this.disableSky = root.getBoolean("disableSky", false);
        String string3 = root.getString("shader");
        if (string3 != null) {
            this.shader = ResourceLocation.tryParse((String)string3);
        }
        this.weatherEffects.clear();
        int n2 = root.getInteger("weatherEffectsSize", 0);
        for (n = 0; n < n2; ++n) {
            try {
                this.weatherEffects.add(WeatherEffectType.values()[root.getInteger("weatherEffect" + n)]);
                continue;
            }
            catch (IndexOutOfBoundsException indexOutOfBoundsException) {
                // empty catch block
            }
        }
        try {
            this.mapEffects.clear();
            n = root.getInteger("mapEffectSize", 0);
            for (int i = 0; i < n; ++i) {
                byte by = root.getByte("mapEffectType" + i);
                Class<?> clazz = MapEffectRegistry.getEffect(by);
                if (clazz == null) continue;
                AbstractMapEffect abstractMapEffect = (AbstractMapEffect)clazz.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
                FDSTagCompound fDSTagCompound = root.getTagCompound("mapEffect" + i);
                if (fDSTagCompound == null) continue;
                abstractMapEffect.readFromFDS(fDSTagCompound);
                this.mapEffects.add(abstractMapEffect);
            }
        }
        catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException reflectiveOperationException) {
            throw new RuntimeException(reflectiveOperationException);
        }
    }

    @NotNull
    public AssetCommandBuilder getCommand() {
        return this.command;
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    public void updateGame(@NotNull AbstractGame<?, ?, ?> game, @NotNull BFAbstractManager<?, ?, ?> manager, @NotNull ServerLevel level, @NotNull Set<UUID> players) {
        if (game.getStatus() == GameStatus.IDLE) {
            return;
        }
        for (AbstractMapEffect abstractMapEffect : this.mapEffects) {
            abstractMapEffect.updateGame(level, manager, game, ThreadLocalRandom.current(), players);
        }
    }

    @OnlyIn(value=Dist.CLIENT)
    public void reset(@NotNull Minecraft minecraft) {
        for (AbstractMapEffect abstractMapEffect : this.mapEffects) {
            if (!(abstractMapEffect instanceof ConditionedMapEffect)) continue;
            ConditionedMapEffect conditionedMapEffect = (ConditionedMapEffect)abstractMapEffect;
            conditionedMapEffect.reset(minecraft);
        }
    }

    @OnlyIn(value=Dist.CLIENT)
    public void triggerCondition(@NotNull Minecraft minecraft, @NotNull ClientLevel level, @NotNull AbstractGame<?, ?, ?> game, @NotNull String effectId) {
        for (AbstractMapEffect abstractMapEffect : this.mapEffects) {
            ConditionedMapEffect conditionedMapEffect;
            if (!(abstractMapEffect instanceof ConditionedMapEffect) || !(conditionedMapEffect = (ConditionedMapEffect)abstractMapEffect).getEffectId().equals(effectId)) continue;
            conditionedMapEffect.onConditionMet(minecraft, level, game);
        }
    }

    @OnlyIn(value=Dist.CLIENT)
    public void updateClient(@NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull AbstractGame<?, ?, ?> game, @NotNull LocalPlayer player, @NotNull ClientLevel level, @NotNull Random random, @NotNull RandomSource randomSource, float delta, @NotNull Vec3 cameraPos, @NotNull BlockPos cameraBlockPos) {
        boolean bl = Minecraft.useFancyGraphics();
        for (AbstractMapEffect object : this.mapEffects) {
            if (!bl && object.requiresFancyGraphics()) continue;
            object.updateGameClient(minecraft, manager, random, game, player, level, delta);
        }
        for (WeatherEffectType weatherEffectType : this.weatherEffects) {
            AbstractParticleEffect abstractParticleEffect = BFParticleEffects.get(weatherEffectType);
            if (abstractParticleEffect == null) continue;
            abstractParticleEffect.render(minecraft, manager, level, level.random, cameraPos, cameraBlockPos);
        }
    }

    @OnlyIn(value=Dist.CLIENT)
    public void render(@NotNull RenderLevelStageEvent event, @NotNull Minecraft minecraft, @NotNull BFClientManager manager, @NotNull LocalPlayer player, @NotNull ClientLevel level, @NotNull BlockRenderDispatcher blockDispatcher, @NotNull MultiBufferSource.BufferSource buffer, @NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, Font font, Camera camera, float renderTime, float delta) {
        boolean bl = Minecraft.useFancyGraphics();
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        this.mapEffects.stream().filter(mapEffect -> (bl || !mapEffect.requiresFancyGraphics()) && mapEffect.getRenderStage() == event.getStage()).forEach(mapEffect -> mapEffect.render(minecraft, manager, player, level, blockDispatcher, buffer, threadLocalRandom, event, graphics, poseStack, camera, renderTime, delta));
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            this.renderDebug(event, minecraft, player, level, buffer, graphics.pose(), graphics, font, camera, delta);
        }
    }

    @OnlyIn(value=Dist.CLIENT)
    private void renderDebug(@NotNull RenderLevelStageEvent event, @NotNull Minecraft minecraft, LocalPlayer player, ClientLevel level, @NotNull MultiBufferSource.BufferSource buffer, @NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, Font font, Camera camera, float delta) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES || level == null || player == null || !minecraft.getDebugOverlay().showDebugScreen() || !player.isCreative()) {
            return;
        }
        for (Entity entity : level.entitiesForRendering()) {
            if (!(entity instanceof BotEntity)) continue;
            BotEntity botEntity = (BotEntity)entity;
            Vec3 vec3 = entity.getEyePosition(delta);
            BlockPos blockPos = botEntity.method_2013();
            BFRendering.billboardLine(camera, poseStack, vec3, blockPos.getCenter(), 1.0f, 0xFFFF00);
        }
        this.mapEffects.forEach(mapEffect -> mapEffect.renderDebug(minecraft, event, buffer, poseStack, graphics, font, camera));
    }

    @NotNull
    public MapEnvironment addMapEffect(@NotNull AbstractMapEffect mapEffect) {
        this.mapEffects.add(mapEffect);
        return this;
    }

    @NotNull
    public MapEnvironment removeMapEffect(@NotNull AbstractMapEffect mapEffect) {
        this.mapEffects.remove(mapEffect);
        return this;
    }

    public boolean hasMapEffect(@NotNull AbstractMapEffect mapEffect) {
        return this.mapEffects.contains(mapEffect);
    }

    @NotNull
    public List<AbstractMapEffect> getMapEffects() {
        return this.mapEffects;
    }

    public void clearMapEffects() {
        this.mapEffects.clear();
    }

    @NotNull
    public MapEnvironment addParticleEffect(@NotNull WeatherEffectType type) {
        this.weatherEffects.add(type);
        return this;
    }

    @NotNull
    public MapEnvironment removeParticleEffect(@NotNull WeatherEffectType type) {
        this.weatherEffects.remove((Object)type);
        return this;
    }

    public boolean hasParticleEffect(@NotNull WeatherEffectType type) {
        return this.weatherEffects.contains((Object)type);
    }

    @NotNull
    public EnumSet<WeatherEffectType> getParticleEffects() {
        return this.weatherEffects;
    }

    public void clearParticleEffects() {
        this.weatherEffects.clear();
    }

    @NotNull
    public MapEnvironment setTime(int time) {
        this.time = time;
        return this;
    }

    public int getTime() {
        return this.time;
    }

    @NotNull
    public MapEnvironment setCustomSkyColor(int color) {
        this.hasCustomSkyColor = true;
        this.customSkyColor = color;
        return this;
    }

    public void clearCustomSkyColor() {
        this.hasCustomSkyColor = false;
        this.customSkyColor = 0xFFFFFF;
    }

    public boolean hasCustomSkyColor() {
        return this.hasCustomSkyColor;
    }

    public int getCustomSkyColor() {
        return this.customSkyColor;
    }

    @NotNull
    public MapEnvironment setCustomWaterColor(int color) {
        this.hasCustomWaterColor = true;
        this.customWaterColor = color;
        return this;
    }

    public void clearCustomWaterColor() {
        this.hasCustomWaterColor = false;
        this.customWaterColor = 0xFFFFFF;
    }

    public boolean hasCustomWaterColor() {
        return this.hasCustomWaterColor;
    }

    public int getCustomWaterColor() {
        return this.customWaterColor;
    }

    @NotNull
    public MapEnvironment setCustomFogColor(int color) {
        this.hasCustomFogColor = true;
        this.customFogColor = color;
        return this;
    }

    public void clearCustomFogColor() {
        this.hasCustomFogColor = false;
        this.customFogColor = 0xFFFFFF;
    }

    public boolean hasCustomFogColor() {
        return this.hasCustomFogColor;
    }

    public int getCustomFogColor() {
        return this.customFogColor;
    }

    @NotNull
    public MapEnvironment setCustomLightColor(int color) {
        this.hasCustomLightColor = true;
        this.customLightColor = color;
        return this;
    }

    public void clearCustomLightColor() {
        this.hasCustomLightColor = false;
        this.customLightColor = 0xFFFFFF;
    }

    public boolean hasCustomLightColor() {
        return this.hasCustomLightColor;
    }

    public int getCustomLightColor() {
        return this.customLightColor;
    }

    @NotNull
    public MapEnvironment setDisableClouds(boolean disableClouds) {
        this.disableClouds = disableClouds;
        return this;
    }

    public boolean getDisableClouds() {
        return this.disableClouds;
    }

    @NotNull
    public MapEnvironment setExteriorSound(@NotNull Supplier<SoundEvent> exteriorSound) {
        this.exteriorSound = exteriorSound;
        return this;
    }

    @NotNull
    public Supplier<SoundEvent> getExteriorSound() {
        return this.exteriorSound;
    }

    @NotNull
    public MapEnvironment setInteriorSound(@NotNull Supplier<SoundEvent> interiorSound) {
        this.interiorSound = interiorSound;
        return this;
    }

    @NotNull
    public Supplier<SoundEvent> getInteriorSound() {
        return this.interiorSound;
    }

    @NotNull
    public MapEnvironment setCustomFogDensity(float near, float far) {
        this.hasCustomFogDensity = true;
        this.nearFogDensity = near;
        this.farFogDensity = far;
        return this;
    }

    public boolean hasCustomFogDensity() {
        return this.hasCustomFogDensity;
    }

    @NotNull
    public MapEnvironment clearCustomFogDensity() {
        this.hasCustomFogDensity = false;
        this.nearFogDensity = 0.0f;
        this.farFogDensity = 0.0f;
        return this;
    }

    public float getNearFogDensity() {
        return this.nearFogDensity;
    }

    public float getFarFogDensity() {
        return this.farFogDensity;
    }

    @NotNull
    public MapEnvironment setDisableSky(boolean disableSky) {
        this.disableSky = disableSky;
        return this;
    }

    public boolean getDisableSky() {
        return this.disableSky;
    }

    @NotNull
    public MapEnvironment setShader(@Nullable ResourceLocation shader) {
        this.shader = shader;
        return this;
    }

    @Nullable
    public ResourceLocation getShader() {
        return this.shader;
    }

    public void clearShader() {
        this.shader = null;
    }

    public void write(@NotNull ByteBuf buf) throws IOException {
        IPacket.writeString((ByteBuf)buf, (String)this.name);
        buf.writeInt(this.time);
        IPacket.writeString((ByteBuf)buf, (String)RegistryUtils.getSoundEventId(this.exteriorSound.get()));
        IPacket.writeString((ByteBuf)buf, (String)RegistryUtils.getSoundEventId(this.interiorSound.get()));
        buf.writeBoolean(this.hasCustomFogDensity);
        buf.writeFloat(this.nearFogDensity);
        buf.writeFloat(this.farFogDensity);
        buf.writeBoolean(this.hasCustomSkyColor);
        buf.writeInt(this.customSkyColor);
        buf.writeBoolean(this.hasCustomWaterColor);
        buf.writeInt(this.customWaterColor);
        buf.writeBoolean(this.hasCustomFogColor);
        buf.writeInt(this.customFogColor);
        buf.writeBoolean(this.hasCustomLightColor);
        buf.writeInt(this.customLightColor);
        buf.writeBoolean(this.disableClouds);
        buf.writeBoolean(this.disableSky);
        buf.writeBoolean(this.shader != null);
        if (this.shader != null) {
            IPacket.writeString((ByteBuf)buf, (String)this.shader.toString());
        }
        buf.writeInt(this.weatherEffects.size());
        for (WeatherEffectType weatherEffectType : this.weatherEffects) {
            IPacket.writeEnum((ByteBuf)buf, (Enum)weatherEffectType);
        }
        int n = this.mapEffects.size();
        buf.writeInt(n);
        for (int i = 0; i < n; ++i) {
            AbstractMapEffect abstractMapEffect = this.mapEffects.get(i);
            buf.writeByte((int)MapEffectRegistry.getType(abstractMapEffect.getClass()));
            FDSTagCompound fDSTagCompound = new FDSTagCompound("mapEffect" + i);
            abstractMapEffect.writeToFDS(fDSTagCompound);
            fDSTagCompound.writeData(buf);
        }
    }

    public void writeFDS(@NotNull FDSTagCompound root) {
        root.setString("name", this.name);
        root.setInteger("time", this.time);
        root.setString("exteriorSound", RegistryUtils.getSoundEventId(this.exteriorSound.get()));
        root.setString("interiorSound", RegistryUtils.getSoundEventId(this.interiorSound.get()));
        root.setBoolean("hasCustomFogDensity", this.hasCustomFogDensity);
        root.setFloat("nearFogDensity", this.nearFogDensity);
        root.setFloat("farFogDensity", this.farFogDensity);
        root.setBoolean("hasCustomSkyColor", this.hasCustomSkyColor);
        root.setInteger("customSkyColor", this.customSkyColor);
        root.setBoolean("hasCustomWaterColor", this.hasCustomWaterColor);
        root.setInteger("customWaterColor", this.customWaterColor);
        root.setBoolean("hasCustomFogColor", this.hasCustomFogColor);
        root.setInteger("customFogColor", this.customFogColor);
        root.setBoolean("hasCustomLightColor", this.hasCustomLightColor);
        root.setInteger("customLightColor", this.customLightColor);
        root.setBoolean("disableClouds", this.disableClouds);
        root.setBoolean("disableSky", this.disableSky);
        if (this.shader != null) {
            root.setString("shader", this.shader.toString());
        }
        root.setInteger("weatherEffectsSize", this.weatherEffects.size());
        int n = 0;
        for (WeatherEffectType weatherEffectType : this.weatherEffects) {
            root.setInteger("weatherEffect" + n, weatherEffectType.ordinal());
            ++n;
        }
        int n2 = this.mapEffects.size();
        root.setInteger("mapEffectSize", n2);
        for (int i = 0; i < n2; ++i) {
            AbstractMapEffect abstractMapEffect = this.mapEffects.get(i);
            root.setByte("mapEffectType" + i, MapEffectRegistry.getType(abstractMapEffect.getClass()));
            FDSTagCompound fDSTagCompound = new FDSTagCompound("mapEffect" + i);
            abstractMapEffect.writeToFDS(fDSTagCompound);
            root.setTagCompound("mapEffect" + i, fDSTagCompound);
        }
    }

    public void reset() {
        for (AbstractMapEffect abstractMapEffect : this.mapEffects) {
            abstractMapEffect.reset();
        }
    }
}

