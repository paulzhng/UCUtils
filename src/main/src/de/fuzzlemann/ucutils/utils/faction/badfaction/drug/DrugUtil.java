package de.fuzzlemann.ucutils.utils.faction.badfaction.drug;

import de.fuzzlemann.ucutils.utils.io.JsonManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fuzzlemann
 */
public class DrugUtil {

    public static final List<Drug> DRUGS = new ArrayList<>();
    private static final File DRUG_PRICE_FILE = new File(JsonManager.DIRECTORY, "drugprices.storage");

    public static void loadDrugs() {
        DRUGS.clear();

        List<Drug> drugs = JsonManager.loadObjects(DRUG_PRICE_FILE, Drug.class)
                .stream()
                .map(object -> (Drug) object)
                .distinct()
                .collect(Collectors.toList());

        addDrug(new Drug("Kokain", new String[]{"Koks"}, true), drugs);
        addDrug(new Drug("Marihuana", new String[]{"Gras", "Weed", "Hanf"}, true), drugs);
        addDrug(new Drug("Opium", new String[]{}, true), drugs);
        addDrug(new Drug("Methamphetamin", new String[]{"Meth", "Speed", "Crystal"}, true), drugs);
        addDrug(new Drug("LSD", new String[]{"Acid"}, true), drugs);
        addDrug(new Drug("Medizin", new String[]{"Med"}, false), drugs);
        addDrug(new Drug("Schwarzpulver", new String[]{}, false), drugs);
        addDrug(new Drug("Eisen", new String[]{"Iron"}, false), drugs);
        addDrug(new Drug("Masken", new String[]{"Maske", "Mask", "Masks"}, false), drugs);

        DRUGS.addAll(drugs);
    }

    public static void savePrices() {
        JsonManager.writeList(DRUG_PRICE_FILE, DRUGS);
    }

    private static void addDrug(Drug drug, List<Drug> drugs) {
        if (!drugs.contains(drug))
            drugs.add(drug);
    }

    public static Drug getDrug(String name) {
        for (Drug drug : DRUGS) {
            if (drug.getName().equalsIgnoreCase(name)) return drug;

            for (String alternativeName : drug.getAlternative()) {
                if (alternativeName.equalsIgnoreCase(name))
                    return drug;
            }
        }

        return null;
    }
}
