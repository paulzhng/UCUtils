package de.fuzzlemann.ucutils.commands;

import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.utils.command.api.Command;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class ClearChatCommand {

    @Command("clearchat")
    public boolean onCommand() {
        Main.MINECRAFT.ingameGUI.getChatGUI().clearChatMessages(false);
        return true;
    }
}
