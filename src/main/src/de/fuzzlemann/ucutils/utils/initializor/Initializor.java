package de.fuzzlemann.ucutils.utils.initializor;

import java.lang.annotation.*;

/**
 * @author Fuzzlemann
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface Initializor {

    String value();

    InitMode initMode();

}
