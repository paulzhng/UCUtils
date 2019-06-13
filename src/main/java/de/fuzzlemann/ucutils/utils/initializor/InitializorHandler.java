package de.fuzzlemann.ucutils.utils.initializor;

import com.google.common.collect.Ordering;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.TreeMultimap;
import de.fuzzlemann.ucutils.utils.Logger;
import net.minecraftforge.fml.common.discovery.ASMDataTable;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

/**
 * @author Fuzzlemann
 */
public class InitializorHandler {

    private static final SetMultimap<String, IInitializor> INITIALIZORS = TreeMultimap.create(Ordering.natural(), new InitializorComparator());

    public static void initInitializors(ASMDataTable asmDataTable) {
        if (!INITIALIZORS.isEmpty()) return;

        Set<ASMDataTable.ASMData> asmDataSet = asmDataTable.getAll(Initializor.class.getCanonicalName());
        for (ASMDataTable.ASMData asmData : asmDataSet) {
            try {
                Class<?> clazz = Class.forName(asmData.getClassName());

                IInitializor initializor = (IInitializor) clazz.newInstance();
                Initializor annotation = initializor.getAnnotation();

                if (!annotation.initMode().checkPreConditions()) continue;

                INITIALIZORS.put(annotation.value(), initializor);
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                throw new IllegalStateException(e); //should not happen
            }
        }
    }

    public static void initAll() {
        for (Map.Entry<String, Collection<IInitializor>> entry : INITIALIZORS.asMap().entrySet()) {
            String value = entry.getKey();
            Collection<IInitializor> initializors = entry.getValue();

            for (IInitializor initializor : initializors) {
                Logger.LOGGER.info(value + "-Initializor: loading " + initializor.getClass().getName());

                try {
                    initializor.init();
                    Logger.LOGGER.info(value + "-Initializor: successfully loaded " + initializor.getClass().getName());
                    break;
                } catch (Exception | NoClassDefFoundError e) {
                    Logger.LOGGER.info(value + "-Initializor: failed to load " + initializor.getClass().getName());
                }
            }
        }
    }

    public static class InitializorComparator implements Comparator<IInitializor> {
        @Override
        public int compare(IInitializor o1, IInitializor o2) {
            return o1.getAnnotation().initMode().compareTo(o2.getAnnotation().initMode());
        }
    }
}
