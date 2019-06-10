package de.fuzzlemann.ucutils.utils.command.objectmapper;

import de.fuzzlemann.ucutils.utils.command.api.ParameterParser;

/**
 * @author Fuzzlemann
 */
public class GeneralParser implements ParameterParser<String, GeneralTestObject> {

    @Override
    public GeneralTestObject parse(String input) {
        return new GeneralTestObject(input);
    }
}
