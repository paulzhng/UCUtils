package de.fuzzlemann.ucutils.commands.mobile;

import de.fuzzlemann.ucutils.utils.abstraction.UPlayer;
import de.fuzzlemann.ucutils.utils.command.api.Command;
import de.fuzzlemann.ucutils.utils.command.api.CommandParam;
import de.fuzzlemann.ucutils.utils.mobile.MobileUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
public class ASMSCommand {

    @Command(value = "asms", usage = "/%label% [Spieler] [Nachricht]", async = true)
    public boolean onCommand(UPlayer p, String target, @CommandParam(joinStart = true) String message) {
        int number = MobileUtils.getNumber(target);
        if (number == -1) return true;

        p.sendChatMessage("/sms " + number + " " + message);
        return true;
    }
}
