package de.fuzzlemann.ucutils.utils.command.tabcompletion;

import com.google.common.collect.ImmutableSet;
import de.fuzzlemann.ucutils.utils.ReflectionUtil;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.TabCompleter;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Set;

/**
 * @author Fuzzlemann
 */
@Mod.EventBusSubscriber
public class TabCompletionHandler {

    static final Set<String> EXCLUDED_SERVER = ImmutableSet.of("/navi");

    private static void initTabComplete(GuiChat chat) {
        GuiTextField guiTextField = ReflectionUtil.getValue(chat, GuiTextField.class);
        TabCompleter tabCompleter = ReflectionUtil.getValue(chat, TabCompleter.class);

        if (tabCompleter instanceof TabCompleterEx) return;

        TabCompleterEx tabCompleterEx = new TabCompleterEx(guiTextField);
        ReflectionUtil.setValue(chat, TabCompleter.class, tabCompleterEx);
    }

    @SubscribeEvent
    public static void onGuiScreen(GuiScreenEvent e) {
        if (e.getGui() instanceof GuiChat) initTabComplete((GuiChat) e.getGui());
    }
}
