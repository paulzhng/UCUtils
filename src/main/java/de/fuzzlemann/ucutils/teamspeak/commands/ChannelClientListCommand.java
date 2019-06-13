package de.fuzzlemann.ucutils.teamspeak.commands;

import de.fuzzlemann.ucutils.teamspeak.CommandResponse;
import de.fuzzlemann.ucutils.teamspeak.objects.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Fuzzlemann
 */
public class ChannelClientListCommand extends BaseCommand<ChannelClientListCommand.Response> {

    public ChannelClientListCommand(int channelID) {
        super("channelclientlist cid=" + channelID);
    }

    public static class Response extends CommandResponse {
        private final List<Client> clients = new ArrayList<>();

        public Response(String rawResponse) {
            super(rawResponse);
            List<Map<String, String>> maps = getResponseList();

            for (Map<String, String> clientMap : maps) {
                clients.add(new Client(clientMap));
            }
        }

        public List<Client> getClients() {
            return clients;
        }
    }
}
