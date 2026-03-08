package red.vuis.mercfrontcore.util.property;

import java.util.Map;

public record PropertyHandler<T>(Class<T> type, Map<String, PropertyEntry<T, ?>> properties) {
}
