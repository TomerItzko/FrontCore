package dev.tomerdev.mercfrontcore.util.math;

public record IntBounds(Integer min, Integer max) {
    public static IntBounds all() {
        return of(null, null);
    }

    public static IntBounds of(Integer min, Integer max) {
        return new IntBounds(min, max);
    }

    public static IntBounds ofMin(int min) {
        return of(min, null);
    }

    public boolean test(int value) {
        return within(value);
    }

    public boolean within(int value) {
        if (min != null && value < min) {
            return false;
        }
        if (max != null && value > max) {
            return false;
        }
        return true;
    }

    public int clamp(int value) {
        if (min != null && value < min) {
            return min;
        }
        if (max != null && value > max) {
            return max;
        }
        return value;
    }
}
