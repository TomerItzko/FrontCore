/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.game;

import com.boehmod.blockfront.client.sound.BFMusicManager;
import com.boehmod.blockfront.client.sound.BFMusicType;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.util.ArrayDeque;
import java.util.Collection;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GameMusic {
    @NotNull
    private final ObjectList<BF_440> field_2023 = new ObjectArrayList();

    private GameMusic() {
    }

    @NotNull
    public static GameMusic create() {
        return new GameMusic();
    }

    public GameMusic method_1538(@NotNull BFMusicType bFMusicType) {
        this.field_2023.add((Object)new BF_440(bFMusicType, false, false, 0));
        return this;
    }

    public GameMusic method_1540(@NotNull BFMusicType bFMusicType) {
        this.field_2023.add((Object)new BF_440(bFMusicType, false, true, 0));
        return this;
    }

    public GameMusic method_1541(@NotNull BFMusicType bFMusicType) {
        this.field_2023.add((Object)new BF_440(bFMusicType, true, false, 0));
        return this;
    }

    public GameMusic method_1543(@NotNull BFMusicType bFMusicType) {
        this.field_2023.add((Object)new BF_440(bFMusicType, true, true, 0));
        return this;
    }

    public GameMusic method_1536(int n) {
        this.field_2023.add((Object)new BF_440(null, false, false, n * 20));
        return this;
    }

    public void method_1537(@NotNull BFMusicManager bFMusicManager, @NotNull Minecraft minecraft) {
        bFMusicManager.method_1530(new ArrayDeque<BF_440>((Collection<BF_440>)this.field_2023));
        bFMusicManager.method_1534(minecraft);
    }

    public void method_1542(@NotNull FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeInt(this.field_2023.size());
        for (BF_440 bF_440 : this.field_2023) {
            friendlyByteBuf.writeBoolean(bF_440.field_2024 != null);
            if (bF_440.field_2024 != null) {
                friendlyByteBuf.writeInt(bF_440.field_2024.ordinal());
            }
            friendlyByteBuf.writeBoolean(bF_440.field_2025);
            friendlyByteBuf.writeBoolean(bF_440.field_2026);
            friendlyByteBuf.writeInt(bF_440.field_2027);
        }
    }

    @NotNull
    public static GameMusic method_1539(@NotNull FriendlyByteBuf friendlyByteBuf) {
        GameMusic gameMusic = new GameMusic();
        int n = friendlyByteBuf.readInt();
        for (int i = 0; i < n; ++i) {
            boolean bl = friendlyByteBuf.readBoolean();
            BFMusicType bFMusicType = bl ? BFMusicType.values()[friendlyByteBuf.readInt()] : null;
            boolean bl2 = friendlyByteBuf.readBoolean();
            boolean bl3 = friendlyByteBuf.readBoolean();
            int n2 = friendlyByteBuf.readInt();
            gameMusic.field_2023.add((Object)new BF_440(bFMusicType, bl2, bl3, n2));
        }
        return gameMusic;
    }

    public static class BF_440 {
        @Nullable
        public final BFMusicType field_2024;
        public final boolean field_2025;
        public final boolean field_2026;
        public final int field_2027;

        BF_440(@Nullable BFMusicType bFMusicType, boolean bl, boolean bl2, int n) {
            this.field_2024 = bFMusicType;
            this.field_2025 = bl;
            this.field_2026 = bl2;
            this.field_2027 = n;
        }
    }
}

