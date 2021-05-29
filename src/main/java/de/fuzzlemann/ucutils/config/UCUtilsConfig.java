package de.fuzzlemann.ucutils.config;

import de.fuzzlemann.ucutils.Main;
import de.fuzzlemann.ucutils.teamspeak.TSClientQuery;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author Fuzzlemann
 */
@Config(modid = Main.MOD_ID, name = Main.MOD_ID)
@Mod.EventBusSubscriber
public class UCUtilsConfig {

    @Config.Name("analytics")
    @Config.Comment("Sendet nützliche Informationen um Fehler in der Modifikation leichter zu beheben und um Hilfe bei gewissen Fehlern zu bieten")
    public static boolean analytics = true;

    @Config.Name("blockResourcePackReminder")
    @Config.Comment("Blockiert die Nachricht, die einen informiert, dass man das UC Resourcepack downloaden sollte")
    public static boolean blockResourcePackReminder = false;

    @Config.Name("reportAnnouncement")
    @Config.Comment("Spielt einen Sound ab, wenn ein Report reinkommt")
    public static boolean reportAnnouncement = true;

    @Config.Name("contractAnnouncement")
    @Config.Comment("Spielt einen Sound ab, wenn ein Contract ausgesetzt wird")
    public static boolean contractAnnouncement = true;

    @Config.Name("contractFulfilledAnnouncement")
    @Config.Comment("Spielt einen Sound ab, wenn ein Spieler auf der Contractliste getötet wird")
    public static boolean contractFulfilledAnnouncement = true;

    @Config.Name("serviceAnnouncement")
    @Config.Comment("Spielt einen Sound ab, wenn ein Service reinkommt")
    public static boolean serviceAnnouncement = true;

    @Config.Name("inviteAnnouncement")
    @Config.Comment("Spielt einen Sound ab, wenn ein Spieler invitet oder uninvitet wird")
    public static boolean inviteAnnouncement = false;

    @Config.Name("bombAnnouncement")
    @Config.Comment("Spielt einen Sound ab, wenn eine Bombe gelegt wird")
    public static boolean bombAnnouncement = true;

    @Config.Name("autoNavigationForCarFind")
    @Config.Comment("Startet automatisch die Navi Route zu deinem Auto nach dem ausführen von /car find")
    public static boolean autoNavigationForCarFind = true;

    @Config.Name("munitionDisplay")
    @Config.Comment("Zeigt die Munition an, wenn man schießt")
    public static boolean munitionDisplay = true;

    @Config.Name("bombTimerDisplay")
    @Config.Comment("Zeigt die Zeit seit dem Legen der Bombe an")
    public static boolean bombTimerDisplay = true;

    @Config.Name("logChat")
    @Config.Comment("Der Chat wird geloggt und im Minecraft-Order unter /chatlogs gespeichert")
    public static boolean logChat = true;

    @Config.Name("showFactionPlayers")
    @Config.Comment("Kennzeichnet Personen aus deiner Fraktion")
    public static boolean showFactionPlayers = false;

    @Config.Name("showHouseBans")
    @Config.Comment("Kennzeichnet Personen mit einem Hausverbot")
    public static boolean showHouseBans = false;

    @Config.Name("apiKey")
    @Config.Comment("Der API Key (vom fuzzlemann.de-Panel; siehe Account-Einstellungen)")
    public static String apiKey = "";

    @Config.Name("tsApiKey")
    @Config.Comment("Der TS API Key (TeamSpeak ALT + P -> Erweiterungen -> ClientQuery -> Einstellungen -> API Key)")
    public static String tsAPIKey = "";

    @Config.Name("reportGreeting")
    @Config.Comment("Die Begrüßung, die beim Betreten eines Reports geschrieben wird (Nur als Teammitglied wichtig) (Wenn keine erwünscht ist, leer lassen)")
    public static String reportGreeting = "";

    @Config.Name("notifyWaitingSupport")
    @Config.Comment("Gibt eine Nachricht und einen Sound ab, wenn eine Person das Wartezimmer im TeamSpeak betritt (Nur als Teammitglied wichtig)")
    public static boolean notifyWaitingSupport = false;

    @Config.Name("notifyWaitingPublicChannel")
    @Config.Comment("Gibt eine Nachricht und einen Sound ab, wenn eine Person das Öffentlich der eigenen Fraktion im TeamSpeak betritt")
    public static boolean notifyWaitingPublic = false;

    @Config.Name("warnTazer")
    @Config.Comment("Lass dir mit einem Linksklick eine Warnung in den Chat schreiben, sofern dein Tazer geladen ist")
    public static boolean warnTazer = false;

    @Config.Name("plantTimer")
    @Config.Comment("Setzt automatisch einen Timer (/planttimer), wenn man eine Plant düngt oder wässert")
    public static boolean plantTimer = true;


    @SubscribeEvent
    public static void onConfigChange(ConfigChangedEvent e) {
        if (e == null || e.getModID().equals(Main.MOD_ID)) {
            String previousTSApiKey = tsAPIKey;
            ConfigManager.sync(Main.MOD_ID, Config.Type.INSTANCE);

            if (!previousTSApiKey.equals(tsAPIKey) && !tsAPIKey.isEmpty()) {
                TSClientQuery.reconnect();
            }
        }
    }
}
