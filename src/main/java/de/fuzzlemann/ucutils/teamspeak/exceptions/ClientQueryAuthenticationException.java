package de.fuzzlemann.ucutils.teamspeak.exceptions;

/**
 * @author Fuzzlemann
 */
public class ClientQueryAuthenticationException extends ClientQueryConnectionException {
    public ClientQueryAuthenticationException() {
        super();
    }

    public ClientQueryAuthenticationException(String message) {
        super(message);
    }

    public ClientQueryAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientQueryAuthenticationException(Throwable cause) {
        super(cause);
    }
}
