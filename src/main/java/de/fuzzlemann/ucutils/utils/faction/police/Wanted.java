package de.fuzzlemann.ucutils.utils.faction.police;

/**
 * @author Fuzzlemann
 */
public class Wanted {

    private String reason;
    private int amount;

    public Wanted(String reason, int amount) {
        this.reason = reason;
        this.amount = amount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
