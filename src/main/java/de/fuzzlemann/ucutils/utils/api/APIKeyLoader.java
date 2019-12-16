package de.fuzzlemann.ucutils.utils.api;

import de.fuzzlemann.ucutils.base.udf.UDFLoader;
import de.fuzzlemann.ucutils.base.udf.UDFModule;
import de.fuzzlemann.ucutils.common.udf.DataRegistry;
import de.fuzzlemann.ucutils.config.UCUtilsConfig;

/**
 * @author Fuzzlemann
 */
@UDFModule(value = DataRegistry.API_KEY, version = 1)
public class APIKeyLoader implements UDFLoader<String> {

    @Override
    public void supply(String apiKey) {
        UCUtilsConfig.apiKey = apiKey;
        UCUtilsConfig.onConfigChange(null);
    }
}
