package de.fuzzlemann.ucutils.utils.command.execution.objectmapper;

import de.fuzzlemann.ucutils.utils.command.ParameterParser;

/**
 * @author Fuzzlemann
 */
public class GeneralParser implements ParameterParser<String, GeneralTestObject> {

    @Override
    public GeneralTestObject parse(String input) {
        return new GeneralTestObject(input);
    }
}
