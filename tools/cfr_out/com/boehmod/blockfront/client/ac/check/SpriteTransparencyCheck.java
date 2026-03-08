/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  it.unimi.dsi.fastutil.objects.ObjectList
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.texture.AbstractTexture
 *  net.minecraft.client.renderer.texture.SpriteContents
 *  net.minecraft.client.renderer.texture.TextureAtlas
 *  net.minecraft.client.renderer.texture.TextureAtlasSprite
 *  net.minecraft.client.renderer.texture.TextureManager
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.inventory.InventoryMenu
 *  org.jetbrains.annotations.NotNull
 */
package com.boehmod.blockfront.client.ac.check;

import com.boehmod.blockfront.client.ac.BFClientAntiCheat;
import com.boehmod.blockfront.client.ac.check.IAntiCheatCheck;
import com.boehmod.blockfront.util.BFLog;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import org.jetbrains.annotations.NotNull;

public class SpriteTransparencyCheck
implements IAntiCheatCheck<BFClientAntiCheat> {
    private final ObjectList<ResourceLocation> checkedTextures = new ObjectArrayList();

    public SpriteTransparencyCheck() {
        this.checkedTextures.add((Object)ResourceLocation.withDefaultNamespace((String)"block/stone"));
        this.checkedTextures.add((Object)ResourceLocation.withDefaultNamespace((String)"block/smooth_stone"));
        this.checkedTextures.add((Object)ResourceLocation.withDefaultNamespace((String)"block/cobblestone"));
        this.checkedTextures.add((Object)ResourceLocation.withDefaultNamespace((String)"block/sandstone"));
        this.checkedTextures.add((Object)ResourceLocation.withDefaultNamespace((String)"block/smooth_sandstone"));
        this.checkedTextures.add((Object)ResourceLocation.withDefaultNamespace((String)"block/sand"));
        this.checkedTextures.add((Object)ResourceLocation.withDefaultNamespace((String)"block/dirt"));
        this.checkedTextures.add((Object)ResourceLocation.withDefaultNamespace((String)"block/grass_block"));
        this.checkedTextures.add((Object)ResourceLocation.withDefaultNamespace((String)"block/oak_planks"));
        this.checkedTextures.add((Object)ResourceLocation.withDefaultNamespace((String)"block/stone_bricks"));
        this.checkedTextures.add((Object)ResourceLocation.withDefaultNamespace((String)"block/smooth_stone"));
        this.checkedTextures.add((Object)ResourceLocation.withDefaultNamespace((String)"block/gravel"));
    }

    private static boolean textureHasTransparency(@NotNull TextureAtlasSprite atlas) {
        SpriteContents spriteContents = atlas.contents();
        int n = spriteContents.width();
        int n2 = spriteContents.height();
        try {
            for (int i = 0; i < n2; ++i) {
                for (int j = 0; j < n; ++j) {
                    int n3 = atlas.getPixelRGBA(0, j, i);
                    int n4 = n3 >> 24 & 0xFF;
                    if (n4 >= 255) continue;
                    return true;
                }
            }
        }
        catch (IllegalStateException illegalStateException) {
            // empty catch block
        }
        return false;
    }

    @Override
    public boolean run(@NotNull Minecraft minecraft, @NotNull BFClientAntiCheat bFClientAntiCheat) {
        TextureManager textureManager = minecraft.getTextureManager();
        AbstractTexture abstractTexture = textureManager.getTexture(InventoryMenu.BLOCK_ATLAS);
        if (!(abstractTexture instanceof TextureAtlas)) {
            return false;
        }
        TextureAtlas textureAtlas = (TextureAtlas)abstractTexture;
        for (ResourceLocation resourceLocation : this.checkedTextures) {
            try {
                SpriteContents spriteContents;
                ResourceLocation resourceLocation2;
                TextureAtlasSprite textureAtlasSprite = textureAtlas.getSprite(resourceLocation);
                if (textureAtlasSprite == null || (resourceLocation2 = (spriteContents = textureAtlasSprite.contents()).name()).getPath().equals("missingno") || !SpriteTransparencyCheck.textureHasTransparency(textureAtlasSprite)) continue;
                BFLog.log("[AC] Detected sprite transparency!", new Object[0]);
                return true;
            }
            catch (IllegalStateException illegalStateException) {
                BFLog.logError("[AC] Failed to check resource pack textures", new Object[0]);
            }
        }
        return false;
    }

    @Override
    @NotNull
    public BFClientAntiCheat.Stage getStage() {
        return BFClientAntiCheat.Stage.STARTUP;
    }
}

