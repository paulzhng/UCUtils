package de.fuzzlemann.ucutils.teamspeak;

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
                }.execute().getResponse();

                Logger.LOGGER.info(response.getRawResponse());
            }
        }
    }
}
