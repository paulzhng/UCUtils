package de.fuzzlemann.ucutils.teamspeak.exceptions;

/**
 * @author Fuzzlemann
 */
public class ClientQueryConnectionException extends ClientQueryException {
    public ClientQueryConnectionException() {
    }

    public ClientQueryConnectionException(String message) {
        super(message);
    }

    public ClientQueryConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientQueryConnectionException(Throwable cause) {
        super(cause);
    }
}
