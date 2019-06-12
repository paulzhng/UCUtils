package de.fuzzlemann.ucutils.commands.mobile;

import de.fuzzlemann.ucutils.utils.abstraction.UPlayer;
import de.fuzzlemann.ucutils.utils.command.api.Command;
import de.fuzzlemann.ucutils.utils.command.api.CommandParam;
import de.fuzzlemann.ucutils.utils.mobile.MobileUtils;
import de.fuzzlemann.ucutils.utils.text.TextUtils;

/**
 * @author Fuzzlemann
 */
public class ReplyCommand {

    @Command(value = {"reply", "r"}, usage = "/%label% [Nachricht]")
    public boolean onCommand(UPlayer p, @CommandParam(joinStart = true) String message) {
        int lastNumber = MobileUtils.getLastNumber();
        if (lastNumber == -1) {
            TextUtils.error("Dir hat noch keine Person geschrieben.");
            return true;
        }

        p.sendChatMessage("/sms " + lastNumber + " " + message);
        return true;
    }
}