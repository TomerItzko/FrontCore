package red.vuis.mercfrontcore.util.function;

@FunctionalInterface
public interface ThrowingValueFirstEncoder<O, T> {
	void encode(T value, O buf) throws Exception;
}
