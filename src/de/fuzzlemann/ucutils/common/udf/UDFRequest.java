package de.fuzzlemann.ucutils.common.udf;

import java.util.List;

/**
 * @author Fuzzlemann
 */
public class UDFRequest {

    private final AuthHash authHash;
    private final String apiKey;
    private final List<DataRequest> dataRequests;

    public UDFRequest(AuthHash authHash, String apiKey, List<DataRequest> dataRequests) {
        this.authHash = authHash;
        this.apiKey = apiKey;
        this.dataRequests = dataRequests;
    }

    public AuthHash getAuthHash() {
        return authHash;
    }

    public String getApiKey() {
        return apiKey;
    }

    public List<DataRequest> getDataRequests() {
        return dataRequests;
    }
}
