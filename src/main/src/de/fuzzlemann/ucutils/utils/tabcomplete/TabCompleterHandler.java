package de.fuzzlemann.ucutils.utils.tabcomplete;

import com.google.common.collect.ImmutableSet;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.TabCompleter;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * @author Fuzzlemann
 */
@Mod.EventBusSubscriber
public class TabCompleterHandler {

    static final Set<String> EXCLUDED_SERVER = ImmutableSet.of("/navi");

    public static boolean initTabComplete(GuiChat chat) {
        try {
            Field inputField = null;
            Field tabCompleterField = null;

            for (Field f : GuiChat.class.getDeclaredFields()) {
                if (f.getType().equals(GuiTextField.class)) inputField = f;
                if (f.getType().equals(TabCompleter.class)) tabCompleterField = f;
            }

            if (inputField == null || tabCompleterField == null) return false;

            inputField.setAccessible(true);
            GuiTextField guiTextField = (GuiTextField) inputField.get(chat);

            tabCompleterField.setAccessible(true);

            TabCompleter currentTabCompleter = (TabCompleter) tabCompleterField.get(chat);
            if (currentTabCompleter instanceof TabCompleterEx) return false;

            TabCompleterEx tabCompleter = new TabCompleterEx(guiTextField);
            tabCompleterField.set(chat, tabCompleter);
            return true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return false;
    }

    @SubscribeEvent
    public static void onGuiScreen(GuiScreenEvent e) {
        if (e.getGui() instanceof GuiChat) initTabComplete((GuiChat) e.getGui());
    }
}
