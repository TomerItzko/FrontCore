/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.assets.impl;

import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.assets.AssetCommandBuilder;
import com.boehmod.blockfront.assets.AssetCommandValidators;
import com.boehmod.blockfront.assets.IAsset;
import com.boehmod.blockfront.client.sound.BFMusicSet;
import com.boehmod.blockfront.client.sound.BFMusicSets;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.match.BFCountry;
import com.boehmod.blockfront.common.match.DivisionData;
import com.boehmod.blockfront.map.MapEnvironment;
import com.boehmod.blockfront.util.BFStyles;
import com.boehmod.blockfront.util.CommandUtils;
import com.mojang.brigadier.context.CommandContext;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

public final class MapAsset
implements IAsset {
    private static final String DEFAULT_NAME = "Custom";
    private static final String DEFAULT_AUTHOR = "Unknown";
    private static final String DEFAULT_MUSIC_SET = "europe";
    @NotNull
    public static final Map<String, MapAsset> ENTRIES = new Object2ObjectOpenHashMap();
    @NotNull
    public static final MapAsset DEFAULT = new MapAsset("Custom", "Unknown", DivisionData.US_AIRBORNE, DivisionData.GER_WEHRMACHT).method_3858(false);
    @NotNull
    public Map<String, MapEnvironment> environments = new Object2ObjectOpenHashMap();
    public boolean field_3718 = true;
    @NotNull
    private String name;
    @NotNull
    private String author;
    @NotNull
    private String musicSet = "europe";
    @NotNull
    private DivisionData alliesDivision = DivisionData.US_AIRBORNE;
    @NotNull
    private DivisionData axisDivision = DivisionData.GER_WEHRMACHT;
    @NotNull
    private final AssetCommandBuilder command = new AssetCommandBuilder().subCommand("musicSet", new AssetCommandBuilder((commandContext, stringArray) -> {
        String string = stringArray[0];
        this.setMusicSet(string);
        MutableComponent mutableComponent = Component.literal((String)this.name).withStyle(BFStyles.LIME);
        MutableComponent mutableComponent2 = Component.literal((String)string).withStyle(BFStyles.LIME);
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.translatable((String)"bf.message.command.assets.edit.maps.musicSet.success", (Object[])new Object[]{mutableComponent, mutableComponent2}));
    }).validator(AssetCommandValidators.onlyAllowed(BFMusicSets.getIds()))).subCommand("teams", new AssetCommandBuilder().subCommand("set", new AssetCommandBuilder((commandContext, stringArray) -> {
        String string = stringArray[0];
        String string2 = stringArray[1];
        String string3 = stringArray[2];
        MutableComponent mutableComponent = Component.literal((String)string2).withStyle(BFStyles.LIME);
        MutableComponent mutableComponent2 = Component.literal((String)string3).withStyle(BFStyles.LIME);
        BFCountry bFCountry = BFCountry.fromTag(string2);
        if (bFCountry == null) {
            MutableComponent mutableComponent3 = Component.translatable((String)"bf.message.command.assets.edit.maps.team.set.error.nation", (Object[])new Object[]{mutableComponent});
            CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)mutableComponent3);
            return;
        }
        DivisionData divisionData = DivisionData.getByCountryAndSkin(bFCountry, string3);
        if (divisionData == null) {
            MutableComponent mutableComponent4 = Component.translatable((String)"bf.message.command.assets.edit.maps.team.set.error.team", (Object[])new Object[]{mutableComponent2, mutableComponent});
            CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)mutableComponent4);
            return;
        }
        if (string.equalsIgnoreCase("Axis")) {
            this.axisDivision = divisionData;
        } else {
            this.alliesDivision = divisionData;
        }
        MutableComponent mutableComponent5 = Component.literal((String)string).withStyle(BFStyles.LIME);
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.translatable((String)"bf.message.command.assets.edit.maps.team.set.success", (Object[])new Object[]{mutableComponent5, mutableComponent2, mutableComponent}));
    }).validator(AssetCommandValidators.count(new String[]{"team", "nation", "skin"})))).subCommand("author", new AssetCommandBuilder((commandContext, stringArray) -> {
        String string;
        this.author = string = String.join((CharSequence)" ", stringArray);
        MutableComponent mutableComponent = Component.literal((String)this.name).withStyle(BFStyles.LIME);
        MutableComponent mutableComponent2 = Component.literal((String)string).withStyle(BFStyles.LIME);
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.translatable((String)"bf.message.command.assets.edit.maps.author.success", (Object[])new Object[]{mutableComponent, mutableComponent2}));
    }).validator(AssetCommandValidators.count(new String[]{"author"}))).subCommand("environment", new AssetCommandBuilder().subCommand("edit", new AssetCommandBuilder((commandContext, stringArray) -> {
        String string = stringArray[0];
        MapEnvironment mapEnvironment = this.environments.get(string);
        if (mapEnvironment == null) {
            MutableComponent mutableComponent = Component.literal((String)this.name).withStyle(BFStyles.LIME);
            MutableComponent mutableComponent2 = Component.literal((String)string).withStyle(BFStyles.LIME);
            CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.translatable((String)"bf.message.command.assets.edit.maps.environment.error.notFound", (Object[])new Object[]{mutableComponent2, mutableComponent}));
            return;
        }
        String[] stringArray2 = Arrays.copyOfRange(stringArray, 1, ((String[])stringArray).length);
        mapEnvironment.getCommand().run((CommandContext<CommandSourceStack>)commandContext, stringArray2);
    }).validator(AssetCommandValidators.count(new String[]{"environment"}))).subCommand("list", new AssetCommandBuilder((commandContext, stringArray) -> {
        MutableComponent mutableComponent = Component.literal((String)this.name).withStyle(BFStyles.LIME);
        if (this.environments.isEmpty()) {
            CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.translatable((String)"bf.message.command.assets.edit.maps.environment.error.empty", (Object[])new Object[]{mutableComponent}));
            return;
        }
        ObjectArrayList objectArrayList = new ObjectArrayList(this.environments.values());
        List<MutableComponent> list = objectArrayList.stream().map(mapEnvironment -> Component.literal((String)mapEnvironment.getName()).withStyle(BFStyles.LIME)).toList();
        String string = String.join((CharSequence)", ", list.stream().map(Component::getString).toList());
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.translatable((String)"bf.message.command.assets.edit.maps.environment.list", (Object[])new Object[]{mutableComponent, string}));
    })).subCommand("add", new AssetCommandBuilder((commandContext, stringArray) -> {
        String string = stringArray[0];
        MutableComponent mutableComponent = Component.literal((String)this.name).withStyle(BFStyles.LIME);
        MutableComponent mutableComponent2 = Component.literal((String)string).withStyle(BFStyles.LIME);
        if (this.environments.containsKey(string)) {
            CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.translatable((String)"bf.message.command.assets.edit.maps.environment.error.exists", (Object[])new Object[]{mutableComponent2, mutableComponent}));
            return;
        }
        MapEnvironment mapEnvironment = new MapEnvironment(string);
        this.environments.put(string, mapEnvironment);
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.translatable((String)"bf.message.command.assets.edit.maps.environment.add.success", (Object[])new Object[]{mutableComponent2, mutableComponent}));
    }).validator(AssetCommandValidators.count(new String[]{"environment"}))).subCommand("remove", new AssetCommandBuilder((commandContext, stringArray) -> {
        String string = stringArray[0];
        MutableComponent mutableComponent = Component.literal((String)this.name).withStyle(BFStyles.LIME);
        MutableComponent mutableComponent2 = Component.literal((String)string).withStyle(BFStyles.LIME);
        if (this.environments.size() <= 1) {
            CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.translatable((String)"bf.message.command.assets.edit.maps.environment.error.remove.last", (Object[])new Object[]{mutableComponent2, mutableComponent}));
            return;
        }
        if (!this.environments.containsKey(string)) {
            CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.translatable((String)"bf.message.command.assets.edit.maps.environment.error.not.found", (Object[])new Object[]{mutableComponent2, mutableComponent}));
            return;
        }
        this.environments.remove(string);
        CommandUtils.sendBfa(((CommandSourceStack)commandContext.getSource()).source, (Component)Component.translatable((String)"bf.message.command.assets.edit.maps.environment.remove.success", (Object[])new Object[]{mutableComponent2, mutableComponent}));
    }).validator(AssetCommandValidators.count(new String[]{"environment"}))));

    private void setMusicSet(@NotNull String musicSet) {
        this.musicSet = musicSet;
    }

    public MapAsset(@NotNull BFAbstractManager<?, ?, ?> bFAbstractManager) {
        this(DEFAULT_NAME, DEFAULT_AUTHOR);
    }

    public MapAsset(@NotNull String string, @NotNull String string2) {
        this(string, string2, DivisionData.US_INFANTRY, DivisionData.GER_WEHRMACHT);
    }

    public MapAsset(@NotNull String name, @NotNull String author, @NotNull DivisionData alliesDivision, @NotNull DivisionData axisDivision) {
        ENTRIES.put(name, this);
        this.name = name;
        this.author = author;
        this.alliesDivision = alliesDivision;
        this.axisDivision = axisDivision;
    }

    @NotNull
    public DivisionData getAlliesDivision() {
        return this.alliesDivision;
    }

    @NotNull
    public DivisionData getAxisDivision() {
        return this.axisDivision;
    }

    @NotNull
    public MapAsset method_3858(boolean bl) {
        this.field_3718 = bl;
        return this;
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    @NotNull
    public String getAuthor() {
        return this.author;
    }

    @Override
    public void writeFDS(@NotNull FDSTagCompound root) {
        root.setString("name", this.name);
        root.setString("author", this.author);
        root.setInteger("alliesTeam", (int)this.alliesDivision.id);
        root.setInteger("axisTeam", (int)this.axisDivision.id);
        root.setString("musicSet", this.musicSet);
        ObjectArrayList objectArrayList = new ObjectArrayList(this.environments.values());
        int n = objectArrayList.size();
        root.setInteger("environmentSize", n);
        for (int i = 0; i < n; ++i) {
            MapEnvironment mapEnvironment = (MapEnvironment)objectArrayList.get(i);
            FDSTagCompound fDSTagCompound = new FDSTagCompound("environment" + i);
            mapEnvironment.writeFDS(fDSTagCompound);
            root.setTagCompound("environment" + i, fDSTagCompound);
        }
    }

    @Override
    @NotNull
    public AssetCommandBuilder getCommand() {
        return this.command;
    }

    @Override
    public void sendErrorMessages(@NotNull CommandSource source) {
    }

    @Override
    public void readFDS(@NotNull FDSTagCompound root) {
        this.name = root.getString("name", DEFAULT_NAME);
        this.author = root.getString("author", DEFAULT_AUTHOR);
        this.alliesDivision = DivisionData.fromId(root.getInteger("alliesTeam"));
        this.axisDivision = DivisionData.fromId(root.getInteger("axisTeam"));
        this.musicSet = root.getString("musicSet", DEFAULT_MUSIC_SET);
        this.environments.clear();
        int n = root.getInteger("environmentSize", 0);
        for (int i = 0; i < n; ++i) {
            FDSTagCompound fDSTagCompound = root.getTagCompound("environment" + i);
            if (fDSTagCompound == null) continue;
            MapEnvironment mapEnvironment = new MapEnvironment(fDSTagCompound);
            this.environments.put(mapEnvironment.getName(), mapEnvironment);
        }
        if (this.environments.isEmpty()) {
            this.environments.put("default", new MapEnvironment("default"));
        }
    }

    public boolean equals(Object object) {
        if (object instanceof MapAsset) {
            MapAsset mapAsset = (MapAsset)object;
            return mapAsset.name.equals(this.name);
        }
        return false;
    }

    public Map<String, MapEnvironment> getEnvironments() {
        return this.environments;
    }

    @Nullable
    public BFMusicSet getMusicSet() {
        return BFMusicSets.ENTRIES.get(this.musicSet);
    }

    public void writeBuf(@NotNull ByteBuf buf) {
        IPacket.writeString((ByteBuf)buf, (String)this.name);
        IPacket.writeString((ByteBuf)buf, (String)this.author);
        buf.writeInt((int)this.alliesDivision.id);
        buf.writeInt((int)this.axisDivision.id);
        IPacket.writeString((ByteBuf)buf, (String)this.musicSet);
    }

    public void readBuf(@NotNull ByteBuf buf) {
        this.name = IPacket.readString((ByteBuf)buf);
        this.author = IPacket.readString((ByteBuf)buf);
        this.alliesDivision = DivisionData.fromId(buf.readInt());
        this.axisDivision = DivisionData.fromId(buf.readInt());
        this.musicSet = IPacket.readString((ByteBuf)buf);
    }

    public void reset() {
        for (MapEnvironment mapEnvironment : this.environments.values()) {
            mapEnvironment.reset();
        }
    }

    static {
        MapAsset.DEFAULT.environments.put("default", new MapEnvironment("default"));
    }
}

