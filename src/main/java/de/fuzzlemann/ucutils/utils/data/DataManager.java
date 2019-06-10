package de.fuzzlemann.ucutils.utils.data;

import de.fuzzlemann.ucutils.utils.Logger;
import de.fuzzlemann.ucutils.utils.text.Message;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.discovery.ASMDataTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Fuzzlemann
 */
public class DataManager {

    private static final List<DataLoader> DATA_LOADERS = new ArrayList<>();

    public static void initDataLoaders(ASMDataTable asmDataTable) {
        if (!DATA_LOADERS.isEmpty()) return;

        Set<ASMDataTable.ASMData> asmDataSet = asmDataTable.getAll(DataModule.class.getCanonicalName());
        for (ASMDataTable.ASMData asmData : asmDataSet) {
            try {
                Class<?> clazz = Class.forName(asmData.getClassName());

                DataLoader dataLoader = (DataLoader) clazz.newInstance();
                DATA_LOADERS.add(dataLoader);
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                throw new IllegalStateException(e); //should not happen
            }
        }
    }

    public static void loadData(boolean message) {
        for (DataLoader dataLoader : DATA_LOADERS) {
            DataModule dataModule = dataLoader.getClass().getAnnotation(DataModule.class);

            if (message)
                moduleMessage(dataModule, "wird geladen...");
            try {
                dataLoader.load();

                if (message)
                    moduleMessage(dataModule, "wurde erfolgreich geladen!");
            } catch (Exception e) {
                Logger.LOGGER.catching(e);

                if (message) loadingErrorMessage(dataModule);

                if (dataModule.hasFallback()) {
                    if (message)
                        moduleMessage(dataModule, "über Fallback zu laden...");

                    try {
                        dataLoader.fallbackLoading();

                        if (message)
                            moduleMessage(dataModule, "wurde erfolgreich über Fallback geladen (kann ggf. veraltete Daten enthalten)!");
                    } catch (Exception e2) {
                        Logger.LOGGER.catching(e2);

                        if (message) loadingErrorMessage(dataModule);
                    }
                }
            }
        }
    }

    private static void loadingErrorMessage(DataModule dataModule) {
        moduleMessage(dataModule, "hat fehlgeschlagen, zu laden!");
    }

    private static void moduleMessage(DataModule dataModule, String message) {
        Message.builder()
                .prefix()
                .of("Modul ").color(TextFormatting.GRAY).advance()
                .of("\"" + dataModule.value() + "\"").color(TextFormatting.BLUE).advance()
                .of(" (" + (dataModule.local() ? "Lokal" : "Remote") + ") " + message).color(TextFormatting.GRAY).advance()
                .send();
    }
}
