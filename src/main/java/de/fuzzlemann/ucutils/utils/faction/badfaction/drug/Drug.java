package de.fuzzlemann.ucutils.utils.faction.badfaction.drug;

import de.fuzzlemann.ucutils.utils.command.api.ParameterParser;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author Fuzzlemann
 */
@ParameterParser.At(DrugParser.class)
public class Drug {

    private final String name;
    private final String[] alternative;
    private final boolean drug;
    private int price;

    public Drug(String name, String[] alternative, boolean drug) {
        this.name = name;
        this.alternative = alternative;
        this.drug = drug;
        this.price = 1;
    }

    public String getName() {
        return name;
    }

    String[] getAlternative() {
        return alternative;
    }

    public boolean isDrug() {
        return drug;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Drug drug1 = (Drug) o;
        return drug == drug1.drug &&
                Objects.equals(name, drug1.name) &&
                Arrays.equals(alternative, drug1.alternative);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}