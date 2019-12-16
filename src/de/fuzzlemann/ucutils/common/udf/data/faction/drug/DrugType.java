package de.fuzzlemann.ucutils.common.udf.data.faction.drug;

/**
 * @author Fuzzlemann
 */
public enum DrugType {

    COCAINE("Kokain"),
    MARIHUANA("Marihuana"),
    OPIUM("Opium"),
    METH("Methamphetamin"),
    LSD("LSD"),
    MEDICINE("Medizin"),
    GUNPOWDER("Schwarzpulver"),
    IRON("Eisen"),
    MASKS("Maske");

    private final String name;

    DrugType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
