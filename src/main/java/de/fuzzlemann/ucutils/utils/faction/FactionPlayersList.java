package de.fuzzlemann.ucutils.utils.faction;

import de.fuzzlemann.ucutils.base.udf.UDFLoader;
import de.fuzzlemann.ucutils.base.udf.UDFModule;
import de.fuzzlemann.ucutils.common.udf.DataRegistry;
import de.fuzzlemann.ucutils.common.udf.data.faction.FactionPlayers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Fuzzlemann
 */
@UDFModule(value = DataRegistry.FACTION_PLAYERS, version = 1)
public class FactionPlayersList implements UDFLoader<List<FactionPlayers>> {

    public static final Map<String, Faction> PLAYER_TO_FACTION_MAP = new HashMap<>();

    @Override
    public void supply(List<FactionPlayers> factionPlayersList) {
        for (FactionPlayers factionPlayers : factionPlayersList) {
            Faction faction = Faction.byAPIName(factionPlayers.getFactionName());
            List<String> players = factionPlayers.getPlayers();

            for (String player : players) {
                PLAYER_TO_FACTION_MAP.put(player, faction);
            }
        }
    }

    @Override
    public void cleanUp() {
        PLAYER_TO_FACTION_MAP.clear();
    }

}
