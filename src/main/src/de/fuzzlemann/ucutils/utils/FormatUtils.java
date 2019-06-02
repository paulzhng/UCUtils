package de.fuzzlemann.ucutils.utils;

import de.fuzzlemann.ucutils.utils.text.Message;
import de.fuzzlemann.ucutils.utils.text.MessagePart;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fuzzlemann
 */
public class FormatUtils {

    public static List<MessagePart> formatMillisecondsToMessage(long ms) {
        List<String> timeElements = getTimeElements(ms);

        Message.MessageBuilder builder = Message.builder();
        for (int i = 0; i < timeElements.size(); i++) {
            String timeElement = timeElements.get(i);
            builder.of(timeElement).color(TextFormatting.BLUE).advance();

            if (i == timeElements.size() - 1) continue;
            if (i == timeElements.size() - 2) {
                builder.of(" und ").color(TextFormatting.GRAY).advance();
            } else {
                builder.of(", ").color(TextFormatting.GRAY).advance();
            }
        }

        return builder.build().getMessageParts();
    }

    private static List<String> getTimeElements(long ms) {
        long milliseconds = ms % 1000;
        ms /= 1000;
        long seconds = ms % 60;
        ms /= 60;
        long minutes = ms % 60;
        ms /= 60;
        long hours = ms % 24;

        List<String> timeElements = new ArrayList<>();

        if (hours != 0) {
            timeElements.add(hours + (hours == 1 ? " Stunde" : " Stunden"));
        }

        if (minutes != 0) {
            timeElements.add(minutes + (minutes == 1 ? " Minute" : " Minuten"));
        }

        if (seconds != 0) {
            timeElements.add(seconds + (seconds == 1 ? " Sekunde" : " Sekunden"));
        }

        if (milliseconds != 0) {
            timeElements.add(milliseconds + (milliseconds == 1 ? " Millisekunde" : " Millisekunden"));
        }

        return timeElements;
    }

    public static long toMilliseconds(String[] input) {
        long ms = 0;

        for (String s : input) {
            ms += toMilliseconds(s);
        }

        return ms;
    }

    public static long toMilliseconds(String input) {
        char lastChar = input.charAt(input.length() - 1);
        long value;
        try {
            value = Integer.parseInt(input.substring(0, input.length() - 1));
        } catch (NumberFormatException e) {
            return 0;
        }

        for (TimeFormat timeFormat : TimeFormat.values()) {
            if (timeFormat.getCharacter() != lastChar) continue;

            return value * timeFormat.getMilliseconds();
        }

        return 0;
    }

    private enum TimeFormat {
        HOUR(1000 * 60 * 60, 'h'),
        MINUTE(1000 * 60, 'm'),
        SECOND(1000, 's');

        private final int milliseconds;
        private final char character;

        TimeFormat(int milliseconds, char character) {
            this.milliseconds = milliseconds;
            this.character = character;
        }

        public char getCharacter() {
            return character;
        }

        public long getMilliseconds() {
            return milliseconds;
        }
    }
}
