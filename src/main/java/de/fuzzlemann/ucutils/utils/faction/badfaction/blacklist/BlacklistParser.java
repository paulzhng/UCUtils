package de.fuzzlemann.ucutils.utils.faction.badfaction.blacklist;

import de.fuzzlemann.ucutils.base.command.ParameterParser;
import de.fuzzlemann.ucutils.common.udf.data.faction.blacklist.BlacklistReason;

/**
 * @author Fuzzlemann
 */
public class BlacklistParser implements ParameterParser<String, BlacklistReason> {
    @Override
    public BlacklistReason parse(String input) {
        return BlacklistUtil.getBlacklistReason(input);
    }

    @Override
    public String errorMessage() {
        return "Der Blacklistgrund wurde nicht gefunden.";
    }
}
