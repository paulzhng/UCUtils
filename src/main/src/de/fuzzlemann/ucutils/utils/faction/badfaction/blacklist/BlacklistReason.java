package de.fuzzlemann.ucutils.utils.faction.badfaction.blacklist;

/**
 * @author Fuzzlemann
 */
public class BlacklistReason {

    private final String reason;
    private int price;

    public BlacklistReason(String reason) {
        this.reason = reason;
        this.price = 0;
    }

    public String getReason() {
        return reason;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
