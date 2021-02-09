package de.fuzzlemann.ucutils.utils.faction.badfaction.speech;

import com.google.common.collect.Sets;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
            command = command.substring(1);

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

    public static String replaceIgnoreCase(String toReplaceString, List<Map.Entry<String, String>> replaceMap) {
        for (Map.Entry<String, String> entry : replaceMap) {
            String[] s = toReplaceString.split(" ");

            for (int i=0; i<s.length; i++) {
                String toReplace = entry.getKey();
                String replaceTo = entry.getValue();

                if (s[i].equalsIgnoreCase(toReplace)) {
                    s[i].replace("(?i)" + toReplace, replaceTo);
                }
            }

            toReplaceString = s.toString();

        }

        return toReplaceString;
    }

    public static String replaceRetainingCase(String toReplaceString, List<Map.Entry<String, String>> replaceMap) {
        for (Map.Entry<String, String> entry : replaceMap) {
            String toReplace = entry.getKey();
            String replaceTo = entry.getValue();

            if (toReplace.equalsIgnoreCase(toReplaceString))
                toReplaceString = replaceRetainingCase(toReplaceString, replaceTo);
        }

        return toReplaceString;
    }

    public static String replaceRetainingCase(String toReplace, String replaceTo) {
        return toReplace.replaceAll("(?i)" + toReplace, replaceTo.chars()
                .mapToObj(i -> (char) i)
                .map(i -> {
                    int index = replaceTo.indexOf(i);
                    boolean lowerCase = index > toReplace.length() - 1;

                    if (!lowerCase)
                        lowerCase = Character.isLowerCase(toReplace.charAt(index));

                    return lowerCase
                            ? Character.toString(Character.toLowerCase(i))
                            : Character.toString(Character.toUpperCase(i));
                })
                .collect(Collectors.joining()));
    }
}
