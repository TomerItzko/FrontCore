package dev.tomerdev.mercfrontcore.mixin.client;

import com.boehmod.blockfront.client.render.geo.BFWeaponItemRenderer;
import com.boehmod.blockfront.common.item.BFWeaponItem;
import com.boehmod.blockfront.registry.BFDataComponents;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Modifier;

@Mixin(BFWeaponItemRenderer.class)
public abstract class BFWeaponItemRendererMixin {
    private static volatile List<Field> mercfrontcore$stackFields;
    private static volatile boolean mercfrontcore$stackFieldsResolved;

    @Inject(method = "getTextureLocation", at = @At("HEAD"), cancellable = true, require = 0)
    private void mercfrontcore$useDirectSkinTexture(BFWeaponItem<?> item, CallbackInfoReturnable<Identifier> cir) {
        ItemStack stack = mercfrontcore$resolveCurrentStack(this);
        if (stack == null || stack.isEmpty()) {
            return;
        }

        String skin = stack.get(BFDataComponents.PATTERN_NAME);
        if (skin == null || skin.isBlank()) {
            return;
        }

        String itemPath = Registries.ITEM.getId(item).getPath();
        String sanitizedSkin = sanitize(skin);
        Identifier direct = Identifier.of("bf", "textures/item/" + itemPath + "_" + sanitizedSkin + ".png");
        cir.setReturnValue(direct);
    }

    private static String sanitize(String skin) {
        return skin.toLowerCase(java.util.Locale.ROOT).replace(".", "").replace("-", "");
    }

    private static ItemStack mercfrontcore$resolveCurrentStack(Object rendererInstance) {
        List<Field> fields = mercfrontcore$getCurrentStackFields(rendererInstance.getClass());
        if (fields.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack fallback = ItemStack.EMPTY;
        for (Field field : fields) {
            try {
                Object value = field.get(rendererInstance);
                if (value instanceof ItemStack stack) {
                    if (!stack.isEmpty()) {
                        return stack;
                    }
                    if (fallback.isEmpty()) {
                        fallback = stack;
                    }
                }
            } catch (IllegalAccessException ignored) {
            }
        }
        return fallback;
    }

    private static List<Field> mercfrontcore$getCurrentStackFields(Class<?> startClass) {
        if (mercfrontcore$stackFieldsResolved) {
            return mercfrontcore$stackFields == null ? List.of() : mercfrontcore$stackFields;
        }
        synchronized (BFWeaponItemRendererMixin.class) {
            if (mercfrontcore$stackFieldsResolved) {
                return mercfrontcore$stackFields == null ? List.of() : mercfrontcore$stackFields;
            }
            List<Field> found = new ArrayList<>();
            Class<?> cursor = startClass;
            while (cursor != null && cursor != Object.class) {
                for (Field field : cursor.getDeclaredFields()) {
                    if (Modifier.isStatic(field.getModifiers())) {
                        continue;
                    }
                    if (!ItemStack.class.isAssignableFrom(field.getType())) {
                        continue;
                    }
                    try {
                        field.setAccessible(true);
                        found.add(field);
                    } catch (Throwable ignored) {
                    }
                }
                cursor = cursor.getSuperclass();
            }
            mercfrontcore$stackFieldsResolved = true;
            mercfrontcore$stackFields = List.copyOf(found);
            return mercfrontcore$stackFields;
        }
    }
}
