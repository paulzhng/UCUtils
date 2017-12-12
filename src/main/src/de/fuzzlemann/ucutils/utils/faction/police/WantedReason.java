package de.fuzzlemann.ucutils.utils.faction.police;

/**
 * @author Fuzzlemann
 */
public class WantedReason {

    private final String reason;
    private final int amount;

    WantedReason(String reason, int amount) {
        this.reason = reason;
        this.amount = amount;
    }

    public String getReason() {
        return reason;
    }

    public int getAmount() {
        return amount;
    }
}
