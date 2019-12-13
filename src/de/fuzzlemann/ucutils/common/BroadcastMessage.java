package de.fuzzlemann.ucutils.common;

import javax.persistence.*;

/**
 * @author Fuzzlemann
 */
@Entity
public class BroadcastMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, updatable = false, length = 19)
    private long time;
    private String soundID;
    private String message;

    public BroadcastMessage() {
    }

    public BroadcastMessage(long time, String soundID, String message) {
        this.time = time;
        this.soundID = soundID;
        this.message = message;
    }

    public int getID() {
        return id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getSoundID() {
        return soundID;
    }

    public void setSoundID(String soundID) {
        this.soundID = soundID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
