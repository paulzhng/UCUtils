package de.fuzzlemann.ucutils.utils.faction.badfaction.speech;

import java.util.StringJoiner;

/**
 * @author Fuzzlemann
 */
public class TriadenSpeechModifier implements SpeechModifier {

    @Override
    public String turnIntoSpeech(String[] words) {
        StringJoiner joiner = new StringJoiner(" ");

        for (String word : words) {
            joiner.add(word.replace('r', 'l').replace('R', 'L'));
        }

        return joiner.toString();
    }
}
