package de.fuzzlemann.ucutils.keybind;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import org.lwjgl.input.Keyboard;

/**
 * @author Fuzzlemann
 */
public class UKeyBinding extends KeyBinding {
    public UKeyBinding(String description, int keyCode, String category) {
        super(description, keyCode, category);
    }

    public UKeyBinding(String description, IKeyConflictContext keyConflictContext, int keyCode, String category) {
        super(description, keyConflictContext, keyCode, category);
    }

    public UKeyBinding(String description, IKeyConflictContext keyConflictContext, KeyModifier keyModifier, int keyCode, String category) {
        super(description, keyConflictContext, keyModifier, keyCode, category);
    }

    @Override
    public boolean isPressed() {
        return getKeyCode() != Keyboard.KEY_NONE && Keyboard.isKeyDown(getKeyCode());
    }
}
