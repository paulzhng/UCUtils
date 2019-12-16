package de.fuzzlemann.ucutils.common.udf.data.misc.cape;

import com.google.gson.annotations.Expose;

import java.util.UUID;

/**
 * @author Fuzzlemann
 */
public class UDFCape {
    @Expose
    private UUID uuid;
    @Expose
    private String type;

    public UDFCape() {
    }

    public UDFCape(UUID uuid, String type) {
        this.uuid = uuid;
        this.type = type;
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
