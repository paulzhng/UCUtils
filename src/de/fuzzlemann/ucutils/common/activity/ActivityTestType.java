package de.fuzzlemann.ucutils.common.activity;

import java.util.regex.Pattern;

/**
 * @author Fuzzlemann
 */
public enum ActivityTestType {

    EMERGENCY_LINE("Notruf", Pattern.compile("^Du hast den Service von (?:\\[UC])*[a-zA-Z0-9_]+ als 'Erledigt' markiert!$")),
    REINFORCEMENT("Reinforcement", null), // manually
    ARREST("Verhaftungen", Pattern.compile("^HQ: (?:\\[UC])*[a-zA-Z0-9_]+ wurde von (?:\\[UC])*([a-zA-Z0-9_]+) eingesperrt\\.$")),
    KILLS("Kills", Pattern.compile("^HQ: (?:\\[UC])*[a-zA-Z0-9_]+ wurde von (?:\\[UC])*([a-zA-Z0-9_]+) getötet\\.$")),
    LARGE_OPERATION("Großeinsatz", null),
    OTHER("Andere", null),
    EQUIP("Equip", Pattern.compile("^\\[F-Bank] (?:\\[UC])*([a-zA-Z0-9_]+) hat \\d+\\$ in die Fraktionsbank eingezahlt\\.$"));

    private final String name;
    private final Pattern pattern;

    ActivityTestType(String name, Pattern pattern) {
        this.name = name;
        this.pattern = pattern;
    }

    public String getName() {
        return name;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public static ActivityTestType byName(String name) {
        for (ActivityTestType activityTestType : values()) {
            if (activityTestType.getName().equalsIgnoreCase(name))
                return activityTestType;
        }

        return null;
    }
}
