package de.fuzzlemann.ucutils.base.command.execution.objectmapper;

import de.fuzzlemann.ucutils.base.command.ParameterParser;

/**
 * @author Fuzzlemann
 */
public class GeneralParser implements ParameterParser<String, GeneralTestObject> {

    @Override
    public GeneralTestObject parse(String input) {
        return new GeneralTestObject(input);
    }
}
