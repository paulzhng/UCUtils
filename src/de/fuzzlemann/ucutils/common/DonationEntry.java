package de.fuzzlemann.ucutils.common;

/**
 * @author Fuzzlemann
 */
public class DonationEntry {

    private final String name;
    private final double amount;
    private final String message;

    public DonationEntry(String name, double amount, String message) {
        this.name = name;
        this.amount = amount;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public String getMessage() {
        return message;
    }
}
