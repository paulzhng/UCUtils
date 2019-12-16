package de.fuzzlemann.ucutils.common.udf.data.faction.blacklist;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.util.Set;

/**
 * @author Fuzzlemann
 */
@Entity
public class BlacklistPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String faction;
    @OneToMany(cascade = CascadeType.ALL)
    @Expose
    private Set<BlacklistReason> reasons;

    public BlacklistPrice() {
    }

    public BlacklistPrice(String faction, Set<BlacklistReason> reasons) {
        this.faction = faction;
        this.reasons = reasons;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFaction() {
        return faction;
    }

    public void setFaction(String faction) {
        this.faction = faction;
    }

    public Set<BlacklistReason> getReasons() {
        return reasons;
    }

    public void setReasons(Set<BlacklistReason> reasons) {
        this.reasons = reasons;
    }
}
