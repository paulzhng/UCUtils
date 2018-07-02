package de.fuzzlemann.ucutils.utils;

/**
 * @author Fuzzlemann
 */
public class FormatUtils {

    public static String formatMilliseconds(long ms) {
        StringBuilder sb = new StringBuilder();
        long milliseconds = ms % 1000;
        ms /= 1000;
        long seconds = ms % 60;
        ms /= 60;
        long minutes = ms % 60;
        ms /= 60;
        long hours = ms % 24;

        if (hours != 0) {
            sb.append(hours).append(hours == 1 ? " Stunde " : " Stunden ");
        }

        if (minutes != 0) {
            sb.append(minutes).append(minutes == 1 ? " Minute " : " Minuten ");
        }

        if (seconds != 0) {
            sb.append(seconds).append(seconds == 1 ? " Sekunde " : " Sekunden ");
        }

        if (milliseconds != 0) {
            sb.append(milliseconds).append(milliseconds == 1 ? " Millisekunde" : " Millisekunden");
        }

        return sb.toString();
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
