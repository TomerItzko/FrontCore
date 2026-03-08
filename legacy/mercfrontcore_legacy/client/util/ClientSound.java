package red.vuis.mercfrontcore.client.util;

import java.util.function.Supplier;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;

public final class ClientSound {
	private ClientSound() {
	}
	
	public static void play(SoundCategory category, SoundEvent sound, float volume, float pitch) {
		MinecraftClient.getInstance().getSoundManager().play(new PositionedSoundInstance(
			sound.getId(), category, volume, pitch, SoundInstance.createRandom(), false, 0, SoundInstance.AttenuationType.LINEAR, 0, 0, 0, true
		));
	}
	
	public static void play(SoundCategory category, Supplier<SoundEvent> sound, float volume, float pitch) {
		play(category, sound.get(), volume, pitch);
	}
}
