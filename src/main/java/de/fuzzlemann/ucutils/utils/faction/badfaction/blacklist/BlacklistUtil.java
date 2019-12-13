package de.fuzzlemann.ucutils.utils.faction.badfaction.blacklist;

import de.fuzzlemann.ucutils.utils.ForgeUtils;
import de.fuzzlemann.ucutils.base.data.DataLoader;
import de.fuzzlemann.ucutils.base.data.DataModule;
import de.fuzzlemann.ucutils.utils.io.JsonManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Fuzzlemann
 */
@DataModule(value = "Blacklist", local = true)
public class BlacklistUtil implements DataLoader {

    public static final List<BlacklistReason> BLACKLIST_REASONS = new ArrayList<>();
    private static final File BLACKLIST_REASON_FILE = new File(JsonManager.DIRECTORY, "blacklistreasons.storage");

    public static void savePrices() {
        JsonManager.writeList(BLACKLIST_REASON_FILE, BLACKLIST_REASONS);
    }

    public static BlacklistReason getBlacklistReason(String reason) {
        reason = reason.replace('-', ' ');

        return ForgeUtils.getMostMatching(BLACKLIST_REASONS, reason, BlacklistReason::getReason);
    }

    @Override
    public void load() {
        BLACKLIST_REASONS.clear();

        Set<BlacklistReason> blacklistReasons = new HashSet<>(JsonManager.loadObjects(BLACKLIST_REASON_FILE, BlacklistReason.class));

        blacklistReasons.add(new BlacklistReason("Leadermord"));
        blacklistReasons.add(new BlacklistReason("Rufmord"));
        blacklistReasons.add(new BlacklistReason("Gangzone"));
        blacklistReasons.add(new BlacklistReason("Fraktionsschädigung"));
        blacklistReasons.add(new BlacklistReason("Fraktionsverrat"));

        BLACKLIST_REASONS.addAll(blacklistReasons);
    }
}
