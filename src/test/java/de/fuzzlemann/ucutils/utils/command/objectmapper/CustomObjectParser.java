package de.fuzzlemann.ucutils.utils.command.objectmapper;

import de.fuzzlemann.ucutils.utils.command.api.ParameterParser;

/**
 * @author Fuzzlemann
 */
public class CustomObjectParser implements ParameterParser<DeclaredTestObject, GeneralTestObject> {
    @Override
    public GeneralTestObject parse(DeclaredTestObject input) {
        if (input.getString() == null) return null;

        return new GeneralTestObject(input.getString());
    }
}
