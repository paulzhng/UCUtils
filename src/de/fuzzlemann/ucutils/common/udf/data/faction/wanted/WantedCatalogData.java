package de.fuzzlemann.ucutils.common.udf.data.faction.wanted;

import de.fuzzlemann.ucutils.common.udf.Data;
import de.fuzzlemann.ucutils.common.udf.DataRegistry;

import java.util.List;

/**
 * @author Fuzzlemann
 */
public class WantedCatalogData extends Data<List<WantedReason>> {
    public WantedCatalogData(List<WantedReason> wantedList) {
        super(DataRegistry.WANTED_CATALOG, 1, wantedList);
    }
}
