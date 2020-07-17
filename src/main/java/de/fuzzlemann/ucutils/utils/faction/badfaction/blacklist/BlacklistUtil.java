package de.fuzzlemann.ucutils.utils.faction.badfaction.blacklist;

import de.fuzzlemann.ucutils.base.udf.UDFLoader;
import de.fuzzlemann.ucutils.base.udf.UDFModule;
import de.fuzzlemann.ucutils.common.udf.DataRegistry;
import de.fuzzlemann.ucutils.common.udf.data.faction.blacklist.BlacklistReason;
import de.fuzzlemann.ucutils.common.udf.data.faction.blacklist.BlacklistReasons;
import de.fuzzlemann.ucutils.utils.ForgeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fuzzlemann
 */
@UDFModule(value = DataRegistry.BLACKLIST_REASONS, version = 1)
public class BlacklistUtil implements UDFLoader<BlacklistReasons> {

    public static final List<BlacklistReason> BLACKLIST_REASONS = new ArrayList<>();

    public static BlacklistReason getBlacklistReason(String reason) {
        reason = reason.replace('-', ' ');

        return ForgeUtils.getMostMatching(BLACKLIST_REASONS, reason, BlacklistReason::getReason);
    }

    @Override
    public void supply(BlacklistReasons blacklistReasons) {
        BLACKLIST_REASONS.addAll(blacklistReasons.getReasons());
    }

    @Override
    public void cleanUp() {
        BLACKLIST_REASONS.clear();
    }
}
