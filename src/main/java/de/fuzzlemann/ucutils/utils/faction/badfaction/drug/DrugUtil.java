package de.fuzzlemann.ucutils.utils.faction.badfaction.drug;

import de.fuzzlemann.ucutils.base.udf.UDFLoader;
import de.fuzzlemann.ucutils.base.udf.UDFModule;
import de.fuzzlemann.ucutils.common.udf.DataRegistry;
import de.fuzzlemann.ucutils.common.udf.data.faction.drug.DrugPrice;
import de.fuzzlemann.ucutils.common.udf.data.faction.drug.DrugPriceEntry;
import de.fuzzlemann.ucutils.common.udf.data.faction.drug.DrugQuality;
import de.fuzzlemann.ucutils.common.udf.data.faction.drug.DrugType;

import java.util.Arrays;

/**
 * @author Fuzzlemann
 */
@UDFModule(value = DataRegistry.DRUG_PRICE, version = 1)
public class DrugUtil implements UDFLoader<DrugPrice> {

    public static DrugPrice drugPrice;

    public static int getPiecePrice(DrugType drugType, DrugQuality drugQuality) {
        for (DrugPriceEntry drugPriceEntry : drugPrice.getPrices()) {
            if (drugPriceEntry.getDrugType() == drugType && drugPriceEntry.getDrugQuality() == drugQuality) return drugPriceEntry.getMoney();
        }

        return 0;
    }

    public static DrugType getDrug(String name) {
        return Arrays.stream(DrugType.values()).filter(drugType -> drugType.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    @Override
    public void supply(DrugPrice drugPrice) {
        DrugUtil.drugPrice = drugPrice;
    }
}
