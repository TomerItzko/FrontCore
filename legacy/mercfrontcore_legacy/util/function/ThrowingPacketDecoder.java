package red.vuis.mercfrontcore.util.function;

@FunctionalInterface
public interface ThrowingPacketDecoder<I, T> {
	T decode(I buf) throws Exception;
}
