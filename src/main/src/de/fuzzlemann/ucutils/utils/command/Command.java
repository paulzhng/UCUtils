package de.fuzzlemann.ucutils.utils.command;

import java.lang.annotation.*;

/**
 * @author Fuzzlemann
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Command {
    String[] labels();

    String usage() default "/%label%";

    boolean management() default false;
}
