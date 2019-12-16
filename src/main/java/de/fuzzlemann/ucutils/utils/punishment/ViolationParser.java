package de.fuzzlemann.ucutils.utils.punishment;

import de.fuzzlemann.ucutils.base.command.ParameterParser;
import de.fuzzlemann.ucutils.common.udf.data.supporter.violation.Violation;

/**
 * @author Fuzzlemann
 */
public class ViolationParser implements ParameterParser<String, Violation> {
    @Override
    public Violation parse(String input) {
        return PunishManager.getViolation(input.replace('-', ' '));
    }
}
