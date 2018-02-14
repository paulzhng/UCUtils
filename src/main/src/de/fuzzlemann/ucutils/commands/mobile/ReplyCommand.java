package de.fuzzlemann.ucutils.commands.mobile;

import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.mobile.MobileUtils;
import net.minecraft.client.entity.EntityPlayerSP;

/**
 * @author Fuzzlemann
 */
public class ReplyCommand implements CommandExecutor {

    @Override
    @Command(labels = {"reply", "r"}, usage = "/%label% [Nachricht]")
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        if (args.length == 0) return false;

        String message = String.join(" ", args);

        int lastNumber = MobileUtils.getLastNumber();
        p.sendChatMessage("/sms " + lastNumber + " " + message);
        return true;
    }
}