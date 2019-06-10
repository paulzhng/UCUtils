package de.fuzzlemann.ucutils.utils.house;

import de.fuzzlemann.ucutils.common.House;
import de.fuzzlemann.ucutils.utils.command.api.ParameterParser;

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
