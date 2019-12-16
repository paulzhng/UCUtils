package de.fuzzlemann.ucutils.common.udf.data.personal;

import de.fuzzlemann.ucutils.common.udf.Data;
import de.fuzzlemann.ucutils.common.udf.DataRegistry;

/**
 * @author Fuzzlemann
 */
public class APIKeyData extends Data<String> {
    public APIKeyData(String apiKey) {
        super(DataRegistry.API_KEY, 1, apiKey);
    }
}
