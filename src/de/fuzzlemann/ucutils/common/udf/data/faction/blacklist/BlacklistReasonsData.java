package de.fuzzlemann.ucutils.common.udf.data.faction.blacklist;

import de.fuzzlemann.ucutils.common.udf.Data;
import de.fuzzlemann.ucutils.common.udf.DataRegistry;

/**
 * @author Fuzzlemann
 */
public class BlacklistReasonsData extends Data<BlacklistReasons> {

    public BlacklistReasonsData(BlacklistReasons blacklistReasons) {
        super(DataRegistry.BLACKLIST_REASONS, 1, blacklistReasons);
    }

}
