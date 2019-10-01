package de.fuzzlemann.ucutils.teamspeak;

import java.util.concurrent.CompletableFuture;

/**
 * @author Fuzzlemann
 */
public class CommandFuture<T> extends CompletableFuture<T> {
    @Override
    public T get() {
        try {
            return super.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean complete(Object commandResponse) {
        return super.complete((T) commandResponse);
    }
}
