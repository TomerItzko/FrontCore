package red.vuis.mercfrontcore.mixin;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import com.boehmod.bflib.fds.tag.FDSTagCompound;
import com.boehmod.blockfront.assets.AssetCommandBuilder;
import com.boehmod.blockfront.common.BFAbstractManager;
import com.boehmod.blockfront.common.entity.VendorEntity;
import com.boehmod.blockfront.game.GameShopItem;
import com.boehmod.blockfront.game.GameShopItems;
import com.boehmod.blockfront.game.impl.inf.InfectedGame;
import com.boehmod.blockfront.registry.BFBlocks;
import com.boehmod.blockfront.util.BFStyles;
import com.boehmod.blockfront.util.CommandUtils;
import com.boehmod.blockfront.util.math.FDSPose;
import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import red.vuis.mercfrontcore.command.bf.AssetCommandValidatorsEx;
import red.vuis.mercfrontcore.ex.InfectedGameConstants;
import red.vuis.mercfrontcore.util.AddonRegUtils;
import red.vuis.mercfrontcore.util.AddonUtils;

import static red.vuis.mercfrontcore.util.AddonAccessors.applyGameShopItem;

@Mixin(InfectedGame.class)
public abstract class InfectedGameMixin {
	@Shadow
	@Final
	@NotNull
	public List<GameShopItem> shopItems;
	@Shadow
	@Final
	private @NotNull AssetCommandBuilder command;
	
	@Unique
	private boolean mercfrontcore$useCustomShopItems = false;
	@Unique
	private final List<GameShopItem> mercfrontcore$customShopItems = new ObjectArrayList<>();
	
	@Inject(
		method = "<init>",
		at = @At("TAIL")
	)
	private void addCommands(BFAbstractManager<?, ?, ?> manager, CallbackInfo ci) {
		command.subCommand("shop", mercfrontcore$addShopCommands(new AssetCommandBuilder()));
	}
	
	@Unique
	private AssetCommandBuilder mercfrontcore$addShopCommands(AssetCommandBuilder baseCommand) {
		baseCommand.subCommand("add", new AssetCommandBuilder((context, args) -> {
			CommandOutput output = context.getSource().output;
			
			var itemParse = AddonUtils.parse(AddonRegUtils::getItem, args[0]);
			var priceParse = AddonUtils.parse(InfectedGameConstants.PRICE_BOUNDS::parse, args[1]);
			
			if (AddonUtils.anyEmpty(itemParse, priceParse)) {
				CommandUtils.sendBfaWarn(output, Text.translatable("mercfrontcore.message.command.game.inf.shop.add.error"));
				return;
			}
			
			Item item = itemParse.orElseThrow();
			int price = priceParse.orElseThrow();

			mercfrontcore$customShopItems.add(new GameShopItem(item, price));
			
			CommandUtils.sendBfa(output, Text.translatable(
				"mercfrontcore.message.command.game.inf.shop.add.success",
				item.getName().copy().setStyle(BFStyles.LIME),
				Text.literal("$" + price).setStyle(BFStyles.LIME)
			));
		}).validator(
			AssetCommandValidatorsEx.count("item", "price")
		));
		
		baseCommand.subCommand("clear", new AssetCommandBuilder((context, args) -> {
			CommandOutput output = context.getSource().output;
			
			mercfrontcore$customShopItems.clear();
			
			CommandUtils.sendBfa(output, Text.translatable("mercfrontcore.message.command.game.inf.shop.clear.success"));
		}));
		
		baseCommand.subCommand("useCustom", new AssetCommandBuilder((context, args) -> {
			CommandOutput output = context.getSource().output;
			
			var value = AddonUtils.parse(Boolean::parseBoolean, args[0]);
			if (value.isEmpty()) {
				CommandUtils.sendBfa(output, Text.translatable("mercfrontcore.message.command.error.value.boolean"));
			}
			
			mercfrontcore$useCustomShopItems = value.orElseThrow();
			CommandUtils.sendBfa(output,
				mercfrontcore$useCustomShopItems ?
					Text.translatable("mercfrontcore.message.command.game.inf.shop.useCustom.enabled") :
					Text.translatable("mercfrontcore.message.command.game.inf.shop.useCustom.disabled")
			);
		}).validator(
			AssetCommandValidatorsEx.count("value")
		));
		
		return baseCommand;
	}
	
