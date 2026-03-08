package red.vuis.mercfrontcore.util.property;

import java.util.function.BiConsumer;
import java.util.function.Function;

public record PropertyEntry<T, V>(Function<String, V> parser, BiConsumer<T, V> setter) {
}
