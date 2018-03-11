package de.fuzzlemann.ucutils.utils.faction.badfaction;

import de.fuzzlemann.ucutils.utils.io.JsonManager;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fuzzlemann
 */
public class DrugUtil {

    private static final File DRUG_PRICE_FILE = new File(JsonManager.DIRECTORY, "drugprices.storage");

    public static void loadDrugs() {
        Drug.DRUGS.clear();

        List<Drug> drugs = JsonManager.loadObjects(DRUG_PRICE_FILE, Drug.class)
                .stream()
                .map(object -> (Drug) object)
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

        Drug.DRUGS.addAll(drugs);
    }

    public static void savePrices() {
        JsonManager.writeList(DRUG_PRICE_FILE, Drug.DRUGS);
    }

    private static void addDrug(Drug drug, List<Drug> drugs) {
        if (!drugs.contains(drug))
            drugs.add(drug);
    }
}
