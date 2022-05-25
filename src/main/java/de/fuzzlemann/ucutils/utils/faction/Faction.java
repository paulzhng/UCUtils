package de.fuzzlemann.ucutils.utils.faction;

import de.fuzzlemann.ucutils.base.command.ParameterParser;
import de.fuzzlemann.ucutils.utils.info.InfoStorage;

import java.util.Arrays;

/**
 * @author Fuzzlemann
 */
@ParameterParser.At(FactionParser.class)
public enum Faction {
    UCPD("polizei", "Police", 78),
    FBI("fbi", "FBI", 106),
    UCMD("rettungsdienst", "Medic", 118),
    NEWS("news", "News", 239),
    TERRORISTS("terroristen", "Terroristen", 203),
    HITMAN("hitman", "Hitman", 215),
    CHURCH("kirche", "Church", 227),
    LA_COSA_NOSTRA("lacosanostra", "Mafia", 130),
    CALDERON("calderon", "Mexican", 154),
    BALLAS("westsideballas", "Gang", 142),
    KERZAKOV("kerzakov", "Kerzakov", 166),
    TRIADS("triaden", "Triaden", 179),
    OBRIEN("o_brien", "O_Brien", 191);

    static Faction factionOfPlayer;
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