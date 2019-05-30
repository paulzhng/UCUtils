package de.fuzzlemann.ucutils.utils.command;

import net.minecraft.client.entity.EntityPlayerSP;

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
    String[] labels();

    /**
     * Returns the correct usage of the command ({@code %label%} is substituted with the label of the command the player used).
     * <p>
     * The usage is send to the player if {@code false} is returned by {@link CommandExecutor#onCommand(EntityPlayerSP, String[])}
     *
     * @return the correct usage of the command
     */
    String usage() default "/%label%";

    /**
     * Shows if this command is destined to only be used for management purposes
     * <p>
     * <b>if false:</b> normal command behavior
     * <p>
     * <b>if true:</b>  only admins (currently Fuzzlemann) can use the command
     *
     * @return if the command is a management one
     */
    boolean management() default false;
}
