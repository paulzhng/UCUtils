package de.fuzzlemann.ucutils.common.udf.data.misc.info;

import com.google.gson.annotations.Expose;

import javax.persistence.*;

/**
 * @author Fuzzlemann
 */
@Entity
public class UDFFactionInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Expose
    private String fullName;
    @Expose
    private String shortName;
    @Expose
    private boolean badFrak;
    @Expose
    private String hqPosition;
    @Expose
    private String tasks;
    @Expose
    private String factionType;
    @Expose
    private String naviPoint;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Expose
    private UDFCommandInfo commandInfo;

    public UDFFactionInfo() {
    }

    public UDFFactionInfo(String fullName, String shortName, boolean badFrak, String hqPosition, String tasks, String factionType, String naviPoint, UDFCommandInfo commandInfo) {
        this.fullName = fullName;
        this.shortName = shortName;
        this.badFrak = badFrak;
        this.hqPosition = hqPosition;
        this.tasks = tasks;
        this.factionType = factionType;
        this.naviPoint = naviPoint;
        this.commandInfo = commandInfo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public boolean isBadFrak() {
        return badFrak;
    }

    public void setBadFrak(boolean badFrak) {
        this.badFrak = badFrak;
    }

    public String getHqPosition() {
        return hqPosition;
    }

    public void setHqPosition(String hqPosition) {
        this.hqPosition = hqPosition;
    }

    public String getTasks() {
        return tasks;
    }

    public void setTasks(String tasks) {
        this.tasks = tasks;
    }

    public String getFactionType() {
        return factionType;
    }

    public void setFactionType(String factionType) {
        this.factionType = factionType;
    }

    public String getNaviPoint() {
        return naviPoint;
    }

    public void setNaviPoint(String naviPoint) {
        this.naviPoint = naviPoint;
    }

    public UDFCommandInfo getCommandInfo() {
        return commandInfo;
    }

    public void setCommandInfo(UDFCommandInfo commandInfo) {
        this.commandInfo = commandInfo;
    }
}
