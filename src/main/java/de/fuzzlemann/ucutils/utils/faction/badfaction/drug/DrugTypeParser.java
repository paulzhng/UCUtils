package de.fuzzlemann.ucutils.utils.faction.badfaction.drug;

import de.fuzzlemann.ucutils.base.command.ParameterParser;
import de.fuzzlemann.ucutils.common.udf.data.faction.drug.DrugType;

/**
 * @author Fuzzlemann
 */
public class DrugTypeParser implements ParameterParser<String, DrugType> {
    @Override
    public DrugType parse(String input) {
        return DrugUtil.getDrug(input);
    }

    @Override
    public String errorMessage() {
        return "Die Droge wurde nicht gefunden.";
    }
}
