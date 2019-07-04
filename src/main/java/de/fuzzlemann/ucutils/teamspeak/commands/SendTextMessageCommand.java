package de.fuzzlemann.ucutils.teamspeak.commands;

import de.fuzzlemann.ucutils.teamspeak.CommandResponse;
import de.fuzzlemann.ucutils.teamspeak.TSParser;
import de.fuzzlemann.ucutils.teamspeak.objects.Client;
import de.fuzzlemann.ucutils.teamspeak.objects.TargetMode;

/**
 * @author Fuzzlemann
 */
public class SendTextMessageCommand extends BaseCommand<CommandResponse> {
    public SendTextMessageCommand(Client target, String message) {
        this(target.getClientID(), message);
    }

    public SendTextMessageCommand(int targetID, String message) {
        super("sendtextmessage targetmode=" + TargetMode.PRIVATE.getID() + " target=" + targetID + " msg=" + TSParser.encode(message));
    }

    public SendTextMessageCommand(TargetMode targetMode, String message) {
        super("sendtextmessage targetmode=" + targetMode.getID() + " msg=" + TSParser.encode(message));
    }
}
