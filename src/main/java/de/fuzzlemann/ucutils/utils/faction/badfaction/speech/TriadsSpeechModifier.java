package de.fuzzlemann.ucutils.utils.faction.badfaction.speech;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @author Fuzzlemann
 */
public class TriadsSpeechModifier implements SpeechModifier {
    public static final List<Map.Entry<String, String>> REPLACE_IGNORE_CASE = Lists.newArrayList(

            Maps.immutableEntry("Hallo", "Nia Hao"),
            Maps.immutableEntry("Ja", "Shi De"),
            Maps.immutableEntry("Nein", "Bu Shi"),
            Maps.immutableEntry("Tschüss", "Zai Jian"),
            Maps.immutableEntry("Danke", "Xie Xie")
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
