package de.fuzzlemann.ucutils.base.command.exceptions;

/**
 * @author Fuzzlemann
 */
public class ArgumentException extends RuntimeException {

    private final boolean showUsage;

    public ArgumentException(String message) {
        this(message, true);
    }

    public ArgumentException(String message, boolean showUsage) {
        super(message);
        this.showUsage = showUsage;
    }

    public boolean showUsage() {
        return showUsage;
    }
}
