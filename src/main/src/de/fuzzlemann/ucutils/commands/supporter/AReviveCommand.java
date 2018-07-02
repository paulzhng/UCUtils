package de.fuzzlemann.ucutils.commands.supporter;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class AReviveCommand implements CommandExecutor {

    @Override
    @Command(labels = "arevive", usage = "/arevive [Spieler]")
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        if (args.length == 0) return false;

        for (String arg : args) {
            p.sendChatMessage("/revive " + arg);
        }

        return true;
    }
}
