package de.fuzzlemann.ucutils.utils.data;

import java.lang.annotation.*;

/**
 * @author Fuzzlemann
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface DataModule {

    String value();

    boolean local() default false;

    boolean hasFallback() default false;

    boolean test() default true;

}
