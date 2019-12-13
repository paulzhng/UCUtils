package de.fuzzlemann.ucutils.commands.management;

import de.fuzzlemann.ucutils.base.command.Command;
import de.fuzzlemann.ucutils.base.command.CommandParam;
import de.fuzzlemann.ucutils.base.text.TextUtils;
import net.minecraft.util.text.TextFormatting;

/**
 * @author Fuzzlemann
 */
public class TestMessageCommand {

    @Command(value = "testmessage", management = true)
    public boolean onCommand(@CommandParam(joinStart = true) String message) {
        TextUtils.fromLegacyText(message, TextFormatting.WHITE, '&').send();
        return true;
    }
}
