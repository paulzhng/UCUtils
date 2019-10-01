package de.fuzzlemann.ucutils.teamspeak.exceptions;

/**
 * @author Fuzzlemann
 */
public class ClientQueryListenerDeclarationException extends ClientQueryException {
    public ClientQueryListenerDeclarationException() {
        super();
    }

    public ClientQueryListenerDeclarationException(String message) {
        super(message);
    }

    public ClientQueryListenerDeclarationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientQueryListenerDeclarationException(Throwable cause) {
        super(cause);
    }
}
