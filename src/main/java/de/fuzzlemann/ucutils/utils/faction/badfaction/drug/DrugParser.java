package de.fuzzlemann.ucutils.utils.faction.badfaction.drug;

import de.fuzzlemann.ucutils.utils.command.api.ParameterParser;

/**
 * @author Fuzzlemann
 */
public class DrugParser implements ParameterParser<String, Drug> {
    @Override
    public Drug parse(String input) {
        return DrugUtil.getDrug(input);
    }

    @Override
    public String errorMessage() {
        return "Die Droge wurde nicht gefunden.";
    }
}
