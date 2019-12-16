package de.fuzzlemann.ucutils.common.udf.data.faction.drug;

import de.fuzzlemann.ucutils.common.udf.Data;
import de.fuzzlemann.ucutils.common.udf.DataRegistry;

/**
 * @author Fuzzlemann
 */
public class DrugPriceData extends Data<DrugPrice> {

    public DrugPriceData(DrugPrice drugPrice) {
        super(DataRegistry.DRUG_PRICE, 1, drugPrice);
    }

}
