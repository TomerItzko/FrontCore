/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.unnamed;

import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.match.AbstractMatchSelectClassScreen;
import com.boehmod.blockfront.common.match.Loadout;
import com.boehmod.blockfront.common.match.MatchClass;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.unnamed.BF_218;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public final class BF_197
extends BF_218 {
    @NotNull
    private final ObjectList<Loadout> field_1239;
    @NotNull
    private final MatchClass field_1241;
    private final int field_1246;
    @NotNull
    private final String[] field_1243;
    private final int field_1247;
    private final int field_1248;
    private int field_1249 = 0;
    private boolean field_1242 = false;
    private float field_1244;
    private float field_1245;
    @NotNull
    private final AbstractMatchSelectClassScreen<?, ?> field_1240;

    public BF_197(@NotNull AbstractMatchSelectClassScreen<?, ?> abstractMatchSelectClassScreen, @NotNull MatchClass matchClass, @NotNull ObjectList<Loadout> objectList, @NotNull String[] stringArray, int n, int n2, int n3) {
        this.field_1240 = abstractMatchSelectClassScreen;
        this.field_1239 = objectList;
        this.field_1241 = matchClass;
        this.field_1247 = n;
        this.field_1248 = n2;
        this.field_1243 = stringArray;
        this.field_1246 = n3;
        this.field_1244 = 1.0f;
        this.field_1245 = 1.0f;
    }

    @Override
    public void method_981(@NotNull PoseStack poseStack, @NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull GuiGraphics guiGraphics, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull Font font, int n, int n2, float f, float f2) {
        int n3;
        Loadout loadout;
        super.method_981(poseStack, minecraft, bFClientManager, guiGraphics, clientPlayerDataHandler, font, n, n2, f, f2);
        if (minecraft.player == null) {
            return;
        }
        f2 = minecraft.getTimer().getGameTimeDeltaPartialTick(true);
        this.field_1242 = this.method_992();
        float f3 = MathUtils.lerpf1(this.field_1244, this.field_1245, f2);
        PlayerCloudData playerCloudData = clientPlayerDataHandler.getCloudData(minecraft);
        int n4 = playerCloudData.getExpForClass(this.field_1241.ordinal());
        boolean bl = n4 >= (loadout = (Loadout)this.field_1239.get(this.field_1249)).getMinimumXp();
        int n5 = n3 = bl ? ColorReferences.COLOR_THEME_YELLOW_SOLID : ColorReferences.COLOR_BLACK_SOLID + BFRendering.RED_CHAT_COLOR;
        if (this.field_1242) {
            BFRendering.rectangle(guiGraphics, this.field_1357, this.field_1358 + this.field_1248 - 1, this.field_1247, 1, n3, 1.0f - f3);
        }
        MutableComponent mutableComponent = Component.translatable((String)("enchantment.level." + (this.field_1249 + 1))).withColor(n3);
        MutableComponent mutableComponent2 = Component.translatable((String)this.field_1241.getDisplayTitle()).append(" ").append((Component)mutableComponent);
        BFRendering.component2d(poseStack, font, guiGraphics, (Component)mutableComponent2, (float)(this.field_1357 + 5) + 15.0f * f3, this.field_1358 + 7);
        BFRendering.texture(poseStack, guiGraphics, this.field_1241.getIcon(), this.field_1357 + 2, this.field_1358 + 2, 16, 16, f3);
    }

    public Loadout method_905() {
        return (Loadout)this.field_1239.get(this.field_1249);
    }

    public List<Loadout> method_908() {
        return this.field_1239;
    }

    public int method_909() {
        return this.field_1246;
    }

    public String[] method_907() {
        return this.field_1243;
    }

    public MatchClass method_906() {
        return this.field_1241;
    }

    public int method_910() {
        return this.field_1249;
    }

    public void method_904() {
        ++this.field_1249;
        if (this.field_1249 >= this.field_1239.size()) {
            this.field_1249 = 0;
        }
    }

    @Override
    public void method_987(@NotNull Minecraft minecraft) {
        this.field_1245 = this.field_1244;
        this.field_1244 = Mth.lerp((float)0.8f, (float)this.field_1244, (float)(this.field_1242 ? 0.0f : 1.0f));
    }

    @Override
    public boolean method_990() {
        return true;
    }

    @Override
    public int height() {
        return this.field_1248;
    }

    @Override
    public int method_989() {
        return this.field_1247;
    }

    @Override
    public void method_982(@NotNull Minecraft minecraft, @NotNull BFClientManager bFClientManager, @NotNull ClientPlayerDataHandler clientPlayerDataHandler, int n, int n2, int n3) {
        minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.GUI_BUTTON_PRESS.get()), (float)1.0f));
        if (n3 == 0) {
            this.field_1240.method_896(this);
        } else {
            this.method_904();
        }
    }
}

