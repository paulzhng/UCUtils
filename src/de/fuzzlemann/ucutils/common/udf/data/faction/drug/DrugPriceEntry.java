package de.fuzzlemann.ucutils.common.udf.data.faction.drug;

import com.google.gson.annotations.Expose;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class DrugPriceEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Expose
    private DrugType drugType;
    @Expose
    private DrugQuality drugQuality;
    @Expose
    private int money;

    public DrugPriceEntry() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DrugType getDrugType() {
        return drugType;
    }

    public void setDrugType(DrugType drugType) {
        this.drugType = drugType;
    }

    public DrugQuality getDrugQuality() {
        return drugQuality;
    }

    public void setDrugQuality(DrugQuality drugQuality) {
        this.drugQuality = drugQuality;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
