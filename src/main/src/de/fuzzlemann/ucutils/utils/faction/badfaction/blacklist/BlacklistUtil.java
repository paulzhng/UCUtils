package de.fuzzlemann.ucutils.utils.faction.badfaction.blacklist;

import de.fuzzlemann.ucutils.utils.io.JsonManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fuzzlemann
 */
public class BlacklistUtil {

    public static final List<BlacklistReason> BLACKLIST_REASONS = new ArrayList<>();
    private static final File BLACKLIST_REASON_FILE = new File(JsonManager.DIRECTORY, "blacklistreasons.storage");

    public static void loadBlacklistReasons() {
        BLACKLIST_REASONS.clear();

        List<BlacklistReason> blacklistReasons = JsonManager.loadObjects(BLACKLIST_REASON_FILE, BlacklistReason.class)
                .stream()
                .map(object -> (BlacklistReason) object)
                .distinct()
                .collect(Collectors.toList());

        addBlacklistReason(new BlacklistReason("Mord eines Fraktionsleaders"), blacklistReasons);
        addBlacklistReason(new BlacklistReason("Verrat von Plantagen"), blacklistReasons);
        addBlacklistReason(new BlacklistReason("Verrat von illegalen Gesch\u00e4ften"), blacklistReasons);
        addBlacklistReason(new BlacklistReason("Rufmord an der Fraktion"), blacklistReasons);
        addBlacklistReason(new BlacklistReason("Operationen im Hoheitsgebiet einer Fraktion"), blacklistReasons);
        addBlacklistReason(new BlacklistReason("Gangzone"), blacklistReasons);
        addBlacklistReason(new BlacklistReason("Fraktionssch\u00e4digung"), blacklistReasons);
        addBlacklistReason(new BlacklistReason("Fraktionsverrat"), blacklistReasons);

        BLACKLIST_REASONS.addAll(blacklistReasons);
    }

    public static void savePrices() {
        JsonManager.writeList(BLACKLIST_REASON_FILE, BLACKLIST_REASONS);
    }

    private static void addBlacklistReason(BlacklistReason BlacklistReason, List<BlacklistReason> BlacklistReasons) {
        if (!BlacklistReasons.contains(BlacklistReason))
            BlacklistReasons.add(BlacklistReason);
    }

    public static BlacklistReason getBlacklistReason(String reason) {
        String finalReason = reason.replace('-', ' ');

        return BLACKLIST_REASONS.stream()
                .filter(blacklistReason -> blacklistReason.getReason().equalsIgnoreCase(finalReason))
                .findFirst()
                .orElse(null);
    }
}
