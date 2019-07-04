package de.fuzzlemann.ucutils.commands.management;

import de.fuzzlemann.ucutils.utils.ReflectionUtil;
import de.fuzzlemann.ucutils.utils.command.api.Command;
import de.fuzzlemann.ucutils.utils.command.api.CommandParam;
import de.fuzzlemann.ucutils.utils.text.Message;
import net.minecraft.util.text.TextFormatting;

/**
 * @author Fuzzlemann
 */
public class TestMessageCommand {

    @Command(value = "testmessage", management = true)
    public boolean onCommand(@CommandParam(joinStart = true) String message) {
        Message.Builder builder = Message.builder();
        String[] split = message.split("&");
        for (String s : split) {
            if (s.isEmpty()) continue;
            if (s.length() < 2 || split.length == 1) {
                builder.of(s).advance();
                break;
            }

            char colorCode = s.charAt(0);
            TextFormatting color = null;

            for (TextFormatting textFormatting : TextFormatting.values()) {
                Character searchedChar = ReflectionUtil.getValue(textFormatting, char.class);
                if (searchedChar == null) continue;

                if (searchedChar == colorCode) {
                    color = textFormatting;
                    break;
                }
            }

            if (color == null) color = TextFormatting.WHITE;

            s = s.substring(1);
            builder.of(s).color(color).advance();
        }

        builder.send();
        return true;
    }
}
