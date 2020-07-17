package de.fuzzlemann.ucutils.utils;

import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.config.UCUtilsConfig;
import de.fuzzlemann.ucutils.utils.api.APIUtils;
import de.fuzzlemann.ucutils.utils.io.JsonManager;
import net.minecraft.util.Session;
import net.minecraftforge.fml.common.Loader;

import java.io.File;
import java.util.UUID;

/**
 * @author Fuzzlemann
 */
public class AnalyticsUtil {

    private static final File ANALYTICS_STORAGE_FILE = new File(JsonManager.DIRECTORY, "analyticsid.storage");

    public static void sendStartupAnalytics() {
        if (!UCUtilsConfig.analytics) return;

        Session session = Main.MINECRAFT.getSession();
        String uuid = session.getProfile().getId().toString();
        String name = session.getUsername();

        String version = Main.VERSION;
        String minecraftVersion = Main.MINECRAFT.getVersion();
        String javaVersion = System.getProperty("java.version");

        String mods = String.join(", ", Loader.instance().getIndexedModList().keySet());

        APIUtils.post("http://analytics.fuzzlemann.de/submit.php",
                "analyticsID", getAnalyticsID(),
                "uuid", uuid,
                "name", name,
                "version", version,
                "minecraftVersion", minecraftVersion,
                "javaVersion", javaVersion,
                "mods", mods
        );
    }

    private static String getAnalyticsID() {
        if (ANALYTICS_STORAGE_FILE.exists()) {
            try {
                return UUID.fromString(JsonManager.loadObject(ANALYTICS_STORAGE_FILE, String.class)).toString();
            } catch (Exception e) {
                if (ANALYTICS_STORAGE_FILE.delete()) {
                    Logger.LOGGER.info(ANALYTICS_STORAGE_FILE.getAbsoluteFile() + " deleted");
                    return getAnalyticsID();
                }
            }
        }

        String analyticsID = UUID.randomUUID().toString();
        JsonManager.writeObject(ANALYTICS_STORAGE_FILE, analyticsID);

        return analyticsID;
    }
}
