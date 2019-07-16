package de.fuzzlemann.ucutils.common;

import javax.persistence.*;
import java.util.List;

/**
 * @author Fuzzlemann
 */
@Entity
public class CustomNaviPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ElementCollection
    private List<String> names;
    private int x;
    private int y;
    private int z;

    public CustomNaviPoint() {
    }

    public CustomNaviPoint(List<String> names, int x, int y, int z) {
        this.names = names;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getId() {
        return id;
    }

    public List<String> getNames() {
        return names;
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
