package de.fuzzlemann.ucutils.commands.management;

import de.fuzzlemann.ucutils.utils.ReflectionUtil;
import de.fuzzlemann.ucutils.utils.command.Command;
import de.fuzzlemann.ucutils.utils.command.CommandExecutor;
import de.fuzzlemann.ucutils.utils.text.Message;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextFormatting;

/**
 * @author Fuzzlemann
 */
public class TestMessageCommand implements CommandExecutor {
    @Override
    @Command(labels = "testmessage", management = true)
    public boolean onCommand(EntityPlayerSP p, String[] args) {
        String message = String.join(" ", args);

        Message.MessageBuilder builder = Message.builder();
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
                char searchedChar = ReflectionUtil.getValue(textFormatting, char.class);
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
