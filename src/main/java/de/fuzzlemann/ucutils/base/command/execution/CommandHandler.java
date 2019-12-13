package de.fuzzlemann.ucutils.base.command.execution;

/**
 * @author Fuzzlemann
 */
public class CommandHandler {

    public static void issueCommand(String label, String... args) {
        new CommandIssuer(label, args, false).issue();
    }
}
