package de.fuzzlemann.ucutils.utils.data;

import de.fuzzlemann.ucutils.utils.Logger;
import de.fuzzlemann.ucutils.utils.abstraction.AbstractionHandler;
import de.fuzzlemann.ucutils.utils.abstraction.TestPlayer;
import de.fuzzlemann.ucutils.utils.faction.Faction;
import de.fuzzlemann.ucutils.utils.faction.FactionLoader;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Fuzzlemann
 */
public class DataTest {

    @BeforeAll
    static void setUp() {
        AbstractionHandler.getInstance().setPlayerImplementation(TestPlayer.class);

        ASMDataTable asmDataTable = mock(ASMDataTable.class);
        Set<ASMDataTable.ASMData> asmDataSet = new HashSet<>();

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage("de.fuzzlemann.ucutils.utils"))
                .setScanners(new TypeAnnotationsScanner(), new SubTypesScanner()));
        Set<Class<?>> dataClasses = reflections.getTypesAnnotatedWith(DataModule.class);

        for (Class<?> clazz : dataClasses) {
            ASMDataTable.ASMData asmData = mock(ASMDataTable.ASMData.class);
            when(asmData.getClassName()).thenReturn(clazz.getCanonicalName());

            asmDataSet.add(asmData);
        }

        when(asmDataTable.getAll(DataModule.class.getCanonicalName())).thenReturn(asmDataSet);

        DataManager.initDataLoaders(asmDataTable);
    }

    @Test
    void testLoadDataWithoutMessage() {
        DataManager.loadData(false);
    }

    @Test
    void testLoadDataWithMessage() {
        DataManager.loadData(true);
    }

    @Test
    void testLoadFaction() {
        new FactionLoader().load();
        Logger.LOGGER.info("faction: " + Faction.getFactionOfPlayer());
    }
}
