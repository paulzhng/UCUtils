package de.fuzzlemann.ucutils.utils.police;

/**
 * @author Fuzzlemann
 */
public class Wanted {

    private final String reason;
    private final int amount;

    public Wanted(String reason, int amount) {
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
