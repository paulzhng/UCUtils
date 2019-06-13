package de.fuzzlemann.ucutils.teamspeak.debug;

import de.fuzzlemann.ucutils.teamspeak.CommandResponse;
import de.fuzzlemann.ucutils.teamspeak.TSAPIKeyLoader;
import de.fuzzlemann.ucutils.teamspeak.TSClientQuery;
import de.fuzzlemann.ucutils.teamspeak.commands.BaseCommand;
import de.fuzzlemann.ucutils.utils.Logger;

import java.io.IOException;
import java.util.Scanner;

/**
 * Console used for debugging
 *
 * @author Fuzzlemann
 */
public class TSConsole {
    public static void main(String[] args) throws IOException {
        new TSAPIKeyLoader().load();
        TSClientQuery.getInstance();

        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNext()) {
                String line = scanner.nextLine();

                CommandResponse response = new BaseCommand<CommandResponse>(line) {
                }.getResponse();

                Logger.LOGGER.info(response.getRawResponse());
            }
        }
    }
}
