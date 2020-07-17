package de.fuzzlemann.ucutils.utils.info;

import de.fuzzlemann.ucutils.base.udf.UDFLoader;
import de.fuzzlemann.ucutils.base.udf.UDFModule;
import de.fuzzlemann.ucutils.common.udf.DataRegistry;
import de.fuzzlemann.ucutils.common.udf.data.misc.info.UDFCommandInfo;
import de.fuzzlemann.ucutils.common.udf.data.misc.info.UDFFactionInfo;
import de.fuzzlemann.ucutils.common.udf.data.misc.info.UDFInfo;
import de.fuzzlemann.ucutils.utils.faction.Faction;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @author Fuzzlemann
 */
@UDFModule(value = DataRegistry.INFO, version = 1)
public class InfoStorage implements UDFLoader<UDFInfo> {

    public static CommandInfo commandInfo = new CommandInfo(new HashSet<>());
    public static Map<Faction, FactionInfo> factionInfoMap = new HashMap<>();

    @Override
    public void supply(UDFInfo udfInfo) {
        UDFCommandInfo udfCommandInfo = udfInfo.getUdfCommandInfo();
        Map<String, UDFFactionInfo> udfFactionInfos = udfInfo.getUdfFactionInfos();

        commandInfo = byUDF(udfCommandInfo);

        for (Map.Entry<String, UDFFactionInfo> entry : udfFactionInfos.entrySet()) {
            String factionAPIName = entry.getKey();
            Faction faction = Faction.byAPIName(factionAPIName);

            UDFFactionInfo udfFactionInfo = entry.getValue();
            FactionInfo factionInfo = new FactionInfo(
                    udfFactionInfo.getFullName(),
                    udfFactionInfo.getShortName(),
                    udfFactionInfo.isBadFrak(),
                    udfFactionInfo.getHqPosition(),
                    udfFactionInfo.getTasks(),
                    udfFactionInfo.getFactionType(),
                    udfFactionInfo.getNaviPoint(),
                    byUDF(udfFactionInfo.getCommandInfo())
            );

            factionInfoMap.put(faction, factionInfo);
        }
    }

    private CommandInfo byUDF(UDFCommandInfo udfCommandInfo) {
        return new CommandInfo(udfCommandInfo.getCommandDescriptions());
    }
}
