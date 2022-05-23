package de.fuzzlemann.ucutils.utils.faction;

import de.fuzzlemann.ucutils.base.udf.UDFLoader;
import de.fuzzlemann.ucutils.base.udf.UDFModule;
import de.fuzzlemann.ucutils.common.udf.DataRegistry;
import de.fuzzlemann.ucutils.common.udf.data.faction.FactionPlayers;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Fuzzlemann
 */
@UDFModule(value = DataRegistry.FACTION_PLAYERS, version = 1)
public class FactionPlayersList implements UDFLoader<List<FactionPlayers>> {

    public static final Map<String, Faction> PLAYER_TO_FACTION_MAP = new HashMap<>();
    private final Pattern BALLAS_NAME_PATTERN = Pattern.compile("<h4 class=\"h5 g-mb-5\"><strong>([a-zA-Z0-9-]+)");

    @Override
    public void supply(List<FactionPlayers> factionPlayersList) {
        for (FactionPlayers factionPlayers : factionPlayersList) {
            Faction faction = Faction.byAPIName(factionPlayers.getFactionName());
            List<String> players = factionPlayers.getPlayers();

            for (String player : players) {
                PLAYER_TO_FACTION_MAP.put(player, faction);
            }
        }

        // WORKAROUND Start - because serverside api cannot be updated by contributors (ballas)
        Matcher matcher = BALLAS_NAME_PATTERN.matcher(getWebsiteSourceCode());
        while (matcher.find())
            PLAYER_TO_FACTION_MAP.put(
                    matcher.group().replace("<h4 class=\"h5 g-mb-5\"><strong>",""),
                    Faction.BALLAS
            );

        PLAYER_TO_FACTION_MAP.forEach((k, v) -> {
            if (v.equals(Faction.BALLAS)) System.out.println(k);
        });
        // WORKAROUND End
    }

    @Override
    public void cleanUp() {
        PLAYER_TO_FACTION_MAP.clear();
    }

    private String getWebsiteSourceCode() {
        Scanner scanner = null;
        try {
            URLConnection openConnection = new URL("https://unicacity.de/fraktionen/" + Faction.BALLAS.getAPIName()).openConnection();
            openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
            scanner = new Scanner(new InputStreamReader(openConnection.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder website_code = new StringBuilder();
        while (true) {
            assert scanner != null;
            if (!scanner.hasNextLine()) break;
            website_code.append(scanner.nextLine()).append("\n\r");
        }

        return website_code.toString();
    }
}