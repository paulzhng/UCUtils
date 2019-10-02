package de.fuzzlemann.ucutils.utils.faction;

import de.fuzzlemann.ucutils.utils.abstraction.AbstractionLayer;
import de.fuzzlemann.ucutils.utils.api.APIUtils;
import de.fuzzlemann.ucutils.utils.data.DataLoader;
import de.fuzzlemann.ucutils.utils.data.DataModule;

/**
 * @author Fuzzlemann
 */
@DataModule("Faction")
public class FactionLoader implements DataLoader {

    @Override
    public void load() {
        Faction.factionOfPlayer = Faction.byAPIName(
                APIUtils.post("http://tomcat.fuzzlemann.de/factiononline/getfaction",
                        "uuid", AbstractionLayer.getPlayer().getUniqueID())
        );
    }
}
