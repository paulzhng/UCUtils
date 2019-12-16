package de.fuzzlemann.ucutils.utils.faction;

import de.fuzzlemann.ucutils.base.command.ParameterParser;
import de.fuzzlemann.ucutils.common.activity.ActivityTestType;

/**
 * @author Fuzzlemann
 */
public class ActivityTestParser implements ParameterParser<String, ActivityTestType> {
    @Override
    public ActivityTestType parse(String input) {
        return ActivityTestType.byName(input);
    }
}
