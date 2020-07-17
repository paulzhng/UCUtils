package de.fuzzlemann.ucutils.base.udf.exception;

/**
 * @author Fuzzlemann
 */
public class UDFLoadingException extends Exception {
    public UDFLoadingException() {
    }

    public UDFLoadingException(String message) {
        super(message);
    }

    public UDFLoadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public UDFLoadingException(Throwable cause) {
        super(cause);
    }

    public UDFLoadingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
