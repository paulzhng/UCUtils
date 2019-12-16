package de.fuzzlemann.ucutils.common.udf.data.misc;

import de.fuzzlemann.ucutils.common.udf.Data;
import de.fuzzlemann.ucutils.common.udf.DataRegistry;

/**
 * @author Fuzzlemann
 */
public class VersionData extends Data<String> {
    public VersionData(String version) {
        super(DataRegistry.VERSION, 1, version);
    }
}
