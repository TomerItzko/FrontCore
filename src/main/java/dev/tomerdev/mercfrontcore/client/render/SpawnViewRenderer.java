package dev.tomerdev.mercfrontcore.client.render;

import com.boehmod.blockfront.util.math.FDSPose;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import dev.tomerdev.mercfrontcore.client.data.AddonClientData;
import dev.tomerdev.mercfrontcore.client.input.InputTracker;

public final class SpawnViewRenderer extends RenderObject {
    private static final Identifier SPAWN_TEXTURE = Identifier.of("blockfront", "textures/gui/menu/icons/player.png");

    public SpawnViewRenderer(MinecraftClient client, InputTracker input) {
        super(client, input);
    }

    @Override
    public void render() {
        super.render();

        AddonClientData clientData = AddonClientData.getInstance();
        if (client.world == null || clientData.spawnView == null) {
            return;
        }

        cameraAsOrigin();
        for (int i = 0; i < clientData.spawnView.size(); i++) {
            FDSPose spawn = clientData.spawnView.get(i);
            billboardString("Index: " + i, spawn.position.add(0.0, 1.3, 0.0), 1f, false);
            billboardTexture(SPAWN_TEXTURE, spawn.position.add(0.0, 0.6, 0.0), 1.5f, 1.5f, 1f, false);
        }
    }
}
