package de.fuzzlemann.ucutils.utils.punishment;

/**
 * @author Fuzzlemann
 */
public class Punishment {

    private final PunishmentType type;
    private final String reason;
    private final int value;
    private String command;

    public Punishment(PunishmentType type, String reason) {
        this(type, reason, 0);
    }

    public Punishment(PunishmentType type, String reason, int value) {
        this.type = type;
        this.reason = reason;
        this.value = value;
    }

    public String parseCommand(String player) {
        if (command == null)
            command = type.getCommand().replace("%reason%", reason).replace("%value%", String.valueOf(value));

        return command.replace("%player%", player);
    }

    public PunishmentType getType() {
        return type;
    }

    public String getReason() {
        return reason;
    }

    public int getValue() {
        return value;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public enum PunishmentType {
        FACTION_BAN("/fraksperre %player% %value% %reason%"),
        KICK("/kick %player% %reason%"),
        MUTE("/mute %player% 0 0 %value% %reason%"),
        WARN("/warn %player% %reason%"),
        TEMPORARY_BAN("/timeban %player% 0 0 %value% %reason%"),
        PERMANENT_BAN("/ban %player% %reason%"),
        AD_BAN("/adsperre %player% %value% %reason%"),
        WEAPONS_BAN("/waffensperre %player% 0 0 %value% %reason%"),
        CHECKPOINTS("/checkpoints %player% %value% %reason%");

        private final String command;

        PunishmentType(String command) {
            this.command = command;
        }

        public String getCommand() {
            return command;
        }
    }
}