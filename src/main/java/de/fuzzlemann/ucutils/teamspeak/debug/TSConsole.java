package de.fuzzlemann.ucutils.teamspeak.debug;

import de.fuzzlemann.ucutils.teamspeak.*;
import de.fuzzlemann.ucutils.teamspeak.commands.BaseCommand;
import de.fuzzlemann.ucutils.teamspeak.events.ClientMovedEvent;
import de.fuzzlemann.ucutils.utils.Logger;

import java.io.IOException;
import java.util.Scanner;

/**
 * Console used for debugging
 *
 * @author Fuzzlemann
 */
public class TSConsole implements TSListener {

    public static void main(String[] args) throws IOException {
        new TSAPIKeyLoader().load();
        TSClientQuery.getInstance();

        TSEventHandler.registerListener(new TSConsole());

        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNext()) {
                String line = scanner.nextLine();

                CommandResponse response = new BaseCommand<CommandResponse>(line) {
                }.getResponse();

                Logger.LOGGER.info(response.getRawResponse());
            }
        }
    }

    @EventHandler
    public void onClientMoved(ClientMovedEvent e) {
        System.out.println("e.getClientID() = " + e.getClientID());
        System.out.println("e.getTargetChannelID() = " + e.getTargetChannelID());
        System.out.println("e.getInvokerName() = " + e.getInvokerName());
    }
}
