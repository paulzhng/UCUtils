package de.fuzzlemann.ucutils.utils.command.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotation used for defining the parameters of the command.
 *
 * @author Fuzzlemann
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface CommandParam {

    /**
     * The field which represents {@code null} in a {@link String}.<br>
     * The content of this field should not be replicable in Minecraft as the given {@link String} contains {@code UTF NULL}.
     */
    String NULL = "\u0000 UCUtils_CommandParam null value \u0000";

    /**
     * Shows if the argument is required.
     *
     * @return if the argument is required
     */
    boolean required() default true;

    /**
     * The default value when no argument was given.<br>
     * When set to {@link #NULL}, {@code null} is passed on to the command when no argument was given.
     *
     * @return the default value
     */
    String defaultValue() default "";

    /**
     * Returns the required {@link String} which needs to be present.
     *
     * @return the required {@link String} so that that parameter returns true
     * @apiNote only needed when the parameter is a {@link Boolean} type
     */
    String requiredValue() default "";

    /**
     * Shows if the argument is the start of a joining {@link String}.
     * Can be used to pass a joined {@link String} to another {@link Object} aswell.
     *
     * @return if the argument is the start of a joining {@link String}
     */
    boolean joinStart() default false;

    /**
     * The {@link String} the arguments after (and including) the parameter where {@link #joinStart()} is {@code true} is joined with.<br>
     *
     * @return the {@link String} which is used for joining
     */
    String joiner() default " ";

    /**
     * Shows if the argument is the start of an Array
     *
     * @return if the argument is the start of an Array
     */
    boolean arrayStart() default false;

    /**
     * Optional {@link ParameterParser} which parses the argument.<br>
     * This is needed when the parameter is not a {@link String}, a {@link Boolean}, or a {@link Integer} <b>and</b> no default {@link ParameterParser} is defined.
     *
     * @return the {@link ParameterParser} which parses the argument
     */
    Class<? extends ParameterParser> parameterParser() default ParameterParser.class;
}
