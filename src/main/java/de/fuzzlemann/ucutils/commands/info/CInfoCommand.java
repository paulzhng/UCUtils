package de.fuzzlemann.ucutils.commands.info;

import de.fuzzlemann.ucutils.base.command.Command;
import de.fuzzlemann.ucutils.utils.info.InfoStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class CInfoCommand {

    @Command({"cinfo", "commandinfo"})
    public boolean onCommand() {
        InfoStorage.commandInfo.constructMessage("Wichtige Befehle").send();
        return true;
    }
}
