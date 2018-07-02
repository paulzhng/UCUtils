package de.fuzzlemann.ucutils.utils.teamspeak;

import de.fuzzlemann.ucutils.utils.mcapi.MojangAPI;
import de.fuzzlemann.ucutils.utils.text.TextUtils;

import java.util.*;

/**
 * @author Fuzzlemann
 */
public class TSUtils {

    public static Optional<Integer> getMyClientID() {
        Map<String, String> whoAmIResult = TSClientQuery.exec("whoami");
        String clientID = whoAmIResult.get("clid");

        return clientID == null ? Optional.empty() : Optional.of(Integer.parseInt(clientID));
    }

    public static Optional<Integer> getMyChannelID() {
        Map<String, String> whoAmIResult = TSClientQuery.exec("whoami");
        String channelID = whoAmIResult.get("cid");

        return channelID == null ? Optional.empty() : Optional.of(Integer.parseInt(channelID));
    }

    public static List<Map<String, String>> getAllClientsWithParameters() {
        String clientString = TSClientQuery.rawExec("clientlist", false);
        return ResultParser.parseMap(clientString);
    }

    public static List<Map<String, String>> getClientsByName(String minecraftName) {
        return getClientsByName(Collections.singletonList(minecraftName));
    }

    public static List<Map<String, String>> getClientsByName(List<String> minecraftNames) {
        List<Map<String, String>> clients = new ArrayList<>();

        List<String> names = new ArrayList<>();
        for (String arg : minecraftNames) {
            names.addAll(MojangAPI.getEarlierNames(arg));
        }

        if (names.isEmpty()) return clients;

        for (Map<String, String> map : getAllClientsWithParameters()) {
            String clientIDString = map.get("clid");
            if (clientIDString == null) continue;

            int clientID = Integer.parseInt(clientIDString);

            Map<String, String> result = TSClientQuery.exec("clientvariable clid=" + clientID + " client_description");
            String description = result.get("client_description");

            if (description == null) continue;
            if (!names.contains(TextUtils.stripPrefix(description))) continue;

            clients.add(map);
        }

        return clients;
    }
}
