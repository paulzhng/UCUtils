package de.fuzzlemann.ucutils.base.udf;

import java.lang.annotation.*;

/**
 * @author Fuzzlemann
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface UDFModule {

    String value();

    int version();

}
