package red.vuis.mercfrontcore.mixin;

import com.boehmod.blockfront.game.GameShopItem;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GameShopItem.class)
public interface GameShopItemAccessor {
	@Accessor("item")
	Item getItem();
}
