package de.fuzzlemann.ucutils.utils.location;

/**
 * @author Fuzzlemann
 */
public enum Job {
    POWDER_MINE(510, 63, 178, "Pulvermine", "/startmine"),
    REFUSE_COLLECTOR(510, 63, 178, "M\u00fcllmann", "/m\u00fcllmann"),
    PAPER_TRANSPORT(464, 64, 421, "Papiertransport", "/starttransport"),
    LUMBERJACK(433, 64, 420, "Holzf\u00e4ller", "/startwood"),
    MINE(-277, 69, 571, "Bergarbeiter", "/startstone"),
    DRINK_TRANSPORT(165, 69, -144, "Getr\u00e4nketransport", "/startdelivery"),
    TRANSPORT(-391, 69, 67, "Transport", "/starttransport"),
    FARMER(520, 67, 560, "Farmer", "/farmer"),
    MONEY_TRANSPORT(-70, 69, -348, "Geldtransport", "/geldtransport"),
    NEWSBOY(-104, 71, -364, "Zeitungsjunge", "/zeitungsjunge"),
    TOBACCO_PLANTATION(406, 64, 633, "Tabakplantage", "/tabakplantage"),
    PIZZA_SUPPLIER(260, 69, 67, "Pizzalieferant", "/pizzalieferant"),
    SEA_FISHING(-504, 63, 198, "Hochseefischer", "/fischer");

    private final int x;
    private final int y;
    private final int z;
    private final String name;
    private final String command;

    Job(int x, int y, int z, String name, String command) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
        this.command = command;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public String getName() {
        return name;
    }

    public String getCommand() {
        return command;
    }
}
