package de.fuzzlemann.ucutils.common.udf.data.faction.wanted;

import com.google.gson.annotations.Expose;

import javax.persistence.*;

/**
 * @author Fuzzlemann
 */
@Entity
public class WantedReason implements Comparable<WantedReason> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(columnDefinition = "LONGTEXT")
    @Expose
    private String reason;
    @Expose
    private int amount;

    public WantedReason() {
    }

    public WantedReason(String reason, int amount) {
        this.reason = reason;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public int compareTo(WantedReason o) {
        return Integer.compare(o.getAmount(), this.getAmount());
    }
}
