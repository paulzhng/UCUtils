package de.fuzzlemann.ucutils.teamspeak.exceptions;

public class ClientQueryFutureException extends ClientQueryException {
    public ClientQueryFutureException() {
        super();
    }

    public ClientQueryFutureException(String message) {
        super(message);
    }

    public ClientQueryFutureException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientQueryFutureException(Throwable cause) {
        super(cause);
    }
}
