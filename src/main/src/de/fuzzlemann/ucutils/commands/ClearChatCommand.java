package de.fuzzlemann.ucutils.commands;

import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class ClearChatCommand implements CommandExecutor {

    @Override
    @Command("clearchat")
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        Main.MINECRAFT.ingameGUI.getChatGUI().clearChatMessages(false);
        return true;
    }
}
