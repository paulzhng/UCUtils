package de.fuzzlemann.ucutils.utils.config;

import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.utils.log.ChatLogger;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Fuzzlemann
 */
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class ConfigUtil {

    public static Configuration config;
    public static boolean blockResourcepackReminder = false;
    public static boolean reportAnnouncement = true;
    public static boolean contractAnnouncement = true;
    public static boolean serviceAnnouncement = true;
    public static boolean munitionDisplay = true;
    public static boolean logChat = true;

    public static void syncConfig() {
        config.load();

        Property blockResourcepackReminderProperty = config.get(Configuration.CATEGORY_GENERAL,
                "blockResourcepackReminder",
                true,
                "Blockiert die Nachricht, die einen informiert, dass man das UC Resourcepack downloaden sollte.");
        blockResourcepackReminder = blockResourcepackReminderProperty.getBoolean();

        Property reportAnnouncementProperty = config.get(Configuration.CATEGORY_GENERAL,
                "reportAnnouncement",
                true,
                "Spielt einen Sound ab, wenn ein Report reinkommt");
        reportAnnouncement = reportAnnouncementProperty.getBoolean();

        Property contractAnnouncementProperty = config.get(Configuration.CATEGORY_GENERAL,
                "contractAnnouncement",
                true,
                "Spielt einen Sound ab, wenn ein Contract ausgesetzt wird");
        contractAnnouncement = contractAnnouncementProperty.getBoolean();

        Property serviceAnnouncementProperty = config.get(Configuration.CATEGORY_GENERAL,
                "serviceAnnouncement",
                true,
                "Spielt einen Sound ab, wenn ein Service reinkommt");
        serviceAnnouncement = serviceAnnouncementProperty.getBoolean();

        Property munitionDisplayProperty = config.get(Configuration.CATEGORY_GENERAL,
                "munitionDisplay",
                true,
                "Die Munition wird angezeigt, wenn man schieﬂt");
        munitionDisplay = munitionDisplayProperty.getBoolean();

        Property logChatProperty = config.get(Configuration.CATEGORY_GENERAL,
                "logChat",
                true,
                "Der Chat wird geloggt und im Minecraft-Order unter /chatlogs gespeichert");
        logChat = logChatProperty.getBoolean();

        if (logChat)
            ChatLogger.instance = new ChatLogger();
    }

    @SubscribeEvent
    public static void onConfigChange(ConfigChangedEvent e) {
        if (config.hasChanged())
            config.save();

        if (e.getModID().equals(Main.MOD_ID))
            syncConfig();
    }
}
