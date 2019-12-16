package de.fuzzlemann.ucutils.utils.house;

import de.fuzzlemann.ucutils.base.command.ParameterParser;
import de.fuzzlemann.ucutils.common.udf.data.misc.house.House;

/**
 * @author Fuzzlemann
 */
public class HouseParser implements ParameterParser<Integer, House> {
    @Override
    public House parse(Integer input) {
        for (House house : HouseUtil.HOUSES) {
            if (house.getHouseNumber() == input) return house;
        }

        return null;
    }

    @Override
    public String errorMessage() {
        return "Das Haus wurde nicht gefunden.";
    }
}
