package de.fuzzlemann.ucutils.utils.faction.badfaction.speech;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * @author Fuzzlemann
 */
public class SpeechModifyUtil {

    private static final Set<String> COMMANDS = Sets.newHashSet("asms", "sms", "w", "close", "whisper", "s", "schreien");

    public static String modifyString(String message, SpeechModifier modifier) {
        String[] splitted = message.split(" ");

        if (message.startsWith("/")) {
            if (splitted.length == 1) return null;

            String command = splitted[0].toLowerCase();
            command = command.substring(1, command.length());

            if (COMMANDS.contains(command)) {
                int ignoredPositions = command.equals("sms") || command.equals("asms") ? 2 : 1;

                String[] unmodifiable = new String[ignoredPositions];
                System.arraycopy(splitted, 0, unmodifiable, 0, ignoredPositions);

                String[] modifiable = new String[splitted.length - ignoredPositions];
                System.arraycopy(splitted, ignoredPositions, modifiable, 0, splitted.length - ignoredPositions);

                String modifiableString = modifier.turnIntoSpeech(modifiable);

                String fullCommand = String.join(" ", unmodifiable);
                if (!modifiableString.isEmpty())
                    fullCommand += " " + modifiableString;

                return fullCommand;
            }

            return null;
        }

        return modifier.turnIntoSpeech(splitted);
    }
}
