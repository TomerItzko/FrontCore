package red.vuis.mercfrontcore.util.math;

public class IntBounds {
	public static final IntBounds ALL = new IntBounds(Integer.MIN_VALUE, Integer.MAX_VALUE);
	
	public final int min, max;
	
	private IntBounds(int min, int max) {
		if (max < min) {
			throw new IllegalArgumentException("Invalid bounds");
		}
		this.min = min;
		this.max = max;
	}
	
	public static IntBounds all() {
		return ALL;
	}
	
	public static IntBounds of(int min, int max) {
		return new IntBounds(min, max);
	}
	
	public static IntBounds ofMin(int min) {
		return new IntBounds(min, Integer.MAX_VALUE);
	}
	
	public static IntBounds ofMax(int max) {
		return new IntBounds(Integer.MIN_VALUE, max);
	}
	
	public static IntBounds ofOnly(int value) {
		return new IntBounds(value, value);
	}
	
	public boolean within(int value) {
		return value >= min && value <= max;
	}
	
	public int clamp(int value) {
		return Math.max(min, Math.min(max, value));
	}
	
	public int parse(String s) {
		int value = Integer.parseInt(s);
		if (!within(value)) {
			throw new IllegalArgumentException("Value out of bounds");
		}
		return value;
	}
}
