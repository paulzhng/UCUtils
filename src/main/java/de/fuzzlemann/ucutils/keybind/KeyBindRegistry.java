package de.fuzzlemann.ucutils.keybind;

import de.fuzzlemann.ucutils.utils.initializor.IInitializor;
import de.fuzzlemann.ucutils.utils.initializor.InitMode;
import de.fuzzlemann.ucutils.utils.initializor.Initializor;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

/**
 * @author Fuzzlemann
 */
@Initializor(value = "Keybinds", initMode = InitMode.DEFAULT)
public class KeyBindRegistry implements IInitializor {

    private static final String KEY_CATEGORY = "key.categories.ucutils";

    public static KeyBinding aBuy;
    public static KeyBinding autoNC;
    public static KeyBinding acceptAD;
    public static KeyBinding denyAD;

    @Override
    public void init() {
        aBuy = new KeyBinding("key.abuy", Keyboard.KEY_B, KEY_CATEGORY);
        autoNC = new KeyBinding("key.autonc", Keyboard.KEY_G, KEY_CATEGORY);
        acceptAD = new KeyBinding("key.acceptad", Keyboard.KEY_J, KEY_CATEGORY);
        denyAD = new KeyBinding("key.denyad", Keyboard.KEY_N, KEY_CATEGORY);

        ClientRegistry.registerKeyBinding(aBuy);
        ClientRegistry.registerKeyBinding(autoNC);
        ClientRegistry.registerKeyBinding(acceptAD);
        ClientRegistry.registerKeyBinding(denyAD);
    }
}
