package de.fuzzlemann.ucutils.utils.faction;

import de.fuzzlemann.ucutils.common.activity.ActivityTestType;
import de.fuzzlemann.ucutils.utils.command.api.ParameterParser;

/**
 * @author Fuzzlemann
 */
public class ActivityTestParser implements ParameterParser<String, ActivityTestType> {
    @Override
    public ActivityTestType parse(String input) {
        return ActivityTestType.byName(input);
    }
}
