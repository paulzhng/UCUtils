package de.fuzzlemann.ucutils.checks;

import de.fuzzlemann.ucutils.base.abstraction.AbstractionLayer;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author RettichLP
 */
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber
public class CommunicationsChecker {
    private static boolean connected;
    private static boolean activeCommunicationsCheck;

    private static final Pattern PLAYER_TOOK_COMMUNICATIONS_PATTERN = Pattern.compile("^((?:\\[UC])*[a-zA-Z0-9_]+) hat dir deine Kommunikationsgeräte abgenommen\\.$");

    public static boolean hasCommunications = false;
    public static String noCommunicationsMessage = "Du hast keine Kommunikationsgeräte oder sie sind ausgeschaltet.";

    @SubscribeEvent
    public static void onJoin(FMLNetworkEvent.ClientConnectedToServerEvent e) {
        connected = true;
    }

    @SubscribeEvent
    public static void onJoinWorld(EntityJoinWorldEvent e) {
        if (!connected) return;
        connected = false;

        /**
         * If a player enters UnicaCity, 'hasCommunications' is set to false by default. At the same time /call is executed and 'activeCommunicationsCheck' is set to 'true'.
         * Depending on the response, 'hasCommunications' is set to 'true' or 'false'.
         * If 'activeCommunicationsCheck' is set to 'true' both possible responses wouldn't be shown in chat.
         */

        activeCommunicationsCheck = true;
        AbstractionLayer.getPlayer().sendChatMessage("/call");
    }

    @SubscribeEvent
    public static void onChatReceived(ClientChatReceivedEvent e) {

        /**
         * conditions to set 'has Communications' to 'false':
         * - chat: 'Dein Handy ist ausgeschaltet.' (try to use /call)
         * - chat: 'Du hast dein Telefon ausgeschaltet.'
         * - chat: 'Der Akku von deinem Handy ist leer.' (try to turn mobile on)
         * - chat: 'Ihr Akku ist leer.' (try to use /call)
         * - match: PLAYER_TOOK_COMMUNICATIONS_PATTERN
         *
         * conditions to set 'has Communications' to 'true':
         * - chat: 'Fehler: /call [Nummer]' (try to use /call without number)
         * - chat: 'Du hast dein Telefon eingeschaltet.'
         * - chat: 'Der Akku deines Handys ist nun wieder voll.'
         */

        String msg = e.getMessage().getUnformattedText();
        Matcher communicationsTaken = PLAYER_TOOK_COMMUNICATIONS_PATTERN.matcher(msg);
        if (communicationsTaken.find()) {
            hasCommunications = false;
            return;
        }

        if (msg.equals("Du hast dein Telefon ausgeschaltet.") || msg.equals("Dein Handy ist ausgeschaltet.")) {
            hasCommunications = false;
            if (activeCommunicationsCheck) {
                activeCommunicationsCheck = false;
                e.setCanceled(true);
            }
            return;
        }

        if (msg.equals("Der Akku von deinem Handy ist leer.") || msg.equals("Ihr Akku ist leer.")) {
            hasCommunications = false;
            if (activeCommunicationsCheck) {
                activeCommunicationsCheck = false;
                e.setCanceled(true);
            }
            return;
        }

        if (msg.equals("Du hast dein Telefon eingeschaltet.") || msg.equals("Fehler: /call [Nummer]") || msg.equals("Der Akku deines Handys ist nun wieder voll.")) {
            hasCommunications = true;
            if (activeCommunicationsCheck) {
                activeCommunicationsCheck = false;
                e.setCanceled(true);
            }
        }
    }
}