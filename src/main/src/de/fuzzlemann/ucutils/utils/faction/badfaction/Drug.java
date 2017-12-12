package de.fuzzlemann.ucutils.utils.faction.badfaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author Fuzzlemann
 */
public class Drug {

    public static final List<Drug> DRUGS = new ArrayList<>();

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

    public static Drug getDrug(String name) {
        for (Drug drug : Drug.DRUGS) {
            if (drug.getName().equalsIgnoreCase(name))
                return drug;

            for (String alternativeName : drug.getAlternative()) {
                if (alternativeName.equalsIgnoreCase(name))
                    return drug;
            }
        }

        return null;
    }

    public String getName() {
        return name;
    }

    public String[] getAlternative() {
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