	@Definition(id = "getBlock", method = "Lnet/minecraft/block/BlockState;getBlock()Lnet/minecraft/block/Block;")
	@Definition(id = "IRON_DOOR", field = "Lnet/minecraft/block/Blocks;IRON_DOOR:Lnet/minecraft/block/Block;")
	@Expression("?.getBlock() != IRON_DOOR")
	@ModifyExpressionValue(
		method = "method_3657",
		at = @At("MIXINEXTRAS:EXPRESSION")
	)
	private boolean addBlastDoor(boolean original, @Local BlockState target) {
		return original && target.getBlock() != BFBlocks.DOOR_BLAST.get();
	}
	
	@ModifyArg(
		method = "method_3657",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/text/Text;literal(Ljava/lang/String;)Lnet/minecraft/text/MutableText;",
			ordinal = 1
		),
		index = 0
	)
	private String changeIronDoorMessage(String string) {
		return "Failed to create new door! (Invalid door position! No iron or blast door found.)";
	}
	
	@Redirect(
		method = "relocateVendor",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/world/ServerWorld;spawnEntity(Lnet/minecraft/entity/Entity;)Z",
			ordinal = 0
		)
	)
	private boolean fixVendorRelocate(ServerWorld world, Entity entity, @Local(argsOnly = true) FDSPose pose) {
		entity.refreshPositionAndAngles(pose.position, pose.rotation.x, pose.rotation.y);
		return world.spawnEntity(entity);
	}
	
	@Redirect(
		method = "relocateVendor",
		at = @At(
			value = "INVOKE",
			target = "Lcom/boehmod/blockfront/common/entity/VendorEntity;teleport(Lnet/minecraft/server/world/ServerWorld;DDDLjava/util/Set;FF)Z",
			ordinal = 0
		)
	)
	private boolean disableVendorTeleport(VendorEntity instance, ServerWorld world, double destX, double destY, double destZ, Set<PositionFlag> flags, float yaw, float pitch) {
		return false;
	}
	
	@Inject(
		method = "method_3648",
		at = @At(
			value = "INVOKE",
			target = "Ljava/util/List;clear()V",
			shift = At.Shift.AFTER
		)
	)
	private void betterShopItemLoop(CallbackInfo ci) {
		List<GameShopItem> remainingItems = new ObjectArrayList<>(
			mercfrontcore$useCustomShopItems ? mercfrontcore$customShopItems : GameShopItems.ENTRIES
		);
		int numShopItems = Math.min(6, remainingItems.size());
		Random random = ThreadLocalRandom.current();
		
		for (int i = 0; i < numShopItems; i++) {
			int randomIndex = random.nextInt(remainingItems.size());
			shopItems.add(remainingItems.get(randomIndex));
			remainingItems.remove(randomIndex);
		}
	}
	
	@ModifyConstant(
		method = "method_3648",
		constant = @Constant(intValue = 6)
	)
	private int disableDefaultLoop(int constant) {
		return Integer.MIN_VALUE;
	}
	
	@Inject(
		method = "writeSpecificFDS",
		at = @At("TAIL")
	)
	private void writeCustomFDS(FDSTagCompound root, CallbackInfo ci) {
		root.setBoolean("useCustomShopItems", mercfrontcore$useCustomShopItems);
		
		root.setInteger("customShopItemCount", mercfrontcore$customShopItems.size());
		for (int i = 0; i < mercfrontcore$customShopItems.size(); i++) {
			GameShopItem shopItem = mercfrontcore$customShopItems.get(i);
			
			FDSTagCompound itemRoot = new FDSTagCompound("customShopItem" + i);
			itemRoot.setString("item", Registries.ITEM.getId(applyGameShopItem(shopItem, GameShopItemAccessor::getItem)).toString());
			itemRoot.setInteger("price", shopItem.getPrice());
			
			root.setTagCompound("customShopItem" + i, itemRoot);
		}
	}
	
	@Inject(
		method = "readSpecificFDS",
		at = @At("TAIL")
	)
	private void readCustomFDS(FDSTagCompound root, CallbackInfo ci) {
		mercfrontcore$useCustomShopItems = root.getBoolean("useCustomShopItems", false);
		
		int customShopItemCount = root.getInteger("customShopItemCount");
		for (int i = 0; i < customShopItemCount; i++) {
			FDSTagCompound itemRoot = root.getTagCompound("customShopItem" + i);
			if (itemRoot == null) {
				continue;
			}
			
			String itemStr = root.getString("item");
			if (itemStr == null) {
				continue;
			}
			mercfrontcore$customShopItems.add(new GameShopItem(
				Registries.ITEM.get(Identifier.tryParse(itemStr)),
				itemRoot.getInteger("price")
			));
		}
	}
}
