package de.fuzzlemann.ucutils.utils.faction.badfaction.drug;

import de.fuzzlemann.ucutils.utils.data.DataLoader;
import de.fuzzlemann.ucutils.utils.data.DataModule;
import de.fuzzlemann.ucutils.utils.io.JsonManager;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Fuzzlemann
 */
@DataModule(value = "Drugs", local = true)
public class DrugUtil implements DataLoader {

    public static final Set<Drug> DRUGS = new HashSet<>();
    private static final File DRUG_PRICE_FILE = new File(JsonManager.DIRECTORY, "drugprices.storage");

    public static void savePrices() {
        JsonManager.writeList(DRUG_PRICE_FILE, DRUGS);
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

    @Override
    public void load() {
        DRUGS.clear();

        Set<Drug> drugs = new HashSet<>(JsonManager.loadObjects(DRUG_PRICE_FILE, Drug.class));

        drugs.add(new Drug("Kokain", new String[]{"Koks"}, true));
        drugs.add(new Drug("Marihuana", new String[]{"Gras", "Weed", "Hanf"}, true));
        drugs.add(new Drug("Opium", new String[]{}, true));
        drugs.add(new Drug("Methamphetamin", new String[]{"Meth", "Speed", "Crystal"}, true));
        drugs.add(new Drug("LSD", new String[]{"Acid"}, true));
        drugs.add(new Drug("Medizin", new String[]{"Med"}, false));
        drugs.add(new Drug("Schwarzpulver", new String[]{}, false));
        drugs.add(new Drug("Eisen", new String[]{"Iron"}, false));
        drugs.add(new Drug("Masken", new String[]{"Maske", "Mask", "Masks"}, false));

        DRUGS.addAll(drugs);
    }
}
