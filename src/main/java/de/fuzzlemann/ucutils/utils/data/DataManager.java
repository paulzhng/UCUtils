package de.fuzzlemann.ucutils.utils.data;

import de.fuzzlemann.ucutils.utils.ForgeUtils;
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

    public static void registerDataLoaders(ASMDataTable asmDataTable) {
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

    public static void loadData(boolean verbose) {
        for (DataLoader dataLoader : DATA_LOADERS) {
            DataModule dataModule = dataLoader.getClass().getAnnotation(DataModule.class);
            if (ForgeUtils.isTest() && !dataModule.test()) continue;

            moduleMessage(verbose, dataModule, "wird geladen...");
            try {
                dataLoader.load();

                moduleMessage(verbose, dataModule, "wurde erfolgreich geladen!");
            } catch (Exception e) {
                Logger.LOGGER.catching(e);

                moduleMessage(verbose, dataModule, "hat fehlgeschlagen, zu laden!");

                if (dataModule.hasFallback()) {
                    moduleMessage(verbose, dataModule, "über Fallback zu laden...");

                    try {
                        dataLoader.fallbackLoading();

                        moduleMessage(verbose, dataModule, "wurde erfolgreich über Fallback geladen (kann ggf. veraltete Daten enthalten)!");
                    } catch (Exception e2) {
                        Logger.LOGGER.catching(e2);

                        moduleMessage(verbose, dataModule, "hat fehlgeschlagen über Fallback, zu laden!");
                    }
                }
            }
        }
    }

    private static void moduleMessage(boolean verbose, DataModule dataModule, String message) {
        if (verbose) return;

        Message.builder()
                .prefix()
                .of("Modul ").color(TextFormatting.GRAY).advance()
                .of("\"" + dataModule.value() + "\"").color(TextFormatting.BLUE).advance()
                .of(" (" + (dataModule.local() ? "Lokal" : "Remote") + ") " + message).color(TextFormatting.GRAY).advance()
                .send();
    }
}
