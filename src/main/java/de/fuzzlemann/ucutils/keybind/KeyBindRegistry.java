package de.fuzzlemann.ucutils.keybind;

import de.fuzzlemann.ucutils.base.initializor.IInitializor;
import de.fuzzlemann.ucutils.base.initializor.InitMode;
import de.fuzzlemann.ucutils.base.initializor.Initializor;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

/**
 * @author Fuzzlemann
 */
@Initializor(value = "Keybinds", initMode = InitMode.DEFAULT)
public class KeyBindRegistry implements IInitializor {

    private static final String KEY_CATEGORY = "key.categories.ucutils";

    public static UKeyBinding aBuy;
    public static UKeyBinding autoNC;
    public static UKeyBinding acceptAD;
    public static UKeyBinding denyAD;
    public static UKeyBinding alternateScreenshot;
    public static UKeyBinding alternateScreenshotWithUpload;

    @Override
    public void init() {
        aBuy = new UKeyBinding("key.abuy", Keyboard.KEY_B, KEY_CATEGORY);
        autoNC = new UKeyBinding("key.autonc", Keyboard.KEY_G, KEY_CATEGORY);
        acceptAD = new UKeyBinding("key.acceptad", Keyboard.KEY_J, KEY_CATEGORY);
        denyAD = new UKeyBinding("key.denyad", Keyboard.KEY_N, KEY_CATEGORY);
        alternateScreenshot = new UKeyBinding("key.alternatescreenshot", Keyboard.KEY_NONE, KEY_CATEGORY);
        alternateScreenshotWithUpload = new UKeyBinding("key.alternatescreenshotwithupload", Keyboard.KEY_NONE, KEY_CATEGORY);

        ClientRegistry.registerKeyBinding(aBuy);
        ClientRegistry.registerKeyBinding(autoNC);
        ClientRegistry.registerKeyBinding(acceptAD);
        ClientRegistry.registerKeyBinding(denyAD);
        ClientRegistry.registerKeyBinding(alternateScreenshot);
        ClientRegistry.registerKeyBinding(alternateScreenshotWithUpload);
    }

}
