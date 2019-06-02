package de.fuzzlemann.ucutils.utils.initializor;

/**
 * @author Fuzzlemann
 */
public interface IInitializor {

    void init() throws Exception;

    default Initializor getAnnotation() {
        return this.getClass().getAnnotation(Initializor.class);
    }
}
