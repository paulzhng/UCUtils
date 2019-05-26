package de.fuzzlemann.ucutils.utils.faction.badfaction.speech;

import com.google.common.base.CharMatcher;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.regex.Pattern;

/**
 * @author Fuzzlemann
 */
public class CosaNostraSpeechModifier implements SpeechModifier {

    private static final Pattern VOCALS_PATTERN = Pattern.compile("[aeiouöäüAEIOUÄÜÖàáèéìíóòùúÀÁÈÉÌÍÒÓÙÚ]");
    private static final Set<String> EXCLUDED = Sets.newHashSet("xd");
    private static final List<Map.Entry<String, String>> REPLACE_IGNORE_CASE = Lists.newArrayList(
            Maps.immutableEntry("guten tag", "buongiorno"),
            Maps.immutableEntry("tschüss", "arrivederci"),
            Maps.immutableEntry("junge", "ragazzo"),
            Maps.immutableEntry("mädchen", "ragazza"),
            Maps.immutableEntry("Wollen Sie Drogen", "Vogliono la droga"),
            Maps.immutableEntry("waffen", "stemma"),
            Maps.immutableEntry("Wie geht es dir", "Come stai"),
            Maps.immutableEntry("Guten Abend", "Buona sera"),
            Maps.immutableEntry("Schönen Abend", "Buona serata")
    );
    private static final List<Map.Entry<String, String>> REPLACE_RETAIN_CASE = Lists.newArrayList(
            Maps.immutableEntry("ja", "sì"),
            Maps.immutableEntry("danke", "grazie"),
            Maps.immutableEntry("bitte", "prego")
    );

    @Override
    public String turnIntoSpeech(String[] words) {
        String fullString = String.join(" ", words);
        fullString = SpeechModifyUtil.replaceIgnoreCase(fullString, REPLACE_IGNORE_CASE);
        words = fullString.split(" ");

        StringJoiner stringJoiner = new StringJoiner(" ");

        for (String word : words) {
            if (word.isEmpty()) continue;
            if (EXCLUDED.contains(word.toLowerCase())) {
                stringJoiner.add(word);
                continue;
            }

            String lastChar = word.substring(word.length() - 1);

            int lastIndex = word.length() - 1;
            int i = lastIndex;
            while (lastIndex != 0 && i != 1 && !CharMatcher.javaLetter().matches(lastChar.toCharArray()[0])) {
                lastChar = word.substring(--i, i + 1);
            }

            if (CharMatcher.javaLetter().matches(lastChar.toCharArray()[0])) {
                String replaceWord = word;

                boolean addSpecialCharacters = lastIndex != i;
                if (addSpecialCharacters) {
                    replaceWord = SpeechModifyUtil.replaceRetainingCase(replaceWord.substring(0, i + 1), REPLACE_RETAIN_CASE);
                } else {
                    replaceWord = SpeechModifyUtil.replaceRetainingCase(replaceWord, REPLACE_RETAIN_CASE);
                }

                boolean addE = !VOCALS_PATTERN.matcher(String.valueOf(replaceWord.charAt(replaceWord.length() - 1))).find();

                if (addE)
                    replaceWord += "e";

                if (addSpecialCharacters)
                    replaceWord += word.substring(i + 1, lastIndex + 1);

                word = replaceWord;
            }

            stringJoiner.add(word);
        }

        return stringJoiner.toString();
    }
}
