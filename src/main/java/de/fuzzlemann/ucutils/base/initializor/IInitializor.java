package de.fuzzlemann.ucutils.base.initializor;

/**
 * @author Fuzzlemann
 */
public interface IInitializor {

    void init() throws Exception;

    default Initializor getAnnotation() {
        return this.getClass().getAnnotation(Initializor.class);
    }
}
