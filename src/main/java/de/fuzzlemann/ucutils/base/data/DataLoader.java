package de.fuzzlemann.ucutils.base.data;

/**
 * @author Fuzzlemann
 */
public interface DataLoader {

    void load() throws Exception;

    default void fallbackLoading() throws Exception {
    }
}
