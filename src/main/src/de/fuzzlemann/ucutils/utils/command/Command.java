package de.fuzzlemann.ucutils.utils.command;

import java.lang.annotation.*;

/**
 * @author Fuzzlemann
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    String[] labels();

    String usage() default "";
}
