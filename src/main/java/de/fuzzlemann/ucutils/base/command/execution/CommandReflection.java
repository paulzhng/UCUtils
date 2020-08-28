package de.fuzzlemann.ucutils.base.command.execution;

import de.fuzzlemann.ucutils.base.abstraction.UPlayer;
import de.fuzzlemann.ucutils.base.command.Command;
import de.fuzzlemann.ucutils.base.command.CommandParam;
import de.fuzzlemann.ucutils.base.command.exceptions.DeclarationException;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author Fuzzlemann
 */
public class CommandReflection {
    
    static Command getCommand(Object command) {
        return getCommand(getOnCommand(command));
    }

    static Command getCommand(Method onCommand) {
        return onCommand.getAnnotation(Command.class);
    }

    static Method getOnCommand(Object command) {
        Method onCommand = null;
        for (Method method : command.getClass().getMethods()) {
            if (!method.getName().equals("onCommand")) continue;
            if (!Modifier.isPublic(method.getModifiers()))
                throw new DeclarationException("onCommand() at " + command.getClass() + " is not declared public");

            onCommand = method;
            break;
        }

        if (onCommand == null)
            throw new DeclarationException(command.getClass() + " did not contain onCommand()");

        return onCommand;
    }

    /**
     * Checks if {@code onCommand} has {@link UPlayer} as its first parameter.
     *
     * @return if {@code onCommand} has {@link UPlayer} as its first parameter
     */
    static boolean hasPlayerParam(Method onCommand) {
        Class<?>[] parameterTypes = onCommand.getParameterTypes();
        if (parameterTypes.length == 0)
            return false; // no parameters at all -> UPLayer cannot be the first parameter

        return parameterTypes[0].isAssignableFrom(UPlayer.class);
    }

    /**
     * Checks if the raw unparsed arguments are wanted by {@code onCommand}.<br>
     * When a {@code String[]} parameter is present which is <b>not</b> annotated with {@link CommandParam},
     * it is considered that the raw unparsed arguments are wanted.
     *
     * @return if the raw unparsed arguments are wanted
     */
    static boolean checkDefaultUsage(Method onCommand) {
        Class<?>[] parameterTypes = onCommand.getParameterTypes();
        if (parameterTypes.length == 0)
            return false; // when no arguments are present, no String[] can be passed onto the method

        boolean containsNonAnnotatedStringArray = false;
        for (Class<?> parameterType : parameterTypes) {
            if (parameterType.isAssignableFrom(String[].class) && !parameterType.isAnnotationPresent(CommandParam.class)) { // checks if the parameter type is a String[]
                containsNonAnnotatedStringArray = true;
                continue;
            }

            if (!parameterType.isAssignableFrom(UPlayer.class)) { // UPlayer can be another valid parameter type
                return false; //no other parameter types except UPlayer and String[] can be defined when using default usage
            }
        }

        return containsNonAnnotatedStringArray;
    }
}
