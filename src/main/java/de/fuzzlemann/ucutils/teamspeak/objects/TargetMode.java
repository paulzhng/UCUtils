package de.fuzzlemann.ucutils.teamspeak.objects;

/**
 * @author Fuzzlemann
 */
public enum TargetMode {

    PRIVATE(1),
    CHANNEL(2),
    SERVER(3);

    private final int id;

    TargetMode(int id) {
        this.id = id;
    }

    public static TargetMode byID(int id) {
        for (TargetMode targetMode : TargetMode.values()) {
            if (targetMode.getID() == id) return targetMode;
        }

        return null;
    }

    public int getID() {
        return id;
    }
}
