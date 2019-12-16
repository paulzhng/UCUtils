package de.fuzzlemann.ucutils.utils.faction;

import de.fuzzlemann.ucutils.base.command.ParameterParser;
import de.fuzzlemann.ucutils.utils.info.CommandInfo;
import de.fuzzlemann.ucutils.utils.info.InfoStorage;

import java.util.Arrays;

/**
 * @author Fuzzlemann
 */
@ParameterParser.At(FactionParser.class)
public enum Faction {
    UCPD("polizei", "Police", 60),
    FBI("fbi", "FBI", 5412),
    UCMD("rettungsdienst", "Medic", 69),
    NEWS("news", "News", 109),
    TERRORISTS("terroristen", "Terroristen", 199),
    HITMAN("hitman", "Hitman", 150),
    CHURCH("kirche", "Church", 101),
    LA_COSA_NOSTRA("lacosanostra", "Mafia", 79),
    CALDERON("calderon", "Mexican", 94),
    JUGGALOS("juggalos", "Gang", 86),
    KERZAKOV("kerzakov", "Kerzakov", 163),
    TRIADS("triaden", "Triaden", 5419),
    OBRIEN("o_brien", "O_Brien", 12328);

    static Faction factionOfPlayer;
    private static CommandInfo factionCommandInfo;
    private static CommandInfo badFactionCommandInfo;
    private final String apiName;
    private final String factionKey;
    private final int publicChannelID;

    Faction(String apiName, String factionKey, int publicChannelID) {
        this.apiName = apiName;
        this.factionKey = factionKey;
        this.publicChannelID = publicChannelID;
    }

    public static Faction byAPIName(String key) {
        for (Faction faction : values()) {
            if (faction.getAPIName().equalsIgnoreCase(key)) return faction;
        }

        return null;
    }

    public static Faction byShortName(String shortName) {
        return Arrays.stream(values())
                .filter(faction -> InfoStorage.factionInfoMap.get(faction).getShortName().equalsIgnoreCase(shortName))
                .findFirst()
                .orElse(null);
    }

    public String getAPIName() {
        return apiName;
    }

    public String getFactionKey() {
        return factionKey;
    }

    public int getPublicChannelID() {
        return publicChannelID;
    }

    public static Faction getFactionOfPlayer() {
        return factionOfPlayer;
    }
}
