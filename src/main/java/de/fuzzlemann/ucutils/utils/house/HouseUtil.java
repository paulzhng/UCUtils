package de.fuzzlemann.ucutils.utils.house;

import de.fuzzlemann.ucutils.base.udf.UDFLoader;
import de.fuzzlemann.ucutils.base.udf.UDFModule;
import de.fuzzlemann.ucutils.common.udf.DataRegistry;
import de.fuzzlemann.ucutils.common.udf.data.misc.house.House;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fuzzlemann
 */
@UDFModule(value = DataRegistry.HOUSES, version = 1)
public class HouseUtil implements UDFLoader<List<House>> {

    public static final List<House> HOUSES = new ArrayList<>();

    @Override
    public void supply(List<House> houses) {
        HOUSES.addAll(houses);
    }

    @Override
    public void cleanUp() {
        HOUSES.clear();
    }
}
