package red.vuis.mercfrontcore.registry;

import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;

import red.vuis.mercfrontcore.AddonConstants;

@SuppressWarnings("unused")
public final class AddonSounds {
	public static final DeferredRegister<SoundEvent> DR = DeferredRegister.create(Registries.SOUND_EVENT, AddonConstants.MOD_ID);
	
	/**
	 * For testing purposes
	 */
	@ApiStatus.Internal
	public static final DeferredHolder<SoundEvent, SoundEvent> AMBIENT_LSP_RADIO_BIENVENIDA = register("ambient.lsp.radio.bienvenida");
	
	private AddonSounds() {
	}
	
	private static DeferredHolder<SoundEvent, SoundEvent> register(String id) {
		return DR.register(id, () -> SoundEvent.of(AddonConstants.id(id)));
	}
	
	public static void init(IEventBus eventBus) {
		DR.register(eventBus);
	}
}
