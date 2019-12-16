package de.fuzzlemann.ucutils.common.udf.data.faction.blacklist;

import com.google.gson.annotations.Expose;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

/**
 * @author Fuzzlemann
 */
@Entity
public class BlacklistReason {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Expose
    private String reason;
    @Expose
    private int kills;
    @Expose
    private int amount;

    public BlacklistReason() {
    }

    public BlacklistReason(String reason, int kills, int amount) {
        this.reason = reason;
        this.kills = kills;
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

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlacklistReason that = (BlacklistReason) o;
        return id == that.id &&
                kills == that.kills &&
                amount == that.amount &&
                Objects.equals(reason, that.reason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reason, kills, amount);
    }
}
