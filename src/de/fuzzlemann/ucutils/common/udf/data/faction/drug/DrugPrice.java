package de.fuzzlemann.ucutils.common.udf.data.faction.drug;

import javax.persistence.*;
import java.util.Map;

/**
 * @author Fuzzlemann
 */
@Entity
public class DrugPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String faction;
    @ElementCollection
    @MapKeyColumn(name = "drugType")
    @MapKeyEnumerated
    private Map<DrugType, Integer> prices;

    public DrugPrice() {
    }

    public DrugPrice(String faction, Map<DrugType, Integer> prices) {
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

    public Map<DrugType, Integer> getPrices() {
        return prices;
    }

    public void setPrices(Map<DrugType, Integer> prices) {
        this.prices = prices;
    }
}
