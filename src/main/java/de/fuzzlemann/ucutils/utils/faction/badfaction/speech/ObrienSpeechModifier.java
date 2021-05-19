package de.fuzzlemann.ucutils.utils.faction.badfaction.speech;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Map;

/**
 * @author Fuzzlemann
 */

@SideOnly(Side.CLIENT)

public class ObrienSpeechModifier implements SpeechModifier {

    private static final List<Map.Entry<String, String>> REPLACE_IGNORE_CASE = Lists.newArrayList(

            Maps.immutableEntry("Hallo", "Dia dhuit"),
            Maps.immutableEntry("Meine Schwester", "mo dheirfiúr "),
            Maps.immutableEntry("Mein Bruder", "mo dheartháir "),
            Maps.immutableEntry("Mein Herr", "Sir"),
            Maps.immutableEntry("Meine Dame", "Miss"),
            Maps.immutableEntry("Gott", "dia"),
            Maps.immutableEntry("Tschüss", "Slán"),
            Maps.immutableEntry("Bye", "Slán"),
            Maps.immutableEntry("Hurensohn", "mác soith")

    );

    @Override
    public String turnIntoSpeech(String[] words) {
        String fullString = String.join(" ", words);
        fullString = SpeechModifyUtil.replaceIgnoreCase(fullString, REPLACE_IGNORE_CASE);

        return fullString;
    }

}
