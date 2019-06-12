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
        return new WhoAmICommand().execute().getResponse().getClientID();
    }

    public static int getMyChannelID() {
        return new WhoAmICommand().execute().getResponse().getChannelID();
    }

    public static List<Client> getClients() {
        return new ClientListCommand().execute().getResponse().getClientList();
    }

    public static List<Client> getClientsByName(String minecraftName) {
        return getClientsByName(Collections.singletonList(minecraftName));
    }

    public static List<Client> getClientsByName(List<String> minecraftNames) {
        List<Client> clients = new ArrayList<>();
        if (minecraftNames.isEmpty()) return clients;

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

            System.out.println(minecraftName);

            if (!minecraftNames.contains(minecraftName)) continue;
            clients.add(client);
        }

        return clients;
    }
}
