package de.fuzzlemann.ucutils.common.udf.data.personal;

import de.fuzzlemann.ucutils.common.udf.Data;
import de.fuzzlemann.ucutils.common.udf.DataRegistry;

/**
 * @author Fuzzlemann
 */
public class FactionData extends Data<String> {
    public FactionData(String factionName) {
        super(DataRegistry.FACTION, 1, factionName);
    }
}
