package de.fuzzlemann.ucutils.common.udf.data.faction.blacklist;

import de.fuzzlemann.ucutils.common.udf.Data;
import de.fuzzlemann.ucutils.common.udf.DataRegistry;

/**
 * @author Fuzzlemann
 */
public class BlacklistPriceData extends Data<BlacklistPrice> {

    public BlacklistPriceData(BlacklistPrice blacklistPrice) {
        super(DataRegistry.BLACKLIST_PRICE, 1, blacklistPrice);
    }

}
