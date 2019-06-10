package de.fuzzlemann.ucutils.common;

import com.google.gson.annotations.Expose;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author Fuzzlemann
 */
@Entity
public class House {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String owner;
    @Expose
    private int houseNumber;
    @Expose
    private int x;
    @Expose
    private int y;
    @Expose
    private int z;

    public House() {
    }

    public House(String owner, int houseNumber, int x, int y, int z) {
        this.owner = owner;
        this.houseNumber = houseNumber;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getID() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }
}
