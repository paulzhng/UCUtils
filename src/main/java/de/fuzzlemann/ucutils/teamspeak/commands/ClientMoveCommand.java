package de.fuzzlemann.ucutils.teamspeak.commands;

import de.fuzzlemann.ucutils.teamspeak.CommandResponse;
import de.fuzzlemann.ucutils.teamspeak.objects.Client;

import java.util.Arrays;
import java.util.Collection;
import java.util.StringJoiner;

/**
 * @author Fuzzlemann
 */
public class ClientMoveCommand extends BaseCommand<CommandResponse> {

    public ClientMoveCommand(int channelID, Collection<Client> clients) {
        this(channelID, clients.stream().mapToInt(Client::getClientID).toArray());
    }

    public ClientMoveCommand(int channelID, Client... clients) {
        this(channelID, Arrays.stream(clients).mapToInt(Client::getClientID).toArray());
    }

    public ClientMoveCommand(int channelID, int... clientIDs) {
        super(parseCommand(channelID, clientIDs));
    }

    private static String parseCommand(int channelID, int... clientIDs) {
        StringJoiner stringJoiner = new StringJoiner("|");
        for (int clientID : clientIDs) {
            stringJoiner.add("clid=" + String.valueOf(clientID));
        }

        return "clientmove cid=" + channelID + " " + stringJoiner;
    }
}
