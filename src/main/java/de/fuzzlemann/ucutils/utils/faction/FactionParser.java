package de.fuzzlemann.ucutils.utils.faction;

import de.fuzzlemann.ucutils.base.command.ParameterParser;

/**
 * @author Fuzzlemann
 */
public class FactionParser implements ParameterParser<String, Faction> {
    @Override
    public Faction parse(String input) {
        return Faction.byShortName(input);
    }

    @Override
    public String errorMessage() {
        return "Die Fraktion wurde nicht gefunden";
    }
}
