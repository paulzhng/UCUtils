package de.fuzzlemann.ucutils.base.command;

import java.lang.annotation.*;

/**
 * @author Fuzzlemann
 */
public interface ParameterParser<I, T> {

    T parse(I input);

    default String errorMessage() {
        return null;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Inherited
    @interface At {
        Class<? extends ParameterParser> value();
    }

}
