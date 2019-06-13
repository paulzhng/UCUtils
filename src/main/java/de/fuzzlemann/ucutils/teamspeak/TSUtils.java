package de.fuzzlemann.ucutils.teamspeak;

import de.fuzzlemann.ucutils.teamspeak.commands.ClientListCommand;
import de.fuzzlemann.ucutils.teamspeak.commands.ClientVariableCommand;
import de.fuzzlemann.ucutils.teamspeak.commands.WhoAmICommand;
import de.fuzzlemann.ucutils.teamspeak.objects.Client;

import java.util.*;

/**
 * @author Fuzzlemann
 */
public class TSUtils {

    public static int getMyClientID() {
        return new WhoAmICommand().getResponse().getClientID();
    }

    public static int getMyChannelID() {
        return new WhoAmICommand().getResponse().getChannelID();
    }

    public static List<Client> getClients() {
        return new ClientListCommand().getResponse().getClientList();
    }

    public static List<Client> getClientsByName(String minecraftName) {
        return getClientsByName(Collections.singletonList(minecraftName));
    }

    public static List<Client> getClientsByName(List<String> minecraftNames) {
        if (minecraftNames.isEmpty()) return Collections.emptyList();

        List<Client> clients = new ArrayList<>();
        Map<Client, CommandFuture<ClientVariableCommand.Response>> futures = new HashMap<>();
        for (Client client : getClients()) {
            int clientID = client.getClientID();

            CommandFuture<ClientVariableCommand.Response> future = new ClientVariableCommand(clientID, "client_description", "client_nickname").execute().getResponseFuture();
            futures.put(client, future);
        }

        for (Map.Entry<Client, CommandFuture<ClientVariableCommand.Response>> entry : futures.entrySet()) {
            Client client = entry.getKey();
            CommandFuture<ClientVariableCommand.Response> future = entry.getValue();

            ClientVariableCommand.Response response = future.get();
            String minecraftName = response.getMinecraftName();

            if (!minecraftNames.contains(minecraftName)) continue;
            clients.add(client);
        }

        return clients;
    }
}
