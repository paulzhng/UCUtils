package de.fuzzlemann.ucutils.common.udf;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fuzzlemann
 */
public class DataCollection {

    @Expose
    private String timestamp;
    @Expose
    private String version;
    @Expose
    private boolean authenticated;
    @Expose
    private List<Data<?>> data;

    public DataCollection(String timestamp, String version, boolean authenticated) {
        this.timestamp = timestamp;
        this.version = version;
        this.authenticated = authenticated;
        this.data = new ArrayList<>();
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public List<Data<?>> getData() {
        return data;
    }

    public void setData(List<Data<?>> data) {
        this.data = data;
    }
}
