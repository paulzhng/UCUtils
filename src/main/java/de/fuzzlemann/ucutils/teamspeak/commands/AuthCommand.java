package de.fuzzlemann.ucutils.teamspeak.commands;

import de.fuzzlemann.ucutils.teamspeak.CommandResponse;

/**
 * @author Fuzzlemann
 */
public class AuthCommand extends BaseCommand<CommandResponse> {
    public AuthCommand(String apiKey) {
        super("auth apikey=" + apiKey);
    }
}
