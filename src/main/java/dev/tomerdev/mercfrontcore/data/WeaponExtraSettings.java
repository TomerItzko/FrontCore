package dev.tomerdev.mercfrontcore.data;

import lombok.AllArgsConstructor;

import java.util.Optional;

import com.boehmod.blockfront.common.item.GunItem;
import com.boehmod.blockfront.registry.BFDataComponents;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import dev.tomerdev.mercfrontcore.setup.GunSkinIndex;

@AllArgsConstructor
public class WeaponExtraSettings {
	public static final PacketCodec<ByteBuf, WeaponExtraSettings> PACKET_CODEC = PacketCodec.tuple(
		PacketCodecs.BOOL,
		e -> e.scope,
		PacketCodecs.STRING,
		e -> e.magType,
		PacketCodecs.STRING,
		e -> e.barrelType,
		PacketCodecs.STRING.collect(PacketCodecs::optional),
		e -> Optional.ofNullable(e.skin),
		(scope, magType, barrelType, skin) -> new WeaponExtraSettings(scope, magType, barrelType, skin.orElse(null))
	);
	
	public boolean scope;
	public String magType;
	public String barrelType;
	public String skin;
	
	public WeaponExtraSettings() {
		scope = false;
		magType = "default";
		barrelType = "default";
		skin = null;
	}
	
	public void getComponents(@Nullable ItemStack itemStack) {
		if (itemStack == null) {
			return;
		}
		
		scope = GunItem.getScope(itemStack);
		magType = GunItem.getMagType(itemStack);
		barrelType = GunItem.getBarrelType(itemStack);
		skin = GunSkinIndex.getSkinName(itemStack).orElse(itemStack.get(BFDataComponents.PATTERN_NAME));
	}
	
	public ItemStack setComponents(@Nullable ItemStack itemStack) {
		if (itemStack != null && itemStack.getItem() instanceof GunItem) {
			GunSkinIndex.ensureInitialized();
			if (scope) {
				GunItem.setScope(itemStack, true);
			}
			if (!magType.equals("default")) {
				GunItem.setMagType(itemStack, magType);
			}
			if (!barrelType.equals("default")) {
				GunItem.setBarrelType(itemStack, barrelType);
			}
			Float skinId = GunSkinIndex.getSkinId(itemStack.getItem(), skin).orElse(null);
			if (skin == null || skin.isBlank()) {
				itemStack.remove(BFDataComponents.SKIN_ID);
				itemStack.remove(BFDataComponents.PATTERN_NAME);
				itemStack.set(BFDataComponents.HAS_PATTERN, false);
			} else {
				// Keep PATTERN_NAME as fallback for direct texture override when cloud skin-id is unavailable.
				if (skinId != null) {
					itemStack.set(BFDataComponents.SKIN_ID, skinId);
				} else {
					itemStack.remove(BFDataComponents.SKIN_ID);
				}
				itemStack.set(BFDataComponents.HAS_PATTERN, false);
				itemStack.set(BFDataComponents.PATTERN_NAME, skin);
			}
		}
		return itemStack;
	}
	
	public @NotNull ItemStack getItemStack(@NotNull GunItem item) {
		return setComponents(new ItemStack(item));
	}
}
