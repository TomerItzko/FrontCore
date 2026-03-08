package dev.tomerdev.mercfrontcore.client.render;

import com.boehmod.blockfront.assets.AssetStore;
import com.boehmod.blockfront.assets.impl.MapAsset;
import com.boehmod.blockfront.map.MapEnvironment;
import com.boehmod.blockfront.map.effect.AbstractMapEffect;
import com.boehmod.blockfront.map.effect.LoopingSoundPointMapEffect;
import com.boehmod.blockfront.map.effect.PositionedMapEffect;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.neoforged.neoforge.network.PacketDistributor;
import dev.tomerdev.mercfrontcore.client.data.AddonClientData;
import dev.tomerdev.mercfrontcore.client.input.InputTracker;
import dev.tomerdev.mercfrontcore.client.util.ClientSound;
import dev.tomerdev.mercfrontcore.net.packet.MapEffectPositionPacket;

public final class AssetEditRenderer extends RenderObject {
    private static final Identifier LOOPING_SOUND_POINT = Identifier.of("blockfront", "textures/misc/debug/sound_looping.png");
    private static final float ICON_SIZE = 0.75f;

    private PositionedMapEffect movingGhost;
    private double movingGhostDist;

    public AssetEditRenderer(MinecraftClient client, InputTracker input) {
        super(client, input);
    }

    @Override
    public void render() {
        super.render();

        AddonClientData clientData = AddonClientData.getInstance();
        MapEnvironment editingEnv = resolveEditingEnvironment(clientData);
        if (client.world == null || clientData.editingMapName == null || editingEnv == null) {
            return;
        }

        List<HighlightResult> highlights = new ArrayList<>();
        cameraAsOrigin();

        for (AbstractMapEffect absMapEffect : editingEnv.getMapEffects()) {
            renderIcon(absMapEffect, 1f);
            renderInfo(absMapEffect);
            if (absMapEffect instanceof PositionedMapEffect posMapEffect) {
                getHighlight(posMapEffect).ifPresent(highlights::add);
            }
        }

        if (!highlights.isEmpty()) {
            highlights.sort(Comparator.comparing(HighlightResult::dist));
            HighlightResult highlight = highlights.getFirst();
            if (input.leftClicked()) {
                movingGhost = highlight.mapEffect;
                movingGhostDist = highlight.dist;
                ClientSound.play(SoundCategory.MASTER, SoundEvents.ENTITY_ITEM_PICKUP, 0.75f, 1.5f);
            }
            boxOutline(highlight.box, 0xFFFFFFFF);
        }

        if (movingGhost != null) {
            Vec3d target = camera.getPos().add(new Vec3d(camera.getHorizontalPlane()).multiply(movingGhostDist));
            line(movingGhost.position, target, 0x7FFFFFFF);

            if (input.leftReleased()) {
                int index = editingEnv.getMapEffects().indexOf(movingGhost);
                if (index >= 0) {
                    ((PositionedMapEffect) editingEnv.getMapEffects().get(index)).position = target;
                }
                if (!client.isInSingleplayer()) {
                    PacketDistributor.sendToServer(new MapEffectPositionPacket(clientData.editingMapName, clientData.editingEnvName, index, target));
                }
                movingGhost = null;
                ClientSound.play(SoundCategory.MASTER, SoundEvents.ENTITY_ITEM_PICKUP, 0.75f, 0.75f);
            } else {
                renderIcon(movingGhost, target, 0.5f);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static MapEnvironment resolveEditingEnvironment(AddonClientData clientData) {
        if (clientData.editingMapName == null || clientData.editingEnvName == null) {
            return null;
        }
        try {
            MapAsset map = AssetStore.getInstance().getRegistry(MapAsset.class).getByName(clientData.editingMapName);
            if (map == null) {
                return null;
            }
            Object envObj = map.getClass().getMethod("getEnvironments").invoke(map);
            if (envObj instanceof Map<?, ?> envs) {
                Object selected = envs.get(clientData.editingEnvName);
                if (selected instanceof MapEnvironment mapEnvironment) {
                    return mapEnvironment;
                }
            }
        } catch (Throwable ignored) {
        }
        return null;
    }

    private void renderIcon(AbstractMapEffect absMapEffect, float alpha) {
        if (absMapEffect instanceof PositionedMapEffect posMapEffect) {
            renderIcon(posMapEffect, posMapEffect.position, alpha);
        }
    }

    private void renderIcon(AbstractMapEffect absMapEffect, Vec3d position, float alpha) {
        if (absMapEffect instanceof LoopingSoundPointMapEffect) {
            billboardTexture(LOOPING_SOUND_POINT, position, ICON_SIZE, ICON_SIZE, alpha);
        }
    }

    private void renderInfo(AbstractMapEffect absMapEffect) {
        if (absMapEffect instanceof LoopingSoundPointMapEffect mapEffect) {
            Optional.ofNullable(mapEffect.sound)
                .map(Supplier::get)
                .map(Registries.SOUND_EVENT::getId)
                .map(Identifier::toString)
                .ifPresent(id -> billboardString(id, mapEffect.position.add(0, 0.75, 0), 0.75f));
        }
    }

    private Optional<HighlightResult> getHighlight(PositionedMapEffect mapEffect) {
        Vec3d cameraPos = camera.getPos();
        Vec3d lookingPos = cameraPos.add(new Vec3d(camera.getHorizontalPlane()).multiply(4.5f));
        Box box = getHighlightBox(mapEffect.position);
        return box.raycast(cameraPos, lookingPos).isPresent()
            ? Optional.of(new HighlightResult(mapEffect, cameraPos.distanceTo(mapEffect.position), box))
            : Optional.empty();
    }

    private static Box getHighlightBox(Vec3d position) {
        double s = ICON_SIZE / 2.0;
        return new Box(position.subtract(s, s, s), position.add(s, s, s));
    }

    private record HighlightResult(PositionedMapEffect mapEffect, double dist, Box box) {
    }
}
