package de.fuzzlemann.ucutils.common.activity;

/**
 * @author Fuzzlemann
 */
public enum ActivityTestType {

    EMERGENCY_LINE("Notruf"),
    REINFORCEMENT("Reinforcement"),
    ARREST("Verhaftungen"),
    KILLS("Kills"),
    LARGE_OPERATION("Großeinsatz"),
    OTHER("Andere"),
    EQUIP("Equip");

    private final String name;

    ActivityTestType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ActivityTestType byName(String name) {
        for (ActivityTestType activityTestType : values()) {
            if (activityTestType.getName().equalsIgnoreCase(name))
                return activityTestType;
        }

        return null;
    }
}
