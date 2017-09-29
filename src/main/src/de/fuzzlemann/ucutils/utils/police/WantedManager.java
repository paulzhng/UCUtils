package de.fuzzlemann.ucutils.utils.police;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fuzzlemann
 */
public class WantedManager {

    private static List<Wanted> wantedList = new ArrayList<>();

    public static void fillWantedList() throws IOException {
        URL url = new URL("http://www.fuzzlemann.de/wanteds.html");
        String result = IOUtils.toString(url, StandardCharsets.UTF_8);

        String[] wantedStrings = result.split("<>");

        for (String wantedString : wantedStrings) {
            String[] splittedWantedString = wantedString.split(";");

            String reason = StringEscapeUtils.unescapeJava(splittedWantedString[0]);
            int wanteds = Integer.parseInt(splittedWantedString[1]);

            wantedList.add(new Wanted(reason, wanteds));
        }
    }

    public static List<String> getWantedReasons() {
        return wantedList.stream()
                .map(Wanted::getReason)
                .collect(Collectors.toList());
    }

    public static Wanted getWanted(String reason) {
        for (Wanted wanted : wantedList) {
            if (wanted.getReason().equalsIgnoreCase(reason)) return wanted;
        }

        Wanted foundWanted = null;
        String lowerReason = reason.toLowerCase();

        int delta = Integer.MAX_VALUE;

        for (Wanted wanted : wantedList) {
            String wantedReason = wanted.getReason().toLowerCase();
            if (!wantedReason.startsWith(lowerReason)) {
                continue;
            }

            int curDelta = Math.abs(wantedReason.length() - lowerReason.length());
            if (curDelta < delta) {
                foundWanted = wanted;
                delta = curDelta;
            }

            if (curDelta == 0) {
                break;
            }
        }

        return foundWanted;
    }

}
