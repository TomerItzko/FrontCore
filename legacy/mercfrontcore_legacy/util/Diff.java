package red.vuis.mercfrontcore.util;

import lombok.Getter;

import java.util.Objects;

@Getter
public class Diff<T> {
	private T value;
	private T prevValue;
	private boolean updated = false;
	
	public Diff(T value) {
		this.value = value;
		this.prevValue = value;
	}
	
	public void update(T newValue) {
		updated = !Objects.equals(value, newValue);
		prevValue = value;
		value = newValue;
	}
}
