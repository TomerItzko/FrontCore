package dev.tomerdev.mercfrontcore.util.math;

public class FloatBounds {
	public static final FloatBounds ALL = new FloatBounds(Float.MIN_VALUE, Float.MAX_VALUE);
	
	public final float min, max;
	
	private FloatBounds(float min, float max) {
		if (max < min) {
			throw new IllegalArgumentException("Invalid bounds");
		}
		this.min = min;
		this.max = max;
	}
	
	public static FloatBounds all() {
		return ALL;
	}
	
	public static FloatBounds of(float min, float max) {
		return new FloatBounds(min, max);
	}
	
	public static FloatBounds ofMin(float min) {
		return new FloatBounds(min, Float.MAX_VALUE);
	}
	
	public static FloatBounds ofMax(float max) {
		return new FloatBounds(Float.MIN_VALUE, max);
	}
	
	public static FloatBounds ofOnly(float value) {
		return new FloatBounds(value, value);
	}
	
	public boolean within(float value) {
		return value >= min && value <= max;
	}
	
	public float clamp(float value) {
		return Math.max(min, Math.min(max, value));
	}
}
