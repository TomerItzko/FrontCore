package dev.tomerdev.mercfrontcore.util;

import java.util.Objects;

public final class Diff<T> {
    private T value;
    private T prevValue;
    private boolean updated;

    public Diff(T value) {
        this.value = value;
        this.prevValue = value;
        this.updated = false;
    }

    public void update(T newValue) {
        updated = !Objects.equals(value, newValue);
        prevValue = value;
        value = newValue;
    }

    public T getValue() {
        return value;
    }

    public T getPrevValue() {
        return prevValue;
    }

    public boolean isUpdated() {
        return updated;
    }
}
