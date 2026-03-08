package dev.tomerdev.mercfrontcore.client.input;

public interface InputAcceptor {
    default void mousePressed(MouseButton button) {
    }

    default void mouseReleased(MouseButton button) {
    }
}
