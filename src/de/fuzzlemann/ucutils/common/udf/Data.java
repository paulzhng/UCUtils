package de.fuzzlemann.ucutils.common.udf;

import com.google.gson.annotations.Expose;

/**
 * @author Fuzzlemann
 */
public class Data<T> {

    @Expose
    private String name;
    @Expose
    private int version;
    @Expose
    private boolean permitted;
    @Expose
    private T content;

    public Data() {
    }

    public Data(String name, int version, T content) {
        this.name = name;
        this.version = version;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public int getVersion() {
        return version;
    }

    public void setPermitted(boolean permitted) {
        this.permitted = permitted;
    }

    public boolean isPermitted() {
        return permitted;
    }

    public T getContent() {
        return content;
    }
}
