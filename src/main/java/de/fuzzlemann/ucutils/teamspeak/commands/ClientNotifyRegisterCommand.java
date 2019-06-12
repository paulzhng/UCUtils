package de.fuzzlemann.ucutils.teamspeak.commands;

import de.fuzzlemann.ucutils.teamspeak.CommandResponse;

/**
 * @author Fuzzlemann
 */
public class ClientNotifyRegisterCommand extends BaseCommand<CommandResponse> {
    public ClientNotifyRegisterCommand(int schandlerID, String eventName) {
        super("clientnotifyregister schandlerid=" + schandlerID + " event=" + eventName);
    }
}
