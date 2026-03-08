/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.client.setup;

import com.boehmod.blockfront.client.render.nametag.BFAbstractNameTagRenderer;
import com.boehmod.blockfront.client.render.nametag.BFBotNameTagRenderer;
import com.boehmod.blockfront.client.render.nametag.BFPlayerNameTagRenderer;
import com.boehmod.blockfront.client.render.nametag.BFTankNameTagRenderer;
import com.boehmod.blockfront.registry.BFEntityTypes;
import net.minecraft.world.entity.EntityType;

public class BFNameTagRendererSetup {
    public static void register() {
        BFAbstractNameTagRenderer.register(EntityType.PLAYER, new BFPlayerNameTagRenderer());
        BFAbstractNameTagRenderer.register((EntityType)BFEntityTypes.BOT.get(), new BFBotNameTagRenderer());
        BFAbstractNameTagRenderer.register((EntityType)BFEntityTypes.WILLYS_JEEP_CAR.get(), new BFTankNameTagRenderer());
        BFAbstractNameTagRenderer.register((EntityType)BFEntityTypes.KUBELWAGEN_CAR.get(), new BFTankNameTagRenderer());
        BFAbstractNameTagRenderer.register((EntityType)BFEntityTypes.CHIHA_TANK.get(), new BFTankNameTagRenderer());
        BFAbstractNameTagRenderer.register((EntityType)BFEntityTypes.LANCIA_1ZM_CAR.get(), new BFTankNameTagRenderer());
        BFAbstractNameTagRenderer.register((EntityType)BFEntityTypes.FLAK88_GUN.get(), new BFTankNameTagRenderer());
        BFAbstractNameTagRenderer.register((EntityType)BFEntityTypes.TYPE96_GUN.get(), new BFTankNameTagRenderer());
        BFAbstractNameTagRenderer.register((EntityType)BFEntityTypes.PANZERIV_TANK.get(), new BFTankNameTagRenderer());
        BFAbstractNameTagRenderer.register((EntityType)BFEntityTypes.T34_TANK.get(), new BFTankNameTagRenderer());
        BFAbstractNameTagRenderer.register((EntityType)BFEntityTypes._7TP_PELICAN_TANK.get(), new BFTankNameTagRenderer());
    }
}

