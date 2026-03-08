package red.vuis.mercfrontcore.registry;

import java.util.function.Supplier;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.boehmod.blockfront.common.block.TinyFloorBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import red.vuis.mercfrontcore.AddonConstants;

@SuppressWarnings("unused")
public final class AddonBlocks {
	public static final DeferredRegister.Blocks DR = DeferredRegister.createBlocks(AddonConstants.MOD_ID);
	
	public static final DeferredBlock<TinyFloorBlock> BLOOD_NO_COLLISION = registerWithBlockItem(
		"blood_no_collision",
		() -> new TinyFloorBlock(
			settingsFromBlockFront("BLOOD", Blocks.REDSTONE_BLOCK)
		)
	);
	public static final DeferredBlock<TinyFloorBlock> DEAD_FISH_NO_COLLISION = registerWithBlockItem(
		"dead_fish_no_collision",
		() -> new TinyFloorBlock(
			settingsFromBlockFront("DEAD_FISH", Blocks.OAK_PLANKS)
		)
	);
	
	private AddonBlocks() {
	}
	
	private static <T extends Block> DeferredBlock<T> registerWithBlockItem(String id, Supplier<T> blockSupplier) {
		DeferredBlock<T> block = DR.register(id, blockSupplier);
		AddonItems.DR.registerSimpleBlockItem(id, block);
		return block;
	}

	private static AbstractBlock.Settings settingsFromBlockFront(String fieldName, Block fallback) {
		try {
			Class<?> bfBlocksClass = Class.forName("com.boehmod.blockfront.registry.BFBlocks");
			Field field = bfBlocksClass.getDeclaredField(fieldName);
			Object holder = field.get(null);
			Method get = holder.getClass().getMethod("get");
			Object value = get.invoke(holder);
			if (value instanceof Block block) {
				return AbstractBlock.Settings.copy(block).noCollision();
			}
		} catch (Throwable throwable) {
			AddonConstants.LOGGER.warn("Falling back for BFBlocks.{} while registering MERCFront-core blocks.", fieldName);
		}
		return AbstractBlock.Settings.copy(fallback).noCollision();
	}
	
	public static void init(IEventBus eventBus) {
		DR.register(eventBus);
	}
}
