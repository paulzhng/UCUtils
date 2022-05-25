package de.fuzzlemann.ucutils.commands.faction.chat;

import de.fuzzlemann.ucutils.base.abstraction.UPlayer;
import de.fuzzlemann.ucutils.base.command.Command;
import de.fuzzlemann.ucutils.base.command.CommandParam;

/**
 * @author RettichLP
 */
public class SFForceCommand {

    @Command(value = "sfforce", usage = "/sfforce [Nachricht]")
    public boolean onCommand(UPlayer p, @CommandParam(arrayStart = true) String[] message) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String piece : message) stringBuilder.append(piece + " ");
        p.sendChatMessage("/sf " + stringBuilder);
        return true;
    }
}