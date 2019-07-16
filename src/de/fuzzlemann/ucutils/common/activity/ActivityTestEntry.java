package de.fuzzlemann.ucutils.common.activity;

import javax.persistence.*;

/**
 * @author Fuzzlemann
 */
@Entity
public class ActivityTestEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int playerID;
    private long time;
    @Enumerated(EnumType.STRING)
    private ActivityTestType type;
    private String link;
    private boolean activated;
    private long deactivateTime;

    public ActivityTestEntry() {
    }

    public ActivityTestEntry(int playerID, ActivityTestType type, String link) {
        this.playerID = playerID;
        this.time = System.currentTimeMillis();
        this.type = type;
        this.link = link;
        this.activated = true;
        this.deactivateTime = -1L;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public ActivityTestType getType() {
        return type;
    }

    public void setType(ActivityTestType type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public long getDeactivateTime() {
        return deactivateTime;
    }

    public void setDeactivateTime(long deactivateTime) {
        this.deactivateTime = deactivateTime;
    }
}
