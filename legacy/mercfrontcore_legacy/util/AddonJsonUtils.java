package red.vuis.mercfrontcore.util;

import java.util.NoSuchElementException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public final class AddonJsonUtils {
	private AddonJsonUtils() {
	}
	
	public static JsonElement getOrThrow(JsonObject root, String memberName) {
		JsonElement value = root.get(memberName);
		if (value == null) {
			throw new NoSuchElementException(memberName);
		}
		return value;
	}
}
