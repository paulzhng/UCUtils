package de.fuzzlemann.ucutils.common.udf.data.faction;

import de.fuzzlemann.ucutils.common.udf.Data;
import de.fuzzlemann.ucutils.common.udf.DataRegistry;

import java.util.List;

/**
 * @author Fuzzlemann
 */
public class FactionPlayersData extends Data<List<FactionPlayers>> {
    public FactionPlayersData(List<FactionPlayers> factionPlayers) {
        super(DataRegistry.FACTION_PLAYERS, 1, factionPlayers);
    }
}
