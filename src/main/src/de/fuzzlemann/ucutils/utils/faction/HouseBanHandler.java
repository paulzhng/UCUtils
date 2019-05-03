package de.fuzzlemann.ucutils.utils.faction;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Fuzzlemann
 */
public class HouseBanHandler {

    public static final List<String> HOUSE_BANS = new ArrayList<>();

    public static void fillHouseBanList() throws IOException {
        HOUSE_BANS.clear();

        URL url = new URL("http://tomcat.fuzzlemann.de/factiononline/housebans");
        String result = IOUtils.toString(url, StandardCharsets.UTF_8);

        HOUSE_BANS.addAll(Arrays.asList(result.split("<>")));
    }
}
