package dev.tomerdev.mercfrontcore.client.input;

import net.minecraft.client.Mouse;
import dev.tomerdev.mercfrontcore.util.Diff;

public final class InputTracker {
    private static InputTracker instance;

    private final Diff<Boolean> leftClicked = new Diff<>(false);

    private InputTracker() {
    }

    public static InputTracker getInstance() {
        if (instance == null) {
            instance = new InputTracker();
        }
        return instance;
    }

    public void update(Mouse mouse) {
        leftClicked.update(mouse.wasLeftButtonClicked());
    }

    public boolean leftClicked() {
        return leftClicked.isUpdated() && leftClicked.getValue();
    }

    public boolean leftReleased() {
        return leftClicked.isUpdated() && !leftClicked.getValue();
    }
}
