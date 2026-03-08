/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.screen;

import com.boehmod.bflib.cloud.packet.IPacket;
import com.boehmod.bflib.cloud.packet.common.profile.PacketSkipBootcamp;
import com.boehmod.bflib.common.ColorReferences;
import com.boehmod.blockfront.client.gui.widget.BFButton;
import com.boehmod.blockfront.client.render.BFRendering;
import com.boehmod.blockfront.client.screen.SidebarScreen;
import com.boehmod.blockfront.cloud.client.ClientConnectionManager;
import com.boehmod.blockfront.game.BFGameType;
import com.boehmod.blockfront.unnamed.BF_214;
import com.boehmod.blockfront.util.BFLog;
import com.boehmod.blockfront.util.BFStyles;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BootcampSkipScreen
extends SidebarScreen {
    private static final Component field_6340 = Component.translatable((String)"bf.screen.overlay.skip.bootcamp");
    private static final Component field_6341 = Component.translatable((String)"bf.screen.overlay.skip.bootcamp.play");
    private static final Component field_6342 = Component.translatable((String)"bf.screen.overlay.skip.bootcamp.play.tip");
    private static final Component field_6343 = Component.translatable((String)"bf.screen.overlay.skip.bootcamp.skip");
    private static final Component field_6344 = Component.translatable((String)"bf.screen.overlay.skip.bootcamp.skip.tip");
    private static final Component field_6345 = Component.translatable((String)"bf.screen.overlay.skip.bootcamp.title").withStyle(BFStyles.BOLD);
    private static final Component field_6346 = Component.translatable((String)"bf.screen.overlay.skip.bootcamp.message");

    public BootcampSkipScreen(@Nullable Screen screen) {
        super(screen, field_6340);
    }

    @Override
    protected boolean method_753(double d, double d2) {
        return false;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int n, int n2, float f) {
        super.render(guiGraphics, n, n2, f);
        PoseStack poseStack = guiGraphics.pose();
        List list = this.font.split((FormattedText)field_6346, 300);
        int n3 = this.width / 2;
        int n4 = this.height / 2 - 50;
        poseStack.pushPose();
        poseStack.translate(0.0f, 0.0f, 400.0f);
        BFRendering.centeredComponent2d(poseStack, this.font, guiGraphics, field_6345, n3, n4, 2.0f);
        BFRendering.renderTextLines(poseStack, this.font, (MultiBufferSource)this.minecraft.renderBuffers().bufferSource(), (List<FormattedCharSequence>)list, n3, n4 + 30, 10, false);
        poseStack.popPose();
    }

    @Override
    public void method_758() {
        super.method_758();
        int n = 100;
        int n2 = 20;
        int n3 = 5;
        int n4 = this.height / 2 + 20;
        int n5 = this.width / 2 - 100 - 5;
        this.addRenderableWidget((GuiEventListener)new BFButton(n5, n4, 100, 20, field_6341, button -> {
            BFLog.log("Starting search for bootcamp match", new Object[0]);
            BF_214.method_970(this.minecraft, this.manager, BFGameType.BOOTCAMP);
            this.method_752();
        }).method_395(ColorReferences.COLOR_TEAM_ALLIES_SOLID).method_397(ColorReferences.COLOR_TEAM_ALLIES_HOVERED_SOLID).tip(field_6342).displayType(BFButton.DisplayType.SHADOW).method_390(0));
        int n6 = this.width / 2 + 5;
        this.addRenderableWidget((GuiEventListener)new BFButton(n6, n4, 100, 20, field_6343, button -> {
            BFLog.log("Sending request to skip bootcamp", new Object[0]);
            ((ClientConnectionManager)this.manager.getConnectionManager()).sendPacket((IPacket)new PacketSkipBootcamp());
            this.method_752();
        }).tip(field_6344).displayType(BFButton.DisplayType.SHADOW));
    }
}

