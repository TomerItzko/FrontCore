package red.vuis.mercfrontcore.client.render;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import com.boehmod.blockfront.map.effect.AbstractMapEffect;
import com.boehmod.blockfront.map.effect.LoopingSoundPointMapEffect;
import com.boehmod.blockfront.map.effect.PositionedMapEffect;
import com.boehmod.blockfront.util.BFRes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.neoforged.neoforge.network.PacketDistributor;

import red.vuis.mercfrontcore.client.data.AddonClientData;
import red.vuis.mercfrontcore.client.input.InputTracker;
import red.vuis.mercfrontcore.client.util.ClientSound;
import red.vuis.mercfrontcore.net.packet.MapEffectPositionPacket;

/**
 * Currently unused, to be implemented properly eventually :P
 */
public final class AssetEditRenderer extends RenderObject {
	private static final Identifier LOOPING_SOUND_POINT = BFRes.loc("textures/misc/debug/sound_looping.png");
	private static final float ICON_SIZE = 0.75f;
	
	private PositionedMapEffect movingGhost = null;
	private double movingGhostDist;
	
	public AssetEditRenderer(MinecraftClient client, InputTracker input) {
		super(client, input);
	}
	
	@Override
	public void render() {
		super.render();
		
		AddonClientData clientData = AddonClientData.getInstance();
		if (client.world == null || clientData.editingMapName == null || clientData.editingEnv == null) {
			return;
		}
		
		List<HighlightResult> highlights = new ArrayList<>();
		
		cameraAsOrigin();
		
		for (AbstractMapEffect absMapEffect : clientData.editingEnv.getMapEffects()) {
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
				int index = clientData.editingEnv.getMapEffects().indexOf(movingGhost);
				if (index >= 0) {
					((PositionedMapEffect) clientData.editingEnv.getMapEffects().get(index)).position = target;
				}
				if (!client.isInSingleplayer()) {
					PacketDistributor.sendToServer(new MapEffectPositionPacket(clientData.editingMapName, clientData.editingEnv.getName(), index, target));
				}
				
				movingGhost = null;
				ClientSound.play(SoundCategory.MASTER, SoundEvents.ENTITY_ITEM_PICKUP, 0.75f, 0.75f);
			} else {
				renderIcon(movingGhost, target, 0.5f);
			}
		}
	}
	
	private void renderIcon(AbstractMapEffect absMapEffect, float alpha) {
		if (absMapEffect instanceof PositionedMapEffect posMapEffect) {
			renderIcon(posMapEffect, posMapEffect.position, alpha);
		}
	}
	
	private void renderIcon(AbstractMapEffect absMapEffect, Vec3d position, float alpha) {
		switch (absMapEffect) {
			case LoopingSoundPointMapEffect ignored -> {
				billboardTexture(LOOPING_SOUND_POINT, position, ICON_SIZE, ICON_SIZE, alpha);
			}
			default -> {
			}
		}
	}
	
	private void renderInfo(AbstractMapEffect absMapEffect) {
		switch (absMapEffect) {
			case LoopingSoundPointMapEffect mapEffect -> {
				Optional.ofNullable(mapEffect.sound)
					.map(Supplier::get)
					.map(Registries.SOUND_EVENT::getKeyOrNull)
					.map(Identifier::toString)
					.ifPresent(id -> billboardString(id, mapEffect.position.add(0, 0.75, 0), 0.75f));
			}
			default -> {
			}
		}
	}
	
	private Optional<HighlightResult> getHighlight(PositionedMapEffect mapEffect) {
		Vec3d cameraPos = camera.getPos();
		Vec3d lookingPos = cameraPos.add(new Vec3d(camera.getHorizontalPlane()).multiply(4.5f));
		Box box = getHighlightBox(mapEffect.position);
		
		return box.raycast(cameraPos, lookingPos).isPresent() ?
			Optional.of(new HighlightResult(mapEffect, cameraPos.distanceTo(mapEffect.position), box)) :
			Optional.empty();
	}
	
	private static Box getHighlightBox(Vec3d position) {
		double s = ICON_SIZE / 2.0;
		return new Box(position.subtract(s, s, s), position.add(s, s, s));
	}
	
	private record HighlightResult(PositionedMapEffect mapEffect, double dist, Box box) {
	}
}
