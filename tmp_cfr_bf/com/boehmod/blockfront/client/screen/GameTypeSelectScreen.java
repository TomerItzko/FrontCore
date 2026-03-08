/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.screen;

import com.boehmod.bflib.cloud.common.mm.SearchRegion;
import com.boehmod.blockfront.client.gui.widget.BFButton;
import com.boehmod.blockfront.client.match.ClientMatchMaking;
import com.boehmod.blockfront.client.player.ClientPlayerDataHandler;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.BFOverlayScreen;
import com.boehmod.blockfront.cloud.client.ClientConnectionManager;
import com.boehmod.blockfront.common.player.PlayerCloudData;
import com.boehmod.blockfront.game.BFGameType;
import com.boehmod.blockfront.registry.BFSounds;
import com.boehmod.blockfront.unnamed.BF_207;
import com.boehmod.blockfront.unnamed.BF_208;
import com.boehmod.blockfront.unnamed.BF_214;
import com.boehmod.blockfront.util.BFRes;
import com.boehmod.blockfront.util.math.MathUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public final class GameTypeSelectScreen
extends BFOverlayScreen {
    private static final Component field_934 = Component.translatable((String)"bf.screen.menu.lobby.edit");
    private static final Component field_935 = Component.translatable((String)"bf.menu.button.nav.text.lobby");
    private static final Component field_936 = Component.translatable((String)"bf.message.server.all");
    private static final Component field_937 = Component.translatable((String)"bf.message.server.us");
    private static final Component field_938 = Component.translatable((String)"bf.message.server.eu");
    private static final ResourceLocation field_929 = BFRes.loc("textures/gui/flag_all.png");
    private static final ResourceLocation field_930 = BFRes.loc("textures/gui/flag_us.png");
    private static final ResourceLocation field_931 = BFRes.loc("textures/gui/flag_eu.png");
    @NotNull
    private BFGameType.Category field_927 = BFGameType.Category.ALL;
    private BF_208<BF_214> field_928;
    private float field_932 = 0.0f;
    private float field_933 = 0.0f;

    public GameTypeSelectScreen(@NotNull Screen parent) {
        super(parent, field_934, field_935);
    }

    @Override
    public void tick() {
        super.tick();
        this.field_933 = this.field_932;
        this.field_932 = Mth.lerp((float)0.4f, (float)this.field_932, (float)1.0f);
    }

    @Override
    public void method_774() {
        super.method_774();
        this.field_928 = new BF_207<BF_214>(0, 25, this.width, 185, this);
        this.method_764(this.field_928);
        this.method_707(this.field_927);
    }

    @Override
    public void method_758() {
        super.method_758();
        this.method_704();
        int n = 75;
        int n2 = 16;
        int n3 = this.width / 2 - 37;
        int n4 = this.height - 16 - 10;
        BFButton bFButton = new BFButton(n3, n4, 75, 16, (Component)Component.translatable((String)"bf.menu.button.cancel"), button -> this.minecraft.setScreen(this.parentScreen)).displayType(BFButton.DisplayType.SHADOW);
        this.addRenderableWidget((GuiEventListener)bFButton);
    }

    private void method_5929() {
        ClientMatchMaking clientMatchMaking = this.manager.getMatchMaking();
        SearchRegion searchRegion = clientMatchMaking.getSearchRegion();
        ClientPlayerDataHandler clientPlayerDataHandler = (ClientPlayerDataHandler)this.manager.getPlayerDataHandler();
        int n = this.width / 2;
        int n2 = this.height - 56;
        int n3 = 3;
        int n4 = 14;
        int n5 = 35;
        int n6 = 0;
        ObjectArrayList objectArrayList = new ObjectArrayList();
        objectArrayList.add((Object)new BFButton(n, n2, 35, 14, (Component)Component.empty(), button -> this.method_705(clientPlayerDataHandler, SearchRegion.ALL)).method_392(field_929).method_387(searchRegion != SearchRegion.ALL).tip(field_936));
        objectArrayList.add((Object)new BFButton(n, n2, 35, 14, (Component)Component.empty(), button -> this.method_705(clientPlayerDataHandler, SearchRegion.US)).method_392(field_930).method_387(searchRegion != SearchRegion.US).tip(field_937));
        objectArrayList.add((Object)new BFButton(n, n2, 35, 14, (Component)Component.empty(), button -> this.method_705(clientPlayerDataHandler, SearchRegion.EU)).method_392(field_931).method_387(searchRegion != SearchRegion.EU).tip(field_938));
        for (BFButton bFButton : objectArrayList) {
            n6 += bFButton.getWidth() + 3;
        }
        int n7 = n - n6 / 2 + 1;
        int n8 = 0;
        for (BFButton bFButton : objectArrayList) {
            bFButton.setX(n7 + n8);
            this.addRenderableWidget((GuiEventListener)bFButton);
            n8 += bFButton.getWidth() + 3;
        }
    }

    public void method_704() {
        int n = this.width / 2;
        int n2 = 2;
        int n3 = 48;
        int n4 = 20;
        int n5 = BFGameType.Category.values().length * 50 - 2;
        int n6 = n - n5 / 2;
        for (BFGameType.Category category : BFGameType.Category.values()) {
            BFButton bFButton = new BFButton(n6, 2, 48, 20, (Component)category.getTitle().copy(), button -> this.method_707(category)).tip(category.getTip()).alignment(BFButton.Alignment.CENTER).displayType(BFButton.DisplayType.NONE);
            this.addRenderableWidget((GuiEventListener)bFButton);
            n6 += 50;
        }
    }

    public void method_705(@NotNull ClientPlayerDataHandler clientPlayerDataHandler, @NotNull SearchRegion searchRegion) {
        this.manager.getMatchMaking().setSearchRegion(searchRegion);
        ((ClientConnectionManager)this.manager.getConnectionManager()).updateParty(clientPlayerDataHandler, this.minecraft, this.manager);
        this.minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)((SoundEvent)BFSounds.GUI_LOBBY_CANCEL.get()), (float)1.0f));
        this.minecraft.setScreen((Screen)new GameTypeSelectScreen(this.parentScreen));
    }

    private void method_707(@NotNull BFGameType.Category category) {
        this.field_927 = category;
        this.field_928.method_948();
        PlayerCloudData playerCloudData = this.playerDataHandler.getCloudData(this.minecraft);
        if (!playerCloudData.hasCompletedBootcamp()) {
            BF_214 bF_214 = new BF_214(BFGameType.BOOTCAMP);
            MutableComponent mutableComponent = BFGameType.BOOTCAMP.getDisplayName().copy().withColor(8159560);
            bF_214.method_986((Component)Component.translatable((String)"bf.container.gamemode.click", (Object[])new Object[]{mutableComponent}));
            this.field_928.method_950(bF_214);
            return;
        }
        for (BFGameType bFGameType : BFGameType.getByCategory(category)) {
            if (bFGameType.isHidden()) continue;
            BF_214 bF_214 = new BF_214(bFGameType);
            MutableComponent mutableComponent = bFGameType.getDisplayName().copy().withColor(8159560);
            MutableComponent mutableComponent2 = bFGameType.isExperimental() ? Component.literal((String)" (Experimental)").withStyle(ChatFormatting.GRAY) : Component.empty();
            bF_214.method_986((Component)Component.translatable((String)"bf.container.gamemode.click", (Object[])new Object[]{mutableComponent}).append((Component)mutableComponent2));
            this.field_928.method_950(bF_214);
        }
        this.field_1048 = category.getTitle();
    }

    @Override
    public void renderOverlay(@NotNull PoseStack poseStack, @NotNull GuiGraphics graphics, int n, int n2, float f) {
        float f2 = MathUtils.lerpf1(this.field_933, this.field_932, f);
        BFRendering.rectangle(graphics, 0, 0, this.width, this.height, BFRendering.translucentBlack(), 0.4f * f2);
        BFRendering.rectangleWithDarkShadow(graphics, 0, 0, this.width, 20, BFRendering.translucentBlack());
    }
}

