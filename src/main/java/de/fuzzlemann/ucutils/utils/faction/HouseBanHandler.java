package de.fuzzlemann.ucutils.utils.faction;

import de.fuzzlemann.ucutils.base.udf.UDFLoader;
import de.fuzzlemann.ucutils.base.udf.UDFModule;
import de.fuzzlemann.ucutils.common.udf.DataRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fuzzlemann
 */
@UDFModule(value = DataRegistry.HOUSE_BANS, version = 1)
public class HouseBanHandler implements UDFLoader<List<String>> {

    public static final List<String> HOUSE_BANS = new ArrayList<>();

    @Override
    public void supply(List<String> uuids) {
        HOUSE_BANS.addAll(uuids);
    }

    @Override
    public void cleanUp() {
        HOUSE_BANS.clear();
    }
}
