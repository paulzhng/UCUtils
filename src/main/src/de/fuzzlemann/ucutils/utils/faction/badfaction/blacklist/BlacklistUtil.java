package de.fuzzlemann.ucutils.utils.faction.badfaction.blacklist;

import de.fuzzlemann.ucutils.utils.ForgeUtils;
import de.fuzzlemann.ucutils.utils.data.DataLoader;
import de.fuzzlemann.ucutils.utils.data.DataModule;
import de.fuzzlemann.ucutils.utils.io.JsonManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    private static void addBlacklistReason(BlacklistReason BlacklistReason, List<BlacklistReason> BlacklistReasons) {
        if (!BlacklistReasons.contains(BlacklistReason))
            BlacklistReasons.add(BlacklistReason);
    }

    public static BlacklistReason getBlacklistReason(String reason) {
        reason = reason.replace('-', ' ');

        return ForgeUtils.getMostMatching(BLACKLIST_REASONS, reason, BlacklistReason::getReason);
    }

    @Override
    public void load() {
        BLACKLIST_REASONS.clear();

        List<BlacklistReason> blacklistReasons = JsonManager.loadObjects(BLACKLIST_REASON_FILE, BlacklistReason.class)
                .stream()
                .map(object -> (BlacklistReason) object)
                .distinct()
                .collect(Collectors.toList());

        addBlacklistReason(new BlacklistReason("Mord eines Fraktionsleaders"), blacklistReasons);
        addBlacklistReason(new BlacklistReason("Verrat von Plantagen"), blacklistReasons);
        addBlacklistReason(new BlacklistReason("Verrat von illegalen Geschäften"), blacklistReasons);
        addBlacklistReason(new BlacklistReason("Rufmord an der Fraktion"), blacklistReasons);
        addBlacklistReason(new BlacklistReason("Operationen im Hoheitsgebiet einer Fraktion"), blacklistReasons);
        addBlacklistReason(new BlacklistReason("Gangzone"), blacklistReasons);
        addBlacklistReason(new BlacklistReason("Fraktionsschädigung"), blacklistReasons);
        addBlacklistReason(new BlacklistReason("Fraktionsverrat"), blacklistReasons);

        BLACKLIST_REASONS.addAll(blacklistReasons);
    }
}
