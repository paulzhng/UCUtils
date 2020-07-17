package de.fuzzlemann.ucutils.common.udf.data.faction.drug;

public enum DrugQuality {

    HIGH(0, "hochwertig"),
    GOOD(1, "gut"),
    MEDIUM(2, "mittel"),
    BAD(3, "schlecht");

    private final int id;
    private final String name;

    DrugQuality(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
