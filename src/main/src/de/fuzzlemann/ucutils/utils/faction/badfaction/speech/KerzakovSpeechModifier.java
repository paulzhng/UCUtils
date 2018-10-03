package de.fuzzlemann.ucutils.utils.faction.badfaction.speech;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @author Fuzzlemann
 */
public class KerzakovSpeechModifier implements SpeechModifier {

    private static final List<Map.Entry<String, String>> REPLACE_IGNORE_CASE = Lists.newArrayList(
            Maps.immutableEntry("hurensohn", "sukin syn"),
            Maps.immutableEntry("hallo", "privet"),
            Maps.immutableEntry("guten tag", "dobry den"),
            Maps.immutableEntry("tschüss", "paka"),
            Maps.immutableEntry("auf wiedersehen", "do svidaniya"),
            Maps.immutableEntry("prost", "na sdorove"),
            Maps.immutableEntry("was kostet", "skolko stoit"),
            Maps.immutableEntry("ich heiße", "menja sovut"),
            Maps.immutableEntry("freund", "drug"),
            Maps.immutableEntry("freunde", "drusja"),
            Maps.immutableEntry("blacklist", "cherny spisok"),
            Maps.immutableEntry("tochter", "doch'"),
            Maps.immutableEntry("sohn", "syn"),
            Maps.immutableEntry("häschen", "saika"),
            Maps.immutableEntry("krümmel", "kroschka"),
            Maps.immutableEntry("engel", "angil"),
            Maps.immutableEntry("bärchen", "mischka"),
            Maps.immutableEntry("hure", "slyha"),
            Maps.immutableEntry("dummkopf", "durak"),
            Maps.immutableEntry("ja", "da"),
            Maps.immutableEntry("nein", "njet"),
            Maps.immutableEntry("danke", "spasibo"),
            Maps.immutableEntry("bitte", "poshaluyst")
    );

    @Override
    public String turnIntoSpeech(String[] words) {
        String fullString = String.join(" ", words);
        for (Map.Entry<String, String> entry : REPLACE_IGNORE_CASE) {
            String toReplace = entry.getKey();
            String replaceTo = entry.getValue();

            fullString = fullString.replaceAll("(?i)" + toReplace, replaceTo);
        }

        words = fullString.split(" ");
        StringJoiner stringJoiner = new StringJoiner(" ");

        for (String word : words) {
            if (word.isEmpty()) continue;

            if (word.startsWith("h")) {
                word = word.replaceFirst("h", "ch");
            }

            if (word.startsWith("H")) {
                word = word.replaceFirst("H", "Ch");
            }

            stringJoiner.add(word);
        }

        return stringJoiner.toString();
    }
}
