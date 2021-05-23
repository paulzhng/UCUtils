package de.fuzzlemann.ucutils.utils.faction.badfaction.speech;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @author Fuzzlemann
 */
public class TriadeSpeechModifier implements SpeechModifier {
    public static final List<Map.Entry<String, String>> REPLACE_IGNORE_CASE = Lists.newArrayList(

            Maps.immutableEntry("Nia Hao", "Halllo"),
            Maps.immutableEntry("Shi De", "Ja"),
            Maps.immutableEntry("Bu Shi", "Nein"),
            Maps.immutableEntry("Zai Jian", "Tschüss"),
            Maps.immutableEntry("Xie Xie", "Danke")
    );

    @Override
    public String turnIntoSpeech(String[] words) {
        StringJoiner joiner = new StringJoiner(" ");

        for (String word : words) {
            joiner.add(word.replace('r', 'l').replace('R', 'L'));
        }

        return joiner.toString();
    }
}
