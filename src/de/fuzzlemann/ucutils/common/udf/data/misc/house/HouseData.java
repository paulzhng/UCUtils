package de.fuzzlemann.ucutils.common.udf.data.misc.house;

import de.fuzzlemann.ucutils.common.udf.Data;
import de.fuzzlemann.ucutils.common.udf.DataRegistry;

import java.util.List;

/**
 * @author Fuzzlemann
 */
public class HouseData extends Data<List<House>> {
    public HouseData(List<House> houses) {
        super(DataRegistry.HOUSES, 1, houses);
    }
}
