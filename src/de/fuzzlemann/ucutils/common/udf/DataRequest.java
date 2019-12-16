package de.fuzzlemann.ucutils.common.udf;

/**
 * @author Fuzzlemann
 */
public class DataRequest {

    private final String name;
    private final int version;

    public DataRequest(String name, int version) {
        this.name = name;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public int getVersion() {
        return version;
    }
}
