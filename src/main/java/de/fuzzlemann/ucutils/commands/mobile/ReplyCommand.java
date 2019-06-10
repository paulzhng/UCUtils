package de.fuzzlemann.ucutils.commands.mobile;

import de.fuzzlemann.ucutils.utils.command.api.Command;
import de.fuzzlemann.ucutils.utils.command.api.CommandParam;
import de.fuzzlemann.ucutils.utils.mobile.MobileUtils;
import net.minecraft.client.entity.EntityPlayerSP;

/**
 * @author Fuzzlemann
 */
public class ReplyCommand {

    @Command(value = {"reply", "r"}, usage = "/%label% [Nachricht]")
    public boolean onCommand(EntityPlayerSP p, @CommandParam(joinStart = true) String message) {
        int lastNumber = MobileUtils.getLastNumber();
        p.sendChatMessage("/sms " + lastNumber + " " + message);
        return true;
    }
}