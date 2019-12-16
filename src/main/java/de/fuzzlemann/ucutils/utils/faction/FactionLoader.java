package de.fuzzlemann.ucutils.utils.faction;

import de.fuzzlemann.ucutils.base.udf.UDFLoader;
import de.fuzzlemann.ucutils.base.udf.UDFModule;
import de.fuzzlemann.ucutils.common.udf.DataRegistry;

/**
 * @author Fuzzlemann
 */
@UDFModule(value = DataRegistry.FACTION, version = 1)
public class FactionLoader implements UDFLoader<String> {

    @Override
    public void supply(String faction) {
        Faction.factionOfPlayer = Faction.byAPIName(faction);
    }
}
