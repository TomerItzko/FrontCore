/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.screen.intro;

import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.BFClientManager;
import com.boehmod.blockfront.client.gui.widget.BFButton;
import com.boehmod.blockfront.client.net.ConnectionMode;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.BFScreen;
import com.boehmod.blockfront.client.screen.intro.ContentWarningIntroScreen;
import com.boehmod.blockfront.client.screen.intro.DiscordIntroScreen;
import com.boehmod.blockfront.client.screen.intro.ModeIntroScreen;
import com.boehmod.blockfront.client.screen.intro.PatreonIntroScreen;
import com.boehmod.blockfront.client.screen.intro.RulesIntroScreen;
import com.boehmod.blockfront.client.screen.title.LobbyTitleScreen;
import com.boehmod.blockfront.client.settings.BFClientSettings;
import com.boehmod.blockfront.cloud.client.ClientConnectionManager;
import com.boehmod.blockfront.unnamed.BF_162;
import com.boehmod.blockfront.util.BFRes;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.NotNull;

public abstract class BFIntroScreen
extends BFScreen {
    protected static final int field_826 = 80;
    protected static final int field_827 = 20;
    protected static final int field_828 = 5;
    @NotNull
    private static final ResourceLocation field_823 = BFRes.loc("textures/gui/background/introduction.png");
    @NotNull
    private static final ResourceLocation field_824 = BFRes.loc("textures/gui/background/introduction_character.png");
    @NotNull
    private static final ResourceLocation field_825 = BFRes.loc("textures/gui/background/introduction_shadow.png");
    @NotNull
    private static final ResourceLocation field_822 = BFRes.loc("textures/gui/cornershadow.png");
    @NotNull
    private static final Component field_833 = Component.translatable((String)"bf.screen.introduction.disclaimer", (Object[])new Object[]{"BlockFront"});
    @NotNull
    private static final Component field_835 = Component.translatable((String)"bf.screen.menu.intro.mod", (Object[])new Object[]{"BlockFront"});
    private static final int field_829 = 1920;
    private static final int field_830 = 1080;
    public static final int field_831 = 854;
    public static final int field_832 = 480;
    public static final int field_834 = 1155850964;
    protected static final int field_836 = 4;

    public BFIntroScreen() {
        super(field_835);
    }

    public boolean shouldCloseOnEsc() {
        return false;
    }

    @OverridingMethodsMustInvokeSuper
    protected void init() {
        super.init();
        SoundEvent soundEvent = this.method_656();
        if (soundEvent != null) {
            this.minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((SoundEvent)soundEvent, (float)1.0f, (float)1.0f));
        }
        this.method_653(this.method_657());
    }

    @NotNull
    protected abstract List<BF_162> method_657();

    @Nullable
    protected abstract SoundEvent method_656();

    @Nullable
    protected abstract ResourceLocation method_658();

    public void render(@NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
        super.render(guiGraphics, n, n2, f);
        PoseStack poseStack = guiGraphics.pose();
        float f2 = BFRendering.getRenderTime();
        for (Renderable renderable : this.renderables) {
            if (!(renderable instanceof BFButton)) continue;
            BFButton bFButton = (BFButton)renderable;
            bFButton.method_379(this.minecraft, poseStack, this.font, guiGraphics, n, n2, f2);
        }
    }

    public void renderBackground(@Nonnull GuiGraphics guiGraphics, int n, int n2, float f) {
        PoseStack poseStack = guiGraphics.pose();
        int n3 = this.width / 2;
        int n4 = this.height / 2;
        float f2 = n - n3;
        float f3 = f2 / (float)n3;
        BFRendering.rectangle(guiGraphics, 0, 0, this.width, this.height, ColorReferences.COLOR_BLACK_SOLID);
        ResourceLocation resourceLocation = this.method_658();
        if (resourceLocation != null) {
            var11_11 = (float)this.width / 854.0f;
            float f4 = (float)this.height / 480.0f;
            float f5 = Math.max(var11_11, f4) * 1.1f;
            float f6 = 854.0f * f5;
            float f7 = 480.0f * f5;
            BFRendering.centeredTexture(poseStack, guiGraphics, resourceLocation, (float)n3, (float)n4, f6, f7, 0.0f, 0.25f);
            BFRendering.backgroundBlur(this.minecraft, f);
        } else {
            var11_11 = (float)this.width / 1920.0f;
            float f8 = (float)this.height / 1080.0f;
            float f9 = Math.max(var11_11, f8) * 1.1f;
            float f10 = 1920.0f * f9;
            float f11 = 1080.0f * f9;
            float f12 = 1.5f * f3;
            float f13 = 2.5f * f3;
            BFRendering.centeredTintedTexture(poseStack, guiGraphics, field_823, (float)n3 + f12, (float)n4, f10, f11, 0.0f, 0.4f, 15000276);
            BFRendering.centeredTintedTexture(poseStack, guiGraphics, field_824, (float)n3 + f13, (float)n4, f10, f11, 0.0f, 1.0f, 15000276);
            BFRendering.texture(poseStack, guiGraphics, field_825, 0, 0, this.width, this.height);
        }
        int n5 = 6;
        BFRendering.promptBackground(poseStack, guiGraphics, 6, 6, this.width - 12, this.height - 12);
        BFRendering.texture(poseStack, guiGraphics, field_822, 0, 0, this.width, this.height);
        n5 = this.height - 24;
        BFRendering.centeredComponent2d(poseStack, this.font, guiGraphics, field_833, (float)n3, (float)n5, 1155850964);
    }

    private void method_653(@NotNull List<BF_162> list) {
        int n = this.width / 2;
        int n2 = this.height / 2;
        int n3 = 85 * list.size() - 5;
        int n4 = 0;
        for (BF_162 bF_162 : list) {
            int n5;
            int n6;
            int n7;
            int n8 = n - n3 / 2 + n4 * 85;
            int n9 = n2 + 30;
            BFButton bFButton = new BFButton(n8, n9, 80, 20, bF_162.method_669(), bF_162.method_664()).textStyle(BFButton.TextStyle.SHADOW);
            Component component = bF_162.method_670();
            if (component != null) {
                bFButton.tip(component);
            }
            if ((n7 = bF_162.method_671()) != -1) {
                bFButton.method_395(n7);
            }
            if ((n6 = bF_162.method_672()) != -1) {
                bFButton.method_397(n6);
            }
            if ((n5 = bF_162.method_673()) != -1) {
                bFButton.method_390(n5);
            }
            bFButton.displayType(BFButton.DisplayType.SHADOW);
            Consumer<BFButton> consumer = bF_162.method_663();
            if (consumer != null) {
                consumer.accept(bFButton);
            }
            this.addRenderableWidget((GuiEventListener)bFButton);
            ++n4;
        }
    }

    @NotNull
    public static Screen method_655() {
        BFClientManager bFClientManager = BFClientManager.getInstance();
        assert (bFClientManager != null) : "Client mod manager is null!";
        ClientConnectionManager clientConnectionManager = (ClientConnectionManager)bFClientManager.getConnectionManager();
        ConnectionMode connectionMode = clientConnectionManager.getConnectionMode();
        switch (connectionMode) {
            case UNDECIDED: {
                return new ModeIntroScreen();
            }
            case ONLINE: {
                if (!BFClientSettings.INTRO_RULES.isEnabled()) {
                    return new RulesIntroScreen();
                }
                if (!BFClientSettings.INTRO_CONTENT.isEnabled()) {
                    return new ContentWarningIntroScreen();
                }
                if (!BFClientSettings.INTRO_DISCORD.isEnabled()) {
                    return new DiscordIntroScreen();
                }
                if (!BFClientSettings.INTRO_PATREON.isEnabled()) {
                    return new PatreonIntroScreen();
                }
                clientConnectionManager.getConnection().bumpReconnectTick();
                return new LobbyTitleScreen();
            }
        }
        return new LobbyTitleScreen();
    }

    protected void method_654(@NotNull GuiGraphics guiGraphics, int n, int n2) {
        int n3 = 32;
        boolean bl = true;
        int n4 = 5;
        int n5 = 37 * n2 - 5;
        int n6 = this.width / 2 - n5 / 2;
        int n7 = this.height - 32;
        for (int i = 0; i < n2; ++i) {
            int n8 = n6 + i * 37;
            int n9 = i < n ? -8617656 : -1;
            BFRendering.rectangle(guiGraphics, n8, n7, 32, 1, n9);
        }
    }
}

