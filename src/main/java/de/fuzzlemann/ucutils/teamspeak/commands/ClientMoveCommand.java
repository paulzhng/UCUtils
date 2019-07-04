package de.fuzzlemann.ucutils.teamspeak.commands;

import de.fuzzlemann.ucutils.teamspeak.CommandResponse;
import de.fuzzlemann.ucutils.teamspeak.TSParser;
import de.fuzzlemann.ucutils.teamspeak.TSUtils;
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

    public ClientMoveCommand(int channelID, String password) {
        super("clientmove cid=" + channelID + " cpw= " + TSParser.encode(password) + " clid=" + TSUtils.getMyClientID());
    }

    private static String parseCommand(int channelID, int... clientIDs) {
        StringJoiner stringJoiner = new StringJoiner("|");
        for (int clientID : clientIDs) {
            stringJoiner.add("clid=" + clientID);
        }

        return "clientmove cid=" + channelID + " " + stringJoiner;
    }
}
