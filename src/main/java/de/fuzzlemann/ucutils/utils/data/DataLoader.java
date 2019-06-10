package de.fuzzlemann.ucutils.utils.data;

/**
 * @author Fuzzlemann
 */
public interface DataLoader {

    void load();

    default void fallbackLoading() {
    }
}
