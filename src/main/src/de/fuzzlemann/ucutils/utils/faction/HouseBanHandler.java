package de.fuzzlemann.ucutils.utils.faction;

import de.fuzzlemann.ucutils.utils.data.DataLoader;
import de.fuzzlemann.ucutils.utils.api.APIUtils;
import de.fuzzlemann.ucutils.utils.data.DataModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Fuzzlemann
 */
@DataModule("Houseban")
public class HouseBanHandler implements DataLoader {

    public static final List<String> HOUSE_BANS = new ArrayList<>();

    @Override
    public void load() {
        HOUSE_BANS.clear();
        String result = APIUtils.get("http://tomcat.fuzzlemann.de/factiononline/housebans");

        HOUSE_BANS.addAll(Arrays.asList(result.split("<>")));
    }
}
