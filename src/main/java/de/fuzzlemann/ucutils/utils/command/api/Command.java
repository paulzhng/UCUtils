package de.fuzzlemann.ucutils.utils.command.api;

import java.lang.annotation.*;

/**
 * @author Fuzzlemann
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Command {

    /**
     * Returns the labels which the command are triggered by
     *
     * @return the labels which the command are triggered by
     */
    String[] value();

    /**
     * Returns the correct usage of the command ({@code %label%} is substituted with the label of the command the player used).
     * <p>
     * The usage is send to the player if {@code false} is returned by onCommand(EntityPlayerSP, ...)
     *
     * @return the correct usage of the command
     */
    String usage() default "/%label%";

    /**
     * Returns the usage when these {@link Throwable}s or a subclass of those are thrown.
     *
     * @return the {@link Class}es of the {@link Throwable}s
     */
    Class<? extends Throwable>[] sendUsageOn() default {};

    /**
     * Shows if the command should be executed in a separate {@link Thread}.
     * <b>if false:</b> normal command behavior
     * <p>
     * <b>if true:</b>  command is executed in a separate {@link Thread}
     *
     * @return if the command should be executed asynchronously
     */
    boolean async() default false;

    /**
     * Shows if this command is destined to only be used for management purposes.
     * <p>
     * <b>if false:</b> normal command behavior
     * <p>
     * <b>if true:</b>  only admins (currently Fuzzlemann) can use the command
     *
     * @return if the command is a management one
     */
    boolean management() default false;

}
