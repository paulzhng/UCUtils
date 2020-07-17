package de.fuzzlemann.ucutils.utils.faction.police;

import de.fuzzlemann.ucutils.base.udf.UDFLoader;
import de.fuzzlemann.ucutils.base.udf.UDFModule;
import de.fuzzlemann.ucutils.common.udf.DataRegistry;
import de.fuzzlemann.ucutils.utils.ForgeUtils;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
@UDFModule(value = DataRegistry.WANTED_CATALOG, version = 1)
public class WantedManager implements UDFLoader<List<WantedReason>> {

    private static final List<WantedReason> WANTED_LIST = new ArrayList<>();

    public static List<String> getWantedReasons() {
        return WANTED_LIST.stream()
                .map(WantedReason::getReason)
                .collect(Collectors.toList());
    }

    public static WantedReason getWantedReason(String reason) {
        return ForgeUtils.getMostMatching(WANTED_LIST, reason, WantedReason::getReason);
    }

    @Override
    public void supply(List<WantedReason> wantedReasons) {
        WANTED_LIST.addAll(wantedReasons);
    }

    @Override
    public void cleanUp() {
        WANTED_LIST.clear();
    }
}
