package de.fuzzlemann.ucutils.common.udf.data.faction.drug;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.util.List;

/**
 * @author Fuzzlemann
 */
@Entity
public class DrugPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String faction;
    @Expose
    @OneToMany(cascade = CascadeType.ALL)
    private List<DrugPriceEntry> prices;

    public DrugPrice() {
    }

    public DrugPrice(String faction, List<DrugPriceEntry> prices) {
        this.faction = faction;
        this.prices = prices;
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

    public List<DrugPriceEntry> getPrices() {
        return prices;
    }

    public void setPrices(List<DrugPriceEntry> prices) {
        this.prices = prices;
    }
}
