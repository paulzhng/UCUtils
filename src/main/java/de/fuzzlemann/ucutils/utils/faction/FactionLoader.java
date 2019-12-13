package de.fuzzlemann.ucutils.utils.faction;

import de.fuzzlemann.ucutils.base.abstraction.AbstractionLayer;
import de.fuzzlemann.ucutils.utils.api.APIUtils;
import de.fuzzlemann.ucutils.base.data.DataLoader;
import de.fuzzlemann.ucutils.base.data.DataModule;

/**
 * @author Fuzzlemann
 */
@DataModule("Faction")
public class FactionLoader implements DataLoader {

    @Override
    public void load() {
        Faction.factionOfPlayer = Faction.byAPIName(
                APIUtils.post("http://tomcat.fuzzlemann.de/factiononline/getfaction",
                        "uuid", AbstractionLayer.getPlayer().getUniqueID().toString())
        );
    }
}
