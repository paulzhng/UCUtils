package de.fuzzlemann.ucutils.common.udf.data.misc.info;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.util.Map;

/**
 * @author Fuzzlemann
 */
@Entity
public class UDFInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    @Expose
    @OneToOne(cascade = CascadeType.ALL)
    private UDFCommandInfo udfCommandInfo;
    @Expose
    @MapKeyColumn(name = "faction")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Map<String, UDFFactionInfo> udfFactionInfos;

    public UDFInfo() {
    }

    public UDFInfo(UDFCommandInfo udfCommandInfo, Map<String, UDFFactionInfo> udfFactionInfos) {
        this.udfCommandInfo = udfCommandInfo;
        this.udfFactionInfos = udfFactionInfos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UDFCommandInfo getUdfCommandInfo() {
        return udfCommandInfo;
    }

    public void setUdfCommandInfo(UDFCommandInfo udfCommandInfo) {
        this.udfCommandInfo = udfCommandInfo;
    }

    public Map<String, UDFFactionInfo> getUdfFactionInfos() {
        return udfFactionInfos;
    }

    public void setUdfFactionInfos(Map<String, UDFFactionInfo> udfFactionInfos) {
        this.udfFactionInfos = udfFactionInfos;
    }
}
