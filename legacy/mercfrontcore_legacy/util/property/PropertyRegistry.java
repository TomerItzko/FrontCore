package red.vuis.mercfrontcore.util.property;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import red.vuis.mercfrontcore.util.AddonUtils;

public class PropertyRegistry {
	private final List<PropertyHandler<?>> handlers;
	
	public PropertyRegistry(List<PropertyHandler<?>> handlers) {
		this.handlers = handlers;
	}
	
	public PropertyRegistry(PropertyHandler<?>... handlers) {
		this(List.of(handlers));
	}
	
	public PropertyHandleResult handle(Object object, String property, String arg) {
		boolean matchedType = false;
		
		for (PropertyHandler<?> handler : handlers) {
			if (!handler.type().isInstance(object)) {
				continue;
			}
			matchedType = true;
			
			PropertyHandleResult processResult = processHandler(handler, object, property, arg);
			if (processResult != null) {
				return processResult;
			}
		}
		
		if (matchedType) {
			return PropertyHandleResult.ERROR_PROPERTY;
		} else {
			return PropertyHandleResult.ERROR_TYPE;
		}
	}
	
	private static <T> @Nullable PropertyHandleResult processHandler(PropertyHandler<T> handler, Object object, String property, String arg) {
		PropertyEntry<T, ?> entry = handler.properties().get(property);
		if (entry == null) {
			return null;
		}
		
		return handleEntry(handler.type(), entry, object, arg);
	}
	
	private static <O, V> PropertyHandleResult handleEntry(Class<O> clazz, PropertyEntry<O, V> entry, Object object, String arg) {
		Optional<V> result = AddonUtils.parse(entry.parser(), arg);
		if (result.isEmpty()) {
			return PropertyHandleResult.ERROR_PARSE;
		}
		
		entry.setter().accept(clazz.cast(object), result.orElseThrow());
		return PropertyHandleResult.SUCCESS;
	}
	
	public List<String> getProperties(Object object) {
		List<String> result = new ArrayList<>();
		
		for (PropertyHandler<?> handler : handlers) {
			if (handler.type().isInstance(object)) {
				result.addAll(handler.properties().keySet());
			}
		}
		
		result.sort(null);
		return result;
	}
}
