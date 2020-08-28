package de.fuzzlemann.ucutils.common.udf.data.faction;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * @author Fuzzlemann
 */
public class FactionPlayers {

    @Expose
    private final String factionName;
    @Expose
    private final List<String> players;

    public FactionPlayers(String factionName, List<String> players) {
        this.factionName = factionName;
        this.players = players;
    }

    public String getFactionName() {
        return factionName;
    }

    public List<String> getPlayers() {
        return players;
    }
}
