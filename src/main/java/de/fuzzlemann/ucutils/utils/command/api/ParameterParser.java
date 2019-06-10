package de.fuzzlemann.ucutils.utils.command.api;

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
