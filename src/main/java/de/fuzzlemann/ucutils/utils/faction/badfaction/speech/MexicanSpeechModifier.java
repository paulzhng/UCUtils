package de.fuzzlemann.ucutils.utils.faction.badfaction.speech;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @author Fuzzlemann
 */
public class MexicanSpeechModifier implements SpeechModifier {

    private static final List<Map.Entry<String, String>> REPLACE_IGNORE_CASE = Lists.newArrayList(
            Maps.immutableEntry("hurensohn", "hijo de puta"),
            Maps.immutableEntry("hallo", "hola"),
            Maps.immutableEntry("tschüss", "adios"),
            Maps.immutableEntry("prost", "salud"),
            Maps.immutableEntry("ich heiße", "me llamo"),
            Maps.immutableEntry("freund", "amigo"),
            Maps.immutableEntry("freundin", "amiga"),
            Maps.immutableEntry("freunde", "amigos"),
            Maps.immutableEntry("tochter", "hermana'"),
            Maps.immutableEntry("sohn", "el hijo"),
            Maps.immutableEntry("häschen", "conejito"),
            Maps.immutableEntry("krümel", "migas"),
            Maps.immutableEntry("engel", "ángel"),
            Maps.immutableEntry("bärchen", "osito"),
            Maps.immutableEntry("hure", "puta"),
            Maps.immutableEntry("dummkopf", "enganar"),
            Maps.immutableEntry("ja", "si"),
            Maps.immutableEntry("nein", "no"),
            Maps.immutableEntry("danke", "gracias"),
            Maps.immutableEntry("bitte", "por favor"),
            Maps.immutableEntry("penis", "el pene")
    );

    @Override
    public String turnIntoSpeech(String[] words) {
        String fullString = String.join(" ", words);
        fullString = SpeechModifyUtil.replaceIgnoreCase(fullString, REPLACE_IGNORE_CASE);

        return fullString;
    }
}
