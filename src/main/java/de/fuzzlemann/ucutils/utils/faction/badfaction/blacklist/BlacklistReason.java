package de.fuzzlemann.ucutils.utils.faction.badfaction.blacklist;

import de.fuzzlemann.ucutils.utils.command.ParameterParser;

import java.util.Objects;

/**
 * @author Fuzzlemann
 */
@ParameterParser.At(BlacklistParser.class)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlacklistReason that = (BlacklistReason) o;
        return Objects.equals(reason, that.reason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reason);
    }
}
