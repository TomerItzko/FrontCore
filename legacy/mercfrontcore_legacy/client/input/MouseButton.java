package red.vuis.mercfrontcore.client.input;

import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

public enum MouseButton {
	LEFT,
	RIGHT,
	MIDDLE;
	
	public static @Nullable MouseButton fromCode(int code) {
		return switch (code) {
			case GLFW.GLFW_MOUSE_BUTTON_LEFT -> LEFT;
			case GLFW.GLFW_MOUSE_BUTTON_RIGHT -> RIGHT;
			case GLFW.GLFW_MOUSE_BUTTON_MIDDLE -> MIDDLE;
			default -> null;
		};
	}
	
	public static void apply(int code, Consumer<MouseButton> consumer) {
		MouseButton button = fromCode(code);
		if (button != null) {
			consumer.accept(button);
		}
	}
}
