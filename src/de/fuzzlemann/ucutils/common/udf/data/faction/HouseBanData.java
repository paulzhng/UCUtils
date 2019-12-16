package de.fuzzlemann.ucutils.common.udf.data.faction;

import de.fuzzlemann.ucutils.common.udf.Data;
import de.fuzzlemann.ucutils.common.udf.DataRegistry;

import java.util.List;

/**
 * @author Fuzzlemann
 */
public class HouseBanData extends Data<List<String>> {
    public HouseBanData(List<String> houseBans) {
        super(DataRegistry.HOUSE_BANS, 1, houseBans);
    }
}
